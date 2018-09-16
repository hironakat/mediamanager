package com.takashi.mediamanager;


import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream<Path> fileList = Utils.findFiles(FileInfoTypes.RootDir);

        GetFileInfo getFileInfoObj = new GetFileInfo();
        FileListUtils filelist = new FileListUtils();
        LocalTime start;
        LocalTime finish;

        start = LocalTime.now();

        if (!filelist.getFileInfoDBexist()) {
            fileList.forEach(path -> {
                FileInfo fi = getFileInfoObj.getFileInfo(path.toAbsolutePath().toFile());
                if (fi != null) {
                    filelist.add(fi);
                }
                Utils.printProgress("FileList");
            });
            finish = LocalTime.now();
            System.out.println("FileList " + Duration.between(start, finish).getSeconds() + "sec");
        }

        start = LocalTime.now();
        if (!filelist.getFileInfoDBexist()){
            filelist.duplicateCheck();
            finish = LocalTime.now();
            System.out.println("duplicateCheck " + Duration.between(start, finish).getSeconds() + "sec");
        }else{
            filelist.getFromDB();
            finish = LocalTime.now();
            System.out.println("duplicateFiles.getFromDB " + Duration.between(start, finish).getSeconds() + "sec");
        }

        start = LocalTime.now();
        //filelist.print();
        filelist.mkdir();
        finish = LocalTime.now();
        System.out.println("mkdir "+ Duration.between(start, finish).getSeconds()+"sec");

        start = LocalTime.now();
        filelist.fileCopy();
        finish = LocalTime.now();
        System.out.println("fileCopy "+ Duration.between(start, finish).getSeconds()+"sec");

        if (!filelist.getFileInfoDBexist()) {
            start = LocalTime.now();
            filelist.setDB();
            finish = LocalTime.now();
            System.out.println("setDB " + Duration.between(start, finish).getSeconds() + "sec");
            start = LocalTime.now();
        }

        ListWindow listWindow = new ListWindow();
        DuplicateFileList duplicateFiles;
        duplicateFiles = filelist.setDuplicate();
        if(!duplicateFiles.isEmpty()) {
            duplicateFiles = duplicateFiles.dupSort();
            listWindow.setList(duplicateFiles);
            finish = LocalTime.now();
            System.out.println("setList " + Duration.between(start, finish).getSeconds() + "sec");
        }
        System.out.println("Total Files " + filelist.getNumberOfFiles() + " Non picture files "+ filelist.countNumberOfNonPictureFiles() + " Duplicate File "+ filelist.countNumberOfDuplicateFiles()+" File Copied "+ filelist.countNumberOfFileCopied());
        listWindow.close();
        filelist.closeDB();
    }

}

