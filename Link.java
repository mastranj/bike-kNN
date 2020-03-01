import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

// Link - a single link object with its data being a DataPoint object
class Link {
	public DataPoint data;              // data item (key)
	public Link next;              // next link in list

	public Link(DataPoint d) {
		data = d;
		next = null;
	}
		
	public void displayLink() {
		System.out.print(data);
	}
}
