package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import danfulea.utils.ExampleFileFilter;
import danfulea.math.Convertor;
import danfulea.utils.FrameUtilities;

/**
 * Auxiliary class for GammaBetaFrame (a.k.a. GES - Gamma Efficiency Simulator). 
 * It performs basic coincidence correction for pulses.
 * 
 * 
 * @author Dan Fulea, 29 SEP. 2006
 */
@SuppressWarnings("serial")
public class CoinCorr extends JFrame {
	private GammaBetaFrame mf;
	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	protected Color fundal = new Color(144, 37, 38, 255);// new
															// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(980, 550);

	protected JTextField eff1Tf = new JTextField(20);
	protected JTextField efffile1Tf = new JTextField(50);
	protected JTextField efferr1Tf = new JTextField(20);
	protected JTextField eff2Tf = new JTextField(20);
	protected JTextField efffile2Tf = new JTextField(50);
	protected JTextField efferr2Tf = new JTextField(20);

	protected JTextField net1Tf = new JTextField(10);
	protected JTextField errnet1Tf = new JTextField(10);
	protected JTextField net2Tf = new JTextField(10);
	protected JTextField errnet2Tf = new JTextField(10);

	protected JTextArea corTa = new JTextArea();
	protected JButton load1B = new JButton();
	protected JButton load2B = new JButton();

	protected JTextField p12Tf = new JTextField(5);
	protected JButton computeB = new JButton();

	private double eff1load = 0.0;
	//private double erreff1load = 0.0;
	private double eff2load = 0.0;
	//private double erreff2load = 0.0;
	private int loadI = 1;

	/**
	 * Constructor.
	 * @param mf, the GammaBetaFrame object.
	 */
	public CoinCorr(GammaBetaFrame mf) {
		super("Coincidence correction calculator");
		this.mf = mf;
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

		JPanel tabs = createPanel();
		content.add(tabs, BorderLayout.CENTER);

		setContentPane(new JScrollPane(content));
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	//private TitledBorder getGroupBoxBorder(String title) {
	//	TitledBorder tb = BorderFactory.createTitledBorder(LINE_BORDER, title);
	//	tb.setTitleColor(Color.white);
	//	Font fnt = tb.getTitleFont();
	//	Font f = fnt.deriveFont(Font.BOLD);
	//	tb.setTitleFont(f);
	//	return tb;
	//}

	/**
	 * Create main panel
	 * @return the result
	 */
	private JPanel createPanel() {
		//eff1Tf.setOpaque(true);
		eff1Tf.setEditable(false);
		//efffile1Tf.setOpaque(true);
		efffile1Tf.setEditable(false);
		//efferr1Tf.setOpaque(true);
		efferr1Tf.setEditable(false);
		//eff2Tf.setOpaque(true);
		eff2Tf.setEditable(false);
		//efffile2Tf.setOpaque(true);
		efffile2Tf.setEditable(false);
		//efferr2Tf.setOpaque(true);
		efferr2Tf.setEditable(false);

		//net1Tf.setOpaque(true);
		//errnet1Tf.setOpaque(true);
		//net2Tf.setOpaque(true);
		//errnet2Tf.setOpaque(true);

		//p12Tf.setOpaque(true);

		corTa.setCaretPosition(0);
		corTa.setEditable(false);
		corTa.setText("About this method: ");
		//corTa.setLineWrap(true);
		corTa.setWrapStyleWord(true);
		corTa.setBackground(GammaBetaFrame.textAreaBkgColor);// setForeground
		corTa.setForeground(GammaBetaFrame.textAreaForeColor);// setForeground
		// ====================
		String s = " \n";
		s = s
				+ "Computes the true coincidence correction (2 photons emitted in cascade) for the corresponding net counts rate!"
				+ " \n";
		s = s
				+ "Inputs are: total efficiencies (from whole spectrum) for both initial 2 gamma energies,"
				+ " \n";
		s = s
				+ "observed net counts rate in both peaks and its errors, taken from a specific gamma analyser software (e.g. MAB)."
				+ " \n";
		s = s + "Outputs are the corrected net counts rate and its errors."
				+ " \n";
		s = s
				+ "Note that the net counts in one peak is affected by coincidence with signals from any photon of second type (continuum spectrum)"
				+ " \n";
		s = s
				+ "That is the reason for considering TOTAL efficiencies instead of peak efficiencies (these affect only the sum peak counts)"
				+ " \n";
		s = s
				+ "Coincidence for more than 2 photons as well as the false coincidence (resolved time for many systems is very small) can be neglected!"
				+ " \n";
		s = s
				+ "It is very dificult to compute total efficiency directly from spectrum (the 'experimental' way) due to existence of many peaks!"
				+ " \n";
		s = s
				+ "Thus, the Monte-Carlo method for coincidence correction is a very good approach and the results are very accurate!"
				+ " \n";
		corTa.append(s);
		// =====================

		//load1B.setOpaque(true);
		//load2B.setOpaque(true);
		//computeB.setOpaque(true);

		load1B.setText("Load 1st tot. efficiency");
		Character mnemonic;
		mnemonic = new Character('L');
		load1B.setMnemonic(mnemonic.charValue());

		load2B.setText("Load 2nd tot. efficiency");
		mnemonic = new Character('o');
		load2B.setMnemonic(mnemonic.charValue());

		computeB.setText("Compute");
		mnemonic = new Character('C');
		computeB.setMnemonic(mnemonic.charValue());
		// -=====================================================================
		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel("Filename");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(efffile1Tf);
		d1P.add(load1B);
		d1P.setBackground(fundal);

		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("1st total efficiency (norm. at 100 particles):");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(eff1Tf);
		label = new JLabel(" +/- ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(efferr1Tf);
		label = new JLabel(" % ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.setBackground(fundal);

		JPanel d11P = new JPanel();
		d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Filename");
		d11P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(efffile2Tf);
		d11P.add(load2B);
		d11P.setBackground(fundal);

		JPanel d22P = new JPanel();
		d22P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("2nd total efficiency (norm. at 100 particles):");
		d22P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d22P.add(eff2Tf);
		label = new JLabel(" +/- ");
		d22P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d22P.add(efferr2Tf);
		label = new JLabel(" % ");
		d22P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d22P.setBackground(fundal);

		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(" 1st Counts/s ");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(net1Tf);
		label = new JLabel(" +/- ");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(errnet1Tf);
		d3P.setBackground(fundal);

		JPanel d33P = new JPanel();
		d33P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(" 2nd Counts/s ");
		d33P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d33P.add(net2Tf);
		label = new JLabel(" +/- ");
		d33P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d33P.add(errnet2Tf);
		d33P.setBackground(fundal);

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(" Cascade probability [0,1]: ");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(p12Tf);
		p12Tf.setText("1");
		d4P.add(computeB);
		d4P.setBackground(fundal);

		JPanel resultP = new JPanel(new BorderLayout());
		JScrollPane jspres = new JScrollPane();
		jspres.getViewport().add(corTa, null);
		resultP.add(jspres, BorderLayout.CENTER);
		resultP.setBackground(fundal);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d1P, null);
		detP.add(d2P, null);
		detP.add(d11P, null);
		detP.add(d22P, null);
		detP.add(d3P, null);
		detP.add(d33P, null);
		detP.add(d4P, null);
		// detP.setBorder(getGroupBoxBorder("Activity calculator"));
		detP.setBackground(fundal);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(detP), BorderLayout.CENTER);
		main2P.setBackground(fundal);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(main2P, BorderLayout.NORTH);// (detP,BorderLayout.NORTH);
		mainP.add(resultP, BorderLayout.CENTER);

		mainP.setBackground(fundal);
		return mainP;

	}

	// END GUI-----------------------------------------------------------------
	/**
	 * All actions are defined here
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == load1B || evt.getSource() == load2B) {
					if (evt.getSource() == load1B)
						loadI = 1;
					if (evt.getSource() == load2B)
						loadI = 2;

					loadEff();
				}

				if (evt.getSource() == computeB) {
					performCoinCorr();
				}

			}
		};
		load1B.addActionListener(al);
		load2B.addActionListener(al);
		computeB.addActionListener(al);
	}

	// #################################
	/**
	 * Perform coincidence correction.
	 */
	private void performCoinCorr() {

		boolean neg = false;

		double net1 = 0.0;
		double snet1 = 0.0;
		double net2 = 0.0;
		double snet2 = 0.0;
		double p12 = 0.0;

		try {
			p12 = Convertor.stringToDouble(p12Tf.getText());// 7.3;
			if (p12 <= 0)
				neg = true;

			net1 = Convertor.stringToDouble(net1Tf.getText());// 7.3;
			if (net1 <= 0)
				neg = true;
			snet1 = Convertor.stringToDouble(errnet1Tf.getText());// 0.05;
			if (snet1 <= 0)
				neg = true;
			net2 = Convertor.stringToDouble(net2Tf.getText());// 7.3;
			if (net2 <= 0)
				neg = true;
			snet2 = Convertor.stringToDouble(errnet2Tf.getText());// 0.05;
			if (snet2 <= 0)
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

		if (eff1load == 0.0 || eff2load == 0.0) {
			String title = "Input error";
			String message = "Not null efficiencies must be loaded first!";
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		double eff1 = eff1load / 100.;
		//double seff1 = erreff1load * eff1 / 100.;
		double eff2 = eff2load / 100.;
		//double seff2 = erreff2load * eff1 / 100.;

		double c1 = 1 / (1 - p12 * eff2);
		double c2 = 1 / (1 - p12 * eff1);

		double n1cor = c1 * net1;
		double n2cor = c2 * net2;
		double en1cor = c1 * snet1;
		double en2cor = c2 * snet2;

		corTa.selectAll();
		corTa.replaceSelection("");

		String s = "";
		s = s + "Corrected net counts rate 1 [imp/s]= " + n1cor + " +/- "
				+ en1cor + " \n";
		s = s + "Corrected net counts rate 2 [imp/s]= " + n2cor + " +/- "
				+ en2cor + " \n";
		s = s + " \n";
		s = s
				+ "Select and copy/paste these results in a text file for later use (e.g. in efficiency utilities)"
				+ " \n";
		corTa.append(s);
	}

	/**
	 * Load efficiency from a file
	 */
	private void loadEff() {
		String ext = "eff";
		String pct = ".";
		String description = "Efficiency file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = "Data";String egsDatas="egs";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsDatas + file_sep + "eff";
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
			if (loadI == 1)
				efffile1Tf.setText(filename);
			if (loadI == 2)
				efffile2Tf.setText(filename);

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
						if (lnr == 3) {
							if (loadI == 1) {
								eff1load = Convertor.stringToDouble(line);
								eff1Tf.setText(line);
							}
							if (loadI == 2) {
								eff2load = Convertor.stringToDouble(line);
								eff2Tf.setText(line);
							}

						}

						if (lnr == 4) {
							if (loadI == 1) {
								//erreff1load = Convertor.stringToDouble(line);
								efferr1Tf.setText(line);
							}
							if (loadI == 2) {
								//erreff2load = Convertor.stringToDouble(line);
								efferr2Tf.setText(line);
							}

						}

						desc = new StringBuffer();

					}
				}
				in.close();
			} catch (Exception e) {

			}
		}
	}

}
