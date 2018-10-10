package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import danfulea.utils.ExampleFileFilter;
import danfulea.phys.egs.PEGS4;
import danfulea.phys.egs.PEGS4A;
import danfulea.phys.egs.PEGS4AGraph;
import danfulea.utils.FrameUtilities;

/**
 * Main class for creating material data and displaying the cross sections for 
 * electrons, positrons and photons. EPGCS stands for Electron, Positron and Gamma Cross 
 * Sections.<br>
 * Medium can be autoselected from density file; Medium can also be user defined 
 * but avoiding duplicates, in other words only mixtures can be manually created. 
 * PEGS4 can handle compound case (see SPINIT subroutine), i.e. H2 liquid with ne=2 and z[0]=z[1]=1.0, 
 * but this is not allowed here (by design, we want elements and their fraction by weight, so we can't have H and H with non sense fractions).
 * 
 * @author Dan Fulea, 22 AUG. 2005
 */

@SuppressWarnings("serial")
public class EPGCS extends JFrame {
	private GammaBetaFrame mf;

	private static final String datas = "Data";
	private static final String egsDatas = "egs";
	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	
	protected ResourceBundle resources;

	protected JButton graphB = new JButton();
	protected JButton calclB = new JButton();
	protected JButton addB = new JButton();
	protected JButton delB = new JButton();
	protected JButton resetB = new JButton();
	protected JButton selectB = new JButton();
	protected JButton createB = new JButton();
	// ----------------------------------------------
	protected JPanel medAutP = new JPanel();
	protected JPanel medManP = new JPanel();
	// ----------------------------------------------
	protected JRadioButton autoRb;
	protected JRadioButton manualRb;
	@SuppressWarnings("rawtypes")
	protected JComboBox elemCb;
	// -------------------------------------------------
	protected JTextField mediumTf = new JTextField(5);
	protected JTextField densityTf = new JTextField(5);
	protected JTextField pathTf = new JTextField(30);
	protected JTextField weightTf = new JTextField(5);
	protected JTextField aeTf = new JTextField(5);
	protected JTextField apTf = new JTextField(5);
	protected JTextField ueTf = new JTextField(5);
	protected JTextField upTf = new JTextField(5);
	protected JTextField glowTf = new JTextField(5);
	protected JTextField ghighTf = new JTextField(5);
	protected JTextField elowTf = new JTextField(5);
	protected JTextField ehighTf = new JTextField(5);
	protected JTextField energyTf = new JTextField(5);
	protected JTextField gaspTf = new JTextField(5);
	protected JTextField saveMaterialTf = new JTextField(25);
	// ---------------------------------------------------------------
	protected JTextArea simTa = new JTextArea();
	// -----------------------------------------------------
	@SuppressWarnings("rawtypes")
	protected DefaultListModel kvdlm = new DefaultListModel();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected JList kvmL = new JList(kvdlm);
	// --------------------------------------------------
	protected String densityPath = "";
	protected int nlistpoints = 0;
	private Vector<String> rhozv = new Vector<String>();
	private Vector<String> elemv = new Vector<String>();
	private String[] asyme;
	private double[] rhoze;
	private double[] pze;
	private double sumrhoz = 0.;
	// --------------------------------------------------
	protected Color fundal = new Color(144, 37, 38, 255);// new
														// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(950, 700);

	//private AboutFrame aboutFrame;

	/**
	 * Constructor.
	 * @param mf, the GammaBetaFrame object 
	 */
	public EPGCS(GammaBetaFrame mf) {
		super("EPGCS Electron, Positron and Gamma Cross Sections");
		this.mf = mf;
		this.resources=mf.resources;
		fundal = GammaBetaFrame.bkgColor;
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
	 * This method is called from within the constructor to initialize the form.
	 */
	private void createGUI() {
		JPanel content = new JPanel(new BorderLayout());
		// restul//////////////////////////////////////////
		EPGCSGUI dsg = new EPGCSGUI(this);
		JPanel dsPan = dsg.createSimMainPanel();
		// /////////////////////////////////////////////////
		content.add(dsPan, BorderLayout.CENTER);
		setContentPane(new JScrollPane(content));
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	/**
	 * All actions are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == selectB) {
					performFileChoose(pathTf);
					// String
					// label=resources.getString("main.PleaseWait.label");
					// pw = new PleaseWait(label);
					// pw.startAnimation();

					// startThread();

				}

				if (evt.getSource() == autoRb) {
					manualPanelEnabled(false);
					autoPanelEnabled(true);
				}

				if (evt.getSource() == manualRb) {
					manualPanelEnabled(true);
					autoPanelEnabled(false);
				}

				if (evt.getSource() == weightTf || evt.getSource() == addB) {
					addDataInList();
				}

				if (evt.getSource() == resetB) {
					resetData();
				}

				if (evt.getSource() == delB) {
					deleteSelectedData();
				}

				if (evt.getSource() == calclB) {
					computeCS();
				}

				if (evt.getSource() == createB) {
					createFile();
				}

				if (evt.getSource() == graphB) {
					plotGraph();
				}

			}
		};
		weightTf.addActionListener(al);
		selectB.addActionListener(al);
		autoRb.addActionListener(al);
		manualRb.addActionListener(al);
		addB.addActionListener(al);
		resetB.addActionListener(al);
		delB.addActionListener(al);
		calclB.addActionListener(al);
		createB.addActionListener(al);
		graphB.addActionListener(al);
	}

	/**
	 * Plot cross-sections.
	 */
	private void plotGraph() {
		PEGS4A.reset();
		String aes = elowTf.getText();
		String aps = glowTf.getText();
		String ues = ehighTf.getText();
		String ups = ghighTf.getText();
		// String ens=energyTf.getText();
		// ----------------------
		double EMKS = 1.60210E-19;
		double RME = 9.1091E-28;
		double C = 2.997925E+10;
		double ERGMEV = (1.E+6) * (EMKS * 1.E+7);
		double RM = RME * C * C / ERGMEV;
		String aes1 = aeTf.getText();
		String aps1 = apTf.getText();
		String ues1 = ueTf.getText();
		String ups1 = upTf.getText();
		String gasps = gaspTf.getText();
		double gaspd = 0.;
		// String ens1=energyTf.getText();
		double aed1 = 0.0;
		double apd1 = 0.0;
		double ued1 = 0.0;
		double upd1 = 0.0;// double end1=0.0;
		try {
			aed1 = stringToDouble(aes1);
			apd1 = stringToDouble(aps1);
			ued1 = stringToDouble(ues1);
			upd1 = stringToDouble(ups1);
			gaspd = stringToDouble(gasps);
			// end1=stringToDouble(ens1);
		} catch (Exception e) {
			String title = resources.getString("dialog.double.title");
			String message = resources.getString("dialog.double.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed1 < 0 || ued1 < 0) || (apd1 < 0 || upd1 < 0)) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (gaspd < 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// double testdbl=Math.min(ued,upd);
		if ((aed1 >= ued1) || (apd1 >= upd1)) {
			String title = resources.getString("dialog.au.title");
			String message = resources.getString("dialog.au.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (aed1 < RM) {
			String title = resources.getString("dialog.ae.title");
			String message = resources.getString("dialog.ae.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// ------------------------
		double aed = 0.0;
		double apd = 0.0;
		double ued = 0.0;
		double upd = 0.0;// double end=0.0;
		try {
			aed = stringToDouble(aes);
			apd = stringToDouble(aps);
			ued = stringToDouble(ues);
			upd = stringToDouble(ups);
			// end=stringToDouble(ens);
		} catch (Exception e) {
			String title = resources.getString("dialog.double.title");
			String message = resources.getString("dialog.double.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed < 0 || ued < 0) || (apd < 0 || upd < 0)) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed >= ued) || (apd >= upd)) {
			String title = resources.getString("dialog.au2.title");
			String message = resources.getString("dialog.au2.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (autoRb.isSelected()) {
			// System.out.println("y");
			if (densityPath.compareTo("") == 0) {
				String title = resources.getString("dialog.path.title");
				String message = resources.getString("dialog.path.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// init
			PEGS4A.setDensData(densityPath);
			// PEGS4A.setELimit();
			PEGS4A.setELimit(aed1, apd1, ued1, upd1);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4_exact();
		} else// manual rb
		{
			if (nlistpoints == 0) {
				String title = resources.getString("dialog.list.title");
				String message = resources.getString("dialog.list.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			convertVectors();
			// test normalize------------------
			if ((sumrhoz > 1.01) || (sumrhoz < 0.99)) {
				String title = resources.getString("dialog.norm.title");
				String message = resources.getString("dialog.norm.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// --------------------------------
			String med = mediumTf.getText();
			String rhoes = densityTf.getText();
			double rhoe = 0.;
			try {
				rhoe = stringToDouble(rhoes);
			} catch (Exception excep) {
				String title = resources.getString("dialog.double.title");
				String message = resources.getString("dialog.double.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			if (rhoe <= 0.) {
				String title = resources.getString("dialog.pos.title");
				String message = resources.getString("dialog.pos.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			// initialisation
			PEGS4A.preinit(med, nlistpoints, rhoe, asyme, rhoze, pze);
			// PEGS4A.setELimit();
			PEGS4A.setELimit(aed1, apd1, ued1, upd1);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4_exact();
		}
		// ---------------NOW ALL IS OK SO PLOT the graph:
		new PEGS4AGraph(apd, upd, aed, ued);
	}

	/**
	 * Create material file to be used in EGSnrc based simulations.
	 */
	private void createFile() {
		PEGS4A.reset();
		double EMKS = 1.60210E-19;
		double RME = 9.1091E-28;
		double C = 2.997925E+10;
		double ERGMEV = (1.E+6) * (EMKS * 1.E+7);
		double RM = RME * C * C / ERGMEV;
		String aes = aeTf.getText();
		String aps = apTf.getText();
		String ues = ueTf.getText();
		String ups = upTf.getText();
		String ens = energyTf.getText();
		String gasps = gaspTf.getText();
		double gaspd = 0.;
		double aed = 0.0;
		double apd = 0.0;
		double ued = 0.0;
		double upd = 0.0;
		double end = 0.0;

		String filenam = saveMaterialTf.getText();
		if (filenam.compareTo("") == 0) {
			PEGS4A.mediumB = true;
		} else {
			PEGS4A.mediumB = false;
			PEGS4A.mediumFilename = filenam;
		}
		try {
			aed = stringToDouble(aes);
			apd = stringToDouble(aps);
			ued = stringToDouble(ues);
			upd = stringToDouble(ups);
			end = stringToDouble(ens);
			gaspd = stringToDouble(gasps);
		} catch (Exception e) {
			String title = resources.getString("dialog.double.title");
			String message = resources.getString("dialog.double.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed < 0 || ued < 0) || (apd < 0 || upd < 0)) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (gaspd < 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		double testdbl = Math.min(ued, upd);
		if (end > testdbl) {
			String title = resources.getString("dialog.ue.title");
			String message = resources.getString("dialog.ue.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (end <= 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed >= ued) || (apd >= upd)) {
			String title = resources.getString("dialog.au.title");
			String message = resources.getString("dialog.au.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (aed < RM) {
			String title = resources.getString("dialog.ae.title");
			String message = resources.getString("dialog.ae.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (autoRb.isSelected()) {
			// System.out.println("y");
			if (densityPath.compareTo("") == 0) {
				String title = resources.getString("dialog.path.title");
				String message = resources.getString("dialog.path.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// init
			PEGS4A.setDensData(densityPath);
			PEGS4A.setELimit(aed, apd, ued, upd);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4();
		} else// manual rb
		{
			if (nlistpoints == 0) {
				String title = resources.getString("dialog.list.title");
				String message = resources.getString("dialog.list.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			convertVectors();
			// test normalize------------------
			if ((sumrhoz > 1.01) || (sumrhoz < 0.99)) {
				String title = resources.getString("dialog.norm.title");
				String message = resources.getString("dialog.norm.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// --------------------------------
			String med = mediumTf.getText();
			String rhoes = densityTf.getText();
			double rhoe = 0.;
			try {
				rhoe = stringToDouble(rhoes);
			} catch (Exception excep) {
				String title = resources.getString("dialog.double.title");
				String message = resources.getString("dialog.double.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			if (rhoe <= 0.) {
				String title = resources.getString("dialog.pos.title");
				String message = resources.getString("dialog.pos.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			// initialisation
			PEGS4A.preinit(med, nlistpoints, rhoe, asyme, rhoze, pze);
			PEGS4A.setELimit(aed, apd, ued, upd);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4();
		}

		simTa.selectAll();
		simTa.replaceSelection("");

		simTa.append("pegs4dat file created succesfully!" + " \n");

	}

	/**
	 * Compute cross sections.
	 */
	private void computeCS() {
		PEGS4A.reset();
		double EMKS = 1.60210E-19;
		double RME = 9.1091E-28;
		double C = 2.997925E+10;
		double ERGMEV = (1.E+6) * (EMKS * 1.E+7);
		double RM = RME * C * C / ERGMEV;
		String aes = aeTf.getText();
		String aps = apTf.getText();
		String ues = ueTf.getText();
		String ups = upTf.getText();
		String ens = energyTf.getText();
		String gasps = gaspTf.getText();
		double gaspd = 0.;
		double aed = 0.0;
		double apd = 0.0;
		double ued = 0.0;
		double upd = 0.0;
		double end = 0.0;
		try {
			aed = stringToDouble(aes);
			apd = stringToDouble(aps);
			ued = stringToDouble(ues);
			upd = stringToDouble(ups);
			end = stringToDouble(ens);
			gaspd = stringToDouble(gasps);
		} catch (Exception e) {
			String title = resources.getString("dialog.double.title");
			String message = resources.getString("dialog.double.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed < 0 || ued < 0) || (apd < 0 || upd < 0)) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (gaspd < 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		double testdbl = Math.min(ued, upd);
		if (end > testdbl) {
			String title = resources.getString("dialog.ue.title");
			String message = resources.getString("dialog.ue.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (end <= 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if ((aed >= ued) || (apd >= upd)) {
			String title = resources.getString("dialog.au.title");
			String message = resources.getString("dialog.au.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (aed < RM) {
			String title = resources.getString("dialog.ae.title");
			String message = resources.getString("dialog.ae.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (autoRb.isSelected()) {
			// System.out.println("y");
			if (densityPath.compareTo("") == 0) {
				String title = resources.getString("dialog.path.title");
				String message = resources.getString("dialog.path.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// init
			PEGS4A.setDensData(densityPath);
			PEGS4A.setELimit(aed, apd, ued, upd);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4_exact();
		} else// manual rb
		{
			if (nlistpoints == 0) {
				String title = resources.getString("dialog.list.title");
				String message = resources.getString("dialog.list.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			convertVectors();
			// test normalize------------------
			if ((sumrhoz > 1.01) || (sumrhoz < 0.99)) {
				String title = resources.getString("dialog.norm.title");
				String message = resources.getString("dialog.norm.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// --------------------------------
			String med = mediumTf.getText();
			String rhoes = densityTf.getText();
			double rhoe = 0.;
			try {
				rhoe = stringToDouble(rhoes);
			} catch (Exception excep) {
				String title = resources.getString("dialog.double.title");
				String message = resources.getString("dialog.double.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			if (rhoe <= 0.) {
				String title = resources.getString("dialog.pos.title");
				String message = resources.getString("dialog.pos.message");
				JOptionPane.showMessageDialog(null, message, title,
						JOptionPane.ERROR_MESSAGE);
				densityTf.setText("");
				densityTf.requestFocusInWindow();
				return;
			}
			// initialisation
			PEGS4A.preinit(med, nlistpoints, rhoe, asyme, rhoze, pze);
			PEGS4A.setELimit(aed, apd, ued, upd);
			PEGS4A.setGasp(gaspd);
			PEGS4A.init_pegs4_exact();
		}
		// ---------------NOW ALL IS OK SO COMPUTE CROSS SECTIONS:
		double[] cst = PEGS4A.getTotalCrossSections(end);
		// IUNRST=0->MEANS RESTRICTED STOPPING POWER (COLLISION +RADIATIVE)
		// "TOTAL ELECTRON STOPPING POWER (COLLISION +RADIATIVE)"
		double sptote = PEGS4.SPTOTE(end, PEGS4.AE, PEGS4.AP) / PEGS4.RLC;
		// "TOTAL POSITRON STOPPING POWER (COLLISION +RADIATIVE)"
		double sptotp = PEGS4.SPTOTP(end, PEGS4.AE, PEGS4.AP) / PEGS4.RLC;
		// -----------first clear the textarea
		simTa.selectAll();
		simTa.replaceSelection("");
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumFractionDigits(5);// default e 2 oricum!!
		nf.setMaximumFractionDigits(5);// default e 2 oricum!!
		nf.setGroupingUsed(false);// no 4,568.02 but 4568.02
		String pattern = "0.###E0";
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
		DecimalFormat nff = new DecimalFormat(pattern, dfs);
		simTa.append(resources.getString("resut.def") + " \n");
		simTa.append(resources.getString("resut.notes1") + " \n");
		simTa.append(resources.getString("resut.notes2") + " \n");
		simTa.append(resources.getString("resut.notes3") + " \n");
		simTa.append(resources.getString("resut.notes4") + " \n");
		simTa.append(resources.getString("resut.RLC") + nff.format(PEGS4.RLC)
				+ " \n");
		simTa.append(resources.getString("resut.ep") + " \n");
		simTa.append(resources.getString("resut.brems.rl") + nff.format(cst[0])
				+ resources.getString("resut.brems.cm") + nff.format(cst[1])
				+ resources.getString("resut.brems.g") + nff.format(cst[2])
				+ resources.getString("resut.brems.b") + nff.format(cst[3])
				+ " \n");
		simTa.append(resources.getString("resut.Molle.rl") + nff.format(cst[4])
				+ resources.getString("resut.Molle.cm") + nff.format(cst[5])
				+ resources.getString("resut.Molle.g") + nff.format(cst[6])
				+ resources.getString("resut.Molle.b") + nff.format(cst[7])
				+ " \n");
		simTa.append(resources.getString("resut.ETot.rl") + nff.format(cst[8])
				+ resources.getString("resut.ETot.cm") + nff.format(cst[9])
				+ resources.getString("resut.ETot.g") + nff.format(cst[10])
				+ resources.getString("resut.ETot.b") + nff.format(cst[11])
				+ " \n");
		simTa.append(resources.getString("resut.ebr") + nf.format(cst[12])
				+ " \n");
		simTa.append(resources.getString("resut.sptote") + nff.format(sptote)
				+ " \n");
		simTa.append(resources.getString("resut.Bhabh.rl")
				+ nff.format(cst[13]) + resources.getString("resut.Bhabh.cm")
				+ nff.format(cst[14]) + resources.getString("resut.Bhabh.g")
				+ nff.format(cst[15]) + resources.getString("resut.Bhabh.b")
				+ nff.format(cst[16]) + " \n");
		simTa.append(resources.getString("resut.Annih.rl")
				+ nff.format(cst[17]) + resources.getString("resut.Annih.cm")
				+ nff.format(cst[18]) + resources.getString("resut.Annih.g")
				+ nff.format(cst[19]) + resources.getString("resut.Annih.b")
				+ nff.format(cst[20]) + " \n");
		simTa.append(resources.getString("resut.PTot.rl") + nff.format(cst[21])
				+ resources.getString("resut.PTot.cm") + nff.format(cst[22])
				+ resources.getString("resut.PTot.g") + nff.format(cst[23])
				+ resources.getString("resut.PTot.b") + nff.format(cst[24])
				+ " \n");
		simTa.append(resources.getString("resut.pbr1") + nf.format(cst[25])
				+ " \n");
		simTa.append(resources.getString("resut.pbr2") + nf.format(cst[26])
				+ " \n");
		simTa.append(resources.getString("resut.sptotp") + nff.format(sptotp)
				+ " \n");
		simTa.append(resources.getString("resut.ph") + " \n");
		simTa.append(resources.getString("resut.Photo.rl")
				+ nff.format(cst[27]) + resources.getString("resut.Photo.cm")
				+ nff.format(cst[28]) + resources.getString("resut.Photo.g")
				+ nff.format(cst[29]) + resources.getString("resut.Photo.b")
				+ nff.format(cst[30]) + " \n");
		simTa.append(resources.getString("resut.Compt.rl")
				+ nff.format(cst[31]) + resources.getString("resut.Compt.cm")
				+ nff.format(cst[32]) + resources.getString("resut.Compt.g")
				+ nff.format(cst[33]) + resources.getString("resut.Compt.b")
				+ nff.format(cst[34]) + " \n");
		simTa.append(resources.getString("resut.Pair.rl") + nff.format(cst[35])
				+ resources.getString("resut.Pair.cm") + nff.format(cst[36])
				+ resources.getString("resut.Pair.g") + nff.format(cst[37])
				+ resources.getString("resut.Pair.b") + nff.format(cst[38])
				+ " \n");
		simTa.append(resources.getString("resut.Cohe.rl") + nff.format(cst[39])
				+ resources.getString("resut.Cohe.cm") + nff.format(cst[40])
				+ resources.getString("resut.Cohe.g") + nff.format(cst[41])
				+ resources.getString("resut.Cohe.b") + nff.format(cst[42])
				+ " \n");
		simTa.append(resources.getString("resut.PhT_R.rl")
				+ nff.format(cst[43]) + resources.getString("resut.PhT_R.cm")
				+ nff.format(cst[44]) + resources.getString("resut.PhT_R.g")
				+ nff.format(cst[45]) + resources.getString("resut.PhT_R.b")
				+ nff.format(cst[46]) + " \n");
		simTa.append(resources.getString("resut.PhT.rl") + nff.format(cst[47])
				+ resources.getString("resut.PhT.cm") + nff.format(cst[48])
				+ resources.getString("resut.PhT.g") + nff.format(cst[49])
				+ resources.getString("resut.PhT.b") + nff.format(cst[50])
				+ " \n");
		// (GAMMA MEAN FREE PATH)
		double gmfp = 1. / cst[48];
		simTa.append(resources.getString("resut.gbr1") + nf.format(cst[51])
				+ resources.getString("resut.gbr11") + nf.format(cst[52])
				+ " \n");
		simTa.append(resources.getString("resut.gbr2") + nf.format(cst[53])
				+ resources.getString("resut.gbr22") + nf.format(cst[54])
				+ " \n");
		simTa.append(resources.getString("resut.gbr3") + nf.format(cst[55])
				+ resources.getString("resut.gbr33") + nf.format(cst[56])
				+ " \n");
		simTa.append(resources.getString("resut.gmfp") + nff.format(gmfp)
				+ " \n");

	}

	/**
	 * Convert some vectors to arrays.
	 */
	private void convertVectors() {
		asyme = new String[nlistpoints];
		rhoze = new double[nlistpoints];
		pze = new double[nlistpoints];
		sumrhoz = 0.;
		for (int i = 0; i < nlistpoints; i++) {
			String s = (String) elemv.elementAt(i);
			asyme[i] = s;
			String ss = (String) rhozv.elementAt(i);
			rhoze[i] = stringToDouble(ss);
			sumrhoz = sumrhoz + rhoze[i];
			pze[i] = 0.;
		}
	}

	/**
	 * Reset data for new element entries to be used in a new mixture.
	 */
	private void resetData() {
		rhozv.removeAllElements();
		elemv.removeAllElements();

		removeAll(kvdlm);
		nlistpoints = 0;

		weightTf.setText("");
		weightTf.requestFocusInWindow();
	}

	/**
	 * Delete selected element from a mixture
	 */
	private void deleteSelectedData() {
		if (nlistpoints != 0) {

			nlistpoints--;

			int index = getSelectedIndex(kvmL);

			remove(index, kvdlm);
			select(nlistpoints - 1, kvmL);

			elemv.removeElementAt(index);
			rhozv.removeElementAt(index);

			weightTf.setText("");
			weightTf.requestFocusInWindow();
		}
	}

	/**
	 * Add element data for creating a mixture.
	 */
	private void addDataInList() {
		boolean b = true;
		String ss = (String) (elemCb.getSelectedItem());
		String s1 = weightTf.getText();
		double d1 = 0.0;
		try {
			d1 = stringToDouble(s1);
		} catch (Exception e) {
			b = false;
			String title = resources.getString("dialog.double.title");
			String message = resources.getString("dialog.double.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			weightTf.setText("");
			weightTf.requestFocusInWindow();
		}

		if (!b)
			return;

		if (d1 <= 0) {
			String title = resources.getString("dialog.pos.title");
			String message = resources.getString("dialog.pos.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			weightTf.setText("");
			weightTf.requestFocusInWindow();
			return;
		}

		if (d1 > 1.) {
			String title = resources.getString("dialog.fraction.title");
			String message = resources.getString("dialog.fraction.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			weightTf.setText("");
			weightTf.requestFocusInWindow();
			return;
		}

		// end test-->la succes se poate merge mai departe
		// avoid repetition
		b = true;
		for (int i = 0; i < elemv.size(); i++) {
			String tst = (String) (elemv.elementAt(i));
			if (tst.compareTo(ss) == 0) {
				b = false;
				break;
			}
		}

		if (!b) {
			String title = resources.getString("dialog.duplicate.title");
			String message = resources.getString("dialog.duplicate.message");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			weightTf.setText("");
			weightTf.requestFocusInWindow();
			return;
		}
		// ---------------------------------
		// normalized test in calculations and graph!!!
		// ----------------------------------@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		add("Element: " + ss + " weight: " + d1, kvdlm);
		select(nlistpoints, kvmL);

		s1 = doubleToString(d1);
		rhozv.addElement(s1);
		elemv.addElement(ss);

		nlistpoints++;
		weightTf.setText("");
		weightTf.requestFocusInWindow();
	}

	/**
	 * Choose and open the density file.
	 * @param jtf the chosen file is displayed here
	 */
	private void performFileChoose(JTextField jtf) {
		ExampleFileFilter jpgFilter = new ExampleFileFilter("density",
				"Density correction file ");
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas +file_sep + egsDatas;
		JFileChooser chooser = new JFileChooser(opens);
		chooser.addChoosableFileFilter(jpgFilter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filename = chooser.getSelectedFile().toString();
			densityPath = filename;// System.out.println(densityPath);
		}
		jtf.setText(filename);
		jtf.setToolTipText(filename);
		// ------------------------------------------------------------
		// PEGS4A.setDensData(densityPath);
		// PEGS4A.setELimit(0.550,0.004,20.,100.);
		// PEGS4A.init_pegs4_exact();//WORKS!!
		// ------------------------------------------------------------
	}

	// -----------------	
	/**
	 * Enable/disable the manual mixture creation panel.
	 * @param b b
	 */
	protected void manualPanelEnabled(boolean b) {
		// elemRb.setEnabled(b);
		// mixtRb.setEnabled(b);
		mediumTf.setEnabled(b);
		densityTf.setEnabled(b);
		kvmL.setEnabled(b);
		elemCb.setEnabled(b);
		weightTf.setEnabled(b);
		addB.setEnabled(b);
		delB.setEnabled(b);
		resetB.setEnabled(b);
	}

	/**
	 * Enable/disable the auto mixture creation panel.
	 * @param b b
	 */
	private void autoPanelEnabled(boolean b) {
		pathTf.setEnabled(b);
		selectB.setEnabled(b);
	}
		
	// -----------------------------------------------------------------------------------------
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

	/**
	 * Get a value from a list.
	 * @param list, the list
	 * @return the result
	 */
	public static String getSelectedValueAsString(@SuppressWarnings("rawtypes") JList list) {
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

	/**
	 * Converts an int to another base
	 * @param value the string representation of an int (base 10 that is)
	 * @param base the desired base for conversion
	 * @return the String representation of the number in desired base
	 * @throws NumberFormatException can throw this exception
	 */
	public static String convertIntToOtherBase(String value, int base)
			throws NumberFormatException// 3a3f15f7=format Hexazecimal ex.
	{
		BigInteger bi = new BigInteger(value);
		return bi.toString(base);
	}

	/**
	 * Converts a number from other base to base 10
	 * @param value the String representation of a number
	 * @param base the base of the number
	 * @return the String representation of number in base 10
	 * @throws NumberFormatException can throw this exception
	 */
	public static String convertOtherBaseToInt(String value, int base)
			throws NumberFormatException {
		BigInteger bi = new BigInteger(value, base);
		return bi.toString(10);
	}

	/**
	 * Converts a string to int value.
	 * 
	 * @param value
	 *            the string
	 * @return the int value of input string
	 * @throws NumberFormatException
	 * can throws this exception
	 */
	public static int stringToInt(String value) throws NumberFormatException {
		value = value.trim();
		if (value.length() == 0) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	/**
	 * Converts an int number to string.
	 * 
	 * @param i
	 *            the int value
	 * @return the string representation
	 */
	public static String intToString(int i) {
		return Integer.toString(i);
	}

	/**
	 * Converts a string to float value.
	 * 
	 * @param value
	 *            the string
	 * @return the float value of input string
	 * @throws NumberFormatException
	 * can throws this exception
	 */
	public static float stringToFloat(String value)
			throws NumberFormatException {
		value = value.trim();
		if (value.length() == 0) {
			return 0;
		} else {
			return Float.parseFloat(value);
		}
	}


	/**
	 * Converts a float number to string.
	 * 
	 * @param f
	 *            the float value
	 * @return the string representation
	 */
	public static String floatToString(float f) {
		return Float.toString(f);
	}

	/**
	 * Converts a string to double value.
	 * 
	 * @param value
	 *            the string
	 * @return the double value of input string
	 * @throws NumberFormatException
	 * can throws this exception
	 */
	public static double stringToDouble(String value)
			throws NumberFormatException {
		value = value.trim();
		if (value.length() == 0) {
			return 0;
		} else {
			return Double.parseDouble(value);
		}
	}

	/**
	 * Converts a double number to string.
	 * 
	 * @param d
	 *            the double value
	 * @return the string representation
	 */
	public static String doubleToString(double d) {
		return Double.toString(d);
	}
}
