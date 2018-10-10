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
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import danfulea.utils.ExampleFileFilter;
import danfulea.math.Convertor;
import danfulea.utils.FrameUtilities;

/**
 * Utility class. Based on pre-computed efficiency (by Monte Carlo method) the sample 
 * activity is calculated. Also, one can perform crude estimation of experimental 
 * efficiency based on known source activity and compare this efficiency with the 
 * theoretical one (based on Monte Carlo simulation). 
 * @author Dan Fulea, 11 Jul. 2005
 *
 */
@SuppressWarnings("serial")
public class EffUtilities extends JFrame {
	private GammaBetaFrame mf;
	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	protected Color fundal = new Color(144, 37, 38, 255);// new
															// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(950, 450);

	protected JTextField effTf = new JTextField(20);
	protected JTextField efffileTf = new JTextField(50);
	protected JTextField efferrTf = new JTextField(20);
	protected JTextField yieldTf = new JTextField(5);
	protected JTextField netTf = new JTextField(10);
	protected JTextField errnetTf = new JTextField(10);
	protected JTextField activTf = new JTextField(20);
	protected JTextField erractivTf = new JTextField(20);

	protected JTextField massVolTf = new JTextField(5);
	protected JTextField unitmassVolTf = new JTextField(5);
	protected JTextField activFTf = new JTextField(20);// final
	protected JTextField erractivFTf = new JTextField(20);// final
	protected JTextField unitFTf = new JTextField(5);

	protected JButton loadB = new JButton();
	protected JButton activB = new JButton();

	protected JTextField net2Tf = new JTextField(10);
	protected JTextField errnet2Tf = new JTextField(10);
	protected JTextField yield2Tf = new JTextField(5);
	protected JTextField activ2Tf = new JTextField(20);
	protected JTextField erractiv2Tf = new JTextField(20);
	protected JTextField eff2Tf = new JTextField(20);
	protected JTextField efferr2Tf = new JTextField(20);
	protected JButton effB = new JButton();

	//private double effload = 0.0;
	//private double erreffload = 0.0;

	/**
	 * Constructor
	 * @param mf, GammaBetaFrame object
	 */
	public EffUtilities(GammaBetaFrame mf) {
		super("Efficiency utilities");
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

	 /**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	private TitledBorder getGroupBoxBorder(String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(LINE_BORDER, title);
		tb.setTitleColor(GammaBetaFrame.foreColor);
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
		//effTf.setOpaque(true);// effTf.setEditable(false);
		//efffileTf.setOpaque(true);
		efffileTf.setEditable(false);
		//efferrTf.setOpaque(true);// efferrTf.setEditable(false);
		//yieldTf.setOpaque(true);
		//netTf.setOpaque(true);
		//errnetTf.setOpaque(true);
		//activTf.setOpaque(true);
		activTf.setEditable(false);
		//erractivTf.setOpaque(true);
		erractivTf.setEditable(false);

		//massVolTf.setOpaque(true);
		//unitmassVolTf.setOpaque(true);
		//activFTf.setOpaque(true);
		activFTf.setEditable(false);
		//erractivFTf.setOpaque(true);
		erractivFTf.setEditable(false);
		//unitFTf.setOpaque(true);
		unitFTf.setEditable(false);

		//loadB.setOpaque(true);
		//activB.setOpaque(true);

		//net2Tf.setOpaque(true);
		//errnet2Tf.setOpaque(true);
		//yield2Tf.setOpaque(true);
		//activ2Tf.setOpaque(true);
		//erractiv2Tf.setOpaque(true);
		//eff2Tf.setOpaque(true);
		eff2Tf.setEditable(false);
		//efferr2Tf.setOpaque(true);
		efferr2Tf.setEditable(false);
		//effB.setOpaque(true);

		loadB.setText("Load efficiency");
		Character mnemonic;
		mnemonic = new Character('L');
		loadB.setMnemonic(mnemonic.charValue());

		activB.setText("Activity calc.");
		effB.setText("Efficiency calc.");
		mnemonic = new Character('A');
		activB.setMnemonic(mnemonic.charValue());
		mnemonic = new Character('E');
		effB.setMnemonic(mnemonic.charValue());

		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel("Efficiency filename");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(efffileTf);
		d1P.add(loadB);
		d1P.setBackground(fundal);
		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Efficiency (norm. at 100 particles):");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(effTf);
		label = new JLabel(" +/- ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.add(efferrTf);
		label = new JLabel(" % ");
		d2P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d2P.setBackground(fundal);
		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Yield (%):");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(yieldTf);
		label = new JLabel(" Counts/s ");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(netTf);
		label = new JLabel(" +/- ");
		d3P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d3P.add(errnetTf);
		d3P.setBackground(fundal);
		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel(" Sample mass (volume) = ");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(massVolTf);
		label = new JLabel(" Units (kg or l): ");
		d4P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d4P.add(unitmassVolTf);
		d4P.add(activB);
		d4P.setBackground(fundal);
		JPanel d5P = new JPanel();
		d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Activity (Bq):");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(activTf);
		label = new JLabel(" +/- (1 sigma unc.)");
		d5P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(erractivTf);
		d5P.setBackground(fundal);
		JPanel d6P = new JPanel();
		d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
		label = new JLabel("Mass(volume) activity: ");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(activFTf);
		label = new JLabel(" +/- (1 sigma unc.)");
		d6P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d6P.add(erractivFTf);
		d6P.add(unitFTf);
		d6P.setBackground(fundal);

		JPanel detP = new JPanel();
		BoxLayout bld = new BoxLayout(detP, BoxLayout.Y_AXIS);
		detP.setLayout(bld);
		detP.add(d1P, null);
		detP.add(d2P, null);
		detP.add(d3P, null);
		detP.add(d4P, null);
		detP.add(d5P, null);
		detP.add(d6P, null);
		detP.setBorder(getGroupBoxBorder("Activity calculator"));
		detP.setBackground(fundal);

		JPanel d21P = new JPanel();
		d21P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Activity(Bq):");
		d21P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d21P.add(activ2Tf);
		label = new JLabel(" +/- ");
		d21P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d21P.add(erractiv2Tf);
		d21P.setBackground(fundal);
		JPanel d31P = new JPanel();
		d31P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Yield (%):");
		d31P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d31P.add(yield2Tf);
		label = new JLabel(" Counts/s ");
		d31P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d31P.add(net2Tf);
		label = new JLabel(" +/- ");
		d31P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d31P.add(errnet2Tf);
		d31P.setBackground(fundal);
		JPanel d41P = new JPanel();
		d41P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d41P.add(effB);
		d41P.setBackground(fundal);
		JPanel d51P = new JPanel();
		d51P.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
		label = new JLabel("Efficiency (norm. at 100 particles):");
		d51P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d51P.add(eff2Tf);
		label = new JLabel(" +/- (1 sigma unc.)");
		d51P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d51P.add(efferr2Tf);
		label = new JLabel(" % ");
		d51P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d51P.setBackground(fundal);

		JPanel det1P = new JPanel();
		BoxLayout bld1 = new BoxLayout(det1P, BoxLayout.Y_AXIS);
		det1P.setLayout(bld1);
		det1P.add(d21P, null);
		det1P.add(d31P, null);
		det1P.add(d41P, null);
		det1P.add(d51P, null);
		det1P.setBorder(getGroupBoxBorder("Efficiency calculator"));
		det1P.setBackground(fundal);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(detP, BorderLayout.NORTH);
		mainP.add(det1P, BorderLayout.SOUTH);

		mainP.setBackground(fundal);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(mainP), BorderLayout.CENTER);
		main2P.setBackground(fundal);

		return main2P;// mainP;

	}

	// END GUI-----------------------------------------------------------------
	/**
	 * All actions are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == loadB) {
					loadEff();
				}

				if (evt.getSource() == activB) {
					performActivCalc();
				}
				if (evt.getSource() == effB) {
					performEffCalc();
				}

			}
		};
		loadB.addActionListener(al);
		activB.addActionListener(al);
		effB.addActionListener(al);
	}

	// #################################
	/**
	 * Perform activity calculation based on theoretical efficiency.
	 */
	private void performActivCalc() {
		boolean neg = false;
		double yield = 0.0;
		double net = 0.0;
		double snet = 0.0;
		double eff = 0.0;
		double seff = 0.0;
		try {
			yield = Convertor.stringToDouble(yieldTf.getText());// 6.3;
			if (yield <= 0)
				neg = true;
			net = Convertor.stringToDouble(netTf.getText());// 7.3;
			if (net <= 0)
				neg = true;
			snet = Convertor.stringToDouble(errnetTf.getText());// 0.05;
			if (snet <= 0)
				neg = true;
			eff = Convertor.stringToDouble(effTf.getText());// 6.3;
			if (eff <= 0)
				neg = true;
			seff = Convertor.stringToDouble(efferrTf.getText());// 6.3;
			if (seff <= 0)
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

		/*
		 * if(effload==0.0) { String title ="Input error"; String message
		 * ="Efficiency must be loaded first!";
		 * JOptionPane.showMessageDialog(null, message, title,
		 * JOptionPane.ERROR_MESSAGE); return; }
		 */

		// double eff=effload/100.;
		eff = eff / 100;
		// double seff=erreffload*eff/100.;
		seff = seff * eff / 100.;//NO!! @MODIF==DIRECTLY FROM GAMMA ANALYSIS!!!!!!
		// double activ=100*100*net/(yield*effload);
		double activ = 100 * net / (yield * eff);
		double sactiv = activ * activ
				* (snet * snet / (net * net) + seff * seff / (eff * eff));
		//sactiv = 2 * Math.sqrt(sactiv);// 2 sigma
		sactiv = Math.sqrt(sactiv);// 1 sigma

		activTf.setText(Convertor.doubleToString(activ));
		erractivTf.setText(Convertor.doubleToString(sactiv));

		String units = unitmassVolTf.getText();
		if (units.compareTo("") == 0)
			units = "Bq/[mass/vol]";
		else
			units = "Bq/" + units;

		// massvold=massVolTf.getText();
		double massvold = 0.0;
		try {
			massvold = Convertor.stringToDouble(massVolTf.getText());
		} catch (Exception ex) {
			return;
		}

		if (massvold != 0.0) {
			activ = activ / massvold;
			sactiv = sactiv / massvold;
			activFTf.setText(Convertor.doubleToString(activ));
			erractivFTf.setText(Convertor.doubleToString(sactiv));

			unitFTf.setText(units);

		}

	}

	/**
	 * Estimate experimental efficiency based on source activity.
	 */
	private void performEffCalc() {
		boolean neg = false;
		double yield = 0.0;
		double net = 0.0;
		double snet = 0.0;
		double activ = 0.0;
		double sactiv = 0.0;
		try {
			yield = Convertor.stringToDouble(yield2Tf.getText());// 6.3;
			if (yield <= 0)
				neg = true;
			net = Convertor.stringToDouble(net2Tf.getText());// 7.3;
			if (net <= 0)
				neg = true;
			snet = Convertor.stringToDouble(errnet2Tf.getText());// 0.05;
			if (snet <= 0)
				neg = true;
			activ = Convertor.stringToDouble(activ2Tf.getText());// 7.3;
			if (activ <= 0)
				neg = true;
			sactiv = Convertor.stringToDouble(erractiv2Tf.getText());// 0.05;
			if (sactiv <= 0)
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

		double eff = 100 * 100 * net / (yield * activ);
		double effp = 100 * net / (yield * activ);
		double seff = effp
				* effp
				* (snet * snet / (net * net) + sactiv * sactiv
						/ (activ * activ));
		//seff = 2 * Math.sqrt(seff);// 2 sigma
		seff = Math.sqrt(seff);// 1 sigma
		seff = 100 * seff / effp;

		eff2Tf.setText(Convertor.doubleToString(eff));
		efferr2Tf.setText(Convertor.doubleToString(seff));

	}

	/**
	 * Load theoretical efficiency from a file.
	 */
	private void loadEff() {
		String ext = "eff";
		String pct = ".";
		String description = "Efficiency file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String datas = "Data";String egsdatas="egs";
		String filename = "";
		String currentDir = System.getProperty("user.dir");
		String file_sep = System.getProperty("file.separator");
		String opens = currentDir + file_sep + datas+file_sep+egsdatas + file_sep + "eff";
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
			efffileTf.setText(filename);
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
							//effload = Convertor.stringToDouble(line);
							effTf.setText(line);
						}
						if (lnr == 2)// s compoz
						{
							//erreffload = Convertor.stringToDouble(line);
							efferrTf.setText(line);
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
