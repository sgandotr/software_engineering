package com.example.nedtaylor.scramblegame;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nedtaylor on 12/8/16.
 */

public class GameScreen extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener {


    /**
     * the grid layout the image views will populate
     */
    private GridLayout layout;
    /**
     * the arrayList of bitmaps in their unscrambled order
     */
    private ArrayList<Tile> original;
    /**
     * the arrayList of bitmaps after they have been re-arranged
     */
    private ArrayList<Tile> scramble;
    /**
     * the 2D array of Tile objects
     */
    private Tile[][] tilearray;
    /**
     * the 2D array of imageViews that actually show the user the image
     */
    private ImageView[][] iv;
    /**
     * the dimensions of the grid
     */
    private int numCells;
    /**
     * the help button that displays the instructions
     */
    private Button help;
    /**
     * the quit button that solves the puzzle for a user
     */
    private Button quit;
    /**
     * the home button that sends the user back to the WelcomeActivity
     */
    private Button home;

    /**
     * This method displays the original state of the Screen, i.e. the
     * first screen the user sees
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);//stretch if bitmaps don't take whole screen


        //get dimensions from size screen
        numCells = getIntent().getIntExtra("size", 0 );

        iv = new ImageView[numCells][numCells];
        tilearray = new Tile[numCells][numCells];

        int count = 0;


        //set parameters of grid layouts
        layout = (GridLayout) findViewById(R.id.grid);

        layout.setColumnCount(numCells);
        layout.setRowCount(numCells);
        layout.setId(count);
        layout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        layout.setRowOrderPreserved(false);
        layout.setColumnOrderPreserved(false);
        layout.setOnDragListener(this);

        original = new ArrayList<Tile>();
        scramble = new ArrayList<Tile>();

        ImageView img;
        Bitmap b = null;

        //
        original = ImageItem.bm;

        //get the image scrambled
        for(int i = 0; i < original.size(); i++){
            scramble.add(i, original.get(i));
        }

        Collections.shuffle(scramble);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;

        int bmWidth = width / numCells;
        int usefulHeight = bmWidth * (scramble.get(0).getBitmap().getHeight()/scramble.get(0).getBitmap().getWidth());

        int c = 0;

        //set grid layout of image views
        for(int i = 0; i < layout.getColumnCount(); i++){
            for(int j = 0; j < layout.getRowCount(); j++){

                tilearray[i][j] = scramble.get(c);

                iv[i][j] = new ImageView(this);

                iv[i][j].setImageBitmap(tilearray[i][j].getBitmap());

                iv[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                layout.addView(iv[i][j], bmWidth, usefulHeight);

                iv[i][j].setOnLongClickListener(this);

                c++;

            }
        }
        //set buttons and click listeners
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        help = (Button) findViewById(R.id.Help);
        help.setOnClickListener(this);
        quit = (Button) findViewById(R.id.Quit);
        quit.setOnClickListener(this);

    }

    /**
     * TAKEN FROM: http://patrick-iv.github.io/2015/05/04/drag-n-drop/
     *
     * Utilized to facilitate the user dragging a tile of the GridLayout
     * and dropping it
     *
     * @param v the View
     * @param event an Event that recognizes that a drag is happening
     * @return boolean true
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {

        final View view = (View) event.getLocalState();
        switch (event.getAction()) {
            //while dragging to a given index
            case DragEvent.ACTION_DRAG_LOCATION:

                if (view == v)
                    return true;

                final int index = calculateNewIndex(event.getX(), event.getY());

                layout.removeView(view);

                layout.addView(view, index);
                break;

            //when the tile is dropped
            case DragEvent.ACTION_DROP:

                view.setVisibility(View.VISIBLE);

                break;

            //when the drag has stopped
            case DragEvent.ACTION_DRAG_ENDED:
                if (!event.getResult()) {
                    view.setVisibility(View.VISIBLE);
                }
                //check for a win condition
                if(isWon(scramble)){
                    Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
                }

                break;
            }

        return true;
    }

    /**
     * Taken from: http://patrick-iv.github.io/2015/05/04/drag-n-drop/
     *
     * Calculating the new index of the tile that is being dragged
     *
     * @param x the x value of the tile being dragged
     * @param y the y value of the tile being dragged
     * @return int - the new index of the tile after being dragged
     */
    public int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = layout.getWidth() / numCells;
        final int column = (int)(x / cellWidth);

        // calculate which row to move to
        final float cellHeight = layout.getHeight() / numCells;
        final int row = (int)Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * numCells + column;
        if (index >= layout.getChildCount()) {
            index = layout.getChildCount() - 1;
        }

        return index;
    }

    /**
     *
     * Taken from: http://patrick-iv.github.io/2015/05/04/drag-n-drop/
     *
     * Recognizes a long click by the user (or press with a finger)
     * and raises the tile above the GridLayout, allowing for it to be
     * dragged
     *
     * @param v the View v
     * @return boolean true
     */
    @Override
    public boolean onLongClick(View v) {

        final ClipData data = ClipData.newPlainText("", "");

        //elevates the tile off the grid layout and allows for it to be dragged while being held down

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

        v.startDrag(data, shadowBuilder, v, 0);
        v.setVisibility(View.INVISIBLE);

        return true;

    }

    /**
     *
     * Checks if the array has been won or not
     *
     * @param a the arrayList of the current board
     * @return true if a = original and false otherwise
     */
    public boolean isWon(ArrayList<Tile> a){

        int win = 0;

        int count = 0;
        int bs = 0;


        //if every index of the scrambled arrayList equals the same index of the original arraylist
        //return true
        for(int i = 0; i < numCells*numCells; i++) {

            if (a.get(count).getId() != original.get(count).getId()) {
                System.out.println(a.get(count) + " " + original.get(count).getId());
                return false;
            }
            count++;
        }

        return true;
    }

    /**
     *
     * Based on a button click, do different tasks
     *
     * @param v the View v
     */
    @Override
    public void onClick(View v) {

        //if button click = help, set alert dialog
        if(v.getId() == help.getId()){

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Help");
            alertDialog.setMessage("You've made it to the game screen. Hold your finger down on any tile you " +
                    "want to move and drag it to the cell desired. Good luck solving! Press QUIT to have the puzzle " +
                    "solved for you or press HOME to return to the home screen.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        //if button click = home, go back to welcome screen
        else if(v.getId() == home.getId()){

            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);

        }
        //if button click = quit, solve the puzzle for them
        else if(v.getId() == quit.getId()){

            int count = 0;

            for(int i = 0; i < numCells; i++){
                for(int j = 0; j < numCells; j++){
                    iv[i][j].setImageBitmap(original.get(count).getBitmap());
                    count++;
                }
            }

        }

    }

}
