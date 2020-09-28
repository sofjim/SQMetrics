package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MethodObj {

	Class <?> methodClass;
	Method currentMethod;
	String name;
	int start;
	int end;
	int complexity;
	int [] startEnd;
	String [] sourceCode;
	Field [] accessedFields;
	ArrayList <String> mods;
	
	// Constructor
	public MethodObj (Method m) {
		methodClass = m.getDeclaringClass ();
		name = m.getName();
		startEnd = startEndMethod (m);
		start = startEnd [0];
		end = startEnd [1];
		complexity = cyclComplex (m);
		sourceCode = sourCode (m);
		accessedFields = accessFields (m);
		currentMethod = m;
		mods = modifiers(m);
	}
	
	// getters
	public Class<?> getMethodClass() {
		return methodClass;
	}


	public Method getCurrentMethod() {
		return currentMethod;
	}


	public String getName() {
		return name;
	}


	public String[] getSourceCode() {
		return sourceCode;
	}


	public Field[] getAccessedFields() {
		return accessedFields;
	}


	public ArrayList<String> getMods() {
		return mods;
	}

	public int getComplexity() {
		return complexity;
	}
	
	public int getStart() {
		return start;
	}


	public int getEnd() {
		return end;
	}
	
	// other methods
	public static ArrayList <Method> otherMethodsCalled (Method m) {
		/**
		 * This method returns an array list that contains 
		 * the methods (of all classes) that this method calls
		 */
		
		
		/*
		 *  methods of other classes that communicate with the methods of this class
		 *  will be added into the following array list.
		 */
		ArrayList <Method> otherMethods = new ArrayList <Method> ();
		
		// get the classes of the whole project and remove the current method's class
		ArrayList <Class<?>> otherClasses = new ArrayList <>(ClassObj.classes);
		otherClasses.remove(m.getDeclaringClass());
		
		// get the source code of the method
		String [] thisMethodCode = MethodObj.sourCode (m);
		
		// clear the code and keep only words
		for (int a = 0; a < thisMethodCode.length; a++) {
			String [] tempLine = thisMethodCode [a].split("[^\\w']+");
			String line = "";
			for (int b = 0; b < tempLine.length; b++) {
				line = line + " " + tempLine [b];
			}
			thisMethodCode [a] = line;
		}
		
		/*
		 * First check which methods from the other classes, this method call.
		 */
	
		// check where in the method's code, the name of another class appears
		for (int a = 0; a < thisMethodCode.length; a++) {
			for (Class<?> otherClass : otherClasses) {
				String otherClassName = otherClass.getSimpleName();
				
				// get the methods of the other class
				Method [] otherClassMethods = otherClass.getDeclaredMethods();
				
				// get the names of the other methods
				String [] otherMethodsNames = new String [otherClassMethods.length];
				for (int i = 0; i < otherClassMethods.length; i++) {
					otherMethodsNames [i] = otherClassMethods [i].getName();
				}
				
				/*
				 *  if a class name arrears, the next word is either
				 *  the name of a method or
				 *  it is the name of an object of this class, in case
				 *  that the previous word is not "new"
				 */
				if (thisMethodCode [a].contains(otherClassName)) {
					String next = "";
					next = JavaFile.getNextWord (thisMethodCode [a], otherClassName);
					boolean prevIsNew = JavaFile.getPreviousWord (thisMethodCode [a], otherClassName) == "new";
					
					String prev = "";
					
					// if the next word is a method's name, add this method to array list
					for (int i = 0; i < otherMethodsNames.length; i++) {
						if (otherMethodsNames[i].equals(next)) {
							otherMethods.add(otherClassMethods[i]);
							break;
						}
						else {
							prev = JavaFile.getPreviousWord (thisMethodCode [a], otherMethodsNames[i]);
						}
						
						/*
						 *  if the previous word of the method's name is the name of an object
						 *  of this class, then the method should be added to the array list
						 */
						if (next != null && prev != null && next.equals(prev) && prevIsNew == false) {
							otherMethods.add(otherClassMethods[i]);
							break;
						}
					}
				}
			}
		}
		
		// Each method should appears only 1 time. So duplicates must be eliminated.
		Set <Method> set = new HashSet <> (otherMethods);
		otherMethods.clear();
		otherMethods.addAll(set);
					
		return otherMethods;
	}
	
	public ArrayList <String> modifiers (Method m) {
		/**
		 * This method returns an array list with the modifiers names of the method m
		 */
		
		
		String [] mod = Modifier.toString (m.getModifiers()).split("[^\\w']+");
		ArrayList <String> mods = new ArrayList <>(Arrays.asList (mod));
		
		return mods;
	}
	
	public static Field [] accessFields (Method m) {
		/**
		 * This method returns an array containing the fields that the method access
		 */
		
		ArrayList <String> af = new ArrayList <>();
		
		Class<?> c = m.getDeclaringClass();
		
		// get the class's fields
		Field [] classFields = c.getDeclaredFields();
		
		// get the names of the fields
		String [] fieldNames = new String [classFields.length];

		for (int i = 0; i < classFields.length; i++) {
			fieldNames [i] = classFields[i].getName();
		}
		
		// get the source code of the method as an array. Each cell is 1 line of code
		String [] source = sourCode (m);
		for (int i = 0; i < source.length; i++) {
			
			// split to words only
			String[] splitedString = source [i].split("[^\\w']+");	
			
			// check if the name of the field exists into the code
			for (int k = 0; k < fieldNames.length; k++) {
				for (int a = 0; a < splitedString.length; a++) {
					if (splitedString [a].equals(fieldNames[k]) && !(af.contains(fieldNames[k]))) {
						af.add(fieldNames[k]);
					}
				}
			}
		}
		int d = af.size();
		Field [] accFields = new Field [d];
		int a = 0;
		for (String fieldN : af) {
			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldN.equals(fieldNames [i]) ) {
					accFields [a] = classFields [i];
				}
			}
		a++;	
			
			
		}
		return accFields;
	}
	
	public static Field [] accessInstanceFields (Method m) {
		/**
		 * This method returns an array containing the instance (non static) 
		 * fields that the method access
		 */
		
		ArrayList <String> af = new ArrayList <>();
		
		Class<?> c = m.getDeclaringClass();
		
		// get the instance (non static) fields of the class
		ArrayList <Field> fields  = ClassObj.instanceFields(c);
		
		// get the names of the fields
		String [] fieldNames = new String [fields.size()];
		Field [] instanceFields = new Field [fields.size()];
		int ix = 0;
		for (Field f : fields) {
			
			// these 2 arrays have the same order
			fieldNames [ix] = f.getName();
			instanceFields  [ix] = f;
			ix++;
		}
		
		// get the source code of the method as an array. Each cell is 1 line of code
		String [] source = sourCode (m);
		for (int i = 0; i < source.length; i++) {
			
			// split to words only
			String[] splitedString = source [i].split("[^\\w']+");	
			
			// check if the name of the field exists into the code
			for (int k = 0; k < fieldNames.length; k++) {
				for (int a = 0; a < splitedString.length; a++) {
					if (splitedString [a].equals(fieldNames[k]) && !(af.contains(fieldNames[k]))) {
						af.add(fieldNames[k]);
					}
				}
			}
		}
		int d = af.size();
		Field [] accFields = new Field [d];
		int a = 0;
		for (String fieldN : af) {
			for (int i = 0; i < fieldNames.length; i++) {
				if (fieldN.equals(fieldNames [i]) ) {
					accFields [a] = instanceFields [i];
				}
			}
		a++;	
			
			
		}
		return accFields;
	}

	public static String [] sourCode (Method m) {
		/**
		 * This method returns an array containing the source code of the method.
		 * Each cell is 1 line of code. The declaration line is not included.
		 */
		
		int end = startEndMethod(m)[1];
		int start = startEndMethod(m)[0];
		String [] sCode = new String [end - start +1];
		
		Class<?> c = m.getDeclaringClass();
		String javaName = ClassFile.javaFilePath(c);
		int totalLines = Metrics.linesOfCode(c);
		
		// scan the file that contains the source code
				Scanner sc;
				try {
					sc = new Scanner (new File(javaName), "UTF-8");
					String input;
					int line = 0;
					int index = 0;
					while (sc.hasNextLine() && line <= totalLines) {	
						input = sc.nextLine();
						
						// measure only the lines of the method
						if (line >= start && line <= end) {
							
							// register the code lines into the array
							sCode [index] = input;
							index++;
						}
						line++;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				for (int i = 0; i < sCode.length; i++) {
					
					// check if it is comment
					int a = JavaFile.checkIfComment (sCode [i]);
					
					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int n = sCode [i].length();
						if (sCode [i].contains("//")) {
							n = sCode [i].indexOf("//");
						}
						else if (sCode [i].contains("/*")) {
							n = sCode [i].indexOf("/*");
						}
						sCode [i] = sCode [i].substring(0, n);
					}
					
					// if it is comment, it's not code. Make it a blank line.
					else if (a == -1) {
						sCode [i] = " ";
					}
					// if there is any string into quotes, it's not code. Cut them.
					sCode [i] = JavaFile.clearInsideStrings(sCode [i]);
				}
		return sCode;
	}
	
	public static String [] sourCode (Constructor <?> cons) {
		/**
		 * This method returns an array containing the source code of the method.
		 * Each cell is 1 line of code. The declaration line is not included.
		 */
		
		int end = startEndConstructor(cons)[1];
		int start = startEndConstructor(cons)[0];
		String [] sCode = new String [end - start +1];
		
		Class<?> c = cons.getDeclaringClass();
		String javaName = ClassFile.javaFilePath(c);
		int totalLines = Metrics.linesOfCode(c);
		
		// scan the file that contains the source code
				Scanner sc;
				try {
					sc = new Scanner (new File(javaName), "UTF-8");
					String input;
					int line = 0;
					int index = 0;
					while (sc.hasNextLine() && line <= totalLines) {	
						input = sc.nextLine();
						
						// measure only the lines of the method
						if (line >= start && line <= end) {
							
							// register the code lines into the array
							sCode [index] = input;
							index++;
						}
						line++;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				for (int i = 0; i < sCode.length; i++) {
					
					// check if it is comment
					int a = JavaFile.checkIfComment (sCode [i]);
					
					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int n = sCode [i].length();
						if (sCode [i].contains("//")) {
							n = sCode [i].indexOf("//");
						}
						else if (sCode [i].contains("/*")) {
							n = sCode [i].indexOf("/*");
						}
						sCode [i] = sCode [i].substring(0, n);
					}
					
					// if it is comment, it's not code. Make it a blank line.
					else if (a == -1) {
						sCode [i] = " ";
					}
					// if there is any string into quotes, it's not code. Cut them.
					sCode [i] = JavaFile.clearInsideStrings(sCode [i]);
				}
		return sCode;
	}

	public static int cyclComplex (Method m) {		
		/**
		 * This method returns the cyclomatic complexity of the method "m"
		 */
		
		int cc = 1;
		int returnCount = 0;
		
		// get the class that the method belongs
		Class<?> c = m.getDeclaringClass(); 
		String javaName = ClassFile.javaFilePath(c);
		int totalLines = Metrics.linesOfCode(c);
		
		// get the index of the start and the end of the method's code, in the text file
		int [] startEnd = startEndMethod (m);
		int start = startEnd [0];
		int end = startEnd [1];
		
		// make an array containing the key words to count complexity
		String [] wordsToCheck = {"if", "case", "default", "for", "while", "break", 
				"continue", "catch", "finaly", "throw", "throws"};
		
		// make an array to store the code lines of the method
		String [] codeLine = new String [end - start +1];
		
		// scan the file that contains the source code
		Scanner sc;
		try {
			sc = new Scanner (new File(javaName), "UTF-8");
			String input;
			int line = 0;
			int index = 0;
			while (sc.hasNextLine() && line <= totalLines) {	
				input = sc.nextLine();
				
				// measure only the lines of the method
				if (line >= start && line <= end) {
					
					// register the code lines into the array
					codeLine [index] = input;
					index++;
				}
				line++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < codeLine.length; i++) {
			
			// check if it is comment
			int a = JavaFile.checkIfComment (codeLine [i]);
			
			// cut the comment part (if exists) and keep the code
			if (a == 0 || a == 1) {
				int n = codeLine [i].length();
				if (codeLine [i].contains("//")) {
					n = codeLine [i].indexOf("//");
				}
				else if (codeLine [i].contains("/*")) {
					n = codeLine [i].indexOf("/*");
				}
				codeLine [i] = codeLine [i].substring(0, n);
			}
			
			// if it is comment, it must not been calculated
			else if (a == -1) {
				codeLine [i] = " ";
			}
			// if there is any string into quotes, it's not code. Cut them.
			codeLine [i] = JavaFile.clearInsideStrings(codeLine [i]);
			
			/*
			 *  The &&, ||, ? and : operators must be counted 
			 */
			if (codeLine [i].contains ("&&")) {
				// check how many time the "word" appears
				String s = codeLine [i];
				int index = s.indexOf("&&");
				int count = 0;
				while (index != -1) {
				    count++;
				    s = s.substring(index + 1);
				    index = s.indexOf("&&");
				}
				cc = cc + count;
			}
			if (codeLine [i].contains ("||")) {
				// check how many time the "word" appears.
				String s = codeLine [i];
				int index = s.indexOf("||");
				int count = 0;
				while (index != -1) {
				    count++;
				    s = s.substring(index + 1);
				    index = s.indexOf("||");
				}
				cc = cc + count;
			}
			
			if (codeLine [i].contains ("?")) {
				// check how many time the "word" appears.
				String s = codeLine [i];
				int index = s.indexOf("?");
				int count = 0;
				while (index != -1) {
				    count++;
				    s = s.substring(index + 1);
				    index = s.indexOf("?");
				}
				cc = cc + count;

			}
			if (codeLine [i].contains (":")) {
				// check how many time the "word" appears.
				String s = codeLine [i];
				int index = s.indexOf(":");
				int count = 0;
				while (index != -1) {
				    count++;
				    s = s.substring(index + 1);
				    index = s.indexOf(":");
				}
				cc = cc + count;

			}
			
			// split to words only
			String[] splitedString = codeLine [i].split("[^\\w']+"); 
			ArrayList <String> words = new ArrayList <> (Arrays.asList(splitedString));
			
			// check if any of the key words appears
			for (String word : wordsToCheck) {
				if (words.contains(word)) {
					
					// check how many time the "word" appears
					int count = 0;
					for (String s : words) {
						if (s.equals(word)) {
							count++;
						}
					}
					cc = cc + count;
				}
			}
			// count the times that "return" appears
			if (words.contains("return")) {
				returnCount++;
			}
			
		}
		// the last appearing of the word "return" must not count
		if (returnCount > 0){
			returnCount--;
		}
		
		return cc + returnCount;
	}
	
	public static int [] startEndMethod (Method m) {
		/**
		 * This method returns an array of 2 integers representing 
		 * the start and the end lines of the method "m"
		 */
		
		int [] stEnd = new int [2];
		
		// get the modifiers of the method
		String mods = Modifier.toString(m.getModifiers());
		String [] methodMods = mods.split("[^\\w']+");
		
		// get the class of the method
		Class <?> c = m.getDeclaringClass();
		
		// get the class' methods
		Method [] methods = c.getDeclaredMethods();
		
		// remove this method 
		ArrayList <Method> otherMethod = new ArrayList  <>();
		for (int i = 0; i < methods.length; i++) {
			otherMethod.add(methods [i]);
		}
		otherMethod.remove(m);
		Method [] otherMethods = otherMethod.toArray (new Method [otherMethod.size()]);
		
		// get the names, the modifiers and the types of the other methods
		String [] otherNames = new String [methods.length - 1];
		String [] otherTypes = new String [methods.length - 1];
		
		String [] otherMods = new String [methods.length -1];
		for (int i = 0; i < methods.length - 1; i++) {
			otherNames [i] = otherMethods [i].getName();
			otherTypes [i] = otherMethods [i].getReturnType().getSimpleName().replaceAll("[^a-zA-Z0-9]", "");
			otherMods [i] = Modifier.toString(otherMethods [i].getModifiers());
			
		}
		
		// get the text file that contains the source code of the class
		String javaName = ClassFile.javaFilePath(c);
		
		// get the name and the return type of the method
		String methodName = m.getName();
		String methodType = m.getReturnType().getSimpleName().replaceAll("[^a-zA-Z0-9]", "");
		
		// scan the file
		Scanner sc;
		try {
			sc = new Scanner(new File(javaName), "UTF-8");
			String input;
			int start = 0;
			boolean methodFound = false;
			// check if the next line exists
			while (sc.hasNextLine() && methodFound == false) {
				start++;
				// if exists make it a String
				input = sc.nextLine();
				
				// check if it is comment
				int a = JavaFile.checkIfComment (input);
				
				// cut the comment part (if exists) and keep the code
				if (a == 0 || a == 1) {
					int k = input.length();

					if (input.contains("//")) {
						k = input.indexOf("//");
					}
					else if (input.contains("/*")) {
						k = input.indexOf("/*");
					}
					input = input.substring(0, k);
				}
				// if it is comment, it must not been calculated
				else if (a == -1) {
					input = " ";
				}
				// if there is any string into quotes, it's not code. Cut them.
				input = JavaFile.clearInsideStrings(input);
				
				// split to words only
				String[] words = input.split("[^\\w']+");
					
				// check if input contains the method's name, the type and the modifiers
				boolean modExists = false;
				for (int a1 = 0; a1 < methodMods.length; a1++) {
					modExists = Arrays.asList(words).contains(methodMods [a1]);
				}
				methodFound = (Arrays.asList(words).contains(methodName) && Arrays.asList(words).contains(methodType) && modExists);	
			}
			stEnd [0] = start;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sc = new Scanner(new File(javaName), "UTF-8");
			String input;
			int end = 0;
			boolean nextMethodFound = false;
			// check if the next line exists
			while (sc.hasNextLine() && nextMethodFound == false) {
				end++;
				// if exists make it a String
				input = sc.nextLine();
				
				if (end > stEnd [0] && end <= (Metrics.linesOfCode(c)) && nextMethodFound == false) {

					// check if it is comment
					int a = JavaFile.checkIfComment (input);
					
					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int k = input.length();
						if (input.contains("//")) {
							k = input.indexOf("//");
						}
						else if (input.contains("/*")) {
							k = input.indexOf("/*");
						}
						input = input.substring(0, k);
					}
					// if it is comment, it must not been calculated
					else if (a == -1) {
						input = " ";
					}
					// if there is any string into quotes, it's not code. Cut them.
					input = JavaFile.clearInsideStrings(input);
					
					// split to words only
					String[] words = input.split("[^\\w']+");
					
					for (int i = 0; i < otherNames.length; i++) {
						
						// check if input contains the method's name, the type and the modifiers
						
						String [] otherModWords= otherMods[i].split("[^\\w']+");
						boolean modExists = false;
						for (int a1 = 0; a1 < otherModWords.length; a1++) {
							modExists = Arrays.asList(words).contains(otherModWords [a1]);
						}
						
						if (Arrays.asList(words).contains(otherNames [i]) && Arrays.asList(words).contains(otherTypes [i]) && modExists) {
							nextMethodFound = true;
						}
					}
				}
			}
			stEnd [1] = end - 1;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stEnd;
	}
	
	public static int [] startEndConstructor (Constructor <?> cons) {
		/**
		 * This method returns an array of 2 integers representing 
		 * the start and the end lines of the constructor "cons".
		 */
		
		int [] stEnd = new int [2];
		
		// get the modifiers of the constructor
		String mods = Modifier.toString(cons.getModifiers());
		String [] consMods = mods.split("[^\\w']+");
		
		// get the class of the constructor
		Class <?> c = cons.getDeclaringClass();
		
		// get the class's constructors
		Constructor <?> [] constructors = c.getDeclaredConstructors();
		
		// remove this constructor
		ArrayList <Constructor <?>> otherCon = new ArrayList  <>();
		for (int i = 0; i < constructors.length; i++) {
			otherCon.add(constructors [i]);
		}
		otherCon.remove(cons);
		Constructor <?> [] otherCons = otherCon.toArray (new Constructor [otherCon.size()]);
		
		// get the class's methods
		Method [] methods = c.getDeclaredMethods();
		
		// get the names, the modifiers and the types of the methods
		String [] methodsNames = new String [methods.length];
		String [] methodsTypes = new String [methods.length];
		String [] methodsMods = new String [methods.length];

		for (int i = 0; i < methods.length; i++) {
			methodsNames [i] = methods [i].getName();
			methodsTypes [i] = methods [i].getReturnType().getSimpleName().replaceAll("[^a-zA-Z0-9]", "");
			methodsMods [i] = Modifier.toString(methods [i].getModifiers());
					
		}
		
		// get the names, the modifiers of the other constructors
		String [] otherConNames = new String [otherCons.length];
		
		String [] otherConMods = new String [otherCons.length];
		for (int i = 0; i < otherCons.length; i++) {
			otherConNames [i] = otherCons [i].getName();
			otherConMods [i] = Modifier.toString(otherCons [i].getModifiers());
		}
		
		// get the text file that contains the source code of the class
		String javaName = ClassFile.javaFilePath(c);
		
		// get the name and the type of the method
		String consName = cons.getName();
		
		// scan the file
		Scanner sc;
		try {
			sc = new Scanner(new File(javaName), "UTF-8");
			String input;
			int start = 0;
			boolean consFound = false;
			// check if the next line exists
			while (sc.hasNextLine() && consFound == false) {
				start++;
				// if exists make it a String
				input = sc.nextLine();
				
				// check if it is comment
				int a = JavaFile.checkIfComment (input);
				
				// cut the comment part (if exists) and keep the code
				if (a == 0 || a == 1) {
					int k = input.length();
					if (input.contains("//")) {
						k = input.indexOf("//");
					}
					else if (input.contains("/*")) {
						k = input.indexOf("/*");
					}
					input = input.substring(0, k);
				}
				// if it is comment, it must not been calculated
				else if (a == -1) {
					input = " ";
				}
				// if there is any string into quotes, it's not code. Cut them.
				input = JavaFile.clearInsideStrings(input);
				
				// split to words only
				String[] words = input.split("[^\\w']+");
					
				// check if input contains the constructor's name and the modifiers
				boolean modExists = false;
				for (int a1 = 0; a1 < consMods.length; a1++) {
					modExists = Arrays.asList(words).contains(consMods [a1]);
				}
				consFound = (Arrays.asList(words).contains(consName) && modExists);	
			}
			stEnd [0] = start;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sc = new Scanner(new File(javaName), "UTF-8");
			String input;
			int end = 0;
			boolean nextFound = false;
			// check if the next line exists
			while (sc.hasNextLine() && nextFound == false) {
				end++;
				// if exists make it a String
				input = sc.nextLine();
				
				if (end > stEnd [0] && end <= (Metrics.linesOfCode(c)) && nextFound == false) {

					// check if it is comment
					int a = JavaFile.checkIfComment (input);
					
					// cut the comment part (if exists) and keep the code
					if (a == 0 || a == 1) {
						int k = input.length();
						if (input.contains("//")) {
							k = input.indexOf("//");
						}
						else if (input.contains("/*")) {
							k = input.indexOf("/*");
						}
						input = input.substring(0, k);
					}
					// if it is comment, it must not been calculated
					else if (a == -1) {
						input = " ";
					}
					// if there is any string into quotes, it's not code. Cut them.
					input = JavaFile.clearInsideStrings(input);
					
					// split to words only
					String[] words = input.split("[^\\w']+");
					
					for (int i = 0; i < otherConNames.length; i++) {
						
						// check if input contains the method's name, the type and the modifiers
						
						String [] otherModWords= otherConMods[i].split("[^\\w']+");
						boolean modExists = false;
						for (int a1 = 0; a1 < otherModWords.length; a1++) {
							modExists = Arrays.asList(words).contains(otherModWords [a1]);
						}
						
						if ((Arrays.asList(words).contains(otherConNames [i]) && Arrays.asList(words).contains(methodsTypes [i]) && modExists)
								|| (Arrays.asList(words).contains(methodsNames [i]) && Arrays.asList(words).contains(methodsTypes [i]) && modExists)) {
							nextFound = true;
						}
					}
				}
			}
			stEnd [1] = end - 1;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return stEnd;
	}
	
	public static Collection <Method> getAllMethods (Class<?> c,
			boolean includeAllPackageAndPrivateMethodsOfSuperclasses, 
			boolean includeOverriddenAndHidden) {
		/**
		 * This method returns a Collection of all the methods that the class "c" has,
		 *  the methods that inherits, included the "package" and "private" (if you choose "true") and
		 * "Overridden" and "Hidden" (if you choose "true").
		 * 
		 * Many thanks to Holger, at Stack Overflow
		 * (https://stackoverflow.com/questions/28400408/what-is-the-new-way-of-getting-all-methods-of-a-class-including-inherited-defau)
		 */

		Predicate<Method> include = m -> !m.isBridge() && !m.isSynthetic()
				&& Character.isJavaIdentifierStart(m.getName().charAt(0))
				&& m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

		Set<Method> methods = new LinkedHashSet<>();
		Collections.addAll(methods, c.getMethods());
		methods.removeIf(include.negate());
		Stream.of(c.getDeclaredMethods()).filter(include).forEach(methods::add);

		final int access = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

		Package p = c.getPackage();
		if (!includeAllPackageAndPrivateMethodsOfSuperclasses) {
			int pass = includeOverriddenAndHidden ? Modifier.PUBLIC | Modifier.PROTECTED : Modifier.PROTECTED;
			include = include.and(m -> {
				int mod = m.getModifiers();
				return (mod & pass) != 0 || (mod & access) == 0 && m.getDeclaringClass().getPackage() == p;
			});
		}
		if (!includeOverriddenAndHidden) {
			Map<Object, Set<Package>> types = new HashMap<>();
			final Set<Package> pkgIndependent = Collections.emptySet();
			for (Method m : methods) {
				int acc = m.getModifiers() & access;
				if (acc == Modifier.PRIVATE)
					continue;
				if (acc != 0)
					types.put(methodKey(m), pkgIndependent);
				else
					types.computeIfAbsent(methodKey(m), x -> new HashSet<>()).add(p);
			}
			include = include.and(m -> {
				int acc = m.getModifiers() & access;
				return acc != 0 ? acc == Modifier.PRIVATE || types.putIfAbsent(methodKey(m), pkgIndependent) == null
						: noPkgOverride(m, types, pkgIndependent);
			});
		}
		for (c = c.getSuperclass(); c != null; c = c.getSuperclass())
			Stream.of(c.getDeclaredMethods()).filter(include).forEach(methods::add);
		return methods;
	}

	static boolean noPkgOverride(Method m, Map<Object, Set<Package>> types, Set<Package> pkgIndependent) {
		/** Many thanks to Holger, at Stack Overflow
		 * (https://stackoverflow.com/questions/28400408/what-is-the-new-way-of-getting-all-methods-of-a-class-including-inherited-defau)
		 */
		Set<Package> pkg = types.computeIfAbsent(methodKey(m), key -> new HashSet<>());
		return pkg != pkgIndependent && pkg.add(m.getDeclaringClass().getPackage());
	}

	private static Object methodKey(Method m) {
		/** Many thanks to Holger, at Stack Overflow
		 * (https://stackoverflow.com/questions/28400408/what-is-the-new-way-of-getting-all-methods-of-a-class-including-inherited-defau)
		 */
		return Arrays.asList(m.getName(), MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
	}
	
	public static Method [] purifyMethods (Class <?> c) {
		/** Sometimes, some clone methods have been created by the compiler.
		 * These methods must be removed or the number of the methods 
		 * will be increased highly.
		 */
		
		ArrayList <Method> purMethods = new ArrayList <> ();
		Method [] allMethods = c.getDeclaredMethods ();
		
		/* Check the start and the end of the method. If it is a clone,
		 * then the startEnd array should be null and it has no code.
		 */
		for (int i = 0; i < allMethods.length; i++){
			int [] a = startEndMethod(allMethods [i]);
			if (a [0] < a [1]) {
				purMethods.add (allMethods [i]);
			}
		}
		
		// Make an array to return
		Method [] pureMethods = new Method [purMethods.size()];
		int i = 0;
		for (Method a : purMethods){
			pureMethods [i] = a;
			i++;
		}
		
		return pureMethods;
	}
	
}
