package com.mycompany.minorigv.motif;

import java.util.ArrayList;
import java.util.HashMap;

public class MotifAlignment {
//atcg
    public static HashMap<Integer, double[]> Align(String kaas, ArrayList<PositionScoreMatrix> matrixesForSearch) {
        String baas = kaas;
        int f = 0;
        HashMap<Integer, double[]> allMotifScores = new HashMap<>();
        //System.out.println("matrixesforsearch.size" + matrixesForSearch.size());
        for (int m = 0; m < matrixesForSearch.size(); m++) {
            //System.out.println("matrixesforsearch.size" + matrixesForSearch.size());

            double[][] test = matrixesForSearch.get(m).getScoreMatrix();
            double[] motifScores = new double[baas.length()-test[0].length + 1];

            for (int seq = 0; seq < baas.length()-test[0].length + 1;  seq++){

                double[] positionScores = new double[test[0].length];

                for (int x = 0; x < test[0].length; x++){
                    //hier word met elke letter motief iets gedaan
                    if (baas.charAt(seq + x) == 'A'){
                        positionScores[x] = test[0][x];
              //          System.out.println(test[0][x] + "if a");
                    } else if (baas.charAt(seq + x) == 'T'){
                        positionScores[x] = test[1][x];
                    }else if (baas.charAt(seq + x) == 'C'){
                        positionScores[x] = test[2][x];
                    } else if (baas.charAt(seq + x) == 'G'){
                        positionScores[x] = test[3][x];
                    }
            //        System.out.println(baas.charAt(seq + x));
          //          System.out.println(positionScores[x]);
        //            System.out.println(positionScores.length + "in x");
                }

      //          System.out.println("___");
    //            System.out.println(positionScores.length + "in loop seq buiten x");

                double sumPositionScores = 0;
                for (int s = 0; s < positionScores.length; s++){
                    sumPositionScores += positionScores[s];
                }

                motifScores[seq] = sumPositionScores;

            }
            //add motifScores to hashmap
         //   Integer key = new Integer(m);
            allMotifScores.put(m,motifScores);

        }

        return allMotifScores;
    }

    public static double[] GenerateNrOfScores (String sequence, ArrayList<PositionScoreMatrix> matrixes){
        double[] nrOfScores = new double[matrixes.size()];
        for (int m = 0; m < matrixes.size(); m++) {
            //System.out.println("matrixesforsearch.size" + matrixesForSearch.size());

            double[][] curMatrix = matrixes.get(m).getScoreMatrix();
            nrOfScores[m] = sequence.length() - curMatrix[0].length + 1;
        }

        return nrOfScores;
    }
}
