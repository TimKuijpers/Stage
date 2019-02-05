package com.mycompany.minorigv.gffparser;

import java.util.HashMap;

/**
 * Factory Design pattern voor het maken van Features.
 */
public class FeatureFactory {

    /**
     * Factory method.
     * @param seqID     Het ID van het contig/chromosoom waarin de feature aanwezig is.
     * @param start     Start positie van de feature op het chromosoom/contig
     * @param end       Stop positie van de feature op het chromosoom/contig
     * @param score     De score van de feature.
     * @param strand    Of de feature aanwezig is in de strand (+) of complementaire strand (-)
     * @param phase     Het geeft het readingframe aan waarin het feature voorkomt (0,1,2).
     * @param attributen de Attributen als String die nog geparsed gaan worden in de factory.
     * @return een Feature Object corresponderend met featType.
     */
    public static Feature makeFeature(String featType, String seqID, String start, String end, String score, String strand, String phase, HashMap attributen) {

        switch (featType) {
            case "gene":
                return new Gene(seqID,featType, start, end, score, strand, phase, attributen);

            case "mRNA":
                return new mRNA(seqID,featType, start, end, score, strand, phase, attributen);

            case "exon":
                return new Exon(seqID,featType, start, end, score, strand, phase, attributen);

            case "CDS":
                return new CDS(seqID,featType, start, end, score, strand, phase, attributen);

            case "region":
                return new Region(seqID,featType, start, end, score, strand, phase, attributen);

        }

        return new Feature(seqID,featType, start, end, score, strand, phase, attributen); //Generiekste Feature mogelijk.
    }


}

