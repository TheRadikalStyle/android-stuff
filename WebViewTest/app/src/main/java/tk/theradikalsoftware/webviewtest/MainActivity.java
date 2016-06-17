/*
*   Use of Spinner and WebView
*
*   David Ochoa "TheRadikalStyle"
*   17/06/2016
* */

package tk.theradikalsoftware.webviewtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "WebView implementation - TheRadikalStyle", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_values_val, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item  = String.valueOf(spinner.getItemAtPosition(position));
                String url;

                switch (item){
                    case "Marcianito 100% real no fake":
                        url = "http://i.makeagif.com/media/1-16-2015/I8TlUp.gif";
                        webview.loadUrl(url);
                        break;
                    case "Puke rainbow":
                        url = "http://www.fivestarsandamoon.com/wp-content/uploads/2016/04/gnome-rainbow-puke.gif";
                        webview.loadUrl(url);
                        break;
                    case "Travolta":
                        url = "http://i.makeagif.com/media/11-27-2015/RSIHUf.gif";
                        webview.loadUrl(url);
                        break;
                    case "Nyan Cat":
                        url = "https://cdn2.scratch.mit.edu/get_image/gallery/1273172_200x130.png?v=1433113952.54";
                        webview.loadUrl(url);
                        break;
                    case "Homer":
                        url = "http://media0.giphy.com/media/jUwpNzg9IcyrK/giphy.gif";
                        webview.loadUrl(url);
                        break;
                    case "Universe Secrets":
                        url = "http://stream1.gifsoup.com/view3/3399184/rick-roll-o.gif";
                        webview.loadUrl(url);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
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

        return super.onOptionsItemSelected(item);
    }
}
