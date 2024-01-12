package com.takashi.mediamanager;

import com.drew.imaging.FileType;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class FileListUtils extends FileList{
    //private FileListDB db;

    public FileListUtils(){
        super();
        //db = new FileListDB();
    }


    public void mkdir (){
        List<FileDateDupflag> datelist = new ArrayList<FileDateDupflag>();
        synchronized (getFileList()) {
            Iterator<FileInfo> fileInfoIterator = getFileList().iterator();
            while (fileInfoIterator.hasNext()) {
                FileInfo fi = fileInfoIterator.next();
                FileDateDupflag fileDateDupflag = new FileDateDupflag(fi);
                datelist.add(fileDateDupflag);
            }
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
        synchronized (getFileList()) {
            Collections.sort(getFileList(), new FileInfoComparator());
            BufferedWriter duplicatefileout = null;
            //List<String> targetFileNameVariation = new ArrayList<String>();

            ListIterator<FileInfo> fileInfoIterator = getFileList().listIterator(0);
            ListIterator<FileInfo> fileInfoIteratorCheck;
            while (fileInfoIterator.hasNext()) {
                FileInfo originalFile = fileInfoIterator.next();
                fileInfoIteratorCheck = getFileList().listIterator(fileInfoIterator.nextIndex());
                fileInfoIteratorCheck.forEachRemaining(targetFile -> {
                    if (originalFile.getFileSize() == targetFile.getFileSize()) {
                        try {
                            if (java.nio.file.Files.mismatch(originalFile.getFileObj().toPath(), targetFile.getFileObj().toPath()) == -1L) {
                                targetFile.setDuplicate(true);
                                targetFile.setDuplicateOriginalFile(originalFile.getFileObj());
                            }
                        } catch (IOException e) {
                            Utils.errPrint(this.getClass().getName() + " " + Thread.currentThread().getStackTrace()[1].getMethodName() + " " + Thread.currentThread().getStackTrace()[1].getLineNumber());
                            Utils.errPrint((originalFile.getFileObj().getPath()));
                            Utils.errPrint((targetFile.getFileObj().getPath()));
                            Utils.errPrint(e);
                        }
                    }
                });

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
            fileInfoIterator = getFileList().listIterator(0);
            while (fileInfoIterator.hasNext()) {
                FileInfo data = fileInfoIterator.next();
                if (data.getDuplicate()) {
                    //System.out.println(data.getFileName() + " " + data.getDuplicate() + " " + data.getNonPictureFile());
                    if (FileInfoTypes.OUTPUT_FILE_DUP_DEF) {
                        try {
                            duplicatefileout.write(data.getFilePath() + " " + data.getDuplicateOriginalFile().getPath() + "\r\n");
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
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
    }

    public void fileCopy()  {
        synchronized (getFileList()) {
            Iterator<FileInfo> fileInfoIterator = getFileList().iterator();
            String dirName;
            File fileOut;
            while (fileInfoIterator.hasNext()) {
                FileInfo originalFile = fileInfoIterator.next();
                try {
                    fileOut = new File(originalFile.getPath());
                } catch (NullPointerException e) {
                    Utils.errPrint(originalFile.getFileObj().getPath(), e);
                    if (originalFile.getFileType().getMimeType() != null) {
                        Utils.errPrint(originalFile.getDuplicate() + " " + originalFile.getFileType().getMimeType() + " " + originalFile.getPath());
                    } else {
                        Utils.errPrint(originalFile.getDuplicate() + " getMimeType NULL");
                    }
                    return;
                }
                if (!fileOut.exists()) {
                    try {
                        FileChannel orgFile = new FileInputStream(originalFile.getFileObj()).getChannel();
                        FileChannel destFile = new FileOutputStream(fileOut).getChannel();

                        orgFile.transferTo(0, orgFile.size(), destFile);
                        originalFile.setDestination(fileOut);
                        originalFile.setFileCopied(true);
                        destFile.close();
                        orgFile.close();
                    } catch (IOException e) {

                        Utils.errPrint(e);
                    }
                } else {
                    originalFile.setDestination(fileOut);
                    originalFile.setDestFileExist(true);
                }
                Utils.printProgress("fileCopy");
            }
        }
    }

    public DuplicateFileList getDuplicate(){
        DuplicateFileList duplicateList = new DuplicateFileList();
        synchronized (getFileList()) {
            ListIterator<FileInfo> fileInfoIterator = getFileList().listIterator(0);
            while (fileInfoIterator.hasNext()) {
                FileInfo data = fileInfoIterator.next();
                if (data.getDuplicate()) {
                    duplicateList.add(data.getDuplicateOriginalFile().getPath(), data.getFilePath());
                }
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
        synchronized (getFileList()) {
            ListIterator<FileInfo> fileInfoIterator = getFileList().listIterator(0);
            //FileListDB db = new FileListDB();
            fileInfoIterator.forEachRemaining(filelist -> {
                System.out.println(filelist.getDateTaken().toString() + " " + filelist.getFileName() + " " + filelist.getDuplicate());
            });
        }
    }

    /*public boolean getFileInfoDBexist(){
        return db.getFileInfoDBexist();
    }*/

    /*public long countNumberOfNonPictureFiles(){
        return filelist.stream().filter(fl -> fl.getNonPictureFile()).count();
    }*/

    public long countNumberOfDuplicateFiles(){
        synchronized (getFileList()) {
            return getFileList().stream().filter(fl -> fl.getDuplicate()).count();
        }
    }

    public long countNumberOfFileCopied(){
        synchronized (getFileList()) {
            return getFileList().stream().filter(fl -> fl.getFileCopied()).count();
        }
    }

    public long countNumberOfDestFileExist(){
        synchronized (getFileList()) {
            return getFileList().stream().filter(fl -> fl.getDestFileExist()).count();
        }
    }

    public long getNumberOfFiles(){
        long numOfFiles = 0L;
        try {
            Stream<Path> fileList = java.nio.file.Files.walk(Paths.get(FileInfoTypes.RootDir)).filter(java.nio.file.Files::isRegularFile);
            numOfFiles = fileList.count();
        } catch(IOException e){
            Utils.errPrint(e);
        }
        return numOfFiles;
    }

    public void handleUnknownFile(){
        synchronized (getFileList()) {
            Iterator<FileInfo> fileInfoIterator = getFileList().iterator();

            while (fileInfoIterator.hasNext()) {
                FileInfo fi = fileInfoIterator.next();
                if (fi.getFileType() == FileType.Unknown) {
                    String unknownFileDir = fi.getFileObj().getParent();
                    String unknownFileName = fi.getFileObj().getName();
                    unknownFileName = unknownFileName.substring(0, unknownFileName.indexOf('.'));
                    if (unknownFileName.contains(" ")) {
                        unknownFileName = unknownFileName.substring(0, unknownFileName.indexOf(' '));
                    }
                    Iterator<FileInfo> fileInfoIterator2 = getFileList().iterator();
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
                    if (fi.getFileType() == FileType.Unknown) {
                        System.err.println("handleUnknownFile no match " + fi.getFileObj().getPath());
                        try {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            fi.setParentDir(df.format(Files.readAttributes(fi.getFileObj().toPath(), BasicFileAttributes.class).lastModifiedTime().toMillis()));
                        } catch (UnsupportedOperationException | IOException e) {
                            Utils.errPrint(e);
                        }
                    }
                }
            }
        }
    }
}
