package com.takashi.mediamanager;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

public class Main{
    public static void main(String[] args) {
        Utils util1 = new Utils();
        Utils util2 = new Utils();
        Stream<Path> fileList1 = util1.findFiles(FileInfoTypes.Dir1);
        Stream<Path> fileList2 = util2.findFiles(FileInfoTypes.Dir2);

        GetFileInfo getFileInfoObj = new GetFileInfo();
        FileListUtils filelist1 = new FileListUtils();
        FileListUtils filelist2 = new FileListUtils();
        LocalTime start;
        LocalTime finish;

        start = LocalTime.now();

        fileList1.forEach(path -> {
            FileInfo fi = getFileInfoObj.getFileInfo(path.toAbsolutePath().toFile());
            if (fi != null) {
                filelist1.add(fi);
            }
            util1.printProgress("FileList1");
        });
        fileList2.forEach(path -> {
            FileInfo fi = getFileInfoObj.getFileInfo(path.toAbsolutePath().toFile());
            if (fi != null) {
                filelist2.add(fi);
            }
            util2.printProgress("FileList2");
        });
        finish = LocalTime.now();
        System.out.println("FileList " + Duration.between(start, finish).getSeconds() + "sec");


        start = LocalTime.now();

        filelist1.duplicateCheck(filelist2, util2);
        finish = LocalTime.now();
        System.out.println("duplicateCheck " + Duration.between(start, finish).getSeconds() + "sec");

        filelist2.printDup();



    }

}

