package usagi.exception;

/**
 * Custom exception class for Usagi chatbot application.
 * 
 * This exception is thrown when errors occur during task processing,
 * file operations, or other application-specific operations.
 */
public class UsagiException extends Exception {
    /**
     * Constructs a UsagiException with the specified message and cause.
     * 
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public UsagiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a UsagiException with the specified message.
     * 
     * @param message The detail message
     */
    public UsagiException(String message) {
        super(message);
    }
}


