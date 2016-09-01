package com.example.luxiansheng.query.service;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.model.CetScore;
import com.example.luxiansheng.query.view.Cet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * 获取四级成绩
 */
public class GetCet extends Thread implements Address {
    String CetCookie;
    private String number;
    private String name;
    public GetCet(String number,String name){
        this.number=number;
        this.name=name;
    }
    public void run() {
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(CetUrl);
        try {
            HttpResponse httpResponse=httpClient.execute(httpGet);
            CetCookie=httpResponse.getFirstHeader("Set-Cookie").getValue();
            HttpGet httpGet1=new HttpGet(CetUrlQuery+number+"&xm="+name);
            httpGet1.setHeader("Cookie",CetCookie);
            httpGet1.setHeader("Referer",CetUrl);
            HttpResponse httpResponse1=httpClient.execute(httpGet1);
            String html=GetHtml.GetHtml(httpResponse1.getEntity().getContent(),"utf-8");
            getCet(html);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getCet(String html){
        Document document= Jsoup.parse(html);
        if(document.getElementsByClass("m_cnt_m").select("table").isEmpty()){
            Message message=new Message();
            message.what=10;
            Bundle bundle=new Bundle();
            bundle.putString("IsOK","OK");
            message.setData(bundle);
            Cet.handler.sendMessage(message);
        }else {
            Elements trs=document.getElementsByClass("cetTable").select("tr");
            CetScore cetScore=new CetScore();
            String[] cet=new String[9];
            int i;
            for(i=0;i<5;i++){
                Elements tds=trs.get(i).select("td");
                cet[i]=tds.get(0).text();

            }
            Elements tds=trs.get(5).select("td");
            String string=tds.get(0).text();
            String[] score=string.split(" ");
            for(String s:score)
                if(s.matches("[0-9]+"))
                    cet[i++]=s;
            cetScore.setName(cet[0]);
            cetScore.setSchool(cet[1]);
            cetScore.setType(cet[2]);
            cetScore.setNumber(cet[3]);
            cetScore.setTime(cet[4]);
            cetScore.setTotalScore(cet[5]);
            cetScore.setListening(cet[6]);
            cetScore.setReading(cet[7]);
            cetScore.setWrite(cet[8]);
            Message message=new Message();
            Bundle bundle=new Bundle();
            bundle.putSerializable("cetScore",cetScore);
            message.what=10;
            message.setData(bundle);
            Cet.handler.sendMessage(message);
        }




    }
}
