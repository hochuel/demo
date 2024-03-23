package com.example.oauth.web;

import com.example.info.MoveAge;
import com.example.stock.StockBring;
import com.example.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ApprovalController {

    private final String BASE_URL = "https://openapi.koreainvestment.com:9443";

    /**
     * 한국 투자 증권 웹소켓 접속 키 가져오기.
     * @return
     */
    public String Approval(){

        String path = "/oauth2/Approval";
        String url = BASE_URL + path;

        JSONObject param = new JSONObject();
        param.put("grant_type", "client_credentials");
        param.put("appkey", "PSmCTOHzwixL58lhbQOxI2TRlLX7xYYF1sMm");
        param.put("secretkey", "orNM1+7tp/aiFRnGQj0wPj8WFZr+Pc3OB6nM5j9l5MhYFco8oIr4brNnZQdcjcI5tH7Z74OiH+9DOoHeLgFpGSXNeYBa3pK4DT+6K3YDfyxmTCHugU53Qw6M1oug6RirmEzQ46aWeOU3KYpJD6QeYa2RJGEVgr25pj2jUNpPxM0Osmq0wZ8=");

        System.out.println(param.toJSONString());


        RestClient restClient = RestClient.create();
        JSONObject response = restClient.post().uri(url, param)
                .contentType(APPLICATION_JSON)
                .body(param).retrieve().body(JSONObject.class);


        return response.toJSONString();
    }

    public String getToken(){
        String token = null;
        try {

            File file = new File("/stock/TokenP");
            if(!file.exists()){
                System.out.println("토큰 가져오기1...");
                return getTokenP();
            }
            String readData = FileUtils.readFileToString(file, Charset.defaultCharset());
            //System.out.println(readData);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(readData);
            String accessTokenExpired = jsonObject.get("access_token_token_expired").toString();

            LocalDateTime tokenDateTime = DateUtils.getStringToLocalDateTime(accessTokenExpired);
            LocalDateTime localDateTime = LocalDateTime.now();

            if(tokenDateTime.isBefore(localDateTime)){
                System.out.println("토큰 가져오기2...");
                return getTokenP();
            }

            token = jsonObject.get("access_token").toString();
        }catch (Exception e){
            e.printStackTrace();
        }

        //System.out.println("토큰 가져오기3..." + token);
        return token;
    }
    public String getTokenP() throws IOException {

        String path = "/oauth2/tokenP";
        String url = BASE_URL + path;

        JSONObject param = new JSONObject();
        param.put("grant_type", "client_credentials");
        param.put("appkey", "PSmCTOHzwixL58lhbQOxI2TRlLX7xYYF1sMm");
        param.put("appsecret", "orNM1+7tp/aiFRnGQj0wPj8WFZr+Pc3OB6nM5j9l5MhYFco8oIr4brNnZQdcjcI5tH7Z74OiH+9DOoHeLgFpGSXNeYBa3pK4DT+6K3YDfyxmTCHugU53Qw6M1oug6RirmEzQ46aWeOU3KYpJD6QeYa2RJGEVgr25pj2jUNpPxM0Osmq0wZ8=");


        RestClient restClient = RestClient.create();
        JSONObject response = restClient.post().uri(url, param)
                .contentType(APPLICATION_JSON)
                .body(param).retrieve().body(JSONObject.class);

        System.out.println(response.toJSONString());

        FileUtils.write(new File("/stock/tokenP"), response.toJSONString(), Charset.defaultCharset());

        return response.get("access_token").toString();
    }

    /**
     * 주식 종목 기간별 조회
     * @return
     */
    public JSONObject inquireDailyPrice(String 종목코드, String token){
        return inquireDailyPrice(종목코드, token, null, null);
    }


    public JSONObject inquireDailyPrice(String 종목코드, String token, String stDate, String enDate){
        String path = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
        String url = BASE_URL + path;

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json; charset=utf-8");
        headers.add("authorization", "Bearer "+token);
        headers.add("appkey", "PSmCTOHzwixL58lhbQOxI2TRlLX7xYYF1sMm");
        headers.add("appsecret", "orNM1+7tp/aiFRnGQj0wPj8WFZr+Pc3OB6nM5j9l5MhYFco8oIr4brNnZQdcjcI5tH7Z74OiH+9DOoHeLgFpGSXNeYBa3pK4DT+6K3YDfyxmTCHugU53Qw6M1oug6RirmEzQ46aWeOU3KYpJD6QeYa2RJGEVgr25pj2jUNpPxM0Osmq0wZ8=");
        headers.add("tr_id", "FHKST03010100");
        headers.add("custtype", "P");
        HttpEntity request = new HttpEntity(headers);

        if(enDate == null || enDate.equals("")){
            enDate = DateUtils.getCurrentDateTime("yyyyMMdd");
        }

        if(stDate == null || stDate.equals("")){
            stDate = DateUtils.getMinusDate(enDate, "yyyyMMdd", 365);
        }

        //System.out.println(enDate + ":" + stDate);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", 종목코드)
                .queryParam("FID_INPUT_DATE_1", stDate)
                .queryParam("FID_INPUT_DATE_2", enDate)
                .queryParam("FID_PERIOD_DIV_CODE", "D")
                .queryParam("FID_ORG_ADJ_PRC", "1");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                JSONObject.class
        );

        return result.getBody();
    };


    public static void main(String[] args){
        try {
            ApprovalController approvalController = new ApprovalController();


            StockBring stockBring = new StockBring();
            List<String> 종목_List = stockBring.getList();

            String token = approvalController.getToken();

            int index = 0;
            for(String 종목코드 : 종목_List) {
                JSONObject jsonObject = approvalController.inquireDailyPrice(종목코드, token);
                try{
                    Thread.sleep(30);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Map output1 = (Map) jsonObject.get("output1");
                String 주식한글명 = output1.get("hts_kor_isnm").toString();

                List<Map> list = (List<Map>) jsonObject.get("output2");
                Map currentMap = list.get(0);
                //System.out.println(currentMap);

                String 날짜 = currentMap.get("stck_bsop_date").toString();
                String 종가 = currentMap.get("stck_clpr").toString();
                String 거래량 = currentMap.get("acml_vol").toString();
                /**
                 * 5,000,000 ~ 1,000,000
                 */

                if (Integer.parseInt(거래량) < 1000000 && Integer.parseInt(거래량) > 500000) {
/*
                    ArrayList<Map> arrayList = new ArrayList<>(list.subList(0, 20));
                    IntStream intStream = arrayList.stream().filter(d -> (d != null && d.get("stck_clpr") != null)).mapToInt(
                            d -> {
                                try {
                                    return Integer.parseInt(d.get("stck_clpr").toString());
                                }catch (Exception e){
                                    e.printStackTrace();
                                    return 0;
                                }
                            });
                    int avg = intStream.sum() / 20;
                    //System.out.println("20일 평균 값은 :: " + avg);
                    if (avg < Integer.parseInt(종가)) {
                        System.out.println(주식한글명 + "(" + 종목코드 + ")");
                    }
*/
                    MoveAge moveAge = new MoveAge();

                    list = moveAge.sma(list, 5);
                    list = moveAge.sma(list, 20);
                    list = moveAge.sma(list, 60);

                    Map stockData = list.get(0);

                    double day5 = Double.parseDouble(stockData.get("5일선").toString());
                    double day20 = Double.parseDouble(stockData.get("20일선").toString());
                    double day60 = Double.parseDouble(stockData.get("60일선").toString());

                    if((Integer.parseInt(종가) > day5) && (day5 > day20)  && (day20 > day60)){
                        System.out.println(주식한글명 + "(" + 종목코드 + ")");
                    }

                }
                index ++;
                //System.out.println(index+"/"+종목_List.size());
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


