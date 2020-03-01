import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// DataPoint - object class with instance data for each column in source data
// file, a constructor, a display method, getters, and a method for computing
// distance metric
class DataPoint {
	private String datetime;
	private int season, holiday, workingday, weather, humidity, count;
	private int hour; //time of day difference
	private double temp, atemp,	windspeed;
	
	public DataPoint(String d, int s, int ho, int wD, int we, double t, double aT,
			int hu, double wi, int c) {
		datetime = d;
		season = s;	// categorical
		holiday = ho;	// categorical
		workingday = wD;	// categorical
		weather = we;	// categorical
		temp = t;	// numerical
		atemp = aT;	// numerical
		humidity = hu;	// numerical
		windspeed = wi;	// numerical
		count = c;
		
		String hr = d.split(" ")[1]; //Split at space,
		hr = hr.split(":")[0]; //Split at x:00, we want x which is index 0
		hour = Integer.parseInt(hr); //Convert string to int and store in hours
	}
	
	public String getDatetime() {return datetime;}
	public int getSeason() {return season;}
	public int getHoliday() {return holiday;}
	public int getWorkingDay() {return workingday;}
	public int getWeather() {return weather;}
	public int getHumidity() {return humidity;}
	public int getCount() {return count;}
	public double getTemp() {return temp;}
	public double getAtemp() {return atemp;}
	public double getWindspeed() {return windspeed;}
	public int getHour() { return hour;} //Get the hour of day
	
	public double distance(DataPoint day2) {
		// CODE REQUIRED HERE FOR CALCULATING DISTANCE BETWEEN TWO DATA POINTS
		int diff1, diff2, diff3, diff4, diff5; //Season, holiday, workingday, weather, humid
		double diff6, diff7, diff8; //temp, actual temp, windspeed
		int diff9; //time of day
		
		//NOTE: The squaring in the actual equation will handle negatives
		if (this.getSeason() != day2.getSeason()) { //Season is different
			diff1 = 1; //So increase their distance apart
		} else { // If the season is the same
			diff1 = 0; //Do not increase their distance apart
		}
		diff2 = Math.abs(this.getHoliday() - day2.getHoliday()); //get difference which is
		diff3 = Math.abs(this.getWorkingDay() - day2.getWorkingDay()); //not greater than 1
		if (this.getWeather() != day2.getWeather()) { //not same
			diff4 = 1; //increase distance apart
		} else { //They are equal
			diff4 = 0; //Their distance apart shall not be increased
		}
		diff5 = this.getHumidity() - day2.getHumidity();
		diff6 = this.getTemp() - day2.getTemp();
		diff7 = this.getAtemp() - day2.getAtemp();
		diff8 = this.getWindspeed() - day2.getWindspeed();
		
		//Calculate difference between hours of day
		diff9 = Math.abs(this.getHour() - day2.getHour());
		
		//The largest difference in hours should be 12...
		if (diff9 > 12) { //then we have to fix the time diff
			diff9 = Math.abs((24-day2.getHour()) + this.getHour());
		}
		//System.out.println("This: "+this.getDatetime()+", Comp:"+day2.getDatetime()
			//+", Diff: "+diff9);
		
		return Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2) +
					     Math.pow(diff3, 2) + Math.pow(diff4, 2) +
					     Math.pow(diff5, 2) + Math.pow(diff6, 2) +
					     Math.pow(diff7, 2) + Math.pow(diff8, 2) +
					     Math.pow(diff9,  2));
	}
	
	/************************************************
	 * Distance without considering hour:
	 */
		/*public double distance(DataPoint day2) {
		// CODE REQUIRED HERE FOR CALCULATING DISTANCE BETWEEN TWO DATA POINTS
		int diff1, diff2, diff3, diff4, diff5;
		double diff6, diff7, diff8;
		
		//NOTE: The squaring in the actual equation will handle negatives
		if (this.getSeason() != day2.getSeason()) { //Season is different
			diff1 = 1; //So increase their distance apart
		} else { // If the season is the same
			diff1 = 0; //Do not increase their distance apart
		}
		diff2 = Math.abs(this.getHoliday() - day2.getHoliday()); //get difference which is
		diff3 = Math.abs(this.getWorkingDay() - day2.getWorkingDay()); //not greater than 1
		if (this.getWeather() != day2.getWeather()) { //not same
			diff4 = 1; //increase distance apart
		} else { //They are equal
			diff4 = 0; //Their distance apart shall not be increased
		}
		diff5 = this.getHumidity() - day2.getHumidity();
		diff6 = this.getTemp() - day2.getTemp();
		diff7 = this.getAtemp() - day2.getAtemp();
		diff8 = this.getWindspeed() - day2.getWindspeed();
		
		
		return Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2) +
					     Math.pow(diff3, 2) + Math.pow(diff4, 2) +
					     Math.pow(diff5, 2) + Math.pow(diff6, 2) +
					     Math.pow(diff7, 2) + Math.pow(diff8, 2));
	}
	*/
	 
	
	public String toString() {
		return datetime + ": " + count + " rentals";
	}
}
