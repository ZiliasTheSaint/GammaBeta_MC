package gammaBetaMc;

import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

//import Jad.mathemathics.Convertor;
/**
 * Auxiliary class for GammaGlobal_hybrid. It handles GUI controls. 
 * 
 * @author Dan Fulea, 19 Apr. 2009
 */

public class GES_hybridGUI {
	private GammaGlobal_hybrid ds;
	// private static final Dimension sizeCb = new Dimension(50, 21);
	private static final Dimension sizeTxtCb = new Dimension(100, 21);

	 private static final Dimension sizeTxtCb2 = new Dimension(150, 21);
	// private static final Dimension dimP= new Dimension(200, 80);
	// private static final Dimension dimPp= new Dimension(200, 140);

	// Construct the frame
	 /**
	  * Constructor
	  * @param ds the GammaGlobal_hybrid object
	  */
	public GES_hybridGUI(GammaGlobal_hybrid ds) {
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
		// String tools="";
		// ####################################################
		ds.startsimB.setText(GammaGlobal_hybrid.resources
				.getString("main.startsimB"));
		Character mnemonic;
		mnemonic = (Character) GammaGlobal_hybrid.resources
				.getObject("main.startsimB.mnemonic");
		ds.startsimB.setMnemonic(mnemonic.charValue());
		// ds.stopsimB.setText(ds.resources.getString("main.stopsimB"));
		// mnemonic=(Character)
		// ds.resources.getObject("main.stopsimB.mnemonic");
		// ds.stopsimB.setMnemonic(mnemonic.charValue());
		ds.saveSB
				.setText(GammaGlobal_hybrid.resources.getString("main.saveSB"));
		mnemonic = (Character) GammaGlobal_hybrid.resources
				.getObject("main.saveSB.mnemonic");
		ds.saveSB.setMnemonic(mnemonic.charValue());
		ds.saveDB
				.setText(GammaGlobal_hybrid.resources.getString("main.saveDB"));
		mnemonic = (Character) GammaGlobal_hybrid.resources
				.getObject("main.saveDB.mnemonic");
		ds.saveDB.setMnemonic(mnemonic.charValue());
		ds.loadSB
				.setText(GammaGlobal_hybrid.resources.getString("main.loadSB"));
		mnemonic = (Character) GammaGlobal_hybrid.resources
				.getObject("main.loadSB.mnemonic");
		ds.loadSB.setMnemonic(mnemonic.charValue());
		ds.loadDB
				.setText(GammaGlobal_hybrid.resources.getString("main.loadDB"));
		mnemonic = (Character) GammaGlobal_hybrid.resources
				.getObject("main.loadDB.mnemonic");
		ds.loadDB.setMnemonic(mnemonic.charValue());

		String[] photonnr = (String[]) GammaGlobal_hybrid.resources
				.getObject("main.nPhotonsCb");
		ds.nPhotonsCb = new JComboBox(photonnr);
		String s = photonnr[1];// 20000
		ds.nPhotonsCb.setSelectedItem((Object) s);
		ds.nPhotonsCb.setMaximumRowCount(5);
		ds.nPhotonsCb.setPreferredSize(sizeTxtCb);

		String[] sType = (String[]) GammaGlobal_hybrid.resources
				.getObject("main.sourceTypeCb");
		ds.sourceTypeCb = new JComboBox(sType);
		s = sType[0];// [1];//cilindru
		ds.sourceTypeCb.setSelectedItem((Object) s);
		ds.sourceTypeCb.setMaximumRowCount(5);
		ds.sourceTypeCb.setPreferredSize(sizeTxtCb);

		String[] sEq = (String[]) GammaGlobal_hybrid.resources
				.getObject("main.sourceEqCb");
		ds.sourceEqCb = new JComboBox(sEq);
		s = sEq[0];// H2O
		ds.sourceEqCb.setSelectedItem((Object) s);
		ds.sourceEqCb.setMaximumRowCount(5);
		ds.sourceEqCb.setPreferredSize(sizeTxtCb);

		String[] sEq2 = (String[]) GammaGlobal_hybrid.resources
				.getObject("main.detTypeCb");
		ds.detTypeCb = new JComboBox(sEq2);
		s = sEq2[0];// NaI
		ds.detTypeCb.setSelectedItem((Object) s);
		ds.detTypeCb.setMaximumRowCount(5);
		ds.detTypeCb.setPreferredSize(sizeTxtCb);

		String[] sEq22 = (String[]) GammaGlobal_hybrid.resources
				.getObject("main.winCb");
		ds.windowCb = new JComboBox(sEq22);
		s = sEq22[0];// Al
		ds.windowCb.setSelectedItem((Object) s);
		ds.windowCb.setMaximumRowCount(5);
		ds.windowCb.setPreferredSize(sizeTxtCb);

		String[] sTy7 = { "Monoenergetic", "Spectrum" };
		ds.einCb = new JComboBox(sTy7);
		s = sTy7[0];
		ds.einCb.setSelectedItem((Object) s);
		ds.einCb.setMaximumRowCount(5);
		ds.einCb.setPreferredSize(sizeTxtCb2);
		// ds.einCb.setToolTipText("Real efficiency is computed for monoenergetic case only!");

		ds.simTa.setCaretPosition(0);
		ds.simTa.setEditable(false);
		// ds.simTa.setText(ds.resources.getString("rezultat"));
		ds.simTa.setLineWrap(true);
		ds.simTa.setWrapStyleWord(true);
		ds.simTa.setBackground(GammaBetaFrame.textAreaBkgColor);// setForeground
		ds.simTa.setForeground(GammaBetaFrame.textAreaForeColor);// setForeground

		ds.adetTf
				.setText(GammaGlobal_hybrid.resources.getString("main.adetTf"));
		ds.hdetTf
				.setText(GammaGlobal_hybrid.resources.getString("main.hdetTf"));
		ds.asourceTf.setText(GammaGlobal_hybrid.resources
				.getString("main.asourceTf"));
		ds.hsourceTf.setText(GammaGlobal_hybrid.resources
				.getString("main.hsourceTf"));
		ds.bsourceTf.setText(GammaGlobal_hybrid.resources
				.getString("main.bsourceTf"));
		ds.bsourceTf.setToolTipText(GammaGlobal_hybrid.resources
				.getString("main.bsourceTf.toolTip"));
		ds.hsourceupTf.setText(GammaGlobal_hybrid.resources
				.getString("main.hsourceupTf"));
		ds.hsourceupTf.setToolTipText(GammaGlobal_hybrid.resources
				.getString("main.hsourceupTf.toolTip"));
		ds.energyTf.setText(GammaGlobal_hybrid.resources
				.getString("main.energyTf"));
		// ds.energyTf.setToolTipText(ds.resources.getString("main.energyTf.toolTip"));
		ds.hUpTf.setText("0.50");
		ds.hUpTf.setToolTipText(GammaGlobal_hybrid.resources
				.getString("main.hUpTf.toolTip"));

		// ds.hsourceupTf.setEnabled(false);
		// ds.bsourceTf.setEnabled(false);
		ds.simTa.setToolTipText(GammaGlobal_hybrid.resources
				.getString("simTa.toolTip"));

		// ds.windowmaterialTf.setText("aluminum");ds.windowmaterialTf.setEnabled(false);
		ds.windowThickTf.setText("0.05");

		ds.spectrumB.setText(" ..... ");
		ds.spectrumB.setEnabled(false);
		ds.spectrumTf.setEnabled(false);
	}

	/**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	private TitledBorder getGroupBoxBorder(String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(
				GammaGlobal_hybrid.LINE_BORDER, title);
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
		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel(
				GammaGlobal_hybrid.resources.getString("photon.label"));
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.nPhotonsCb);
		d1P.setBackground(ds.fundal);

		// ---------det---------------------------
		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(GammaGlobal_hybrid.resources.getString("adet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.adetTf);
		label = new JLabel(GammaGlobal_hybrid.resources.getString("hdet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.hdetTf);

		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("hdetUp.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.hUpTf);

		d2P.setBackground(ds.fundal);

		// label=new JLabel(ds.resources.getString("hdetUp.label"));
		// d4P.add(label);label.setForeground(Color.white);
		// d4P.add(ds.hUpTf);
		// d4P.setBackground(ds.fundal);

		JPanel d6P = new JPanel();
		d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Detector material ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.detTypeCb);// d6P.add(ds.naimaterialTf);

		label = new JLabel("Window material ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.windowCb);// materialTf);
		label = new JLabel("Thickness (cm) ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.windowThickTf);

		d6P.setBackground(ds.fundal);
		/*
		 * JPanel d101P=new JPanel(); d101P.setLayout(new
		 * FlowLayout(FlowLayout.CENTER, 20,2)); //label=new
		 * JLabel("Monture material (entrance window) "); label=new
		 * JLabel("Window material ");
		 * d101P.add(label);label.setForeground(Color.white);
		 * d101P.add(ds.windowCb);//materialTf); label=new
		 * JLabel("Thickness (cm) ");
		 * d101P.add(label);label.setForeground(Color.white);
		 * d101P.add(ds.windowThickTf); d101P.setBackground(ds.fundal);
		 */

		JPanel d23P = new JPanel();
		d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d23P.add(ds.saveDB);
		d23P.add(ds.loadDB);
		d23P.setBackground(ds.fundal);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d2P, null);
		detP.add(d6P, null);
		detP.add(d23P, null);
		detP.setBorder(getGroupBoxBorder(GammaGlobal_hybrid.resources
				.getString("detector.border")));
		// -------------source---------------------------------------
		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("sourcetype.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(ds.sourceTypeCb);
		// -----------------------------------------
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("sourceeq.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(ds.sourceEqCb);
		// d3P.add(ds.sourcematerialTf);
		// d3P.add(ds.smatchooseB);
		d3P.setBackground(ds.fundal);

		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("asource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.asourceTf);
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("hsource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.hsourceTf);
		d5P.setBackground(ds.fundal);

		JPanel d7P = new JPanel();
		d7P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("bsource.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.bsourceTf);
		label = new JLabel(
				GammaGlobal_hybrid.resources.getString("asourceup.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.hsourceupTf);
		d7P.setBackground(ds.fundal);

		JPanel d8P = new JPanel();
		d8P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d8P.add(ds.saveSB);
		d8P.add(ds.loadSB);
		d8P.setBackground(ds.fundal);

		JPanel sourceP = new JPanel();
		BoxLayout bls = new BoxLayout(sourceP, BoxLayout.Y_AXIS);
		sourceP.setLayout(bls);
		sourceP.add(d3P, null);
		sourceP.add(d5P, null);
		sourceP.add(d7P, null);
		sourceP.add(d8P, null);
		sourceP.setBorder(getGroupBoxBorder(GammaGlobal_hybrid.resources
				.getString("source.border")));
		// -------------------------------------------------
		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));// ds.einCb
		label = new JLabel("Source energy");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.einCb);
		label = new JLabel(GammaGlobal_hybrid.resources.getString("en.label"));
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.energyTf);
		d11P.add(ds.spectrumTf);
		d11P.add(ds.spectrumB);
		d11P.setBackground(ds.fundal);

		JPanel d111P = new JPanel();
		d111P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));// ds.einCb
		d111P.add(ds.startsimB);
		d111P.setBackground(ds.fundal);

		JPanel eastP = new JPanel();
		BoxLayout bl1 = new BoxLayout(eastP, BoxLayout.Y_AXIS);
		eastP.setLayout(bl1);
		eastP.add(d1P, null);
		eastP.add(sourceP, null);
		eastP.add(detP, null);
		eastP.add(d11P, null);
		eastP.add(d111P, null);

		JPanel northP = new JPanel(new BorderLayout());
		northP.add(eastP, BorderLayout.CENTER);

		northP.setBackground(ds.fundal);
		eastP.setBackground(ds.fundal);
		sourceP.setBackground(ds.fundal);
		detP.setBackground(ds.fundal);

		JPanel resultP = new JPanel(new BorderLayout());
		// JScrollPane jspres=new JScrollPane();
		resultP.add(new JScrollPane(ds.simTa), BorderLayout.CENTER);
		resultP.setBackground(ds.fundal);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(northP), BorderLayout.CENTER);
		main2P.setBackground(ds.fundal);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(main2P, BorderLayout.NORTH);// /CENTER);//(northP,BorderLayout.NORTH);///CENTER);
		mainP.add(resultP, BorderLayout.CENTER);// main dimension !!
		mainP.setBackground(ds.fundal);
		return mainP;
	}

	/*
	 * protected JPanel createOutputPanel() { JPanel resultP=new JPanel(new
	 * BorderLayout()); JScrollPane jspres=new JScrollPane(); resultP.add(new
	 * JScrollPane(ds.simTa),BorderLayout.CENTER);
	 * resultP.setBackground(ds.fundal);
	 * 
	 * JPanel mainP=new JPanel(new BorderLayout());
	 * mainP.add(resultP,BorderLayout.CENTER);//main dimension !!
	 * mainP.setBackground(ds.fundal); return mainP; }
	 */
}
