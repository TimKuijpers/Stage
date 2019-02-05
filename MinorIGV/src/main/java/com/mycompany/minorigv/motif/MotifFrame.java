package com.mycompany.minorigv.motif;

import com.mycompany.minorigv.gui.Context;
import com.mycompany.minorigv.gui.IGVMenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JFrame;
/**
 * deze class bevat een frame waarin data word geplaatst door de gebruiker om matrixen te generen en selecteren voor
 * verdere handelingen
 */
public class MotifFrame extends Frame {
    private Context cont;
    private IGVMenuBar bar;
    JFrame frame2;
/**
 * bouwt de frame en zet de funties van de knoppen
 */
    public MotifFrame(ArrayList<PositionScoreMatrix> startUpContextMatrixList, IGVMenuBar barr) {
        frame2 = new JFrame();
        bar = barr;

        JLabel matrixNameLabel = new JLabel("Matrix name");
        JTextField matrixNameField = new JTextField();

        JLabel sequencesLabel = new JLabel("Sequences");
        TextArea sequenceArea = new TextArea("AAA");

        JButton matrixButton = new JButton("build matrix");

        JLabel matrixesBuildLabel = new JLabel("Matrixes build*");
        matrixesBuildLabel.setToolTipText("Multiple matrixes can be selected by holding the ctrl button and clicking the left mouse button.");

        JScrollPane matrixesbuild = new JScrollPane();
        DefaultListModel model = new DefaultListModel();
        JList<String> matrixesBuildList = new JList(model);
        matrixesbuild.setViewportView(matrixesBuildList);

        JButton searchButton = new JButton ("<html>search motives<br />with selected</html");
        JButton deleteButton = new JButton ("Delete Selected");


        matrixNameLabel.setBounds(20, 20, 100, 30);
        matrixNameField.setBounds(20, 50, 120, 30);

        sequencesLabel.setBounds(20, 100, 100, 30);
        sequenceArea.setBounds(20, 130, 250, 300);

        matrixButton.setBounds(300,130,120,40);

        matrixesBuildLabel.setBounds(450, 20, 100, 30);

        matrixesbuild.setBounds(450, 50, 150, 380);

        searchButton.setBounds(630,360,130,70);
        deleteButton.setBounds(630, 130, 130, 40);


        //voegd de vorige lijst items toe wanneer de frame was gesloten en heropend word
        if (startUpContextMatrixList.size() >= 1) {
        //    System.out.println("if contexMatrixList.size() >= 1 is true");
            for (int i = 0; i < startUpContextMatrixList.size(); i++) {
                model.addElement(startUpContextMatrixList.get(i).getName());
            }
        }

        matrixButton.addActionListener(new ActionListener() {       //neemt de input data. bouwd een matrix. voegt item toe aan de list(via maodel)
            @Override
            public void actionPerformed(ActionEvent e) {
                cont.addMatrix(PositionScoreMatrix.buildMatrix(sequenceArea.getText(), matrixNameField.getText()));
                ArrayList<PositionScoreMatrix> contextMatrixList = cont.getMatrixes();
                model.addElement(contextMatrixList.get(contextMatrixList.size()-1).getName());
            }
        });

        deleteButton.addActionListener(new ActionListener() { // removes matrix from the list and context
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedItems = matrixesBuildList.getSelectedIndices();
                    for(int counter=selectedItems.length - 1; counter >= 0;counter--){
                       model.remove(selectedItems[counter]);
                       cont.removeMatrix(selectedItems[counter]);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() { //clears de matrixesForSearch in context. voegd de huidig geselecteerde toe. start de motif search
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedItems = matrixesBuildList.getSelectedIndices();
                ArrayList<PositionScoreMatrix> contextMatrixList = cont.getMatrixes();
                cont.removeAllMatrixForSearch();
                for(int i=0; i < selectedItems.length ;i++) {
                    cont.addMatrixForSearch(contextMatrixList.get(selectedItems[i]));
                }
                cont.gogo();
                cont.drawMotifs();
            }
        });

        frame2.add(matrixNameLabel);
        frame2.add(matrixNameField);
        frame2.add(sequencesLabel);
        frame2.add(sequenceArea);

        frame2.add(matrixButton);

        frame2.add(matrixesBuildLabel);
        frame2.add(matrixesbuild);

        frame2.add(searchButton);
        frame2.add(deleteButton);

        frame2.setSize(800, 505);
        frame2.setLayout(null);
        frame2.setVisible(true);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    }

    public void kys(){
        frame2.dispose();
    }

    /**
     * sets context
     * @param cont
     */
    public void setContext(Context cont) {
        this.cont = cont;
    }
}
/** use this to test syncronization between the shown list and context
 int[] selectedItems = matrixesBuildList.getSelectedIndices();

 for (int i = 0; i < selectedItems.length; i++) {
 System.out.println(selectedItems[i]);
 }

 ArrayList<PositionScoreMatrix> contextMatrixList = cont.getMatrixes();
 for (int i = 0; i < contextMatrixList.size(); i++) {
 System.out.println(contextMatrixList.get(i).getName());
 }
 }
 }); **/