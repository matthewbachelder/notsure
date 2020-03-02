package com.machinelearning.project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CrossValidation {
	
	private int kFold;
	private int numberOfExamples;
	private int numberOfShuffles;
	private ArrayList<Integer>[] examples;
	
	public CrossValidation(){
		
	}
	
	
	
	
	public int getKFold(){
		return this.kFold;
	}
	public void setKFold(int kFold){
		this.kFold = kFold;
	}
	
	public int getNumberOfExamples(){
		return this.numberOfExamples;
	}
	public void setNumberOfExamples(int numberOfExamples){
		this.numberOfExamples = numberOfExamples;
	}
	
	public int getNumberOfShuffles(){
		return this.numberOfShuffles;
	}
	public void setNumberOfShuffles(int numberOfShuffles){
		this.numberOfShuffles = numberOfShuffles;
	}
	
	public ArrayList<Integer>[] getExamples(){
		return this.examples;
	}
	public void setExamples(ArrayList<Integer>[] examples){
		this.examples = examples;
	}
	
	public void intialize(String file) throws FileNotFoundException{
		
		Scanner scanner = new Scanner(new File(file));
		
		int row =0;
		while(scanner.hasNextLine()){
			
			String[] line = scanner.nextLine().split(" ");
			
			if(line.length>0){
				if(row==0){ 
					this.validateContent(row, line);
					this.intialize(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
				}else{
					this.validateContent(row, line);
					this.setExamples(row-1, line);
					
				}
			}
			row++;
		}
		scanner.close();
	}
	

	
	private void setExamples(int row, String[] line){
		
		for(int i=0; i < this.getNumberOfExamples(); i++ ){
			this.getExamples()[row].add(Integer.parseInt(line[i]));
		}
		
	}
	
	private void intialize(int kFold, int numberOfExamples, int numberOfShuffles){
		this.setKFold(kFold);
		this.setNumberOfExamples(numberOfExamples);
		this.setNumberOfShuffles(numberOfShuffles);
		this.setExamples(new ArrayList[numberOfShuffles]); 
		
		for(int i=0; i < numberOfShuffles; i++)
			this.getExamples()[i] = new ArrayList<Integer>();
	}
	
	
	
	private void validateContent(int row, String[] line){
		
		if(row==0){
			if(line.length<3){
				System.err.println("There is an error in row one of the validation file."
						+ " \nShould be 3 numbers. \nEach number saperated by a space (ex 2 3 1)"
						+ " \nQuitting now. Correct and retry.");
				System.exit(1);
				
			}
		}else{
			if(line.length < this.getNumberOfExamples()){
				System.err.println("There is an error at row "+(row)+" of the validation file."
						+ " \nShould be "+this.getNumberOfExamples()+" numbers. \nEach number saperated by a space (ex 2 3 1)"
						+ " \nQuitting now. Correct and retry.");
				System.exit(1);
			}
		}
	}
	
}
