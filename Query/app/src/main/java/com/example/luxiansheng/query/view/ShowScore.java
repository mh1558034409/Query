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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luxiansheng.query.R;
import com.example.luxiansheng.query.model.Score;
import com.example.luxiansheng.query.service.QueryScore;
import com.example.luxiansheng.query.service.SetListView;
import com.google.gson.Gson;


import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowScore extends Activity {
    private ListView ScoreView;
    public static Handler mainHandler = new Handler();
    private Spinner year;
    private Spinner term;
    private Button query;
    private TextView titleText;
    private List<Score> list1=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_score);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_title);
        final ScrollView Scroll=(ScrollView)findViewById(R.id.Scroll);
        Scroll.smoothScrollTo(0,0);
        ScoreView = (ListView) findViewById(R.id.ScoreView);
        year=(Spinner)findViewById(R.id.year);
        term=(Spinner)findViewById(R.id.term);
        query=(Button)findViewById(R.id.query);
        titleText=(TextView)findViewById(R.id.titleText);
        titleText.setText("成绩查询");
        final List<Map<String, Object>> list=new ArrayList<>();
        final SharedPreferences preferences=getSharedPreferences("MyFile",MODE_PRIVATE);
        Object[] yearTextTemp= preferences.getStringSet("yearText",null).toArray();
        String[] yearText=new String[yearTextTemp.length];
        for (int i=0;i<yearText.length;i++)
            yearText[i]=""+yearTextTemp[i];
        ArrayAdapter arrayAdapter=new ArrayAdapter(ShowScore.this,android.R.layout.simple_spinner_item,yearText) ;
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(arrayAdapter);
        if (preferences.contains("year")&&preferences.contains("term")){
            year.setSelection(getInt(preferences.getString("year",""),yearText));
            term.setSelection(getInt(preferences.getString("term",""),new String[]{"1","2"}));
        }
        if (preferences.contains("totalData")){
            String total=preferences.getString("totalData","");
            org.json.JSONArray array = null;
            try {
                array=new JSONArray(total);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list1.clear();
            for (int i=0;i<array.length();i++){
                Score score=new Score();
                org.json.JSONObject json= null;
                try {
                    json =array.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    score.setCourse(""+json.getString("Course"));
                    score.setCourseProperty(""+json.getString("CourseProperty"));
                    score.setCouseId(""+json.getString("CouseId"));
                    score.setCredit(""+json.getString("Credit"));
                    score.setGPA(""+json.getString("GPA"));
                    score.setRetestScore(""+json.getString("RetestScore"));
                    score.setScore(""+json.getString("Score"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list1.add(score);
            }
            for (int i=0;i<list1.size();i++){
                Map<String, Object> map1 = new HashMap<>();
                map1.put("First","课程名："+list1.get(i).getCourse());
                map1.put("Second","成绩："+list1.get(i).getScore());
                map1.put("Third","学分："+list1.get(i).getCredit());
                list.add(map1);
            }
            SimpleAdapter ScoreAdapter = new SimpleAdapter(ShowScore.this, list, R.layout.scoreadapter, new String[]{"First", "Second","Third"}, new int[]{R.id.First, R.id.Second,R.id.Third});
            ScoreView.setAdapter(ScoreAdapter);
            SetListView.setListViewHeightBasedOnChildren(ScoreView);


        }
        SimpleAdapter ScoreAdapter = new SimpleAdapter(ShowScore.this, list, R.layout.scoreadapter, new String[]{"First", "Second","Third"}, new int[]{R.id.First, R.id.Second,R.id.Third});
        ScoreView.setAdapter(ScoreAdapter);
        year.setPrompt("请选择学年");
        term.setPrompt("请选择学期");
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 3:
                        list.clear();
                        Bundle bundle = msg.getData();
                        list1= (ArrayList<Score>) bundle.getSerializable("data");
                        for (int i=0;i<list1.size();i++){
                            Map<String, Object> map1 = new HashMap<>();
                            map1.put("First","课程名："+list1.get(i).getCourse());
                            map1.put("Second","成绩："+list1.get(i).getScore());
                            map1.put("Third","学分："+list1.get(i).getCredit());
                            list.add(map1);
                        }
                        SimpleAdapter ScoreAdapter = new SimpleAdapter(ShowScore.this, list, R.layout.scoreadapter, new String[]{"First", "Second","Third"}, new int[]{R.id.First, R.id.Second,R.id.Third});
                        Querying.instance.finish();
                        ScoreView.setAdapter(ScoreAdapter);
                        SetListView.setListViewHeightBasedOnChildren(ScoreView);
                        Gson gson=new Gson();
                        String totalData=gson.toJson(list1);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("totalData",totalData);
                        editor.commit();
                        break;
                }

            }
        };
        ScoreView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Score score=list1.get(position);
                SharedPreferences.Editor editor =preferences.edit();
                editor.putString("year",year.getSelectedItem().toString());
                editor.putString("term",term.getSelectedItem().toString());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(score);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String data = new String(Base64.encodeBase64(baos.toByteArray()));
                editor.putString("data",data);
                editor.commit();
                Intent details=new Intent(ShowScore.this,DetialsScore.class);
                startActivity(details);
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
                boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                if(wifi||internet){
                    String yearText=year.getSelectedItem().toString();
                    String termText=term.getSelectedItem().toString();
                    list.clear();
                    SimpleAdapter ScoreAdapter = new SimpleAdapter(ShowScore.this, list, R.layout.scoreadapter, new String[]{"First", "Second","Third"}, new int[]{R.id.First, R.id.Second,R.id.Third});
                    ScoreView.setAdapter(ScoreAdapter);
                    SetListView.setListViewHeightBasedOnChildren(ScoreView);
                    Intent intent1=new Intent(ShowScore.this,Querying.class);
                    startActivity(intent1);
                    new QueryScore(yearText,termText,preferences.getString("Cookie",""),preferences.getString("StuNumber",""),preferences.getString("StuName","")).start();
                }else {
                    Toast.makeText(getApplicationContext(),"亲，网络出错啦",Toast.LENGTH_LONG).show();
                }
            }
        });
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowScore.this,Main.class);
                startActivity(intent);

            }
        });

    }

    public int getInt(String string,String[] a){
        for (int i=0;i<a.length;i++){
            if (string.equals(a[i]))
                return i;
        }
        return 0;
    }

}