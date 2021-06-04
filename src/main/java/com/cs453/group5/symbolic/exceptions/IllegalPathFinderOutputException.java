package com.cs453.group5.symbolic.exceptions;

/**
 * The IllegalPathFinderOutputException raised when this tool cannot find the
 * proper meaning of the Python Path Finder's output.
 * 
 * Maybe you can check the output format for the path finder when this error
 * raised.
 * 
 * @author Yeongil Yoon
 */
public class IllegalPathFinderOutputException extends RuntimeException {
    public IllegalPathFinderOutputException() {
        super("Python path finder returns invalid output!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
