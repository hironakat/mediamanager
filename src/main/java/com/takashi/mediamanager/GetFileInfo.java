package com.takashi.mediamanager;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.mp4.Mp4Directory;

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

        /*if(!ifPictureFile(file)) {
            returnValue.setNonPictureFile(true);
        }else*/ if(!ifVideoFile(file)){
            returnValue.setNonVideoFile(true);
        }else {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                String givenParam;
                //print(metadata, "Using ImageMetadataReader");
                Iterable<Directory> dir = metadata.getDirectories();

                for (Directory i : dir) {
                    if (FileInfoTypes.Dir_MP4.equals(i.getName())) {
                        givenParam = i.getString(Mp4Directory.TAG_CREATION_TIME);
                        if (givenParam != null) {
                            givenParam = "Sun Feb 17 21:42:14 PST 2019";
                            returnValue.set(stringToDateTimeMP4(givenParam));
                        } else {
                            returnValue.set(stringToDateTime("-999999999:01:01 0:0:0"));
                        }
                        //returnValue.set(stringToDateTime(i.getString(ExifDirectoryBase.TAG_DATETIME)));
                    }
                    /*if (FileInfoTypes.Dir_SubIFD.equals(i.getName())){
                        if(returnValue.getDateTaken().equals(LocalDate.MIN)){
                            givenParam = i.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
                            if (givenParam != null) {
                                returnValue.set(stringToDateTime(givenParam));
                            } else {
                                returnValue.set(stringToDateTime("-999999999:01:01 0:0:0"));
                            }
                        }
                    }*/
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
            } else{
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getName().substring(i+1);
                    if(     extension.equalsIgnoreCase("wmv") ||
                            extension.equalsIgnoreCase("mts") ||
                            extension.equalsIgnoreCase("mpg") ||
                            extension.equalsIgnoreCase("mp4") ||
                            extension.equalsIgnoreCase("mov") ||
                            extension.equalsIgnoreCase("avi") ||
                            extension.equalsIgnoreCase("3g2")) {
                        returnvalue = true;
                    }
                }
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

    private LocalDateTime stringToDateTimeMP4(String datetime) {
        int index0, index1;
        int year = 0, month = 0, dayOfMonth = 0, hour = 0, minute = 0, second = 0;
        char dateDelimiter;
        //System.out.print(datetime.indexOf(':'));
        //dateDelimiter = checkDateDelimiter(datetime);
        dateDelimiter = ' ';
        try{
            if(dateDelimiter ==  Character.MIN_VALUE){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }
            //Sun Feb 17 21:42:14 PST 2019
            index0 = datetime.indexOf(dateDelimiter);
            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
            if(index1 == -1){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }
            month = strToIntMonth(datetime.substring(index0 + 1, index1));
            //year = Integer.parseInt(datetime.substring(0, index0));


            index0 = index1;
            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
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

            index0 = index1;
            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
            if(index1 == -1){
                throw new FileInfoException("EXCEPTION dateTime no delimiter " + datetime+"\n");
            }
            second = Integer.parseInt(datetime.substring(index0 + 1, index1));

            index0 = index1;
            index1 = datetime.indexOf(dateDelimiter, index0 + 1);
            String tmp = datetime.substring(index1 + 1);
            year = Integer.parseInt(datetime.substring(index1 + 1));


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

    private int strToIntMonth(String month){
        int returnvalue = 0;
        switch (month) {
            case "Jan":
                returnvalue = 1;
                break;
            case "Feb":
                returnvalue = 2;
                break;
            case "Mar":
                returnvalue = 3;
                break;
            case "Apr":
                returnvalue = 4;
                break;
            case "May":
                returnvalue = 5;
                break;
            case "Jun":
                returnvalue = 6;
                break;
            case "Jul":
                returnvalue = 7;
                break;
            case "Aug":
                returnvalue = 8;
                break;
            case "Sep":
                returnvalue = 9;
                break;
            case "Oct":
                returnvalue = 10;
                break;
            case "Nov":
                returnvalue = 11;
                break;
            case "Dec":
                returnvalue = 12;
                break;
            default:
                System.out.println("エラー");
        }
        return returnvalue;
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
