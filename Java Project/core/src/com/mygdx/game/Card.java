package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * These are used in the game MemoryGame and hold a location, image and size
 * They tell you if they have been clicked and if they have, they show a secondary image
 * @author muel2767
 */
public class Card 
{
    int x, y;
    int width=25;
    int mark;
    float xScale=4f, yScale=2.73f;
    Sprite image, hidden;//=null;
    boolean clicked;
    int attempted;

    /**
     * setup the image width, place the position
     * @param xt
     * @param yt
     * @param img
     * @param markT
     * @param xSpace
     * @param ySpace
     */
        public Card(float xt, float yt, Sprite img, int markT, float xSpace, float ySpace)
    {
        x=(int) (xt*xSpace*(Gdx.graphics.getWidth()/1920.0f))+35;
        y=(int) (yt*ySpace*(Gdx.graphics.getHeight()/1080.0f))+25;
        mark=markT;
        clicked=false;
        hidden=new Sprite(new Texture(Gdx.files.internal("Items/PlayingCard.png")));
        image=new Sprite(img);
        image.setPosition(x, y);
        image.setScale(xScale, yScale);
        hidden.setPosition(x, y);
        hidden.setScale(xScale, yScale);
        attempted=0;
    }
    
    /**
     * if it was clicked draw the image otherwise draw the back of the card
     * @param batch
     */
    public void draw(SpriteBatch batch)
    {
        if(clicked==true)
            image.draw(batch);
        else
            hidden.draw(batch);
    }
    
    /**
     * was the mouse inside of the bounds of the card
     * @param xt
     * @param yt
     * @return
     */
    public boolean click(double xt, double yt)
    {
        //System.out.println("my x: "+x+" my y: "+y);
        if(xt>=x-50*(Gdx.graphics.getWidth()/1920.0f) && xt<=x+(width*xScale-50)*(Gdx.graphics.getWidth()/1920.0f) && yt>=y-width*(Gdx.graphics.getWidth()/1920.0f) && yt<=y+width*yScale*(Gdx.graphics.getHeight()/1080.0f))
        {
            return true;
        }
        //clicked=false;
        return false;
    }
    
    /**
     * got clicked, so increment attempted
     * @param cl
     */
    public void setClicked(boolean cl)
    {
        if(clicked==false && cl)
            attempted++;
        clicked=cl;
        
    }    
    
    /**
     * get the id of the card
     * @return
     */
    public int getMark()
    {
        return mark;
    }
    
    /**
     * has this card been clicked more than once
     * @return
     */
    public boolean wasAttempted()
    {
        return attempted>1;
    }
}