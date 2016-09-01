package com.example.luxiansheng.query.service;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.view.Login;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 传递参数，判断登录
 */
public class IsLogin extends Thread implements Address{
    private String stuNumber="";
    private String password;
    private String cookie;
    private String viewState;
    private String identifyCode;
    private Boolean isOk=false;
    public IsLogin(String stuNumber,String password,String identifyCode,String cookie,String viewState){
        this.stuNumber=stuNumber;
        this.password=password;
        this.identifyCode=identifyCode;
        this.cookie=cookie;
        this.viewState=viewState;

    }
    public void run() {

        HttpClient httpClient= new DefaultHttpClient();

        try {
            HttpPost httpPostLogin=new HttpPost(LoginUrl);
            httpPostLogin.setHeader("Cookie",cookie);
            List<NameValuePair> list=new ArrayList<>();
            list.add(new BasicNameValuePair("__VIEWSTATE", viewState));
            list.add(new BasicNameValuePair("txtUserName", stuNumber));
            list.add(new BasicNameValuePair("TextBox2", password));
            list.add(new BasicNameValuePair("txtSecretCode", identifyCode));
            list.add(new BasicNameValuePair("RadioButtonList1","学生"));
            list.add(new BasicNameValuePair("Button1", ""));
            list.add(new BasicNameValuePair("lbLanguage", ""));
            list.add(new BasicNameValuePair("hidPdrs", ""));
            list.add(new BasicNameValuePair("hidsc", ""));
            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(list,"GB2312");
            httpPostLogin.setEntity(entity);
            HttpParams params = new BasicHttpParams();
            params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
            httpPostLogin.setParams(params);
            HttpResponse httpResponseLogin=httpClient.execute(httpPostLogin);
            if(httpResponseLogin.getStatusLine().getStatusCode()==302){
                HttpGet httpGetMain=new HttpGet(MainUrl+stuNumber);
                httpGetMain.setHeader("Cookie",cookie);
                httpGetMain.setHeader("Referer",LoginUrl);
                HttpResponse httpResponseMain=httpClient.execute(httpGetMain);
                InputStream inputStream=httpResponseMain.getEntity().getContent();
                String html=GetHtml(inputStream);
                String stuName= Jsoup.parse(html).getElementById("xhxm").text();
                String[] year=null;
                //得到year
                HttpGet httpGet=new HttpGet(QueryScoreUrl + stuNumber + Name + stuName + ScoreTag);
                httpGet.setParams(params);
                httpGet.setHeader("Cookie",cookie);
                httpGet.setHeader("Referer",MainUrl+stuNumber);
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    String html1=GetHtml.GetHtml(httpResponse.getEntity().getContent(),"GB2312");
                    Document document= Jsoup.parse(html1);
                    Elements elements=document.select("#ddlXN>option");
                    year=new String[elements.size()-1];
                    int i=0;
                    for (Element element:elements ){
                        if (element.text().toString().equals("")==false){
                            year[i++]=element.text();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                Bundle bundle=new Bundle();
                isOk=true;
                bundle.putBoolean("isOk",isOk);
                bundle.putString("stuName",stuName);
                bundle.putString("cookie",cookie);
                bundle.putStringArray("year",year);
                message.what=1;
                message.setData(bundle);
                Login.handler.sendMessage(message);

            }else{
                isOk=false;
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isOk",isOk);
                message.what=1;
                message.setData(bundle);
                Login.handler.sendMessage(message);
                System.out.println("登录失败");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *  获得服务器返回的html
     */
    public String GetHtml(InputStream in) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        in.close();
        return new String(bos.toByteArray(), "GB2312");
    }
}
