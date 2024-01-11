package com.takashi.mediamanager;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
//import com.drew.metadata.heif.HeifDirectory;
//import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import com.drew.metadata.mp4.Mp4Directory;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GetFileInfo {
    public FileInfo getFileInfo(File file) {

        FileInfo returnValue = new FileInfo();
        try {
            returnValue.set(file);
        } catch(FileInfoException e){
            Utils.errPrint(e);
        }

        BufferedInputStream bis;
        FileType fileType = FileType.Unknown;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            fileType = FileTypeDetector.detectFileType(bis);
        }catch(IOException e){
            Utils.errPrint(file.toPath().toString(), e);
        }
        //System.out.println(file.getPath()+" "+fileType.getName()+" "+fileType.getLongName()+" "+fileType.getMimeType()+" "+fileType.getCommonExtension());
        returnValue.setFileType(fileType);
        returnValue.setNonPictureFile(ifPictureFile(fileType));

        if(fileType != FileType.Unknown){
            returnValue = processFileInfo(file, returnValue);
        }
        returnValue.checkValues();
        returnValue.print();
        return returnValue;
    }
    private FileInfo processFileInfo(File file, FileInfo fileInfoval){
        FileInfo fileInfo = new FileInfo(fileInfoval);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            String[] givenParam = new String[2];
            givenParam[0] = file.getPath();
            Iterable<Directory> dir = metadata.getDirectories();

            for (Directory i : dir) {
                if(!i.isEmpty()) {
                    ExifSubIFDDirectory exifsub = new ExifSubIFDDirectory();
                    if (exifsub.getName().equals(i.getName())) {
                        givenParam[1] = i.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
                        if (givenParam[1]  != null) {
                            ExifSubDateTime exifDateTime = new ExifSubDateTime(givenParam);
                            fileInfo.set(exifDateTime.stringToDateTime());
                        }
                    }
                    Mp4Directory mp4 = new Mp4Directory();
                    if (mp4.getName().equals(i.getName())) {
                        givenParam[1] = i.getString(Mp4Directory.TAG_CREATION_TIME);
                        if (givenParam[1]  != null) {
                            Mp4DateTime mp4DateTime = new Mp4DateTime(givenParam);
                            fileInfo.set(mp4DateTime.stringToDateTime());
                        }
                    }
                    QuickTimeMetadataDirectory qt = new QuickTimeMetadataDirectory();
                    if (qt.getName().equals(i.getName())) {
                        givenParam[1] = i.getString(QuickTimeMetadataDirectory.TAG_CREATION_DATE);
                        if (givenParam[1]  != null) {
                            QtDateTime qtDateTime = new QtDateTime(givenParam);
                            fileInfo.set(qtDateTime.stringToDateTime());
                        }
                    }
                    if (FileInfoTypes.Dir_FILE.equals(i.getName())) {
                        givenParam[1] = i.getString(FileSystemDirectory.TAG_FILE_SIZE);
                        fileInfo.set(Long.valueOf(givenParam[1]));
                        try {
                            fileInfo.set(file);
                        } catch (FileInfoException e) {
                            Utils.errPrint(e);
                        }
                    }
                }
            }
            if(fileInfo.getDateTimeTakenLocalDateTime() == LocalDateTime.MIN){
                //System.err.println("Date Taken is empty "+file.toPath());
                print(metadata, file);
                fileInfo.setParentDir(getParentDirName(file, dir));
            }
            print(metadata, file);
            return fileInfo;
        } catch (ImageProcessingException | IOException e) {
            System.err.println("\n"+file.toPath());
            Utils.errPrint(e);
            return fileInfo;
        }
    }

    /*private FileInfo processUnknownFileInfo(File file, FileInfo fileInfoval){
        FileInfo fileInfo = new FileInfo(fileInfoval);
        return fileInfo;
    }*/
    private boolean ifPictureFile(FileType fileType){
        boolean returnvalue = false;
        try {
            if (fileType.getMimeType().contains(FileInfoTypes.imageMimeTag)) {
                returnvalue = true;
            }else if(fileType == FileType.Arw){
                returnvalue = true;
            }
        }catch(NullPointerException e){
            //returnvalue = false;
        }
        return returnvalue;
    }

    private static void print(Metadata metadata, File file)
    {
        BufferedWriter notpicturefileout = null;
        try {
            if(FileInfoTypes.OUTPUT_METADATA_FILE_DEF) {
                FileWriter notpicturefile = new FileWriter(FileInfoTypes.OUTPUT_METADATA_FILE_NAME, true); //true tells to append data.
                notpicturefileout = new BufferedWriter(notpicturefile);
                notpicturefileout.write(file.toPath() + "\r\n");

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        notpicturefileout.write(tag.toString()+ "\r\n");
                        //System.out.println(tag);
                    }
                    for (String error : directory.getErrors()) {
                        notpicturefileout.write("ERROR: " + error);
                    }
                }
                notpicturefileout.write("\r\n");
            }
        }catch(IOException e){
            Utils.errPrint(file.toPath().toString(), e);
        }finally{
            if(FileInfoTypes.OUTPUT_METADATA_FILE_DEF) {
                if (notpicturefileout != null) {
                    try {
                        notpicturefileout.close();
                    } catch (IOException e) {
                        Utils.errPrint(e);
                    }
                }
            }
        }
    }
    public void fileCleanUp(){
        fileDelete(Paths.get(FileInfoTypes.OUTPUT_METADATA_FILE_NAME));
        fileDelete(Paths.get(FileInfoTypes.OUTPUT_DUPLICATE_FILE_NAME));
        //fileDelete(Paths.get(FileInfoTypes.OUTPUT_FILE_COUNT_FILE_NAME));
        fileDelete(Paths.get(FileInfoTypes.OUTPUT_FILE_INFO_FILE_NAME));
    }
    private void fileDelete(Path path){
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path.getFileName());
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path.getFileName());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }
    private String getParentDirName(File file, Iterable<Directory> dir){
        String returnValue = null;
        String[] givenParam = new String[2];
        givenParam[0] = file.getPath();
        if(file.getParent()!=null){
            returnValue = file.getParentFile().getName();

        }else{
            for (Directory i : dir) {
                if (!i.isEmpty()) {
                    if (FileInfoTypes.Dir_FILE.equals(i.getName())) {
                        givenParam[1] = i.getString(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
                        Mp4DateTime mp4DateTime = new Mp4DateTime(givenParam);
                        returnValue = mp4DateTime.stringToDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                }
            }
        }
        return  returnValue;
    }
}
