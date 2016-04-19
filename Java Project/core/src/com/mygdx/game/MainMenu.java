/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**** README

    Here's a basic description of how the menu works and the layout of the code

    How it works:
    The menu consists of a single screen. That screen is created and on it we will be displaying all of the menu info.
    To the user, it looks like there are multiple screens because it displays different information when they press "Next"
    or "Exit" and the like. What is really going on is when they press a button that will take them to the next "screen"
    libgdx is simply clearing the screen of the table that has all of the buttons and textfields and putting up another one.
    Technically they are all there stacked on top of one another, we just hide and show them based on what the user has clicked
    on. So as I said, all the information displayed on screen is in the form of tables. These tables are laid out in a specific
    fashion(align left, blank row, 10px spacing between text fields, etc.). Libgdx is cool because we can have one parent table for
    the screen and then add more tables to it to make the formatting easier. Then all we have to do is show() or hide() the parent
    table.

    Now onto the code:

    Public declarations:
    Here we have all of the declarations. There are a lot of them because almost no two buttons are similar. I did my best to separate 
    them into the various screens they belong to but it still looks muddled. I also did my best to give the variables clear names to
    indicate what they do, but I admit that they are not the best. Even I get confused sometimes when navigating the code. So all of these
    declarations are the tables, buttons, text fields, etc. How they are all put together comes in section 2.

    createTables():
    This is the function that goes through and adds all of the buttons and tables to the parent tables. It handles all of the formatting.
    I separated the sections into the various "screens" that the user will see.

    createListeners():
    So now that we have a bunch of "screens" and buttons, we need to tell the buttons what to do when they are pressed. That is what this
    function does. It tells which tables to hide and which to show when certain buttons are pressed. It also handles certain I/O functions
    when dealing with saving and loading text files that contain the patient/therapist information.
    
    FileIO class:
    This is how we handle saving and loading information. Through text files. To be honest, there is a much better way of doing this, and that
    is through creating an SQL database; however, we simply did not have the time this semester to implement such a thing. Basically when certain
    listeners are pressed, we will open or create text files and read/write from/to them. The info is separated by commas and I used a scanner to
    sift through it.

****/

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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
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
    TODO
    
    TECHNICAL DEBT
    // patient info
    -text field at the end of patient info
    -add a test option
    -dates for tests
    -should be able to have no info in the fields
    -trail making a || b || (a && b)
    -class routine
        -pinch : tip
        -3 jaw
        -lateral

    -num trials before feedback is given (6 or 12)

    // misc
    -cursor in text fields
    -Duplicate username
    -Patient/therapist deletion
    -HIDE THE PASSWORDDDD


    NEXT ITERATION
    // create routine
    
    // edit routine

    // delete routine


    Priorities:

    1. Finish patient information page
        -text box at the end
        -file IO stuff involved
        -be able to add and keep track of dates for patient

    2. Routine creation
        -Be able to choose from the 4 games
        -Be able to customize parameters
        -Link the games via the routines

    3. Misc
        -Password hiding
        -Patient/Therapist deletion
        -patient info scrolling



*/

public class MainMenu implements Screen {
    
    // Static variables
    public static int TB_WIDTH = 400; // text button width
    public static int TB_HEIGHT = 100; // text button height
    public static float LABEL_FS = 0.5f; // label font scale
    public static int CB_SIDE = 80;
    
    // Set up the stage and ready the skins
    public static Stage stage = new Stage();
    public static Skin skin = new Skin(Gdx.files.internal("skins/skins.json"), new TextureAtlas(Gdx.files.internal("skins/test.pack")));

    // Misc - do some things
    FileIO file = new FileIO(); // file I/O for db stuff
    public String theName, thePw, theConPw, pFirst, pLast; // therapist name, password; patient first and last name
    public float fm = 0f, gs = 0f, ps = 0f, hp = 0f; // patient test scores
    public boolean lArmBool = false, rArmBool = false, bArmBool = false;
    
    
    
    /*
     *
     * THERAPIST LOGIN
     */
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

    
    
    /*
     *
     * CREATE THERAPIST
     */
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
    
    
    
    /*
     *
     * THERAPIST MENU
     */
    private Table therapistMenuTitleTable = new Table();
    private Label therapistMenuTitle = new Label("", skin); // text will be set when therapist logs in
    
    private Table therapistMenuTable = new Table();
    private TextButton createP = new TextButton("Create Patient Profile", skin);
    private TextButton loadP = new TextButton("Load Patient Profile", skin);
    private TextButton logT = new TextButton("Logout Therapist", skin);
    private TextButton exitT = new TextButton("Exit", skin);
    
    
    
    /*
     *
     * PATIENT INFORMATION
     */
    private Table patientInfoTable = new Table(); // add scroller to me
    private Table patientInfoTitleTable = new Table();
    private ScrollPane scroller = new ScrollPane(patientInfoTitleTable, skin);
    private Label patientInfoTitle = new Label("PATIENT INFORMATION", skin);
    
    // Label table
    private Table patientInfoLabelTable = new Table();
    private Label firstNameLabel = new Label("First name: ", skin);
    private Label lastNameLabel = new Label("Last name: ", skin);
    private Label involvedArmLabel = new Label("Most involved arm: ", skin);
    private Label fmLabel = new Label("Fugl-Meyer Score: ", skin);
    private Label gsLabel = new Label("Grip Strength(lbs): ", skin);
    private Label psLabel = new Label("Pinch Strength(lbs): ", skin);
    private Label hpLabel = new Label("9 Hole Peg Test(sec): ", skin);
    
    // Text Field table
    private Table patientInfoValuesTable = new Table();
    private TextField firstNameTextField = new TextField("", skin);
    private TextField lastNameTextField = new TextField("", skin);
    private TextField fmTextField = new TextField("", skin);
    private TextField gsTextField = new TextField("", skin);
    private TextField psTextField = new TextField("", skin);
    private TextField hpTextField = new TextField("", skin);
    
    private Label lArmLabel = new Label("Left ", skin);
    private Label rArmLabel = new Label("Right ", skin);
    private Label bArmLabel = new Label("Both ", skin);
        
    private CheckBox lArmBox = new CheckBox("", skin);
    private CheckBox rArmBox = new CheckBox("", skin);
    private CheckBox bArmBox = new CheckBox("", skin);
    
    // Drop down menu
    private SelectBox dropDown = new SelectBox(skin);
    
    // patient info error
    private Label piError = new Label("One or more of the selections have invalid information", skin, "error");
    
    // patient info buttons
    private Table piButtons = new Table();
    private TextButton piDone = new TextButton("Done", skin);
    private TextButton piBack = new TextButton("Back", skin);
    
    
    
    /*
     *
     * LOAD PATIENT FIELDS
     */
    private Table thPatFields = new Table();
    private Label thPatFieldsTitle = new Label("PLEASE ENTER YOUR PATIENT'S NAME", skin); 
    
    private Table patFields = new Table();
    
    private Label loadPatientFNLabel = new Label("First Name: ", skin);
    private Label loadPatientLNLabel = new Label("Last Name: ", skin);
    
    private TextField loadPatientFNTextField = new TextField("", skin);
    private TextField loadPatientLNTextField = new TextField("", skin);
    
    private Label loadPatientError = new Label("The patient entered does not exist", skin, "error");

    private Table patFieldsButtons = new Table();
    private TextButton patFieldsDone = new TextButton("Done", skin);
    private TextButton patFieldsBack = new TextButton("Back", skin);
    
    
    
    /*
     *
     * PATIENT MENU
     */
    private Table patientMenuTitleTable = new Table();
    private Label patientMenuTitle = new Label("What to do with the patient?", skin);
    
    private Table patientMenuTable = new Table();
    private TextButton createR = new TextButton("Create Routine", skin);
    private TextButton loadR = new TextButton("Load Routine", skin);
    private TextButton editP = new TextButton("Edit Patient Information", skin);
    private TextButton logP = new TextButton("Logout Patient", skin);
    
    
    
    /*
     *
     * CREATE ROUTINE
     */
    private Table createRoutineTable = new Table();
    
    // title
    private Table createRoutineTitleTable = new Table();
    private Label createRoutineTitle = new Label("CREATE ROUTINE", skin);
    
    // choose which game to modify
    private Table gameTable = new Table();
    private Label gameLabel = new Label("Choose a game to add", skin);
    private TextButton ispyBt = new TextButton("I Spy", skin);
    private TextButton memoryBt = new TextButton("Memory", skin);
    private TextButton mazeBt = new TextButton("Maze", skin);
    private TextButton pathTraceBt = new TextButton("Path Tracing", skin);
    
    // The routine name table and the routine overview table will reside here
    private Table leftSide = new Table();
    
    // name routine
    private Table nameRoutineTable = new Table();
    private Label nameRoutineLabel = new Label("Please name your routine:", skin);
    private TextField nameRoutineTextField = new TextField("", skin);
    
    // routine overview
    private Table routineOverviewTable = new Table();
    private Label routineOverviewLabel = new Label("Your routine:", skin);
    private TextField routineOverviewTextField = new TextField("", skin); // may need to find a better solution
                                                                          // because this will be modifiable
    // buttons
    private Table routineButtonsTable = new Table();
    private TextButton routineDone = new TextButton("Done", skin);
    private TextButton routineBack = new TextButton("Back", skin);
    
    
    
    /*
     *
     * ISPY ROUTINE CREATION
     */
    
    
    
    
    
    /*
     *
     * MEMORY ROUTINE CREATION
     */
    
    
    
    
    
    /*
     *
     * MAZE ROUTINE CREATION
     */
    
    
    
    
    
    /*
     *
     * PATH TRACING ROUTINE CREATION
     */
    
    
    
    
    
    
    /*
     *
     * LOAD ROUTINE
     */
    
    
    


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
        therapistMenuTable.add().size(600, 100).row(); // this is to push everything to the left
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
        patientInfoTable.setFillParent(true);
        patientInfoTable.add(scroller).fill().expand();
        patientInfoTitleTable.add(patientInfoTitle).padTop(50).padBottom(20).align(Align.center).row();
        
        // put label table and values table into their own table, then add that to patient info title table
        Table patientInfoDoubleTable = new Table();
        patientInfoDoubleTable.add(patientInfoLabelTable);
        patientInfoDoubleTable.add(patientInfoValuesTable);
        
        patientInfoTitleTable.add(patientInfoDoubleTable).center().row();
        patientInfoTitleTable.add(piError).row();
        patientInfoTitleTable.add(piButtons);
        
        // title
        patientInfoTitle.setFontScale(0.8f);
        
        // specific for these labels only
        int LABEL_BOT_PAD = 62;
        int TF_BOT_PAD = 10;
        
        // patientInfoLabelTable
        patientInfoLabelTable.add(firstNameLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(lastNameLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(involvedArmLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(fmLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(gsLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(psLabel).left().padBottom(LABEL_BOT_PAD).row();
        patientInfoLabelTable.add(hpLabel).left();
        firstNameLabel.setFontScale(LABEL_FS);
        lastNameLabel.setFontScale(LABEL_FS);
        involvedArmLabel.setFontScale(LABEL_FS);
        fmLabel.setFontScale(LABEL_FS);
        gsLabel.setFontScale(LABEL_FS);
        psLabel.setFontScale(LABEL_FS);
        hpLabel.setFontScale(LABEL_FS);
        
        // patientInfoValuesTable
        patientInfoValuesTable.add(firstNameTextField).width(TB_WIDTH).height(TB_HEIGHT).padBottom(TF_BOT_PAD).row();
        patientInfoValuesTable.add(lastNameTextField).width(TB_WIDTH).height(TB_HEIGHT).padBottom(TF_BOT_PAD).row();
        
        // Since we need to cram multiple things into one cell, we will create a table, add the stuff to it, then the table to the cell
        Table armStuff = new Table(); 
        
        // left arm  
        armStuff.add(lArmBox).width(CB_SIDE).height(CB_SIDE).left();
        armStuff.add(lArmLabel).left().padRight(20);
        lArmLabel.setFontScale(LABEL_FS);
        
        // right arm
        armStuff.add(rArmBox).width(CB_SIDE).height(CB_SIDE).left();
        armStuff.add(rArmLabel).left().padRight(20);
        rArmLabel.setFontScale(LABEL_FS);
        
        // both arms
        armStuff.add(bArmBox).width(CB_SIDE).height(CB_SIDE).left();
        armStuff.add(bArmLabel).left();
        bArmLabel.setFontScale(LABEL_FS);
        
        patientInfoValuesTable.add(armStuff).padLeft(170).padBottom(TF_BOT_PAD).row(); // add the table to the cell
        
        // ...back to patientInfoValuesTable
        patientInfoValuesTable.add(fmTextField).width(TB_WIDTH).height(TB_HEIGHT).padBottom(TF_BOT_PAD).row();
        patientInfoValuesTable.add(gsTextField).width(TB_WIDTH).height(TB_HEIGHT).padBottom(TF_BOT_PAD).row();
        patientInfoValuesTable.add(psTextField).width(TB_WIDTH).height(TB_HEIGHT).padBottom(TF_BOT_PAD).row();
        patientInfoValuesTable.add(hpTextField).width(TB_WIDTH).height(TB_HEIGHT);
        
        // error message
        piError.setFontScale(LABEL_FS);
        piError.setVisible(false);
        
        // patient info buttons
        piButtons.add(piDone).size(300,80).left().padTop(0).center().row();
        piDone.getLabel().setFontScale(LABEL_FS);
        piButtons.add(piBack).size(300,80).left().padTop(10).center().row();
        piBack.getLabel().setFontScale(LABEL_FS);
        
        piButtons.add(dropDown).row();
        
        String[] poop = new String[]{"stuff", "things", "some other things", "oh yea and that other thing"};
        
        dropDown.setItems((Object) poop);
        
        
        
        /*
        *
        * LOAD PATIENT FIELDS
        */
        thPatFields.setFillParent(true);
        thPatFields.add(thPatFieldsTitle).align(Align.center).align(Align.top).padTop(20).row();
        thPatFields.add(patFields).padTop(20).row();
        thPatFields.add(patFieldsButtons).row();
        
        // First name
        patFields.add(loadPatientFNLabel).left();
        patFields.add(loadPatientFNTextField).width(TB_WIDTH).height(TB_HEIGHT).left().row();
        loadPatientFNLabel.setFontScale(LABEL_FS);
        
        // Last name
        patFields.add(loadPatientLNLabel).left();
        patFields.add(loadPatientLNTextField).width(TB_WIDTH).height(TB_HEIGHT).left().row();
        loadPatientLNLabel.setFontScale(LABEL_FS);
        
        // error message
        patFieldsButtons.add(loadPatientError).center().row();
        loadPatientError.setFontScale(LABEL_FS);
        loadPatientError.setVisible(false);
        
        // buttons
        patFieldsButtons.add(patFieldsDone).center().width(TB_WIDTH).height(TB_HEIGHT).row();
        patFieldsButtons.add(patFieldsBack).center().width(TB_WIDTH).height(TB_HEIGHT);
        patFieldsBack.getLabel().setFontScale(LABEL_FS);
        patFieldsDone.getLabel().setFontScale(LABEL_FS);
        
        
        
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
        createRoutineTitleTable.add(createRoutineTable).left();
        
        
        createRoutineTable.add(ispyBt).size(600, 80).left().padTop(50);
        createRoutineTable.add().size(600,100).row(); // this is to push everything to the left
        createRoutineTable.add(memoryBt).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(mazeBt).size(600, 80).left().padTop(10).row();
        createRoutineTable.add(routineBack).size(600, 80).left().padTop(10);
        
        ispyBt.getLabel().setFontScale(LABEL_FS);
        ispyBt.getLabel().setAlignment(Align.left);
        memoryBt.getLabel().setFontScale(LABEL_FS);
        memoryBt.getLabel().setAlignment(Align.left);
        mazeBt.getLabel().setFontScale(LABEL_FS);
        mazeBt.getLabel().setAlignment(Align.left);
        routineBack.getLabel().setFontScale(LABEL_FS);
        routineBack.getLabel().setAlignment(Align.left);
        
        
        
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
                    
                    theName = "";
                    thePw = "";
                    
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
                stage.addActor(patientInfoTable);
            } 
        });
        
        // Load Patient
        loadP.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                stage.clear();
                stage.addActor(thPatFields);
            } 
        });
        
        // Logout Therapist
        logT.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                theName = "";
                thePw = "";
             
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
        piDone.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {   
                try {
                    // file io stuff for the first and last name
                    pFirst = firstNameTextField.getText();
                    pLast = lastNameTextField.getText();
                    
                    // file io stuff for the check boxes
                    lArmBool = lArmBox.isChecked();
                    rArmBool = rArmBox.isChecked();
                    bArmBool = bArmBox.isChecked();
                    
                    // the following ifs handle the case where the therapist put nothing in the fields
                    // instead it just sets the fields to 0.0
                    if(!fmTextField.getText().equals("")) {
                        fm = Float.parseFloat(fmTextField.getText());
                    }
                    
                    if(!gsTextField.getText().equals("")) {
                        gs = Float.parseFloat(gsTextField.getText());
                    }
                    
                    if(!psTextField.getText().equals("")) {
                        ps = Float.parseFloat(psTextField.getText());
                    }
                    
                    if(!hpTextField.getText().equals("")) {
                        hp = Float.parseFloat(hpTextField.getText());
                    }
                    
                    // add the new patient info in their own file and to the patient list
                    // bool isNewPatient(); // first item is therapists name
                    file.patientInfo(theName, pFirst, pLast, lArmBool, rArmBool, bArmBool, fm, gs, ps, hp, true); 

                    // zero out the actors and the global variables
                    // first and last name
                    pFirst = pLast = "";
                    firstNameTextField.setText("");
                    lastNameTextField.setText("");
                    
                    // arm check boxes
                    lArmBool = rArmBool = bArmBool = false;
                    lArmBox.setChecked(false);
                    rArmBox.setChecked(false);
                    bArmBox.setChecked(false);
                    
                    // test scores
                    fm = gs = ps = hp = 0f;
                    fmTextField.setText("");
                    gsTextField.setText("");
                    psTextField.setText("");
                    hpTextField.setText("");       
                    
                    scroller.scrollTo(0, 0, 0, 0);                 
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
                // zero out the actors and the global variables
                // first and last name
                pFirst = pLast = "";
                firstNameTextField.setText("");
                lastNameTextField.setText("");

                // arm check boxes
                lArmBool = rArmBool = bArmBool = false;
                lArmBox.setChecked(false);
                rArmBox.setChecked(false);
                bArmBox.setChecked(false);

                // test scores
                fm = gs = ps = hp = 0f;
                fmTextField.setText("");
                gsTextField.setText("");
                psTextField.setText("");
                hpTextField.setText("");
                
                scroller.scrollTo(1, 1, 1, 1);
                piError.setVisible(false);
                
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
            }
        });  
        
        
        
        /*
        *
        * LOAD PATIENT PROFILE
        */
        patFieldsDone.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                
                // patient found
                if(file.isPatient(theName, loadPatientFNTextField.getText(), loadPatientLNTextField.getText())) {
                    patientMenuTitle.setText("Menu for " + loadPatientFNTextField.getText() + " " + loadPatientLNTextField.getText()); 
                        
                    // clear text fields
                    loadPatientLNTextField.setText("");
                    loadPatientFNTextField.setText("");
            
                    loadPatientError.setVisible(false);
                    
                    stage.clear();
                    stage.addActor(patientMenuTitleTable);
                } else { // patient not found
                    loadPatientError.setVisible(true);
                }
            }
        });
        
        patFieldsBack.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {    
                
                // clear text fields
                loadPatientLNTextField.setText("");
                loadPatientFNTextField.setText("");
            
                loadPatientError.setVisible(false);
                
                stage.clear();
                stage.addActor(therapistMenuTitleTable);
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
                stage.addActor(patientInfoTable);
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
        ispyBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new I_Spy());
            }
        });    
        
        // Memory Game
        memoryBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //MainMenu.hide();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame());
            }
        });    
        
        mazeBt.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //MainMenu.hide();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MazeGame());
            }
        }); 
        
        // Back
        routineBack.addListener(new ChangeListener() {
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
        patientInfoLabelTable.remove();
        patientInfoValuesTable.remove();
        loginButtons.remove();
        loginTable.remove();
        loginTitle.remove();
        newThButtons.remove();
        newTherapistInfo.remove();
        newTherapistTitleTable.remove();
        patientInfoTitleTable.remove();
        patientMenuTable.remove();
        patientMenuTitleTable.remove();
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
