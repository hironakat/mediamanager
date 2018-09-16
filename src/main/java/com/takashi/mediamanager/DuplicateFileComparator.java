package com.takashi.mediamanager;

import java.util.Comparator;

public class DuplicateFileComparator implements Comparator<DuplicateFiles>{
    @Override
    public int compare(DuplicateFiles p1, DuplicateFiles p2) {
        return p1.getOrginalFile().compareTo(p2.getDuplicateFile());
    }
}
