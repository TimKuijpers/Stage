package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.gffparser.*;
import com.mycompany.minorigv.sequence.Strand;

import java.awt.*;
import java.util.*;

/**
 * Coordinaten bepalen van elke feature, rekening gehouden met overlappende features.
 *
 * @author Huub en Amber Janssen Groesbeek
 */
public class CoordinatesFeatures {
    FeaturePanel draw;

    /**
     * Aangeven dat draw het feature panel is in FeaturePanel bestand.
     * @param featurePanel      De panel waarop getekent gaat worden.
     */
    public CoordinatesFeatures(FeaturePanel featurePanel) {
        draw = featurePanel;
    }

    /**
     * Aparte lijsten maken voor elke Feature.
     * @param featureFilteredList      Lijst met alle features door elkaar op basis van de keuze van de gebruiker.
     * @param g                        Graphic context
     */
    public void seperateFeatures(Feature[] featureFilteredList, Graphics g){
        // De coordinaten van de y as op de forward en reverse panel.
        // index 0: de y as coordinaat op reverse panel
        // index 1: de y as coordinaat op forward panel
        // index 2: de hoogste y as coordinaat op de forward panel
        // index 3: de hoogste y as coordinaat op de reverse panel
        ArrayList<Integer> newCoord = new ArrayList<>(Arrays.asList(4,4,0,0));

        // TODO : In het aanmaken van de featureFilteredList al een hashmap genereren.
        ArrayList<Feature> featureGenePos = new ArrayList<>();
        ArrayList<Feature> featureGeneNeg = new ArrayList<>();

        ArrayList<Feature> featuremRNAPos = new ArrayList<>();
        ArrayList<Feature> featuremRNANeg = new ArrayList<>();


        ArrayList<Feature> featureExonPos = new ArrayList<>();
        ArrayList<Feature> featureExonNeg = new ArrayList<>();

        ArrayList<Feature> featureCDSPos = new ArrayList<>();
        ArrayList<Feature> featureCDSNeg = new ArrayList<>();

        ArrayList<Feature> featureRegionPos = new ArrayList<>();
        ArrayList<Feature> featureRegionNeg = new ArrayList<>();

        // Elke feature in een aparte lijst zetten
        for(Feature feat : featureFilteredList) {
            if (feat instanceof Gene) {
                if(feat.getStrand().equals(Strand.POSITIVE)){
                    featureGenePos.add(feat);
                }else if(feat.getStrand().equals(Strand.NEGATIVE)){
                    featureGeneNeg.add(feat);
                }else{
                    //pass
                }
            } else if (feat instanceof mRNA) {
                if(feat.getStrand().equals(Strand.POSITIVE)){
                    featuremRNAPos.add(feat);
                }else if(feat.getStrand().equals(Strand.NEGATIVE)){
                    featuremRNANeg.add(feat);
                }else{
                    //pass
                }
            } else if (feat instanceof Exon){
                if(feat.getStrand().equals(Strand.POSITIVE)){
                    featureExonPos.add(feat);
                }else if(feat.getStrand().equals(Strand.NEGATIVE)){
                    featureExonNeg.add(feat);
                }else{
                    //pass
                }
            } else if (feat instanceof CDS){
                if(feat.getStrand().equals(Strand.POSITIVE)){
                    featureCDSPos.add(feat);
                }else if(feat.getStrand().equals(Strand.NEGATIVE)){
                    featureCDSNeg.add(feat);
                }else{
                    //pass
                }
            } else if (feat instanceof Region){
                if(feat.getStrand().equals(Strand.POSITIVE)){
                    featureRegionPos.add(feat);
                }else if(feat.getStrand().equals(Strand.NEGATIVE)){
                    featureRegionNeg.add(feat);
                }else{
                    //pass
                }
            } else {
                // voor volledigheid
            }
        }

        // TODO : Als er een hashmap is gemaakt, dan de if statements aanpassen.
        // Als de lijsten niet leeg zijn, dan worden de coordinaten bepaald voor het mappen.
        if(!featureGenePos.isEmpty()){
            newCoord = setCoordinates(featureGenePos, g, newCoord.get(0), newCoord.get(1),newCoord.get(3),newCoord.get(2), new Color(255, 135, 12));
        }if(!featureGeneNeg.isEmpty()){
            newCoord = setCoordinates(featureGeneNeg, g, newCoord.get(0), newCoord.get(1),newCoord.get(3),newCoord.get(2), new Color(255, 135, 12));
        }if(!featuremRNAPos.isEmpty()){
            newCoord = setCoordinates(featuremRNAPos, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2), new Color(255, 229, 57));
        }if(!featuremRNANeg.isEmpty()){
            newCoord = setCoordinates(featuremRNANeg, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2), new Color(255, 229, 57));
        }if(!featureExonPos.isEmpty()){
            newCoord = setCoordinates(featureExonPos, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2), new Color(255, 79, 35));
        }if(!featureExonNeg.isEmpty()){
            newCoord = setCoordinates(featureExonNeg, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2), new Color(255, 79, 35));
        }if(!featureCDSPos.isEmpty()){
            newCoord = setCoordinates(featureCDSPos, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2),  new Color(140, 183, 255));
        }if(!featureCDSNeg.isEmpty()){
            newCoord = setCoordinates(featureCDSNeg, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2),  new Color(140, 183, 255));
        }if(!featureRegionPos.isEmpty()){
            newCoord = setCoordinates(featureRegionPos, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2),  new Color(150, 238, 75));
        }if(!featureRegionNeg.isEmpty()){
            newCoord = setCoordinates(featureRegionNeg, g, newCoord.get(0), newCoord.get(1), newCoord.get(3), newCoord.get(2),  new Color(150, 238, 75));
        }
    }

    /**
     * Coordinaten van elke feature worden bepaald en meegegeven aan een methode die de features tekent.
     * @param oneFeature            Een lijst met daarin alle objecten van één feature, bijvoorbeeld alle objecten van de feature Genes.
     * @param g                     Graphics context
     * @param yCoodReverse          De y as coordinaat in de reverse panel
     * @param yCoodForward          De y as coordinaat in de forward panel
     * @param yCoodReverseMax       De hoogste y coordinaat van een getekende feature in de reverse panel
     * @param yCoodForwardMax       De hoogste y coordinaat van een getekende feature in de forward panel
     * @param col                   Kleur van een soort feature. Genes: Orange, mRNA: Blue.
     * @return  ArrayList met op index 1: yCoodReverse, index 2: yCoodForward, 3: yCoodForwardMax, 4: yCoodReverseMax.
     */
    public ArrayList<Integer> setCoordinates(ArrayList<Feature> oneFeature, Graphics g, int yCoodReverse, int yCoodForward, int yCoodReverseMax, int yCoodForwardMax, Color col){
        int latestStopReverse = 0;
        int latestStopForward = 0;

        int latestStartReverse = 0;
        int latestStartForward = 0;
        HashMap<Feature, Integer> listOvFt = new HashMap<>();
        Feature feat = null;
        ArrayList<Integer> newCoord = new ArrayList<>();

        // Voor elke feature in een lijst met dezelfde features (bijv. alleen genen) wordt er gekeken op welke strand het voorkomt en wordt getekend.
        for(Feature feature: oneFeature){
            if (feature.getStrand().equals(Strand.NEGATIVE)){
                // Ophalen van de coordinaten op de reverse strand van de Feature.
                yCoodReverse = getCoordinates(feature, latestStopReverse, listOvFt, yCoodReverse, feat, latestStartReverse);
                // Ophalen tags
                String tag = getTag(feature);

                // Tekenen van de feature op de reverse strand.
                feat = draw.drawFeatures(feature,g,tag, yCoodReverse, col);

                // Opslaan van de stop positie van de laaste feature om overlap te achterhalen.
                latestStopReverse = feat.getStop();
                latestStartReverse = feat.getStart();
                // Ophalen van de hoogste y-coordinaat voor het tekenen van andere Features (bijv. mRNA na genen)
                yCoodReverseMax = getMaxCoordinates(yCoodReverse, yCoodReverseMax);
            }else if(feature.getStrand().equals(Strand.POSITIVE)){
                // Ophalen van de coordinaten op de template strand van de Feature.
                yCoodForward = getCoordinates(feature, latestStopForward, listOvFt, yCoodForward, feat, latestStartForward);
                String tag = getTag(feature);

                feat = draw.drawFeatures(feature,g,tag, yCoodForward, col);

                latestStopForward = feat.getStop();
                latestStartForward = feat.getStart();
                yCoodForwardMax = getMaxCoordinates(yCoodForward, yCoodForwardMax);
            }
        }

        yCoodForward = yCoodForwardMax + 20;
        yCoodReverse = yCoodReverseMax + 20;

        // Returnen van de coordinaten waarna verder getekent moet worden.
        newCoord.add(yCoodReverse);
        newCoord.add(yCoodForward);
        newCoord.add(yCoodForwardMax);
        newCoord.add(yCoodReverseMax);

        return newCoord;
    }

    /**
     * Ophalen van de tags die in de features moeten komen te staan. Als de feature een gen is, wordt de locus_tag gevisualiseerd.
     * Als de feature een exon is, wordt het gene gevisualiseerd. Anders wordt de naam van de feature uit de attributes opgehaald.
     * @param feature   De feature waarvan de tag opgehaald moet worden
     * @return          Een string met daarin de tag voor in de feature.
     */
    private String getTag(Feature feature){
        // Als de feature een gen is wordt de locus tag als tag opgeslagen. Als de feature een exon is wordt het gene
        // als tag opgeslagen. Bij de andere features wordt de naam van de feature.
        if(feature instanceof Gene){
            return (String) feature.getAttributes().get("locus_tag");
        }else if (feature instanceof Gene){
            return "Gene";
        }else if (feature instanceof mRNA){
            return "mRNA";
        }else if (feature instanceof Exon){
            return "Exon";
        }else if (feature instanceof CDS){
            return "CDS";
        }else if (feature instanceof Region){
            return "Region";
        }else{
            return "?";
        }
    }

    /**
     * Bepalen of er overlap is met features. y as coordinaten bepalen.
     *
     * @param curFeature    Huidige feature
     * @param latestStop   De stop positie van de laatste feature
     * @param listOvFt    Lijst met features die mogelijk overlap veroorzaken
     * @param yCood        y coordinaat waarna verder gegaan  moet worden
     * @param lastFeature   Vorige feature.
     * @param latestStart
     * @return              Integer van de y as coordinaat.
     */
    private int getCoordinates(Feature curFeature, int latestStop, HashMap<Feature, Integer> listOvFt, int yCood, Feature lastFeature, int latestStart){

        // Bepalen of er overlap is van twee features: dan is de start van de huidige feature kleiner dan de stop van de laatste feature.
        if((curFeature.getStart() < latestStop && curFeature.getStop() > latestStop) ||
                (curFeature.getStart() > latestStart && curFeature.getStop() < latestStop) ||
                (curFeature.getStart() < latestStop && curFeature.getStop() > latestStop) ||
                (curFeature.getStop() > latestStart && curFeature.getStop() < latestStop)){
            // Als er overlap is wordt de vorige feature (object) opgeslagen in een hashmap met als value de y as coordinaat.
            listOvFt.put(lastFeature, yCood);
            // loopen over de overlappende features en achterhalen of er nog steeds overlapping plaatsvindt over die vorige features.
            for(Feature f:listOvFt.keySet()){
                if(curFeature.getStart() < f.getStop()){
                    yCood += (20/ listOvFt.size());
                }else{
                    yCood = listOvFt.get(f);
                }
            }
        }else{
            if(listOvFt.size() > 1){
                yCood = Collections.min(listOvFt.values());
            }else if (listOvFt.size() == 1){
                yCood = (int) listOvFt.values().toArray()[0];

            }else{
                //pass
            }
            listOvFt.clear();

        }
        return yCood;
    }

    /**
     * Bepalen van de hoogste waarde op de y as.
     *
     * @param yCood        Huidige y coordinaat
     * @param yCoodMax    Maximale y coordinaat die is getekent
     * @return              De hoogste y coordinaat
     */
    private int getMaxCoordinates(int yCood, int yCoodMax){
        // Bepalen van de hoogste waarde van de y-coordinaat, vanaf daar wordt de volgende Feature getekend.
        if(yCood > yCoodMax){
            return yCood;
        }else{
            return yCoodMax;
        }
    }
}