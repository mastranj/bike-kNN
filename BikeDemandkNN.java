import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BikeDemandkNN {
	public static void main (String[] args) throws IOException {
		int k = 5; //Starting k value
		int checkks = 5; //ending k value
		int bestk = -1; //k with lowest squared difference
		long bestSquared = -1; //best squared value
		long errors[] = new long[checkks-k+1]; //used to make copying and pasting
											  //data easier, for square diffs
		
		final boolean DISP_TIME_ALL = true; //Toggles display of all times at once
		final boolean DISP_ERROR_ALL = true; //Toggles display of all sums at once
		long times[] = new long[checkks-k+1]; // Same as above, but for times
			//System.out.println("Time through: "+(i+1));
			Scanner fileScan = new Scanner(new File("sharingData.csv"));
			fileScan.nextLine();	// read past header line
			
			DataList training = new DataList();
			DataList testing = new DataList();
			
			Random gen = new Random();
			
			while (fileScan.hasNext()){
				String rowData = fileScan.nextLine();
				Scanner dayScan = new Scanner(rowData);
				dayScan.useDelimiter(",");
				
				String datetime;
				int season, holiday, workingday, weather, humidity, count;
				double temp, atemp,	windspeed;
				
				datetime = dayScan.next();
				season = dayScan.nextInt();
				holiday = dayScan.nextInt();
				workingday = dayScan.nextInt();
				weather = dayScan.nextInt();
				temp = dayScan.nextDouble();
				atemp = dayScan.nextDouble();
				humidity = dayScan.nextInt();
				windspeed = dayScan.nextDouble();
				count = dayScan.nextInt();
				
				DataPoint tempDay = new DataPoint(datetime, season, holiday, workingday,
						weather, temp, atemp, humidity, windspeed, count);
	
				
				// once insertion has been tested, the following code inserts
				// data items into the testing list with 1 in 100 probability
				if (gen.nextInt(100)==0) {
					testing.insert(tempDay);
				} else {
					training.insert(tempDay);
				}
				
	
				
				dayScan.close();
			}
			fileScan.close();
	
			System.out.println(training.getNum() + " items inserted in training list");
			System.out.println(testing.getNum() + " items inserted in testing list");
	
			
			// method call to compare held out test data against set of training
			// data using a k of 5
			int i = 0; // i does not necessarily equal k.
			while (k <= checkks) {
				long time = System.nanoTime(); //starting time
				long error = testing.runTest(training, k);
				//System.out.println("Total sum of squared error: " + error +"\n");
				time = (System.nanoTime() - time); //time to run
				
				//error = error/testing.getNum();
				if (bestSquared == -1 || bestSquared > error) {
					bestSquared = error;
					bestk = k;
				}
				System.out.println("K Value: "+k+":"); //Displays the time per run
				System.out.println("Square Diff: "+error); //prints out the square diff
				System.out.println("Time (ns): "+time+"\n");
				errors[i] = error;
				times[i] = time;
				k++;
				i++;
			}
			if (DISP_ERROR_ALL) {
				System.out.println("Square Differences for each k, sequentially:");
				for (int j = 0; j<i; j++) {
					System.out.println(errors[j]);
				}
			}
			if (DISP_TIME_ALL) {
				System.out.println("\nTime Ran for each k, sequentially:");
				for (int j = 0; j<i; j++) {
					System.out.println(times[j]);
				}
			}
		System.out.println("Best k: "+bestk+" with: "+bestSquared);
			/*int optimalK = 8;
			DataPoint testDay = new DataPoint("1/19/2011 0:00", 1, 0, 1, 2, 9.02, 13.635, 93, 0, 0);
			System.out.println("Prediction 1: " + training.kNN(testDay, optimalK));
			testDay = new DataPoint("1/19/2011 10:00", 1, 0, 1, 2, 10.66, 13.635, 93, 8.9981, 0);
			System.out.println("Prediction 2: " + training.kNN(testDay, optimalK));
			testDay = new DataPoint("4/19/2011 12:00", 2, 0, 1, 2, 22.14, 25.76, 64, 6.0032, 0);
			System.out.println("Prediction 3: " + training.kNN(testDay, optimalK));
			testDay = new DataPoint("4/19/2011 22:00", 2, 0, 1, 1, 19.68, 23.485, 77,  6.0032, 0);
			System.out.println("Prediction 4: " + training.kNN(testDay, optimalK));
			testDay = new DataPoint("7/19/2011 4:00", 3, 0, 1, 1, 29.52, 34.09, 70, 8.9981, 0);
			System.out.println("Prediction 5: " + training.kNN(testDay, optimalK));
			testDay = new DataPoint("7/19/2011 13:00", 3, 0, 1, 1, 35.26, 41.665, 53, 7.0015, 0);
			System.out.println("Prediction 6: " + training.kNN(testDay, optimalK));
		*/
	}
}