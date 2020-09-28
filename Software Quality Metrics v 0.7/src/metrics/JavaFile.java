package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class JavaFile extends GenericFile {
	
	private static final long serialVersionUID = -4138581110383357352L;
	
	protected static ArrayList<JavaFile> javaFiles;
	
	//Instance variables
	private Path javaPath; 
	
	//Constructor
	public JavaFile(String pathname, String plainName, Path jPath) {
		super(pathname, plainName);
		this.setJavaPath(jPath);
	}

	public String getPackage() {	   
		/**
		 *  This method gets the package name that the java file belongs, scanning the text
		 */
		
		File path = this.getAbsoluteFile();
		Scanner sc = null;
		String a = null;
		String input;		
		
		try {
			sc = new Scanner(path, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		while (sc.hasNextLine()) {
			input = sc.nextLine();
			
			if (input.contains("package ") ) {
				a = getNextWord(input, "package");
				if (getNextWord(input, "package") != null) {
					break; // As the word "package" usually exists at the first line, there is no need to continue
				}
			}
			
		}
		return a;
	}
	
	public static ArrayList<JavaFile> getJavaFiles() { 
		return javaFiles; 
	}

	public static String getNextWord (String text, String word) {
		/**
		 *  This method returns the next word to the given "word", inside the given "text"
		 */
			
			String nextWord = null;
			try {
				// check the index of the word
				int index = text.indexOf(word);
				
				// cut the text at the beginning of the word
				String textPart = text.substring(index); 
				
				// check if there is any spaces after the word
				if(textPart.indexOf(" ") > 0) {
					
					// cut the text at the beginning of the next word
					textPart = textPart.substring(textPart.indexOf(" ") + 1);
		
					// check the index after the end of the word
					int a = textPart.indexOf(" ");
					int b = textPart.indexOf("{"); // This is because, in many cases, there is no space before curly bracket 
					int c = textPart.indexOf(";"); // Or a ";" follows without space between
					
					// get only the next word and not the rest ones
					
					if(a > 0 && b > a) {
						nextWord = textPart.substring (0, a);
					}
					else if (a > 0 && c > a) {
						nextWord = textPart.substring (0, a);
					}
					else if (a > 0 && b < a && b > 0) {
						nextWord = textPart.substring (0, b);
					}
					else if (a > 0 && c < a && c > 0) {
						nextWord = textPart.substring (0, c);
					}
					else if (b > 0) {
						nextWord = textPart.substring (0, b);
					}
					else if (c > 0 ) {
						nextWord = textPart.substring (0, c);
					}
					else
			            nextWord = textPart;
				}
			} catch (IndexOutOfBoundsException ex) {
			}
			return nextWord;
		}
	
	public static String getPreviousWord (String text, String word) {
		/**
		 *  This method returns the previous word of the given "word", inside the given "text"
		 */
		
		String prevWord = null;
		
		// check the index of the word
		int index = text.indexOf (word);
		if (index == -1) return prevWord;
		
		// cut the text at the beginning of the word and keep the 1st part
		String textPart = text.substring (0, index); 
		
		// get the last word of the phrase
		prevWord = textPart.replaceAll("^.*?(\\w+)\\W*$", "$1"); // Thanks to "dasblinkenlight" (from stackoverflow.com)
		
		return prevWord;
		
	}
	
	public static int checkIfComment (String line) {
		/**
		 * This method checks if the input (a line of code) is a comment or code and returns 
		 * "1" 	for code
		 * "-1"	for comment
		 * "0" 	for both
		 * "2" for null or empty line
		 */

		// check if the line is empty and if not, get its length
		int len = 0;
		if (line.isEmpty()) {
			return 2;
		}
		else {
	    	len = line.length();
	    }
    	int i = 0;
        	 
    	// check if there is any white character at the beginning of the line
    	while (((line.charAt(i) == ' ') || line.charAt(i) == '\t') && i < len - 1) {
    		// if so, define the index of the first character
        	i++;
        }
    	
    	// get the first character which is not white
    	char a = line.charAt(i);
    
    	// check if the line does not start with a comment and does not consists of white characters
    	if (!((a == '/' && line.charAt(i+1) == '*') || a == '*' || (a == '/' && line.charAt(i+1) == '/')) && a != ' ' && a != '\t') {
    		
    		// check if the line is both: code and comment
    		if (line.contains("//") || line.contains("/*")) {
    			return 0;
    		}
    		else return 1;
    	}
	    return -1;	    
	}
	
	public static int countChar (String str, char ch) {
		/**
		 * This method counts how many times a character appears in a String
		 */
		
	    int count = 0;

	    for(int i=0; i < str.length(); i++)
	    {    if(str.charAt(i) == ch)
	            count++;
	    }

	    return count;
	}
	
	public static ArrayList <Integer> getIndexesOfChar (String input, char ch) {
		
		/**
		 * This method returns an arraylist containing the indexes 
		 * of the given character inside the given string
		 */
		
		ArrayList <Integer> indexes = new ArrayList <>();
		
		
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ch) {
				indexes.add(i);
			}
		}
		
		return indexes;
	}
	
	public static String clearInsideStrings (String input) {
		
		/**
         * This method clears the parts of the string 
         * that are inside quotes
         */
       
    	while (input.indexOf('\"') != -1){
    		int start = input.indexOf('\"');
    		char a1 = input.charAt(start-1);
    		
    		/* This "while loop checks if there is a (\") character and ignores it.
    		 * If a (") character exists inside the string, pass to the next one.
    		 */
	        while (a1 == '\\') {
	        	start = input.indexOf('\"', start+1);
	        	if (start == -1) {
	        		return input;
	        	}
	        	a1 = input.charAt(start-1);
	        }
	        int end = input.indexOf('\"', start + 1);
	        
	        
	        if (end == -1) {
	        	return input;
	        }
	        char b = input.charAt(end-1);
	        
	      //This "while loop checks if there is a (\") character and ignores it.
	        while (b == '\\') {
	        	end = input.indexOf('\"', end+1);
	        	if (end == -1) return input;
	        	b = input.charAt(end-1);
	        }
	        String sub = input.substring(start, end+1); 
	        input = input.replace(sub, "");
	    }
    	return input;
    }

	/**
	 * @return the javaPath
	 */
	public Path getJavaPath() {
		return javaPath;
	}

	/**
	 * @param javaPath the javaPath to set
	 */
	public void setJavaPath(Path javaPath) {
		this.javaPath = javaPath;
	}
}
