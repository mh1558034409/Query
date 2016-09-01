package com.example.luxiansheng.query.service;


import android.os.Bundle;
import android.os.Message;

import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.model.Score;
import com.example.luxiansheng.query.view.ShowScore;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 传递参数，查询成绩
 */
public class QueryScore extends Thread implements Address {
    private String ddlXN;
    private String ddlXQ;
    private String cookie;
    private String viewState;
    private String stuName;
    private String stuNumber;
    public QueryScore(String ddlXN, String ddlXQ,String cookie,String stuNumber,String stuName) {
        this.ddlXN = ddlXN;
        this.ddlXQ = ddlXQ;
        this.cookie=cookie;
        this.stuNumber=stuNumber;
        this.stuName=stuName;
    }
    public void run() {
        viewState = GetViewState.Get(QueryScoreUrl + stuNumber + Name + stuName + ScoreTag, cookie, MainUrl + stuNumber);
        HttpPost httpPostQuery = new HttpPost(QueryScoreUrl + stuNumber + Name + stuName + ScoreTag);
        httpPostQuery.setHeader("Cookie", cookie);
        httpPostQuery.setHeader("Referer", MainUrl +stuNumber);
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        list.add(new BasicNameValuePair("__EVENTTARGET", ""));
        list.add(new BasicNameValuePair("__VIEWSTATE", viewState));
        list.add(new BasicNameValuePair("btn_xq", BTN_XQ));
        list.add(new BasicNameValuePair("ddlXN", ddlXN));
        list.add(new BasicNameValuePair("ddlXQ", ddlXQ));
        list.add(new BasicNameValuePair("ddl_kcxz", ""));
        list.add(new BasicNameValuePair("hidLanguage", ""));
        HttpClient httpClient = new DefaultHttpClient();
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "Gb2312");
            httpPostQuery.setEntity(entity);
            HttpResponse httpResponseQuery = httpClient.execute(httpPostQuery);
            String html = GetHtml.GetHtml(httpResponseQuery.getEntity().getContent(),"GB2312");
            List<Score> data = Parse(html);
            Message message=new Message();
            Bundle bundle=new Bundle();
            bundle.putSerializable("data", (Serializable) data);
            message.what=3;
            message.setData(bundle);
            ShowScore.mainHandler.sendMessage(message);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    public List<Score> Parse(String html) {
        List<Score> data = new ArrayList<>();
        Document document=Jsoup.parse(html);
        Elements trs=document.getElementById("Datagrid1").select("tr");
        for(int i=1;i<trs.size();i++){
            Elements tds=trs.get(i).select("td");
            Score score = new Score();
            score.setCouseId(tds.get(2).text());
            score.setCourse(tds.get(3).text());
            score.setCourseProperty(tds.get(4).text());
            score.setCredit(tds.get(6).text());
            score.setGPA(tds.get(7).text());
            score.setScore(tds.get(8).text());
            score.setRetestScore(tds.get(10).text());
            data.add(score);
        }
        return data;
    }

}

