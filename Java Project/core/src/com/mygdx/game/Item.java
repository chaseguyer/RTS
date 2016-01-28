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
    public Item(Sprite s, int xt, int yt, String n)
    {
        image=s;
        x=xt;
        y=yt;
        name=n;
        image.setPosition(x, y);
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean clicked(int xt, int yt)
    {
        if(xt>=x && xt<=x+shift && yt>=y && yt<=y+shift)
            return true;
        return false;
    }
    
    public void draw(SpriteBatch batch)
    {
        image.draw(batch);
    }
}
