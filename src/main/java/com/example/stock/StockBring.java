package com.example.stock;


import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipInputStream;

public class StockBring {

    public void getStockFileBring(String addrUrl){
        try{
            String[] name = addrUrl.split("/");

            URL url = new URL(addrUrl);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            File file = new File("/stock/"+name[name.length - 1]);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();

            unCompressZip(file.getPath());


            Path path = Paths.get(file.getPath());
            try {
                Files.deleteIfExists(path);
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void unCompressZip(String filepath) throws Exception{
        File zipFile = new File(filepath);

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
        ZipInputStream zipInputStream = new ZipInputStream(in);

        while(zipInputStream.getNextEntry() != null){
            int length = 0;

            String temp = filepath.substring(0, filepath.indexOf("zip"));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
            while((length = zipInputStream.read()) != -1){
                out.write(length);
            }

            zipInputStream.closeEntry();
        }

        in.close();
        zipInputStream.close();
    }


    public List<String> getStockList(String fileName){

        List<String> resultStockList = null;
        File file = new File("/stock/"+fileName);

        try {
            List<String> lines = FileUtils.readLines(file, Charset.forName("UTF-8"));
            String regExp = "^[0-9]+$";
            resultStockList = lines.stream().filter(s->s.substring(0, 6).matches(regExp) == true).map(s->s.substring(0, 6)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultStockList;
    }


    public List getList(){
        String kosdaqUrl = "https://new.real.download.dws.co.kr/common/master/kosdaq_code.mst.zip";
        String kospiUrl = "https://new.real.download.dws.co.kr/common/master/kospi_code.mst.zip";

        StockBring stockBring = new StockBring();
        stockBring.getStockFileBring(kosdaqUrl);
        List<String> kosdaqList = stockBring.getStockList("kosdaq_code.mst");
        stockBring.getStockFileBring(kospiUrl);
        List<String> kospiList = stockBring.getStockList("kospi_code.mst");

        //코스피 종목, 코스탁 종목 합치기
        List<String> resultList = Stream.concat(kosdaqList.stream(), kospiList.stream()).collect(Collectors.toList());

        return resultList;
    }

    public static void main(String[] args){


        List<String> resultList = new StockBring().getList();

        resultList.forEach(System.out::println);
        System.out.println(resultList.size());
    }
}
