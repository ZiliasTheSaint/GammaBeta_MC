package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * Auxiliary class for G_app. It handles GUI controls. 
 * 
 * @author Dan Fulea, 28 MAR. 2007
 */
public class gGUI {
	private G_app ds;
	//private static final Dimension sizeCb = new Dimension(50, 21);
	private static final Dimension sizeTxtCb = new Dimension(100, 21);
	private static final Dimension sizeTxtCb2 = new Dimension(200, 21);
	private static final Dimension sizeLst = new Dimension(253, 125);
	 private static final Dimension textAreaDimension = new Dimension(900, 400);
	private JScrollPane listSp = new JScrollPane();

	// Construct the frame
	/**
	 * Constructor
	 * @param ds G_app object
	 */
	public gGUI(G_app ds) {
		this.ds = ds;
		initComponents();
		// opacity();
	}

	// Component initialization
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Initialize components.
	 */
	private void initComponents() {
		Character mnemonic;

		ds.startsimB.setText("Run");
		mnemonic = new Character('R');
		ds.startsimB.setMnemonic(mnemonic.charValue());

		ds.printB.setText("PDF Print result");
	    ds.printB.setMnemonic(new Character('P'));
	    
		ds.stopsimB.setText("Kill");
		mnemonic = new Character('K');
		ds.stopsimB.setMnemonic(mnemonic.charValue());

		ds.loadSpectrumB.setText("Load spectrum...");
		mnemonic = new Character('s');
		ds.loadSpectrumB.setMnemonic(mnemonic.charValue());

		ds.loadMediaB.setText("Load media...");
		mnemonic = new Character('m');
		ds.loadMediaB.setMnemonic(mnemonic.charValue());

		ds.deleteB.setText("Delete");
		mnemonic = new Character('D');
		ds.deleteB.setMnemonic(mnemonic.charValue());

		ds.addB.setText("Add");
		mnemonic = new Character('A');
		ds.addB.setMnemonic(mnemonic.charValue());

		String[] elems = { "100", "1000", "10000", "20000", "50000", "100000",
				"500000", "1000000" };// ncase
		ds.ncaseCb = new JComboBox(elems);
		String s = elems[1];// 1000
		ds.ncaseCb.setSelectedItem((Object) s);
		ds.ncaseCb.setMaximumRowCount(5);
		ds.ncaseCb.setPreferredSize(sizeTxtCb);

		s = "Note: All transport parameters are taken from main program!!";
		ds.gsimTa.setCaretPosition(0);
		ds.gsimTa.setEditable(false);
		ds.gsimTa.setText(s);
		//ds.gsimTa.setLineWrap(true);
		ds.gsimTa.setWrapStyleWord(true);
		ds.gsimTa.setBackground(GammaBetaFrame.textAreaBkgColor);// setForeground
		ds.gsimTa.setForeground(GammaBetaFrame.foreColor);// setForeground
		ds.gsimTa.setFocusable(false);//THE KEY TO AVOID THREAD BUG WHEN SELECT TEXTAREA!!!!
		// ---------------------------------------------------
		ds.eTf.setText("1.0");
		ds.eminTf.setText("0.2");
		ds.emaxTf.setText("1.2");
		ds.bwidthTf.setText("1");
		ds.eminTf.setEnabled(false);
		ds.emaxTf.setEnabled(false);
		ds.bwidthTf.setEnabled(false);

		ds.mediaTf.setText("AIR_gas_normal");
		ds.mediaTf.setEnabled(false);
		ds.spectrumTf.setEnabled(false);
		ds.loadSpectrumB.setEnabled(false);

		ds.angleTf.setText("0.0");
		ds.angleTf
				.setToolTipText("Angle, in degrees, of incident radiation with medium plane normal");
		ds.rbeamTf.setText("5.0");
		ds.rbeamTf.setToolTipText("Radius of the incident beam in cm");
		ds.distanceTf.setText("20.0");
		ds.distanceTf
				.setToolTipText("Distance of beam source to the medium plane in cm");
		ds.rbeamTf.setEnabled(false);
		ds.distanceTf.setEnabled(false);

		String[] elems1 = { "Point source", "Beam source" };// source_type
		ds.sourceCb = new JComboBox(elems1);
		s = elems1[0];// 1000
		ds.sourceCb.setSelectedItem((Object) s);
		ds.sourceCb.setMaximumRowCount(5);
		ds.sourceCb.setPreferredSize(sizeTxtCb2);

		String[] elems2 = { "Succesive energies", "Spectrum",
				"Energies from Emin to Emax" };// energies
		ds.einCb = new JComboBox(elems2);
		s = elems2[0];// 1000
		ds.einCb.setSelectedItem((Object) s);
		ds.einCb.setMaximumRowCount(5);
		ds.einCb.setPreferredSize(sizeTxtCb2);

		ds.photonRb = new JRadioButton("Photon");
		ds.electronRb = new JRadioButton("Electron");
		ds.positronRb = new JRadioButton("Positron");

		//ds.gsimTa.setToolTipText("Select text then CTRL+C to copy elsewhere!");
	}

	/**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	private TitledBorder getGroupBoxBorder(String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(G_app.LINE_BORDER,
				title);
		tb.setTitleColor(GammaBetaFrame.foreColor);
		Font fnt = tb.getTitleFont();
		Font f = fnt.deriveFont(Font.BOLD);
		tb.setTitleFont(f);
		return tb;// BorderFactory.createTitledBorder(ds.LINE_BORDER,title);
	}

	/**
	   * Create main panel
	   * @return the result
	   */
	protected JPanel createSimMainPanel() {
		// -----------------------------------------------------------------------------------
		ButtonGroup group = new ButtonGroup();
		group.add(ds.photonRb);
		group.add(ds.electronRb);
		group.add(ds.positronRb);

		JPanel buttP = new JPanel();
		buttP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel lbl = new JLabel("Incident particle ");
		lbl.setForeground(GammaBetaFrame.foreColor);
		buttP.add(lbl);
		buttP.add(ds.photonRb);
		buttP.add(ds.electronRb);
		buttP.add(ds.positronRb);
		lbl = new JLabel("Number of histories: ");
		lbl.setForeground(GammaBetaFrame.foreColor);
		buttP.add(lbl);
		buttP.add(ds.ncaseCb);
		// -------------
		ds.photonRb.setBackground(ds.fundal);
		ds.photonRb.setForeground(GammaBetaFrame.foreColor);
		ds.electronRb.setBackground(ds.fundal);
		ds.electronRb.setForeground(GammaBetaFrame.foreColor);
		ds.positronRb.setBackground(ds.fundal);
		ds.positronRb.setForeground(GammaBetaFrame.foreColor);
		buttP.setBackground(ds.fundal);
		ds.photonRb.setSelected(true);
		// -----------------------------------------------------------------------------------
		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		JLabel label = new JLabel("Energy inputs: ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.einCb);
		d1P.setBackground(ds.fundal);

		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Energy [MeV]:");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.eTf);
		d2P.setBackground(ds.fundal);

		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		d3P.add(ds.addB);
		d3P.add(ds.deleteB);
		d3P.setBackground(ds.fundal);

		JPanel eP = new JPanel();
		BoxLayout bl0 = new BoxLayout(eP, BoxLayout.Y_AXIS);
		eP.setLayout(bl0);
		eP.add(d2P);
		eP.add(d3P);
		eP.setBackground(ds.fundal);

		// -----------------------------------------------------------------------------------
		JPanel lstmasP = new JPanel();
		lstmasP.setLayout(new BorderLayout());
		lstmasP.add(listSp, BorderLayout.CENTER);
		listSp.getViewport().add(ds.kvmL, null);
		listSp.setPreferredSize(sizeLst);
		// ----------------------------------------

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Emin [MeV]:");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.eminTf);
		d4P.setBackground(ds.fundal);

		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Emax [MeV]:");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.emaxTf);
		d5P.setBackground(ds.fundal);

		JPanel d6P = new JPanel();
		d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("BandWidth :");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.bwidthTf);
		d6P.setBackground(ds.fundal);

		JPanel emmP = new JPanel();
		BoxLayout bl1 = new BoxLayout(emmP, BoxLayout.Y_AXIS);
		emmP.setLayout(bl1);
		emmP.add(d4P);
		emmP.add(d5P);
		emmP.add(d6P);
		emmP.setBackground(ds.fundal);

		JPanel d7P = new JPanel();
		d7P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		d7P.add(eP);
		d7P.add(lstmasP);
		d7P.add(emmP);
		d7P.setBackground(ds.fundal);

		JPanel d8P = new JPanel();
		d8P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Spectrum filename:");
		d8P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d8P.add(ds.spectrumTf);
		d8P.add(ds.loadSpectrumB);
		d8P.setBackground(ds.fundal);

		JPanel enerP = new JPanel();
		BoxLayout bl2 = new BoxLayout(enerP, BoxLayout.Y_AXIS);
		enerP.setLayout(bl2);
		enerP.add(d1P);
		enerP.add(d7P);
		enerP.add(d8P);
		enerP.setBackground(ds.fundal);
		enerP.setBorder(getGroupBoxBorder("Energy sample"));

		JPanel d9P = new JPanel();
		d9P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Source inputs: ");
		d9P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d9P.add(ds.sourceCb);
		d9P.setBackground(ds.fundal);

		JPanel d10P = new JPanel();
		d10P.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Angle [degrees]: ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.angleTf);
		label = new JLabel("Beam radius [cm]: ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.rbeamTf);
		label = new JLabel("Distance [cm]: ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.distanceTf);
		d10P.setBackground(ds.fundal);

		JPanel sourceP = new JPanel();
		BoxLayout bl3 = new BoxLayout(sourceP, BoxLayout.Y_AXIS);
		sourceP.setLayout(bl3);
		sourceP.add(d9P);
		sourceP.add(d10P);
		sourceP.setBackground(ds.fundal);
		sourceP.setBorder(getGroupBoxBorder("Source sample"));

		JPanel mediumP = new JPanel();
		mediumP.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
		label = new JLabel("Medium filename: ");
		mediumP.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		mediumP.add(ds.mediaTf);
		mediumP.add(ds.loadMediaB);
		mediumP.setBackground(ds.fundal);
		// ----------------------------------------------------------------
		JPanel eastP = new JPanel();
		BoxLayout bl111 = new BoxLayout(eastP, BoxLayout.Y_AXIS);
		eastP.setLayout(bl111);
		eastP.add(buttP, null);
		eastP.add(enerP, null);
		eastP.add(sourceP, null);
		eastP.add(mediumP, null);
		eastP.setBackground(ds.fundal);

		// JPanel mainP=new JPanel(new BorderLayout());
		// mainP.add(eastP,BorderLayout.NORTH);
		// mainP.add(resultP,BorderLayout.CENTER);//main dimension !!
		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(eastP), BorderLayout.CENTER);
		main2P.setBackground(ds.fundal);

		return main2P;// eastP;//mainP;
	}

	/**
	   * Create result panel
	   * @return the result
	   */
	protected JPanel createOutputPanel() {
		JPanel resultP = new JPanel(new BorderLayout());
		//JScrollPane jspres = new JScrollPane();
		resultP.add(new JScrollPane(ds.gsimTa), BorderLayout.CENTER);
		resultP.setPreferredSize(textAreaDimension);
		resultP.setBackground(ds.fundal);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(resultP, BorderLayout.CENTER);// main dimension !!
		mainP.setBackground(ds.fundal);
		return mainP;
	}
}
