package com.takashi.mediamanager;

public class FileInfoTypes {
    final static String Dir_EXIFIFD0 = "Exif IFD0";
    final static String Dir_SubIFD = "Exif SubIFD";
    final static String fileInfoUri = "jdbc:derby:D:\\Users\\Takashi\\IdeaProjects\\mediamanager\\picturefiledb;create=true";
    final static String fileInfoTableName = "fileinfo";
    final static String versionTableName = "version";
    final static String queryFileinfo = "SELECT * FROM fileinfo";
    final static String querydbVer = "SELECT ver FROM version WHERE id = 0";
    final static String queryDupDBinfo = "SELECT * FROM dupinfo";

    final static String Dir_FILE = "File";

    final static String RootDir = "D:\\phone";
    final static String OutputDir = "D:\\Output";
    final static String DuplicateDir = "duplicate";
    final static String DateUnknownDir = "DateUnknown";

    final static Integer dbver = 1;

    public final static boolean OUTPUT_FILE_DUP_DEF = false;
    public final static String OUTPUT_DUPLICATE_FILE_NAME = "D:\\DuplicateFile.txt";
    public final static boolean OUTPUT_NON_PIC_FILE_DEF = false;
    public final static String OUTPUT_NON_PIC_FILE_NAME = "D:\\nonPictureFile.txt";
}
