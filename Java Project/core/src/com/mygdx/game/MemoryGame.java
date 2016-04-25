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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muel2767
 * Requirements:
 *      1: leave cards flipped for 1-5 seconds
 *      2: Chose card layout vertical/horizontal
 *      3: background solid or designed
 *      4: max of 12 card pairs, min of 2
 *      5: Allow for the cards to be placed onto the screen at different distances
 *      6: Time the user to match all of the cards
 *      7: Record the missed matches
 *      8: Terminate when the user hits the escape key, for this, it must: 
 *          A: Save the game
 *          B: Find the patients averages
 *          C: Show the statistics
 *          D: Quit to main menu or skip to next game
 *      9: Play N waves of rounds and quit automatticly
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
    int difficulty=-1, round=0;
    boolean stripped=false; 
    boolean orientation, displayStats=false;
    int cardsPerRound, roundsTillStats,waves, wave;
    float displacement, revealTime;
    int failedAttempts=0;
    ArrayList<Float> statsTime=new ArrayList<Float>();
    ArrayList<Integer> statsWrong=new ArrayList<Integer>();
    BitmapFont font=new BitmapFont();
    float averageMissesUp, averageTimeUp, counterUp;
    float averageMissesSide, averageTimeSide, counterSide;
    long roundTime;
    String firstN, lastN, routine;
    boolean quitEarly=false;
    
    MemoryGame(String fName, String lName, String routineName) {
        firstN = fName;
        lastN = lName;
        routine = routineName; 
        wave=0;
    }
    
    /**
     * load from a file and set up the placement of the board
     */
        public void loadPlacement()
    {
        String f=firstN+lastN+"/Data/"+routine+"/MemoryGameInfo.txt";
        File file=new File(f);
        try 
        {
            Scanner scan=new Scanner(file);
            //vertical or horizontal
            orientation=scan.nextBoolean();
            //percent across the screen 
            displacement=scan.nextFloat();
            //how many pairs on the board
            cardPairs=scan.nextInt();
            //rounds till it displayes info from the player
            roundsTillStats=scan.nextInt();
            //how long are the cards revealed before being removed or flipped back over
            revealTime=scan.nextFloat();
            //choose the card set
            difficulty=scan.nextInt();
            //is the background stripped?
            stripped=scan.nextBoolean();
            if(scan.hasNextInt())
                waves=scan.nextInt();
            else 
                waves=3;
            scan.close();
            //client wants minimum of 2 pairs
            if(cardPairs<2) cardPairs=2;
            //maximum of 12 pairs
            if(cardPairs>12) cardPairs=12;
            //cant have more than a 100% displacement
            if(displacement >100) displacement=100;
            else if(displacement<0) displacement=0;
            //use the orientation to move the displacement based off of a percent
            if(orientation)
                displacement=(9.0f*(displacement/100.0f));
            else
                displacement=(12.0f*(displacement/100.0f));
            if(roundsTillStats<0)
                roundsTillStats=1;
            if(waves<0)
                waves=1;
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * load the averages of a patient
     */
        public void loadAverage()
    {
        //concatinate to create the string of the file
        String fileName=firstN+lastN+"/Data/"+routine+"/MemoryGameStatistics.txt";
        File file=new File(fileName);
        try 
        {
            Scanner scan=new Scanner(file);
            counterUp=0;
            counterSide=0;
            averageMissesUp=0;
            averageTimeUp=0;
            averageMissesSide=0;
            averageTimeSide=0;
            //is there more in the data file?
            while(scan.hasNext())
            {
                //stats displayed do not need background, so consume the data
                boolean isStrip=scan.nextBoolean();
                //switch on orientation
                if(scan.nextBoolean())//vertical
                {
                    averageMissesUp+=scan.nextInt();
                    averageTimeUp+=scan.nextFloat();
                    counterUp++;
                }
                else//horizontal
                {
                    averageMissesSide+=scan.nextInt();
                    averageTimeSide+=scan.nextFloat(); 
                    counterSide++;
                }
                scan.nextInt();
                scan.next();
                scan.nextInt();
            }
            //math to set up the averages
            averageMissesUp=averageMissesUp/counterUp;
            averageTimeUp=averageTimeUp/counterUp;
            averageMissesSide=averageMissesSide/counterSide;
            averageTimeSide=averageTimeSide/counterSide;
            scan.close();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * save the info of the player(client) into their first and last name folder and into a data file
     */
        public void saveClient()
    {
        //make the string for the file to be saved into
        String file=firstN+lastN+"/Data/"+routine+"/MemoryGameStatistics.txt";
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        Date date = new Date();
        //System.out.println(dateFormat.format(date)); //2014/08/06 
        try 
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for(int i=0; i<statsWrong.size(); ++i)
            {
                //save background(boolean), orientation(boolean), wrong(int), time(float) and the date int, string, int
                bw.append(stripped+" "+orientation+" "+ statsWrong.get(i)+" "+(statsTime.get(i))+" "+dateFormat.format(date)+"\n");// time is *1000 so that it displays in seconds
                bw.flush();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MemoryGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * setup the input processor and 
     */
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
        if(!quitEarly)
        {
            //not drawing game stats
            if(!displayStats)
            {
                if(first)//does it need to create the game? 
                {
                    first=false;
                    loadPlacement();
                    cardGame(cardPairs);
                    roundTime=System.currentTimeMillis();
                    failedAttempts=0;
                }
                memoryGameLogic(cardPairs);//place the board
                //is the game still going?
                if(running(cardPairs)==false)
                {
                    first=true;
                    statsTime.add((System.currentTimeMillis()-roundTime)/1000.0f);
                    statsWrong.add(failedAttempts);
                    //is the player done with a block round
                    if(round==roundsTillStats-1)//-1 to roundsTillStats because i start round at 0
                    {
                        //save the block averages
                        saveClient();
                        round=0;
                        displayStats=true;
                        //load the averages to be displayed in the displayBlockInfo()
                        loadAverage();
                    }
                    else
                    {
                        round++;
                    }
                }
            }
            else
            {
                //draw the block info until the player hits a key
                displayBlockInfo();
                if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) 
                {
                    if(wave>=waves)
                        endGame();
                    //flip the draw stats status 
                    displayStats=!displayStats;
                    //clear the stats holders
                    statsTime.clear();
                    statsWrong.clear();
                }
            }
        }
        else
        {
            quitStats();
        }
    }
    
    /**
     * draw end of game stats and return the game condition. 0 is waiting, 1 is quit, 2 is continue
     * @return 
     */
    public int quitStats()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        //display averages by orientation
        font.draw(batch, "PRESS N TO CONTINUE TO NEXT GAME OR Q TO QUIT", (int)(Gdx.graphics.getWidth()*.4f), (int)(Gdx.graphics.getHeight()*.1f));
        font.draw(batch, "Vertical Averages", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.95f);//was horizontal
        font.draw(batch, "Missed: "+averageMissesSide+"     "+"Time: "+averageTimeSide, Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.85f);
        font.draw(batch, "Horizontal Average", Gdx.graphics.getWidth()*.75f, Gdx.graphics.getHeight()*.95f);//was vertical
        font.draw(batch, "Missed: "+averageMissesUp+"     "+"Time: "+averageTimeUp, Gdx.graphics.getWidth()*.75f, Gdx.graphics.getHeight()*.85f);
        //display the stats of the last block of rounds
        for(int i=0; i<statsWrong.size(); ++i)
        {
            font.draw(batch, statsWrong.get(i)+" "+( statsTime.get(i)  ), Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.75f-i*25);
        }
        batch.end();
        if(Gdx.input.isKeyJustPressed(Keys.Q))
        {
            return 1;
        }
        else if(Gdx.input.isKeyJustPressed(Keys.N))
        {
            return 2;
        }
        return 0;
    }
    
    /**
     * draw the post block round info
     */
        public void displayBlockInfo()
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        //display averages by orientation
        font.draw(batch, "PRESS ANY KEY TO CONTINUE", (int)(Gdx.graphics.getWidth()*.4f), (int)(Gdx.graphics.getHeight()*.1f));
        font.draw(batch, "Vertical Averages", Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.95f);//was horizontal
        font.draw(batch, "Missed: "+averageMissesSide+"     "+"Time: "+averageTimeSide, Gdx.graphics.getWidth()*.25f, Gdx.graphics.getHeight()*.85f);
        font.draw(batch, "Horizontal Average", Gdx.graphics.getWidth()*.75f, Gdx.graphics.getHeight()*.95f);//was vertical
        font.draw(batch, "Missed: "+averageMissesUp+"     "+"Time: "+averageTimeUp, Gdx.graphics.getWidth()*.75f, Gdx.graphics.getHeight()*.85f);
        //display the stats of the last block of rounds
        for(int i=0; i<statsWrong.size(); ++i)
            font.draw(batch, statsWrong.get(i)+" "+( statsTime.get(i)  ), Gdx.graphics.getWidth()*.45f, Gdx.graphics.getHeight()*.75f-i*25);
        batch.end();
    }

    /**
     * is the deck emptied out or is the player still looking?
     * @param cardPairs
     * @return
     */
        public boolean running(int cardPairs)
    {
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<numRow; ++y)
                if(deck[x][y]!=null) 
                    return true;
        return false;
    }
    
    /**
     * make the game board
     * @param cardPairs
     */
        public void cardGame(double cardPairs)
    {
        //all of the sprites. This portion could be optimized but there is no need for this small of a program
        Sprite one=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/one.png")));
        Sprite two=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/two.png")));
        Sprite three=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/three.png")));
        Sprite four=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/four.png")));
        Sprite five=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/five.png")));
        Sprite six=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/six.png")));
        Sprite seven=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/seven.png")));
        Sprite eight=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/eight.png")));
        Sprite nine=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/nine.png")));
        Sprite ten=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/ten.png")));
        Sprite eleven=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/eleven.png")));
        Sprite twelve=new Sprite(new Texture(Gdx.files.internal("Items/NumbersLetters/twelve.png")));
        Sprite grey=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Grey.png")));
        Sprite brown=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Brown.png")));
        Sprite orange=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Orange.png")));
        Sprite purple=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Purple.png")));
        Sprite white=new Sprite(new Texture(Gdx.files.internal("Items/Colors/White.png")));
        Sprite yellow=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Yellow.png")));
        Sprite bp=new Sprite(new Texture(Gdx.files.internal("Items/Colors/BP.png")));
        Sprite blue=new Sprite(new Texture(Gdx.files.internal("Items/Colors/Blue.png")));
        Sprite gb=new Sprite(new Texture(Gdx.files.internal("Items/Colors/GB.png")));
        Sprite gy=new Sprite(new Texture(Gdx.files.internal("Items/Colors/GY.png")));
        Sprite op=new Sprite(new Texture(Gdx.files.internal("Items/Colors/OP.png")));
        Sprite yb=new Sprite(new Texture(Gdx.files.internal("Items/Colors/YB.png")));
        Sprite box=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Box.png")));
        Sprite circle=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Circle.png")));
        Sprite diamond=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Diamond.png")));
        Sprite heart=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Heart.png")));
        Sprite star=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Star.png")));
        Sprite triangle=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/Triangle.png")));
        Sprite arrow=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/arrow.png")));
        Sprite cross=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/cross.png")));
        Sprite downArrow=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/downArrow.png")));
        Sprite leftArrow=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/leftArrow.png")));
        Sprite pentagon=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/pentagon.png")));
        Sprite upArrow=new Sprite(new Texture(Gdx.files.internal("Items/Shapes/upArrow.png")));
        deck=new Card[(int)cardPairs][(int)numRow];
        int [][] deckFill= new int[(int)cardPairs][(int)(numRow)];
        Random rand=new Random();
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(numRow); ++y)//make sure that the deck has a number to check for "null"
                deckFill[x][y]=0;
        
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<(int)(numRow); ++y)//flip through the size of the deck and assign cards
            {
                Sprite temp=null;
                boolean taken=true;
                int num=0;
                while(taken==true)
                {
                    num=rand.nextInt((int) cardPairs)+1;//find a number from 1 - card pairs (no more than 12 since client only wants a max of 12 card pairs at once)
                    num+= 12*difficulty; //makes the sprite pull from the correct batch
                    int count=0;
                    for(int a=0; a<cardPairs; ++a)
                        for(int b=0; b<numRow; ++b)
                            if(deckFill[a][b]==num)//has the card already been placed?
                                count++;
                    if(count<2)//is there already 2 of this card out?
                        taken=false;
                }                
                //find the correct sprite to assign to a card
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
                        temp=bp;
                        break;
                    case 8:
                        temp=blue;
                        break;
                    case 9:
                        temp=gb;
                        break;
                    case 10:
                        temp=gy;
                        break;
                    case 11:
                        temp=op;
                        break;
                    case 12:
                        temp=yb;
                        break;
                    case 13:
                        temp=one;
                        break;
                    case 14:
                        temp=two;
                        break;
                    case 15:
                        temp=three;
                        break;
                    case 16:
                        temp=four;
                        break;
                    case 17:
                        temp=five;
                        break;
                    case 18:
                        temp=six;
                        break;
                    case 19:
                        temp=seven;
                        break;
                    case 20:
                        temp=eight;
                        break;
                    case 21:
                        temp=nine;
                        break;
                    case 22:
                        temp=ten;
                        break;
                    case 23:
                        temp=eleven;
                        break;
                    case 24:
                        temp=twelve;
                        break;
                    case 25:
                        temp=box;
                        break;
                    case 26:
                        temp=circle;
                        break;
                    case 27:
                        temp=diamond;
                        break;
                    case 28:
                        temp=heart;
                        break;
                    case 29:
                        temp=star;
                        break;
                    case 30:
                        temp=triangle;
                        break;
                    case 31:
                        temp=arrow;
                        break;
                    case 32:
                        temp=cross;
                        break;
                    case 33:
                        temp=downArrow;
                        break;
                    case 34:
                        temp=leftArrow;
                        break;
                    case 35:
                        temp=upArrow;
                        break;
                    case 36:
                        temp=pentagon;
                        break;
                }
                //up down
                if(orientation)
                    deck[x][y]=new Card(x,y+displacement,temp, num, 150, 100);
                else//left right
                    deck[x][y]=new Card(y+displacement,x,temp, num, 150, 90);
                //place into the deck
                deckFill[x][y]=num;
            }
    }
    
    /**
     * draws the screen and performs the logic for card deletion
     * @param cardPairs
     */
    public void memoryGameLogic(int cardPairs)
    {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        Sprite s=new Sprite(new Texture(Gdx.files.internal("Items/stripped.png")));
        //draw the stripped background across the screen. 
        if(stripped)
            for(int x=0; x<80; ++x)
            {
                for(int y=0; y<50; ++y)
                {
                    batch.draw(s, x*25,y*25);
                }
            }
        
        //go through the X and Y and draw the cards if not null
        for(int x=0; x<cardPairs; ++x)
            for(int y=0; y<numRow; ++y)
                if(deck[x][y]!=null)//null check
                {   
                    deck[x][y].draw(batch);
                }

        //has the player chosen two cards? and has the player waited long enough
        if(two.x!=-1 && System.currentTimeMillis()-counter>revealTime*1000)
        {
            //flip the cards over
            deck[(int)one.x][(int)one.y].setClicked(false);
            deck[(int)two.x][(int)two.y].setClicked(false);
            //did the player chose the correct mark
            if(mark==deck[(int)two.x][(int)two.y].getMark())
            {
                //delete the two cards from the deck
                deck[(int)one.x][(int)one.y]=null;
                deck[(int)two.x][(int)two.y]=null;
            }
            else
            {
                //the player has chosen incorrectly for more than the first time on a card and has now failed an attempt
                if(deck[(int)one.x][(int)one.y].wasAttempted() || deck[(int)two.x][(int)two.y].wasAttempted())
                {
                    //increase failed attempts
                    failedAttempts++;
                    //flip cards over
                    deck[(int)one.x][(int)one.y].setClicked(false);
                    deck[(int)two.x][(int)two.y].setClicked(false);
                }
                //flip cards over
                deck[(int)one.x][(int)one.y].setClicked(false);
                deck[(int)two.x][(int)two.y].setClicked(false);
            }
            
            //"null" out the two cards and the mark by setting to -1
            one.x=-1;
            one.y=-1;
            two.x=-1;
            two.y=-1;
            mark=-1;
        }
        batch.end();
        
        //quit the game and return to game menu screen
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) 
        {
            quitEarly=true;
            quit();
        }
        
        //player has clicked and they are not waiting 
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched() && two.x==-1)
        {
            for(int x=0; x<cardPairs; ++x)//N cardpairs
                for(int y=0; y<numRow; ++y)//with 2 rows
                {
                    if(deck[x][y]!=null) // is the card null? 
                    {
                        if(deck[x][y].click(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()))//did the player click on a card
                        {
                            if( same(x,y)==false && one.x!=-1)//x and y do not match card one and one is not "null" IE -1
                            {
                                //hit and matched
                                two.x=x;
                                two.y=y;
                                deck[x][y].setClicked(true);
                                counter=System.currentTimeMillis();
                            }
                            else  
                            {
                                //set card one 
                                one.x=x;
                                one.y=y;
                                deck[x][y].setClicked(true);
                                mark=deck[x][y].getMark();
                            }
                        }
                    }
                }
        }
    }
    
    /**
     * do the two sets of cards match
     * @param x
     * @param y
     * @return 
     */
    boolean same(int x, int y)
    {
        //if the x's are the same but different y's
        if(x==one.x && y!=one.y)
            return false;
        //if the y's are the same but different x's
        else if(x!=one.x && y==one.y)
            return false;
        //if x and y both dont match
        else if(x!=one.x && y!=one.y)
            return false;
        //otherwise you match
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

    /**
     * used to check if the player is trying to quit early
     * @param c
     * @return
     */
    @Override
    public boolean keyTyped(char c) {
        if(!quitEarly)
            return false;
        if(Gdx.input.isKeyJustPressed(Keys.Q))
            MainMenu.continueRoutine=false;//quit to next game
        else if(Gdx.input.isKeyJustPressed(Keys.N))
            MainMenu.continueRoutine=true;//continue to next game
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
        return false;
    }
    
    /**
     * Save to close the application and load the averages
     */
    public void quit()
    {
        saveClient();
        loadAverage();
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
    
    /**
     * The game has ended normally
     */
    public void endGame()
    {
        MainMenu.continueRoutine=true;//continue to next game
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
    }
}
