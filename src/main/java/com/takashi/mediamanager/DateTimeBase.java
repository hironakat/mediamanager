package com.takashi.mediamanager;

//import com.drew.imaging.FileType;

import java.time.LocalDateTime;

public abstract class DateTimeBase {
    protected int year = 0;
    protected int month = 0;
    protected int dayOfMonth = 0;
    protected int hour = 0;
    protected int minute = 0;
    protected int second = 0;
    protected String datetime;
    protected String path;
    //private char dateDelimiter ;


    public abstract LocalDateTime stringToDateTime();

    protected char checkDelimiter(String dateTime){
        char delimiter = Character.MIN_VALUE;
        int index0, index1;

        index0 = dateTime.indexOf(':');
        index1 = dateTime.indexOf('/');

        if(index0 == -1 && index1 == -1){
            //System.err.println(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"no delimiter found "+dateTime);
        }else if(index0 == -1){
            delimiter = '/';
        }else if(index1 == -1){
            delimiter = ':';
        }else if(index0>index1){
            delimiter = '/';
        }else if(index0<index1){
            delimiter = ':';
        }
        return delimiter;
    }

}
