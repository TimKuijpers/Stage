package com.mycompany.minorigv.motif;

public class main {

    public static void main(String[] args) {
        String chromosoom1 = new String("aaaaaaaaaabbbb");
        String chromosoom2 = new String("ccccdddddddddd");
      String chromoCompare1 = new String  ("0123456");
        String chromoCompare2 = new String("3456789");

        System.out.println(chromosoom1.length());
        System.out.println(chromosoom1.substring(0,1));

        double[][] frequentieMatrix = new double[4][5];
        System.out.println(chromosoom1.substring(chromosoom1.length() - frequentieMatrix[0].length,chromosoom1.length()));
        System.out.println(chromosoom2.substring(0, frequentieMatrix[0].length));

        //System.out.println(frequentieMatrix[0].length);
        for (int i = 0; i < chromosoom1.length() - frequentieMatrix[0].length + 1; i++){
            System.out.println(chromosoom1.substring(i, i + frequentieMatrix[0].length) + "k");
        }

        String lijmStuk1 = chromosoom1.substring(chromosoom1.length() - frequentieMatrix[0].length + 1,chromosoom1.length());
        System.out.println(lijmStuk1);
        String lijmStuk2 = chromosoom2.substring(0, frequentieMatrix[0].length -1);
        System.out.println(lijmStuk2);
        String lijmStuk3 = chromosoom1.substring(chromosoom1.length() - frequentieMatrix[0].length + 1,chromosoom1.length()) + chromosoom2.substring(0, frequentieMatrix[0].length -1); ;
        System.out.println(lijmStuk3);
    }
}
