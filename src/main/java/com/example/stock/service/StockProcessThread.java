package com.example.stock.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class StockProcessThread {
    public void stockSaveThread(List<Runnable> threadList) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor executorService =
                new ThreadPoolExecutor(20,25,3, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        for (Runnable runnable : threadList) {
            executorService.execute(runnable);
        }
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        executorService.shutdown();
    }

}
