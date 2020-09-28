package metrics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ComparePage extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3759350367451952617L;
	
	private JLabel applicationLabel;
	private JLabel promptForFiles;
	private JButton helpButton;
	private JButton backButton;
	private JButton compareButton;
	private JButton exitButton;
	private JFrame helpFrame;
	private JPanel choosePanel;
	private static JPanel mainPanel;
	private JPanel buttonPanel;
	private JPanel filePanel;
	private JTextField fileField;
	private JScrollPane scrollPane;
	private JButton newFileButton;
	private static ArrayList <File> files;
	private String fileName;
	private int y = 0;
	private static double [] [] totalValues;
	private static double [] [] normalValues;
	private static double [] [] characteristics;
	private static int [] [] normalValuesSoft;
	private static Object[][] resultsValuesSoft;
	private static JTable resultsTable;
	private static JTable resultsTable2;
	static Object [] [] results;
	private static String [] header1;
	private static String [] header2;

	// Constructor
	public ComparePage() {

		newComparePage ();
	}

	public ComparePage(LayoutManager layout) {
		
		super(layout);
		newComparePage ();
	}

	public void newComparePage () {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Initialize variables
		files = new ArrayList <> ();
		
		header1 = new String [] {"FILE", "REUSABILITY", "FLEXIBILITY", "UNDERSTANDABILITY", "FUNCTIONALITY", "EXTENDIBILITY", "EFFECTIVENESS", "OVERALL"};
		header2 = new String [] {"FILE", "Design Size", "Hierarchies", "Abstraction", "Encaplulation", "Coupling", "Cohesion", "Composition", "Inheritance", "Polymorphism", "Messaging", "Complexity"};
		
		applicationLabel = new JLabel("Software Quality Metrics Application", SwingConstants.CENTER);
		applicationLabel.setFont(new Font("Tahoma", 1, 32));
		add (applicationLabel);
		
		mainPanel = new JPanel ();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//mainPanel.setPreferredSize(new Dimension(1700, 630));
		mainPanel.setMaximumSize(new Dimension(1920, 1080));
		
		promptForFiles = new JLabel ("Choose at least 2 .csv files ---> ", SwingConstants.CENTER);
		promptForFiles.setFont(new Font("Tahoma", 1, 18));

		newFileButton = new JButton ();
		newFileButton.setFont(new Font("Tahoma", 1, 12));
		newFileButton.setText("Get .csv File");
		newFileButton.setToolTipText("You must add, at least 2 .csv files, made by this application (SQMetrics)");
		newFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				newFileButtonActionPerformed(evt);
			}
		});
		
		choosePanel = new JPanel ();
		choosePanel.setBackground(Color.LIGHT_GRAY);

		choosePanel.add(promptForFiles);
		choosePanel.add(newFileButton);
		choosePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		choosePanel.setPreferredSize(new Dimension(600, 60));
		choosePanel.setMaximumSize(new Dimension(600, 60));
		

		mainPanel.add(choosePanel);

		
		filePanel = new JPanel ();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
		//filePanel.setBackground(Color.ORANGE);
		filePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		

		scrollPane = new JScrollPane ();
		//scrollPane.setBackground(Color.RED);
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		

		
		
		add(mainPanel);

		
		// make the window for the help file
		helpFrame = new JFrame();
		
		// make buttons
		helpButton = new JButton ("Help");
		backButton = new JButton ("Back");
		compareButton = new JButton ("Compare");
        exitButton = new JButton ("Exit");
        
        helpButton.setFont(new Font("Tahoma", 1, 12));
        backButton.setFont(new Font("Tahoma", 1, 12));
        compareButton.setFont(new Font("Tahoma", 1, 12));
        exitButton.setFont(new Font("Tahoma", 1, 12));
        
        // make button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout (1, 1, 50, 0));
        //buttonPanel.setBackground (Color.GREEN);
        buttonPanel.setPreferredSize(new Dimension(1700, 30));
        buttonPanel.setMaximumSize(new Dimension(1920, 30));
 		add (buttonPanel, BorderLayout.PAGE_END);
		
 		// add buttons
 		buttonPanel.add(helpButton);
 		buttonPanel.add(backButton);
 		buttonPanel.add(compareButton);
 		buttonPanel.add(exitButton);
 		
		// add listeners to buttons
		helpButton.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent evt) {
        		helpButtonActionPerformed(evt);
        	}
        });
		
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					backButtonActionPerformed(evt);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		
		compareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				compareButtonActionPerformed(evt);
			}

		});
		
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				exitButtonActionPerformed(evt);
			}
		});

		
	}
	
	public JTextField newfileField (String path) {
		/**
		 * This method makes a new text field to display
		 * the selected file
		 */
		JTextField fileFld;
		fileFld = new JTextField ();
		fileFld.setColumns(70);
		fileFld.setEditable(false);
		fileFld.setFont(new Font("Tahoma", 0, 12));
		fileFld.setText(path);
		fileFld.setToolTipText("Add a .csv file");
		
		return fileFld;
		
	}
	
	private void newFileButtonActionPerformed(ActionEvent evt) {
		/**
		 * This method is called when the "Get .csv File" is pressed
		 */

		y++;
		File file = null;
		String fileExt = null;
		Component errorWindow = null;
		//String osName = System.getProperty("os.name");
		//String homeDir = System.getProperty("user.home");

		// Select a file


		//if (osName.equals("Mac OS X")) {// In case the OS is Mac OS X, use the OS native file dialog
		JFrame appleFrame = new JFrame();
		appleFrame.setIconImage(MainWindow.img.getImage());
		System.setProperty("apple.awt.fileDialogForFiles", "true");
		FileDialog fd = new FileDialog (appleFrame, "Choose .csv file");
		fd.setIconImage(MainWindow.img.getImage());
		//fd.setDirectory(homeDir);
		fd.setFile("*.csv");
		fd.setVisible(true);
		String fName = fd.getDirectory() + fd.getFile();
		if (fd.getFile() == null) {
			return;
		}
		file = new File (fName);
		fileName = file.getAbsolutePath();
		fileExt = GenericFile.getFileExtension (file.getName());
		//			if (fileName == null) {
		//				//System.out.println("You cancelled the choice");
		//			} 
		//			else {
		//				file = new File (fileName);
		//				if (selectedPath.isDirectory()) {
		//					//System.out.println("You chose " + filename);
		//					
		//				}
		//				else {
		//					fileName = fd.getDirectory();
		//					//System.out.println("You chose " + filename);
		//					
		//					selectedPath = new File (fileName);
		//					
		//				}
		//				//selectedPath = new File(filename);
		//
		//				
		//			}
		System.setProperty("apple.awt.fileDialogForFiles", "false");

		//		} 
		//		else {// In case the OS is Windows or Linux, use the JFileChooser dialog
		//
		//			JFileChooser chooser = new JFileChooser();
		//			chooser.setCurrentDirectory(new File("."));
		//			chooser.setDialogTitle("choose .csv file");
		//			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//
		//			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
		//
		//				fileName = chooser.getSelectedFile().getAbsolutePath();
		//				file = chooser.getSelectedFile();
		//				fileExt = GenericFile.getFileExtension (file.getName());
		//			}
		//
		//		}

		if (files.contains(file)) {
			JOptionPane.showMessageDialog(errorWindow, "You have already selected this file.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		else if (fileExt.equals("csv")) {
			files.add(file);
			addNewFileField (fileName);
		}
		// check if the file is not a 'csv file
		else {
			JOptionPane.showMessageDialog(errorWindow, "The file you have choosen, is not a .csv file.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		this.validate ();
		//this.repaint ();

	}
	
	private void helpButtonActionPerformed(ActionEvent evt) {
    	/**
    	 * This method is called when the "Help" button is pressed.
    	 * It displays, in a new window, the "help.txt" file.
    	 */
    	
        helpFrame.setSize(600, 600);
        helpFrame.setLocationRelativeTo(null);
        
        ImageIcon img = new ImageIcon("icon.jpg");
        helpFrame.setIconImage(img.getImage());
		
		JTextArea jTextArea2 = new JTextArea();
		jTextArea2.setFont(new Font("Tahoma", 0, 12));
		jTextArea2.setLineWrap(true);
		jTextArea2.setWrapStyleWord(true);
		jTextArea2.setCaretPosition(0);
		
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setOpaque(false);
		jScrollPane2.getViewport().setOpaque(false);
		jScrollPane2.setViewportView(jTextArea2);

		helpFrame.add(jScrollPane2);
		helpFrame.setVisible(true);
		
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new FileReader("Docs" + File.separator + "Compare.txt"));
			String str;
			while ((str = buff.readLine()) != null) {
				jTextArea2.append("\n" + str);
			}
		} catch (IOException e) {
		} finally {
			try {
				buff.close();
			} catch (Exception ex) {
			}
		}
		// This line is for displaying the beginning and not the end of the text.
		jTextArea2.setCaretPosition(0);
	}
	
	private void backButtonActionPerformed(ActionEvent evt) throws URISyntaxException {
		/**
    	 * This method is called when the "Back" button is pressed.
    	 * It clears all the variables and makes new pages
    	 */
    	
		helpFrame.dispose ();
		MainWindow.newCompareVersionsPage ();
		CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		cardLayout.show (MainWindow.pages, "Welcome Page");

	}
	
	private void compareButtonActionPerformed(ActionEvent evt) {

		Component dialog = null;
		if (!(files.size () > 1)) {
			JOptionPane.showMessageDialog(dialog, "You must choose at least 2 .csv files,\n"
					+ "made by the current version of SQMetrics (this application).", "Error",	JOptionPane.ERROR_MESSAGE);
		}
		else {
			helpFrame.dispose ();
			int n = newDialog (dialog);
			
			totalValues = makeFinalArray ();
			
			switch (n) {
			case 0:
				// different versions commands
				
				normDifferVers ();
				makeCharacts ();
				makeResults ();
				makeResultsTable ();

				mainPanel.remove (choosePanel);
		
				resultsTable.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				int x = files.size();
				//scrollPane.setPreferredSize(new Dimension(1320, 1 + (x + 1) * 20));
				scrollPane.setMaximumSize(new Dimension(1900, 1 + (x + 1) * 20));
				scrollPane.setViewportView(resultsTable);
				scrollPane.revalidate ();

				mainPanel.revalidate ();
				
				this.revalidate ();
				break;
				
			case 1:
				// different software commands
				normDifferSoft ();
				makeResultsSoft ();
				makeTable2 ();
				
				mainPanel.remove (choosePanel);
		
				resultsTable2.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				int x1 = files.size();
				//scrollPane.setPreferredSize(new Dimension(1320, 1 + (x1 + 1) * 20));
				scrollPane.setMaximumSize(new Dimension(1900, 1 + (x1 + 1) * 20));
				scrollPane.setViewportView(resultsTable2);
				scrollPane.revalidate ();

				mainPanel.revalidate ();
				
				this.revalidate ();
				break;
				
			case 2:
				// nothing happens
			}
			
		}
		
	}
	
	private void exitButtonActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void addNewFileField (String name) {
		/**
		 * This method adds the selected file on the screen
		 */
		
		y++;
		
		fileField = new JTextField ();
		fileField.setColumns(60);
		fileField.setEditable(false);
		fileField.setFont(new Font("Tahoma", 1, 12));
		fileField.setText(fileName);
		fileField.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		filePanel.setPreferredSize(new Dimension(700, y * 15));
		filePanel.setMaximumSize(new Dimension(700, y * 15));
		filePanel.add(fileField);
		
		int x = y;
		if (y > 27) {
			x = 27;
		}
		
		scrollPane.setPreferredSize(new Dimension(800, x * 20));
		scrollPane.setMaximumSize(new Dimension(800, x * 20));
		scrollPane.setViewportView(filePanel);
		
		
		mainPanel.add(scrollPane);
		
		
		filePanel.revalidate();
		scrollPane.revalidate();
		mainPanel.revalidate();

	}
	
	public static int newDialog (Component frame) {
		/**
		 * This method pop ups a dialog message that prompts
		 * the user to pick an option for calculation
		 */
		
		//Custom button text
		Object[] options = {"Different Versions",
		                    "Different Software",
		                    "Cancel"};
		int n = JOptionPane.showOptionDialog(frame,
		    "Are these files from different versions of the same software\n"
		    + "or from different software applications?",
		    "Make a decision",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[2]);
		return n;
	}
	
	public String findLastLine (File f) {
		/**
		 * This method returns the last line of the .csv file,
		 * which is the only line we need.
		 */
		
		String file = f.getAbsolutePath();
		String input = null; 
		Scanner sc = null;
		
		try {
			sc = new Scanner(new File(file), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

		// check if the next line exists
		while (sc.hasNextLine()) {
			// if exists make it a String
			input = sc.nextLine();
		}
		
		return input;
	}
	
	public char findSepChar (String input) {
		
		/**
		 *  This method gets the separate character
		 */
		char a = 0;
		int indexTab = input.lastIndexOf('\t');
		if (input.indexOf (',') != -1) a = ',';
		else if (input.indexOf (';') != -1) a = ';';
		
		// TABs probably exist at the beginning, so we start searching it from the end
		else if (indexTab != -1 && ((Character.isDigit(input.charAt(indexTab - 1))) || (Character.isLetter(input.charAt(indexTab - 1))))) 
			a = '\t';
		else {
			Component dialog = null;
			JOptionPane.showMessageDialog(dialog, "Ups! Something was wrong.\n"
					+ "It is not a valid .csv file!.", "Error",	JOptionPane.ERROR_MESSAGE);
		}
		
		return a;
	}
	
	public double [] findTotalResults (String lastLine, char sepChar) {
		/**
		 * This method converts the values of
		 * the last line of the .csv file 
		 * for the QMOOD metrics,
		 * into an array of doubles.
		 */
		
		double [] totalResults = new double [11];
		
		int a;
		
		// cut the words at the beginning
		for (int i = 0; i < 2; i++) {
			lastLine = lastLine.substring (lastLine.indexOf(sepChar) + 1);
		}
		
		for (int i = 0; i < 22; i++) {
			a = lastLine.indexOf(sepChar);
			
			// check if this .csv file is made by the current version of SQMetrics
			if (a == -1) {
				Component dialog = null;
				JOptionPane.showMessageDialog(dialog, "Ups! Something was wrong.\n"
						+ "It is not a valid .csv file!.", "Error",	JOptionPane.ERROR_MESSAGE);
				helpFrame.dispose ();
				MainWindow.newCompareVersionsPage ();
				CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
				cardLayout.show (MainWindow.pages, "Welcome Page");
				break;
			}
			
			// get the metric value as String
			String value = lastLine.substring(0, a);
			if (i >= 11) {
				// Make it double and add it to the array
				totalResults [i - 11] = Double.parseDouble(value);
			}
			
			// cut the value from the last line 
			lastLine = lastLine.substring(a + 1);
		}
		
		return totalResults;
		
	}
	
	public double [] [] makeFinalArray () {
		/**
		 * This method makes an array which contains
		 * all the total values, for QMOOD metrics, 
		 * of all the .csv files.
		 */
		
		int rows = files.size();
		double [] [] finalArray = new double [rows] [11];
		
		int i = 0;
		for (File file : files) {
			String lastLine = findLastLine (file);
			char sepChar = findSepChar (lastLine);
			double [] totalResults = findTotalResults (lastLine, sepChar);
			finalArray [i] = totalResults;
			i++;
		}
		
		return finalArray;
	}
	
	public void normDifferVers () {
		/**
		 * This method normalizes the values of all the versions
		 * of the software, using as base the values of the 1st version.
		 */
		
		normalValues = new double [files.size()] [11];
		
		for (int row = 0; row < files.size (); row++) {
			for (int col = 0; col < 11; col++) {
				
				// Avoid division by zero
				if (totalValues [0] [col] == 0) {
					totalValues [0] [col] = 0.001;
					if (row > 0) {
						totalValues [row] [col] += 0.001; // the difference is negligible						
					}
				}
				
				// for normalization we divide every value with the value of the 1st version
				normalValues [row] [col] = totalValues [row] [col] / totalValues [0] [col];
				
				// This trick gives at most 2 decimal digits and it's locale independent
				normalValues [row] [col] = Math.round (normalValues [row] [col] * 100.0) / 100.0;
			}
		}

	}
	
	public static void makeCharacts () {
		/**
		 * This method calculates the characteristics from the normalized values
		 */
		
		
		characteristics = new double [files.size()] [7];
		for (int row = 0; row < files.size(); row++) {
			
			double dsc = normalValues [row] [0];
			double noh = normalValues [row] [1];
			double ana = normalValues [row] [2];
			double dam = normalValues [row] [3];
			double dcc = normalValues [row] [4];
			double cam = normalValues [row] [5];
			double moa = normalValues [row] [6];
			double mfa = normalValues [row] [7];
			double nop = normalValues [row] [8];
			double cis = normalValues [row] [9];
			double nom = normalValues [row] [10];
			
			// calculate reusability
			double reuse = (- 0.25) * dcc + 0.25 * cam + 0.5 * cis + 0.5 * dsc;
			reuse = Math.round (reuse * 100.0) / 100.0;
			
			// calculate flexibility
			double flex = 0.25 * dam - 0.25 * dcc + 0.5 * moa + 0.5 * nop;
			flex = Math.round (flex * 100.0) / 100.0;
			
			// calculate Understandability
			double under = (- 0.33) * ana + 0.33 * dam - 0.33 * dcc + 0.33 * cam - 0.33 * nop - 0.33 * nom - 0.33 * dsc;
			under = Math.round (under * 100.0) / 100.0;
			
			// calculate functionality
			double func = 0.12 * cam + 0.22 * nop + 0.22 * cis + 0.22 * dsc + 0.22 * noh;
			func = Math.round (func * 100.0) / 100.0;
			
			// calculate extendibility
			double exte = 0.5 * ana - 0.5 * dcc + 0.5 * mfa + 0.5 * nop;
			exte = Math.round (exte * 100.0) / 100.0;
			
			// calculate effectiveness
			double effe = 0.2 * ana + 0.2 * dam + 0.2 * moa + 0.2 * mfa + 0.2 * nop;
			effe = Math.round (effe * 100.0) / 100.0;
			
			// put the values into the cells
			characteristics [row] [0] = reuse;
			characteristics [row] [1] = flex;
			characteristics [row] [2] = under;
			characteristics [row] [3] = func;
			characteristics [row] [4] = exte;
			characteristics [row] [5] = effe;
			characteristics [row] [6] = reuse + flex + under + func + exte + effe;
		}
	}
	
	public static void makeResults () {
		/**
		 * This method constructs a 2 dimensions array of objects
		 * containing the normalized values of the characteristics 
		 * and the name of each .csv file.
		 */
		
		results = new Object [files.size()] [8];
		
		int row = 0;
		for (File f : files) {
			// make the first cell containing the file name
			String name = GenericFile.removeExtension(f.getName());

			// keep only the name of the project
			if (name.contains(" (by SQMetrics v")){
				int a = name.indexOf(" (by SQMetrics v");
				name = name.substring(0, a);
			}

			results [row] [0] = name;
			// make the rest cells of each row, containing the characteristics' values
			for (int col = 1; col < 8; col++) {
				results [row] [col] = characteristics [row] [col - 1];;
			}
			row++;
		}
		
	}

	@SuppressWarnings("serial")
	public static void makeResultsTable () {

		/**
		 * This method constructs the table that contains the results
		 * for different version of the same software application
		 */


		resultsTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
				Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
				//Object value = getModel().getValueAt(rowIndex, columnIndex);
				if (columnIndex == 0) {
					componenet.setBackground(Color.BLUE);
					componenet.setForeground(Color.WHITE);
					componenet.setFont(new Font("Tahoma", 3, 12));
				}
				
				else if (columnIndex == 7) {
					componenet.setBackground(Color.ORANGE);
					componenet.setForeground(Color.BLACK);
					componenet.setFont(new Font("Tahoma", 3, 12));
				}

				else {
					componenet.setBackground(Color.WHITE);
					componenet.setForeground(Color.BLACK);
					componenet.setFont(new Font("Tahoma", 0, 12));
				}
				return componenet;
			}
		};
		resultsTable.getTableHeader().setReorderingAllowed(false);
		resultsTable.getTableHeader().setBackground(Color.ORANGE);
		resultsTable.getTableHeader().setFont(new Font("Tahoma", 1, 14));
		//resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// make a table to display the results
		resultsTable.setModel (new DefaultTableModel(results, header1) {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {String.class, 
					Double.class, Double.class, Double.class,
					Double.class, Double.class, Double.class, Double.class};

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass (int columnIndex) {
				return types [columnIndex];
			}
		});

	}
	
	public static void normDifferSoft () {
		/**
		 * This method normalizes the values of all the different software
		 *  applications, using as base the highest values.
		 */
		
		normalValuesSoft = new int [files.size()] [11];
		
		
		double max;
		
		// make a set with the values of the specific metric (col)
		Set <Double> set = new HashSet<>();

		for (int col = 0; col < 11; col++) {
			for (int row = 0; row < files.size (); row++) {
				set.add(totalValues [row] [col] );
			}
			
			int x = files.size ();
			int y = set.size();
			// check which metric values equal to the max
			for (int i = 0; i < y; i++) {

				// get the maximum value of the metric
				max = Collections.max(set);

				// this counter counts how many values are equal to max
				int counter = 0;
				for (int a = 0; a < files.size(); a++) {

					if (totalValues [a] [col] == max) {
						normalValuesSoft [a] [col] = x;
						counter ++;
					}
				}
				x -= counter;

				// remove the max and find the next max
				set.remove(max);
			}
		}
	}
	
	public static void makeResultsSoft () {
		/**
		 * This method constructs a 2 dimensions array of objects
		 * containing the normalized values of the metrics for different 
		 * software applications and the name of each .csv file.
		 */
		
		resultsValuesSoft = new Object [files.size()] [12];
		
		int row = 0;
		for (File f : files) {
			// make the first cell containing the file name
			String name = GenericFile.removeExtension(f.getName());
			
			// keep only the name of the project
			if (name.contains(" (by SQMetrics v")){
				int a = name.indexOf(" (by SQMetrics v");
				name = name.substring(0, a);
			}
			resultsValuesSoft [row] [0] = name;
			// make the rest cells of each row, containing the characteristics' values
			for (int col = 1; col < 12; col++) {
				resultsValuesSoft [row] [col] = normalValuesSoft [row] [col - 1];;
			}
			row++;
		}
		
	}
	
	@SuppressWarnings("serial")
	public static void makeTable2 () {

		/**
		 * This method constructs the table that contains the results
		 * for different software applications
		 */

		resultsTable2 = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
				Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
				//Object value = getModel().getValueAt(rowIndex, columnIndex);
				if (columnIndex == 0) {
					componenet.setBackground(Color.BLUE);
					componenet.setForeground(Color.WHITE);
					componenet.setFont(new Font("Tahoma", 3, 12));
				}

				else {
					componenet.setBackground(Color.WHITE);
					componenet.setForeground(Color.BLACK);
					componenet.setFont(new Font("Tahoma", 0, 12));
				}
				return componenet;
			}
		};
		resultsTable2.getTableHeader().setReorderingAllowed(false);
		resultsTable2.getTableHeader().setBackground(Color.ORANGE);
		resultsTable2.getTableHeader().setFont(new Font("Tahoma", 1, 14));
		//resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// make a table to display the results
		resultsTable2.setModel (new DefaultTableModel(resultsValuesSoft, header2) {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {String.class, 
					Integer.class, Integer.class, Integer.class, Integer.class
					, Integer.class, Integer.class, Integer.class, Integer.class
					, Integer.class, Integer.class, Integer.class};

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass (int columnIndex) {
				return types [columnIndex];
			}
		});
	}
}

