package org.memehazard.wheel.query.parser;

public class ParserException extends Exception
{
    private static final long serialVersionUID = -5005104195990210660L;


    public ParserException(String s)
    {
        super(s);
    }


    public ParserException(String s, Exception e)
    {
        super(s, e);
    }
}
