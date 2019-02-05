package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.blast.ColorORFs;
import com.mycompany.minorigv.gffparser.BlastedORF;
import com.mycompany.minorigv.gffparser.ORF;
import com.mycompany.minorigv.sequence.CodonTable;
import com.mycompany.minorigv.sequence.Strand;
import com.mycompany.minorigv.sequence.TranslationManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;


/**
 * JPanel voor de visualisatie van de verschillende reading frames passend bij het referentie genoom.
 * @author Huub en Anne
 * Date: 20/11/18
 */
public class CodonPanel extends IGVPanel implements PropertyChangeListener{


	Strand strand;
	Dimension dim;
	ColorORFs colorPicker;

	private final int ZOOM_SIZE_1 = 20; //Hoeveelheid pixels waarna maar één kleur blauw gebruikt wordt. Letters weergeven.
	private final int ZOOM_SIZE_2 = 14; //Hoeveelheid pixels waarna de aminozuur letters verdwijnen. (ORF (geel) + start (groen) + stop (rood))
	private final int ZOOM_SIZE_3 = 3;  //Hoeveelheid pixels waarna alleen de ORFs (geel) worden weergegeven. Geen letters weergeven. Geen letters weergegeven.

    public CodonPanel(Context cont,Strand strand){
        super();
        this.setContext(cont);
        this.setListeners();
        this.init(strand);
        colorPicker = new ColorORFs(cont);

    }

    /**
     * Initaliseerd
     * @param strand
     */
	public void init(Strand strand) {
		this.strand = strand;
		setPreferredSize(new Dimension(500,75));
		setMaximumSize(new Dimension(2000,40));
		setMinimumSize(new Dimension(100,30));

		MyMouseListener listener = new MyMouseListener();
		addMouseListener(listener);

	}

    /**
     * Override de paintComponent uit JPanel.
     * @param g     Graphics context
     */
	@Override
	public void paintComponent(Graphics g) {
        dim = this.getSize();
		super.paintComponent(g);
		// Doet niks wanneer er geen sequentie aanwezig is.
		String seq;
		try {
			seq = cont.getSequentie();
			if (seq == null){
			    return;
            }
		}
		catch (Exception e){
			return;
		}

		// Breedte van een aminozuurblokje.
        double letterWidth = DrawingTools.calculateLetterWidth((int) this.getSize().getWidth(), cont.getLength())*3;
		//TODO dit is voor in de toekomst
		// Beïnvloed de tekenmethode van de positive (forward) strand.
        if(strand == Strand.POSITIVE) {
            if(letterWidth > ZOOM_SIZE_1){
                drawPositive(g,seq, letterWidth);
            }else if(letterWidth > ZOOM_SIZE_2){
                drawPositive(g,seq, letterWidth);
            }else if(letterWidth > ZOOM_SIZE_3){
                drawPositive(g,seq, letterWidth);
            }else{
                drawZoomedOut(g, seq);
            }
		}
		// Beïnvloed de tekenmethode van de negative (reverse) strand.
        //TODO dit is voor in de toekomst
		else {
            if(letterWidth > ZOOM_SIZE_1){
                drawNegative(g,seq, letterWidth);
            }else if(letterWidth > ZOOM_SIZE_2){
                drawNegative(g,seq, letterWidth);
            }else if(letterWidth > ZOOM_SIZE_3){
                drawNegative(g,seq, letterWidth);
            }else{
                drawZoomedOut(g, seq);
            }
		}

	}

    /**
     *Tekent het positive CodonPanel.
     * @param g     Graphics context
     * @param seq   Sequentie van het chromosoom/contig.
     * @param letterWidth
     */
	private void drawPositive(Graphics g, String seq, double letterWidth) {
		int start = cont.getStart(); 					//start van het beeld.
		int stop = cont.getStop(); 						//stop van het beeld.
		int length = cont.getLength();					//lengte subsequentie.
		int panelWidth = (int) getSize().getWidth(); 	//breedte paneel

        CodonTable curTable = cont.getCurrentCodonTable();

		for (int f = 0; f < 3; f++) { // f teller voor de frames.
			int frameStart = start+f;
            int frame = ORF.calcFrame(frameStart, Strand.POSITIVE, cont.getFullLenght());

            ArrayList<ORF> strandORFs = getORFs(Strand.POSITIVE, frame);
			String aaSeq = TranslationManager.getInstance().getAminoAcids(strand,seq.substring(start+f,stop),curTable);
			int xPosLeft; //linkerkant van rechthoekje, positie op de x-as.
            int xPosRight = (int) DrawingTools.calculateLetterPosition( panelWidth, length, f- 0.5); //onthouden oude x_pos.

            int aa = 0; // aa teller
			for (int indexRef = frameStart+1; indexRef < stop-1; indexRef+=3) {
				char letter = aaSeq.charAt(aa);
                int indexSubSeq = indexRef - start;
                xPosLeft = xPosRight; //oude links -> rechts.
                xPosRight = (int) DrawingTools.calculateLetterPosition( panelWidth, length, indexSubSeq+1.5); //nieuwe xPos bepalen.
                int xPos = (int) DrawingTools.calculateLetterPosition(panelWidth,length, indexSubSeq); //positie van de letter.
                selectColor(strandORFs, g, indexRef, letter, letterWidth);

                int width = xPosRight-xPosLeft; //breedte van het vierkantje |<-->|
                int height = calcHeight(strand,frame);//20+20*(frame);

                selectColor(strandORFs, g, indexRef, letter,letterWidth); //instellen juiste kleur.
                g.fillRect(xPosLeft,height,width,20);
				g.setColor(Color.BLACK);

				if(letterWidth > ZOOM_SIZE_2){
                    DrawingTools.drawCenteredChar(g,letter,xPos,height+5);
                }
				aa++;
			}
		}
	}

    /**
     * Tekent de negative CodonPanel.
     * @param g     Graphics context
     * @param seq   Sequentie van het chromosoom/contig.
     * @param letterWidth
     */
    private void drawNegative(Graphics g, String seq, double letterWidth) {

        int start = cont.getStart(); 					//start van het beeld.
        int stop = cont.getStop(); 						//stop van het beeld.
        int length = cont.getLength();					//lengte subsequentie.
        int panelWidth = (int) getSize().getWidth(); 	//breedte paneel

        CodonTable curTable = cont.getCurrentCodonTable();

        for (int f = 0; f < 3; f++) { //loopen over de drie mogelijke frames.
            int frame = ORF.calcFrame(stop-f, Strand.NEGATIVE, cont.getFullLenght());  //bepalen frame van de subsequentie.

            ArrayList<ORF> strandORFs = getORFs(Strand.NEGATIVE, frame); //ORF's ophalen behorend bij dit frame.
            String aaSeq = TranslationManager.getInstance().getAminoAcids(strand,seq.substring(start,stop-f),curTable); //sequentie behorend bij dit frame.

            int xPosRight; //linkerkant van rechthoekje, positie op de x-as.
            int xPosLeft = (int) DrawingTools.calculateLetterPosition( panelWidth, length, stop-f-start-2+1.5); //onthouden oude x_pos.

            int aa = 0; // aa teller
            for (int indexRef = stop-f; indexRef > start+2; indexRef-=3) { //loopen van rechts naar links op de referentie <-> links naar rechts complement
                char letter = aaSeq.charAt(aa); //character op de positie bijbehorend aminozuur.

                int indexSubSeq = indexRef-start-2; //index op referentie ten opzichte subsequentie
                xPosRight = xPosLeft; //oude links -> rechts.
                xPosLeft = (int) DrawingTools.calculateLetterPosition( panelWidth, length, indexSubSeq-1.5); //nieuwe xPos bepalen.
                int xPos = (int) DrawingTools.calculateLetterPosition(panelWidth,length, indexSubSeq); //positie van de letter.

                int width = xPosRight-xPosLeft; //breedte van het vierkantje |<-->|
                int height = calcHeight(strand,frame);//20+20*(frame);

                selectColor(strandORFs, g, indexRef, letter,letterWidth); //instellen juiste kleur.
                g.fillRect(xPosLeft,height,width,20);

                g.setColor(Color.BLACK);

                if(letterWidth > ZOOM_SIZE_2){ //teken de letters alleen als ze nog leesbaar zijn,
                    DrawingTools.drawCenteredChar(g,aaSeq.charAt(aa),xPos,height+5);
                }
                aa++; // aa teller ophogen.
            }
        }
    }

	public void setListeners(){
        cont.addPropertyChangeListener("range", this);
        cont.addPropertyChangeListener("chromosome",this);
        cont.addPropertyChangeListener("CodonTable", this);
    }

    /**
     * Selecteren van de kleur die meegeven moet worden aan het ORF op de visualisatie.
     * 
     * @param strandORFs  een ArrayList met van 1 frame op 1 strand alle ORFs tussen de start en stop van de gebruiker
     * @param g           graphics object g.
     * @param indexRef    de index gemapt naar de referentie.
     * @param letter      de letter van het aminozuur.
     * @param width
     */
    public void selectColor(ArrayList<ORF> strandORFs, Graphics g, int indexRef, char letter, double width){

        ArrayList<ORF> overlappingBlastedORFs = new ArrayList<>();

        if(strandORFs != null && strandORFs.size() > 0 ){ //als uberhaubt ORF's.
            for(ORF o: strandORFs) {
                int startORF = o.getStart();
                int stopORF = o.getStop();
                if (indexRef <= stopORF && indexRef > startORF) {

                    if(!(o instanceof BlastedORF)) {
                        colorFrames(g, indexRef, letter, "ORF", width, null);
                        return;
                    }
                    overlappingBlastedORFs.add(o);

                }
            }
            // Als er ORFs overlappen wordt de kleur van het kortste ORF gevisualiseerd.
            if (!overlappingBlastedORFs.isEmpty()) {
                ORF temp = overlappingBlastedORFs.get(0);
                for (ORF o : overlappingBlastedORFs) {
                    if (temp.getLengthORF() > o.getLengthORF()) {
                        temp = o;
                    }
                }
                if(temp instanceof BlastedORF){
                    g.setColor(colorPicker.getColor(temp));
                    return;
                }
            }

        }

        colorFrames(g, indexRef, letter, "-", width, null);

    }

    /**
     * @param g Graphics Context Object.
     * @param indexRef Index op de referentie.
     * @param letter de letter van het aminozuur
     * @param sign bevestigings String.
     * @param letterWidth breedte van de letter
     */
	public void colorFrames(Graphics g, int indexRef, char letter, String sign, double letterWidth, Color colBlast){
	    if('M' == letter){
            g.setColor(new Color(0, 153, 0));
		}else if('*' == letter){
            g.setColor(new Color(250, 0, 0));
        }else if(sign.equals("ORF")){
            g.setColor(new Color(255, 255, 0));
        }else if(indexRef%2 > 0 || letterWidth < ZOOM_SIZE_1 ) {  //als te smalle vakjes voor 2 kleuren blauw.
            g.setColor(new Color(127, 191, 226));
        }else{
            g.setColor(new Color(42, 112, 150));
		}
	}

    /**
     * Haalt de ORFs op tussen start en stop
     * Haalt de ORFs op, op één van de strands en op één van de readingframes van de strands
     * @param strand Strand waarop de ORF's moeten zijn gevonden.
     * @param frame het Frame waarin de ORF's moeten liggen.
     * @return een ArrayList van ORF's
     */
	public ArrayList<ORF> getORFs(Strand strand, int frame){
		ArrayList<ORF> listORF = cont.getCurORFListBetween();

        ArrayList<ORF> strandORFs = new ArrayList<>();
        if(listORF != null){
            for(ORF o : listORF){
                if(o.getStrand().equals(strand) && o.getReadingframe() == frame){
                    strandORFs.add(o);
                }
            }
            return strandORFs;
        }else{
            return null;
        }

	}

    /**
     * Haalt de ORFs op tussen start en stop
     * Haalt de ORFs op, op één van de strands en op één van de readingframes van de strands
     * @param strand Strand waarop de ORF's moeten zijn gevonden.
     * @return een ArrayList van ORF's
     */
    public ArrayList<ORF> getORFs(Strand strand){
        ArrayList<ORF> listORF = cont.getCurORFListBetween();

        ArrayList<ORF> strandORFs = new ArrayList<>();
        if(listORF != null){
            for(ORF o : listORF){
                if(o.getStrand().equals(strand)){
                    strandORFs.add(o);
                }
            }
            return strandORFs;
        }else{
            return null;
        }

    }

    /**
     * Functie voor het bepalen van de hoogte waarop getekent moet worden.
     * @param strand Strand die getekent wordt.
     * @param frame het frame wat momenteel wordt getekent.
     * @return de hoogte in pixels waarop getekent moet worden.
     */
    private static int calcHeight(Strand strand, int frame){
	    if(strand == Strand.NEGATIVE){
	        return 10+20*(frame);
        }else if(strand == Strand.POSITIVE){
	        return 10+20*(2-frame);
        }
        else return -1;

    }

    /**
     * Functie voor het tekenen als er ver is uitgezoomt.
     * @param g Graphics context object.
     * @param seq Sequentie als String.
     */
    private void drawZoomedOut(Graphics g, String seq){

        int start = cont.getStart(); 					//start van het beeld.
        int stop = cont.getStop(); 					    //stop van het beeld.
        int length = cont.getLength();					//lengte subsequentie.
        int panelWidth = (int) getSize().getWidth(); 	//breedte paneel

        // Link positie van het grote blauwe blok.
        int xPosLeft = (int) DrawingTools.calculateLetterPosition(panelWidth, length,start-start);
        // Rechter positie van het grote blauw blok.
        int xPosRight = (int) DrawingTools.calculateLetterPosition(panelWidth, length, stop-start);

        g.setColor(new Color(127, 191, 226));
        // Tekent het grote blauwe blok
        g.fillRect(xPosLeft,10,xPosRight-xPosLeft,3*20);


        ArrayList<ORF> listORF = getORFs(strand);

        if(listORF != null){
            for(ORF o : listORF){
                // Linker positie in pixel van het ORF (geel)
                xPosLeft = (int) DrawingTools.calculateLetterPosition(panelWidth, length,o.getStart()-start);
                // Rechter positie in pixel van het ORF (geel).
                xPosRight = (int) DrawingTools.calculateLetterPosition(panelWidth, length, o.getStop()-start);
                int height = calcHeight(strand, o.getReadingframe());
                g.setColor(colorPicker.getColor(o));
                g.fillRect(xPosLeft, height, xPosRight-xPosLeft, 20);
                g.setColor(Color.BLACK);
            }
        }

    }

    /**
     * Checken op welk geblaste ORF de gebruiker klikt: visualiseren van de informatie van dat (geblaste)ORF.
     *
     * @author Anne van Ewijk en Amber Janssen Groesbeek
     */
    class MyMouseListener extends MouseAdapter{

        /**
         * Bepalen waar de gebruiker klikt: welk reading frame en welk ORF.
         * @param e
         */
        public void mouseClicked(MouseEvent e){
            int X = e.getX();
            int Y = e.getY();

            // Ophalen breedte van scherm
            double widthPanel = (double) dim.getWidth();

            // Ophalen aantal nucleotide
            double length = cont.getLength();
            int start = cont.getStart();

            //int positie = (int) ((double)(X - 12) / ((widthPanel-24) / (length-1)));
            int positie = (int) DrawingTools.calculatePixelPosition(X, widthPanel, length, start); //inverse functie Drawingtools.calculateLetterPosition();
            //positie += start;

            ArrayList<ORF> listORF = cont.getCurORFListBetween();
            String bigMessage = ""; // Wanneer er meerdere ORFs zijn (op de geklikte plek) wordt de informatie van deze ORFs onder elkaar geplakt.
            if(listORF != null){
                for(ORF o: listORF){
                    if(o.getStart() <= positie && o.getStop() >= positie && o.getStrand() == strand){
                        if(strand.equals(Strand.POSITIVE)){
                            if(Y >= 10 && Y <= 29 && o.getReadingframe() == 2){         // Reading frame bepalen
                                String message = getInformationORF(o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }else if(Y >= 30 && Y <= 49 && o.getReadingframe() == 1){
                                String message = getInformationORF(o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }else if(Y >= 50 && Y <= 69 && o.getReadingframe() == 0){
                                String message = getInformationORF( o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }
                        }else if(strand.equals(Strand.NEGATIVE)){
                            if(Y >= 10 && Y <= 29 && o.getReadingframe() == 0){         // Reading frame bepalen
                                String message = getInformationORF(o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }else if(Y >= 30 && Y <= 49 && o.getReadingframe() == 1){
                                String message = getInformationORF(o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }else if(Y >= 50 && Y <= 69 && o.getReadingframe() == 2){
                                String message = getInformationORF(o);
                                bigMessage = bigMessage  + message + "\n\n\n";
                                //popUp(message);           // Kan men aanzetten wanneer men per ORF een pop-up wilt.
                            }
                        }
                    }
                }
                if(!bigMessage.equals("")){
                    popUp(bigMessage);                    // Kan men uitzetten wanneer men per ORF een pop-up wilt.
                }

            }

        }

        /**
         * Maken van een popup window
         * @param message   De tekst die gevisualiseerd moet worden in het window.
         */
        public void popUp(String message){

            JTextArea textVeld = new JTextArea(message);
            textVeld.setText(message);
            textVeld.setEditable(false);

            JScrollPane scroller = new JScrollPane(textVeld);

            scroller.setPreferredSize(new Dimension(400,400));
            textVeld.setCaretPosition(0);
            JOptionPane.showMessageDialog(null, scroller, "Hits BLAST ORF(s)", JOptionPane.INFORMATION_MESSAGE);

            System.out.println(textVeld.getText());
        }

        /**
         * Ophalen van de informatie van het geblaste ORF.
         * @param orf   Object ORF die geselecteerd is door de gebruiker.
         * @return      String die gevisualiseerd moet worden.
         */
        public String getInformationORF(ORF orf){

            int startORF = orf.getStart();
            int stopORF = orf.getStop();
            int rfORF = orf.getReadingframe();
            if(!(orf instanceof BlastedORF)){

                String message = "Start ORF: " + startORF + System.lineSeparator() +
                                 "Stop ORF: "  + stopORF  + System.lineSeparator() +
                                 "RF ORF: "    + rfORF    + System.lineSeparator();
                return message;
            }

            BlastedORF blastedOrf = (BlastedORF) orf;


            if(!blastedOrf.hasHit()){
                return "No BLAST-hits.";
            }

            String hitID = blastedOrf.getBestHit().getHitId();
            String hitDef = blastedOrf.getBestHit().getHitDef();
            String hitAcc = blastedOrf.getBestHit().getHitAccession();
            String bitScore = blastedOrf.getBestHsp().getHspBitScore();
            String score = blastedOrf.getBestHsp().getHspScore();
            String evalue = blastedOrf.getBestHsp().getHspEvalue();
            String identity = blastedOrf.getBestHsp().getHspIdentity();


            String message =
                    "Hit id:\t" + hitID + "\n" +
                    "Hit def:\t" + hitDef + "\n" +
                    "Hit acc:\t" + hitAcc + "\n" +
                    "Bit score:\t" + bitScore + "\n" +
                    "Score:\t" + score + "\n" +
                    "E-value:\t" + evalue + "\n" +
                    "Identity:\t" + identity + "\n"+
                    "Start ORF:\t" + startORF + "\n"+
                    "Stop ORF:\t" + stopORF + "\n"+
                    "RF ORF:\t" + rfORF + "\n";

            return message;
        }

    }

}