package com.example.stock.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class 이십일선횡보Service extends SeviceAbstract{
    @Override
    public List<Runnable> getProcess() {
        List<Runnable> resultList = new ArrayList<Runnable>();
        for(String 종목코드 : this.getFileList()){
            resultList.add(new 이십일선횡보Thread("2024-03-14", 종목코드, "이십일선위횡보"));
        }

        log.info("Thread SIZE :::" + resultList.size());
        return resultList;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new 이십일선횡보Service().start();
    }
}

class 이십일선횡보Thread extends ThreadAbstract{


    public 이십일선횡보Thread(String dirPath, String 종목코드, String dirName) {
        super(dirPath, 종목코드, dirName);
    }

    @Override
    public boolean algorism(List<Map> list) {

        Map stock0 = list.get(0);

        double 거래량 = stock0.get("acml_vol") == null ? 0 : Double.parseDouble(stock0.get("acml_vol").toString());
        double 현재가 = stock0.get("stck_clpr") == null ? 0 : Double.parseDouble(stock0.get("stck_clpr").toString());
        double day20 = stock0.get("20일선") == null ? 0 : Double.parseDouble(stock0.get("20일선").toString());

        boolean result = check(list, 10, 0.05);
        if(현재가 >= day20 && result && 거래량 >= 500000){
            return true;
        }

        return false;
    }

    public boolean check(List<Map> list, int num, double scope){
        boolean result = false;
        for(int i = 1; i < num; i++){
            Map stock = list.get(i);

            double day = stock.get("stck_clpr") == null ? 0 : Double.parseDouble(stock.get("stck_clpr").toString());
            double day20DayLine = stock.get("20일선") == null ? 0 : Double.parseDouble(stock.get("20일선").toString());

            double persent = day * scope;
            double lineAmtAdd = day20DayLine + persent;
            double lineAmtMinus = day20DayLine - persent;
            result = (day20DayLine <= day) && (lineAmtAdd >= day) || (day20DayLine >= day && lineAmtMinus < day) ? true:false;
            if(!result) return false;
        }

        return result;
    }

}
