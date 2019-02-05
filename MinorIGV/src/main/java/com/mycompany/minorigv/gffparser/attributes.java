package com.mycompany.minorigv.gffparser;
import java.util.HashMap;
import java.util.Map;

/**
 * Kolom 8 van het ingeladen bestand bevat verdere informatie over het element. Deze informatie wordt in een hashmap gezet
 * en zo opgeslagen in het object. Hierdoor kan deze informatie altijd opgehaald worden. Per regel van het bestand wordt deze
 * class aangeroepen.
 *
 *  @author Anne van Ewijk en Amber Janssen Groesbeek
 */
public class attributes {


    /**
     * De kolom 8 (attributen) wordt gesplit waarna de informatie wordt opgeslagen in een hashmap.
     * Deze hashmap wordt gereturned zodat deze informatie ook opgeslagen kan worden in het object.
     *
     * @param attr     Kolom 8 uit de ingeladen bestanden die informatie van element bevat.
     * @return         Er wordt een hashmap gereturned verdere informatie over het element.
     */
    public static HashMap splitAttributes(String attr){
        Map<String, Object> attribute = new HashMap<String, Object>();
        // Alle informatie in kolom 8 is gescheiden doormiddel van ;.
        String[] subAttr = attr.split(";");

        // Er wordt een hashmap gemaakt van de informatie van het element.
        for(String s: subAttr){
            String[] splitString = s.split("=");
            attribute.put(splitString[0], splitString[1]);
        }

        // Als de hashmap een key heeft Dbxref, word er gekeken of er een "," aanwezig is.
        if (attribute.containsKey("Dbxref")){
            Map<String, String> dbxrefIDs = new HashMap<String, String>();
            String line = (String) attribute.get("Dbxref");

            // Een "," geef aan dat er meerdere IDs zijn. Er wordt een nieuwe hashmap gemaakt met deze IDs.
            if(line.contains(",")){
                String[] splitIDs = line.split(",");
                for(String ID: splitIDs){
                    String[] IdKeyValue = ID.split(":");
                    dbxrefIDs.put(IdKeyValue[0], IdKeyValue[1]);
                }
            }else{
                String[] idKeyValue = line.split(":");
                dbxrefIDs.put(idKeyValue[0], idKeyValue[1]);
            }
            attribute.put("Dbxref", dbxrefIDs);
        }
        return (HashMap) attribute;
    }
}
