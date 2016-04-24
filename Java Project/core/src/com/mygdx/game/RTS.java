package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

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
        setScreen(new MainMenu());
    }
    
}
