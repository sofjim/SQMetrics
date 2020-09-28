package metrics;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1473363746265356505L;
	/**
	 * This is the main window of the application.
	 * It is the only JFrame that is used and all the other pages 
	 * are JPanels into a CardLayout.
	 */
	
	private static String version = "SQMetrics v 0.7";
	static MainWindow mainWindow;
	static JPanel pages = new JPanel(new CardLayout());
	static WelcomePage welcomePage;
	static InputPage inputPage;
	static OutputPage outputPage;
	static ComparePage comparePage;
	static ImageIcon img;
	
	

	public MainWindow() throws HeadlessException {
		initComponents();
	}

	public MainWindow(GraphicsConfiguration gc) {
		super(gc);
		initComponents();
	}

	public MainWindow(String title) throws HeadlessException {
		super(title);
		initComponents();
		
	}

	public MainWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
		initComponents();
		
	}
	
	private void initComponents() {
		
		// Apply the "Nimbus" look and feel (if exists)
		try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		// make the frame
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle (version);
		
		// apply the icon
		img = new ImageIcon("Images" + File.separator + "icon.jpg");
		
		// set "welcome" image
		setIconImage(img.getImage());
		
		// make pages
		welcomePage = new WelcomePage (new BorderLayout ());
		inputPage = new InputPage (new BorderLayout ());
		outputPage = new OutputPage (new BorderLayout());
		comparePage = new ComparePage (new BorderLayout ());
		
		
		// add pages
		pages.add(welcomePage, "Welcome Page");
		pages.add(inputPage, "Input Page");
		pages.add(outputPage, "Output Page");
		pages.add(comparePage, "Compare Page");
		
		this.add (pages);
		

		
		setVisible(true);
		
		
	}

	public static String getVersion() {
		return version;
	}
	
	public static void newInputPage () {
		/**
		 * This method is called from other classes
		 * to reset the Input Page
		 */
		pages.remove(inputPage);
		inputPage = new InputPage (new BorderLayout ());
		pages.add(inputPage, "Input Page");
	}
	
	public static void newOutputPage () {
		/**
		 * This method is called from other classes
		 * to reset the Output Page
		 */
		pages.remove(outputPage);
		outputPage = new OutputPage (new BorderLayout ());
		pages.add(outputPage, "Output Page");
	}
	
	public static void newCompareVersionsPage () {
		/**
		 * This method is called from other classes
		 * to reset the Compare Different Versions Page
		 */
		pages.remove(comparePage);
		comparePage = new ComparePage (new BorderLayout ());
		pages.add(comparePage, "Compare Page");
	}

	

	
	

}
