package com.example.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class 천만주Service extends SeviceAbstract{

    @Override
    public List<Runnable> getProcess() {
        List<Runnable> resultList = new ArrayList<Runnable>();
        for(String 종목코드 : this.getFileList()){
            resultList.add(new 천만주SearchChild("2024-03-12", 종목코드, "천만주"));

        }
        return resultList;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new 천만주Service().start();
    }
}


class 천만주SearchChild extends ThreadAbstract{

    public 천만주SearchChild(String dirPath, String 종목코드, String dirName) {
        super(dirPath, 종목코드, dirName);
    }

    @Override
    public boolean algorism(List<Map> list) {
        Map currentMap = list.get(0);
        String 거래량 = currentMap.get("acml_vol").toString();

        return Integer.parseInt(거래량) > 10000000;
    }
}