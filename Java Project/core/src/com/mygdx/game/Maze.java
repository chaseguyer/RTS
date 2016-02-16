/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author Kenny
 */
public class Maze {
    private int width, height;
    private List<List<Node>> maze;
    private Texture wall;
    private Texture path;
    private float wallWidth;
    
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        wall = new Texture(Gdx.files.internal("Textures/Wall.png"));
        path = new Texture(Gdx.files.internal("Textures/Path.png"));
        wallWidth = wall.getWidth();
        kruskal();
    }
    
    public void draw(SpriteBatch batch) {
        batch.begin();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                batch.draw(wall, i*3*wallWidth, j*3*wallWidth);
                batch.draw(wall, (i*3+2)*wallWidth, j*3*wallWidth);
                batch.draw(wall, (i*3+2)*wallWidth, (j*3+2)*wallWidth);
                batch.draw(wall, i*3*wallWidth, (j*3+2)*wallWidth);
                batch.draw(path, (i*3+1)*wallWidth, (j*3+1)*wallWidth);
                
                if (maze.get(i).get(j).isUp()) {
                    batch.draw(path, (i*3+1)*wallWidth, (j*3+2)*wallWidth);
                }
                else {
                    batch.draw(wall, (i*3+1)*wallWidth, (j*3+2)*wallWidth);
                }
                
                if (maze.get(i).get(j).isDown()) {
                    batch.draw(path, (i*3+1)*wallWidth, j*3*wallWidth);
                }
                else {
                    batch.draw(wall, (i*3+1)*wallWidth, j*3*wallWidth);
                }
                
                if (maze.get(i).get(j).isLeft()) {
                    batch.draw(path, i*3*wallWidth, (j*3+1)*wallWidth);
                }
                else {
                    batch.draw(wall, i*3*wallWidth, (j*3+1)*wallWidth);
                }
                
                if (maze.get(i).get(j).isRight()) {
                    batch.draw(path, (i*3+2)*wallWidth, (j*3+1)*wallWidth);
                }
                else {
                    batch.draw(wall, (i*3+2)*wallWidth, (j*3+1)*wallWidth);
                }
            }
        }
        batch.end();
    }
    
    public void print() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (maze.get(j).get(i).isDown()) {
                    System.out.print("# #");
                }
                else {
                    System.out.print("###");
                }
            }
            System.out.println();
            for (int j = 0; j < width; ++j) {
                if (maze.get(j).get(i).isLeft() && maze.get(j).get(i).isRight()) {
                    System.out.print("   ");
                }
                else if (maze.get(j).get(i).isLeft()) {
                    System.out.print("  #");
                }
                else if (maze.get(j).get(i).isRight()) {
                    System.out.print("#  ");
                }
                else {
                    System.out.print("# #");
                }
            }
            System.out.println();
            for (int j = 0; j < width; ++j) {
                if (maze.get(j).get(i).isUp()) {
                    System.out.print("# #");
                }
                else {
                    System.out.print("###");
                }
            }
            System.out.println();
        }
    }
    
    private void kruskal() {
        List<List<Node>> grid = new ArrayList();
        List<Pair<Integer,Integer>> points = new ArrayList();
        
        int k = 0;
        for (int i = 0; i < width; ++i) {
            List<Node> l = new ArrayList();
            for (int j = 0; j < height; ++j) {
                l.add(new Node(k));
                points.add(new Pair(i,j));
                ++k;
            }
            grid.add(l);
        }
        
        Collections.shuffle(points);
        
        for (int i = 0; i < points.size(); ++i) {
            Pair<Integer,Integer> p = points.get(i);
            int x = p.getKey();
            int y = p.getValue();
            if (x == 0 && y == 0) {
                connectProbalistic(false,true,false,true,x,y,grid);
            }
            else if (x == width-1 && y == height-1) {
                connectProbalistic(true,false,true,false,x,y,grid);
            }
            else if (x == 0 && y == height -1) {
                connectProbalistic(false,true,true,false,x,y,grid);
            }
            else if (x == width-1 && y == 0) {
                connectProbalistic(true,false,false,true,x,y,grid);
            }
            else if (x == 0 && y < height-1 && y > 0) {
                connectProbalistic(false,true,true,true,x,y,grid);
            }
            else if (y == 0 && x < width-1 && x > 0) {
                connectProbalistic(true,true,false,true,x,y,grid);
            }
            else if (x == width-1 && y < height-1 && y > 0) {
                connectProbalistic(true,false,true,true,x,y,grid);
            }
            else if (y == height-1 && x > 0 && x < width-1) {
                connectProbalistic(true,true,true,false,x,y,grid);
            }
            else {
                connectProbalistic(true,true,true,true,x,y,grid);
            }
        }
        
        maze = grid;
    }
    
    private void connectProbalistic(
            boolean left, 
            boolean right, 
            boolean down, 
            boolean up,
            int x,
            int y,
            List<List<Node>> grid) {
        List<Pair<Integer,Integer>> points = new ArrayList();
        if (left) {
            points.add(new Pair(x-1,y));
        }
        if (right) {
            points.add(new Pair(x+1,y));
        }
        if (down) {
            points.add(new Pair(x,y-1));
        }
        if (up) {
            points.add(new Pair(x,y+1));
        }
        Collections.shuffle(points);
        for (int i = 0; i < points.size(); ++i) {
            int connectX = points.get(i).getKey();
            int connectY = points.get(i).getValue();
            if (eligible(x,y,connectX,connectY,grid)) {
                connectPoints(x,y,connectX,connectY,grid);
                break;
            }
        }
    }
    
    private boolean eligible(int x1, int y1, int x2, int y2, List<List<Node>> grid) {
        return grid.get(x1).get(y1).getSetnum() != grid.get(x2).get(y2).getSetnum();
    }
    
    private void connectPoints(int x1, int y1, int x2, int y2, List<List<Node>> grid) {
        Node n = grid.get(x1).get(y1);
        Node n2 = grid.get(x2).get(y2);
        if (x2 > x1) {
            n.setRight(true);
            n2.setLeft(true);
        }
        else if (y2 > y1) {
            n.setUp(true);
            n2.setDown(true);
        }
        else if (x1 > x2) {
            n.setLeft(true);
            n2.setRight(true);
        }
        else if (y1 > y2) {
            n.setDown(true);
            n2.setUp(true);
        }
        else {
            return;
        }
        mergeSets(n.getSetnum(), n2.getSetnum(), grid);
    }
    
    private void mergeSets(int s1, int s2, List<List<Node>> grid) {
        for (int i = 0; i < grid.size(); ++i) {
            for (int j = 0; j < grid.get(i).size(); ++j) {
                if (grid.get(i).get(j).getSetnum() == s1) {
                    grid.get(i).get(j).setSetnum(s2);
                }
            }
        }
    }
}
