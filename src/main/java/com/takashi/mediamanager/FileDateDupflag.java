package com.takashi.mediamanager;

import com.drew.imaging.FileType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileDateDupflag {
    private LocalDate date;
    private boolean dupFlag;
    private String nonDateDirName;
    private boolean isImage;

    public FileDateDupflag(LocalDate dateInput, boolean dupInput, String dirname, boolean nonPictureVal){
        date = dateInput;
        dupFlag = dupInput;
        nonDateDirName = dirname;
        isImage = !nonPictureVal;
    }

    public FileDateDupflag(FileInfo fi){
        date = fi.getDateTaken();
        dupFlag = fi.getDuplicate();
        nonDateDirName = fi.getNonDateDirName();
        isImage = fi.getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag);
    }

    public LocalDate getDate(){return date;}
    public boolean getDupFlag(){return dupFlag;}
    public String getNonDateDirName(){
        return nonDateDirName;
    }
    public boolean getnonPicture(){return !isImage;}
    public String getDirName(){
        String dirName;
        String folderName;
        if(date.equals(LocalDate.MIN) && !dupFlag && isImage){
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName);
        }else if(date.equals(LocalDate.MIN) && !dupFlag && !isImage){
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName);
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && isImage) {
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + folderName);
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && !isImage){
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + folderName);
        }else if(date.equals(LocalDate.MIN) && dupFlag && isImage){
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName);
        }else if(date.equals(LocalDate.MIN) && dupFlag && !isImage){
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName);
        }else if(!date.equals(LocalDate.MIN) && dupFlag && isImage){
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + folderName);
        }else{
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + folderName);
        }
        return dirName;
    }
}
