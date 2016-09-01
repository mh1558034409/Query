package com.example.luxiansheng.query.service;

import android.os.Bundle;
import android.os.Message;

import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.view.Login;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

/**
 * 获取学年
 */
public class GetYear extends Thread implements Address {
    private String cookie;
    private String stuNumber;
    private String stuName;
    private String[] yearText;
    public GetYear(String cookie,String stuNumber,String stuName){
        this.cookie=cookie;
        this.stuNumber=stuNumber;
        this.stuName=stuName;
    }
    public void run() {
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(QueryScoreUrl + stuNumber + Name + stuName + ScoreTag);
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.protocol.handle-redirects", false);
        httpGet.setParams(params);
        httpGet.setHeader("Cookie",cookie);
        httpGet.setHeader("Referer",MainUrl+stuNumber);
        try {
            HttpResponse httpResponse=httpClient.execute(httpGet);
            String html=GetHtml.GetHtml(httpResponse.getEntity().getContent(),"GB2312");
            Document document= Jsoup.parse(html);
            Elements elements=document.select("#ddlXN>option");
            yearText=new String[elements.size()-1];
            int i=0;
            for (Element element:elements ){
                if (element.text().toString().equals("")==false){
                    yearText[i++]=element.text();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
