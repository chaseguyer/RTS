package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author muel2767
 */
public class I_Spy extends ApplicationAdapter implements Screen, InputProcessor 
{

    SpriteBatch batch;
    boolean first=true, stripped=false, reshuffle, one=true, drawStats=false, drawPreRound=true;
    ArrayList<Item> board=new ArrayList<Item>();
    int score=0, marked, imageCount=27, roundsTillStats, placeCounter=0, placeMax, wrong=0;
    Random rand=new Random();
    BitmapFont font;
    boolean displacement[]=new boolean[9];
    ArrayList<Float> statsTime=new ArrayList<Float>();
    ArrayList<Integer> statsWrong=new ArrayList<Integer>();
    float averageMisses, averageTime, counter;
    long roundTime;
    
    
    public void loadPlacement()
    {
        File file=new File("Data/ISpyGameInfo.txt");
        try 
        {
            Scanner scan=new Scanner(file);
            for(int i=0; i<9; ++i)
            {
                displacement[i]=scan.nextBoolean();
                if(displacement[i])
                    placeMax++;
            }
            roundsTillStats=scan.nextInt();
            reshuffle=scan.nextBoolean();
            scan.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadAverage(String patientFirst, String patientLast)
    {
        String fileName=patientFirst+patientLast+"ISpyStatistics.txt";
        File file=new File(fileName);
        try 
        {
            Scanner scan=new Scanner(file);
            counter=0;
            averageMisses=0;
            averageTime=0;
            while(scan.hasNext())
            {
                averageMisses+=scan.nextInt();
                averageTime+=scan.nextFloat();
                counter++;
                scan.nextInt();
                scan.next();
                scan.nextInt();
            }
            averageMisses=averageMisses/counter;
            averageTime=averageTime/counter;
            scan.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveClient(String patientFirst, String patientLast)
    {
        String file=patientFirst+patientLast+"ISpyStatistics.txt";
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2014/08/06 
        try 
        {
            //System.out.println(roundsTillStats+" "+statsWrong.size());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for(int i=0; i<roundsTillStats; ++i)
            {
                bw.append(statsWrong.get(i)+" "+(statsTime.get(i))+" "+dateFormat.format(date)+"\n");// time is *1000 so that it displays in seconds
                bw.flush();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void displayBlockInfo()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        //display averages up/side
        font.draw(batch, "Averages", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.95f);
        font.draw(batch, "Missed: "+averageMisses+"     "+"Time: "+averageTime, Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.85f);
        for(int i=0; i<roundsTillStats; ++i)
            font.draw(batch, statsWrong.get(i)+" "+( statsTime.get(i)  ), Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.75f-i*25);
            //batch.write(stats[i].misses+" "+( (System.currentTimeMillis()-stats[i].time)*1000  )+dateFormat.format(date));// time is *1000 so that it displays in seconds
        batch.end();
    }
    
    public void displayPreRoundInfo()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        font.getData().setScale(2);
        font.draw(batch, board.get(marked).name, Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.95f);
        batch.end();
    }
    
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
        {
            loadPlacement();
            if(reshuffle || one)
                makeGame(); 
            marked=rand.nextInt(imageCount);
            roundTime=System.currentTimeMillis();
            wrong=0;
            first=false;
        }
        if(drawPreRound)
        {
            displayPreRoundInfo();
            if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
            {
                drawPreRound=false;
            }
        }
        else if(drawStats)
        {
            displayBlockInfo();
            if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
            {
                drawPreRound=true;
                drawStats=false;
                statsWrong.clear();
                statsTime.clear();
            }
        }
        else
        {
            batch.begin();
            ISpy();
            batch.end();
        }
        if(Gdx.input.isKeyJustPressed(Keys.Q)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        }
        if(Gdx.input.isKeyJustPressed(Keys.SPACE))
            stripped=!stripped;
        
    }

    public void makeGame()
    {
        one=false;
        int count=0;
        board.clear();
        int xs=50, ys=75;
        placeCounter=0;
        int id=0;
        int xZone=(( (1920*Gdx.graphics.getWidth()/1920)) /3 );//+25
        int yZone=(( (1080*Gdx.graphics.getHeight()/1080))/3 );//+25
        int amountPerZone=3, num=0;
        int loop=0;
        while(count<=imageCount)
        {
            int x=25;//rand.nextInt(( (1920*Gdx.graphics.getWidth()/1920))+25);
            int y=25;//rand.nextInt(( (1080*Gdx.graphics.getHeight()/1080))+25);
            boolean taken=false;
            //System.out.println("loop: "+loop++);
            //System.out.println(x+" "+y);
            if(displacement[id])
            {
                //System.out.println( (xZone*(id%3))+" ");
                x=xZone*(id%3)+rand.nextInt(xZone);
                y=yZone*(id/3)+rand.nextInt(yZone);
            }
            else
            {
                x=rand.nextInt(( (1920*Gdx.graphics.getWidth()/1920)));
                y=rand.nextInt(( (1080*Gdx.graphics.getHeight()/1080)));
            }
            if(x<25) x+=25;
            if(y<25) y+=25;
            if(x>Gdx.graphics.getWidth()-60) x-=50;
            if(y>Gdx.graphics.getHeight()-60) y-=50;
            
            for(int i=0; i<board.size(); ++i)
            {
                for(int a=-25; a<xs; ++a)
                    for(int b=-25; b<ys; ++b)
                        if(board.get(i).clicked(x+a, y+b))
                            taken=true;
            }
            if(!taken)
            {
                board.add(getItem(count, (int) x, (int) y));
                count++;
                if(placeCounter>=amountPerZone)
                {
                    id++;
                    placeCounter=0;
                }
                else
                    placeCounter++;
            }
//            if(displacement[id])
//            {
//                if(num<=amountPerZone && placeCounter>25)
//                    id--;
//            }    
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
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Grey.png")));
                name="Find the grey box";
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
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Orange.png")));
                name="Find the Orange Box";
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
                System.out.println("defualt");
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Triangle.png")));
                name="Find the Triangle";
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
                    statsWrong.add(wrong);
                    statsTime.add( (float)(System.currentTimeMillis()-roundTime)/1000.0f);
                    if(score%roundsTillStats==0 && score>0)
                    {
                        saveClient("a", "a");
                        
                        loadAverage("a", "a");
                        drawStats=true;
                    }
                    else
                        drawPreRound=true;
                    score++;
                    first=true;
                }
        }
        
        //font.draw(batch, "your score is: "+score, Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.92f);
//        boolean hit=false;
//        for(int i=0; i<board.size(); ++i)
//                if(board.get(i).clicked(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()) && i==marked)
//                    hit=true;
        //font.getData().setScale(1);
        //font.draw(batch, "Status: "+hit+" x: "+Gdx.input.getX()+" y: "+ (Gdx.graphics.getHeight()-Gdx.input.getY()) , 50,50);
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
