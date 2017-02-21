package me.shihao.xstatusbarhelper;

import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;

import me.shihao.library.XStatusBarHelper;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        XStatusBarHelper.forceFitsSystemWindows(this);
        XStatusBarHelper.immersiveStatusBar(this);
        XStatusBarHelper.setHeightAndPadding(this, toolbar);

        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int color = getResources().getColor(R.color.colorPrimary);
                toolbar.setBackgroundColor(Color.argb(scrollY > 255 ? 255 : scrollY, Color.red(color), Color.green
                        (color), Color.blue(color)));
            }
        });
    }
}
