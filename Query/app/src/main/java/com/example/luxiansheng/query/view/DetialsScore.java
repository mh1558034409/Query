package com.example.luxiansheng.query.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.luxiansheng.query.R;
import com.example.luxiansheng.query.model.Score;
import com.example.luxiansheng.query.service.SetListView;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为listView的每一个item创造界面
 */
public class DetialsScore extends Activity{
    private ListView listView;
    private TextView titleText;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_detialsscore);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
        ScrollView Scroll=(ScrollView)findViewById(R.id.Scroll);
        Scroll.smoothScrollTo(0,0);
        listView=(ListView)findViewById(R.id.details);
        titleText=(TextView)findViewById(R.id.titleText);
        SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
        String  data=preferences.getString("data",null);
        byte[] base64Bytes = Base64.decodeBase64(data.getBytes());
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        Score score=new Score();
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            score =(Score) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        final List<Map<String,Object>> list=new ArrayList<>();
        String[] left={"课程名","课程性质","课程代码","成绩","学分","绩点","补考成绩"};
        String[] right={score.getCourse(),score.getCourseProperty(),score.getCouseId(),
                score.getScore(),score.getCredit(),score.getGPA(),score.getRetestScore()};
        TextView InforTitle=(TextView)findViewById(R.id.InforTitle);
        InforTitle.setText(""+right[0]);
        titleText.setText("详细信息");
        for (int i=0;i<left.length;i++){
            Map<String ,Object> map=new HashMap<>();
            map.put("left",left[i]);
            map.put("right",right[i]);
            list.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(DetialsScore.this,list,R.layout.inforadapter,
                new String[]{"left","right"},new int[]{R.id.Left,R.id.Right});
        listView.setAdapter(adapter);
        SetListView.setListViewHeightBasedOnChildren(listView);
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetialsScore.this,ShowScore.class);
                startActivity(intent);
            }
        });

    }
}
