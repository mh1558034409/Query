package com.example.luxiansheng.query.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import java.io.IOException;


/**
 * 获得viewState
 */
public class GetViewState {
    public static String Get(String url,String cookie,String referer){
        String viewState="";
        HttpClient httpClient= new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);
        httpGet.setHeader("Cookie",cookie);
        httpGet.setHeader("Referer",referer);
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
            String html= EntityUtils.toString(httpResponse.getEntity());
            viewState= Jsoup.parse(html).select("input[name=__VIEWSTATE]").val();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewState;
    }
}
