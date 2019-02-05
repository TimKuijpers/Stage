/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2007-2015 Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mycompany.minorigv.sequence;



import java.net.URL;
import java.util.*;
import java.io.*;


/**
 * @author jrobinso. Stan Wehkamp
 * singleton class die hulperfuncties aanroept om de CodonTables te bouwen
 * vertaald ook nucleotide sequenties naar aminzuur sequenties
 * neemt de codontabel van context
 * de codontabellen in context worden uit allCodonTables in deze class gehaald
 */
public class TranslationManager {
    private static final String[] BASE_SEQUENCES = {    "TTTTTTTTTTTTTTTTCCCCCCCCCCCCCCCCAAAAAAAAAAAAAAAAGGGGGGGGGGGGGGGG",      //hardcoded aangezien de codon volgorde altijd gelijk is in het dataformat voor codontabellen
                                                        "TTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGGTTTTCCCCAAAAGGGG",
                                                        "TCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAGTCAG"};
    public static final String TEST_AMINOVOLGORDE =     "FFLLSSSSYY**CC*WLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG";     // TEST_AMINOVOLGORDE, TEST_AMINOSTARTS worden in de toekomst uit een file gegenereed
    public static final String TEST_AMINOSTARTS =       "---M---------------M---------------M----------------------------";

    static final String DEFAULT_CODON_TABLE_PATH = "CodonTabels.txt";

    private LinkedHashMap<Integer, CodonTable> allCodonTables = new LinkedHashMap<Integer, CodonTable>();

    private static TranslationManager instance;

    private TranslationManager(){    }                  //leave this here

    /**
     * bouwt de instance van de singelton class
     * start ook de generatie van de codontabellen
     * @return TranslationManeger instance
     */
    public static TranslationManager getInstance() {
        if (instance == null) {
            try {
                TranslationManager newInstance = new TranslationManager();
                newInstance.loadCodonTabels(DEFAULT_CODON_TABLE_PATH);
                instance = newInstance;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
      return instance;
    }

    /**
     * laad bestand out de resource map en parses de inhoud naar CodonTable.build.
     * die returnt de codonTable terug naar deze methode die het in de hashmap allCodonTables zet.
     * @param codonTablesPath
     * @throws FileNotFoundException
     * @throws IOException
     */
	private void loadCodonTabels(String codonTablesPath) throws FileNotFoundException, IOException {
        
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream(DEFAULT_CODON_TABLE_PATH);

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
        String line = fileReader.readLine();
        while (line != null) {
            // huidige line bevat de opende brace
            line = fileReader.readLine();

            //huidige line bevat de alle namen van de tabel
            String[] tableNames = line.substring(line.lastIndexOf("[")+1, line.lastIndexOf("]")).replaceAll("\"", "").split(",");
            line = fileReader.readLine();

            //huidige line bevat het tabel id
            Integer key = Integer.valueOf(line.substring(line.lastIndexOf(":") + 1, line.length() -1 ));
            line = fileReader.readLine();

            //huidige line bevat aminozuren in een specifieke volgorde om alle mogelijke codons te representeren
            String aminoNormal = line.substring(line.lastIndexOf(":" )+2, line.length() -2);
            line = fileReader.readLine();

            //huidige line bevat alternative aminozuren in een specifieke volgorde om alle mogelijke codons te representeren
            String aminoStarts = line.substring(line.lastIndexOf(":" )+2, line.length() -1);
            line = fileReader.readLine();

            //huidige line bevat de sluitende brace
            line = fileReader.readLine();

            CodonTable curTable = CodonTable.build(key, tableNames, BASE_SEQUENCES, aminoNormal, aminoStarts);
            allCodonTables.put(curTable.getKey(), curTable);
        }
    }

    /**
     * getter voor 1 codontable uit allCodonTables
     * gebruikt een Integer als key
     * @param key
     * @return Codontable
     */
    public CodonTable getCodonTable (Integer key){
        return allCodonTables.get(key);
    }

    /**
     * getter voor alle codontabellen uit allCodonTables
     * @return collection van allCodonTables
     */
    public Collection<CodonTable> getAllCodonTables() {
        return Collections.unmodifiableCollection(allCodonTables.values());
    }

     /**
     * retuns een string van aminozuren gegenereed van een nucleotide sequentie en een codontabel. 
     * 
     * @param direction de enum strand zort ervoor dat de sequentie word gebruikt zoal het is of gereversed word waneer enum NEGATIVE is
     * @param sequence string de een nucleotide sequentie voorsteld
     * @param curTable object CodonTable
     * @return string waar elk karakter een aminozuur voorsteld
     **/
    public String getAminoAcids(Strand direction, String sequence, CodonTable curTable) {
        // sequentie moet deelbaar zijn door 3
        // het aanroepende programma is verandwoordelijk om een uitgelijnde sequentie mee te geven.
        int readLength = sequence.length()/3;
        List<String> acids = new ArrayList<String>(readLength);
        
        if (direction == Strand.NEGATIVE) {
            sequence = MakeCompStrand.getReverseComplement(sequence);
        }
        
        for (int i = 0; i <= sequence.length() - 3; i += 3) {
            String codon = sequence.substring(i, i + 3).toUpperCase();
            String aa = toAA(codon, curTable);
            acids.add(aa);
        }

        //if (direction == Strand.NEGATIVE) {     // aminozuur sequentie word omgedraaid voor visualisatie doeleinden
        //    Collections.reverse(acids);         // de output is dus achterstevoren met de volgorde waarin ze in een biologische omgeving getransleerd zouden worden
        //}

        String listString = String.join("", acids);     // veranderd de arraylist in een string
        return listString;
    }
    
    
/**
 * zet een codon om naar een string aminozuur
 * @param codon string bestaand uit 3 karakters. dient als key voor CodonMap
 * @param table object CodonTable bevat de CodonMap.
 * @return string van 1 karakter dat een aminozuur voorsteld
 */
    public static String toAA(String codon, CodonTable table) {
    	return table.getCodonMap().get(codon);
    }
}
