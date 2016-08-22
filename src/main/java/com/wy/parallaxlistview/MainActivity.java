package com.wy.parallaxlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ParallaxLIstView mListView;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ParallaxLIstView) findViewById(R.id.id_lv);

        View header = LayoutInflater.from(this).inflate(R.layout.listview_header, null);
        mImageView = (ImageView) header.findViewById(R.id.iv);
        mListView.addHeaderView(header);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{
           "星期一","星期2","星期3",
                "星期4","星期5","星期6", "星期一","星期2",
                "星期3","星期4","星期5","星期6", "星期一",
                "星期2","星期3","星期4","星期5","星期6"
        });
        mListView.setAdapter(adapter);
        mListView.setParallaxImageView(mImageView);

    }
}
