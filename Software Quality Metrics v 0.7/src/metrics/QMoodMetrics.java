package metrics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class QMoodMetrics {
	
	public static int designSizeClasses () {
		/**
		 * This method returns the DSC metric (Design Size in Classes),
		 * which means the number of the classes of the whole project
		 */
		
		return ClassObj.classes.size ();
	}
	
	public static int numOfHierarchies () {
		/**
		 * This method returns the NOH metric (number of hierarchies)
		 * that the project has, which means the number of the
		 * classes that have at least 1 child and DIT = 1.
		 */
		
		int noh = 0;
		for (Class <?> c: ClassObj.classes) {
			if (Metrics.depthInhTree(c) == 1 && Metrics.numberOfChildren(c) != 0) {
				noh++;
			}
		}
		
		return noh;
	}
	
	public static double averNumAncestors () {
		/**
		 * This method returns the ANA metric (average number of ancestors),
		 * which means the sum of the DITs of all classes divided by the DSC.
		 */
		
		double ana = 0;
		int d = 0;
		
		for (Class <?> c: ClassObj.classes) {
			d = d + Metrics.depthInhTree(c);
		}
		
		ana = (double) d / Metrics.cr.getDsc();
		
		// This trick gives at most 2 decimal digits and it's locale independent
		ana = Math.round(ana * 100) / 100.0;
		
		return ana;
	}
	
	public static double dataAccessMetric (Class<?> c) {
		/**
		 * This method returns the DAM metric (Data Access Metric),
		 * which means the number of the private and protected fields of a class
		 * divided by the number of all the fields of this class.
		 * The protected fields are counted only if the user has selected the appropriate option.
		 */
		
		double dam = 0;
		int pr = 0;
		Field [] fields = c.getDeclaredFields ();
		int l = fields.length;
		
		// if no fields exist, then the DAM must be 1 (better)
		if (l == 0) {
			dam = 1;
			return dam;
		}
		
		// check the user's selection
		boolean countProtected = InputPage.getOptions().getDamProtected();
		
		if (countProtected == true) {
			for (int i = 0; i < l; i++) {
				if (Modifier.isPrivate (fields[i].getModifiers() ) || Modifier.isProtected (fields[i].getModifiers() )) {
					pr++;
				}
			}
			
		}
		
		else {
			for (int i = 0; i < l; i++) {
				if (Modifier.isPrivate (fields[i].getModifiers() )) {
					pr++;
				}
			}
		}
		
		//if (l == 0) l = 1; // avoid division by zero
		dam = (double) pr / l;
		
		// This trick gives at most 2 decimal digits and it's locale independent
		dam = Math.round (dam * 100) / 100.0;
		
		return dam;
	}
	
	public static int directClassCoupling (Class<?> c) {
		/**
		 * This method returns the DCC metric (Direct Class Coupling) of a class,
		 * which means the number of the other classes that are directly coupled 
		 * with this class. A class is directly coupled with another class when:
		 * it is parent or child of the other class, 
		 * has a field with the type of the other class, 
		 * its methods return a value or have a parameter with a type of the other class.
		 */
		
		int dcc = 0;
		
		// the classes that are direct coupled with this class, will be stored into the following array list.
		ArrayList <Class<?>> directClasses = new ArrayList <> ();
		
		Field [] fields = c.getDeclaredFields ();
		
				
		// add the super class, if it exists and is not external
		if (ClassObj.classes.contains (c.getSuperclass ())) {
			directClasses.add (c.getSuperclass ());
		}
		
		// add the children if exist any
		directClasses.addAll (Metrics.children);
		
		// check the type of any field and add the type, if it is an existing class of the project
		for (int i = 0; i < fields.length; i++) {
			if (ClassObj.classes.contains(fields [i].getType () ) ) {
				directClasses.add (fields [i].getType () );
			}
		}
		
		// Check the methods of the class
		for (int i = 0; i < Metrics.methods.length; i++) {
			
			/* check the return type of any method of this class.
			 * If it is an existing class of the project, add it.
			 */
			if (ClassObj.classes.contains (Metrics.methods [i].getReturnType () )) {
				directClasses.add (Metrics.methods [i].getReturnType () );
			}
			
			/* check the types of the parameters of any method of this class. 
			 * If any of them is an existing class of the project, add it.
			 */
			for (int l = 0; l < Metrics.methods [i].getParameterTypes ().length; l++) {
				if (ClassObj.classes.contains (Metrics.methods [i].getParameterTypes () [l]) ) {
					directClasses.add ((Metrics.methods [i].getParameterTypes () [l]));
				}
			}
		}
		
		// Each class should appear only once. So duplicates must be eliminated.
		Set <Class <?> > set = new HashSet <> (directClasses);
		directClasses.clear();
		directClasses.addAll(set);
		
		// add the size of the list to the dcc
		dcc = dcc + directClasses.size();
		
		/* check all the other classes if they use this class
		 *  as a  field type, or return type of any method, 
		 *  or type of any method's parameter. 
		 */
		
		//Make a clone array list of the classes, to avoid change the original list
		ArrayList <Class<?>> restClasses = new ArrayList <>();
		for (Class<?> a : ClassObj.classes) {
			restClasses.add(a);
		}
		restClasses.removeAll(directClasses); // there is no need to calculate the already calculated classes
		for (Class <?> a: restClasses) {
			Field [] otherFields = a.getDeclaredFields ();
			Method [] otherMethods = MethodObj.purifyMethods (a);
			boolean f = false;
			boolean r = false;
			boolean p = false;
			
			// check if this class is a field type of the other class
			for (int i = 0; i < otherFields.length; i++) {
				if (otherFields [i].getType().equals(c)) {
					f = true;
				}
			}
			
			// check if this class is a return type of any method of the other class
			for (int i = 0; i < otherMethods.length; i++) {
				if (otherMethods [i].getReturnType().equals(c)) {
					r = true;
				}
				
				// check if this class is a type of any parameter of any method of the other class
				for (int j = 0; j < otherMethods [i].getParameterTypes().length; j++) {
					if (otherMethods [i].getParameterTypes() [j].equals(c)) {
						p = true;
					}
				}
			}
			
			// If any of the above is true, increase dcc by 1.
			if (f == true || r == true || p == true) {
				dcc++;
			}
		}
		
		return dcc;
	}
	
	public static double cohAmongMethods (Class <?> c) {
		/**
		 * This method returns the Cohesion Among Methods (CAM) metric.
		 * For that, we must take into account the distinct parameters
		 * of each method of the class and the distinct parameters of 
		 * all the methods of the class.
		 */
		
		// CAM = (SUM m)/n * (t + 1)
		double cam = 0;
		int sumM = 0; // Sum of any method's distinct Parameter types
		int n = 0; // Number of methods of this class
		int t = 0; // Number of distinct parameter types of all the methods
		
		/* If the following variable becomes "true", it means that the class "c" 
		 * is type of a parameter of at least one method of this class
		 */
		boolean thisClassTypeExists = false;  
		
		// get the methods of the class
		Method [] methods = Metrics.methods;
		n = methods.length;
		if (n == 0) n = 1; // Avoid division by zero
		
		ArrayList <Class <?>> allParamTypes = new ArrayList <> ();
		
		// get the parameter types of any method
		for (int i = 0; i < methods.length; i++) {
			Class <?> methodParamTypes [] = methods [i].getParameterTypes();
			for (int a = 0; a < methodParamTypes.length; a ++) {
				ArrayList <Class<?>> mpt = new ArrayList <> ();
				
				// Ensure that there will be no duplicates. Insert only if the Type does not exist.
				
				// At first add into this method's parameter types
				if (!mpt.contains (methodParamTypes [a]) ) {
					mpt.add (methodParamTypes [a]);
				}
				
				// Then add into the summary, the parameter types of all the methods
				if (!allParamTypes.contains (methodParamTypes [a]) ) {
					allParamTypes.add (methodParamTypes [a]);
				}
				if (methodParamTypes [a].equals (c)) {
					thisClassTypeExists = true;
				}
				sumM = sumM + mpt.size ();
			}
			
		}
		
		/* Types must be distinct and each Type should appear only once.
		 * So duplicates must be eliminated.
		 */
		Set <Class <?>> set = new HashSet <> (allParamTypes);
		allParamTypes.clear();
		allParamTypes.addAll(set);
		
		if (thisClassTypeExists == false) {
			t = allParamTypes.size () - 1;
		}
		else t = allParamTypes.size ();

		cam = (double) sumM / (n * (t + 1));
		
		// This trick gives at most 2 decimal digits and it's locale independent
		cam = Math.round (cam * 100.0) / 100.0;
		
		return cam;
	}
	
	public static int measureOfAggreg (Class <?> c) {
		/**
		 * This method returns the number of variables which are declared by the user.
		 * This means the variables, that their type is another class of the project.
		 * It counts the class's fields and if the user has selected the appropriate options,
		 * the constructor's parameters and variables they use 
		 * and the methods's parameters, their return types and the variables they use.
		 */
		
		int moa = 0;
		Field [] classFields = c.getDeclaredFields();
		Method [] methods = Metrics.methods;
		Constructor <?> [] cons = c.getDeclaredConstructors();
		
		// The types will be stored into this arraylist.
		ArrayList <Class <?>> variableTypes = new ArrayList <> ();
		
		// At first, check the class fields
		for (int i = 0; i < classFields.length; i++) {
			
			// Add the type, only if it is a class of the project
			if (ClassObj.classes.contains(classFields [i].getType())) 
			variableTypes.add(classFields [i].getType());
		}
		
		/** 
		 * The following calculations are executed if an only if 
		 * the user has selected the appropriate options
		 */
		
		// Then check the constructors
		for (int k = 0; k < cons.length; k++) {

			boolean parameters = InputPage.getOptions().getMoaParams();

			if (parameters == true) {
				/* check the types of the parameters of any constructor of this class. 
				 * If any of them is an existing class of the project, add it.
				 */
				for (int l = 0; l < cons [k].getParameterTypes ().length; l++) {
					if (ClassObj.classes.contains (cons [k].getParameterTypes () [l]) ) {
						variableTypes.add ((cons [k].getParameterTypes () [l]));
					}
				}

			}

			boolean variables = InputPage.getOptions().getMoaInsideParams();

			if (variables == true) {

				/* We can get the variables that a constructor uses, 
				 * only by check the constructor's code, so we must
				 * get the source code of the constructor.
				 * The code is clear (no comments, no string inside quotes).
				 */
				String [] consCode = MethodObj.sourCode(cons [k]);

				for (int a = 0; a < consCode.length; a++) {

					// check if any class's name is mentioned into the code.
					for (Class <?> cl: ClassObj.classes) {
						String className = ClassObj.plName(cl);
						if (consCode [a].contains(className)) {

							// At first check the previous word
							boolean z = true; // If this become "false", the "cl" should not be added to the array list
							String previous = JavaFile.getPreviousWord(consCode [a], className);
							if (previous.contentEquals("instanceOF")) z = false;

							// cut the string at the end of the className
							consCode [a] = consCode [a].substring(consCode [a].indexOf(className) + className.length());

							// Remove any whitespaces at the beginning (and at the end) of the string
							consCode [a] = consCode [a].trim();

							/* check if the first char of the string is ".".
							 * In this case, there is a static reference to a method 
							 * of the class and it should not be counted
							 */
							char ch = consCode [a].charAt(0);
							if (ch != '.' && z == true) {
								variableTypes.add(cl);
							}
						}
					}
				}
			}
		}

		// Then check the methods one by one, to get their variables's types.
		for (int k = 0; k < methods.length; k++) {

			boolean returnType = InputPage.getOptions().getMoaReturnTypes();

			if (returnType == true) {

				/* check the return type of any method of this class.
				 * If it is an existing class of the project, add it.
				 */
				if (ClassObj.classes.contains(methods [k].getReturnType ())){
					variableTypes.add (methods [k].getReturnType ());
				}
			}

			boolean parameters = InputPage.getOptions().getMoaParams();

			if (parameters == true) {

				/* check the types of the parameters of any method of this class. 
				 * If any of them is an existing class of the project, add it.
				 */
				for (int l = 0; l < methods [k].getParameterTypes ().length; l++) {
					if (ClassObj.classes.contains (methods [k].getParameterTypes () [l]) ) {
						variableTypes.add ((methods [k].getParameterTypes () [l]));
					}
				}
			}

			boolean variables = InputPage.getOptions().getMoaInsideParams();

			if (variables == true) {

				/* We can get the variables that a method uses, 
				 * only by check the method's code, so we must
				 * get the source code of the method.
				 * The code is clear (no comments, no string inside quotes).
				 */
				//String mName = methods [k].getName();
				String [] methodCode = MethodObj.sourCode(methods [k]);

				for (int a = 0; a < methodCode.length; a ++) {

					// check if any class name is mentioned into the code.
					for (Class <?> cl: ClassObj.classes) {
						//ClassObj clObj = new ClassObj (cl);
						String className = ClassObj.plName(cl);
						if (methodCode [a].contains(className)) {

							// At first check the previous word
							boolean z = true; // If this become "false", the "cl" should not be added to the arraylist
							String previous = JavaFile.getPreviousWord(methodCode [a], className);
							if (previous.contentEquals("instanceOF")) z = false;

							// cut the string at the end of the className
							methodCode [a] = methodCode [a].substring(methodCode [a].indexOf(className) + className.length());

							// Remove any white spaces at the beginning (and at the end) of the string
							methodCode [a] = methodCode [a].trim();

							/* check if the first char of the string is ".".
							 * In this case, there is a static reference to a method 
							 * of the class and it should not be counted
							 */
							char ch = methodCode [a].charAt(0);
							if (ch != '.' && z == true) {
								variableTypes.add(cl);
							}
						}
					}
				}

			}
		}
		
		/* The types of the variables must be distinct
		 * and each type should appear only once.
		 * So duplicates must be eliminated.
		 */
		Set <Class <?>> set = new HashSet <> (variableTypes);
		variableTypes.clear();
		variableTypes.addAll(set);
		
		moa = variableTypes.size();
		return moa;
	}
	
	public static double measFunctAbstr (Class <?> c) {
		/**
		 * This method calculates the "Measure of Functional Abstraction" (MFA) metric,
		 * which is the division between the inherited methods, by all the methods of the class
		 * (inherited and declared).
		 */

		double mfa = 0;

		Method [] methods = Metrics.methods;
		
		// Do not get the overridden and hidden methods from the superclass
		Collection <Method> allMethods = MethodObj.getAllMethods (c,  true,  false);
		
		int inh = allMethods.size() - methods.length;
		
		int all = allMethods.size();
		
		// Avoid division by zero
		if (all == 0) all = 1;
		
		mfa = (double) inh / all;
		
		// This trick gives at most 2 decimal digits and it's locale independent
		mfa = Math.round (mfa * 100.0) / 100.0;

		return mfa;
	}
	
	public static int numOfPolyMeths (Class <?> c) {
		/**
		 * This method returns the number of the methods of a class, 
		 * which may have polymorphic behavior
		 */
		int nop = 0;
		Method [] thisMethods = Metrics.methods;
		String [] thisMethodsNames = new String [thisMethods.length];
		Set <Method> polyMethods = new HashSet <> ();
		
		// Get the names of the methods
		for (int i = 0; i < thisMethods.length; i++) {
			thisMethodsNames [i] = thisMethods [i].getName();
		}
		
		// Check which methods are "abstract"
		for (int i = 0; i < thisMethods.length; i++) {
			// If a method is abstract, it may have polymorphic behavior
			if (Modifier.isAbstract (thisMethods[i].getModifiers() )) {
				polyMethods.add(thisMethods[i]); 
			}
		}
		
		// Get the children of the class
		ArrayList <Class<?>> children = Metrics.children;
		for (Class <?> child: children) {
			Method [] childMethods = MethodObj.purifyMethods (child);
			String [] childMethodsNames = new String [childMethods.length];
			
			// Get the names of the methods of the child
			for (int i = 0; i < childMethods.length; i++) {
				childMethodsNames [i] = childMethods [i].getName();
			}
			
			/* Check if child has a method which overrides or hides any method of this class.
			 * The method of this class must be public or protected.
			 */
			for (int i = 0; i < thisMethods.length; i++) {
				for (int a = 0; a < childMethods.length; a++) {
					if ((Modifier.isPublic (thisMethods[i].getModifiers() ) || Modifier.isProtected (thisMethods[i].getModifiers() )) 
							&& childMethodsNames [a].equals(thisMethodsNames [i])) {
						polyMethods.add(thisMethods[i]); 
					}
				}
			}
		}
		
		nop = polyMethods.size();
		
		return nop;
	}
	
	public static int classInterSize (Class <?> c) {
		/**
		 * This method returns a number, that represents the
		 * interface of the class. This number is the number of 
		 * the "public" methods of the class.
		 */
		
		int cis = 0;
		Method [] methods = Metrics.methods;
		
		for (int i = 0; i < methods.length; i++) {
			if (Modifier.isPublic (methods[i].getModifiers() )) {
				cis++;
			}
		}
		
		return cis;
	}
	
	public static int numOfMethods (Class<?> c) {
		/**
		 * This method calculates the NOM metric, which means
		 * the number of the public, protected, default (package)
		 * access, and private methods of the class
		 */
		int nom = 0;
		Method methods[] = Metrics.methods;
		/* "methods"contains the methods that are public, protected, 
		 * default (package) access or private */
		
		nom = methods.length; // get the number of the methods
		return nom;
	}
	
	public static double reusability (int dcc, double cam, int cis, int dsc) {
		/**
		 * This method calculates the QMOOD model's "reusability".
		 */
		
		double reuse = (- 0.25) * dcc + 0.25 * cam + 0.5 * cis + 0.5 * dsc;
		
		// This trick gives at most 2 decimal digits and it's locale independent
		reuse = Math.round (reuse * 100.0) / 100.0;
		
		return reuse;
	}
	
	public static double flexibility (double dam, int dcc, int moa, int nop) {
		/**
		 * This method calculates the QMOOD model's "flexibility".
		 */
		
		double flex = 0.25 * dam - 0.25 * dcc + 0.5 * moa + 0.5 * nop;
		
		// This trick gives at most 2 decimal digits and it's locale independent
		flex = Math.round (flex * 100.0) / 100.0;
		
		return flex;
	}
	
	public static double understandability (double ana, double dam, int dcc, double cam, int nop, int nom, int dsc) {
		/**
		 * This method calculates the QMOOD model's "understandability".
		 */
		
		double under = (- 0.33) * ana + 0.33 * dam - 0.33 * dcc + 0.33 * cam - 0.33 * nop - 0.33 * nom - 0.33 * dsc;

		// This trick gives at most 2 decimal digits and it's locale independent
		under = Math.round (under * 100.0) / 100.0;
		
		return under;
	}
	
	public static double functionality (double cam, int nop, int cis, int dsc, int noh) {
		/**
		 * This method calculates the QMOOD model's "functionality".
		 */
		
		double func = 0.12 * cam + 0.22 * nop + 0.22 * cis + 0.22 * dsc + 0.22 * noh;

		// This trick gives at most 2 decimal digits and it's locale independent
		func = Math.round (func * 100.0) / 100.0;
		
		return func;
	}
	
	public static double extendibility (double ana, int dcc, double mfa, int nop) {
		/**
		 * This method calculates the QMOOD model's "extendability".
		 */
		
		double exte = 0.5 * ana - 0.5 * dcc + 0.5 * mfa + 0.5 * nop;

		// This trick gives at most 2 decimal digits and it's locale independent
		exte = Math.round (exte * 100.0) / 100.0;
		
		return exte;
	}
	
	public static double effectiveness (double ana, double dam, int moa, double mfa, int nop) {
		/**
		 * This method calculates the QMOOD model's "effectiveness".
		 */
		
		double effe = 0.2 * ana + 0.2 * dam + 0.2 * moa + 0.2 * mfa + 0.2 * nop;

		// This trick gives at most 2 decimal digits and it's locale independent
		effe = Math.round (effe * 100.0) / 100.0;
		
		return effe;
	}
}