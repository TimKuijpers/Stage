package com.mycompany.minorigv.gui;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class voor het tekenen van de Grafiek bij de readCoverage.
 * @author Tim, Huub
 *
 */
public class GraphPanel extends IGVPanel implements PropertyChangeListener {

    private static final int BORDER_GAP = 12;
    private static final Color GRAPH_COLOR = Color.blue;
    private static final Stroke GRAPH_STROKE = new BasicStroke(1f);

    /**
     * Constructor
     * @param context huidige context
     */
    public GraphPanel(Context context) {
        super();
        this.setContext(context);
        this.setListeners();
        this.init();
    }

    @Override
    protected void paintComponent(Graphics g) {

        if(cont.getCurrentReadCoverage() == null){
            return;
        }

        ArrayList<int[]> readLists = cont.getCurrentReadCoverage();

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int MAX_SCORE = 0;
        for( int[] hoi : readLists) {
            for (int a : hoi) {
                if (a > MAX_SCORE) MAX_SCORE = a;
            }
        }

        int length = readLists.get(0).length;

        double yScale = ((double) getHeight() - 2 * BORDER_GAP) / (MAX_SCORE - 1);

        int steps = (int) Math.ceil((length / (double) (getWidth() - 2 *BORDER_GAP)));

        ArrayList<ArrayList<Point>> graphPoints = new ArrayList<ArrayList<Point>>();

        for(int[] a : readLists){
            graphPoints.add(new ArrayList<>());
        }

        for (int i = 0; i < length; i+=steps) {

            int x1 = (int) DrawingTools.calculateLetterPosition(getWidth(),length,i);
            int t = 0;
            for(int[] hoi : readLists) {
                double valueNeg = new Double(MAX_SCORE - hoi[i]);
                double percHeight = valueNeg / (double) MAX_SCORE;
                int pixHeight = (getHeight() - 2 * BORDER_GAP);
                int y1 = (int) (percHeight * pixHeight);
                graphPoints.get(t).add(new Point(x1, y1));
                t++;
            }
        }

        // create x and y axes
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP , BORDER_GAP, BORDER_GAP + 20);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(GRAPH_STROKE);

        int t = 0;
        for (ArrayList<Point> lijst : graphPoints) { //fw en rv
            for (int i = 0; i < lijst.size() - 1; i++) { //loopen lijst

                if(t == 0){
                    g.setColor(Color.BLUE); //eerste
                }
                else{
                    g.setColor(Color.RED); //tweede
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
        g2.drawString(String.valueOf(MAX_SCORE),0,20);

    }


    public void init() {
        this.setPreferredSize(new Dimension(200, 25));
        this.setMinimumSize(new Dimension(100, 25));

    }

    @Override
    public void setListeners() {
        cont.addPropertyChangeListener("range", this);
        cont.addPropertyChangeListener("chromosome", this);
        cont.addPropertyChangeListener("blastedReads",this);
    }
}