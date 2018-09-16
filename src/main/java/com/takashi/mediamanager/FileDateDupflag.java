package com.takashi.mediamanager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FileDateDupflag {
    private LocalDate date;
    private boolean dupFlag;
    private String nonDateDirName;

    public FileDateDupflag(LocalDate dateInput, boolean dupInput, String dirname){
        date = dateInput;
        dupFlag = dupInput;
        nonDateDirName = dirname;
    }

    public LocalDate getDate(){return date;}
    public boolean getDupFlag(){return dupFlag;}
    /*private void setNonDateDirName(String pathstring){
        Path path = Paths.get(pathstring);

        int i = path.getNameCount();
        Path dir = path.getName(i-2);
        StringBuffer strbuf = new StringBuffer(dir.toString());
        Character ch;
        if(Character.isDigit(strbuf.charAt(1))) {
            for(i = 0; i< strbuf.length(); i++) {
                if(i == 4 || i == 7 || i == 10 || i == 13){
                    ch = strbuf.charAt(i);
                    if (!ch.equals('-')&&Character.isDigit(ch)) {
                        strbuf.insert(i, '-');
                    }
                }
            }
        }
        nonDateDirName = strbuf.toString();
    }*/
    public String getNonDateDirName(){
        return nonDateDirName;
    }
}
