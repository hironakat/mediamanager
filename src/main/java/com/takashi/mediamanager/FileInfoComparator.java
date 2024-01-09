package com.takashi.mediamanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class FileInfoComparator implements Comparator<FileInfo> {
    @Override
    public int compare(FileInfo p1, FileInfo p2) {
        String regex_num = "\\(\\d+\\)";
        return p1.getFileName().replaceFirst(regex_num,"").compareToIgnoreCase(p2.getFileName().replaceFirst(regex_num,""));
    }
}

