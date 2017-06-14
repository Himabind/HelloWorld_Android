package com.example.ideas.helloworld;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class MainActivity extends AppCompatActivity {

    Button bgetAPIJson ;
    TextView display, display1 ;
    String value ="";
    String responseCode ="";
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Trial code to make http calls and parse json responses and display the dynamic content on App UI
        bgetAPIJson = (Button) findViewById(R.id.bgetAPIJson);
        display = (TextView) findViewById(R.id.tvdisplayJson);
        display1 = (TextView) findViewById(R.id.tvdisplayInfo);

        bgetAPIJson.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                new Thread(new Runnable() {
                    public void run() {
                           try {
                                // Create URL
                                 URL githubEndpoint = new URL("https://api.github.com/");

                                  // Create connection
                                 HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
                                 myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
                                 myConnection.setRequestProperty("Accept", "application/json");
                                   // myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                                // myConnection.setRequestProperty("Contact-Me",
                                // "hima@example.com");

                                    responseCode = ""+myConnection.getResponseCode();
                                    if (myConnection.getResponseCode() == 200) {
                                        // Success
                                        // Further processing here
                                        InputStream responseBody = myConnection.getInputStream();
                                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                                        jsonReader.beginObject(); // Start processing the JSON object
                                        while (jsonReader.hasNext()) { // Loop through all keys
                                            String key = jsonReader.nextName(); // Fetch the next key
                                            if (key.equals("organization_url")) { // Check if desired key
                                                // Fetch the value as a String
                                                 value = "actualValue " + jsonReader.nextString();

                                                break; // Break out of the loop
                                            } else {
                                                jsonReader.skipValue(); // Skip values of other keys
                                            }
                                        }
                                        jsonReader.close();
                                    } else {
                                        // Error handling code goes here
                                        value = "inelse " ;
                                    }
                                    myConnection.disconnect();

                                } catch (Exception e) {
                                    //myConnection.disconnect();
                                  //  display.setText("FailedAgain " + e.toString());
                                    value = "Exception " + e.toString();

                                }


                    }
                }).start();

                display.setText("Status " + value);
                display1.setText("responsecode " + responseCode);

            }
        });
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
        if (id == R.id.menu_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
