package com.wellingtonmb88.aprovado.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.wellingtonmb88.aprovado.R;
import com.wellingtonmb88.aprovado.utils.CommonUtils;
import com.wellingtonmb88.aprovado.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_layout)
    Toolbar mToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        WebView wv = (WebView) findViewById(R.id.webView);

        wv.loadUrl(getString(R.string.url_activity_about));

        setSupportActionBar(mToolbarLayout);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        backForResult();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String uriString =
                    "market://details?id=com.wellingtonmb88.aprovado";
            intent.setData(Uri.parse(uriString));
            startActivity(intent);

            return true;
        }
        if (id == android.R.id.home) {
            backForResult();
        }
        return super.onOptionsItemSelected(item);
    }

    private void backForResult() {
        if (getIntent().hasExtra(Constants.TabSharedPreferences.SELECTED_TAB)) {
            int result = getIntent().getIntExtra(Constants.TabSharedPreferences.SELECTED_TAB, 0);
            CommonUtils.backForResult(this, result);
        }
    }
}
