package com.example.luxiansheng.query.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.view.Main;
import com.example.luxiansheng.query.view.ShowInfor;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获得学生的个人信息
 */
public class GetInfor extends Thread implements Address{
    private String cookie;
    private String stuNumber;
    private String stuName;
    public GetInfor(String cookie,String stuNumber,String stuName){
        this.cookie=cookie;
        this.stuNumber=stuNumber;
        this.stuName=stuName;
    }
    public void run() {
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(InforUrl+stuNumber+"&xm="+stuName+InforTag);
        httpGet.setHeader("Cookie",cookie);
        httpGet.setHeader("Referer",MainUrl+stuNumber);
        try {
            HttpResponse httpResponse=httpClient.execute(httpGet);
            String html=GetHtml.GetHtml(httpResponse.getEntity().getContent(),"GB2312");
            Document document=Jsoup.parse(html);
            String xy= document.getElementById("xy").text();
            String zy=document.getElementById("zy").text();
            String xzb=document.getElementById("xzb").text();
            String dqszj=document.getElementById("dqszj").text();
            String cc=document.getElementById("cc").text();
            HttpGet httpGetImage=new HttpGet(InforImageUrl+stuNumber);
            httpGetImage.setHeader("Cookie",cookie);
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects", true);//设置重定向
            httpGetImage.setParams(params);
            HttpResponse httpResponseImage=httpClient.execute(httpGetImage);
            Bitmap image= BitmapFactory.decodeStream(httpResponseImage.getEntity().getContent());
            Bundle bundle=new Bundle();
            bundle.putString("xy",xy);
            bundle.putString("zy",zy);
            bundle.putString("xzb",xzb);
            bundle.putString("dqszj",dqszj);
            bundle.putString("cc",cc);
            bundle.putParcelable("image",image);
            Message message=new Message();
            message.setData(bundle);
            message.what=4;
            ShowInfor.handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
