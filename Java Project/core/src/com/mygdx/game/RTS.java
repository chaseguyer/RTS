package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.io.File;

/**
 *
 * @author chaseguyer
 */

public class RTS extends Game {
    
    public static MainMenu menu;
    
    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);        
        
        //having a weird problem where public static MainMenu = new MainMenu() wouldn't compile, some problem with MainMenu's stage
        // so i created the menu object and then did this swap around and it seems to work. the purpose is to have a public menu object
        // that i can pass around and hide and change wherever i need to.8
        MainMenu menuAtStart = new MainMenu();
        RTS.menu = menuAtStart;
        
        // create the folder that all the information for this application will go in
        File dir = new File("RTS Data/patients");
        dir.mkdirs();
        dir = new File("RTS Data/therapists");
        dir.mkdirs();
        
        setScreen(menu);
    }
    
}
