package br.com.r2p.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mauro on 15/01/2017.
 */

public class Data {

    public static String DATE_MASK = "yyyyMMdd";

    public static Date convertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_MASK);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
}
