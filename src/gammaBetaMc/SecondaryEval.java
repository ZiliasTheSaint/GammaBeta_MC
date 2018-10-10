package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import danfulea.math.Convertor;
import danfulea.utils.FrameUtilities;


/**
 * Perform some custom application, i.e. the radiological program for evaluating 
 * the secondary X-Rays emerged from patient when they are undergoing radiographic exams. 
 * These kind of radiations is of concern for medical staff.
 * 
 * @author Dan Fulea, 11 Jul. 2005  
 */
@SuppressWarnings("serial")
public class SecondaryEval extends JFrame {

	private GammaBetaFrame mf;

	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	private Color fundal = new Color(144,37,38,255);//new
	// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(910, 500);

	protected JTextField massTf = new JTextField(5);
	protected JTextField distanceTf = new JTextField(5);
	protected JButton setB = new JButton();
	protected JButton resetB = new JButton();

	/**
	 * Constructor
	 * @param mf GammaBetaFrame object
	 */
	public SecondaryEval(GammaBetaFrame mf) {
		super("Secondary X-Ray Evaluation");
		this.mf = mf;
		fundal=GammaBetaFrame.bkgColor;
		createGUI();
		initEvent();// main event

		setDefaultLookAndFeelDecorated(true);
		FrameUtilities.createImageIcon(mf.resources.getString("form.icon.url"),
				this);

		FrameUtilities.centerFrameOnScreen(this);

		setVisible(true);
		mf.setEnabled(false);
		//mf.setVisible(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// force attemptExit to be called always!
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				attemptExit();

			}
		});
	}

	/**
	 * Exit method
	 */
	private void attemptExit() {

		mf.setEnabled(true);
		//mf.setVisible(true);
		dispose();
	}

	/**
	 * Setting up the frame size.
	 */
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void createGUI() {
		JPanel content = new JPanel(new BorderLayout());

		JPanel tabs = createPanel();
		content.add(tabs, BorderLayout.CENTER);

		setContentPane(new JScrollPane(content));
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	@SuppressWarnings("unused")
	 /**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	private TitledBorder getGroupBoxBorder(String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(LINE_BORDER, title);
		tb.setTitleColor(GammaBetaFrame.foreColor);//Color.white);
		Font fnt = tb.getTitleFont();
		Font f = fnt.deriveFont(Font.BOLD);
		tb.setTitleFont(f);
		return tb;
	}

	/**
	   * Create main panel
	   * @return the result
	   */
	private JPanel createPanel() {
		massTf.setOpaque(true);
		distanceTf.setOpaque(true);
		setB.setOpaque(true);

		setB.setText("Set detector");
		Character mnemonic;
		mnemonic = new Character('S');
		setB.setMnemonic(mnemonic.charValue());

		resetB.setText("Reset to default computation mode");
		mnemonic = new Character('R');
		resetB.setMnemonic(mnemonic.charValue());
		resetB.setToolTipText("Incident radiation is colimated to outer cylinder radius!");

		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel(
				"This custom detector is intended to be used for secondary radiation evaluation emerged from patients. ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);//Color.white);
		
		d1P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d11P=new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label=new JLabel("Scatter Fraction is the recommended type of calculation, IFULL!");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);//Color.white);		
		d11P.setBackground(GammaBetaFrame.bkgColor);

		
		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(
				"It must be used ONLY in conjunction with point Source and an external valid X-Ray spectrum. ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.setBackground(fundal);
		
		JPanel d22P=new JPanel();
		d22P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label=new JLabel("Set material as Soft tissue for main detector and for the Monture, and AIR for the rest!");
		d22P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);//Color.white);		
		d22P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(
				"In this approach the incident photons are colimated to patient entrance, hence to inner cylinder radius!");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.setBackground(fundal);

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(" Patient mass [kg] = ");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(massTf);
		label = new JLabel(" Evaluation at distance [cm]= ");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(distanceTf);
		d4P.setBackground(fundal);

		massTf.setText("70");
		distanceTf.setText("100");

		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d5P.add(setB);
		d5P.add(resetB);
		d5P.setBackground(fundal);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d1P, null);detP.add(d11P, null);
		detP.add(d2P, null);detP.add(d22P, null);
		detP.add(d3P, null);
		detP.add(d4P, null);
		detP.add(d5P, null);
		detP.setBackground(fundal);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(detP), BorderLayout.CENTER);
		main2P.setBackground(fundal);
		return main2P;// detP;
	}

	// END GUI-----------------------------------------------------------------
	/**
	 * All actions are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == setB) {
					setDetector();
				}

				if (evt.getSource() == resetB) {
					restoreDefaultCalculationMode();
				}

			}
		};
		setB.addActionListener(al);
		resetB.addActionListener(al);
	}

	// #################################
	/**
	 * Set custom detector
	 */
	private void setDetector() {
		boolean neg = false;
		double mass = 0.0;
		double dist = 0.0;

		try {
			mass = Convertor.stringToDouble(massTf.getText());// 6.3;
			if (mass <= 0)
				neg = true;
			dist = Convertor.stringToDouble(distanceTf.getText());// 7.3;
			if (dist <= 0)
				neg = true;
		} catch (Exception ex) {
			String title = "Input error";
			String message = "Insert real numbers!";
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (neg) {
			String title = "Input error";
			String message = "Insert positive numbers!";
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		double dens = 1.04;// softTissue->g/cm3
		mass = mass * 1000.0;// ->g

		double dim = Math.pow(4. * mass / (Math.PI * dens), 1. / 3.);// right
																		// cyl
																		// dim
																		// diam=thickness

		double eth = 2. * dist / 100.;// 2% from dist
		double hup = 0.1 * dist / 100.;

		NumberFormat nfe = NumberFormat.getInstance(Locale.US);
		nfe.setMinimumFractionDigits(3);// default e 2 oricum!!
		nfe.setMaximumFractionDigits(3);// default e 2 oricum!!
		nfe.setGroupingUsed(false);

		mf.althickTf.setText(nfe.format(eth));
		mf.adetTf.setText(nfe.format(dim));
		mf.hdetTf.setText(nfe.format(dim));
		mf.adetTotTf.setText(nfe.format(dim + 2. * eth + 2. * dist));
		mf.hdetTotTf.setText(nfe.format(dim + 2. * eth + 8.0 + hup));// 8Cm
																		// bellow->default
		mf.hUpTf.setText(nfe.format(hup + eth));

		GammaBetaFrame.noSup = true;
		//mf.setVisible(true);
		mf.setEnabled(true);
		dispose();
	}

	/**
	 * Restore default calculation mode. Use this after you are done with secondary X-Ray evaluation. 
	 */
	private void restoreDefaultCalculationMode() {
		GammaBetaFrame.noSup = false;
		//mf.setVisible(true);
		mf.setEnabled(true);
		dispose();
	}

}
