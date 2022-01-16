/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.authentication;

/**
 * 
 * JWT凭证过期
 *
 */
public class JWTExpirationException extends Exception {

    private static final long serialVersionUID = 753383316255672873L;

    /**
     * Constructs a new JWTExpirationException with {@code null} as its detail
     * message. The cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     */
    public JWTExpirationException() {
	super();
    }

    /**
     * Constructs a new JWTExpirationException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a call
     * to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     */
    public JWTExpirationException(String message) {
	super(message);
    }

    /**
     * Constructs a new JWTExpirationException with the specified detail message and
     * cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this JWTExpirationException's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public JWTExpirationException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * Constructs a new JWTExpirationException with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). This constructor is
     * useful for exceptions that are little more than wrappers for other throwables
     * (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public JWTExpirationException(Throwable cause) {
	super(cause);
    }

    /**
     * Constructs a new JWTExpirationException with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack trace enabled or
     * disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause. (A {@code null} value is permitted, and
     *                           indicates that the cause is nonexistent or
     *                           unknown.)
     * @param enableSuppression  whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     * @since 1.7
     */
    protected JWTExpirationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }
}
