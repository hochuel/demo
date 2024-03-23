package com.example.stock.service;

import com.example.stock.StockBring;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SeviceAbstract {

    private List<String> fileList = null;

    public List<String> getFileList() {
        return fileList;
    }

    public SeviceAbstract(){
        fileList = getDirs();
    }

    final String BASE_URL = "/stock";

    private List<String> getDirs(){
        StockBring stockBring = new StockBring();
        String fileName = "/kosdaq_code.mst";
        List<String> kosdaqList = stockBring.getStockList(fileName);

        fileName = "/kospi_code.mst";
        List<String> kospiList =  stockBring.getStockList(fileName);

        return Stream.concat(kosdaqList.stream(), kospiList.stream()).collect(Collectors.toList());
    }

    public abstract List<Runnable> getProcess();

    public void start() throws ExecutionException, InterruptedException {
        List <Runnable> runnableList = getProcess();
        StockProcessThread stockProcessThread = new StockProcessThread();
        stockProcessThread.stockSaveThread(runnableList);
    }
}
