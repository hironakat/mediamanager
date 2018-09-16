package com.takashi.mediamanager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ListWindow extends FileList  {

    private static final long serialVersionUID = 1L;
    private JTable list;
    static GraphicsConfiguration gc;
    JFrame dupListwindow, orgImgWindow, dupImageWindow;
    Image   orgImg, dupImg;
    static PictureDisplay orgCanv;
    static PictureDisplay dupCanv;

    public ListWindow(){
        super();
        dupListwindow= new JFrame(gc);
        orgImgWindow= new JFrame(gc);
        dupImageWindow= new JFrame(gc);

        dupListwindow.setVisible(true);
        dupListwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orgImgWindow.setVisible(true);
        orgImgWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dupImageWindow.setVisible(true);
        dupImageWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dupListwindow.setTitle("Duplicates");

        orgCanv = null;
        dupCanv = null;
    }
    public void setList(DuplicateFileList data){
        String[] columnName = {"Original","Duplicate"};
        list = new JTable(data.toStringArray(), columnName);

        ListSelectionModel cellSelectionModel = list.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    String orgFile = (String)list.getValueAt(list.getSelectedRow(),0);
                    String duplicateFile = (String)list.getValueAt(list.getSelectedRow(),1);
                    displayDuplicatePictures(orgFile, duplicateFile);

                }
            }

        });

        dupListwindow.add(list,BorderLayout.CENTER);
        dupListwindow.setSize(new Dimension(1000,200));
        JScrollPane scrPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        dupListwindow.getContentPane().add(scrPane);
    }
    public void close(){
        dupListwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dupListwindow.setResizable(false);
        orgImgWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orgImgWindow.setResizable(false);
        dupImageWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dupImageWindow.setResizable(false);
    }

    public void displayDuplicatePictures(String orgFile, String duplicateFile){
        orgImgWindow.setTitle(orgFile);
        dupImageWindow.setTitle(duplicateFile);

        if(orgCanv!=null && orgImgWindow.isAncestorOf(orgCanv)){
            orgImgWindow.remove(orgCanv);
        }
        if(dupCanv!=null && dupImageWindow.isAncestorOf(dupCanv)){
            dupImageWindow.remove(dupCanv);
        }

        orgCanv = new PictureDisplay(orgFile);
        dupCanv = new PictureDisplay(duplicateFile);

        orgImgWindow.add(orgCanv);
        orgImgWindow.pack();
        orgImgWindow.setSize(orgCanv.getX(), orgCanv.getY());
        orgImgWindow.setVisible(true);

        dupImageWindow.add(dupCanv);
        dupImageWindow.pack();
        dupImageWindow.setSize(dupCanv.getX(), dupCanv.getY());
        dupImageWindow.setVisible(true);

        dupListwindow.setVisible(true);
    }
}

