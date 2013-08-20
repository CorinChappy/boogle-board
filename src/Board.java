import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Board extends JPanel{
	
	// Board Options
	boolean rotateDice = false;
	int fontSize = 72;
	
	
	// The labels on the board
	private DiceDisplay[][] grid = new DiceDisplay[Boggle.GRID_SIZE][Boggle.GRID_SIZE];
	private JPanel container;
	private JPanel white;
	private JTextArea title;
	
	// The dice
	private List<Dice> die = new ArrayList<Dice>(Boggle.GRID_SIZE*Boggle.GRID_SIZE);
	

	private Font diceFont = new Font("Arial",Font.BOLD,fontSize);
	
	
	public Board() {
		this.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		this.setLayout(new GridLayout());
		
		white = new JPanel(new GridLayout());
		white.setBackground(Color.WHITE);
		title = new JTextArea();
		title.setText("Welcome to Boggle!");
		title.setEditable(false);
		title.setWrapStyleWord(true);
		title.setLineWrap(true);
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		title.setFont(new Font("Arial",Font.BOLD,72));
		white.add(title);
		
		
		container = new JPanel(new GridLayout(Boggle.GRID_SIZE, Boggle.GRID_SIZE, 0, 0));
		
		Boggle.log("Creating grid",2);
		// Put in the JLabels
		for(int i = 0;i < Boggle.GRID_SIZE;i++){
			for(int j = 0;j < Boggle.GRID_SIZE;j++){
				DiceDisplay l = new DiceDisplay();
				grid[i][j] = l;
				container.add(l);
			}
		}
		
		showBoard(false);
	}
	
	public Board(Dice[] dice){
		this();
		addDice(dice);
	}
	
	public void addDice(Dice[] dice){
		Boggle.log("Adding dice",2);
		for(Dice d : dice){
			addDie(d);
		}
	}
	
	public void addDie(Dice d){
		die.add(d);
	}
	
	
	public void shuffle(){
		shuffleBoard();
		
		Boggle.log("Rolling the dice",2);
		for(Dice d : die){
			d.roll();
		}
		
		if(rotateDice){Boggle.log("Rotating dice",2);}
		// Display the results
		int k = 0;
		for(int i = 0;i < Boggle.GRID_SIZE;i++){
			for(int j = 0;j < Boggle.GRID_SIZE;j++){
				grid[i][j].setText(die.get(k++).getStringLetter());
				// Rotate each square
				if(rotateDice){rotateDice(i, j);}
			}
		}
	}
	
	private void shuffleBoard(){
		Boggle.log("Moving dice around",2);
		Collections.shuffle(die);
	}
	
	private void rotateDice(int i, int j){
		grid[i][j].changeRotate(new Random().nextInt(4));
	}
	
	
	public void showBoard(boolean b){
		this.removeAll();
		if(b){
			this.add(container);
		}else{
			this.add(white);
		}
		this.repaint();
	}
	
	public void setNotice(String s){
		title.setText(s);
	}
	
	
	void setDiceFontSize(Integer size){
		fontSize = size;
		diceFont = diceFont.deriveFont(size.floatValue());
		for(DiceDisplay[] a : grid){
			for(DiceDisplay b : a){
				b.setDiceFont(diceFont);
			}
		}
		
	}
	
	
	private class DiceDisplay extends JComponent {
		
		private DiceLabel label = new DiceLabel();
		
		private int rot = 0;
		private double[] vals = {0,0.5*Math.PI,Math.PI,1.5*Math.PI};
		
		public DiceDisplay() {
			super();
			this.setLayout(new BorderLayout());
			this.setBorder(new LineBorder(new Color(0, 0, 0), 2));			
			
			this.add(label);
		}
		
		void changeRotate(int r){
			if(r > 3){return;}
			rot = r;
		}
		
		void setText(String s){
			label.setText(s);
		}
		
		void setDiceFont(Font f){
			label.setDiceFont(f);
		}
		
		private class DiceLabel extends JPanel {
			
			private JLabel l = new JLabel("");
			
			void setDiceFont(Font f){
				l.setFont(f);
			}
			
			public DiceLabel() {
				l.setHorizontalAlignment(SwingConstants.CENTER);
				l.setFont(diceFont);
				
				this.setLayout(new BorderLayout());
				this.add(l);
			}
			
			void setText(String s){
				l.setText(s);
			}
			// Paint component
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				if(rotateDice){
					Graphics2D g2 = (Graphics2D) g;
					g2.rotate(vals[rot], this.getWidth()/2, this.getHeight()/2);
				}
			}
		}
	}
}
