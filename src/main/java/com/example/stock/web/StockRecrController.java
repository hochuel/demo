package com.example.stock.web;

import com.example.oauth.web.ApprovalController;
import com.example.stock.StockBring;
import com.example.stock.service.StockRecvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class StockRecrController {

    @Autowired
    private StockRecvService stockRecvService;


    @RequestMapping("/api/stockRecvService")
    public SseEmitter stockRecvService() throws Exception {
        log.info("stockRecvService");

        SseEmitter emitter = new SseEmitter(400000L);

        StockBring stockBring = new StockBring();
        List<String> 종목_List = stockBring.getList();

        ApprovalController approvalController = new ApprovalController();
        String token = approvalController.getToken();

        List<Map> threadResult = new ArrayList<>();
        stockRecvService.stockSaveThread(종목_List, token, threadResult);


        Thread t = new Thread(() -> {
            boolean start = true;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while(start){
                Map result = threadResult.get(threadResult.size() - 1);

                try {
                    emitter.send(result);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                start = ((int)result.get("activeCnt") == 1)?false:true;

                if(!start){
                    emitter.complete();
                }
            }


        });
        t.start();

        return emitter;
    }

}
