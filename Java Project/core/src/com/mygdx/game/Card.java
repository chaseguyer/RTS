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
    int width=25;
    int mark;
    float xScale=2f, yScale=3f;
    Sprite image, hidden;//=null;
    boolean clicked;
    
    public Card(int xt, int yt, Sprite img, int markT, float xSpace, float ySpace)
    {
        x=(int) (xt*xSpace*(Gdx.graphics.getWidth()/1920.0f))+25;
        y=(int) (yt*ySpace*(Gdx.graphics.getHeight()/1080.0f))+25;
        mark=markT;
        clicked=false;
        hidden=new Sprite(new Texture(Gdx.files.internal("Items/PlayingCard.png")));
        image=new Sprite(img);
        image.setPosition(x, y);
        image.setScale(xScale, yScale);
        hidden.setPosition(x, y);
        hidden.setScale(xScale, yScale);
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
        if(xt>=x && xt<=x+width*xScale && yt>=y-width && yt<=y+width*yScale)
        {
            return true;
        }
        //clicked=false;
        return false;
    }
    
    public void setClicked(boolean cl)
    {
        clicked=cl;
    }    
    
    public int getMark()
    {
        return mark;
    }
}