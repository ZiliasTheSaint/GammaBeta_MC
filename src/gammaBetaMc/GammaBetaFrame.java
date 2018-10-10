package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import org.apache.pdfbox.pdmodel.PDDocument;

import danfulea.utils.ExampleFileFilter;
import danfulea.utils.ScanDiskLFGui;
import danfulea.utils.SystemInfo;
import danfulea.math.Convertor;
import danfulea.math.RandomCollection;
import danfulea.phys.GammaDetector;
import danfulea.phys.egs.EGS4;
import danfulea.phys.egs.EGS4Core;
import danfulea.phys.egs.EGS4Geom;
import danfulea.phys.egs.EGS4Grid;
import danfulea.phys.egs.EGS4Macro;
import danfulea.phys.egs.EGS4SrcEns;
import danfulea.phys.egsOutput.BremsApp2;
import danfulea.phys.egsOutput.FluenceRz;
import danfulea.phys.egsOutput.GammaDetA;
import danfulea.phys.egsOutput.GammaDetEffA;
import danfulea.phys.egsOutput.SpwrRz;
import danfulea.utils.FrameUtilities;
import danfulea.utils.PDFRenderer;

/**
 * Main class for computing detector efficiency by Monte Carlo method 
 * using EGSnrc engine or Geant4 engine! It is also used for computing dose, fluence 
 * and stopping power in RZ geometry. Basically, it is GUI interface for GammaDetEffA, 
 * GammaDetA, SpwrRz, FluenceEz and BremsApp2.
 * 
 * @author Dan Fulea, 11 Jul. 2005
 */
@SuppressWarnings("serial")
public class GammaBetaFrame extends JFrame implements ActionListener,
		ItemListener, Runnable {

	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c =Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(c,2);
	
	private static final Dimension PREFERRED_SIZE = new Dimension(990, 700);
	//private final Dimension sizeCb = new Dimension(100, 20);
	private Window parent = null;
	private boolean standalone = true;
	public static Color bkgColor = new Color(230, 255, 210, 255);// Linux mint
	public static Color foreColor = Color.black;// Color.white;
	public static Color textAreaBkgColor = Color.white;// Color.black;
	public static Color textAreaForeColor = Color.black;// Color.yellow;
	public static boolean showLAF = true;

	private static final String BASE_RESOURCE_CLASS = "gammaBetaMc.resources.GammaBetaFrameResources";
	protected ResourceBundle resources;
	private static final String EXIT_COMMAND = "EXIT";
	private static final String ABOUT_COMMAND = "ABOUT";
	private static final String RUN_COMMAND = "RUN";
	private static final String KILL_COMMAND = "KILL";
	private static final String LOOKANDFEEL_COMMAND = "LOOKANDFEEL";
	private static final String CREATE_MATERIAL = "CREATE_MATERIAL";
	private static final String EFF_UTILITIES = "EFF_UTILITIES";
	private static final String NUCLIDE_INTERFERENCES = "NUCLIDE_INTERFERENCES";
	private static final String KN_ALGOR = "KN_ALGOR";
	private static final String COIN_CORR = "COIN_CORR";
	private static final String G_CALC = "G_CALC";
	private static final String GESHYBRID_CALC = "GESHYBRID_CALC";
	private static final String BOXAPP_CALC = "BOXAPP_CALC";

	private static final String SAVEDET_COMMAND = "SAVEDET";
	private static final String LOADDET_COMMAND = "LOADDET";
	private static final String SAVESOURCE_COMMAND = "SAVESOURCE";
	private static final String LOADSOURCE_COMMAND = "LOADSOURCE";

	private static final String PHOTONFORCING_COMMAND = "PHOTONFORCING";
	private static final String IREJECT_COMMAND = "IREJECT";
	private static final String ALCHOOSE_COMMAND = "ALCHOOSE";
	private static final String AIRCHOOSE_COMMAND = "AIRCHOOSE";
	private static final String NAICHOOSE_COMMAND = "NAICHOOSE";
	private static final String POINTSTOD_COMMAND = "POINTSTODCHOOSE";
	private static final String SMATCHOOSE_COMMAND = "SMATCHOOSE";
	private static final String EMATCHOOSE_COMMAND = "EMATCHOOSE";
	private static final String WINDOWCHOOSE_COMMAND = "WINDOWCHOOSE";
	private static final String SPECTRUM_COMMAND = "SPECTRUM";
	private static final String CUSTOM_COMMAND = "CUSTOM";
	
	private static final String PRINT_COMMAND = "PRINT";

	private String command = "";

	private boolean stopAnim = true;
	//private boolean STOPCOMPUTATION = false;
	private boolean stopAppend = false;

	private JLabel statusL = new JLabel("Waiting...");
	private volatile Thread simTh = null;
	private volatile Thread statusTh = null;// status display thread!
	private int delay = 100;
	private int frameNumber = -1;
	private String statusRunS = "";
	protected JTextArea simTa = new JTextArea();

	// ================
	JTabbedPane tabs;
	protected JTextField ethreshTf = new JTextField(5);
	protected JTextField sddistTf = new JTextField(5);
	protected JTextField pointstodmaterialTf = new JTextField(25);// !!!!!!!
	protected JTextField sourcematerialTf = new JTextField(25);// !!!!!!!
	protected JTextField envelopematerialTf = new JTextField(25);// !!!!!!!
	protected JButton pointstodchooseB = new JButton();
	protected JButton smatchooseB = new JButton();
	protected JButton ematchooseB = new JButton();
	protected JButton windowchooseB = new JButton();
	protected JTextField windowmaterialTf = new JTextField(25);// !!!!!!!
	protected JTextField windowThickTf = new JTextField(5);// !!!!!!!
	protected JTextField envelopeThickTf = new JTextField(5);// !!!!!!!
	protected JTextField statTf = new JTextField(5);
	protected JTextField sloteTf = new JTextField(5);
	protected JTextField deltaeTf = new JTextField(5);
	protected JTextField rbeamTf = new JTextField(5);
	protected JTextField anglebeamTf = new JTextField(5);
	protected JCheckBox fanoCh;
	protected JCheckBox kermaCh;
	@SuppressWarnings("rawtypes")
	protected JComboBox calculationCb, einCb;
	protected JButton spectrumB = new JButton();
	protected JTextField spectrumTf = new JTextField(25);
	public double althick = 0.0;
	public double hdet = 0.0;
	public double hdet_tot = 0.0;
	public double adet = 0.0;
	public double adet_tot = 0.0;
	public double inac_thick_sup = 0.0;
	public double winthick = 0.0;// window entrance
	public double envthick = 0.0;// envelope
	public double asource = 0.0;
	public double bsource = 0.0;
	public double hsource = 0.0;
	public double hsourceup = 0.0;
	public double stddist = 0.0;
	public double rbeam = 0.0;
	public double anglebeam = 0.0;
	protected JTextField althickTf = new JTextField(5);
	protected JTextField almaterialTf = new JTextField(25);
	protected JButton alchooseB = new JButton();
	protected JTextField airmaterialTf = new JTextField(25);
	protected JButton airchooseB = new JButton();
	protected JTextField naimaterialTf = new JTextField(25);
	protected JButton naichooseB = new JButton();

	protected JButton startsimB = new JButton();
	protected JButton stopsimB = new JButton();
	protected JButton printB = new JButton();
	//private static final Dimension aboutDim = new Dimension(800, 500);
	//private final String FILESEPARATOR = System.getProperty("file.separator");
	@SuppressWarnings("rawtypes")
	protected JComboBox sourceTypeCb;// source type-0=Cylinder, 1=Marinelli
	@SuppressWarnings("rawtypes")
	protected JComboBox sourceEqCb;// source equivalent composition 0=water,
									// 1=NaCl
	protected JTextField asourceTf = new JTextField(5);// diam exterior sursa
	protected JTextField bsourceTf = new JTextField(5);// diam interior sursa
	protected JTextField hsourceTf = new JTextField(5);// inaltime totala sursa
	protected JTextField hsourceupTf = new JTextField(5);// inaltime superioara
															// sursa
	protected JTextField hdetTf = new JTextField(5);// inaltime detector
	protected JTextField adetTf = new JTextField(5);// diametru
													// detector---------all in
													// cm
	protected JTextField hdetTotTf = new JTextField(5);
	protected JTextField adetTotTf = new JTextField(5);
	protected JTextField hUpTf = new JTextField(5);
	protected JTextField energyTf = new JTextField(5);// energie photon --MeV
	protected JButton saveSB = new JButton();
	protected JButton saveDB = new JButton();
	protected JButton loadSB = new JButton();
	protected JButton loadDB = new JButton();
	protected JCheckBox atenuareCh;// attenuare in sursa
	@SuppressWarnings("rawtypes")
	protected JComboBox nPhotonsCb;// nr of photons in sim
	protected JRadioButton photonRb, electronRb, positronRb;
	protected JCheckBox createFileCh;// attenuare in sursa
	@SuppressWarnings("rawtypes")
	protected JComboBox paCb, baCb, bcsCb, esaCb, bcaCb, pcsCb, printCb, rngCb;
	protected JCheckBox spinCh;
	protected JRadioButton incoh_OnRb, pe_OnRb, coh_OnRb, relax_OnRb;
	protected JRadioButton incoh_OffRb, pe_OffRb, coh_OffRb, relax_OffRb;
	protected JRadioButton radc_OnRb, triplet_OnRb, eii_OnRb;
	protected JRadioButton radc_OffRb, triplet_OffRb, eii_OffRb,
			photoxRb_default, photoxRb_epdl, photoxRb_xcom, eii_casnatiRb,
			eii_kolbenstvedtRb, eii_gryzinskiRb;
	protected JCheckBox forcingCh, irejectCh, RRCh;
	protected JSpinner photonsplitSp, startforcingSp, stopforcingSp,
			csenhancementSp, bremsplitSp;
	protected JTextField cbiasTf = new JTextField(5);
	protected JTextField zdepthTf = new JTextField(5);
	protected JTextField zfractionTf = new JTextField(5);
	protected JTextField esaveinTf = new JTextField(5);
	protected JTextField filenameeffTf = new JTextField(25);
	protected JCheckBox saveeffCh;
	// private AboutFrame aboutFrame;
	public static boolean noSup = false;// from ch!!
	@SuppressWarnings("rawtypes")
	protected JComboBox customCb;
	protected JButton customB;

	// ============
	protected String outFilename = null;
	
	private boolean isRunning=false;
	
	protected JCheckBox geantCh;//
	protected JCheckBox graphicSceneAutoRefreshCh;
	private static String filenameMC = "MonteCarloPath.txt";//
	private static String detExeName= "detector.exe";//
	private static String detExeNameLinux= "detector";//
	protected static String detExeFileName= "detector";
	protected String detFolderURL;//
	protected String macroFilename;
	protected File macroFile;
	private boolean useGeant = false;
	private double geff = 0.0;
	private double geff_unc =0.0;
	private double gefft=0.0;
	private double gefft_unc=0.0;

	/**
	 * Constructor... setting up the application GUI!
	 */
	public GammaBetaFrame() {
		// TODO Auto-generated constructor stub
		// super("Alpha_MC");

		resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
		this.setTitle(this.resources.getString("Application.NAME"));// ("Application.name"));

		noSup = false;

		// the key to force attemptExit() method on close!!
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				attemptExit();
			}
		});

		JMenuBar menuBar = createMenuBar(resources);
		setJMenuBar(menuBar);

		createGUI();
		setDefaultLookAndFeelDecorated(true);
		FrameUtilities.createImageIcon(
				this.resources.getString("form.icon.url"), this);

		FrameUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	/**
	 * Constructor when linked to another GUI program.
	 * @param frame frame
	 */
	public GammaBetaFrame(Window frame) {
		this();
		this.parent = frame;
		standalone = false;
	}

	/**
	 * Setting up the frame size.
	 */
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
	}

	/**
	 * Setting up the menu bar.
	 * 
	 * @param resources resources
	 * @return the menu bar
	 */
	private JMenuBar createMenuBar(ResourceBundle resources) {
		ImageIcon img;
		String imageName = "";
		// create the menus
		JMenuBar menuBar = new JMenuBar();

		String label;
		Character mnemonic;

		// first the file menu
		label = resources.getString("menu.file");
		mnemonic = (Character) resources.getObject("menu.file.mnemonic");
		JMenu fileMenu = new JMenu(label, true);
		fileMenu.setMnemonic(mnemonic.charValue());
		
		label = "Create material";
        mnemonic = new Character('m');
        JMenuItem cmItem = new JMenuItem(label, mnemonic.charValue());
        cmItem.setActionCommand(CREATE_MATERIAL);
        cmItem.addActionListener(this);
        fileMenu.add(cmItem);

        label = "Efficiency utilities";
        mnemonic = new Character('E');
        JMenuItem euItem = new JMenuItem(label, mnemonic.charValue());
        euItem.setActionCommand(EFF_UTILITIES);
        euItem.addActionListener(this);
        fileMenu.add(euItem);

        label = "PDF print results";
        mnemonic = new Character('P');
        JMenuItem pdfItem = new JMenuItem(label, mnemonic.charValue());
        pdfItem.setActionCommand(PRINT_COMMAND);
        pdfItem.addActionListener(this);
        fileMenu.add(pdfItem);

        
        
        label = "Tools";
		mnemonic = new Character('T');;
		JMenu toolsMenu = new JMenu(label);
		toolsMenu.setMnemonic(mnemonic.charValue());
		
		label = "Nuclide interferences";
        mnemonic = new Character('N');
        JMenuItem niItem = new JMenuItem(label, mnemonic.charValue());
        niItem.setActionCommand(NUCLIDE_INTERFERENCES);
        niItem.addActionListener(this);
        toolsMenu.add(niItem);

        label = "Coincidence correction";
        mnemonic = new Character('C');
        JMenuItem ccItem = new JMenuItem(label, mnemonic.charValue());
        ccItem.setActionCommand(COIN_CORR);
        ccItem.addActionListener(this);
        toolsMenu.add(ccItem);
        
        label = "g-Kerma,mu_tr,mu_en";
        mnemonic = new Character('g');
        JMenuItem gcItem = new JMenuItem(label, mnemonic.charValue());
        gcItem.setActionCommand(G_CALC);
        gcItem.addActionListener(this);
        toolsMenu.add(gcItem);
        
        label = "Box application";
        mnemonic = new Character('B');
        JMenuItem boxItem = new JMenuItem(label, mnemonic.charValue());
        boxItem.setActionCommand(BOXAPP_CALC);
        boxItem.addActionListener(this);
        toolsMenu.add(boxItem);
        
        label = "Quick gamma global (analytical-Monte Carlo)";
        mnemonic = new Character('Q');
        JMenuItem geshItem = new JMenuItem(label, mnemonic.charValue());
        geshItem.setActionCommand(GESHYBRID_CALC);
        geshItem.addActionListener(this);
        toolsMenu.add(geshItem);
        
        //KN_ALGOR
        label = "Klein-Nishina algorithms test";
        mnemonic = new Character('K');
        JMenuItem knItem = new JMenuItem(label, mnemonic.charValue());
        knItem.setActionCommand(KN_ALGOR);
        knItem.addActionListener(this);
        toolsMenu.add(knItem);
		
		// imageName = resources.getString("img.set");
		// img = FrameUtilities.getImageIcon(imageName, this);
		// label = resources.getString("runB");
		// mnemonic = (Character) resources.getObject("runB.mnemonic");
		// JMenuItem runItem = new JMenuItem(label, mnemonic.charValue());
		// runItem.setActionCommand(RUN_COMMAND);
		// runItem.setIcon(img);
		// runItem.addActionListener(this);
		// fileMenu.add(runItem);

		// imageName = resources.getString("img.close");
		// img = FrameUtilities.getImageIcon(imageName, this);
		// label = resources.getString("killB");
		// mnemonic = (Character) resources.getObject("killB.mnemonic");
		// JMenuItem killItem = new JMenuItem(label, mnemonic.charValue());
		// killItem.setActionCommand(KILL_COMMAND);
		// killItem.setIcon(img);
		// killItem.addActionListener(this);
		// fileMenu.add(killItem);

		imageName = resources.getString("img.close");
		img = FrameUtilities.getImageIcon(imageName, this);
		label = resources.getString("menu.file.exit");
		mnemonic = (Character) resources.getObject("menu.file.exit.mnemonic");
		JMenuItem exitItem = new JMenuItem(label, mnemonic.charValue());
		exitItem.setActionCommand(EXIT_COMMAND);
		exitItem.setIcon(img);
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);

		// then the help menu
		label = resources.getString("menu.help");
		mnemonic = (Character) resources.getObject("menu.help.mnemonic");
		JMenu helpMenu = new JMenu(label);
		helpMenu.setMnemonic(mnemonic.charValue());

		label = resources.getString("menu.help.LF");
		mnemonic = (Character) resources.getObject("menu.help.LF.mnemonic");
		JMenuItem lfItem = new JMenuItem(label, mnemonic.charValue());
		lfItem.setActionCommand(LOOKANDFEEL_COMMAND);
		lfItem.addActionListener(this);
		lfItem.setToolTipText(resources.getString("menu.help.LF.toolTip"));

		if (showLAF) {
			helpMenu.add(lfItem);
			helpMenu.addSeparator();
		}

		imageName = resources.getString("img.about");
		img = FrameUtilities.getImageIcon(imageName, this);
		label = resources.getString("menu.help.about");
		mnemonic = (Character) resources.getObject("menu.help.about.mnemonic");
		JMenuItem aboutItem = new JMenuItem(label, mnemonic.charValue());
		aboutItem.setActionCommand(ABOUT_COMMAND);
		aboutItem.setIcon(img);
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);

		// finally, glue together the menu and return it
		menuBar.add(fileMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);

		return menuBar;
	}

	/**
	 * Setting up the status bar.
	 * 
	 * @param toolBar toolBar
	 */
	private void initStatusBar(JToolBar toolBar) {
		JPanel toolP = new JPanel();
		toolP.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 1));

		toolP.add(statusL);
		toolBar.add(toolP);
		statusL.setText(resources.getString("status.wait"));
	}

	/**
	 * GUI creation.
	 */
	private void createGUI() {
		GammaDetEffA.jta = simTa;
		GammaDetA.jta = simTa;
		FluenceRz.jta = simTa;
		SpwrRz.jta = simTa;
		BremsApp2.jta = simTa;

		JPanel content = new JPanel(new BorderLayout());

		tabs = createTabs();
		content.add(tabs, BorderLayout.CENTER);

		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 2));
		d11P.add(startsimB);
		d11P.add(stopsimB);
		d11P.add(printB);
		d11P.setBackground(bkgColor);
		
		startsimB.setActionCommand(RUN_COMMAND);
		startsimB.addActionListener(this);
		stopsimB.setActionCommand(KILL_COMMAND);
		stopsimB.addActionListener(this);
		
		printB.setActionCommand(PRINT_COMMAND);
		printB.addActionListener(this);
		
		saveSB.setActionCommand(SAVESOURCE_COMMAND);
		saveSB.addActionListener(this);
		saveDB.setActionCommand(SAVEDET_COMMAND);
		saveDB.addActionListener(this);
		loadSB.setActionCommand(LOADSOURCE_COMMAND);
		loadSB.addActionListener(this);
		loadDB.setActionCommand(LOADDET_COMMAND);
		loadDB.addActionListener(this);
		forcingCh.setActionCommand(PHOTONFORCING_COMMAND);
		forcingCh.addActionListener(this);
		irejectCh.setActionCommand(IREJECT_COMMAND);
		irejectCh.addActionListener(this);
		alchooseB.setActionCommand(ALCHOOSE_COMMAND);
		alchooseB.addActionListener(this);
		airchooseB.setActionCommand(AIRCHOOSE_COMMAND);
		airchooseB.addActionListener(this);
		naichooseB.setActionCommand(NAICHOOSE_COMMAND);
		naichooseB.addActionListener(this);
		pointstodchooseB.setActionCommand(POINTSTOD_COMMAND);
		pointstodchooseB.addActionListener(this);
		smatchooseB.setActionCommand(SMATCHOOSE_COMMAND);
		smatchooseB.addActionListener(this);
		ematchooseB.setActionCommand(EMATCHOOSE_COMMAND);
		ematchooseB.addActionListener(this);
		windowchooseB.setActionCommand(WINDOWCHOOSE_COMMAND);
		windowchooseB.addActionListener(this);
		spectrumB.setActionCommand(SPECTRUM_COMMAND);
		spectrumB.addActionListener(this);
		customB.setActionCommand(CUSTOM_COMMAND);
		customB.addActionListener(this);		
		
		calculationCb.addItemListener(this);
		sourceTypeCb.addItemListener(this);
		einCb.addItemListener(this);
		
		//THE KEY TO PROPER DISPLAY FORMATTED STRING WITHIN TEXTAREA:!!!
		Font font = new Font("Monospaced", Font.PLAIN, 12);
		simTa.setFont(font);
		
		content.add(d11P, BorderLayout.SOUTH);
		
		JPanel content0 = new JPanel(new BorderLayout());
		content0.add(content, BorderLayout.CENTER);

		// Create the statusbar.
		JToolBar statusBar = new JToolBar();
		statusBar.setFloatable(false);
		initStatusBar(statusBar);
		content0.add(statusBar, BorderLayout.PAGE_END);

		setContentPane(new JScrollPane(content0));
		content0.setOpaque(true); // content panes must be opaque
		pack();
	}

	/**
	 * Create tabs
	 * @return the result
	 */
	private JTabbedPane createTabs() {

		JTabbedPane tabs = new JTabbedPane();

		GesGUI dsg = new GesGUI(this);
		JPanel dsPan = dsg.createSimMainPanel();
		JPanel geoPan = dsg.createGeometryPanel();
		JPanel trPan = dsg.createTransportPanel();
		JPanel varPan = dsg.createVarRedPanel();
		JPanel outPan = dsg.createOutputPanel();

		dsPan.setBorder(GammaBetaFrame.STANDARD_BORDER);
		geoPan.setBorder(GammaBetaFrame.STANDARD_BORDER);
		trPan.setBorder(GammaBetaFrame.STANDARD_BORDER);
		varPan.setBorder(GammaBetaFrame.STANDARD_BORDER);
		outPan.setBorder(GammaBetaFrame.STANDARD_BORDER);

		String s = "General Monte Carlo";
		tabs.add(s, dsPan);
		s = "Geometry settings";
		tabs.add(s, geoPan);
		s = "Transport parameters";
		tabs.add(s, trPan);
		s = "Variance reduction";
		tabs.add(s, varPan);
		s = "Results";
		tabs.add(s, outPan);

		return tabs;

	}
	
	/**
	 * Print the simluation results
	 */
	private void printResults(){
		String FILESEPARATOR = System.getProperty("file.separator");
		String currentDir = System.getProperty("user.dir");
		File infile = null;

		String ext = resources.getString("file.extension");
		String pct = ".";
		String description = resources.getString("file.description");
		ExampleFileFilter eff = new ExampleFileFilter(ext, description);

		String myDir = currentDir + FILESEPARATOR;//
		// File select
		JFileChooser chooser = new JFileChooser(myDir);
		chooser.addChoosableFileFilter(eff);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showSaveDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			infile = chooser.getSelectedFile();
			outFilename = chooser.getSelectedFile().toString();

			int fl = outFilename.length();
			String test = outFilename.substring(fl - 4);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				outFilename = chooser.getSelectedFile().toString() + pct + ext;

			if (infile.exists()) {
				String title = resources.getString("dialog.overwrite.title");
				String message = resources
						.getString("dialog.overwrite.message");

				Object[] options = (Object[]) resources
						.getObject("dialog.overwrite.buttons");
				int result = JOptionPane
						.showOptionDialog(this, message, title,
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				if (result != JOptionPane.YES_OPTION) {
					return;
				}

			}

			//new GammaBetaMcReport(this);
			performPrintReport();
			statusL.setText(resources.getString("status.save") + outFilename);
		} else {
			return;
		}
	}

	/**
	 * Actual pdf renderer is called here. Called by printReport.
	 */
	public void performPrintReport(){
		PDDocument doc = new PDDocument();
		PDFRenderer renderer = new PDFRenderer(doc);
		try{
			renderer.setTitle(resources.getString("pdf.content.title"));
			renderer.setSubTitle(
					resources.getString("pdf.content.subtitle")+
					resources.getString("pdf.metadata.author")+ ", "+
							new Date());
						
			String str = " \n" + simTa.getText();
		
			//renderer.renderTextHavingNewLine(str);//works!!!!!!!!!!!!!!!!
			renderer.renderTextEnhanced(str);
			
			renderer.addPageNumber();
			renderer.close();		
			doc.save(new File(outFilename));
			doc.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Call GammaGlobal_hybrid.
	 */
	private void gesHybrid() {
		new GammaGlobal_hybrid(this);
	}

	/**
	 * Call BoxApp
	 */
	private void boxApp() {
		new BoxApp(this);
	}

	/**
	 * Setting up actions!
	 */
	public void actionPerformed(ActionEvent arg0) {
		
		// TODO Auto-generated method stub
		//String command = arg0.getActionCommand();
		command = arg0.getActionCommand();
		if (command.equals(ABOUT_COMMAND)) {
			about();
		} else if (command.equals(EXIT_COMMAND)) {
			attemptExit();
		} else if (command.equals(RUN_COMMAND)) {
			//System.out.println("enter");
			statusRunS = resources.getString("status.computing");
			startSimulation();
		} else if (command.equals(KILL_COMMAND)) {
			stopAppend = true;
			stopSimulation();
		} else if (command.equals(LOOKANDFEEL_COMMAND)) {
			lookAndFeel();
		} else if (command.equals(SAVESOURCE_COMMAND)) {
			saveSource();
		} else if (command.equals(SAVEDET_COMMAND)) {
			saveDet();
		} else if (command.equals(LOADSOURCE_COMMAND)) {
			loadSource();
		} else if (command.equals(LOADDET_COMMAND)) {
			loadDet();
		} else if (command.equals(PHOTONFORCING_COMMAND)) {
			photonforcing();
		} else if (command.equals(IREJECT_COMMAND)) {
			erange();
		} else if (command.equals(ALCHOOSE_COMMAND)) {
			selectMaterial(0);
		} else if (command.equals(AIRCHOOSE_COMMAND)) {
			selectMaterial(1);
		} else if (command.equals(NAICHOOSE_COMMAND)) {
			selectMaterial(2);
		} else if (command.equals(POINTSTOD_COMMAND)) {
			selectMaterial(3);
		} else if (command.equals(SMATCHOOSE_COMMAND)) {
			selectMaterial(4);
		} else if (command.equals(EMATCHOOSE_COMMAND)) {
			selectMaterial(5);
		} else if (command.equals(WINDOWCHOOSE_COMMAND)) {
			selectMaterial(6);
		} else if (command.equals(SPECTRUM_COMMAND)) {
			chooseSpectrum();
		} else if (command.equals(CUSTOM_COMMAND)) {
			customApp();
		} else if (command.equals(CREATE_MATERIAL)) {
			createMaterial();
		} else if (command.equals(EFF_UTILITIES)) {
			effUtilities();
		} else if (command.equals(NUCLIDE_INTERFERENCES)) {
			interf();
		} else if (command.equals(KN_ALGOR)) {
			kn_algor_test();
		} else if (command.equals(COIN_CORR)) {
			coinCorr();
		} else if (command.equals(G_CALC)) {
			gCalc();
		} else if (command.equals(GESHYBRID_CALC)) {
			gesHybrid();
		} else if (command.equals(BOXAPP_CALC)) {
			boxApp();
		} else if (command.equals(PRINT_COMMAND)) {
			printResults();
		}
	}

	/**
	 * More actions are defined here.
	 */
	public void itemStateChanged(ItemEvent ie) {
		if (ie.getSource() == calculationCb) {
			// calculationCb.setToolTipText((String)uMedCb.getSelectedItem());
			ifullSet();
		}

		if (ie.getSource() == sourceTypeCb) {
			setSource();
		}
		if (ie.getSource() == einCb) {
			setEin();
		}

	}

	/**
	 * Change look and feel. You can also look for files in computer, play with colors 
	 * and see some gadgets. 
	 */
	private void lookAndFeel() {
		setVisible(false);// setEnabled(false);
		new ScanDiskLFGui(this);
	}

	/**
	 * Shows the about window!
	 */
	private void about() {
		new AboutFrame(this);
	}

	/**
	 * Close program
	 */
	private void attemptExit() {
		// String title = resources.getString("dialog.exit.title");
		// String message = resources.getString("dialog.exit.message");

		// Object[] options = (Object[]) resources
		// .getObject("dialog.exit.buttons");
		// int result = JOptionPane.showOptionDialog(this, message, title,
		// JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
		// options, options[0]);
		// if (result == JOptionPane.YES_OPTION) {
		// dispose();
		// System.exit(0);
		// }
		if (standalone) {
			dispose();
			System.exit(0);
		} else {
			parent.setVisible(true);
			dispose();
		}
	}

	/**
	 * Perform some custom application, i.e. the radiological program for evaluating 
	 * the secondary X-Rays emerged from patient when they are undergoing radiographic exams. 
	 * These kind of radiations is of concern for medical staff. 
	 */
	private void customApp() {
		if (isRunning){
		//	return;//not necessary here!!
		}
		int index = customCb.getSelectedIndex();

		if (index == 0) {
			new SecondaryEval(this);
		}

	}

	/**
	 * Choose a spectrum.
	 */
	private void chooseSpectrum() {
		String ext = "spectrum";// resources.getString("src.load");
		String pct = ".";
		String description = "Spectrum file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");
		String datas = "Data" + file_sep + "egs" + file_sep + "spectra";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String opens = currentDir + file_sep + datas;
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filename = chooser.getSelectedFile().toString();
			int ifs = 0;
			int indx = 0;
			while (ifs < filename.length()) {
				String s = filename.substring(ifs, ifs + 1);
				if (s.compareTo(file_sep) == 0) {
					indx = ifs;
				}
				ifs++;
			}
			// filename="air521.pegs4dat";
			int fl = filename.length();
			String test = filename.substring(fl - 9);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) == 0)// with extension
			{
				// filename=chooser.getSelectedFile().toString()+pct+ext;
				filename = filename.substring(indx + 1, fl - 9);// exstension
																// lookup!!
			} else {
				filename = filename.substring(indx + 1);
			}

			spectrumTf.setText(filename);
		}

	}

	/**
	 * Handle energy related controls.
	 */
	private void setEin() {
		int index = einCb.getSelectedIndex();
		spectrumB.setEnabled(false);
		energyTf.setEnabled(false);

		if (index == 0) {
			energyTf.setEnabled(true);
		}

		if (index == 1) {
			spectrumB.setEnabled(true);
		}

	}

	/**
	 * Handle source related controls.
	 */
	private void setSource() {
		int index = sourceTypeCb.getSelectedIndex();
		sddistTf.setEnabled(false);
		hsourceupTf.setEnabled(false);
		bsourceTf.setEnabled(false);
		asourceTf.setEnabled(false);
		hsourceTf.setEnabled(false);
		// pointstodchooseB.setEnabled(false);
		smatchooseB.setEnabled(false);
		ematchooseB.setEnabled(false);
		envelopeThickTf.setEnabled(false);
		rbeamTf.setEnabled(false);
		anglebeamTf.setEnabled(false);

		if (index == 0) {
			sddistTf.setEnabled(true);
			// pointstodchooseB.setEnabled(true);
		}
		if (index == 1) {
			asourceTf.setEnabled(true);
			hsourceTf.setEnabled(true);

			smatchooseB.setEnabled(true);
			ematchooseB.setEnabled(true);
			envelopeThickTf.setEnabled(true);
		}
		if (index == 2) {
			asourceTf.setEnabled(true);
			hsourceTf.setEnabled(true);
			hsourceupTf.setEnabled(true);
			bsourceTf.setEnabled(true);

			smatchooseB.setEnabled(true);
			ematchooseB.setEnabled(true);
			envelopeThickTf.setEnabled(true);
		}
		if (index == 3) {
			sddistTf.setEnabled(true);
			rbeamTf.setEnabled(true);
			anglebeamTf.setEnabled(true);
		}

	}

	/**
	 * Handle photon-forcing controls.
	 */
	private void photonforcing() {
		startforcingSp.setEnabled(false);
		stopforcingSp.setEnabled(false);
		if (forcingCh.isSelected()) {
			startforcingSp.setEnabled(true);
			stopforcingSp.setEnabled(true);
		}
	}

	/**
	 * Handle e-range controls.
	 */
	private void erange() {
		esaveinTf.setEnabled(false);
		if (irejectCh.isSelected()) {
			esaveinTf.setEnabled(true);
		}
	}

	/**
	 * Handle controls based on calculation mode.
	 */
	private void ifullSet() {
		int index = calculationCb.getSelectedIndex();
		String tools = "";
		sloteTf.setEnabled(false);
		deltaeTf.setEnabled(false);
		kermaCh.setEnabled(false);
		fanoCh.setEnabled(false);
		bremsplitSp.setEnabled(false);
		RRCh.setEnabled(false);
		photonsplitSp.setEnabled(false);

		if (index == 0) {
			tools = "Just compute total dose and that due to stoppers and discards";
			calculationCb.setToolTipText(tools);// );
			fanoCh.setEnabled(true);
			photonsplitSp.setEnabled(true);
		}
		if (index == 1) {
			tools = "Calculate total dose, that due to stoppers and discards and Aatt and Ascat."
					+ " \n" + "See definitions in outputs";
			calculationCb.setToolTipText(tools);// );
			fanoCh.setEnabled(true);
			photonsplitSp.setEnabled(true);
		}
		if (index == 2) {
			tools = "Score a pulse height distribution along with the efficiecy";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			deltaeTf.setEnabled(true);
			kermaCh.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 3) {
			tools = "Score the scatter fraction instead of stoppers. Only for incident photons."
					+ " \n"
					+ "Includes: dose from compton scattered photons and "
					+ " \n"
					+ "dose from fluorescent photons which are re-absorbed";
			calculationCb.setToolTipText(tools);// );

			kermaCh.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		// =============================
		if (index == 4) {
			tools = "Score total fluence spectra  only.";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 5) {
			tools = "Score electron primaries separately as well include brem generated photons as primaries";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 6) {
			tools = "Score electron primaries but include those generated by brem as secondaries";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 7) {
			tools = "Primaries are all first generation photons,including brem."
					+ " \n"
					+ "Secondaries include all scattered photons and annihilations photons.";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 8) {
			tools = "Score electron secondaries";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 9) {
			tools = "Score stopping power ratio with respect to detector material";
			calculationCb.setToolTipText(tools);// );

			// sloteTf.setEnabled(true);
			fanoCh.setEnabled(true);
			bremsplitSp.setEnabled(true);
			RRCh.setEnabled(true);
		}
		if (index == 10) {
			tools = "Score emerging photon fluence from detector material";
			calculationCb.setToolTipText(tools);// );

			sloteTf.setEnabled(true);
		}

	}

	/**
	 * Save source information
	 */
	private void saveSource() {
		String ext = resources.getString("src.load");
		String pct = ".";
		String description = resources.getString("src.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas + file_sep + "egs";
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showSaveDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filename= chooser.getSelectedFile().toString()+pct+ext;
			filename = chooser.getSelectedFile().toString();
			int fl = filename.length();
			String test = filename.substring(fl - 4);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;

			// System.out.println(filename);
			String s = "";
			s = (String) sourceTypeCb.getSelectedItem() + "\n";
			// s=s+(String)sourceEqCb.getSelectedItem()+"\n";
			s = s + sourcematerialTf.getText() + "\n";
			s = s + asourceTf.getText() + "\n";
			s = s + hsourceTf.getText() + "\n";
			s = s + bsourceTf.getText() + "\n";
			s = s + hsourceupTf.getText() + "\n";
			s = s + sddistTf.getText() + "\n";
			s = s + pointstodmaterialTf.getText() + "\n";// ======================
			s = s + envelopematerialTf.getText() + "\n";// ======================
			s = s + envelopeThickTf.getText() + "\n";// ======================
			s = s + rbeamTf.getText() + "\n";// ======================
			s = s + anglebeamTf.getText() + "\n";// ======================

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
				
				statusL.setText(resources.getString("status.save") + filename);
			} catch (Exception ex) {

			}

		}

	}

	/**
	 * Save detector information
	 */
	private void saveDet() {
		String ext = resources.getString("det.load");
		String pct = ".";
		String description = resources.getString("det.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas + file_sep + "egs";
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showSaveDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filename= chooser.getSelectedFile().toString()+pct+ext;
			filename = chooser.getSelectedFile().toString();
			int fl = filename.length();
			String test = filename.substring(fl - 4);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;

			String s = "";
			s = s + adetTf.getText() + "\n";
			s = s + hdetTf.getText() + "\n";
			s = s + adetTotTf.getText() + "\n";
			s = s + hdetTotTf.getText() + "\n";
			s = s + hUpTf.getText() + "\n";
			s = s + althickTf.getText() + "\n";
			s = s + almaterialTf.getText() + "\n";
			s = s + airmaterialTf.getText() + "\n";
			s = s + naimaterialTf.getText() + "\n";
			s = s + windowmaterialTf.getText() + "\n";
			s = s + windowThickTf.getText() + "\n";

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
				
				statusL.setText(resources.getString("status.save") + filename);
			} catch (Exception ex) {

			}
		}

	}

	/**
	 * Select material by loading the corresponding pegs4 data file.
	 * @param imat imat, where to set material.
	 */
	private void selectMaterial(int imat) {
		// .pegs4dat
		String ext = "pegs4dat";// resources.getString("src.load");
		String pct = ".";
		String description = "Material constants file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");
		String datas = "Data" + file_sep + "egs" + file_sep + "interactiv";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String opens = currentDir + file_sep + datas;
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filename = chooser.getSelectedFile().toString();
			int ifs = 0;
			int indx = 0;
			while (ifs < filename.length()) {
				String s = filename.substring(ifs, ifs + 1);
				if (s.compareTo(file_sep) == 0) {
					indx = ifs;
				}
				ifs++;
			}
			// filename="air521.pegs4dat";
			int fl = filename.length();
			String test = filename.substring(fl - 9);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) == 0)// with extension
			{
				// filename=chooser.getSelectedFile().toString()+pct+ext;
				filename = filename.substring(indx + 1, fl - 9);// exstension
																// lookup!!
			} else {
				filename = filename.substring(indx + 1);
			}

			if (imat == 0) {
				almaterialTf.setText(filename);
			} else if (imat == 1) {
				airmaterialTf.setText(filename);
			} else if (imat == 2) {
				naimaterialTf.setText(filename);
			} else if (imat == 3) {
				pointstodmaterialTf.setText(filename);
			} else if (imat == 4) {
				sourcematerialTf.setText(filename);
			} else if (imat == 5) {
				envelopematerialTf.setText(filename);
			} else if (imat == 6) {
				windowmaterialTf.setText(filename);
			}
			
			statusL.setText(resources.getString("status.load") + filename);
		}

	}

	/**
	 * Load source information
	 */
	private void loadSource() {
		String ext = resources.getString("src.load");
		String pct = ".";
		String description = resources.getString("src.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas + file_sep + "egs";
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// -----------------
		char lineSep = '\n';// System.getProperty("line.separator").charAt(0);
		int i = 0;
		int lnr = 0;// line number
		StringBuffer desc = new StringBuffer();
		String line = "";
		// --------------
		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filename= chooser.getSelectedFile().toString()+pct+ext;
			filename = chooser.getSelectedFile().toString();
			int fl = filename.length();
			String test = filename.substring(fl - 4);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;

			try {
				FileInputStream in = new FileInputStream(filename);
				while ((i = in.read()) != -1) {
					if ((char) i != lineSep) {
						desc.append((char) i);
					} else {
						lnr++;
						line = desc.toString();
						if (lnr == 1)// s type
						{
							// System.out.println("enter");
							sourceTypeCb.setSelectedItem(line);
						}
						if (lnr == 2)// s compoz
						{
							// System.out.println("enter");
							// sourceEqCb.setSelectedItem(line);
							sourcematerialTf.setText(line);
						}
						if (lnr == 3) {
							asourceTf.setText(line);
						}
						if (lnr == 4) {
							hsourceTf.setText(line);
						}
						if (lnr == 5) {
							bsourceTf.setText(line);
						}
						if (lnr == 6) {
							hsourceupTf.setText(line);
						}
						if (lnr == 7) {
							sddistTf.setText(line);
						}
						if (lnr == 8) {
							pointstodmaterialTf.setText(line);
						}
						if (lnr == 9) {
							envelopematerialTf.setText(line);
						}
						if (lnr == 10) {
							envelopeThickTf.setText(line);
						}
						if (lnr == 11) {
							rbeamTf.setText(line);
						}
						if (lnr == 12) {
							anglebeamTf.setText(line);
						}

						desc = new StringBuffer();

					}
				}
				in.close();
				statusL.setText(resources.getString("status.load") + filename);
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Load detector information
	 */
	private void loadDet() {
		String ext = resources.getString("det.load");
		String pct = ".";
		String description = resources.getString("det.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas + file_sep + "egs";
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// -----------------
		char lineSep = '\n';// System.getProperty("line.separator").charAt(0);
		int i = 0;
		int lnr = 0;// line number
		StringBuffer desc = new StringBuffer();
		String line = "";
		// --------------

		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filename= chooser.getSelectedFile().toString()+pct+ext;
			filename = chooser.getSelectedFile().toString();
			int fl = filename.length();
			String test = filename.substring(fl - 4);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;
			try {
				FileInputStream in = new FileInputStream(filename);
				while ((i = in.read()) != -1) {
					if ((char) i != lineSep) {
						desc.append((char) i);
					} else {
						lnr++;
						line = desc.toString();
						if (lnr == 1) {
							adetTf.setText(line);
						}
						if (lnr == 2) {
							hdetTf.setText(line);
						}
						if (lnr == 3) {
							adetTotTf.setText(line);
						}
						if (lnr == 4) {
							hdetTotTf.setText(line);
						}
						if (lnr == 5) {
							hUpTf.setText(line);
						}
						if (lnr == 6) {
							althickTf.setText(line);
						}
						if (lnr == 7) {
							almaterialTf.setText(line);
						}
						if (lnr == 8) {
							airmaterialTf.setText(line);
						}
						if (lnr == 9) {
							naimaterialTf.setText(line);
						}
						if (lnr == 10) {
							windowmaterialTf.setText(line);
						}
						if (lnr == 11) {
							windowThickTf.setText(line);
						}

						desc = new StringBuffer();

					}
				}
				in.close();
				statusL.setText(resources.getString("status.load") + filename);
			} catch (Exception e) {

			}

		}

	}

	/**
	 * Save efficiency to a file.
	 */
	private void saveeff() {
		int i = calculationCb.getSelectedIndex();
		if (i != 2) {
			return;
		}
		if (EGS4SrcEns.monoindex != EGS4SrcEns.iMONOENERGETIC) {
			return;
		}
		if (!saveeffCh.isSelected()) {
			return;
		}

		double eff = 0.0;
		double erreff = 0.0;
		double teff = 0.0;
		double terreff = 0.0;

		if (useGeant){
			eff = geff;
			erreff=geff_unc;
			teff=gefft;
			terreff=gefft_unc;
			
		}else{//default EGS
		if (EGS4SrcEns.iqin == 0) {
			eff = GammaDetEffA.efficiency;// /100
			erreff = GammaDetEffA.efficiency_error;// %
			teff = GammaDetEffA.photontotal_efficiency;// /100
			terreff = GammaDetEffA.photontotal_efficiency_error;// %

		} else {
			eff = GammaDetEffA.eefficiency;// /100
			erreff = GammaDetEffA.eefficiency_error;// %
			teff = eff;
			terreff = erreff;
		}
		}
		String effnames = "NONAME";
		if ((filenameeffTf.getText()).compareTo("") != 0) {
			effnames = filenameeffTf.getText();
		}

		String ext = "eff";
		String pct = ".";
		// String description="Efficiency file";
		// ExampleFileFilter jpgFilter =
		// new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas + file_sep + "egs"
				+ file_sep + "eff";

		filename = opens + file_sep + effnames + pct + ext;
		String s = "";
		s = s + EGS4.doubleToString(eff) + "\n";
		s = s + EGS4.doubleToString(erreff) + "\n";
		s = s + EGS4.doubleToString(teff) + "\n";
		s = s + EGS4.doubleToString(terreff) + "\n";

		try {
			FileWriter sigfos = new FileWriter(filename);
			sigfos.write(s);
			sigfos.close();
			
			statusL.setText(resources.getString("status.save") + filename);
		} catch (Exception ex) {

		}
	}

	/**
	 * Start the Monte-Carlo simulation.
	 */
	private void startSimulation() {
		isRunning=true;
		stopAnim=false;
		if (simTh == null) {
			simTh = new Thread(this);
			simTh.start();// Allow one simulation at time!
		}
		
		if (statusTh == null) {
			statusTh = new Thread(this);
			statusTh.start();
		}
	}

	/**
	 * Stop the computation thread.
	 */
	private void stopSimulation() {
		statusTh = null;
		frameNumber = 0;
		stopAnim = true;
		isRunning=false;
		if (simTh == null) {
			stopAppend = false;
			// press kill button but simulation never started

			return;
		}

		simTh = null;

		if (stopAppend) {// kill button was pressed!
			EGS4.STOPPROGRAM = true;
			simTa.append(resources.getString("text.simulation.stop") + "\n");
			stopAppend = false;
			String label = resources.getString("status.done");
			statusL.setText(label);
		}
		FluenceRz.destroyArrays();
		SpwrRz.destroyArrays();
		// setEnabled(true);
	}

	/**
	 * Thread specific method.
	 */
	public void run() {
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);// both thread
																	// same
																	// priority

		long startTime = System.currentTimeMillis();
		Thread currentThread = Thread.currentThread();
		while (!stopAnim && currentThread == statusTh) {// if thread is status
														// display
			// Thread!!
			frameNumber++;
			if (frameNumber % 2 == 0)
				statusL.setText(statusRunS + ".....");
			else
				statusL.setText(statusRunS);

			// Delay
			try {
				startTime += delay;
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				break;
			}
		}

		if (currentThread == simTh) {// if thread is the main
										// computation Thread!!
			if (command.equals(RUN_COMMAND)) {
				performEfficiencyCalculation();
			}
		}
	}

	/**
	 * Perform efficiency calculation
	 */
	private void performEfficiencyCalculation()// ----@@@@@@@@DO NOT FORGET
												// CHECK READ!!!!!
	{
		simTa.selectAll();
		simTa.replaceSelection("");
		// GammaDetEff.systemOutB=false;GammaDet.systemOutB=false;
		GammaDetEffA.systemOutB = false;
		GammaDetA.systemOutB = false;
		FluenceRz.systemOutB = false;
		SpwrRz.systemOutB = false;
		BremsApp2.systemOutB = false;
		tabs.setSelectedIndex(4);//3);
		// ======================OBLIGE=======================
		EGS4Geom.$MAXZREG = 20;// 61;//"MAX # OF DOSE SCORING PLANAR ZONES           "
		EGS4Geom.$MAXRADII = 20;// 60;//"MAX # OF DOSE SCORING PLANAR ZONES           "
		EGS4.setMXMED(50);// (10);//"MAX # OF MEDIA 		"
		EGS4.setMXREG(EGS4Geom.$MAXRADII * EGS4Geom.$MAXZREG + 1);// "$MAXRADII*$MAXZREG)+1   "
		EGS4.setMXSTACK(4000);// "NEED HUGE STACK FOR CORRELATIONS+splitting   "
		EGS4.setMXRANGE(500); // "for range arrays used in range_rejection()"
		EGS4.$MXMDSH = 100;// allways
		// ==============================
		FluenceRz.$MAXRZ = Math.max(EGS4Geom.$MAXZREG, EGS4Geom.$MAXRADII);
		EGS4SrcEns.$NENSRC = 500;// MAXIM OK
		EGS4SrcEns.$MXRDIST = 1000;// MAXIM OK
		FluenceRz.$MAXIT = 2;//
		// FluenceRz.$MAXCMPTS=12;//geom->at init
		FluenceRz.$MAXBRSPLIT = 200;
		// ================================
		EGS4.reset();
		EGS4Core.reset();
		EGS4Geom.reset();
		EGS4Grid.reset();
		EGS4Macro.reset();
		EGS4SrcEns.reset();
		GammaDetA.reset();// GammaDet.reset();=>4
		GammaDetEffA.reset();// GammaDetEff.reset();=>7
		FluenceRz.reset();// =>12
		SpwrRz.reset();
		BremsApp2.reset();
		EGS4.egs_set_defaults();// first default then:
		// ======================OBLIGE=======================
		int i = printCb.getSelectedIndex();
		int runmode = 0;
		if (i == 0) {
			GammaDetEffA.IPRINT = 1;
			GammaDetA.IPRINT = 1;
		}// {GammaDetEff.IPRINT=1;GammaDet.IPRINT=1;}
		else if (i == 1) {
			GammaDetEffA.IPRINT = 2;
			GammaDetA.IPRINT = 2;
		}// {GammaDetEff.IPRINT=2;GammaDet.IPRINT=2;}
		else if (i == 2) {
			GammaDetEffA.IPRINT = 3;
			GammaDetA.IPRINT = 3;
		}// {GammaDetEff.IPRINT=3;GammaDet.IPRINT=3;}
		FluenceRz.IPRINT = GammaDetEffA.IPRINT;
		SpwrRz.IPRINT = GammaDetEffA.IPRINT;

		if (createFileCh.isSelected()) {
			GammaDetEffA.createOutputFile = true;
			GammaDetA.createOutputFile = true;
		}// {GammaDetEff.createOutputFile=true;GammaDet.createOutputFile=true;}
		else {
			GammaDetEffA.createOutputFile = false;
			GammaDetA.createOutputFile = false;
		}// {GammaDetEff.createOutputFile=false;GammaDet.createOutputFile=false;}
		FluenceRz.createOutputFile = GammaDetEffA.createOutputFile;
		SpwrRz.createOutputFile = GammaDetEffA.createOutputFile;

		i = rngCb.getSelectedIndex();
		if (i == 0) {
			EGS4.RandomUse = 1;
			EGS4.ranluxB = true;
			RandomCollection.ranluxB=true;
			RandomCollection.RandomUse=1;
		}// DEFAULT RANLUX
		else if (i == 1) {
			EGS4.RandomUse = 0;
			RandomCollection.RandomUse=0;//for KNAlgor!!!
		}

		// GammaDetEff.IWATCH=GammaDetEff.IWATCH_OFF;GammaDet.IWATCH=GammaDet.IWATCH_OFF;
		GammaDetEffA.IWATCH = GammaDetEffA.IWATCH_OFF;
		GammaDetA.IWATCH = GammaDetA.IWATCH_OFF;
		FluenceRz.IWATCH = FluenceRz.IWATCH_OFF;
		SpwrRz.IWATCH = SpwrRz.IWATCH_OFF;

		// GammaDetEff.ISTORE=GammaDetEff.ISTORE_NO;GammaDet.ISTORE=GammaDet.ISTORE_NO;
		GammaDetEffA.ISTORE = GammaDetEffA.ISTORE_NO;
		GammaDetA.ISTORE = GammaDetA.ISTORE_NO;
		FluenceRz.ISTORE = FluenceRz.ISTORE_NO;
		SpwrRz.ISTORE = SpwrRz.ISTORE_NO;
		// GammaDetEff.IRESTART=GammaDetEff.IRESTART_FIRST;GammaDet.IRESTART=GammaDet.IRESTART_FIRST;
		GammaDetEffA.IRESTART = GammaDetEffA.IRESTART_FIRST;
		GammaDetA.IRESTART = GammaDetA.IRESTART_FIRST;
		FluenceRz.IRESTART = FluenceRz.IRESTART_FIRST;
		SpwrRz.IRESTART = SpwrRz.IRESTART_FIRST;
		// GammaDetEff.IOOPTN=GammaDetEff.IOOPTN_MATERIAL_SUMMARY;GammaDet.IOOPTN=GammaDet.IOOPTN_SHORT;
		GammaDetEffA.IOOPTN = GammaDetEffA.IOOPTN_MATERIAL_SUMMARY;
		GammaDetA.IOOPTN = GammaDetA.IOOPTN_SHORT;
		FluenceRz.IOOPTN = FluenceRz.IOOPTN_MATERIAL_SUMMARY;
		SpwrRz.IOOPTN = SpwrRz.IOOPTN_MATERIAL_SUMMARY;
		// EGS4Macro.ICSDA=GammaDetEff.ETRANS_NORMAL;
		EGS4Macro.ICSDA = GammaDetEffA.ETRANS_NORMAL;
		// GammaDetEff.IDAT=GammaDetEff.IDAT_NO;GammaDet.IDAT=GammaDet.IDAT_NO;
		GammaDetEffA.IDAT = GammaDetEffA.IDAT_NO;
		GammaDetA.IDAT = GammaDetA.IDAT_NO;
		FluenceRz.IDAT = FluenceRz.IDAT_NO;
		SpwrRz.IDAT = SpwrRz.IDAT_NO;
		// GammaDetEff.TIMMAX=90.000;GammaDet.TIMMAX=90.000;
		GammaDetEffA.TIMMAX = 90.000;
		GammaDetA.TIMMAX = 90.000;
		FluenceRz.TIMMAX = 90.000;
		SpwrRz.TIMMAX = 90.000;

		// GammaDetEff.NCASE=EGS4.stringToInt((String)nPhotonsCb.getSelectedItem());//20000;
		GammaDetEffA.NCASE = EGS4.stringToInt((String) nPhotonsCb
				.getSelectedItem());// 20000;
		// GammaDet.NCASE=EGS4.stringToInt((String)nPhotonsCb.getSelectedItem());//20000;
		GammaDetA.NCASE = EGS4.stringToInt((String) nPhotonsCb
				.getSelectedItem());// 20000;
		FluenceRz.NCASE = GammaDetA.NCASE;
		SpwrRz.NCASE = GammaDetA.NCASE;
		BremsApp2.ncase = GammaDetA.NCASE;
		// ==========
		FluenceRz.IPRNTP = 0;// PRINT FLUENCE SPECTRA ==DEFAULT
		// =======
		i = calculationCb.getSelectedIndex();
		int ifull = 0;
		if (i == 0)// {runmode=0;ifull=0;EGS4Macro.IFULL=GammaDet.IFULL_DOSE_AND_STOPPERS;}
		{
			runmode = 0;
			ifull = 0;
			EGS4Macro.IFULL = GammaDetA.IFULL_DOSE_AND_STOPPERS;
		} else if (i == 1)// {runmode=0;ifull=1;EGS4Macro.IFULL=GammaDet.IFULL_AATT_AND_ASCAT;}
		{
			runmode = 0;
			ifull = 1;
			EGS4Macro.IFULL = GammaDetA.IFULL_AATT_AND_ASCAT;
		} else if (i == 2)// {runmode=1;ifull=2;EGS4Macro.IFULL=GammaDetEff.IFULL_PULSE_HEIGHT_DISTRIBUTION;}
		{
			runmode = 1;
			ifull = 2;
			EGS4Macro.IFULL = GammaDetEffA.IFULL_PULSE_HEIGHT_DISTRIBUTION;
		} else if (i == 3)// {runmode=1;ifull=3;EGS4Macro.IFULL=GammaDetEff.IFULL_SCATTER_FRACTION;}
		{
			runmode = 1;
			ifull = 3;
			EGS4Macro.IFULL = GammaDetEffA.IFULL_SCATTER_FRACTION;
		}
		// ==================================
		else if (i == 4) {
			runmode = 2;
			FluenceRz.IPRIMARY = FluenceRz.IPRIMARY_TOTAL_FLUENCE;
		} else if (i == 5) {
			runmode = 2;
			FluenceRz.IPRIMARY = FluenceRz.IPRIMARY_ELECTRON_PRIMARIES;
		} else if (i == 6) {
			runmode = 2;
			FluenceRz.IPRIMARY = FluenceRz.IPRIMARY_INCLUDE_BREM_SECONDARIES;
		} else if (i == 7) {
			runmode = 2;
			FluenceRz.IPRIMARY = FluenceRz.IPRIMARY_PHOTON_PRIMARIES;
		} else if (i == 8) {
			runmode = 2;
			FluenceRz.IPRIMARY = FluenceRz.IPRIMARY_ELECTRON_SECONDARIES;
		} else if (i == 9) {
			runmode = 3;
		}// SpwrRz
		else if (i == 10) {
			runmode = 4;
		}// BremsApp

		// ======================================
		String s = "";
		try {
			GammaDetEffA.STATLM = EGS4.stringToDouble(statTf.getText());// GammaDetEff.STATLM=EGS4.stringToDouble(statTf.getText());
			GammaDetA.STATLM = EGS4.stringToDouble(statTf.getText());// GammaDet.STATLM=EGS4.stringToDouble(statTf.getText());
			FluenceRz.STATLM = GammaDetEffA.STATLM;
			SpwrRz.STATLM = GammaDetEffA.STATLM;
		} catch (Exception e) {
			s = "Statistical accuracy must be a positive number!" + " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		// if(GammaDetEff.STATLM<0)
		if (GammaDetEffA.STATLM < 0) {
			s = "Statistical accuracy must be a positive number!" + " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}

		if (fanoCh.isSelected())
			GammaDetA.IFANO = GammaDetA.IFANO_YES;// GammaDet.IFANO=GammaDet.IFANO_YES;
		else
			GammaDetA.IFANO = GammaDetA.IFANO_NO;// GammaDet.IFANO=GammaDet.IFANO_NO;
		SpwrRz.IFANO = GammaDetA.IFANO;
		if (kermaCh.isSelected()) {
			GammaDetEffA.IKERMA = GammaDetEffA.KERMA_YES;
		}// {GammaDetEff.IKERMA=GammaDetEff.KERMA_YES;}
		else {
			GammaDetEffA.IKERMA = GammaDetEffA.KERMA_NO;
		}// {GammaDetEff.IKERMA=GammaDetEff.KERMA_NO;}

		EGS4Geom.iterseindex = EGS4Geom.iINDIVIDUAL;
		EGS4Geom.Z_OF_FRONT_FACE = 0.0;// OK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// ============@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		boolean neg = false;
		try {
			hdet = EGS4.stringToDouble(hdetTf.getText());// 6.3;
			if (hdet < 0)
				neg = true;
			hdet_tot = EGS4.stringToDouble(hdetTotTf.getText());// 7.3;
			if (hdet_tot < 0)
				neg = true;
			GammaDetEffA.hdettot = hdet_tot;
			GammaDetA.hdettot = hdet_tot;
			FluenceRz.hdettot = hdet_tot;
			SpwrRz.hdettot = hdet_tot;
			althick = EGS4.stringToDouble(althickTf.getText());// 0.05;
			if (althick < 0)
				neg = true;
			adet = EGS4.stringToDouble(adetTf.getText());// 6.3/2;
			adet = adet / 2.0;
			if (adet < 0)
				neg = true;
			adet_tot = EGS4.stringToDouble(adetTotTf.getText());// 7.3/2;
			adet_tot = adet_tot / 2.0;
			GammaDetEffA.adettot = adet_tot;
			GammaDetA.adettot = adet_tot;
			FluenceRz.adettot = adet_tot;
			SpwrRz.adettot = adet_tot;
			if (adet_tot < 0)
				neg = true;
			inac_thick_sup = EGS4.stringToDouble(hUpTf.getText());// 0.50;
			if (inac_thick_sup < 0)
				neg = true;
			winthick = EGS4.stringToDouble(windowThickTf.getText());// 0.05;
			if (winthick < 0)
				neg = true;
			// ==========================================================
			// note errors are also handle below!! h>,<,=hup??
			hsource = EGS4.stringToDouble(hsourceTf.getText());// 8.6;
			if (hsource < 0)
				neg = true;
			hsourceup = EGS4.stringToDouble(hsourceupTf.getText());// 3.8;
			if (hsourceup < 0)
				neg = true;
			asource = EGS4.stringToDouble(asourceTf.getText());// 13.25/2;
			if (asource < 0)
				neg = true;
			asource = asource / 2.0;
			bsource = EGS4.stringToDouble(bsourceTf.getText());// 8.55/2;
			if (bsource < 0)
				neg = true;
			bsource = bsource / 2.0;
			envthick = EGS4.stringToDouble(envelopeThickTf.getText());// 0.05;
			if (envthick < 0)
				neg = true;
			stddist = EGS4.stringToDouble(sddistTf.getText());// 0.05;
			if (stddist < 0)
				neg = true;
			rbeam = EGS4.stringToDouble(rbeamTf.getText());// 0.05;
			if (rbeam < 0)
				neg = true;
			anglebeam = EGS4.stringToDouble(anglebeamTf.getText());// 0.05;
			if (anglebeam < 0 || anglebeam >= 90.0)
				neg = true;

		} catch (Exception ex) {
			s = "Error in detector and source inputs. Insert positive numbers!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (rbeam >= adet_tot) {
			s = "Error in detector and source inputs. For consistency, rbeam must be < detector radius!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}

		if (neg) {
			s = "Error in detector and source inputs. Insert positive numbers! Beam angle>0 and <90!!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (adet_tot < adet) {
			s = "Error: Total detector diameter < effective detector diameter?"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (hdet_tot < hdet) {
			s = "Error: Total detector height < effective detector height?"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (inac_thick_sup > (hdet_tot - hdet - winthick)) {
			s = "Error: Inactive superior detector height > Total detector height-effective detector height-window thickness?"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		// @@@@@@@@@@@#####################################################################
		/*
		 * double zp2=inac_thick_sup;//(hdet_tot-hdet)/2.0; double zp3=zp2+hdet;
		 * double zp4=hdet_tot-althick; double zp5=hdet_tot; double
		 * rair=adet_tot-althick; EGS4Geom.nNSLAB=5; //EGS4Geom.nNSLAB=4;
		 * //DEPTH BOUNDARIES EGS4Geom.ZPLANE[1]=althick;//0-0.05;AL canister
		 * monture(entrance window) EGS4Geom.ZPLANE[2]=zp2;//0.1//0.05+0.45;AIR
		 * inactive detector //EGS4Geom.ZPLANE[2]=6.35;//0.05+0.45;AIR inactive
		 * detector //EGS4Geom.ZPLANE[3]=7.25; //EGS4Geom.ZPLANE[4]=7.30;
		 * EGS4Geom.ZPLANE[3]=zp3;//6.40;//0.50+6.30;NAI active detector
		 * EGS4Geom.ZPLANE[4]=zp4;//7.25;//6.8+0.45; AIR inactive det: optical
		 * window EGS4Geom.ZPLANE[5]=zp5;//7.30;//7.25+0.05;AL canister monture
		 * 
		 * EGS4Geom.nCyl=3;//"number of radial cylinders input" //#Radii of
		 * cylinders EGS4Geom.RCYL[1]=adet;//6.3/2;//NAI
		 * EGS4Geom.RCYL[2]=rair;//7.2/2;//air;
		 * EGS4Geom.RCYL[3]=adet_tot;//7.3/2;//EXTERNAL;
		 * 
		 * if(!noSup) {
		 * GammaDetEffA.scattCaseB=false;//GammaDetEff.scattCaseB=false;
		 * 
		 * EGS4Geom.nMEDIA=7; //EGS4Geom.nMEDIA=5;
		 * EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		 * EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA
		 * [6]=naimaterialTf.getText();//"sodiumiodide";//"NAI_Fortran";
		 * //EGS4.MEDIA[4]="sodiumiodide";//"NAI_Fortran";
		 * EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA
		 * 
		 * EGS4Geom.nMEDNUM=7; //EGS4Geom.nMEDNUM=5; EGS4Geom.MEDNUM[0]=1;//Al
		 * EGS4Geom.MEDNUM[1]=2;//Air EGS4Geom.MEDNUM[2]=3;//Air
		 * EGS4Geom.MEDNUM[3]=4;//Air EGS4Geom.MEDNUM[4]=5;//Air
		 * EGS4Geom.MEDNUM[5]=6;//Air EGS4Geom.MEDNUM[6]=7;//NAI
		 * //EGS4Geom.MEDNUM[4]=5;//NAI EGS4Geom.nNREGLO=7; EGS4Geom.nNREGHI=7;
		 * //EGS4Geom.nNREGLO=5; //EGS4Geom.nNREGHI=5;
		 * 
		 * EGS4Geom.NREGLO[0]=2;//START REGION EGS4Geom.NREGHI[0]=16;//STOP
		 * REGION=>PUT Al every where EGS4Geom.NREGLO[1]=3;//START REGION
		 * EGS4Geom.NREGHI[1]=3;//STOP REGION=>AIR in region 3
		 * EGS4Geom.NREGLO[2]=5;//START REGION EGS4Geom.NREGHI[2]=5;//STOP
		 * REGION=>AIR in region 5 EGS4Geom.NREGLO[3]=8;//START REGION
		 * EGS4Geom.NREGHI[3]=8;//STOP REGION=>AIR in region 8
		 * EGS4Geom.NREGLO[4]=9;//START REGION EGS4Geom.NREGHI[4]=9;//STOP
		 * REGION=>AIR in region 9 EGS4Geom.NREGLO[5]=10;//START REGION
		 * EGS4Geom.NREGHI[5]=10;//STOP REGION=>AIR in region 10
		 * EGS4Geom.NREGLO[6]=4;//START REGION EGS4Geom.NREGHI[6]=4;//STOP
		 * REGION=>NAI in region 4 }
		 * 
		 * if(noSup) {
		 * //runmode=1;ifull=3;EGS4Macro.IFULL=GammaDetEff.IFULL_SCATTER_FRACTION
		 * ;//force!!
		 * 
		 * GammaDetEffA.scattCaseB=true;//force colimatted
		 * beam!!//GammaDetEff.scattCaseB=true;//force colimatted beam!!
		 * 
		 * EGS4Geom.nMEDIA=9;
		 * 
		 * EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		 * EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA
		 * [6]=naimaterialTf.getText();//"sodiumiodide";//"NAI_Fortran";
		 * EGS4.MEDIA[7]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * EGS4.MEDIA[8]=airmaterialTf.getText();//"air_dry_nearsealevel";
		 * 
		 * EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA
		 * 
		 * EGS4Geom.nMEDNUM=9;
		 * 
		 * EGS4Geom.MEDNUM[0]=1;//Al EGS4Geom.MEDNUM[1]=2;//Air
		 * EGS4Geom.MEDNUM[2]=3;//Air EGS4Geom.MEDNUM[3]=4;//Air
		 * EGS4Geom.MEDNUM[4]=5;//Air EGS4Geom.MEDNUM[5]=6;//Air
		 * EGS4Geom.MEDNUM[6]=7;//NAI EGS4Geom.MEDNUM[7]=8;//AIR
		 * EGS4Geom.MEDNUM[8]=9;//AIR
		 * 
		 * EGS4Geom.nNREGLO=9; EGS4Geom.nNREGHI=9;
		 * 
		 * EGS4Geom.NREGLO[0]=2;//START REGION EGS4Geom.NREGHI[0]=16;//STOP
		 * REGION=>PUT Al every where EGS4Geom.NREGLO[1]=3;//START REGION
		 * EGS4Geom.NREGHI[1]=3;//STOP REGION=>AIR in region 3
		 * EGS4Geom.NREGLO[2]=5;//START REGION EGS4Geom.NREGHI[2]=5;//STOP
		 * REGION=>AIR in region 5 EGS4Geom.NREGLO[3]=8;//START REGION
		 * EGS4Geom.NREGHI[3]=8;//STOP REGION=>AIR in region 8
		 * EGS4Geom.NREGLO[4]=9;//START REGION EGS4Geom.NREGHI[4]=9;//STOP
		 * REGION=>AIR in region 9 EGS4Geom.NREGLO[5]=10;//START REGION
		 * EGS4Geom.NREGHI[5]=10;//STOP REGION=>AIR in region 10
		 * EGS4Geom.NREGLO[6]=4;//START REGION EGS4Geom.NREGHI[6]=4;//STOP
		 * REGION=>NAI in region 4
		 * 
		 * EGS4Geom.NREGLO[7]=2;//START REGION EGS4Geom.NREGHI[7]=2;//STOP
		 * REGION=>AIR in region 2 EGS4Geom.NREGLO[8]=7;//START REGION
		 * EGS4Geom.NREGHI[8]=7;//STOP REGION=>AIR in region 7
		 * 
		 * } //EGS4Geom.NREGLO[0]=2;//START REGION //
		 * EGS4Geom.NREGHI[0]=13;//STOP REGION=>PUT Al every where
		 * //EGS4Geom.NREGLO[1]=4;//START REGION // EGS4Geom.NREGHI[1]=4;
		 * //EGS4Geom.NREGLO[2]=7;//START REGION // EGS4Geom.NREGHI[2]=7;
		 * //EGS4Geom.NREGLO[3]=8;//START REGION // EGS4Geom.NREGHI[3]=8;
		 * //EGS4Geom.NREGLO[4]=3;//START REGION // EGS4Geom.NREGHI[4]=3;
		 * 
		 * EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		 * EGS4Geom.ISUMCV[0]=4;//REGION NUMBERS OF THE CAVITY= 4
		 * //EGS4Geom.ISUMCV[0]=3;//REGION NUMBERS OF THE CAVITY= 4
		 * GammaDetEffA.nREGSOLV=1;//GammaDetEff.nREGSOLV=1;
		 * GammaDetEffA.REGSVOL[0]=4;//GammaDetEff.REGSVOL[0]=4;
		 * //GammaDetEff.REGSVOL[0]=3;
		 */
		// @@@@@@@@@@@#####################################################################
		i = calculationCb.getSelectedIndex();
		if (i == 2) {
			try {
				// GammaDetEff.SLOTE=EGS4.stringToDouble(sloteTf.getText());//0.01;
				GammaDetEffA.SLOTE = EGS4.stringToDouble(sloteTf.getText());// 0.01;
				// GammaDetEff.DELTAE=EGS4.stringToDouble(deltaeTf.getText());//0.005;
				GammaDetEffA.DELTAE = EGS4.stringToDouble(deltaeTf.getText());// 0.005;
			} catch (Exception e) {
				s = "Pulse height inputs must be positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.SLOTE < 0 || GammaDetEffA.DELTAE < 0)// if(GammaDetEff.SLOTE<0
																	// ||
																	// GammaDetEff.DELTAE<0)
			{
				s = "Pulse height inputs must be positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		}
		if (runmode == 2 || runmode == 4) {
			try {
				GammaDetEffA.SLOTE = EGS4.stringToDouble(sloteTf.getText());// 0.01;
				FluenceRz.SLOTE = GammaDetEffA.SLOTE;
				BremsApp2.SLOTE = GammaDetEffA.SLOTE;
			} catch (Exception e) {
				s = "SLOTE inputs must be positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.SLOTE < 0) {
				s = "SLOTE inputs must be positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		}

		if (photonRb.isSelected()) {
			EGS4SrcEns.ipart = EGS4SrcEns.ipart_photon;
			BremsApp2.iqin = 0;
		} else if (electronRb.isSelected()) {
			EGS4SrcEns.ipart = EGS4SrcEns.ipart_electron;
			BremsApp2.iqin = -1;
		} else if (positronRb.isSelected()) {
			EGS4SrcEns.ipart = EGS4SrcEns.ipart_positron;
			BremsApp2.iqin = 1;
		}

		if (EGS4SrcEns.ipart != 0) {
			try {
				// GammaDetEff.ETHRESHOLD=EGS4.stringToDouble(ethreshTf.getText());
				GammaDetEffA.ETHRESHOLD = EGS4.stringToDouble(ethreshTf
						.getText());
				// GammaDet.ETHRESHOLD=EGS4.stringToDouble(ethreshTf.getText());
				GammaDetA.ETHRESHOLD = EGS4.stringToDouble(ethreshTf.getText());
				FluenceRz.ETHRESHOLD = GammaDetEffA.ETHRESHOLD;
				SpwrRz.ETHRESHOLD = GammaDetEffA.ETHRESHOLD;
			} catch (Exception e) {
				s = "Kinetic threshold energy for electrons must be a positive (or zero) number!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.ETHRESHOLD < 0)// if(GammaDetEff.ETHRESHOLD<0)
			{
				s = "Kinetic threshold energy for electrons must be a positive (or zero) number!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		}

		if (EGS4SrcEns.ipart == 4) {
			EGS4SrcEns.ipart = -1;
		}

		EGS4SrcEns.ISOURC = 0;
		// EGS4SrcEns.ISOURC=EGS4SrcEns.point_source_on_axis_incident_from_the_front;//1;
		// ATENUARE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		if (atenuareCh.isSelected())
		// {GammaDetEff.sourceatt=true;GammaDet.sourceatt=true;}
		{
			GammaDetEffA.sourceatt = true;
			GammaDetA.sourceatt = true;
		} else {
			GammaDetEffA.sourceatt = false;
			GammaDetA.sourceatt = false;
		}// {GammaDetEff.sourceatt=false;GammaDet.sourceatt=false;}
		FluenceRz.sourceatt = GammaDetEffA.sourceatt;
		SpwrRz.sourceatt = GammaDetEffA.sourceatt;

		i = sourceEqCb.getSelectedIndex();
		GammaDetEffA.sourcecode = i + 1;// 0 vs
										// 1//GammaDetEff.sourcecode=i+1;//0 vs
										// 1
		GammaDetA.sourcecode = i + 1;// GammaDet.sourcecode=i+1;
		FluenceRz.sourcecode = i + 1;
		SpwrRz.sourcecode = i + 1;

		i = sourceTypeCb.getSelectedIndex();
		GammaDetEffA.SOURCE = i;
		GammaDetA.SOURCE = i;// GammaDetEff.SOURCE=i;GammaDet.SOURCE=i;
		FluenceRz.SOURCE = i;
		SpwrRz.SOURCE = i;
		boolean brunmode4 = false;
		if (i == GammaDetEffA.SOURCE_POINT)// 0//if(i==GammaDetEff.SOURCE_POINT)//0
		{
			try {
				EGS4SrcEns.source_option[0] = EGS4.stringToDouble(sddistTf
						.getText());// 3.8;//100.;
			} catch (Exception e) {
				s = "Distance source-detector must be a positive number!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (EGS4SrcEns.source_option[0] < 0) {
				s = "Distance source-detector must be a positive number!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
			EGS4SrcEns.source_option[1] = adet_tot;// 7.3/2;//1.3;
			EGS4SrcEns.source_option[2] = 0.;
			EGS4SrcEns.source_option[3] = 0.;
		} else if (i == GammaDetEffA.SOURCE_SARPAGAN)// else
														// if(i==GammaDetEff.SOURCE_SARPAGAN)
		{
			try {
				// GammaDetEff.hsource=EGS4.stringToDouble(hsourceTf.getText());//3.1;
				GammaDetEffA.hsource = EGS4.stringToDouble(hsourceTf.getText());// 3.1;
				// GammaDetEff.asource=EGS4.stringToDouble(asourceTf.getText());//7.1/2;
				GammaDetEffA.asource = EGS4.stringToDouble(asourceTf.getText());// 7.1/2;
				// GammaDetEff.asource=GammaDetEff.asource/2.0;
				GammaDetEffA.asource = GammaDetEffA.asource / 2.0;
				// GammaDet.hsource=EGS4.stringToDouble(hsourceTf.getText());//3.1;
				GammaDetA.hsource = EGS4.stringToDouble(hsourceTf.getText());// 3.1;
				// GammaDet.asource=EGS4.stringToDouble(asourceTf.getText());//7.1/2;
				GammaDetA.asource = EGS4.stringToDouble(asourceTf.getText());// 7.1/2;
				// GammaDet.asource=GammaDet.asource/2.0;
				GammaDetA.asource = GammaDetA.asource / 2.0;

				GammaDetA.ethick = EGS4.stringToDouble(envelopeThickTf
						.getText());
				GammaDetEffA.ethick = EGS4.stringToDouble(envelopeThickTf
						.getText());

				FluenceRz.hsource = GammaDetEffA.hsource;
				FluenceRz.asource = GammaDetEffA.asource;
				FluenceRz.ethick = GammaDetEffA.ethick;

				SpwrRz.hsource = GammaDetEffA.hsource;
				SpwrRz.asource = GammaDetEffA.asource;
				SpwrRz.ethick = GammaDetEffA.ethick;

			} catch (Exception e) {
				s = "Error in source inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.ethick >= GammaDetEffA.hsource) {
				s = "Source envelope thickness must be smaller than total source height!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			// if(GammaDetEff.hsource<0 || GammaDetEff.asource<0)
			if (GammaDetEffA.hsource < 0 || GammaDetEffA.asource < 0) {
				s = "Error in source inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		} else if (i == GammaDetEffA.SOURCE_MARINELLI) {
			try {
				// GammaDetEff.hsource=EGS4.stringToDouble(hsourceTf.getText());//8.6;
				GammaDetEffA.hsource = EGS4.stringToDouble(hsourceTf.getText());// 8.6;
				// GammaDetEff.hsourceup=EGS4.stringToDouble(hsourceupTf.getText());//3.8;
				GammaDetEffA.hsourceup = EGS4.stringToDouble(hsourceupTf
						.getText());// 3.8;
				// GammaDetEff.asource=EGS4.stringToDouble(asourceTf.getText());//13.25/2;
				GammaDetEffA.asource = EGS4.stringToDouble(asourceTf.getText());// 13.25/2;
				// GammaDetEff.asource=GammaDetEff.asource/2.0;
				GammaDetEffA.asource = GammaDetEffA.asource / 2.0;
				// GammaDetEff.bsource=EGS4.stringToDouble(bsourceTf.getText());//8.55/2;
				GammaDetEffA.bsource = EGS4.stringToDouble(bsourceTf.getText());// 8.55/2;
				// GammaDetEff.bsource=GammaDetEff.bsource/2.0;
				GammaDetEffA.bsource = GammaDetEffA.bsource / 2.0;
				GammaDetA.hsource = EGS4.stringToDouble(hsourceTf.getText());// 8.6;
				GammaDetA.hsourceup = EGS4
						.stringToDouble(hsourceupTf.getText());// 3.8;
				GammaDetA.asource = EGS4.stringToDouble(asourceTf.getText());// 13.25/2;
				GammaDetA.asource = GammaDetA.asource / 2.0;
				GammaDetA.bsource = EGS4.stringToDouble(bsourceTf.getText());// 8.55/2;
				GammaDetA.bsource = GammaDetA.bsource / 2.0;

				GammaDetA.ethick = EGS4.stringToDouble(envelopeThickTf
						.getText());
				GammaDetEffA.ethick = EGS4.stringToDouble(envelopeThickTf
						.getText());

				FluenceRz.hsource = GammaDetEffA.hsource;
				FluenceRz.asource = GammaDetEffA.asource;
				FluenceRz.ethick = GammaDetEffA.ethick;
				FluenceRz.hsourceup = GammaDetEffA.hsourceup;
				FluenceRz.bsource = GammaDetEffA.bsource;

				SpwrRz.hsource = GammaDetEffA.hsource;
				SpwrRz.asource = GammaDetEffA.asource;
				SpwrRz.ethick = GammaDetEffA.ethick;
				SpwrRz.hsourceup = GammaDetEffA.hsourceup;
				SpwrRz.bsource = GammaDetEffA.bsource;

			} catch (Exception e) {
				s = "Error in source inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.ethick >= GammaDetEffA.hsourceup
					|| GammaDetEffA.ethick >= (GammaDetEffA.asource - GammaDetEffA.bsource)) {
				s = "Source envelope thickness must be smaller than upper source height and the dofferences of diameters!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.hsource < 0 || GammaDetEffA.asource < 0) {
				s = "Error in source inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.bsource < adet_tot
					|| GammaDetEffA.bsource > GammaDetEffA.asource) {
				s = "Error. Verify internal source diameter value vs. external source diameter and total detector diameter! "
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (GammaDetEffA.hsourceup >= GammaDetEffA.hsource) {
				s = "Height of upper source part > height of total source? "
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

		} else if (i == SpwrRz.SOURCE_BEAM)// 0//if(i==GammaDetEff.SOURCE_POINT)//0
		{
			try {
				EGS4SrcEns.source_option[0] = EGS4.stringToDouble(sddistTf
						.getText());// 3.8;//100.;
				SpwrRz.beam_angle = EGS4.stringToDouble(anglebeamTf.getText());
				SpwrRz.beam_radius = EGS4.stringToDouble(rbeamTf.getText());
				GammaDetEffA.beam_angle = SpwrRz.beam_angle;
				GammaDetA.beam_angle = SpwrRz.beam_angle;
				FluenceRz.beam_angle = SpwrRz.beam_angle;
				BremsApp2.beam_angle = SpwrRz.beam_angle;
				GammaDetEffA.beam_radius = SpwrRz.beam_radius;
				GammaDetA.beam_radius = SpwrRz.beam_radius;
				FluenceRz.beam_radius = SpwrRz.beam_radius;
				BremsApp2.beam_radius = SpwrRz.beam_radius;
				brunmode4 = true;

			} catch (Exception e)// never see above line 1325
			{
				s = "Distance source-detector and beam_radius must be positive numbers!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (EGS4SrcEns.source_option[0] < 0 || SpwrRz.beam_radius < 0) {
				s = "Distance source-detector and beam_radius must be positive number!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
			EGS4SrcEns.source_option[1] = adet_tot;// 7.3/2;//1.3;
			EGS4SrcEns.source_option[2] = 0.;
			EGS4SrcEns.source_option[3] = 0.;
		}

		// System.out.println("ontra-n la constructie");
		i = sourceTypeCb.getSelectedIndex();
		if (i == GammaDetEffA.SOURCE_POINT || i == SpwrRz.SOURCE_BEAM) {
			constructSourcePointGeometry();// identic
		} else if (i == GammaDetEffA.SOURCE_SARPAGAN) {
			if (asource < adet) {
				constructSarpagan1();
			} else if (asource >= adet && asource < (adet_tot - althick)) {
				constructSarpagan2();
			} else if (asource >= (adet_tot - althick) && asource < adet_tot) {
				constructSarpagan3();
			} else if (asource >= adet_tot) {
				constructSarpagan4();
			}
		} else// MARINELLI
		{
			if (hsource - hsourceup > hdet_tot) {
				constructMarinelli1();
			} else if (hsource - hsourceup <= hdet_tot
					&& hsource - hsourceup > hdet_tot - althick) {
				constructMarinelli2();
			} else if (hsource - hsourceup <= hdet_tot - althick
					&& hsource - hsourceup > winthick + inac_thick_sup + hdet) {
				constructMarinelli3();
			} else if (hsource - hsourceup <= winthick + inac_thick_sup + hdet
					&& hsource - hsourceup > winthick + inac_thick_sup) {
				constructMarinelli4();
			} else if (hsource - hsourceup <= winthick + inac_thick_sup
					&& hsource - hsourceup > winthick) {
				constructMarinelli5();
			} else if (hsource - hsourceup <= winthick) {
				constructMarinelli6();
			}
		}
		GammaDetEffA.MAXZ = EGS4Geom.nNSLAB + 1;
		GammaDetEffA.MINZ = 1;
		GammaDetEffA.MAXR = EGS4Geom.nCyl;
		GammaDetEffA.MINR = 0;
		FluenceRz.MAXZ = GammaDetEffA.MAXZ;
		FluenceRz.MINZ = 1;
		FluenceRz.MAXR = GammaDetEffA.MAXR;
		FluenceRz.MINR = 0;
		// System.out.println("yese din constructie");
		// ============IFIN CS config=====================
		// ============end IFIN CS config=====================
		// ==========POINT SOURCE=======================================
		// ==========end POINT SOURCE=======================================
		// ==============MARINELLII==========================
		// /================================================
		i = einCb.getSelectedIndex();
		if (i == 0) {
			EGS4SrcEns.monoindex = EGS4SrcEns.iMONOENERGETIC;
			BremsApp2.mono = 0;
			BremsApp2.neis = 1;
			try {
				EGS4SrcEns.ikemev = EGS4.stringToDouble(energyTf.getText());// 10.662;
				BremsApp2.eis = new double[1];
				BremsApp2.eis[0] = EGS4SrcEns.ikemev + EGS4.RM
						* Math.abs(BremsApp2.iqin);// 100kev
				BremsApp2.EPHTOP = 0.150;
			} catch (Exception e) {
				s = "Error in energy inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		} else// spectrum
		{
			EGS4SrcEns.monoindex = EGS4SrcEns.iSPECTRUM;
			BremsApp2.mono = 1;

			EGS4SrcEns.enerFilename = spectrumTf.getText();
			BremsApp2.enerFilename = spectrumTf.getText();
			if (EGS4SrcEns.enerFilename.compareTo("") == 0) {
				s = "Invalid spectrum file!" + " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;

			}
		}

		if (incoh_OnRb.isSelected()) {
			GammaDetEffA.incoh = GammaDetEffA.incoh_ON;
			GammaDetA.incoh = GammaDetA.incoh_ON;
		} else {
			GammaDetEffA.incoh = GammaDetEffA.incoh_OFF;
			GammaDetA.incoh = GammaDetA.incoh_OFF;
		}
		FluenceRz.incoh = GammaDetEffA.incoh;
		SpwrRz.incoh = GammaDetEffA.incoh;
		BremsApp2.incoh = GammaDetEffA.incoh;
		if (coh_OnRb.isSelected()) {
			GammaDetEffA.coh = GammaDetEffA.coh_ON;
			GammaDetA.coh = GammaDetA.coh_ON;
		} else {
			GammaDetEffA.coh = GammaDetEffA.coh_OFF;
			GammaDetA.coh = GammaDetA.coh_OFF;
		}
		FluenceRz.coh = GammaDetEffA.coh;
		SpwrRz.coh = GammaDetEffA.coh;
		BremsApp2.coh = GammaDetEffA.coh;
		if (relax_OnRb.isSelected()) {
			GammaDetEffA.relax = GammaDetEffA.relax_ON;
			GammaDetA.relax = GammaDetA.relax_ON;
		} else {
			GammaDetEffA.relax = GammaDetEffA.relax_OFF;
			GammaDetA.relax = GammaDetA.relax_OFF;
		}
		FluenceRz.relax = GammaDetEffA.relax;
		SpwrRz.relax = GammaDetEffA.relax;
		BremsApp2.relax = GammaDetEffA.relax;
		if (pe_OnRb.isSelected()) {
			GammaDetEffA.pe = GammaDetEffA.pe_ang_ON;
			GammaDetA.pe = GammaDetA.pe_ang_ON;
		} else {
			GammaDetEffA.pe = GammaDetEffA.pe_ang_OFF;
			GammaDetA.pe = GammaDetA.pe_ang_OFF;
		}
		FluenceRz.pe = GammaDetEffA.pe;
		SpwrRz.pe = GammaDetEffA.pe;
		BremsApp2.pe = GammaDetEffA.pe;

		i = baCb.getSelectedIndex();
		if (i == 0)
			EGS4.ibrdst = GammaDetEffA.brems_ang_SIMPLE;
		else
			EGS4.ibrdst = GammaDetEffA.brems_ang_KM;

		i = paCb.getSelectedIndex();
		if (i == 0)
			EGS4.iprdst = GammaDetEffA.pair_ang_OFF;
		else if (i == 1)
			EGS4.iprdst = GammaDetEffA.pair_ang_SIMPLE;
		else if (i == 2)
			EGS4.iprdst = GammaDetEffA.pair_ang_KM;
		else if (i == 3)
			EGS4.iprdst = GammaDetEffA.pair_ang_UNIFORM;
		else if (i == 4)
			EGS4.iprdst = GammaDetEffA.pair_ang_BLEND;

		i = bcsCb.getSelectedIndex();
		if (i == 0)
			EGS4.ibr_nist = GammaDetEffA.brems_cross_BH;
		else
			EGS4.ibr_nist = GammaDetEffA.brems_cross_NIST;

		i = pcsCb.getSelectedIndex();
		if (i == 0)
			EGS4.pair_nrc = GammaDetEffA.pair_cross_BH;
		else
			EGS4.pair_nrc = GammaDetEffA.pair_cross_NRC;

		if (spinCh.isSelected()) {
			GammaDetEffA.ispin = GammaDetEffA.spin_ON;
			GammaDetA.ispin = GammaDetA.spin_ON;
		} else {
			GammaDetEffA.ispin = GammaDetEffA.spin_OFF;
			GammaDetA.ispin = GammaDetA.spin_OFF;
		}
		FluenceRz.ispin = GammaDetEffA.ispin;
		SpwrRz.ispin = GammaDetEffA.ispin;
		BremsApp2.ispin = GammaDetEffA.ispin;
		// Electron impact ionization
		EGS4.eii_flag = GammaDetEffA.eii_OFF;// default
		if (eii_OnRb.isSelected())
			EGS4.eii_flag = GammaDetEffA.eii_ON;
		else if (eii_casnatiRb.isSelected())
			EGS4.eii_flag = GammaDetEffA.eii_casnati;
		else if (eii_kolbenstvedtRb.isSelected())
			EGS4.eii_flag = GammaDetEffA.eii_kolbenstvedt;
		else if (eii_gryzinskiRb.isSelected())
			EGS4.eii_flag = GammaDetEffA.eii_gryzinski;

		// photon cross section
		GammaDetEffA.photon_xsection = "";
		GammaDetA.photon_xsection = "";// default
		FluenceRz.photon_xsection = "";
		SpwrRz.photon_xsection = "";
		BremsApp2.photon_xsection = "";
		if (photoxRb_epdl.isSelected()) {
			GammaDetEffA.photon_xsection = EGS4.photon_xsections_epdl;
			GammaDetA.photon_xsection = EGS4.photon_xsections_epdl;
		} else if (photoxRb_xcom.isSelected()) {
			GammaDetEffA.photon_xsection = EGS4.photon_xsections_xcom;
			GammaDetA.photon_xsection = EGS4.photon_xsections_xcom;
		}
		FluenceRz.photon_xsection = GammaDetEffA.photon_xsection;
		SpwrRz.photon_xsection = GammaDetEffA.photon_xsection;
		BremsApp2.photon_xsection = GammaDetEffA.photon_xsection;
		// Triplet
		if (triplet_OnRb.isSelected())
			EGS4.itriplet = GammaDetEffA.triplet_ON;
		else
			EGS4.itriplet = GammaDetEffA.triplet_OFF;
		// Radiative compton correction
		if (radc_OnRb.isSelected())
			EGS4.radc_flag = GammaDetEffA.radc_ON;
		else
			EGS4.radc_flag = GammaDetEffA.radc_OFF;

		i = bcaCb.getSelectedIndex();
		if (i == 0)
			EGS4.bca_algorithm = GammaDetEffA.BCA_EXACT;
		else
			EGS4.bca_algorithm = GammaDetEffA.BCA_PRESTA_I;

		i = esaCb.getSelectedIndex();
		if (i == 0)
			EGS4.transport_algorithm = GammaDetEffA.estep_alg_PRESTA_II;
		else
			EGS4.transport_algorithm = GammaDetEffA.estep_alg_PRESTA_I;

		if (ifull == 2 || ifull == 3 || runmode == 2 || runmode == 3) {
			Integer itg = (Integer) bremsplitSp.getValue();// (Integer)photonsplitSp.getValue();
			// System.out.println(i.intValue());

			EGS4.nbr_split = itg.intValue();// 1;//$MAXBRSPLIT
			if (RRCh.isSelected())
				EGS4.i_play_RR = 1;
			else
				EGS4.i_play_RR = 0;
		}

		if (irejectCh.isSelected()) {
			EGS4Macro.irejct = GammaDetEffA.irejct_ON;
			try {
				GammaDetEffA.ESAVEIN = EGS4.stringToDouble(esaveinTf.getText());
				GammaDetA.ESAVEIN = EGS4.stringToDouble(esaveinTf.getText());
				FluenceRz.ESAVEIN = GammaDetEffA.ESAVEIN;
				SpwrRz.ESAVEIN = GammaDetEffA.ESAVEIN;
			} catch (Exception e) {
				s = "Error in range rejection inputs. Insert positive numbers!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
			if (GammaDetEffA.ESAVEIN < 0.) {
				s = "Error in range rejection inputs. Insert positive numbers!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		} else {
			EGS4Macro.irejct = GammaDetEffA.irejct_OFF;
			GammaDetEffA.ESAVEIN = 2.0;
			GammaDetA.ESAVEIN = 2.0;
			FluenceRz.ESAVEIN = GammaDetEffA.ESAVEIN;
			SpwrRz.ESAVEIN = GammaDetEffA.ESAVEIN;
		}

		try {
			GammaDetEffA.RRZ = EGS4.stringToDouble(zdepthTf.getText());// 0.000;
			GammaDetEffA.RRCUT = EGS4.stringToDouble(zfractionTf.getText());// 0.000;
			GammaDetA.RRZ = EGS4.stringToDouble(zdepthTf.getText());// 0.000;
			GammaDetA.RRCUT = EGS4.stringToDouble(zfractionTf.getText());// 0.000;
			FluenceRz.RRZ = GammaDetEffA.RRZ;
			FluenceRz.RRCUT = GammaDetEffA.RRCUT;
			SpwrRz.RRZ = GammaDetEffA.RRZ;
			SpwrRz.RRCUT = GammaDetEffA.RRCUT;

		} catch (Exception e) {
			s = "Error in photon Russian Roulette inputs. Insert positive numbers!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (GammaDetEffA.RRZ < 0. || GammaDetEffA.RRCUT < 0.) {
			s = "Error in photon Russian Roulette inputs. Insert positive numbers!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;

		}

		Integer itg = (Integer) csenhancementSp.getValue();
		EGS4Macro.cs_enhance = itg.intValue();// 1.0;

		try {
			EGS4Macro.CEXPTR = EGS4.stringToDouble(cbiasTf.getText());// 0.000;
		} catch (Exception e) {
			s = "Error in pathlength biasing inputs. Insert real numbers!"
					+ " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}

		if (forcingCh.isSelected()) {
			GammaDetEffA.IFARCE = GammaDetEffA.IFARCE_ON;
			GammaDetA.IFARCE = GammaDetEffA.IFARCE_ON;
		}// !!
		else {
			GammaDetEffA.IFARCE = GammaDetEffA.IFARCE_OFF;
			GammaDetA.IFARCE = GammaDetEffA.IFARCE_OFF;
		}
		FluenceRz.IFARCE = GammaDetEffA.IFARCE;
		SpwrRz.IFARCE = GammaDetEffA.IFARCE;

		if (forcingCh.isSelected()) {
			itg = (Integer) startforcingSp.getValue();
			EGS4Macro.NFMIN = itg.intValue();// 1;//#Start forcing at this
												// interaction number
			itg = (Integer) stopforcingSp.getValue();
			EGS4Macro.NFMAX = itg.intValue();// 1;//#Number of photon
												// interactions after which
		}

		if (ifull == 0 || ifull == 1) {
			itg = (Integer) photonsplitSp.getValue();
			GammaDetA.phsplitt = itg.intValue();// 1;
		}

		if (runmode == 0)
			new GammaDetA();
		else if (runmode == 1){
			boolean geantB = geantCh.isSelected();
			int monoState = einCb.getSelectedIndex();//if 0 is mono
			//ifull=2 <=> pulseHeight or detection efficiency mode
			if (ifull==2 && monoState==0 && geantB){
				//all checks are already performed
				useGeant = true;
				geant4Run();
			}else{
				useGeant = false;
				new GammaDetEffA();
			}
		}
		else if (runmode == 2) {
			// FluenceRz.$MAXCMPTS=12;
			new FluenceRz();
		} else if (runmode == 3) {
			// FluenceRz.$MAXCMPTS=12;
			new SpwrRz();
		} else if (runmode == 4)// && brunmode4)
		{
			if (brunmode4)
				new BremsApp2();
			else {
				s = "brems application valid only for paralel beam source geometry!"
						+ " \n";
				simTa.append(s);
				stopSimulation();statusL.setText(resources.getString("status.done"));
				return;
			}
		}

		saveeff();

		stopSimulation();// kill all threads
		statusL.setText(resources.getString("status.done"));
	}

	/**
	 * Initiate G program
	 */
	private void gCalc() {
		new G_app(this);
	}

	/**
	 * Initiate Coincidence correction program
	 */
	private void coinCorr() {
		new CoinCorr(this);
	}

	/**
	 * Test several Klein-Nishina algorithms.
	 */
	private void kn_algor_test() {
		simTa.selectAll();
		simTa.replaceSelection("");
		tabs.setSelectedIndex(4);//3);

		String s = "";
		double gen = 0.000;// MeV
		int ii = einCb.getSelectedIndex();

		if (ii == 0) {
			try {
				gen = EGS4.stringToDouble(energyTf.getText());// 10.662;
			} catch (Exception e) {
				s = "Error in energy inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				return;
			}
		} else {
			s = "Monoenrgretic case only!. Insert positive numbers!" + " \n";
			simTa.append(s);
			stopSimulation();statusL.setText(resources.getString("status.done"));
			return;
		}

		int ns = 100000;// default;
		ns = EGS4.stringToInt((String) nPhotonsCb.getSelectedItem());// 20000;
		// EGS4 based
		int ie0_15 = 0;
		int ie15_30 = 0;
		int ie30_45 = 0;
		int ie45_60 = 0;
		int ie60_75 = 0;
		int ie75_90 = 0;
		int ie90_105 = 0;
		int ie105_120 = 0;
		int ie120_135 = 0;
		int ie135_150 = 0;
		int ie150_165 = 0;
		int ie165_180 = 0;
		double thetae = 0.0;
		// Kahn based
		int ik0_15 = 0;
		int ik15_30 = 0;
		int ik30_45 = 0;
		int ik45_60 = 0;
		int ik60_75 = 0;
		int ik75_90 = 0;
		int ik90_105 = 0;
		int ik105_120 = 0;
		int ik120_135 = 0;
		int ik135_150 = 0;
		int ik150_165 = 0;
		int ik165_180 = 0;
		double thetak = 0.0;
		// Clasic based
		int ic0_15 = 0;
		int ic15_30 = 0;
		int ic30_45 = 0;
		int ic45_60 = 0;
		int ic60_75 = 0;
		int ic75_90 = 0;
		int ic90_105 = 0;
		int ic105_120 = 0;
		int ic120_135 = 0;
		int ic135_150 = 0;
		int ic150_165 = 0;
		int ic165_180 = 0;
		double thetac = 0.0;
		// Wielopolski based
		int iw0_15 = 0;
		int iw15_30 = 0;
		int iw30_45 = 0;
		int iw45_60 = 0;
		int iw60_75 = 0;
		int iw75_90 = 0;
		int iw90_105 = 0;
		int iw105_120 = 0;
		int iw120_135 = 0;
		int iw135_150 = 0;
		int iw150_165 = 0;
		int iw165_180 = 0;
		double thetaw = 0.0;

		for (int i = 1; i <= ns; i++) {
			thetae = GammaDetector.comptonSimE(gen);
			thetae = thetae * 180 / Math.PI;// degrees
			thetak = GammaDetector.comptonSimK(gen);
			thetak = thetak * 180 / Math.PI;// degrees
			thetac = GammaDetector.comptonSimC(gen);
			thetac = thetac * 180 / Math.PI;// degrees
			thetaw = GammaDetector.comptonSimW(gen);
			thetaw = thetaw * 180 / Math.PI;// degrees

			if (thetae >= 0. && thetae < 15.) {
				ie0_15++;
			} else if (thetae >= 15. && thetae < 30.) {
				ie15_30++;
			} else if (thetae >= 30. && thetae < 45.) {
				ie30_45++;
			} else if (thetae >= 45. && thetae < 60.) {
				ie45_60++;
			} else if (thetae >= 60. && thetae < 75.) {
				ie60_75++;
			} else if (thetae >= 75. && thetae < 90.) {
				ie75_90++;
			} else if (thetae >= 90. && thetae < 105.) {
				ie90_105++;
			} else if (thetae >= 105. && thetae < 120.) {
				ie105_120++;
			} else if (thetae >= 120. && thetae < 135.) {
				ie120_135++;
			} else if (thetae >= 135. && thetae < 150.) {
				ie135_150++;
			} else if (thetae >= 150. && thetae < 165.) {
				ie150_165++;
			} else if (thetae >= 165. && thetae <= 180.) {
				ie165_180++;
			}

			if (thetak >= 0. && thetak < 15.) {
				ik0_15++;
			} else if (thetak >= 15. && thetak < 30.) {
				ik15_30++;
			} else if (thetak >= 30. && thetak < 45.) {
				ik30_45++;
			} else if (thetak >= 45. && thetak < 60.) {
				ik45_60++;
			} else if (thetak >= 60. && thetak < 75.) {
				ik60_75++;
			} else if (thetak >= 75. && thetak < 90.) {
				ik75_90++;
			} else if (thetak >= 90. && thetak < 105.) {
				ik90_105++;
			} else if (thetak >= 105. && thetak < 120.) {
				ik105_120++;
			} else if (thetak >= 120. && thetak < 135.) {
				ik120_135++;
			} else if (thetak >= 135. && thetak < 150.) {
				ik135_150++;
			} else if (thetak >= 150. && thetak < 165.) {
				ik150_165++;
			} else if (thetak >= 165. && thetak <= 180.) {
				ik165_180++;
			}

			if (thetac >= 0. && thetac < 15.) {
				ic0_15++;
			} else if (thetac >= 15. && thetac < 30.) {
				ic15_30++;
			} else if (thetac >= 30. && thetac < 45.) {
				ic30_45++;
			} else if (thetac >= 45. && thetac < 60.) {
				ic45_60++;
			} else if (thetac >= 60. && thetac < 75.) {
				ic60_75++;
			} else if (thetac >= 75. && thetac < 90.) {
				ic75_90++;
			} else if (thetac >= 90. && thetac < 105.) {
				ic90_105++;
			} else if (thetac >= 105. && thetac < 120.) {
				ic105_120++;
			} else if (thetac >= 120. && thetac < 135.) {
				ic120_135++;
			} else if (thetac >= 135. && thetac < 150.) {
				ic135_150++;
			} else if (thetac >= 150. && thetac < 165.) {
				ic150_165++;
			} else if (thetac >= 165. && thetac <= 180.) {
				ic165_180++;
			}

			if (thetaw >= 0. && thetaw < 15.) {
				iw0_15++;
			} else if (thetaw >= 15. && thetaw < 30.) {
				iw15_30++;
			} else if (thetaw >= 30. && thetaw < 45.) {
				iw30_45++;
			} else if (thetaw >= 45. && thetaw < 60.) {
				iw45_60++;
			} else if (thetaw >= 60. && thetaw < 75.) {
				iw60_75++;
			} else if (thetaw >= 75. && thetaw < 90.) {
				iw75_90++;
			} else if (thetaw >= 90. && thetaw < 105.) {
				iw90_105++;
			} else if (thetaw >= 105. && thetaw < 120.) {
				iw105_120++;
			} else if (thetaw >= 120. && thetaw < 135.) {
				iw120_135++;
			} else if (thetaw >= 135. && thetaw < 150.) {
				iw135_150++;
			} else if (thetaw >= 150. && thetaw < 165.) {
				iw150_165++;
			} else if (thetaw >= 165. && thetaw <= 180.) {
				iw165_180++;
			}

		}

		Integer nn = new Integer(ns);

		double de0_15 = 100. * ie0_15 / nn.doubleValue();
		double de15_30 = 100. * ie15_30 / nn.doubleValue();
		double de30_45 = 100. * ie30_45 / nn.doubleValue();
		double de45_60 = 100. * ie45_60 / nn.doubleValue();
		double de60_75 = 100. * ie60_75 / nn.doubleValue();
		double de75_90 = 100. * ie75_90 / nn.doubleValue();
		double de90_105 = 100. * ie90_105 / nn.doubleValue();
		double de105_120 = 100. * ie105_120 / nn.doubleValue();
		double de120_135 = 100. * ie120_135 / nn.doubleValue();
		double de135_150 = 100. * ie135_150 / nn.doubleValue();
		double de150_165 = 100. * ie150_165 / nn.doubleValue();
		double de165_180 = 100. * ie165_180 / nn.doubleValue();
		double sume = de0_15 + de15_30 + de30_45 + de45_60 + de60_75 + de75_90
				+ de90_105 + de105_120 + de120_135 + de135_150 + de150_165
				+ de165_180;

		double dk0_15 = 100. * ik0_15 / nn.doubleValue();
		double dk15_30 = 100. * ik15_30 / nn.doubleValue();
		double dk30_45 = 100. * ik30_45 / nn.doubleValue();
		double dk45_60 = 100. * ik45_60 / nn.doubleValue();
		double dk60_75 = 100. * ik60_75 / nn.doubleValue();
		double dk75_90 = 100. * ik75_90 / nn.doubleValue();
		double dk90_105 = 100. * ik90_105 / nn.doubleValue();
		double dk105_120 = 100. * ik105_120 / nn.doubleValue();
		double dk120_135 = 100. * ik120_135 / nn.doubleValue();
		double dk135_150 = 100. * ik135_150 / nn.doubleValue();
		double dk150_165 = 100. * ik150_165 / nn.doubleValue();
		double dk165_180 = 100. * ik165_180 / nn.doubleValue();
		double sumk = dk0_15 + dk15_30 + dk30_45 + dk45_60 + dk60_75 + dk75_90
				+ dk90_105 + dk105_120 + dk120_135 + dk135_150 + dk150_165
				+ dk165_180;

		double dc0_15 = 100. * ic0_15 / nn.doubleValue();
		double dc15_30 = 100. * ic15_30 / nn.doubleValue();
		double dc30_45 = 100. * ic30_45 / nn.doubleValue();
		double dc45_60 = 100. * ic45_60 / nn.doubleValue();
		double dc60_75 = 100. * ic60_75 / nn.doubleValue();
		double dc75_90 = 100. * ic75_90 / nn.doubleValue();
		double dc90_105 = 100. * ic90_105 / nn.doubleValue();
		double dc105_120 = 100. * ic105_120 / nn.doubleValue();
		double dc120_135 = 100. * ic120_135 / nn.doubleValue();
		double dc135_150 = 100. * ic135_150 / nn.doubleValue();
		double dc150_165 = 100. * ic150_165 / nn.doubleValue();
		double dc165_180 = 100. * ic165_180 / nn.doubleValue();
		double sumc = dc0_15 + dc15_30 + dc30_45 + dc45_60 + dc60_75 + dc75_90
				+ dc90_105 + dc105_120 + dc120_135 + dc135_150 + dc150_165
				+ dc165_180;

		double dw0_15 = 100. * iw0_15 / nn.doubleValue();
		double dw15_30 = 100. * iw15_30 / nn.doubleValue();
		double dw30_45 = 100. * iw30_45 / nn.doubleValue();
		double dw45_60 = 100. * iw45_60 / nn.doubleValue();
		double dw60_75 = 100. * iw60_75 / nn.doubleValue();
		double dw75_90 = 100. * iw75_90 / nn.doubleValue();
		double dw90_105 = 100. * iw90_105 / nn.doubleValue();
		double dw105_120 = 100. * iw105_120 / nn.doubleValue();
		double dw120_135 = 100. * iw120_135 / nn.doubleValue();
		double dw135_150 = 100. * iw135_150 / nn.doubleValue();
		double dw150_165 = 100. * iw150_165 / nn.doubleValue();
		double dw165_180 = 100. * iw165_180 / nn.doubleValue();
		double sumw = dw0_15 + dw15_30 + dw30_45 + dw45_60 + dw60_75 + dw75_90
				+ dw90_105 + dw105_120 + dw120_135 + dw135_150 + dw150_165
				+ dw165_180;

		NumberFormat nfe = NumberFormat.getInstance(Locale.US);
		nfe.setMinimumFractionDigits(5);// default e 2 oricum!!
		nfe.setMaximumFractionDigits(5);// default e 2 oricum!!
		nfe.setGroupingUsed(false);

		s = "------------------------------------------------------------------------------"
				+ " \n";
		s = s + "EGS based: Weight of 0-15 degrees angle interval (%)= "
				+ nfe.format(de0_15) + " \n";
		s = s + "EGS based: Weight of 15-30 degrees angle interval (%)= "
				+ nfe.format(de15_30) + " \n";
		s = s + "EGS based: Weight of 30-45 degrees angle interval (%)= "
				+ nfe.format(de30_45) + " \n";
		s = s + "EGS based: Weight of 45-60 degrees angle interval (%)= "
				+ nfe.format(de45_60) + " \n";
		s = s + "EGS based: Weight of 60-75 degrees angle interval (%)= "
				+ nfe.format(de60_75) + " \n";
		s = s + "EGS based: Weight of 75-90 degrees angle interval (%)= "
				+ nfe.format(de75_90) + " \n";
		s = s + "EGS based: Weight of 90-105 degrees angle interval (%)= "
				+ nfe.format(de90_105) + " \n";
		s = s + "EGS based: Weight of 105-120 degrees angle interval (%)= "
				+ nfe.format(de105_120) + " \n";
		s = s + "EGS based: Weight of 120-135 degrees angle interval (%)= "
				+ nfe.format(de120_135) + " \n";
		s = s + "EGS based: Weight of 135-150 degrees angle interval (%)= "
				+ nfe.format(de135_150) + " \n";
		s = s + "EGS based: Weight of 150-165 degrees angle interval (%)= "
				+ nfe.format(de150_165) + " \n";
		s = s + "EGS based: Weight of 165-180 degrees angle interval (%)= "
				+ nfe.format(de165_180) + " \n";
		s = s + "SUM =" + nfe.format(sume) + " \n";
		s = s
				+ "-------------------------------------------------------------------------------"
				+ " \n";
		s = s + "Kahn: Weight of 0-15 degrees angle interval (%)= "
				+ nfe.format(dk0_15) + " \n";
		s = s + "Kahn: Weight of 15-30 degrees angle interval (%)= "
				+ nfe.format(dk15_30) + " \n";
		s = s + "Kahn: Weight of 30-45 degrees angle interval (%)= "
				+ nfe.format(dk30_45) + " \n";
		s = s + "Kahn: Weight of 45-60 degrees angle interval (%)= "
				+ nfe.format(dk45_60) + " \n";
		s = s + "Kahn: Weight of 60-75 degrees angle interval (%)= "
				+ nfe.format(dk60_75) + " \n";
		s = s + "Kahn: Weight of 75-90 degrees angle interval (%)= "
				+ nfe.format(dk75_90) + " \n";
		s = s + "Kahn: Weight of 90-105 degrees angle interval (%)= "
				+ nfe.format(dk90_105) + " \n";
		s = s + "Kahn: Weight of 105-120 degrees angle interval (%)= "
				+ nfe.format(dk105_120) + " \n";
		s = s + "Kahn: Weight of 120-135 degrees angle interval (%)= "
				+ nfe.format(dk120_135) + " \n";
		s = s + "Kahn: Weight of 135-150 degrees angle interval (%)= "
				+ nfe.format(dk135_150) + " \n";
		s = s + "Kahn: Weight of 150-165 degrees angle interval (%)= "
				+ nfe.format(dk150_165) + " \n";
		s = s + "Kahn: Weight of 165-180 degrees angle interval (%)= "
				+ nfe.format(dk165_180) + " \n";
		s = s + "SUM =" + nfe.format(sumk) + " \n";
		s = s
				+ "-------------------------------------------------------------------------------"
				+ " \n";
		s = s + "'Classic': Weight of 0-15 degrees angle interval (%)= "
				+ nfe.format(dc0_15) + " \n";
		s = s + "'Classic': Weight of 15-30 degrees angle interval (%)= "
				+ nfe.format(dc15_30) + " \n";
		s = s + "'Classic': Weight of 30-45 degrees angle interval (%)= "
				+ nfe.format(dc30_45) + " \n";
		s = s + "'Classic': Weight of 45-60 degrees angle interval (%)= "
				+ nfe.format(dc45_60) + " \n";
		s = s + "'Classic': Weight of 60-75 degrees angle interval (%)= "
				+ nfe.format(dc60_75) + " \n";
		s = s + "'Classic': Weight of 75-90 degrees angle interval (%)= "
				+ nfe.format(dc75_90) + " \n";
		s = s + "'Classic': Weight of 90-105 degrees angle interval (%)= "
				+ nfe.format(dc90_105) + " \n";
		s = s + "'Classic': Weight of 105-120 degrees angle interval (%)= "
				+ nfe.format(dc105_120) + " \n";
		s = s + "'Classic': Weight of 120-135 degrees angle interval (%)= "
				+ nfe.format(dc120_135) + " \n";
		s = s + "'Classic': Weight of 135-150 degrees angle interval (%)= "
				+ nfe.format(dc135_150) + " \n";
		s = s + "'Classic': Weight of 150-165 degrees angle interval (%)= "
				+ nfe.format(dc150_165) + " \n";
		s = s + "'Classic': Weight of 165-180 degrees angle interval (%)= "
				+ nfe.format(dc165_180) + " \n";
		s = s + "SUM =" + nfe.format(sumc) + " \n";
		s = s
				+ "-------------------------------------------------------------------------------"
				+ " \n";
		s = s + "Wielopolski: Weight of 0-15 degrees angle interval (%)= "
				+ nfe.format(dw0_15) + " \n";
		s = s + "Wielopolski: Weight of 15-30 degrees angle interval (%)= "
				+ nfe.format(dw15_30) + " \n";
		s = s + "Wielopolski: Weight of 30-45 degrees angle interval (%)= "
				+ nfe.format(dw30_45) + " \n";
		s = s + "Wielopolski: Weight of 45-60 degrees angle interval (%)= "
				+ nfe.format(dw45_60) + " \n";
		s = s + "Wielopolski: Weight of 60-75 degrees angle interval (%)= "
				+ nfe.format(dw60_75) + " \n";
		s = s + "Wielopolski: Weight of 75-90 degrees angle interval (%)= "
				+ nfe.format(dw75_90) + " \n";
		s = s + "Wielopolski: Weight of 90-105 degrees angle interval (%)= "
				+ nfe.format(dw90_105) + " \n";
		s = s + "Wielopolski: Weight of 105-120 degrees angle interval (%)= "
				+ nfe.format(dw105_120) + " \n";
		s = s + "Wielopolski: Weight of 120-135 degrees angle interval (%)= "
				+ nfe.format(dw120_135) + " \n";
		s = s + "Wielopolski: Weight of 135-150 degrees angle interval (%)= "
				+ nfe.format(dw135_150) + " \n";
		s = s + "Wielopolski: Weight of 150-165 degrees angle interval (%)= "
				+ nfe.format(dw150_165) + " \n";
		s = s + "Wielopolski: Weight of 165-180 degrees angle interval (%)= "
				+ nfe.format(dw165_180) + " \n";
		s = s + "SUM =" + nfe.format(sumw) + " \n";
		s = s
				+ "-------------------------------------------------------------------------------"
				+ " \n";

		// System.out.println(s);
		simTa.append(s);
	}

	/**
	 * Initiate Nuclide interference correction program. 
	 */
	private void interf() {
		new Interf(this);
	}

	/**
	 * Initiate Efficiency utilities program.
	 */
	private void effUtilities() {
		new EffUtilities(this);
	}

	/**
	 * Create material using EPGCS program.
	 */
	private void createMaterial() {
		// System.out.println("jfsagklajsrgkl");
		new EPGCS(this);
	}
	
	/**
	 * Construct source point geometry.
	 */
	private void constructSourcePointGeometry()
	{
		EGS4Geom.nNSLAB=6;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=stddist;//source to detector distance
	    EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+winthick;//+window thickness
		EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+inac_thick_sup;
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+hdet;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet_tot-winthick-althick-hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+althick;

		EGS4Geom.nCyl=3;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=12;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		if(!noSup)
		{
		EGS4.MEDIA[10]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[11]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air";
		}

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=12;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//Air
		EGS4Geom.MEDNUM[10]=11;//Al
		EGS4Geom.MEDNUM[11]=12;//Al

		EGS4Geom.nNREGLO=12;
		EGS4Geom.nNREGHI=12;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=19;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=4;//START REGION
			EGS4Geom.NREGHI[1]=4;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=6;//START REGION
			EGS4Geom.NREGHI[2]=6;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=10;//START REGION
			EGS4Geom.NREGHI[3]=10;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=11;//START REGION
			EGS4Geom.NREGHI[4]=11;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=12;//START REGION
			EGS4Geom.NREGHI[5]=12;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=5;//START REGION
			EGS4Geom.NREGHI[6]=5;//STOP REGION=>NAI
		EGS4Geom.NREGLO[7]=2;//START REGION
			EGS4Geom.NREGHI[7]=2;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=8;//START REGION
			EGS4Geom.NREGHI[8]=8;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=14;//START REGION
			EGS4Geom.NREGHI[9]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=3;//START REGION
			EGS4Geom.NREGHI[10]=3;//STOP REGION=>Al or Be
		EGS4Geom.NREGLO[11]=9;//START REGION
			EGS4Geom.NREGHI[11]=9;//STOP REGION=>Al or Be

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=5;//REGION NUMBERS OF THE CAVITY= 4

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=5;//where to score pulse height distribution
	}

	/**
	 * Based on source-detector geometry, construct type 1 Sarpagan geometry.
	 */
	private void constructSarpagan1()
	{
		EGS4Geom.nNSLAB=7;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsource-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource!!
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+althick;

		EGS4Geom.nCyl=4;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=asource;
		EGS4Geom.RCYL[2]=adet;//=EGS4Geom.RCYL[1]+adet-asource;
		EGS4Geom.RCYL[3]=adet_tot-althick;//EGS4Geom.RCYL[2]+adet_tot-althick-adet;
		EGS4Geom.RCYL[4]=adet_tot;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=21;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[8]=airmaterialTf.getText();//"vacuum";
		EGS4.MEDIA[9]=naimaterialTf.getText();//"sodiumiodide";
		EGS4.MEDIA[10]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[12]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[13]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[14]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[15]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[16]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[17]=pointstodmaterialTf.getText();//"surr by air";
		if(!noSup)
		{
		EGS4.MEDIA[18]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[19]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[20]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[18]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[19]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[20]=pointstodmaterialTf.getText();//"air";
		}

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=21;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//NAI
		EGS4Geom.MEDNUM[10]=11;//H2O
		EGS4Geom.MEDNUM[11]=12;//air_surr
		EGS4Geom.MEDNUM[12]=13;//air_surr
		EGS4Geom.MEDNUM[13]=14;//air_surr
		EGS4Geom.MEDNUM[14]=15;//plastic
		EGS4Geom.MEDNUM[15]=16;//air_surr
		EGS4Geom.MEDNUM[16]=17;//air_surr
		EGS4Geom.MEDNUM[17]=18;//air_surr
		EGS4Geom.MEDNUM[18]=19;//win-e.g. Be
		EGS4Geom.MEDNUM[19]=20;//win-e.g. Be
		EGS4Geom.MEDNUM[20]=21;//win-e.g. Be

		EGS4Geom.nNREGLO=21;
		EGS4Geom.nNREGHI=21;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=29;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=19;//START REGION
			EGS4Geom.NREGHI[3]=19;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=20;//START REGION
			EGS4Geom.NREGHI[4]=20;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=21;//START REGION
			EGS4Geom.NREGHI[5]=21;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=12;//START REGION
			EGS4Geom.NREGHI[7]=12;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=14;//START REGION
			EGS4Geom.NREGHI[8]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=13;//START REGION
			EGS4Geom.NREGHI[9]=13;//STOP REGION=>NAI
		//--------------------------------------------
		EGS4Geom.NREGLO[10]=2;//START REGION
			EGS4Geom.NREGHI[10]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[11]=9;//START REGION
			EGS4Geom.NREGHI[11]=9;//STOP REGION=>sursa
		EGS4Geom.NREGLO[12]=16;//START REGION
			EGS4Geom.NREGHI[12]=16;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=23;//START REGION
			EGS4Geom.NREGHI[13]=23;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=3;//START REGION
			EGS4Geom.NREGHI[14]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[15]=10;//START REGION
			EGS4Geom.NREGHI[15]=10;//STOP REGION=>plastic
		EGS4Geom.NREGLO[16]=17;//START REGION
			EGS4Geom.NREGHI[16]=17;//STOP REGION=>plastic
		EGS4Geom.NREGLO[17]=24;//START REGION
			EGS4Geom.NREGHI[17]=24;//STOP REGION=>plastic
		EGS4Geom.NREGLO[18]=4;//START REGION
			EGS4Geom.NREGHI[18]=4;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[20]=18;//START REGION
			EGS4Geom.NREGHI[20]=18;//STOP REGION=>win-BE

		EGS4Geom.NSUMCV=2;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY
		EGS4Geom.ISUMCV[1]=13;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=2;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution
		GammaDetEffA.REGSVOL[1]=13;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 2 Sarpagan geometry.
	 */
	private void constructSarpagan2()
	{
		EGS4Geom.nNSLAB=7;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsource-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+althick;//=hdet_tot+hsource!!

		EGS4Geom.nCyl=4;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=asource;
		EGS4Geom.RCYL[3]=adet_tot-althick;//EGS4Geom.RCYL[2]+adet_tot-althick-adet;
		EGS4Geom.RCYL[4]=adet_tot;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=21;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[11]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[12]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[13]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[14]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[15]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[16]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[17]=pointstodmaterialTf.getText();//"surr by air";
		if(!noSup)
		{
		EGS4.MEDIA[18]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[19]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[20]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[18]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[19]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[20]=pointstodmaterialTf.getText();//"air";
		}

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=21;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//H2O
		EGS4Geom.MEDNUM[11]=12;//H2O
		EGS4Geom.MEDNUM[12]=13;//air_surr
		EGS4Geom.MEDNUM[13]=14;//air_surr
		EGS4Geom.MEDNUM[14]=15;//plastic
		EGS4Geom.MEDNUM[15]=16;//plastic
		EGS4Geom.MEDNUM[16]=17;//air_surr
		EGS4Geom.MEDNUM[17]=18;//air_surr
		EGS4Geom.MEDNUM[18]=19;//win-e.g. Be
		EGS4Geom.MEDNUM[19]=20;//win-e.g. Be
		EGS4Geom.MEDNUM[20]=21;//win-e.g. Be

		EGS4Geom.nNREGLO=21;
		EGS4Geom.nNREGHI=21;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=29;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=19;//START REGION
			EGS4Geom.NREGHI[3]=19;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=20;//START REGION
			EGS4Geom.NREGHI[4]=20;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=21;//START REGION
			EGS4Geom.NREGHI[5]=21;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=12;//START REGION
			EGS4Geom.NREGHI[7]=12;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=14;//START REGION
			EGS4Geom.NREGHI[8]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=13;//START REGION
			EGS4Geom.NREGHI[9]=13;//STOP REGION=>AIR
		//--------------------------------------------
		EGS4Geom.NREGLO[10]=2;//START REGION
			EGS4Geom.NREGHI[10]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[11]=9;//START REGION
			EGS4Geom.NREGHI[11]=9;//STOP REGION=>sursa
		EGS4Geom.NREGLO[12]=16;//START REGION
			EGS4Geom.NREGHI[12]=16;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=23;//START REGION
			EGS4Geom.NREGHI[13]=23;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=3;//START REGION
			EGS4Geom.NREGHI[14]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[15]=10;//START REGION
			EGS4Geom.NREGHI[15]=10;//STOP REGION=>plastic
		EGS4Geom.NREGLO[16]=17;//START REGION
			EGS4Geom.NREGHI[16]=17;//STOP REGION=>plastic
		EGS4Geom.NREGLO[17]=24;//START REGION
			EGS4Geom.NREGHI[17]=24;//STOP REGION=>plastic
		EGS4Geom.NREGLO[18]=4;//START REGION
			EGS4Geom.NREGHI[18]=4;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[20]=18;//START REGION
			EGS4Geom.NREGHI[20]=18;//STOP REGION=>win-BE

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 3 Sarpagan geometry.
	 */
	private void constructSarpagan3()
	{
		EGS4Geom.nNSLAB=7;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsource-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+althick;//=hdet_tot+hsource!!

		EGS4Geom.nCyl=4;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=asource;
		EGS4Geom.RCYL[4]=adet_tot;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=20;//21;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=almaterialTf.getText();//"AL";
		EGS4.MEDIA[8]=almaterialTf.getText();//"AL";
		EGS4.MEDIA[9]=almaterialTf.getText();//"AL";
		EGS4.MEDIA[10]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[11]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=pointstodmaterialTf.getText();//"surr by air";
		EGS4.MEDIA[14]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[15]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[16]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[17]=pointstodmaterialTf.getText();//"surr by air";
		if(!noSup)
		{
		EGS4.MEDIA[18]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[19]=windowmaterialTf.getText();//"aluminum";
		//EGS4.MEDIA[20]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[18]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[19]=pointstodmaterialTf.getText();//"air";
		//EGS4.MEDIA[20]=pointstodmaterialTf.getText();//"air";
		}

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=20;//21;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//H2O
		EGS4Geom.MEDNUM[11]=12;//H2O
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//air_surr
		EGS4Geom.MEDNUM[14]=15;//plastic
		EGS4Geom.MEDNUM[15]=16;//plastic
		EGS4Geom.MEDNUM[16]=17;//plastic
		EGS4Geom.MEDNUM[17]=18;//air_surr
		EGS4Geom.MEDNUM[18]=19;//win-e.g. Be
		EGS4Geom.MEDNUM[19]=20;//win-e.g. Be
		//EGS4Geom.MEDNUM[20]=21;//win-e.g. Be

		EGS4Geom.nNREGLO=20;//21;
		EGS4Geom.nNREGHI=20;//21;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=29;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=12;//START REGION
			EGS4Geom.NREGHI[3]=12;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=19;//START REGION
			EGS4Geom.NREGHI[7]=19;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=20;//START REGION
			EGS4Geom.NREGHI[8]=20;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=21;//START REGION
			EGS4Geom.NREGHI[9]=21;//STOP REGION=>AIR
		//--------------------------------------------
		EGS4Geom.NREGLO[10]=2;//START REGION
			EGS4Geom.NREGHI[10]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[11]=9;//START REGION
			EGS4Geom.NREGHI[11]=9;//STOP REGION=>sursa
		EGS4Geom.NREGLO[12]=16;//START REGION
			EGS4Geom.NREGHI[12]=16;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=23;//START REGION
			EGS4Geom.NREGHI[13]=23;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=3;//START REGION
			EGS4Geom.NREGHI[14]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[15]=10;//START REGION
			EGS4Geom.NREGHI[15]=10;//STOP REGION=>plastic
		EGS4Geom.NREGLO[16]=17;//START REGION
			EGS4Geom.NREGHI[16]=17;//STOP REGION=>plastic
		EGS4Geom.NREGLO[17]=24;//START REGION
			EGS4Geom.NREGHI[17]=24;//STOP REGION=>plastic
		EGS4Geom.NREGLO[18]=4;//START REGION
			EGS4Geom.NREGHI[18]=4;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>win-BE
		//EGS4Geom.NREGLO[20]=18;//START REGION
		//	EGS4Geom.NREGHI[20]=18;//STOP REGION=>win-BE

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 4 Sarpagan geometry.
	 */
	private void constructSarpagan4()
	{
		EGS4Geom.nNSLAB=7;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsource-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+althick;//=hdet_tot+hsource!!

		EGS4Geom.nCyl=4;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=22;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[11]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[15]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[16]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[17]=envelopematerialTf.getText();//"plastic";
		if(!noSup)
		{
		EGS4.MEDIA[18]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[19]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[18]=pointstodmaterialTf.getText();//"air";
		EGS4.MEDIA[19]=pointstodmaterialTf.getText();//"air";
		}
		EGS4.MEDIA[20]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[21]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=22;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//H2O
		EGS4Geom.MEDNUM[11]=12;//H2O
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//plastic
		EGS4Geom.MEDNUM[15]=16;//plastic
		EGS4Geom.MEDNUM[16]=17;//plastic
		EGS4Geom.MEDNUM[17]=18;//plastic
		EGS4Geom.MEDNUM[18]=19;//win-e.g. Be
		EGS4Geom.MEDNUM[19]=20;//win-e.g. Be
		EGS4Geom.MEDNUM[20]=21;//AIR
		EGS4Geom.MEDNUM[21]=22;//AIR

		EGS4Geom.nNREGLO=22;
		EGS4Geom.nNREGHI=22;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=29;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=12;//START REGION
			EGS4Geom.NREGHI[3]=12;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=26;//START REGION
			EGS4Geom.NREGHI[7]=26;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=27;//START REGION
			EGS4Geom.NREGHI[8]=27;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=28;//START REGION
			EGS4Geom.NREGHI[9]=28;//STOP REGION=>AIR
		//--------------------------------------------
		EGS4Geom.NREGLO[10]=2;//START REGION
			EGS4Geom.NREGHI[10]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[11]=9;//START REGION
			EGS4Geom.NREGHI[11]=9;//STOP REGION=>sursa
		EGS4Geom.NREGLO[12]=16;//START REGION
			EGS4Geom.NREGHI[12]=16;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=23;//START REGION
			EGS4Geom.NREGHI[13]=23;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=3;//START REGION
			EGS4Geom.NREGHI[14]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[15]=10;//START REGION
			EGS4Geom.NREGHI[15]=10;//STOP REGION=>plastic
		EGS4Geom.NREGLO[16]=17;//START REGION
			EGS4Geom.NREGHI[16]=17;//STOP REGION=>plastic
		EGS4Geom.NREGLO[17]=24;//START REGION
			EGS4Geom.NREGHI[17]=24;//STOP REGION=>plastic
		EGS4Geom.NREGLO[18]=4;//START REGION
			EGS4Geom.NREGHI[18]=4;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>win-BE
		EGS4Geom.NREGLO[20]=25;//START REGION
			EGS4Geom.NREGHI[20]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[21]=29;//START REGION
			EGS4Geom.NREGHI[21]=29;//STOP REGION=>AIR

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 1 Marinelli geometry.
	 */
	private void constructMarinelli1()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+althick;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=hsource;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[27]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[28]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[29]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[30]=envelopematerialTf.getText();//"plastic";
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=sourcematerialTf.getText();//"water_liquid";
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[39]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[40]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR

		EGS4Geom.nNREGLO=42;
		EGS4Geom.nNREGHI=42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 2 Marinelli geometry.
	 */
	private void constructMarinelli2()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;
		EGS4Geom.ZPLANE[7]=hsource;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=EGS4Geom.ZPLANE[6]+althick;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[27]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[28]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[29]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[30]=pointstodmaterialTf.getText();
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=pointstodmaterialTf.getText();
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=almaterialTf.getText();
		EGS4.MEDIA[39]=almaterialTf.getText();
		EGS4.MEDIA[40]=almaterialTf.getText();
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR

		EGS4Geom.nNREGLO=42;
		EGS4Geom.nNREGHI=42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		//-----------------------------------------
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		//-------------------------------------------------
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
			//----------------------------
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
			//------------------------------
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR

		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 3 Marinelli geometry.
	 */
	private void constructMarinelli3()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[6]=hsource;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[5]+hdet_tot-winthick-inac_thick_sup-althick-hdet;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=EGS4Geom.ZPLANE[7]+althick;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=45;//42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[27]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[28]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[29]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[30]=pointstodmaterialTf.getText();
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=pointstodmaterialTf.getText();
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=almaterialTf.getText();
		EGS4.MEDIA[39]=almaterialTf.getText();
		EGS4.MEDIA[40]=almaterialTf.getText();
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();
		//==================
		EGS4.MEDIA[42]=airmaterialTf.getText();
		EGS4.MEDIA[43]=airmaterialTf.getText();
		EGS4.MEDIA[44]=almaterialTf.getText();

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=45;//42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR
		EGS4Geom.MEDNUM[42]=43;//AIR
		EGS4Geom.MEDNUM[43]=44;//AIR
		EGS4Geom.MEDNUM[44]=45;//AIR

		EGS4Geom.nNREGLO=45;//42;
		EGS4Geom.nNREGHI=45;//42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		//-----------------------------------------
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		//-------------------------------------------------
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
			//----------------------------
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
			//------------------------------
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR
		//-------
		EGS4Geom.NREGLO[42]=8;//START REGION
			EGS4Geom.NREGHI[42]=8;//STOP REGION=>AIR
		EGS4Geom.NREGLO[43]=16;//START REGION
			EGS4Geom.NREGHI[43]=16;//STOP REGION=>AIR
		EGS4Geom.NREGLO[44]=24;//START REGION
			EGS4Geom.NREGHI[44]=24;//STOP REGION=>AIR


		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 4 Marinelli geometry.
	 */
	private void constructMarinelli4()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[5]=hsource;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[4]+hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+hdet_tot-winthick-inac_thick_sup-althick-hdet;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=EGS4Geom.ZPLANE[7]+althick;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=45;//42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=naimaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=naimaterialTf.getText();//"sodiumiodide";
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[27]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[28]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[29]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[30]=pointstodmaterialTf.getText();
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=pointstodmaterialTf.getText();
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=almaterialTf.getText();
		EGS4.MEDIA[39]=almaterialTf.getText();
		EGS4.MEDIA[40]=almaterialTf.getText();
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();
		//==================
		EGS4.MEDIA[42]=airmaterialTf.getText();
		EGS4.MEDIA[43]=airmaterialTf.getText();
		EGS4.MEDIA[44]=almaterialTf.getText();

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=45;//42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR
		EGS4Geom.MEDNUM[42]=43;//AIR
		EGS4Geom.MEDNUM[43]=44;//AIR
		EGS4Geom.MEDNUM[44]=45;//AIR

		EGS4Geom.nNREGLO=45;//42;
		EGS4Geom.nNREGHI=45;//42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		//-----------------------------------------
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		//-------------------------------------------------
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
			//----------------------------
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
			//------------------------------
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR
		//-------
		EGS4Geom.NREGLO[42]=8;//START REGION
			EGS4Geom.NREGHI[42]=8;//STOP REGION=>AIR
		EGS4Geom.NREGLO[43]=16;//START REGION
			EGS4Geom.NREGHI[43]=16;//STOP REGION=>AIR
		EGS4Geom.NREGLO[44]=24;//START REGION
			EGS4Geom.NREGHI[44]=24;//STOP REGION=>AIR


		EGS4Geom.NSUMCV=2;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=6;//REGION NUMBERS OF THE CAVITY
		EGS4Geom.ISUMCV[1]=7;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=2;
		GammaDetEffA.REGSVOL[0]=6;//where to score pulse height distribution
		GammaDetEffA.REGSVOL[1]=7;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 5 Marinelli geometry.
	 */
	private void constructMarinelli5()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=EGS4Geom.ZPLANE[2]+winthick;//+window thickness
		EGS4Geom.ZPLANE[4]=hsource;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[3]+inac_thick_sup;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+hdet_tot-winthick-inac_thick_sup-althick-hdet;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=EGS4Geom.ZPLANE[7]+althick;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=45;//42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		EGS4.MEDIA[1]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=naimaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[4]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=airmaterialTf.getText();
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[27]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[28]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[29]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[30]=pointstodmaterialTf.getText();
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=pointstodmaterialTf.getText();
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=almaterialTf.getText();
		EGS4.MEDIA[39]=almaterialTf.getText();
		EGS4.MEDIA[40]=almaterialTf.getText();
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();
		//==================
		EGS4.MEDIA[42]=airmaterialTf.getText();
		EGS4.MEDIA[43]=airmaterialTf.getText();
		EGS4.MEDIA[44]=almaterialTf.getText();

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=45;//42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR
		EGS4Geom.MEDNUM[42]=43;//AIR
		EGS4Geom.MEDNUM[43]=44;//AIR
		EGS4Geom.MEDNUM[44]=45;//AIR

		EGS4Geom.nNREGLO=45;//42;
		EGS4Geom.nNREGHI=45;//42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		//-----------------------------------------
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		//-------------------------------------------------
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
			//----------------------------
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
			//------------------------------
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR
		//-------
		EGS4Geom.NREGLO[42]=8;//START REGION
			EGS4Geom.NREGHI[42]=8;//STOP REGION=>AIR
		EGS4Geom.NREGLO[43]=16;//START REGION
			EGS4Geom.NREGHI[43]=16;//STOP REGION=>AIR
		EGS4Geom.NREGLO[44]=24;//START REGION
			EGS4Geom.NREGHI[44]=24;//STOP REGION=>AIR


		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=7;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=7;//where to score pulse height distribution

	}

	/**
	 * Based on source-detector geometry, construct type 6 Marinelli geometry.
	 */
	private void constructMarinelli6()
	{
		EGS4Geom.nNSLAB=8;
		//DEPTH BOUNDARIES
		EGS4Geom.ZPLANE[1]=hsourceup-envthick;
		EGS4Geom.ZPLANE[2]=EGS4Geom.ZPLANE[1]+envthick;//+envelope thickness=hsource
	    EGS4Geom.ZPLANE[3]=hsource;//+window thickness
		EGS4Geom.ZPLANE[4]=EGS4Geom.ZPLANE[2]+winthick;
		EGS4Geom.ZPLANE[5]=EGS4Geom.ZPLANE[4]+inac_thick_sup;
		EGS4Geom.ZPLANE[6]=EGS4Geom.ZPLANE[5]+hdet;
		EGS4Geom.ZPLANE[7]=EGS4Geom.ZPLANE[6]+hdet_tot-winthick-inac_thick_sup-althick-hdet;//=hdet_tot+hsource!!
		EGS4Geom.ZPLANE[8]=EGS4Geom.ZPLANE[7]+althick;

		EGS4Geom.nCyl=6;//"number of radial cylinders input"
		EGS4Geom.RCYL[1]=adet;
		EGS4Geom.RCYL[2]=adet_tot-althick;
		EGS4Geom.RCYL[3]=adet_tot;
		EGS4Geom.RCYL[4]=bsource;
		EGS4Geom.RCYL[5]=bsource+envthick;
		EGS4Geom.RCYL[6]=asource;

		if(!noSup)
		{GammaDetEffA.scattCaseB=false;}
		else
		{GammaDetEffA.scattCaseB=true;}

		EGS4Geom.nMEDIA=45;//42;
		EGS4.MEDIA[0]=almaterialTf.getText();//"aluminum";
		if(!noSup)
			EGS4.MEDIA[1]=windowmaterialTf.getText();//"air_dry_nearsealevel";
		else
			EGS4.MEDIA[1]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[2]=naimaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[3]=airmaterialTf.getText();//"air_dry_nearsealevel";
		if(!noSup)
			EGS4.MEDIA[4]=windowmaterialTf.getText();//"air_dry_nearsealevel";
		else
			EGS4.MEDIA[4]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[5]=airmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[6]=airmaterialTf.getText();
		//-----------------------------
		EGS4.MEDIA[7]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[8]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[9]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[10]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		EGS4.MEDIA[11]=pointstodmaterialTf.getText();//"air_dry_nearsealevel";
		//-------------------------------------------
		EGS4.MEDIA[12]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[13]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[14]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[15]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[16]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[17]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[18]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[19]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[20]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[21]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[22]=envelopematerialTf.getText();//"plastic";
		//------------------------------------------------------
		if(!noSup)
		{
		EGS4.MEDIA[23]=windowmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=windowmaterialTf.getText();//"aluminum";
		}
		else
		{
		EGS4.MEDIA[23]=pointstodmaterialTf.getText();//"aluminum";
		EGS4.MEDIA[24]=pointstodmaterialTf.getText();//"aluminum";
		}
		//plastic:
		EGS4.MEDIA[25]=envelopematerialTf.getText();//"plastic";
		EGS4.MEDIA[26]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[27]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[28]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[29]=pointstodmaterialTf.getText();//"plastic";
		EGS4.MEDIA[30]=pointstodmaterialTf.getText();
		//source:
		EGS4.MEDIA[31]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[32]=sourcematerialTf.getText();//"water_liquid";
		EGS4.MEDIA[33]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[34]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[35]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[36]=pointstodmaterialTf.getText();//"water_liquid";
		EGS4.MEDIA[37]=pointstodmaterialTf.getText();
		//--------------------------------------------------------------
		EGS4.MEDIA[38]=almaterialTf.getText();
		EGS4.MEDIA[39]=almaterialTf.getText();
		EGS4.MEDIA[40]=almaterialTf.getText();
		EGS4.MEDIA[41]=pointstodmaterialTf.getText();
		//==================
		EGS4.MEDIA[42]=airmaterialTf.getText();
		EGS4.MEDIA[43]=airmaterialTf.getText();
		EGS4.MEDIA[44]=almaterialTf.getText();

		EGS4Geom.DESCRIBE=EGS4Geom.DESCRIBE_REGIONS;//##INPUT DATA

		EGS4Geom.nMEDNUM=45;//42;
		EGS4Geom.MEDNUM[0]=1;//Al
		EGS4Geom.MEDNUM[1]=2;//Air
		EGS4Geom.MEDNUM[2]=3;//Air
		EGS4Geom.MEDNUM[3]=4;//Air
		EGS4Geom.MEDNUM[4]=5;//Air
		EGS4Geom.MEDNUM[5]=6;//Air
		EGS4Geom.MEDNUM[6]=7;//NAI
		//-----------------------------
		EGS4Geom.MEDNUM[7]=8;//Air
		EGS4Geom.MEDNUM[8]=9;//Air
		EGS4Geom.MEDNUM[9]=10;//AIR
		EGS4Geom.MEDNUM[10]=11;//AIR
		EGS4Geom.MEDNUM[11]=12;//AIR
		//-----------------------------
		EGS4Geom.MEDNUM[12]=13;//H2O
		EGS4Geom.MEDNUM[13]=14;//H2O
		EGS4Geom.MEDNUM[14]=15;//H2O
		EGS4Geom.MEDNUM[15]=16;//H2O
		EGS4Geom.MEDNUM[16]=17;//H2O
		EGS4Geom.MEDNUM[17]=18;//H2O
		EGS4Geom.MEDNUM[18]=19;//plastic
		EGS4Geom.MEDNUM[19]=20;//plastic
		EGS4Geom.MEDNUM[20]=21;//plastic
		EGS4Geom.MEDNUM[21]=22;//plastic
		EGS4Geom.MEDNUM[22]=23;//plastic
		EGS4Geom.MEDNUM[23]=24;//window
		EGS4Geom.MEDNUM[24]=25;//window
		EGS4Geom.MEDNUM[25]=26;//plastic
		EGS4Geom.MEDNUM[26]=27;//plastic
		EGS4Geom.MEDNUM[27]=28;//plastic
		EGS4Geom.MEDNUM[28]=29;//plastic
		EGS4Geom.MEDNUM[29]=30;//plastic
		EGS4Geom.MEDNUM[30]=31;//plastic
		EGS4Geom.MEDNUM[31]=32;//H2O
		EGS4Geom.MEDNUM[32]=33;//H2O
		EGS4Geom.MEDNUM[33]=34;//H2O
		EGS4Geom.MEDNUM[34]=35;//H2O
		EGS4Geom.MEDNUM[35]=36;//H2O
		EGS4Geom.MEDNUM[36]=37;//H2O
		EGS4Geom.MEDNUM[37]=38;//H2O
		EGS4Geom.MEDNUM[38]=39;//AIR
		EGS4Geom.MEDNUM[39]=40;//AIR
		EGS4Geom.MEDNUM[40]=41;//AIR
		EGS4Geom.MEDNUM[41]=42;//AIR
		EGS4Geom.MEDNUM[42]=43;//AIR
		EGS4Geom.MEDNUM[43]=44;//AIR
		EGS4Geom.MEDNUM[44]=45;//AIR

		EGS4Geom.nNREGLO=45;//42;
		EGS4Geom.nNREGHI=45;//42;

		EGS4Geom.NREGLO[0]=2;//START REGION
			EGS4Geom.NREGHI[0]=49;//STOP REGION=>PUT Al every where
		EGS4Geom.NREGLO[1]=5;//START REGION
			EGS4Geom.NREGHI[1]=5;//STOP REGION=>AIR
		EGS4Geom.NREGLO[2]=7;//START REGION
			EGS4Geom.NREGHI[2]=7;//STOP REGION=>AIR
		EGS4Geom.NREGLO[3]=15;//START REGION
			EGS4Geom.NREGHI[3]=15;//STOP REGION=>AIR
		EGS4Geom.NREGLO[4]=13;//START REGION
			EGS4Geom.NREGHI[4]=13;//STOP REGION=>AIR
		EGS4Geom.NREGLO[5]=14;//START REGION
			EGS4Geom.NREGHI[5]=14;//STOP REGION=>AIR
		EGS4Geom.NREGLO[6]=6;//START REGION
			EGS4Geom.NREGHI[6]=6;//STOP REGION=>NAI
		//----------------------------------------------
		EGS4Geom.NREGLO[7]=28;//START REGION
			EGS4Geom.NREGHI[7]=28;//STOP REGION=>AIR
		EGS4Geom.NREGLO[8]=29;//START REGION
			EGS4Geom.NREGHI[8]=29;//STOP REGION=>AIR
		EGS4Geom.NREGLO[9]=30;//START REGION
			EGS4Geom.NREGHI[9]=30;//STOP REGION=>AIR
		EGS4Geom.NREGLO[10]=31;//START REGION
			EGS4Geom.NREGHI[10]=31;//STOP REGION=>AIR
		EGS4Geom.NREGLO[11]=32;//START REGION
			EGS4Geom.NREGHI[11]=32;//STOP REGION=>AIR
		//------------------------------------------------
		EGS4Geom.NREGLO[12]=2;//START REGION
			EGS4Geom.NREGHI[12]=2;//STOP REGION=>sursa
		EGS4Geom.NREGLO[13]=10;//START REGION
			EGS4Geom.NREGHI[13]=10;//STOP REGION=>sursa
		EGS4Geom.NREGLO[14]=18;//START REGION
			EGS4Geom.NREGHI[14]=18;//STOP REGION=>sursa
		EGS4Geom.NREGLO[15]=26;//START REGION
			EGS4Geom.NREGHI[15]=26;//STOP REGION=>sursa
		EGS4Geom.NREGLO[16]=34;//START REGION
			EGS4Geom.NREGHI[16]=34;//STOP REGION=>sursa
		EGS4Geom.NREGLO[17]=42;//START REGION
			EGS4Geom.NREGHI[17]=42;//STOP REGION=>sursa
			//------------------------------
		EGS4Geom.NREGLO[18]=3;//START REGION
			EGS4Geom.NREGHI[18]=3;//STOP REGION=>plastic
		EGS4Geom.NREGLO[19]=11;//START REGION
			EGS4Geom.NREGHI[19]=11;//STOP REGION=>plastic
		EGS4Geom.NREGLO[20]=19;//START REGION
			EGS4Geom.NREGHI[20]=19;//STOP REGION=>plastic
		EGS4Geom.NREGLO[21]=27;//START REGION
			EGS4Geom.NREGHI[21]=27;//STOP REGION=>plastic
		EGS4Geom.NREGLO[22]=35;//START REGION
			EGS4Geom.NREGHI[22]=35;//STOP REGION=>plastic
		//-----------------------------------------
		EGS4Geom.NREGLO[23]=4;//START REGION
			EGS4Geom.NREGHI[23]=4;//STOP REGION=>window
		EGS4Geom.NREGLO[24]=12;//START REGION
			EGS4Geom.NREGHI[24]=12;//STOP REGION=>window
		//-------------------------------------------------
		EGS4Geom.NREGLO[25]=36;//START REGION
			EGS4Geom.NREGHI[25]=36;//STOP REGION=>plastic
		EGS4Geom.NREGLO[26]=37;//START REGION
			EGS4Geom.NREGHI[26]=37;//STOP REGION=>plastic
		EGS4Geom.NREGLO[27]=38;//START REGION
			EGS4Geom.NREGHI[27]=38;//STOP REGION=>plastic
		EGS4Geom.NREGLO[28]=39;//START REGION
			EGS4Geom.NREGHI[28]=39;//STOP REGION=>plastic
		EGS4Geom.NREGLO[29]=40;//START REGION
			EGS4Geom.NREGHI[29]=40;//STOP REGION=>plastic
		EGS4Geom.NREGLO[30]=41;//START REGION
			EGS4Geom.NREGHI[30]=41;//STOP REGION=>plastic
			//----------------------------
		EGS4Geom.NREGLO[31]=43;//START REGION
			EGS4Geom.NREGHI[31]=43;//STOP REGION=>water
		EGS4Geom.NREGLO[32]=44;//START REGION
			EGS4Geom.NREGHI[32]=44;//STOP REGION=>water
		EGS4Geom.NREGLO[33]=45;//START REGION
			EGS4Geom.NREGHI[33]=45;//STOP REGION=>water
		EGS4Geom.NREGLO[34]=46;//START REGION
			EGS4Geom.NREGHI[34]=46;//STOP REGION=>water
		EGS4Geom.NREGLO[35]=47;//START REGION
			EGS4Geom.NREGHI[35]=47;//STOP REGION=>water
		EGS4Geom.NREGLO[36]=48;//START REGION
			EGS4Geom.NREGHI[36]=48;//STOP REGION=>water
		EGS4Geom.NREGLO[37]=49;//START REGION
			EGS4Geom.NREGHI[37]=49;//STOP REGION=>water
			//------------------------------
		EGS4Geom.NREGLO[38]=9;//START REGION
			EGS4Geom.NREGHI[38]=9;//STOP REGION=>AIR
		EGS4Geom.NREGLO[39]=17;//START REGION
			EGS4Geom.NREGHI[39]=17;//STOP REGION=>AIR
		EGS4Geom.NREGLO[40]=25;//START REGION
			EGS4Geom.NREGHI[40]=25;//STOP REGION=>AIR
		EGS4Geom.NREGLO[41]=33;//START REGION
			EGS4Geom.NREGHI[41]=33;//STOP REGION=>AIR
		//-------
		EGS4Geom.NREGLO[42]=8;//START REGION
			EGS4Geom.NREGHI[42]=8;//STOP REGION=>AIR
		EGS4Geom.NREGLO[43]=16;//START REGION
			EGS4Geom.NREGHI[43]=16;//STOP REGION=>AIR
		EGS4Geom.NREGLO[44]=24;//START REGION
			EGS4Geom.NREGHI[44]=24;//STOP REGION=>AIR


		EGS4Geom.NSUMCV=1;//NUMBER OF CAVITY REGIONS
		EGS4Geom.ISUMCV[0]=7;//REGION NUMBERS OF THE CAVITY

		GammaDetEffA.nREGSOLV=1;
		GammaDetEffA.REGSVOL[0]=7;//where to score pulse height distribution

	}
	
	/**
	 * Converts ASCII int value to a String.
	 * 
	 * @param i
	 *            the ASCII integer
	 * @return the string representation
	 */
	@SuppressWarnings("unused")
	private static String asciiToStr(int i) {
		char a[] = new char[1];
		a[0] = (char) i;
		return (new String(a)); // char to string
	}
	
	//FOR LINUX==========================ENVVAR
	private String G4LEDATA="";
	private String G4LEVELGAMMADATA="";
	private String G4NEUTRONHPDATA="";
	private String G4NEUTRONXSDATA="";
	private String G4PIIDATA="";
	private String G4RADIOACTIVEDATA="";
	private String G4REALSURFACEDATA="";
	private String G4SAIDXSDATA="";
	protected String detFolderURL2="";
	//===========================================
	/**
	 * Initiate variables from MonteCarloPath.txt file located in current folder for 
	 * running Geant4 based simulation.
	 */
	private void geant4Run(){//all checks are already done!!!!
		//String s = "GEANT4 engine selected"	+ " \n";
		//simTa.append(s);
		String fileSeparator = System.getProperty("file.separator");
		String curentDir = System.getProperty("user.dir");
		String filename1 = curentDir + fileSeparator + filenameMC;//MonteCarloPath.txt
		
		G4LEDATA="";
		G4LEVELGAMMADATA="";
		G4NEUTRONHPDATA="";
		G4NEUTRONXSDATA="";
		G4PIIDATA="";
		G4RADIOACTIVEDATA="";
		G4REALSURFACEDATA="";
		G4SAIDXSDATA="";
		
		File f = new File(filename1);
		int i = 0;
		String pathMC = "";

		int countLine=0;//@@@@@@@@@@@@@@@@@@@@@@@@@
		StringBuffer desc = new StringBuffer();
		boolean haveData = false;
		if (f.exists()) {
			try {
				FileReader fr = new FileReader(f);
				while ((i = fr.read()) != -1) {
					if (!Character.isWhitespace((char) i)) {
						desc.append((char) i);
						haveData = true;
					} else {
						if (haveData)// we have data
						{
							haveData = false;// reset
							if (countLine==0){//@@@@@@@@@@@@@@
								pathMC = pathMC + desc.toString();
							} else if (countLine==1){
								G4LEDATA=G4LEDATA+desc.toString();
							}else if (countLine==2){
								G4LEVELGAMMADATA=G4LEVELGAMMADATA+desc.toString();
							}else if (countLine==3){
								G4NEUTRONHPDATA=G4NEUTRONHPDATA+desc.toString();
							}else if (countLine==4){
								G4NEUTRONXSDATA=G4NEUTRONXSDATA+desc.toString();
							}else if (countLine==5){
								G4PIIDATA=G4PIIDATA+desc.toString();
							}else if (countLine==6){
								G4RADIOACTIVEDATA=G4RADIOACTIVEDATA+desc.toString();
							}else if (countLine==7){
								G4REALSURFACEDATA=G4REALSURFACEDATA+desc.toString();
							}else if (countLine==8){
								G4SAIDXSDATA=G4SAIDXSDATA+desc.toString();
							}
							
							countLine++;
						}
						desc = new StringBuffer();
					}					
				}
				fr.close();
				pathMC.trim();G4LEDATA.trim();
				G4LEVELGAMMADATA.trim();G4NEUTRONHPDATA.trim();G4NEUTRONXSDATA.trim();
				G4PIIDATA.trim();G4RADIOACTIVEDATA.trim();G4REALSURFACEDATA.trim();G4SAIDXSDATA.trim();
				
				//System.out.println("Path= "+pathMC);
				//System.out.println("G4LEDATA= "+G4LEDATA);
				//System.out.println("G4LEVELGAMMADATA= "+G4LEVELGAMMADATA);
				//System.out.println("G4NEUTRONHPDATA= "+G4NEUTRONHPDATA);
				//System.out.println("G4NEUTRONXSDATA= "+G4NEUTRONXSDATA);
				//System.out.println("G4RADIOACTIVEDATA= "+G4RADIOACTIVEDATA);
				//System.out.println("G4REALSURFACEDATA= "+G4REALSURFACEDATA);
				//System.out.println("G4SAIDXSDATA= "+G4SAIDXSDATA);
				
				detFolderURL=pathMC+ fileSeparator;
				
				detFolderURL2=pathMC;
				if (SystemInfo.isLinux())
					detExeName= detExeNameLinux;//"detector";
				
				String filename = pathMC+ fileSeparator + detExeName;
				//System.out.println("filename= "+filename);
				File ff = new File(filename);
				if (ff.exists()){
					
					if(!prepareMacroFile())
						return;
					//===========run					
					startGEANT4();
				}else{
					simTa.append("Geant 4 - MC engine is not available yet"+"\n");
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
		}
	}
	
	/**
	 * Try preparing input macro file for running Geant4 based simulation.
	 * @return true or false
	 */
	private boolean prepareMacroFile(){
		simTa.append("Geant 4 - MC engine selected. Supported media: "+"\n");
		simTa.append("Environment media: AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		simTa.append("Detector media: sodiumiodide, germanium, ArCo2_80.20"+"\n");
		simTa.append("Window media: aluminum, beryllium, AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		simTa.append("Monture -wall- media: aluminum, AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		simTa.append("Gap -inactive- media: AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		simTa.append("Source media: water_liquid, soil_typicalloam_seltzer, tissue_soft_icrp, concrete_ordinary, AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		simTa.append("Envelope media: polyvinylchloride, AIR_gas_normal, air_dry_nearsealevel, vacuum"+"\n");
		
		String str = "";
		str=str+"/control/verbose 2"+"\n";
		str=str+"/run/verbose 2"+"\n";
		str=str+"/event/verbose 0"+"\n";
		str=str+"/tracking/verbose 0"+"\n";
		str=str+"/run/initialize"+"\n";
		//Visualize with openGL
		if (graphicSceneAutoRefreshCh.isSelected()){
		str = str+"/control/execute vis.mac"+"\n";
		}
		
		//environment
		String worldMaterial = pointstodmaterialTf.getText();
		if (worldMaterial.equals("AIR_gas_normal") || worldMaterial.equals("air_dry_nearsealevel"))
			worldMaterial = "G4_AIR";
		else if(worldMaterial.equals("vacuum"))
			worldMaterial = "G4_Galactic";
		str=str+"/gdet/det/setWorldMaterial"+" "+worldMaterial+"\n";
		
		//detector
		
		String totalDiameter = adetTotTf.getText();//request.getParameter("totalDiameter");
		str=str+"/gdet/det/total_diameter"+" "+totalDiameter+" cm"+"\n";
		
		String totalHeight =  hdetTotTf.getText();//request.getParameter("totalHeight");
		str=str+"/gdet/det/total_height"+" "+totalHeight+" cm"+"\n";
		
		String detectorMaterial = naimaterialTf.getText();//request.getParameter("detectorMaterial");
		if (detectorMaterial.equals("sodiumiodide"))
			detectorMaterial="G4_SODIUM_IODIDE";
		else if (detectorMaterial.equals("germanium"))
			detectorMaterial="G4_Ge";
		else if (detectorMaterial.equals("ArCo2_80.20"))
			detectorMaterial="ArCO2_80_20";
		str=str+"/gdet/det/setTargetMaterial"+" "+detectorMaterial+"\n";
		
		String activeDiameter = adetTf.getText();//request.getParameter("activeDiameter");
		str=str+"/gdet/det/active_diameter"+" "+activeDiameter+" cm"+"\n";
		
		String activeHeight = hdetTf.getText();//request.getParameter("activeHeight");
		str=str+"/gdet/det/active_height"+" "+activeHeight+" cm"+"\n";
		
		String windowMaterial = windowmaterialTf.getText();//request.getParameter("windowMaterial");
		if (windowMaterial.equals("AIR_gas_normal") || windowMaterial.equals("air_dry_nearsealevel"))
			windowMaterial = "G4_AIR";
		else if(windowMaterial.equals("vacuum"))
			windowMaterial = "G4_Galactic";
		else if(windowMaterial.equals("aluminum"))
			windowMaterial = "G4_Al";
		else if(windowMaterial.equals("beryllium"))
			windowMaterial = "G4_Be";
		str=str+"/gdet/det/setWindowMaterial"+" "+windowMaterial+"\n";
		
		String windowThickness = windowThickTf.getText();//request.getParameter("windowThickness");
		str=str+"/gdet/det/window_thickness"+" "+windowThickness+" cm"+"\n";
		
		String gapMaterial = airmaterialTf.getText();//request.getParameter("gapMaterial");		
		if (gapMaterial.equals("AIR_gas_normal") || gapMaterial.equals("air_dry_nearsealevel"))
			gapMaterial = "G4_AIR";
		else if(gapMaterial.equals("vacuum"))
			gapMaterial = "G4_Galactic";
		str=str+"/gdet/det/setGapMaterial"+" "+gapMaterial+"\n";
		
		String gapThickness = hUpTf.getText();//request.getParameter("gapThickness");
		str=str+"/gdet/det/endCapToDetector"+" "+gapThickness+" cm"+"\n";
		
		String montureMaterial = almaterialTf.getText();//request.getParameter("montureMaterial");
		if (montureMaterial.equals("AIR_gas_normal") || montureMaterial.equals("air_dry_nearsealevel"))
			montureMaterial = "G4_AIR";
		else if(montureMaterial.equals("vacuum"))
			montureMaterial = "G4_Galactic";
		else if(montureMaterial.equals("aluminum"))
			montureMaterial = "G4_Al";
		str=str+"/gdet/det/setMontureMaterial"+" "+montureMaterial+"\n";
		
		String montureThickness = althickTf.getText();//request.getParameter("montureThickness");
		str=str+"/gdet/det/monture_thickness"+" "+montureThickness+" cm"+"\n";
		
		String isCoaxial = "off";//request.getParameter("isCoaxial");//not supported here
		str=str+"/gdet/det/isCoaxial"+" "+isCoaxial+"\n";
		String holeMaterial = "G4_Cu";//request.getParameter("holeMaterial");
		str=str+"/gdet/det/setHoleMaterial"+" "+holeMaterial+"\n";
		String holeDiameter = "0.585";//request.getParameter("holeDiameter");
		str=str+"/gdet/det/hole_diameter"+" "+holeDiameter+" cm"+"\n";
		String holeHeight = "1.41";//request.getParameter("holeHeight");
		str=str+"/gdet/det/hole_height"+" "+holeHeight+" cm"+"\n";
		
		//source
		//for point source and parallel beam
		String distance = sddistTf.getText();//request.getParameter("pointSourceOrFrontalBeamToDetectorDistance");
		str=str+"/gdet/det/pointSourceOrFrontalBeamToDetectorDistance"+" "+distance+" cm"+"\n";
		
		String beamRadius = rbeamTf.getText();//request.getParameter("frontalBeamRadius");
		str=str+"/gdet/det/frontalBeamRadius"+" "+beamRadius+" cm"+"\n";
		
		String beamAngle = anglebeamTf.getText();//request.getParameter("frontalBeamAngle");
		str=str+"/gdet/det/frontalBeamAngle"+" "+beamAngle+" deg"+"\n";
		
		//for sarpagan and marinelli
		String sourceMaterial = sourcematerialTf.getText();//request.getParameter("sourceMaterial");
		if (sourceMaterial.equals("AIR_gas_normal") || sourceMaterial.equals("air_dry_nearsealevel"))
			sourceMaterial = "G4_AIR";
		else if(sourceMaterial.equals("vacuum"))
			sourceMaterial = "G4_Galactic";
		else if (sourceMaterial.equals("water_liquid"))
			sourceMaterial="G4_WATER";
		else if (sourceMaterial.equals("soil_typicalloam_seltzer"))
			sourceMaterial="soil_typicalloam_seltzer";
		else if (sourceMaterial.equals("tissue_soft_icrp"))
			sourceMaterial="G4_TISSUE_SOFT_ICRP";
		else if (sourceMaterial.equals("concrete_ordinary"))
			sourceMaterial="G4_CONCRETE";
		str=str+"/gdet/det/setSourceMaterial"+" "+sourceMaterial+"\n";
		
		String sourceDiameter = asourceTf.getText();//request.getParameter("sourceExternalDiameter");
		str=str+"/gdet/det/source_external_diameter"+" "+sourceDiameter+" cm"+"\n";
		
		String sourceTotalHeight = hsourceTf.getText();//request.getParameter("sourceTotalHeight");
		str=str+"/gdet/det/source_total_height"+" "+sourceTotalHeight+" cm"+"\n";
		
		String internalDiameter = bsourceTf.getText();//request.getParameter("sourceInternalDiameter");		
		if (internalDiameter.equals("0")){
			//not really an error but not null require!!
			internalDiameter = "1E-03";
		}
		str=str+"/gdet/det/source_internal_diameter"+" "+internalDiameter+" cm"+"\n";
		
		String topHeight = hsourceupTf.getText();//request.getParameter("sourceTopHeight");
		if (topHeight.equals("0")){
			//not really an error but not null require!!
			topHeight = "1E-03";
		}
		str=str+"/gdet/det/source_upper_height"+" "+topHeight+" cm"+"\n";
		
		String envelopeMaterial = envelopematerialTf.getText();//request.getParameter("envelopeMaterial");
		if (envelopeMaterial.equals("AIR_gas_normal") || envelopeMaterial.equals("air_dry_nearsealevel"))
			envelopeMaterial = "G4_AIR";
		else if(envelopeMaterial.equals("vacuum"))
			envelopeMaterial = "G4_Galactic";
		else if (envelopeMaterial.equals("polyvinylchloride"))
			envelopeMaterial="G4_POLYVINYL_CHLORIDE";
		str=str+"/gdet/det/setSourceEnvelopeMaterial"+" "+envelopeMaterial+"\n";
		
		String envelopeThickness = envelopeThickTf.getText();//request.getParameter("envelopeThickness");
		str=str+"/gdet/det/source_envelope_thickness"+" "+envelopeThickness+" cm"+"\n";
		
		int isource = sourceTypeCb.getSelectedIndex();//0=PS;1=Sarp;2=Mar;3=FB
		int geantSourceIndex=0;
		//geant: 3=mar;2=Sarp;1=PS;0=FB!!!!
		if (isource==0)//PS
			geantSourceIndex=1;
		else if (isource==1)//Sarp
			geantSourceIndex=2;
		else if (isource==2)//Mar
			geantSourceIndex=3;
		else if (isource==3)//FB
			geantSourceIndex=0;
		str=str+"/gdet/gun/source "+geantSourceIndex+"\n";//Marinelli type here!
		str=str+"/gdet/gun/rndm off"+"\n";//internal
		
		String peakWindow = deltaeTf.getText();//request.getParameter("peakWindow");
		double pwd = Convertor.stringToDouble(peakWindow);//MeV here
		pwd = pwd *1000.0;//kev
		str=str+"/gdet/gun/deltaEnergy"+" "+pwd+" keV"+"\n";
		
		String virtualThreshold = ethreshTf.getText();//request.getParameter("energyThresholdForCounting");
		double vtd = Convertor.stringToDouble(virtualThreshold);//MeV here
		vtd = vtd *1000.0;//kev
		str=str+"/gdet/event/energyThresholdForCounting"+" "+vtd+" keV"+"\n";
		
		str=str+"/gdet/det/update"+"\n";//internal
		str=str+"/gdet/event/printModulo 1000"+"\n";//internal default
		
		//clear and reset the view on the fly
		if (graphicSceneAutoRefreshCh.isSelected()){
		str=str+"/vis/viewer/clear"+"\n";
		str=str+"/vis/viewer/refresh"+"\n";
		str=str+"/vis/viewer/set/autoRefresh false"+"\n";
		}
		//==================
		
		
		String particleType = "";
		if (photonRb.isSelected()) {
			particleType = "gamma";
		} else if (electronRb.isSelected()) {
			particleType = "e-";
		} else if (positronRb.isSelected()) {
			particleType = "e+";
		}		
		str=str+"/gun/particle"+" "+particleType+"\n";
		
		String particleKineticEnergy = energyTf.getText();//request.getParameter("incidentKineticEnergy");
		str=str+"/gun/energy"+" "+particleKineticEnergy+" MeV"+"\n";
		
		String nRuns= (String)nPhotonsCb.getSelectedItem();
		str=str+"/run/beamOn"+" "+nRuns+"\n";
		
		//some additional check: may be redundant
		if (isource==0){//PS
			if (Convertor.stringToDouble(totalDiameter)-Convertor.stringToDouble(activeDiameter)<=2.0*Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!");
				simTa.append("Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(totalHeight)<=Convertor.stringToDouble(activeHeight)+
					Convertor.stringToDouble(windowThickness)+Convertor.stringToDouble(gapThickness)+
					Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!");
				simTa.append("Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!\n");
				return false;
			}
		} else if (isource==1){//Sarp
			if (Convertor.stringToDouble(totalDiameter)-Convertor.stringToDouble(activeDiameter)<=2.0*Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!");
				simTa.append("Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(totalHeight)<=Convertor.stringToDouble(activeHeight)+
					Convertor.stringToDouble(windowThickness)+Convertor.stringToDouble(gapThickness)+
					Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!");
				simTa.append("Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(sourceTotalHeight)<=Convertor.stringToDouble(envelopeThickness)){
				//out.println("<br>Error: Source top height should be '>' Source envelope thickness!");
				simTa.append("Error: Source top height should be '>' Source envelope thickness!\n");
				return false;
			}
		} else if (isource==2){//Mar
			if (Convertor.stringToDouble(totalDiameter)-Convertor.stringToDouble(activeDiameter)<=2.0*Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!");
				simTa.append("Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(totalHeight)<=Convertor.stringToDouble(activeHeight)+
					Convertor.stringToDouble(windowThickness)+Convertor.stringToDouble(gapThickness)+
					Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!");
				simTa.append("Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(topHeight)>=Convertor.stringToDouble(sourceTotalHeight)){
				//out.println("<br>Error: Marinelli baker top height should be '<' Marinelli baker total height!");
				simTa.append("Error: Marinelli baker top height should be '<' Marinelli baker total height!\n");
				return false;
			}
			if (Convertor.stringToDouble(topHeight)<=Convertor.stringToDouble(envelopeThickness)){
				//out.println("<br>Error: Marinelli baker top height should be '>' Marinelli baker envelope thickness!");
				simTa.append("Error: Marinelli baker top height should be '>' Marinelli baker envelope thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(internalDiameter)>=Convertor.stringToDouble(sourceDiameter)){
				//out.println("<br>Error: Marinelli baker internal diameter should be '<' Marinelli baker external diameter!");
				simTa.append("Error: Marinelli baker internal diameter should be '<' Marinelli baker external diameter!\n");
				return false;
			}
			if (Convertor.stringToDouble(internalDiameter)<=Convertor.stringToDouble(totalDiameter)){
				//out.println("<br>Error: Marinelli baker internal diameter should be '>' Detector total diameter!");
				simTa.append("Error: Marinelli baker internal diameter should be '>' Detector total diameter!\n");
				return false;
			}
			if (Convertor.stringToDouble(sourceDiameter)-Convertor.stringToDouble(internalDiameter)<=2.0*Convertor.stringToDouble(envelopeThickness)){
				//out.println("<br>Error: Marinelli baker diameter difference should be '>' 2 x Marinelli baker envelope thickness!");
				simTa.append("Error: Marinelli baker diameter difference should be '>' 2 x Marinelli baker envelope thickness!\n");
				return false;
			}
		} else if (isource==3){//FB
			if (Convertor.stringToDouble(totalDiameter)-Convertor.stringToDouble(activeDiameter)<=2.0*Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!");
				simTa.append("Error: Difference between detector total diameter and detector active diameter should be '>' 2 x detector wall thickness!\n");
				return false;
			}
			if (Convertor.stringToDouble(totalHeight)<=Convertor.stringToDouble(activeHeight)+
					Convertor.stringToDouble(windowThickness)+Convertor.stringToDouble(gapThickness)+
					Convertor.stringToDouble(montureThickness)){
				//out.println("<br>Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!");
				simTa.append("Error: Detector total height should be '>' the sum of window thickness, active detector height, gap thickness and wall thickness!\n");
				return false;
			}
		}
			
		//2.Now save the file into gdet_exe_web folder!!=====================
		
		long time = System.currentTimeMillis();
		String timeS="run"+time+".mac";
		String file_sep = System.getProperty("file.separator");
		String filename =detFolderURL + file_sep + timeS; ;//(String)session.getAttribute("gdetFolderURL") +file_sep+timeS;
		//"D:"+file_sep+"gdet_exe_web"+file_sep+timeS;
		//File macroFile=new File(filename);		
		macroFile = new File(filename);
		macroFilename=timeS;
		
		boolean succesWriteFile=true;
		try {
			FileWriter sigfos = new FileWriter(macroFile);//new FileWriter(filename);
			sigfos.write(str);
			sigfos.close();			
				
		} catch (Exception e) {
			e.printStackTrace();
			succesWriteFile=false;
		}
		if(!succesWriteFile){
			//out.println("<br>Unexpected error has occurred when trying to process input data!");
			String title ="Error";//resources.getString("dialog.number.title");
		    String message ="Unexpected error has occurred when trying to process input data!";//resources.getString("dialog.input.message");
		    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Start Geant4 base simulation by calling the proper executable.
	 */
	private void startGEANT4(){
		
		String file_sep = System.getProperty("file.separator");
		
		String workDir =detFolderURL;
		File directory = new File(workDir);
		
		String command =workDir+file_sep+detExeFileName;
		String argument = macroFilename;//timeS;
		//===========================LINUX========================
		if (SystemInfo.isLinux()){
			//Creating a running Script because we want to set environmental variable
			//there is no global variable in Linux so we are forced to use scripts!!!
			String currentDir = System.getProperty("user.dir");
			//String file_sep = System.getProperty("file.separator");
			String filename = currentDir + file_sep + "runScript";
			File scriptFile = new File(filename);
			String str = "#!/bin/bash"+"\n";

			str= str+G4LEDATA+"\n";
			str= str+G4LEVELGAMMADATA+"\n";
			str= str+G4NEUTRONHPDATA+"\n";
			str= str+G4NEUTRONXSDATA+"\n";
			str= str+G4PIIDATA+"\n";
			str= str+G4RADIOACTIVEDATA+"\n";
			str= str+G4REALSURFACEDATA+"\n";
			str= str+G4SAIDXSDATA+"\n";
			str = str+"cd "+detFolderURL2+"; ./"+detExeFileName+" ./"+macroFilename;
			
			//System.out.println(str);
			//return;
			//boolean succesWriteFile=true;
			try {
				FileWriter sigfos = new FileWriter(scriptFile);
				sigfos.write(str);
				sigfos.close();			
					
			} catch (Exception e) {
				e.printStackTrace();
				//succesWriteFile=false;
			}
			//============END SCRIPT CREATION
			//==========now setting up permission===============	
			String cmd_arg = "chmod 755 runScript";
			ProcessBuilder pbuilder = new ProcessBuilder("bash", "-c", cmd_arg);
			try {
				Process p = pbuilder.start();
				p.destroy();
			}catch (Exception e){
				e.printStackTrace();
			}
			//===============END PERMISSION====================			
			command = "./runScript";			
		}
		//==========================================================
		ProcessBuilder builder = new ProcessBuilder(command, argument);
		builder.directory(directory);
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@		
		if (SystemInfo.isLinux()){
			String currentDir = System.getProperty("user.dir");
			builder = new ProcessBuilder(command);
			builder.directory(new File(currentDir));
		}
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		try {
			final Process process = builder.start();
			
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			
			simTa.append("\n"+"Simulation starts!"+"\n");

			while ((line = br.readLine()) != null) {
				String newLine=line.replaceAll("<", "[");
				newLine=newLine.replaceAll(">", "]");
				simTa.append(newLine+"\n");		
				
				//EXTRACTIONS==========================
				String str = " peak efficiency in Detector (%): ";//"Effective dose in body";
				//"peak efficiency in Detector (%): "
				//"global efficiency in Detector (%): "
				//3.45555 +- 2.33209 %
				int len = str.length();
				int newLineLen=newLine.length();
				if (newLineLen>len)
				{
		//System.out.println(newLine);
					String cstr=newLine.substring(0, len);
		//System.out.println(str);
		//System.out.println(cstr);
					if (str.equals(cstr)){
		//System.out.println(newLine);
						String[] effunit = extractValueAndUnit(newLine);
						geff = Convertor.stringToDouble(effunit[0]);
						geff_unc = Convertor.stringToDouble(effunit[1]);
	//System.out.println("Eff="+effunit[0]+";error:"+effunit[1]);
					}
				}
				
				str = " global efficiency in Detector (%): ";// "----- Lifetime fatal cancer risk [cases/1 million";
				len = str.length();
				newLineLen=newLine.length();
				if (newLineLen>len)
				{
					String cstr=newLine.substring(0, len);
					if (str.equals(cstr)){
						//String newlineSkiped1=newLine.substring(len);
						//System.out.println(newlineSkiped1);
						String[] riskS = extractValueAndUnit(newLine);//newlineSkiped1);
						gefft = Convertor.stringToDouble(riskS[0]);
						gefft_unc = Convertor.stringToDouble(riskS[1]);
	//System.out.println("EffT="+gefft+";error:"+gefft_unc);
						//THIS IS NEEDED SINCE SCRIPT LIKE SHELL WAITS FOR EXIT!!!!
						if (SystemInfo.isLinux())
							   break;
					}
				}
				//================================
				
			}
			isr.close();
			is.close();
			process.destroy();

			//out.println("<br>Simulation ends successfully!");
			simTa.append("Simulation ends successfully!"+"\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//now delete macrofile
		try {
			macroFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//the end:
		//stopComputation();
		
	}
	
	/**
	 * Internally used by startGeant4 for displaying results.
	 * @param src src
	 * @return the result
	 */
	private String[] extractValueAndUnit(String src) {
		String[] result = new String[2];
		
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();
		
		boolean stop=false;
		boolean enter=false;
		int index =0; 
		
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < src.length(); i++) {
	        char c = src.charAt(i);	        
	        if (Character.isDigit(c)) {	        	
	        	stop=true;
	        	enter=true;
	            builder.append(c);
	        } else {
	        	if (enter && c == localeDecimalSeparator){
	        		stop=true;//just continue
	        		builder.append(c);
	        	}
	        	else
	        		stop=false;
	        }
	        
	        if (!stop && enter){
	        	index=i;
	        	break;	        	
	        }	        
	    }
	    String valueS = builder.toString();
	    
	    //3.45555 +- 2.33209 %
	    //value was caught...that is 3.45555	    
	    String str= src.substring(index);// +- 2.33209 %
	    int len = str.length();	
	    String errorS = str.substring(4, len-2);//4 inclusive, 11 (len-2) exclusive
	    
		result[0]=valueS;
		result[1]=errorS;
		
		return result;
	}
}
