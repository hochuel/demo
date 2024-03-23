package com.example.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public abstract class ThreadAbstract implements Runnable{

    private String dirPath;
    private String 종목코드;

    private String dirName;


    public ThreadAbstract(String dirPath, String 종목코드, String dirName){
        this.dirPath = dirPath;
        this.종목코드 = 종목코드;
        this.dirName = dirName;
    }

    @Override
    public void run() {
        String basePath = "/stock/"+dirPath;
        File file = new File(basePath+"/"+종목코드);

        String readStr = null;
        try {
            readStr = FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = null;
        try {
            jsonObject = mapper.readValue(readStr, JSONObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String path = basePath + "/"+ this.dirName;
        File dir = new File(path);

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        List<Map> list = (List<Map>) jsonObject.get("output2");
        if(algorism(list)){
            path = path + "/" + 종목코드;
            File sfile = new File(path);
            try {
                FileUtils.writeStringToFile(sfile, jsonObject.toJSONString(), "utf8", false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract boolean algorism(List<Map> list);
}
