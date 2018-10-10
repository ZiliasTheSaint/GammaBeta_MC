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
 * Auxiliary class for EPGCS. It handles GUI controls. 
 * 
 * @author Dan Fulea, 22 AUG. 2005
 *
 */
public class EPGCSGUI {

	private EPGCS ds;
	  private static final Dimension sizeCb = new Dimension(50, 21);
	  private static final Dimension sizeLst = new Dimension(253,125);
	  private JScrollPane listSp = new JScrollPane();
	  
	  public EPGCSGUI(EPGCS ds)
	  {
		this.ds=ds;
		initComponents();
		//opacity();
	  }

	  //Component initialization
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  /**
	   * Component initialization
	   */
	private void initComponents()
	  {
	    ds.graphB.setText(ds.resources.getString("main.graphB"));
	    Character mnemonic;
	    mnemonic=(Character) ds.resources.getObject("main.graphB.mnemonic");
	    ds.graphB.setMnemonic(mnemonic.charValue());
	    ds.calclB.setText(ds.resources.getString("main.calclB"));
	    mnemonic=(Character) ds.resources.getObject("main.calclB.mnemonic");
	    ds.createB.setText("Create material file");
	    mnemonic=new Character('m');
	    ds.createB.setMnemonic(mnemonic.charValue());
	    ds.calclB.setMnemonic(mnemonic.charValue());
		ds.addB.setText(ds.resources.getString("main.addB"));
	    mnemonic=(Character) ds.resources.getObject("main.addB.mnemonic");
	    ds.addB.setMnemonic(mnemonic.charValue());
		ds.delB.setText(ds.resources.getString("main.delB"));
	    mnemonic=(Character) ds.resources.getObject("main.delB.mnemonic");
	    ds.delB.setMnemonic(mnemonic.charValue());
		ds.resetB.setText(ds.resources.getString("main.resetB"));
	    mnemonic=(Character) ds.resources.getObject("main.resetB.mnemonic");
	    ds.resetB.setMnemonic(mnemonic.charValue());
		ds.selectB.setText(ds.resources.getString("main.selectB"));
	    mnemonic=(Character) ds.resources.getObject("main.selectB.mnemonic");
	    ds.selectB.setMnemonic(mnemonic.charValue());

		String[] elems = (String[])ds.resources.getObject("main.elemCb");
		ds.elemCb=new JComboBox(elems);
	    //String s=elems[3];//100000
	    //ds.elemCb.setSelectedItem((Object)s);
		ds.elemCb.setMaximumRowCount(5);
		ds.elemCb.setPreferredSize(sizeCb);//(sizeTxtCb);

	    ds.simTa.setCaretPosition(0);
	    ds.simTa.setEditable(false);
	    ds.simTa.setText(ds.resources.getString("rezultat1"));
	    ds.simTa.setLineWrap(true);
	    ds.simTa.setWrapStyleWord(true);
		ds.simTa.setBackground(GammaBetaFrame.textAreaBkgColor);//setForeground
		ds.simTa.setForeground(GammaBetaFrame.textAreaForeColor);//setForeground
	    //---------------------------------------------------
	  	ds.aeTf.setOpaque(true);
	    ds.apTf.setOpaque(true);
	  	ds.ueTf.setOpaque(true);
	  	ds.upTf.setOpaque(true);
	  	ds.glowTf.setOpaque(true);
	  	ds.ghighTf.setOpaque(true);
	  	ds.elowTf.setOpaque(true);
	  	ds.ehighTf.setOpaque(true);
	  	ds.energyTf.setOpaque(true);

	  	ds.aeTf.setText(ds.resources.getString("main.aeTf"));
	  	ds.aeTf.setToolTipText(ds.resources.getString("main.aeTf.toolTip"));
	  	ds.apTf.setText(ds.resources.getString("main.apTf"));
	  	ds.apTf.setToolTipText(ds.resources.getString("main.apTf.toolTip"));
	  	ds.ueTf.setText(ds.resources.getString("main.ueTf"));
	  	ds.ueTf.setToolTipText(ds.resources.getString("main.ueTf.toolTip"));
	  	ds.upTf.setText(ds.resources.getString("main.upTf"));
	  	ds.upTf.setToolTipText(ds.resources.getString("main.upTf.toolTip"));
	  	ds.mediumTf.setText(ds.resources.getString("main.mediumTf"));
	  	ds.densityTf.setText(ds.resources.getString("main.densityTf"));
	  	ds.densityTf.setToolTipText(ds.resources.getString("main.densityTf.toolTip"));
	  	ds.glowTf.setText(ds.resources.getString("main.glowTf"));
		ds.glowTf.setToolTipText(ds.resources.getString("main.glowTf.toolTip"));
	  	ds.ghighTf.setText(ds.resources.getString("main.ghighTf"));
		ds.ghighTf.setToolTipText(ds.resources.getString("main.ghighTf.toolTip"));
	  	ds.elowTf.setText(ds.resources.getString("main.elowTf"));
		ds.elowTf.setToolTipText(ds.resources.getString("main.elowTf.toolTip"));
	  	ds.ehighTf.setText(ds.resources.getString("main.ehighTf"));
		ds.ehighTf.setToolTipText(ds.resources.getString("main.ehighTf.toolTip"));
	  	ds.energyTf.setText(ds.resources.getString("main.energyTf1"));
	  	ds.energyTf.setToolTipText(ds.resources.getString("main.energyTf.toolTip"));

	  	ds.pathTf.setEnabled(false);

	  	ds.autoRb=new JRadioButton(ds.resources.getString("autoRb.name"));
	  	ds.autoRb.setToolTipText(ds.resources.getString("autoRb.toolTip"));
	  	ds.manualRb=new JRadioButton(ds.resources.getString("manualRb.name"));
	  	ds.manualRb.setToolTipText(ds.resources.getString("manualRb.toolTip"));
	  	//ds.elemRb=new JRadioButton(ds.resources.getString("elemRb.name"));
	  	//ds.mixtRb=new JRadioButton(ds.resources.getString("mixtRb.name"));
		ds.gaspTf.setText(ds.resources.getString("main.gaspTf"));
		ds.gaspTf.setToolTipText(ds.resources.getString("main.gaspTf.toolTip"));
		ds.simTa.setToolTipText(ds.resources.getString("simTa.toolTip"));
	  }

	  /**
	   * This method create a panel border with some text.
	   * @param title the border text
	   * @return the result
	   */
	  private TitledBorder getGroupBoxBorder(String title)
	  {
		  TitledBorder tb =BorderFactory.createTitledBorder(EPGCS.LINE_BORDER,title);
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
		group.add(ds.autoRb);
		group.add(ds.manualRb);
		
	    //Put the radio buttons in a column in a panel.
	    JPanel buttP = new JPanel();
		buttP.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
		buttP.add(ds.autoRb);
		buttP.add(ds.manualRb);
	    JPanel butt1P = new JPanel();
		butt1P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
		//butt1P.add(ds.elemRb);
		//butt1P.add(ds.mixtRb);
		//-------------
		ds.autoRb.setBackground(ds.fundal);ds.autoRb.setForeground(GammaBetaFrame.foreColor);
		ds.manualRb.setBackground(ds.fundal);ds.manualRb.setForeground(GammaBetaFrame.foreColor);
		//ds.elemRb.setBackground(ds.fundal);ds.elemRb.setForeground(Color.white);
		//ds.mixtRb.setBackground(ds.fundal);ds.mixtRb.setForeground(Color.white);
		buttP.setBackground(ds.fundal);butt1P.setBackground(ds.fundal);
		ds.autoRb.setSelected(true);//medManP.setEnabled(false);//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		//ds.mixtRb.setSelected(true);
		//-----------------------------------------------------------------------------------
	    JPanel d1P=new JPanel();
	    d1P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    JLabel label=new JLabel(ds.resources.getString("medium.label"));
	    d1P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d1P.add(buttP);
	    d1P.setBackground(ds.fundal);
		//-----------------------------------------------------------------------------------
	    ds.medAutP.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("path.label"));
	    ds.medAutP.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    ds.medAutP.add(ds.pathTf);
	    ds.medAutP.add(ds.selectB);
	    ds.medAutP.setBackground(ds.fundal);
	    ds.medAutP.setBorder(getGroupBoxBorder(ds.resources.getString("auto.border")));

	    JPanel d3P=new JPanel();
	    d3P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("medname.label"));
	    //d3P.add(butt1P);
	    d3P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d3P.add(ds.mediumTf);
	    label=new JLabel(ds.resources.getString("density.label"));
	    d3P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d3P.add(ds.densityTf);
	    d3P.setBackground(ds.fundal);
	    //------------------------
	    JPanel lstmasP=new JPanel();
	    lstmasP.setLayout(new BorderLayout());
		lstmasP.add(listSp, BorderLayout.CENTER);
		listSp.getViewport().add(ds.kvmL, null);
	    listSp.setPreferredSize(sizeLst);
	    //----------------------------------------
		JPanel masuratP=new JPanel();
		BoxLayout bl0 = new BoxLayout(masuratP,BoxLayout.Y_AXIS);
		masuratP.setLayout(bl0);
	    JPanel m1=new JPanel();
	    m1.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("elem.label"));
	    m1.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    m1.add(ds.elemCb);
	    m1.setBackground(ds.fundal);
	    JPanel m2=new JPanel();
	    m2.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("weight.label"));
	    m2.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    m2.add(ds.weightTf);
	    m2.setBackground(ds.fundal);
	    JPanel m3=new JPanel();
	    m3.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    m3.add(ds.addB);
	    m3.setBackground(ds.fundal);
		masuratP.add(m1);
		masuratP.add(m2);
		masuratP.add(m3);
		masuratP.setBackground(ds.fundal);
		//-----------------------------------------------
		JPanel lstopP=new JPanel();
		JPanel jp1=new JPanel();
		jp1.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
		jp1.add(ds.delB);
		JPanel jp2=new JPanel();
		jp2.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
		jp2.add(ds.resetB);
		jp1.setBackground(ds.fundal);
		jp2.setBackground(ds.fundal);
		BoxLayout bl = new BoxLayout(lstopP,BoxLayout.Y_AXIS);
		lstopP.setLayout(bl);
		lstopP.add(jp1);
		lstopP.add(jp2);
		//-----------------------------------------------------
	    JPanel d4P=new JPanel();
	    d4P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    d4P.add(masuratP, null);
	    d4P.add(lstmasP, null);
		d4P.add(lstopP, null);
	    d4P.setBackground(ds.fundal);
	    BoxLayout bbl = new BoxLayout(ds.medManP,BoxLayout.Y_AXIS);
	    ds.medManP.setLayout(bbl);
	    ds.medManP.add(d3P);
	    ds.medManP.add(d4P);
	    ds.medManP.setBackground(ds.fundal);
	    ds.medManP.setBorder(getGroupBoxBorder(ds.resources.getString("manual.border")));
	    //ds.medManP.setEnabled(false);//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@INIT
	    ds.manualPanelEnabled(false);

	    JPanel d5P=new JPanel();
	    d5P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("ae.label"));
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d5P.add(ds.aeTf);
	    label=new JLabel(ds.resources.getString("ap.label"));
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.apTf);
	    label=new JLabel(ds.resources.getString("ue.label"));
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.ueTf);
	    label=new JLabel(ds.resources.getString("up.label"));
	    d5P.add(label);label.setForeground(GammaBetaFrame.foreColor);
		d5P.add(ds.upTf);
	    d5P.setBackground(ds.fundal);
	    JPanel d6P=new JPanel();
	    d6P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("glow.label"));
	    d6P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d6P.add(ds.glowTf);
	    label=new JLabel(ds.resources.getString("ghigh.label"));
	    d6P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d6P.add(ds.ghighTf);
	    d6P.setBackground(ds.fundal);
	    JPanel d7P=new JPanel();
	    d7P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("elow.label"));
	    d7P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d7P.add(ds.elowTf);
	    label=new JLabel(ds.resources.getString("ehigh.label"));
	    d7P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d7P.add(ds.ehighTf);
	    d7P.setBackground(ds.fundal);
	    JPanel energyP=new JPanel();
	    BoxLayout bbl1 = new BoxLayout(energyP,BoxLayout.Y_AXIS);
	    energyP.setLayout(bbl1);
	    energyP.add(d5P);
	    energyP.add(d6P);
	    energyP.add(d7P);
	    energyP.setBackground(ds.fundal);
	    energyP.setBorder(getGroupBoxBorder(ds.resources.getString("energy.border")));

	    JPanel d9P=new JPanel();
	    d9P.setLayout(new FlowLayout(FlowLayout.CENTER, 20,2));
	    //label=new JLabel(ds.resources.getString("energy.label"));
	    //d9P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	   // d9P.add(ds.energyTf);
	   // d9P.add(ds.calclB);
	   // d9P.add(ds.graphB);
	    d9P.add(ds.createB);
	    label=new JLabel("Material filename ");
	    d9P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d9P.add(ds.saveMaterialTf);
	    d9P.setBackground(ds.fundal);
	    ds.saveMaterialTf.setToolTipText("Can be null for default name!!");

	    JPanel d99P=new JPanel();
	    d99P.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
	    label=new JLabel(ds.resources.getString("gasp.label"));
	    d99P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d99P.add(ds.gaspTf);
	    label=new JLabel(ds.resources.getString("energy.label"));
	    d99P.add(label);label.setForeground(GammaBetaFrame.foreColor);
	    d99P.add(ds.energyTf);
		d99P.add(ds.calclB);
		d99P.add(ds.graphB);
	    d99P.setBackground(ds.fundal);
	//-----------------------------------------------------------------
	    //result panel
	    JPanel resultP=new JPanel(new BorderLayout());
	    JScrollPane jspres=new JScrollPane();
	    jspres.getViewport().add(ds.simTa, null);
	    resultP.add(jspres,  BorderLayout.CENTER);
	    resultP.setBackground(ds.fundal);
	//----------------------------------------------------------------
	    JPanel eastP=new JPanel();
	    BoxLayout bl1 = new BoxLayout(eastP,BoxLayout.Y_AXIS);
	    eastP.setLayout(bl1);
	    eastP.add(d1P, null);
	    eastP.add(ds.medAutP, null);
	    eastP.add(ds.medManP, null);
	    eastP.add(energyP, null);
		eastP.add(d99P, null);
		eastP.add(d9P, null);
		eastP.setBackground(ds.fundal);

	JPanel main2P=new JPanel(new BorderLayout());
	main2P.add(new JScrollPane(eastP),BorderLayout.CENTER);
	main2P.setBackground(ds.fundal);

	    JPanel mainP=new JPanel(new BorderLayout());
	    mainP.add(main2P,BorderLayout.NORTH);//(eastP,BorderLayout.NORTH);
	    mainP.add(resultP,BorderLayout.CENTER);//main dimension !!
	    return mainP;
	  }
}
