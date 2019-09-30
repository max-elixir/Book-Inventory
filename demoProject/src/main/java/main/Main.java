package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	public static void main(String[] args) {
		Logger logger = LogManager.getLogger();
		System.out.println("sup nerds, this is a maven project or something.");
		
		logger.error("hello error");
		logger.info("hello debug");
	}

}
