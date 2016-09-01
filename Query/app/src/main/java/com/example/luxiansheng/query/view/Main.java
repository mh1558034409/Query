package com.example.luxiansheng.query.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.luxiansheng.query.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Activity {
    private GridView gridView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view=findViewById(R.id.myBg);
        view.setAlpha(0.7f);
        gridView=(GridView)findViewById(R.id.mainFunction);
        gridView.setAlpha(0.7f);
        int[] images={R.drawable.main_query,R.drawable.main_query,R.drawable.main_query,R.drawable.main_query,R.drawable.main_infor,R.drawable.main_set};
        String[] text={"教务成绩","四六级成绩","课表查询","教务考勤","教务信息","设置"};
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i=0;i<images.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("image",images[i]);
            map.put("text",text[i]);
            list.add(map);
        }
        SimpleAdapter gridViewAdapter=new SimpleAdapter(this,list,R.layout.gridviewadapter,new String[]{"image","text"},new int[]{R.id.functionImage,R.id.functionName});
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(Main.this);
                normalDialog.setTitle("提示");
                normalDialog.setIcon(android.R.drawable.ic_dialog_info);
                normalDialog.setMessage("此功能待开发中...");
                final Dialog dialog;
                Handler handler=new Handler();
                switch (position){
                    case 0:
                        Intent intent=new Intent(Main.this,Querying.class);
                        startActivity(intent);
                        Intent intentQuery=new Intent(Main.this,ShowScore.class);
                        startActivity(intentQuery);
                        break;
                    case 1:
                        Intent cet=new Intent(Main.this,Cet.class);
                        startActivity(cet);
                        break;
                    case 2:
                        dialog=normalDialog.show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },2000);
                        break;
                    case 3:
                        dialog=normalDialog.show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },2000);
                        break;
                    case 4:
                        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
                        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                        if (wifi||internet){
                            Intent inforIntent=new Intent(Main.this,ShowInfor.class);
                            startActivity(inforIntent);
                            Intent intent1=new Intent(Main.this,Querying.class);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(getApplicationContext(),"亲，网络出错啦",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 5:
                        dialog=normalDialog.show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },2000);
                        break;
                }
            }
        });
    }
}

