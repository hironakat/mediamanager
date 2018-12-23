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
    private  List<Path> formerPathList = new ArrayList<Path>();
    private  long numOfFiles = -1, fileCount = 0/*, msgLen = 0*/;
    private  String funcName = null;
    private  String rootPath;
    private  static CountWindow cw = new CountWindow();

    public  Stream<Path> findFiles(String path) {
        Stream<Path> fileList = null;
        rootPath = path;

        try {
            fileList = Files.walk(Paths.get(path)).filter(Files::isRegularFile);
        }catch(IOException e){
            errPrint(e);
        }
        return fileList;
    }

    public  void mkDateDir(LocalDate date, boolean dupFlag, String nonDateDirName) {
        String folderName;
        String dirName;
        Path dupDir, dnDir;

        dupDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DuplicateDir);
        if(!formerPathList.contains(dupDir)) {
            mkDateDir(dupDir);
            //dupDir.toFile().mkdir();
            formerPathList.add(dupDir);
        }
        dnDir = Paths.get(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DateUnknownDir);
        if(!formerPathList.contains(dnDir)) {
            mkDateDir(dnDir);
            formerPathList.add(dnDir);
        }

        if(date.equals(LocalDate.MIN)&&!dupFlag){
            dirName = new String(FileInfoTypes.OutputDir + "\\" + FileInfoTypes.DateUnknownDir+"\\"+nonDateDirName);
        }else if(!date.equals(LocalDate.MIN) && !dupFlag) {
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\" + folderName);
        }else if(date.equals(LocalDate.MIN)&&dupFlag){
            dirName = new String(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DuplicateDir + "\\" + FileInfoTypes.DateUnknownDir+"\\"+nonDateDirName);
        }else /*if(!date.equals(LocalDate.MIN) && dupFlag)*/{
            folderName = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dirName = new String(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DuplicateDir + "\\" + folderName);
        }
        Path dir = Paths.get(dirName+"\\");
        mkDateDir(dir);
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
    }

    private  void mkDateDir(Path dir){
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

    public  void printProgress(String str){
        if(funcName == null){
            funcName = str;
        } else if(!funcName.equalsIgnoreCase(str)){
            fileCount = 0;
            funcName = str;
        }
        if( numOfFiles < 0){
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

    private  int getNumOfFiles(String dirPath) {
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
