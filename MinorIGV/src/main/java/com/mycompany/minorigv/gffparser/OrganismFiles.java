package com.mycompany.minorigv.gffparser;

import com.mycompany.minorigv.FastaFileReader;
import com.mycompany.minorigv.gui.Context;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Files ophalen en unzippen als het nodig is.
 * @author Amber Janssen Groesbeek en Anne van Ewijk
 */
public class OrganismFiles {

    private String fnaFile, gffFile, gzFNA, gzGFF, pathNewFNA, pathNewGFF;

    /**
     * Het ophalen van de files die overeenkomen met het organismen dat de gebruiker gekozen heeft. Hieruit worden
     * de .fna en .gff files opgehaald.
     * @param pathNAS       Het pad naar de NAS toe waar alle bestanden van de organismen zijn opgeslagen.
     * @param nameOrg       De naam van het gekozen organismen.
     * @throws Exception
     */
    public void getFiles(String pathNAS, String nameOrg) throws Exception {
        String pathDirectory = pathNAS + nameOrg + File.separator;
        File f = new File(pathDirectory);
        String[] directories = f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                // De correcte gff en fna files uit de directory ophalen.
                if(!name.contains("from") && !name.contains(".txt")){
                    if(name.contains("fna")){
                        fnaFile = name;
                    }else if(name.contains("gff")){
                        gffFile = name;
                    }
                }
                return new File(dir, name).isDirectory();
            }
        });

        // Het pad naar het gezipte mapje waarin de fna file staat.
        gzFNA = pathDirectory + fnaFile;

        // Unzipte pad waar het fna file staat .
        pathNewFNA = pathDirectory + fnaFile.replace(".gz", "");

        // Gezipte pad naar het mapje waar het gff file staat
        gzGFF = pathDirectory + gffFile;

        // Unzipte pad naar de gff file.
        pathNewGFF = pathDirectory + gffFile.replace(".gz", "");

        // Controleren of het mapje nog unzipt moet worden.
        if(gzFNA.contains(".gz")){
            unzip(gzFNA, pathNewFNA);
        }if(gzGFF.contains(".gz")){
            unzip(gzGFF, pathNewGFF);
        }
    }

    /**
     * Het unzippen van de mappen.
     * @param pathMap       Path naar de map die unzipt moet worden
     * @param pathNewFile   Path naar het unzipte file
     */
    private void unzip(String pathMap, String pathNewFile){
        try {
            FileInputStream fis = new FileInputStream(pathMap);
            GZIPInputStream gzis = new GZIPInputStream(fis);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(pathNewFile);

            while ((length = gzis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fos.close();
            gzis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deleten van de gezipte mapjes.
        File deleteFile = new File(pathMap);
        deleteFile.delete();

    }

    /**
     * @return  Path naar de unzipte fna file
     */
    public String getFNAPath(){
        return pathNewFNA;
    }

    /**
     * @return Path naar de unzipte gff file
     */
    public String getGFFPath(){
        return pathNewGFF;
    }

}
