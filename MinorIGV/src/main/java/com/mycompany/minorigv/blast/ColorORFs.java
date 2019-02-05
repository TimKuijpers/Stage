package com.mycompany.minorigv.blast;

import com.mycompany.minorigv.gffparser.BlastedORF;
import com.mycompany.minorigv.gffparser.Chromosome;
import com.mycompany.minorigv.gffparser.ORF;
import com.mycompany.minorigv.gui.Context;
import com.mycompany.minorigv.gui.Paths;
import com.mycompany.minorigv.sequence.Strand;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/**
 * Bepalen van de kleur waarmee het geblaste ORF gevisualiseerd wordt en het genereert de legenda.
 *
 * @author Anne van Ewijk en Amber Janssen Groesbeek
 */
public class ColorORFs extends JPanel {
    Context cont;

    public static Double[] eValueScores = new Double[]{             // Range waarin de E-value wordt gekleurd
            1.0E-4,
            1.0E-10,
            1.0E-20,
            1.0E-30,
            1.0E-50,
            1.0E-80,
            1.0E-100,
            1.0E-150,
            1.0E-200,
            null
    };

    public static Integer[] identityScores = new Integer[]{         // Range waarin de identity score wordt gekleurd
            0,
            50,
            100,
            200,
            500,
            1000,
            2000,
            4000,
            8000,
            null
    };

    public static Integer[] bitScores = new Integer[]{              // Range waarin de bit score wordt gekleurd
            0,
            200,
            500,
            1000,
            2000,
            3000,
            4000,
            5000,
            6000,
            null
    };

    public static Integer[] scoreScores = new Integer[]{            // Range waarin de score wordt gekleurd
            0,
            100,
            200,
            400,
            800,
            1000,
            2000,
            4000,
            8000,
            null
    };

    public static Color[] colors = new Color[]{                     // De kleuren waarmee de geblaste ORFs worden gekleurd.
            new Color(0, 40, 0),
            new Color(0, 66, 0),
            new Color(0, 92, 0),
            new Color(0, 118, 0),
            new Color(0, 144, 0),
            new Color(0, 170, 0),
            new Color(0, 196, 0),
            new Color(0, 222, 0),
            new Color(0, 255, 0),
            new Color(255, 255, 0)
    };

    private static Choices colorSetting;

    /**
     * Constructor
     */
    public ColorORFs() {

    }

    /**
     * Constructor
     * @param context
     */
    public ColorORFs(Context context) {
        super();
        cont = context;
        init();
    }

    /**
     * Het ophalen van de kleur van een geblaste ORF.
     * @return      Kleur code.
     */
    public Choices getColorSetting() {
        return colorSetting;
    }

    /**
     * Het setten van de kleur van een geblast ORF
     * @param colorSetting  De keuze van de gebruiker waarop er gekleurd moet worden.
     */
    public static void setColorSetting(Choices colorSetting) {
        ColorORFs.colorSetting = colorSetting;
    }

    /**
     * Op basis van de keuze van de gebruiker de kleuren bepalen van de geblaste ORFs.
     * @param orf   Object ORF.
     * @return
     */
    public Color getColor(ORF orf) {

        if (!(orf instanceof BlastedORF)) {
            return colors[colors.length - 1];
        }

        BlastedORF blastedOrf = (BlastedORF) orf;

        switch (colorSetting) {
            case IDENTITY:
                return pickColorIdentity(blastedOrf);
            case EVALUE:
                return pickColorEvalue(blastedOrf);
            case SCORE:
                return pickColorScore(blastedOrf);
            case BITSCORE:
                return pickColorBitScore(blastedOrf);
            default:
                return colors[colors.length - 1];

        }
    }


    /**
     * De kleuren van de geblaste ORFs bepalen wanneer de gebruiker heeft gekozen voor kleuren op Identity.
     *
     * @param orf      ORF object met blast resultaten.
     * @return         Kleur voor een geblast ORF.
     */
    private Color pickColorIdentity(BlastedORF orf) {

        if (!orf.hasHit()) {
            return colors[colors.length - 1]; //geen hit dus geel
        }

        int identityScore = Integer.parseInt(orf.getBestHsp().getHspIdentity());

        if (identityScore >= identityScores[0] && identityScore < identityScores[1]) {
            return colors[0];
        } else if (identityScore >= identityScores[1] && identityScore < identityScores[2]) {
            return colors[1];
        } else if (identityScore >= identityScores[2] && identityScore < identityScores[3]) {
            return colors[2];
        } else if (identityScore >= identityScores[3] && identityScore < identityScores[4]) {
            return colors[3];
        } else if (identityScore >= identityScores[4] && identityScore < identityScores[5]) {
            return colors[4];
        } else if (identityScore >= identityScores[5] && identityScore < identityScores[6]) {
            return colors[5];
        } else if (identityScore >= identityScores[6] && identityScore < identityScores[7]) {
            return colors[6];
        } else if (identityScore >= identityScores[7] && identityScore < identityScores[8]) {
            return colors[7];
        } else if (identityScore >= identityScores[8]) {
            return colors[8];
        }

        return colors[colors.length - 1];


    }

    /**
     * De kleuren van de geblaste ORFs bepalen wanneer de gebruiker heeft gekozen voor kleuren op E-Value.
     *
     * @param orf      ORF object met blast resultaten.
     * @return         Kleur voor een geblast ORF.
     */
    public Color pickColorEvalue(BlastedORF orf) {
        if (!orf.hasHit()) {
            return colors[colors.length - 1]; //geen hit dus geel
        }

        Double evalue = Double.parseDouble(orf.getBestHsp().getHspEvalue());
        //Checken waartussen de e-value valt
        if (evalue <= eValueScores[0] && evalue > eValueScores[1]) {
            return colors[0];
        } else if (evalue <= eValueScores[1] && evalue > eValueScores[2]) {
            return colors[1];
        } else if (evalue <= eValueScores[2] && evalue > eValueScores[3]) {
            return colors[2];
        } else if (evalue <= eValueScores[3] && evalue > eValueScores[4]) {
            return colors[3];
        } else if (evalue <= eValueScores[4] && evalue > eValueScores[5]) {
            return colors[4];
        } else if (evalue <= eValueScores[5] && evalue > eValueScores[6]) {
            return colors[5];
        } else if (evalue <= eValueScores[6] && evalue > eValueScores[7]) {
            return colors[6];
        } else if (evalue <= eValueScores[7] && evalue > eValueScores[8]) {
            return colors[7];
        } else if (evalue <= eValueScores[8]) {
            return colors[8];
        }
        return colors[colors.length - 1];

    }


    /**
     * De kleuren van de geblaste ORFs bepalen wanneer de gebruiker heeft gekozen voor kleuren op Score.
     *
     * @param orf      ORF object met blast resultaten.
     * @return         Kleur voor een geblast ORF.
     */
    public Color pickColorScore(BlastedORF orf) {


        if (!orf.hasHit()) {
            return colors[colors.length - 1]; //geen hit dus geel
        }

        int score = Integer.parseInt(orf.getBestHsp().getHspScore());

        //Checken waartussen de score valt
        if (score >= scoreScores[0] && score < scoreScores[1]) {
            return colors[0];
        } else if (score >= scoreScores[1] && score < scoreScores[2]) {
            return colors[1];
        } else if (score >= scoreScores[2] && score < scoreScores[3]) {
            return colors[2];
        } else if (score >= scoreScores[3] && score < scoreScores[4]) {
            return colors[3];
        } else if (score >= scoreScores[4] && score < scoreScores[5]) {
            return colors[4];
        } else if (score >= scoreScores[5] && score < scoreScores[6]) {
            return colors[5];
        } else if (score >= scoreScores[6] && score < scoreScores[7]) {
            return colors[6];
        } else if (score >= scoreScores[7] && score < scoreScores[8]) {
            return colors[7];
        } else if (score >= scoreScores[8]) {
            return colors[8];
        }
        return colors[colors.length - 1];

    }


    /**
     * De kleuren van de geblaste ORFs bepalen wanneer de gebruiker heeft gekozen voor kleuren op bit score.
     *
     * @param orf      ORF object met blast resultaten.
     * @return         Kleur voor een geblast ORF.
     */
    public Color pickColorBitScore(BlastedORF orf) {

        if (!orf.hasHit()) {
            return colors[colors.length - 1]; //geen hit dus geel
        }

        Double bitScore = Double.parseDouble(orf.getBestHsp().getHspBitScore());

        //Checken waartussen de bitScore valt
        if (bitScore >= bitScores[0] && bitScore < bitScores[1]) {
            return colors[0];
        } else if (bitScore >= bitScores[1] && bitScore < bitScores[2]) {
            return colors[1];
        } else if (bitScore >= bitScores[2] && bitScore < bitScores[3]) {
            return colors[2];
        } else if (bitScore >= bitScores[3] && bitScore < bitScores[4]) {
            return colors[3];
        } else if (bitScore >= bitScores[4] && bitScore < bitScores[5]) {
            return colors[4];
        } else if (bitScore >= bitScores[5] && bitScore < bitScores[6]) {
            return colors[5];
        } else if (bitScore >= bitScores[6] && bitScore < bitScores[7]) {
            return colors[6];
        } else if (bitScore >= bitScores[7] && bitScore < bitScores[8]) {
            return colors[7];
        } else if (bitScore >= bitScores[8]) {
            return colors[8];
        }


        return colors[colors.length - 1];
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Object[] rangeScores;
        String typeColor;

        // Bepalen van de setting (door gebruiker gekozen).
        switch (colorSetting) {
            case IDENTITY:
                rangeScores = identityScores;
                typeColor = "identity";
                break;
            case EVALUE:
                rangeScores = eValueScores;
                typeColor = "e-value";
                break;
            case SCORE:
                rangeScores = scoreScores;
                typeColor = "score";
                break;
            case BITSCORE:
                rangeScores = bitScores;
                typeColor = "bit score";
                break;
            default:
                rangeScores = null;
                typeColor = null;

        }

        // Maken van de legenda op basis van de settings van de gebruiker.
        if (!rangeScores.equals(null)) {

            g.drawString(typeColor, this.getWidth() / 2, 10);

            g.setColor(colors[0]);
            g.fillRect(10, 20, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[0] + "-" + rangeScores[1], 50, 35);

            g.setColor(colors[1]);
            g.fillRect(10, 45, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[1] + "-" + rangeScores[2], 50, 60);

            g.setColor(colors[2]);
            g.fillRect(10, 70, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[2] + "-" + rangeScores[3], 50, 85);

            g.setColor(colors[3]);
            g.fillRect(10, 95, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[3] + "-" + rangeScores[4], 50, 110);

            g.setColor(colors[4]);
            g.fillRect(10, 120, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[4] + "-" + rangeScores[5], 50, 135);

            g.setColor(colors[5]);
            g.fillRect(10, 145, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[5] + "-" + rangeScores[6], 50, 160);

            g.setColor(colors[6]);
            g.fillRect(10, 170, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[6] + "-" + rangeScores[7], 50, 185);

            g.setColor(colors[7]);
            g.fillRect(10, 195, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(rangeScores[7] + "-" + rangeScores[8], 50, 210);

            g.setColor(colors[8]);
            g.fillRect(10, 220, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString(">" + rangeScores[8], 50, 235);

            g.setColor(colors[9]);
            g.fillRect(10, 245, 25, 25);
            g.setColor(Color.BLACK);
            g.drawString("Geen hits", 50, 260);

        }

    }

    /**
     * initiatie van het paneel waarin de sequenties worden getekent
     */
    public void init() {
        setPreferredSize(new Dimension(200, 300));
        setMaximumSize(new Dimension(200, 300));
        setMinimumSize(new Dimension(200, 300));
        setBackground(Color.WHITE);
    }


}
