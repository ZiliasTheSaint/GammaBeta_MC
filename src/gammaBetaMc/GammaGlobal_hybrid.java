package gammaBetaMc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import danfulea.utils.ExampleFileFilter;
import danfulea.math.Sort;
import danfulea.utils.FrameUtilities;

/**
 * 
 * This class perform a hybrid method (Monte Carlo and analytical) for global (gross) gamma efficiency calculation.
 * @author Dan Fulea, 19 Apr. 2009
 *
 */
@SuppressWarnings("serial")
public class GammaGlobal_hybrid extends JFrame implements Runnable {
	private GammaBetaFrame mf;

	protected static final Border STANDARD_BORDER = BorderFactory
			.createEmptyBorder(5, 5, 5, 5);
	private static final Color c = Color.BLACK;
	protected static final Border LINE_BORDER = BorderFactory.createLineBorder(
			c, 2);
	protected Color fundal = new Color(144, 37, 38, 255);// new
	// Color(112,178,136,255);
	private static final Dimension PREFERRED_SIZE = new Dimension(950, 700);

	protected static ResourceBundle resources;
	private static final String BASE_RESOURCE_CLASS = "gammaBetaMc.resources.GES_hybridResources";

	private volatile Thread simTh;

	protected JTextArea simTa = new JTextArea();
	protected JTextField spectrumTf = new JTextField(25);
	protected JButton spectrumB = new JButton();
	protected JButton startsimB = new JButton();
	protected JButton saveSB = new JButton();
	protected JButton saveDB = new JButton();
	protected JButton loadSB = new JButton();
	protected JButton loadDB = new JButton();
	@SuppressWarnings("rawtypes")
	protected JComboBox nPhotonsCb;
	@SuppressWarnings("rawtypes")
	protected JComboBox sourceTypeCb;// source type-0=Cylinder, 1=Marinelli
	@SuppressWarnings("rawtypes")
	protected JComboBox detTypeCb;
	@SuppressWarnings("rawtypes")
	protected JComboBox windowCb;
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
	protected JTextField hUpTf = new JTextField(5);
	protected JTextField windowThickTf = new JTextField(5);
	protected JTextField energyTf = new JTextField(5);// energie photon --MeV
	@SuppressWarnings("rawtypes")
	protected JComboBox einCb;

	public static int mono = 1;// spectrum or 0=monoen
	public static String enerFilename = "";
	public static final String datas = "Data";
	public static final String dataspec = "spectra";
	public static String file_sep = System.getProperty("file.separator");
	public static String defaultext = ".spectrum";
	public static int $NENSRC = 300;// "MAX # OF POINTS IN ENERGY DISTRIBUTION  "
	public static int NENSRC = 0;
	public static double[] ENSRCD = new double[$NENSRC + 1];// (0:$NENSRC)
	public static double[] SRCPDF = new double[$NENSRC];// ($NENSRC)
	public static double[] srcpdf_at = new double[$NENSRC];// ($NENSRC),
	public static int[] srcbin_at = new int[$NENSRC];// ($NENSRC)
	public static int mode = 0;
	public static double ein = 0.0;
	public static double inac_thick_sup = 0.0;

	public static int sourcecode = 0;
	public static int sourcegeometrycode = 0;
	public static int detectorcode = 0;// 0->NaI and 1->Ge
	public static int NCASE = 0;
	public static boolean sourceatt = true;
	public static double deltazup = 0.0;

	public static int RandomUse = 1;// if 0- old Java random
	public static boolean ranluxB = true;// use ranlux if true, ranmar otherwise

	private static String simulationTimeElapsed = "";// store simulation time
														// elapsed
	private static int n_total_all = 0;// stores the total number of simulated
										// photons
	private static double[] efficiency;// efficiency NOT in % plus its error!!!
	private static double pondt = 0.0;// total weight of photon
	private static double pondt2 = 0.0;
	private static double pond = 0.0;
	private static double source_parcurs = 0.0;

	public static double adet = 0.0;// detector radius
	public static double asource = 0.0;// source radius
	public static double hdet = 0.0;// detector height
	public static double hsource = 0.0;// source height
	public static double hsourceup = 0.0;// upper source height---MARINELLI
	public static double bsource = 0.0;// inner radius of beaker---MARINELLI

	public static double winthick = 0.0;
	private static boolean stopB = false;// &&&&&&&&&&&
	private static boolean geomB = false;// &&&&&&&&&&&
	private static double[] wrho;// &&&&&&&&
	private static double[] watt;// &&&&&&&&&

	public static int ngood = 0;
	public static int nzer = 0;
	public static int nneg = 0;
	public static int allnneg = 0;
	public static int dummy1 = 0;
	public static int dummy2 = 0;
	public static int dummy22 = 0;
	public static int dummy3 = 0;
	private static double hwin00 = 0.0;

	private static double[] pefficiency;// photo
	private static double ppondt = 0.0;// total weight of photon
	private static double ppondt2 = 0.0;
	private static double ppond = 0.0;// reprezents the geometric efficiency.
										// This will be added to

	/**
	 * Constructor.
	 * @param mf, the GammaBetaFrame object
	 */
	public GammaGlobal_hybrid(GammaBetaFrame mf) {
		super("Gamma global-MC hybrid");
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
	 * This method is called from within the constructor to initialize the form.
	 */
	private void createGUI() {
		JPanel content = new JPanel(new BorderLayout());

		GES_hybridGUI ghGUI = new GES_hybridGUI(this);
		JPanel tabs = ghGUI.createSimMainPanel();
		content.add(tabs, BorderLayout.CENTER);

		setContentPane(new JScrollPane(content));
		content.setOpaque(true); // content panes must be opaque
		pack();
	}

	/**
	 * Set stop simulation flag
	 */
	public static void setStop() {
		stopB = true;
	}

	/**
	 * Set start simulation flag
	 */
	public static void setStart() {
		stopB = false;
	}

	/**
	 * Return efficiency and its uncertainty
	 * @return the result
	 */
	public static double[] getEfficiency() {
		return efficiency;
	}

	/**
	 * Return photo-peak efficiency and its uncertainty if available
	 * @return the result
	 */
	public static double[] getpEfficiency() {
		return pefficiency;
	}

	/**
	 * Return the total number of photons
	 * @return the result
	 */
	public static int getTotalPhotons() {
		return n_total_all;
	}

	/**
	 * Return simulation time.
	 * @return the result
	 */
	public static String getSimulationTimeElapsed() {
		return simulationTimeElapsed;
	}

	/**
	 * Compute time elapsed
	 * @param startTime startTime
	 * @return the result
	 */
	public static String timeElapsed(long startTime) {
		String times = "";
		long currentTime = System.currentTimeMillis();
		// tipul int spre deosebire de long--->aprox 8-9 zile suporta ca numar
		// de sec!!
		int delta = (new Long(currentTime - startTime)).intValue();// time
																	// elapsed
																	// in
																	// milliseconds
		int sec = delta / 1000;// impartire intreaga->catul!!!
		int milis = delta % 1000;// restul impartirii intregi!!
		if (sec > 60) {
			int min = sec / 60;
			sec = sec % 60;
			if (min > 60) {
				int h = min / 60;
				min = min % 60;
				if (h > 24) {
					int z = h / 24;
					h = h % 24;
					times = z + " days " + h + " h, " + min + " min, " + sec
							+ " sec, " + milis + " milis";
				} else {
					times = h + " h, " + min + " min, " + sec + " sec, "
							+ milis + " milis";
				}
			} else {
				times = min + " min, " + sec + " sec, " + milis + " milis";
			}
		} else {
			times = sec + " sec, " + milis + " milis";
		}
		return times;
	}

	
	/**
	 * Linear interpolation
	 * @param x1 first point x-value
	 * @param y1 first point y-value
	 * @param x2 second point x-value
	 * @param y2 second point y-value
	 * @param x desire point x-value
	 * @return desire point y-value
	 */
	public static double linInt(double x1, double y1, double x2, double y2,
			double x) {
		double result = -1.0;
		double[] mn = new double[2];
		// insucces
		mn[0] = -1.0;// m
		mn[1] = -1.0;// n
		double num = x1 - x2;
		if (num != 0.0) {
			mn[0] = (y1 - y2) / num;
			mn[1] = (x1 * y2 - y1 * x2) / num;
			result = mn[0] * x + mn[1];
		}
		return result;
	}

	// hwin=total thickness of absorbers
	/**
	 * Computes random coordinates and direction cosines for Sarpagan (Cylinder) geometry. 
	 * It also computes particle weight based on attenuation in window volume and the straight line path through the source.  
	 * @param delzup delzup, the detector gap or upper inactive volume thickness. For a symmetric detector it is roughly (detector_totalHeight-detector_crystal_height)/2
	 * @param wtype wtype, multiple window materials
	 * @param wthick wthick, multiple window thickness
	 * @param e_inci e_inci used in computing the weight
	 * @return the result
	 */
	private static double[] getCylinderRandom2(double delzup, String[] wtype,
			double[] wthick, double e_inci) {
		double hwin = delzup;// 0.5=(7.3-6.3)/2
		double[] result = new double[6];
		double r = random01();// RandomCollection.random01();
		// center of SC is in the center of cylinder (source shape), det is
		// placed at -hsource/2!!
		double z1 = hsource * r - hsource / 2;// initial random z coord in
												// cylinder coordinates

		r = random01();// RandomCollection.random01();
		double ro1 = asource * Math.sqrt(r);// dist to z axis ro1:
		r = random01();// RandomCollection.random01();
		double phi1 = 2 * Math.PI * r;// random angle for x0,y0 eval. from phi1
										// and ro1!!

		double d = hsource / 2 + z1 + hwin;// distance point 0 to detector----is
											// >0 always
		double dso = hsource / 2 + z1;// distance point to out of source

		// double diam=0.0;
		// if (asource<=adet)
		// {
		// diam=2*adet;
		// }
		// else
		// {
		// diam=2*asource;
		// }
		// double us1=2*Math.PI*(1-d/Math.sqrt(d*d+diam*diam));
		// double costmax=d/Math.sqrt(d*d+diam*diam);//cosines of theta max.
		// costet evaluation-->polar angle
		// double dom=(1-costmax)/2;//>0 and <1/2, costmax <1 and >0,
		// thetamax<90!
		// r=RandomCollection.random01();
		// r=r*dom;
		// double costet=2*r-1;//<0 always--- negativ z axis!!
		// double sintet=Math.sqrt(1-costet*costet);
		// double tgtet=sintet/costet;

		// ***************
		r = random01();// RandomCollection.random01();
		double costet = r - 1.0;// -r;//costmax=0=cos(pi/2)=>dom=1/2=>r=1/2r=>costet=2*1/2*r-1=r-1
								// or simply r
		double sintet = Math.sqrt(1 - costet * costet);
		double tgtet = sintet / costet;
		double teta = Math.abs(Math.atan(tgtet));// -pi/2,pi/2
		// double teta=r*Math.PI/2.0;//0-PI/2;cos,sin teta>0
		// **************
		r = random01();// RandomCollection.random01();
		double phi2 = 2 * Math.PI * r;// azimutal angle
		pond = 1.0 / 2.0;// us1/(4*Math.PI);//initial weight associated with
							// this variance reduction
		// double teta=Math.abs(Math.atan(tgtet));//-pi/2,pi/2
		// initial we have formal directional cosines u,v,w = 0,0,-1 and then we
		// have teta and phi2:
		double u = Math.sin(teta) * Math.cos(phi2);
		double v = Math.sin(teta) * Math.sin(phi2);
		double w = costet;// -Math.cos(teta);//costet;//<0
		source_parcurs = d / w;// costet;
		source_parcurs = Math.abs(source_parcurs);// >0
		double x0 = ro1 * Math.cos(phi1);
		double y0 = ro1 * Math.sin(phi1);
		// double x=x0+u*source_parcurs;
		// double y=y0+v*source_parcurs;
		// double z=-hsource/2-hwin;//@@@@@@@@@@@@@@@@@@@@@@@@@@
		double source_parcurs2 = dso / w;// costet;
		source_parcurs2 = Math.abs(source_parcurs2);// >0

		double ux = u;
		double uy = v;
//		double uz = w;
		// strictly in source volume
		double l1 = 0.0;
		double zer = (ux * x0 + uy * y0) * (ux * x0 + uy * y0)
				+ (ux * ux + uy * uy) * (asource * asource - x0 * x0 - y0 * y0);
		if (zer > 0.) {
			double s1 = (-(ux * x0 + uy * y0) - Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (asource * asource - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s2 = (-(ux * x0 + uy * y0) + Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (asource * asource - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s = 0.;
			if ((s1 < 0.) && (s2 > 0)) {
				s = s2;
			} else {
				s = Math.min(Math.abs(s1), Math.abs(s2));
			}
			l1 = s;// System.out.println(l1);
		} else
			l1 = source_parcurs2;// something wrong is happened!!
		if (l1 < source_parcurs2) // it also fly in air (neglected)
			source_parcurs2 = l1;

		// detector attenuation---------------------------------------------
//		double ldetatt = source_parcurs - source_parcurs2;// no use only in Al
//		double windens = 0.;
//		double winatt = 0.;
		if (!geomB) {
			wrho = solvewinDens(wtype);
			watt = solvewinAtt(wtype, e_inci);
		}

		for (int i = 0; i < wthick.length; i++) {
			pond = pond
					* Math.exp(-wrho[i] * watt[i] * wthick[i] / Math.abs(w));// Math.abs(costet));
		}

		// -------------------------------------------------------------------
		source_parcurs = source_parcurs2;// reset to in source attenuation
		// ----------------------------------
		// new pond based on attenuation in source volume will be based on this
		// source_parcurs!!
		result[0] = x0;// x;
		result[1] = y0;// y;
		result[2] = z1 + (hwin + (hdet + hsource) / 2.0);// z;
		result[3] = u;
		result[4] = v;
		result[5] = w;
		// OBS am translatat SC la centrul detectorului (sursa e concentric
		// plasata pe detector)
		// La translatia pe o axa de coordonate se modifica doar z nu si x,y, si
		// nici cosinusurile directoare
		return result;
	}

	/**
	 * Get window densities (for multiple window materials).
	 * @param s s
	 * @return the result
	 */
	public static double[] solvewinDens(String[] s) {
		double[] result = new double[s.length];
		double[] windensity = (double[]) resources
				.getObject("window.densities");
		String[] wintype = (String[]) resources.getObject("window.type");
		for (int j = 0; j < s.length; j++) {
			for (int i = 0; i < wintype.length; i++) {
				if (s[j].compareTo(wintype[i]) == 0) {
					result[j] = windensity[i];
					break;
				}
			}
		}
		geomB = true;
		return result;
	}

	/**
	 * Given incident energy, this routine reads attenuation coefficient table and compute 
	 * the actual window attenuation coefficients for multiple window materials and using linear interpolation.  
	 * @param s s
	 * @param e_inc e_inc
	 * @return the result
	 */
	public static double[] solvewinAtt(String[] s, double e_inc) {
		double[] result = new double[s.length];
		int index_low = 0;
		int index_high = 0;
		double[][] wintable = (double[][]) resources
				.getObject("detector.window.attenuationCoef");
		double[] energy = new double[wintable.length];
		String[] wintype = (String[]) resources.getObject("window.type");
		for (int jj = 0; jj < s.length; jj++) {
			double[] total_coeff = new double[wintable.length];
			for (int i = 0; i < energy.length; i++) {
				energy[i] = wintable[i][0];
				for (int j = 0; j < wintype.length; j++) {
					if (s[jj].compareTo(wintype[j]) == 0) {
						total_coeff[i] = wintable[i][j + 1];
						break;
					}
				}
			}
			Sort.findNearestValue(energy, e_inc, true);
			index_low = Sort.getNearestPosition();
			if (index_low < energy.length - 1)
				index_high = index_low + 1;
			else
				index_high = index_low;
			result[jj] = linInt(energy[index_high], total_coeff[index_high],
					energy[index_low], total_coeff[index_low], e_inc);
		}
		return result;
	}

	/**
	 * For a given material, this method (routine, function whatever you called it) returns 
	 * the window density.
	 * @param s s
	 * @return the result
	 */
	public static double solvewinDens(String s) {
		double result = 0.;
		double[] windensity = (double[]) resources
				.getObject("window.densities");
		String[] wintype = (String[]) resources.getObject("window.type");
		for (int i = 0; i < wintype.length; i++) {
			if (s.compareTo(wintype[i]) == 0) {
				result = windensity[i];
				break;
			}
		}

		return result;
	}

	/**
	 * For a given material and incident energy, this method returns 
	 * the window attenuation coefficient.
	 * @param s s
	 * @param e_inc e_inc
	 * @return the result
	 */
	public static double solvewinAtt(String s, double e_inc) {
		double result = 0.;
		int index_low = 0;
		int index_high = 0;
		double[][] wintable = (double[][]) resources
				.getObject("detector.window.attenuationCoef");
		double[] energy = new double[wintable.length];
		double[] total_coeff = new double[wintable.length];
		String[] wintype = (String[]) resources.getObject("window.type");
		for (int i = 0; i < energy.length; i++) {
			energy[i] = wintable[i][0];
			for (int j = 0; j < wintype.length; j++) {
				if (s.compareTo(wintype[j]) == 0) {
					total_coeff[i] = wintable[i][j + 1];
					break;
				}
			}

		}
		Sort.findNearestValue(energy, e_inc, true);
		index_low = Sort.getNearestPosition();
		if (index_low < energy.length - 1)
			index_high = index_low + 1;
		else
			index_high = index_low;
		result = linInt(energy[index_high], total_coeff[index_high],
				energy[index_low], total_coeff[index_low], e_inc);
		return result;
	}

	/**
	 * Computes random coordinates and direction cosines for Marinelli geometry. 
	 * It also initialize particle weight and computes the straight line path through the source.  
	 * @param deltazup deltazup, the detector gap or upper inactive volume thickness. For a symmetric detector it is roughly (detector_totalHeight-detector_crystal_height)/2
	 * @param deltazdown deltazdown, similar with deltazup, it is the distance from bottom active detector to bottom detector.
	 * @param deltaxy deltaxy, similar with deltazup, it is the distance from side active detector to side detector.
	 * @param wtype wtype, multiple window materials
	 * @param wthick wthick, multiple window thickness
	 * @param e_inci e_inci used in computing the weight
	 * @return the result
	 */
	private static double[] getMarrinelliRandom2(double deltazup,
			double deltazdown, double deltaxy, String[] wtype, double[] wthick,
			double e_inci) {
		double[] result = new double[6];
		double r = random01();// RandomCollection.random01();
		double z1 = hsource * r - hsource / 2;// initial random z coord in
												// cylinder coordinates
		boolean infcyl = false;// test if inf cylinder exists

		// ----------
		hwin = deltazup;// pt translatie coord la getMMidInf2
		// ---------
		if (hsource > hsourceup + hdet + deltazup + deltazdown)
			infcyl = true;

		if (z1 >= hsource / 2 - hsourceup)// -deltazup)
		{
			result = getMCylSup2(z1, deltazup, wtype, wthick, e_inci);
		} else if (infcyl) {
			if (z1 <= hsource / 2 - hsourceup - hdet - deltazup - deltazdown) {
				result = getMCylInf2(z1, deltazup, deltazdown);
			} else// middle region
			{
				result = getMMidInf2(z1, deltaxy);
			}
		} else// middle region
		{
			result = getMMidInf2(z1, deltaxy);
		}

		return result;
	}

	/**
	 * Internally used by getMarrinelliRandom2.
	 * @param zi zi
	 * @param delzup delzup
	 * @param wtype wtype
	 * @param wthick wthick
	 * @param e_inci e_inci
	 * @return the result
	 */
	private static double[] getMCylSup2(double zi, double delzup,
			String[] wtype, double[] wthick, double e_inci) {
		double hwin = delzup;
		double[] result = new double[6];
		double z1 = zi;// initial random z coord in cylinder coordinates

		double r = random01();// RandomCollection.random01();
		double ro1 = asource * Math.sqrt(r);// dist to z axis ro1:
		r = random01();// RandomCollection.random01();
		double phi1 = 2 * Math.PI * r;// random angle for x0,y0 eval. from phi1
										// and ro1!!
		double d = z1 - (hsource / 2 - hsourceup - hwin);// distance point 0 to
															// detector>0
		double dso = z1 - (hsource / 2 - hsourceup);
		;// distance point to out of source

		// double diam=2*asource;
		// double us1=2*Math.PI*(1-d/Math.sqrt(d*d+diam*diam));
		// double costmax=d/Math.sqrt(d*d+diam*diam);//cosines of theta max.
		// costet evaluation-->polar angle
		// double dom=(1-costmax)/2;//>0 and <1/2, costmax <1 and >0,
		// thetamax<90!
		// r=RandomCollection.random01();
		// r=r*dom;
		// double costet=2*r-1;//<0 always--- negativ z axis!!
		// double sintet=Math.sqrt(1-costet*costet);
		// double tgtet=sintet/costet;
		r = random01();// RandomCollection.random01();
		double phi2 = 2 * Math.PI * r;// azimutal angle
		pond = 1.0 / 2.0;// us1/(4*Math.PI);//initial weight associated with
							// this variance reduction
		// double teta=Math.abs(Math.atan(tgtet));//-pi/2,pi/2
		// -------------
		r = random01();// RandomCollection.random01();
		// double teta=r*Math.PI/2.0;
		double costet = -r;// costmax=0=cos(pi/2)=>dom=1/2=>r=1/2r=>costet=2*1/2*r-1=r-1
							// or simply r
		double sintet = Math.sqrt(1 - costet * costet);
		double tgtet = sintet / costet;
		double teta = Math.abs(Math.atan(tgtet));// -pi/2,pi/2
		// -------------------------
		// initial we have formal directional cosines u,v,w = 0,0,-1 and then we
		// have teta and phi2:
		double u = Math.sin(teta) * Math.cos(phi2);
		double v = Math.sin(teta) * Math.sin(phi2);
		double w = costet;// -Math.cos(teta);//costet;//<0
		source_parcurs = d / w;// costet;
		source_parcurs = Math.abs(source_parcurs);// >0
		double x0 = ro1 * Math.cos(phi1);
		double y0 = ro1 * Math.sin(phi1);
		// double x=x0+u*source_parcurs;
		// double y=y0+v*source_parcurs;
		// double z=hsource/2-hsourceup;-hwin;
		// double z=hsource/2-hsourceup-hwin;//@@@@@@@@@@@@@@@@@@@@@@@@@@
		double source_parcurs2 = dso / w;// costet;
		source_parcurs2 = Math.abs(source_parcurs2);// >0
		// attenuation is always in source:
		// detector attenuation---------------------------------------------
//		double ldetatt = source_parcurs - source_parcurs2;// no use only in Al
//		double windens = 0.;
//		double winatt = 0.;
		if (!geomB) {
			wrho = solvewinDens(wtype);
			watt = solvewinAtt(wtype, e_inci);
		}

		for (int i = 0; i < wthick.length; i++) {
			pond = pond
					* Math.exp(-wrho[i] * watt[i] * wthick[i] / Math.abs(w));// Math.abs(costet));
		}
		// -------------------------------------------------------------------
		source_parcurs = source_parcurs2;// reset to in source attenuation
		// ----------------------------------
		result[0] = x0;// x;
		result[1] = y0;// y;
		result[2] = z1 - (hsource / 2.0 - hsourceup - hwin - hdet / 2.0);// z;
		result[3] = u;
		result[4] = v;
		result[5] = w;

		return result;
	}

	/**
	 * Internally used by getMarrinelliRandom2.
	 * @param zi zi
	 * @param delup delup
	 * @param deldown deldown
	 * @return the result
	 */
	private static double[] getMCylInf2(double zi, double delup, double deldown) {
		// from behind=>no change!!
		double[] result = new double[6];
		double z1 = zi;// initial random z coord in cylinder coordinates
		double r = random01();// RandomCollection.random01();
		double ro1 = Math.sqrt((asource * asource - bsource * bsource) * r
				+ bsource * bsource);// dist to z axis ro1:
		r = random01();// RandomCollection.random01();
		double phi1 = 2 * Math.PI * r;// random angle for x0,y0 eval. from phi1
										// and ro1!!

		// double d=Math.abs(z1+(hsource/2-hsourceup-hdet-delup));
		// double dso=Math.abs(z1+(hsource/2-hsourceup-hdet-delup-deldown));
		double d = Math.abs(z1 - (hsource / 2 - hsourceup - hdet - delup));
		double dso = Math.abs(z1
				- (hsource / 2 - hsourceup - hdet - delup - deldown));

		// for MC variance reduction we impose that all photons emerged from
		// point 0 will go into
		// an solid angle us1 having a distance d and a radius=2*sourceradius in
		// order
		// to make sure that this allways contain the detector!!
		// double diam=2*asource;
		// double us1=2*Math.PI*(1-d/Math.sqrt(d*d+diam*diam));
		// double costmax=d/Math.sqrt(d*d+diam*diam);//cosines of theta max.
		// costet evaluation-->polar angle
		// double dom=(1-costmax)/2;//>0 and <1/2, costmax <1 and >0,
		// thetamax<90!
		// r=RandomCollection.random01();
		// r=r*dom;
		// double costet=-2*r+1;//>0 always--- positiv z axis!!!!!!!!!!!!!!
		// double sintet=Math.sqrt(1-costet*costet);
		// double tgtet=sintet/costet;

		r = random01();// RandomCollection.random01();
		double phi2 = 2 * Math.PI * r;// azimutal angle
		pond = 1.0 / 2.0;// us1/(4*Math.PI);//initial weight associated with
							// this variance reduction
		// double teta=Math.abs(Math.atan(tgtet));//-pi/2,pi/2
		r = random01();// RandomCollection.random01();
		// double teta=r*Math.PI/2.0;
		double costet = r;// costmax=0=cos(pi/2)=>dom=1/2=>r=1/2r=>costet=2*1/2*r-1=r-1
							// or simply r
		double sintet = Math.sqrt(1 - costet * costet);
		double tgtet = sintet / costet;
		double teta = Math.abs(Math.atan(tgtet));// -pi/2,pi/2

		// ---------
		// initial we have formal directional cosines u,v,w = 0,0,-1 and then we
		// have teta and phi2:
		double u = Math.sin(teta) * Math.cos(phi2);
		double v = Math.sin(teta) * Math.sin(phi2);
		double w = costet;// Math.cos(teta);//costet;//>0

		source_parcurs = d / w;// costet;
		source_parcurs = Math.abs(source_parcurs);// >0
		double x0 = ro1 * Math.cos(phi1);
		double y0 = ro1 * Math.sin(phi1);
		// double x=x0+u*source_parcurs;
		// double y=y0+v*source_parcurs;
		// double z=hsource/2-hsourceup-hdet;
		// double z=hsource/2-hsourceup-hdet-delup;
		double source_parcurs2 = dso / w;// costet;
		source_parcurs2 = Math.abs(source_parcurs2);// >0
		// attenuation !is always in source:
		double ux = u;
		double uy = v;
	//	double uz = w;
		// strictly in source volume
		double l1 = 0.0;
		double zer = (ux * x0 + uy * y0) * (ux * x0 + uy * y0)
				+ (ux * ux + uy * uy) * (bsource * bsource - x0 * x0 - y0 * y0);
		if (zer > 0.) {
			double s1 = (-(ux * x0 + uy * y0) - Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (bsource * bsource - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s2 = (-(ux * x0 + uy * y0) + Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (bsource * bsource - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s = 0.;
			if ((s1 < 0.) && (s2 > 0)) {
				s = s2;
			} else {
				s = Math.min(Math.abs(s1), Math.abs(s2));
			}
			l1 = s;// System.out.println(l1);
		} else
			l1 = source_parcurs2;// something wrong is happened!!
		if (l1 < source_parcurs2) // it also fly in air (neglected)
			source_parcurs2 = l1;
		// ----------------------------------
		source_parcurs = source_parcurs2;// reset to in source attenuation
		// ----------------------------------
		result[0] = x0;// x;
		result[1] = y0;// y;
		result[2] = z1 - (hsource / 2.0 - hsourceup - delup - hdet / 2.0);// ;//z;
		result[3] = u;
		result[4] = v;
		result[5] = w;

		return result;
	}

	// neglected
	private static double hwin = 0.0;

	/**
	 * Internally used by getMarrinelliRandom2.
	 * @param zi zi
	 * @param delxy delxy
	 * @return the result
	 */
	private static double[] getMMidInf2(double zi, double delxy) {
		double[] result = new double[6];
		double z1 = zi;// initial random z coord in cylinder coordinates
		double r = random01();// RandomCollection.random01();
		double ro1 = Math.sqrt((asource * asource - bsource * bsource) * r
				+ bsource * bsource);// dist to z axis ro1:
		r = random01();// RandomCollection.random01();
		double phi1 = 2 * Math.PI * r;// ///////for x0 and y0 eval
										// ???????????????????/
//		double rdet = Math.sqrt(adet * adet + hdet * hdet / 4);// for us eval!!
//		double d = ro1;// distance for us eval!!
		// to make sure that this allways contain the detector!!
		// double diam=2*rdet;//for us eval!!
		// double us1=2*Math.PI*(1-d/Math.sqrt(d*d+diam*diam));//OK!
		// double costmax=d/Math.sqrt(d*d+diam*diam);//cosines of theta max.
		// costet evaluation-->polar angle
		// double dom=(1-costmax)/2;//>0 and <1/2, costmax <1 and >0,
		// thetamax<90!
		// r=RandomCollection.random01();
		// r=r*dom;
		// double costet=-2*r+1;//>0 for instance
		// double sintet=Math.sqrt(1-costet*costet);
		// double tgtet=sintet/costet;
		r = random01();// RandomCollection.random01();
		// for simplicity and due to simmetry of problem we choose that ***
		double phi2 = 2 * Math.PI * r;// phi1;//2*Math.PI*r;//azimutal angle
		pond = 1.0 / 2.0;// us1/(4*Math.PI);//initial weight associated with
							// this variance reduction
		r = random01();// RandomCollection.random01();
		// double teta=r*Math.PI/2.0;
		double costet = r;// costmax=0=cos(pi/2)=>dom=1/2=>r=1/2r=>costet=2*1/2*r-1=r-1
							// or simply r
		double sintet = Math.sqrt(1 - costet * costet);
		double tgtet = sintet / costet;
		double teta = Math.abs(Math.atan(tgtet));// -pi/2,pi/2

		// double teta=Math.abs(Math.atan(tgtet));//-pi/2,pi/2
		// initial we have formal directional cosines u,v,w and then we have
		// teta and phi2:
		double xsi = Math.PI / 2;// shift with Pi/2 relative to z axis!!!
		double u = Math.sin(xsi) * Math.cos(phi1 + Math.PI);// -changing
															// direction
		double v = Math.sin(xsi) * Math.sin(phi1 + Math.PI);// -changing
															// direction
		double w = Math.cos(xsi);// 0!
		// "new " direction
		double ux = 0.0;
		double uy = 0.0;
		double uz = 0.0;
		if (Math.abs(w) > 0.99999)// miuz->normal incident
		{
			ux = Math.sin(teta) * Math.cos(phi2);// new miux
			uy = Math.sin(teta) * Math.sin(phi2);// new miuy
			if (w < 0)
				uz = -Math.cos(teta);// new miuz--never here---random
			else
				uz = Math.cos(teta);// new miuz
		} else {
			double temp = Math.sqrt(1.0 - w * w);
			ux = Math.sin(teta) * (u * w * Math.cos(phi2) - v * Math.sin(phi2))
					/ temp + u * Math.cos(teta);
			uy = Math.sin(teta) * (v * w * Math.cos(phi2) + u * Math.sin(phi2))
					/ temp + v * Math.cos(teta);
			uz = -Math.sin(teta) * Math.cos(phi2) * temp + w * Math.cos(teta);
		}
		// --------------------------------------
		// source_parcurs=(d-adet)/costet;//minimum for phi2 particular see ***
		// source_parcurs=Math.abs(source_parcurs)+0.1;//>0 and be sure to hit
		// if it is tend to!!
		double x0 = ro1 * Math.cos(phi1);
		double y0 = ro1 * Math.sin(phi1);
		// -----------------------------------
		double zer = (ux * x0 + uy * y0) * (ux * x0 + uy * y0)
				+ (ux * ux + uy * uy) * (adet * adet - x0 * x0 - y0 * y0);
		if (zer > 0.) {
			// source_parcurs=-(ux*x0+uy*y0);//System.out.println(
			// source_parcurs);>0
			double s1 = (-(ux * x0 + uy * y0) - Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (adet * adet - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s2 = (-(ux * x0 + uy * y0) + Math.sqrt((ux * x0 + uy * y0)
					* (ux * x0 + uy * y0) + (ux * ux + uy * uy)
					* (adet * adet - x0 * x0 - y0 * y0)))
					/ (ux * ux + uy * uy);
			double s = 0.;
			if ((s1 < 0.) && (s2 > 0)) {
				s = s2;
			} else {
				s = Math.min(Math.abs(s1), Math.abs(s2));
			}
			source_parcurs = s + 0.1;// System.out.println(source_parcurs);
			// aici - radical ca vreu prima atingere, aia mica, Nu a doua in
			// spate cilindru!!!!
			// source_parcurs=Math.abs(source_parcurs/(ux*ux+uy*uy))+0.025;//System.out.println(source_parcurs);
		} else
			source_parcurs = 0.;// not hit
		// ----------------------------------
		// double x=x0+ux*source_parcurs;
		// double y=y0+uy*source_parcurs;
		// double z=z1+uz*source_parcurs;
		// attenuation is always in source:
		// starting point
		result[0] = x0;// x;
		result[1] = y0;// y;
		result[2] = z1 - (hsource / 2.0 - hsourceup - hwin - hdet / 2.0);// ;//z;
		result[3] = ux;
		result[4] = uy;
		result[5] = uz;

		return result;
	}

	/**
	 * Given initial coordinates (and direction cosines) computes the traversed distance through detector.
	 * @param coord coord
	 * @return the result
	 */
	private static double getTraversedDistance(double[] coord) {
		double result = -1.0;// failure

		double db1 = -1.0;// failure
		double db2 = -1.0;// failure
		double dc1 = -1.0;// failure
		double dc2 = -1.0;// failure

		double x0 = coord[0];
		double y0 = coord[1];
		double z0 = coord[2];
		double ux = coord[3];
		double uy = coord[4];
		double uz = coord[5];

		//SIMA O. <=> 3MC_general sampling PDF p44.
		//Notice failure if ux=uy=0!!!!
		double zer = (ux * x0 + uy * y0) * (ux * x0 + uy * y0)
				+ (ux * ux + uy * uy) * (adet * adet - x0 * x0 - y0 * y0);
		if (zer >= 0.) {
			dc1 = (-(ux * x0 + uy * y0) - Math.sqrt(zer)) / (ux * ux + uy * uy);
			dc2 = (-(ux * x0 + uy * y0) + Math.sqrt(zer)) / (ux * ux + uy * uy);
		}// else dc1,2=-1.0 <0.0!!!!!!!!!!!!!!!!
		db1 = ((-hdet / 2.0) - z0) / uz;
		db2 = ((hdet / 2.0) - z0) / uz;
		if (zer < 0.0) {
			return result;
		}
		/*
		 * //@0. All distances <0.0 or db1,2<0 and zer<0.0 if (((zer<0.0) ||
		 * (dc1<0.0 && dc2<0.0)) && (db1<0.0 && db2<0.0)) { allnneg++; return
		 * result; }
		 * 
		 * //@1. dc1,2 <0.0 or zer<0.0 if ((zer<0.0) || (dc1<0.0 && dc2<0.0)) {
		 * //dc does not exist! if (db1>0.0 && db2>0.0) {
		 * if(x0*x0+y0*y0<=adet*adet)//inside det circus { if (zer<0) { nzer++;
		 * } else { nneg++; } //ngood++; result=Math.abs(db1-db2); //return
		 * result; } return result;//-1 } else//db1>0; db2<0 or db2>0;db1<0 {
		 * dummy1++; result=Math.max(db1,db2);//inside point return result; } }
		 * //@2 db1<0 && db2<0 if (db1<0.0 && db2<0.0) { if (dc1>0.0 && dc2>0.0)
		 * { dummy22++; //ngood++; //result=Math.abs(dc1-dc2);//particular case
		 * uz=0=>neglected return result; } else//dc1>0; dc2<0 or dc2>0;dc1<0 {
		 * dummy2++; result=Math.max(dc1,dc2);//inside point return result; } }
		 * //@3 1 distance >0 and 1 distance <0.0 for both db and dc if
		 * (((dc1>0.0) && (dc2<0.0) || (dc1<0.0) && (dc2>0.0))&& ((db1>0.0) &&
		 * (db2<0.0) || (db1<0.0) && (db2>0.0))) {//inside point dummy3++;
		 * double db=Math.max(db1,db2); double dc=Math.max(dc1,dc2);
		 * result=Math.min(db,dc); return result;
		 * 
		 * } //@4 4 positive values -all of them //or //@5. 3 positive values
		 * //if ((dc1>0.0) && (dc2>0.0) && (db1>0.0) && (db2>0.0)) //{
		 */
		// ngood++;
		// we have dc1,dc2,db1,db2..ROUNDOFF NOT GOOD!!
		// we looking only 2 points from 2 possible!!!!!!!!!!!!!!
		double x1 = 0.0;
		double y1 = 0.0;
		double xyz = 0.0;
		double test1 = 0.0;
		double test2 = 0.0;
		double testround = 0.0;
		double testp = 0.1;// 1%
		double aa = 0.0;
		double bb = 0.0;
		boolean founda = false;
		boolean foundb = false;

		// 1 dc1 set:
		xyz = z0 + uz * dc1;
		test1 = Math.sqrt(xyz * xyz);
		test2 = Math.sqrt(hdet * hdet / 4.0);
		testround = 100.0 * Math.abs(test1 - test2) / test2;
		if ((xyz > -hdet / 2.0 && xyz < hdet / 2.0) || (testround < testp))// <1%
																			// diff=>is
																			// real!
		{
			dummy1++;
			aa = dc1;
			founda = true;
		}

		// 2 dc2 set:
		xyz = z0 + uz * dc2;
		test1 = Math.sqrt(xyz * xyz);
		test2 = Math.sqrt(hdet * hdet / 4.0);
		testround = 100.0 * Math.abs(test1 - test2) / test2;
		if ((xyz > -hdet / 2.0 && xyz < hdet / 2.0) || (testround < testp))// <1%
																			// diff=>is
																			// real!
		{
			dummy2++;
			if (!founda) {
				aa = dc2;
				founda = true;
			} else {
				bb = dc2;
				foundb = true;
			}
		}

		// 3 db1 set:
		x1 = x0 + ux * db1;
		y1 = y0 + uy * db1;
		xyz = x1 * x1 + y1 * y1;
		test1 = Math.sqrt(xyz);
		test2 = Math.sqrt(adet * adet);
		testround = 100.0 * Math.abs(test1 - test2) / test2;
		if ((xyz < adet * adet) || (testround < testp))// <1% diff=>is real!
		{
			dummy22++;
			if (!founda) {
				aa = db1;
				founda = true;
			} else {
				bb = db1;
				foundb = true;
			}
		}

		// 4 db2 set:
		x1 = x0 + ux * db2;
		y1 = y0 + uy * db2;
		xyz = x1 * x1 + y1 * y1;
		test1 = Math.sqrt(xyz);
		test2 = Math.sqrt(adet * adet);
		testround = 100.0 * Math.abs(test1 - test2) / test2;
		if ((xyz < adet * adet) || (testround < testp))// <1% diff=>is real!
		{
			dummy3++;
			if (!founda) {
				aa = db2;
				founda = true;
			} else {
				bb = db2;
				foundb = true;
			}
		}

		if (founda && foundb)// real
		{
			ngood++;
			result = Math.abs(aa - bb);
		}

		return result;
		/*
		 * double dmin=Math.min(db1,db2); dmin=Math.min(dmin,dc1);
		 * dmin=Math.min(dmin,dc2); //elimination of dim=>not real double[]
		 * ds=new double[3]; if (dmin==db1) { ds[0]=db2; ds[1]=dc1; ds[2]=dc2; }
		 * else if (dmin==db2) { ds[0]=db1; ds[1]=dc1; ds[2]=dc2; } else if
		 * (dmin==dc1) { ds[0]=db1; ds[1]=db2; ds[2]=dc2; } else //it is dc2 if
		 * (dmin==db2) { ds[0]=db1; ds[1]=db2; ds[2]=dc1; } //entrance distance
		 * double din=Math.min(ds[0],ds[1]); din=Math.min(din,ds[2]);
		 * 
		 * double[] dss=new double[2]; if (din==ds[0]) { dss[0]=ds[1];
		 * dss[1]=ds[2]; } else if (din==ds[1]) { dss[0]=ds[0]; dss[1]=ds[2]; }
		 * else //it is ds[2] { dss[0]=ds[0]; dss[1]=ds[1]; } double
		 * dout=Math.min(dss[0],dss[1]); result=dout-din; return result; //}
		 * //else //@5. 3 positive values //{
		 * 
		 * //}
		 */
		// return result;//never happen
	}

	/*
	 * The probability of gamma radiation reaching the NaI(Tl) scintillator
	 * without any interactions inside the CaF2(Eu) crystal is given by
	 * 
	 * (9) q1=exp(-µCaF2(E)·W1) and this probability for quartz layer is given
	 * by
	 * 
	 * (10) q2=exp(-µQ(E)·W2) where µCaF2(E) and µQ(E) are total linear
	 * attenuation coefficients for gamma photons with an energy of E inside the
	 * CaF2 and quartz respectively.
	 * 
	 * In this study, gamma interactions in air were neglected, hence the
	 * probability q0 of going through the distance W0 between the source and
	 * detector was assumed to be 1.
	 * 
	 * The absorption fraction S(E) of a gamma photon inside the NaI(Tl)
	 * scintillator reaching the scintillator without any interactions is given
	 * as
	 * 
	 * (11) S(E)=q1q2(1-exp(-µNaI(E)·W)) where µNaI(E) is total linear
	 * attenuation coefficient without coherent scattering.
	 * 
	 * The total of the S(E) values for all photons that traverse through the
	 * detector is given as SUM(S(E)).
	 * 
	 * N number of photons is generated within a solid angle of 2PI in the
	 * simulation. However, real sources emit photons in all directions of the
	 * 4PI solid angle. Therefore, the total detector efficiency for this point
	 * source detector arrangement is calculated using Eq. (11) as presented in
	 * Haase et al. (1993).
	 * 
	 * eff GLOBAL=[SUM(S(E))]/[2N]
	 * 
	 * (12) View the MathML source
	 * 
	 * OBS punand TOTAL-COH in loc de TOT (<TOT)=>exp(-miu..) e mai mare=>eff
	 * scade!!! OK! ca am si rayleigh de fapt! M-am limitat la atenuare in sens
	 * ca fotonul sufera interactii depozitand energie ceea ce e valabil doar la
	 * photo,compt,perechi (NU RAYLEIGH!)
	 */
	/**
	 * Perform hybrid simulation.
	 * @param photon_number photon_number
	 * @param sourcecode sourcecode
	 * @param sourcegeometrycode sourcegeometrycode
	 * @param detectorcode detectorcode
	 * @param gammaenergy gammaenergy
	 * @param det_radius det_radius
	 * @param source_radius source_radius
	 * @param det_height det_height
	 * @param source_height source_height
	 * @param source_inner_radius source_inner_radius
	 * @param source_height_up source_height_up
	 * @param deltazup deltazup
	 * @param deltazdown deltazdown
	 * @param deltaxy deltaxy
	 * @param winDetType winDetType
	 * @param winThickness winThickness
	 * @param sourceatt sourceatt
	 */
	public static void gamaSim(int photon_number, int sourcecode,
			int sourcegeometrycode, int detectorcode, double gammaenergy,
			double det_radius, double source_radius, double det_height,
			double source_height, double source_inner_radius,
			double source_height_up,
			// ////////////////////////////////////
			double deltazup, double deltazdown, double deltaxy,
			String[] winDetType, double[] winThickness,
			// ////////////////////////////////////
			boolean sourceatt)

	{
		hwin00 = deltazup;
		geomB = false;
		adet = det_radius;// detector radius
		asource = source_radius;// source radius
		hdet = det_height;// detector height
		hsource = source_height;// source height
		// ---------Marinelli-----------------
		hsourceup = source_height_up;// upper source height---MARINELLI
		bsource = source_inner_radius;// inner radius of beaker---MARINELLI
		// --------------------------------------------
		long startSimulationTime = System.currentTimeMillis();
		simulationTimeElapsed = "";
		int index_low = 0;
		int index_high = 0;
		double ph_interp = 0.0;// interpolation
		// double incoh_interp=0.0;//Compton=incoherent scattering!!
		// double incoh_probab=0.0;//probability
		// double coh_probab=0.0;//probability
		// double coh_interp=0.0;//RAYLEIGH=coherent scattering!!
		double total_interp = 0.0;// interpolation
		// double ph_probab=0.0;//probability
//		double r = 0.0;// [0,1] random number
//		double r0 = 0.0;// [0,1] random number
		double dist_to_int = 0.0;// store distance to interaction
//		double phi = 0.0;// store the azimutal angle
		// double phi2=0.0;//store the azimutal angle for 2nd annihlilation
		// photon
		// double [] newcoord;//new coordinates
		double e_incident = 0.0;// init
		// double e_scatt=0.0;//store the energy of the Compton scattered
		// photons
		// double endep=0.0;//temporary energy deposition of each photon
		double[] oldcrd = new double[6];// old coordinates
		// double [] newcoord2=new double[6];;//new coordinates for 2nd
		// annihilation photon
		// int nrepeat=10;//repeat 10 times for unc. eval
		// effind=new double [nrepeat];
	//	double effmed = 0.0;
	//	double efferr = 0.0;
		// double fwhm=0.0;//error in peak energy due to "electronics"
		// here is the left side of peak at 95% confidence (2*fwhm/2)
		efficiency = new double[2];
		efficiency[0] = 0.0;
		efficiency[1] = 0.0;

		pefficiency = new double[2];
		pefficiency[0] = 0.0;
		pefficiency[1] = 0.0;

		//ouble theta = 0.0;// store the Compton simulation's scattering angle
		// double theta2=0.0;//store the polar angle for 2nd annihlilation
		// photon
		//int n_photons = 0;// number of photons being transported
		// ------read attenuation coefficient and density----------
		// source coefficients
		double sattc_interp = 0.0;
		int scod = sourcecode + 1;// handle further reading from the following
									// general table ([i][0]=first= energy)
		double[][] sourcecoeftable = (double[][]) resources
				.getObject("source.attenuationCoef");
		double[] sourcedensity = (double[]) resources
				.getObject("source.densities");// scod-1 will go
		// the source total linear attenuation coefficient
		double smiu = 0.;
		double smius = sourcedensity[scod - 1];// *mass total
												// ....@@@@@@@@@@@@@@@@@@@@@
		// detector coeficients
		double[][] coeftable = new double[0][0];
		double[] density = (double[]) resources.getObject("detector.densities");
		// detectorcode=0->NaI and 1->Ge
		if (detectorcode == 0) {
			coeftable = (double[][]) resources
					.getObject("detector.nai.attenuationCoef");
			// fwhm=(53.0*Math.sqrt(gammaenergy*1000/661.66))/1000;//in MeV!!
			// System.out.println(""+fwhm);
		} else if (detectorcode == 1) {
			coeftable = (double[][]) resources
					.getObject("detector.ge.attenuationCoef");
			// fwhm=(0.6097+0.0011*gammaenergy*1000)/1000;//in MeV!!
		} else// anyother option->NaI
		{
			coeftable = (double[][]) resources
					.getObject("detector.nai.attenuationCoef");
			// fwhm=(53.0*Math.sqrt(gammaenergy*1000/661.66))/1000;//in MeV!!
		}
		double pdmiu = 0.;
		double dmiu = 0.;
		double dmius = density[detectorcode];// density*mass total
												// ....@@@@@@@@@@@@@@@@@@@@@@
		// coeftable[i][0])-->energy in MeV;
		// coeftable[i][2])-->Compton att. coef in cm2/g;
		// coeftable[i][3])-->Photoelectric abs. coef in cm2/g;etc.
		// ------read data and build arrays-------------------------------------
		double[] energy = new double[coeftable.length];
		// double[] coh_coeff= new double[coeftable.length];
		// double[] incoh_coeff= new double[coeftable.length];
		double[] ph_coeff = new double[coeftable.length];
		// double[] pairn_coeff= new double[coeftable.length];
		// double[] paire_coeff= new double[coeftable.length];
		double[] total_coeff = new double[coeftable.length];
		double[] satt_coeff = new double[sourcecoeftable.length];

		for (int i = 0; i < energy.length; i++) {
			energy[i] = coeftable[i][0];
			// coh_coeff[i]=coeftable[i][1];
			// incoh_coeff[i]=coeftable[i][2];
			ph_coeff[i] = coeftable[i][3];
			// pairn_coeff[i]=coeftable[i][4];
			// paire_coeff[i]=coeftable[i][5];
			total_coeff[i] = coeftable[i][7];// coeftable[i][6];
			satt_coeff[i] = sourcecoeftable[i][scod];
		}
		// ---------------END--------------------------------------------------
		//boolean indet = false;// in detector?
		// double rcoh=0.0;//another ALEATOR NUMBER
		// double rpair=0.0;//another ALEATOR NUMBER
		// n_photoefect=0;
		// n_pair=0;
		n_total_all = 0;
		// ------control test var
		ngood = 0;
		nzer = 0;
		nneg = 0;
		allnneg = 0;
		dummy1 = 0;
		dummy2 = 0;
		dummy22 = 0;
		dummy3 = 0;
		// ---------
		// n_fwhm=0;
		// -------MONTE CARLO
		// SIMULATION---------------------------------------------------
		// for (int j=0; j<nrepeat; j++)//run 10 times for uncertainty
		// evaluation
		// {
		// temporary zero init
		// n_total=0;//for each eff eval
		pondt = 0.0;
		pondt2 = 0.0;
		ppondt = 0.0;
		ppondt2 = 0.0;

		// ----------MC BASED LOOP---------------------------
		for (int i = 0; i < photon_number; i++) {
			// npairprod=0;//init
			// endep=0.0;//energy deposited by current photon
			// n_total++;
			// n_total_all++;
			source_sample();// compute ein from spectrum
			e_incident = ein;// gammaenergy;
			// -----------getting initial attenuation coeficient
			Sort.findNearestValue(energy, e_incident, true);
			index_low = Sort.getNearestPosition();
			if (index_low < energy.length - 1)
				index_high = index_low + 1;
			else
				index_high = index_low;
			sattc_interp = linInt(energy[index_high], satt_coeff[index_high],
					energy[index_low], satt_coeff[index_low], e_incident);
			smiu = smius * sattc_interp;// smiu=smiu*sattc_interp;
			// ---------END------------------------------------
			// ---------getting initial evaluation parameters------
			if (sourcegeometrycode == 0)// cylinder
			{
				oldcrd = getCylinderRandom2(deltazup, winDetType, winThickness,
						e_incident);
				// indet=atSurfaceDet(oldcrd);
			} else if (sourcegeometrycode == 1)// Marrinelli
			{
				oldcrd = getMarrinelliRandom2(deltazup, deltazdown, deltaxy,
						winDetType, winThickness, e_incident);
				// indet=atSurfaceDet(oldcrd);
			} else// default=cylinder
			{
				oldcrd = getCylinderRandom2(deltazup, winDetType, winThickness,
						e_incident);
				// indet=atSurfaceDet(oldcrd);
			}
			if (sourceatt)
				pond = pond * Math.exp(-smiu * source_parcurs);// decreasing
																// weight due to
																// attenuation
																// in sorce
			// volume. attenuation in air, in source and detector walls etc. are
			// neglected
			// ------------END-----------------------------------
			dist_to_int = getTraversedDistance(oldcrd);
			// dist_to_int=getDistPctif(oldcrd);
			if (stopB)
				return;

			// evaluate ph_probab,ph_interp,compton_interp,total_interp for
			// given energy
			Sort.findNearestValue(energy, e_incident, true);
			index_low = Sort.getNearestPosition();
			if (index_low < energy.length - 1)
				index_high = index_low + 1;
			else
				index_high = index_low;
			// --------------choose interaction coefficients and interaction
			// probabilities----
			ph_interp = linInt(energy[index_high], ph_coeff[index_high],
					energy[index_low], ph_coeff[index_low], e_incident);
			// incoh_interp=linInt(energy[index_high], incoh_coeff[index_high],
			// energy[index_low],
			// incoh_coeff[index_low] ,e_incident);
			total_interp = linInt(energy[index_high], total_coeff[index_high],
					energy[index_low], total_coeff[index_low], e_incident);
			// coh_interp=linInt(energy[index_high], coh_coeff[index_high],
			// energy[index_low],
			// coh_coeff[index_low] ,e_incident);
			// incoh_probab=(incoh_interp)/(total_interp-ph_interp-coh_interp);//incoh
			// from remaining
			// coh_probab=(coh_interp)/(total_interp-ph_interp);//coh from
			// REMAINING
			// ph_probab=ph_interp/total_interp;//absortion from total
			// ---------------END-----------------------------------------------------------
			dmiu = dmius * total_interp;// dmiu=dmiu*total_interp;
			pdmiu = dmius * ph_interp;

			// r=RandomCollection.random01();
			// double rdist=RandomCollection.random01();
			// while(rdist==0.0)
			// rdist=RandomCollection.random01();
			// dist_to_int=-Math.log(rdist)/(dmiu);//[cm]!!@distance to
			// interaction

			if (dist_to_int > 0)// we have valid traversed
			{
				// S(E)=q1q2(1-exp(-µNaI(E)·W))
				ppond = pond * (1.0 - Math.exp(-pdmiu * dist_to_int));
				pond = pond * (1.0 - Math.exp(-dmiu * dist_to_int));
				pondt = pondt + pond;// SUM S(E)
				pondt2 = pondt2 + pond * pond;// SUM S(E)*S(E)

				ppondt = ppondt + ppond;// SUM S(E)
				ppondt2 = ppondt2 + ppond * ppond;// SUM S(E)*S(E)

				n_total_all++;
			}
			// }//end while
		}// end for i=0 to photon_number
			// ---------STAT-----------------------------
		pondt = pondt / photon_number;
		pondt2 = pondt2 / photon_number;
		pondt2 = (pondt2 - pondt * pondt) / (photon_number - 1.0);
		if (pondt2 >= 0.)
			pondt2 = Math.sqrt(pondt2);
		if (pondt != 0.) {
			pondt2 = Math.min(pondt2 / pondt * 100., 99.9);
		} else {
			pondt2 = 99.9;
		}// proc!

		efficiency[0] = pondt;
		efficiency[1] = 2.0 * pondt2;// %...2 sigma error, 95% IT IS SIGMA NOT
										// OF MEAN!!

		ppondt = ppondt / photon_number;
		ppondt2 = ppondt2 / photon_number;
		ppondt2 = (ppondt2 - ppondt * ppondt) / (photon_number - 1.0);
		if (ppondt2 >= 0.)
			ppondt2 = Math.sqrt(ppondt2);
		if (ppondt != 0.) {
			ppondt2 = Math.min(ppondt2 / ppondt * 100., 99.9);
		} else {
			ppondt2 = 99.9;
		}// proc!

		pefficiency[0] = ppondt;
		pefficiency[1] = 2.0 * ppondt2;// %...2 sigma error, 95% IT IS SIGMA NOT
										// OF MEAN!!

		simulationTimeElapsed = timeElapsed(startSimulationTime);
	}

	// ---------------------------------------------------------------------
	/**
	 * Not used anymore. Old algorithm
	 * @param coord coord
	 * @return the result
	 */
	@Deprecated
	public static double getDistPctif(double[] coord) {
		double result = -1.0;

	//	double x0 = coord[0];
	//	double y0 = coord[1];
	//	double z0 = coord[2];
	//	double ux = coord[3];
	//	double uy = coord[4];
		double uz = coord[5];

		double tgtet1 = adet / (hwin00 + hdet);
		double teta1 = Math.abs(Math.atan(tgtet1));// -pi/2,pi/2
		double costet1 = Math.cos(teta1);

		double tgtet2 = adet / (hwin00);
		double teta2 = Math.abs(Math.atan(tgtet2));// -pi/2,pi/2
		double costet2 = Math.cos(teta2);

		double costet = Math.abs(uz);
		double sintet = Math.sqrt(1.0 - costet * costet);

		if (costet >= costet1)// teta<teta1
		{
			result = hdet / costet;
		} else if ((costet1 > costet) && (costet > costet2)) {
			result = Math.abs((adet / sintet) - (hwin00 / costet));
		}

		return result;
	}

	// ==========================================================================================
	/**
	 * Gather all data and call gamaSim to perform efficiency calculations! 
	 */
	private void performEfficiencyCalculation() {
		reset();
		simTa.selectAll();
		simTa.replaceSelection("");
		String s = "";
		boolean neg = false;
		int i = 0;

		NCASE = stringToInt((String) nPhotonsCb.getSelectedItem());// 20000;

		try {
			hdet = stringToDouble(hdetTf.getText());// 6.3;
			if (hdet < 0)
				neg = true;
			adet = stringToDouble(adetTf.getText());// 6.3/2;
			adet = adet / 2.0;
			if (adet < 0)
				neg = true;
			inac_thick_sup = stringToDouble(hUpTf.getText());// 0.50;
			if (inac_thick_sup < 0)
				neg = true;
			winthick = stringToDouble(windowThickTf.getText());// 0.05;
			if (winthick < 0)
				neg = true;
			// ==========================================================
			// note errors are also handle below!! h>,<,=hup??
			hsource = stringToDouble(hsourceTf.getText());// 8.6;
			if (hsource < 0)
				neg = true;
			hsourceup = stringToDouble(hsourceupTf.getText());// 3.8;
			if (hsourceup < 0)
				neg = true;
			asource = stringToDouble(asourceTf.getText());// 13.25/2;
			if (asource < 0)
				neg = true;
			asource = asource / 2.0;
			bsource = stringToDouble(bsourceTf.getText());// 8.55/2;
			if (bsource < 0)
				neg = true;
			bsource = bsource / 2.0;
		} catch (Exception ex) {
			s = "Error in detector and source inputs. Insert positive numbers!"
					+ " \n";
			simTa.append(s);
			stopThread();
			return;
		}
		if (neg) {
			s = "Error in detector and source inputs. Insert positive numbers!"
					+ " \n";
			simTa.append(s);
			stopThread();
			return;
		}

		i = sourceEqCb.getSelectedIndex();
		sourcecode = i;// 0 vs 1//GammaDetEff.sourcecode=i+1;//0 vs 1

		i = sourceTypeCb.getSelectedIndex();
		sourcegeometrycode = i;

		i = detTypeCb.getSelectedIndex();
		detectorcode = i;// 0-NaI,1-Ge

		int index = einCb.getSelectedIndex();
		if (index == 0) {
			mono = 0;
			try {
				String eins = energyTf.getText();
				ein = stringToDouble(eins);
			} catch (Exception e) {
				s = "Error in energy inputs. Insert positive numbers!" + " \n";
				simTa.append(s);
				stopThread();
				return;
			}
		}
		if (index == 1) {
			mono = 1;
			enerFilename = spectrumTf.getText();
			if (enerFilename.compareTo("") == 0) {
				s = "Invalid spectrum file!" + " \n";
				simTa.append(s);
				stopThread();
				return;

			}

		}
		energy_inputs();
		deltazup = winthick + inac_thick_sup;
		sourceatt = true;
		double[] winThickness = new double[1];
		winThickness[0] = winthick;
		String[] winDetType = new String[1];
		winDetType[0] = (String) windowCb.getSelectedItem();

		gamaSim(NCASE, sourcecode, sourcegeometrycode, detectorcode,
				1.0,// does not matter here
				adet, asource, hdet, hsource, bsource, hsourceup, deltazup,
				0.0, 0.0,// dummy...but must be 0!
				winDetType, winThickness, sourceatt);
		// ---------------
		// printout resutls===========
		double[] eff = getEfficiency();//GES_hybrid.getEfficiency();

		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumFractionDigits(5);// default e 2 oricum!!
		nf.setMaximumFractionDigits(5);// default e 2 oricum!!
		nf.setGroupingUsed(false);// no 4,568.02 but 4568.02
		//String pattern = "0.###E0";
		//DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
	//DecimalFormat nff = new DecimalFormat(pattern, dfs);

		simTa.append("Gamma global efficiency -number in (0,1)-: "
				+ nf.format(eff[0]) + " +/- " + nf.format(eff[1]) + " % "
				+ " \n");

		// ==============================
		stopThread();// kill all threads
	}

	// ======================================================================================
	/**
	 * Load a spectrum file.
	 */
	private void chooseSpectrum() {
		String ext = "spectrum";// resources.getString("src.load");
		String pct = ".";
		String description = "Spectrum file";
		ExampleFileFilter jpgFilter = new ExampleFileFilter(ext, description);
		String file_sep = System.getProperty("file.separator");
		String datas = "Data" +file_sep+"egs"+ file_sep + "spectra";
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

			int fl = filename.length();
			String test = filename.substring(fl - 9);// exstension lookup!!
			String ctest = pct + ext;
			if (test.compareTo(ctest) == 0)// with extension
			{
				filename = filename.substring(indx + 1, fl - 9);// exstension
																// lookup!!
			} else {
				filename = filename.substring(indx + 1);
			}

			spectrumTf.setText(filename);
			enerFilename = filename;
		}

	}

	/**
	 * Start computation thread
	 */
	private void startThread() {
		if (simTh == null) {
			simTh = new Thread(this);
		}
		simTh.start();
	}

	/**
	 * Stop computation thread
	 */
	private void stopThread() {
		simTh = null;
		/*
		 * if(stopAppend) { EGS4.STOPPROGRAM=true;
		 * simTa.append("Interrupted by the user!!"+" \n"); stopAppend=false; }
		 * FluenceRz.destroyArrays(); SpwrRz.destroyArrays();
		 */
	}

	/**
	 * Thread specific method.
	 */
	public void run() {
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		simTh.setPriority(Thread.NORM_PRIORITY);
		performEfficiencyCalculation();
	}

	/**
	 * All actions are defined here.
	 */
	private void initEvent() {
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == startsimB) {
					startThread();
				}

				// if (evt.getSource()==stopsimB)
				// {
				// stopAppend=true;
				// stopThread();
				// }
				if (evt.getSource() == spectrumB) {
					chooseSpectrum();
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

			}
		};

		ItemListener il = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getSource() == einCb) {
					setEin();
				}
			}
		};

		spectrumB.addActionListener(al);
		startsimB.addActionListener(al);
		// stopsimB.addActionListener(al);
		saveSB.addActionListener(al);
		loadSB.addActionListener(al);
		saveDB.addActionListener(al);
		loadDB.addActionListener(al);

		einCb.addItemListener(il);
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
			mono = 0;
		}

		if (index == 1) {
			spectrumB.setEnabled(true);
			mono = 1;
		}

	}

	/**
	 * Save source
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
		String opens = currentDir + file_sep + datas+file_sep+"egs";
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
			s = s + (String) sourceEqCb.getSelectedItem() + "\n";
			// s=s+sourcematerialTf.getText()+"\n";
			s = s + asourceTf.getText() + "\n";
			s = s + hsourceTf.getText() + "\n";
			s = s + bsourceTf.getText() + "\n";
			s = s + hsourceupTf.getText() + "\n";
			// s=s+sddistTf.getText()+"\n";
			// s=s+pointstodmaterialTf.getText()+"\n";//======================
			// s=s+envelopematerialTf.getText()+"\n";//======================
			// s=s+envelopeThickTf.getText()+"\n";//======================
			// s=s+rbeamTf.getText()+"\n";//======================
			// s=s+anglebeamTf.getText()+"\n";//======================

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			} catch (Exception ex) {

			}

		}

	}

	/**
	 * Save detector
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
		String opens = currentDir + file_sep + datas+file_sep+"egs";
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
			// s=s+adetTotTf.getText()+"\n";
			// s=s+hdetTotTf.getText()+"\n";
			s = s + hUpTf.getText() + "\n";
			// s=s+althickTf.getText()+"\n";
			// s=s+almaterialTf.getText()+"\n";
			// s=s+airmaterialTf.getText()+"\n";
			s = s + (String) detTypeCb.getSelectedItem() + "\n";// naimaterialTf.getText()+"\n";
			s = s + (String) windowCb.getSelectedItem() + "\n";// windowmaterialTf.getText()+"\n";
			s = s + windowThickTf.getText() + "\n";

			try {
				FileWriter sigfos = new FileWriter(filename);
				sigfos.write(s);
				sigfos.close();
			} catch (Exception ex) {

			}
		}

	}

	/**
	 * Load source
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
		String opens = currentDir + file_sep + datas+file_sep+"egs";
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
							sourceEqCb.setSelectedItem(line);
							// sourcematerialTf.setText(line);
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
						/*
						 * if(lnr==7) { sddistTf.setText(line); } if(lnr==8) {
						 * pointstodmaterialTf.setText(line); } if(lnr==9) {
						 * envelopematerialTf.setText(line); } if(lnr==10) {
						 * envelopeThickTf.setText(line); } if(lnr==11) {
						 * rbeamTf.setText(line); } if(lnr==12) {
						 * anglebeamTf.setText(line); }
						 */

						desc = new StringBuffer();

					}
				}
				in.close();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Load detector
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
		String opens = currentDir + file_sep + datas+file_sep+"egs";
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
						/*
						 * if(lnr==3) { adetTotTf.setText(line); } if(lnr==4) {
						 * hdetTotTf.setText(line); }
						 */
						if (lnr == 3)// 5)
						{
							hUpTf.setText(line);
						}
						/*
						 * if(lnr==6) { althickTf.setText(line); } if(lnr==7) {
						 * almaterialTf.setText(line); } if(lnr==8) {
						 * airmaterialTf.setText(line); }
						 */
						if (lnr == 4)// 9)
						{
							detTypeCb.setSelectedItem(line);
							// naimaterialTf.setText(line);
						}
						if (lnr == 5)// 10)
						{
							windowCb.setSelectedItem(line);
							// windowmaterialTf.setText(line);
						}
						if (lnr == 6)// 11)
						{
							windowThickTf.setText(line);
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
	 * Set energy inputs. If spectrum, this routine prepare alias sampling arrays.
	 */
	public static void energy_inputs() {
		if (mono == 1) {
			readSpectrum(enerFilename);
			/*
			 * EGS4.seqStr=" Spectrum file:  "+enerFilename; if(EGS4.iprint>1)
			 * printSequence(EGS4.seqStr);
			 * 
			 * EGS4.seqStr="    HAVE READ"+EGS4.format(NENSRC,5)+
			 * " INPUT ENERGY BINS FROM FILE"; if(EGS4.iprint>1)
			 * printSequence(EGS4.seqStr);
			 */
			if (mode == 0) {
				/*
				 * EGS4.seqStr="      Counts/bin assumed"; if(EGS4.iprint>1)
				 * printSequence(EGS4.seqStr);
				 */
			} else if (mode == 1) {
				/*
				 * EGS4.seqStr="      Counts/MeV assumed"; if(EGS4.iprint>1)
				 * printSequence(EGS4.seqStr);
				 */

				for (int IB = 1; IB <= NENSRC; IB++) {
					SRCPDF[IB - 1] = SRCPDF[IB - 1]
							* (ENSRCD[IB] - ENSRCD[IB - 1]);
				}
			}// "end mode = 1 block"
			else {
				/*
				 * EGS4.seqStr="*****MODE not 0 or 1 in spectrum file? **";
				 * if(EGS4.iprint>1) printSequence(EGS4.seqStr);
				 */
			}

			ein = ENSRCD[NENSRC];// "SET TO MAX ENERGY FOR SOME CHECKS"

			/*
			 * EGS4.seqStr="    ENERGY RANGES FROM"+EGS4.format(enmin,10,true)+
			 * " MeV TO"+EGS4.format(ein,12,true)+" MeV"; if(EGS4.iprint>1)
			 * printSequence(EGS4.seqStr);
			 */

			// enmax = ein;
			// Eave = 0; sum = 0;
			// for(int i=1;i<=NENSRC;i++)
			// {
			// sum = sum + SRCPDF[i-1];
			// Eave = Eave + 0.5*SRCPDF[i-1]*(ENSRCD[i]+ENSRCD[i-1]);
			// }
			// Eave = Eave/sum;
			// call prepare_alias_sampling(nensrc,srcpdf,srcpdf_at,srcbin_at);
			prepare_alias_sampling(NENSRC, SRCPDF, srcpdf_at, srcbin_at);
		}
	}

	/**
	 * Read spectrum data from a file
	 * @param enerfile enerfile
	 */
	public static void readSpectrum(String enerfile) {
		String filename = datas+file_sep+"egs" + file_sep + dataspec + file_sep + enerfile
				+ defaultext;
		int iread = 0;
		int lnr = 0;// data number
		int lnrr = 0;// line number
		int indx = 1;
		StringBuffer desc = new StringBuffer();
		boolean haveData = false;
		//String equals = "=";
		char comma = ',';
		char lineSep = '\n';// System.getProperty("line.separator").charAt(0);

		boolean enB = false;
		boolean srB = false;

		try {
			FileInputStream in = new FileInputStream(filename);

			while ((iread = in.read()) != -1) {
				if (!Character.isWhitespace((char) iread)
						&& ((char) iread != comma)) {
					desc.append((char) iread);
					haveData = true;
				} else {
					if (haveData)// we have data
					{
						// System.out.println("ok");
						haveData = false;// reset
						if (lnrr != 0)// for skip first line
						{
							lnr++;
							// READ(9,*) nensrc,ensrcd(0),mode;
							if (lnr == 1) {
								String s = desc.toString();
								NENSRC = stringToInt(s);// System.out.println(NENSRC);
							}
							if (lnr == 2) {
								String s = desc.toString();
								ENSRCD[0] = stringToDouble(s);// System.out.println(ENSRCD[0]);
							}
							if (lnr == 3) {
								String s = desc.toString();
								mode = stringToInt(s);// System.out.println(mode);
								// enmin = ENSRCD[0];
								if (NENSRC > $NENSRC) {
									// OUTPUT NENSRC,$NENSRC;
									// (//' ********** Asked for too many energy
									// bins******'/
									// ' NENSRC =',I4, ' reduced to max allowed
									// =',I4/1x,30('*')//);
									/*
									 * EGS4.seqStr=
									 * " ********** Asked for too many energy bins******"
									 * ; if(EGS4.iprint>1)
									 * printSequence(EGS4.seqStr);
									 * EGS4.seqStr=" NENSRC ="
									 * +NENSRC+" reduced to max allowed ="
									 * +$NENSRC; //EGS4.STOPPROGRAM=true;
									 * if(EGS4.iprint>1)
									 * printSequence(EGS4.seqStr);
									 */

									NENSRC = $NENSRC;
								}

							} else if (lnr > 3) {
								// READ(9,*)(ENSRCD(IB),SRCPDF(IB),IB=1,NENSRC);
								String s = desc.toString();
								if (!enB) {
									ENSRCD[indx] = stringToDouble(s);
									enB = true;
								} else if (!srB) {
									SRCPDF[indx - 1] = stringToDouble(s);
									// System.out.println(ENSRCD[indx]+"    "+SRCPDF[indx-1]);

									indx++;
									if (indx == NENSRC + 1)
										break;
									srB = true;
									enB = false;
									srB = false;
								}
							}

						}

					}// have data
					if ((char) iread == lineSep)
						lnrr++;
					desc = new StringBuffer();
				}// else
			}// main while
			in.close();
		}// try
		catch (Exception exc) {

		}

	}

	/**
	 * Sample incident energy.
	 */
	private static void source_sample() {
		// "=============================================================="
		// iqin = iqi;
		// irin = 1;
		if (mono == 0 || mono == 2)// | mono = 3 )
		{
			// ein = ei;
			// wtin = 1;
		} else {
			// ein = alias_sample(nensrc,ensrcd,srcpdf_at,srcbin_at);
			ein = alias_sample(NENSRC, ENSRCD, srcpdf_at, srcbin_at);
			// wtin = 1;
			// eik = ein;
			// if( iqin != 0 )
			// { ein = ein + EGS4.RM; }//total energy
		}
		// ecount = ecount + 1;
		// esum = esum + eik;
		// esum2 = esum2 + eik*eik;

		// if( source_type == 0 )
		// {
		// xin = 0; uin = 0; zin = 0;
		// uin = ui; vin = vi; win = wi;
		// }
		// else
		// {
		// double r=EGS4.random01();//$RANDOMSET r;
		// r = rbeam*Math.sqrt(r);
		// double phi=EGS4.random01();//$RANDOMSET phi;
		// phi = 2*phi*Math.PI;
		// xin = r*Math.cos(phi); yin = r*Math.sin(phi);
		// aux = 1/Math.sqrt(xin*xin + yin*yin + distance*distance);
		// uin = xin*aux; vin = yin*aux; win = distance*aux;
		// zin = 0;
		// }

		return;
	}

	/**
	 * Internally used by source_sample. 
	 * @param nsbin nsbin
	 * @param xs_array xs_array
	 * @param ws_array ws_array
	 * @param ibin_array ibin_array
	 * @return the result
	 */
	public static double alias_sample(int nsbin, double[] xs_array,// )
			double[] ws_array, int[] ibin_array) {
		// "===============================================================
		// "
		// " samples from an alias table which must have been prepared
		// " using prepare_alias_table
		// "
		// "===============================================================

		// ;Copyright NRC;
		// implicit none;

		// $INTEGER nsbin,ibin_array(nsbin);
		// $REAL xs_array(0:nsbin),ws_array(nsbin);

		// ;COMIN/RANDOM/;
		double alias_sample = 0.0;
		double v1 = 0.0;
		double v2 = 0.0;
		double aj = 0.0;
		int j = 0;

		v1 = random01();
		v2 = random01();
		aj = 1.0 + v1 * nsbin;
		Double dbl = new Double(aj);
		j = dbl.intValue();// minim1 maxim nsbin!!
		if (j > nsbin)
			j = nsbin; // " this happens only if $RANDOMSET produces
						// " numbers in (0,1]--------> is not the DEFAULT
						// case!!!
		aj = aj - j;
		if (aj > ws_array[j - 1]) {
			j = ibin_array[j - 1];
		}
		alias_sample = (1.0 - v2) * xs_array[j - 1] + v2 * xs_array[j];// ok, xs
																		// =0
																		// biased
		return alias_sample;
	}

	/**
	 * Internally used by energy_inputs.
	 * @param nsbin nsbin
	 * @param fs_array fs_array
	 * @param ws_array ws_array
	 * @param ibin_array ibin_array
	 */
	public static void prepare_alias_sampling(int nsbin, double[] fs_array,
			double[] ws_array, int[] ibin_array) {
		// "====================================================================
		// "
		// " inputs: nsbin: number of bins in the histogram
		// " fs_array: bin probabilities
		// "
		// " Note that we don't need the bin limits at this point, they
		// " are needed for the actual sampling (in alias_sample)
		// "
		// " outputs: ws_array, ibin_array: alias table ready for sampling
		// "
		// "====================================================================

		// ;Copyright NRC;

		// $INTEGER nsbin,ibin_array(nsbin);
		// $REAL fs_array(nsbin),ws_array(nsbin);

		int i = 0;
		int j_l = 0;
		int j_h = 0;
		double sum = 0.0;
		double aux = 0.0;

		boolean exit1b = false;
		boolean exit2b = false;

		sum = 0;
		for (i = 1; i <= nsbin; i++) {
			if (fs_array[i - 1] < 1.e-30) {
				fs_array[i - 1] = 1.e-30;
			}
			ws_array[i - 1] = -fs_array[i - 1];
			ibin_array[i - 1] = 1;
			sum = sum + fs_array[i - 1];
		}
		sum = sum / nsbin;

		// DO i=1,nsbin-1 [
		for (i = 1; i <= nsbin - 1; i++) {
			exit1b = false;
			exit2b = false;
			for (j_h = 1; j_h <= nsbin; j_h++) {
				if (ws_array[j_h - 1] < 0) {
					if (Math.abs(ws_array[j_h - 1]) > sum) {
						// GOTO :AT_EXIT1:;
						exit1b = true;
						break;
					}
				}
			}
			if (!exit1b)
				j_h = nsbin;
			// :AT_EXIT1:

			for (j_l = 1; j_l <= nsbin; j_l++) {
				if (ws_array[j_l - 1] < 0) {
					if (Math.abs(ws_array[j_l - 1]) < sum) {
						// GOTO :AT_EXIT2:;
						exit2b = true;
						break;
					}
				}
			}
			if (!exit2b)
				j_l = nsbin;
			// :AT_EXIT2:

			aux = sum - Math.abs(ws_array[j_l - 1]);
			ws_array[j_h - 1] = ws_array[j_h - 1] + aux;
			ws_array[j_l - 1] = -ws_array[j_l - 1] / sum;
			ibin_array[j_l - 1] = j_h;

			if (i == nsbin - 1) {
				ws_array[j_h - 1] = 1;
			}

		}

		return;
	}

	// =================RANDOM=================================================================
	public static int $NRANMAR = 128;
	public static double[] rng_array1 = new double[$NRANMAR];
	public static int rng_seed = 999999;// "current pointer for rng_array            "
	public static double[] rng_array = new double[24];// "containes 24 random numbers              "
	public static int[] seeds = new int[24];// "for storing the rng state                "
	public static int seedin = 0;
	public static int luxury_level = 0;
	public static int[] state = new int[25];
	public static int carry = 0;// " The state of the generator "
	public static int i24 = 0;
	public static int j24 = 0;// " The rng seeds "
	public static int[] next = new int[24];// " for convinience "
	public static int jseed_dflt = 314159265;// 0;
	public static int nskipRnd = 0;
	public static int icon = 2147483563;// 0;
	public static int status = 0;
	public static int jseed = 0;
	public static int[] nskipll = { 0, 24, 73, 199, 365 };// new int[5];//(0:4),
	public static int icarry = 0;
	public static int kRnd = 0;
	public static int jRnd = 0;
	public static boolean not_initialized = true;
	public static int uni = 0;
	public static double twom24 = 0.0;
	public static double twop24 = 0.0;
	public static int $DEFAULT_LL = 1;// REPLACE {$DEFAULT-LL} WITH {1}
	public static int $MXRNGDIM = 5;// REPLACE {$MXRNGDIM} WITH {5}
	// ranmar
	public static int[] urndm = new int[97];
	public static int crndm = 0;
	public static int cdrndm = 0;
	public static int cmrndm = 0;
	public static int i4opt = 0;
	public static int ixx = 0;
	public static int jxx = 0;
	public static int fool_optimizer = 0;

	/**
	 * Return a random number in [0,1] interval.
	 * @return the result.
	 */
	public static double random01() {
		// To get <0,1> instead of <0,1), we substract the random number from 1
		// in half cases
		double result = 0.0;
		if (RandomUse == 0) {
			double randomNumber = Math.random();
			if (Math.random() < 0.5)
				randomNumber = 1.0 - randomNumber;
			return randomNumber;
		} else if (RandomUse == 1) {
			result = RANDOMSET();
		} else if (RandomUse == 2) {
			result = Math.random();
		}

		return result;
	}

	// ===========
	/**
	 * The EGSnrc implementation for RNG (RandomNumberGenerator).
	 * @return the random number
	 */
	public static double RANDOMSET() {
		double result = 0.0;
		if (ranluxB) {
			if (rng_seed > 24) {
				ranlux(rng_array);
				rng_seed = 1;
			}

			result = rng_array[rng_seed - 1];
			rng_seed = rng_seed + 1;
		} else// ranmar
		{
			if (rng_seed > $NRANMAR)
				ranmar_get();
			result = rng_array1[rng_seed - 1] * twom24;
			rng_seed = rng_seed + 1;
		}

		return result;
		// " i.e. take the rng_seed'th random number from rng_array, "
		// " if all 24 numbers used, generate a new set of 24        "
	}

	/**
	 * Internal use by RANDOMSET.
	 */
	public static void ranmar_get() {
		int i = 0;
		int iopt = 0;
		if (rng_seed == 999999)
			init_ranmar();
		for (i = 1; i <= $NRANMAR; i++) {
			iopt = urndm[ixx - 1] - urndm[jxx - 1];
			if (iopt < 0)
				iopt = iopt + 16777216;
			urndm[ixx - 1] = iopt;
			ixx = ixx - 1;
			jxx = jxx - 1;
			if (ixx == 0) {
				ixx = 97;
			} else if (jxx == 0) {
				jxx = 97;
			}
			crndm = crndm - cdrndm;
			if (crndm < 0)
				crndm = crndm + cmrndm;
			iopt = iopt - crndm;
			if (iopt < 0)
				iopt = iopt + 16777216;
			rng_array1[i - 1] = iopt;
		}
		rng_seed = 1;
		return;
	}

	/**
	 * Internal use by ranmar_get.
	 */
	public static void init_ranmar() {
		int s = 0;
		int t = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int m = 0;
		int ii = 0;
		int jj = 0;

		if (ixx <= 0 || ixx > 31328)
			ixx = 1802; // "Sets Marsaglia default"
		if (jxx <= 0 || jxx > 30081)
			jxx = 9373; // "sets Marsaglia default"

		i = ixx / 177;
		i = i % 177;
		i = i + 2;// i = mod(ixx/177,177) + 2;
		j = ixx % 177;
		j = j + 2;// j = mod(ixx, 177) + 2;
		k = jxx / 169;
		k = k % 178;
		k = k + 1;// k = mod(jxx/169,178) + 1;
		l = jxx % 169;// l = mod(jxx, 169) ;

		for (ii = 1; ii <= 97; ii++) {

			s = 0;
			t = 8388608;// "t is 2**23 i.e. half of the maximum allowed"
						// "(note that only 24 bits are used)          "

			for (jj = 1; jj <= 24; jj++) {

				// "The if( fool_optimizer ...) statements below are"
				// "to prevent re-arangement of statements for high "
				// "level optimizations and thus different sequences"
				// "on different architectures                      "
				m = i * j;
				m = m % 179;
				m = m * k;
				m = m % 179;
				// m = mod(mod(i*j,179)*k,179);
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				i = j;
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				j = k;
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				k = m;
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				l = 53 * l + 1;
				l = l % 169;// l = mod(53*l+1,169);
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				int lm = l * m;
				lm = lm % 64;
				if (lm >= 32)
					s = s + t;// IF(mod(l*m,64) >= 32) s = s + t;
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
				t = t / 2;
				// IF( fool_optimizer = 999 ) [ write(6,*) i,j,k,m,s,t; ]
			}
			urndm[ii - 1] = s;
		}

		crndm = 362436;
		cdrndm = 7654321;
		cmrndm = 16777213;

		twom24 = 1. / 16777216.;

		ixx = 97;
		jxx = 33;

		rng_seed = $NRANMAR + 1;

		return;
	}

	/**
	 * Internal use by RANDOMSET
	 * @param rng_array rng_array
	 */
	public static void ranlux(double[] rng_array) {

		if (not_initialized) {
			not_initialized = false;
			nskipRnd = nskipll[$DEFAULT_LL];
			twom24 = 1.0;
			twop24 = 1.0;
			jseed = jseed_dflt;
			for (jRnd = 1; jRnd <= 24; jRnd++) {
				twom24 = twom24 * 0.5;
				twop24 = twop24 * 2.0;
				kRnd = jseed / 53668;
				jseed = 40014 * (jseed - kRnd * 53668) - kRnd * 12211;
				if (jseed < 0) {
					jseed = jseed + icon;
				}
				seeds[jRnd - 1] = jseed % 16777216;// mod(jseed,16777216);
				next[jRnd - 1] = jRnd - 1;
			}
			next[0] = 24;// next(1) = 24;
			i24 = 24;
			j24 = 10;
			carry = 0;
			// if( seeds(24) = 0 )
			if (seeds[23] == 0) {
				carry = 1;
			}
		}

		for (jRnd = 1; jRnd <= 24; jRnd++) // j=1,24
		{
			uni = seeds[j24 - 1] - seeds[i24 - 1] - carry;// seeds(j24) -
															// seeds(i24) -
															// carry;
			if (uni < 0) {
				uni = uni + 16777216;
				carry = 1;
			} else {
				carry = 0;
			}
			seeds[i24 - 1] = uni;// seeds(i24) = uni;
			// "IF( uni = 0 ) [ uni = twom24*twom24; ]"
			i24 = next[i24 - 1];// i24 = next(i24);
			j24 = next[j24 - 1];// j24 = next(j24);
			if (uni >= 4096) {
				rng_array[jRnd - 1] = uni * twom24;// rng_array(j) = uni*twom24;
			} else {
				// rng_array(j) = uni*twom24 + seeds(j24)*twom24*twom24;
				rng_array[jRnd - 1] = uni * twom24 + seeds[j24 - 1] * twom24
						* twom24;
			}
		}

		if (nskipRnd > 0) {
			for (jRnd = 1; jRnd <= nskipRnd; jRnd++)// DO jRnd=1,nskipRnd
			{
				// uni = seeds(j24) - seeds(i24) - carry;
				uni = seeds[j24 - 1] - seeds[i24 - 1] - carry;
				if (uni < 0) {
					uni = uni + 16777216;
					carry = 1;
				} else {
					carry = 0;
				}
				seeds[i24 - 1] = uni;// seeds(i24) = uni;
				i24 = next[i24 - 1];// i24 = next[i24];
				j24 = next[j24 - 1];// j24 = next[j24];
			}
		}
		return;
	}

	/**
	 * Initialize ranlux.
	 * @param luxury_level1 luxury_level1
	 * @param seedin1 seedin1
	 */
	public static void init_ranlux(int luxury_level1, int seedin1) {
		seedin = seedin1;// $$$$$$$$$$$
		luxury_level = luxury_level1;// $$$$$$$$$$$

		jseed = seedin;
		if (jseed <= 0)
			jseed = jseed_dflt;
		if ((luxury_level < 0) || (luxury_level > 4)) {
			luxury_level = $DEFAULT_LL;
		}

		nskipRnd = nskipll[luxury_level];

		// OUTPUT luxury_level,jseed;
		// System.out.println(" ** RANLUX initialization **"+
		// " luxury level: "+luxury_level+
		// " initial seed: "+jseed+
		// "*******");

		not_initialized = false;
		twom24 = 1;
		twop24 = 1;
		for (jRnd = 1; jRnd <= 24; jRnd++) {
			twom24 = twom24 * 0.5;
			twop24 = twop24 * 2.0;
			kRnd = jseed / 53668;
			jseed = 40014 * (jseed - kRnd * 53668) - kRnd * 12211;
			if (jseed < 0) {
				jseed = jseed + icon;
			}
			seeds[jRnd - 1] = jseed % 16777216;// mod(jseed,16777216);
			next[jRnd - 1] = jRnd - 1;
		}
		next[0] = 24;
		i24 = 24;
		j24 = 10;
		carry = 0;
		if (seeds[23] == 0) {
			carry = 1;
		}

		return;
	}

	/**
	 * Get ranlux state based on seeds. It is stored in input state variable.
	 * @param state state
	 */
	public static void get_ranlux_state(int[] state) {

		for (jRnd = 1; jRnd <= 24; jRnd++) {
			state[jRnd - 1] = seeds[jRnd - 1];
		}
		state[24] = i24 + 100 * (j24 + 100 * nskipRnd);
		if (carry > 0)
			state[24] = -state[24];
		return;
	}

	/**
	 * Set ranlux status.
	 * @param state state
	 */
	public void set_ranlux_state(int[] state)// public static void
												// set_ranlux_state(int[] state)
	{
		twom24 = 1;
		twop24 = 1;
		for (jRnd = 1; jRnd <= 24; jRnd++) {
			twom24 = twom24 * 0.5;
			twop24 = twop24 * 2;
			next[jRnd - 1] = jRnd - 1;
		}
		next[0] = 24;
		for (jRnd = 1; jRnd <= 24; jRnd++) {
			seeds[jRnd - 1] = state[jRnd - 1];
		}
		if (state[24] <= 0) {
			status = -state[24];
			carry = 1;
		} else {
			status = state[24];
			carry = 0;
		}
		nskipRnd = status / 10000;
		status = status - nskipRnd * 10000;
		j24 = status / 100;
		i24 = status - 100 * j24;
		if (((j24 < 1) || (j24 > 24)) || ((i24 < 1) || (i24 > 24))) {
			// stop;
			/*
			 * STOPPROGRAM=true;
			 * 
			 * seqStr=
			 * "*** Error in set_ranlux_state: seeds outside of allowed range!"
			 * +" \n"+ "   status = "+state[24]+" \n"+
			 * "   nskip = "+nskipRnd+" \n"+ "   i24 = "+i24+" \n"+
			 * "   j24 = "+j24;//+" \n"; //if(iprint>1)
			 * eq.printSequence(seqStr);
			 */

			return;
		}
		not_initialized = false;
		return;
	}

	/**
	 * Show summary of Ranlux seeds
	 */
	public static void show_ranlux_seeds() {
		if (carry > 0) {
			icarry = 1;
		} else {
			icarry = 0;
		}
		/*
		 * seqStr="' skip = "+format(nskipRnd,4)+",  ix jx = "+format(i24,3)+" ,"
		 * +format(j24,3)+ " carry = "+format(icarry,2); if(iprint>1)
		 * eq.printSequence(seqStr);
		 */

	}

	/**
	 * Show summary of RNG state.
	 */
	public static void SHOW_RNG_STATE() {
		if (ranluxB) {
			show_ranlux_seeds();
		} else// ranmar
		{
			/*
			 * seqStr="  ixx jxx = "+format(ixx,4)+" ,"+format(jxx,4);
			 * if(iprint>1) eq.printSequence(seqStr);
			 */
		}
	}

	// ==========
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

	/**
	 * Reset all variables for re-use.
	 */
	public static void reset() {
		mono = 0;// spectrum or 0=monoen
		// enerFilename="";
		// $NENSRC=300;// "MAX # OF POINTS IN ENERGY DISTRIBUTION  "
		// NENSRC=0;
		// ENSRCD=new double[$NENSRC+1];//(0:$NENSRC)
		// SRCPDF=new double[$NENSRC];//($NENSRC)
		// srcpdf_at=new double[$NENSRC];//($NENSRC),
		// srcbin_at=new int[$NENSRC];//($NENSRC)
		// mode=0;
		// ein=0.662;
		inac_thick_sup = 0.0;

		sourcecode = 0;
		sourcegeometrycode = 0;
		detectorcode = 0;// 0->NaI and 1->Ge
		NCASE = 0;
		sourceatt = true;
		deltazup = 0.0;

		RandomUse = 1;// if 0- old Java random
		ranluxB = true;// use ranlux if true, ranmar otherwise

		simulationTimeElapsed = "";// store simulation time elapsed
		n_total_all = 0;// stores the total number of simulated photons

		pondt = 0.0;// total weight of photon
		pondt2 = 0.0;
		pond = 0.0;// reprezents the geometric efficiency. This will be added to

		source_parcurs = 0.0;

		adet = 0.0;// detector radius
		asource = 0.0;// source radius
		hdet = 0.0;// detector height
		hsource = 0.0;// source height
		hsourceup = 0.0;// upper source height---MARINELLI
		bsource = 0.0;// inner radius of beaker---MARINELLI

		winthick = 0.0;

		stopB = false;// &&&&&&&&&&&
		geomB = false;// &&&&&&&&&&&
		ppondt = 0.0;// total weight of photon
		ppondt2 = 0.0;
		ppond = 0.0;// reprezents the geometric efficiency. This will be added
					// to

		rng_seed = 999999;
		jseed_dflt = 314159265;
		icon = 2147483563;
		rng_array = new double[24];
		rng_array1 = new double[$NRANMAR];
		seeds = new int[24];
		seedin = 0;
		luxury_level = 0;
		state = new int[25];
		carry = 0;
		i24 = 0;
		j24 = 0;
		next = new int[24];
		nskipRnd = 0;
		status = 0;
		jseed = 0;
		icarry = 0;
		kRnd = 0;
		jRnd = 0;
		not_initialized = true;
		uni = 0;
		twom24 = 0.0;
		twop24 = 0.0;
		urndm = new int[97];
		crndm = 0;
		cdrndm = 0;
		cmrndm = 0;
		i4opt = 0;
		ixx = 0;
		jxx = 0;
		fool_optimizer = 0;
	}
}
