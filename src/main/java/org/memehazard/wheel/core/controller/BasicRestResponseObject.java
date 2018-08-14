package org.memehazard.wheel.core.controller;

public class BasicRestResponseObject
{
    public String  message;
    public boolean success;


    public BasicRestResponseObject(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }


    public String getMessage()
    {
        return message;
    }


    public boolean isSuccess()
    {
        return success;
    }
}
