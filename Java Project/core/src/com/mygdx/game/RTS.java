package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import java.io.File;

/**
 *
 * @author chaseguyer
 */

public class RTS extends Game {
    
    //MainMenu menu = new MainMenu();
    
    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);        
	//setScreen(menu);
        
        // create the folder that all the information for this game will go in
        File dir = new File("RTS Data/patients");
        dir.mkdirs();
        dir = new File("RTS Data/therapists");
        dir.mkdirs();
        
        setScreen(new MainMenu());
    }
    
}
