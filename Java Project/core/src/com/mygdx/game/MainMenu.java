/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 *
 * @author chaseguyer
 */
public class MainMenu implements Screen {

    public static Stage stage = new Stage();
    public static Skin skin = new Skin(Gdx.files.internal("skins/skins.json"), new TextureAtlas(Gdx.files.internal("skins/test.pack")));
    
    private Table table = new Table();
    private TextButton button1 = new TextButton("Create Routine", skin);
    private TextButton button2 = new TextButton("Edit Routine", skin);
    private TextButton button3 = new TextButton("Load Routine", skin);
    private TextButton button4 = new TextButton("Settings", skin);
    
    @Override
    public void show() {
        stage.addActor(table);
        table.setFillParent(true);
        table.add(button1);
        table.row();
        table.add(button2);
        table.row();
        table.add(button3);
        table.row();
        table.add(button4);
        table.row();

               
        button1.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //hide();
            }
        });
        
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float f) {
        Gdx.gl.glClearColor((float)0.3176, (float)0.6510, (float)0.5333, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act();
        stage.draw();
    }

    /*
    buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DSGame.exit = true;
                Gdx.app.exit();
            }
        });
    */
    
    @Override
    public void resize(int i, int i1) {
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
  
}
