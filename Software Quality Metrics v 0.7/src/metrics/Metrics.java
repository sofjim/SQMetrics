package metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Metrics {
	
	static ClassResults cr;
	static Method [] methods;
	static ArrayList<Class<?>> children;
	
	public static void executeClassMetrics (Class<?> c) {
		/**
		 *  This method executes the metrics
		 *  that the user has checked to be measured
		 */
		
		cr = new ClassResults (c);
		methods = MethodObj.purifyMethods(c);
		children = ClassObj.getChildren(c);
		
		// write informations to the report file
		importReport (c);
		
		// initialize metric variables
		int ploc, lloc, lc, dit, noc, cbo, wmc, lcom1, rfc;
		int dsc = 0, noh = 0, dcc = 0, moa = 0, nop = 0, cis = 0, nom = 0;
		double [] lcom23 = new double [2];
		double lcom2, lcom3, ana = 0, dam = 0 ,cam = 0 , mfa = 0;
		double reusability, flexibility, understandability, functionality, extendibility, effectiveness;
		
		// display a progress bar for the classes
		String clName = c.getName();
		InputPage.updateClassProgress(clName);
		
		if (InputPage.getPlocCheck().isSelected()) {
			InputPage.updateMetricProgress("PLOC");
			ploc = linesOfCode(c);
			cr.setPloc(ploc);
		}
		if (InputPage.getLlocCheck().isSelected()) {
			InputPage.updateMetricProgress("LLOC");
			lloc = linesLogicCode(c);
			cr.setLloc(lloc);
		}
		if (InputPage.getLcCheck().isSelected()) {
			InputPage.updateMetricProgress("LC");
			lc = comments(c);
			cr.setLc(lc);
		}
		if (InputPage.getDitCheck().isSelected()) {
			InputPage.updateMetricProgress("DIT");
			dit = depthInhTree(c);
			cr.setDit(dit);
		}
		if (InputPage.getNocCheck().isSelected()) {
			InputPage.updateMetricProgress("NOC");
			noc = numberOfChildren(c);
			cr.setNoc(noc);
		}
		if (InputPage.getCboCheck().isSelected()) {
			InputPage.updateMetricProgress("CBO");
			cbo = couplingObjects(c);
			cr.setCbo(cbo);
		}
		if (InputPage.getWmcCheck().isSelected()) {
			InputPage.updateMetricProgress("WMC");
			wmc = weightMethodsClass(c);
			cr.setWmc(wmc);
		}
		if (InputPage.getLcom1Check().isSelected()) {
			InputPage.updateMetricProgress("LCOM 1");
			lcom1 = lackCohMethods1(c);
			cr.setLcom1(lcom1);
		}
		if (InputPage.getLcom2Check().isSelected() || InputPage.getLcom3Check().isSelected()) {
			
			InputPage.updateMetricProgress("LCOM 2");
			lcom23 = lackCohMethods23 (c);
		}
		if (InputPage.getLcom2Check().isSelected()) {
			lcom2 = lcom23 [0];
			cr.setLcom2(lcom2);
		}
		if (InputPage.getLcom3Check().isSelected()) {
			InputPage.updateMetricProgress("LCOM 3");
			lcom3 = lcom23 [1];
			cr.setLcom3(lcom3);
		}
		if (InputPage.getRfcCheck().isSelected()) {
			InputPage.updateMetricProgress("RFC");
			rfc = responseForClass(c);
			cr.setRfc(rfc);
		}
		if (InputPage.getDscCheck().isSelected()) {
			InputPage.updateMetricProgress("DSC");
			dsc = QMoodMetrics.designSizeClasses();
			cr.setDsc(dsc);
		}
		if (InputPage.getNohCheck().isSelected()) {
			InputPage.updateMetricProgress("NOH");
			noh = QMoodMetrics.numOfHierarchies();
			cr.setNoh(noh);
		}
		if (InputPage.getAnaCheck().isSelected()) {
			InputPage.updateMetricProgress("ANA");
			ana = QMoodMetrics.averNumAncestors();
			cr.setAna(ana);
		}
		if (InputPage.getDamCheck().isSelected()) {
			InputPage.updateMetricProgress("DAM");
			dam = QMoodMetrics.dataAccessMetric(c);
			cr.setDam(dam);
		}
		if (InputPage.getDccCheck().isSelected()) {
			InputPage.updateMetricProgress("DCC");
			dcc = QMoodMetrics.directClassCoupling(c);
			cr.setDcc(dcc);
		}
		if (InputPage.getCamCheck().isSelected()) {
			InputPage.updateMetricProgress("CAM");
			cam = QMoodMetrics.cohAmongMethods(c);
			cr.setCam(cam);
		}
		if (InputPage.getMoaCheck().isSelected()) {
			InputPage.updateMetricProgress("MOA");
			moa = QMoodMetrics.measureOfAggreg(c);
			cr.setMoa(moa);
		}
		if (InputPage.getMfaCheck().isSelected()) {
			InputPage.updateMetricProgress("MFA");
			mfa = QMoodMetrics.measFunctAbstr(c);
			cr.setMfa(mfa);
		}
		if (InputPage.getNopCheck().isSelected()) {
			InputPage.updateMetricProgress("NOP");
			nop = QMoodMetrics.numOfPolyMeths(c);
			cr.setNop(nop);
		}
		if (InputPage.getCisCheck().isSelected()) {
			InputPage.updateMetricProgress("CIS");
			cis = QMoodMetrics.classInterSize(c);
			cr.setCis(cis);
		}
		if (InputPage.getNomCheck().isSelected()) {
			InputPage.updateMetricProgress("NOM");
			nom = QMoodMetrics.numOfMethods(c);
			cr.setNom(nom);
		}
		if (InputPage.getDccCheck().isSelected() && InputPage.getCamCheck().isSelected()
				&& InputPage.getCisCheck().isSelected() && InputPage.getDscCheck().isSelected()){
			InputPage.updateMetricProgress("Reusability");
			reusability = QMoodMetrics.reusability (dcc, cam, cis, dsc);
			cr.setReusability(reusability);
		}
		if (InputPage.getDamCheck().isSelected() && InputPage.getDccCheck().isSelected()
				&& InputPage.getMoaCheck().isSelected() && InputPage.getNopCheck().isSelected()){
			InputPage.updateMetricProgress("Flexibility");
			flexibility = QMoodMetrics.flexibility (dam, dcc, moa, nop);
			cr.setFlexibility(flexibility);
		}
		if (InputPage.getAnaCheck().isSelected() && InputPage.getDamCheck().isSelected()
				&& InputPage.getDccCheck().isSelected() && InputPage.getCamCheck().isSelected()
				&& InputPage.getNopCheck().isSelected() && InputPage.getNomCheck().isSelected() 
				&& InputPage.getDscCheck().isSelected() ){
			InputPage.updateMetricProgress("Understandability");
			understandability = QMoodMetrics.understandability (ana, dam, dcc, cam, nop, nom, dsc);
			cr.setUnderstandability(understandability);
		}
		if (InputPage.getCamCheck().isSelected() && InputPage.getNopCheck().isSelected()
				&& InputPage.getCisCheck().isSelected() && InputPage.getDscCheck().isSelected()
				&& InputPage.getNohCheck().isSelected()){
			InputPage.updateMetricProgress("Functionality");
			functionality = QMoodMetrics.functionality (cam, nop, cis, dsc, noh);
			cr.setFunctionality(functionality);
		}
		if (InputPage.getAnaCheck().isSelected() && InputPage.getDccCheck().isSelected()
				&& InputPage.getMfaCheck().isSelected() && InputPage.getNopCheck().isSelected()){
			InputPage.updateMetricProgress("Extendibility");
			extendibility = QMoodMetrics.extendibility (ana, dcc, mfa, nop);
			cr.setExtendibility(extendibility);
		}
		if (InputPage.getAnaCheck().isSelected() && InputPage.getDamCheck().isSelected()
				&& InputPage.getMoaCheck().isSelected() && InputPage.getMfaCheck().isSelected()
				&& InputPage.getNopCheck().isSelected()){
			InputPage.updateMetricProgress("Effectiveness");
			effectiveness = QMoodMetrics.effectiveness (ana, dam, moa, mfa, nop);
			cr.setEffectiveness(effectiveness);
		}
		ClassResults.results.add(cr);
		
		
		// reset the metrics bar for each new class
		InputPage.resetMetricProgress();

	}

	public static int linesOfCode(Class<?> c) {
		/**
		 * This method calculates the physical lines of code of a class
		 */

		// Get the corresponding javaFileName
		String javaFileName = null;
		int ploc = 0;
		for (JavaFile j : JavaFile.javaFiles) {
			if (c.getSimpleName().equals(j.plainName)) {
				javaFileName = j.getPath();
			}
		}

		if (javaFileName == null) {
			return ploc;
		}

		try {
			FileReader myFile = new FileReader(javaFileName);

			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(myFile);

			// check if the next line exists
			while ((br.readLine()) != null) {

				// if exists, increase the counter
				ploc++;
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		return ploc;
	}

	public static int linesLogicCode(Class<?> c) {
		/**
		 * This method calculates the logical lines of the code. If a line contains
		 * both, code and comment, it counts as code. 
		 * The lines which contain only 1 bracket do not count as logical lines of code
		 */

		int lloc = 0;

		// Get the corresponding javaFileName
		String javaFileName = null;
		for (JavaFile j : JavaFile.javaFiles) {
			if (c.getSimpleName().equals(j.plainName)) {
				javaFileName = j.getPath();
			}
		}

		if (javaFileName == null) {
			return lloc;
		}

		// Scan the file

		Scanner sc;
		try {
			sc = new Scanner(new File(javaFileName), "UTF-8");
			String input;

			// check if the next line exists
			while (sc.hasNextLine()) {
				// if exists make it a String
				input = sc.nextLine();

				String noComment = null;
				
				// check if the line is empty and if not, get its length
				if (!(input.isEmpty())) {
					int len = input.length();
					int i = 0;

					// check if there are any white characters at the beginning of the String and cut them
					while (((input.charAt(i) == ' ') || input.charAt(i) == '\t') && i < len - 1) {
						// if so, define the index of the first character
						i++;
					}
					String cutInput = input.substring (i, len);
					
					char a = 0;
					// get the first character which is not whitespace
					if (!cutInput.isEmpty()) {
						a = cutInput.charAt(0);
					}

					// check if the line is not a comment and not consists of white characters
					if (!cutInput.isEmpty() && (!((a == '/' && cutInput.charAt(1) == '*') || a == '*'
							|| (a == '/' && cutInput.charAt(1) == '/')) && a != ' ' && a != '\t')) {
						
						// then check if the line contains a comment at the end and cut it
						int in = cutInput.length() - 1;
						if (cutInput.contains ("//") || cutInput.contains ("/*")) {
							int in1 = cutInput.indexOf ("//");
							int in2 = cutInput.indexOf ("/*");
							if (in1 != -1 && in2 != -1) {
								in = Math.min(in1, in2);
							}
							else if (in1 == -1 && in2 != -1) {
								in = in2;
							}
							else if (in2 == -1 && in1 != -1) {
								in = in1;
							}
						}
						noComment = cutInput.substring(0, in); // noComment is the line without comments
						
						// now, check if there is any white characters at the end of the line and cut them
						int l = noComment.length();
						while (l > 0 && (noComment.charAt (l - 1) == ' ' || noComment.charAt (l - 1) == '\t')) {
							l--;
						}
						
						String finalString = noComment.substring(0, l);

						// if the line contains code and it's not only a bracket, increase the counter
						if ((finalString.length() > 1) && a != '\t' && a != ' ') lloc++;
					} 
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lloc;
	}

	public static int comments(Class<?> c) {
		/**
		 * This method calculates the comment lines (if a line contains both, code and
		 * comment, it counts as a comment)
		 */

		// Get the corresponding javaFileName
		String javaFileName = null;
		int lc = 0;
		for (JavaFile j : JavaFile.javaFiles) {
			if (c.getSimpleName().equals(j.plainName)) {
				javaFileName = j.getPath();
			}
		}

		if (javaFileName == null) {
			return lc;
		}

		// Scan the file
		Scanner sc;
		try {
			sc = new Scanner(new File(javaFileName), "UTF-8");
			String input;

			// check if the next line exists
			while (sc.hasNextLine()) {
				// if exists make it a String
				input = sc.nextLine();

				// check if the line is empty and if not get its length
				if (!(input.isEmpty())) {
					int len = input.length();
					int i = 0;

					// check if there is any white character at the beginning of the String
					while (((input.charAt(i) == ' ') || input.charAt(i) == '\t') && i < len - 1) {
						i++;
					}

					// check if the line is comment and not consists of white characters
					boolean m = input.contains("//") || input.contains ("/*");
					if (input.charAt(i) == '*' || m == true) {
						// if so, increase the counter
						lc++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return lc;
	}

	public static int numberOfChildren(Class<?> c) {
		/**
		 * This method returns the number of children that a class has.
		 */

		int noc = 0;
		String a = c.getName();
		
		// check all the classes of the project for superclasses
		for (Class<?> clas : ClassObj.classes) {
			Class<?> sc = clas.getSuperclass();
			
			// if a class has superclass, check if this superclass is the class we measure
			if (sc != null) {
				String b = sc.getName();
				
				// if so, increase the counter
				if (a.equals(b)) {
				noc++;
				}
			}
			
		}
		return noc;
	}

	public static int depthInhTree(Class<?> c) {
		/**
		 * This method returns the depth of inheritance tree that a class has.
		 * If a class inherits from the "Object" class or another class out of the project,
		 * then the DIT is 1.
		 */

		int dit = 1;
		Class<?> sc = c.getSuperclass();
		if (sc != null) {
			String a = sc.getName();
			boolean exists = false;
			
			// check if the superClass does exist into the list of the classes
			for (Class<?> clas : ClassObj.classes) {
				String b = clas.getName();
				if (a.equals(b)) {
					exists = true;
				}
			}
			
			// the method calls itself recursively, to calculate the superclass' DIT
			if (exists == true) {
				dit = dit + depthInhTree(sc);
			}
		}
		
		return dit;
	}

	public static int couplingObjects(Class<?> c) {
		/**
		 * This method calculates the CBO metric (cohesion between objects) of the
		 * current class. If one class uses another class' methods, fields or objects, then the name of
		 * the other class should exists, somewhere in its code. This is the way we measure it.
		 * 
		 * The inheritance must be measured only if the user has selected the appropriate option
		 * If the current class has children or superclass, then it is
		 * coupled with them. So, the CBO must be increased. According to C & K, inheritance should not be counted.
		 */

		int cbo = 0;
		
		boolean countInheritance = InputPage.getOptions().getCboInh();
		
		ArrayList <Class<?>> remove = new ArrayList <Class<?>> ();
		
		// if the user choose to count inheritance, find the children and the parent
		if (countInheritance == true) {
			cbo = numberOfChildren(c);
			if (cr.getDit() > 1) {
				cbo++;
			}
			remove = ClassObj.getChildren(c);
			
			Class<?> scl = c.getSuperclass();
			if (scl == null) {
				remove.add(scl);
			}
			else {
				String sname = scl.getName();
				for (Class<?> a : ClassObj.classes) {
					if (sname.equals(a.getName())) {
						remove.add(ClassObj.classes.get(ClassObj.classes.indexOf(a)));
					}
				}
			}
		}

		// it is not necessary to calculate the current class 
		remove.add(c);
		
		ArrayList<Class<?>> classesToMeasure = new ArrayList<Class<?>>();

		for (Class<?> a : ClassObj.classes) {
			classesToMeasure.add(a);
		}
		
		// It is not necessary children, superclass and the current class to be measured
		// again.
		classesToMeasure.removeAll(remove);
		if (classesToMeasure.size() == 0) {
			return cbo;
		}

		// Get the corresponding javaFileName
		String javaFileName = ClassFile.javaFilePath(c);

		if (javaFileName == null) {
			return cbo;
		}

		ArrayList<Class<?>> classesToRemove = new ArrayList<>();

		// Check if this class refers to another class
		if (classesToMeasure.size() == 0) {
			return cbo;
		}
		for (Class<?> otherClass : classesToMeasure) {

			boolean coupled = false;
			String name = otherClass.getName();
			int n = name.lastIndexOf('.');
			if (n >= 0) {
				name = otherClass.getName().substring(n + 1);
			}

			// Scan the file
			Scanner sc;
			try {
				sc = new Scanner(new File(javaFileName), "UTF-8");
				String input;

				// check if the next line exists
				while (sc.hasNextLine() && coupled == false) {
					// if exists make it a String
					input = sc.nextLine();

					// check if it is comment
					int a = JavaFile.checkIfComment(input);

					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int i = input.length();
						if (input.contains("//")) {
							i = input.indexOf("//");
						} else if (input.contains("/*")) {
							i = input.indexOf("/*");
						}
						input = input.substring(0, i);
					}

					input = JavaFile.clearInsideStrings(input);

					// check if input contains the other class' name
					String[] words = input.split("[^\\w']+");

					if (Arrays.asList(words).contains(name) == true) {
						int index = Arrays.asList(words).indexOf(name);
						String prev = words [index - 1];
						
						// check if the current class is a child of the other class
						if (!prev.equals ("extends")) {
							coupled = true;
						}
					}
					else coupled = false;

					if (coupled == true) {
						cbo++; // a
						classesToRemove.add(otherClass);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		classesToMeasure.removeAll(classesToRemove);

		// Check if another class refers to the current class
		if (classesToMeasure.size() == 0) {
			return cbo;
		}
		for (Class<?> otherClass : classesToMeasure) {
			boolean coupled = false;
			int n = c.getName().lastIndexOf('.');
			String name = c.getName().substring(n + 1);

			String javaOtherName = ClassFile.javaFilePath(otherClass);

			if (javaOtherName == null) {
				return cbo;
			}
			// Scan the file
			Scanner sca;
			try {
				sca = new Scanner(new File(javaOtherName), "UTF-8");
				String input;

				// check if the next line exists
				while (sca.hasNextLine() && coupled == false) {
					// if exists make it a String
					input = sca.nextLine();

					// check if it is comment
					int a = JavaFile.checkIfComment(input);

					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int i = input.length();
						if (input.contains("//")) {
							i = input.indexOf("//");
						} else if (input.contains("/*")) {
							i = input.indexOf("/*");
						}
						input = input.substring(0, i);
					}

					input = JavaFile.clearInsideStrings(input);

					// check if input contains this class' name

					String[] words = input.split("[^\\w']+");

					if (Arrays.asList(words).contains(name) == true) {
						int index = Arrays.asList(words).indexOf(name);
						String prev = words [index - 1];
						
						// check if the other class is a child of the current class
						if (!prev.equals ("extends")) {
							coupled = true;
						}
					}
					else coupled = false;

					if (coupled == true) {
						cbo++; // b
						classesToRemove.add(otherClass);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cbo;
	}

	public static int weightMethodsClass(Class<?> c) {

		/**
		 * This method returns the weight of methods of the class "c" (WMC),
		 * which means the sum of the cyclomatic complexity of its methods
		 */

		int wmc = 0;

		// get the cyclomatic complexity of each method
		for (int i = 0; i < methods.length; i++) {
			
			int a = MethodObj.cyclComplex(methods[i]);
			wmc = wmc + a;
			
			// write the infos for the method to the report file
			reportMethod (methods [i], a);
		}
		return wmc;
	}

	public static int lackCohMethods1(Class<?> c) {
		/**
		 * This method returns the lack of cohesion of methods (LCOM) of a class, 
		 * using the LCOM 1 algorithm. Only the instance (non static) fields are counted.
		 * Getters and setters such as constructors are excluded.
		 */

		int lcom1 = 0;
		int p = 0;
		int q = 0;

		
		ArrayList <Method> noGetSet = new ArrayList <> ();
		for (int i = 0; i < methods.length; i++) {
			noGetSet.add(methods [i]);
		}
		
		// Get the fields of the class
		Field [] allFields = c.getDeclaredFields();
		
		// get the instance (non static) fields of the class
		ArrayList <Field> fields = ClassObj.instanceFields(c);
				
		// if none instance fields exist, then the LCOM1 is "0"
		if (fields.size() == 0) {
			return 0;
		}
		
		// exclude getters and setters
		for (Field f : allFields) {
			String nam = f.getName();
			char a = nam.charAt (0);
			a = Character.toUpperCase (a);
			String b = nam.substring(1);
			String getName = "get" + a + b;	// this is the name of the getter	
			String setName = "set" + a + b; // this is the name of the setter
			for (int n = 0; n < methods.length; n++) {
				if (methods [n].getName().equals(getName)) {
					if (noGetSet.contains(methods [n])) noGetSet.remove(methods [n]);
				}
				else if (methods [n].getName().equals(setName)) {
					if (noGetSet.contains(methods [n])) noGetSet.remove(methods [n]);
				}
			}
		}
		
		// compare the methods as pairs
		for (Method m : noGetSet) {
			for (Method n : noGetSet) {

				// avoid a pair of the same method
				if (!m.equals(n)) {

					// check if the 2 methods access the same field
					Field[] first = MethodObj.accessInstanceFields (m);
					Field[] second = MethodObj.accessInstanceFields (n);
					boolean coherent = false;

					for (int k = 0; k < first.length; k++) {
						for (int l = 0; l < second.length; l++) {
							if (first[k].equals(second[l])) {
								coherent = true;
							}
						}
					}
					if (coherent == true)
						q++;
					else
						p++;
				}
			}
		}
		lcom1 = p - q;
		if (lcom1 < 0)
			lcom1 = 0;

		return lcom1;
	}

	public static double [] lackCohMethods23(Class<?> c) {
		/**
		 * This method returns the lack of cohesion of methods (LCOM) of a class, 
		 * using the LCOM 2 and LCOM3 algorithms. 
		 * Getters and setters such as constructors are excluded.
		 */

		double lcom2 = 0;
		double lcom3 = 0;
		double [] lcom23 = new double [2];

		int m = 0;
		int f = 0;
		int nA = 0; // number of methods that access the field "A"
		int sum = 0; // the sum of nA for all fields
		
		// get the instance (non static) fields of the class
		ArrayList <Field> fields = ClassObj.instanceFields(c);
			
		// if no fields exist, then the LCOM2 is "0"
		f = fields.size();
		if (f == 0) {
			lcom23 [0] = 0;
			lcom23 [1] = 0;
			return lcom23;
		}

		m = methods.length;
		
		ArrayList <Method> methods1 = new ArrayList <> ();
		for (int i = 0; i < m; i++) {
			methods1.add(methods [i]);
		}
		
		// exclude getters and setters
		for (Field fld : fields) {
			String nam = fld.getName();
			char a = nam.charAt (0);
			a = Character.toUpperCase (a);
			String b = nam.substring(1);
			String getName = "get" + a + b;	// this is the name of the getter	
			String setName = "set" + a + b; // this is the name of the setter
			for (int n = 0; n < methods.length; n++) {
				if (methods [n].getName().equals(getName) || methods [n].getName().equals(setName)) {
					methods1.remove(methods [n]);
				}
			}
		}
		
		for (Field fld : fields) {
			
			// nA is the number of methods that access field fld
			nA = ClassObj.methodsAccessField (c, fld).size();
			
			// count the sum for all fields
			sum = sum + nA;
		}
		
		// Avoid division by zero
		int n = 1;
		int k = 2;
		
		if (f == 0)
			f = 1;
		if (m != 0) n = m;
			
		lcom2 = 1 - (double) sum / (n * f);
		
		// This trick gives at most 2 decimal digits and it's locale independent
		lcom2 = Math.round(lcom2 * 100) / 100.0;
		

		if (m != 1) k = m;
		lcom3 = (double) (m - sum / f) / (k - 1);
		
		// This trick gives at most 2 decimal digits and it's locale independent
		lcom3 = Math.round(lcom3 * 100) / 100.0;
		
		lcom23 [0] = lcom2;
		lcom23 [1] = lcom3;

		return lcom23;
	}

	
	
	public static int responseForClass (Class <?> c) {
		  /**
		  * This method returns the Response For a Class (RFC) metric, for the class "c"
		  */

		int rfc = 0;
		
		// the methods and the constructors of the current class must be counted
		Method [] thisMethods = methods;
		Constructor <?> [] constructors = c.getConstructors();
		  
		ArrayList <Class<?>> otherClasses = new ArrayList <>(ClassObj.classes);
		otherClasses.remove(c);
		
		/*
		 *  methods of other classes that communicate with the methods of this class
		 *  will be added into the following array list.
		 */
		ArrayList <Method> otherMethods = new ArrayList <Method> ();
		
		/*
		 * At first, check which methods from the other classes, 
		 * this class's methods call. They must be counted.
		 */
		for (int i = 0; i < thisMethods.length; i++) {
			otherMethods.addAll(MethodObj.otherMethodsCalled(thisMethods [i]));
		}
		
		/*
		 * Subsequently, check which methods from the other classes,
		 * call this class's methods or constructors. 
		 * This is calculated only if the user has selected the option (quite much slower)
		 */
		
		boolean checkOtherMethods = InputPage.getOptions().getRfcOtherMeths();
		
		if (checkOtherMethods == true) {
			
			for (Class<?> otherClass : otherClasses) {
				Method [] otherMeths = MethodObj.purifyMethods(otherClass);
				for (int i = 0; i < otherMeths.length; i++) {
					boolean refers = false;
					ArrayList <Method> otherRefers = MethodObj.otherMethodsCalled(otherMeths [i]);
					for (int a = 0; a < thisMethods.length; a++) {
						refers = otherRefers.contains(thisMethods [a]);
					}
					if (refers == true) {
						otherMethods.add(otherMeths [i]);
					}
				}
			}
			
		}
		
		// Each method should appear only once. So duplicates must be eliminated.
		Set <Method> set = new HashSet <> (otherMethods);
		otherMethods.clear();
		otherMethods.addAll(set);
		
		int methCount = thisMethods.length + constructors.length;
		int otherMethCount = otherMethods.size();
		
		rfc = methCount + otherMethCount;
		return rfc;
	}
		
	public static void importReport (Class<?> c) {
		
		// write the children of the class to the report file
		if (children.size () == 0) {
			InputPage.writeToFile ("\t" + "Sub Classes:\t" +  "none", true);
		}
		else {
			String childrenString = "Sub Classes:\t";
			for (Class <?> child : children) {
				childrenString += child.getName() + ", ";
			}
			InputPage.writeToFile ("\t" + childrenString, true);
		}
		
//		// write the nested classes of the class to the report file
//		Class<?> [] nested = c.getClasses();
//		if (nested.length == 0) {
//			InputPage.writeToFile ("\t" + "Nested Classes Classes:\t" +  "none", true);
//		}
//		else {
//			String nestedName = "Nested Classes:\t";
//			for (Class <?> nestedClass : nested) {
//				nestedName += nestedClass.getName() + ", ";
//			}
//			InputPage.writeToFile ("\t" + nestedName, true);
//		}
		
		// write the fields of the class to the report file
		InputPage.writeToFile ("\n\t" + "Fields: ", true);
		Field [] fields = c.getDeclaredFields();
		if (fields.length == 0) {
			InputPage.writeToFile ("\t\t" + "none", true);
		}
		else {
			for (Field field : fields) {
				int modifiers = field.getModifiers();
				InputPage.writeToFile ("\t\t" + field.getName() + ": " + Modifier.toString (modifiers), true);
			}
		}
		
		if (methods.length == 0) {
			InputPage.writeToFile ("\n\t" + "Methods:\t" + "none", true);
		}
		else {
			InputPage.writeToFile ("\n\t" + "Methods:", true);
		}
		


	}
	
	public static void reportMethod (Method m, int cc) {
		
		// write the methods of the class to the report file
		int modifiers = m.getModifiers();
		InputPage.writeToFile ("\t\t" + m.getName() + ":\t" +  Modifier.toString (modifiers) + "\tCC= " + cc,  true);
	}		


}
