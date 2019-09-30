package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WarException extends Exception {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = LogManager.getLogger();
	
	public WarException(String errorMessage) {
		super();
		logger.error(errorMessage);
	}

}
