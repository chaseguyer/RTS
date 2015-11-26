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
    private Label pwLabel = new Label("Password:", skin);
    private TextField pwText = new TextField("", skin);
    
    private Table loginButtons = new Table();
    private TextButton next = new TextButton("Next", skin);
    private TextButton exit = new TextButton("Exit", skin);
    
    // Main menu
    private Table mainMenuTitleTable = new Table();
    private Label mainMenuTitle = new Label("Welcome back, therapist", skin);
    
    private Table mainMenuTable = new Table();
    private TextButton button1 = new TextButton("Create Routine", skin);
    private TextButton button2 = new TextButton("Edit Routine", skin);
    private TextButton button3 = new TextButton("Load Routine", skin);
    private TextButton button4 = new TextButton("Exit", skin);
    private TextButton button5 = new TextButton("Logout Therapist", skin);
    
    // Create Routine
    private Table createRoutineTitleTable = new Table();
    private Label createRoutineTitle = new Label("Pick a game to play", skin);
    
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
        loginTitle.add(loginTable).row();
        loginTitle.add(loginButtons);
        
        loginTable.row();
        loginTable.add(unLabel).left();
        unLabel.setFontScale(0.6f);
        loginTable.add(unText).width(400).row();
        loginTable.add(pwLabel).left();
        pwLabel.setFontScale(0.6f);
        loginTable.add(pwText).width(400).row();
        
        loginButtons.add(next).size(300,80).left().padTop(50).align(Align.center).row();
        next.getLabel().setFontScale(0.5f);
        loginButtons.add(exit).size(300,80).left().padTop(10).align(Align.center).row();
        exit.getLabel().setFontScale(0.5f);
                
        next.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                loginTitle.clear();
                loginTitle.remove();          
                loginTable.clear();
                loginTable.remove();
                stage.clear();

                createMainMenu();
            }
        }); 
        
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        }); 
    }
    
    public void createMainMenu() {
        stage.addActor(mainMenuTitleTable);
        mainMenuTitleTable.setFillParent(true);
        mainMenuTitleTable.add(mainMenuTitle).align(Align.center).row();
        mainMenuTitle.setFontScale(0.9f);
        mainMenuTitleTable.add(mainMenuTable);
        
        mainMenuTable.add(button1).size(600, 80).left().padTop(50);
        mainMenuTable.add().size(600, 100).row();
        mainMenuTable.add(button2).size(600, 80).left().padTop(10).row();
        mainMenuTable.add(button3).size(600, 80).left().padTop(10).row();
        mainMenuTable.add(button5).size(600, 80).left().padTop(10).row();
        mainMenuTable.add(button4).size(600, 80).left().padTop(10);

        button1.getLabel().setFontScale(0.6f);
        button1.getLabel().setAlignment(Align.left);
        button2.getLabel().setFontScale(0.6f);
        button2.getLabel().setAlignment(Align.left);
        button3.getLabel().setFontScale(0.6f);
        button3.getLabel().setAlignment(Align.left);
        button4.getLabel().setFontScale(0.6f);
        button4.getLabel().setAlignment(Align.left);
        button5.getLabel().setFontScale(0.6f);
        button5.getLabel().setAlignment(Align.left);
        
        button1.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                mainMenuTitleTable.clear();
                mainMenuTitleTable.remove();
                mainMenuTable.clear();
                mainMenuTable.remove();
                stage.clear();
                createRoutine();
           } 
        });
           
        button5.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //mainMenuTable.clear();
                //mainMenuTable.remove();
                //mainMenuTitleTable.clear();
                //mainMenuTitleTable.remove();
                //stage.clear();

                //createLogin();            
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
        stage.addActor(createRoutineTitleTable);
        createRoutineTitleTable.setFillParent(true);
        createRoutineTitleTable.add(createRoutineTitle).align(Align.center).row();
        createRoutineTitle.setFontScale(0.9f);
        createRoutineTitleTable.add(createRoutineTable);
        
        createRoutineTable.add(buttonA).size(600, 80).left().padTop(50);
        createRoutineTable.add().size(600, 100).row();
        createRoutineTable.add(buttonB).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(buttonC).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(buttonD).size(600, 80).left().padTop(10);
        
        buttonA.getLabel().setFontScale(0.6f);
        buttonA.getLabel().setAlignment(Align.left);
        buttonB.getLabel().setFontScale(0.6f);
        buttonB.getLabel().setAlignment(Align.left);
        buttonC.getLabel().setFontScale(0.6f);
        buttonC.getLabel().setAlignment(Align.left);
        buttonD.getLabel().setFontScale(0.6f);
        buttonD.getLabel().setAlignment(Align.left);
    
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
