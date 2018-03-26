package klecet.cn.sudhir.cnklecet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1>please check your Internet Connection</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";
    ProgressBar progressBar;
    ImageButton reload, settingsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        reload = (ImageButton) findViewById(R.id.btn_reoload);
        settingsbtn = (ImageButton) findViewById(R.id.btn_settings);

        reload.setVisibility(View.INVISIBLE);
        settingsbtn.setVisibility(View.INVISIBLE);

        webView = (WebView) findViewById(R.id.webview_sportee);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //The data where we put all our stuff
        String cacheDir = getDir("WebView", Context.MODE_WORLD_WRITEABLE).getAbsolutePath();
        WebSettings settings = webView.getSettings();
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheEnabled(true);
//        settings.setAppCacheMaxSize(pAppCacheSize);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new AppWebViewClients(progressBar));
        if (haveNetworkConnection(progressBar)) {
            MainActivity.this.webView.loadUrl("http://cn-15cs52.blogspot.in/");

        } else {
            reload.setVisibility(View.VISIBLE);
            settingsbtn.setVisibility(View.VISIBLE);
            MainActivity.this.webView.loadData(html, "text/html", "UTF-8");
            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
        }

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection(progressBar)) {
                    settingsbtn.setVisibility(View.INVISIBLE);
                    reload.setVisibility(View.INVISIBLE);
                    MainActivity.this.webView.loadUrl("http://cn-15cs52.blogspot.in/");
                } else {
                    settingsbtn.setVisibility(View.VISIBLE);
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    webView.loadData(html, "text/html", "UTF-8");
                    Toast.makeText(getApplicationContext(), "Going to Settings", Toast.LENGTH_LONG).show();
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection(progressBar)) {
                    reload.setVisibility(View.INVISIBLE);
                    settingsbtn.setVisibility(View.INVISIBLE);
                    MainActivity.this.webView.loadUrl("http://cn-15cs52.blogspot.in/");
                } else {
                    reload.setVisibility(View.VISIBLE);
                    webView.loadData(html, "text/html", "UTF-8");
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;


        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar = progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
            super.onReceivedError(view, errorCode, description, failingUrl);
            reload.setVisibility(View.VISIBLE);
            settingsbtn.setVisibility(View.VISIBLE);
            webView.loadData(html, "text/html", "UTF-8");
            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();

        }

    }

    private boolean haveNetworkConnection(ProgressBar progressBar) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        this.progressBar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            webView.reload();
        }
        if (id == R.id.action_zoomin) {
            webView.zoomIn();
        }
        if (id == R.id.action_zoomout) {
            webView.zoomOut();
        }
        if (id == R.id.action_forward) {
            webView.goForward();
        }
        if (id == R.id.action_backward) {
            webView.goBack();
        }       return super.onOptionsItemSelected(item);
    }
}
