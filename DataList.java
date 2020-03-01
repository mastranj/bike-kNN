import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// DataList - a collection of DataPoints stored in a Linked List arrangement
// Supports insertion of DataPoints into the list, a kNN list processing
// method operation that determines for a DataPoint and a value of k what the
// predicted number of bikes needed for the given DataPoint, and a runTest
// method that takes in a list of DataPoints with known bike demand and
// computes the total sum of squared error of kNN against the list of DataPoints
// using the given k.
// Note that delete and find operations are not required.
class DataList {
	private Link first;
	private int numItems;

	public DataList() {
		first = null;
		numItems = 0;
	}
	
	public int getNum() {return numItems;}

	// insert DataPoint in list in constant time
	public void insert(DataPoint d) {
		Link newLink = new Link(d);
		newLink.next = first;
		first = newLink;
		numItems ++;
   }

   // calculate kNN prediction of number of bikes needed on newDay considering
   // given k number of nearest neighbors
	
	/****************************************
	 * The kNN below, keeps the array sorted
	 *  */
	
	/*public int kNN(DataPoint newDay, int k) {
		DataPoint kDataPoints[] = new DataPoint[k];
		Double kDistances[] = new Double[k];
		Link temp = first;
		long time = System.nanoTime();
		
		for (int i = 0; i < numItems; i++) {
			double dist = newDay.distance(temp.data);
			//System.out.println("At hand: "+dist);
			if (i < k) { //Then the array is not filled because we must insert
				kDataPoints[i] = newDay;  //Up until i = k, then array is filled
				kDistances[i] = dist;
				
				//Need to sort initially,
				for (int sort = 0; sort < i-1; sort++) {
					if (kDistances[sort] > kDistances[sort+1]) {
						//Then swap
						double tempD = kDistances[sort+1];
						kDistances[sort+1] = kDistances[sort];
						kDistances[sort] = tempD;
					}
				} //End of sort loop, sorted :)
				
				//Note on sorting:
				//Highest index means highest distance
			} else { //We must see if our new distance is smaller than any inside
				int where = 0;
				boolean found = false;
				for (int j = 0; j < kDataPoints.length; j++) {
					if (kDistances[j] > dist) { //The current dist is smaller
						//So, we are going to have to bump everything up
						found = true;
						where = j;
						for (int sort = k-2; sort >= j; sort--) {
							kDistances[sort+1] = kDistances[sort];
							kDataPoints[sort+1] = kDataPoints[sort];
						}
						break;
					} //else continue through loop
				}
				if (found) {
					kDistances[where] = dist;
					kDataPoints[where] = temp.data;
				}
			}
			//System.out.println("\n New:");
			//for (int show = 0; show < k; show++) {
			//	System.out.println(kDistances[show]);
			//}
			temp = temp.next;
		}
		
		double pred = 0;
		for (int i = 0; i<kDataPoints.length; i++) {
			pred += kDataPoints[i].getCount();
			//System.out.print(kDataPoints[i].getCount()+ "\n");
		}
		long endTime = System.nanoTime();
		long timeDiff = endTime - time;
		System.out.println("Time to run: " + timeDiff);
		return (int)Math.round(pred/k);
	}*/
	
	/****************************************
	 * This kNN, as opposed to the one above,
	 * Finds the maximum each time and then
	 * replaces it with the new dist, if the
	 * new dist is less than at least one of
	 * the currents.
	 *  */
	public int kNN(DataPoint newDay, int k) {
		
		//Arrays start with nulls, assuming that the k value is less than
		//numItems. Also assumes that k is greater than 0.
		//If k=0, then there is no prediction. we will return 0.
		//The first k iterations will fill the array. After that,
		//Comparisons start to decide if there is a closer neighbor.
		
		//kDataPoints and kDistances indexes line up with information about the
		//same neighbor. kDistances stores the distance between the neighbor
		//and the newDay, kDataPoints stores the neighbor as a datapoint.
		//If kDataPoints[j] is assigned a new DataPoint, then, kDistances[j]
		//is updated too
		if (k==0) { //Then this algorithm will not be accurate
			return 0;
		}
		if (k > numItems) { //There are not enough neighbors to do this algorithm
			return 0;
		}
		DataPoint kDataPoints[] = new DataPoint[k];
		Double kDistances[] = new Double[k];
		
		//Starts at the first link, after each iteration it updates to next
		Link temp = first;
		//long time = System.nanoTime();

		for (int i = 0; i < numItems; i++) {
			double dist = newDay.distance(temp.data); //Get distance between two points
			//System.out.println("New Distance at hand: "+dist);
			if (i < k) { //Then the array is not filled because we must insert
				kDataPoints[i] = temp.data;  //Up until i = k, then array is filled
				kDistances[i] = dist; //Must assign distance too. Don't want to lose
									  //it, additionally, these two arrays align
									  //when describing the same point and distance
			} else { //We must see if our new distance is smaller than any inside
				
				int max = 0;
				boolean found = false;
				
				for (int j = 0; j < kDataPoints.length; j++) {
					if (kDistances[j] > dist) { //The current dist is smaller, replace
						found = true; //True, the dist and DataPoint should be inserted
						if (kDistances[j] > kDistances[max]) {
							max = j;
						}
					} //else continue through loop
				}
				if (found) { //only if a different maximum was found
					kDataPoints[max] = temp.data;
					kDistances[max] = dist;
				}
			}
			temp = temp.next;
		}
		
		
		//Now let's get the predicted count
		double pred = 0;
		for (int i = 0; i<kDataPoints.length; i++) {
			pred += kDataPoints[i].getCount();
		}
			return (int)Math.round(pred/k); //This averages the predictions
											//of the k nearest neighbors
	}

	// calculate total sum of squared error across all labeled items in
	// training against kNN prediction with given k
	public long runTest(DataList training, int k) {
		// runTest operation required
		
		Link curr = first; //curr initially points to first link
		long difference = 0; //Total square diff
		//keep track of smallest and largest square diff, start with current data point
		int max = curr.data.getCount(), min = curr.data.getCount(); 
		final boolean DISP = false; //True if you want to check how accurate this method is
		
		
		while (curr != null) { //run until curr is null, otherwise the link curr
							   //is not null and thus contains a DataPoint
			int pred = training.kNN(curr.data, k); //get k nearest neighbors over the Data List
									//Training (using its data) to predict count for
									//curr.data (which is the data of the Link
									//currently on in the testing data list)
									//This returns the prediction, but we know the actual
			int currDiff = (int)Math.pow(pred-curr.data.getCount(), 2); //Take diff, then square
			difference += currDiff; //Add the difference of one point to entire difference
			
			//Calculate max and min square diff
			if (currDiff < min)
				min = currDiff;
			if (currDiff>max)
				max = currDiff;
			
			
			//Info output:
			String currDate = curr.data.getDatetime();
			int actualCount = curr.data.getCount();
			if (DISP)
				System.out.println(currDate+": actual bikes: "+actualCount
						+" Predicted count: "+pred+". Square Difference: "+currDiff);
			
			curr = curr.next;
		}
		
		
		//System.out.println("Total Sum of Squared Differences: "+difference);
		double avg = (double)difference/(double)numItems; //get square average by dividing by number
										  //of items considered
		//System.out.println("Average Sum of Squared Differences: "+avg);
		//System.out.println("Maximum square difference found: "+max+", Min: "+min);
		
		
		return difference;
	}
	
	public void displayList() {
		System.out.print("List (first-->last): ");
		Link current = first;
		while(current != null) {
			current.displayLink();
			System.out.println("\t");
			current = current.next;
		}
	}
}