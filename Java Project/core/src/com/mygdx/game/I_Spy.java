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
import com.badlogic.gdx.utils.TimeUtils;
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
    int score=0, marked, imageCount=27, roundsTillStats, placeCounter=0, placeMax, wrong=0, roundTimer=3, waves, wave;
    Random rand=new Random();
    BitmapFont font;
    boolean displacement[]=new boolean[9];
    ArrayList<Float> statsTime=new ArrayList<Float>();
    ArrayList<Integer> statsWrong=new ArrayList<Integer>();
    float averageMisses, averageTime, counter;
    long roundTime, timer=System.currentTimeMillis();
    String firstN, lastN, routine;
    boolean quitEarly=false;
    I_Spy(String fName, String lName, String routineName) {
        firstN = fName;
        lastN = lName;
        routine = routineName;         
        wave=0;
    }
    
    /**
     *  load the orientation of the board
    */
    public void loadPlacement()
    {
        //create a string fro the patient folder/data folder/ispygameinfo.txt
        String f=firstN+lastN+"/Data/"+routine+"/ISpyGameInfo.txt";
        File file=new File(f);
        try 
        {
            Scanner scan=new Scanner(file);
            for(int i=0; i<9; ++i)
            {
                //create the tic tac toe board displacement system
                displacement[i]=scan.nextBoolean();
                if(displacement[i])
                    placeMax++;
            }
            //int rounds till stats, should be 6 or 12
            roundsTillStats=scan.nextInt();
            //boolean should the game be shuffled upon
            reshuffle=scan.nextBoolean();
            //boolean should the background be stripped 
            stripped=scan.nextBoolean();
            waves=scan.nextInt();
            scan.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    *   find the patients averages
    */
    public void loadAverage()
    {
        //emter the patient file
        String fileName=firstN+lastN+"/Data/"+routine+"/ISpyStatistics.txt";
        File file=new File(fileName);
        try 
        {
            Scanner scan=new Scanner(file);
            counter=0;
            averageMisses=0;
            averageTime=0;
            //loop on all data
            while(scan.hasNext())
            {
                //waste the background
                boolean temp=scan.nextBoolean();
                //add the misses
                averageMisses+=scan.nextInt();
                //add the times
                averageTime+=scan.nextFloat();
                counter++;
                //waste the data of yeay, month, day
                scan.nextInt();
                scan.next();
                scan.nextInt();
            }
            //calculate the averages
            averageMisses=averageMisses/counter;
            averageTime=averageTime/counter;
            scan.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    *   save the patient data
    */
    public void saveClient()
    {
        //enter the patient data file
        String file=firstN+lastN+"/Data/"+routine+"/ISpyStatistics.txt";
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        Date date = new Date();
        //System.out.println(dateFormat.format(date)); //2014/08/06 
        try 
        {
            //System.out.println(roundsTillStats+" "+statsWrong.size());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for(int i=0; i<statsWrong.size(); ++i)
            {
                //loop and save that data
                bw.append(stripped+" "+statsWrong.get(i)+" "+(statsTime.get(i))+" "+dateFormat.format(date)+"\n");// time is *1000 so that it displays in seconds
                bw.flush();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    *   display the patient info during game
    */
    public void displayBlockInfo()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        //display averages up/side
        font.draw(batch, "PRESS ANY KEY TO CONTINUE", (int)(Gdx.graphics.getWidth()*.4f), (int)(Gdx.graphics.getHeight()*.1f));
        font.draw(batch, "Averages", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.95f);
        font.draw(batch, "Missed: "+averageMisses+"     "+"Time: "+averageTime, Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.85f);
        for(int i=0; i<statsWrong.size(); ++i)
            font.draw(batch, statsWrong.get(i)+"      "+( statsTime.get(i)  ), Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.75f-i*25);
            //batch.write(stats[i].misses+" "+( (System.currentTimeMillis()-stats[i].time)*1000  )+dateFormat.format(date));// time is *1000 so that it displays in seconds
        batch.end();
    }
    
    /**
     * draw end of game stats
     */
    public void quitStats()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        //display averages up/side
        font.draw(batch, "PRESS N TO CONTINUE TO NEXT GAME OR Q TO QUIT", (int)(Gdx.graphics.getWidth()*.4f), (int)(Gdx.graphics.getHeight()*.1f));
        font.draw(batch, "Averages", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.95f);
        font.draw(batch, "Missed: "+averageMisses+"     "+"Time: "+averageTime, Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.85f);
        for(int i=0; i<statsWrong.size(); ++i)
            font.draw(batch, statsWrong.get(i)+"      "+( statsTime.get(i)  ), Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.75f-i*25);
            //batch.write(stats[i].misses+" "+( (System.currentTimeMillis()-stats[i].time)*1000  )+dateFormat.format(date));// time is *1000 so that it displays in seconds
        batch.end();
    }
    
    /**
    *   display the target of the round
    */
    public void displayPreRoundInfo()
    {
       
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        font.getData().setScale(2);
        font.draw(batch, board.get(marked).name, Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.5f);
        Item i=getItem(marked, (int)(Gdx.graphics.getWidth()*.5f), (int)(Gdx.graphics.getHeight()*.40f) );
        i.draw(batch);
        //board.get(marked).draw(batch);
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
        if(first)//recreate the board?
        {
            //load patient file for how to create the board
            loadPlacement();
            if(reshuffle || one)//randomize the board
                makeGame(); 
            //make a random target
            marked=rand.nextInt(imageCount);
            roundTime=System.currentTimeMillis();
            wrong=0;
            first=false;
        }
        if(drawPreRound)//draw the target of the round until time passes
        {
            displayPreRoundInfo();//draw the target
            if(TimeUtils.timeSinceMillis(timer)/1000>roundTimer)//if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
            {
                drawPreRound=false;
                //move mouse to center screen for testing
                Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            }
        }
        else if(drawStats)
        {
            displayBlockInfo();//draw block info unill a button is hit
            if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
            {
                if(waves==wave)
                    endGame();
                wave++;
                drawPreRound=true;
                timer=System.currentTimeMillis();
                drawStats=false;
                //clean out the patient info for the block
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
        //close the game and return to the main menu
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) 
        {
            quitEarly=true;
            endGame();
        }   
    }

    //create the game
    public void makeGame()
    {
        //set mouse to center screen
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
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
        //set up all of the images on the screen randomly
        while(count<=imageCount)
        {
            int x=25;//rand.nextInt(( (1920*Gdx.graphics.getWidth()/1920))+25);
            int y=25;//rand.nextInt(( (1080*Gdx.graphics.getHeight()/1080))+25);
            boolean taken=false;

            //if biased
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
            //is the spot on the board free
            for(int i=0; i<board.size(); ++i)
            {
                for(int a=-25; a<xs; ++a)
                    for(int b=-25; b<ys; ++b)
                        if(board.get(i).clicked(x+a, y+b))
                            taken=true;
            }
            if(!taken)//if free place on board
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
        }
        first=false;
    }

    //make the item to be placed on the board
    Item getItem(int count, int x, int y)
    {
        Sprite image;
        String name;
        switch(count)
        {
            case 0:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Lemon.png")));
                name="Find the Yellow Lemon";
                break;
            case 1:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Grey.png")));
                name="Find the Grey Box";
                break;
            case 2:
                image=new Sprite(new Texture(Gdx.files.internal("Items/delete.png")));
                name="Find the X";
                break;
            case 3:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Circle.png")));
                name="Find the Circle";
                break;
            case 4:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Heart.png")));
                name="Find the Heart";
                break;
            case 5:
                image=new Sprite(new Texture(Gdx.files.internal("Items/LemonSeed.png")));
                name="Find the Brown Lemon seed";
                break;
            case 6:
                image=new Sprite(new Texture(Gdx.files.internal("Items/plus.png")));
                name="Find the Plus Sign";
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
            case 22:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/upArrow.png")));
                name="Find the Up Arrow";
                break;
            case 23:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Pentagon.png")));
                name="Find the Pentagon";
                break;
            case 24:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/leftArrow.png")));
                name="Find the Left Arrow";
                break;
            case 25:
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/downArrow.png")));
                name="Find the Down Arrow";
                break;
            case 26:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/eleven.png")));
                name="Find the Number Eleven";
                break;    
            case 27:
                image=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/eight.png")));
                name="Find the Number Eight";
                break;
            default:
                //System.out.println("defualt");
                image=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Triangle.png")));
                name="Find the Triangle";
                break;
                
        }
        
        return new Item(image, x, y, name);
    }
    
    //perform the ispy logic
    public void ISpy()
    {
        Sprite s=new Sprite(new Texture(Gdx.files.internal("Items/stripped.png")));
        if(stripped)//draw the stripped background
            for(int x=0; x<80; ++x)
            {
                for(int y=0; y<50; ++y)
                {
                    batch.draw(s, x*25,y*25);
                }
            }
        //draw the board pieces
        for(int i=0; i<board.size(); ++i)
            board.get(i).draw(batch);
        //if the player has clicked
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched())
        {
            boolean got=false;
            //check for if the patient clicked the target
            for(int i=0; i<board.size(); ++i)
                if(board.get(i).clicked(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()) && i==marked)
                {
                    got=true;
                    statsWrong.add(wrong);
                    statsTime.add( ( (float)(System.currentTimeMillis()-roundTime)/1000.0f)-roundTimer );
                    if(score%roundsTillStats==0 && score>0)
                    {
                        saveClient();
                        loadAverage();
                        drawStats=true;
                    }
                    else
                    {
                        drawPreRound=true;
                        timer=System.currentTimeMillis();
                    }
                    score++;
                    first=true;
                }
            if(!got)//patient did not get the targetm increase the wrong
                wrong++;
        }
        font.draw(batch, board.get(marked).name, Gdx.graphics.getWidth()*.43f, Gdx.graphics.getHeight()*.99f);
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
    
    public void quit()
    {
        saveClient();
        loadAverage();
        drawStats=true;
    }
    
    /**
     * The game has ended normally
     */
    public void endGame()
    {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
    }
}

