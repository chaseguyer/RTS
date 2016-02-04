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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 *
 * @author chaseguyer
 */


/*
    Quests:        
        -In the listeners
            -stage.clear() then stage.addActor(parentTable);
            -Try Joe's hide visibility thing
            
        -Then when Michael's games are called, hide MainMenu, set screen to game
        -Then when you quit Michael's game, set the screen back to MainMenu
*/

public class MainMenu implements Screen {
    
    // Static variables
    public static int TB_WIDTH = 400;
    
    // Set up the stage and ready the skins
    public static Stage stage = new Stage();
    public static Skin skin = new Skin(Gdx.files.internal("skins/skins.json"), new TextureAtlas(Gdx.files.internal("skins/test.pack")));

    // Misc - do some things
    FileIO file = new FileIO(); // file I/O for db stuff
    public String theName, thePw, theConPw;
    
    // Login
    private Table loginTitle = new Table();
    private Label login = new Label("THERAPIST LOGIN", skin);
    
    private Table loginTable = new Table();
    private Label unLabel = new Label("Username:", skin);
    private TextField unText = new TextField("", skin);
    private Label pwLabel = new Label("Password:", skin);
    private TextField pwText = new TextField("", skin);
    
    private Table loginButtons = new Table();
    private TextButton newTherapist = new TextButton("Create New Therapist Profile", skin); // i want normal underlined text for this
    private TextButton next = new TextButton("Next", skin);
    private TextButton exit = new TextButton("Exit", skin);

    
    // Create New Therpist
    private Table newTherapistTitleTable = new Table();
    private Label newTherapistTitle = new Label("CREATE NEW THERAPIST", skin);
    
    private Table newTherapistInfo = new Table();
    
    private Label thNameLabel = new Label("Name:", skin);
    private TextField thName = new TextField("", skin);
    
    private Label thPwLabel = new Label("Password:", skin);
    private TextField thPw = new TextField("", skin);
    
    private Label thConPwLabel = new Label("Confirm Password:", skin);
    private TextField thConPw = new TextField("", skin); 
    
    private Table newThButtons = new Table();
    private TextButton doneBt = new TextButton("Done", skin);
    private TextButton backBt = new TextButton("Back", skin);
    
    // Therapist menu
    private Table therapistMenuTitleTable = new Table();
    private Label therapistMenuTitle = new Label("", skin); // text will be set when therapist logs in
    
    private Table therapistMenuTable = new Table();
    private TextButton createP = new TextButton("Create Patient Profile", skin);
    private TextButton loadP = new TextButton("Load Patient Profile", skin);
    private TextButton settings = new TextButton("Therpist Settings", skin);
    private TextButton logT = new TextButton("Logout Therapist", skin);
    
    // Create paitent menu
    private Table createPTitleTable = new Table();
    private Label createPTitle = new Label("CREATE NEW PATIENT", skin);
    
    
    
    
    
    
    
    // Patient menu
    private Table patientMenuTitleTable = new Table();
    private Label patientMenuTitle = new Label("What to do with the patient?", skin);
    
    private Table patientMenuTable = new Table();
    private TextButton createR = new TextButton("Create Routine", skin);
    private TextButton loadR = new TextButton("Load Routine", skin);
    private TextButton editP = new TextButton("Edit Patient Information", skin);
    private TextButton logP = new TextButton("Logout Patient", skin);
    
    // Edit Patient - make use of SelectBox (drop down menus)
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
    
    // Load Routine
    


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);   
        resize(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height);
        
        createTables();
        createListeners();
        
        stage.addActor(loginTitle);
    }
           
    public void createTables() {
        
        /*
        *
        * CREATE THERAPIST LOGIN
        */
        loginTitle.setFillParent(true);               
        loginTitle.add(login).padBottom(100).align(Align.center).row();
        loginTitle.add(loginTable).row(); // add the text field tables in a new row
        loginTitle.add(loginButtons); // add the buttons table in another row
        
        loginTable.row();
        loginTable.add(unLabel).left();
        unLabel.setFontScale(0.6f);
        loginTable.add(unText).width(TB_WIDTH).row();
        
        loginTable.add(pwLabel).left();
        pwLabel.setFontScale(0.6f);
        loginTable.add(pwText).width(TB_WIDTH).row();
        
        loginButtons.add(newTherapist).size(650,80).left().padTop(50).align(Align.center).row();
        newTherapist.getLabel().setFontScale(0.5f);
        loginButtons.add(next).size(300,80).left().padTop(10).align(Align.center).row();
        next.getLabel().setFontScale(0.5f);
        loginButtons.add(exit).size(300,80).left().padTop(10).align(Align.center).row();
        exit.getLabel().setFontScale(0.5f);
        
        // does not work for some reason
        pwText.setPasswordMode(true); // makes text in pwText bullet points
        
        
        /*
        *
        * CREATE NEW THERAPIST LOGIN
        */
        newTherapistTitleTable.setFillParent(true);
        newTherapistTitleTable.add(newTherapistTitle).padBottom(100).align(Align.center).row();               
        newTherapistTitleTable.add(newTherapistInfo).row();
        newTherapistTitleTable.add(newThButtons);
        
        newTherapistInfo.row();
        
        // New Username:
        newTherapistInfo.add(thNameLabel).left();
        thNameLabel.setFontScale(0.6f);
        newTherapistInfo.add(thName).width(TB_WIDTH).row();
        
        // New Password:
        newTherapistInfo.add(thPwLabel).left();
        thPwLabel.setFontScale(0.6f);
        newTherapistInfo.add(thPw).width(TB_WIDTH).row();
        thPw.setPasswordMode(true);
        
        // Confirm password:
        newTherapistInfo.add(thConPwLabel).left();     
        thConPwLabel.setFontScale(0.6f);
        newTherapistInfo.add(thConPw).width(TB_WIDTH).row();
        thConPw.setPasswordMode(true);
        
        // done and back buttons
        newThButtons.add(doneBt).size(300,80).padTop(40).align(Align.center).row();
        doneBt.getLabel().setFontScale(0.5f);
        newThButtons.add(backBt).size(300,80).align(Align.center).row();
        backBt.getLabel().setFontScale(0.5f);
        
        
        /*
        *
        * CREATE THERAPIST MENU
        */   
        therapistMenuTitleTable.setFillParent(true);
        therapistMenuTitleTable.add(therapistMenuTitle).align(Align.center).row();
        therapistMenuTitle.setFontScale(0.9f);
        therapistMenuTitleTable.add(therapistMenuTable);
        
        therapistMenuTable.add(createP).size(600, 80).left().padTop(50);
        therapistMenuTable.add().size(600, 100).row();
        therapistMenuTable.add(loadP).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(settings).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(logT).size(600, 80).left().padTop(10).row();

        createP.getLabel().setFontScale(0.6f);
        createP.getLabel().setAlignment(Align.left);
        loadP.getLabel().setFontScale(0.6f);
        loadP.getLabel().setAlignment(Align.left);
        settings.getLabel().setAlignment(Align.left);
        settings.getLabel().setFontScale(0.6f);
        logT.getLabel().setFontScale(0.6f);
        logT.getLabel().setAlignment(Align.left);
        
        
        
        /*
        *
        * CREATE PATIENT MENU
        */
        patientMenuTitleTable.setFillParent(true);
        patientMenuTitleTable.add(patientMenuTitle).align(Align.center).row();
        patientMenuTitle.setFontScale(0.9f);
        patientMenuTitleTable.add(patientMenuTable);
        
        patientMenuTable.add(editP).size(600, 80).left().padTop(50);
        patientMenuTable.add().size(600, 100).row();
        patientMenuTable.add(createR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(loadR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(logP).size(600, 80).left().padTop(10);

        editP.getLabel().setFontScale(0.6f);
        editP.getLabel().setAlignment(Align.left);
        createR.getLabel().setFontScale(0.6f);
        createR.getLabel().setAlignment(Align.left);
        loadR.getLabel().setFontScale(0.6f);
        loadR.getLabel().setAlignment(Align.left);
        logP.getLabel().setFontScale(0.6f);
        logP.getLabel().setAlignment(Align.left);
        
        
        /*
        *
        * CREATE ROUTINE
        */
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
        
        
        /*
        *
        * CREATE SOMETHING ELSE
        */
        
        
        
        
    }
    
    public void createListeners() {
        /*
        *
        * CREATE THERAPIST LOGIN
        */
        // Listener for text fields so we can compare the text
        loginTable.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                theName = unText.getText();
                thePw = pwText.getText();
                
                // maybe put me in listener for next?
                //System.out.println("The name will be " + theName);
                therapistMenuTitle.setText("Welcome back, " + theName);
            }
        });
 
        // Create new therapist
        newTherapist.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.clear();
                
                // textFields.clearText()
                
                stage.addActor(newTherapistTitleTable);
            }
        }); 
        
        // Next button
        next.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {             
                file.checkTherapistCreds(theName, thePw);
                
                // open up dbFile, check if therapistName is in dbFile
                // if not, report error & pwText.clearSelection();, else...
                    // check if therapistPw matches password
                    // if not, report error else move on
                
                // Change the title of the therpist menu to print his name           
                
                
                // textFields.clearText()

                
                
                // IF all checks out...
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
            }
        }); 
        
        // Exit button
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        }); 
        
        
        
        /*
        *
        * CREATE NEW THERAPIST LOGIN
        */
        // Create Patient
        newTherapistInfo.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                theName = thName.getText();
                thePw = thPw.getText();
                theConPw = thConPw.getText();
           } 
        });
                
        doneBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                
                // CHECK FOR NULL STRINGS
                
                if(thePw.equals(theConPw)) {
                    file.newTherapist(theName, thePw);

                    stage.clear();
                    stage.addActor(loginTitle);
                } else {
                    // print an error message saying passwords don't match
                    System.out.println("Passwords did not match");
                    Gdx.app.exit();
                }
           } 
        }); 
    
        backBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    

            
                stage.clear();
                stage.addActor(therapistMenuTitleTable);     
            }
        });
        
        
        
        /*
        *
        * CREATE THERAPIST MENU
        */   
        // Create Patient
        createP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                
                stage.clear();
                
           } 
        });
        
        // Load Patient
        loadP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {

                
                stage.clear();
                
                // if therapist patient list == null
                // print message, would you like to create new patient?
                
                // else
                //stage.addActor();
           } 
        });
        
        // Logout Therapist
        logT.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.clear();
                stage.addActor(loginTitle);          
            }
        });    
        
        // Exit
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
        
        
        
        /*
        *
        * CREATE PATIENT MENU
        */
        // Create Routine
        createR.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                patientMenuTitleTable.clear();
                patientMenuTitleTable.remove();
                patientMenuTable.clear();
                patientMenuTable.remove();
                stage.clear();
                
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

            }
        });    
        
        // Exit
        exit.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
        
        
        
        /*
        *
        * CREATE ROUTINE
        */
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
        
        // table.clear();
        createPTitleTable.remove();
        createRoutineTable.remove();
        createRoutineTitleTable.remove();
        fmScores.remove();
        gsScores.remove();
        hpScores.remove();
        loginButtons.remove();
        loginTable.remove();
        loginTitle.remove();
        newThButtons.remove();
        newTherapistInfo.remove();
        newTherapistTitleTable.remove();
        patientInfoTable.remove();
        patientInfoTitleTable.remove();
        patientMenuTable.remove();
        patientMenuTitleTable.remove();
        psScores.remove();
        therapistMenuTable.remove();
        therapistMenuTitleTable.remove();
        
        // listeners.remove();
        
    }

    @Override
    public void render(float f) {
        Gdx.gl.glClearColor((float)0.3176, (float)0.6510, (float)0.5333, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
    }  
}
