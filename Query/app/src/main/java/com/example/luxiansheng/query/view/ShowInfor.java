package com.example.luxiansheng.query.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.luxiansheng.query.R;
import com.example.luxiansheng.query.service.GetInfor;
import com.example.luxiansheng.query.service.SetListView;

/**
 * 展示学生基本信息
 */
public class ShowInfor extends Activity{
    public static Handler handler=new Handler();
    private ListView infor;
    private ImageView stuImage;
    private TextView titleText;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_infor);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
        ScrollView Scroll=(ScrollView)findViewById(R.id.Scroll);
        Scroll.smoothScrollTo(0,0);
        titleText=(TextView)findViewById(R.id.titleText);
        titleText.setText("个人信息");
        infor=(ListView)findViewById(R.id.infor);
        stuImage=(ImageView)findViewById(R.id.stuImage);
        final SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
        String cookie=preferences.getString("Cookie","");
        String stuNumber=preferences.getString("StuNumber","");
        final String stuName=preferences.getString("StuName","");
        new GetInfor(cookie,stuNumber,stuName).start();
        final List<Map<String,Object>> list=new ArrayList<>();
        handler=new Handler(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==4){
                    Bundle bundle=msg.getData();
                    String[] left={"学号","学院","专业","行政班","当前所在级","学历层次"};
                    Object[] right={
                            preferences.getString("StuNumber",""),
                            bundle.getString("xy"),
                            bundle.getString("zy"),
                            bundle.getString("xzb"),
                            bundle.getString("dqszj"),
                            bundle.getString("cc")
                    };
                   Bitmap bitmap=bundle.getParcelable("image");
                    TextView stuName=(TextView)findViewById(R.id.stuName);
                    if(bitmap==null){
                        Toast.makeText(getApplicationContext(),"图片加载失败",Toast.LENGTH_LONG).show();
                        stuImage.setImageDrawable(getDrawable(R.drawable.main_infor));
                        stuName.setText(preferences.getString("StuName",""));

                    }else{
                        stuImage.setImageBitmap(bitmap);
                        stuName.setText(preferences.getString("StuName",""));
                    }
                   for (int i=0;i<left.length;i++){
                       Map<String ,Object> map=new HashMap<>();
                       map.put("left",left[i]);
                       map.put("right",right[i]);
                       list.add(map);
                   }
                    Querying.instance.finish();
                    SimpleAdapter adapter=new SimpleAdapter(ShowInfor.this,list,R.layout.inforadapter,new String[]{"left","right"},new int[]{R.id.Left,R.id.Right});
                    infor.setAdapter(adapter);
                    SetListView.setListViewHeightBasedOnChildren(infor);
                }
            }
        };
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ShowInfor.this,Main.class);
                startActivity(intent);
            }
        });
    }
}
