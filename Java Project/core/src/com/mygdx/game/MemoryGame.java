package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

/**
 *
 * @author muel2767
 * leave cards flipped for 1-5 seconds
 * vertical/horizontal/diagonal 
 * background solid or designed 
 */
public class MemoryGame extends ApplicationAdapter implements Screen, InputProcessor
{
    SpriteBatch batch;
    Texture img;
    boolean first=true;
    Card[][] deck;
    int cardPairs=7;
    double numRow=3.5;
    int mark=-1;
    Vector2 one=new Vector2(-1,-1);
    Vector2 two=new Vector2(-1,-1);
    int hitMark=0;
    long counter=0;
    int time=3;
    //Vector2 two=new Vector2(-2,-2);
    
    @Override
    public void show ()
    {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        Gdx.input.setInputProcessor(this);

        batch = new SpriteBatch();
    }

    @Override
    public void render (float f) 
    {
        if(first)
        {
            first=false;
            cardGame(cardPairs);
        }
        memoryGameLogic(cardPairs);
        if(running(cardPairs)==false)
            first=true;
            //Gdx.app.exit();
    }

    public boolean running(int cardPairs)
    {
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<cardPairs/numRow; ++y)
                if(deck[x][y]!=null) 
                    return true;
        return false;
    }
    
    public void cardGame(double cardPairs)
    {
        Sprite one=new Sprite(new Texture(Gdx.files.internal("Items/redBox.png")));
        Sprite two=new Sprite(new Texture(Gdx.files.internal("Items/cross.png")));
        Sprite three=new Sprite(new Texture(Gdx.files.internal("Items/greenBox.png")));
        Sprite four=new Sprite(new Texture(Gdx.files.internal("Items/delete.png")));
        Sprite five=new Sprite(new Texture(Gdx.files.internal("Items/plus.png")));
        Sprite six=new Sprite(new Texture(Gdx.files.internal("Items/Lemon.png")));
        Sprite seven=new Sprite(new Texture(Gdx.files.internal("Items/lemonSeed.png")));
        //int cardPairs=3;
        /*Card[][] */deck=new Card[(int)cardPairs][(int)(cardPairs/numRow)];
        int [][] deckFill= new int[(int)cardPairs][(int)(cardPairs/numRow)];
        Random rand=new Random();
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(cardPairs/numRow); ++y)
                deckFill[x][y]=0;
        
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(cardPairs/numRow); ++y)
            {
                Sprite temp=null;
                boolean taken=true;
                int num=0;
                while(taken==true)
                {
                    num=rand.nextInt((int) cardPairs)+1;
                    int count=0;
                    for(int a=0; a<cardPairs; ++a)
                        for(int b=0; b<cardPairs/numRow; ++b)
                            if(deckFill[a][b]==num)
                                count++;
                    if(count<2)
                        taken=false;
                }
                switch(num)
                {
                    case 1:
                        temp=one;
                        break;
                    case 2:
                        temp=two;
                        break;
                    case 3:
                        temp=three;
                        break;
                    case 4:
                        temp=four;
                        break;
                    case 5:
                        temp=five;
                        break;
                    case 6:
                        temp=six;
                        break;
                    case 7:
                        temp=seven;
                        break;
                }
                deck[x][y]=new Card(x,y,temp, num, 250, 250);
                deckFill[x][y]=num;
            }
    }
    
    public void memoryGameLogic(int cardPairs)
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<cardPairs/numRow; ++y)
                if(deck[x][y]!=null)
                {   
                    deck[x][y].draw(batch);
                }
        if(two.x!=-1 && System.currentTimeMillis()-counter>time*1000)
        {
            deck[(int)one.x][(int)one.y].setClicked(false);
            deck[(int)two.x][(int)two.y].setClicked(false);
            if(mark==deck[(int)two.x][(int)two.y].getMark())
            {
                deck[(int)one.x][(int)one.y]=null;
                deck[(int)two.x][(int)two.y]=null;
                System.out.println("purge");
            }
            one.x=-1;
            one.y=-1;
            two.x=-1;
            two.y=-1;
            mark=-1;
            System.out.println("clean");
        }
//          
        batch.end();
        if(Gdx.input.isKeyJustPressed(Keys.Q))
            Gdx.app.exit();
        
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched() && two.x==-1)//if(Gdx.input.isKeyJustPressed(Keys.F))
        {
            //System.out.println("click at x: "+Gdx.input.getX() +" and y: "+(Gdx.graphics.getHeight()-Gdx.input.getY()));
            for(int x=0; x<cardPairs; ++x)
                for(int y=0; y<cardPairs/numRow; ++y)
                    if(deck[x][y]!=null)
                    if(deck[x][y].click(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()))
                    {
                        //System.out.println("mark= "+mark+" vs "+deck[x][y].getMark()+ "x: "+x+" y: "+y+" one.x: "+one.x+" one.y: "+one.y);
                        if( same(x,y)==false && one.x!=-1)//&& mark==deck[x][y].getMark() /*&& one.x!=-1 && mark!=-1*/)
                        {
                            //hit and matched
                            System.out.println("1");
                            two.x=x;
                            two.y=y;
                            deck[x][y].setClicked(true);
                            counter=System.currentTimeMillis();
                        }
                        else //if(mark==-1) 
                        {
                            //set mark
                            System.out.println("2");
                            one.x=x;
                            one.y=y;
                            deck[x][y].setClicked(true);
                            mark=deck[x][y].getMark();
                        }
//                        else
//                        {
//                            //missed on both
//                        }
                    }
        }
    }
    boolean same(int x, int y)
    {
        if(x==one.x && y!=one.y)
            return false;
        else if(x!=one.x && y==one.y)
            return false;
        else if(x!=one.x && y!=one.y)
            return false;
        return true;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    @Override
    public void hide() {
    }
}
