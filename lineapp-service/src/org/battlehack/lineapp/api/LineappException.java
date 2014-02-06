package org.battlehack.lineapp.api;

public class LineappException extends Exception {
	private static final long serialVersionUID = 1L;
	
    private final Error error;

	public LineappException(Error error) {
        super(error.toString());
        this.error = error;
    }

    public LineappException(Error error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
