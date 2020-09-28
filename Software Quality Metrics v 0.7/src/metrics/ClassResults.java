package metrics;

import java.util.ArrayList;

public class ClassResults {
	
	protected static ArrayList <ClassResults> results = new ArrayList <> ();
	private String pack;
	private String name;
	
	// old common metrics
	private int ploc;
	private int lloc;
	private int lc;
	
	// C&K metrics
	private int dit;
	private int noc;
	private int cbo;
	private int wmc;
	private int lcom1;
	private double lcom2;
	private double lcom3;
	private int rfc;
	
	// QMOOD metrics
	private int dsc;
	private int noh;
	private double ana;
	private double dam;
	private int dcc;
	private double cam;
	private int moa;
	private double mfa;
	private int nop;
	private int cis;
	private int nom;
	
	//QMOOD Quality Characteristics
	private double reusability;
	private double flexibility;
	private double understandability;
	private double functionality;
	private double extendibility;
	private double effectiveness;
	
	
	public ClassResults (Class <?> c) {
		name = c.getSimpleName();
		if (c.getPackage() != null) {
			pack = c.getPackage().getName();
		}
		else pack = "";
	}

	public ArrayList <ClassResults> getResults() {
		return results;
	}


	public String getPack() {
		return pack;
	}



	public void setPack(String pack) {
		this.pack = pack;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getPloc() {
		return ploc;
	}



	public void setPloc(int ploc) {
		this.ploc = ploc;
	}



	public int getLloc() {
		return lloc;
	}



	public void setLloc(int lloc) {
		this.lloc = lloc;
	}



	public int getLc() {
		return lc;
	}



	public void setLc(int lc) {
		this.lc = lc;
	}



	public int getDit() {
		return dit;
	}



	public void setDit(int dit) {
		this.dit = dit;
	}



	public int getNoc() {
		return noc;
	}



	public void setNoc(int noc) {
		this.noc = noc;
	}



	public int getNom() {
		return nom;
	}



	public void setNom(int nom) {
		this.nom = nom;
	}



	public int getCbo() {
		return cbo;
	}



	public void setCbo(int cbo) {
		this.cbo = cbo;
	}



	public int getWmc() {
		return wmc;
	}



	public void setWmc(int wmc) {
		this.wmc = wmc;
	}



	public int getLcom1() {
		return lcom1;
	}



	public void setLcom1(int lcom1) {
		this.lcom1 = lcom1;
	}



	public double getLcom2() {
		return lcom2;
	}



	public void setLcom2(double lcom22) {
		this.lcom2 = lcom22;
	}



	public double getLcom3() {
		return lcom3;
	}



	public void setLcom3(double lcom32) {
		this.lcom3 = lcom32;
	}



	public int getRfc() {
		return rfc;
	}



	public void setRfc(int rfc) {
		this.rfc = rfc;
	}

	public int getDsc() {
		return dsc;
	}

	public void setDsc(int dsc) {
		this.dsc = dsc;
	}

	public int getNoh() {
		return noh;
	}

	public void setNoh(int noh) {
		this.noh = noh;
	}

	public double getAna() {
		return ana;
	}

	public void setAna(double ana) {
		this.ana = ana;
	}

	public double getDam() {
		return dam;
	}

	public void setDam(double dam) {
		this.dam = dam;
	}

	public int getDcc() {
		return dcc;
	}

	public void setDcc(int dcc) {
		this.dcc = dcc;
	}

	public double getCam() {
		return cam;
	}

	public void setCam(double cam2) {
		this.cam = cam2;
	}

	public int getMoa() {
		return moa;
	}

	public void setMoa(int moa) {
		this.moa = moa;
	}

	public double getMfa() {
		return mfa;
	}

	public void setMfa(double mfa) {
		this.mfa = mfa;
	}

	public int getNop() {
		return nop;
	}

	public void setNop(int nop) {
		this.nop = nop;
	}

	public int getCis() {
		return cis;
	}

	public void setCis(int cis) {
		this.cis = cis;
	}

	public double getReusability() {
		return reusability;
	}

	public void setReusability(double reusability) {
		this.reusability = reusability;
	}

	public double getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(double flexibility) {
		this.flexibility = flexibility;
	}

	public double getUnderstandability() {
		return understandability;
	}

	public void setUnderstandability(double understandability) {
		this.understandability = understandability;
	}

	public double getFunctionality() {
		return functionality;
	}

	public void setFunctionality(double functionality) {
		this.functionality = functionality;
	}

	public double getExtendibility() {
		return extendibility;
	}

	public void setExtendibility(double extendibility) {
		this.extendibility = extendibility;
	}

	public double getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(double effectiveness) {
		this.effectiveness = effectiveness;
	}

}
