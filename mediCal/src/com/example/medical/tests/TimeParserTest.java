package com.example.medical.tests;


import android.test.InstrumentationTestCase;


import com.example.medical.tests.TimeParser;

import java.util.Arrays;

import java.util.HashSet;

/**
 * Created by meanheffry on 10/27/14.
 */


public class TimeParserTest extends InstrumentationTestCase{
    public static final String[] TIME_VALUES = new String[]{"24:12:35","23:00:00","00:00:11"};
    public static final HashSet<String> TIME_SET = new HashSet<String>(Arrays.asList(TIME_VALUES));
    public static final String TIME_STRING = "24:12:35#00:00:11#23:00:00#";

    public void testDbtoSet() throws Exception{
        assertEquals(TIME_SET, TimeParser.parseDbToSet(TIME_STRING));
    }

    public void testSettoDb() throws Exception{
        assertEquals(TIME_STRING, TimeParser.parseSetToDb(TIME_SET));
    }
}
