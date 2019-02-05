package com.mycompany.minorigv.fastqparser;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Stan Wehkamp
 * Deze class bevat slechts één methode die data uit FASTQ bestanden haalt
 * Deze methode staat in een aparte class omdat hij niet thuis hoort in de andere classes
 */
public class FastqReader {

    /**
     * Leest het bestand van een pathway in en geeft de data door aan de constructor van Read.
     * De constructor geeft de reads terug aan parse waar ze in een arraylist worden gezet.
     *
     * @param path path van de file.
     * @return ArrayList<Read> een ArrayList met hierin de read objecten.
     * @throws IOException, InvalidFileTypeException
     */
    public ArrayList<Read> parse(String path) throws IOException, InvalidFileTypeException {


        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            ArrayList<Read> readList = new ArrayList<>();       // lijst voor Read objecten

            int i = 0;
            while ((line = br.readLine()) != null) {

                String description;
                String sequence;
                String qualitySequence;
                // elke contig in een FASTQ file bestaat uit 4 regels.

                description = line;
                sequence = br.readLine();
                br.readLine();                         //in regel 3 staat alleen een scheidingsteken.
                qualitySequence = br.readLine();

                Read curRead = new Read(description, sequence, qualitySequence);        //maken read object.
                readList.add(curRead);

                if(i%10000 == 0){
                    System.out.println(i);
                }
                i++;

            }
            return readList;
        }

    }

    /**
     * Leest het bestand van een pathway in en geeft de data door aan de constructor van Read.
     * De constructor geeft de reads terug aan parse waar ze in een arraylist worden gezet.
     *
     * @param pathIn path van de in file.
     * @param pathOut path van de out file.
     * @return ArrayList<Read> een ArrayList met hierin de read objecten.
     * @throws IOException, InvalidFileTypeException
     */
    public void convertToFasta(String pathIn, String pathOut) throws IOException, InvalidFileTypeException {

        try (BufferedReader br = new BufferedReader(new FileReader(pathIn)); BufferedWriter bo = new BufferedWriter(new FileWriter(pathOut))) {
            String line;


            int i = 0;
            while ((line = br.readLine()) != null) {

                String description = line;
                line = br.readLine();
                String sequence = line;
                br.readLine();                         //in regel 3 staat alleen een scheidingsteken.
                br.readLine();

                bo.write(">");
                bo.write(description);
                bo.write(System.lineSeparator());
                bo.write(sequence);
                bo.write(System.lineSeparator());

                if(i%10000 == 0){
                    System.out.println(i);
                }
                i++;

            }

        }

    }
}