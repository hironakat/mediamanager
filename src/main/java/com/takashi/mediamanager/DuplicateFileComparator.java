package com.takashi.mediamanager;

import java.util.Comparator;

public class DuplicateFileComparator implements Comparator<DuplicateFiles>{
    @Override
    public int compare(DuplicateFiles p1, DuplicateFiles p2) {
        if (p1 == null || p2 == null) {
            return 0;
        }
        if (p1.getOrginalFile() == null) {
            p1.setFiles("orgFile error", "orgFile error");
            return 0;
        }
        if (p2.getDuplicateFile() == null) {
            p2.setFiles("orgFile error", "dupFile error");
            return 0;
        }
        return p1.getOrginalFile().compareToIgnoreCase(p2.getDuplicateFile());
    }
}
