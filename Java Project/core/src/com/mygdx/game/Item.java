/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author muel2767
 */
public class Item 
{
    Sprite image;
    int x,y;
    String name;
    int shift=25;
    float xScale=2, yScale=3;
    
    //setup and scale the image, name the item and place it on the screen
    public Item(Sprite s, int xt, int yt, String n)
    {
        image=s;
        x=xt;
        y=yt;
        name=n;
        image.setPosition(x, y);
        image.setScale(xScale, yScale);
    }
    
    public String getName()
    {
        return name;
    }
    
    //is the bounds of the mouse inside of the item
    public boolean clicked(int xt, int yt)
    {
        if(xt>=x-shift && xt<=x+shift*xScale && yt>=y-shift && yt<=y+shift*yScale-shift)
            return true;
        return false;
    }
    
    //draw this objects image
    public void draw(SpriteBatch batch)
    {
        image.draw(batch);
    }
}
