package com.mycompany.minorigv.gffparser;
import java.util.HashMap;

/**
 * Het object Region maken waarbij de superclass Feature wordt overgeërfd. Van elke regel in het bestand
 * waarin Region in de derde kolom staat wordt een object gemaakt.
 *
 * @author Anne van Ewijk en Amber Janssen Groesbeek
 *
 */
public class Region extends Feature{
    private HashMap attributes;

    /**
     * Het opslaan van alle informatie van de regio in een object. De regio bevat informatie over het
     * chromosoom zelf.
     * Constructor
     *
     * @param seqID         Het ID van het contig/chromosoom.
     * @param theType       Het type zoals vermeld in het bestand.
     * @param start         Start positie van het contig/chromosoom
     * @param end           Stop positie van het contig/chromosoom
     * @param score         Score van het Region
     * @param strand        Of het contig/chromosoom aanwezig is in de strand (+) of complementaire strand (-)
     * @param phase         Het geeft het reading frame aan waarin de Region voorkomt (0,1,2 of ".")
     * @param attributes    HashMap met daarin de informatie over een Region.
     */
    Region(String seqID,String theType, String start, String end, String score, String strand, String phase, HashMap attributes) {
        super(seqID,theType, start, end, score, strand, phase, attributes);
        this.attributes = attributes;
    }

    /**
     * Ophalen van de attributen/informatie van een Region.
     *
     * @return     HashMap met als key de omschrijving van de informatie (bijv. name) en
     *             als value de specifieke informatie van een Region (bijv. PAU8).
     */
    public HashMap getAttributes() {
        return attributes;
    }

    /**
     *
     * @param attributes    Het maken van de HashMap met daarin de informatie: als key de omschrijving van de informatie (bijv. name)
     *                      en als value de specifieke informatie van een Region (bijv. PAU8).
     */
    public void setAttributes(HashMap attributes) {
        this.attributes = attributes;
    }


    @Override
    public String getName() {
        return "Region";
    }

    @Override
    public String getClassName() {
        return "Region";
    }


}
