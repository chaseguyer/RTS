/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 *
 * @author chaseguyer
 */
public class MainMenu implements Screen {
    
    public static Stage stage = new Stage();
    public static Skin skin = new Skin(Gdx.files.internal("skins/skins.json"), new TextureAtlas(Gdx.files.internal("skins/test.pack")));
    
    // Login
    private Table loginTitle = new Table();
    private Label login = new Label("Therapist Login", skin);
    private Table loginTable = new Table();
    private Label unLabel = new Label("Username:", skin);
    private TextField unText = new TextField("", skin);
    private Label pwLabel = new Label("Address:", skin);
    private TextField pwText = new TextField("", skin);
    private TextButton nextButton = new TextButton("Next", skin);
    
    // Main menu
    private Table mainMenuTable = new Table();
    private TextButton button1 = new TextButton("Create Routine", skin);
    private TextButton button2 = new TextButton("Edit Routine", skin);
    private TextButton button3 = new TextButton("Load Routine", skin);
    private TextButton button4 = new TextButton("Exit", skin);
    
    // Create Routine
    private Table createRoutineTable = new Table();
    private TextButton buttonA = new TextButton("I Spy", skin);
    private TextButton buttonB = new TextButton("Memory", skin);
    private TextButton buttonC = new TextButton("Maze", skin);
    private TextButton buttonD = new TextButton("Back", skin); // make me a BACK
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);   
        resize(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height);
        createLogin();
    }
    
    public void createLogin() {        
        stage.addActor(loginTitle);
        
        loginTitle.setFillParent(true);               
        loginTitle.add(login).padBottom(100).align(Align.center).row();
        loginTitle.add(loginTable);
        
        loginTable.row();
        loginTable.add(unLabel).left();
        loginTable.add(unText).width(400);
        loginTable.row();
        loginTable.add(pwLabel).left();
        loginTable.add(pwText).width(400);
        loginTable.row();
        nextButton.getLabel().setFontScale((float)0.5);
        
        loginTable.add(nextButton).size(100,100).left().padTop(50);
                
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                loginTable.clear();
                loginTable.remove();
                stage.clear();

                createMainMenu();
            }
        }); 
    }
    
    public void createMainMenu() {
        stage.addActor(mainMenuTable);
        mainMenuTable.setFillParent(true);
        mainMenuTable.add(button1).left().padBottom(10);
        mainMenuTable.add().size(700, 100);
        mainMenuTable.row();
        mainMenuTable.add(button2).left().padBottom(10);
        mainMenuTable.row();
        mainMenuTable.add(button3).left().padBottom(10);
        mainMenuTable.row();
        mainMenuTable.add(button4).left().padBottom(10);
        
        button1.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                mainMenuTable.clear();
                mainMenuTable.remove();
                stage.clear();
                createRoutine();
           } 
        });
           
        button4.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
    }
    
    public void createRoutine() {
        stage.addActor(createRoutineTable);
        createRoutineTable.setFillParent(true);
        createRoutineTable.add(buttonA).left().padBottom(10);
        createRoutineTable.row();
        createRoutineTable.add(buttonB).left().padBottom(10);
        createRoutineTable.row();
        createRoutineTable.add(buttonC).left().padBottom(10);
        createRoutineTable.row();
        createRoutineTable.add(buttonD).left().padBottom(10);
    
        buttonA.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new I_Spy());
            }
        });    
        
        buttonB.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame());
            }
        });    
        
        buttonD.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                createRoutineTable.clear();
                createRoutineTable.remove();                              
                stage.clear();
                createMainMenu();
            }
        });    
    
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor((float)0.3176, (float)0.6510, (float)0.5333, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
    }  
}
