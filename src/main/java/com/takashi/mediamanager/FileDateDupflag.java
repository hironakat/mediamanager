package com.takashi.mediamanager;

import com.drew.imaging.FileType;

import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileDateDupflag {
    private final LocalDate date;
    private final boolean dupFlag;
    private final String nonDateDirName;
    private final boolean isImage;
    private final String parentDir;


    public FileDateDupflag(FileInfo fi){
        date = fi.getDateTaken();
        dupFlag = fi.getDuplicate();
        nonDateDirName = fi.getNonDateDirName();
        if(fi.getFileType().getMimeType()!=null){
            isImage = fi.getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag);
        }else if(fi.getFileType() == FileType.Arw) {
            isImage = true;
        }else{
            isImage = false;
        }
        parentDir = fi.getParentDir();
    }

    //public LocalDate getDate(){return date;}
    //public boolean getDupFlag(){return dupFlag;}
    /*public String getNonDateDirName(){
        return nonDateDirName;
    }*/
    //public boolean getnonPicture(){return !isImage;}
    public String getDirName(){
        String dirName;
        if(isImage){
            if(!dupFlag) {
                dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + getFolderName();
            }else{
                dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + getFolderName();
            }
        }else{
            if(!dupFlag){
                dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + getFolderName();
            }else{
                dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + getFolderName();
            }
        }

        return dirName;
    }
    private String getFolderName(){
        String returnValue;
        if(!date.toString().equals(LocalDate.MIN.toString())) {
            returnValue = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else{
            returnValue = parentDir;
        }

        return returnValue;
    }
}
