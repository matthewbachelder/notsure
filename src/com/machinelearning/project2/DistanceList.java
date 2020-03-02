package com.machinelearning.project2;

public class DistanceList {

	private DistanceNode head;

	public DistanceList() {
		head = null;

	}

	public void insert(int distance, int example) {

		if (head == null) {
			head = new DistanceNode(distance, example, null);
		
		} else {
			
			if (distance < head.getDistance()) {
				DistanceNode t = head;
				head = new DistanceNode(distance, example, t);
			} else {

				
				if (head.getNext() != null) {
					boolean inserted = false;
					DistanceNode previous = head;
					DistanceNode next = previous.getNext();
					
					while (next.getNext() != null) {
						
						if(next.getDistance() >= distance){
							DistanceNode t = next;
							next = new DistanceNode(distance, example, t);
							previous.setNext(next);
							inserted = true;
							break;
						}
						previous = next;
						next = next.getNext();
						
					}

					if(!inserted){
						
						if(distance< next.getDistance()){
							DistanceNode t = new DistanceNode(distance, example, next);
							previous.setNext(t);
						}else{
						
						DistanceNode t = new DistanceNode(distance, example, null);
						next.setNext(t);
						}
						
					}
				}else{
					DistanceNode t = new DistanceNode(distance, example, null);
					head.setNext(t);
					
				}
				
				
			}

		}

	}
	
	
	public DistanceNode getHead(){
		return this.head;
	}
	
	public void printList(){
		
		
		DistanceNode t = head;
		
		while(t!=null){
			
			
			System.out.println(" Distance "+t.getDistance());
			t= t.getNext();
		}
		
	}
	

}
