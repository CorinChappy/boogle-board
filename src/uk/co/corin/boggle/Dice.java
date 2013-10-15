package uk.co.corin.boggle;

import java.util.Random;


public class Dice {
	
	// Main array for dice letters
	private char[] letters = new char[6];
	
	private int FU = 0;
	
	public Dice(char[] l) throws Exception{
		// Ignore more than 6 chars
		//if(l.length < 6){throw new Exception();}
		for(int j = 0;j < 6;j++){
			letters[j] = l[j];
		}
	}
	
	public void roll(){
		FU = new Random().nextInt(6);
	}
	
	public char getLetter(){
		return letters[FU];
	}
	
	public String getStringLetter(){
		
		return (String.valueOf(letters[FU]).equals("Q"))?"Qu":String.valueOf(letters[FU]);
	}
	
}
