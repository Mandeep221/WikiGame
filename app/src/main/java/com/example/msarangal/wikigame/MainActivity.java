package com.example.msarangal.wikigame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private LinearLayout textParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // text = (TextView) findViewById(R.id.tvText);
        textParent = (LinearLayout) findViewById(R.id.textParent);
        apiCall();
    }

    public void apiCall() {
        String BASE_URL = "http://en.wikipedia.org/w/api.php?action=query";
        String QUERY = "&prop=revisions&titles=Threadless&rvprop=content&format=json&rvsection=0&rvparse=0";
        String URL = "http://en.wikipedia.org/w/api.php?action=query&prop=extracts&titles=Africa&format=json&exintro=1";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject parent = new JSONObject(response.body().string());
                    JSONObject query = parent.getJSONObject("query");
                    JSONObject pages = query.getJSONObject("pages");
                    JSONObject pageId = pages.getJSONObject(pages.keys().next());
                    final String content = pageId.getString("extract");
                    Log.d("test", Html.fromHtml(content).toString());

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = Html.fromHtml(content).toString();
                            //  String str = "My name is mandeep sarangal";
                            StringTokenizer stringTokenizer = new StringTokenizer(str, ".", false);
                            // String[] lines = str.split(" ", 2);
                            // text.setText(TextUtils.join("+", stringTokenizer.nextToken()));
                            for (int i = 0; i < ((ViewGroup) textParent).getChildCount(); i++) {

                                TextView tv = (TextView) ((ViewGroup) textParent).getChildAt(i);
                                tv.setText(stringTokenizer.nextToken());
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
