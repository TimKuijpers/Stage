package com.mycompany.minorigv.gui;

public enum Paths {

    HOME_DIRECTORY("homeDirectory"),
    GENOMES("genomes"),
    NR("nr"),
    SAVE_ORF("saveORF"),
    SAVE_BLAST_ORF("saveBlastORF"),
    OUTPUT_ORF("outputORF"),
    OUTPUT_READS("outputReads"),
    DIAMOND_DB("diamondDB");

    private final String text;

    /**
     * Constructor.
     * @param text de tekst.
     */
    Paths(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
