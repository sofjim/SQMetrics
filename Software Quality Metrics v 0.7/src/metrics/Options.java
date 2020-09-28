package metrics;

import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Options extends JOptionPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1945379341447108783L;
	private boolean rfcOtherMeths = false; 
	private boolean cboInh = false;
	private boolean damProtected = false;
	private boolean moaParams = false;
	private boolean moaReturnTypes = false;
	private boolean moaInsideParams = false;
	private static JCheckBox rfcBox;
	private static JCheckBox cboBox;
	private static JCheckBox damBox;
	private static JCheckBox moaParBox;
	private static JCheckBox moaRetBox;
	private static JCheckBox moaInsBox;
	
	
	public Options() {

		//Create the check boxes.
        rfcBox = new JCheckBox("RFC: Include other classes' methods which call the current class' methods (slower)");
        rfcBox.setSelected(false);
        rfcBox.setToolTipText("<html><p width=\"400\">"
				+ "According to C&K, there is no need to calculate the other classes' methods,"
				+ " which call the current class' methods, but for others they must be counted. So there is a checkbox added "
				+ "for this option." + "</p></html>");
        
        cboBox = new JCheckBox("CBO: Include inheritance relations (faster)");
        cboBox.setSelected(true);
        cboBox.setToolTipText("<html><p width=\"400\">"
				+ "According to C&K, the inheritance relations must not be calculated for coupling. "
				+ "Others believe that they must, so there is an option for the user to choose." + "</p></html>");
        
        moaParBox = new JCheckBox("MOA: Include constructors' and methods' parameters (a little bit slower)");
        moaParBox.setSelected(false);
        moaParBox.setToolTipText("<html><p width=\"400\">"
				+ "B&D do not specify which variables to consider for the calculation of the MOA metric. "
				+ "With this option checked, the constructors' and methods' parameters, will be included." + "</p></html>");
        
        moaRetBox = new JCheckBox("MOA: Include methods' return types (a little bit slower)");
        moaRetBox.setSelected(false);
        moaRetBox.setToolTipText("<html><p width=\"400\">"
				+ "B&D do not specify which variables to consider for the calculation of the MOA metric. "
				+ "With this option checked, the return types of the methods, will be included." + "</p></html>");
        
        moaInsBox = new JCheckBox("MOA: Include variables that declared inside constructors' and methods' bodies (quite much slower)");
        moaInsBox.setSelected(false);
        moaInsBox.setToolTipText("<html><p width=\"400\">"
				+ "B&D do not specify which variables to consider for the calculation of the MOA metric. "
				+ "With this option checked, the parameters that are defined into the constructors' and methods' bodies, will be included." + "</p></html>");
        
        damBox = new JCheckBox("DAM: Include protected fields (almost the same)");
        damBox.setSelected(false);
        damBox.setToolTipText("<html><p width=\"400\">"
				+ "B&D, for the DAM metric, use the exact phrase:"
				+ " \"the number of the private (protected) attributes\". Of course, "
				+ "these are two different meanings in OO Programming. Therefore, the user will "
				+ "have to decide, whether the protected fields will be counted or not." + "</p></html>");
        
        //Put the check boxes in a column in a panel
        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(rfcBox);
        checkPanel.add(cboBox);
        checkPanel.add(moaParBox);
        checkPanel.add(moaRetBox);
        checkPanel.add(moaInsBox);
        checkPanel.add(damBox);
        
        // add the panel to a dialog
        int n = optDialog (checkPanel);
        
        switch (n) {
		case 0:
        
        rfcOtherMeths = rfcBox.isSelected();
        cboInh = cboBox.isSelected();
        moaParams = moaParBox.isSelected();
        moaReturnTypes = moaRetBox.isSelected();
        moaInsideParams = moaInsBox.isSelected();
        damProtected = damBox.isSelected();
        
        break;
        
		case 1:
			// nothing happens
        }

	}

	public Options (String a) {
		
		
        
	}
	
	public static int optDialog (JPanel panel) {
		/**
		 * This method pop ups a dialog message that prompts
		 * the user to pick options for calculation
		 */
        		
		Object[] options = {"Use", "Cancel"};
		int n = JOptionPane.showOptionDialog(null, panel,
				"Options",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
        
		return n;
	}


	public boolean getRfcOtherMeths() {
		return rfcOtherMeths;
	}


	public boolean getCboInh() {
		return cboInh;
	}


	public boolean getDamProtected() {
		return damProtected;
	}


	public boolean getMoaParams() {
		return moaParams;
	}


	public boolean getMoaReturnTypes() {
		return moaReturnTypes;
	}


	public boolean getMoaInsideParams() {
		return moaInsideParams;
	}

}
