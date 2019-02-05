package com.mycompany.minorigv.blast;

import com.mycompany.minorigv.gffparser.BlastedORF;
import com.mycompany.minorigv.gffparser.Chromosome;
import com.mycompany.minorigv.gffparser.ORF;
import com.mycompany.minorigv.gffparser.Organisms;
import com.mycompany.minorigv.gui.Context;
import com.mycompany.minorigv.gui.Paths;
import com.mycompany.minorigv.sequence.Strand;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Het aanroepen van de blast en het parsen van de blast resultaten. Het BlastedORF object wordt hier aangemaakt.
 *
 * @author Anne van Ewijk en Amber Janssen Groesbeek
 */
public class BlastORF {

    Context cont;

    /**
     * Constructor
     */
    public BlastORF(){

    }

    /**
     * Constructor.
     * @param cont
     */
    public BlastORF(Context cont){
        this.cont = cont;
    }

    /**
     * Aanroepen van de blastp waarbij ORFs worden geblast tegen de NCBI NR database.
     *
     * @param orfList           Alle ORFs of de ORFs tussen twee waardes.
     * @param partOutputName    Stuk naam voor output bestand.
     * @throws IOException
     */
    public String callBlastNCBI(ArrayList<ORF> orfList, String partOutputName) throws IOException{
        cont.saveORFs(orfList, "blastORF");
        String output = nameXML(cont.getPath(Paths.OUTPUT_ORF), partOutputName, "NCBI");
        BLAST.runBLAST(nameInput(cont.getPath(Paths.SAVE_BLAST_ORF)), output, "blastp", cont.getPath(Paths.NR), "NCBI");
        return output;
    }

    /**
     * Aanroepen van de blastp waarbij ORFs worden geblast tegen de Diamond database.
     * @param orfList           Alle ORFs of de ORFs tussen twee waardes.
     * @param partOutputName    Stuk naam voor output bestand.
     * @return
     * @throws IOException
     */
    public String callBlastDiamond(ArrayList<ORF> orfList, String partOutputName) throws IOException{
        cont.saveORFs(orfList, "blastORF");
        String output = nameXML(cont.getPath(Paths.OUTPUT_ORF), partOutputName, "Diamond");
        BLAST.runBLAST(nameInput(cont.getPath(Paths.SAVE_BLAST_ORF)), output, "blastp", cont.getPath(Paths.DIAMOND_DB), "Diamond");
        return output;
    }



    /**
     * Aanmaken naam van XML file (output blast)
     * @param out               Path waar de output file komt te staan.
     * @param partOutputName    Onderdeel naam van het outputfile van de blast
     * @return
     */
    public String nameXML(String out , String partOutputName, String sortBlast){

        String[] nameFasta = cont.getNameFasta().split("////");
        String nameFile = nameFasta[nameFasta.length-1];
        String[] splitNameFile = nameFile.split("_");
        String codeFasta = splitNameFile[0] + "_"+ splitNameFile[1] + "_" + sortBlast;
        String output = out + codeFasta + partOutputName;
        return output;
    }

    /**
     * Path naar het input file maken.
     * @param input     Path naar het mapje waar de file instaat
     * @return
     */
    public String nameInput(String input){
        return input + "blastORF.fasta";
    }


    /**
     * Parsen van de blast resultaten.
     * @param bo    Object van de blast resultaten.
     */
    public void parseBlastResults(BlastOutput bo){

        Organisms org = cont.getOrganism();
        for(Chromosome chrom : org.getChromosomes().values()){              // Loopen over alle chromosomen
            ArrayList<ORF> orfList = new ArrayList<>(0);

            for (Iteration iter : bo.getBlastOutputIterations().getIteration()){        // Loopen over alle blastresultaten per chromosoom

                String chromName = iter.getIterationQueryDef().split("\\|")[5].split(":")[1];
                if(chromName.equals(chrom.getId())){
                    ORF orf = makeORF(iter);
                    orfList.add(orf);
                }

            }

            chrom.setListORF(orfList);
        }
    }

    /**
     * Het maken van het BlastedORF object en eruithalen van informatie van de blast.
     * @param iter
     * @return
     */
    public ORF makeORF(Iteration iter) {

        ORF orf = null;

        String header = iter.iterationQueryDef;
        String[] informationHeader = header.split("\\|");

        String id = (informationHeader[0]);
        int RF = Integer.parseInt(informationHeader[1].split(":")[1]);
        int startORF = Integer.parseInt(informationHeader[2].split(":")[1]);
        int stopORF = Integer.parseInt(informationHeader[3].split(":")[1]);
        String strandORF = informationHeader[4].split(":")[1];
        String chromName = informationHeader[5].split(":")[1];
        Strand strandORFenum = null;

        if (strandORF.equals("POSITIVE")) {
            strandORFenum = Strand.POSITIVE;
        } else if (strandORF.equals("NEGATIVE")) {
            strandORFenum = Strand.NEGATIVE;
        }

        // Maken van BlastedORF object.
        orf = new BlastedORF(startORF, stopORF, RF, id, strandORFenum, stopORF - startORF, chromName,iter);

        return orf;
    }
}