package com.example.nedtaylor.scramblegame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by nedtaylor on 12/8/16.
 */

public class SizeScreen extends AppCompatActivity implements View.OnClickListener {
    /**
     * button to choose a 3x3 board
     */
    private Button b;
    /**
     * button to choose a 4x4 board
     */
    private Button b2;
    /**
     * button to choose a 5x5 board
     */
    private Button b3;
    /**
     * help button that sets an alert dialog
     */
    private Button help;
    /**
     * button to go back to the welcome activity
     */
    private Button back;
    /**
     * value to achieve entry into the gallery of photos
     */
    public static final int IMAGE_GALLERY_REQUEST = 20;
    /**
     * Bitmap to be parsed into chunks according to the difficulty
     * chosen
     */
    private Bitmap image;
    /**
     * the difficulty results in either 3, 4, or 5
     */
    private int numCols;

    /**
     *
     * the method that populates the screen as the user is navigated on to it,
     * centered on putting the buttons on the screen and setting onClickListeners
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GridLayout layout = new GridLayout(this);

        setContentView(layout);

        int count = 0;

        //sets the buttons' text, id, and onClickListener

        b = new Button(this);
        b.setOnClickListener(this);
        b.setText("3x3");
        b.setId(count+1);
        layout.addView(b);

        b2 = new Button(this);
        b2.setOnClickListener(this);
        b2.setText("4x4");
        b2.setId(count+2);
        layout.addView(b2);

        b3 = new Button(this);
        b3.setOnClickListener(this);
        b3.setText("5x5");
        b3.setId(count+3);
        layout.addView(b3);

        help = new Button(this);
        help.setOnClickListener(this);
        help.setText("Help");
        help.setId(count+4);
        layout.addView(help);

        back = new Button(this);
        back.setOnClickListener(this);
        back.setText("Back");
        back.setId(count + 5);
        layout.addView(back);
    }

    /**
     *
     * Method to find out which button was clicked and act accordingly
     *
     * @param v the View v
     */
    @Override
    public void onClick(View v) {

            if(v.getId() == b.getId()) {
                numCols = 3;
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturePath = picDir.getPath();
                Uri data = Uri.parse(picturePath);
                photoIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);

            }
            else if(v.getId() == b2.getId()) {
                numCols = 4;
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturePath = picDir.getPath();
                Uri data = Uri.parse(picturePath);
                photoIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
            }
            else if(v.getId() == b3.getId()) {
                numCols = 5;
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturePath = picDir.getPath();
                Uri data = Uri.parse(picturePath);
                photoIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoIntent, IMAGE_GALLERY_REQUEST);
            }
            else if(v.getId() == help.getId()) {

                AlertDialog alertDialog = new AlertDialog.Builder(SizeScreen.this).create();
                alertDialog.setTitle("Help");
                alertDialog.setMessage("You have chosen to solve a puzzle! This screen will determine the difficulty you would" +
                        " like to compete at\nEasy: 3x3\nMedium: 4x4\nHard 5x5\nThe next screen will be choosing your photo!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
            else if(v.getId() == back.getId()){
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }


    }

    /**
     *
     * This method accesses the camera gallery and upon the user choosing a photo,
     * it parses it into an appropriate number of bitmap pieces, and then sends that
     * arrayList to a static variable in ImageItem which is accessed in Game Screen
     *
     * @param requestCode requests access to gallery
     * @param resultCode contains whether or not the picture choosing was successful
     * @param data the data of the picture
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);


        //if
        if(resultCode == RESULT_OK){
            //
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri imgData = data.getData();
                InputStream input;

                try{

                    input = getContentResolver().openInputStream(imgData);
                    image = BitmapFactory.decodeStream(input);

                    ArrayList<Bitmap> test = new ArrayList<>();
                    test.add(image);

                    ArrayList<Bitmap> array = chunkImage(image, numCols);
                    ArrayList<Tile> tileArray = new ArrayList<>();

                    for(int i = 0; i < numCols*numCols; i++){
                        Tile t = new Tile(array.get(i), i);
                        tileArray.add(t);
                    }

                    ImageItem.bm = tileArray;

                    Intent i = new Intent(this, GameScreen.class);

                    i.putExtra("size", numCols);
                    startActivity(i);

                }catch(FileNotFoundException fo){
                    System.out.println("can't find image");
                }
            }
        }
    }
    /**
     *
     * Parses the bitmap into pieces based on the difficulty chosen by the
     * user and puts those pieces into an ArrayList<Bitmap>
     *
     * @param b the bitmap of the image
     * @param i a number associated with the difficulty the user chooses
     * @return the ArrayList<Bitmap> containing the parsed image
     */
    public ArrayList<Bitmap> chunkImage(Bitmap b, int i){

        ArrayList<Bitmap> alb = new ArrayList<>( (int) Math.pow(i,2));

        Bitmap scale = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        int chunkHeight = b.getHeight()/i;
        int chunkWidth = b.getWidth()/i;

        int yc = 0;

        for(int x=0; x<i; x++){
            int xc= 0;
            for(int y = 0; y < i; y++){

                alb.add(Bitmap.createBitmap(scale, xc, yc, chunkWidth, chunkHeight));
                xc += chunkWidth;
            }
            yc += chunkHeight;
        }
        return alb;
    }

}
