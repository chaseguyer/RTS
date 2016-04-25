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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kenny
 */
public class MazeGame extends ApplicationAdapter implements Screen, InputProcessor {
    
    private Maze maze;
    private SpriteBatch batch;
    private Texture cursor;
    private Texture cursorBad;
    private Texture dot;
    private float cursorRadius;
    private float dotRadius;
    private long ticksGood;
    private long ticksBad;
    private long lastTime;
    private long startTime;
    private boolean isGood;
    private boolean isOn;
    private int numRounds;
    private BitmapFont font;
    private GlyphLayout layout;
    private List<Long> times;
    private List<Long> onTicks;
    private List<Long> offTicks;
    private int sessionSize;
    
    public MazeGame(String firstName, String lastName, String routineName) {
        initialization();
    }
    
    private void initialization() {
        cursor = new Texture(Gdx.files.internal("Textures/Cursor.png"));
        cursorBad = new Texture(Gdx.files.internal("Textures/CursorBad.png"));
        dot = new Texture(Gdx.files.internal("Textures/Ball.png"));
        cursorRadius = cursor.getWidth()/2;
        dotRadius = dot.getWidth()/2;
        lastTime = 0;
        font = new BitmapFont(Gdx.files.internal("fonts/century_gothic-regular.fnt"),Gdx.files.internal("fonts/century_gothic-regular.png"),false);
        numRounds = 0;
        startNewGame();
        layout = new GlyphLayout();
        times = new ArrayList();
        onTicks = new ArrayList();
        offTicks = new ArrayList();
        sessionSize = 5;
    }
    
    private void startNewGame() {
        maze = new Maze(4,4,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        startTime = System.currentTimeMillis();
        isOn = false;
        ticksGood = 0;
        ticksBad = 0;
    }
    
    private void recordStats() {
        times.add(System.currentTimeMillis() - startTime);
        onTicks.add(ticksGood);
        offTicks.add(ticksBad);
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
        if (isOn) {
            // gameRender() must come before gameTick()
            gameRender();
            if (timeForTick()) {
                gameTick();
            }
        }
        else {
            if (numRounds % sessionSize == 0 && numRounds > 0) {
                instructionsAndStatisticsRender();
            }
            else {
                instructionsRender();
            }
            
            batch.begin();
            batch.draw(dot, maze.getStartX()-dotRadius, maze.getStartY() - dotRadius);
            batch.draw(cursor, Gdx.input.getX()-cursorRadius, Gdx.graphics.getHeight()-Gdx.input.getY()-cursorRadius);
            batch.end();
        }
        checkGameState();
    }
    
    private void gameRender() {
        maze.draw(batch);
        isGood = maze.positionGood(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY(), cursorRadius);
        batch.begin();
        if (isGood) {
            batch.draw(cursor, Gdx.input.getX()-cursorRadius, Gdx.graphics.getHeight()-Gdx.input.getY()-cursorRadius);
        }
        else {
            batch.draw(cursorBad, Gdx.input.getX()-cursorRadius, Gdx.graphics.getHeight()-Gdx.input.getY()-cursorRadius);
        }
        batch.end();
    }

    @Override
    public void hide() {
        
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.Q) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            Gdx.input.setCursorCatched(false);
        }
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

    private void gameTick() {
        if (isGood) {
            ticksGood++;
        }
        else {
            ticksBad++;
        }
        
        lastTime = System.currentTimeMillis();
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
        CharSequence timeString = String.format("%d:%d", 
            TimeUnit.MILLISECONDS.toMinutes(timeMillis),
            TimeUnit.MILLISECONDS.toSeconds(timeMillis) - 
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeMillis))
        );
        return timeString;
    }
    
    private void instructionsAndStatisticsRender() {
        instructionsRender();
        List<Double> ratios = new ArrayList();
        double avgRatio = 0;
        for (int i = 0; i < sessionSize; ++i) {
            long curOnTicks = onTicks.get(onTicks.size()-1-i);
            long curOffTicks = offTicks.get(offTicks.size()-1-i);
            double curRatio = ((double)curOnTicks)/(double)(curOnTicks+curOffTicks)*100.0;
            ratios.add(curRatio);
            avgRatio += curRatio;
        }
        avgRatio /= (double)sessionSize;
        
        List<CharSequence> timeStrings = new ArrayList();
        long totalTime = 0;
        for (int i = 0; i < sessionSize; ++i) {
            long curTime = times.get(times.size()-sessionSize+i);
            timeStrings.add(convertToTimeString(curTime));
            totalTime += curTime;
        }
        CharSequence totalTimeString = convertToTimeString(totalTime);
        
        CharSequence statsStr = "Total Time Elapsed: " + totalTimeString + "\n" + "Overall Session Accuracy: " + String.format("%.2f%%\n\n", avgRatio) + "Round Accuracies:\n";
        for (int i = 0; i < ratios.size(); ++i) {
            statsStr += "    Round " + Integer.toString(i+1) + ": " + String.format("%.2f%%",ratios.get(ratios.size()-i-1)) + "\n";
        }
        
        statsStr += "Round Times: \n";
        for (int i = 0; i < timeStrings.size(); ++i) {
            statsStr += "    Round " + Integer.toString(i) + ": " + timeStrings.get(i) + "\n";
        }
        
        layout.setText(font, statsStr);
        batch.begin();
        font.draw(batch, statsStr, Gdx.graphics.getWidth()/2-layout.width/2, Gdx.graphics.getHeight()/2+layout.height/2);
        batch.end();
    }

    private boolean timeForTick() {
        return lastTime + 20 < System.currentTimeMillis();
    }

    private void checkGameState() {
        if (isOn) {
            if (maze.gotRedDot(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY(),cursorRadius)) {
                recordStats();
                startNewGame();
                numRounds++;
                isOn = false;
            }
        }
        else {
            if (startBallTouched()) {
                isOn = true;
                startTime = System.currentTimeMillis();
            }
        }
    }

    private boolean startBallTouched() {
        float xDist = Gdx.input.getX() - maze.getStartX();
        float yDist = Gdx.graphics.getHeight() - Gdx.input.getY() - maze.getStartY();
        float radii = cursorRadius + dotRadius;
        return (xDist*xDist + yDist*yDist) < radii*radii;
    }
    
}