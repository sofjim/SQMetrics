package metrics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class WelcomePage extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7113556768378244915L;
	private JLabel applicationLabel;
	//private JLabel authorLabel;
	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;
	private JPanel mainPanel = new JPanel ();
	private JPanel leftPanel = new JPanel ();
	private JPanel buttonPanel = new JPanel ();
	private JButton newMeasurementButton;
	private JButton compareButton;
	private JButton helpButton;
	private JButton exitButton;
	private JButton aboutButton;
	private JLabel picLabel;
	
	
	

	public WelcomePage() {
		
		initComponents();
		
		

	}

	public WelcomePage(LayoutManager layout) {
		super(layout);
		initComponents();
	}

	public WelcomePage(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initComponents();
	}

	public WelcomePage(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initComponents();
	}
	
	private void initComponents() {
		
		
		// create separate panels
		mainPanel.setLayout(new BorderLayout ());
		//mainPanel.setBackground (Color.LIGHT_GRAY);
		leftPanel.setLayout(new GridBagLayout ());
		GridBagConstraints c = new GridBagConstraints();
		//leftPanel.setBackground (Color.CYAN);
		buttonPanel.setLayout(new GridLayout (1, 1, 50, 0));
		//buttonPanel.setBackground (Color.GREEN);
		
		//rightPanel.setBackground (Color.ORANGE);
		
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("Images" + File.separator + "Screen_picture.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		picLabel = new JLabel(new ImageIcon(myPicture));
		
		// create the components to add to panels
		applicationLabel = new JLabel("Software Quality Metrics Application", SwingConstants.CENTER);
		applicationLabel.setFont(new Font("Tahoma", 1, 32));
		
		//authorLabel = new JLabel("Sofronas Dimitrios");
		//authorLabel.setFont(new Font("Verdana", 2, 24));
		
		jTextArea1 = new JTextArea();
        jScrollPane1 = new JScrollPane();
        jTextArea1.setEditable(false);
        jTextArea1.setOpaque(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new Font("Tahoma", 1, 12));
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setRows(5);
        jTextArea1.setMargin( new Insets(10,20,10,20) );
        jTextArea1.setText("Welcome!\n\n    This is a simple application for measuring java code, using the most common metrics.\n\n  "
        		+ "  If you want to measure the code of a Java application, you must choose a directory, that contains the *.java and *.class files. \n\n  "
        		+ "  If they exist at different folders, choose their parent directory.\n\n    If you want to compare software, you must choose at least 2 .csv"
        		+ " files, made by this application (SQMetrics).\n\n"
        		+ "   Please, read \"Help\" for requirements and details.\n\n    To continue make a choise.");
        
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        
        newMeasurementButton = new JButton ("Measure a Java Application");
        newMeasurementButton.setFont(new Font("Tahoma", 1, 12));
        compareButton = new JButton ("Compare Software");
        compareButton.setFont(new Font("Tahoma", 1, 12));
        helpButton = new JButton ("Help");
        helpButton.setFont(new Font("Tahoma", 1, 12));
        exitButton = new JButton ("Exit");
        exitButton.setFont(new Font("Tahoma", 1, 12));
        aboutButton = new JButton ("About");
        aboutButton.setFont(new Font("Tahoma", 1, 12));
       
        // design left panel
        c.ipady = 10;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.PAGE_START;
        //leftPanel.add(authorLabel, c);
        
        c.ipady = 300;
        c.gridx = 0;
        c.gridy = 1;
        leftPanel.add(jScrollPane1, c);
        
        // add buttons
        buttonPanel.add(aboutButton);
        buttonPanel.add(helpButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(newMeasurementButton);
        buttonPanel.add(exitButton);
        
        add (applicationLabel, BorderLayout.PAGE_START);
        add (leftPanel, BorderLayout.LINE_START);
        add (buttonPanel, BorderLayout.PAGE_END);
        add (picLabel, BorderLayout.LINE_END);
        
        aboutButton.addActionListener(new ActionListener() {
        	
        	@Override
        	public void actionPerformed(ActionEvent evt) {
        		aboutActionPerformed(evt);
        	}
        });
        
        helpButton.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent evt) {
        		helpButtonActionPerformed(evt);
        	}
        });
       
        newMeasurementButton.addActionListener(new ActionListener() {
			 
			 @Override
			 public void actionPerformed(ActionEvent evt) {
				 newMeasurementButtonActionPerformed(evt);
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
        		exitActionPerformed(evt);
        	}
        });
	}
	
	private void aboutActionPerformed(ActionEvent evt) {                                           
		Component paintingChild = null;
		JOptionPane.showMessageDialog(paintingChild, 
				MainWindow.getVersion() + "\n" + 
		"2019 - 2020" + "\n" +
		"Author: Dimitrios M. Sofronas" +
		 "\n" + "Hellenic Open University"
		 		+ "\n" + "email: sofjim65@gmail.com");
	 }
	
	private void helpButtonActionPerformed(ActionEvent evt) {
    	/**
    	 * This method is called when the "Help" button is pressed.
    	 * It displays, in a new window, the "help.txt" file.
    	 */
    	
        JFrame helpFrame = new JFrame();
        ImageIcon img = new ImageIcon("icon.jpg");
        helpFrame.setIconImage(img.getImage());
        helpFrame.setSize(600, 600);
        helpFrame.setLocationRelativeTo(null);
		
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
			buff = new BufferedReader(new FileReader("Docs" + File.separator + "Welcome.txt"));
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
	
	public void compareButtonActionPerformed(ActionEvent evt) {
		 
		 CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		 cardLayout.show(MainWindow.pages, "Compare Page");

			Component dialog = null;
			
			JOptionPane.showMessageDialog(dialog, "***ATTENTION*** \n"
					+ "Make sure that the .csv files you choose have been created,\n"
					+ " with the same options (same metrics etc),\n"
					+ "otherwise you 'll get faulty results.\n\n"
					+ "If you want to measure the code from different versions,\n"
					+ "of the same software, you must select the oldest .csv file, first",
					"Warning",	JOptionPane.WARNING_MESSAGE);
	 }
	
	private void newMeasurementButtonActionPerformed(ActionEvent evt) {
		 
		 CardLayout cardLayout = (CardLayout) MainWindow.pages.getLayout();
		 cardLayout.show(MainWindow.pages, "Input Page");
		 
	 }

	private void exitActionPerformed(ActionEvent evt) {                                           
		System.exit(0);
	}
		

}
