package com.example.api.web;


import com.example.stock.StockBring;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
public class SampleController {

    @GetMapping("/api/getSample")
    public ResponseEntity<?> getSample() throws Exception{

        StockBring stockBring = new StockBring();
        List list = stockBring.getList();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item", list);

        log.info("데이터 ::: " + jsonObject);

        return new ResponseEntity<>(jsonObject, header, HttpStatus.OK);

    }

    @GetMapping("/api/test")
    public ResponseEntity<?> getTest() throws Exception{

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));


        return new ResponseEntity<>("테스트 입니다.", header, HttpStatus.OK);

    }


    @GetMapping("/api/dirs")
    public ResponseEntity<?> getDirs() throws Exception{

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

//천만주 //이십일선위백만
        File file = new File("/stock/2024-03-14/이십일선위횡보 ");
        File[] files = file.listFiles();

        List<File> fileList = Arrays.stream(files).toList();
        List<Map> result = new ArrayList<Map>();
        for(File f : fileList){
            Map<String, String> map = new HashMap();
            map.put("name", f.getName());
            result.add(map);
        }
        log.info("디렉토리 정보::" + result);
        return new ResponseEntity<>(result, header, HttpStatus.OK);
    }


    @GetMapping("/api/fileInfo/{fileName}")
    public ResponseEntity<?> getFileInfo(@PathVariable String fileName) throws Exception{

        log.info("파일이름:::" + fileName);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));


        File file = new File("/stock/2024-03-14/이십일선위횡보/" +fileName);
        String readStr = FileUtils.readFileToString(file, Charset.defaultCharset());

        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject = mapper.readValue(readStr, JSONObject.class);

        Map output1 = (Map)jsonObject.get("output1");
        List<Map> output2 = (List)jsonObject.get("output2");

        log.info("output1 ::" + output1);
        log.info("output2 ::" + output2);
//hts_kor_isnm=TIGER 글로벌비만치료제TOP2Plus, stck_prpr=10470, stck_shrn_iscd=476690
        //stck_bsop_date=20240308, stck_clpr=5520, stck_oprc=5580, stck_hgpr=5620, stck_lwpr=5480
        List list = new ArrayList();
        for(Map data : output2){
            Map detail = new HashMap();

            String stck_bsop_date = data.get("stck_bsop_date").toString(); //날자
            String stck_clpr = data.get("stck_clpr").toString(); //종가
            String stck_oprc = data.get("stck_oprc").toString(); //시작가
            String stck_hgpr = data.get("stck_hgpr").toString(); //고가
            String stck_lwpr = data.get("stck_lwpr").toString(); //저가

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(stck_bsop_date, formatter);

            detail.put("stck_bsop_date", localDate);
            detail.put("stck_clpr", stck_clpr);
            detail.put("stck_oprc", stck_oprc);
            detail.put("stck_hgpr", stck_hgpr);
            detail.put("stck_lwpr", stck_lwpr);

            list.add(detail);
        }

        JSONObject jList = new JSONObject();
        jList.put("data", list);

        ////hts_kor_isnm=TIGER 글로벌비만치료제TOP2Plus, stck_prpr=10470, stck_shrn_iscd=476690
        JSONObject info = new JSONObject();
        info.put("hts_kor_isnm", output1.get("hts_kor_isnm").toString());
        info.put("stck_shrn_iscd", output1.get("stck_shrn_iscd").toString());
        jList.put("info", info);

        log.info("INFO ::" + jList);

        return new ResponseEntity<>(jList, header, HttpStatus.OK);
    }



    @GetMapping("/api/dirFilesInfoList")
    public ResponseEntity<?> dirFilesInfoList() throws Exception{

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        File file = new File("/stock/2024-03-14/이십일선위횡보");
        File[] files = file.listFiles();
        List resultList = new ArrayList();
        for(File f : files) {
            File data = new File(f.getPath());
            String readStr = FileUtils.readFileToString(data, Charset.defaultCharset());

            ObjectMapper mapper = new ObjectMapper();
            JSONObject jsonObject = mapper.readValue(readStr, JSONObject.class);

            resultList.add(jsonObject.get("output1"));
        }

        log.info("resultList :: " + resultList);

        return new ResponseEntity<>(resultList, header, HttpStatus.OK);
    }
}
