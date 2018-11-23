package com.takashi.mediamanager;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.file.FileSystemDirectory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class GetFileInfo {

    public FileInfo getFileInfo(File file) {

        FileInfo returnValue = new FileInfo();
        try {
            returnValue.set(file);
        } catch(FileInfoException e){
            Utils.errPrint(e);
        }

        if(!ifPictureFile(file)) {
            returnValue.setNonPictureFile(true);
        }else if(!ifVideoFile(file)){
            returnValue.setNonVideoFile(true);
        }else {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                String givenParam;
                //print(metadata, "Using ImageMetadataReader");
                Iterable<Directory> dir = metadata.getDirectories();

                for (Directory i : dir) {
                    if (FileInfoTypes.Dir_EXIFIFD0.equals(i.getName())) {
                        givenParam = i.getString(ExifDirectoryBase.TAG_DATETIME);
                        if (givenParam != null) {
                            returnValue.set(stringToDateTime(givenParam));
                        } else {
                            returnValue.set(stringToDateTime("-999999999:01:01 0:0:0"));
                        }
                        //returnValue.set(stringToDateTime(i.getString(ExifDirectoryBase.TAG_DATETIME)));
                    }
                    if (FileInfoTypes.Dir_SubIFD.equals(i.getName())){
                        if(returnValue.getDateTaken().equals(LocalDate.MIN)){
                            givenParam = i.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
                            if (givenParam != null) {
                                returnValue.set(stringToDateTime(givenParam));
                            } else {
                                returnValue.set(stringToDateTime("-999999999:01:01 0:0:0"));
                            }
                        }
                    }
                    if (FileInfoTypes.Dir_FILE.equals(i.getName())) {
                        givenParam = i.getString(FileSystemDirectory.TAG_FILE_SIZE);
                        returnValue.set(Long.valueOf(givenParam));
                        //returnValue.set(Long.valueOf(i.getString(FileSystemDirectory.TAG_FILE_SIZE)));
                        try {
                            returnValue.set(file);
                        } catch (FileInfoException e) {
                            Utils.errPrint(e);
                        }
                    }
                }
            } catch (ImageProcessingException e) {
                Utils.errPrint(e);
                return null;
            } catch (IOException e) {
                Utils.errPrint(e);
                return null;
            }
        }
        returnValue.checkValues();
        //returnValue.print();
        return returnValue;
    }
    private boolean ifPictureFile(File file){
        boolean returnvalue = false;

        BufferedInputStream bis = null;
        BufferedWriter notpicturefileout = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            FileType fileType = FileTypeDetector.detectFileType(bis);
            if (fileType == FileType.Jpeg ||
                fileType == FileType.Tiff ||
                fileType == FileType.Png ||
                fileType == FileType.Bmp ||
                fileType == FileType.Gif ||
                fileType == FileType.Pcx ||
                fileType == FileType.Arw ||
                fileType == FileType.Crw ||
                fileType == FileType.Cr2 ||
                fileType == FileType.Nef ||
                fileType == FileType.Orf ||
                fileType == FileType.Raf ||
                fileType == FileType.Rw2) {
                returnvalue = true;
            }
            if(!returnvalue){
                //System.err.println("not picture file "+file.getPath());
                if(FileInfoTypes.OUTPUT_NON_PIC_FILE_DEF) {
                    FileWriter notpicturefile = new FileWriter(FileInfoTypes.OUTPUT_NON_PIC_FILE_NAME, true); //true tells to append data.
                    notpicturefileout = new BufferedWriter(notpicturefile);
                    notpicturefileout.write(file.getPath() + "\r\n");
                }
            }
        }catch(IOException e){
            Utils.errPrint(file.toPath().toString(), e);
        }finally{
            if(FileInfoTypes.OUTPUT_NON_PIC_FILE_DEF) {
                if (notpicturefileout != null) {
                    try {
                        notpicturefileout.close();
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
        return returnvalue;
    }

    private boolean ifVideoFile(File file){
        boolean returnvalue = false;

        BufferedInputStream bis = null;
        BufferedWriter notvideofileout = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            FileType fileType = FileTypeDetector.detectFileType(bis);
            if (fileType == FileType.Avi ||
                    fileType == FileType.Mov ||
                    fileType == FileType.Mp4 ||
                    fileType == FileType.Asf ||
                    fileType == FileType.Flv ||
                    fileType == FileType.Vob ||
                    fileType == FileType.Arw ) {
                returnvalue = true;
            }

            if(!returnvalue){
                //System.err.println("not picture file "+file.getPath());
                if(FileInfoTypes.OUTPUT_NON_VID_FILE_DEF) {
                    FileWriter notpicturefile = new FileWriter(FileInfoTypes.OUTPUT_NON_VID_FILE_NAME, true); //true tells to append data.
                    notvideofileout = new BufferedWriter(notpicturefile);
                    notvideofileout.write(file.getPath() + "\r\n");
                }
            }
        }catch(IOException e){
            Utils.errPrint(file.toPath().toString(), e);
        }finally{
            if(FileInfoTypes.OUTPUT_NON_VID_FILE_DEF) {
                if (notvideofileout != null) {
                    try {
                        notvideofileout.close();
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
        return returnvalue;
    }

    private LocalDateTime stringToDateTime(String datetime) {
        int index0, index1;
        int year = 0, month = 0, dayOfMonth = 0, hour = 0, minute = 0, second = 0;
        char dateDelimiter;
        //System.out.print(datetime.indexOf(':'));
        dateDelimiter = checkDateDelimiter(datetime);
        try{
            if(dateDelimiter ==  Character.MIN_VALUE){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }

            index0 = datetime.indexOf(dateDelimiter);
            year = Integer.parseInt(datetime.substring(0, index0));

            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
            month = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(' ', index0 + 1);
            if (index1 == -1){
                throw new FileInfoException("EXCEPTION dateTime no delimiter between date and time " + datetime+"\n");
            }
            dayOfMonth = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if(index1 == -1){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }
            hour = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(':', index0 + 1);
            if(index1 == -1){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }
            minute = Integer.parseInt(datetime.substring(index0 + 1, index1));
            second = Integer.parseInt(datetime.substring(index1 + 1));

            //System.out.print(year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second + "\n");
        } catch (IndexOutOfBoundsException e) {
            Utils.errPrint("stringToDateTime "+datetime+" "+year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second, e);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Utils.errPrint("stringToDateTime "+datetime, e);
            e.printStackTrace();
        } catch (FileInfoException e) {
            Utils.errPrint("stringToDateTime "+datetime, e);
            e.printStackTrace();
        }
        if(month<1 || month >12 ||
           dayOfMonth<1 ||dayOfMonth>31||
           hour<0||hour>23||
           minute<0||minute>59||
           second<0||second>59){
            //System.err. print("stringToDateTime "+year + " " + month + " " + dayOfMonth + " " + hour + " " + minute + " " + second+"\n");
            year = -999999999;
            month = 1;
            dayOfMonth = 1;
            hour = 0;
            minute = 0;
            second = 0;

        }
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

    private char checkDateDelimiter(String dateTime){
        char delimiter = Character.MIN_VALUE;
        int index0, index1;

        index0 = dateTime.indexOf(':');
        index1 = dateTime.indexOf('/');

        if(index0 == -1 && index1 == -1){
            System.err.print("no delimiter found "+dateTime);
        }else if(index0 == -1){
            delimiter = '/';
        }else if(index1 == -1){
            delimiter = ':';
        }else if(index0>index1){
            delimiter = '/';
        }else if(index0<index1){
            delimiter = ':';
        }
        return delimiter;
    }
}
