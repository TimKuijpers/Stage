package com.mycompany.minorigv.gui;

import java.awt.*;
import java.awt.font.LineMetrics;

public class DrawingTools {

	private static final int OFFSET = 12;

	/**
	 * @param width de breedte van het Paneel.
	 * @param amount de hoeveelheid letters op het Paneel.
	 * @param i de index van de huidige "letter/positie".
	 * @return de geoorloofde breedte
	 */
	public static double calculateLetterPosition(int width, int amount, double i) {

		width = width - 2*OFFSET;
		double pos = (int) (OFFSET + ((double)width/(double)(amount-1)) * i);
		return pos;

	}
	/**
	 * @param width de breedte van het Paneel.
	 * @param amount de hoeveelheid letters op het Paneel.
	 * @return de geoorloofde breedte
	 */
	public static double calculateLetterWidth(int width, int amount) {
		width = width - 2*OFFSET;
		return ( (double) width)/( (double) (amount-1));

	}

	/**
	 * Hulp functie voor het tekenen van een Character gezien vanuit het midden in niet linksonder.
	 * @param g Graphics tekenPanel waar getekent gaat worden.
	 * @param charr character dat getekent moet gaan worden.
	 * @param x horizontale positie waarop getekent moet worden.
	 * @param y verticale positie waarop getekent moet worden.
	 */
	public static void drawCenteredChar(Graphics g, char charr, int x, int y) {

		g.setFont(new Font ("Monospaced", Font.BOLD, 14)); //TODO
		float font_width = g.getFontMetrics().stringWidth(String.valueOf(charr));
		LineMetrics fm = g.getFontMetrics().getLineMetrics(String.valueOf(charr), g);
		float font_height = fm.getHeight();

		g.drawString(String.valueOf(charr), (int) (x-(font_width/2)),(int) (y+(font_height/2)));
	}

    public static double calculatePixelPosition(int x, double widthPanel, double length, int start) {
		return start + ((double)Math.max((x - 12),0) / ((widthPanel-24) / (length-1)));
    }
}