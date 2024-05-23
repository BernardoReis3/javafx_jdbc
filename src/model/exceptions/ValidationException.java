package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	//first String is the field name in form, the second String is the error message
	private Map<String, String> inputErrors = new HashMap<>();
	
	public ValidationException(String message) {
		super(message);
	}
	
	public Map<String, String> getInputErrors() {
		return inputErrors;
	}
	
	public void addError(String field, String message) {
		inputErrors.put(field, message);
	}

}
