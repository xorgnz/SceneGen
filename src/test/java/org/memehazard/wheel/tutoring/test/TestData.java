package org.memehazard.wheel.tutoring.test;

import java.util.Calendar;

public class TestData
{

    public static final String    C_CREATOR_NAME          = "Creator Name";
    public static final String    C_DESCRIPTION           = "Description";
    public static final String[]  C_NAME                  = { "Name 3", "Name 2", "Name 1" };

    public static final String    CI_DESCRIPTION          = "Description";
    public static final String[]  CI_NAME                 = { "Name 3", "Name 2", "Name 1" };
    public static final double[]  CI_WEIGHT               = { 1, 2, 3, 4 };

    public static final Integer[] EK_FMA_ID               = { 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009 };
    public static final String[]  EK_FMA_LABEL            = { "ENTITY 1", "ENTITY 2", "ENTITY 3", "ENTITY 4", "ENTITY 5", "ENTITY 6", "ENTITY 7", "ENTITY 8",
                                                          "ENTITY 9" };

    public static final long[]    ITSEV_CURRICULUM_ID     = { 1, 2, 3, 4, 5, 6 };
    public static final String[]  ITSEV_DESCRIPTION       = { "desc 0", "desc 1", "desc 2", "desc 3", "desc 4", "desc 5" };
    public static final long[]    ITSEV_OBJECT_ENTITY_ID  = { 100, 101, 102, 103, 104, 105 };
    public static final double[]  ITSEV_P_ASSERTION       = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6 };
    public static final String[]  ITSEV_RELATION          = { "rel0", "rel1", "rel2", "rel3", "rel4", "rel5" };
    public static final String[]  ITSEV_SOURCE            = { "source0", "source1", "source2", "source3", "source4", "source5" };
    public static final long[]    ITSEV_SUBJECT_ENTITY_ID = { 200, 201, 202, 203, 204, 205 };
    public static final int[]     ITSEV_TIMESTAMP_MINUTE  = { 0, 1, 2, 3, 4, 5, 6 };
    public static final String[]  ITSEV_VALUE             = { "val0", "val1", "val2", "val3", "val4", "val5" };

    public static final long[]    ITSS_DOMAIN_ID          = { 1, 2, 3, 4, 1, 2 };
    public static final double[]  ITSS_P_VAL              = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6 };
    public static final long[]    ITSS_STUDENT_ID         = { 1, 1, 1, 1, 2, 2 };
    public static final int[]     ITSS_TIMESTAMP_MINUTE   = { 1, 2, 3, 4, 5, 6 };

    public static final String[]  RK_NAME                 = { "relation0", "relation1", "relation2", "relation3" };
    public static final String    RK_NAMESPACE            = "test";
    public static final String[]  RK_STRING               = { "test:relation0", "test:relation1", "test:relation2", "test:relation3" };

    public static final String[]  USER_EMAIL              = new String[] { "test0@test.org", "test1@test.org", "test2@test.org", "test3@test.org",
                                                          "test4@test.org" };
    public static final String[]  USER_FNAME              = new String[] { "First0", "First1", "First2", "First3", "First4" };
    public static final long[]    USER_ID                 = { 1, 2, 3, 4, 5, 6 };
    public static final String[]  USER_LNAME              = new String[] { "Last0", "Last1", "Last2", "Last3", "Last4" };
    public static final String    USER_PASSWORD           = "abcd";
    public static final String[]  USER_USERNAME           = new String[] { "username0", "username1", "username2", "username3", "username4" };


    public static final Calendar ITSEV_TIMESTAMPS(int i)
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2001);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, ITSS_TIMESTAMP_MINUTE[i]);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }


    public static final Calendar ITSS_TIMESTAMPS(int i)
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2000);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, ITSS_TIMESTAMP_MINUTE[i]);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

}
