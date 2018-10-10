package gammaBetaMc.resources;

import java.util.ListResourceBundle;

/**
 * Resources for GES_hybrid 
 * 
 * @author Dan Fulea, 16 APR. 2009
 */
public class GES_hybridResources extends ListResourceBundle {
	
	/**
	 * Returns the array of strings in the resource bundle.
	 * 
	 * @return the resources.
	 */
	public Object[][] getContents() {
		return CONTENTS;
	}

	/** The resources to be localised. */
	static final Object[][] CONTENTS = {

			{ "source.attenuationCoef", new double[][]
					// energy, Total(+scatter_coh)in cm2/g for multiple source
					// type
					// MeV, water, NaCl soil ciment
					{
							{ 1.000E-03, 4.077E+03, 1.975E+03, 3.179E+03,
									3.466E+03 },
							{ 2.000E-03, 6.173E+02, 8.724E+02, 1.219E+03,
									1.368E+03 },
							{ 3.000E-03, 1.928E+02, 1.093E+03, 4.119E+02,
									4.646E+02 },
							{ 4.000E-03, 8.277E+01, 5.159E+02, 1.905E+02,
									2.188E+02 },
							{ 5.000E-03, 4.259E+01, 2.836E+02, 1.045E+02,
									1.401E+02 },
							{ 6.000E-03, 2.464E+01, 1.723E+02, 6.213E+01,
									8.401E+01 },
							{ 8.000E-03, 1.037E+01, 7.709E+01, 2.964E+01,
									3.879E+01 },
							{ 1.000E-02, 5.330E+00, 4.086E+01, 1.557E+01,
									2.045E+01 },
							{ 2.000E-02, 8.098E-01, 5.505E+00, 2.156E+00,
									2.806E+00 },
							{ 3.000E-02, 3.756E-01, 1.755E+00, 7.666E-01,
									9.601E-01 },
							{ 4.000E-02, 2.683E-01, 8.335E-01, 4.246E-01,
									5.058E-01 },
							{ 5.000E-02, 2.269E-01, 5.036E-01, 3.000E-01,
									3.412E-01 },
							{ 6.000E-02, 2.059E-01, 3.558E-01, 2.424E-01,
									2.660E-01 },
							{ 8.000E-02, 1.837E-01, 2.342E-01, 1.917E-01,
									2.014E-01 },
							{ 1.000E-01, 1.707E-01, 1.867E-01, 1.690E-01,
									1.738E-01 },
							{ 1.500E-01, 1.505E-01, 1.423E-01, 1.424E-01,
									1.436E-01 },
							{ 2.000E-01, 1.370E-01, 1.240E-01, 1.279E-01,
									1.282E-01 },
							{ 3.000E-01, 1.186E-01, 1.044E-01, 1.099E-01,
									1.097E-01 },
							{ 4.000E-01, 1.061E-01, 9.262E-02, 9.807E-02,
									9.783E-02 },
							{ 5.000E-01, 9.687E-02, 8.421E-02, 8.940E-02,
									8.915E-02 },
							{ 6.000E-01, 8.956E-02, 7.772E-02, 8.262E-02,
									8.236E-02 },
							{ 8.000E-01, 7.866E-02, 6.811E-02, 7.251E-02,
									7.227E-02 },
							{ 1.000E+00, 7.072E-02, 6.117E-02, 6.517E-02,
									6.495E-02 },
							{ 1.250E+00, 6.323E-02, 5.468E-02, 5.827E-02,
									5.807E-02 },
							{ 1.500E+00, 5.754E-02, 4.984E-02, 5.305E-02,
									5.288E-02 },
							{ 2.000E+00, 4.942E-02, 4.310E-02, 4.567E-02,
									4.557E-02 },
							{ 3.000E+00, 3.969E-02, 3.547E-02, 3.698E-02,
									3.701E-02 }, } },

			{ "source.densities", new double[]
					// in g/cm3
					// water, NaCl, soil, ciment
					{ 1.0, 2.1, 1.5, 2.29999995 } },

			// Coeficienti de atenuare totali si partiali
			// based on XCOM v.3.1 software developped by NIST (National
			// Institute of Standards
			// and Technology)-->Martin J. Berger,Stephen M. Seltzer and John H.
			// Hubbell
			// last update of software and database:June 1999
			// Partial Interaction Coefficients and Total Attenuation
			// Coefficients
			// energy->MeV and coefficients ->cm2/g.
			// with Tl 0.27% percent by weight!!!
			{
					"detector.nai.attenuationCoef",
					new double[][]
					// energy, Rayleigh, Compton, Photoel., Pair nuc, Pair
					// el.field Total(), TOTAL-RAYLEIGH
					{
							{ 1.000E-03, 7.435E+00, 5.910E-03, 7.787E+03,
									0.000E+00, 0.000E+00, 7.794E+03, 7.787E+03 },
							{ 2.000E-03, 6.569E+00, 1.539E-02, 1.915E+03,
									0.000E+00, 0.000E+00, 1.922E+03, 1.915E+03 },
							{ 3.000E-03, 5.699E+00, 2.457E-02, 7.043E+02,
									0.000E+00, 0.000E+00, 7.100E+02, 7.043E+02 },
							{ 4.000E-03, 4.958E+00, 3.289E-02, 3.375E+02,
									0.000E+00, 0.000E+00, 3.425E+02, 3.375E+02 },
							{ 5.000E-03, 4.345E+00, 4.022E-02, 7.276E+02,
									0.000E+00, 0.000E+00, 7.320E+02, 7.276E+02 },
							{ 6.000E-03, 3.825E+00, 4.674E-02, 5.295E+02,
									0.000E+00, 0.000E+00, 5.334E+02, 5.296E+02 },
							{ 8.000E-03, 3.017E+00, 5.793E-02, 2.488E+02,
									0.000E+00, 0.000E+00, 2.519E+02, 2.489E+02 },
							{ 1.000E-02, 2.444E+00, 6.725E-02, 1.375E+02,
									0.000E+00, 0.000E+00, 1.400E+02, 1.376E+02 },
							{ 2.000E-02, 1.139E+00, 9.456E-02, 2.078E+01,
									0.000E+00, 0.000E+00, 2.202E+01, 2.088E+01 },
							{ 3.000E-02, 6.463E-01, 1.066E-01, 6.665E+00,
									0.000E+00, 0.000E+00, 7.418E+00, 6.771E+00 },
							{ 4.000E-02, 4.209E-01, 1.126E-01, 1.822E+01,
									0.000E+00, 0.000E+00, 1.876E+01, 1.834E+01 },
							{ 5.000E-02, 2.987E-01, 1.156E-01, 1.005E+01,
									0.000E+00, 0.000E+00, 1.047E+01, 1.017E+01 },
							{ 6.000E-02, 2.229E-01, 1.168E-01, 6.106E+00,
									0.000E+00, 0.000E+00, 6.446E+00, 6.223E+00 },
							{ 8.000E-02, 1.370E-01, 1.165E-01, 2.744E+00,
									0.000E+00, 0.000E+00, 2.998E+00, 2.861E+00 },
							{ 1.000E-01, 9.274E-02, 1.143E-01, 1.472E+00,
									0.000E+00, 0.000E+00, 1.679E+00, 1.586E+00 },
							{ 1.500E-01, 4.506E-02, 1.071E-01, 4.627E-01,
									0.000E+00, 0.000E+00, 6.149E-01, 5.698E-01 },
							{ 2.000E-01, 2.664E-02, 1.000E-01, 2.036E-01,
									0.000E+00, 0.000E+00, 3.302E-01, 3.036E-01 },
							{ 3.000E-01, 1.242E-02, 8.858E-02, 6.540E-02,
									0.000E+00, 0.000E+00, 1.664E-01, 1.540E-01 },
							{ 4.000E-01, 7.153E-03, 8.008E-02, 3.016E-02,
									0.000E+00, 0.000E+00, 1.174E-01, 1.102E-01 },
							{ 5.000E-01, 4.640E-03, 7.349E-02, 1.701E-02,
									0.000E+00, 0.000E+00, 9.514E-02, 9.050E-02 },
							{ 6.000E-01, 3.251E-03, 6.820E-02, 1.090E-02,
									0.000E+00, 0.000E+00, 8.236E-02, 7.911E-02 },
							{ 8.000E-01, 1.848E-03, 6.011E-02, 5.647E-03,
									0.000E+00, 0.000E+00, 6.760E-02, 6.575E-02 },
							{ 1.000E+00, 1.189E-03, 5.412E-02, 3.528E-03,
									0.000E+00, 0.000E+00, 5.884E-02, 5.765E-02 },
							{ 1.250E+00, 7.638E-04, 4.845E-02, 2.265E-03,
									1.640E-04, 0.000E+00, 5.164E-02, 5.087E-02 },
							{ 1.500E+00, 5.314E-04, 4.406E-02, 1.621E-03,
									7.622E-04, 0.000E+00, 4.698E-02, 4.645E-02 },
							{ 2.000E+00, 2.997E-04, 3.763E-02, 9.865E-04,
									2.582E-03, 0.000E+00, 4.150E-02, 4.120E-02 },
							{ 3.000E+00, 1.334E-04, 2.963E-02, 5.233E-04,
									6.533E-03, 1.036E-05, 3.683E-02, 3.669E-02 }, } },

			{
					"detector.ge.attenuationCoef",
					new double[][]
					// energy, Rayleigh, Compton, Photoel., Pair nuc, Pair
					// el.field Total(),TOTAL-COHERENT(RAYLEIGH)
					{
							{ 1.000E-03, 5.339E+00, 6.186E-03, 1.887E+03,
									0.000E+00, 0.000E+00, 1.892E+03, 1.887E+03 },
							{ 2.000E-03, 4.713E+00, 1.665E-02, 2.706E+03,
									0.000E+00, 0.000E+00, 2.711E+03, 2.706E+03 },
							{ 3.000E-03, 4.104E+00, 2.603E-02, 9.571E+02,
									0.000E+00, 0.000E+00, 9.612E+02, 9.571E+02 },
							{ 4.000E-03, 3.577E+00, 3.422E-02, 4.461E+02,
									0.000E+00, 0.000E+00, 4.497E+02, 4.462E+02 },
							{ 5.000E-03, 3.123E+00, 4.168E-02, 2.441E+02,
									0.000E+00, 0.000E+00, 2.473E+02, 2.441E+02 },
							{ 6.000E-03, 2.727E+00, 4.857E-02, 1.481E+02,
									0.000E+00, 0.000E+00, 1.509E+02, 1.482E+02 },
							{ 8.000E-03, 2.092E+00, 6.078E-02, 6.675E+01,
									0.000E+00, 0.000E+00, 6.890E+01, 6.681E+01 },
							{ 1.000E-02, 1.636E+00, 7.115E-02, 3.571E+01,
									0.000E+00, 0.000E+00, 3.742E+01, 3.578E+01 },
							{ 2.000E-02, 6.767E-01, 1.027E-01, 4.144E+01,
									0.000E+00, 0.000E+00, 4.222E+01, 4.154E+01 },
							{ 3.000E-02, 3.804E-01, 1.161E-01, 1.335E+01,
									0.000E+00, 0.000E+00, 1.385E+01, 1.347E+01 },
							{ 4.000E-02, 2.411E-01, 1.223E-01, 5.843E+00,
									0.000E+00, 0.000E+00, 6.206E+00, 5.965E+00 },
							{ 5.000E-02, 1.664E-01, 1.248E-01, 3.044E+00,
									0.000E+00, 0.000E+00, 3.335E+00, 3.169E+00 },
							{ 6.000E-02, 1.223E-01, 1.254E-01, 1.775E+00,
									0.000E+00, 0.000E+00, 2.023E+00, 1.900E+00 },
							{ 8.000E-02, 7.468E-02, 1.239E-01, 7.515E-01,
									0.000E+00, 0.000E+00, 9.501E-01, 8.754E-01 },
							{ 1.000E-01, 5.045E-02, 1.210E-01, 3.835E-01,
									0.000E+00, 0.000E+00, 5.550E-01, 5.045E-01 },
							{ 1.500E-01, 2.408E-02, 1.125E-01, 1.125E-01,
									0.000E+00, 0.000E+00, 2.491E-01, 2.250E-01 },
							{ 2.000E-01, 1.400E-02, 1.048E-01, 4.732E-02,
									0.000E+00, 0.000E+00, 1.661E-01, 1.521E-01 },
							{ 3.000E-01, 6.433E-03, 9.231E-02, 1.437E-02,
									0.000E+00, 0.000E+00, 1.131E-01, 1.067E-01 },
							{ 4.000E-01, 3.675E-03, 8.319E-02, 6.412E-03,
									0.000E+00, 0.000E+00, 9.327E-02, 8.960E-02 },
							{ 5.000E-01, 2.372E-03, 7.620E-02, 3.541E-03,
									0.000E+00, 0.000E+00, 8.212E-02, 7.975E-02 },
							{ 6.000E-01, 1.655E-03, 7.063E-02, 2.238E-03,
									0.000E+00, 0.000E+00, 7.452E-02, 7.287E-02 },
							{ 8.000E-01, 9.364E-04, 6.218E-02, 1.144E-03,
									0.000E+00, 0.000E+00, 6.426E-02, 6.332E-02 },
							{ 1.000E+00, 6.006E-04, 5.596E-02, 7.120E-04,
									0.000E+00, 0.000E+00, 5.727E-02, 5.667E-02 },
							{ 1.250E+00, 3.851E-04, 5.008E-02, 4.572E-04,
									8.899E-05, 0.000E+00, 5.101E-02, 5.062E-02 },
							{ 1.500E+00, 2.676E-04, 4.553E-02, 3.291E-04,
									4.412E-04, 0.000E+00, 4.657E-02, 4.630E-02 },
							{ 2.000E+00, 1.507E-04, 3.887E-02, 2.021E-04,
									1.635E-03, 0.000E+00, 4.086E-02, 4.071E-02 },
							{ 3.000E+00, 6.701E-05, 3.060E-02, 1.090E-04,
									4.460E-03, 1.071E-05, 3.524E-02, 3.518E-02 }, } },
			// -----------------------------------------------------------------
			{
					"detector.window.attenuationCoef",
					new double[][]
					// energy+tot: al, al2o3 au be fe ge mgo nai si
					{
							{ 1.000E-03, 1.185E+03, 2.788E+03, 4.653E+03,
									6.042E+02, 9.085E+03, 1.892E+03, 2.378E+03,
									7.801E+03, 1.570E+03 },
							{ 2.000E-03, 2.263E+03, 1.525E+03, 1.137E+03,
									7.470E+01, 1.627E+03, 2.711E+03, 1.441E+03,
									1.924E+03, 2.777E+03 },
							{ 3.000E-03, 7.881E+02, 5.193E+02, 2.049E+03,
									2.127E+01, 5.576E+02, 9.612E+02, 4.832E+02,
									7.060E+02, 9.784E+02 },
							{ 4.000E-03, 3.605E+02, 2.346E+02, 1.144E+03,
									8.685E+00, 2.567E+02, 4.497E+02, 2.163E+02,
									3.401E+02, 4.528E+02 },
							{ 5.000E-03, 1.934E+02, 1.249E+02, 6.660E+02,
									4.369E+00, 1.399E+02, 2.473E+02, 1.145E+02,
									7.320E+02, 2.451E+02 },
							{ 6.000E-03, 1.153E+02, 7.406E+01, 4.252E+02,
									2.528E+00, 8.484E+01, 1.509E+02, 6.756E+01,
									5.336E+02, 1.470E+02 },
							{ 8.000E-03, 5.032E+01, 3.211E+01, 2.072E+02,
									1.124E+00, 3.056E+02, 6.890E+01, 2.910E+01,
									2.519E+02, 6.469E+01 },
							{ 1.000E-02, 2.621E+01, 1.668E+01, 1.181E+02,
									6.466E-01, 1.707E+02, 3.742E+01, 1.506E+01,
									1.400E+02, 3.388E+01 },
							{ 2.000E-02, 3.442E+00, 2.229E+00, 7.881E+01,
									2.251E-01, 2.568E+01, 4.222E+01, 2.010E+00,
									2.185E+01, 4.463E+00 },
							{ 3.000E-02, 1.128E+00, 7.750E-01, 2.752E+01,
									1.792E-01, 8.176E+00, 1.385E+01, 7.111E-01,
									7.358E+00, 1.436E+00 },
							{ 4.000E-02, 5.684E-01, 4.225E-01, 1.298E+01,
									1.640E-01, 3.629E+00, 6.206E+00, 3.969E-01,
									1.877E+01, 7.010E-01 },
							{ 5.000E-02, 3.681E-01, 2.952E-01, 7.258E+00,
									1.554E-01, 1.957E+00, 3.335E+00, 2.832E-01,
									1.048E+01, 4.385E-01 },
							{ 6.000E-02, 2.778E-01, 2.368E-01, 4.529E+00,
									1.493E-01, 1.205E+00, 2.023E+00, 2.307E-01,
									6.450E+00, 3.206E-01 },
							{ 8.000E-02, 2.018E-01, 1.858E-01, 2.185E+00,
									1.401E-01, 5.952E-01, 9.501E-01, 1.843E-01,
									2.999E+00, 2.228E-01 },
							{ 1.000E-01, 1.704E-01, 1.632E-01, 5.158E+00,
									1.328E-01, 3.717E-01, 5.550E-01, 1.633E-01,
									1.669E+00, 1.835E-01 },
							{ 1.500E-01, 1.378E-01, 1.370E-01, 1.859E+00,
									1.190E-01, 1.964E-01, 2.491E-01, 1.381E-01,
									6.112E-01, 1.448E-01 },
							{ 2.000E-01, 1.223E-01, 1.230E-01, 9.215E-01,
									1.089E-01, 1.460E-01, 1.661E-01, 1.242E-01,
									3.285E-01, 1.275E-01 },
							{ 3.000E-01, 1.042E-01, 1.055E-01, 3.744E-01,
									9.463E-02, 1.099E-01, 1.131E-01, 1.067E-01,
									1.658E-01, 1.082E-01 },
							{ 4.000E-01, 9.276E-02, 9.413E-02, 2.180E-01,
									8.472E-02, 9.400E-02, 9.327E-02, 9.521E-02,
									1.171E-01, 9.614E-02 },
							{ 5.000E-01, 8.445E-02, 8.579E-02, 1.530E-01,
									7.739E-02, 8.415E-02, 8.212E-02, 8.680E-02,
									9.497E-02, 8.748E-02 },
							{ 6.000E-01, 7.802E-02, 7.928E-02, 1.194E-01,
									7.155E-02, 7.704E-02, 7.452E-02, 8.021E-02,
									8.225E-02, 8.078E-02 },
							{ 8.000E-01, 6.841E-02, 6.957E-02, 8.603E-02,
									6.286E-02, 6.699E-02, 6.426E-02, 7.039E-02,
									6.755E-02, 7.082E-02 },
							{ 1.000E+00, 6.146E-02, 6.252E-02, 6.953E-02,
									5.652E-02, 5.995E-02, 5.727E-02, 6.326E-02,
									5.881E-02, 6.361E-02 },
							{ 1.250E+00, 5.496E-02, 5.591E-02, 5.793E-02,
									5.054E-02, 5.350E-02, 5.101E-02, 5.656E-02,
									5.162E-02, 5.688E-02 },
							{ 1.500E+00, 5.006E-02, 5.091E-02, 5.167E-02,
									4.598E-02, 4.883E-02, 4.657E-02, 5.152E-02,
									4.697E-02, 5.183E-02 },
							{ 2.000E+00, 4.324E-02, 4.388E-02, 4.570E-02,
									3.938E-02, 4.265E-02, 4.086E-02, 4.439E-02,
									4.149E-02, 4.481E-02 },
							{ 3.000E+00, 3.541E-02, 3.567E-02, 4.202E-02,
									3.138E-02, 3.621E-02, 3.524E-02, 3.607E-02,
									3.681E-02, 3.678E-02 }, } },

			{ "window.densities", new double[]
					// in g/cm3
					// Al, Al2O3, Au, Be, Fe Ge, MgO NaI Si
					{ 2.69889998, 3.97000003, 19.3199997, 1.84800005,
							7.87400007, 5.32299995, 3.57999992, 3.66700006,
							2.32999992 } },

			{
					"window.type",
					new String[]
					// Al
					{ "Al", "Al2O3", "Au", "Be", "Fe", "Ge", "MgO", "NaI", "Si" } },
			// -----------------------------------------------------------------
			{ "detector.densities", new double[]
					// in g/cm3
					// NaI, GeBased
					{ 3.6700006, 5.32299995 } },
			/*
			 * //for r=0-0.39 {"kn.wielopolski.r1", new double[][] // B1, B2,
			 * B3, B4., B5, B6, B7 { {-0.01238, -1.5687, -0.42888, -28.63753,
			 * 31.96466, 6.46298, 0}, {0.02902, 3.8898, 3.73901, 65.38702,
			 * -70.51987, -8.63537, 0}, {0.00185, 0.2423, 0.20125, 3.75526,
			 * -4.0744, 0.83815, 0}, {-0.000085, -0.0113, -0.01234, -0.14929,
			 * 0.16095, 0.03104, 0}, }},
			 * 
			 * //for r=0.39-0.7 {"kn.wielopolski.r2", new double[][] // B1, B2,
			 * B3, B4., B5, B6, B7 { {5.78087, -22.76518, 52.21054, -29.24388,
			 * -2.30266, 19.38525, 0.6351}, {-11.74909, 58.75565, -114.7284,
			 * 60.70503, 5.64759, 19.24324, 0.64313}, {-2.28752, 8.55102,
			 * -56.57779, 44.52396, 8.55989, 4.68978, 0.7508}, {0.03073,
			 * -0.16078, 0.47973, -0.34037, -0.03003, 12.27394, 0.65229}, }},
			 * 
			 * //for r=0.7-1.0 {"kn.wielopolski.r3", new double[][] // B1, B2,
			 * B3, B4., B5, B6, B7 { {3.12358, -5.62131, -51.9096, -20.89746,
			 * 191.18823, 10.07783, 0}, {0.04248, 13.69255, 127.2337, -1.01555,
			 * -411.95, -23.63232, 0}, {0.001942, 0.5932, 5.73861, 6.38184,
			 * -28.01964, -1.71261, 0}, {-0.000045, -0.013426, -0.15548,
			 * -0.45873, 1.31546, 0.04947, 0}, }},
			 */

			{ "src.load", "gsc" },
			{ "src.load.description", " Global app-Source file" },
			{ "det.load", "gdt" },
			{ "det.load.description", " Global app-Detector file" },
			{ "data.load", "Data" },

			{ "form.icon.url", "/danfulea/resources/personal.png"},//"/jdf/resources/duke.png" },
			{ "icon.url", "/danfulea/resources/personal.png"},//"/jdf/resources/globe.gif" },
			{ "simTa.toolTip", "Select text and then hold CNTRL+C to copy!" },
			// about frame...
			{ "about.title", "About..." },
			{ "about.version.label", "Version" },
			{ "main.startsimB", "Run" },
			{ "main.startsimB.mnemonic", new Character('R') },
			{ "main.stopsimB", "Kill" },
			{ "main.stopsimB.mnemonic", new Character('K') },
			{ "rezultat", "" },
			{ "source.border", "Source settings" },
			{ "detector.border", "Detector settings" },
			{ "main.saveSB", "Save source..." },
			{ "main.saveSB.mnemonic", new Character('v') },
			{ "main.saveDB", "Save detector..." },
			{ "main.saveDB.mnemonic", new Character('d') },
			{ "main.loadSB", "Load source..." },
			{ "main.loadSB.mnemonic", new Character('L') },
			{ "main.loadDB", "Load detector..." },
			{ "main.loadDB.mnemonic", new Character('o') },
			{ "dettype.label", "Type: " },
			{ "sourcetype.label", "Geometry: " },
			{ "sourceeq.label", "Equivalent composition: " },
			{ "asource.label", "External diameter (cm): " },
			{ "bsource.label", "Internal diameter (cm): " },
			{ "hsource.label", "Total height (cm): " },
			{ "asourceup.label", "Upper height, above detector (cm): " },
			{ "adet.label", "Effective diameter (cm): " },
			{ "hdet.label", "Effective height (cm): " },
			{ "adetTot.label", "Total diameter (cm): " },
			{ "hdetTot.label", "Total height(cm): " },
			{ "hdetUp.label", "Detector-source gap (cm): " },// Upper inactive
																// volume height
																// (cm): "},
			{
					"main.nPhotonsCb",
					new String[] { "10000", "20000", "40000", "60000", "80000",
							"100000", "300000", "500000", "5000000",
							"10000000", "50000000" } },
			{ "main.detTypeCb", new String[] { "NaI", "Ge" } },
			{ "main.sourceTypeCb", new String[] { "Cylinder", "Marinelli" } },// geometrie//new
																				// String[]
																				// {"Point source",
																				// "Sarpagan",
																				// "Marinelli","Frontal Beam"}},//geometrie
			{ "main.sourceEqCb",
					new String[] { "H2O", "NaCl", "soil", "concrete" } },
			{ "main.adetTf", "6.3" },
			{ "main.hdetTf", "6.3" },
			{ "main.energyTf", "0.662" },
			{ "main.energyTf.toolTip", "Total energy in MeV!" },
			{ "main.asourceTf", "7.8" },
			{ "main.hsourceTf", "3.4" },
			{ "main.bsourceTf", "0" },
			{
					"main.bsourceTf.toolTip",
					"< source external diamter and > total detector diameter. Marinelli geometry only!" },
			{ "main.hsourceupTf", "0" },
			{ "main.hsourceupTf.toolTip",
					"< total source height. Marinelli geometry only!" },
			{ "main.hdetTotTf", "7.3" },
			{ "main.hdetTotTf.toolTip", "Total height of detector house." },
			{ "main.adetTotTf", "7.3" },
			{ "main.adetTotTf.toolTip", "Total diameter of detector house." },
			{ "main.hUpTf", "0.9" },
			{
					"main.hUpTf.toolTip",
					"Detector-source gap. Height of upper (towards source base plane) inactive volume." },
			{ "photon.label", "Number of histories: " },
			{ "en.label", "Incident kinetic energy (MeV): " },
			// menu labels...
			{ "menu.file", "File" },
			{ "menu.file.mnemonic", new Character('F') },

			{ "menu.file.exit", "Close" },
			{ "menu.file.exit.mnemonic", new Character('C') },

			{ "menu.help", "Help" },
			{ "menu.help.mnemonic", new Character('H') },

			{ "menu.help.about", "About..." },
			{ "menu.help.about.mnemonic", new Character('A') },

			// dialog messages...
			{ "dialog.exit.title", "Confirm exit ..." },
			{ "dialog.exit.message", "Are you sure ?" },
			{ "dialog.exit.buttons", new Object[] { "Yes", "No" } },
			{ "dialog.OkCancel.buttons", new Object[] { "Ok", "Cancel" } },
			{ "dialog.filechooser.title", "Save" },
			{ "dialog.filechooser.approveButton", "Save" },
			{ "dialog.filechooser.approveButton.mnemonic", new Character('S') },
			{ "mc.border", "Simulare Monte Carlo: " },
			{ "eff.border", "Eficienta: " },
			{ "activ.border", "Activitate: " },
			{
					"main.winCb",
					new String[] { "Al", "Al2O3", "Au", "Be", "Fe", "Ge",
							"MgO", "NaI", "Si" } }, { "main.win1Tf", "0.05" },
			{ "main.win2Tf", "0.0" }, { "main.win3Tf", "0.0" },
			{ "main.win4Tf", "0.0" }, { "win1.label", "Fereastra 1:  " },
			{ "win2.label", "Fereastra 2:  " },
			{ "win3.label", "Fereastra 3:  " },
			{ "win4.label", "Fereastra 4:  " },
			{ "material.label", "Material  " },
			{ "thickness.label", "Grosime (cm)  " },

	};
}
