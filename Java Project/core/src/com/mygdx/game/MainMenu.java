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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
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

    // Misc
    private TextButton exit = new TextButton("Exit", skin);
    
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
    
    // Therapist menu
    private Table therapistMenuTitleTable = new Table();
    private Label therapistMenuTitle = new Label("Welcome back, therapist", skin);
    
    private Table therapistMenuTable = new Table();
    private TextButton createP = new TextButton("Create Patient", skin);
    private TextButton loadP = new TextButton("Load Patient", skin); 
    private TextButton logT = new TextButton("Logout Therapist", skin);
    
    // Patient menu
    private Table patientMenuTitleTable = new Table();
    private Label patientMenuTitle = new Label("What to do with the patient?", skin);
    
    private Table patientMenuTable = new Table();
    private TextButton createR = new TextButton("Create Routine", skin);
    private TextButton editR = new TextButton("Edit Routine", skin);
    private TextButton loadR = new TextButton("Load Routine", skin);
    private TextButton editP = new TextButton("Edit Patient Information", skin);
    private TextButton logP = new TextButton("Logout Patient", skin);
    
    // Edit/Create Patient - make use of SelectBox (drop down menus)
    private Table patientInfoTitleTable = new Table();
    private Label patientInfoTitle = new Label("Patient Information", skin);
    
    private Table patientInfoTable = new Table();
    
    // Most involved arm
    //private CheckBox box = new CheckBox("Most involved arm", skin);
    
    // Fugel-Meyer score (can add dates with updated scores)
    private Table fmScores = new Table();
    private TextButton newScore = new TextButton("New Score?", skin);
    
    // Grip Strength (lbs) (which hand)
    private Table gsScores = new Table();
    
    // Pinch Strength (lbs) (which hand)
    private Table psScores = new Table();
    
    // 9 hole peg test (seconds)
    private Table hpScores = new Table();
    
    // text boxes to add additional info (keep to minimum)?
    
    // Create Routine
    private Table createRoutineTitleTable = new Table();
    private Label createRoutineTitle = new Label("Pick a game to play", skin);
    
    private Table createRoutineTable = new Table();
    private TextButton btA = new TextButton("I Spy", skin);
    private TextButton btB = new TextButton("Memory", skin);
    private TextButton btC = new TextButton("Maze", skin);
    private TextButton btD = new TextButton("Back", skin);
    
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);   
        resize(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height);
        createTherapistLogin();
    }
           
    public void createTherapistLogin() {                
        stage.clear();
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

                createTherapistMenu();
            }
        }); 
        
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        }); 
    }
    
    public void createTherapistMenu() {
        stage.clear();        
        stage.addActor(therapistMenuTitleTable);
        therapistMenuTitleTable.setFillParent(true);
        therapistMenuTitleTable.add(therapistMenuTitle).align(Align.center).row();
        therapistMenuTitle.setFontScale(0.9f);
        therapistMenuTitleTable.add(therapistMenuTable);
        
        therapistMenuTable.add(createP).size(600, 80).left().padTop(50);
        therapistMenuTable.add().size(600, 100).row();
        therapistMenuTable.add(loadP).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(logT).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(exit).size(600, 80).left().padTop(10).row();

        createP.getLabel().setFontScale(0.6f);
        createP.getLabel().setAlignment(Align.left);
        loadP.getLabel().setFontScale(0.6f);
        loadP.getLabel().setAlignment(Align.left);
        logT.getLabel().setFontScale(0.6f);
        logT.getLabel().setAlignment(Align.left);
        exit.getLabel().setFontScale(0.6f);
        exit.getLabel().setAlignment(Align.left);
        
        // Create Patient
        createP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                therapistMenuTitleTable.clear();
                therapistMenuTitleTable.remove();
                therapistMenuTable.clear();
                therapistMenuTable.remove();
                stage.clear();
                createPatientMenu();
           } 
        });
           
        // Logout Therapist
        logT.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                therapistMenuTable.clear();
                therapistMenuTable.remove();
                therapistMenuTitleTable.clear();
                therapistMenuTitleTable.remove();
                stage.clear();

                createTherapistLogin();            
            }
        });    
        
        // Exit
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
    }
    
    public void createPatientMenu() {
        stage.clear();
        stage.addActor(patientMenuTitleTable);
        patientMenuTitleTable.setFillParent(true);
        patientMenuTitleTable.add(patientMenuTitle).align(Align.center).row();
        patientMenuTitle.setFontScale(0.9f);
        patientMenuTitleTable.add(patientMenuTable);
        
        patientMenuTable.add(editP).size(600, 80).left().padTop(50);
        patientMenuTable.add().size(600, 100).row();
        patientMenuTable.add(createR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(editR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(loadR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(logP).size(600, 80).left().padTop(10);

        editP.getLabel().setFontScale(0.6f);
        editP.getLabel().setAlignment(Align.left);
        createR.getLabel().setFontScale(0.6f);
        createR.getLabel().setAlignment(Align.left);
        editR.getLabel().setFontScale(0.6f);
        editR.getLabel().setAlignment(Align.left);
        loadR.getLabel().setFontScale(0.6f);
        loadR.getLabel().setAlignment(Align.left);
        logP.getLabel().setFontScale(0.6f);
        logP.getLabel().setAlignment(Align.left);
        
        // Create Routine
        createR.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                patientMenuTitleTable.clear();
                patientMenuTitleTable.remove();
                patientMenuTable.clear();
                patientMenuTable.remove();
                stage.clear();
                createRoutine();
           } 
        });
           
        // Back (Logout Patient)
        logP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                patientMenuTable.clear();
                patientMenuTable.remove();
                patientMenuTitleTable.clear();
                patientMenuTitleTable.remove();
                stage.clear();

                createTherapistMenu();            
            }
        });    
        
        // Exit
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
    
    }
    
    public void createRoutine() {
        stage.clear();
        stage.addActor(createRoutineTitleTable);
        createRoutineTitleTable.setFillParent(true);
        createRoutineTitleTable.add(createRoutineTitle).align(Align.center).row();
        createRoutineTitle.setFontScale(0.9f);
        createRoutineTitleTable.add(createRoutineTable);
        
        createRoutineTable.add(btA).size(600, 80).left().padTop(50);
        createRoutineTable.add().size(600, 100).row();
        createRoutineTable.add(btB).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(btC).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(btD).size(600, 80).left().padTop(10);
        
        btA.getLabel().setFontScale(0.6f);
        btA.getLabel().setAlignment(Align.left);
        btB.getLabel().setFontScale(0.6f);
        btB.getLabel().setAlignment(Align.left);
        btC.getLabel().setFontScale(0.6f);
        btC.getLabel().setAlignment(Align.left);
        btD.getLabel().setFontScale(0.6f);
        btD.getLabel().setAlignment(Align.left);
    
        // I Spy
        btA.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new I_Spy());
            }
        });    
        
        // Memory Game
        btB.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame());
            }
        });    
        
        // Back
        btD.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                createRoutineTable.clear();
                createRoutineTable.remove();                              
                stage.clear();
                createPatientMenu();
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
