package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.gffparser.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * PopUpFrame construeerd een JavaFrame als pop up wanneer er om de get featueres button word geklikt.
 * Het frame bevat alle features die de gebruiker will zien in een tabel
 *
 * @Auteur: Tim Kuijpers
 */
public class PopUpFrame extends JFrame {
    Context cont;

    /**
     * PopUpFrame construeerd een JavaFrame als pop up wanneer er om de get featueres button word geklikt.
     * Het frame bevat alle features die de gebruiker will zien in een tabel
     *
     * @param cont
     */
    public PopUpFrame(Context cont) {

        super();
        try{
        this.cont = cont;
        JFrame frame = new JFrame("Features");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Arraylist die alle features van het chromosome bevat

            ArrayList<Feature> featureFilteredList = cont.getWholeFeatureList();
            //Aan maken van een nieuwe Jtable and make it non editable
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int Column) {
                    return false;
                }
            };

            JTable table = new JTable(model);

            //Voeg de kolomnamen toe aan het model
            model.addColumn("ID");
            model.addColumn("Type");
            model.addColumn("Strand");
            model.addColumn("Start");
            model.addColumn("stop");

            //Bepaal het feature type door te kijken of het een instance of .... is.
            for (Feature feat : featureFilteredList) {
                String featureName = "";
                if (feat instanceof mRNA) {
                    featureName = "mRNA";
                } else if (feat instanceof CDS) {
                    featureName = "CDS";
                } else if (feat instanceof Exon) {
                    featureName = "Exon";
                } else if (feat instanceof Region) {
                    featureName = "Region";
                } else if (feat instanceof Gene) {
                    featureName = "Gene";
                }
                //Voeg een regel toe aan het model.
                model.addRow(new Object[]{feat.getId(), featureName, feat.getStrand(), feat.getStart(), feat.getStop()});
            }

            frame.add(new JScrollPane(table));
            frame.setVisible(true);

        } catch (NullPointerException e) {
            final JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "Er is geen gff ingeladen/features beschikbaar ");
            frame.dispose();

        }
    }

    /**
     * Context instellen
     *
     * @param context
     */
    public void setContext(Context context) {
        this.cont = context;
    }
}
