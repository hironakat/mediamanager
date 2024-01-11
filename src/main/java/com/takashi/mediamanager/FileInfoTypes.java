package com.takashi.mediamanager;

public class FileInfoTypes {
    //final static String Dir_EXIFIFD0 = "Exif IFD0";
    //final static String Dir_SubIFD = "Exif SubIFD";
   // final static String fileInfoUri = "jdbc:derby:D:\\Users\\Takashi\\IdeaProjects\\mediamanager\\picturefiledb;create=true";
    //final static String fileInfoTableName = "fileinfo";
    //final static String versionTableName = "version";
    //final static String queryFileinfo = "SELECT * FROM fileinfo";
    //final static String querydbVer = "SELECT ver FROM version WHERE id = 0";
    //final static String queryDupDBinfo = "SELECT * FROM dupinfo";

    final static String imageMimeTag = "image/";
    final static String Dir_FILE = "File";

    //final static String Dir1 = "D:\\Public Pictures";
    final static String Dir1 = "D:\\Public Pictures";
    final static String Dir2 = "D:\\phone";  //This will be output to dup.txt
    final static String OutputDir = "D:\\Users\\Takashi\\IdeaProjects\\mediamanager\\Output";
    final static String DuplicateDir = "duplicate";
    final static String VideoDir = "Public Videos";
    final static String ImageDir = "Public Pictures";
    final static String DateUnknownDir = "DateUnknown";

    //final static Integer dbver = 1;

    public final static boolean OUTPUT_FILE_DUP_DEF = true;
    public final static String OUTPUT_DUPLICATE_FILE_NAME = "D:\\DuplicateFile.txt";
    public final static boolean OUTPUT_METADATA_FILE_DEF = true;
    public final static String OUTPUT_METADATA_FILE_NAME = "D:\\metadata.txt";
    //public final static boolean OUTPUT_FILE_COUNT_FILE_DEF = false;
    //public final static String OUTPUT_FILE_COUNT_FILE_NAME = "D:\\filecount.txt";
    public final static boolean OUTPUT_FILE_INFO_FILE_DEF = true;
    public final static String OUTPUT_FILE_INFO_FILE_NAME = "D:\\fileinfo.txt";
}
