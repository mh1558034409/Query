package com.example.luxiansheng.query.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luxiansheng.query.R;
import com.example.luxiansheng.query.model.Address;
import com.example.luxiansheng.query.service.GetCookieAndImage;
import com.example.luxiansheng.query.service.IsLogin;

import java.util.Arrays;
import java.util.HashSet;

public class Login extends Activity implements Address {
    private Button login;
    private EditText identifyCode;
    private EditText stuNumberText;
    private EditText passwordText;
    private String cookie;
    private String viewState;
    private Boolean isOk=false;
    public static Handler handler = new Handler();
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view=findViewById(R.id.myBg);
        view.setAlpha(0.8f);
        View view1=findViewById(R.id.myLogin);
        view1.setAlpha(0.7f);
        login = (Button) findViewById(R.id.Login);
        identifyCode = (EditText) findViewById(R.id.IdentifyCode);
        stuNumberText = (EditText) findViewById(R.id.StuNumberText);
        passwordText = (EditText) findViewById(R.id.PasswordText);
        GetCookieAndImage first=new GetCookieAndImage();
        first.start();
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        final boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        final boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Loading.class);
                startActivity(intent);
                if(!stuNumberText.getText().toString().trim().equals("")&&
                        !passwordText.getText().toString().trim().equals("")&&
                        !identifyCode.getText().toString().trim().equals("")){
                    IsLogin second=new IsLogin(stuNumberText.getText().toString().trim(),passwordText.getText().toString().trim(),
                            identifyCode.getText().toString().trim(),cookie,viewState);
                    second.start();
                }else {
                    if (stuNumberText.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),"学号不能为空",Toast.LENGTH_LONG).show();
                    }
                    if (passwordText.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_LONG).show();
                    }
                    if (identifyCode.getText().toString().trim().equals("")){
                        Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        final SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
        if(preferences.contains("StuNumber"))
            stuNumberText.setText(preferences.getString("StuNumber",""));
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Bundle bundle=msg.getData();
                        cookie=bundle.getString("cookie");
                        viewState=bundle.getString("viewState");
                        Bitmap bitmap=bundle.getParcelable("image");
                        Drawable image=new BitmapDrawable(bitmap);
                        if (wifi||internet){
                            if (image==null){
                                Drawable breakImage=getDrawable(R.drawable.identifycode_image);
                                image.setBounds(0,0,breakImage.getMinimumWidth(),breakImage.getMinimumHeight());
                                identifyCode.setCompoundDrawables(null,null,breakImage,null);
                                Toast.makeText(getApplicationContext(),"服务器故障，请稍后再试...",Toast.LENGTH_LONG).show();
                            }else{
                                image.setBounds(0,0,image.getMinimumWidth()+100,image.getMinimumHeight()+60);
                                identifyCode.setCompoundDrawables(null,null,image,null);
                            }
                        }else {
                            Drawable breakImage=getDrawable(R.drawable.identifycode_image);
                            image.setBounds(0,0,breakImage.getMinimumWidth(),breakImage.getMinimumHeight());
                            identifyCode.setCompoundDrawables(null,null,breakImage,null);
                            Toast.makeText(getApplicationContext(),"亲，网络出错啦",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        Bundle bundle1=msg.getData();
                        isOk=bundle1.getBoolean("isOk");
                        if (isOk){
                            Loading.instance.finish();
                            Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                            String stuName=bundle1.getString("stuName");
                            SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.clear();
                            editor.putString("StuNumber",stuNumberText.getText().toString());
                            editor.putString("Cookie",cookie);
                            editor.putString("StuName",stuName);
                            editor.putStringSet("yearText",new HashSet<>(Arrays.asList(bundle1.getStringArray("year"))));
                            editor.commit();
                            Intent intent=new Intent(Login.this,Main.class);
                            startActivity(intent);

                        }else{
                            Loading.instance.finish();
                            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_LONG).show();
                            new GetCookieAndImage().start();
                        }
                        break;
//                    case 5:
//                        Bundle yearBundle=msg.getData();
//                        String[] yearText=yearBundle.getStringArray("year");
//                        SharedPreferences.Editor editor=preferences.edit();
//                        Set<String> main_set=new HashSet<>(Arrays.asList(yearText));
//                        editor.putStringSet("yearText",main_set);
//                        editor.commit();
//
//                        break;
                }
            }
        };

    }
}


