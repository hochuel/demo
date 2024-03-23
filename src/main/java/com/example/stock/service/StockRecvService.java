package com.example.stock.service;


import com.example.info.MoveAge;
import com.example.oauth.web.ApprovalController;
import com.example.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.el.lang.ELArithmetic;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class StockRecvService {


    private String basePath = "/stock";

    public void stockSaveThread(List<String> stockList, String token, List<Map> result) throws InterruptedException, ExecutionException {

        ThreadPoolExecutor executorService =
                new ThreadPoolExecutor(20,25,3, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        ApprovalController approvalController = new ApprovalController();
        log.info("종목수 :" + stockList.size());
        for (String 종목코드 : stockList) {
            Future future = executorService.submit(new Task(executorService, approvalController, 종목코드, basePath, token, result));
            //threadNum.add(Integer.parseInt((String)future.get()));
        }
        //executorService.awaitTermination(5, TimeUnit.SECONDS);

        //threadNum.add(executorService.getCompletedTaskCount());

        //executorService.shutdown();
    }



    static class Task implements Callable{

        private int count = 0;
        private ApprovalController approvalController;
        private String 종목코드;
        private String basePath;

        private ThreadPoolExecutor executorService;

        private String token;

        List<Map> result;

        public Task(ThreadPoolExecutor executorService, ApprovalController approvalController, String 종목코드, String basePath, String token, List<Map> result){
            this.executorService = executorService;
            this.approvalController = approvalController;
            this.종목코드 = 종목코드;
            this.basePath = basePath;
            this.token = token;
            this.result = result;

        }

        public String call(){
            //log.info("총 스레드 갯수 :" + executorService.getPoolSize() + ":쓰레드 이름:"+Thread.currentThread().getName());
            JSONObject jsonObject = approvalController.inquireDailyPrice(종목코드, token);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Map> list = (List<Map>) jsonObject.get("output2");

            MoveAge moveAge = new MoveAge();
            list = moveAge.sma(list, 5);
            list = moveAge.sma(list, 20);
            list = moveAge.sma(list, 60);

            jsonObject.put("output2", list);

            String path = basePath + "/" + DateUtils.getCurrentDate();
            File dir = new File(path);

            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            path = path + "/" + 종목코드;

            //log.info(path);
            File file = new File(path);
            try {
                FileUtils.writeStringToFile(file, jsonObject.toJSONString(), "utf8", false);
            } catch (IOException e) {
                e.printStackTrace();
                count ++;
            }

            log.info(executorService.getCompletedTaskCount() + "/" + executorService.getTaskCount() +":::"+executorService.getActiveCount());
            count ++;

            Map mapResult = new HashMap();
            mapResult.put("completeCnt", executorService.getCompletedTaskCount());
            mapResult.put("taskCnt", executorService.getTaskCount());
            mapResult.put("activeCnt", executorService.getActiveCount());
            result.add(mapResult);

            return Integer.toString(count);
        }
    }
}