package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Auxiliary class for Interf. It handles GUI controls. 
 * 
 * @author Dan Fulea, 15 IAN. 2006
 */

public class InterfGUI {
	private Interf ra;

	private static final Dimension sizeLst = new Dimension(253, 125);
	//private JScrollPane listSp = new JScrollPane();

	// Construct the frame
	/**
	 * Constructor
	 * @param ra Interf object
	 */
	public InterfGUI(Interf ra) {
		this.ra = ra;
		initComponents();
		//opacity();
	}

	

	// Component initialization
	/**
	 * Initialize components.
	 */
	private void initComponents() {
		// titledBorder1 = new
		// TitledBorder(BorderFactory.createLineBorder(Color.black,2),
		// ra.resources.getString("kvp.border2"));

		// kvP.setBorder(BorderFactory.createTitledBorder(ra.LINE_BORDER,
		// ra.resources.getString("kvp.border")));

		ra.addcoefB.setText("Add coef.");
		Character mnemonic;
		mnemonic = new Character('a');
		ra.addcoefB.setMnemonic(mnemonic.charValue());

		ra.calcB.setText("Compute");
		mnemonic = new Character('c');
		ra.calcB.setMnemonic(mnemonic.charValue());

		ra.addtlB.setText("Add counts");
		mnemonic = new Character('d');
		ra.addtlB.setMnemonic(mnemonic.charValue());

		ra.resetcoefB.setText("Reset coef.");
		mnemonic = new Character('r');
		ra.resetcoefB.setMnemonic(mnemonic.charValue());

		ra.resettlB.setText("Reset counts");
		mnemonic = new Character('e');
		ra.resettlB.setMnemonic(mnemonic.charValue());

		ra.saveB.setText("Save coef...");
		mnemonic = new Character('s');
		ra.saveB.setMnemonic(mnemonic.charValue());

		ra.loadB.setText("Load coef...");
		mnemonic = new Character('l');
		ra.loadB.setMnemonic(mnemonic.charValue());

		ra.resTa.setCaretPosition(0);
		ra.resTa.setEditable(false);
		ra.resTa.setLineWrap(true);
		ra.resTa.setWrapStyleWord(true);
		ra.resTa.setBackground(GammaBetaFrame.textAreaBkgColor);// setForeground
		ra.resTa.setForeground(GammaBetaFrame.textAreaForeColor);// setForeground

		ra.nTf.setText("3");
		// ====================================================
		String tools = "aij=Nij/Njj; Nij=counts in 'i' band from 'j' nuclide. Taken from etalon source!";
		ra.aijTf.setToolTipText(tools);
		tools = "Select text and then press CTRL+C to copy!";
		ra.resTa.setToolTipText(tools);
	}

	//private TitledBorder getGroupBoxBorder(String title) {
	//	return BorderFactory.createTitledBorder(ra.LINE_BORDER, title);
	//}

	/**
	   * Create main panel
	   * @return the result
	   */
	protected JPanel createMainPanel() {
		JPanel d1P = new JPanel();
		d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		JLabel label = new JLabel("Nuclides number: ");
		d1P.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		d1P.add(ra.nTf);
		d1P.setBackground(ra.fundal);
		// --------------------------------------
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Coeficients [Aij]: ");
		p1.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		p1.add(ra.aijTf);
		p1.setBackground(ra.fundal);
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		p2.add(ra.addcoefB);
		p2.add(ra.resetcoefB);
		p2.setBackground(ra.fundal);
		JPanel coefP = new JPanel();
		BoxLayout bl0 = new BoxLayout(coefP, BoxLayout.Y_AXIS);
		coefP.setLayout(bl0);
		coefP.add(p1);
		coefP.add(p2);
		coefP.setBackground(ra.fundal);
		// ---------------------------------
		JPanel lstcoefP = new JPanel();
		JScrollPane coefSp = new JScrollPane();
		lstcoefP.setLayout(new BorderLayout());// borderLayout2);
		lstcoefP.add(coefSp, BorderLayout.CENTER);
		coefSp.getViewport().add(ra.coefL, null);
		coefSp.setPreferredSize(sizeLst);
		lstcoefP.setBackground(ra.fundal);
		// -------------------------------
		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		p3.add(ra.saveB);
		p3.setBackground(ra.fundal);
		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		p4.add(ra.loadB);
		p4.setBackground(ra.fundal);
		JPanel butcoefP = new JPanel();
		BoxLayout bl1 = new BoxLayout(butcoefP, BoxLayout.Y_AXIS);
		butcoefP.setLayout(bl1);
		label = new JLabel("Filename: ");
		butcoefP.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		butcoefP.add(ra.filenameTf);
		butcoefP.add(p3);
		butcoefP.add(p4);
		butcoefP.setBackground(ra.fundal);

		JPanel d2P = new JPanel();
		d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d2P.add(coefP);
		d2P.add(lstcoefP);
		d2P.add(butcoefP);
		d2P.setBackground(ra.fundal);

		JPanel p11 = new JPanel();
		p11.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		label = new JLabel("Counts: ");
		p11.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		p11.add(ra.tlTf);
		label = new JLabel(" +/- ");
		p11.add(label);
		label.setForeground(GammaBetaFrame.foreColor);
		p11.add(ra.errtlTf);
		p11.setBackground(ra.fundal);
		JPanel p21 = new JPanel();
		p21.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		p21.add(ra.addtlB);
		p21.add(ra.resettlB);
		p21.setBackground(ra.fundal);
		JPanel tlP = new JPanel();
		BoxLayout bl3 = new BoxLayout(tlP, BoxLayout.Y_AXIS);
		tlP.setLayout(bl3);
		tlP.add(p11);
		tlP.add(p21);
		tlP.setBackground(ra.fundal);
		// ---------------------------------
		JPanel lsttlP = new JPanel();
		JScrollPane tlSp = new JScrollPane();
		lsttlP.setLayout(new BorderLayout());// borderLayout2);
		lsttlP.add(tlSp, BorderLayout.CENTER);
		tlSp.getViewport().add(ra.tlL, null);
		tlSp.setPreferredSize(sizeLst);
		lsttlP.setBackground(ra.fundal);
		// -------------------------------

		JPanel d3P = new JPanel();
		d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d3P.add(tlP);
		d3P.add(lsttlP);
		d3P.setBackground(ra.fundal);

		JPanel d4P = new JPanel();
		d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
		d4P.add(ra.calcB);
		d4P.setBackground(ra.fundal);

		JPanel taP = new JPanel();
		taP.setLayout(new BorderLayout());
		JScrollPane taSp = new JScrollPane();
		taSp.getViewport().add(ra.resTa, null);
		taP.add(taSp, BorderLayout.CENTER);
		taP.setBackground(ra.fundal);

		JPanel northP = new JPanel();
		BoxLayout bl2 = new BoxLayout(northP, BoxLayout.Y_AXIS);
		northP.setLayout(bl2);
		northP.add(d1P);
		northP.add(d2P);
		northP.add(d3P);
		northP.add(d4P);
		northP.setBackground(ra.fundal);

		JPanel main2P = new JPanel(new BorderLayout());
		main2P.add(new JScrollPane(northP), BorderLayout.CENTER);
		main2P.setBackground(ra.fundal);

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.add(main2P, BorderLayout.NORTH);// (northP,BorderLayout.NORTH);
		mainP.add(taP, BorderLayout.CENTER);

		mainP.setBackground(ra.fundal);
		return mainP;

	}
}
