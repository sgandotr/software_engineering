package com.example.nedtaylor.scramblegame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
/**
 * Created by Shayla Moore on 12/8/16.
 */

public class SendScreen extends Activity implements View.OnClickListener {

    /**
     * The method that populates the screen that the user sees upon its
     * creation.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Button sendButton = (Button) findViewById(R.id.send);
        sendButton.setOnClickListener(this);

        Button back = (Button) findViewById(R.id.backemail);
        back.setOnClickListener(this);

    }

    /**
     * Method to determine which button was clicked and act accordingly
     *
     * @param v View v
     */
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.send){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"   "});
            emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"    "});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Here's an Image for Your scramblepuzzle!");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Download the Attachment for your scramblepuzzle!");

            emailIntent.setType("text/plain");
            startActivity(Intent.createChooser(emailIntent, "Choose your email client.."));
        }
        else if(v.getId() == R.id.backemail){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }

    }
}