package com.takashi.mediamanager;


import java.util.*;


public class FileList {
    protected List<FileInfo> filelist = new ArrayList<FileInfo>();

    public void add(FileInfo fileinfo){
        filelist.add(fileinfo);
    }
}