package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import danfulea.utils.ExampleFileFilter;
import danfulea.math.Convertor;
import danfulea.math.numerical.LinAEq;
import danfulea.utils.FrameUtilities;
import danfulea.utils.ListUtilities;


/**
 * Class for basic nuclide interference correction. 
 * 
 * @author Dan Fulea, 15 IAN. 2006
 */
@SuppressWarnings("serial")
public class Interf extends JFrame {
	private GammaBetaFrame mf;
	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	protected Color fundal = new Color(144, 37, 38, 255);// new
	// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(900, 600);

	private NumberFormat nf = NumberFormat.getInstance(Locale.US);
	protected JButton addcoefB = new JButton();
	protected JButton resetcoefB = new JButton();
	protected JButton addtlB = new JButton();
	protected JButton resettlB = new JButton();
	protected JButton loadB = new JButton();
	protected JButton saveB = new JButton();
	protected JButton calcB = new JButton();

	protected JTextArea resTa = new JTextArea();

	@SuppressWarnings("rawtypes")
	protected DefaultListModel coefdlm = new DefaultListModel();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected JList coefL = new JList(coefdlm);
	@SuppressWarnings("rawtypes")
	protected DefaultListModel tldlm = new DefaultListModel();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected JList tlL = new JList(tldlm);

	protected JTextField nTf = new JTextField(5);// n=3 for Th,U,K
	protected JTextField aijTf = new JTextField(5);
	protected JTextField tlTf = new JTextField(5);
	protected JTextField errtlTf = new JTextField(5);
	protected JTextField filenameTf = new JTextField(25);
	// ---reprod main variables
	private int nNuc = 0;// number of nuclides
	protected double[][] coef = new double[nNuc][nNuc];// [nNuc][nNuc],
														// coeficients
	protected double[][] tl = new double[nNuc][1];// [nNuc][1],free terms
	protected double[] errtl = new double[nNuc];// [nNuc][1],free terms
	private boolean aijfirsttimeB = true;
	private boolean tlfirsttimeB = true;
	private int icoef = 1;// 1->nNuc
	private int jcoef = 1;// 1->nNuc
	private boolean stopCoef = false;
	private int itl = 1;// 1->nNuc
	private boolean stopTl = false;

	/**
	 * Constructor.
	 * @param mf GammaBetaFrame object
	 */
	public Interf(GammaBetaFrame mf) {
		super("Nuclide interferences");
		this.mf = mf;
		fundal = GammaBetaFrame.bkgColor;
		createGUI();
		initEvents();// main event
		resetCoefData();
		resetTlData();

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
		InterfGUI rag = new InterfGUI(this);
		JPanel racTp = rag.createMainPanel();
		// /////////////////////////////////////////////////
		content.add(racTp, BorderLayout.CENTER);

		setContentPane(new JScrollPane(content));
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	/**
	 * All actions are defined here.
	 */
	private void initEvents() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == aijTf || evt.getSource() == addcoefB) {
					addCoefInList();
				}

				if (evt.getSource() == tlTf || evt.getSource() == addtlB) {
					addTlInList();
				}

				if (evt.getSource() == resetcoefB) {
					resetCoefData();
				}

				if (evt.getSource() == resettlB) {
					resetTlData();
				}

				if (evt.getSource() == calcB) {
					performInterfCalc();
				}
				// LOAD, SAVE
				if (evt.getSource() == saveB) {
					saveCoef();
				}
				if (evt.getSource() == loadB) {
					loadCoef();
				}
			}
		};
		aijTf.addActionListener(al);
		addcoefB.addActionListener(al);
		tlTf.addActionListener(al);
		addtlB.addActionListener(al);
		resetcoefB.addActionListener(al);
		resettlB.addActionListener(al);
		calcB.addActionListener(al);
		saveB.addActionListener(al);
		loadB.addActionListener(al);
	}

	/**
	 * Perform nuclide interference correction.
	 */
	private void performInterfCalc() {
		String s = "";
		// -----------------------------------
		int n = coefdlm.getSize(); // resTa.append("  "+n+" \n");
		int n2 = tldlm.getSize();
		// -----------------------------------
		if (nNuc <= 1 || n != nNuc * nNuc || n2 != nNuc) {
			s = "Please fill in correctly the coefficients and free terms !"
					+ " \n";
			resTa.append(s);
			return;
		}

		double[][] c = new double[nNuc][nNuc];
		double[][] t = new double[nNuc][1];
		for (int i = 1; i <= nNuc; i++) {
			for (int j = 1; j <= nNuc; j++) {
				c[i - 1][j - 1] = coef[i - 1][j - 1];
			}

			t[i - 1][0] = tl[i - 1][0];
		}

		// Equations.sysEqGauss(coef,tl,nNuc,1);
		//Equations.sysEqGauss(c, t, nNuc, 1);
		LinAEq.sysEqGauss(c, t, nNuc, 1);

		resTa.selectAll();
		resTa.replaceSelection("");

		for (int i = 1; i <= nNuc; i++) {
			resTa.append("Corrected counts for nuclide: " + i + " = "
					+ nf.format(t[i - 1][0]) + " +/- "
					+ nf.format(errtl[i - 1] * t[i - 1][0] / 100) + " \n");
		}
	}

	/**
	 * Reset coefficients
	 */
	private void resetCoefData() {
		coef = new double[0][0];
		nNuc = 0;

		ListUtilities.removeAll(coefdlm);
		// nNuc=0;
		aijTf.setText("");
		aijTf.requestFocusInWindow();
		aijfirsttimeB = true;
		nTf.setEditable(true);
		icoef = 1;
		jcoef = 1;
		stopCoef = false;
	}

	/**
	 * Reset free terms data (free terms means right hand side parameters in an equation system)
	 */
	private void resetTlData() {
		tl = new double[0][1];
		errtl = new double[0];
		nNuc = 0;

		ListUtilities.removeAll(tldlm);

		tlTf.setText("");
		tlTf.requestFocusInWindow();
		tlfirsttimeB = true;
		nTf.setEditable(true);
		stopTl = false;
		itl = 1;
	}

	/**
	 * Add coefficients in list.
	 */
	private void addCoefInList() {
		if (stopCoef) {
			return;
		}
		String s = "";
		double d = 0.0;
		if (aijfirsttimeB) {
			try {
				nNuc = Convertor.stringToInt(nTf.getText());
			} catch (Exception ex) {
				s = "Nuclides number must be a positive, bigger than 1 integer!"
						+ " \n";
				resTa.append(s);
				return;
			}

			if (nNuc <= 1) {
				s = "Nuclides number must be a positive, bigger than 1 integer!"
						+ " \n";
				resTa.append(s);
				return;
			}
			// ok
			nTf.setEditable(false);
			coef = new double[nNuc][nNuc];
		}
		aijfirsttimeB = false;
		// ------------------------
		try {
			d = Convertor.stringToDouble(aijTf.getText());
		} catch (Exception ex) {
			s = "Counts number must be a positive (or null) number!" + " \n";
			resTa.append(s);
			resetCoefData();
			return;
		}

		if (d < 0.) {
			s = "Counts number must be a positive (or null) number!" + " \n";
			resTa.append(s);
			resetCoefData();
			return;
		}

		// ------------------------
		if (jcoef != nNuc + 1) {
			coef[icoef - 1][jcoef - 1] = d; // System.out.println(" i: "+icoef+" j: "+jcoef+" value: "+d);
			// lst--------------------------
			ListUtilities.add(" i: " + icoef + " j: " + jcoef + " value: " + d,
					coefdlm);
			// -----------------------------
			jcoef++;
			if ((icoef == nNuc) && (jcoef == nNuc + 1)) {
				stopCoef = true;
			}

		} else {
			icoef++;
			jcoef = 1;
			coef[icoef - 1][jcoef - 1] = d; // System.out.println(" i: "+icoef+" j: "+jcoef+" value: "+d);
			// -----------------------
			ListUtilities.add(" i: " + icoef + " j: " + jcoef + " value: " + d,
					coefdlm);
			// ------------------------
			jcoef++;
		}

		aijTf.setText("");
		aijTf.requestFocusInWindow();
	}

	/**
	 * Add free terms (i.e. counts) in list.
	 */
	private void addTlInList() {
		if (stopTl) {
			return;
		}
		String s = "";
		double d = 0.0;
		double errd = 0.0;
		if (tlfirsttimeB) {
			try {
				nNuc = Convertor.stringToInt(nTf.getText());
			} catch (Exception ex) {
				s = "Nuclides number must be a positive, bigger than 1 integer!"
						+ " \n";
				resTa.append(s);
				return;
			}

			if (nNuc <= 1) {
				s = "Nuclides number must be a positive, bigger than 1 integer!"
						+ " \n";
				resTa.append(s);
				return;
			}
			// ok
			nTf.setEditable(false);
			tl = new double[nNuc][1];
			errtl = new double[nNuc];
		}
		tlfirsttimeB = false;
		// ------------------------
		try {
			d = Convertor.stringToDouble(tlTf.getText());
			errd = Convertor.stringToDouble(errtlTf.getText());
		} catch (Exception ex) {
			s = "Counts/error number must be a positive (or null) number!"
					+ " \n";
			resTa.append(s);
			resetCoefData();
			return;
		}

		if (d < 0. || errd < 0.) {
			s = "Counts/error number must be a positive (or null) number!"
					+ " \n";
			resTa.append(s);
			resetCoefData();
			return;
		}

		// ------------------------
		// if (itl!=nNuc+1)
		// {
		tl[itl - 1][0] = d;
		if (d != 0.)
			errd = 100 * errd / d;
		else
			errd = 0.0;
		errtl[itl - 1] = errd;
		// lst--------------------------
		ListUtilities.add(
				" i: " + itl + " value: " + d + " +/- " + nf.format(errd)
						+ " % ", tldlm);
		// -----------------------------
		itl++;
		if ((itl == nNuc + 1)) {
			stopTl = true;
		}

		// }
		tlTf.setText("");
		errtlTf.setText("");
		tlTf.requestFocusInWindow();
	}

	/**
	 * Save coefficients to a file
	 */
	private void saveCoef() {
		String ss = "";
		int n = coefdlm.getSize(); // resTa.append("  "+n+" \n");
		if (nNuc <= 1 || n != nNuc * nNuc) {
			ss = "Please fill in the coefficients!" + " \n";
			resTa.append(ss);
			return;
		}

		String ext = "interf";
		String pct = ".";
		String description = "interference coefficients file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = "Data";String egsDatas = "egs";
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
			String test = filename.substring(fl - 7);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;

			// System.out.println(filename);
			String s = "";
			s = nNuc + "\n";
			for (int i = 1; i <= nNuc; i++) {
				for (int j = 1; j <= nNuc; j++) {
					s = s + coef[i - 1][j - 1] + "\n";
				}
			}

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			} catch (Exception ex) {

			}

		}

	}

	/**
	 * Load interference coefficients.
	 */
	private void loadCoef() {
		String ext = "interf";
		String pct = ".";
		String description = "interference coefficients file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = "Data";String egsDatas="egs";
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
		int ic = 1;
		int jc = 1;
		// --------------
		int returnVal = chooser.showOpenDialog(this);// parent=this frame
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// filename= chooser.getSelectedFile().toString()+pct+ext;
			filename = chooser.getSelectedFile().toString();
			int fl = filename.length();
			String test = filename.substring(fl - 7);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) != 0)
				filename = chooser.getSelectedFile().toString() + pct + ext;

			try {
				FileInputStream in = new FileInputStream(filename);
				filenameTf.setText(filename);
				filenameTf.setToolTipText(filename);
				while ((i = in.read()) != -1) {
					if ((char) i != lineSep) {
						desc.append((char) i);
					} else {
						lnr++;
						line = desc.toString();
						if (lnr == 1)// s type
						{
							// reset first

							resetCoefData();
							// resetTlData();

							nTf.setText(line);
							nTf.setEditable(false);
							nNuc = Convertor.stringToInt(line);
							coef = new double[nNuc][nNuc];
							aijfirsttimeB = false;
							stopCoef = false;
						}
						if (lnr > 1)// s compoz
						{
							double d = Convertor.stringToDouble(line);
							if (jc != nNuc + 1) {
								coef[ic - 1][jc - 1] = d;
								ListUtilities.add(" i: " + ic + " j: " + jc
										+ " value: " + d, coefdlm);

								jc++;
								if ((ic == nNuc) && (jc == nNuc + 1)) {
									stopCoef = true;
								}
							} else {
								ic++;
								jc = 1;
								coef[ic - 1][jc - 1] = d; // System.out.println(" i: "+icoef+" j: "+jcoef+" value: "+d);
								// -----------------------
								ListUtilities.add(" i: " + ic + " j: " + jc
										+ " value: " + d, coefdlm);
								// ------------------------
								jc++;
							}
						}

						desc = new StringBuffer();

					}
				}
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
