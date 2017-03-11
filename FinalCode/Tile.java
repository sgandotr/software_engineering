package com.example.nedtaylor.scramblegame;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by nedtaylor on 12/16/16.
 */

public class Tile {

    /**
     * instance for the bitmap of the Tile object
     */
    private Bitmap b;
    /**
     * index of the Tile object
     */
    private int i;

    /**
     *
     * An overloaded constructor that populates a Tile object with a bitmap and an int
     *
     * @param bm Bitmap sent in
     * @param id int sent in
     */
    public Tile(Bitmap bm, int id){
        this.b = bm;
        this.i = id;
    }

    /**
     * returns the bitmap of the Tile object
     * @return a Bitmap object associated with the Tile object
     */
    public Bitmap getBitmap(){
        return this.b;
    }

    /**
     * returns the id of the Tile object
     * @return the int associated with the Tile object
     */
    public int getId(){
        return this.i;
    }

    /**
     * sets bitmap to a passed in parameter
     * @param bit the parameter to set the bitmap to
     */
    public void setBitmap(Bitmap bit){
        this.b = bit;
    }

    /**
     * sets the id instance field on the tile object
     * @param id the parameter to set the id to
     */
    public void setId(int id){
        this.i = id;
    }


}
