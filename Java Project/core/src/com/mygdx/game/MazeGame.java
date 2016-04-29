/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kenny
 */
public class MazeGame extends ApplicationAdapter implements Screen, InputProcessor {
    private SpriteBatch batch;
    private Texture cursor;
    private Texture cursorBad;
    private Texture dot;
    private float cursorRadius;
    private float dotRadius;
    private long ticksGood;
    private long ticksBad;
    private long startTime;
    private int roundNum;
    private BitmapFont font;
    private GlyphLayout layout;
    private List<Long> times;
    private List<Long> onTicks;
    private List<Long> offTicks;
    private int numRounds;
    private int mazeWidth;
    private int mazeHeight;
    private String firstName;
    private String lastName;
    private String routine;
    private int numSessions;
    
    private List<Maze> mazes;
    private boolean playing;
    private long lastTickTime;
    
    public MazeGame(String firstName, String lastName, String routineName) {
        //System.out.println("Maze constructor");
        this.firstName = firstName;
        this.lastName = lastName;
        this.routine = routineName;
        cursor = new Texture(Gdx.files.internal("Textures/Cursor.png"));
        cursorBad = new Texture(Gdx.files.internal("Textures/CursorBad.png"));
        dot = new Texture(Gdx.files.internal("Textures/Ball.png"));
        cursorRadius = cursor.getWidth()/2;
        dotRadius = dot.getWidth()/2;
        font = new BitmapFont(Gdx.files.internal("fonts/century_gothic-regular.fnt"),Gdx.files.internal("fonts/century_gothic-regular.png"),false);
        roundNum = 0;
        loadVariables();
        layout = new GlyphLayout();
        times = new ArrayList();
        onTicks = new ArrayList();
        offTicks = new ArrayList();
        playing = false;
        ticksGood = 0;
        ticksBad = 0;
        
        mazes = new ArrayList();
        for (int i = 0; i < numRounds*numSessions; ++i) {
            mazes.add(new Maze(mazeWidth, mazeHeight, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        }
    }
    
    private void recordStats() {
        if (roundNum % numRounds == 0) {
            times.clear();
            onTicks.clear();
            offTicks.clear();
        }
        times.add(System.currentTimeMillis() - startTime);
        onTicks.add(ticksGood);
        offTicks.add(ticksBad);
        ticksGood = 0;
        ticksBad = 0;
    }
    
    @Override
    public void show() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCursorCatched(true);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        if (playing) {
            if (this.lastTickTime + 20 < System.currentTimeMillis()) {
                this.lastTickTime = System.currentTimeMillis();
                gameTick();
            }
            mazes.get(roundNum).draw(batch);
            if (mazes.get(roundNum).gotTarget(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY(), cursorRadius)) {

                //System.out.println("Round: ");
                //System.out.println(roundNum);
                //System.out.println("Ticks bad, good");
                //System.out.println(ticksBad);
                //System.out.println(ticksGood);
                recordStats();
                roundNum++;
                playing = false;
            }
        }
        else {
            if (roundNum % numRounds == 0 && roundNum != 0 || roundNum >= numRounds * numSessions) {
                instructionsAndStatisticsRender();
            }
            else {
                instructionsRender();
            }
            if (roundNum < numRounds*numSessions) {
                renderStartBall();
                if (this.startBallTouched()) {
                    startTime = System.currentTimeMillis();
                    playing = true;
                    this.lastTickTime = System.currentTimeMillis();
                }
            }
        }
        
        renderCursor();
    }

    @Override
    public void hide() {
        
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.Q) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(RTS.menu);
            Gdx.input.setCursorCatched(false);
        }
        else if (i == Input.Keys.ESCAPE) {
            if (roundNum == 0) {
                roundNum++;
            }
            while (roundNum % numRounds != 0) {
                roundNum++;
            }
<<<<<<< HEAD
            playing = false;
            ticksBad = 0;
            ticksGood = 0;
=======
            //System.out.println(roundNum);
            playing = false;           
>>>>>>> master
        }
        return true;
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

    private void gameTick() {
        if (mazes.get(roundNum).positionGood(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY(), cursorRadius)) {
            ticksGood++;
        }
        else {
            System.out.println("BAD");
            System.out.println(roundNum);
            ticksBad++;
        }
    }

    private void instructionsRender() {
        CharSequence str = "Move your mouse to the blue ball\nwithout touching the walls.\nMove the mouse to the blue ball to begin.";
        font.getData().setScale(0.5f);
        layout.setText(font, str);
        float width = layout.width;
        batch.begin();
        font.draw(batch, str, Gdx.graphics.getWidth()/2-width/2, Gdx.graphics.getHeight());
        batch.end();
    }
    
    private CharSequence convertToTimeString(long timeMillis) {
        CharSequence timeString = String.format("%d:%02d", 
            TimeUnit.MILLISECONDS.toMinutes(timeMillis),
            TimeUnit.MILLISECONDS.toSeconds(timeMillis) - 
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMillis))
        );
        return timeString;
    }
    
    private void instructionsAndStatisticsRender() {
        if (roundNum < numRounds * numSessions) {
            instructionsRender();
        }
        else {
            endInstructionsRender();
        }
        
        List<Double> ratios = new ArrayList();
        double avgRatio = 0;
        for (int i = 0; i < onTicks.size(); ++i) {
            long curOnTicks = onTicks.get(onTicks.size()-1-i);
            long curOffTicks = offTicks.get(offTicks.size()-1-i);
            double curRatio = ((double)curOnTicks)/(double)(curOnTicks+curOffTicks)*100.0;
            ratios.add(curRatio);
            avgRatio += curRatio;
        }
        if (!onTicks.isEmpty()) {
            avgRatio /= (double)onTicks.size();
        }
        else {
            avgRatio = 0;
        }
        
        List<CharSequence> timeStrings = new ArrayList();
        long totalTime = 0;
        for (int i = 0; i < times.size(); ++i) {
            long curTime = times.get(i);
            timeStrings.add(convertToTimeString(curTime));
            totalTime += curTime;
        }
        CharSequence totalTimeString = convertToTimeString(totalTime);
        
        CharSequence statsStr = "Total Time Elapsed: " + totalTimeString + "\n" + "Overall Session Accuracy: " + String.format("%.2f%%\n\n", avgRatio) + "Round Accuracies:\n";
        for (int i = 0; i < ratios.size(); ++i) {
            statsStr += "    " + Integer.toString(i+1) + ": " + String.format("%.2f%%",ratios.get(ratios.size()-i-1)) + "\n";
        }
        
        statsStr += "Round Times: \n";
        for (int i = 0; i < timeStrings.size(); ++i) {
            statsStr += "    " + Integer.toString(i+1) + ": " + timeStrings.get(i) + "\n";
        }
        
        layout.setText(font, statsStr);
        batch.begin();
        font.draw(batch, statsStr, Gdx.graphics.getWidth()/2-layout.width/2, Gdx.graphics.getHeight()/2+layout.height/2);
        batch.end();
    }

    private boolean startBallTouched() {
        float xDist = Gdx.input.getX() - mazes.get(roundNum).getStartX();
        float yDist = Gdx.graphics.getHeight() - Gdx.input.getY() - mazes.get(roundNum).getStartY();
        float radii = cursorRadius + dotRadius;
        return (xDist*xDist + yDist*yDist) < radii*radii;
    }

    private void loadVariables() {
        String filePath ="RTS Data/patients/" + firstName + "_" + lastName + "/" + routine + "/MazeGameInfo.txt";
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            // mazeWidth and mazeHeight have slightly deceiving names; they are
            // actually the number of nodes horizontally and vertically. This
            // means that the number of squares horizontally and vertically is
            // 2*mazeWidth+1 and 2*mazeHeight+1, respectively.
            //
            // As far as UI design, do what you like. However, note that if the
            // user puts in 10, that does not mean 10 squares across; it means
            // 21. You may want to "translate" their numbers for usability.
            // So, if the user puts in an odd number n, you would give me:
            // (n-1)/2
            // Or, if the user puts in an even number n, you would give me:
            // n/2
            // The number of squares horizontally and vertically will always be
            // odd - this is by design. I doubt they'll care much about 1 extra
            // square in their maze.
            this.mazeWidth = scanner.nextInt();
            this.mazeHeight = scanner.nextInt();
            // number of rounds before stats
            this.numRounds = scanner.nextInt();
            // number of sessions before quitting
            this.numSessions = scanner.nextInt();
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
            this.mazeWidth = 4;
            this.mazeHeight = 4;
            this.numRounds = 6;
            this.numSessions = 1;
        }
    }

    private void endInstructionsRender() {
        CharSequence str = "Press Q to quit.";
        font.getData().setScale(0.5f);
        layout.setText(font, str);
        float width = layout.width;
        batch.begin();
        font.draw(batch, str, Gdx.graphics.getWidth()/2-width/2, Gdx.graphics.getHeight());
        batch.end();
    }

    private void renderStartBall() {
        batch.begin();
        batch.draw(dot, mazes.get(roundNum).getStartX()-dotRadius/2, mazes.get(roundNum).getStartY()-dotRadius/2);
        batch.end();
    }

    private void renderCursor() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight()-Gdx.input.getY();
        batch.begin();
        if (!playing || mazes.get(roundNum).positionGood((int)mouseX, (int)mouseY, cursorRadius)) {
            batch.draw(cursor, mouseX-cursorRadius/2, mouseY-cursorRadius/2);
        }
        else {
            batch.draw(cursorBad, mouseX-cursorRadius/2, mouseY-cursorRadius/2);
        }
        batch.end();
    }
    
}
