package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.graphics.GL20;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author muel2767
 */
public class I_Spy extends ApplicationAdapter implements Screen, InputProcessor {

    SpriteBatch batch;
    boolean first=true;
    ArrayList<Item> board=new ArrayList<Item>();
    int score=0, marked, imageCount=7;
    Random rand=new Random();
    BitmapFont font;
  
    @Override
    public void show () 
    {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        Gdx.input.setInputProcessor(this);
        font=new BitmapFont();
        batch = new SpriteBatch();
    }

    @Override
    public void render (float f) 
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if(first)
            makeGame();
        batch.begin();
        ISpy();
        batch.end();
        if(Gdx.input.isKeyJustPressed(Keys.Q))
            Gdx.app.exit();
    }

    public void makeGame()
    {
        Sprite image;
        int count=0;
        String name=null;
        marked=rand.nextInt(imageCount);
        
        for(int y=1; y<10; ++y)
        {
            for(int x=1; x<11; ++x)
            {
                //image=getImage(count);
                //System.out.println("round: "+count);
                board.add(getItem(count, (int) (x*100+Gdx.graphics.getHeight()*.45f), (int) (y+Gdx.graphics.getHeight()*.5f)));
                ++count;
                if(count>imageCount)
                {
                    first=false;
                    return;
                }
            }
        }
        first=false;
    }

    Item getItem(int count, int x, int y)
    {
        Sprite image;
        String name;
        switch(count)
        {
            case 0:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Lemon.png")));
                name="Find the Lemon";
                break;
            case 1:
                image=new Sprite(new Texture(Gdx.files.internal("Items/cross.png")));
                name="Find the Cross";
                break;
            case 2:
                image=new Sprite(new Texture(Gdx.files.internal("Items/delete.png")));
                name="Find the X";
                break;
            case 3:
                image=new Sprite(new Texture(Gdx.files.internal("Items/dirt.png")));
                name="Find the dirt";
                break;
            case 4:
                image=new Sprite(new Texture(Gdx.files.internal("Items/greenBox.png")));
                name="Find the Green box";
                break;
            case 5:
                image=new Sprite(new Texture(Gdx.files.internal("Items/LemonSeed.png")));
                name="Find the Lemon seed";
                break;
            case 6:
                image=new Sprite(new Texture(Gdx.files.internal("Items/plus.png")));
                name="Find the plus sign";
                break;
            case 7:
                image=new Sprite(new Texture(Gdx.files.internal("Items/redBox.png")));
                name="Find the red box";
                break;                
            default: 
                image=null;
                name=null;
                break;
        }
        
        return new Item(image, x, y, name);
    }
    public void ISpy()
    {
        for(int i=0; i<board.size(); ++i)
            board.get(i).draw(batch);
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
        {
            for(int i=0; i<board.size(); ++i)
                if(board.get(i).clicked(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()) && i==marked)
                {
                    score++;
                    first=true;
                }
        }
        font.draw(batch, board.get(marked).name+" and your score is: "+score, Gdx.graphics.getWidth()*.5f, Gdx.graphics.getHeight()*.95f);
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
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {  
    }  
    
}
