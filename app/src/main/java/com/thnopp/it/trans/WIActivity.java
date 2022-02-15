package com.thnopp.it.trans;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WIActivity extends Activity {
    private WebView webView;
    DatabaseHelper db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi);

        db = DatabaseHelper.getInstance(getApplicationContext());


      /*  Button logout = (Button)findViewById(R.id.buttonBack);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MenuWIActivity.class);
                startActivity(intent);

            }
        });*/


        String s = getIntent().getStringExtra("WI");
        String id = getIntent().getStringExtra("ID");

        if (id==null){

        }else{
            db.updateWIStatus(id);
        }
        db.closeDB();
        webView = (WebView) findViewById(R.id.webView1);
        webView.setInitialScale(1);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+s);

      /*  webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);*/



        //webView.loadUrl(s);




    }

}
