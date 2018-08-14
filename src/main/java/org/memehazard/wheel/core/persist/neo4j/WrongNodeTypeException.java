package org.memehazard.wheel.core.persist.neo4j;


@SuppressWarnings("serial")
public class WrongNodeTypeException extends RuntimeException
{
    public WrongNodeTypeException()
    {
        super();
    }


    public WrongNodeTypeException(String message)
    {
        super(message);
    }


    public WrongNodeTypeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public WrongNodeTypeException(Throwable cause)
    {
        super(cause);
    }
}