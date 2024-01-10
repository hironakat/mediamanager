package com.takashi.mediamanager;

import java.util.Comparator;

public class DuplicateFileComparator implements Comparator<DuplicateFiles>{
    @Override
    public int compare(DuplicateFiles p1, DuplicateFiles p2) {
        if (p1 == null) {
            p1 = new DuplicateFiles("orgFile error","orgFile error");
        }else if(p2 == null){
            p1 = new DuplicateFiles("dupFile error","dupFile error");
        }else if(p1.getOrginalFile() == null){
            p1.setFiles("orgFile error","orgFile error");
        }else if(p2.getDuplicateFile() == null){
            p2.setFiles("orgFile error","dupFile error");
        }
        return p1.getOrginalFile().compareTo(p2.getDuplicateFile());
    }
}
