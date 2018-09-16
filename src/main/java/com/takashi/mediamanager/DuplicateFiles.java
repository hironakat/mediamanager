package com.takashi.mediamanager;


public class DuplicateFiles {
    private String orginalFile;
    private String duplicateFile;
    private Boolean deleteFlag;

    public DuplicateFiles(String orgFile, String dupFile){
        orginalFile = orgFile;
        duplicateFile = dupFile;
        deleteFlag = false;
    }

    public void setFiles(String orgFile, String dupFile){
        orginalFile = orgFile;
        duplicateFile = dupFile;
    }

    public void setDeletFlag(boolean flag){
        deleteFlag = flag;
    }

    public String getOrginalFile(){
        return orginalFile;
    }

    public String getDuplicateFile(){
        return duplicateFile;
    }

    public Boolean getDeleteFlag(){
        return deleteFlag;
    }
}
