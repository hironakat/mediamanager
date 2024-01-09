package com.takashi.mediamanager;

import java.time.LocalDateTime;

public class QtDateTime extends DateTimeBase{
    public QtDateTime (String[] datetimeIn){
        path = datetimeIn[0];
        datetime = datetimeIn[1];
    }
    public LocalDateTime stringToDateTime(){
        int index0, index1;
        char dateDelimiter;

        dateDelimiter = checkDelimiter(datetime);
        try {
            if (dateDelimiter == Character.MIN_VALUE) {
                throw new DateDelimiterException(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"EXCEPTION dateTime no delimiter " + datetime + "\n");
            }

            index0 = datetime.indexOf('-');
            year = Integer.parseInt(datetime.substring(0, index0));

            index1 = datetime.indexOf('-', index0 + 1);
            month = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf('T', index0 + 1);
            if (index1 == -1) {
                throw new DateDelimiterException(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime7 EXCEPTION dateTime no delimiter between date and time " + datetime + "\n");
            }
            dayOfMonth = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if (index1 == -1) {
                throw new DateDelimiterException(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime6 EXCEPTION dateTime no delimiter " + datetime);
            }
            hour = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if (index1 == -1) {
                throw new DateDelimiterException(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime5 EXCEPTION dateTime no delimiter " + datetime);
            }
            minute = Integer.parseInt(datetime.substring(index0 + 1, index1));
            index0 = index1;
            index1 = datetime.indexOf('-', index0 + 1);
            if (index1 == -1) {
                throw new DateDelimiterException(this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime4 EXCEPTION dateTime no delimiter " + datetime);
            }
            second = Integer.parseInt(datetime.substring(index0 + 1, index1));

            //System.out.print(year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second + "\n");
        } catch (IndexOutOfBoundsException e) {
            Utils.errPrint(path+" "+this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime1 " + datetime + " " + year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second);
            //e.printStackTrace();
        } catch (NumberFormatException e) {
            Utils.errPrint(path+" "+this.getClass().getName()+" "+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+Thread.currentThread().getStackTrace()[1].getLineNumber()+" "+"stringToDateTime2 " + datetime );
            //e.printStackTrace();
        } catch (DateDelimiterException e) {
            Utils.errPrint(path+" "+e);
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
