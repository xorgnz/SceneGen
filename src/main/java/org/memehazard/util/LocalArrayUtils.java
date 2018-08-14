package org.memehazard.util;

import org.apache.commons.lang.StringUtils;

public class LocalArrayUtils
{

    public static int[] parseIntArrayFromString(String string, String separator)
    {
        String[] substrings = StringUtils.split(string, separator);
        int[] ints = new int[substrings.length];
        int i = 0;
        for (String str : substrings)
            ints[i++] = Integer.parseInt(str);

        return ints;
    }


    public static String toString(int[] array, String separator)
    {
        if (separator == null)
            separator = "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
                sb.append(separator);
            sb.append(array[i]);
        }

        return sb.toString();
    }


    public static String toString(int[] array)
    {
        return toString(array, "");
    }
}
