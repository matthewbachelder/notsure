package com.machinelearning.project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {

	private char[][] data;
	private int rows;
	private int columns;
	private int examplesCount;
	private int[][] xValues;
	private char[] yValue;
	
	public Data() {

	}
	
	public int[][] getXValues(){
		return this.xValues;
	}
	public void setXValues(int[][] xValues){
		this.xValues = xValues;
	}
	
	public char[] getYValue(){
		return this.yValue;
	}
	public void setYValue(char[] yValue){
		this.yValue = yValue;
	}

	public char[][] getData() {
		return this.data;
	}

	public void setData(char[][] data) {
		this.data = data;
	}
	
	
	public int getRows(){
		return this.rows;
	}
	public void setRows(int rows){
		this.rows = rows;
	}
	
	
	public int getColumns(){
		return this.columns;
	}
	public void setColumns(int columns){
		this.columns = columns;
	}
	
	public int getExamplesCount(){
		return this.examplesCount;
	}
	public void setExamplesCount(int examplesCount){
		this.examplesCount = examplesCount;
	}
	
	public void incrementExamplesCount(){
		this.examplesCount++;
	}

	public void intialize(String fileName) throws FileNotFoundException {

		Scanner scanner = new Scanner(new File(fileName));

		int row = 0;
		
		while (scanner.hasNextLine()) {
			String[] line = scanner.nextLine().split(" ");

			if (row == 0) {
				validateContent(row, line);
				intialize(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
			}else{
				validateContent(row, line);
				setData(row-1, line);
				
			}
			row++;
		}
		scanner.close();
		intializeDataValues();
		
	}
	
	
	private void intializeDataValues(){
		 this.setXValues(new int[this.getExamplesCount()][2]);
		 this.setYValue(new char[this.getExamplesCount()]);
		 
		 int example = 0;
		 
		 for(int x2=0; x2 < this.getRows(); x2++){
			 
			 for(int x1=0; x1 < this.getColumns(); x1++){
				 
				 if(this.getData()[x2][x1]=='+' || this.getData()[x2][x1]=='-'){
					 this.getXValues()[example][0] = x1;
					 this.getXValues()[example][1] = x2;
					 this.getYValue()[example] = this.getData()[x2][x1];
					 example++;
				 }
				 
			 }
			 
		 }
		
	}
	
	private void setData(int row, String[] data){
		
		for(int i=0; i < this.columns; i++){
			
			if(data[i].compareTo("+")==0){
				this.getData()[row][i] = '+';
				this.incrementExamplesCount();
			}
			else if(data[i].compareTo("-")==0){
				this.getData()[row][i] = '-';
				this.incrementExamplesCount();
			}
			
		}
		
	}

	private void intialize(int rows, int columns){
		this.setRows(rows);
		this.setColumns(columns);
		this.setData(new char[rows][columns]);
		for(int i=0; i < this.getRows(); i ++){
			for(int j=0; j < this.getColumns(); j++){
				this.getData()[i][j] = '.';
			}
		}
	}
	
	
	private void validateContent(int row, String[] line) {

		if (row == 0) {
			if (line.length < 2) {
				System.err.println("There is an error in row one of the validation file."
						+ " \nShould be 3 numbers. \nEach number saperated by a space (ex 2 3 1)"
						+ " \nQuitting now. Correct and retry.");
				System.exit(1);

			}
		} else {
			if (line.length < this.columns) {
				System.err.println("There is an error at row " + (row) + " of the validation file." + " \nShould be "
						+ this.columns + " numbers. \nEach number saperated by a space (ex 2 3 1)"
						+ " \nQuitting now. Correct and retry.");
				System.exit(1);
			}
		}
	}
}
