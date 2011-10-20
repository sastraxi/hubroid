/**
 * 
 */
package net.idlesoft.android.apps.github;

/**
 * ProgrammerException
 *
 * Thrown when the programmer is doing something he shouldn't.
 * This is more or less to catch my silly errors. :)
 */
public class ProgrammerException extends RuntimeException {

    /**
     * Generated serial version uid
     */
    private static final long serialVersionUID = -712339499261901563L;

    public ProgrammerException() {
        super("The programmer is doing something he shouldn't!");
    }

    public ProgrammerException(final String msg) {
        super(msg);
    }
}
