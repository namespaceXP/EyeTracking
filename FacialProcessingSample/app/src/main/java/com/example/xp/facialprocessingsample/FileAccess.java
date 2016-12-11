package com.example.xp.facialprocessingsample;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.Calendar;


class FileAccess{
    String FileName;//要保存在哪里，路径你自己设
    File myFile;
    FileOutputStream bos;

    FileAccess(){
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
        Date date=new Date();
        FileName = android.os.Environment.getExternalStorageDirectory().getPath() + "/" + dateFormater.format(date) +".txt";
        myFile = new File(FileName);
        try {
            bos = new FileOutputStream(myFile, true);
        }
        catch (Exception e){

        }
    }

    void initFile(String str){
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");
        Date date=new Date();
        FileName = android.os.Environment.getExternalStorageDirectory().getPath() + "/" + str +".txt";
        myFile = new File(FileName);
        try {
            bos = new FileOutputStream(myFile, true);
        }
        catch (Exception e){

        }
    }

    void FileWriteLog(String str){
        StringBuffer sb = new StringBuffer();
        sb.append(str + "\n");
        try {
            bos.write(sb.toString().getBytes("utf-8"));
        }
        catch (Exception e){

        }
    }
}