package com.mycompany.minorigv.gui;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class IGVPanel extends JPanel implements PropertyChangeListener {

    protected Context cont;

    /**
     * instellen context
     * @param cont de huidige context
     */
    public void setContext(Context cont) {
        this.cont = cont;
    }

    /**
     * functie voor het instellen van listeners.
     */
    public abstract void setListeners();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.invalidate();
        this.repaint();
    }

}