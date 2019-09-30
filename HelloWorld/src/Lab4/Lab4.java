package Lab4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import length.*;

/**
 * @author - Maximilian Guzman
 * ASAP: gan022
 * Assignment: Lab 4
 * Due 9/29/2017
 * Lab4 reads in a text file per-line and creates Length objects
 * in meters, inches, yards, and feet. Program will find the smallest
 * and largest Length object found, then attempt to add all objects together
 * in order and reverse order. The sum is printed out in 
 * meters, inches, feet, yards to demonstrate errors in decimal point rounding.
 **/
public class Lab4 {

		/**
		 * @param args - Standard main parameter
		 * Reads a file to create Length objects. Main will
		 * find and print out the smallest and largest length.
		 * All length objects are added together in-order and 
		 * in reverse order and printed out 
		 * in meters, inches, feet and yards. 
		 */
	public static void main(String[] args) {
		// Attempt to find the file and open
		// it for reading
		Scanner in = null;
		try {
			in = new Scanner(new File("data.txt"));
		} catch (FileNotFoundException exception) {
			throw new RuntimeException("failed to open data.txt");
		}

		// Use an ArrayList of Length objects to store all Length
		// objects made.
		ArrayList<Length> length = new ArrayList<Length>();
		
		// Read file for a double and String.
		// The String pulled will determine what subclass
		// of Length is made with the double to be stored
		// in the ArrayList
		while (in.hasNextDouble()) {
			double value = in.nextDouble();
			String unit = in.next();

			// Determine the subclass to be made and stored
			char charCompare = unit.toLowerCase().charAt(0);
			switch (charCompare) {
			case 'm':
				Meter meter = new Meter(value);
				System.out.println(meter);
				length.add(meter);
				break;
			case 'f':
				Foot foot = new Foot(value);
				System.out.println(foot);
				length.add(foot);
				break;
			case 'i':
				Inch inch = new Inch(value);
				System.out.println(inch);
				length.add(inch);
				break;
			case 'y':
				Yard yard = new Yard(value);
				System.out.println(yard);
				length.add(yard);
				break;
			} // end switch
		} // end while loop

		// After creating an ArrayList with all length objects,
		// determine the smallest length and print it out
		Length smallest = length.get(0);
		for(Length i:length){
				// if the compareTo results in -1,
				// then i is smaller than the current
				// smallest length.
			if(i.compareTo(smallest) < 0){
				smallest = i;
			}
		}
		System.out.println("\nMinimum is "+ smallest);
		
		// Find the largest length and print it out
		Length largest = length.get(0);
		for(Length j:length){
			// if the compareTo results in 1,
			// then i is larger then the current
			// largest length
			if(j.compareTo(largest) > 0){
				largest = j;
			}
		}
		System.out.println("Maximum is "+ largest + "\n");
		
		// Start from the first element, add all elements together
		// print them out as the 4 length subclasses
		Meter firstToLastM = new Meter(0);
		Yard firstToLastY = new Yard(0);
		Foot firstToLastF =  new Foot(0);
		Inch firstToLastI = new Inch(0);
		for(Length k: length){
		firstToLastM.add(k);	
		}
		firstToLastY.add(firstToLastM);
		firstToLastF.add(firstToLastM);
		firstToLastI.add(firstToLastM);
		System.out.println("Sum of Lengths Adding from First to Last");
		System.out.println(firstToLastM);
		System.out.println(firstToLastI);
		System.out.println(firstToLastF);
		System.out.println(firstToLastY);
		
		// Start at the last element, add all the elements together.
		// Print them out as the 4 length subclasses.
		firstToLastM = new Meter(0);
		firstToLastY = new Yard(0);
		firstToLastF =  new Foot(0);
		firstToLastI = new Inch(0);
		for(int i = length.size() -1; i >= 0 ; i--){
		firstToLastM.add(length.get(i));
		}
		firstToLastY.add(firstToLastM);
		firstToLastF.add(firstToLastM);
		firstToLastI.add(firstToLastM);
		System.out.println("\nSum of Lengths Adding from First to Last");
		System.out.println(firstToLastM);
		System.out.println(firstToLastI);
		System.out.println(firstToLastF);
		System.out.println(firstToLastY);
		
		// Close the file
		in.close();
	} // end method main

} // end class Lab4

