package com.machinelearning.project2;

public class Result {
	
	private char[][] data;
	private double error;
	
	
	
	
	public Result(){
		
	}
	
	public char[][] getData(){
		return this.data;
	}
	public void setData(char[][] data){
		this.data = data;
	}
	
	
	public double getError(){
		return this.error;
	}
	public void setError(double error){
		this.error = error;
	}
	
	

}
