package com.mycompany.minorigv.gffparser;

import java.io.*;
import java.util.*;


/**
 * Deze class leest een bestand met genomische data in. Per regel wordt er gekeken of de data in die regel relevant is
 * aan de hand van de derde kolom. Vervolgens worden er van de relevante regels objecten gemaakt zodat de data wordt opgeslagen.
 *
 * @author Anne van Ewijk en Amber Janssen Groesbeek
 */
public class GffReader {

    /**
     * Het inlezen van het bestand per regel en het opslaan van data in de regels wanneer er in de derde kolom gene, mRNA,
     * exon, CDS of region staat.
     *
     * @param path Het pad waar het bestand staat.
     * @throws FileNotFoundException Afvangen van een error zodra het bestand niet gevonden kan worden.
     * @throws IOException           Als er een input of output exception plaatsvindt.
     */
    public static Organisms readData(Organisms org, String path) throws FileNotFoundException, IOException, Exception {
//        if (!(path.endsWith(".gff") || path.endsWith(".gtf") || path.endsWith(".gff3") || path.endsWith(".txt"))) {
//            throw new IOException("Niet ondersteund file type");
//        }
        BufferedReader reader = new BufferedReader(new FileReader(path));
        ArrayList<String> allContigs = new ArrayList<String>();

        String line;
        String ID = null;
        String IdOrganism;

        String[] columns;

        if (org == null) {
            org = new Organisms();
        }

        Chromosome chrom = null;
        // inlezen van het bestand per regel.
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("##sequence-region")) {
                String[] splited = line.split("\\s+");
                ID = splited[1];
                allContigs.add(ID);

                chrom = org.getChromosome(ID);
                if (chrom == null) {
                    chrom = new Chromosome();
                    chrom.setId(ID);
                    org.addChromosome(chrom);
                }

            } else if (line.startsWith("##species ")) {
                String[] idOrg = line.split("=");
                IdOrganism = idOrg[1];
                org.setId(IdOrganism);
                // Als de regel met ID begint is het mogelijk relevante data om op te slaan.
            } else if (line.startsWith("#")) {
            } else if (line.startsWith(ID)) {
                columns = line.split("\\t");

                // Class waarin de kolom met attributen wordt verwerkt.

                String featType = line.split("\\t")[2];
                String seqID = columns[1];
                String start = columns[3];
                String end = columns[4];
                String score = columns[5];
                String strand = columns[6];
                String phase = columns[7];
                HashMap attributen = attributes.splitAttributes(columns[8]);

                Feature feat = FeatureFactory.makeFeature(featType, seqID, start, end, score, strand, phase, attributen);
                // Als er in de regel in kolom 3 gene, mRNA, exon, CDS of region staat, bevat de regel relevante data dat wordt opgeslagen.

                chrom.addFeature(feat);
            }
        }
        return org;
    }
}