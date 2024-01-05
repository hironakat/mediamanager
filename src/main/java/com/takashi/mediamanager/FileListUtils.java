package com.takashi.mediamanager;

import com.drew.imaging.FileType;
import com.google.common.io.Files;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class FileListUtils extends FileList{
    //private FileListDB db;

    public FileListUtils(){
        super();
        //db = new FileListDB();
    }


    public void mkdir (){
        Iterator<FileInfo> fileInfoIterator = filelist.iterator();
        List<FileDateDupflag> datelist = new ArrayList<FileDateDupflag>();
        while (fileInfoIterator.hasNext()) {
            FileInfo fi = fileInfoIterator.next();
            //if(!fi.getNonPictureFile()) {
                //FileDateDupflag fileDateDupflag = new FileDateDupflag(fi.getDateTaken(), fi.getDuplicate(), fi.getNonDateDirName(),fi.getNonPictureFile());
            FileDateDupflag fileDateDupflag = new FileDateDupflag(fi);
            datelist.add(fileDateDupflag);
            //}
        }
        Set<FileDateDupflag> hs = new HashSet<FileDateDupflag>();
        hs.addAll(datelist);
        datelist.clear();
        datelist.addAll(hs);
        Iterator<FileDateDupflag> dateListIterator = datelist.iterator();
        while (dateListIterator.hasNext()) {
            FileDateDupflag fileDateDupFlag = dateListIterator.next();
            //Utils.mkDateDir(fileDateDupFlag.getDate(), fileDateDupFlag.getDupFlag(), fileDateDupFlag.getNonDateDirName(), fileDateDupFlag.getnonPicture());
            Utils.mkDateDir(fileDateDupFlag);
        }
    }

    public void duplicateCheck() {
        Collections.sort(filelist, new FileInfoComparator());

        BufferedWriter duplicatefileout = null;

        ListIterator<FileInfo> fileInfoIterator = filelist.listIterator(0);
        ListIterator<FileInfo> fileInfoIteratorCheck;
        //List<String> targetFileNameVariation = new ArrayList<String>();

        while (fileInfoIterator.hasNext()) {
            FileInfo originalFile = fileInfoIterator.next();
            //if(!originalFile.getNonPictureFile()) {
                fileInfoIteratorCheck = filelist.listIterator(fileInfoIterator.nextIndex());
                fileInfoIteratorCheck.forEachRemaining(targetFile -> {
                    if(originalFile.getFileSize() == targetFile.getFileSize()){
                        try {
                            if (java.nio.file.Files.mismatch(originalFile.getFileObj().toPath(), targetFile.getFileObj().toPath()) == -1L) {
                                targetFile.setDuplicate(true);
                                targetFile.setDuplicateOriginalFile(originalFile.getFileObj());
                            }
                        } catch (IOException e) {
                            Utils.errPrint(e);
                        }
                    }
                    /*int pos = originalFile.getFileName().indexOf('.');
                    StringBuilder sborg = new StringBuilder(originalFile.getFileName());
                    targetFileNameVariation.add(sborg.insert(pos, " copy").toString());
                    sborg = new StringBuilder(originalFile.getFileName());

                    for (int i = 1; i < 10; i++) {
                        targetFileNameVariation.add(sborg.insert(pos, " (" + String.valueOf(i) + ")").toString());
                        sborg = new StringBuilder(originalFile.getFileName());
                    }

                    if (originalFile.getFileName().equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(0).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(1).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(2).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(3).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(4).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(5).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(6).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(7).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(8).equalsIgnoreCase(targetFile.getFileName()) ||
                            targetFileNameVariation.get(9).equalsIgnoreCase(targetFile.getFileName())) {
                        try {
                            if (Files.equal(originalFile.getFileObj(), targetFile.getFileObj())) {
                                targetFile.setDuplicate(true);
                                targetFile.setDuplicateOriginalFile(originalFile.getFileObj());
                            }
                        } catch (IOException e) {
                            Utils.errPrint(e);
                        }
                    }
                    targetFileNameVariation.clear();*/
                });
            //}
            Utils.printProgress("duplicateCheck");
        }

        if (FileInfoTypes.OUTPUT_FILE_DUP_DEF) {
            try {
                FileWriter duplicateFileOutput = new FileWriter(FileInfoTypes.OUTPUT_DUPLICATE_FILE_NAME, true); //true tells to append data.
                duplicatefileout = new BufferedWriter(duplicateFileOutput);
            } catch (IOException e) {
                Utils.errPrint(e);
            }
        }
        fileInfoIterator = filelist.listIterator(0);
        while (fileInfoIterator.hasNext()) {
            FileInfo data = fileInfoIterator.next();
            if(data.getDuplicate()) {
                //System.out.println(data.getFileName() + " " + data.getDuplicate() + " " + data.getNonPictureFile());
                if (FileInfoTypes.OUTPUT_FILE_DUP_DEF) {
                    try {
                        duplicatefileout.write(data.getFilePath() + " " + data.getDuplicateOriginalFile().getPath() +"\r\n");
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
        if (FileInfoTypes.OUTPUT_FILE_DUP_DEF) {
            if (duplicatefileout != null) {
                try {
                    duplicatefileout.close();
                }catch(IOException e){
                    Utils.errPrint(e);
                }
            }
        }
    }

    public void fileCopy()  {
        Iterator<FileInfo> fileInfoIterator = filelist.iterator();
        String dirName;
        while (fileInfoIterator.hasNext()) {
            FileInfo originalFile = fileInfoIterator.next();
            //if (/*!originalFile.getNonPictureFile() &&*/ !originalFile.getDuplicate()) {
                /*LocalDate ld = originalFile.getDateTaken();
                if(ld.equals(LocalDate.MIN)) {
                    dirName = new String(FileInfoTypes.OutputDir + "\\" + "DateUnknown"+"\\"+originalFile.getNonDateDirName());
                } else {
                    dirName = new String(FileInfoTypes.OutputDir + "\\" + ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }*/

                //File fileOut = new File(dirName+"\\"+originalFile.getFileName());
                File fileOut = new File(originalFile.getPath());
                if(!fileOut.exists()) {
                    try {
                        FileChannel orgFile = new FileInputStream(originalFile.getFileObj()).getChannel();
                        FileChannel destFile = new FileOutputStream(fileOut).getChannel();

                        orgFile.transferTo(0, orgFile.size(), destFile);
                        originalFile.setDestination(fileOut);
                        originalFile.setFileCopied(true);
                        destFile.close();
                        orgFile.close();
                    }catch(IOException e){
                        Utils.errPrint(e);
                    }
                }else{
                    originalFile.setDestination(fileOut);
                    originalFile.setDestFileExist(true);
                }
            //}else if (/*!originalFile.getNonPictureFile() &&*/ originalFile.getDuplicate()) {
                /*LocalDate ld = originalFile.getDateTaken();
                if(ld.equals(LocalDate.MIN)) {
                    dirName = new String(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DuplicateDir + "\\" + "DateUnknown"+"\\"+originalFile.getNonDateDirName());
                } else {
                    dirName = new String(FileInfoTypes.OutputDir + "\\"+FileInfoTypes.DuplicateDir + "\\" + ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }

                File fileOut = new File(dirName+"\\"+originalFile.getFileName());
                if(!fileOut.exists()) {
                    try {
                        FileChannel orgFile = new FileInputStream(originalFile.getFileObj()).getChannel();
                        FileChannel destFile = new FileOutputStream(fileOut).getChannel();

                        orgFile.transferTo(0, orgFile.size(), destFile);

                        destFile.close();
                        orgFile.close();

                        originalFile.setDestination(fileOut);
                        originalFile.setFileCopied(true);
                    }catch(IOException e){
                        Utils.errPrint(e);
                    }
                }else{
                    originalFile.setDestination(fileOut);
                    originalFile.setDestFileExist(true);
                }
            }*/
            Utils.printProgress("fileCopy");
        }
    }

    public DuplicateFileList getDuplicate(){
        DuplicateFileList duplicateList = new DuplicateFileList();
        ListIterator<FileInfo> fileInfoIterator = filelist.listIterator(0);
        while (fileInfoIterator.hasNext()) {
            FileInfo data = fileInfoIterator.next();
            if(data.getDuplicate()) {
                duplicateList.add(data.getDuplicateOriginalFile().getPath(), data.getFilePath());
            }
        }
        return duplicateList;
    }

    /*public void setDB(){
        ListIterator<FileInfo> fileInfoIterator = filelist.listIterator(0);

        fileInfoIterator.forEachRemaining(filelist-> {
            db.InsertRecord(filelist);
        });
        db.resetCounter();
    }*/

    /*public void updateDB(){
        ListIterator<FileInfo> fileInfoIterator = filelist.listIterator(0);

        fileInfoIterator.forEachRemaining(filelist-> {
            db.updateRecord(filelist);
        });
        db.resetCounter();
        db.print();
    }*/

    /*public void getFromDB(){
        //db.print();
        db.getFromDB(filelist);
    }*/

    /*public void closeDB(){
        //db.print();
        db.CloseDB();
    }*/

    public void print(){
        ListIterator<FileInfo> fileInfoIterator = filelist.listIterator(0);
        //FileListDB db = new FileListDB();
        fileInfoIterator.forEachRemaining(filelist-> {
            System.out.println(filelist.getDateTaken().toString()+" "+filelist.getFileName()+" "+filelist.getDuplicate());
        });
    }

    /*public boolean getFileInfoDBexist(){
        return db.getFileInfoDBexist();
    }*/

    /*public long countNumberOfNonPictureFiles(){
        return filelist.stream().filter(fl -> fl.getNonPictureFile()).count();
    }*/

    public long countNumberOfDuplicateFiles(){
        return filelist.stream().filter(fl -> fl.getDuplicate()).count();
    }

    public long countNumberOfFileCopied(){
        return filelist.stream().filter(fl -> fl.getFileCopied()).count();
    }

    public long countNumberOfDestFileExist(){
        return filelist.stream().filter(fl -> fl.getDestFileExist()).count();
    }

    public long getNumberOfFiles(){
        long numOfFiles = 0l;
        try {
            Stream<Path> fileList = java.nio.file.Files.walk(Paths.get(FileInfoTypes.RootDir)).filter(java.nio.file.Files::isRegularFile);
            numOfFiles = fileList.count();
        } catch(IOException e){
            Utils.errPrint(e);
        }
        return numOfFiles;
    }

    public void handleUnknownFile(){
        Iterator<FileInfo> fileInfoIterator = filelist.iterator();

        while (fileInfoIterator.hasNext()) {
            FileInfo fi = fileInfoIterator.next();
            if (fi.getFileType() == FileType.Unknown) {
                String unknownFileDir = fi.getFileObj().getParent();
                String unknownFileName = fi.getFileObj().getName();
                unknownFileName = unknownFileName.substring(0, unknownFileName.indexOf('.'));
                if(unknownFileName.contains(" ")){
                    unknownFileName = unknownFileName.substring(0, unknownFileName.indexOf(' '));
                }
                Iterator<FileInfo> fileInfoIterator2 = filelist.iterator();
                while (fileInfoIterator2.hasNext()) {
                    FileInfo knownFile = fileInfoIterator2.next();
                    if (knownFile.getFileType() != FileType.Unknown &&
                        knownFile.getFileObj().getParent().equalsIgnoreCase(unknownFileDir) &&
                        knownFile.getFileObj().getName().substring(0, knownFile.getFileObj().getName().indexOf('.')).equalsIgnoreCase(unknownFileName) &&
                        !knownFile.getFileObj().getPath().equalsIgnoreCase(fi.getFileObj().getPath())) {
                        //System.err.println("match " + fi.getFileObj().getPath() + " " + knownFile.getFileObj().getPath());
                        fi.set(knownFile.getDateTimeTakenLocalDateTime());
                        fi.setFileType(knownFile.getFileType());
                        fi.setNonPictureFile(knownFile.getNonPictureFile());
                        fi.setFileType(knownFile.getFileType());
                        fi.set(knownFile.getDateTimeTakenLocalDateTime());
                        fi.set(fi.getFileObj().length());
                    }
                }
                if(fi.getFileType() == FileType.Unknown) {
                    System.err.println("handleUnknownFile no match " + fi.getFileObj().getPath());
                }
            }
        }
    }
}
