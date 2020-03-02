package com.machinelearning.project2;

public class DistanceNode {

	
	private int distance;
	private int example;
	private DistanceNode next;
	
	
	
	public DistanceNode(int distance, int example, DistanceNode next){

		this.distance = distance;
		this.example = example;
		this.next = next;
		
		
	}
	
	
	public int getDistance(){
		return this.distance;
	}
	public int getExample(){
		return this.example;
	}
	public DistanceNode  getNext(){
		return this.next;
	}
	public void setNext(DistanceNode next){
		this.next = next;
	}
	
}
