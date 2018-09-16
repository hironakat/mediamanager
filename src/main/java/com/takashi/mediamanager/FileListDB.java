package com.takashi.mediamanager;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class FileListDB extends DBManager {
    private Statement st = null;
    private Long counter = 0l;

    private Boolean fileinfoDBexist = false;

    public FileListDB(){
        super();
        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);

            if (st == null) {
                st = conn.createStatement();
            }
            while (rs.next()) {
                if (FileInfoTypes.fileInfoTableName.compareToIgnoreCase(rs.getString(3)) == 0) {
                    fileinfoDBexist = true;
                }
            }
            if (!fileinfoDBexist) {
                st.executeUpdate("create table fileinfo(id bigint primary key, datetaken varchar(30), filesize bigint, filename varchar(32672), duplicate varchar(16), noPictureFile varchar(16), duplicateOrgFile varchar(32672), destination varchar(32672), destFileExist varchar(16))");
            }
        } catch (SQLException e) {
            Utils.errPrint(e);
        }
    }

    public void InsertRecord(FileInfo fileinfodata) {
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into fileinfo(id, datetaken, filesize, filename, duplicate, noPictureFile, duplicateOrgFile, destination, destFileExist, fileCopied) values(?,?,?,?, ?, ?, ?, ?, ?, ?)");

            pstmt.setLong(1, counter);
            pstmt.setString(2, fileinfodata.getDateTimeTaken());
            pstmt.setLong(3, fileinfodata.getFileSize());
            pstmt.setString(4, fileinfodata.getFilePath());
            pstmt.setString(5, fileinfodata.getDuplicate().toString());
            pstmt.setString(6, fileinfodata.getNonPictureFile().toString());
            File filepath = fileinfodata.getDuplicateOriginalFile();
            if(filepath == null) {
                pstmt.setString(7, "null");
            }else{
                pstmt.setString(7, filepath.getPath());
            }
            filepath = fileinfodata.getDestinationFile();
            if(filepath == null) {
                pstmt.setString(8, "null");
            }else{
                pstmt.setString(8, filepath.getPath());
            }
            pstmt.setString(9, fileinfodata.getDestFileExist().toString());
            pstmt.setString(10, fileinfodata.getFileCopied().toString());
            pstmt.executeUpdate();
            counter++;
        } catch(SQLException e){
            Utils.errPrint(e);
        }
    }

    public void updateRecord(FileInfo fileinfodata) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("update fileinfo set destination = ?, destFileExist = ? where filename = ?");

            File filepath = fileinfodata.getDuplicateOriginalFile();
            if(filepath == null) {
                pstmt.setString(1, "null");
            }else{
                pstmt.setString(1, filepath.getPath());
            }
            pstmt.setString(2, fileinfodata.getDestFileExist().toString());
            pstmt.setString(3, fileinfodata.getFilePath() );
            pstmt.executeUpdate();
            counter++;
        } catch(SQLException e){
            Utils.errPrint(e);
        }
    }

    public void getFromDB(List<FileInfo> filelist){
        filelist.clear();
        try {
            ResultSet rs = st.executeQuery(FileInfoTypes.queryFileinfo);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                FileInfo fi = new FileInfo();
                CharSequence dateTime = rs.getString(2);
                fi.set(LocalDateTime.parse(dateTime));
                fi.set(rs.getLong(3));
                try {
                    fi.set(new File(rs.getString(4)));
                } catch (FileInfoException e) {
                    Utils.errPrint(e);
                }
                fi.setDuplicate((rs.getString(5).equalsIgnoreCase("true")));
                fi.setNonPictureFile((rs.getString(6).equalsIgnoreCase("true")));
                fi.setDuplicateOriginalFile(new File(rs.getString(7)));
                fi.setDestination(new File(rs.getString(8)));
                fi.setDestFileExist((rs.getString(9).equalsIgnoreCase("true")));

                filelist.add(fi);
            }
        }catch(SQLException e){
            Utils.errPrint(e);
        }
    }

    public void CloseDB(){
        try {
            if (st != null) st.close();
        } catch(SQLException e){
            Utils.errPrint(e);
        }
        super.closeDB();
    }

    public void print(){
        try {
            ResultSet rs = st.executeQuery(FileInfoTypes.queryFileinfo);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
        }catch(SQLException e){
            Utils.errPrint(e);
        }
    }

    public boolean getFileInfoDBexist(){
        return fileinfoDBexist;
    }
    public void resetCounter(){
        counter = 0l;
    }
}
