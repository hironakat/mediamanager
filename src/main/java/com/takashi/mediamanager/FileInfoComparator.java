package com.takashi.mediamanager;

import java.util.Comparator;


public class FileInfoComparator implements Comparator<FileInfo> {
    @Override
    public int compare(FileInfo p1, FileInfo p2) {
        String regex_num = "\\(\\d+\\)";
        return p1.getFileName().replaceFirst(regex_num,"").compareToIgnoreCase(p2.getFileName().replaceFirst(regex_num,""));
    }
}

