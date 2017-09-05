package com.example.obscu.gestrepair5;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Obscu on 05/09/2017.
 */

public class DateTime {
    public String DateTime(String DateTime) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String Date = simpleDateFormat.format(new Date());
        return Date;
    }
}
