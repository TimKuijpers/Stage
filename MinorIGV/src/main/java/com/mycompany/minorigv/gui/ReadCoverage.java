package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.gffparser.Organisms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


/**
 * @author kahuub 
 * Minimalistische ligt gehackte manier om readCoverage via blast te bepalen zonder te hoeven dealen met memory issues.
 */
public class ReadCoverage {


    private HashMap<String,int[]> coverageMap;
    private Organisms organism;


    /**
     * Constructor
     * @param org organism om in te stellen
     */
    public ReadCoverage(Organisms org){
        this.organism = org;

    }

    /**
     * Maken van de hashmap voor de coverage depth arrays.
     */
    public void makeMap(){
        this.coverageMap = new HashMap<>(organism.getChromosomes().keySet().size());

        for (String chrID : organism.getChromosomes().keySet()){

            int[] errey = new int[organism.getChromosome(chrID).getSeqTemp().length()];
            Arrays.fill(errey,0);

            coverageMap.put(chrID,errey); //int array ter grote van chromosoom's sequentie.

        }
    }


    /**
     * Parsen van de CSV die uit blast rolt.
     * @param filePath path van de csv
     * @throws IOException standaard Input output exception
     */
    public void parseBlastCSV(String filePath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        makeMap();

        String line;
        int k = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String[] splitLine = line.split(",");

            String header = splitLine[0];
            String chrID  = splitLine[1];
            int start     = Integer.parseInt(splitLine[2]);
            int stop      = Integer.parseInt(splitLine[3]);

            int[] arrey = coverageMap.get(chrID);

            if(start > stop){
                int temp = start;
                start = stop;
                stop = temp;
            }

            for (int i = start; i < stop; i++){
                arrey[i]++;  //ophogen van alle waardes tussen start en stop met 1 omdat hier een read ligt.
            }

//            if(start ==1){
//                System.out.println(line);
//            }


        }
//        System.out.println(k);

    }

    /**
     * Functie voor de diepte tussen 2 punten verkrijgen
     * @param chrID ID van het chromosoom
     * @param startIndex start index
     * @param endIndex stop index.
     * @return coverage depth int array.
     */
    public int[] getCoverageBetween(String chrID, int startIndex, int endIndex){

        if (coverageMap == null){
            return null;
        }
        return Arrays.copyOfRange(coverageMap.get(chrID), startIndex, endIndex);

    }
}
