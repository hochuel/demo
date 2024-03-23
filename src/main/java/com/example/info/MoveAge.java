package com.example.info;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveAge {

    public List<Map> sma(List<Map> list, int period){

        double[] closePrice = new double[list.size()];
        double[] out = new double[list.size()];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        for (int i = 0; i < list.size(); i++) {
            try {
                Map data = list.get(i);
                closePrice[i] = Double.parseDouble((String) data.get("stck_clpr"));
            }catch (Exception e){
                closePrice[i] = 0;
            }
        }

        Core c = new Core();
        RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, period, begin, length, out);

        List<Map> rlist = new ArrayList<Map>();
        if (retCode == RetCode.Success) {

            for (int i = begin.value; i < begin.value + length.value; i++) {
                int index = i - (period - 1);
                Map data = list.get(index);
                data.put(period+"일선", out[index]);
                rlist.add(data);
            }
        }

        return rlist;
    }
}
