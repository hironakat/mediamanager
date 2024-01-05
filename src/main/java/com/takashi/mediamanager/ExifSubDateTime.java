package com.takashi.mediamanager;

import java.time.LocalDateTime;

public class ExifSubDateTime extends DateTimeBase{
    public ExifSubDateTime (String datetimeIn){
        datetime = datetimeIn;
    }
    public LocalDateTime stringToDateTime(){
        int index0, index1;
        char dateDelimiter;

        dateDelimiter = checkDelimiter(datetime);
        try {
            if (dateDelimiter == Character.MIN_VALUE) {
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime + "\n");
            }

            index0 = datetime.indexOf(dateDelimiter);
            year = Integer.parseInt(datetime.substring(0, index0));

            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
            month = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(' ', index0 + 1);
            if (index1 == -1) {
                throw new FileInfoException("EXCEPTION dateTime no delimiter between date and time " + datetime + "\n");
            }
            dayOfMonth = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if (index1 == -1) {
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime + "\n");
            }
            hour = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if (index1 == -1) {
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime + "\n");
            }
            minute = Integer.parseInt(datetime.substring(index0 + 1, index1));
            second = Integer.parseInt(datetime.substring(index1 + 1));

            //System.out.print(year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second + "\n");
        } catch (IndexOutOfBoundsException e) {
            Utils.errPrint("stringToDateTime " + datetime + " " + year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second, e);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Utils.errPrint("stringToDateTime " + datetime, e);
            e.printStackTrace();
        } catch (FileInfoException e) {
            Utils.errPrint("stringToDateTime " + datetime, e);
            e.printStackTrace();
        }
        if (month < 1 || month > 12 ||
                dayOfMonth < 1 || dayOfMonth > 31 ||
                hour < 0 || hour > 23 ||
                minute < 0 || minute > 59 ||
                second < 0 || second > 59) {
            //System.err. print("stringToDateTime "+year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second+"\n");
            year = -999999999;
            month = 1;
            dayOfMonth = 1;
            hour = 0;
            minute = 0;
            second = 0;
        }
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }
}
