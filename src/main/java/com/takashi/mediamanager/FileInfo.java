package com.takashi.mediamanager;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FileInfo {
    private LocalDateTime dateTaken;
    private Long fileSize;
    private File fileObj;
    private String fileName;
    private Boolean duplicate;
    private Boolean noPictureFile;
    private Boolean noVideoFile;
    private File duplicateOrgFile;
    private File destination;
    private boolean destFileExist;
    private boolean fileCopied;
    private String nonDateDirName;
    private boolean thm;

    public FileInfo(){
        dateTaken = LocalDateTime.MIN;
        fileSize = Long.MIN_VALUE;
        fileObj = null;
        fileName = null;
        duplicate = false;
        noPictureFile = false;
        noVideoFile = false;
        duplicateOrgFile = null;
        destination = null;
        destFileExist = false;
        nonDateDirName = null;
        fileCopied = false;
        thm = false;
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
    protected void setNonVideoFile(boolean dup){noVideoFile = dup;}
    protected void setDuplicateOriginalFile(File file){duplicateOrgFile = file;}
    protected void setDestination(File des){destination = des;}
    protected void setDestFileExist(boolean flag){destFileExist = flag;}
    protected void setFileCopied(boolean flag){fileCopied = flag;}
    protected void setThm(boolean flag){thm = flag;}

    public String getFilePath(){return fileObj.getPath();}
    public String getFileName(){return fileName;}
    public File getFileObj(){return fileObj;}
    public String getDateTimeTaken(){return dateTaken.toString();}
    public LocalDate getDateTaken(){return dateTaken.toLocalDate();}
    public LocalDateTime getDateTimeTakenLTD(){return dateTaken;}
    public long getFileSize(){ return fileSize.longValue();}
    public Boolean getDuplicate(){ return duplicate; }
    public Boolean getNonPictureFile(){ return noPictureFile; }
    public Boolean getNonVideoFile(){ return noVideoFile; }
    public File getDuplicateOriginalFile(){return duplicateOrgFile;}
    public File getDestinationFile(){return destination;}
    public Boolean getDestFileExist(){return destFileExist;}
    public String getNonDateDirName(){return nonDateDirName;}
    public Boolean getFileCopied(){return fileCopied;}
    public Boolean getThm(){return thm;}

    public void checkValues(){
        if(dateTaken == null){
            set(LocalDateTime.MIN);
        }
        if(fileSize == null){
            set(Long.MIN_VALUE);
        }
    }

    public void print()
    {
        System.out.println("Directory name "+fileObj.getParent());
        System.out.println("File name "+fileObj.getName());
        System.out.println("File size "+fileSize.toString());
        System.out.println("Date Taken "+dateTaken.toString());
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
