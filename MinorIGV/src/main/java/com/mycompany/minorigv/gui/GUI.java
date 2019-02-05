package com.mycompany.minorigv.gui;

import com.mycompany.minorigv.sequence.Strand;

import javax.swing.*;
import java.awt.*;


/**
 * Class waarin de Main staat waarmee de GUI geinitialiseerd wordt en verschillende objecten worden toegevoegt.
 * @author kahuub
 * Date: 19/11/18
 */
public class GUI {

	private JFrame frame;
	private Context context;
	public OrganismPanel organism;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 710);
		frame.setMinimumSize(new Dimension(900,710));
		frame.setBounds(100, 100, 760, 480);
		frame.setMinimumSize(new Dimension(760,480));

        context = new Context(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);

		IGVMenuBar igvMenuBar = new IGVMenuBar(context);

		GridBagConstraints gbcPanel = new GridBagConstraints();
		//gbcPanel.insets = new Insets(0, 0, 0, 0);
		gbcPanel.fill = GridBagConstraints.BOTH;
		gbcPanel.gridx = 0;
		gbcPanel.gridy = 0;
		frame.getContentPane().add(igvMenuBar,gbcPanel );

		GenomePanel genomePanel = new GenomePanel(context);
		
		GridBagConstraints gbcGenome = new GridBagConstraints();
		gbcGenome.fill = GridBagConstraints.HORIZONTAL;
		gbcGenome.gridx = 0;
		gbcGenome.gridy = 1;

		frame.getContentPane().add(genomePanel,gbcGenome);

        GridBagConstraints gbcOrganism = new GridBagConstraints();
        //gbcPanel.insets = new Insets(0, 0, 0, 0);
        gbcOrganism.fill = GridBagConstraints.BOTH;
        gbcOrganism.gridx = 0;
        gbcOrganism.gridy = 2;
        gbcOrganism.weighty = 1.0;

        organism = new OrganismPanel(context);

		organism.init();

		frame.getContentPane().add(organism,gbcOrganism);

		RulerPanel ruler = new RulerPanel(context);
		organism.add(ruler);

		FeaturePanel featuresForward = new FeaturePanel(context,true);
		organism.add(featuresForward);

		CodonPanel forwardPanel = new CodonPanel(context,Strand.POSITIVE);

		organism.add(forwardPanel);

		ReferencePanel refPanel1 = new ReferencePanel(context);
		organism.add(refPanel1);

		CodonPanel reversePanel = new CodonPanel(context,Strand.NEGATIVE);
		organism.add(reversePanel);

		FeaturePanel featuresReverse = new FeaturePanel(context,false);
		organism.add(featuresReverse);

		ScrollBar scroller = new ScrollBar(JScrollBar.HORIZONTAL,0,100,0,100);

		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new BoxLayout(scrollPanel,BoxLayout.PAGE_AXIS));
        scrollPanel.setBackground(Color.pink);
		scrollPanel.add(scroller);

        GridBagConstraints gbcScrollPanel = new GridBagConstraints();
        gbcScrollPanel.fill = GridBagConstraints.BOTH;
        gbcScrollPanel.gridx = 0;
        gbcScrollPanel.gridy = 3;

        frame.getContentPane().add(scrollPanel, gbcScrollPanel);

		scroller.setContext(context);
		scroller.setListeners();
		scroller.init();

    }
}