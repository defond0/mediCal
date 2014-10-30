package com.example.medical;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by meanheffry on 10/27/14.
 */
public class TimeParser {
    public static HashSet parseDbToSet(String times){
       HashSet set = new HashSet<String>();
       String[] timesArray = times.split("#");
       int n = timesArray.length;
       long l=0;
       for(int i =0; i < n;i++){
           set.add(timesArray[i]);
        }

       return set;
    }
    public static String parseSetToDb(HashSet<String> times){
        String string="";
        Iterator n = times.iterator();
        while (n.hasNext()){
            string=string + n.next() +"#";
        }
        return string;
    }
}


