package com.takashi.mediamanager;


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
    public String getNonDateDirName(){
        return nonDateDirName;
    }
}
