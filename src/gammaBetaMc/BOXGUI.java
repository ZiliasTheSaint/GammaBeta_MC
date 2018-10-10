package gammaBetaMc;

import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * Auxiliary class for BoxApp. It creates GUI controls.
 
 * @author Dan Fulea, 11 Jul. 2005
*/

public class BOXGUI {
	private BoxApp ds;
	//  private static final Dimension sizeCb = new Dimension(50, 21);
	  private static final Dimension sizeTxtCb = new Dimension(200, 21);
	//  private static final Dimension sizeTxtCb2 = new Dimension(200, 21);
	 // private static final Dimension dimP= new Dimension(200, 80);
	//  private static final Dimension dimPp= new Dimension(200, 140);
	  private static final Dimension textAreaDimension = new Dimension(900, 400);

	  //Construct the frame
	  /**
	   * Constructor.
	   * @param ds, the BoxApp object
	   */
	  public BOXGUI(BoxApp ds)
	  {
		this.ds=ds;
		initComponents();
		//opacity();
	  }
	  
	//Component initialization
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	  /**
	   * Initialize components.
	   */
	private void initComponents()
	  {
		  String tools="";
		  //####################################################
	    ds.startsimB.setText(ds.resources.getString("main.startsimB"));
	    ds.printB.setText("PDF Print result");
	    ds.printB.setMnemonic(new Character('P'));
	    Character mnemonic;
	    mnemonic=(Character) ds.resources.getObject("main.startsimB.mnemonic");
	    ds.startsimB.setMnemonic(mnemonic.charValue());
	    ds.stopsimB.setText(ds.resources.getString("main.stopsimB"));
	    mnemonic=(Character) ds.resources.getObject("main.stopsimB.mnemonic");
	    ds.stopsimB.setMnemonic(mnemonic.charValue());
		ds.saveSB.setText(ds.resources.getString("main.saveSB"));
		mnemonic=(Character) ds.resources.getObject("main.saveSB.mnemonic");
		ds.saveSB.setMnemonic(mnemonic.charValue());
		ds.saveDB.setText(ds.resources.getString("main.saveDB"));
		mnemonic=(Character) ds.resources.getObject("main.saveDB.mnemonic");
		ds.saveDB.setMnemonic(mnemonic.charValue());
		ds.loadSB.setText(ds.resources.getString("main.loadSB"));
		mnemonic=(Character) ds.resources.getObject("main.loadSB.mnemonic");
		ds.loadSB.setMnemonic(mnemonic.charValue());
		ds.loadDB.setText(ds.resources.getString("main.loadDB"));
		mnemonic=(Character) ds.resources.getObject("main.loadDB.mnemonic");
		ds.loadDB.setMnemonic(mnemonic.charValue());
		String[] photonnr = (String[])ds.resources.getObject("main.nPhotonsCb");
		ds.nPhotonsCb=new JComboBox(photonnr);
	    String s=photonnr[1];//20000
	    ds.nPhotonsCb.setSelectedItem((Object)s);
		ds.nPhotonsCb.setMaximumRowCount(5);
		ds.nPhotonsCb.setPreferredSize(sizeTxtCb);

		String[] sType = (String[])ds.resources.getObject("main.sourceTypeCb");
		ds.sourceTypeCb=new JComboBox(sType);
	    s=sType[1];//cilindru
	    ds.sourceTypeCb.setSelectedItem((Object)s);
		ds.sourceTypeCb.setMaximumRowCount(5);
		ds.sourceTypeCb.setPreferredSize(sizeTxtCb);

		tools="Incident electron kinetic threshold energy, below no counts are detected in detector.";
		ds.ethreshTf.setToolTipText(tools);
		ds.ethreshTf.setText("0.00");

		String[] sTy7 = {"Monoenergetic","Spectrum"};
		ds.einCb=new JComboBox(sTy7);
		s=sTy7[0];
	    ds.einCb.setSelectedItem((Object)s);
		ds.einCb.setMaximumRowCount(5);
		ds.einCb.setPreferredSize(sizeTxtCb);

	    ds.simTa.setCaretPosition(0);
	    ds.simTa.setEditable(false);
	    //ds.simTa.setText(ds.resources.getString("rezultat"));
	    //ds.simTa.setLineWrap(true);
	    ds.simTa.setWrapStyleWord(true);
	    ds.simTa.setFocusable(false);//THE KEY TO AVOID THREAD BUG WHEN SELECT TEXTAREA!!!!
	    
		ds.simTa.setBackground(GammaBetaFrame.textAreaBkgColor);//setForeground
		ds.simTa.setForeground(GammaBetaFrame.textAreaForeColor);//setForeground

	  	ds.XBOUNDTf.setText("12.0");
	  	ds.YBOUNDTf.setText("12.0");
	  	ds.ZBOUNDTf.setText("12.0");
	  	ds.XDETTf.setText("6.0");
	  	ds.YDETTf.setText("6.0");
		ds.XDETCENTERTf.setText("0.0");
	  	ds.YDETCENTERTf.setText("0.0");

	  	ds.energyTf.setText("2.662");

	  	ds.sddistTf.setText("6.2");
		ds.sddistTf.setToolTipText("Point source only!");
		ds.sddistTf.setEnabled(false);

		//ds.simTa.setToolTipText(ds.resources.getString("simTa.toolTip"));

		ds.airmaterialTf.setText("air_dry_nearsealevel");ds.airmaterialTf.setEnabled(false);
		ds.airchooseB.setText(" ..... ");

		ds.spectrumB.setText(" ..... ");ds.spectrumB.setEnabled(false);
		ds.spectrumTf.setEnabled(false);

		ds.electronRb=new JRadioButton("electron");
		ds.positronRb=new JRadioButton("positron");

	  }

	  /**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	  private TitledBorder getGroupBoxBorder(String title)
	  {
		  TitledBorder tb =BorderFactory.createTitledBorder(BoxApp.LINE_BORDER,title);
		  tb.setTitleColor(GammaBetaFrame.foreColor);
		  Font fnt=tb.getTitleFont();
		  Font f=fnt.deriveFont(Font.BOLD);
		  tb.setTitleFont(f);
	      return	tb;//BorderFactory.createTitledBorder(ds.LINE_BORDER,title);
	  }

	  /**
	   * Create main panel
	   * @return the result
	   */
	  protected JPanel createSimMainPanel()
	  {
		//-----------------------------------------------------------------------------------
			// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(ds.electronRb);
		group.add(ds.positronRb);
	    //Put the radio buttons in a column in a panel.
	    JPanel buttP = new JPanel();
		buttP.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
		JLabel lbl=new JLabel("Incident particle ");
		lbl.setForeground(GammaBetaFrame.foreColor);
		buttP.add(lbl);
		buttP.add(ds.electronRb);
		buttP.add(ds.positronRb);
		ds.electronRb.setBackground(ds.fundal);ds.electronRb.setForeground(GammaBetaFrame.foreColor);
		ds.positronRb.setBackground(ds.fundal);ds.positronRb.setForeground(GammaBetaFrame.foreColor);
		buttP.setBackground(ds.fundal);
		ds.electronRb.setSelected(true);
		//-----------------------------------------------------------------------------------

	    JPanel d1P=new JPanel();
	    d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    JLabel label=new JLabel(ds.resources.getString("photon.label"));
	    d1P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d1P.add(ds.nPhotonsCb);
	    d1P.setBackground(ds.fundal);

	    JPanel d26P=new JPanel();
	    d26P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel("Ekin_THRESHOLD ");
	    d26P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d26P.add(ds.ethreshTf);
	    d26P.setBackground(ds.fundal);
	//---------det---------------------------
	    JPanel d2P=new JPanel();
	    d2P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel(" X Detector [cm] = ");
	    d2P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d2P.add(ds.XDETTf);
	    label=new JLabel(" Y Detector [cm] = ");
	    d2P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d2P.add(ds.YDETTf);
	    d2P.setBackground(ds.fundal);

	    JPanel d4P=new JPanel();
	    d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 10,2));
	    label=new JLabel(" Relative to BOX center: X center detector [cm] = ");
	    d4P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d4P.add(ds.XDETCENTERTf);
	    label=new JLabel(" Relative to BOX center: Y center detector [cm] = ");
	    d4P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d4P.add(ds.YDETCENTERTf);
	    d4P.setBackground(ds.fundal);

	    JPanel d6P=new JPanel();
	    d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel("BOX filled with: ");
	    d6P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d6P.add(ds.airmaterialTf);
	    d6P.add(ds.airchooseB);
	    d6P.setBackground(ds.fundal);

	    JPanel d23P=new JPanel();
	    d23P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    d23P.add(ds.saveDB);
	    d23P.add(ds.loadDB);
	    d23P.setBackground(ds.fundal);

	    JPanel detP=new JPanel();
	    BoxLayout bld = new BoxLayout(detP,BoxLayout.Y_AXIS);
	    detP.setLayout(bld);
	    detP.add(d2P, null);
	    detP.add(d4P, null);
	    //detP.add(d6P, null);
	    detP.add(d23P, null);
		detP.setBorder(getGroupBoxBorder(ds.resources.getString("detector.border")));
	//-------------source---------------------------------------
	    JPanel d3P=new JPanel();
	    d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel(ds.resources.getString("sourcetype.label"));
	    d3P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d3P.add(ds.sourceTypeCb);
	    d3P.setBackground(ds.fundal);

	    JPanel d55P=new JPanel();
	    d55P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel("Point source to detector distance (cm)");
	    d55P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d55P.add(ds.sddistTf);
	    d55P.setBackground(ds.fundal);

	    JPanel d5P=new JPanel();
	    d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    label=new JLabel(" X BOX [cm] = ");
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d5P.add(ds.XBOUNDTf);
	    //----------------------------------------------------
	    label=new JLabel(" Y BOX [cm] = ");
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d5P.add(ds.YBOUNDTf);
	    //----------------------------------------------------
	    label=new JLabel(" Z BOX [cm] = ");
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d5P.add(ds.ZBOUNDTf);
	    d5P.setBackground(ds.fundal);

	    JPanel d8P=new JPanel();
	    d8P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    d8P.add(ds.saveSB);
	    d8P.add(ds.loadSB);
	    d8P.setBackground(ds.fundal);

	    JPanel sourceP=new JPanel();
	    BoxLayout bls = new BoxLayout(sourceP,BoxLayout.Y_AXIS);
	    sourceP.setLayout(bls);
	    sourceP.add(buttP, null);
	    sourceP.add(d3P, null);
	    sourceP.add(d55P, null);
	    sourceP.add(d5P, null);
	    sourceP.add(d6P, null);
	    sourceP.add(d8P, null);
		sourceP.setBorder(getGroupBoxBorder(ds.resources.getString("source.border")));

	//-------------------------------------------------
	    JPanel d11P=new JPanel();
	    d11P.setLayout(new FlowLayout(FlowLayout.CENTER, 10,2));//ds.einCb
	    label=new JLabel("Source energy");
	    d11P.add(label);label.setForeground(GammaBetaFrame.foreColor);
		d11P.add(ds.einCb);
	    label=new JLabel(ds.resources.getString("en.label"));
	    d11P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d11P.add(ds.energyTf);
	    d11P.add(ds.spectrumTf);
	    d11P.add(ds.spectrumB);
	    d11P.setBackground(ds.fundal);

	    JPanel eastP=new JPanel();
	    BoxLayout bl1 = new BoxLayout(eastP,BoxLayout.Y_AXIS);
	    eastP.setLayout(bl1);
	    eastP.add(d1P, null);
	    eastP.add(d26P, null);
		eastP.add(sourceP, null);
		eastP.add(detP, null);
		eastP.add(d11P, null);

	    JPanel northP=new JPanel(new BorderLayout());
	    northP.add(eastP,BorderLayout.CENTER);

		northP.setBackground(ds.fundal);
		eastP.setBackground(ds.fundal);
		sourceP.setBackground(ds.fundal);
		detP.setBackground(ds.fundal);

	    JPanel mainP=new JPanel(new BorderLayout());
	    mainP.add(northP,BorderLayout.CENTER);

		mainP.setBackground(ds.fundal);

	JPanel main2P=new JPanel(new BorderLayout());
	main2P.add(new JScrollPane(mainP),BorderLayout.CENTER);
	main2P.setBackground(ds.fundal);

	    return main2P;//mainP;
	  }

	  /**
	   * Create the output panel (holding TextArea)
	   * @return the result
	   */
	  protected JPanel createOutputPanel()
	  {
		  JPanel resultP=new JPanel(new BorderLayout());
		 // JScrollPane jspres=new JScrollPane();
		  resultP.add(new JScrollPane(ds.simTa),BorderLayout.CENTER);
		  resultP.setPreferredSize(textAreaDimension);
		  resultP.setBackground(ds.fundal);

	      JPanel mainP=new JPanel(new BorderLayout());
	      mainP.add(resultP,BorderLayout.CENTER);//main dimension !!
		  mainP.setBackground(ds.fundal);
	      return mainP;
	  }

}
