package com.takashi.mediamanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DuplicateFileList {
    private static List<DuplicateFiles> duplicateFilelist = new ArrayList<DuplicateFiles>();

    public void add(String orgFile, String dupFile){
        duplicateFilelist.add(new DuplicateFiles(orgFile, dupFile));
    }

    public void add(DuplicateFiles dup){
        duplicateFilelist.add(dup);
    }

    public String[][] toStringArray(){
        String [][] returnvalue = new String[duplicateFilelist.size()][2];
        int counter =0;
        for(DuplicateFiles dupFile : duplicateFilelist) {
            returnvalue[counter][0] = dupFile.getOrginalFile();
            returnvalue[counter][1] = dupFile.getDuplicateFile();
            counter++;
        }
        return returnvalue;
    }

    public DuplicateFileList dupSort(){
        Collections.sort(duplicateFilelist, new DuplicateFileComparator());
        return this;
    }

    public boolean isEmpty(){
        if(duplicateFilelist == null)
            return true;
        else
            return duplicateFilelist.isEmpty();
    }
}
