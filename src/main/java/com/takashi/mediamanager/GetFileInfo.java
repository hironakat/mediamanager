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
import com.drew.metadata.heif.HeifDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mp4.Mp4Directory;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;


public class GetFileInfo {
    public FileInfo getFileInfo(File file) {

        FileInfo returnValue = new FileInfo();
        try {
            returnValue.set(file);
        } catch(FileInfoException e){
            Utils.errPrint(e);
        }

        BufferedInputStream bis = null;
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
            String givenParam;

            Iterable<Directory> dir = metadata.getDirectories();

            for (Directory i : dir) {
                if(!i.isEmpty()) {
                    ExifSubIFDDirectory exifsub = new ExifSubIFDDirectory();
                    if (exifsub.getName().equals(i.getName())) {
                        givenParam = i.getString(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
                        if (givenParam != null) {
                            ExifSubDateTime exifDateTime = new ExifSubDateTime(givenParam);
                            fileInfo.set(exifDateTime.stringToDateTime());
                        }
                    }
                    Mp4Directory mp4 = new Mp4Directory();
                    if (mp4.getName().equals(i.getName())) {
                        givenParam = i.getString(Mp4Directory.TAG_CREATION_TIME);
                        if (givenParam != null) {
                            Mp4DateTime mp4DateTime = new Mp4DateTime(givenParam);
                            fileInfo.set(mp4DateTime.stringToDateTime());
                        }
                    }
                    QuickTimeDirectory qt = new QuickTimeDirectory();
                    if (qt.getName().equals(i.getName())) {
                        givenParam = i.getString(QuickTimeDirectory.TAG_CREATION_TIME);
                        if (givenParam != null) {
                            Mp4DateTime mp4DateTime = new Mp4DateTime(givenParam);
                            fileInfo.set(mp4DateTime.stringToDateTime());
                        }
                    }
                    if (FileInfoTypes.Dir_FILE.equals(i.getName())) {
                        givenParam = i.getString(FileSystemDirectory.TAG_FILE_SIZE);
                        fileInfo.set(Long.valueOf(givenParam));
                        try {
                            fileInfo.set(file);
                        } catch (FileInfoException e) {
                            Utils.errPrint(e);
                        }
                    }
                }
            }
            if(fileInfo.getDateTimeTakenLocalDateTime() == LocalDateTime.MIN){
                System.err.println("Date Taken is empty "+file.toPath().toString());
                for (Directory i : dir) {
                    if (!i.isEmpty()) {
                        if (FileInfoTypes.Dir_FILE.equals(i.getName())) {
                            givenParam = i.getString(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
                            Mp4DateTime mp4DateTime = new Mp4DateTime(givenParam);
                            fileInfo.set(mp4DateTime.stringToDateTime());
                            try {
                                fileInfo.set(file);
                            } catch (FileInfoException e) {
                                Utils.errPrint(e);
                            }
                        }
                    }
                }
            }
            print(metadata, file);
            return fileInfo;
        } catch (ImageProcessingException e) {
            System.err.println("\n"+file.toPath().toString());
            Utils.errPrint(e);
            return fileInfo;
        } catch (IOException e) {
            System.err.println("\n"+file.toPath().toString());
            Utils.errPrint(e);
            return fileInfo;
        }
    }

    private FileInfo processUnknownFileInfo(File file, FileInfo fileInfoval){
        FileInfo fileInfo = new FileInfo(fileInfoval);
        return fileInfo;
    }
    private boolean ifPictureFile(FileType fileType){
        boolean returnvalue = false;
        try {
            if (fileType.getMimeType().contains(FileInfoTypes.imageMimeTag)) {
                returnvalue = true;
            }
        }catch(NullPointerException e){
            //System.err.println("\n"+fileType.getName());
            //Utils.errPrint(e);
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
                        /*notpicturefileout.write("getDirectoryName\r\n"+tag.getDirectoryName()+ "\r\n");
                        notpicturefileout.write("getTagName\r\n"+tag.getTagName()+ "\r\n");
                        notpicturefileout.write("getDescription\r\n"+tag.getDescription()+ "\r\n");*/

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
        fileDelete(Paths.get(FileInfoTypes.OUTPUT_FILE_COUNT_FILE_NAME));
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
}
