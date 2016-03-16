package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    int cardPairs=6;
    double numRow=2;//cardPairs/2;
    int mark=-1;
    Vector2 one=new Vector2(-1,-1);
    Vector2 two=new Vector2(-1,-1);
    int hitMark=0;
    long counter=0;
    int time=2;
    //Vector2 two=new Vector2(-2,-2);
    int difficulty=-1, maxDifficulty=2;
    boolean stripped=false; 
    boolean orientation;
    int cardsPerRound, roundsTillStats;
    float displacement, revealTime;
    int failedAttempts;
    
    public void loadPlacement()
    {
        File file=new File("Data/MemoryGameInfo.txt");
        try 
        {
            Scanner scan=new Scanner(file);
            orientation=scan.nextBoolean();
            displacement=scan.nextFloat();
            cardPairs=scan.nextInt();
            roundsTillStats=scan.nextInt();
            revealTime=scan.nextFloat();
            difficulty=scan.nextInt();
            scan.close();
            if(cardPairs<2) cardPairs=2;
            if(cardPairs>12) cardPairs=12;
            if(displacement >100) displacement=100;
            else if(displacement<0) displacement=0;
            if(orientation)
                displacement=(9.0f*(displacement/100.0f));
            else
                displacement=(12.0f*(displacement/100.0f));
         
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
            loadPlacement();
            cardGame(cardPairs);
            failedAttempts=0;
        }
        memoryGameLogic(cardPairs);
        if(running(cardPairs)==false)
        {
            first=true;
        }
            //Gdx.app.exit();
    }

    public boolean running(int cardPairs)
    {
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<numRow; ++y)//for(int y=0; y<cardPairs/numRow; ++y)
                if(deck[x][y]!=null) 
                    return true;
        return false;
    }
    
    public void cardGame(double cardPairs)
    {
        Sprite one=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/one.png")));
        Sprite two=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/two.png")));
        Sprite three=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/three.png")));
        Sprite four=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/four.png")));
        Sprite five=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/five.png")));
        Sprite six=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/six.png")));
        Sprite grey=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Grey.png")));
        Sprite brown=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Brown.png")));
        Sprite orange=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Orange.png")));
        Sprite purple=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Purple.png")));
        Sprite white=new Sprite(new Texture(Gdx.files.internal("Items/Colors/White.png")));
        Sprite yellow=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Yellow.png")));
        Sprite box=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Box.png")));
        Sprite circle=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Circle.png")));
        Sprite diamond=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Diamond.png")));
        Sprite heart=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Heart.png")));
        Sprite star=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Star.png")));
        Sprite triangle=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Triangle.png")));
        deck=new Card[(int)cardPairs][(int)numRow];
        //deck=new Card[(int)cardPairs][(int)(cardPairs/numRow)];
        int [][] deckFill= new int[(int)cardPairs][(int)(numRow)];
//        int [][] deckFill= new int[(int)cardPairs][(int)(cardPairs/numRow)];
        Random rand=new Random();
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(numRow); ++y)//for(int y=0; y<(int)(cardPairs/numRow); ++y)
                deckFill[x][y]=0;
        
//        if(difficulty>=maxDifficulty)
//        {
//            difficulty=0;
//            stripped=true;
//        }
//        else
//            difficulty++;
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(numRow); ++y)//for(int y=0; y<(int)(cardPairs/numRow); ++y)
            {
                Sprite temp=null;
                boolean taken=true;
                int num=0;
                while(taken==true)
                {
                    num=rand.nextInt((int) cardPairs)+1;
                    num+= 6*difficulty;
                    int count=0;
                    for(int a=0; a<cardPairs; ++a)
                        for(int b=0; b<numRow; ++b)//for(int b=0; b<cardPairs/numRow; ++b)
                            if(deckFill[a][b]==num)
                                count++;
                    if(count<2)
                        taken=false;
                }                
                
                switch(num)
                {
                    case 1:
                        temp=grey;
                        break;
                    case 2:
                        temp=brown;
                        break;
                    case 3:
                        temp=orange;
                        break;
                    case 4:
                        temp=purple;
                        break;
                    case 5:
                        temp=white;
                        break;
                    case 6:
                        temp=yellow;
                        break;
                    case 7:
                        temp=one;
                        break;
                    case 8:
                        temp=two;
                        break;
                    case 9:
                        temp=three;
                        break;
                    case 10:
                        temp=four;
                        break;
                    case 11:
                        temp=five;
                        break;
                    case 12:
                        temp=six;
                        break;
                    case 13:
                        temp=box;
                        break;
                    case 14:
                        temp=circle;
                        break;
                    case 15:
                        temp=diamond;
                        break;
                    case 16:
                        temp=heart;
                        break;
                    case 17:
                        temp=star;
                        break;
                    case 18:
                        temp=triangle;
                        break;
                }
                
                if(orientation)
                    deck[x][y]=new Card(x,y+displacement,temp, num, 150, 100);
                else
                    deck[x][y]=new Card(y+displacement,x,temp, num, 150, 90);
                deckFill[x][y]=num;
            }
    }
    
    public void memoryGameLogic(int cardPairs)
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        Sprite s=new Sprite(new Texture(Gdx.files.internal("Items/stripped.png")));
        if(stripped)
            for(int x=0; x<80; ++x)
            {
                for(int y=0; y<50; ++y)
                {
                    batch.draw(s, x*25,y*25);
                }
            }
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<numRow; ++y)//for(int y=0; y<cardPairs/numRow; ++y)
                if(deck[x][y]!=null)
                {   
                    deck[x][y].draw(batch);
                }

        if(two.x!=-1 && System.currentTimeMillis()-counter>revealTime*1000)
        {
            deck[(int)one.x][(int)one.y].setClicked(false);
            deck[(int)two.x][(int)two.y].setClicked(false);
            if(mark==deck[(int)two.x][(int)two.y].getMark())
            {
                deck[(int)one.x][(int)one.y]=null;
                deck[(int)two.x][(int)two.y]=null;
            }
            else
            {
                if(deck[(int)one.x][(int)one.y].wasAttempted() || deck[(int)two.x][(int)two.y].wasAttempted())
                {
                    failedAttempts++;
                    deck[(int)one.x][(int)one.y].setClicked(false);
                    deck[(int)two.x][(int)two.y].setClicked(false);
                    System.out.println(failedAttempts+" wrong");
                }
                deck[(int)one.x][(int)one.y].setClicked(false);
                deck[(int)two.x][(int)two.y].setClicked(false);
                
            }
            one.x=-1;
            one.y=-1;
            two.x=-1;
            two.y=-1;
            mark=-1;
        }
        batch.end();
        if(Gdx.input.isKeyJustPressed(Keys.Q)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        }

        if(Gdx.input.isKeyJustPressed(Keys.SPACE))
            first=true;
        
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched() && two.x==-1)//if(Gdx.input.isKeyJustPressed(Keys.F))
        {
            //System.out.println("click at x: "+Gdx.input.getX() +" and y: "+(Gdx.graphics.getHeight()-Gdx.input.getY()));
            for(int x=0; x<cardPairs; ++x)
                for(int y=0; y<numRow; ++y)//for(int y=0; y<cardPairs/numRow; ++y)
                    if(deck[x][y]!=null)
                    if(deck[x][y].click(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()))
                    {
                        //System.out.println("mark= "+mark+" vs "+deck[x][y].getMark()+ "x: "+x+" y: "+y+" one.x: "+one.x+" one.y: "+one.y);
                        if( same(x,y)==false && one.x!=-1)//&& mark==deck[x][y].getMark() /*&& one.x!=-1 && mark!=-1*/)
                        {
                            //hit and matched
                            two.x=x;
                            two.y=y;
                            deck[x][y].setClicked(true);
                            counter=System.currentTimeMillis();
                        }
                        else //if(mark==-1) 
                        {
                            //set mark
                            one.x=x;
                            one.y=y;
                            deck[x][y].setClicked(true);
                            mark=deck[x][y].getMark();
                        }
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
