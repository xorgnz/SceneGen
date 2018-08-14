/**
 * 
 */
package org.memehazard.wheel.asset.codec;

/**
 * @author xorgnz
 * 
 */
public class CodecException extends Exception
{
    private static final long serialVersionUID = 4755141254383137175L;


    public CodecException(String msg)
    {
        super(msg);
    }


    public CodecException(String msg, Throwable t)
    {
        super(msg, t);
    }
}
