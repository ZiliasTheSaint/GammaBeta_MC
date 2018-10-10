package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import org.apache.pdfbox.pdmodel.PDDocument;

import danfulea.utils.ExampleFileFilter;
import danfulea.phys.egs.EGS4;
import danfulea.phys.egs.EGS4Core;
import danfulea.phys.egs.EGS4Macro;
import danfulea.phys.egs.EGS4SrcEns;
import danfulea.phys.egs.G;
import danfulea.utils.FrameUtilities;
import danfulea.utils.PDFRenderer;

//####################################################################
/**
 * Computes g,mu_tr,mu_en. This is the GUI interface for G class.
 * 
 * 
 * @author Dan Fulea, 27 MAR. 2007
 */
@SuppressWarnings("serial")
public class G_app extends JFrame implements Runnable {
	private GammaBetaFrame mf;

	protected String outFilename = "";
	protected ResourceBundle resources;
	private JLabel statusL = new JLabel("Waiting...");
	private volatile Thread statusTh = null;// status display thread!
	private int delay = 100;
	private int frameNumber = -1;
	private String statusRunS = "";

	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);

	// ##################################################################
	JTabbedPane tabs;
	protected JTextField eTf = new JTextField(5);
	protected JTextField eminTf = new JTextField(5);
	protected JTextField emaxTf = new JTextField(5);// !!!!!!!
	protected JTextField bwidthTf = new JTextField(5);// !!!!!!!
	protected JTextField spectrumTf = new JTextField(25);// !!!!!!!
	protected JTextField mediaTf = new JTextField(25);// !!!!!!!
	protected JTextField angleTf = new JTextField(5);
	protected JTextField rbeamTf = new JTextField(5);
	protected JTextField distanceTf = new JTextField(5);
	protected JButton addB = new JButton();
	protected JButton loadSpectrumB = new JButton();
	protected JButton loadMediaB = new JButton();
	protected JButton deleteB = new JButton();
	@SuppressWarnings("rawtypes")
	protected JComboBox ncaseCb, einCb, sourceCb;
	protected JTextArea gsimTa = new JTextArea();
	protected JButton startsimB = new JButton();
	protected JButton stopsimB = new JButton();
	protected JRadioButton photonRb, electronRb, positronRb;
	protected JButton printB = new JButton();
	@SuppressWarnings("rawtypes")
	protected DefaultListModel kvdlm = new DefaultListModel();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected JList kvmL = new JList(kvdlm);

	protected Color fundal = new Color(144, 37, 38, 255);// new
															// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(950, 700);
	// private static final Dimension aboutDim=new Dimension(800,500);
	// private final String FILESEPARATOR =
	// System.getProperty("file.separator");
	private volatile Thread gsimTh;
	private boolean stopAnim = true;
	private boolean stopAppend = false;

	protected int nlistpoints = 0;
	private Vector<String> einv = new Vector<String>();
	private double[] ein;

	public G_app(GammaBetaFrame mf) {
		super("g KERMA, mu_tr and mu_en");
		this.mf = mf;
		fundal = GammaBetaFrame.bkgColor;

		String BASE_RESOURCE_CLASS = "gammaBetaMc.resources.BoxAppResources";
		resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);

		createGUI();
		initEvent();// main event

		setDefaultLookAndFeelDecorated(true);
		FrameUtilities.createImageIcon(mf.resources.getString("form.icon.url"),
				this);

		FrameUtilities.centerFrameOnScreen(this);

		setVisible(true);
		mf.setEnabled(false);
		// mf.setVisible(false);
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
		// mf.setVisible(true);
		dispose();
	}

	/**
	 * Setting up the frame size.
	 */
	public Dimension getPreferredSize() {
		return PREFERRED_SIZE;
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
	 * GUI creation
	 */
	private void createGUI() {
		G.jta = gsimTa;
		JPanel content = new JPanel(new BorderLayout());

		tabs = createTabs();
		content.add(tabs, BorderLayout.CENTER);

		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 70, 2));
		d11P.add(startsimB);
		d11P.add(stopsimB);
		d11P.add(printB);
		d11P.setBackground(fundal);

		// THE KEY TO PROPER DISPLAY FORMATTED STRING WITHIN TEXTAREA:!!!
		Font font = new Font("Monospaced", Font.PLAIN, 12);
		gsimTa.setFont(font);
		content.add(d11P, BorderLayout.SOUTH);

		JPanel content0 = new JPanel(new BorderLayout());
		content0.add(content, BorderLayout.CENTER);

		// Create the statusbar.
		JToolBar statusBar = new JToolBar();
		statusBar.setFloatable(false);
		initStatusBar(statusBar);
		content0.add(statusBar, BorderLayout.PAGE_END);

		setContentPane(new JScrollPane(content0));// content);
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	/**
	 * Create tabs
	 * @return the result
	 */
	private JTabbedPane createTabs() {

		JTabbedPane tabs = new JTabbedPane();

		gGUI dsg = new gGUI(this);
		JPanel dsPan = dsg.createSimMainPanel();
		JPanel outPan = dsg.createOutputPanel();

		dsPan.setBorder(GammaBetaFrame.STANDARD_BORDER);
		outPan.setBorder(GammaBetaFrame.STANDARD_BORDER);

		String s = "General Monte Carlo";
		tabs.add(s, dsPan);
		s = "Results";
		tabs.add(s, outPan);

		return tabs;
	}

	// END GUI-----------------------------------------------------------------
	/**
	 * All actions are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == startsimB) {
					statusRunS = resources.getString("status.computing");
					startThread();
				}
				if (evt.getSource() == printB) {
					printResults();
				}
				if (evt.getSource() == stopsimB) {
					stopAppend = true;
					stopThread();
				}

				if (evt.getSource() == loadSpectrumB) {
					chooseSpectrum();
				}

				if (evt.getSource() == loadMediaB) {
					selectMaterial(0);
				}

				if (evt.getSource() == addB) {
					addDataInList();
				}

				if (evt.getSource() == deleteB) {
					deleteSelectedData();
				}

			}
		};

		ItemListener il = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getSource() == sourceCb) {
					setSource();
				}
				if (ie.getSource() == einCb) {
					setEin();
				}

			}
		};

		loadSpectrumB.addActionListener(al);
		addB.addActionListener(al);
		loadMediaB.addActionListener(al);
		deleteB.addActionListener(al);
		startsimB.addActionListener(al);
		stopsimB.addActionListener(al);
		printB.addActionListener(al);
		sourceCb.addItemListener(il);
		einCb.addItemListener(il);
	}

	// #################################
	/**
	 * Print the simulation results.
	 */
	private void printResults() {

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

			//new G_appReport(this);
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
			renderer.setTitle(resources.getString("pdf.content.title1"));
			renderer.setSubTitle(
					resources.getString("pdf.content.subtitle")+
					resources.getString("pdf.metadata.author1")+ ", "+
							new Date());
						
			String str = " \n" + gsimTa.getText();
		
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
	 * Convert vector to array.
	 */
	private void convertVectors() {
		ein = new double[nlistpoints];

		for (int i = 0; i < nlistpoints; i++) {
			String ss = (String) einv.elementAt(i);
			ein[i] = EGS4.stringToDouble(ss);
		}
	}

	/**
	 * Add energy data in list.
	 */
	private void addDataInList() {
		boolean b = true;
		String s1 = eTf.getText();
		String s = "";
		double d1 = 0.0;
		try {
			d1 = EGS4.stringToDouble(s1);
		} catch (Exception e) {
			b = false;
			s = "Insert positive energy value < 50MeV" + " \n";
			gsimTa.append(s);

			eTf.setText("");
			eTf.requestFocusInWindow();
		}

		if (!b)
			return;

		if (d1 <= 0) {
			s = "Insert positive energy value < 50MeV" + " \n";
			gsimTa.append(s);

			eTf.setText("");
			eTf.requestFocusInWindow();
			return;
		}

		// end test-->la succes se poate merge mai departe
		// avoid repetition
		b = true;
		for (int i = 0; i < einv.size(); i++) {
			String tst = (String) (einv.elementAt(i));
			if (tst.compareTo(s1) == 0) {
				b = false;
				break;
			}
		}

		if (!b) {
			s = "Inserted energy already exists!" + " \n";
			gsimTa.append(s);

			eTf.setText("");
			eTf.requestFocusInWindow();
			return;
		}
		// ---------------------------------
		// normalized test in calculations and graph!!!
		// ----------------------------------@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		add(s1 + " MeV", kvdlm);
		select(nlistpoints, kvmL);

		s1 = EGS4.doubleToString(d1);
		einv.addElement(s1);

		nlistpoints++;
		eTf.setText("");
		eTf.requestFocusInWindow();
	}

	/**
	 * Delete selected data
	 */
	private void deleteSelectedData() {
		if (nlistpoints != 0) {

			nlistpoints--;

			int index = getSelectedIndex(kvmL);

			remove(index, kvdlm);
			select(nlistpoints - 1, kvmL);

			einv.removeElementAt(index);

			eTf.setText("");
			eTf.requestFocusInWindow();
		}
	}

	/**
	 * Choose a spectrum file
	 */
	private void chooseSpectrum() {
		String ext = "spectrum";// resources.getString("src.load");
		String pct = ".";
		String description = "Spectrum file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");
		String egsDatas = "egs";
		String datas = "Data" + file_sep + egsDatas + file_sep + "spectra";
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
		loadSpectrumB.setEnabled(false);
		eTf.setEnabled(false);
		addB.setEnabled(false);
		deleteB.setEnabled(false);
		eminTf.setEnabled(false);
		emaxTf.setEnabled(false);
		bwidthTf.setEnabled(false);

		if (index == 0) {
			eTf.setEnabled(true);
			addB.setEnabled(true);
			deleteB.setEnabled(true);
		}

		if (index == 1) {
			loadSpectrumB.setEnabled(true);
		}

		if (index == 2) {
			eminTf.setEnabled(true);
			emaxTf.setEnabled(true);
			bwidthTf.setEnabled(true);
		}

	}

	/**
	 * Handle source related controls.
	 */
	private void setSource() {
		int index = sourceCb.getSelectedIndex();
		angleTf.setEnabled(false);
		rbeamTf.setEnabled(false);
		distanceTf.setEnabled(false);

		if (index == 0) {
			angleTf.setEnabled(true);
		}
		if (index == 1) {
			rbeamTf.setEnabled(true);
			distanceTf.setEnabled(true);
		}

	}

	/**
	 * Select material
	 * @param imat dummy argument. Not used, set this to whatever you like.
	 */
	private void selectMaterial(int imat) {
		// .pegs4dat
		String ext = "pegs4dat";// resources.getString("src.load");
		String pct = ".";
		String description = "Material constants file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");
		String egsDatas = "egs";
		String datas = "Data" + file_sep + egsDatas + file_sep + "interactiv";
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

			// if(imat==0)
			// {mediaTf.setText(filename);}
			// else if(imat==1)
			// {airmaterialTf.setText(filename);}
			mediaTf.setText(filename);
		}

	}

	/**
	 * Start all threads
	 */
	private void startThread() {
		stopAnim = false;
		if (gsimTh == null) {
			gsimTh = new Thread(this);
			gsimTh.start();
		}
		if (statusTh == null) {
			statusTh = new Thread(this);
			statusTh.start();
		}

	}

	/**
	 * Stop all threads
	 */
	private void stopThread() {
		statusTh = null;
		frameNumber = 0;
		stopAnim = true;

		if (gsimTh == null) {
			stopAppend = false;
			// press kill button but simulation never started

			return;
		}

		gsimTh = null;
		if (stopAppend) {
			EGS4.STOPPROGRAM = true;
			gsimTa.append("Interrupted by the user!!" + " \n");
			stopAppend = false;
			String label = resources.getString("status.done");
			statusL.setText(label);
		}
	}

	/**
	 * Thread specific method.
	 */
	public void run() {
		//Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		//gsimTh.setPriority(Thread.NORM_PRIORITY);
		//performGCalculation();
		
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

		if (currentThread == gsimTh) {// if thread is the main
		// computation Thread!!
			//if (command.equals(RUN_COMMAND)) {
				performGCalculation();
			//}
		}
	}

	/**
	 * Perform G calculations.
	 */
	private void performGCalculation()// ----@@@@@@@@DO NOT FORGET CHECK
										// READ!!!!!
	{
		gsimTa.selectAll();
		gsimTa.replaceSelection("");

		G.systemOutB = false;
		tabs.setSelectedIndex(1);
		// ======================OBLIGE=======================
		EGS4.setMXMED(1);// "we need just one medium                 "
		EGS4.setMXREG(1);// "and just one geometrical region         "
		EGS4.setMXSTACK(50);// "this should be enough for any purposes  "
		EGS4.$MXGE = 2000;// //REPLACE {$MXGE} WITH {2000}

		EGS4.reset();
		EGS4Core.reset();
		EGS4Macro.reset();
		EGS4SrcEns.reset();
		G.reset();

		EGS4.egs_set_defaults();// first default then:
		EGS4.RandomUse = 1;// use EGS ranlux or ranmar generators
		EGS4.ranluxB = true;// use ranlux!!

		EGS4.$MXMDSH = 500;//
		// ======================OBLIGE=======================
		G.ncase = EGS4.stringToInt((String) ncaseCb.getSelectedItem());// 1000;
		G.source_type = sourceCb.getSelectedIndex();
		G.mono = einCb.getSelectedIndex();

		boolean neg = false;
		double Emin = 0.0;
		double emax = 0.0;
		double bwidth = 1;
		String s = "";
		try {
			G.angle = EGS4.stringToDouble(angleTf.getText());// 6.3;
			if (G.angle < 0 || G.angle > 90) {
				// neg = true;
				s = "Angle must be in the range: 0 - 90 degrees!" + " \n";
				gsimTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}

			G.rbeam = EGS4.stringToDouble(rbeamTf.getText());// 7.3;
			if (G.rbeam < 0)
				neg = true;
			G.distance = EGS4.stringToDouble(distanceTf.getText());// 7.3;
			if (G.distance < 0)
				neg = true;
			Emin = EGS4.stringToDouble(eminTf.getText());// 7.3;
			if (Emin < 0)
				neg = true;
			emax = EGS4.stringToDouble(emaxTf.getText());// 7.3;
			if (emax < 0)
				neg = true;
			bwidth = EGS4.stringToDouble(bwidthTf.getText());// 7.3;
			if (bwidth < 0)
				neg = true;
		} catch (Exception ex) {
			s = " Inputs ERROR. Insert positive numbers!" + " \n";
			gsimTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (neg) {
			s = "Error in detector inputs. Insert positive numbers!" + " \n";
			gsimTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			return;
		}

		if (emax > 50.0) {
			s = "Insert positive energy value < 50MeV" + " \n";
			gsimTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			emaxTf.setText("");
			return;
		}

		if (photonRb.isSelected()) {
			G.iqin = 0;
		} else if (electronRb.isSelected()) {
			G.iqin = -1;
		} else if (positronRb.isSelected()) {
			G.iqin = 1;
		}

		G.calc_type = 0;// FIXED
		EGS4.NMED = 1;// 1 medium==maximum media allowed
		EGS4.MEDIA[0] = mediaTf.getText();// "AIR_gas_normal";//mediaTf.getText();
		int nreg = 1;// 1 region
		for (int I = 1; I <= nreg; I++) {
			EGS4.MED[I - 1] = 1;
		}

		G.eis = new double[G.$MXENE];

		if (G.mono == 0) {
			convertVectors();
			G.neis = ein.length;// 1 energy
			if (G.neis > G.$MXENE || G.neis <= 0) {
				s = "Max. number of energies must not exceed " + G.$MXENE
						+ " and must be >0!! \n";
				gsimTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}
			for (int i = 1; i <= G.neis; i++) {
				G.eis[i - 1] = ein[i - 1] + EGS4.RM * Math.abs(G.iqin);
			}

		} else if (G.mono == 2) {// "lin grid, reading: Emin, emax, bwidth"
			G.neis = (int) Math.floor(Math.abs(emax - Emin) / bwidth) + 1;
			if (G.neis > G.$MXENE || G.neis <= 0) {
				s = "Max. number of energies must not exceed " + G.$MXENE
						+ " and must be >0!! \n";
				gsimTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}

			for (int i = 0; i <= G.neis - 1; i++) {
				G.eis[i] = Emin + i * bwidth + EGS4.RM * Math.abs(G.iqin);
			}
		} else// spectrum
		{
			G.neis = 0;
			G.enerFilename = spectrumTf.getText();

			if (G.enerFilename.compareTo("") == 0) {
				s = "Invalid spectrum file!" + " \n";
				gsimTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;

			}

		}

		if (mf.incoh_OnRb.isSelected()) {
			G.incoh = G.incoh_ON;
		} else {
			G.incoh = G.incoh_OFF;
		}
		if (mf.coh_OnRb.isSelected()) {
			G.coh = G.coh_ON;
		} else {
			G.coh = G.coh_OFF;
		}
		if (mf.relax_OnRb.isSelected()) {
			G.relax = G.relax_ON;
		} else {
			G.relax = G.relax_OFF;
		}
		if (mf.pe_OnRb.isSelected()) {
			G.pe = G.pe_ang_ON;
		} else {
			G.pe = G.pe_ang_OFF;
		}

		int i = mf.baCb.getSelectedIndex();
		if (i == 0)
			EGS4.ibrdst = G.brems_ang_SIMPLE;
		else
			EGS4.ibrdst = G.brems_ang_KM;

		i = mf.paCb.getSelectedIndex();
		if (i == 0)
			EGS4.iprdst = G.pair_ang_OFF;
		else if (i == 1)
			EGS4.iprdst = G.pair_ang_SIMPLE;
		else if (i == 2)
			EGS4.iprdst = G.pair_ang_KM;
		else if (i == 3)
			EGS4.iprdst = G.pair_ang_UNIFORM;
		else if (i == 4)
			EGS4.iprdst = G.pair_ang_BLEND;

		i = mf.bcsCb.getSelectedIndex();
		if (i == 0)
			EGS4.ibr_nist = G.brems_cross_BH;
		else
			EGS4.ibr_nist = G.brems_cross_NIST;

		i = mf.pcsCb.getSelectedIndex();
		if (i == 0)
			EGS4.pair_nrc = G.pair_cross_BH;
		else
			EGS4.pair_nrc = G.pair_cross_NRC;

		if (mf.spinCh.isSelected()) {
			G.ispin = G.spin_ON;
		} else {
			G.ispin = G.spin_OFF;
		}

		// Electron impact ionization
		EGS4.eii_flag = G.eii_OFF;// default
		if (mf.eii_OnRb.isSelected())
			EGS4.eii_flag = G.eii_ON;
		else if (mf.eii_casnatiRb.isSelected())
			EGS4.eii_flag = G.eii_casnati;
		else if (mf.eii_kolbenstvedtRb.isSelected())
			EGS4.eii_flag = G.eii_kolbenstvedt;
		else if (mf.eii_gryzinskiRb.isSelected())
			EGS4.eii_flag = G.eii_gryzinski;

		// photon cross section
		G.photon_xsection = "";// default
		if (mf.photoxRb_epdl.isSelected()) {
			G.photon_xsection = EGS4.photon_xsections_epdl;
		} else if (mf.photoxRb_xcom.isSelected()) {
			G.photon_xsection = EGS4.photon_xsections_xcom;
		}

		// Triplet
		if (mf.triplet_OnRb.isSelected())
			EGS4.itriplet = G.triplet_ON;
		else
			EGS4.itriplet = G.triplet_OFF;
		// Radiative compton correction
		if (mf.radc_OnRb.isSelected())
			EGS4.radc_flag = G.radc_ON;
		else
			EGS4.radc_flag = G.radc_OFF;

		i = mf.bcaCb.getSelectedIndex();
		if (i == 0)
			EGS4.bca_algorithm = G.BCA_EXACT;
		else
			EGS4.bca_algorithm = G.BCA_PRESTA_I;

		i = mf.esaCb.getSelectedIndex();
		if (i == 0)
			EGS4.transport_algorithm = G.estep_alg_PRESTA_II;
		else
			EGS4.transport_algorithm = G.estep_alg_PRESTA_I;

		new G();

		stopThread();// kill all threads
		statusL.setText(resources.getString("status.done"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Add element in list
	 * @param s, the data
	 * @param dlm, the list model
	 */
	public static void add(String s, DefaultListModel dlm) {
		dlm.addElement(s);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Remove all list entries.
	 * @param dlm, the list model
	 */
	public static void removeAll(DefaultListModel dlm) {
		dlm.clear();
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get a value from a list.
	 * @param list, the list
	 * @return the result
	 */
	public static String getSelectedValueAsString(JList list) {
		return (String) list.getSelectedValue();
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Select an entry from a list
	 * @param index the entry index
	 * @param list the list object (JList that is) 
	 */
	public static void select(int index, JList list) {
		list.setSelectedIndex(index);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Get selected index from a list
	 * @param list the list
	 * @return the result
	 */
	public static int getSelectedIndex(JList list) {
		return list.getSelectedIndex();
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Remove an entry from the list
	 * @param index the entry index
	 * @param dlm the list model
	 */
	public static void remove(int index, DefaultListModel dlm) {
		dlm.remove(index);
	}

}
