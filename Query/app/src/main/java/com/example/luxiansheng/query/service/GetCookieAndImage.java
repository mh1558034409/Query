package com.example.luxiansheng.query.service;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.view.Login;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


/**
 * 获得cookie和验证码
 */
public class GetCookieAndImage extends Thread implements Address{
    private String viewState="";
    private String cookie="";
    public void run() {
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(ImageUrl);

        try {
            HttpResponse httpResponse=  httpClient.execute(httpGet);
            cookie=httpResponse.getFirstHeader("Set-Cookie").getValue();
            viewState=GetViewState.Get(LoginUrl,"","");
            Bitmap image= BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
            Bundle data=new Bundle();
            data.putString("cookie",cookie);
            data.putString("viewState",viewState);
            data.putParcelable("image",image);
            Message message=new Message();
            message.what=0;
            message.setData(data);
            Login.handler.sendMessage(message);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
