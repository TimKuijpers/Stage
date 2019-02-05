package com.mycompany.minorigv.motif;

import com.mycompany.minorigv.gui.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * deze class bevat een score matrix voor die gebruikt word om motieven te vinden
 */
public class PositionScoreMatrix {
    private String name;
    private double sumScore;
    private double[][] scoreMatrix;
    private String userInputSequences;

    /**
     * constructor voor PositionScoreMatrix
     * @param name string naam voor de PositionScoreMatrix die gegeven is door de user
     * @param sumScore double de hoogste score die te krijgen is met deze matrix
     * @param scoreMatrix double[][] de matrix met positie specifieke scores voor de karakters A,T,C,G
     * @param userInputSequences String deze worden gebruikt om de matrix te berekenen.
     */
    public PositionScoreMatrix(String name, double sumScore, double[][] scoreMatrix, String userInputSequences){
        this.name = name;
        this.sumScore = sumScore;
        this.scoreMatrix = scoreMatrix;
        this.userInputSequences = userInputSequences;
    }

    /**
     * genereerd de argumenten voor de Constructor van PositionScoreMatrix vervolgens roept het de constructor aan
     * en returns een nieuwe PositionScoreMatrix instance
     * @param inputSequences
     * @param name
     * @return
     */
    public static PositionScoreMatrix buildMatrix(String inputSequences, String name) {
        String[] lines = splitInput(inputSequences);
        //System.out.println(lines.length + "liness");
        double[][] matrix = buildFrequentieMatrix(lines);

        matrix = normalizeByNumberOfLines(matrix, lines.length);
        double sumScore = calculateSumScore(matrix);
 //       matrix = normalizeBySumScore(matrix, sumScore);
        PositionScoreMatrix newMatrix = new PositionScoreMatrix(name, sumScore, matrix, inputSequences);

        return newMatrix;
    }

    /**
     *
     * @param input
     * @return
     */
    private static String[] splitInput(String input) {
        input = input.toUpperCase();
        return input.split("\\r?\\n");
    }

    /**
     * de functie maakt een frequentie matrix. dit is nodig voor het genereren van een score matrix. het voorkomen van ATCG word op elke positie op elke line geteld en in de matrix gezet
     * @param lines Srting[]
     * @return double[][] frequentie matrix
     */
    private static double[][] buildFrequentieMatrix(String[] lines) {
        double[][] frequentieMatrix = new double[4][lines[0].length()];        //4 rijen voor ATGC en het aantal kollomen voor de lengte van het ingevoerde motief
        //System.out.println(frequentieMatrix.length + "lengt funcite van frequentie matrix");
        for (int column = 0; column < lines[0].length(); column++) {           //lengte van het motief = het aantal kollomen in de matrix
            //System.out.println("loopnr" + column);
            int aCount = 0;
            int tCount = 0;
            int cCount = 0;
            int gCount = 0;


            for (int y = 0; y < lines.length; y++) {      //het aantal motieven
                //System.out.println("motief" + y);
                if (lines[y].substring(column, column + 1).equals("A")) {
                    aCount += 1;
                } else if (lines[y].substring(column, column + 1).equals("T")) {
                    tCount += 1;
                } else if (lines[y].substring(column, column + 1).equals("C")) {
                    cCount += 1;
                } else if (lines[y].substring(column, column + 1).equals("G")) {
                    gCount += 1;
                } else {
                    //System.out.println("exception hier" + lines[y].substring(column, column + 1) + "exception hier end");
                }
            }


            for (int row = 0; row < 4; row++) {
                //System.out.println("row " + row);
                if (row == 0) {
                    frequentieMatrix[row][column] = aCount;
                } else if (row == 1) {
                    frequentieMatrix[row][column] = tCount;
                } else if (row == 2) {
                    frequentieMatrix[row][column] = cCount;
                } else if (row == 3) {
                    frequentieMatrix[row][column] = gCount;
                } else {
                    //System.out.println("exception hier" + row + "rownummer"); // word misschien optie voor spaties
                }
            }
        }
        return frequentieMatrix;
    }

    /**
     *
     * @param matrix double[][]  de frequentie matrix
     * @param nrLines int het aantal lines/sequenties die de gebruiker had ingevoerd
     * @return
     */
    private static double[][] normalizeByNumberOfLines(double[][] matrix, int nrLines) {
        for (int column = 0; column < matrix[0].length; column++) {
            for (int row = 0; row < matrix.length; row++) {
                matrix[row][column] = matrix[row][column] / nrLines;
            }
        }
        return matrix;
    }

    /**
     * berekend de som van de max waarde in elke kollom. dit is de maximale score die te halen valt met deze score matrix
     * @param matrix double[][]. de frequentie matrix die genormaliseerd is met het aantal lijnen
     * @return double sumScore de som van de max waarde in elke kollom
     **/
    private static double calculateSumScore(double[][] matrix){
        double[] maxScorePerPosition = new double[matrix[0].length];
        double sumScore;

        for (int column = 0; column < matrix[0].length; column++) {    // voor elke kollom
            double maxPositionScore = 0;

            for (int row = 0; row < matrix.length; row++) {   // voor elke rij
                if (matrix[row][column] > maxPositionScore){
                    maxPositionScore = matrix[row][column];
                }
            }
            maxScorePerPosition[column] = maxPositionScore;
        }

        sumScore = 0;
        for (int i = 0; i < maxScorePerPosition.length; i++){
            sumScore += maxScorePerPosition[i];
        }
        return sumScore;
    }

    /**
     * normaliseerd de matrix met de maximale score die te halen valt met deze matrix
     * @param matrix double[][] frequentie
     * @param sumScore double sumScore de som van de max waarde in elke kollom
     * @return
     */
    private static double[][] normalizeBySumScore(double[][] matrix, double sumScore) {
        int maxScore = matrix[0].length;
        double normalizer = sumScore / maxScore;
        for (int column = 0; column < matrix[0].length; column++) {
            for (int row = 0; row < matrix.length; row++) {
                matrix[row][column] = matrix[row][column] * normalizer;
            }
        }
    return matrix;
    }
// 1    1     0,5       0,5      3     0,75
// 0,75 0,75  0,375     0,375    2,25

// 1    0,5    0,5      0,5      2,5
// 0,75 0,375  0,375    0,375    1,875
//
// 0,5    0,5    0,5    0,5      2
// 0,375  0,375  0,375  0,375    1,5
//
//0,25 0,25 0,5 0,5
//0,25 0,25 0,5 0,5           1,125
    /**
     * getter
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * getter
     * @return double sumScore
     */
    public double getSumScore() {
        return sumScore;
    }

    /**
     * getter
     * @return double[][] scoreMatrix
     */
    public double[][] getScoreMatrix() {
        return scoreMatrix;
    }

    /**
     * getter
     * @return String userInputSequences
     */
    public String getUserInputSequences() {
        return userInputSequences;
    }
}

/**
 for (int x = 0; x < matrix.length; x++) {
 for (int q = 0; q < matrix[0].length; q++) {
 System.out.print(matrix[x][q] + " ");
 }
 System.out.println("row");
 }

 for (int x = 0; x < matrix[0].length; x++) {
 for (int q = 0; q < matrix.length; q++) {
 System.out.print(matrix[q][x] + " ");
 }
 System.out.println("collum");
 }
 **/
/**
 * gebruik dit om door de matrix per rij te loopen
 * for (int x = 0; x < frequentieMatrix.length; x++){
 * for (int q = 0; q < frequentieMatrix[0].length; q++){
 * System.out.print(frequentieMatrix[x][q]);
 * }
 * }
 * // (frequentieMatrix.length) = 4 = de 4 rijen voor ATCG
 * //(frequentieMatrix[0]) = freqentie A voor elke positie in de motieven
 * //(frequentieMatrix[x].lenght) = lengte van een motief. de motieven moeten even lang zijn
 **/