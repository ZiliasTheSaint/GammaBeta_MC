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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import danfulea.phys.egs.EGS4Geom;
import danfulea.phys.egs.EGS4Grid;
import danfulea.phys.egs.EGS4Macro;
import danfulea.phys.egs.EGS4SrcEns;
import danfulea.phys.egsOutput.BoxAppEff;
import danfulea.utils.FrameUtilities;
import danfulea.utils.PDFRenderer;

/**
 * BoxApp class is GUI interface for BoxAppEff. <br>
 * It is special created for Druker Al. former employed at National Institute of Metrology Bucharest.
 * @author Dan Fulea, final version on 27 Jun. 2011
 * 
 */
@SuppressWarnings("serial")
public class BoxApp extends JFrame implements Runnable {
	private GammaBetaFrame mf;
	protected String outFilename = null;
	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	protected Color fundal = new Color(144, 37, 38, 255);// new
	// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(950, 700);

	protected ResourceBundle resources;
	// ##################################################################
	JTabbedPane tabs;
	protected JTextField ethreshTf = new JTextField(5);
	protected JTextField sddistTf = new JTextField(5);// point source distance

	@SuppressWarnings("rawtypes")
	protected JComboBox einCb;
	protected JButton spectrumB = new JButton();
	protected JTextField spectrumTf = new JTextField(20);

	protected JTextField XBOUNDTf = new JTextField(5);
	protected JTextField YBOUNDTf = new JTextField(5);
	protected JTextField ZBOUNDTf = new JTextField(5);
	protected JTextField XDETTf = new JTextField(5);
	protected JTextField YDETTf = new JTextField(5);
	protected JTextField XDETCENTERTf = new JTextField(5);
	protected JTextField YDETCENTERTf = new JTextField(5);

	protected JTextField airmaterialTf = new JTextField(25);
	protected JButton airchooseB = new JButton();

	protected JTextArea simTa = new JTextArea();

	protected JButton startsimB = new JButton();
	protected JButton stopsimB = new JButton();
	protected JButton printB = new JButton();

	//private final String FILESEPARATOR = System.getProperty("file.separator");
	@SuppressWarnings("rawtypes")
	protected JComboBox sourceTypeCb;// source type-0=Cylinder, 1=Marinelli

	protected JTextField energyTf = new JTextField(5);// energie photon --MeV
	protected JButton saveSB = new JButton();
	protected JButton saveDB = new JButton();
	protected JButton loadSB = new JButton();
	protected JButton loadDB = new JButton();

	private boolean stopAnim = true;
	private boolean stopAppend = false;

	private JLabel statusL = new JLabel("Waiting...");
	private volatile Thread simTh = null;
	private volatile Thread statusTh = null;// status display thread!
	private int delay = 100;
	private int frameNumber = -1;
	private String statusRunS = "";

	@SuppressWarnings("rawtypes")
	protected JComboBox nPhotonsCb;// nr of photons in sim
	protected JRadioButton electronRb, positronRb;

	private static final String BASE_RESOURCE_CLASS = "gammaBetaMc.resources.BoxAppResources";

	/**
	 * Constructor.  
	 * @param mf GammaBetaFrame object
	 */
	public BoxApp(GammaBetaFrame mf) {
		super(
				"BOX Application. Efficiency evaluation for a given BETA spectrum");
		this.mf = mf;
		fundal = GammaBetaFrame.bkgColor;
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
	 * This method is called from within the constructor to initialize the form.
	 */
	private void createGUI() {
		BoxAppEff.jta = simTa;
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
		// setContentPane(new JScrollPane(content));
		// content.setOpaque(true); // content panes must be opaque
		// pack();
	}

	/**
	 * Creating tabs
	 * @return the result
	 */
	private JTabbedPane createTabs() {

		JTabbedPane tabs = new JTabbedPane();

		BOXGUI dsg = new BOXGUI(this);
		JPanel dsPan = dsg.createSimMainPanel();
		JPanel outPan = dsg.createOutputPanel();

		dsPan.setBorder(BoxApp.STANDARD_BORDER);
		outPan.setBorder(BoxApp.STANDARD_BORDER);

		String s = "General Monte Carlo";
		tabs.add(s, dsPan);
		s = "Results";
		tabs.add(s, outPan);

		return tabs;
	}

	// END GUI-----------------------------------------------------------------
	/**
	 * All events (as in actions) are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == startsimB) {
					statusRunS = resources.getString("status.computing");
					startThread();
				}

				if (evt.getSource() == stopsimB) {
					stopAppend = true;
					stopThread();
				}
				if (evt.getSource() == printB) {
					printResults();
				}
				if (evt.getSource() == saveSB) {
					saveSource();
				}
				if (evt.getSource() == saveDB) {
					saveDet();
				}
				if (evt.getSource() == loadSB) {
					loadSource();
				}
				if (evt.getSource() == loadDB) {
					loadDet();
				}

				if (evt.getSource() == airchooseB) {
					selectMaterial(1);
				}

				if (evt.getSource() == spectrumB) {
					chooseSpectrum();
				}

			}
		};

		ItemListener il = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getSource() == sourceTypeCb) {
					setSource();
				}
				if (ie.getSource() == einCb) {
					setEin();
				}

			}
		};
		
		saveSB.addActionListener(al);
		loadSB.addActionListener(al);
		saveDB.addActionListener(al);
		loadDB.addActionListener(al);
		spectrumB.addActionListener(al);

		airchooseB.addActionListener(al);

		startsimB.addActionListener(al);
		stopsimB.addActionListener(al);
		printB.addActionListener(al);

		sourceTypeCb.addItemListener(il);
		einCb.addItemListener(il);
	}

	/**
	 * Print simulation information.
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

			//new BoxAppReport(this);
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
	 * Read spectrum file.
	 */
	private void chooseSpectrum() {
		String ext = "spectrum";// resources.getString("src.load");
		String pct = ".";
		String description = "Spectrum file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");String egsDatas="egs";
		String datas = "Data"+file_sep+egsDatas + file_sep + "spectra";
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
		XBOUNDTf.setEnabled(false);
		YBOUNDTf.setEnabled(false);
		ZBOUNDTf.setEnabled(false);

		if (index == 0) {
			sddistTf.setEnabled(true);

		}
		if (index == 1) {
			XBOUNDTf.setEnabled(true);
			YBOUNDTf.setEnabled(true);
			ZBOUNDTf.setEnabled(true);
		}

	}

	/**
	 * Save source information to a file.
	 */
	private void saveSource() {
		String ext = resources.getString("src.load");
		String pct = ".";
		String description = resources.getString("src.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String egsDatas="egs";
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsDatas;
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
			s = s + XBOUNDTf.getText() + "\n";
			s = s + YBOUNDTf.getText() + "\n";
			s = s + ZBOUNDTf.getText() + "\n";
			s = s + sddistTf.getText() + "\n";

			s = s + airmaterialTf.getText() + "\n";
			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			} catch (Exception ex) {

			}

		}

	}

	/**
	 * Save detector information to a file.
	 */
	private void saveDet() {
		String ext = resources.getString("det.load");
		String pct = ".";
		String description = resources.getString("det.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String egsDatas="egs";
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsDatas;
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
			s = s + XDETTf.getText() + "\n";
			s = s + YDETTf.getText() + "\n";
			s = s + XDETCENTERTf.getText() + "\n";
			s = s + YDETCENTERTf.getText() + "\n";
			// s=s+airmaterialTf.getText()+"\n";

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			} catch (Exception ex) {

			}
		}

	}

	/**
	 * Set box material.
	 * @param imat imat - this should be 1 for now.
	 */
	private void selectMaterial(int imat) {
		// .pegs4dat
		String ext = "pegs4dat";// resources.getString("src.load");
		String pct = ".";
		String description = "Material constants file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");String egsDatas="egs";
		String datas = "Data"+file_sep+egsDatas + file_sep + "interactiv";
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

			if (imat == 1) {
				airmaterialTf.setText(filename);
			}

		}

	}

	/**
	 * Load the source information
	 */
	private void loadSource() {
		String ext = resources.getString("src.load");
		String pct = ".";
		String description = resources.getString("src.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String egsDatas="egs";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsDatas;
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
							XBOUNDTf.setText(line);
						}
						if (lnr == 3) {
							YBOUNDTf.setText(line);
						}
						if (lnr == 4) {
							ZBOUNDTf.setText(line);
						}
						if (lnr == 5) {
							sddistTf.setText(line);
						}
						if (lnr == 6) {
							airmaterialTf.setText(line);
						}

						desc = new StringBuffer();

					}
				}
				in.close();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Load the detector information
	 */
	private void loadDet() {
		String ext = resources.getString("det.load");
		String pct = ".";
		String description = resources.getString("det.load.description");
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = resources.getString("data.load");// "Data";
		String filename = "";
		String egsDatas="egs";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsDatas;
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
							XDETTf.setText(line);
						}
						if (lnr == 2) {
							YDETTf.setText(line);
						}
						if (lnr == 3) {
							XDETCENTERTf.setText(line);
						}
						if (lnr == 4) {
							YDETCENTERTf.setText(line);
						}
						// if(lnr==5)
						// {
						// airmaterialTf.setText(line);
						// }

						desc = new StringBuffer();

					}
				}
				in.close();
			} catch (Exception e) {

			}

		}

	}

	/**
	 * Start thread related tasks
	 */
	private void startThread() {
		stopAnim = false;
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
	 * Stop thread related tasks
	 */
	private void stopThread() {
		statusTh = null;
		frameNumber = 0;
		stopAnim = true;

		if (simTh == null) {
			stopAppend = false;
			// press kill button but simulation never started

			return;
		}

		simTh = null;

		if (stopAppend) {// kill button was pressed!
			EGS4.STOPPROGRAM = true;
			simTa.append("Interrupted by the user!!" + " \n");
			stopAppend = false;
			String label = resources.getString("status.done");
			statusL.setText(label);
		}

	}

	/**
	 * Thread specific run method.
	 */
	public void run() {
		// Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		// simTh.setPriority(Thread.NORM_PRIORITY);
		// performEfficiencyCalculation();
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
			//if (command.equals(RUN_COMMAND)) {
				performEfficiencyCalculation();
			//}
		}
	}

	/**
	 * Compute efficiency by calling Monte Carlo engine.
	 */
	private void performEfficiencyCalculation()// ----@@@@@@@@DO NOT FORGET
												// CHECK READ!!!!!
	{
		String s = "";
		simTa.selectAll();
		simTa.replaceSelection("");

		tabs.setSelectedIndex(1);
		// ======================OBLIGE=======================
		BoxAppEff.reset();
		BoxAppEff.systemOutB = false;

		boolean neg = false;
		try {
			BoxAppEff.ZBOUND = EGS4.stringToDouble(ZBOUNDTf.getText());// 12.0;//cm
			if (BoxAppEff.ZBOUND < 0)
				neg = true;

			BoxAppEff.YBOUND = EGS4.stringToDouble(YBOUNDTf.getText());// 12.0;//cm
			if (BoxAppEff.YBOUND < 0)
				neg = true;

			BoxAppEff.XBOUND = EGS4.stringToDouble(XBOUNDTf.getText());// 12.0;//cm
			if (BoxAppEff.XBOUND < 0)
				neg = true;

			BoxAppEff.XDET = EGS4.stringToDouble(XDETTf.getText());// 6.0;//cm
			if (BoxAppEff.XDET < 0)
				neg = true;

			BoxAppEff.YDET = EGS4.stringToDouble(YDETTf.getText());// 6.0;//cm
			if (BoxAppEff.YDET < 0)
				neg = true;

			BoxAppEff.XDETCENTER = EGS4.stringToDouble(XDETCENTERTf.getText());// 0.0;//cm
			if (BoxAppEff.XDETCENTER < 0)
				neg = true;

			BoxAppEff.YDETCENTER = EGS4.stringToDouble(YDETCENTERTf.getText());// 0.0;//cm
			if (BoxAppEff.YDETCENTER < 0)
				neg = true;

			BoxAppEff.ps_distance = EGS4.stringToDouble(sddistTf.getText());// 6.2;//cm
			if (BoxAppEff.ps_distance < 0)
				neg = true;

		} catch (Exception ex) {
			s = "Error in detector inputs. Insert positive numbers!" + " \n";
			simTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			return;
		}
		if (neg) {
			s = "Error in detector inputs. Insert positive numbers!" + " \n";
			simTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			return;
		}

		if (BoxAppEff.YDETCENTER + BoxAppEff.YDET / 2. > BoxAppEff.YBOUND / 2.
				|| BoxAppEff.YDETCENTER - BoxAppEff.YDET / 2. < 0.0 - BoxAppEff.YBOUND / 2.
				|| BoxAppEff.XDETCENTER + BoxAppEff.XDET / 2. > BoxAppEff.XBOUND / 2.
				|| BoxAppEff.XDETCENTER - BoxAppEff.XDET / 2. < 0.0 - BoxAppEff.XBOUND / 2.) {
			s = "Error in detector inputs. Detector position and detector dimensions DO NOT FIT with BOX dimensions!"
					+ " \n";
			simTa.append(s);
			stopThread();statusL.setText(resources.getString("status.done"));
			return;
		}

		BoxAppEff.NCASE = EGS4.stringToInt((String) nPhotonsCb
				.getSelectedItem());// 20000;
		BoxAppEff.airmaterial = "air_dry_nearsealevel";

		int i = sourceTypeCb.getSelectedIndex();
		BoxAppEff.SOURCE = i;

		EGS4.setMXMED(1);// "MAX # OF MEDIA 		"
		EGS4.setMXREG(3);
		EGS4.setMXSTACK(4000);// "NEED HUGE STACK FOR CORRELATIONS+splitting   "
		EGS4.setMXRANGE(500); // "for range arrays used in range_rejection()"
		EGS4.$MXMDSH = 100;// allways

		EGS4.reset();
		EGS4Core.reset();
		EGS4Geom.reset();
		EGS4Grid.reset();
		EGS4Macro.reset();
		EGS4SrcEns.reset();
		EGS4.egs_set_defaults();// first default then:

		if (electronRb.isSelected()) {
			EGS4SrcEns.ipart = EGS4SrcEns.ipart_electron;
		} else if (positronRb.isSelected()) {
			EGS4SrcEns.ipart = EGS4SrcEns.ipart_positron;
		}
		if (EGS4SrcEns.ipart != 0) {
			try {
				BoxAppEff.ETHRESHOLD = EGS4.stringToDouble(ethreshTf.getText());
			} catch (Exception e) {
				s = "Kinetic threshold energy for electrons must be a positive (or zero) number!"
						+ " \n";
				simTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}

			if (BoxAppEff.ETHRESHOLD < 0) {
				s = "Kinetic threshold energy for electrons must be a positive (or zero) number!"
						+ " \n";
				simTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}
		}

		i = einCb.getSelectedIndex();
		if (i == 0) {
			EGS4SrcEns.monoindex = EGS4SrcEns.iMONOENERGETIC;
			try {
				EGS4SrcEns.ikemev = EGS4.stringToDouble(energyTf.getText());// 2.662;
			} catch (Exception e) {
				s = "Error in energy inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;
			}
		} else// spectrum
		{
			EGS4SrcEns.monoindex = EGS4SrcEns.iSPECTRUM;
			EGS4SrcEns.enerFilename = spectrumTf.getText();// "sr90y90"

			if (EGS4SrcEns.enerFilename.compareTo("") == 0) {
				s = "Invalid spectrum file!" + " \n";
				simTa.append(s);
				stopThread();statusL.setText(resources.getString("status.done"));
				return;

			}
		}

		new BoxAppEff();

		stopThread();// kill all threads
		statusL.setText(resources.getString("status.done"));
	}
}
