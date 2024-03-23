package com.example.stock.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class 이십일선위백만Service extends SeviceAbstract{
    @Override
    public List<Runnable> getProcess() {
        List<Runnable> resultList = new ArrayList<Runnable>();
        for(String 종목코드 : this.getFileList()){
            resultList.add(new 이십일선위백만Thread("2024-03-14", 종목코드, "이십일선위백만"));
        }

        log.info("Thread SIZE :::" + resultList.size());
        return resultList;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new 이십일선위백만Service().start();
    }
}

class 이십일선위백만Thread extends ThreadAbstract{


    public 이십일선위백만Thread(String dirPath, String 종목코드, String dirName) {
        super(dirPath, 종목코드, dirName);
    }

    @Override
    public boolean algorism(List<Map> list) {
        Map currentMap = list.get(0);

        System.out.println("Data ::" + currentMap);
        double 거래량 = currentMap.get("acml_vol") == null ? 0 : Double.parseDouble(currentMap.get("acml_vol").toString());
        double 현재가 = currentMap.get("stck_clpr") == null ? 0 : Double.parseDouble(currentMap.get("stck_clpr").toString());
        double day20 = currentMap.get("20일선") == null ? 0 : Double.parseDouble(currentMap.get("20일선").toString());
        //System.out.println(거래량 + ":"+ 현재가 +":"+ day20);
        return (거래량 < 5000000 && 거래량 > 1000000) && 현재가 >= day20;
    }
}
