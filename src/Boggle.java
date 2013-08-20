import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Boggle {
	
	private int roundNum = 0;
	
	static Dice[] d;
	static final int GRID_SIZE = 4;
	
	// Worker
	private CountdownWorker wc;
	
	private JFrame frame;
	private Board board;
	
	private JSpinner mins;
	private JSpinner secs;
	
	private JProgressBar progressBar;
	
	
	JButton start;
	JButton reveal;
	JButton cancel;
	
	static void log(String s){
		log(s,1);
	}
	static void log(String s, int level){
		if(level < 1){log(s,1);return;}
		while(level > 1){
			System.out.print("	");
			level--;
		}
		System.out.print(s+"\n");
	}

	public static void main(String[] args) throws Exception{
		log("Boggle App 0.6 started!");
		
		log("Setting up dice definitions...");
		// Set up dice definitions
		d = new Dice[25];
		d[0] = new Dice(new char[]{
				'A','A','A','F','R','S'
		});
		d[1] = new Dice(new char[]{
				'A','A','E','E','E','E'
		});
		d[2] = new Dice(new char[]{
				'A','A','F','I','R','S'
		});
		d[3] = new Dice(new char[]{
				'A','D','E','N','N','N'
		});
		d[4] = new Dice(new char[]{
				'A','E','E','E','E','M'
		});
		d[5] = new Dice(new char[]{
				'A','E','E','G','M','U'
		});
		d[6] = new Dice(new char[]{
				'A','E','G','M','N','N'
		});
		d[7] = new Dice(new char[]{
				'A','F','I','R','S','Y'
		});
		d[8] = new Dice(new char[]{
				'B','J','K','Q','X','Z'
		});
		d[9] = new Dice(new char[]{
				'C','C','E','N','S','T'
		});
		d[10] = new Dice(new char[]{
				'C','E','I','I','L','T'
		});
		d[11] = new Dice(new char[]{
				'C','C','I','L','P','T'
		});
		d[12] = new Dice(new char[]{
				'C','E','I','P','S','T'
		});
		d[13] = new Dice(new char[]{
				'D','D','H','N','O','T'
		});
		d[14] = new Dice(new char[]{
				'D','H','H','L','O','R'
		});
		d[15] = new Dice(new char[]{
				'D','H','L','N','O','R'
		});
		d[16] = new Dice(new char[]{
				'D','H','L','N','O','R'
		});
		d[17] = new Dice(new char[]{
				'E','I','I','I','T','T'
		});
		d[18] = new Dice(new char[]{
				'E','M','O','T','T','T'
		});
		d[19] = new Dice(new char[]{
				'E','N','S','S','S','U'
		});
		d[20] = new Dice(new char[]{
				'F','I','P','R','S','Y'
		});
		d[21] = new Dice(new char[]{
				'G','O','R','R','V','W'
		});
		d[22] = new Dice(new char[]{
				'I','P','R','R','R','Y'
		});
		d[23] = new Dice(new char[]{
				'N','O','O','T','U','W'
		});
		d[24] = new Dice(new char[]{
				'O','O','O','T','T','U'
		});
		
		log("Passing over to EDT =>");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Boggle().createGUI(d);
			}
		});
	}
	
	private void createGUI(Dice[] d){
		log("Creating board:");
		board = new Board(d);
		
		// Create a frame
		log("Creating frame");
		frame = new JFrame("Boggle!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600,600));
		frame.setLayout(new BorderLayout());
		
		frame.add(board,BorderLayout.CENTER);
		
		log("Creating controls:");
		createControls();
		
		log("Displaying GUI");
		// Set Visible
		frame.pack();
		frame.setVisible(true);
		
		log("Game ready and waiting!");
	}
	
	
	private void createControls(){
		JPanel controlsBot = new JPanel(new BorderLayout());
		
		JPanel mainButtons = new JPanel();
		
		log("Creating buttons",2);
		// Start Button
		start = new JButton("Start");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Increase round count
				roundNum++;
				log("Starting round "+roundNum+":");
				start.setEnabled(false);
				board.showBoard(false);
				board.setNotice("5");
				new SwingWorker<Object, Object>(){
					int i;
					protected Object doInBackground() throws Exception {
						log("Countdown starting...",2);
						for(i = 5;i > 0;i--){
							// Sleep 5 timess
							try{Thread.sleep(1000);}catch(InterruptedException ex){}
							publish();
						}
						return null;
					}
					protected void process(List<Object> ls){
						board.setNotice(""+i+"");
					}
					protected void done(){
						// When done show the board and continue
						board.showBoard(true);
						board.shuffle();
						wc = new CountdownWorker();
						wc.execute();
					}
					
				}.execute();
			}
		});
		
		// Reveal Button
		reveal = new JButton("Show Board");
		reveal.setEnabled(false);
		reveal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.showBoard(true);
				reveal.setEnabled(false);
				start.setEnabled(true);
			}
		});
		
		// Cancel button
		cancel = new JButton("End");
		cancel.setEnabled(false);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(wc != null){
					wc.end();
				}
				cancel.setEnabled(false);
			}
		});
		
		mainButtons.add(start);
		mainButtons.add(reveal);
		mainButtons.add(cancel);
		
		
		
		
		// Options panel
		JPanel options = new JPanel();
		options.add(new JLabel("Options:"));
		
		JToggleButton rotate = new JToggleButton("Rotate Dice");
		rotate.setSelected(board.rotateDice);
		
		rotate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				board.rotateDice = (e.getStateChange() == ItemEvent.SELECTED);
			}
		});
		options.add(rotate);
		
		JSpinner fontSizer = new JSpinner(new SpinnerNumberModel(board.fontSize, 12, 150, 1));
		fontSizer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				board.setDiceFontSize((int) (((JSpinner) (e.getSource())).getValue()));
			}
		});

		options.add(fontSizer);
		
		controlsBot.add(mainButtons,BorderLayout.NORTH);
		controlsBot.add(options,BorderLayout.SOUTH);
		
		
		
		
		
		log("Creating timer",2);
		JPanel controlsTop = new JPanel(new BorderLayout());
		
		// Time chooser
		JPanel timeChooser = new JPanel();
		Font font = new Font("Arial",0,25);
		mins = new JSpinner(new SpinnerNumberModel(3, 0, 5, 1));
		timeChooser.add(mins);
		mins.setFont(font);
		timeChooser.add(new JLabel(":")).setFont(font);
		secs = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
		secs.setFont(font);
		timeChooser.add(secs);
		
		// Progress bar
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMaximum(180);
		
		controlsTop.add(timeChooser,BorderLayout.NORTH);
		controlsTop.add(progressBar,BorderLayout.SOUTH);
		
		
		
		
		frame.add(controlsTop,BorderLayout.NORTH);
		frame.add(controlsBot,BorderLayout.SOUTH);
	}
	
	
	
	
	class CountdownWorker extends SwingWorker<Integer, int[]>{
		
		int vals[] = new int[2];
		
		int min;
		int sec;
		
		boolean c = false;
		
		public void end(){
			c = true;
			if(cancel(true)){
				// Decrease round number
				roundNum--;
				log("Round cancelled",3);
				
			}
		}
		
		protected Integer doInBackground() throws Exception {
			log("Preparing timer",2);
			// Freeze the spinners
			mins.setEnabled(false);
			secs.setEnabled(false);
			
			min = vals[0] = ((int) mins.getValue());
			sec = vals[1] = ((int) secs.getValue());
			
			// Set up the progress bar
			progressBar.setMaximum(min*60 + sec);
			progressBar.setValue(0);
			
			// Activate the cancel button
			cancel.setEnabled(true);
			
			// Repeat each time
			log("Timer starting...",2);
			while((min > 0 || sec > 0) && c == false){
				// Sleep for 1 second
				try{Thread.sleep(1000);}catch(InterruptedException e){return null;}
				// Publish the time has decreased
				publish();
			}
			
			// Return null when finished (time will be at zero)
			return null;
		}
		
		protected void process(List<int[]> ls){
			// Check if value of secs is zero
			if(sec == 0){
				// Double check for mins
				if(min > 0){
					// Reset secs and decrease mins
					sec = 59;
					if(!c){
						secs.setValue(sec);
						mins.setValue(--min);
					}
				}
			}else{
				// Decrease Secs
				if(!c){secs.setValue(--sec);}
			}
			progressBar.setValue(progressBar.getValue()+1);
		}
		
		protected void done(){
			log("Timer over",2);
			// Make sound etc...
			
			log("Cleaning up",2);
			// Reactivate spinners
			mins.setEnabled(true);
			secs.setEnabled(true);
			mins.setValue(vals[0]);
			secs.setValue(vals[1]);
			
			// Hide the boggle board
			board.showBoard(false);
			board.setNotice("");
			
			reveal.setEnabled(true);
			start.setEnabled(false);
			
			log("Round end.");
		}
		
	}
}
