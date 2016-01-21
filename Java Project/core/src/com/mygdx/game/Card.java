package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author muel2767
 */
public class Card 
{
    int x, y;
    int width=100;
    int mark;
    Sprite image, hidden;//=null;
    boolean clicked=false;
    public Card(int xt, int yt, Sprite img, int markT)
    {
        x=(int) (xt*250*(Gdx.graphics.getWidth()/1920.0f));
        y=(int) (yt*250*(Gdx.graphics.getHeight()/1080.0f));
        mark=markT;
        hidden=new Sprite(new Texture(Gdx.files.internal("Items/dirt.png")));
        image=new Sprite(img);
        image.setPosition(x, y);
        hidden.setPosition(x, y);
    }
    
    public void draw(SpriteBatch batch)
    {
        if(clicked==true)
            image.draw(batch);
        else
            hidden.draw(batch);
    }
    
    public boolean click(double xt, double yt)
    {
        //System.out.println("my x: "+x+" my y: "+y);
        if(xt>=x && xt<=x+width && yt>=y && yt<=y+width)
        {
            //System.out.println("TEST");
            clicked=true;
            return true;
        }
        clicked=false;
        return false;
    }
    
    public int getMark()
    {
        return mark;
    }
}