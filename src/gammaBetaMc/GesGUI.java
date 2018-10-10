package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

//import Jad.mathemathics.Convertor;
/**
 * Auxiliary class for GES (GammaBetaFrame). It handles GUI controls.
 * 
 * @author Dan Fulea, 11 Jul. 2005
 */

public class GesGUI {

	private GammaBetaFrame ds;
	//private static final Dimension sizeCb = new Dimension(50, 21);
	private static final Dimension sizeTxtCb = new Dimension(100, 21);
	private static final Dimension sizeTxtCb2 = new Dimension(200, 21);
	private static final Dimension sizeTxtCb3 = new Dimension(130, 21);
	private static final Dimension sizeTxtCb4 = new Dimension(280, 21);
	private static final Dimension dimP = new Dimension(200, 80);
	private static final Dimension dimPp = new Dimension(200, 140);
	private static final Dimension textAreaDimension = new Dimension(900, 400);

	/**
	 * Constructor
	 * @param ds GammaBetaFrame object
	 */
	public GesGUI(GammaBetaFrame ds) {
		this.ds = ds;
		initComponents();
		// opacity();
	}

	@SuppressWarnings("unused")
	/**
	 * Set controls opacity...not needed though.
	 */
	private void opacity() {
		ds.rbeamTf.setOpaque(true);
		ds.anglebeamTf.setOpaque(true);
		ds.startsimB.setOpaque(true);
		ds.stopsimB.setOpaque(true);
		ds.saveSB.setOpaque(true);
		ds.saveDB.setOpaque(true);
		ds.loadSB.setOpaque(true);
		ds.loadDB.setOpaque(true);
		ds.sourceTypeCb.setOpaque(true);
		ds.sourceEqCb.setOpaque(true);
		ds.atenuareCh.setOpaque(true);
		ds.createFileCh.setOpaque(true);
		ds.asourceTf.setOpaque(true);
		ds.bsourceTf.setOpaque(true);
		ds.hsourceTf.setOpaque(true);
		ds.hsourceupTf.setOpaque(true);
		ds.hdetTf.setOpaque(true);
		ds.adetTf.setOpaque(true);
		ds.hdetTotTf.setOpaque(true);
		ds.adetTotTf.setOpaque(true);
		ds.hUpTf.setOpaque(true);
		ds.energyTf.setOpaque(true);
		ds.nPhotonsCb.setOpaque(true);
		ds.photonRb.setOpaque(true);
		ds.electronRb.setOpaque(true);
		ds.positronRb.setOpaque(true);
		ds.althickTf.setOpaque(true);
		ds.almaterialTf.setOpaque(true);
		ds.alchooseB.setOpaque(true);
		ds.airmaterialTf.setOpaque(true);
		ds.airchooseB.setOpaque(true);
		ds.naimaterialTf.setOpaque(true);
		ds.naichooseB.setOpaque(true);
		ds.sddistTf.setOpaque(true);
		ds.statTf.setOpaque(true);
		ds.sloteTf.setOpaque(true);
		ds.deltaeTf.setOpaque(true);
		ds.fanoCh.setOpaque(true);
		ds.kermaCh.setOpaque(true);ds.geantCh.setOpaque(true);ds.graphicSceneAutoRefreshCh.setOpaque(true);
		ds.calculationCb.setOpaque(true);
		ds.paCb.setOpaque(true);
		ds.baCb.setOpaque(true);
		ds.bcsCb.setOpaque(true);
		ds.pcsCb.setOpaque(true);
		ds.esaCb.setOpaque(true);
		ds.bcaCb.setOpaque(true);
		ds.ethreshTf.setOpaque(true);

		ds.printCb.setOpaque(true);
		ds.rngCb.setOpaque(true);
		ds.spinCh.setOpaque(true);
		ds.incoh_OnRb.setOpaque(true);
		ds.pe_OnRb.setOpaque(true);
		ds.coh_OnRb.setOpaque(true);
		ds.relax_OnRb.setOpaque(true);
		ds.incoh_OffRb.setOpaque(true);
		ds.pe_OffRb.setOpaque(true);
		ds.coh_OffRb.setOpaque(true);
		ds.relax_OffRb.setOpaque(true);
		ds.radc_OnRb.setOpaque(true);
		ds.triplet_OnRb.setOpaque(true);
		ds.eii_OnRb.setOpaque(true);
		ds.radc_OffRb.setOpaque(true);
		ds.triplet_OffRb.setOpaque(true);
		ds.eii_OffRb.setOpaque(true);

		ds.photoxRb_default.setOpaque(true);
		ds.photoxRb_epdl.setOpaque(true);
		ds.photoxRb_xcom.setOpaque(true);
		ds.eii_casnatiRb.setOpaque(true);
		ds.eii_kolbenstvedtRb.setOpaque(true);
		ds.eii_gryzinskiRb.setOpaque(true);

		ds.photonsplitSp.setOpaque(true);
		ds.forcingCh.setOpaque(true);
		ds.irejectCh.setOpaque(true);
		ds.RRCh.setOpaque(true);
		ds.startforcingSp.setOpaque(true);
		ds.stopforcingSp.setOpaque(true);
		ds.csenhancementSp.setOpaque(true);
		ds.bremsplitSp.setOpaque(true);
		ds.cbiasTf.setOpaque(true);
		ds.zdepthTf.setOpaque(true);
		ds.zfractionTf.setOpaque(true);
		ds.esaveinTf.setOpaque(true);

		ds.einCb.setOpaque(true);
		ds.spectrumB.setOpaque(true);
		ds.spectrumTf.setOpaque(true);

		ds.filenameeffTf.setOpaque(true);
		ds.saveeffCh.setOpaque(true);

		ds.customB.setOpaque(true);
		ds.customCb.setOpaque(true);

		ds.pointstodmaterialTf.setOpaque(true);
		ds.sourcematerialTf.setOpaque(true);
		ds.envelopematerialTf.setOpaque(true);
		ds.envelopeThickTf.setOpaque(true);
		ds.pointstodchooseB.setOpaque(true);
		ds.smatchooseB.setOpaque(true);
		ds.ematchooseB.setOpaque(true);
		ds.windowmaterialTf.setOpaque(true);
		ds.windowThickTf.setOpaque(true);
		ds.windowchooseB.setOpaque(true);
	}

	// Component initialization
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * Initialize components.
	 */
	private void initComponents() {
		String tools = "";
		// ####################################################
		ds.startsimB.setText(ds.resources.getString("main.startsimB"));
		Character mnemonic;
		mnemonic = (Character) ds.resources
				.getObject("main.startsimB.mnemonic");
		ds.startsimB.setMnemonic(mnemonic.charValue());
		ds.stopsimB.setText(ds.resources.getString("main.stopsimB"));
		mnemonic = (Character) ds.resources.getObject("main.stopsimB.mnemonic");
		ds.stopsimB.setMnemonic(mnemonic.charValue());
		
		ds.printB.setText("PDF Print result");
	    ds.printB.setMnemonic(new Character('P'));
		
		ds.saveSB.setText(ds.resources.getString("main.saveSB"));
		mnemonic = (Character) ds.resources.getObject("main.saveSB.mnemonic");
		ds.saveSB.setMnemonic(mnemonic.charValue());
		ds.saveDB.setText(ds.resources.getString("main.saveDB"));
		mnemonic = (Character) ds.resources.getObject("main.saveDB.mnemonic");
		ds.saveDB.setMnemonic(mnemonic.charValue());
		ds.loadSB.setText(ds.resources.getString("main.loadSB"));
		mnemonic = (Character) ds.resources.getObject("main.loadSB.mnemonic");
		ds.loadSB.setMnemonic(mnemonic.charValue());
		ds.loadDB.setText(ds.resources.getString("main.loadDB"));
		mnemonic = (Character) ds.resources.getObject("main.loadDB.mnemonic");
		ds.loadDB.setMnemonic(mnemonic.charValue());

		ds.saveDB
				.setToolTipText("For default geometry. Do not use in conjunction with custom applications!");
		ds.loadDB
				.setToolTipText("For default geometry. Do not use in conjunction with custom applications!");

		ds.customB = new JButton();
		ds.customB.setText("Custom detector...");
		mnemonic = new Character('C');
		ds.customB.setMnemonic(mnemonic.charValue());

		ds.pointstodchooseB.setText(" ..... ");
		ds.smatchooseB.setText(" ..... ");
		ds.ematchooseB.setText(" ..... ");
		ds.windowchooseB.setText(" ..... ");

		String[] sCstm = { "Radiology application" };
		ds.customCb = new JComboBox(sCstm);
		String str = sCstm[0];
		ds.customCb.setSelectedItem((Object) str);
		ds.customCb.setMaximumRowCount(5);
		ds.customCb.setPreferredSize(sizeTxtCb2);

		ds.atenuareCh = new JCheckBox(
				ds.resources.getString("main.atenuareCh"), true);
		ds.createFileCh = new JCheckBox(" Create output file ", false);
		String[] photonnr = (String[]) ds.resources
				.getObject("main.nPhotonsCb");
		ds.nPhotonsCb = new JComboBox(photonnr);
		String s = photonnr[1];// 20000
		ds.nPhotonsCb.setSelectedItem((Object) s);
		ds.nPhotonsCb.setMaximumRowCount(5);
		ds.nPhotonsCb.setPreferredSize(sizeTxtCb);

		String[] sType = (String[]) ds.resources.getObject("main.sourceTypeCb");
		ds.sourceTypeCb = new JComboBox(sType);
		s = sType[1];// cilindru
		ds.sourceTypeCb.setSelectedItem((Object) s);
		ds.sourceTypeCb.setMaximumRowCount(5);
		ds.sourceTypeCb.setPreferredSize(sizeTxtCb3);

		String[] sTyp = { "Results", "Standard", "Detail" };
		ds.printCb = new JComboBox(sTyp);
		s = sTyp[1];
		ds.printCb.setSelectedItem((Object) s);
		ds.printCb.setMaximumRowCount(5);
		ds.printCb.setPreferredSize(sizeTxtCb2);
		String[] sTy = { "Standard", "Full chaotic" };
		ds.rngCb = new JComboBox(sTy);
		s = sTy[0];
		ds.rngCb.setSelectedItem((Object) s);
		ds.rngCb.setMaximumRowCount(5);
		ds.rngCb.setPreferredSize(sizeTxtCb);

		String[] sTy1 = { "Off", "Simple", "KM", "Uniform", "Blend" };
		ds.paCb = new JComboBox(sTy1);
		s = sTy1[1];
		ds.paCb.setSelectedItem((Object) s);
		ds.paCb.setMaximumRowCount(5);
		ds.paCb.setPreferredSize(sizeTxtCb);
		tools = "If Off, pair set in motion at angle m/E;"
				+ " \n"
				+ "If Simple, leading term of angular distribution turned on;"
				+ " \n"
				+ "If KM, turns on 2BS from Koch and Motz;"
				+ " \n"
				+ "Note :KM could be inefficient for low and very high energies!";
		ds.paCb.setToolTipText(tools);

		tools = "Incident electron kinetic threshold energy, below no counts are detected in detector. Electrons only!";
		ds.ethreshTf.setToolTipText(tools);
		ds.ethreshTf.setText("0.00");

		String[] sTy2 = { "Simple", "KM" };
		ds.baCb = new JComboBox(sTy2);
		s = sTy2[1];
		ds.baCb.setSelectedItem((Object) s);
		ds.baCb.setMaximumRowCount(5);
		ds.baCb.setPreferredSize(sizeTxtCb);
		tools = "If Simple, only leading term used;" + " \n"
				+ "If KM, complete modified Koch-Motz 2BS used";
		ds.baCb.setToolTipText(tools);

		String[] sTy3 = { "BH", "NIST" };
		ds.bcsCb = new JComboBox(sTy3);
		s = sTy3[0];
		ds.bcsCb.setSelectedItem((Object) s);
		ds.bcsCb.setMaximumRowCount(5);
		ds.bcsCb.setPreferredSize(sizeTxtCb);
		tools = "If BH, Bethe-Heitler brems. cross sections used;"
				+ " \n"
				+ "If NIST, NIST cross sections used (based on ICRU radiative stopping powers)";
		ds.bcsCb.setToolTipText(tools);

		String[] sTy4 = { "BH", "NRC" };
		ds.pcsCb = new JComboBox(sTy4);
		s = sTy4[0];
		ds.pcsCb.setSelectedItem((Object) s);
		ds.pcsCb.setMaximumRowCount(5);
		ds.pcsCb.setPreferredSize(sizeTxtCb);
		tools = "If BH, Bethe-Heitler brems. cross sections used;" + " \n"
				+ "If NRC, NRC cross sections used";
		ds.pcsCb.setToolTipText(tools);

		String[] sTy5 = { "PRESTA II", "PRESTA I" };
		ds.esaCb = new JComboBox(sTy5);
		s = sTy5[0];
		ds.esaCb.setSelectedItem((Object) s);
		ds.esaCb.setMaximumRowCount(5);
		ds.esaCb.setPreferredSize(sizeTxtCb);
		tools = "Algorithms used to take into account lateral and longitudinal"
				+ " \n" + "correlations in a condensed history step" + " \n";
		ds.esaCb.setToolTipText(tools);

		String[] sTy6 = { "EXACT", "PRESTA I" };
		ds.bcaCb = new JComboBox(sTy6);
		s = sTy6[0];
		ds.bcaCb.setSelectedItem((Object) s);
		ds.bcaCb.setMaximumRowCount(5);
		ds.bcaCb.setPreferredSize(sizeTxtCb);
		tools = "EXACT means crossing boundaries in a single scattering mode;"
				+ " \n"
				+ "PRESTA I means boundary crossed with lateral correlation turned off"
				+ " \n" + "and multiscatter forced at boundaries";
		ds.bcaCb.setToolTipText(tools);

		String[] sEq = (String[]) ds.resources.getObject("main.sourceEqCb");
		ds.sourceEqCb = new JComboBox(sEq);
		s = sEq[0];// H2O
		ds.sourceEqCb.setSelectedItem((Object) s);
		ds.sourceEqCb.setMaximumRowCount(5);
		ds.sourceEqCb.setPreferredSize(sizeTxtCb);

		String[] sTy7 = { "Monoenergetic", "Spectrum" };
		ds.einCb = new JComboBox(sTy7);
		s = sTy7[0];
		ds.einCb.setSelectedItem((Object) s);
		ds.einCb.setMaximumRowCount(5);
		ds.einCb.setPreferredSize(sizeTxtCb3);
		ds.einCb.setToolTipText("Real efficiency is computed for monoenergetic case only!");

		ds.simTa.setCaretPosition(0);
		ds.simTa.setEditable(false);
		// ds.simTa.setText(ds.resources.getString("rezultat"));
		//ds.simTa.setLineWrap(true);
		ds.simTa.setWrapStyleWord(true);
		ds.simTa.setFocusable(false);//THE KEY TO AVOID THREAD BUG WHEN SELECT TEXTAREA!!!!
		
		ds.simTa.setBackground(GammaBetaFrame.textAreaBkgColor);// setForeground
		ds.simTa.setForeground(GammaBetaFrame.textAreaForeColor);// setForeground
		//ds.simTa.setEnabled(false);
		DefaultCaret caret = (DefaultCaret) ds.simTa.getCaret(); 
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		ds.adetTf.setText(ds.resources.getString("main.adetTf"));
		ds.hdetTf.setText(ds.resources.getString("main.hdetTf"));
		ds.asourceTf.setText(ds.resources.getString("main.asourceTf"));
		ds.hsourceTf.setText(ds.resources.getString("main.hsourceTf"));
		ds.bsourceTf.setText(ds.resources.getString("main.bsourceTf"));
		ds.bsourceTf.setToolTipText(ds.resources
				.getString("main.bsourceTf.toolTip"));
		ds.hsourceupTf.setText(ds.resources.getString("main.hsourceupTf"));
		ds.hsourceupTf.setToolTipText(ds.resources
				.getString("main.hsourceupTf.toolTip"));
		ds.energyTf.setText(ds.resources.getString("main.energyTf"));
		// ds.energyTf.setToolTipText(ds.resources.getString("main.energyTf.toolTip"));

		ds.sddistTf.setText("3.8");
		ds.sddistTf.setToolTipText("Point source only!");

		ds.rbeamTf.setText("0.1");// cm
		ds.anglebeamTf.setText("0.0");// degrees
		ds.rbeamTf.setEnabled(false);
		ds.anglebeamTf.setEnabled(false);

		ds.adetTotTf.setText(ds.resources.getString("main.adetTotTf"));
		ds.adetTotTf.setToolTipText(ds.resources
				.getString("main.adetTotTf.toolTip"));
		ds.hdetTotTf.setText(ds.resources.getString("main.hdetTotTf"));
		ds.hdetTotTf.setToolTipText(ds.resources
				.getString("main.hdetTotTf.toolTip"));
		ds.hUpTf.setText("0.50");
		ds.hUpTf.setToolTipText(ds.resources.getString("main.hUpTf.toolTip"));

		ds.sddistTf.setEnabled(false);
		// ds.pointstodchooseB.setEnabled(false);
		ds.hsourceupTf.setEnabled(false);
		ds.bsourceTf.setEnabled(false);
		//ds.simTa.setToolTipText(ds.resources.getString("simTa.toolTip"));

		ds.althickTf.setText("0.05");
		ds.almaterialTf.setText("aluminum");
		ds.almaterialTf.setEnabled(false);
		ds.alchooseB.setText(" ..... ");
		// ds.airmaterialTf.setText("air_dry_nearsealevel");ds.airmaterialTf.setEnabled(false);
		ds.airmaterialTf.setText("vacuum");
		ds.airmaterialTf.setEnabled(false);
		ds.airchooseB.setText(" ..... ");
		ds.naimaterialTf.setText("sodiumiodide");
		ds.naimaterialTf.setEnabled(false);
		ds.naichooseB.setText(" ..... ");

		ds.pointstodmaterialTf.setText("AIR_gas_normal");
		ds.pointstodmaterialTf.setEnabled(false);
		ds.sourcematerialTf.setText("water_liquid");
		ds.sourcematerialTf.setEnabled(false);
		ds.envelopematerialTf.setText("polyvinylchloride");
		ds.envelopematerialTf.setEnabled(false);
		ds.envelopeThickTf.setText("0.1");
		ds.windowmaterialTf.setText("aluminum");
		ds.windowmaterialTf.setEnabled(false);
		ds.windowThickTf.setText("0.05");

		ds.spectrumB.setText(" ..... ");
		ds.spectrumB.setEnabled(false);
		ds.spectrumTf.setEnabled(false);

		ds.statTf.setText("0.000");
		tools = "Finish the run if this accuracy is obtained at the" + " \n"
				+ "end of a batch prior to the total number of histories"
				+ " \n" + "being run";
		ds.statTf.setToolTipText(tools);// );
		ds.sloteTf.setText("0.01");
		tools = "Defines the output energy bins. It must be >0.0 so equal size bins"
				+ " \n"
				+ "of this width in MeV are used. It will get increased bt factors"
				+ " \n" + "of two until the whole spectrum input gets covered.";
		ds.sloteTf.setToolTipText(tools);
		ds.deltaeTf.setText("0.005");
		tools = "Peak efficiencies will be analysed using a bin width of"
				+ " \n"
				+ "2*DELTAE for each peak and two background regions of "
				+ " \n"
				+ "width DELTAE above and below the peak. Meaningless for electrons!";
		ds.deltaeTf.setToolTipText(tools);
		ds.fanoCh = new JCheckBox("photon regeneration ", false);
		ds.fanoCh.setEnabled(false);
		tools = "If checked, calculation performed with regeneration" + " \n"
				+ "of the parent photons after they have interacted.A" + " \n"
				+ "typical settings when FANO conditions are examined";
		ds.fanoCh.setToolTipText(tools);// );

		ds.kermaCh = new JCheckBox("score kerma ", true);ds.geantCh = new JCheckBox("use GEANT4 engine", false);
		ds.spinCh = new JCheckBox("spin effect ", true);ds.graphicSceneAutoRefreshCh = new JCheckBox("view detector",false);

		String[] calcs = { "dose and stoppers", "Aatt and Ascat",
				"pulse height distribution", "scatter fraction",
				"fluence-total", "fluence-electron primaries",
				"fluence-e-include brem secondaries",
				"fluence-photon primaries", "fluence-electron secondaries",
				"stopping power ratio", "brems application" };
		ds.calculationCb = new JComboBox(calcs);
		s = calcs[2];// PHD
		ds.calculationCb.setSelectedItem((Object) s);
		ds.calculationCb.setMaximumRowCount(5);
		ds.calculationCb.setPreferredSize(sizeTxtCb4);
		tools = "Score a pulse height distribution along with detector efficiencies";
		ds.calculationCb.setToolTipText(tools);// );

		ds.photonRb = new JRadioButton("photon");
		ds.electronRb = new JRadioButton("electron");
		ds.positronRb = new JRadioButton("positron");

		ds.incoh_OnRb = new JRadioButton("On");
		ds.pe_OnRb = new JRadioButton("On");
		ds.coh_OnRb = new JRadioButton("On");
		ds.relax_OnRb = new JRadioButton("On");
		ds.incoh_OffRb = new JRadioButton("Off");
		ds.pe_OffRb = new JRadioButton("Off");
		ds.coh_OffRb = new JRadioButton("Off");
		ds.relax_OffRb = new JRadioButton("Off");
		ds.radc_OnRb = new JRadioButton("On");
		ds.triplet_OnRb = new JRadioButton("On");
		ds.eii_OnRb = new JRadioButton("On");
		ds.radc_OffRb = new JRadioButton("Off");
		ds.triplet_OffRb = new JRadioButton("Off");
		ds.eii_OffRb = new JRadioButton("Off");
		ds.incoh_OnRb.setToolTipText("Bounding effect turned on");
		ds.incoh_OffRb.setToolTipText("Simple Klein-Nishina scattering");
		ds.pe_OffRb
				.setToolTipText("Photoelectron inherits the photon direction");
		ds.pe_OnRb.setToolTipText("Photoelectron angular sampling turned on");
		ds.relax_OnRb
				.setToolTipText("Emission of fluorescent X-ray, Auger and Coster-Kronig electrons");

		ds.photoxRb_default = new JRadioButton("internal/default");
		ds.photoxRb_epdl = new JRadioButton("EPDL");
		ds.photoxRb_epdl
				.setToolTipText("photon x-sections based on EPDL library");
		ds.photoxRb_xcom = new JRadioButton("XCOM");
		ds.photoxRb_xcom
				.setToolTipText("photon x-sections based on NIST/XCOM library");
		ds.eii_casnatiRb = new JRadioButton("Casnati");
		ds.eii_casnatiRb
				.setToolTipText("based on EII theory by Casnati. Only K-shell taken into account!");
		ds.eii_kolbenstvedtRb = new JRadioButton("Kolbenstvedt");
		ds.eii_kolbenstvedtRb
				.setToolTipText("based on EII theory by Kolbenstvedt. Only K-shell taken into account!");
		ds.eii_gryzinskiRb = new JRadioButton("Gryzinski");
		ds.eii_gryzinskiRb
				.setToolTipText("based on EII theory by Gryzinski. Only K-shell taken into account!");

		// Constructs a SpinnerNumberModel with the specified value,
		// minimum/maximum bounds, and stepSize
		SpinnerNumberModel snm = new SpinnerNumberModel(1, 0, 999, 1);
		SpinnerNumberModel snm1 = new SpinnerNumberModel(1, 0, 999, 1);
		SpinnerNumberModel snm2 = new SpinnerNumberModel(1, 0, 45, 1);
		SpinnerNumberModel snm3 = new SpinnerNumberModel(1, 0, 7, 1);
		SpinnerNumberModel snm4 = new SpinnerNumberModel(1, 0, 8, 1);
		ds.photonsplitSp = new JSpinner(snm);
		ds.photonsplitSp.setEnabled(false);
		tools = "If <2, normal transport. OVERRIDES photon forcing if>=2. Can only be >=2 if IFULL="
				+ " \n" + "dose and stoppers or Aatt and Ascat";
		ds.photonsplitSp.setToolTipText(tools);// );

		ds.forcingCh = new JCheckBox("Photon forcing ", false);
		ds.irejectCh = new JCheckBox("Electron range rejection ", false);
		ds.RRCh = new JCheckBox("Russian Roulette (charged particles)", false);

		ds.startforcingSp = new JSpinner(snm3);
		ds.startforcingSp.setEnabled(false);
		tools = "Start forcing photons at this interaction number";
		ds.startforcingSp.setToolTipText(tools);// );

		ds.stopforcingSp = new JSpinner(snm4);
		ds.stopforcingSp.setEnabled(false);
		tools = "Number of photon interactions after which to stop forcing photon interactions";
		ds.stopforcingSp.setToolTipText(tools);// );

		ds.csenhancementSp = new JSpinner(snm1);
		tools = "Scales the photon cross section by this factor in entire geometry";
		ds.csenhancementSp.setToolTipText(tools);// );

		ds.bremsplitSp = new JSpinner(snm2);
		tools = "Splitting bremsstrahlung photons is a very powerful technique for brems beams. "
				+ "\n"
				+ "It can be shown (accelerator modelling) that splitting factors of 20-40 are optimal (saves a factor of 4 in computing time)";
		ds.bremsplitSp.setToolTipText(tools);// );

		tools = "Parameter for pathlength biasing <0 for SHORTENING. If=0.0, no biasing is done";
		ds.cbiasTf.setToolTipText(tools);
		ds.cbiasTf.setText("0.0");

		tools = "Play Russian Roulette as any photon crosses the plane. Z=Russian Roulette Depth";
		ds.zdepthTf.setToolTipText(tools);
		ds.zdepthTf.setText("0.0");

		tools = "Survival probability for Russian Roulette."
				+ " \n"
				+ " If photon survives, its weight is increased by inverse of this fraction.";
		ds.zfractionTf.setToolTipText(tools);
		ds.zfractionTf.setText("0.0");

		tools = "If electron range rejection is on, discard an electron when E<ESAVEIN. "
				+ "\n"
				+ "This ignores brems losses below ESAVEIN. If 0.0, turns off range rejection. "
				+ " \n" + "ESAVEIN is total energy (Ekin+0.511 MeV)";
		ds.esaveinTf.setToolTipText(tools);
		ds.esaveinTf.setEnabled(false);
		ds.esaveinTf.setText("2.0");

		tools = "This form of RR is meant to complement the use of bremsstrahlung splitting "
				+ "\n"
				+ "for those cases where only the photons are of significant interest. It is designed to ensure "
				+ " \n" + "that charged particles carry their natural weight.";
		ds.RRCh.setToolTipText(tools);// );

		ds.saveeffCh = new JCheckBox("Save eff.", false);
		// ds.airmaterialTf.setToolTipText("This must be air at the desired pressure! If necessary, the medium for outside detector geometry will be air!");
	}

	/**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	private TitledBorder getGroupBoxBorder(String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(GammaBetaFrame.LINE_BORDER,
				title);
		tb.setTitleColor(GammaBetaFrame.foreColor);//Color.white);
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
		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(ds.photonRb);
		group.add(ds.electronRb);
		group.add(ds.positronRb);
		// Put the radio buttons in a column in a panel.
		JPanel buttP = new JPanel();
		buttP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel lbl = new JLabel("Incident particle ");
		lbl.setForeground(GammaBetaFrame.foreColor);
		buttP.add(lbl);
		buttP.add(ds.photonRb);
		buttP.add(ds.electronRb);
		buttP.add(ds.positronRb);
		// -------------
		ds.photonRb.setBackground(GammaBetaFrame.bkgColor);
		ds.photonRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		ds.electronRb.setBackground(GammaBetaFrame.bkgColor);
		ds.electronRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		ds.positronRb.setBackground(GammaBetaFrame.bkgColor);
		ds.positronRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		buttP.setBackground(GammaBetaFrame.bkgColor);
		ds.photonRb.setSelected(true);
		// -----------------------------------------------------------------------------------

		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel(ds.resources.getString("photon.label"));
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.nPhotonsCb);
		label = new JLabel("statistical accuracy sought ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.statTf);
		label = new JLabel("Random number generator ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.rngCb);
		d1P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d25P = new JPanel();
		d25P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Type of calculation (IFULL)");
		d25P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d25P.add(ds.calculationCb);
		ds.kermaCh.setBackground(GammaBetaFrame.bkgColor);ds.geantCh.setBackground(GammaBetaFrame.bkgColor);
		ds.kermaCh.setForeground(GammaBetaFrame.foreColor);ds.geantCh.setForeground(GammaBetaFrame.foreColor);
		ds.fanoCh.setBackground(GammaBetaFrame.bkgColor);
		ds.fanoCh.setForeground(GammaBetaFrame.foreColor);
		ds.graphicSceneAutoRefreshCh.setBackground(GammaBetaFrame.bkgColor);
		ds.graphicSceneAutoRefreshCh.setForeground(GammaBetaFrame.foreColor);
		d25P.add(ds.kermaCh);
		d25P.add(ds.fanoCh);
		d25P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d26P = new JPanel();
		d26P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Pulse height distribution inputs: ");
		d26P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		label = new JLabel("SLOTE ");
		d26P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d26P.add(ds.sloteTf);
		label = new JLabel("DELTAE ");
		d26P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d26P.add(ds.deltaeTf);
		label = new JLabel("Ekin_THRESHOLD ");
		d26P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d26P.add(ds.ethreshTf);
		d26P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d266P = new JPanel();
		d266P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Efficiency filename: ");
		d266P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d266P.add(ds.filenameeffTf);
		ds.saveeffCh.setBackground(GammaBetaFrame.bkgColor);
		ds.saveeffCh.setForeground(GammaBetaFrame.foreColor);
		d266P.add(ds.saveeffCh);
		d266P.setBackground(GammaBetaFrame.bkgColor);

		/*// ---------det---------------------------
		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("adet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.adetTf);
		label = new JLabel(ds.resources.getString("hdet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.hdetTf);
		d2P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("adetTot.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.adetTotTf);
		label = new JLabel(ds.resources.getString("hdetTot.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.hdetTotTf);
		label = new JLabel(ds.resources.getString("hdetUp.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.hUpTf);
		d4P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d6P = new JPanel();
		d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Detector material ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.naimaterialTf);
		d6P.add(ds.naichooseB);
		d6P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d9P = new JPanel();
		d9P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Surrounding inactive zone material ");
		d9P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d9P.add(ds.airmaterialTf);
		d9P.add(ds.airchooseB);
		d9P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d10P = new JPanel();
		d10P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		// label=new JLabel("Monture material (entrance window) ");
		label = new JLabel("Monture material ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.almaterialTf);
		d10P.add(ds.alchooseB);
		label = new JLabel("Thickness (cm) ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.althickTf);
		d10P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d101P = new JPanel();
		d101P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		// label=new JLabel("Monture material (entrance window) ");
		label = new JLabel("Window material ");
		d101P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d101P.add(ds.windowmaterialTf);
		d101P.add(ds.windowchooseB);
		label = new JLabel("Thickness (cm) ");
		d101P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d101P.add(ds.windowThickTf);
		d101P.setBackground(GammaBetaFrame.bkgColor);

		JPanel cstmP = new JPanel();
		cstmP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		cstmP.add(ds.customCb);
		cstmP.add(ds.customB);
		cstmP.setBorder(getGroupBoxBorder("Custom applications"));
		cstmP.setBackground(GammaBetaFrame.bkgColor);

		JPanel d23P = new JPanel();
		d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d23P.add(ds.saveDB);
		d23P.add(ds.loadDB);
		d23P.add(cstmP);
		d23P.setBackground(GammaBetaFrame.bkgColor);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d2P, null);
		detP.add(d4P, null);
		detP.add(d6P, null);
		detP.add(d9P, null);
		detP.add(d10P, null);
		detP.add(d101P, null);
		detP.add(d23P, null);
		detP.setBorder(getGroupBoxBorder(ds.resources
				.getString("detector.border")));
		// -------------source---------------------------------------
		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("sourcetype.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(ds.sourceTypeCb);
		// -----------------------------------------
		label = new JLabel(ds.resources.getString("sourceeq.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		// d3P.add(ds.sourceEqCb);
		d3P.add(ds.sourcematerialTf);
		d3P.add(ds.smatchooseB);
		d3P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d55P = new JPanel();
		d55P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Point source to detector distance (cm)");
		d55P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d55P.add(ds.sddistTf);
		label = new JLabel("Environment medium ");
		d55P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d55P.add(ds.pointstodmaterialTf);
		d55P.add(ds.pointstodchooseB);
		d55P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		// =====================
		label = new JLabel("Beam radius (cm)");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.rbeamTf);
		label = new JLabel("Beam angle (degrees)");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.anglebeamTf);

		label = new JLabel(ds.resources.getString("asource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.asourceTf);
		// ----------------------------------------------------
		label = new JLabel(ds.resources.getString("hsource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.hsourceTf);
		d5P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d7P = new JPanel();
		d7P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("bsource.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.bsourceTf);
		// --------------------------------------------------
		label = new JLabel(ds.resources.getString("asourceup.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.hsourceupTf);
		d7P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d71P = new JPanel();
		d71P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Envelope material");
		d71P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d71P.add(ds.envelopematerialTf);
		d71P.add(ds.ematchooseB);
		label = new JLabel("Envelope thickness (cm)");
		d71P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d71P.add(ds.envelopeThickTf);
		d71P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d8P = new JPanel();
		d8P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d8P.add(ds.saveSB);
		d8P.add(ds.loadSB);
		d8P.setBackground(GammaBetaFrame.bkgColor);

		JPanel sourceP = new JPanel();
		BoxLayout bls = new BoxLayout(sourceP, BoxLayout.Y_AXIS);
		sourceP.setLayout(bls);
		sourceP.add(buttP, null);
		sourceP.add(d3P, null);
		sourceP.add(d55P, null);
		sourceP.add(d5P, null);
		sourceP.add(d7P, null);
		sourceP.add(d71P, null);
		sourceP.add(d8P, null);
		sourceP.setBorder(getGroupBoxBorder(ds.resources
				.getString("source.border")));
				
		sourceP.setBackground(GammaBetaFrame.bkgColor);
		detP.setBackground(GammaBetaFrame.bkgColor);
				
*/
		// -------------------------------------------------
		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));// ds.einCb
		label = new JLabel("Source energy");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.einCb);
		label = new JLabel(ds.resources.getString("en.label"));
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.energyTf);
		d11P.add(ds.spectrumTf);
		d11P.add(ds.spectrumB);
		ds.atenuareCh.setBackground(GammaBetaFrame.bkgColor);
		// ======================================================
		// d11P.add(ds.atenuareCh);ds.atenuareCh.setForeground(Color.white);
		// label=new JLabel("Output settings ");
		// d11P.add(label);label.setForeground(Color.white);
		// d11P.add(ds.printCb);
		// ds.createFileCh.setBackground(ds.fundal);
		// d11P.add(ds.createFileCh);ds.createFileCh.setForeground(Color.white);
		d11P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d12P = new JPanel();
		d12P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));// ds.einCb
		// d12P.add(ds.atenuareCh);ds.atenuareCh.setForeground(Color.white);
		label = new JLabel("Output settings ");
		d12P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d12P.add(ds.printCb);
		ds.createFileCh.setBackground(GammaBetaFrame.bkgColor);
		d12P.add(ds.createFileCh);
		ds.createFileCh.setForeground(GammaBetaFrame.foreColor);
		d12P.setBackground(GammaBetaFrame.bkgColor);
		
		//-------------------------------
		JPanel geantP = new JPanel();
		geantP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("If detection efficiency (pulse heigh distribution) mode and mono-energetic beam: ");
		geantP.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		geantP.add(ds.geantCh);
		geantP.add(ds.graphicSceneAutoRefreshCh);
		geantP.setBackground(GammaBetaFrame.bkgColor);

		JPanel eastP = new JPanel();
		BoxLayout bl1 = new BoxLayout(eastP, BoxLayout.Y_AXIS);
		// -----------
		// eastP.add(buttP, null);
		// ----------------------------
		eastP.setLayout(bl1);
		eastP.add(d1P, null);
		eastP.add(d25P, null);
		eastP.add(d26P, null);
		eastP.add(d266P, null);//eastP.add(geantP, null);
		
		eastP.add(buttP, null);
		//eastP.add(sourceP, null);
		//eastP.add(detP, null);
		
		eastP.add(d11P, null);
		eastP.add(d12P, null);eastP.add(geantP, null);

		JPanel northP = new JPanel(new BorderLayout());
		northP.add(eastP, BorderLayout.CENTER);

		northP.setBackground(GammaBetaFrame.bkgColor);
		eastP.setBackground(GammaBetaFrame.bkgColor);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(northP, BorderLayout.CENTER);

		mainP.setBackground(GammaBetaFrame.bkgColor);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(mainP), BorderLayout.CENTER);
		main2P.setBackground(GammaBetaFrame.bkgColor);
		return main2P;// mainP;
	}
	
	/**
	   * Create geometry panel
	   * @return the result
	   */
	protected JPanel createGeometryPanel(){
		JLabel label=null;
		
		/*ButtonGroup group = new ButtonGroup();
		group.add(ds.photonRb);
		group.add(ds.electronRb);
		group.add(ds.positronRb);
		// Put the radio buttons in a column in a panel.
		JPanel buttP = new JPanel();
		buttP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel lbl = new JLabel("Incident particle ");
		lbl.setForeground(GammaBetaFrame.foreColor);
		buttP.add(lbl);
		buttP.add(ds.photonRb);
		buttP.add(ds.electronRb);
		buttP.add(ds.positronRb);
		// -------------
		ds.photonRb.setBackground(GammaBetaFrame.bkgColor);
		ds.photonRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		ds.electronRb.setBackground(GammaBetaFrame.bkgColor);
		ds.electronRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		ds.positronRb.setBackground(GammaBetaFrame.bkgColor);
		ds.positronRb.setForeground(GammaBetaFrame.foreColor);//Color.white);
		buttP.setBackground(GammaBetaFrame.bkgColor);
		ds.photonRb.setSelected(true);*/
		
		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("adet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.adetTf);
		label = new JLabel(ds.resources.getString("hdet.label"));
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.hdetTf);
		d2P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("adetTot.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.adetTotTf);
		label = new JLabel(ds.resources.getString("hdetTot.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.hdetTotTf);
		label = new JLabel(ds.resources.getString("hdetUp.label"));
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(ds.hUpTf);
		d4P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d6P = new JPanel();
		d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Detector material ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(ds.naimaterialTf);
		d6P.add(ds.naichooseB);
		d6P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d9P = new JPanel();
		d9P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Surrounding inactive zone material ");
		d9P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d9P.add(ds.airmaterialTf);
		d9P.add(ds.airchooseB);
		d9P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d10P = new JPanel();
		d10P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		// label=new JLabel("Monture material (entrance window) ");
		label = new JLabel("Monture material ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.almaterialTf);
		d10P.add(ds.alchooseB);
		label = new JLabel("Thickness (cm) ");
		d10P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d10P.add(ds.althickTf);
		d10P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d101P = new JPanel();
		d101P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		// label=new JLabel("Monture material (entrance window) ");
		label = new JLabel("Window material ");
		d101P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d101P.add(ds.windowmaterialTf);
		d101P.add(ds.windowchooseB);
		label = new JLabel("Thickness (cm) ");
		d101P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d101P.add(ds.windowThickTf);
		d101P.setBackground(GammaBetaFrame.bkgColor);

		JPanel cstmP = new JPanel();
		cstmP.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		cstmP.add(ds.customCb);
		cstmP.add(ds.customB);
		cstmP.setBorder(getGroupBoxBorder("Custom applications"));
		cstmP.setBackground(GammaBetaFrame.bkgColor);

		JPanel d23P = new JPanel();
		d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d23P.add(ds.saveDB);
		d23P.add(ds.loadDB);
		d23P.add(cstmP);
		d23P.setBackground(GammaBetaFrame.bkgColor);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d2P, null);
		detP.add(d4P, null);
		detP.add(d6P, null);
		detP.add(d9P, null);
		detP.add(d10P, null);
		detP.add(d101P, null);
		detP.add(d23P, null);
		detP.setBorder(getGroupBoxBorder(ds.resources
				.getString("detector.border")));
		// -------------source---------------------------------------
		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("sourcetype.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(ds.sourceTypeCb);
		// -----------------------------------------
		label = new JLabel(ds.resources.getString("sourceeq.label"));
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		// d3P.add(ds.sourceEqCb);
		d3P.add(ds.sourcematerialTf);
		d3P.add(ds.smatchooseB);
		d3P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d55P = new JPanel();
		d55P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Point source to detector distance (cm)");
		d55P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d55P.add(ds.sddistTf);
		label = new JLabel("Environment medium ");
		d55P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d55P.add(ds.pointstodmaterialTf);
		d55P.add(ds.pointstodchooseB);
		d55P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
		// =====================
		label = new JLabel("Beam radius (cm)");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.rbeamTf);
		label = new JLabel("Beam angle (degrees)");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.anglebeamTf);

		label = new JLabel(ds.resources.getString("asource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.asourceTf);
		// ----------------------------------------------------
		label = new JLabel(ds.resources.getString("hsource.label"));
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.hsourceTf);
		d5P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d7P = new JPanel();
		d7P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(ds.resources.getString("bsource.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.bsourceTf);
		// --------------------------------------------------
		label = new JLabel(ds.resources.getString("asourceup.label"));
		d7P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d7P.add(ds.hsourceupTf);
		d7P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d71P = new JPanel();
		d71P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Envelope material");
		d71P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d71P.add(ds.envelopematerialTf);
		d71P.add(ds.ematchooseB);
		label = new JLabel("Envelope thickness (cm)");
		d71P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d71P.add(ds.envelopeThickTf);
		d71P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d8P = new JPanel();
		d8P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d8P.add(ds.saveSB);
		d8P.add(ds.loadSB);
		d8P.setBackground(GammaBetaFrame.bkgColor);

		JPanel sourceP = new JPanel();
		BoxLayout bls = new BoxLayout(sourceP, BoxLayout.Y_AXIS);
		sourceP.setLayout(bls);
		//sourceP.add(buttP, null);
		sourceP.add(d3P, null);
		sourceP.add(d55P, null);
		sourceP.add(d5P, null);
		sourceP.add(d7P, null);
		sourceP.add(d71P, null);
		sourceP.add(d8P, null);
		sourceP.setBorder(getGroupBoxBorder(ds.resources
				.getString("source.border")));
				
		sourceP.setBackground(GammaBetaFrame.bkgColor);
		detP.setBackground(GammaBetaFrame.bkgColor);
		//=========
		JPanel eastP = new JPanel();
		BoxLayout bl1 = new BoxLayout(eastP, BoxLayout.Y_AXIS);		
		eastP.setLayout(bl1);
		eastP.add(sourceP, null);
		eastP.add(detP, null);
		
		JPanel northP = new JPanel(new BorderLayout());
		northP.add(eastP, BorderLayout.CENTER);

		northP.setBackground(GammaBetaFrame.bkgColor);
		eastP.setBackground(GammaBetaFrame.bkgColor);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(northP, BorderLayout.CENTER);

		mainP.setBackground(GammaBetaFrame.bkgColor);
		//===========
		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(mainP), BorderLayout.CENTER);
		main2P.setBackground(GammaBetaFrame.bkgColor);
		return main2P;// mainP;
	}

	/**
	   * Create transport panel
	   * @return the result
	   */
	protected JPanel createTransportPanel() {
		ButtonGroup group = new ButtonGroup();
		group.add(ds.incoh_OnRb);
		group.add(ds.incoh_OffRb);
		ButtonGroup group1 = new ButtonGroup();
		group1.add(ds.coh_OnRb);
		group1.add(ds.coh_OffRb);
		ButtonGroup group2 = new ButtonGroup();
		group2.add(ds.pe_OnRb);
		group2.add(ds.pe_OffRb);
		ButtonGroup group3 = new ButtonGroup();
		group3.add(ds.relax_OnRb);
		group3.add(ds.relax_OffRb);
		ButtonGroup group4 = new ButtonGroup();
		group4.add(ds.radc_OnRb);
		group4.add(ds.radc_OffRb);
		ButtonGroup group5 = new ButtonGroup();
		group5.add(ds.triplet_OnRb);
		group5.add(ds.triplet_OffRb);
		ButtonGroup group6 = new ButtonGroup();
		group6.add(ds.eii_OnRb);
		group6.add(ds.eii_OffRb);
		group6.add(ds.eii_casnatiRb);
		group6.add(ds.eii_kolbenstvedtRb);
		group6.add(ds.eii_gryzinskiRb);
		ButtonGroup group7 = new ButtonGroup();
		group7.add(ds.photoxRb_default);
		group7.add(ds.photoxRb_epdl);
		group7.add(ds.photoxRb_xcom);

		// Put the radio buttons in a column in a panel.
		JPanel buttP = new JPanel();
		BoxLayout bl = new BoxLayout(buttP, BoxLayout.Y_AXIS);
		buttP.setLayout(bl);
		buttP.add(ds.incoh_OnRb);
		buttP.add(ds.incoh_OffRb);
		buttP.setBorder(getGroupBoxBorder("Bound Compton scattering"));
		ds.incoh_OnRb.setSelected(true);
		ds.incoh_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.incoh_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.incoh_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.incoh_OffRb.setForeground(GammaBetaFrame.foreColor);
		buttP.setBackground(GammaBetaFrame.bkgColor);
		buttP.setPreferredSize(dimP);

		JPanel butt1P = new JPanel();
		bl = new BoxLayout(butt1P, BoxLayout.Y_AXIS);
		butt1P.setLayout(bl);
		butt1P.add(ds.coh_OnRb);
		butt1P.add(ds.coh_OffRb);
		butt1P.setBorder(getGroupBoxBorder("Rayleigh scattering"));
		ds.coh_OnRb.setSelected(true);
		ds.coh_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.coh_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.coh_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.coh_OffRb.setForeground(GammaBetaFrame.foreColor);
		butt1P.setBackground(GammaBetaFrame.bkgColor);
		butt1P.setPreferredSize(dimP);

		JPanel butt2P = new JPanel();
		bl = new BoxLayout(butt2P, BoxLayout.Y_AXIS);
		butt2P.setLayout(bl);
		butt2P.add(ds.pe_OnRb);
		butt2P.add(ds.pe_OffRb);
		butt2P.setBorder(getGroupBoxBorder("Photoelectron angular sampling"));
		ds.pe_OnRb.setSelected(true);
		ds.pe_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.pe_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.pe_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.pe_OffRb.setForeground(GammaBetaFrame.foreColor);
		butt2P.setBackground(GammaBetaFrame.bkgColor);
		butt2P.setPreferredSize(dimP);

		JPanel butt3P = new JPanel();
		bl = new BoxLayout(butt3P, BoxLayout.Y_AXIS);
		butt3P.setLayout(bl);
		butt3P.add(ds.relax_OnRb);
		butt3P.add(ds.relax_OffRb);
		butt3P.setBorder(getGroupBoxBorder("Atomic relaxation"));
		ds.relax_OnRb.setSelected(true);
		ds.relax_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.relax_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.relax_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.relax_OffRb.setForeground(GammaBetaFrame.foreColor);
		butt3P.setBackground(GammaBetaFrame.bkgColor);
		butt3P.setPreferredSize(dimP);

		JPanel butt4P = new JPanel();
		bl = new BoxLayout(butt4P, BoxLayout.Y_AXIS);
		butt4P.setLayout(bl);
		butt4P.add(ds.radc_OnRb);
		butt4P.add(ds.radc_OffRb);
		butt4P.setBorder(getGroupBoxBorder("Radiative Compton effect"));
		ds.radc_OnRb.setSelected(true);
		ds.radc_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.radc_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.radc_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.radc_OffRb.setForeground(GammaBetaFrame.foreColor);
		butt4P.setBackground(GammaBetaFrame.bkgColor);
		butt4P.setPreferredSize(dimP);

		JPanel butt5P = new JPanel();
		bl = new BoxLayout(butt5P, BoxLayout.Y_AXIS);
		butt5P.setLayout(bl);
		butt5P.add(ds.triplet_OnRb);
		butt5P.add(ds.triplet_OffRb);
		butt5P.setBorder(getGroupBoxBorder("Triplet production"));
		ds.triplet_OnRb.setSelected(true);
		ds.triplet_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.triplet_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.triplet_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.triplet_OffRb.setForeground(GammaBetaFrame.foreColor);
		butt5P.setBackground(GammaBetaFrame.bkgColor);
		butt5P.setPreferredSize(dimP);

		JPanel butt6P = new JPanel();
		bl = new BoxLayout(butt6P, BoxLayout.Y_AXIS);
		butt6P.setLayout(bl);
		butt6P.add(ds.eii_OnRb);
		butt6P.add(ds.eii_OffRb);
		butt6P.add(ds.eii_casnatiRb);
		// butt6P.add(ds.eii_kolbenstvedtRb);//wrong datafile!! -125-587 without
		// blank between!!
		butt6P.add(ds.eii_gryzinskiRb);
		butt6P.setBorder(getGroupBoxBorder("Electron impact ionisation"));
		ds.eii_OnRb.setSelected(true);
		ds.eii_OnRb.setBackground(GammaBetaFrame.bkgColor);
		ds.eii_OnRb.setForeground(GammaBetaFrame.foreColor);
		ds.eii_OffRb.setBackground(GammaBetaFrame.bkgColor);
		ds.eii_OffRb.setForeground(GammaBetaFrame.foreColor);
		ds.eii_casnatiRb.setBackground(GammaBetaFrame.bkgColor);
		ds.eii_casnatiRb.setForeground(GammaBetaFrame.foreColor);
		ds.eii_kolbenstvedtRb.setBackground(GammaBetaFrame.bkgColor);
		ds.eii_kolbenstvedtRb.setForeground(GammaBetaFrame.foreColor);
		ds.eii_gryzinskiRb.setBackground(GammaBetaFrame.bkgColor);
		ds.eii_gryzinskiRb.setForeground(GammaBetaFrame.foreColor);
		butt6P.setBackground(GammaBetaFrame.bkgColor);
		butt6P.setPreferredSize(dimPp);

		JPanel butt7P = new JPanel();
		bl = new BoxLayout(butt7P, BoxLayout.Y_AXIS);
		butt7P.setLayout(bl);
		butt7P.add(ds.photoxRb_default);
		butt7P.add(ds.photoxRb_epdl);
		butt7P.add(ds.photoxRb_xcom);
		butt7P.setBorder(getGroupBoxBorder("Photon cross sections"));
		ds.photoxRb_default.setSelected(true);
		ds.photoxRb_default.setBackground(GammaBetaFrame.bkgColor);
		ds.photoxRb_default.setForeground(GammaBetaFrame.foreColor);
		ds.photoxRb_epdl.setBackground(GammaBetaFrame.bkgColor);
		ds.photoxRb_epdl.setForeground(GammaBetaFrame.foreColor);
		ds.photoxRb_xcom.setBackground(GammaBetaFrame.bkgColor);
		ds.photoxRb_xcom.setForeground(GammaBetaFrame.foreColor);
		butt7P.setBackground(GammaBetaFrame.bkgColor);
		butt7P.setPreferredSize(dimPp);

		JPanel mainP = new JPanel(new BorderLayout());

		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel("Pair angular sampling");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.paCb);
		label = new JLabel("Brems angular sampling");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.baCb);
		d11P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d12P = new JPanel();
		d12P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Pair cross sections");
		d12P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d12P.add(ds.pcsCb);
		label = new JLabel("Brems cross sections");
		d12P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d12P.add(ds.bcsCb);
		d12P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Electron-step algorithm");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.esaCb);
		label = new JLabel("Boundary crossing algorithm");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.bcaCb);
		ds.spinCh.setBackground(GammaBetaFrame.bkgColor);
		ds.spinCh.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.spinCh);
		d1P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d2P.add(buttP);
		d2P.add(butt1P);
		d2P.add(butt2P);
		d2P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d22P = new JPanel();
		d22P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d22P.add(butt3P);
		d22P.add(butt4P);
		d22P.add(butt5P);
		d22P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d23P = new JPanel();
		d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d23P.add(butt6P);
		d23P.add(butt7P);
		d23P.setBackground(GammaBetaFrame.bkgColor);

		JPanel northP = new JPanel(new BorderLayout());
		BoxLayout bls = new BoxLayout(northP, BoxLayout.Y_AXIS);
		northP.setLayout(bls);
		northP.add(d11P, null);
		northP.add(d12P, null);
		northP.add(d1P, null);
		northP.add(d2P, null);
		northP.add(d22P, null);
		northP.add(d23P, null);
		northP.setBackground(GammaBetaFrame.bkgColor);

		mainP.add(northP, BorderLayout.CENTER);
		mainP.setBackground(GammaBetaFrame.bkgColor);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(mainP), BorderLayout.CENTER);
		main2P.setBackground(GammaBetaFrame.bkgColor);
		return main2P;// mainP;
	}

	/**
	   * Create variance reduction panel
	   * @return the result
	   */
	protected JPanel createVarRedPanel() {
		ds.forcingCh.setBackground(GammaBetaFrame.bkgColor);
		ds.forcingCh.setForeground(GammaBetaFrame.foreColor);
		ds.irejectCh.setBackground(GammaBetaFrame.bkgColor);
		ds.irejectCh.setForeground(GammaBetaFrame.foreColor);
		ds.RRCh.setBackground(GammaBetaFrame.bkgColor);
		ds.RRCh.setForeground(GammaBetaFrame.foreColor);
		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel("Photon splitting ");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.photonsplitSp);
		label = new JLabel("Photon cross section enhancement ");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.csenhancementSp);
		d11P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d1P.add(ds.forcingCh);
		label = new JLabel("Start forcing ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.startforcingSp);
		label = new JLabel("Stop forcing ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ds.stopforcingSp);
		d1P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d12P = new JPanel();
		d12P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Photon pathlength biasing parameter C ");
		d12P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d12P.add(ds.cbiasTf);
		d12P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Russian Roulette (photons) ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		label = new JLabel("Z Depth ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.zdepthTf);
		label = new JLabel("Fraction");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(ds.zfractionTf);
		d2P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d22P = new JPanel();
		d22P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d22P.add(ds.irejectCh);
		label = new JLabel("ESAVEIN ");
		d22P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d22P.add(ds.esaveinTf);
		d22P.setBackground(GammaBetaFrame.bkgColor);

		JPanel d23P = new JPanel();
		d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Number of brems per event ");
		d23P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d23P.add(ds.bremsplitSp);
		d23P.add(ds.RRCh);
		d23P.setBackground(GammaBetaFrame.bkgColor);

		JPanel northP = new JPanel(new BorderLayout());
		BoxLayout bls = new BoxLayout(northP, BoxLayout.Y_AXIS);
		northP.setLayout(bls);
		northP.add(d11P, null);
		northP.add(d1P, null);
		northP.add(d12P, null);
		northP.add(d2P, null);
		northP.add(d22P, null);
		northP.add(d23P, null);
		northP.setBackground(GammaBetaFrame.bkgColor);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(northP, BorderLayout.CENTER);
		mainP.setBackground(GammaBetaFrame.bkgColor);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(mainP), BorderLayout.CENTER);
		main2P.setBackground(GammaBetaFrame.bkgColor);
		return main2P;// mainP;
	}

	/**
	   * Create result panel
	   * @return the result
	   */
	protected JPanel createOutputPanel() {
		JPanel resultP = new JPanel(new BorderLayout());
		//JScrollPane jspres = new JScrollPane();
		resultP.add(new JScrollPane(ds.simTa), BorderLayout.CENTER);
		resultP.setPreferredSize(textAreaDimension);
		resultP.setBackground(GammaBetaFrame.bkgColor);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(resultP, BorderLayout.CENTER);// main dimension !!
		mainP.setBackground(GammaBetaFrame.bkgColor);
		return mainP;
	}

}
