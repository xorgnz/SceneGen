package org.memehazard.math.geometry;

public class Face3
{
    public int idx0;
    public int idx1;
    public int idx2;


    public Face3(int idx0, int idx1, int idx2)
    {
        this.idx0 = idx0;
        this.idx1 = idx1;
        this.idx2 = idx2;
    }


    @Override
    public String toString()
    {
        return idx0 + " " + idx1 + " " + idx2;
    }
}