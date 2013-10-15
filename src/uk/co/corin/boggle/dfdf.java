package uk.co.corin.boggle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;


public class dfdf extends JPanel {
	public dfdf() {
		
		JSpinner spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setModel(new SpinnerNumberModel(3, 1, 5, 0));
		add(spinner);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		add(spinner_1);
		
		
		
		JTextPane textPane = new JTextPane();
		add(textPane);
		
		JLabel lblNewLabel = new JLabel("New label");
		add(lblNewLabel);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(20);
		add(progressBar);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		add(tglbtnNewToggleButton);
	}

}
