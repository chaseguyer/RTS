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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import java.util.ArrayList;

/**
 *
 * @author chaseguyer
 */

/*
    -Highlight boxes when mouse is over them
    -Create new kind of text button w/o box
    -Text field font smaller***




*/



public class MainMenu implements Screen {
    
    // Static variables
    public static int TB_WIDTH = 400; // text button width
    public static int TB_HEIGHT = 100; // text button height
    public static float LABEL_FS = 0.5f; // label font scale
    
    // Set up the stage and ready the skins
    public static Stage stage = new Stage();
    public static Skin skin = new Skin(Gdx.files.internal("skins/skins.json"), new TextureAtlas(Gdx.files.internal("skins/test.pack")));

    // Misc - do some things
    FileIO file = new FileIO(); // file I/O for db stuff
    public String theName, thePw, theConPw, pFirst, pLast; // therapist name, password; patient first and last name
    public float fm, gs, ps, hp; // patient test scores
    
    // Login
    private Table loginTitle = new Table();
    private Label login = new Label("THERAPIST LOGIN", skin);
    
    private Table loginTable = new Table();
    private Label unLabel = new Label("Username:", skin);
    private TextField unText = new TextField("", skin);
    private Label pwLabel = new Label("Password:", skin);
    private final TextField pwText = new TextField("", skin);

    private Table loginButtons = new Table();
    private Label loginError = new Label("Incorrect username or password", skin, "error");
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
    
    private Label pwError = new Label("The passwords are not valid", skin, "error");
    
    private Table newThButtons = new Table();
    private TextButton doneBt = new TextButton("Done", skin);
    private TextButton backBt = new TextButton("Back", skin);
    
    // Therapist menu
    private Table therapistMenuTitleTable = new Table();
    private Label therapistMenuTitle = new Label("", skin); // text will be set when therapist logs in
    
    private Table therapistMenuTable = new Table();
    private TextButton createP = new TextButton("Create Patient Profile", skin);
    private TextButton loadP = new TextButton("Load Patient Profile", skin);
    private TextButton logT = new TextButton("Logout Therapist", skin);
    private TextButton exitT = new TextButton("Exit", skin);
    
    // Patient information
    private Table patientInfoTitleTable = new Table();
    private Label patientInfoTitle = new Label("PATIENT INFORMATION", skin);    
    
    // Name info
    private Table pNameTable = new Table();
    private Label firstNameLabel = new Label("First name: ", skin);
    private TextField firstName = new TextField("", skin);
    private Label lastNameLabel = new Label("Last name: ", skin);
    private TextField lastName = new TextField("", skin);
    
    
    // HAVE TO SET UP THE STYLE
    // Most involved arm
    //private CheckBox box = new CheckBox("Most involved arm", skin);
    
    // Fugel-Meyer score (can add dates with updated scores)
    private Table fmScores = new Table();
    private Label fmLabel = new Label("Fugel-Meyer Score: ", skin);
    private TextField fmTF = new TextField("", skin);
    //private TextButton newScore = new TextButton("New Score?", skin);
    
    // Grip Strength (lbs) (which hand)
    private Table gsScores = new Table();
    private Label gsLabel = new Label("Grip Strength: ", skin);
    private TextField gsTF = new TextField("", skin);
    private Label gsLbs = new Label(" lbs", skin);
    
    // Pinch Strength (lbs) (which hand)
    private Table psScores = new Table();
    private Label psLabel = new Label("Pinch Strength: ", skin);
    private TextField psTF = new TextField("", skin);
    private Label psLbs = new Label(" lbs", skin);
    
    // 9 hole peg test (seconds)
    private Table hpScores = new Table();
    private Label hpLabel = new Label("9 Hole Peg Test: ", skin);
    private TextField hpTF = new TextField("", skin);
    private Label hpSec = new Label(" seconds", skin);
    
    // patient info error
    private Label piError = new Label("One or more of the selections have invalid information", skin, "error");
    
    // patient info buttons
    private Table piButtons = new Table();
    private TextButton piDone = new TextButton("Done", skin);
    private TextButton piBack = new TextButton("Back", skin);
    
    
    
    // Load therpapist's patient list
    private Table thPatList = new Table();
    private Label thPatListTitle = new Label("PLEASE CHOOSE A PATIENT", skin); // set this later with theName
    
    private Table patList = new Table();
    // add new buttons as you go?
    // or have to type the names in?
    private TextButton pat1 = new TextButton("", skin);
    private TextButton pat2 = new TextButton("", skin);
    private TextButton pat3 = new TextButton("", skin);
    private TextButton pat4 = new TextButton("", skin);
    private TextButton pat5 = new TextButton("", skin);
    private TextButton pat6 = new TextButton("", skin);
    private TextButton pat7 = new TextButton("", skin);
    private TextButton pat8 = new TextButton("", skin);
    private TextButton pat9 = new TextButton("", skin);
    private TextButton pat10 = new TextButton("", skin);

    private Table patListButtons = new Table();
    private TextButton patListBack = new TextButton("Back", skin);
    
    
    
    
    // Patient menu
    private Table patientMenuTitleTable = new Table();
    private Label patientMenuTitle = new Label("What to do with the patient?", skin);
    
    private Table patientMenuTable = new Table();
    private TextButton createR = new TextButton("Create Routine", skin);
    private TextButton loadR = new TextButton("Load Routine", skin);
    private TextButton editP = new TextButton("Edit Patient Information", skin);
    private TextButton logP = new TextButton("Logout Patient", skin);
    
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
        
        /*
        TextField text = new TextField("", skin);
        text.setHeight(TB_HEIGHT);
        text.setWidth(TB_WIDTH);
        //text.setPasswordCharacter('*');
        text.setPasswordMode(true);
        
        loginTable.add(text);
        */
        
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
        loginTable.add(unLabel).right();
        unLabel.setFontScale(LABEL_FS);
        loginTable.add(unText).width(TB_WIDTH).height(TB_HEIGHT).row();
        unText.setMaxLength(40);
         
        loginTable.add(pwLabel).right();
        pwLabel.setFontScale(LABEL_FS);
        loginTable.add(pwText).width(TB_WIDTH).height(TB_HEIGHT).row();
        pwText.setPasswordCharacter('*'); // makes text in pwText a '*'
        pwText.setMaxLength(40);

        loginButtons.add(loginError).row();
        loginError.setFontScale(0.6f);
        loginError.setVisible(false);
        loginButtons.add(newTherapist).size(650,80).padTop(30).align(Align.center).row();
        newTherapist.getLabel().setFontScale(LABEL_FS);
        loginButtons.add(next).size(300,80).left().padTop(10).align(Align.center).row();
        next.getLabel().setFontScale(LABEL_FS);
        loginButtons.add(exit).size(300,80).left().padTop(10).align(Align.center).row();
        exit.getLabel().setFontScale(LABEL_FS);

        
        
        /*
        *
        * CREATE NEW THERAPIST
        */
        newTherapistTitleTable.setFillParent(true);
        newTherapistTitleTable.add(newTherapistTitle).padBottom(100).align(Align.center).row();               
        newTherapistTitleTable.add(newTherapistInfo).row();
        newTherapistTitleTable.add(newThButtons);
        
        newTherapistInfo.row();
        
        // New Username:
        newTherapistInfo.add(thNameLabel).left();
        thNameLabel.setFontScale(LABEL_FS);
        newTherapistInfo.add(thName).width(TB_WIDTH).height(TB_HEIGHT).row();
        thName.setMaxLength(40);
        
        // New Password:
        newTherapistInfo.add(thPwLabel).left();
        thPwLabel.setFontScale(LABEL_FS);
        newTherapistInfo.add(thPw).width(TB_WIDTH).height(TB_HEIGHT).row();
        thPw.setPasswordCharacter('*');
        thPw.setMaxLength(40);
        
        // Confirm password:
        newTherapistInfo.add(thConPwLabel).left();     
        thConPwLabel.setFontScale(LABEL_FS);
        newTherapistInfo.add(thConPw).width(TB_WIDTH).height(TB_HEIGHT).row();
        thConPw.setPasswordCharacter('*');
        thConPw.setMaxLength(40);
        
        //error message
        newThButtons.add(pwError).align(Align.center).row();
        pwError.setFontScale(LABEL_FS);
        pwError.setVisible(false); // makes it not visible for now

        // done and back buttons
        newThButtons.add(doneBt).size(300,80).padTop(40).align(Align.center).row();
        doneBt.getLabel().setFontScale(LABEL_FS);
        newThButtons.add(backBt).size(300,80).align(Align.center).row();
        backBt.getLabel().setFontScale(LABEL_FS);
        
        
        
        /*
        *
        * CREATE THERAPIST MENU
        */   
        therapistMenuTitleTable.setFillParent(true);
        therapistMenuTitleTable.add(therapistMenuTitle).align(Align.center).row();
        therapistMenuTitleTable.add(therapistMenuTable);
        
        therapistMenuTable.add(createP).size(600, 80).left().padTop(50);
        therapistMenuTable.add().size(600, 100).row();
        therapistMenuTable.add(loadP).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(logT).size(600, 80).left().padTop(10).row();
        therapistMenuTable.add(exitT).size(600, 80).left().padTop(10).row();

        
        createP.getLabel().setFontScale(LABEL_FS);
        createP.getLabel().setAlignment(Align.left);
        loadP.getLabel().setFontScale(LABEL_FS);
        loadP.getLabel().setAlignment(Align.left);       
        logT.getLabel().setFontScale(LABEL_FS);
        logT.getLabel().setAlignment(Align.left);
        exitT.getLabel().setFontScale(LABEL_FS);
        exitT.getLabel().setAlignment(Align.left);
        
        
        /*
        *
        * Patient Information
        */
        patientInfoTitleTable.setFillParent(true);
        patientInfoTitleTable.add(patientInfoTitle).padBottom(20).align(Align.center).row();
        patientInfoTitleTable.add(pNameTable).left().row();
        patientInfoTitleTable.add(fmScores).left().row();
        patientInfoTitleTable.add(gsScores).left().row();
        patientInfoTitleTable.add(psScores).left().row();
        patientInfoTitleTable.add(hpScores).left().row();
        patientInfoTitleTable.add(piError).row();
        patientInfoTitleTable.add(piButtons);
        
        // Name info
        pNameTable.add(firstNameLabel).left();
        firstNameLabel.setFontScale(LABEL_FS);
        pNameTable.add(firstName).width(TB_WIDTH).height(TB_HEIGHT).row();
        
        pNameTable.add(lastNameLabel).left();
        lastNameLabel.setFontScale(LABEL_FS);
        pNameTable.add(lastName).width(TB_WIDTH).height(TB_HEIGHT).row();
        
        // fm scores
        fmScores.add(fmLabel).left();
        fmLabel.setFontScale(LABEL_FS);
        fmScores.add(fmTF).width(200).height(TB_HEIGHT);
        
        // gs scores
        gsScores.add(gsLabel).left();
        gsLabel.setFontScale(LABEL_FS);
        gsScores.add(gsTF).width(200).height(TB_HEIGHT);
        gsScores.add(gsLbs).left();
        gsLbs.setFontScale(LABEL_FS);

        // ps scores
        psScores.add(psLabel).left();
        psLabel.setFontScale(LABEL_FS);
        psScores.add(psTF).width(200).height(TB_HEIGHT);
        psScores.add(psLbs).left();
        psLbs.setFontScale(LABEL_FS);
        
        // 9 hp scores
        hpScores.add(hpLabel).left();
        hpLabel.setFontScale(LABEL_FS);
        hpScores.add(hpTF).width(200).height(TB_HEIGHT);
        hpScores.add(hpSec).left();
        hpSec.setFontScale(LABEL_FS);
        
        // error message
        piError.setFontScale(LABEL_FS);
        piError.setVisible(false);
        
        // patient info buttons
        piButtons.add(piDone).size(300,80).left().padTop(10).align(Align.center).row();
        piDone.getLabel().setFontScale(LABEL_FS);
        piButtons.add(piBack).size(300,80).left().padTop(10).align(Align.center).row();
        piBack.getLabel().setFontScale(LABEL_FS);
        
        
        
        /*
        *
        * LOAD PATIENT LIST
        */
        thPatList.setFillParent(true);
        thPatList.add(thPatListTitle).align(Align.center).align(Align.top).padTop(20).row();
        thPatList.add(patList).padTop(40).row();
        thPatList.add(patListButtons).padTop(20).row();
        
        int tb_width = 600;
        
        patList.add(pat1).left().width(tb_width).height(TB_HEIGHT).padRight(10).padBottom(10);
        patList.add(pat6).width(tb_width).height(TB_HEIGHT).padBottom(10).row();
        patList.add(pat2).left().width(tb_width).height(TB_HEIGHT).padRight(10).padBottom(10);
        patList.add(pat7).width(tb_width).height(TB_HEIGHT).padBottom(10).row();
        patList.add(pat3).left().width(tb_width).height(TB_HEIGHT).padRight(10).padBottom(10);
        patList.add(pat8).width(tb_width).height(TB_HEIGHT).padBottom(10).row();
        patList.add(pat4).left().width(tb_width).height(TB_HEIGHT).padRight(10).padBottom(10);
        patList.add(pat9).width(tb_width).height(TB_HEIGHT).padBottom(10).row();
        patList.add(pat5).left().width(tb_width).height(TB_HEIGHT).padRight(10).padBottom(10);
        patList.add(pat10).width(tb_width).height(TB_HEIGHT).padBottom(10).row();
        
        pat1.getLabel().setFontScale(LABEL_FS);
        pat2.getLabel().setFontScale(LABEL_FS);
        pat3.getLabel().setFontScale(LABEL_FS);
        pat4.getLabel().setFontScale(LABEL_FS);
        pat5.getLabel().setFontScale(LABEL_FS);
        pat6.getLabel().setFontScale(LABEL_FS);
        pat7.getLabel().setFontScale(LABEL_FS);
        pat8.getLabel().setFontScale(LABEL_FS);
        pat9.getLabel().setFontScale(LABEL_FS);
        pat10.getLabel().setFontScale(LABEL_FS);
        
        patListButtons.add(patListBack).center().width(TB_WIDTH).height(TB_HEIGHT);
        patListBack.getLabel().setFontScale(LABEL_FS);
        
        /*
        *
        * CREATE PATIENT MENU
        */
        patientMenuTitleTable.setFillParent(true);
        patientMenuTitleTable.add(patientMenuTitle).align(Align.center).row();
        patientMenuTitleTable.add(patientMenuTable);
        
        patientMenuTable.add(editP).size(600, 80).left().padTop(50);
        patientMenuTable.add().size(600, 100).row();
        patientMenuTable.add(createR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(loadR).size(600, 80).left().padTop(10).row();
        patientMenuTable.add(logP).size(600, 80).left().padTop(10);

        editP.getLabel().setFontScale(LABEL_FS);
        editP.getLabel().setAlignment(Align.left);
        createR.getLabel().setFontScale(LABEL_FS);
        createR.getLabel().setAlignment(Align.left);
        loadR.getLabel().setFontScale(LABEL_FS);
        loadR.getLabel().setAlignment(Align.left);
        logP.getLabel().setFontScale(LABEL_FS);
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
        
        btA.getLabel().setFontScale(LABEL_FS);
        btA.getLabel().setAlignment(Align.left);
        btB.getLabel().setFontScale(LABEL_FS);
        btB.getLabel().setAlignment(Align.left);
        btC.getLabel().setFontScale(LABEL_FS);
        btC.getLabel().setAlignment(Align.left);
        btD.getLabel().setFontScale(LABEL_FS);
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
            }
        });
 
        // Create new therapist
        newTherapist.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // clear text when we move to next window
                unText.setText("");
                pwText.setText("");
                
                theName = "";
                thePw = "";
                
                loginError.setVisible(false);
                
                stage.clear();
                stage.addActor(newTherapistTitleTable);
            }
        }); 
        
        // Next button
        next.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {             
                if(file.isValidLogin(theName, thePw)) {
                    therapistMenuTitle.setText("Welcome, " + theName + "!");
                
                    loginError.setVisible(false);
                    
                    unText.setText("");
                    pwText.setText("");                
                
                    stage.clear();
                    stage.addActor(therapistMenuTitleTable);  
                } else {
                    loginError.setVisible(true);
                }
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
        * CREATE NEW THERAPIST
        */
        // Create Patient
        newTherapistInfo.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // Grab the text in the fields and put into strings
                theName = thName.getText();
                thePw = thPw.getText();
                theConPw = thConPw.getText();
           } 
        });
                
        doneBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if((thePw == null || theConPw == null) || !(thePw.equals(theConPw))) {
                    pwError.setVisible(true);
                } else {
                    file.newTherapist(theName, thePw);
                    
                    pwError.setVisible(false);
                    
                    // Need to clear text in the fields when leaving this page
                    thName.setText("");
                    thPw.setText("");
                    thConPw.setText("");
                    
                    stage.clear();
                    stage.addActor(loginTitle);
                }  
           } 
        }); 
    
        backBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                thName.setText("");
                thPw.setText("");
                thConPw.setText("");
                
                pwError.setVisible(false);
                
                stage.clear();
                stage.addActor(loginTitle);
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
                stage.addActor(patientInfoTitleTable);
           } 
        });
        
        // Load Patient
        loadP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // if therapist patient list == null
                // print message, would you like to create new patient?
                
                // else
                ArrayList<String> pList = file.loadPatients(theName);
                                
                // set the buttons to the members of pList; I do not like this at all              
                for(int i = 0; i < pList.size(); i++) {
                    if(i == 0) { pat1.getLabel().setText(pList.get(i)); }
                    if(i == 1) { pat2.getLabel().setText(pList.get(i)); }
                    if(i == 2) { pat3.getLabel().setText(pList.get(i)); }
                    if(i == 3) { pat4.getLabel().setText(pList.get(i)); }
                    if(i == 4) { pat5.getLabel().setText(pList.get(i)); }
                    if(i == 5) { pat6.getLabel().setText(pList.get(i)); }
                    if(i == 6) { pat7.getLabel().setText(pList.get(i)); }
                    if(i == 7) { pat8.getLabel().setText(pList.get(i)); }
                    if(i == 8) { pat9.getLabel().setText(pList.get(i)); }
                    if(i == 9) { pat10.getLabel().setText(pList.get(i)); }
                }
                
                
                stage.clear();
                stage.addActor(thPatList);
           } 
        });
        
        // Logout Therapist
        logT.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                theName = "";
                thePw = "";
                
                pat1.getLabel().setText("");
                pat2.getLabel().setText("");
                pat3.getLabel().setText("");
                pat4.getLabel().setText("");
                pat5.getLabel().setText("");
                pat6.getLabel().setText("");
                pat7.getLabel().setText("");
                pat8.getLabel().setText("");
                pat9.getLabel().setText("");
                pat10.getLabel().setText("");
                
                stage.clear();
                stage.addActor(loginTitle);          
            }
        });    
        
        // Exit
        exitT.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });    
        
        
        
        
        /*
        *
        * PATIENT INFORMATION
        */
        pNameTable.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                pFirst = firstName.getText();
                pLast = lastName.getText();
            }
        });
        
        piDone.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {   
                try {
                    fm = Float.parseFloat(fmTF.getText());
                    gs = Float.parseFloat(gsTF.getText());
                    ps = Float.parseFloat(psTF.getText());
                    hp = Float.parseFloat(hpTF.getText());
                    
                    // set the text back to blank
                    firstName.setText("");
                    lastName.setText("");
                    fmTF.setText("");
                    gsTF.setText("");
                    psTF.setText("");
                    hpTF.setText("");       

                    // add the new patient info in their own file and to the patient list
                    file.patientInfo(theName, pFirst, pLast, fm, gs, ps, hp, true); // 1 isNewPatient(); // first item is therapists name

                    pFirst = "";
                    pLast = "";

                    piError.setVisible(false);
                    stage.clear();
                    stage.addActor(therapistMenuTitleTable);
                } catch(NumberFormatException e) {
                    piError.setVisible(true);
                }
            }
        });  
        
        piBack.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                // set the text back to blank
                firstName.setText("");
                lastName.setText("");
                fmTF.setText("");
                gsTF.setText("");
                psTF.setText("");
                hpTF.setText("");   
                
                piError.setVisible(false);
                
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
            }
        });  
        
        
        
        /*
        *
        * LOAD PATIENT PROFILE
        */
        patListBack.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                
                pat1.getLabel().setText("");
                pat2.getLabel().setText("");
                pat3.getLabel().setText("");
                pat4.getLabel().setText("");
                pat5.getLabel().setText("");
                pat6.getLabel().setText("");
                pat7.getLabel().setText("");
                pat8.getLabel().setText("");
                pat9.getLabel().setText("");
                pat10.getLabel().setText("");
                
                
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
            }
        });
        
        // Patient buttons - i really, really dont like this part
        pat1.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(!pat1.getLabel().getText().toString().equals("")) {   
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat2.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat2.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat3.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat3.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat4.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat4.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat5.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat5.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat6.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat6.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat7.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat7.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat8.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat8.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat9.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat9.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
            }
        });
        pat10.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                // set global patient name to this patient
                if(pat10.getLabel().getText().toString().equals("")) {
                    // ignore this button
                } else {
                    // have to grab the patient name here
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                }
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
                stage.clear();
                stage.addActor(createRoutineTitleTable);
                
           } 
        });
           
        // Back (Logout Patient)
        editP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.clear();
                stage.addActor(patientInfoTitleTable);
            }
        });  
        
        // Back (Logout Patient)
        logP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
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
                //MainMenu.hide();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame());
            }
        });    
        
        // Back
        btD.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {                
                stage.clear();
                stage.addActor(patientMenuTitleTable);
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
        
        // table.clear();
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
        patientInfoTitleTable.remove();
        patientMenuTable.remove();
        patientMenuTitleTable.remove();
        psScores.remove();
        therapistMenuTable.remove();
        therapistMenuTitleTable.remove();
        piButtons.remove();
        
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
