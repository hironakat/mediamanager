package com.takashi.mediamanager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DBManager {
    protected static Connection conn = null;
    private Statement st = null;

    private Boolean versionDBexist = false;

    public DBManager() {
        cleanDB();
        try {
            if(conn == null){
                conn = DriverManager.getConnection(FileInfoTypes.fileInfoUri);
            }

            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);

            st = conn.createStatement();

            while (rs.next()) {
                if (FileInfoTypes.versionTableName.compareToIgnoreCase(rs.getString(3)) == 0) {
                    versionDBexist = true;
                }
            }
            if (!versionDBexist) {
                st.executeUpdate("create table version(id integer primary key, ver int)");
                st.executeUpdate("insert into version values(" + 0 + ", " + FileInfoTypes.dbver + ")");
            } else {
                rs = st.executeQuery(FileInfoTypes.querydbVer);
                while (rs.next()) {
                    int ver = rs.getInt("ver");
                    if (ver != FileInfoTypes.dbver) {
                        System.out.println("version number different");
                    }/* else {
                        System.out.println("version number same");
                    }*/
                }
            }
        } catch (SQLException e) {
            Utils.errPrint(e);
        }
    }

    public void closeDB() {
        try {
            if (st != null) st.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Utils.errPrint(e);
        }
    }

    private void cleanDB(){
        File folder = new File("D:\\Users\\Takashi\\IdeaProjects\\mediamanager\\picturefiledb");
        try {
            FileUtils.cleanDirectory(folder);
        }catch(IOException e){
            Utils.errPrint(e);
        }
        folder.delete();
    }



}
