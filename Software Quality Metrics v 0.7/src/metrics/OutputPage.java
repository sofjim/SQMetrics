package metrics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
//import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class OutputPage extends JPanel {
	
	private static final long serialVersionUID = -4348409265818336437L;
	static Object [] [] results;
	private JLabel applicationLabel;
	private static JPanel mainPanel;
	private JPanel buttonPanel;
	private static String [] header;
	private static JTable resultsTable;
	private static JScrollPane resultsScrollPane;
	private static JScrollPane resultsScrollPane2;
	private JButton backButton;
	private JButton exportButton;
	private JButton exitButton;
	private JButton mainButton;

	//private JFileChooser chooser = null;
	private static JComboBox<String> chooseSep;
	
	// Constructor
	public OutputPage () {
		
		initComponents ();
	}
	
	public OutputPage (LayoutManager layout) {

		super (layout);
		initComponents ();
	}

	private void initComponents() {
		
		/**
    	 * This method is called from within the constructor to initialize the page.
    	 */
    	
    	// initialize variables
		applicationLabel = new JLabel ("Software Quality Metrics Application", SwingConstants.CENTER);
		applicationLabel.setFont(new Font("Tahoma", 1, 32));
		mainPanel = new JPanel (new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout(10, 10));
		
		new GridBagConstraints();
		buttonPanel = new JPanel (new GridLayout(1, 1, 50, 0));
    	
		header = new String [] {"Package", "Class", "PLOC", "LLOC", "LC", "DIT", "NOC", "CBO", "WMC", "LCOM 1", "LCOM 2", 
    			"LCOM 3", "RFC", "DSC", "NOH", "ANA", "DAM", "DCC", "CAM", "MOA", "MFA", "NOP", "CIS", "NOM",
    			"REUSABILITY", "FLEXIBILITY", "UNDERSTANDABILITY", "FUNCTIONALITY", "EXTENDIBILITY", "EFFECTIVENESS"};
    	
		// Make the buttons
        mainButton = new JButton ("Main Menu");
        backButton = new JButton ("Back");
        exportButton =  new JButton ("Export");
        exitButton = new JButton ("Exit");
        
        // make the separator character chooser
        chooseSep = new JComboBox<String> ();
        chooseSep.setFont(new Font("Tahoma", 1, 12));
        chooseSep.addItem("Separate char = ;");
        chooseSep.addItem("Separate char = ,");
        chooseSep.addItem("Separate char = TAB");
        chooseSep.setToolTipText("Chose the separate character for the .csv file");
        
        mainPanel.setBackground(Color.LIGHT_GRAY);
        
        // Add the buttons
        //buttonPanel.setBackground(Color.YELLOW);
        buttonPanel.add(mainButton);
        buttonPanel.add(backButton);
        buttonPanel.add(chooseSep);
		buttonPanel.add(exportButton);
		buttonPanel.add(exitButton);
		
		// make the page
        add(applicationLabel, BorderLayout.PAGE_START);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
        
        mainButton.setFont(new Font("Tahoma", 1, 12));
        mainButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		try {
        			mainButtonActionPerformed(evt);
        		} catch (URISyntaxException e) {
        			e.printStackTrace();
        		}
        	}
        });
        
        backButton.setFont(new Font("Tahoma", 1, 12));
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
		
		exportButton.setFont(new Font("Tahoma", 1, 12));
		exportButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(ActionEvent evt) {
                try {
					exportButtonActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		exitButton.setFont(new Font("Tahoma", 1, 12));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				exitButtonActionPerformed(evt);
			}
		});
	}
	
	
	
	@SuppressWarnings("serial")
	public static void makeResultsTable () {
		
		/**
		 * This method constructs the table that contains the results
		 */
		
		results = initResults ();
		
		int numOfRows = results.length;
		
		resultsTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
				Component component = super.prepareRenderer(renderer, rowIndex, columnIndex);
				//Object value = getModel().getValueAt(rowIndex, columnIndex);
				if (columnIndex == 0 && !(rowIndex == numOfRows - 1)) {
					component.setBackground(Color.CYAN);
					component.setForeground(Color.BLACK);
					component.setFont(new Font("Tahoma", 3, 12));
				}
				else if (columnIndex == 1 && !(rowIndex == numOfRows - 1)) {
					component.setBackground(Color.ORANGE);
					component.setForeground(Color.BLACK);
					component.setFont(new Font("Tahoma", 3, 12));
				}
				else if (rowIndex == numOfRows - 1) {
					component.setBackground(Color.BLUE);
					component.setForeground(Color.WHITE);
					component.setFont(new Font("Tahoma", 1, 12));
				}
				
				else {
					component.setBackground(Color.WHITE);
					component.setForeground(Color.BLACK);
					component.setFont(new Font("Tahoma", 0, 12));
				}
				
				// Adjust the width of the columns to fit contents
				int rendererWidth = component.getPreferredSize().width;
		           TableColumn tableColumn = getColumnModel().getColumn(columnIndex);
		           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));

		           return component;
			}
		};
    	resultsTable.getTableHeader().setReorderingAllowed(false);
    	resultsTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
    	resultsTable.getTableHeader().setFont(new Font("Tahoma", 1, 14));
    	resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	
		// make a table to display the results
        resultsTable.setModel (new DefaultTableModel(results, header) {
            @SuppressWarnings("rawtypes")
			Class[] types = new Class [] {String.class, String.class, Integer.class,
					Integer.class, Integer.class, Integer.class, Integer.class,
					Integer.class, Integer.class, Integer.class, Double.class, 
					Double.class, Integer.class, Integer.class, Integer.class, 
					Double.class, Double.class, Integer.class, Double.class, 
					Integer.class, Double.class, Integer.class, Integer.class, 
					Integer.class, Double.class, Double.class, Double.class, 
					Double.class, Double.class, Double.class};

            @Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass (int columnIndex) {
                return types [columnIndex];
            }
        });
        
        int x = ClassObj.classes.size();
        resultsScrollPane = new JScrollPane();
        
        // get the dimensions of the main panel (without borders)
        mainPanel.revalidate ();
        Dimension size = mainPanel.getSize();
        
        // specify the height of the results' panel
        int height = 31 + (x + 2) * 16; // 24 for the header and 16 for each line and bar
        int h = size.height;
        int w = size.width;
        
        // Do not allow to the results panel, to be more than the half of the screen
        if (height > h/2) {
        	height = h/2;
        }
        
        resultsScrollPane.setPreferredSize(new Dimension(w - 50, height));
        resultsScrollPane.setMaximumSize(new Dimension(w - 50, h/2));
        resultsScrollPane.setViewportView(resultsTable);
        resultsScrollPane.revalidate ();
        
        mainPanel.add(resultsScrollPane, BorderLayout.PAGE_START);
        
        JTextArea jTextArea2 = new JTextArea();
        //jTextArea2.setBackground(Color.orange);
		jTextArea2.setFont(new Font("Tahoma", 0, 12));
		//jTextArea2.setLineWrap(true);
		//jTextArea2.setWrapStyleWord(true);
		jTextArea2.setCaretPosition(0);
		
		resultsScrollPane2 = new JScrollPane();
		resultsScrollPane2.setPreferredSize(new Dimension(1200, 10 + h/2 - 50));
		resultsScrollPane2.setMaximumSize(new Dimension(w, h/2 - 50));
		resultsScrollPane2.setOpaque(false);
		resultsScrollPane2.getViewport().setOpaque(false);
		resultsScrollPane2.setBorder((TitledBorder) BorderFactory.createTitledBorder("Detailed Report"));
		
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new FileReader(InputPage.fullFileName));
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
        
        
		resultsScrollPane2.setViewportView(jTextArea2);
        resultsScrollPane2.setViewportView(jTextArea2);
        resultsScrollPane2.revalidate ();
        
        mainPanel.add(resultsScrollPane2, BorderLayout.PAGE_END);
        
	}

	private static Object [] [] initResults () {
    	/**
    	 * This method returns a 2 dimensions Object array, witch contains the results
    	 */
    	
    	ArrayList <ClassResults> cr = new ArrayList <> (ClassResults.results);
    	int rows = cr.size() + 1;
    	int cols = header.length;
    	results = new Object [rows] [cols] ;
    	
    	int i = 0;
    	for (ClassResults cl : cr) {
    		
    		results [i] [0] = cl.getPack();
    		results [i] [1] = cl.getName();
    		results [i] [2] = cl.getPloc();	
    		results [i] [3] = cl.getLloc();
    		results [i] [4] = cl.getLc();
    		results [i] [5] = cl.getDit();
    		results [i] [6] = cl.getNoc();
    		results [i] [7] = cl.getCbo();
    		results [i] [8] = cl.getWmc();
    		results [i] [9] = cl.getLcom1();
    		results [i] [10] = cl.getLcom2();
    		results [i] [11] = cl.getLcom3();
    		results [i] [12] = cl.getRfc();
    		results [i] [13] = cl.getDsc();
    		results [i] [14] = cl.getNoh();
    		results [i] [15] = cl.getAna();
    		results [i] [16] = cl.getDam();
    		results [i] [17] = cl.getDcc();
    		results [i] [18] = cl.getCam();
    		results [i] [19] = cl.getMoa();
    		results [i] [20] = cl.getMfa();
    		results [i] [21] = cl.getNop();
    		results [i] [22] = cl.getCis();
    		results [i] [23] = cl.getNom();
    		results [i] [24] = cl.getReusability();
    		results [i] [25] = cl.getFlexibility();
    		results [i] [26] = cl.getUnderstandability();
    		results [i] [27] = cl.getFunctionality();
    		results [i] [28] = cl.getExtendibility();
    		results [i] [29] = cl.getEffectiveness();
    		
    		i++; 
    	}
    	
    	results [i] [0] = "PROJECT'S";
    	results [i] [1] = "SUM OR AVG";
    	
    	// check which columns must be Sum and which must be Average
    	int [] intSet = new int [] {2, 3, 4, 21, 22, 23};
    	
    	for (int col = 2; col < 30; col++) {
    		
    		boolean contains = false;
    	
    		for (int i1 = 0; i1 < 6; i1++) {
    			if (col == intSet [i1]) contains = true;
    		}
    		
    		if (contains == true) {
    			results [i] [col] = sum (i, col);
    		}
    		
    		else {
    			double a = avg (i, col);
    			int b = (int) avg (i, col);
    			if (col == 13 || col == 14) {
    				results [i] [col] = b;
    			}
    			else {
    					results [i] [col] = a;
    			}
    		}
    	}
 		return results;
	}
	
	private static int sum (int a, int col) {
		
		int sum = 0;
		for (int i = 0; i < a; i++) {
			sum = sum + (int) results [i][col];
		}
		return sum;
	}
	
	private static double avg (int a, int col) {
		
		double avg = 0;
		double sum = 0;
		for (int i = 0; i < a; i++) {
			
			double d = new Double(results [i][col].toString());
			
			sum = sum + d;
		}
		avg = sum/a;
		
		avg = Math.round (avg * 100.0) / 100.0;
				
		return avg;
	}
	
	private char getSepChar () {
		
		
		char sepChar = 0;
		
		String a = (String) chooseSep.getSelectedItem();
		
		switch (a) {
		case "Separate char = ;":
			sepChar = ';';
			break;
		case "Separate char = ,":
			sepChar = ',';
			break;
		case "Separate char = TAB":
			sepChar = '\t';
			break;
		
		}
		return sepChar;
		
		
		
	}
	
    public void export (String fullPath) throws IOException {
    	
    	/** 
    	 * This method creates a String array from the results and exports it into a .csv file
    	 */
    	
    	// At first create the array
    	String [] [] array = new String [results.length + 1] [results [0].length];
    	
    	//Make the header
    	array [0] = header;
    	
    	// Make the rest of the rows
    	for (int row = 0; row < results.length; row++) {
    		for (int col = 0; col < results [0].length; col++) {
    			array [row+1][col] = results[row] [col].toString();
    		}
    		
    	}
    	
    	char sepChar = getSepChar ();

    	BufferedWriter br = new BufferedWriter(new FileWriter(fullPath));
    	StringBuilder sb = new StringBuilder();

    	// Append strings from array
    	for (int row = 0; row < array.length; row++) {
    		for (int col = 0; col < array [0].length; col++) {
				sb.append(array[row][col]);

				sb.append(sepChar);

			}
    	sb.append("\n"); // at the end of each line add a new line
    	}
    	
    	br.write(sb.toString());
    	br.close();
    	    	    	
    }
    
    private void mainButtonActionPerformed(ActionEvent evt) throws URISyntaxException {
    	/**
    	 * This method is called when the "Main Menu" button is pressed.
    	 * It clears all the variables, makes new pages and
    	 * displays the Welcome Page.
    	 */
    	
    	results = null;
    	ClassResults.results.clear ();
    	resultsTable = null;
    	
    	MainWindow.newInputPage ();
    	MainWindow.newOutputPage ();
		CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		cardLayout.show (MainWindow.pages, "Welcome Page");
		
	}
    
    private void backButtonActionPerformed(ActionEvent evt) throws URISyntaxException {
    	/**
    	 * This method is called when the "Back" button is pressed.
    	 * It clears all the variables, makes new pages and
    	 * displays the Input Page.
    	 */
    	
    	results = null;
    	ClassResults.results.clear ();
    	resultsTable = null;
    	
    	MainWindow.newInputPage ();
    	MainWindow.newOutputPage ();
		CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		cardLayout.show (MainWindow.pages, "Input Page");
		
	}
    
    private void exportButtonActionPerformed(ActionEvent evt) throws IOException {                                           
    	/**
    	 * This method is called when the "Export" button is pressed.
    	 * It allows the user to choose where to save the .csv file
    	 * which contains the results.
    	 */
    	String file;

    	//String osName = System.getProperty("os.name");
    	//String homeDir = System.getProperty("user.home");

    	// in case the OS is Mac OS X
//    	if (osName.equals("Mac OS X")) {
    		JFrame appleFrame = new JFrame();
    		appleFrame.setIconImage(MainWindow.img.getImage());
    		System.setProperty("apple.awt.fileDialogForFiles", "true");
    		FileDialog fd = new FileDialog (appleFrame, "Save as .csv file", FileDialog.SAVE);
    		fd.setIconImage(MainWindow.img.getImage());
    		fd.setDirectory(InputPage.getPath());
    		String date =  InputPage.dateTime("short");
    		String exportName = InputPage.projectName + " (by " + MainWindow.getVersion() + " on " + date +  ").csv";
    		fd.setFile(exportName);
    		fd.setVisible(true);
    		
    		if (fd.getFile() == null) {
    			//System.out.println("You cancelled the choice");
    			return;
    		} 
    		else {
    			String f = new File (fd.getFile()).getName();
    		
    		file = fd.getDirectory() + f;
    		
//    		Component showWindow = null;
//    		JOptionPane.showMessageDialog(showWindow, "Your file is: " + file, "info",
//					JOptionPane.INFORMATION_MESSAGE);
    		
    			/*
    			 *  if the user does not add the ".csv" extension or adds a wrong one,
    			 *  then the following lines will correct it.
    			 */

    			String ext = GenericFile.getFileExtension(file);
    			if (ext == null) {
    				file = file + ".csv";
    			}
    			else if (!(ext.equals(".csv"))) {
    				file = GenericFile.removeExtension(file) + ".csv";

    			}
    			export (file);

    		}
//    	}
//
//    	else {// in case the OS is Windows or Linux
//
//    		chooser = new JFileChooser (); 
//    		chooser.setCurrentDirectory (new File("."));
//    		chooser.setDialogTitle ("Save as .csv file");
//    		chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
//    		chooser.setAcceptAllFileFilterUsed (false); // disable the "All files" option.
//
//    		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { 
//    			file = chooser.getSelectedFile().getAbsolutePath();
//
//    			/*
//    			 *  if the user does not add the ".csv" extension or adds a wrong one,
//    			 *  then the following lines will correct it.
//    			 */
//
//    			String ext = GenericFile.getFileExtension(file);
//    			if (ext == null) {
//    				file = file + ".csv";
//    			}
//    			else if (!(ext.equals(".csv"))) {
//    				file = GenericFile.removeExtension(file) + ".csv";
//
//    			}
//    			export (file);
//    		}
//    	}
    }
    
    private void exitButtonActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
    
   
}
