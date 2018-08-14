package org.memehazard.wheel.core.persist.neo4j;


@SuppressWarnings("serial")
public class InTransactionException extends RuntimeException
{
    public InTransactionException()
    {
        super();
    }


    public InTransactionException(String message)
    {
        super(message);
    }


    public InTransactionException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public InTransactionException(Throwable cause)
    {
        super(cause);
    }
}