package com.example.luxiansheng.query.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luxiansheng.query.R;
import com.example.luxiansheng.query.model.CetScore;
import com.example.luxiansheng.query.service.GetCet;
import com.example.luxiansheng.query.service.SetListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建四六级成绩展示页面
 */
public class Cet extends Activity {
    public static Handler handler=new Handler();
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cetscore);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
        ScrollView Scroll=(ScrollView)findViewById(R.id.Scroll);
        Scroll.smoothScrollTo(0,0);
        Button back= (Button) findViewById(R.id.back);
        final ListView listView=(ListView)findViewById(R.id.CetScore);
        final List<Map<String ,Object>> list=new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(Cet.this,Main.class);
                startActivity(intent);
            }
        });
        TextView titleText=(TextView)findViewById(R.id.titleText);
        final TextView name=(TextView)findViewById(R.id.name);
        final TextView number=(TextView)findViewById(R.id.number);
        titleText.setText("四六级查询");
        Button select=(Button)findViewById(R.id.query);
        final SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
        if(preferences.contains("CetNumber")){
            number.setText(preferences.getString("CetNumber",""));

        }
        select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
                boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                if(wifi||internet){
                    String nameText=name.getText().toString().trim();
                    String numberText=number.getText().toString().trim();
                    list.clear();
                    SimpleAdapter adapter=new SimpleAdapter(Cet.this,list,R.layout.inforadapter,new String[]{"left","right"},new int[]{R.id.Left,R.id.Right});
                    listView.setAdapter(adapter);
                    SetListView.setListViewHeightBasedOnChildren(listView);
                    if (numberText.matches("\\d{15}")){
                        if(nameText.length()!=0){
                            Pattern pattern=Pattern.compile("[\\u4E00-\\u9FA5]*");
                            Matcher m = pattern.matcher(nameText);
                            if (m.matches()){
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("CetNumber",numberText);
                                editor.commit();
                                new GetCet(numberText,nameText).start();
                                Intent query=new Intent(Cet.this,Querying.class);
                                startActivity(query);
                            }else {
                                Toast.makeText(getApplicationContext(),"请输入正确的名字",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"请输入姓名",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Querying.instance.finish();
                        Toast.makeText(getApplicationContext(),"请输入正确的准考证号",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText( getApplicationContext(),"亲，网络出错啦",Toast.LENGTH_LONG).show();
                }
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==10){
                    if (msg.getData().containsKey("IsOK")){
                        Querying.instance.finish();
                        Toast.makeText(getApplicationContext(),"您输入的信息有误，请核对",Toast.LENGTH_LONG).show();
                    }else {
                        CetScore cetScore= (CetScore) msg.getData().getSerializable("cetScore");
                        String[] left= {"姓名","学校","考试级别",
                                "准考证号","考试时间","总分",
                                "听力","阅读","翻译和写作"};
                        String[] right={cetScore.getName(),cetScore.getSchool(),cetScore.getType(),
                                cetScore.getNumber(),cetScore.getTime(),cetScore.getTotalScore(),
                                cetScore.getListening(),cetScore.getReading(),cetScore.getWrite()};
                        if(Integer.parseInt(right[5])>425){
                            Toast.makeText(getApplicationContext(),"恭喜小主人（＾Ｏ＾）",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"主人，还要加油哦(T . T)",Toast.LENGTH_LONG).show();
                        }
                        for (int i=0;i<left.length;i++){
                            Map<String ,Object> map=new HashMap<>();
                            map.put("left",left[i]);
                            map.put("right",right[i]);
                            list.add(map);
                        }
                        Querying.instance.finish();
                        SimpleAdapter adapter=new SimpleAdapter(Cet.this,list,R.layout.inforadapter,new String[]{"left","right"},new int[]{R.id.Left,R.id.Right});
                        listView.setAdapter(adapter);
                        SetListView.setListViewHeightBasedOnChildren(listView);
                    }
                }
            }
        };
    }

}
