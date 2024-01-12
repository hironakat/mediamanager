package com.takashi.mediamanager;


import java.util.*;


public class FileList {
    private final List<FileInfo> filelist = Collections.synchronizedList(new ArrayList<FileInfo>());

    public void add(FileInfo fileinfo){
        synchronized(filelist){
            filelist.add(fileinfo);
        }
    }

    public List<FileInfo> getFileList() { return filelist; }

}