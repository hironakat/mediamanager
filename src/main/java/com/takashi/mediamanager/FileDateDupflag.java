package com.takashi.mediamanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileDateDupflag {
    private final LocalDate date;
    private final boolean dupFlag;
    private final String nonDateDirName;
    private final boolean isImage;
    private String parentDir;

    /*public FileDateDupflag(LocalDate dateInput, boolean dupInput, String dirname, boolean nonPictureVal){
        date = dateInput;
        dupFlag = dupInput;
        nonDateDirName = dirname;
        isImage = !nonPictureVal;
    }*/

    public FileDateDupflag(FileInfo fi){
        date = fi.getDateTaken();
        dupFlag = fi.getDuplicate();
        nonDateDirName = fi.getNonDateDirName();
        isImage = fi.getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag);
        parentDir = fi.getParentDir();
    }

    public LocalDate getDate(){return date;}
    //public boolean getDupFlag(){return dupFlag;}
    /*public String getNonDateDirName(){
        return nonDateDirName;
    }*/
    //public boolean getnonPicture(){return !isImage;}
    public String getDirName(){
        String dirName;
        if(!dupFlag && isImage) {
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + getFolderName();
        }else if(!dupFlag && !isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + getFolderName();
        }else if(dupFlag && isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + getFolderName();
        }else{
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + getFolderName();
        }

        /*if(date.equals(LocalDate.MIN) && !dupFlag && isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName;
        }else if(date.equals(LocalDate.MIN) && !dupFlag && !isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName;
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && isImage) {
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + getFolderName();
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && !isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + getFolderName();
        }else if(date.equals(LocalDate.MIN) && dupFlag && isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName;
        }else if(date.equals(LocalDate.MIN) && dupFlag && !isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName;
        }else if(!date.equals(LocalDate.MIN) && dupFlag && isImage){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + getFolderName();
        }else{
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + getFolderName();
        }*/
        return dirName;
    }
    private String getFolderName(){
        String returnValue = null;
        if(!date.toString().equals(LocalDate.MIN.toString())) {
            returnValue = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else{
            returnValue = parentDir;
        }

        return returnValue;
    }
}
