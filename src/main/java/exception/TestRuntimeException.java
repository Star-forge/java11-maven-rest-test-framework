package exception;

/**
 * @author starforge
 */
public class TestRuntimeException extends RuntimeException {
    public TestRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TestRuntimeException(String msg) {
        super(msg);
    }
}
