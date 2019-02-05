package com.mycompany.minorigv.blast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Huub
 * Class voor het aanroepen van BLAST via de commandline.
 */
public class BLAST {

    private static ProcessBuilder pb;
    /**
     * Runnen van de BLAST
     *
     * @param input         Path waar de input file staat
     * @param output        Path naar de output file.
     * @param blastType     Welke blast er uitgevoerd moet worden (blastp, blastn, etc.)
     * @param database      De database waartegen geblast moet worden.
     * @throws IOException
     */
    public static void runBLAST(String input, String output, String blastType, String database, String sortBlast) throws IOException {
        try
        {
            if(sortBlast.equals("NCBI")){
                pb = blastCommandNCBI(blastType, database, input, output);
            }else{
                pb = blastCommandDiamond(blastType, database, input, output);
            }


            // Checken of de huidige instellingen al een keer gebruikt zijn, zo niet: blasten.
            if(new File(output).exists() == false){
                Process proc = pb.start();      // Starten van de blast
                if(proc.waitFor()!=0) throw new RuntimeException("error occured");
            }

            parseXML(output);

        }
        catch(Exception err)
        {
            new File(output).delete();
            err.printStackTrace();
            throw new RuntimeException(err);
        }
        finally
        {

        }
    }

    /**
     * Parsen van de output file (XML) van de blast.
     *
     * @param output        Het path naar de gegenereerde output (XML) file.
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws FileNotFoundException
     */
    public static BlastOutput parseXML(String output) throws JAXBException, SAXException, ParserConfigurationException, FileNotFoundException {

        SAXSource source;
        Unmarshaller u;
        JAXBContext jc = JAXBContext.newInstance("com.mycompany.minorigv.blast");

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        spf.setFeature("http://xml.org/sax/features/validation", false);

        XMLReader xmlReader = spf.newSAXParser().getXMLReader();
        InputSource inputSource = new InputSource(
                new FileReader(output));
        source = new SAXSource(xmlReader, inputSource);

        u = jc.createUnmarshaller();
        BlastOutput bo = (BlastOutput) u.unmarshal(source);
        return bo;
    }

    /**
     * Command line argument voor het uitvoeren van blast via NCBI.
     * @param input         Path waar de input file staat
     * @param output        Path naar de output file.
     * @param blastType     Welke blast er uitgevoerd moet worden (blastp, blastn, etc.)
     * @param database      De database waartegen geblast moet worden.
     * @return
     */
    public static ProcessBuilder blastCommandNCBI(String blastType, String database, String input, String output){
        pb = new ProcessBuilder(
                blastType,
                "-db",database,              //database
                "-query",input,              //input
                "-evalue", "1e-5",           //Cutoff e-value
                "-num_threads","8",          //Hoeveelheid threads
                "-outfmt", "5",              //soort output XML
                "-max_target_seqs", "1",     //Aantal hits per ORF
                "-out",output                //output
        );
        return pb;
    }

    /**
     * Command line argument voor het uitvoeren van blast via Diamond.
     * @param input         Path waar de input file staat
     * @param output        Path naar de output file.
     * @param blastType    Welke blast er uitgevoerd moet worden (blastp, blastn, etc.)
     * @param database      De database waartegen geblast moet worden.
     * @return
     */
    public static ProcessBuilder blastCommandDiamond(String blastType, String database, String input, String output){
        pb = new ProcessBuilder(
                "diamond",blastType,
                "-d",database,              //database
                "-q",input,                 //input
                "-e", "1e-5",               //Cutoff e-value
                "-p","8",                   //Hoeveelheid threads
                "-f", "5",                  //soort output XML
                "-k", "1",                  //Aantal hits per ORF
                "-o",output                 //output
        );
        return pb;
    }

    /**
     * Functie voor het maken van een blastbare nucl database.
     * @param input input path
     * @param output output locatie voor de db
     */
    public static void makeBlastDB(String input, String output) {

        try //makeblastdb -in bakkersgist.fna -out bakkersgist -dbtype nucl

        {
            // command line argument voor het uitvoeren van blast.
            ProcessBuilder pb = new ProcessBuilder(
                    "makeblastdb",
                    "-in", input,
                    "-out", output,
                    "-dbtype", "nucl"
            );

            // Checken of de huidige instellingen al een keer gebruikt zijn, zo niet: blasten.
            Process proc = pb.start();      // Starten van de blast
            if (proc.waitFor() != 0) throw new RuntimeException("error occured");

        } catch (Exception err) {
            new File(output).delete();
            err.printStackTrace();
            throw new RuntimeException(err);
        }

    }

    /**
     * Functie voor het runnen van BLAST tegen een referentie met blastn in csv output simplistisch
     * @param input input file
     * @param output outputfile
     * @param soortBlast type blast (nucl)
     * @param database custom database
     * @throws IOException standaard IO exception.
     */
    public static void runBLASTAgainstReference(String input, String output, String soortBlast, String database) throws IOException {
        try {
            // command line argument voor het uitvoeren van blast. //"'10 qacc sacc sstart send'"

            String[] args = {

                    soortBlast,
                    "-db", database,
                    "-num_threads", "8",
                    "-query", input,
                    "-evalue", "1e-5",
                    "-outfmt","10 qacc sacc sstart send",
                    "-max_target_seqs", "1",
                    "-out", output,
                    "-max_hsps" ,"1"

            };

            ProcessBuilder pb = new ProcessBuilder(
                    args
            );



            // Checken of de huidige instellingen al een keer gebruikt zijn, zo niet: blasten.
            if (new File(output).exists() == false) {
                Process proc = pb.start();      // Starten van de blast
                if (proc.waitFor() != 0) throw new RuntimeException("error occured");
            }


        } catch (Exception err) {
            new File(output).delete();
            err.printStackTrace();
            throw new RuntimeException(err);
        } finally {

        }
    }



}
