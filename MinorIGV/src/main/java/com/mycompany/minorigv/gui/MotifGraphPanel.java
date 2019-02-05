package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.motif.PositionScoreMatrix;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;


public class MotifGraphPanel extends IGVPanel implements PropertyChangeListener {

    private static final int BORDER_GAP = 12;
    private static final Color GRAPH_COLOR = Color.blue;
    private static final Stroke GRAPH_STROKE = new BasicStroke(1f);

    /**
     * Constructor
     *
     * @param context huidige context
     */
    public MotifGraphPanel(Context context) {
        super();
        this.setContext(context);
        this.setListeners();
        this.init();

    }

    @Override
    protected void paintComponent(Graphics g) {
        ArrayList<PositionScoreMatrix> motifs = cont.getMatrixesforSearch();
        HashMap<Integer, double[]> matrixForwardAlignmentScores = cont.getmatrixForwardAlignmentScores();
        HashMap<Integer, double[]> matrixReverseAlignmentScores = cont.getMatrixReverseAlignmentScores();
        double[] stepsArray = cont.getStepsForMotifs();


        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int start = cont.getStart();
        int stop = cont.getStop();
        int length = cont.getLength();

        int stepSize = (int) Math.ceil((length / (double) (getWidth() - 2 * BORDER_GAP)));

        ArrayList<ArrayList<Point>> graphPoints = new ArrayList<ArrayList<Point>>();


        for (int i = 0; i < matrixForwardAlignmentScores.size(); i++) {
            double MAX_SCORE = motifs.get(i).getSumScore();
            double[] scores = matrixForwardAlignmentScores.get(i);
            graphPoints.add(new ArrayList<Point>());

            for (int t = start; t < Math.min(stop,scores.length-1); t += stepSize) {
                Point punt;
                int x1 = (int) DrawingTools.calculateLetterPosition(getWidth(), length, t-start);

                int k;
                double valueNeg = MAX_SCORE;
                for (k = 0; k < (int) stepSize; k++) {

                    double temp = new Double(MAX_SCORE - scores[Math.min(t+k,scores.length-1)]);
                    if (temp < valueNeg){
                        valueNeg = temp;
                    }
                }

                double percHeight = valueNeg / MAX_SCORE;
                int pixHeight = (getHeight() - 2 * BORDER_GAP);
                int y1 =  BORDER_GAP + (int)(percHeight * pixHeight);
                graphPoints.get(i).add(new Point(x1, y1));

            }
        }

        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(new BasicStroke(2));

        int t = 0;
        String rede = new String("RED");
        for (ArrayList<Point> lijst : graphPoints) { //fw en rv
            for (int i = 0; i < lijst.size() - 1; i++) { //loopen lijst

                if (t == 0) {
                    g.setColor(Color.RED); //eerste
                } else if (t == 1){
                    g.setColor(Color.ORANGE); //tweede
                } else if (t == 2){
                    g.setColor(Color.YELLOW); //tweede
          //      } else if (t == 3){
           //         g.setColor(new Color(173,255,47)); //tweede
                } else if (t == 3){
                    g.setColor(Color.GREEN); //tweede
                } else if (t == 4){
                    g.setColor(new Color(0,255,127)); //tweede
                } else if (t == 5){
                    g.setColor(new Color(0,255,255));//tweede
                } else if (t == 6){
                    g.setColor(Color.BLUE); //tweede
                }

                int x1 = lijst.get(i).x;
                int y1 = lijst.get(i).y;
                int x2 = lijst.get(i + 1).x;
                int y2 = lijst.get(i + 1).y;

                g2.drawLine(x1, y1, x2, y2);


            }
            t++; //kleuren teller
        }

        g2.setStroke(oldStroke);
        g2.setColor(Color.BLACK);

    }

    public void init() {
        this.setPreferredSize(new Dimension(200, 25));
        this.setMinimumSize(new Dimension(100, 25));

    }

    @Override
    public void setListeners() {
        cont.addPropertyChangeListener("range", this);
        cont.addPropertyChangeListener("chromosome", this);
        cont.addPropertyChangeListener("blastedReads", this);
    }
}
