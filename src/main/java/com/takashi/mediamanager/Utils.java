package com.takashi.mediamanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    private final static List<Path> formerPathList = new ArrayList<Path>();
    private static long numOfFiles = -1, fileCount = 1/*, msgLen = 0*/;
    private static String funcName = null;
    private static String rootPath;
    private final static CountWindow cw = new CountWindow();

    public static Stream<Path> findFiles(String path) {
        Stream<Path> fileList = null;
        rootPath = path;

        try {
            fileList = Files.walk(Paths.get(path)).filter(Files::isRegularFile);
        }catch(IOException e){
            errPrint(e);
        }
        return fileList;
    }

    /*public static void mkDateDir(LocalDate date, boolean dupFlag, String nonDateDirName, boolean nonPicture) {
        String folderName;
        String dirName;
        Path imageDupDir, videoDupDir, imageDnDir, videoDnDir;

        imageDupDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir);
        if(!formerPathList.contains(imageDupDir)) {
            mkDateDir(imageDupDir);
            //dupDir.toFile().mkdir();
            formerPathList.add(imageDupDir);
        }
        imageDnDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DateUnknownDir);
        if(!formerPathList.contains(imageDnDir)) {
            mkDateDir(imageDnDir);
            formerPathList.add(imageDnDir);
        }
        videoDupDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir);
        if(!formerPathList.contains(videoDupDir)) {
            mkDateDir(videoDupDir);
            //dupDir.toFile().mkdir();
            formerPathList.add(videoDupDir);
        }
        videoDnDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DateUnknownDir);
        if(!formerPathList.contains(videoDnDir)) {
            mkDateDir(videoDnDir);
            formerPathList.add(videoDnDir);
        }

        if(date.equals(LocalDate.MIN) && !dupFlag && !nonPicture){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName;
        }else if(date.equals(LocalDate.MIN) && !dupFlag && nonPicture){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DateUnknownDir + "\\"+nonDateDirName;
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && !nonPicture) {
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + folderName;
        }else if(!date.equals(LocalDate.MIN) && !dupFlag && nonPicture){
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + folderName;
        }else if(date.equals(LocalDate.MIN) && dupFlag && !nonPicture){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName;
        }else if(date.equals(LocalDate.MIN) && dupFlag && nonPicture){
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir + "\\" + nonDateDirName;
        }else if(!date.equals(LocalDate.MIN) && dupFlag && !nonPicture){
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir + "\\" + folderName;
        }else{
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = FileInfoTypes.OutputDir + "\\" + FileInfoTypes.VideoDir + "\\"+ FileInfoTypes.DuplicateDir + "\\" + folderName;
        }
        Path dir = Paths.get(dirName+"\\");
        mkDateDir(dir);*/
        /*if(!formerPathList.contains(dir)) {
            Iterator<Path> it = dir.iterator();
            String pathName = FileInfoTypes.OutputDir.substring(0, FileInfoTypes.OutputDir.indexOf(':') + 2);

            while (it.hasNext()) {
                pathName = pathName + it.next().toString();
                File currentPath = new File(pathName);
                if (!currentPath.exists()) {
                    currentPath.mkdir();
                    pathName = pathName + "\\";
                } else {
                    pathName = pathName + "\\";
                }
            }
            formerPathList.add(Paths.get(pathName));
        }*/
    //}

    public static void mkDateDir(FileDateDupflag dupFlag) {
        String folderName;
        String dirName;
        Path imageDupDir, videoDupDir, imageDnDir, videoDnDir;

        imageDupDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DuplicateDir);
        if(!formerPathList.contains(imageDupDir)) {
            mkDateDir(imageDupDir);
            //dupDir.toFile().mkdir();
            formerPathList.add(imageDupDir);
        }
        imageDnDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.ImageDir + "\\" + FileInfoTypes.DateUnknownDir);
        if(!formerPathList.contains(imageDnDir)) {
            mkDateDir(imageDnDir);
            formerPathList.add(imageDnDir);
        }
        videoDupDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DuplicateDir);
        if(!formerPathList.contains(videoDupDir)) {
            mkDateDir(videoDupDir);
            //dupDir.toFile().mkdir();
            formerPathList.add(videoDupDir);
        }
        videoDnDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.VideoDir + "\\" + FileInfoTypes.DateUnknownDir);
        if(!formerPathList.contains(videoDnDir)) {
            mkDateDir(videoDnDir);
            formerPathList.add(videoDnDir);
        }

        Path dir = Paths.get(dupFlag.getDirName()+"\\");
        mkDateDir(dir);
    }

    private static void mkDateDir(Path dir){
        if(!formerPathList.contains(dir)) {
            Iterator<Path> it = dir.iterator();
            String pathName = FileInfoTypes.OutputDir.substring(0, FileInfoTypes.OutputDir.indexOf(':') + 2);

            while (it.hasNext()) {
                pathName = pathName + it.next().toString();
                File currentPath = new File(pathName);
                if (!currentPath.exists()) {
                    currentPath.mkdir();
                    pathName = pathName + "\\";
                } else {
                    pathName = pathName + "\\";
                }
            }
            formerPathList.add(Paths.get(pathName));
        }
    }

    public static void errPrint(Exception e){
        System.err.println("EXCEPTION: " + e);
        e.printStackTrace();
    }

    public static void errPrint(String str, Exception e){
        System.err.println("EXCEPTION: " + str + " " + e);
        e.printStackTrace();
    }

    public static void printProgress(String str){
        if(funcName == null){
            funcName = str;
        } else if(!funcName.equalsIgnoreCase(str)){
            fileCount = 1;
            funcName = str;
        }
        if(numOfFiles < 0){
            try {
                Stream<Path> fileList = Files.walk(Paths.get(rootPath)).filter(Files::isRegularFile);
                numOfFiles = fileList.count();
            } catch(IOException e){
                errPrint(e);
            }
        }
        float percentage = (float)fileCount/(float)numOfFiles*(float)100;
        String msg = new String(str+" "+ fileCount +" "+numOfFiles+" "+ String.format ("%.2f", percentage)+"%\n");
        //msgLen = msg.length();
        //System.out.print(msg);
        cw.update(msg);
        fileCount++;
    }

    private static int getNumOfFiles(String dirPath) {
        int count = 0;
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                count++;
                File file = files[i];

                if (file.isDirectory()) {
                    getNumOfFiles(file.getAbsolutePath());
                }
            }
        }
        return count;
    }


}
