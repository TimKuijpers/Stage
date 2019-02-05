package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.gffparser.Feature;
import com.mycompany.minorigv.sequence.Strand;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * Class voor de visualisatie van verschillende features zoals mRNA/Genen/Telomeren etc die te vinden zijn
 * in GFF(Generic feature format) files.
 *
 * @author Huub en Amber Janssen Groesbeek
 * Date: 20/11/18
 */
public class FeaturePanel extends IGVPanel implements PropertyChangeListener {

    boolean forward;

    public FeaturePanel(Context context, boolean forward) {
        super();
        setContext(context);
        setListeners();
        init(forward);
    }


    /**
     * Alle features binnen een gewenst start-stop positie ophalen en deze weergeven in de GUI.
     *
     * @param g Graphics waarin getekend wordt.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            if (cont == null || cont.getOrganism() == null || cont.getCurrentFeatureList() == null) return;

        } catch (Exception e) {
            return; // Dit is zo'n harde hack dat het pijn doet.
        }
        Feature[] featureFilteredList = null;

        try {
            // Alle features die gekozen zijn door de gebruiker tussen een start en stop positie.
            featureFilteredList = cont.getCurrentFeatureList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CoordinatesFeatures cood = new CoordinatesFeatures(this);
        if (cont != null) {
            cood.seperateFeatures(featureFilteredList, g);
        }
    }


    /**
     * Het tekenen van Features in de forward of reverse panel. Dit hangt af van de input van de gebruiker in de toolbar Features.
     *
     * @param feat Het object van een Feature.
     * @param g    Graphics waarin getekend wordt
     * @param tag  Wat er in de balkjes van de Feature komt te staan (locus tag, naam, etc)
     * @param col  Kleur van de balkjes van een Feature. Genes: Orange, mRNA: Blue
     */
    public Feature drawFeatures(Feature feat, Graphics g, String tag, int overlapCoord, Color col) {
        double start, stop;
        Dimension dim = this.getSize();
        int length = cont.getLength();

        start = feat.getStart() - cont.getStart();
        stop = feat.getStop() - cont.getStart();
        Strand strand = feat.getStrand();

        int infoStart = (int) DrawingTools.calculateLetterPosition((int) dim.getWidth(), length, start);
        int infoStop = (int) DrawingTools.calculateLetterPosition((int) dim.getWidth(), length, stop);
        // Als de strand van het gen + is, dan wordt er op de forward panel getekend.
        if (strand.equals(Strand.POSITIVE) && forward == true) {
            g.setColor(col);
            // Het op de goede positie zetten van de genen, gelet op schaalbaarheid.
            g.fillRect(infoStart, dim.height - (overlapCoord + 20), infoStop - infoStart, 15);
            g.setColor(Color.BLACK);

            // Kleiner lettertype bij een kleiner balkje
            if (g.getFontMetrics().stringWidth(tag) > infoStop - infoStart) {
                // pass
            } else {
                // locus tag in midden van gen visualiseren.
                int centerTag = g.getFontMetrics().stringWidth(tag) / 2;
                g.drawString(tag, ((infoStop + infoStart) / 2) - centerTag, (dim.height - (7 + overlapCoord)));
            }


            // Als de strand van het gen - is, wordt er op de reverse panel getekend.
        } else if (strand.equals(Strand.NEGATIVE) && forward == false) {
            g.setColor(col);

            // Het op de goede positie zetten van de genen.
            g.fillRect(infoStart, overlapCoord, infoStop - infoStart, 15);
            g.setColor(Color.BLACK);


            // Kleiner lettertype bij een kleiner balkje
            if (g.getFontMetrics().stringWidth(tag) > infoStop - infoStart) {
                // pass
            } else {
                // locus tag in midden van gen visualiseren.
                int centerTag = g.getFontMetrics().stringWidth(tag) / 2;
                g.drawString(tag, ((infoStop + infoStart) / 2) - centerTag, overlapCoord + 12);
            }

        }

        return feat;
    }

    /**
     * @param forward boolean voor welke panel: True voor forward panel, False voor reverse panel.
     */
    public void init(boolean forward) {
        this.forward = forward;
        this.setBackground(Color.lightGray);
    }

    @Override
    public void setListeners() {
        cont.addPropertyChangeListener("currentFeatureList", this);
        cont.addPropertyChangeListener("chromosome", this);
        cont.addPropertyChangeListener("choiceUser",this);
    }

}