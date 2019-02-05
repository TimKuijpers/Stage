package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.FastaFileChooser;
import com.mycompany.minorigv.blast.BLAST;
import com.mycompany.minorigv.blast.BlastORF;
import com.mycompany.minorigv.blast.Choices;
import com.mycompany.minorigv.blast.ColorORFs;
import com.mycompany.minorigv.fastqparser.InvalidFileTypeException;
import com.mycompany.minorigv.motif.MotifFrame;
import com.mycompany.minorigv.motif.PositionScoreMatrix;
import com.mycompany.minorigv.sequence.CodonTable;
import com.mycompany.minorigv.sequence.TranslationManager;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Deze class maakt een menubar voor de applicatie die verschillende menus bevat zoals menus voor het laden van files tools etc.
 *
 * @Auteur: Tim Kuijpers
 */
public class IGVMenuBar extends JMenuBar implements PropertyChangeListener {

    private Context cont;
    // Defineer de menu items die zelf sub items zullen bevatten.
    JMenu files, tools, features, reads;

    // Defineer de menu items die zelf niet sub items zullen bevatten.
    JMenuItem openRef, openData, saveORF, findORF, blast, genes, mRNA, exon, region, CDS, featureList, blast_reads, load_reads, showLegendButton,showTabelButton ;


    JMenu motifSearchMenu;
    JMenuItem matrixFrame;

    MotifFrame x;

    // Een lijst die de features bevat die de gebruiker op dat moment wil zien.
    ArrayList<String> featureArray = new ArrayList<String>();

    ColorORFs colorORFs;

    private ButtonGroup group, groupBlast;
    private JRadioButton buttonAll, buttonBetween, buttonIdentity, buttonBitScore, buttonScore, buttonEvalue, buttonNCBI, buttonDiamond;
    private JFormattedTextField textField;

    public IGVMenuBar(Context context) {
        super();
        setContext(context);
        init();
    }

    /**
     * Initializeer de verschillende menus.
     */
    public void init() {
        this.setPreferredSize(new Dimension(200, 25));
        this.setMinimumSize(new Dimension(100, 25));
        menus();
        featureMenu();
        condonTableMenu();
        motifSearchMenu();
        readMenu();

        setEnabledButtonsSequence(false);
        setEnabledButtonsGFF(false);

    }

    /**
     * Het maken van de eerste 2 menus "File" en "Tools"
     */
    public void menus() {
        //Eerst menu item Files
        files = new JMenu("Files");

        //Sub items voor Files
        openRef = new JMenuItem("Load reference");
        openData = new JMenuItem("Load GFF");

        //Action listeners voor de sub items van Files
        openRef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReferenceAction();
            }
        });
        openData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDataAction();
            }
        });

        //Voeg de subitems toe aan  "File"
        files.add(openRef);
        files.add(openData);

        //Voeg Files toe aan de menubar.
        add(files);

        //Tweede menu item "tools"
        tools = new JMenu("ORF");

	    //Sub items voor menu item 2 "tools"
		findORF = new JMenuItem("Find ORFs");
		saveORF = new JMenuItem("Save ORFs");
		blast = new JMenuItem("BLAST");
		showLegendButton = new JMenuItem("Show legend");
        showLegendButton.setEnabled(false);
        showTabelButton = new JMenuItem("Show Table");
        showTabelButton.setEnabled(false);

		File f = new File(cont.getPath(Paths.HOME_DIRECTORY));
		if(f.exists() && f.isDirectory()){
			blast.setEnabled(true);
		}else{
			blast.setEnabled(false);
		}

        //Action listeners voor de sub item van tools
        findORF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    findOrfAction();

                } catch (NumberFormatException el) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "can not find orf");
                }
            }

        });
        saveORF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrfAction();
            }
        });
        blast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blastAction();
            }
        });

        showLegendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLegendAction();
            }
        });

        showTabelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTableAction();
            }
        });

        //Voeg de sub items toe aan "tools"
        tools.add(findORF);
        tools.add(saveORF);
        tools.add(blast);
        tools.add(showLegendButton);
        tools.add(showTabelButton);
        //Voeg tools toe aan de menu bar.
        add(tools);
    }

    private void setEnabledButtonsSequence(boolean b) {

        findORF.setEnabled(b);
        saveORF.setEnabled(b);
        blast.setEnabled(b);
        showLegendButton.setEnabled(b);
        showTabelButton.setEnabled(b);
        matrixFrame.setEnabled(b);
        blast_reads.setEnabled(b);

    }

    private void setEnabledButtonsGFF(boolean b) {

        featureList.setEnabled(b);
        genes.setEnabled(b);
        mRNA.setEnabled(b);
        exon.setEnabled(b);
        region.setEnabled(b);
        CDS.setEnabled(b);

    }

    private void showTableAction() {
        BlastTable blastTable = new BlastTable(cont);
    }

    /**
     * Het aan maken van het 3de menu  "features".
     */
    public void featureMenu() {
        // Aanmaken van een feature menu voor op de menu bar
        features = new JMenu("Features");

        featureList = new JMenuItem("Show features");// Checkboxes die de verschillende features bevatten die de gebruiker kan kiezen.


        // Checkboxes die de verschillende features bevatten die de gebruiker kan kiezen.
        genes = new JCheckBoxMenuItem("Genes");
        mRNA = new JCheckBoxMenuItem("mRNA");
        exon = new JCheckBoxMenuItem("Exon");
        region = new JCheckBoxMenuItem("Region");
        CDS = new JCheckBoxMenuItem("CDS");

        featureList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                featureListAction();
            }
        });

        // Voeg action listeners toe aan de check boxes.
        genes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genesAction();
            }
        });
        mRNA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mrnaAction();
            }
        });
        region.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regionAction();
            }
        });
        exon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exonAction();
            }
        });
        CDS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cdsAction();
            }
        });

        // Voeg de checkboxes toe aan features menu
        features.add(featureList);
        features.add(genes);
        features.add(mRNA);
        features.add(exon);
        features.add(region);
        features.add(CDS);


        // Aanmaken van booleans. Een boolean is true als een checkbock is aan geselecteerd en false als dit niet het geval is
        boolean m = mRNA.isSelected();
        boolean e = exon.isSelected();
        boolean r = region.isSelected();
        boolean c = CDS.isSelected();

        // Voeg het features menu aan de menu bar
        add(features);

    }

    /**
     * Het aan maken van het 3de menu  "features".
     */
    public void readMenu() {
        // Aanmaken van een feature reads voor op de menu bar
        reads = new JMenu("Reads");

        //Sub items voor Files
        load_reads = new JMenuItem("Blast against reference");
        blast_reads = new JMenuItem("Read coverage file");


        load_reads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReadsAction();
            }
        });

        // Voeg action listeners toe aan de check boxes.
        blast_reads.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blastReadsAction();
            }
        });

        // Voeg de checkboxes toe aan features menu
        reads.add(load_reads);
        reads.add(blast_reads);

        // Voeg het features menu aan de menu bar
        add(reads);

    }

    // Action performed voor files en tools.
    private void loadReferenceAction() {
        try {

            FastaFileChooser fasta = new FastaFileChooser();
            String path = fasta.fastafile();

            cont.addFasta(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDataAction() {
        try {
            FastaFileChooser fasta = new FastaFileChooser();
            String path = fasta.fastafile();

            cont.addGFF(path);

		}catch (Exception e){
        }
    }

    private void openReadsAction() {
        try{
            FastaFileChooser fasta = new FastaFileChooser();
            String path = fasta.fastafile();

            cont.readReads(path);

        }catch (Exception e){}
    }

    /**
     * Wanneer er op de button Save ORF wordt geklikt, komt er een pop-up die hier wordt aangemaakt.
     * Deze pop-up bevat twee Radio Buttons (All en Between), een Label, een Textfield en een button (Save).
     * In het Textfield kan de lengte ingevoerd worden die het ORF minimaal moet hebben in nucleotide.
     * Bij de RadioButton kan er gekozen worden of gezocht wordt over het hele sequentie of tussen een bepaalde start en stop.
     */
    private void saveOrfAction() {
        JPanel panel = popupWindow();

        // Save button wordt aangemaakt.
        JButton saveButton = new JButton();
        saveButton.setText("Save");

        // Panel voor de Button wordt aangemaakt.
        JPanel panelForButton = new JPanel();
        panelForButton.add(saveButton);

        // Er wordt een Padding ingesteld.
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Er wordt een frame aangemaakt.
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.PAGE_AXIS));
        f.getContentPane().add(panel);
        f.getContentPane().add(panelForButton);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ActionListener voor de Save Button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveButtonAction();
                    f.dispose();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    /**
     * Wanneer er op de Save Button in het panel wordt geklikt, worden de gegevens opgehaald die op dat moment zijn ingevoerd.
     * En wordt de functie saveORFs aangeroepen, om de ORFs in een file op te slaan.
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private void saveButtonAction() throws FileNotFoundException, UnsupportedEncodingException {
        // haalt de ingevoerde lengte op van het ORF.
        int lengthORFUser = 0;
        try {
            lengthORFUser = Integer.parseInt(textField.getValue().toString());
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
            return;
        }
        // Set de ORFs
        cont.setCurORFListALL(lengthORFUser);
        // Kijkt welke Radio Button is aangeklikt.
        Boolean m = buttonAll.isSelected();
        if (m == true) {
            cont.saveORFs(cont.getCurORFListALL(), "saveORF");
        } else {
            cont.saveORFs(cont.getCurORFListBetween(), "saveORF");
        }
    }

    /**
     * Wanneer er op de button Find ORF gedrukt wordt, komt er een pop-up waarin de lengte van het ORF ingevuld kan worden (nt).
     * Vervolgens worden de ORFs gezocht en gevisualiseerd.
     */
    private void findOrfAction() throws NumberFormatException {
        int lenghtORF = Integer.parseInt(JOptionPane.showInputDialog("Length ORF (nt)"));
        cont.setCurORFListALL(lenghtORF);
    }

    /**
     * Het maken van een pop-up window zodra BLAST wordt geselecteerd. Hierin heeft de gebruiker de optie om alle ORFs van het chromosoom/contig
     * te blasten of tussen bepaalde posities de ORFs te blasten. Daarnaast kan de gebruiker aangeven op welke eigenschappen de geblaste ORFs
     * worden gekleurd (Identity, E-value, Bit score en Score).
     */
    private void blastAction() {
        JPanel panel = popupWindow();

        // Save button wordt aangemaakt.
        JButton blastButton = new JButton();
        blastButton.setText("BLAST");

        // Panel voor de Button wordt aangemaakt.
        JPanel panelForButton = new JPanel();
        panelForButton.add(blastButton);

        // Er wordt een Padding ingesteld.
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelForColor = new JPanel();

        JPanel panelForBlast = new JPanel();

        // Opties voor de kleuring van de geblaste ORFs.
        group = new ButtonGroup();
        buttonIdentity = new JRadioButton("Identity");
        buttonIdentity.setSelected(true);
        buttonBitScore = new JRadioButton("Bit score");
        buttonScore = new JRadioButton("Score");
        buttonEvalue = new JRadioButton("E-value");

        group.add(buttonIdentity);
        group.add(buttonBitScore);
        group.add(buttonScore);
        group.add(buttonEvalue);

        panelForColor.add(buttonIdentity);
        panelForColor.add(buttonBitScore);
        panelForColor.add(buttonScore);
        panelForColor.add(buttonEvalue);

        groupBlast = new ButtonGroup();
        buttonNCBI = new JRadioButton("NCBI BLAST");
        buttonNCBI.setSelected(true);
        buttonDiamond = new JRadioButton("Diamond BLAST");

        groupBlast.add(buttonNCBI);
        groupBlast.add(buttonDiamond);

        panelForBlast.add(buttonNCBI);
        panelForBlast.add(buttonDiamond);

        panelForColor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Color"));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "ORF Settings"));
        panelForBlast.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "BLAST"));

        // Er wordt een frame aangemaakt.
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.PAGE_AXIS));
        f.getContentPane().add(panelForBlast);
        f.getContentPane().add(panel);
        f.getContentPane().add(panelForColor);
        f.getContentPane().add(panelForButton);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // ActionListener voor de Save Button
        blastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    blastButtonAction();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (JAXBException e1) {
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
                f.dispose();
            }
        });

    }

	/**
	 * Kijk of er op de All of Between knop is geklikt. En roept de CallBlastORF vervolgens aan.
	 * @throws IOException
	 * @throws JAXBException
	 */
	private void blastButtonAction() throws IOException, JAXBException, ParserConfigurationException, SAXException {


		BlastORF blastORF = new BlastORF(cont);
        // haalt de ingevoerde lengte op van het ORF.
        int lengthORFUser = Integer.parseInt(textField.getValue().toString());
        // Zoeken van de ORFs
        cont.setCurORFListALL(lengthORFUser);

		//buttonIdentity, buttonBitScore, buttonScore, buttonEvalue;
        if(buttonIdentity.isSelected()){
            ColorORFs.setColorSetting(Choices.IDENTITY);
        }else if(buttonBitScore.isSelected()){
            ColorORFs.setColorSetting(Choices.BITSCORE);
        }else if(buttonScore.isSelected()){
            ColorORFs.setColorSetting(Choices.SCORE);
        }else if(buttonEvalue.isSelected()){
            ColorORFs.setColorSetting(Choices.EVALUE);
        }


		BlastORF blastOrf = new BlastORF(cont);
        // Als de optie All is geselecteerd.
        if(buttonAll.isSelected()){
			String partOutputName = "_A_" + lengthORFUser+ ".xml";
			if(buttonNCBI.isSelected()){
                String blastXMLFile = blastORF.callBlastNCBI(cont.getCurORFListALL(), partOutputName);
                blastOrf.parseBlastResults(BLAST.parseXML(blastXMLFile));
            }else{
                String blastXMLFile = blastORF.callBlastDiamond(cont.getCurORFListALL(), partOutputName);
                blastOrf.parseBlastResults(BLAST.parseXML(blastXMLFile));
            }
		}else{
			String partOutputName = "_B_" + lengthORFUser + "_" + cont.getStart() + "-" + cont.getStop()+ ".xml";
			if(buttonNCBI.isSelected()){
                String blastXMLFile = blastORF.callBlastNCBI(cont.getCurORFListBetween(), partOutputName);
                blastOrf.parseBlastResults(BLAST.parseXML(blastXMLFile));
            }else{
                String blastXMLFile = blastORF.callBlastDiamond(cont.getCurORFListBetween(), partOutputName);
                blastOrf.parseBlastResults(BLAST.parseXML(blastXMLFile));
            }

		}

        blastIsParsedAction();

    }

    /**
     * Als er Blast resultaten zijn worden de legenda en tabel opties in het menu enabled.
     */
    private void blastIsParsedAction() {
	    this.showTabelButton.setEnabled(true);
	    this.showLegendButton.setEnabled(true);
    }

    /**
     * Pop-up window zodra er op de show legend optie wordt geklikt.
     */
    private void showLegendAction(){
	    JFrame window = new JFrame();
	    window.add(new ColorORFs(cont));
	    window.pack();
	    window.setVisible(true);
    }

    /**
     * Maken van de pop-up window voor de BLAST en save ORF.
     * @return
     */
    private JPanel popupWindow(){
        // Radio Button wordt aangemaakt.
        buttonAll = new JRadioButton("All", true);
        buttonBetween = new JRadioButton("Between");
        ButtonGroup groupRadioButton = new ButtonGroup();
        groupRadioButton.add(buttonAll);
        groupRadioButton.add(buttonBetween);

        // Label wordt aangemaakt.
        final JLabel labelLengthORFUser = new JLabel("Length ORF (nt): ", JLabel.LEFT);

        // Er kunnen geen letters ingevoerd worden, wanneer dit wel gebeurd wordt het vorige cijfer gebruikt.
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(true);
        textField = new JFormattedTextField(formatter);
        textField.setValue(100); // Strandaard lengte van het ORF.

        // Panel voor Radio Button, Label en Textfield wordt aangemaakt
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(buttonAll);
        panel.add(buttonBetween);
        panel.add(labelLengthORFUser);
        panel.add(textField);
        return panel;
    }

    private void featureListAction() {
        PopUpFrame popup = new PopUpFrame(cont);
    }

    /**
     * Action performed voor het choose Feature menu
     * Als een checkbox geslecteerd word zal de bijhorende feature worden toegevoegd aan de featureArray.
     */
    private void genesAction() {
        Boolean m = genes.isSelected();
        if (m == true) {
            featureArray.add("Gene");
        } else {
            featureArray.remove("Gene");
        }
        tellContext();
    }

    private void mrnaAction() {
        Boolean m = mRNA.isSelected();
        if (m == true) {
            featureArray.add("mRNA");
        } else {
            featureArray.remove("mRNA");
        }

        tellContext();
    }

    private void regionAction() {
        Boolean m = region.isSelected();
        if (m == true) {
            featureArray.add("Region");
        } else {
            featureArray.remove("Region");
        }
        tellContext();
    }

    private void exonAction() {
        Boolean m = exon.isSelected();
        if (m == true) {
            featureArray.add("Exon");
        } else {
            featureArray.remove("Exon");
        }

        tellContext();
    }

    private void cdsAction() {
        Boolean m = CDS.isSelected();
        if (m == true) {
            featureArray.add("CDS");
        } else {
            featureArray.remove("CDS");
        }

        tellContext();
    }

    private void loadReadsAction() {

	    JOptionPane.showMessageDialog(null,"kies een fastq");
	    FastaFileChooser ffc = new FastaFileChooser();
	    String fastqFile = ffc.fastafile();

        JOptionPane.showMessageDialog(null,"kies een reference genome");
        ffc = new FastaFileChooser();
        String genomeFile = ffc.fastafile();


        try {
            cont.blastAgainstReference(fastqFile,genomeFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFileTypeException e) {
            e.printStackTrace();
        }


    }

    private void blastReadsAction() {



        JOptionPane.showMessageDialog(null,"kies de geblaste fastq csv");
        FastaFileChooser ffc = new FastaFileChooser();
        String path = ffc.fastafile();

        try {
            cont.parseBlastedReads(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return Arraylist<Strings> bevat geslecteerde features.
     */
    public ArrayList<String> getFeatureArray() {
        return featureArray;
    }

    public void tellContext() {
        cont.setChoiceUser(this.featureArray);
    }

    public void setContext(Context cont) {
        this.cont = cont;
        cont.addPropertyChangeListener("fasta",this);
        cont.addPropertyChangeListener("gff",this);
    }

    /**
     * maakt en voegt de translatie tabel menu aan de menubalk toe
     * gebruikt getCodonTableMenuItem om de items te genereren
     */
    public void condonTableMenu() {
        JMenu transTableMenu = new JMenu("Translation Table");
        ButtonGroup group = new ButtonGroup();
        for (CodonTable codonTable : TranslationManager.getInstance().getAllCodonTables()) {
            JMenuItem item = getCodonTableMenuItem(codonTable);
            transTableMenu.add(item);
            group.add(item);
        }
        add(transTableMenu);
    }

    /**
     * maakt de menu items voor codonTableMenu
     *
     * @param codonTable
     * @return
     */
    private JRadioButtonMenuItem getCodonTableMenuItem(CodonTable codonTable) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem();
        String fullName = codonTable.getNames()[0];
        String shortName = fullName;
        if (fullName.length() > 40) {
            shortName = fullName.substring(0, 37) + "...";
            item.setToolTipText(fullName);
        }
        item.setText(shortName);
        final Integer curKey = codonTable.getKey();
        item.setSelected(item.getText().contentEquals("Standard"));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cont.setCurrentCodonTable(curKey);


            }
        });
        return item;
    }

    /**
     * maakt de motif search menu knop. maakt ook de nieuwe frame en set de context voor de frame
     */
    public void motifSearchMenu() {
        motifSearchMenu = new JMenu("motif search");
        matrixFrame = new JMenuItem("input sequences");
        matrixFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(x != null){
                    x.kys();
                }

                createMatrixFrame();
            }
        });
        motifSearchMenu.add(matrixFrame);
        add(motifSearchMenu);

    }

    public void createMatrixFrame(){
        //matrixFrame.setEnabled(false);
        ArrayList<PositionScoreMatrix> startUpMatrixList = cont.getMatrixes();
        x = new MotifFrame(startUpMatrixList, this );
        x.setContext(cont);
    }

    public void enableMotifButton(Boolean b){
        matrixFrame.setEnabled(b);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if(evt.getPropertyName().equals("fasta")) {
            setEnabledButtonsSequence(true);
        }
        else if(evt.getPropertyName().equals("gff")){
            setEnabledButtonsGFF(true);
        }

    }

}