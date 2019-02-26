package com.shengqf.flowlayout.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import com.shengqf.view.flowlayout.FlowLayout;
import com.shengqf.flowlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengqf
 * Email : shengqf@bsoft.com.cn
 * date : 2019/2/22
 * describe : https://github.com/xuexiangjys/XUI
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("士大夫士大夫");
            list.add("蒙多");
            list.add("司法所地方");
            list.add("是否");
            list.add("大幅度");
            list.add("的撒范德的");
            list.add("的撒范德萨的撒范德");
            list.add("士大");
            list.add("野第三方的撒范德萨佛挡杀佛");
            list.add("订单");
            list.add("都是所得税");
            list.add("所得税");
        }

        FlowLayout flowLayout = findViewById(R.id.flow_layout);
        for (int j = 0; j < 10; j++) {
            Button button = new Button(this);
            button.setText(list.get(j));
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,0,0);
            button.setLayoutParams(params);
            flowLayout.addView(button);
        }
    }


}
