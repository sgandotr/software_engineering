package com.example.nedtaylor.scramblegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * This class represents the first screen of our application: the Welcome Screen
 */

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * used to access the camera if "take a picture" is selected
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Populates the WelcomeActivity screen according to the activity_welcome xml file
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button b = (Button) findViewById(R.id.button);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(this);

    }

    /**
     *
     * Based on which button was clicked, either access the camera
     * or navigate to a different screen
     *
     * @param v the View v
     */
    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.button:

                Intent intent = new Intent(this, SizeScreen.class);
                startActivity(intent);
                break;

            case R.id.button2:

                Intent intent2 = new Intent(this, SendScreen.class);
                startActivity(intent2);
                break;

            case R.id.button3:

                AlertDialog alertDialog = new AlertDialog.Builder(WelcomeActivity.this).create();
                alertDialog.setTitle("Help");
                alertDialog.setMessage("This is the Welcome Screen of the Scrambler! You have two options at this point: " +
                "do you want to solve a puzzle or send a picture to your friend? Solving a puzzle will bring you to the " +
                        "next screen where you pick the difficulty of your puzzle, while sending a picture will bring you" +
                        "to your gallery of pictures where you can select which photo to send!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;

            case R.id.button4:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
        }
    }

}
