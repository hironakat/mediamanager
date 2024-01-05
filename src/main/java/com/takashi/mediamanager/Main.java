package com.takashi.mediamanager;


import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream<Path> fileList = Utils.findFiles(FileInfoTypes.RootDir);

        GetFileInfo getFileInfoObj = new GetFileInfo();
        FileListUtils filelistUtils = new FileListUtils();
        LocalTime start;
        LocalTime finish;
        getFileInfoObj.fileCleanUp();

        start = LocalTime.now();
        fileList.forEach(path -> {
            FileInfo fi = getFileInfoObj.getFileInfo(path.toAbsolutePath().toFile());
            if (fi != null) {
                filelistUtils.add(fi);
            }
            Utils.printProgress("FileList");
        });
        filelistUtils.handleUnknownFile();
        finish = LocalTime.now();
        System.out.println("FileList " + Duration.between(start, finish).getSeconds() + "sec");

        start = LocalTime.now();
        filelistUtils.duplicateCheck();
        finish = LocalTime.now();
        System.out.println("duplicateCheck " + Duration.between(start, finish).getSeconds() + "sec");
        finish = LocalTime.now();
        System.out.println("duplicateFiles.getFromDB " + Duration.between(start, finish).getSeconds() + "sec");

        start = LocalTime.now();
        filelistUtils.mkdir();
        finish = LocalTime.now();
        System.out.println("mkdir "+ Duration.between(start, finish).getSeconds()+"sec");

        start = LocalTime.now();
        filelistUtils.fileCopy();
        finish = LocalTime.now();
        System.out.println("fileCopy "+ Duration.between(start, finish).getSeconds()+"sec");

        /*start = LocalTime.now();
        //filelist.setDB();
        finish = LocalTime.now();
        System.out.println("setDB " + Duration.between(start, finish).getSeconds() + "sec");*/

        start = LocalTime.now();
        ListWindow listWindow = new ListWindow();
        DuplicateFileList duplicateFiles;
        duplicateFiles = filelistUtils.getDuplicate();
        if(!duplicateFiles.isEmpty()) {
            duplicateFiles = duplicateFiles.dupSort();
            listWindow.setList(duplicateFiles);
            listWindow.dispList();
            finish = LocalTime.now();
            System.out.println("setList " + Duration.between(start, finish).getSeconds() + "sec");
        }
        System.out.println("Total Files " + filelistUtils.getNumberOfFiles() + /*" Non picture files "+ filelistUtils.countNumberOfNonPictureFiles() +*/ " Duplicate File "+ filelistUtils.countNumberOfDuplicateFiles()+" File Copied "+ filelistUtils.countNumberOfFileCopied()+"Destination File Exist "+filelistUtils.countNumberOfDestFileExist());
        BufferedInputStream bis = null;
        BufferedWriter notpicturefileout = null;
        if(FileInfoTypes.OUTPUT_FILE_COUNT_FILE_DEF) {
            try {
                FileWriter notpicturefile = new FileWriter(FileInfoTypes.OUTPUT_FILE_COUNT_FILE_NAME, true); //true tells to append data.
                notpicturefileout = new BufferedWriter(notpicturefile);
                notpicturefileout.write("Total Files " + filelistUtils.getNumberOfFiles() + /*" Non picture files "+ filelistUtils.countNumberOfNonPictureFiles() +*/ " Duplicate File " + filelistUtils.countNumberOfDuplicateFiles() + " File Copied " + filelistUtils.countNumberOfFileCopied() + " Destination File Exist " + filelistUtils.countNumberOfDestFileExist() + "\r\n");
            } catch (IOException e) {
                Utils.errPrint(e);
            } finally {
                if (notpicturefileout != null) {
                    try {
                        notpicturefileout.close();
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
        //listWindow.close();
    }

}

