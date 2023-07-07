package com.takashi.mediamanager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class FileInfoComparator implements Comparator<FileInfo> {
    private List<CharSequence>keyList = new ArrayList<CharSequence>();

    @Override
    public int compare(FileInfo p1, FileInfo p2) {
        int returnvalue = 0;
        keyList.add(" copy");
        for(int i = 1;i<10;i++){
            keyList.add(" ("+String.valueOf(i)+")");
        }

        String tempStr;
        for(CharSequence key : keyList) {
            if(p1.getFileName().contains(key)){
                tempStr = p1.getFileName().replace(key, "");
                if(p2.getFileName().equalsIgnoreCase(tempStr)){
                    returnvalue = 1;
                    break;
                }
            }else if(p2.getFileName().contains(key)){
                tempStr = p2.getFileName().replace(key, "");
                if(p1.getFileName().equalsIgnoreCase(tempStr)){
                    returnvalue = -1;
                    break;
                }
            }
        }
        keyList.clear();
        /*if(returnvalue == 0){
            returnvalue = p1.getFileName().compareTo(p2.getFileName());
            System.out.println("returnvalue " + returnvalue);
            returnvalue = -1;
        }*/
        return returnvalue;
    }
}

