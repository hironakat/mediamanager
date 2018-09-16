package com.takashi.mediamanager;

import javax.swing.*;

public class CountWindow {
    JFrame frame = new JFrame("Counter");
    JTextField countwindow=new JTextField("");

    public CountWindow(){
        countwindow.setBounds(50,50, 200,30);
        frame.add(countwindow);
        frame.setSize(300,200);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public void update(String str){
        countwindow.setText(str);
    }

}
