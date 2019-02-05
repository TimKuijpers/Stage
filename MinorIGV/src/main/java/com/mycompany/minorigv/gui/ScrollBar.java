package com.mycompany.minorigv.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author kahuub
 * Class voor het scrollen van applicatie.
 */
public class ScrollBar extends JScrollBar implements PropertyChangeListener {

    private Context cont;
    private AdjustmentListener listener;
    private boolean semafoor;

    /**
     * Constructor voor Scrollbar.
     * @param orientation richting van de scrollbar
     * @param value huidige positie
     * @param extent breedte
     * @param min minimum
     * @param max maximum
     */
    public ScrollBar(int orientation, int value, int extent, int min, int max){
        super(orientation, value, extent, min, max);
        semafoor = false;
    }


    /**
     * initialisatie van de scrollbar.
     */
    public void init(){
        this.setPreferredSize(new Dimension(10000,20));

    }


    /**
     * instellen listeners.
     */
    public void setListeners(){
        cont.addPropertyChangeListener("range",this);

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (cont == null || cont.getCurChromosome() == null || cont.getSequentie() == null)return ; //als dingen niet kloppen, stop.
        semafoor = true;
        this.setMaximum(cont.getFullLenght()-1); //rechterlimiet
        this.setValue(cont.getStart()); //value is de nucleotide links.
        this.setVisibleAmount(cont.getLength()); //zichtbaar is de lengte van de subseq

        int unitIncrement = (int) Math.ceil((double)cont.getLength() / 10); //10% naar rechts
        this.setUnitIncrement(unitIncrement); //klikken op pijltje
        this.setBlockIncrement(cont.getLength()); //klikken op balk //100%
        semafoor = false;

    }

    /**
     * instellen van de context en toevoegen listener voor scrollen.
     * @param context
     */
    public void setContext(Context context) {
        cont = context;
        listener = new MyAdjustmentListener();
        this.addAdjustmentListener(listener);
    }

    /**
     * Adjustmentlistener class voor het scrollen de GUI laten aanpassen.
     * //TODO luistert naar zichzelf en negeert het.
     */
    class MyAdjustmentListener implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent evt) {
            if (semafoor){
                return;
            }
            Adjustable source = evt.getAdjustable();
            if (evt.getValueIsAdjusting()) {
                //return; hier valt in te stellen dat hij niet reageert als hij beweegt tijdens scrollen.
            }
            int type = evt.getAdjustmentType(); //soort scroll beweging, maar niet interesant.

            int value = evt.getValue();
            int length = cont.getLength();
            int start = cont.getStart();

            if(start == value || start+length > cont.getFullLenght()) return;  //checkt of hij onverandert is en stopt anders.

            cont.changeSize(value,value+length);
        }
    }


}
