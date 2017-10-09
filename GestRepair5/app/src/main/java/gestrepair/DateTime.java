package gestrepair;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Obscu on 05/09/2017.
 */

public class DateTime {
    public String DateTime(String DateTime) {
        Log.d("TAG", DateTime + " Entrance TestDatetime");
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            String inputText = DateTime;
            Date date = inputFormat.parse(inputText);
            String outputText = outputFormat.format(date);
            return outputText;
        } catch (ParseException e) {

            e.printStackTrace();
            Log.d("TAG"," ERRO! TestDatetime");
            return null;
        }

    }
}
