package com.mycompany.minorigv.gui;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Container class voor alle Panelen die behoren bij een gekozen organism,
 * ReferencePanel, RulerPanel, FeaturePanel, GenomePanel, CodonPanel.
 * @author kahuub
 * Date 20/11/18
 */
public class OrganismPanel extends JPanel implements PropertyChangeListener{

    Context cont;

    public OrganismPanel(Context context) {
        super();
        this.setContext(context);
        this.setListeners();
        this.init();
    }


    public void init() {
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.GREEN);
	}

    public void setContext(Context conti){
	    cont = conti;

    }

    public void setListeners(){
        cont.addPropertyChangeListener("range",this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


}