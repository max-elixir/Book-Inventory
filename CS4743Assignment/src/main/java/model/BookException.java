package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookException extends Exception {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = LogManager.getLogger();

	public BookException(String string) {
		super(string);
		logger.error(string);
	}

}
