package com.mycompany.minorigv.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.print.DocFlavor;
import javax.swing.JPanel;

/**
 * @author Stan Wehkamp, Kahuub
 * bouwt en tekent het paneel met de liniaal.
 */
public class RulerPanel extends IGVPanel implements PropertyChangeListener{

	private int x;                  //linkerkant selectie rechthoek
	private int x2;                 //rechterkant selectie rechthoek


	public RulerPanel(Context context) {
		super();
		setContext(context);
		setListeners();
		init();
	}


	/**
	 * initializes the panel
	 */
	public void init() {
		this.setBackground(Color.lightGray);
		setPreferredSize(new Dimension(500,75));
		setMaximumSize(new Dimension(2000,40));
		setMinimumSize(new Dimension(100,30));

		x = x2 = 0; //selectie rechthoek
		MyMouseListener listener = new MyMouseListener(); //luisteren naar de muis
		addMouseListener(listener);
		addMouseMotionListener(listener);

	}

	/**
	 * tekend componenten in de panel
	 * roept helper methods aan om de components te genereren
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int length = cont.getLength();
		int start = cont.getStart();
		int stop = cont.getStop();

		g.drawString(String.valueOf(length)+"bp",(int)(this.getSize().getWidth()/2),15); // tekend de lengte in baseparen in het midden van de panel
		g.fillRect(5,40,(int)(this.getSize().getWidth()-10),5);

		int stepSize = calculateStepSize(length);
		int first = (start - (start % stepSize)) + stepSize - 1;	// de waarde van first is gelijk aan de positie in de sequentie van de
		// eerste nucleotide waarboven de eerste ruler lijn getekend word

		for (int j = first; j < stop; j+= stepSize){


			int pos = (int) DrawingTools.calculateLetterPosition(this.getWidth(), length,Double.valueOf(j-start)); //schaald de posities in sequentie naar de breedte van de panel
			g.drawLine(pos,40,pos,30);								// draws line on the ruler
			g.drawString(String.valueOf(j + 1) , pos, 30);		// draws the nucleotide position(in sequence) above the line

		}

		int alpha = 127; // 50% transparent
		Color myColour = new Color(255, 0, 0, alpha);
		g.setColor(myColour);

		if (x < x2){
			g.fillRect(x,0,x2-x,getHeight());  //selectie rechthoek
		}
		else{
			g.fillRect(x2,0,x-x2,getHeight()); //inverse rechthoek.
		}
	}

	/**
	 * Berekent de stapgrootte.
	 * @param length int
	 * @return stepSize int
	 */
	public static int calculateStepSize(int length){
		double lengte = Double.valueOf(length);
		int multiplier = 1;
		int[] options = {1,2,5};			// de basis waardes van stepSize

		while((lengte / 10) > 10){			// bepaald de vermenigvuldiger voor stepSize
			multiplier = multiplier * 10;
			lengte = lengte / 10;
		}
		for(int baseValue : options){				// loops door options
			if(lengte / baseValue <= 20){			// bepaald welke basis waarde als baseValue gebruikt word
				return multiplier * baseValue;		// returns de StepSize
			}
		}

		//1-2-5-10-20-50-100-200-500-1000-2000-5000-10000
		return -1;
	}

	/**
	 * set de context object voor RulerPanel en voegt een PropertyChangeListener toe aan context
	 * @param conti
	 */
	public void setContext(Context conti) {
		this.cont = conti;
	}

	@Override
	public void setListeners() {
		cont.addPropertyChangeListener("range", this); // luisterd of in context de functie firePropertyChange met als topic: "range", word uitgevoerd.

	}

	/**
	 * Start van selectie rechthoek instellen.
	 * @param x
	 */
	public void setStartPoint(int x) {
		this.x = x;

	}

	/**
	 * stop van selectie rechthoek instellen.
	 * @param x
	 */
	public void setEndPoint(int x) {
		x2 = (x);

	}

	/**
	 * functie die het zoomen doet op mouse release.
	 */
	private void dragZoom() {

		int width = this.getWidth();

		int start = cont.getStart();
		double amount = cont.getLength();

		int newStart = (int) DrawingTools.calculatePixelPosition(x ,width,amount,start);
		int newStop  = (int) DrawingTools.calculatePixelPosition(x2,width,amount,start);
		x = x2 = 0;

		int temp;
		if (newStop < newStart){
			temp = newStart;
			newStart = newStop;
			newStop = temp;
		}

		cont.changeSize(newStart,newStop);

	}

	/**
	 * functie die repaint spamt tijdens het bewegen. //TODO bufferen.
	 */
	private void dragPaint() {

		//buffer die image ooit
		invalidate();
		repaint();
	}

	/**
	 * Hulper class MouseListener voor de selectie
	 */
	class MyMouseListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			setStartPoint(e.getX());
		}

		public void mouseDragged(MouseEvent e) {
			setEndPoint(e.getX());
			dragPaint();
		}

		public void mouseReleased(MouseEvent e) {
			setEndPoint(e.getX());
			dragZoom();
			invalidate();
			repaint();

		}
	}

}