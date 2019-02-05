package com.mycompany.minorigv.gffparser;

import com.mycompany.minorigv.sequence.Strand;

import java.util.Objects;

/**
 * In de sequentie van een chromosoom/contig wordt er gezocht naar Open Reading Frames (ORFs) en
 * informatie van een ORF en de sequentie zelf worden opgeslagen in een object.
 */
public class ORF implements MappableFeature{
    private int start;
    private int stop;
    private int readingframe;
    private String idORF;
    private Strand strand;
    private int lengthORF;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ORF orf = (ORF) o;
        return start == orf.start &&
                stop == orf.stop &&
                readingframe == orf.readingframe &&
                strand == orf.strand &&
                Objects.equals(chromosomeID, orf.chromosomeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, stop, readingframe, strand, chromosomeID);
    }

    private String chromosomeID;


    /**
     * Het opslaan van alle informatie van een ORF in een object.
     * @param start             Start positie van de eerste nucleotide van het startcodon
     * @param stop              Stop positie van de eerste nucleotide van het stopcodon
     * @param readingframe      In welk readingframe én strand (-/+) het ORF is gevonden.
     * @param idORF             Het ID dat wordt meegegeven aan het ORF om ORFs te kunnen onderscheiden
     * @param strand            De strand van het gevonden ORF.
     * @param lengthORF         De lengte van het ORF (stop-start)
     * @param chromosomeID      Het ID van het chromosoom waarop dit ORF gevonden is.
     */
    public ORF(int start, int stop, int readingframe, String idORF, Strand strand, int lengthORF, String chromosomeID) {
        this.start = start;
        this.stop = stop;
        this.readingframe = readingframe;
        this.idORF = idORF;
        this.strand = strand;
        this.lengthORF = lengthORF;
        this.chromosomeID = chromosomeID;
    }

    /**
     * Het ophalen van de startpositie van de eerste nucleotide van het startcodon
     *
     * @return      De start positie van de eerste nucleotide van het startcodon
     */
    public int getStart() {
        return start;
    }

    /**
     * Het opslaan van de startpositie van de eerste nucleotide van het startcodon
     *
     * @param start De start positie van de eerste nucleotide van het startcodon
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Het ophalen van de stop positie van de eerste nucleotide van het stopcodon
     * @return      De stop positie van de eerste nucleotide van het stopcodon
     */
    public int getStop() {
        return stop;
    }

    /**
     * Het opslaan van de stop positie van de eerste nucleotide van het stopcodon
     * @param stop   De stop positie van de eerste nucleotide van het stopcodon
     */
    public void setStop(int stop) {
        this.stop = stop;
    }

    /**
     * Het ophalen van het reading frame waarin het ORF is gevonden
     * @return      De reading frame van het ORF met de strand (+1, +2, +3, -1, -2, -3)
     */
    public int getReadingframe() {
        return readingframe;
    }

    /**
     * Het opslaan van het reading frame waarin het ORF is gevonden, samen met de strand waarin het ORF zit (+ of -)
     * @param readingframe      De reading frame waarin het ORF is gevonden met de strand (+1, +2, +3, -1, -2, -3)
     */
    public void setReadingframe(int readingframe) {
        this.readingframe = readingframe;
    }

    /**
     * Het ophalen van het ID van het gevonden ORF
     * @return      Een ID voor het ORF
     */
    public String getIdORF() {
        return idORF;
    }

    /**
     * Het opslaan van het ID van het gevonden ORF
     * @param idORF     ID van het ORF
     */
    public void setIdORF(String idORF) {
        this.idORF = idORF;
    }


    /**
     *
     * @return
     */
    public Strand getStrand() {
        return strand;
    }

    /**
     *
     * @param strand
     */
    public void setStrand(Strand strand) {
        this.strand = strand;
    }

    public int getLengthORF() {
        return lengthORF;
    }

    public void setLengthORF(int lengthORF) {
        this.lengthORF = lengthORF;
    }

    public String getChromosomeID() {
        return chromosomeID;
    }

    public void setChromosomeID(String chromosomeID) {
        this.chromosomeID = chromosomeID;
    }

    /**
     *
     * @param index de positie van de eerste letter (nucleotide) van een subsequentie op de forward.
     * @param strand de POSITIVE (forward) of NEGATIVE (reverse) strand.
     * @param lenghtSeq de lengte van de hele sequentie.
     * @return
     */
    public static int calcFrame(int index, Strand strand, int lenghtSeq){
        // Frame bepalen van positieve en negatieve strand.
        if(strand == Strand.POSITIVE){
            return index % 3;
        }else if(strand == Strand.NEGATIVE){
            return ((lenghtSeq - 1) - index) % 3;
        }
        else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return "ORF{" +
                "start=" + start +
                ", stop=" + stop +
                ", readingframe=" + readingframe +
                ", strand=" + strand +
                '}';
    }
}