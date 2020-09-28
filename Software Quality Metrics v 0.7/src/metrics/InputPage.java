package metrics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InputPage extends JPanel {

	private static final long serialVersionUID = -506001365916937849L;
	
	// Variables declaration
	private static int numberOfClasses;
	private static int numberOfMetrics;
	private static int classProgress;
	private static int metricProgress;
	private static String path = null;
	public static String projectName = null;
	public static String reportName;
	public static String fullFileName;
	public static String timeStart;
	public static String timeEnd;
	private JPanel metricsPanel;
	private JPanel buttonPanel;
	private JPanel mainPanel;
	private JPanel barPanel;
	private JPanel pathPanel;
	private JLabel chooseLabel;
	private static JProgressBar classProgressBar;
	private static JProgressBar metricProgressBar;
	private JLabel applicationLabel;
	private JLabel sizeMetricsLabel;
	private JLabel ckMetricsLabel;
	private JLabel qmoodMetricsLabel;
	private JLabel pathLabel;
	private JTextField pathField;
	private JButton helpButton;
	private JButton backButton;
	private JButton exitButton;
	private JButton nextButton;
	private JButton dirButton;
	private JButton optsButton;
	private JCheckBox all;
	private static JCheckBox plocCheck;
	private static JCheckBox llocCheck;
	private static JCheckBox lcCheck;
	private static JCheckBox ditCheck;
	private static JCheckBox nocCheck;
	private static JCheckBox cboCheck;
	private static JCheckBox wmcCheck;
	private static JCheckBox lcom1Check;
	private static JCheckBox lcom2Check;
	private static JCheckBox lcom3Check;
	private static JCheckBox rfcCheck;
	private static JCheckBox dscCheck;
	private static JCheckBox nohCheck;
	private static JCheckBox anaCheck;
	private static JCheckBox damCheck;
	private static JCheckBox dccCheck;
	private static JCheckBox camCheck;
	private static JCheckBox moaCheck;
	private static JCheckBox mfaCheck;
	private static JCheckBox nopCheck;
	private static JCheckBox cisCheck;
	private static JCheckBox nomCheck;
	private JFrame helpFrame;
	protected static long startTime;
	protected static long endTime;
	
	private static Options options;

	// Constructor
	public InputPage() {

		initComponents();

	}
	
	public InputPage(LayoutManager layout) {

		super (layout);
		initComponents();

	}

	private void initComponents() {
		
		/**
    	 * This method is called from within the constructor to initialize the page.
    	 */

		path = null;
		metricsPanel = new JPanel();
		buttonPanel = new JPanel();
		mainPanel = new JPanel();
		barPanel = new JPanel();
		pathPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		helpButton = new JButton ("Help");
		backButton = new JButton();
		optsButton = new JButton();
		nextButton = new JButton();
		exitButton = new JButton();
		dirButton = new JButton("Get Dir");
		chooseLabel = new JLabel();
		applicationLabel = new JLabel("Software Quality Metrics Application", SwingConstants.CENTER);
		sizeMetricsLabel = new JLabel();
		ckMetricsLabel = new JLabel();
		qmoodMetricsLabel = new JLabel();
		pathLabel = new JLabel();
		pathField = new JTextField();
		all = new JCheckBox();
		plocCheck = new JCheckBox();
		llocCheck = new JCheckBox();
		lcCheck = new JCheckBox();
		ditCheck = new JCheckBox();
		nocCheck = new JCheckBox();
		cboCheck = new JCheckBox();
		wmcCheck = new JCheckBox();
		lcom3Check = new JCheckBox();
		lcom2Check = new JCheckBox();
		lcom1Check = new JCheckBox();
		rfcCheck = new JCheckBox();
		dscCheck = new JCheckBox();
		nohCheck = new JCheckBox();
		anaCheck = new JCheckBox();
		damCheck = new JCheckBox();
		dccCheck = new JCheckBox();
		camCheck = new JCheckBox();
		moaCheck = new JCheckBox();
		mfaCheck = new JCheckBox();
		nopCheck = new JCheckBox();
		cisCheck = new JCheckBox();
		nomCheck = new JCheckBox();
		helpFrame = new JFrame();

		classProgressBar = new JProgressBar();
		metricProgressBar = new JProgressBar();
		

		applicationLabel.setFont(new Font("Tahoma", 1, 32));

		chooseLabel.setFont(new Font("Tahoma", 1, 24)); // NOI18N
		chooseLabel.setText("Choose Metrics");

		sizeMetricsLabel.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		sizeMetricsLabel.setText("Size Metrics");

		ckMetricsLabel.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		ckMetricsLabel.setText("C & K Metrics");

		try {
			toolTipsTimeInf();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//set the tooltips' color and fonts
		UIManager.put("ToolTip.background", Color.ORANGE);
		UIManager.put("ToolTip.foreground", Color.BLACK);
		UIManager.put("ToolTip.font", new Font("Arial", Font.ITALIC, 14));

		// Size metrics buttons
		plocCheck.setText("Physical Lines Of Code (PLOC)");
		plocCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Physical Line of Code (PLOC): It counts the \"CR\" or \"ENTER\" characters."
				+ " All the lines are counted, included the comment and the blank lines." + "</p></html>");
		llocCheck.setText("Logical Lines Of Code (LLOC)");
		llocCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Logical Lines of Code (LLOC): All lines that contain code are counted. "
				+ "If a line contains both code and comments it will be counted. Line with 1 character are excluded."
				+ "</p></html>");
		lcCheck.setText("Lines of Coments (LC)");
		lcCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Lines of Comments (LC): Only the lines that contain comments are counted. "
				+ "If a line contains both code and comments it will be counted." + "</p></html>");
		// C & K metrics buttons
		ditCheck.setText("Depth of Inheritance Tree (DIT)");
		ditCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Depth of Inheritance Tree (DIT): It calculates the depth "
				+ "of the inheritance tree of a class, into the project. If the class inherits from an external class out"
				+ " of the project, it’s DIT is \"1\"." + "</p></html>");
		nocCheck.setText("Number Of Children (NOC)");
		nocCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Number of Children (NOC): The NOC of a class is the number "
						+ "of the classes that inherit from this class." + "</p></html>");
		cboCheck.setText("Coupling Between Objects (CBO)");
		cboCheck.setToolTipText("<html><p width=\"400\">" + "Two classes are coupled when one of them uses methods, "
				+ "fields or objects of the other class or if they are parent – child." + "</p></html>");
		wmcCheck.setText("Weight Method per Class (WMC)");
		wmcCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Weight Method per Class (WMC): It counts the sum of the cyclomatic "
						+ "complexity of all the methods of the class." + "</p></html>");
		lcom1Check.setText("Lack of Cohesion Of Methods 1 (LCOM1)");
		lcom1Check.setToolTipText("<html><p width=\"400\">"
				+ "- Lack of Cohesion of Methods 1 (LCOM1): It adds \"1\" for every "
				+ "pair of methods that use at least 1 common class or instance field and abstracts \"1\" for every pair which doesn’t."
				+ " If it is below \"0\" it is \"0\". The closer to \"0\" the better."
				+ " Getters and setters such as constructors are excluded." + "</p></html>");
		lcom3Check.setText("Lack of Cohesion Of Methods 3 (LCOM3)");
		lcom2Check.setToolTipText("<html><p width=\"400\">"
				+ "- Lack of Cohesion of Methods 2 (LCOM2): It is calculated by the following "
				+ "formula: LCOM2 = 1 – sum(nA)/(m * a), where m = number of methods and a = number of fields, nA = number of methods "
				+ "that access the field \"A\". It takes values between \"0\" and \"1\". The closer to \"0\" the better. "
				+ "Getters and setters such as constructors are excluded." + "</p></html>");
		lcom2Check.setText("Lack of Cohesion Of Methods 2 (LCOM2)");
		lcom3Check.setToolTipText("<html><p width=\"400\">"
				+ "- Lack of Cohesion of Methods 3 (LCOM3): It is calculated by the following "
				+ "formula: LCOM3 = (m - sum(nA)/a)/(m - 1)., where m = number of methods and a = number of fields, nA = number of methods "
				+ "that access the field \"A\". It takes values between \"0\" and \"2\". The closer to \"0\" the better. Getters and setters "
				+ "such as constructors are excluded." + "</p></html>");
		rfcCheck.setText("Response For a Class (RFC)");
		rfcCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Response For a Class (RFC): It is the number of methods that can"
						+ " be called to answer a message from an object of another class. "
						+ "It is calculated by the formula: RFC = M + R, where M is the number"
						+ " of the methods of the class and R is the number of methods of other classes, "
						+ "calling directly some method of the class." + "</p></html>");

		// QMood metrics buttons
		qmoodMetricsLabel.setFont(new Font("Tahoma", 1, 14)); // NOI18N
		qmoodMetricsLabel.setText("QMood Metrics");
		dscCheck.setText("Design Size in Classes (DSC)");
		dscCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Design Size in Classes (DSC): It is the number of the classes "
						+ "of the project." + "</p></html>");
		nohCheck.setText("Number of Hierarchies (NOH)");
		nohCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Number Of Hierarchies (NOH): It is the number of the subtrees "
				+ "of the project, under the System.Object class, which means the number of the classes that have at least"
				+ " 1 child and DIT = 1." + "</p></html>");
		anaCheck.setText("Average Number of Ancestors (ANA)");
		anaCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Average Number of Ancestors (ANA): It is the average of the classes"
						+ " from which a class inherits. It is given by the formula ANA = DIT / DSC." + "</p></html>");
		damCheck.setText("Data Access Metric (DAM)");
		damCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Data Access Metric (DAM): It is the division between the number of"
				+ " \"private\" and \"protected\" methods by the total number of methods. The closer to \"1\" the better."
				+ "</p></html>");
		dccCheck.setText("Direct Class Coupling (DCC)");
		dccCheck.setToolTipText("<html><p width=\"400\">"
				+ "-  Direct Class Coupling (DCC): It is the number of the other classes"
				+ " that are directly coupled with this class. A class is directly coupled with another class when it is parent or"
				+ " child of the other class, it has a field with the type of the other class, or its methods return a value "
				+ "or have a parameter with a type of the other class." + "</p></html>");
		camCheck.setText("Cohesion Among Methods (CAM)");
		camCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Cohesion Among Methods (CAM): It is calculated by the formula "
				+ "CAM = (sum Mi) / (n * (T + 1)). \"Mi\" is the number of the distinct parameters of the method \"i\","
				+ " \"sum Mi\" is the sum for all methods of the class, \"n\" is the number of the methods of the class"
				+ " and \"T\" is the number distinct parameter type of all the methods of the class. In \"Mi\", the type "
				+ "of the class itself is included, but in the \"T\" it’s not included." + "</p></html>");
		moaCheck.setText("Measure of Aggregation (MOA)");
		moaCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Measure of Aggregation (MOA): This method returns the number "
				+ "of variables which are declared by the user. This means the variables, that their type is another class "
				+ "of the project. It counts the class's fields, the constructor's variables and the methods' variables."
				+ "</p></html>");
		mfaCheck.setText("Measure of Functional Abstraction (MFA)");
		mfaCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Measure of Functional Abstraction (MFA): It is the \"Measure of Functional"
				+ " Abstraction\" (MFA) metric, which is the division between the inherited methods, by all the methods of the class "
				+ "(inherited and declared)." + "</p></html>");
		nopCheck.setText("Number of Polymorphic Methods (NOP)");
		nopCheck.setToolTipText(
				"<html><p width=\"400\">" + "- Number of Polymorphic Methods (NOP): It is the number of the methods "
						+ "of a class, which may have polymorphic behavior." + "</p></html>");
		cisCheck.setText("Class Interface Size (CIS)");
		cisCheck.setToolTipText("<html><p width=\"400\">" + "- Class Interface Size (CIS): It is the number "
				+ "of the \"public\" methods of the class." + "</p></html>");
		nomCheck.setText("Number Of Methods (NOM)");
		nomCheck.setToolTipText("<html><p width=\"400\">"
				+ "- Number of Methods (NOM): It is the number of the methods that are "
				+ "declared in a class, which means the number of the public, protected, default (package) access, and private"
				+ " methods of the class. Constructors are excluded." + "</p></html>");

		// "Select All" button
		all.setText("All");

		// Set all check boxes to "checked" state
		all.setSelected(true);

		// Size metrics buttons
		getPlocCheck().setSelected(true);
		getLlocCheck().setSelected(true);
		getLcCheck().setSelected(true);

		// C & K metrics buttons
		getDitCheck().setSelected(true);
		getNocCheck().setSelected(true);
		getCboCheck().setSelected(true);
		getWmcCheck().setSelected(true);
		getLcom1Check().setSelected(true);
		getLcom2Check().setSelected(true);
		getLcom3Check().setSelected(true);
		getRfcCheck().setSelected(true);

		// QMood metrics buttons
		getDscCheck().setSelected(true);
		getNohCheck().setSelected(true);
		getAnaCheck().setSelected(true);
		getDamCheck().setSelected(true);
		getDccCheck().setSelected(true);
		getCamCheck().setSelected(true);
		getMoaCheck().setSelected(true);
		getMfaCheck().setSelected(true);
		getNopCheck().setSelected(true);
		getCisCheck().setSelected(true);
		getNomCheck().setSelected(true);

		all.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				allStateChanged(evt);
			}
		});

		// Make the group containing the metrics
		GroupLayout metricPanelLayout = new GroupLayout(metricsPanel);
		metricsPanel.setLayout(metricPanelLayout);

		metricPanelLayout.setHorizontalGroup(metricPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(metricPanelLayout.createSequentialGroup().addGroup(metricPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(metricPanelLayout.createSequentialGroup().addGap(41, 41, 41)
								.addGroup(metricPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)

										.addComponent(sizeMetricsLabel).addComponent(plocCheck).addComponent(llocCheck)
										.addComponent(lcCheck)

										.addComponent(ckMetricsLabel).addComponent(ditCheck).addComponent(nocCheck)
										.addComponent(cboCheck).addComponent(wmcCheck).addComponent(lcom1Check)
										.addComponent(lcom2Check).addComponent(lcom3Check).addComponent(rfcCheck)))

						.addGroup(metricPanelLayout.createSequentialGroup().addGap(441, 441, 441)
								.addGroup(metricPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(qmoodMetricsLabel).addComponent(dscCheck).addComponent(nohCheck)
										.addComponent(anaCheck).addComponent(damCheck).addComponent(dccCheck)
										.addComponent(camCheck).addComponent(moaCheck).addComponent(mfaCheck)
										.addComponent(nopCheck).addComponent(cisCheck).addComponent(nomCheck)))

						.addGroup(metricPanelLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(all)))
						.addContainerGap(44, Short.MAX_VALUE)));

		metricPanelLayout.setVerticalGroup(metricPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING,
						metricPanelLayout.createSequentialGroup().addContainerGap(26, Short.MAX_VALUE)

								.addComponent(all)

								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(sizeMetricsLabel)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(plocCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(llocCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lcCheck)

								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ckMetricsLabel)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ditCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(nocCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(cboCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(wmcCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lcom1Check)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lcom2Check)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lcom3Check)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(rfcCheck))

				.addGroup(GroupLayout.Alignment.TRAILING,
						metricPanelLayout.createSequentialGroup().addContainerGap(26, Short.MAX_VALUE)

								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(qmoodMetricsLabel)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(dscCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(nohCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(anaCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(damCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(dccCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(camCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(moaCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(mfaCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(nopCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(cisCheck)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(nomCheck)
								.addContainerGap()));

		// make the path field
		pathLabel.setFont(new Font("Tahoma", 0, 18));
		pathLabel.setText("Directory: ");

		pathField.setColumns(70);
		pathField.setEditable(false);
		pathField.setFont(new Font("Tahoma", 0, 12));
		pathField.setToolTipText("The directory that will be examined");

		pathPanel.add(pathLabel);
		pathPanel.add(pathField);
		pathPanel.add(dirButton);

		// make the buttons
		dirButton.setFont(new Font("Tahoma", 1, 12));
		dirButton.setText("Get Dir");
		dirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dirButtonActionPerformed(evt);
			}
		});

		// make the progress bars
		barPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		barPanel.setLayout(new BorderLayout(0, 0));
		Dimension d = new Dimension(700, 20);
		classProgressBar.setPreferredSize(d);
		classProgressBar.setStringPainted(true);

		metricProgressBar.setPreferredSize(d);
		metricProgressBar.setStringPainted(true);
		barPanel.add(classProgressBar, BorderLayout.PAGE_START);
		barPanel.add(metricProgressBar, BorderLayout.PAGE_END);
		
		helpButton.setFont(new Font("Tahoma", 1, 12));
		helpButton.setText("Help");
		helpButton.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent evt) {
        		helpButtonActionPerformed(evt);
        	}
        });

		backButton.setFont(new Font("Tahoma", 1, 12));
		backButton.setText("Back");
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
		
		optsButton.setFont(new Font("Tahoma", 1, 12));
		optsButton.setText("Options");
		optsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					optButtonActionPerformed(evt);
				} catch (URISyntaxException e) {
					
					e.printStackTrace();
				}
			}
		});

		nextButton.setFont(new Font("Tahoma", 1, 12));
		nextButton.setText("Next");
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});

		exitButton.setFont(new Font("Tahoma", 1, 12));
		exitButton.setText("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				exitButtonActionPerformed(evt);
			}
		});

//		metricsPanel.setBackground(Color.GREEN);
//		buttonPanel.setBackground(Color.YELLOW);
//		mainPanel.setBackground(Color.CYAN);
//		pathPanel.setBackground(Color.RED);
//		barPanel.setBackground(Color.ORANGE);

		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.5;
		c.anchor = GridBagConstraints.PAGE_START;
		mainPanel.add(metricsPanel, c);

		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.5;
		c.anchor = GridBagConstraints.CENTER;
		mainPanel.add(barPanel, c);

		c.ipady = 30;
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.5;
		c.anchor = GridBagConstraints.PAGE_END;
		mainPanel.add(pathPanel, c);

		buttonPanel.setLayout(new GridLayout(1, 1, 50, 0));
		buttonPanel.add(helpButton);
		buttonPanel.add(backButton);
		buttonPanel.add(optsButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(exitButton);

		add(applicationLabel, BorderLayout.PAGE_START);
		add(mainPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);

	}

	private void allStateChanged(javax.swing.event.ChangeEvent evt) {
		/**
		 * This method checks the state of the "all" checkBox and puts all the other
		 * checkBoxes into the same state.
		 */

		all.setRolloverEnabled(false);

		boolean state = all.isSelected();
		getPlocCheck().setSelected(state);
		getLlocCheck().setSelected(state);
		getLcCheck().setSelected(state);
		getDitCheck().setSelected(state);
		getNocCheck().setSelected(state);
		getCboCheck().setSelected(state);
		getWmcCheck().setSelected(state);
		getLcom1Check().setSelected(state);
		getLcom2Check().setSelected(state);
		getLcom3Check().setSelected(state);
		getRfcCheck().setSelected(state);
		getDscCheck().setSelected(state);
		getNohCheck().setSelected(state);
		getAnaCheck().setSelected(state);
		getDamCheck().setSelected(state);
		getDccCheck().setSelected(state);
		getCamCheck().setSelected(state);
		getMoaCheck().setSelected(state);
		getMfaCheck().setSelected(state);
		getNopCheck().setSelected(state);
		getCisCheck().setSelected(state);
		getNomCheck().setSelected(state);
	}

	public static void toolTipsTimeInf() throws Exception {
		/**
		 * This method is used to set the time so that the tooltips are visible to
		 * infinitive.
		 */

		// Get current delay
		int dismissDelay = ToolTipManager.sharedInstance().getDismissDelay();

		// Keep the tool tip showing
		dismissDelay = Integer.MAX_VALUE;
		ToolTipManager.sharedInstance().setDismissDelay(dismissDelay);

	}

	private void dirButtonActionPerformed(ActionEvent evt) {
		
		/**
		 * This method is called when the "Get Dir" Button is pressed
		 */
		
		String osName = System.getProperty("os.name");
	    //String homeDir = System.getProperty("user.home");
	    
	    if (osName.equals("Mac OS X")) {
	    	File selectedPath = null;
	    	JFrame appleFrame = new JFrame();
	    	appleFrame.setIconImage(MainWindow.img.getImage());
	        System.setProperty("apple.awt.fileDialogForDirectories", "true");
	        FileDialog fd = new FileDialog (appleFrame, "Choose Directory");
	        //fd.setDirectory(homeDir);
	        fd.setVisible(true);
	        String dirName = fd.getDirectory() + fd.getFile();
	        
	        selectedPath = new File (dirName);
	        
			if (selectedPath.isDirectory()) {
				//System.out.println("You chose " + filename);
				setPath(dirName);
				setProjectName(selectedPath.getName());
			}
			else {
				dirName = fd.getDirectory();
				//System.out.println("You chose " + filename);
				setPath(dirName);
				selectedPath = new File (dirName);
				setProjectName(selectedPath.getName());
			}
			pathField.setText(path);
	        System.setProperty("apple.awt.fileDialogForDirectories", "false");
	        
	      } else {
	    	  
	    	  
	    	
	    	JFileChooser chooser = new JFileChooser();
	    	chooser.setCurrentDirectory(new File("."));
	    	chooser.setDialogTitle("choose directory");
	    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    	chooser.setAcceptAllFileFilterUsed(false); // disable the "All files" option.
	    	
	    	if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    		setPath(chooser.getSelectedFile().getAbsolutePath());
	    		setProjectName(chooser.getSelectedFile().getName());
	    		pathField.setText(path);
	    		
	    	}
	    }
		

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
			buff = new BufferedReader(new FileReader("Docs" + File.separator + "Input.txt"));
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
    	
    	ClassResults.results.clear ();
    	helpFrame.dispose ();
    	
    	MainWindow.newInputPage ();
    	MainWindow.newOutputPage ();
		CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		cardLayout.show (MainWindow.pages, "Welcome Page");

	}
	
	private void optButtonActionPerformed(ActionEvent evt) throws URISyntaxException {
		/**
    	 * This method is called when the "Option" button is pressed.
    	 * 
    	 */
    	
		
		
		options = new Options ();
		
	}

	private void nextButtonActionPerformed(ActionEvent evt) {

		Component errorWindow = null;
		helpFrame.dispose ();
		
		if (options == null) {
			options = new Options ("without dialog");
		}

		if (path != null) {
			
			// reset the progress bars
			classProgress = 0;
			metricProgress = 0;
			classProgressBar.setValue(0);
			metricProgressBar.setValue(0);
			
			// make a new report file
			timeStart = dateTime("medium");
			reportName = projectName + "_" + "Report_by_" + MainWindow.getVersion() + " at " + timeStart + ".txt";
			
			writeToFile ("", false);
			writeToFile ("REPORT FOR PROJECT: " + projectName + ", (by " + MainWindow.getVersion() + ") " + timeStart + '\n', true);
			
			// Find files and classes
			GenericFile.enlist(path);
			
			// Set the values for the bars
			numberOfClasses = ClassObj.classes.size();
			numberOfMetrics = getNumberOfMetrics();
			classProgressBar.setMinimum(0);
			classProgressBar.setMaximum(numberOfClasses);
			metricProgressBar.setMinimum(0);
			metricProgressBar.setMaximum(numberOfMetrics);
			
			// get the start time of the process
			startTime = System.currentTimeMillis ();
			
			// execute metrics
			for (Class<?> c : ClassObj.classes) {
				
				// write to the report file
				writeToFile ('\n' + "Class:" + '\t' + c.getSimpleName(), true);
				String pack; 
				if (c.getPackage() == null) {
					pack = "-";
				}
				else pack = c.getPackage().getName();
				
				writeToFile ('\t' + "Package: " + '\t' + pack, true);
				writeToFile ('\t' + "Super Class: " + '\t' + c.getSuperclass(), true);
				
				// execute metrics
				Metrics.executeClassMetrics(c);
			}
			
			// get the end time of the process
			endTime = System.currentTimeMillis ();
			
			// write the process time to the report file
			double duration = (double) (endTime - startTime)/1000;
			
			// get only 2 decimal digits
			duration = Math.round(duration * 100) / 100.0;
			
			String processTime = "\nTotal Process Time: " + duration + " sec";
			writeToFile (processTime, true);

		    // Move to the next screen
    		CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
    		cardLayout.next(MainWindow.pages);
    		
    		OutputPage.makeResultsTable ();
		}
		
		// If no path has been selected, do not advance, but prompt the user to select path.
		else
			JOptionPane.showMessageDialog(errorWindow, "You must choose a directory first.", "Error",
					JOptionPane.ERROR_MESSAGE);
	}

	private void exitButtonActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	public static void updateClassProgress(String className) {

		classProgress++;

		try {

//			classProgressBar.setForeground(Color.blue);
//			classProgressBar.setBackground(Color.white);
			classProgressBar.setString(className);
			classProgressBar.paintImmediately(0, 0, 700, 20);
			classProgressBar.setValue(classProgress);

		} catch (Exception e1) {
			System.out.print("Caughted exception is =" + e1);
		}

	}

	public static void updateMetricProgress(String metricName) {

		metricProgress++;

		try {

//			metricProgressBar.setForeground(Color.red);
//			metricProgressBar.setBackground(Color.white);
			metricProgressBar.setString(metricName);
			metricProgressBar.paintImmediately(0, 0, 700, 20);
			metricProgressBar.setValue(metricProgress);

		} catch (Exception e1) {
			System.out.print("Caughted exception is =" + e1);
		}

	}

	public static void resetMetricProgress() {
		/**
		 * This method is called after a class measuring is finished, so the progress
		 * bar for the metrics starts from the beginning.
		 */
		metricProgress = 0;
	}

	public static int getNumberOfMetrics() {

		/**
		 * This method returns the number of metrics that are going to be executed (in
		 * case that not all have been selected)
		 */

		int numOfMetrics = 0;
		if (getPlocCheck().isSelected())
			numOfMetrics++;
		if (getLlocCheck().isSelected())
			numOfMetrics++;
		if (getLcCheck().isSelected())
			numOfMetrics++;
		if (getDitCheck().isSelected())
			numOfMetrics++;
		if (getNocCheck().isSelected())
			numOfMetrics++;
		if (getCboCheck().isSelected())
			numOfMetrics++;
		if (getWmcCheck().isSelected())
			numOfMetrics++;
		if (getLcom1Check().isSelected())
			numOfMetrics++;
		if (getLcom2Check().isSelected())
			numOfMetrics++;
		if (getLcom3Check().isSelected())
			numOfMetrics++;
		if (getRfcCheck().isSelected())
			numOfMetrics++;
		if (getDscCheck().isSelected())
			numOfMetrics++;
		if (getNohCheck().isSelected())
			numOfMetrics++;
		if (getAnaCheck().isSelected())
			numOfMetrics++;
		if (getDamCheck().isSelected())
			numOfMetrics++;
		if (getDccCheck().isSelected())
			numOfMetrics++;
		if (getCamCheck().isSelected())
			numOfMetrics++;
		if (getMoaCheck().isSelected())
			numOfMetrics++;
		if (getMfaCheck().isSelected())
			numOfMetrics++;
		if (getNopCheck().isSelected())
			numOfMetrics++;
		if (getCisCheck().isSelected())
			numOfMetrics++;
		if (getNomCheck().isSelected())
			numOfMetrics++;

		return numOfMetrics;
	}
	
	public static void writeToFile(String g, boolean b) {
		/**
		 * This method writes to a text file a report for the project
		 */

		try {
			
			// make a directory for the reports files , if it does not exist
			new File(path + File.separator + "SQMetrics Reports").mkdirs();
			
			// create or append the report file
			fullFileName = path + File.separator + "SQMetrics Reports" + File.separator + reportName;
			
			FileWriter fw = new FileWriter(fullFileName, b);
			PrintWriter pw = new PrintWriter(fw);

			pw.println(g);
			
			pw.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}
	
	public static String dateTime (String type) {
		/**
		 * This method returns the current date and time using the US locale, but Greek format,
		 * at 4 possible types (short, medium, long or full). Default type is "medium".
		 */
		String timeStamp = new SimpleDateFormat("dd MMM yyyy - HH.mm", Locale.US).format(new Date());
		
		if (!(type.equals("numeric") && type.equals("short") && type.equals("medium") && type.equals("long") && type.equals("full"))){
			type = "medium";
		}
		
		switch (type) {
		case "numeric":
			timeStamp = new SimpleDateFormat("dd.MM.yy", Locale.US).format(new Date());
			break;
		case "short":
			timeStamp = new SimpleDateFormat("dd MMM yy", Locale.US).format(new Date());
			break;
		case "medium":
			timeStamp = new SimpleDateFormat("dd MMM yyyy - HH.mm", Locale.US).format(new Date());
			break;
		case "long":
			timeStamp = new SimpleDateFormat("dd MMMM yyyy - HH.mm.ss", Locale.US).format(new Date());
			break;
		case "full":
			timeStamp = new SimpleDateFormat("dddd dd MMMM yyyy - HH.mm.ss", Locale.US).format(new Date());
			break;
		}
			
		return timeStamp;
	}

	public static JCheckBox getCboCheck() {
		return cboCheck;
	}

	public static JCheckBox getDitCheck() {
		return ditCheck;
	}

	public static JCheckBox getLcCheck() {
		return lcCheck;
	}

	public static JCheckBox getLcom1Check() {
		return lcom1Check;
	}

	public static JCheckBox getLcom2Check() {
		return lcom2Check;
	}

	public static JCheckBox getLcom3Check() {
		return lcom3Check;
	}

	public static JCheckBox getLlocCheck() {
		return llocCheck;
	}

	public static JCheckBox getNocCheck() {
		return nocCheck;
	}

	public static JCheckBox getPlocCheck() {
		return plocCheck;
	}

	public static JCheckBox getRfcCheck() {
		return rfcCheck;
	}

	public static JCheckBox getWmcCheck() {
		return wmcCheck;
	}

	public static JCheckBox getDscCheck() {
		return dscCheck;
	}

	public static JCheckBox getNohCheck() {
		return nohCheck;
	}

	public static JCheckBox getAnaCheck() {
		return anaCheck;
	}

	public static JCheckBox getDamCheck() {
		return damCheck;
	}

	public static JCheckBox getDccCheck() {
		return dccCheck;
	}

	/**
	 * @param pathField the pathField to set
	 */
	public void setPathField(String path) {
		pathField.setText(path);
	}

	public static JCheckBox getCamCheck() {
		return camCheck;
	}

	public static JCheckBox getMoaCheck() {
		return moaCheck;
	}

	public static JCheckBox getMfaCheck() {
		return mfaCheck;
	}

	public static void setMfaCheck(JCheckBox mfaCheck) {
		InputPage.mfaCheck = mfaCheck;
	}

	public static JCheckBox getNopCheck() {
		return nopCheck;
	}

	public static void setNopCheck(JCheckBox nopCheck) {
		InputPage.nopCheck = nopCheck;
	}

	public static JCheckBox getCisCheck() {
		return cisCheck;
	}

	public static void setCisCheck(JCheckBox cisCheck) {
		InputPage.cisCheck = cisCheck;
	}

	public static JCheckBox getNomCheck() {
		return nomCheck;
	}

	public static void setPath(String path) {
		InputPage.path = path;
	}

	public static String getPath() {
		return path;
	}
	private String setProjectName(String name) {
		projectName = name;
		return null;
	}

	public static Options getOptions() {
		return options;
	}

}
