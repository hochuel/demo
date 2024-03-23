package com.example.info;

import com.example.oauth.web.ApprovalController;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockInfoTest {


    public static void main(String[] args){
        ApprovalController approvalController = new ApprovalController();
        JSONObject jsonObject = approvalController.inquireDailyPrice("103590", approvalController.getToken());
        List<Map> list = (List<Map>) jsonObject.get("output2");

        System.out.println(list);

        MoveAge moveAge = new MoveAge();

        list = moveAge.sma(list, 5);
        list = moveAge.sma(list, 20);
        list = moveAge.sma(list, 60);
        //list = moveAge.sma(list, 120);

        System.out.println(list);

    }
}
