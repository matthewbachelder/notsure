package com.machinelearning.project2;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
		
		String cValidationFileName = "resources/crossValidationInformation";
		String dataFileName = "resources/data";
		
		if(args.length< 2){
			System.out.println("Missing program arguments. "
					+ "Defaulting to file names "
					+ "crossValidationInformation and data. Both are stored in the Resources folder");
		}else{
			cValidationFileName = args[0];
			dataFileName = args[1];
		}
		
		
		CrossValidation cv = new CrossValidation();
		
		try {
			cv.intialize(cValidationFileName);
		} catch (FileNotFoundException e) {
			System.err.print("Cross Validation File was not found in the location specified. Exiting.");
			System.exit(1);
		}
		
		Data data = new Data();
		
		try {
			data.intialize(dataFileName);
		} catch (FileNotFoundException e) {
			System.err.print("Data File was not found in the location specified. Exiting.");
			System.exit(1);
		}
		
		if(cv.getNumberOfExamples()!= data.getExamplesCount()){
			System.err.println("Number of examples specified in Cross Validation File does not macth "
					+ "the number of examples in the data file. Exiting");
			System.exit(1);
		}
		
		
		NearestNeighbor nn = new NearestNeighbor(cv, data);
		
		nn.run();
		System.exit(0);
		
		
		
		

	}

}
