package com.takashi.mediamanager;


import com.drew.imaging.FileType;
//import com.drew.metadata.Directory;
//import com.drew.metadata.Tag;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileInfo {
    private LocalDateTime dateTaken;
    private Long fileSize;
    private File fileObj;
    private String fileName;
    private Boolean duplicate;
    private Boolean noPictureFile;
    private File duplicateOrgFile;
    private File destination;
    private boolean destFileExist;
    private boolean fileCopied;
    private String nonDateDirName;
    private FileType fileType;
    private String parentDir;

    public FileInfo(){
        dateTaken = LocalDateTime.MIN;
        fileSize = Long.MIN_VALUE;
        fileObj = null;
        fileName = null;
        duplicate = false;
        noPictureFile = false;
        duplicateOrgFile = null;
        destination = null;
        destFileExist = false;
        nonDateDirName = null;
        fileCopied = false;
        fileType = FileType.Unknown;
        parentDir = null;
    }

    public FileInfo(FileInfo fileinfo){
        dateTaken = fileinfo.dateTaken;
        fileSize = fileinfo.fileSize;
        fileObj = fileinfo.fileObj;
        fileName = fileinfo.fileName;
        duplicate = fileinfo.duplicate;
        noPictureFile = fileinfo.noPictureFile;
        duplicateOrgFile = fileinfo.duplicateOrgFile;
        destination = fileinfo.destination;
        destFileExist = fileinfo.destFileExist;
        nonDateDirName = fileinfo.nonDateDirName;
        fileCopied = fileinfo.fileCopied;
        fileType = fileinfo.fileType;
        parentDir = fileinfo.parentDir;
    }

    protected void set(LocalDateTime date){ dateTaken = date;}
    protected void set(Long size){ fileSize = size;}
    protected void set(File file)throws FileInfoException{
        fileObj = file.getAbsoluteFile();
        if(fileObj.getPath().length() > java.lang.Integer.MAX_VALUE)
            throw new FileInfoException(fileObj.getPath().length());
        fileName = fileObj.getName();
        setNonDateDirName();
    }
    protected void setDuplicate(boolean dup){duplicate = dup;}
    protected void setNonPictureFile(boolean dup){noPictureFile = dup;}
    protected void setDuplicateOriginalFile(File file){duplicateOrgFile = file;}
    protected void setDestination(File des){destination = des;}
    protected void setDestFileExist(boolean flag){destFileExist = flag;}
    protected void setFileCopied(boolean flag){fileCopied = flag;}
    protected void setFileType(FileType type){fileType = type;}
    protected void setParentDir(String dirName){parentDir = dirName;}

    public String getFilePath(){return fileObj.getPath();}
    public String getFileName(){return fileName;}
    public File getFileObj(){return fileObj;}
    public LocalDateTime getDateTimeTakenLocalDateTime(){return dateTaken;}
    //public String getDateTimeTaken(){return dateTaken.toString();}
    public LocalDate getDateTaken(){return dateTaken.toLocalDate();}
    public long getFileSize(){ return fileSize.longValue();}
    public Boolean getDuplicate(){ return duplicate; }
    public Boolean getNonPictureFile(){ return noPictureFile; }
    public File getDuplicateOriginalFile(){return duplicateOrgFile;}
    //public File getDestinationFile(){return destination;}
    public Boolean getDestFileExist(){return destFileExist;}
    public String getNonDateDirName(){return nonDateDirName;}
    public Boolean getFileCopied(){return fileCopied;}
    public FileType getFileType(){return fileType;}
    public String getParentDir(){return parentDir;}
    public String getPath(){
        String path = null;
        //String folderName;
        try {
            if (getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag)) {
                if(!duplicate && getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag)) {
                    path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + getFolderName()+"\\"+fileName;
                }else if(!duplicate && !getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag)){
                    path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ getFolderName()+"\\"+fileName;
                }else if(duplicate && getFileType().getMimeType().contains(FileInfoTypes.imageMimeTag)){
                    path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + getFolderName()+"\\"+fileName;
                }else{
                    path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + getFolderName()+"\\"+fileName;
                }
            }
        }catch(NullPointerException e){
            if(!duplicate) {
                path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + getFolderName() + "\\" + fileName;
            }else {
                path = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + getFolderName() + "\\" + fileName;
            }
        }
        return path;
    }
    private String getFolderName(){
        String returnValue;
        if(!dateTaken.toLocalDate().toString().equals(LocalDate.MIN.toString())) {
            returnValue = dateTaken.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }else{
            returnValue = parentDir;
        }
        return returnValue;
    }

    public void checkValues(){
        if(dateTaken == null){
            set(LocalDateTime.MIN);
        }
        if(fileSize == null){
            set(Long.MIN_VALUE);
        }
    }

    public void print(){
        BufferedWriter fileinfofile = null;
        try {
            if(FileInfoTypes.OUTPUT_FILE_INFO_FILE_DEF) {
                FileWriter fileinfofileb = new FileWriter(FileInfoTypes.OUTPUT_FILE_INFO_FILE_NAME, true); //true tells to append data.
                fileinfofile = new BufferedWriter(fileinfofileb);
                fileinfofile.write(fileObj.toPath() + "\r\n");
                fileinfofile.write(fileObj.getParent()+"\\"+fileObj.getName());
                fileinfofile.write("fileName "+fileName);
                fileinfofile.write("File size "+fileSize.toString());
                fileinfofile.write("Date Taken "+dateTaken.toString());
                fileinfofile.write("File Type "+fileType);
                fileinfofile.write("duplicate "+duplicate);
                fileinfofile.write("noPictureFile "+noPictureFile);
                fileinfofile.write("destFileExist "+destFileExist);
                fileinfofile.write("fileCopied "+fileCopied);
                fileinfofile.write("nonDateDirName "+nonDateDirName);

                fileinfofile.write("\r\n");
            }
        }catch(IOException e){
            Utils.errPrint(fileObj.toPath().toString(), e);
        }finally{
            if(FileInfoTypes.OUTPUT_METADATA_FILE_DEF) {
                if (fileinfofile != null) {
                    try {
                        fileinfofile.close();
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj){
        FileInfo fi;
        if(!(obj instanceof FileInfo)){
            return false;
        }else{
            fi=(FileInfo)obj;
            if(this.getDuplicate().equals(fi.getDuplicate())){
                return true;
            }
        }
        return false;
    }
    private void setNonDateDirName(){
        Path path = Paths.get(getFilePath());

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
    }
}
