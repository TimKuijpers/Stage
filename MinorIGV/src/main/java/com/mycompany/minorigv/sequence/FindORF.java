package com.mycompany.minorigv.sequence;

import com.mycompany.minorigv.gffparser.ORF;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Het vinden van Open Reading Frames (ORFs) in de sequentie van een chromosoom/contig. De informatie van een ORF
 * wordt opgeslagen in een object.
 *
 * @author Anne van Ewijk en Amber Janssen Groesbeek.
 */
public class FindORF {

    /**
     * Het vinden van het ORF in drie verschillende reading frames (Template strand). Elk ORF wordt opgeslagen in een object.
     *
     * @param chromosomeID    Het id van het chromosoom.
     * @param seq   De sequentie van het chromosoom.
     * @return  listORF is een Arraylist met ORF objecten.
     */
    public static ArrayList<ORF> searchORF(String chromosomeID, String seq, int lengthORFUser) {
        // Patroon voor een ORF. Wordt gekeken waar dit ORF op de chromosoom/contig sequentie ligt.
        Pattern patroon = Pattern.compile("(?=((ATG)(.{3})*?(TAG|TGA|TAA)))", Pattern.CASE_INSENSITIVE); //TODO koppel codontabel

        // Informatie ophalen van het ORF.
        String[] forwardAndReverse = {seq,MakeCompStrand.getReverseComplement(seq)};
        ArrayList<ORF> listORF = new ArrayList<>();
        Strand strand = Strand.POSITIVE;
        for(String sequence : forwardAndReverse){
            Matcher match = patroon.matcher(sequence);
            int idORF = 0;

            while(match.find()){

                idORF++; //ophogen ID.
                int stop = generateStop(sequence.length(),match.group(1).length(),match.start(),strand);
                int start = generateStart(stop, match.group(1).length(), match.start(),strand);

                int readingFrame = ORF.calcFrame(start, strand, sequence.length()); // Readingframe wordt bepaald

                int lengthORF = stop - start; // Lengte van het ORF wordt bepaald.

                // Slaat alleen het ORF op wanneer de lenge van het ORF minimaal de lengte heeft die de gebruiker heeft ingesteld.
                if(lengthORF >= lengthORFUser){
                    ORF ORFObject = new ORF(start, stop, readingFrame, String.valueOf(idORF), strand, lengthORF,chromosomeID);
                    listORF.add(ORFObject);
                }
            }
            strand = Strand.NEGATIVE;
        }

        return listORF;
    }

    /**
     * Genereren van de Stop voor de positieve en negatieve strand.
     * @param sequenceLength        int: lengte van de sequentie
     * @param stopCodonPosition     int: lengte van het gematchte deel.
     * @param matchStart            int: positie van het startcodon
     * @param strand                Strand: de strand (negatief of positief)
     * @return
     */
    public static int generateStop(int sequenceLength,int stopCodonPosition, int matchStart, Strand strand){
        // -1 : index laatste letter.   - 1: tot ipv t/m
        if (strand == Strand.NEGATIVE){
            return (sequenceLength - 1) - (matchStart - 1);
        }
        else{
            return matchStart + stopCodonPosition;
        }

    }

    /**
     * Genereren van de Stop voor de positieve en negatieve strand.
     * @param stop                  int: positie van het stopcodon.
     * @param stopCodonPosition     int: lengte van het gematchte deel.
     * @param matchStart            int: positie van het startcodon
     * @param strand                Strand: de strand (negatief of positief)
     * @return
     */
    public static int generateStart(int stop, int stopCodonPosition,int matchStart, Strand strand){
        if (strand == Strand.NEGATIVE){
            return stop - stopCodonPosition;
        }
        else{
            return matchStart;
        }
    }

}