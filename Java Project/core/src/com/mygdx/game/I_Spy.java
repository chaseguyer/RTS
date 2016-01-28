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
    boolean first=true, stripped=false;
    ArrayList<Item> board=new ArrayList<Item>();
    int score=0, marked, imageCount=21;
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
        if(Gdx.input.isKeyJustPressed(Keys.SPACE))
            stripped=!stripped;
    }

    public void makeGame()
    {
        int count=0;
        marked=rand.nextInt(imageCount);
        board.clear();
        while(count<imageCount)
        {
            int x=rand.nextInt(80);
            int y=rand.nextInt(40);
            boolean taken=false;
            for(int i=0; i<board.size(); ++i)
            {
                if(x>=board.get(i).x && x<=board.get(i).x+25 && y>=board.get(i).y && y<=board.get(i).y+25)
                    taken=true;
            }
            if(!taken)
            {
                board.add(getItem(count, (int) (x*25*Gdx.graphics.getHeight()/1920), (int) (y*25*Gdx.graphics.getHeight()/1080)));
                count++;
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
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Blue.png")));
                name="Find the blue box";
                break;
            case 2:
                image=new Sprite(new Texture(Gdx.files.internal("Items/delete.png")));
                name="Find the X";
                break;
            case 3:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Circle.png")));
                name="Find the circle";
                break;
            case 4:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Heart.png")));
                name="Find the Heart";
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
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Star.png")));
                name="Find the Star";
                break;           
            case 8:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/one.png")));
                name="Find the Number One";
                break;
            case 9:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/two.png")));
                name="Find the Number Two";
                break;
            case 10: 
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/three.png")));
                name="Find the Number Three";
                break;
            case 11:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/four.png")));
                name="Find the Number Four";
                break;
            case 12:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/five.png")));
                name="Find the Number Five";
                break;
            case 13:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/six.png")));
                name="Find the Number Six";
                break;
            case 14:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Brown.png")));
                name="Find the Brown Box";
                break;
            case 15:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Green.png")));
                name="Find the Green Box";
                break;
            case 16:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Purple.png")));
                name="Find the Purple Box";
                break;
            case 17:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/White.png")));
                name="Find the White Box";
                break;
            case 18:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Yellow.png")));
                name="Find the Yellow Box";
                break;
            case 19:
                image=new Sprite(new Texture(Gdx.files.internal("Items/PlayingCard.png")));
                name="Find the Playing Card";
                break;
            case 20:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Diamond.png")));
                name="Find the Diamond";
                break;
            case 21:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Triangle.png")));
                name="Find the Triangle";
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
        Sprite s=new Sprite(new Texture(Gdx.files.internal("Items/stripped.png")));
        if(stripped)
            for(int x=0; x<80; ++x)
            {
                for(int y=0; y<50; ++y)
                {
                    batch.draw(s, x*25,y*25);
                }
            }
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
        font.getData().setScale(2);
        font.draw(batch, board.get(marked).name+" and your score is: "+score, Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.95f);
        boolean hit=false;
        for(int i=0; i<board.size(); ++i)
                if(board.get(i).clicked(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()) && i==marked)
                    hit=true;
        font.getData().setScale(1);
        font.draw(batch, "Status: "+hit+" x: "+Gdx.input.getX()+" y: "+ (Gdx.graphics.getHeight()-Gdx.input.getY()) , 50,50);
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
