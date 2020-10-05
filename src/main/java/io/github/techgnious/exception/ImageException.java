/**
 * 
 */
package io.github.techgnious.exception;

/**
 * @author srikanth.anreddy
 *
 */
public class ImageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 843374234004786853L;

	public ImageException() {
		super();
	}

	public ImageException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImageException(String message) {
		super(message);
	}

	public ImageException(Throwable cause) {
		super(cause);
	}
}
