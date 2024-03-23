package com.example.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void fileWrite(String fileName, String contents){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(contents);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
