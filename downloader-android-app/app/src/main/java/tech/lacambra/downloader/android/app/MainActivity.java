package tech.lacambra.downloader.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

  private TextView mTextMessage;
  private Button button;
  private RadioGroup ownerGroup;
  private Switch extractAudio;
  private EditText url;
  private EditText server;
  private SharedPreferences sharedPref;


  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          mTextMessage.setText(R.string.title_home);
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_notifications:
          mTextMessage.setText(R.string.title_notifications);
          return true;
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Context context = MainActivity.this;
    sharedPref = context.getSharedPreferences("downloader", Context.MODE_PRIVATE);
    String serverUri = sharedPref.getString("downloader_server", "none");

    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    mTextMessage = findViewById(R.id.message);
    button = findViewById(R.id.button);
    extractAudio = findViewById(R.id.extractAudio);
    ownerGroup = findViewById(R.id.owner);
    final OwnerSelector ownerSelector = new OwnerSelector(MainActivity.this, getNameOfRadioButtom(this, ownerGroup.getCheckedRadioButtonId()));
    url = findViewById(R.id.url);
    server = findViewById(R.id.server);

    server.setText(serverUri);
    ownerGroup.setOnCheckedChangeListener(ownerSelector);

    Intent intent = getIntent();
    StringBuilder total = new StringBuilder();
    if (intent.getAction().equalsIgnoreCase("android.intent.action.SEND")) {

      CharSequence urlFromIntent = intent.getClipData().getItemAt(0).getText();

      if (urlFromIntent != null) {
        url.setText(intent.getClipData().getItemAt(0).getText());
        intent.getClipData().getItemCount();
      } else {
//        Uri uri = intent.getClipData().getItemAt(0).getUri();
//        try {
//          InputStream inputStream = getContentResolver().openInputStream(uri);
//          BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
//          for (String line; (line = r.readLine()) != null; ) {
//            total.append(line).append('\n');
//          }
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
      }


//      getContentResolver().acquireContentProviderClient(intent.getClipData().getItemAt(0).getUri()).;
    }

    button.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("downloader_server", server.getText().toString());
        editor.apply();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObject payload = new JsonObject();
        payload.addProperty("owner", ownerSelector.getOwner());
        payload.addProperty("extractAudio", extractAudio.isChecked());
        payload.addProperty("url", url.getText().toString());

        JsonRequest stringRequest = createJsonRequest(payload);
        queue.add(stringRequest);
      }
    });
  }

  private JsonRequest createJsonRequest(JsonObject payload) {
    return new JsonRequest(
        Request.Method.POST,
        server.getText().toString(),
        payload.toString(),
        new Response.Listener<String>() {

          @Override
          public void onResponse(String response) {
            System.out.println(response);
            mTextMessage.setText(response);
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        mTextMessage.setText(error.getMessage());
      }
    }) {

      @Override
      public int compareTo(@NonNull Object o) {
        return 0;
      }

      @Override
      protected Response parseNetworkResponse(NetworkResponse response) {
        try {
          String jsonString =
              new String(
                  response.data,
                  HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
          return Response.success(
              jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
          return Response.error(new ParseError(e));
        }
      }
    };
  }

  static class OwnerSelector implements RadioGroup.OnCheckedChangeListener {

    private MainActivity mainActivity;
    private String owner;

    public OwnerSelector(MainActivity mainActivity, String defaultOwner) {
      this.mainActivity = mainActivity;
      owner = defaultOwner;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
      owner = getNameOfRadioButtom(mainActivity, checkedId);
    }

    public String getOwner() {
      return owner;
    }
  }

  private static String getNameOfRadioButtom(MainActivity mainActivity, int id) {
    RadioButton radioButton = mainActivity.findViewById(id);
    return radioButton.getText().toString().toLowerCase();
  }

}
