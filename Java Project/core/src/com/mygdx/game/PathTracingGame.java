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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Shape;

/**
 *
 * @author Kenny
 */
public class PathTracingGame extends ApplicationAdapter implements Screen, InputProcessor {

    private SpriteBatch batch;
    private boolean gameLooping;
    private int roundNum;
    private int numPoints;
    private float angleOffset;
    private List<ShapeFloatPoint> shape;
    private final Texture pointTexture;
    private float pointRadius;
    private final Texture pointNotConnectedTexture;
    private final Texture pointTargetTexture;
    private long lastRecordTime;
    private List<Float> lineDistances;
    private ShapeRenderer shapeRenderer;
    private boolean endingSequence;
    private long endingTime;
    
    private String firstName;
    private String lastName;
    private String routine;
    private int numRounds;
    private TracedPathType pathType;
    private int numSessions;
    
    /**
     * 
     * @param firstName
     * @param lastName
     * @param routine 
     */
    public PathTracingGame(String firstName, String lastName, String routine) {
        pointTexture = new Texture(Gdx.files.internal("Textures/Ball.png"));
        pointNotConnectedTexture = new Texture(Gdx.files.internal("Textures/BallNotConnected.png"));
        pointTargetTexture = new Texture(Gdx.files.internal("Textures/BallTarget.png"));
        pointRadius = pointTexture.getWidth()/2;
        gameLooping = true;
        lineDistances = new ArrayList();
        shapeRenderer = new ShapeRenderer();
        endingSequence = false;
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.routine = routine;
        
        loadVariables();
        startGame();
    }
    
    @Override
    public void show() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor(0,0,0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        if (this.gameLooping) {
            gameLoop();
            renderGame();
        }
        else if (!timeToEnd()) {
            renderStats();
        }
        else {
            goBackToScreen();
        }
    }

    @Override
    public void hide() {
        
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            Gdx.input.setCursorCatched(false);
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

    private void loadVariables() {
        String filePath ="RTS Data/patients/" + firstName + "_" + lastName + "/" + routine + "/PathTracingGameInfo.txt";
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            // number of points in the path
            this.numPoints = scanner.nextInt();
            // number of rounds before stats
            this.numRounds = scanner.nextInt();
            // number of sessions before quitting - a session is a set of n
            // plays, where n = numRounds
            this.numSessions = scanner.nextInt();
            // this option will be active by the time expo is on,
            // but it may not be available in the menu by expo. In that case,
            // I have it handled.
            // 
            // For reference, it should be a lowercase string that says either
            // "random" or "circular", without quotes. "random" means random 
            // path, and "circular" means circular path, as I demonstrated at
            // the meeting the other day.
            if (scanner.hasNext()) {
                String s = scanner.next();
                if (s.equals("random")) {
                    pathType = TracedPathType.RANDOM;
                }
                else {
                    pathType = TracedPathType.CIRCULAR;
                }
            }
            else {
                pathType = TracedPathType.CIRCULAR;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
            this.numPoints = 6;
            this.numRounds = 6;
            this.numSessions = 1;
            this.pathType = TracedPathType.CIRCULAR;
        }
    }

    private void gameLoop() {
        if (shouldRecordDistance()) {
            recordLineDistance();
        }
        
        if (gameDone()) {
            finish();
        }
        else if (roundFinished()) {
            writeStats();
            if (!gameDone()) {
                nextRound();
            }
        }
        else if (touchingNextPoint()) {
            goToNextPoint();
        }
    }

    private boolean touchingNextPoint() {
        // TODO: check this
        ShapeFloatPoint nextPoint = getNextPoint();
        float xDist = nextPoint.x - Gdx.input.getX();
        float yDist = nextPoint.y - (Gdx.graphics.getHeight() - Gdx.input.getY());
        return (xDist*xDist + yDist*yDist) < pointRadius*pointRadius;
    }

    private void goToNextPoint() {
        for (int i = 0; i < numPoints; ++i) {
            if (!shape.get(i).connected) {
                shape.get(i).connected = true;
                return;
            }
        }
    }

    private boolean roundFinished() {
        for (int i = 0; i < numPoints; ++i) {
            if (!shape.get(i).connected) {
                return false;
            }
        }
        return true;
    }

    private void nextRound() {
        // go to next round
        Random r = new Random();
        this.roundNum++;
        this.angleOffset += (float) (r.nextFloat() * Math.PI / 8);
        createShape();
    }

    private boolean gameDone() {
        return roundNum == numRounds;
    }

    private void finish() {
        this.endingSequence = true;
        this.endingTime = System.currentTimeMillis();
        this.gameLooping = false;
    }

    private void writeStats() {
        // TODO:
        // write the stats for the last round to a file
        
    }

    private void startGame() {
        Random r = new Random();
        this.roundNum = 1;
        this.angleOffset = (float) (r.nextFloat() * Math.PI / 8) + (float)Math.PI/16;
        createShape();
    }
    
    private void createShape() {
        this.shape = new ArrayList();
        for (int i = 0; i < numPoints; ++i) {
            ShapeFloatPoint p = new ShapeFloatPoint();
            float angle = (float) (i*(Math.PI*2)/numPoints + angleOffset);
            p.x = (float) Math.cos(angle) * (float)(0.2*Gdx.graphics.getHeight()) + Gdx.graphics.getWidth()/2;
            p.y = (float) Math.sin(angle) * (float)(0.2*Gdx.graphics.getHeight()) + Gdx.graphics.getHeight()/2;
            p.connected = false;
            shape.add(p);
        }
    }

    private boolean timeToEnd() {
        if (endingSequence) {
            return this.endingTime + 5000 < System.currentTimeMillis();
        }
        else {
            return false;
        }
    }

    private void goBackToScreen() {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
    }

    private void renderGame() {
        shapeRenderer.begin(ShapeType.Filled);
        for (int i = 0; i < shape.size()-1; ++i) {
            if (shape.get(i).connected && shape.get(i+1).connected) {
                float x1 = shape.get(i).x;
                float y1 = shape.get(i).y;
                float x2 = shape.get(i+1).x;
                float y2 = shape.get(i+1).y;
                shapeRenderer.setColor(1,1,0,1);
                shapeRenderer.rectLine(x1, y1, x2, y2,2.0f);
            }
            else {
                break;
            }
        }
        shapeRenderer.end();
        
        batch.begin();
        boolean drawingConnected = true;
        for (int i = 0; i < shape.size(); ++i) {
            float xValue = shape.get(i).x - pointRadius;
            float yValue = shape.get(i).y - pointRadius;
            if (drawingConnected) {
                if (shape.get(i).connected) {
                    batch.draw(pointTexture, xValue, yValue);
                }
                else {
                    batch.draw(pointTargetTexture, xValue, yValue);
                    drawingConnected = false;
                }
            }
            else {
                batch.draw(pointNotConnectedTexture, xValue, yValue);
            }
        }
        batch.end();
    }

    private void renderStats() {
        // TODO:
        // render end game statistics
    }
    
    private ShapeFloatPoint getLastConnectedPoint() {
        for (int i = 0; i < shape.size()-1; ++i) {
            if (!shape.get(i+1).connected) {
                return shape.get(i);
            }
        }
        return shape.get(shape.size()-1);
    }
    
    private ShapeFloatPoint getNextPoint() {
        for (int i = 0; i < shape.size(); ++i) {
            if (!shape.get(i).connected) {
                return shape.get(i);
            }
        }
        return null;
    }
    
    private float pointLineDistance(float x1, float y1, 
                                      float x2, float y2,
                                      float px, float py) {
        float ydist = y2-y1;
        float xdist = x2-x1;
        return (float)(Math.abs(ydist*px - xdist*py + x2*y1 + y2*x1)/Math.sqrt(ydist*ydist + xdist*xdist));
    }

    private boolean shouldRecordDistance() {
        if (shape.size() == 0 || shape.get(0).connected == false || shape.get(shape.size()-1).connected) {
            this.lastRecordTime = System.currentTimeMillis();
            return false;
        }
        else {
            boolean doRecord = this.lastRecordTime + 20 < System.currentTimeMillis();
            if (doRecord) {
                this.lastRecordTime = System.currentTimeMillis();
            }
            return doRecord;
        }
    }

    private void recordLineDistance() {
        ShapeFloatPoint lastConnectedPoint = getLastConnectedPoint();
        ShapeFloatPoint nextPoint = getNextPoint();
        float distance = 
                pointLineDistance(lastConnectedPoint.x, lastConnectedPoint.y,
                                  nextPoint.x, nextPoint.y,
                                  Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
        this.lineDistances.add(distance);
    }
    
    class ShapeFloatPoint {
        float x;
        float y;
        boolean connected;
    }
    
    enum TracedPathType {
        RANDOM,
        CIRCULAR
    }
}
