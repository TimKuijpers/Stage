package com.mycompany.minorigv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * FastaFileWriter waarin een HashMap gestopt wordt van Headers -> sequenties.
 * @author kahuub
 */
public class FastaFileWriter {

    /**
     * Write functie.
     * @param fastaMap Map met hierin headers->sequences.
     * @param outputFilePath Path voor de output.
     * @throws IOException Input output error.
     */
    public static void writeFasta(Map<String,String> fastaMap, String outputFilePath) throws IOException {

        File file = new File(outputFilePath);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        for (String header : fastaMap.keySet()){

            String header2 = header;
            if (!header.startsWith(">")){
                header2 = ">" + header;
            }

            bw.write(header2);
            bw.write(System.lineSeparator());

            String seq = fastaMap.get(header);
            int lines = seq.length()/80;

            int i;
            for(i = 0; i < lines; i++){
                bw.write(seq.substring(i*80,(i+1)*80));
                bw.write(System.lineSeparator());
            }
            bw.write(seq.substring((i+1)*80) );
            bw.write(System.lineSeparator());

        }
    }
}