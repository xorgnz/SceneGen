package org.memehazard.wheel.game.quiz.model;

public class Question
{
    private long   factId;
    private int    id;
    private String name;
    private double weight;


    public Question(int id, String name, double weight, long factId)
    {
        super();
        this.name = name;
        this.weight = weight;
        this.id = id;
        this.factId = factId;
    }


    public long getFactId()
    {
        return factId;
    }


    public int getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public double getWeight()
    {
        return weight;
    }
}
