package org.memehazard;

import java.util.ArrayList;
import java.util.List;

public class Helper
{
    @SafeVarargs
    public static <T extends Object> List<T> asList(T... objects)
    {
        List<T> list = new ArrayList<T>();

        for (T obj : objects)
            list.add(obj);

        return list;
    }

}
