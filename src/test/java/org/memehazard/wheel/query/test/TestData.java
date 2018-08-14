package org.memehazard.wheel.query.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestData
{
    public static final String[] Q_NAMES        = new String[] { "Query 30", "Query 21", "Query 12", "Query 03" };
    public static final String[] Q_DESCRIPTIONS = new String[] { "Desc 1", "Desc 2", "Desc 3", "Desc 4" };
    public static final int[]    Q_QIDS         = new int[] { 295, 291, 292, 293 };
    public static final String[] Q_TAGS         = new String[] { "Tags A", "Tags B", "Tags C", "Tags D" };
    public static final String[] QCL_PARAM_VALS = new String[] {
                                                "args=param_value_0",
                                                "args=param_value_1",
                                                "args=param_value_2",
                                                "args=param_value_3",
                                                "args=param_value_4",
                                                "args=param_value_5", };
    public static final String[] QCL_RESULTS    = new String[] { "Result 1", "Result 2", "Result 3", "Result 4", "Result 5", "Result 6" }; ;
    public static final String[] QP_LABELS      = new String[] { "Param 1", "Param 2", "Param 3", "Param 4" };
    public static final String[] QP_VARIABLES   = new String[] { "param1", "param2", "param3", "param4" };


    public static final Date QCL_RETRIEVED(int id)
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, id);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }


    public static final Set<String> QP_TAGS(int id)
    {
        Set<String> tags = new HashSet<String>();

        tags.add("tag_shared");
        tags.add("tag_" + id);

        return tags;
    }
}
