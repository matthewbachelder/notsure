package com.machinelearning.project2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NearestNeighbor {

	private CrossValidation cv;
	private Data data;

	public NearestNeighbor(CrossValidation cv, Data data) {
		this.cv = cv;
		this.data = data;
	}

	public void run() {
		for (int j = 1; j < 6; j++) { //this is for k nearest nieghbors starting with k= 1
			
			ArrayList<Result> results = new ArrayList<Result>(); //intializa  anew result set to store the result from each shuffle
			
			for (int i = 0; i < cv.getNumberOfShuffles(); i++) { //do KNN for each shuffle

				ArrayList<Integer> shuffle = cv.getExamples()[i]; //shuffle i

				int section = cv.getNumberOfExamples() / cv.getKFold(); //get the number of sections

				ArrayList<Integer>[] folds = partitionFolds(section, shuffle); //partition the eaxmples into their fold

				
				Result result = doKNN(j, folds);
				results.add(result); //add the result to the results set
			}
			
			
			Result min = null;
			
			for (int i = 0; i < results.size(); i++) {

				if (min == null)
					min = results.get(i);
				else {

					if (min.getError() > results.get(i).getError())
						min = results.get(i);
				}

			}

			

			
			
			double totalError = 0.0;
			for(int i=0; i < results.size(); i++){
				totalError += results.get(i).getError();
				
			}
			
			min.setError((double)totalError/(double)this.cv.getNumberOfShuffles());
			
			double e = (double)totalError/(double)this.cv.getNumberOfShuffles();
			double V = 0.0;
			
			for(int i=0; i < results.size(); i++){
				
				V+= ((results.get(i).getError()- e)*(results.get(i).getError()- e));
				
			}
			V = V/((double)this.cv.getNumberOfShuffles()-1);
			
			double sigma = Math.sqrt(V);
		
			
			System.out.println("k="+j+" e="+e+" sigma="+sigma+"");
			
			
			//char[][] trainSet(int kNearest, char[][] data, ArrayList<Integer> trainingExamples) {
			
			ArrayList<Integer> trainingExamples = new ArrayList<Integer>();
			for(int i=0; i < this.data.getExamplesCount(); i++){
				trainingExamples.add(i);
			}
			
			
			char[][] out = new char[this.data.getRows()][this.data.getColumns()];
			
			
			for (int i = 0; i < this.data.getRows(); i++) {
				for (int s = 0; s < this.data.getColumns(); s++) {
					
						out[i][s] = this.data.getData()[i][s];

				}
				
			}
			
			char[][] trained = trainSet(j, out, trainingExamples) ;
			
			for (int i = 0; i < this.data.getRows(); i++) {
				for (int s = 0; s < this.data.getColumns(); s++) {
					
						System.out.print(out[i][s] + " ");

				}
				System.out.println("");
			}
			
			out = null;
			trained = null;
			System.out.println("\n");
			
		}
	}

	private Result doKNN(int kNearest, ArrayList<Integer>[] folds) {

		int leaveOut = 0; // the section we are nor training with

		ArrayList<Result> results = new ArrayList<Result>();
		for (int k = 0; k < folds.length; k++) {
			
			Result result = new Result();

			result.setData(intializeTest(leaveOut, folds));
			ArrayList<Integer> trainingExamples = getTrainingExamples(leaveOut, folds);
			ArrayList<Integer> testingExamples = getTestingExamples(leaveOut, folds);


			result.setData(trainSet(kNearest, result.getData(), trainingExamples));

			
			int errorCount = 0;
			for (int i = 0; i < testingExamples.size(); i++) {

				char correctSymbol = this.data.getYValue()[testingExamples.get(i)];
				int x1 = this.data.getXValues()[testingExamples.get(i)][1];
				int x2 = this.data.getXValues()[testingExamples.get(i)][0];
				char appliedSymbol = result.getData()[x1][x2];

				if (appliedSymbol != correctSymbol)
					errorCount++;
			}

			result.setError(errorCount);
			

			leaveOut++;
			results.add(result);
			
		}

		Result min = null;

		for (int i = 0; i < results.size(); i++) {

			if (min == null)
				min = results.get(i);
			else {

				if (min.getError() > results.get(i).getError())
					min = results.get(i);
			}

		}

		
		int totalError = 0;
		for(int i=0; i < results.size(); i++){
			totalError += results.get(i).getError();
		}
		
		min.setError((double)totalError/this.data.getExamplesCount());
		
		return min;
	}

	private char[][] trainSet(int kNearest, char[][] data, ArrayList<Integer> trainingExamples) {

		for (int i = 0; i < this.data.getRows(); i++) {
			for (int j = 0; j < this.data.getColumns(); j++) {

				if (data[i][j] == '.') {

					DistanceList distance = getDistance(trainingExamples, i, j);

					
					int iterations = 0;
					int plus = 0;
					int minus = 0;

					DistanceNode head = distance.getHead();
					DistanceNode previous = null;
					while (head != null) {
						if (iterations < kNearest) {
							char symbol = this.data.getYValue()[head.getExample()];

							if (symbol == '+')
								plus++;
							else if (symbol == '-')
								minus++;

							iterations++;
						} else {
							
							char symbol = this.data.getYValue()[head.getExample()];
							char previousSymbol = this.data.getYValue()[previous.getExample()];
							
							if(symbol=='-' && previousSymbol=='+' && head.getDistance()==previous.getDistance()){
								plus--;
								minus++;
								
							}
							
							break;
							
						}
						previous = head;
						head = head.getNext();
					}
					
					if (plus == minus || minus > plus)
						data[i][j] = '-';
					else if (minus < plus)
						data[i][j] = '+';

				}

			}
		}

		return data;
	}

	private DistanceList getDistance(ArrayList<Integer> trainingExamples, int i, int j) {

		DistanceList distance = new DistanceList();

		for (int e = 0; e < trainingExamples.size(); e++) {

			int example = trainingExamples.get(e);

			int x1 = this.data.getXValues()[example][0];
			int x2 = this.data.getXValues()[example][1];

			int newx1 = j;
			int newx2 = i;

			int currentDistance = getSquare((x1 - newx1)) + getSquare((x2 - newx2));
			
			distance.insert(currentDistance, example);

		}


		return distance;
	}

	private int getSquare(int n) {
		return n * n;
	}

	private ArrayList<Integer> getTestingExamples(int leaveOut, ArrayList<Integer>[] folds) {
		ArrayList<Integer> testingExamples = new ArrayList<Integer>();
		for (int j = 0; j < folds[leaveOut].size(); j++) {
			testingExamples.add(folds[leaveOut].get(j));
		}
		return testingExamples;
	}

	private ArrayList<Integer> getTrainingExamples(int leaveOut, ArrayList<Integer>[] folds) {
		ArrayList<Integer> trainingExamples = new ArrayList<Integer>();

		for (int i = 0; i < folds.length; i++) {
			if (i != leaveOut) {

				for (int j = 0; j < folds[i].size(); j++) {
					trainingExamples.add(folds[i].get(j));
				}
			}
		}

		return trainingExamples;
	}

	private char[][] intializeTest(int leaveOut, ArrayList<Integer>[] folds) {
		char[][] data = new char[this.data.getRows()][this.data.getColumns()];

		for (int i = 0; i < this.data.getRows(); i++) {
			for (int j = 0; j < this.data.getColumns(); j++) {
				data[i][j] = '.';

			}

		}

		for (int i = 0; i < folds.length; i++) {

			if (i != leaveOut) {

				for (int j = 0; j < folds[i].size(); j++) {

					// folds[i].get(j) is the example number
					int row = this.data.getXValues()[folds[i].get(j)][1];
					int column = this.data.getXValues()[folds[i].get(j)][0];
					char symbol = this.data.getYValue()[folds[i].get(j)];

					data[row][column] = symbol;

				}

			}
		}

		
		
		return data;
	}

	private ArrayList<Integer>[] partitionFolds(int sectionSize, ArrayList<Integer> shuffle) {

		ArrayList<Integer>[] folds = new ArrayList[this.cv.getKFold()];

		int currentSection = 0;

		while (currentSection < this.cv.getKFold()) {
			folds[currentSection] = new ArrayList<Integer>();

			for (int i = 0; i < sectionSize; i++) {
				folds[currentSection].add(shuffle.get((currentSection * sectionSize) + i));
			}
			currentSection++;
		}

		if (sectionSize * this.cv.getKFold() < this.cv.getNumberOfExamples()) {
			int remainder = (currentSection * sectionSize);
			currentSection--;
			for (; remainder < this.cv.getNumberOfExamples(); remainder++) {
				folds[currentSection].add(shuffle.get(remainder));
			}
		}

		return folds;
	}

}
