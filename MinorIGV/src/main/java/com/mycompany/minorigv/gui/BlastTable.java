package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.blast.*;
import com.mycompany.minorigv.gffparser.BlastedORF;
import com.mycompany.minorigv.gffparser.Chromosome;
import com.mycompany.minorigv.gffparser.ORF;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * Table output voor een blastXML
 */
public class BlastTable extends JFrame {

    Context cont;

    /**
     * Constructor voor de Table
     * @param cont een blastoutput die gevisualiseerd moet worden.
     */
    public BlastTable(Context cont) {
        super("blast tabel");
        this.cont = cont;
        setMinimumSize(new Dimension(500, 500));
        this.add(new JScrollPane(makeTable()));
        this.pack();
        this.setVisible(true);

    }

    /**
     * Functie die de JTable maakt uit de data.
     * @return een JTable.
     */
    public JTable makeTable() {

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int Column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        String[] Columns = {
                "hitDef",
                "hitAccession",
                "hspScore",
                "hspEvalue",
                "hspIdentity",
                "hspPositives",
                "hpsGaps",
                "start ORF",
                "stop ORF",
                "RF ORF",
                "strand ORF",
                "chromosoom ORF"

        };

        for (String colHeader : Columns) {
            model.addColumn(colHeader);
        }


        for (Chromosome chrom : cont.getOrganism().getChromosomes().values()){


            for (ORF o : chrom.getListORF()){
                ArrayList<String> row = new ArrayList<String>();
                if(o instanceof BlastedORF){

                    if(((BlastedORF) o).hasHit()) {

                        row.add(((BlastedORF) o).getBestHit().getHitDef());
                        row.add(((BlastedORF) o).getBestHit().getHitAccession());

                        String scoreString = ((BlastedORF) o).getBestHsp().getHspBitScore() + " bits(" + ((BlastedORF) o).getBestHsp().getHspScore() + ")";
                        row.add(scoreString);

                        row.add(((BlastedORF) o).getBestHsp().getHspEvalue());

                        int length = Integer.parseInt(((BlastedORF) o).getBestHsp().getHspAlignLen());
                        int identity = Integer.parseInt(((BlastedORF) o).getBestHsp().getHspIdentity());
                        int positives = Integer.parseInt(((BlastedORF) o).getBestHsp().getHspPositive());
                        int gaps = Integer.parseInt(((BlastedORF) o).getBestHsp().getHspGaps());

                        String identityString = identity + "/" + length + "(" + identity * 100 / length + "%)";
                        String positivesString = positives + "/" + length + "(" + positives * 100 / length + "%)";
                        String gapsString = gaps + "/" + length + "(" + gaps * 100 / length + "%)";

                        row.add(identityString);
                        row.add(positivesString);
                        row.add(gapsString);

                    }
                    else {
                        for(int i = 0;i < 7; i++){
                            row.add("");
                        }

                    }
                    row.add(String.valueOf(o.getStart()));
                    row.add(String.valueOf(o.getStop()));
                    row.add(String.valueOf(o.getReadingframe()));
                    row.add(String.valueOf(o.getStrand()));
                    row.add(String.valueOf(o.getChromosomeID()));
                    model.addRow(row.toArray());

                }
            }
        }





        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {

                if(!event.getValueIsAdjusting()){
                    newInfoPanel(table.getSelectedRow());
                }

            }
        });

        return table;
    }

    /**
     * Functie voor het tonen van de hit behorende bij de huidige selectie.
     * @param index
     */
    private void newInfoPanel(int index) {

        JFrame iterationFrame = new JFrame();

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int Column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        iterationFrame.add(table);

    }

}