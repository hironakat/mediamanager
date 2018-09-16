package com.takashi.mediamanager;

public class FileInfoException extends Exception {
    private static final long serialVersionUID = 1L;
    FileInfoException (long len){
        super("File name length is " + Long.toString(len) + "\n");
    }
    FileInfoException (String str){
        super(str);
    }
}
