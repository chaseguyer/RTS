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
    private Texture wall;
    private Texture path;
    private Texture dot;
    private float wallWidth;
    private float wallHeight;
    private float dotRadius;
    private float redDotX;
    private float redDotY;
    private float startX;
    private float startY;
    private int[][] genMaze;
    private int screenWidth;
    private int screenHeight;
    private static int lastEndX = -1;
    private static int lastEndY = -1;
    
    /**
     * Make a new maze.
     * @param width Number of nodes width-wise.
     * @param height Number of nodes height-wise.
     * @param screenWidth
     * @param screenHeight
     */
    public Maze(int width, int height, int screenWidth, int screenHeight) {
        this.width = width;
        this.height = height;
        wall = new Texture(Gdx.files.internal("Textures/Wall.png"));
        path = new Texture(Gdx.files.internal("Textures/Path.png"));
        dot = new Texture(Gdx.files.internal("Textures/Ball.png"));
        wallWidth = (float)screenWidth/(float)(width*2+1);
        wallHeight = (float)screenHeight/(float)(height*2+1);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        dotRadius = dot.getWidth()/2;
        
        Random r = new Random(System.currentTimeMillis());
        List<Point> corners = new ArrayList();
        corners.add(new Point(1,1));
        corners.add(new Point(1, 1+2*(height-1)));
        corners.add(new Point(1+2*(width-1), 1+2*(height-1)));
        corners.add(new Point(1+2*(width-1), 1));
        
        // make sure the last ending point is not the same as the beginning
        // point of this maze.
        do {
            Collections.shuffle(corners, r);
        } while (corners.get(0).x == lastEndX && corners.get(0).y == lastEndY);
        
        startX = (1+corners.get(0).x)*wallWidth-wallWidth/2;
        redDotX = (1+corners.get(1).x)*wallWidth-wallWidth/2;
        startY = (1+corners.get(0).y)*wallHeight-wallHeight/2;
        redDotY = (1+corners.get(1).y)*wallHeight-wallHeight/2;
        lastEndX = corners.get(1).x;
        lastEndY = corners.get(1).y;
        
        kruskal();
    }
    
    /**
     * Draw the maze.
     * @param batch The spritebatch to draw on.
     */
    public void draw(SpriteBatch batch) {
        batch.begin();
        for (int i = 0; i < genMaze.length; ++i) {
            for (int j = 0; j < genMaze[i].length; ++j) {
                
                if (genMaze[i][j] == 0) {
                    batch.draw(wall, i*wallWidth, j*wallHeight, wallWidth, wallHeight);
                }
                else {
                    batch.draw(path, i*wallWidth, j*wallHeight, wallWidth, wallHeight);
                }
            }
        }
        batch.draw(dot, redDotX-dotRadius, redDotY-dotRadius);
        batch.end();
    }
    
    /**
     * Print the maze on the console.
     */
    public void print() {
        for (int i = 0; i < genMaze.length; ++i) {
            for (int j = 0; j < genMaze[i].length; ++j) {
                if (genMaze[i][j] == 0) {
                    System.out.print("#");
                }
                else if (genMaze[i][j] == 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    private void kruskal() {
        // 1. Make maze
        int maze[][] = new int[width][height];
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                maze[i][j] = width*j+i;
            }
        }
        // 2. Create edges
        List<Edge> edges = new ArrayList();
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (i < width-1) {
                    Edge e = new Edge();
                    e.x1 = i;
                    e.y1 = j;
                    e.x2 = i+1;
                    e.y2 = j;
                    edges.add(e);
                }
                if (j < height-1) {
                    Edge e = new Edge();
                    e.x1 = i;
                    e.y1 = j;
                    e.x2 = i;
                    e.y2 = j+1;
                    edges.add(e);
                }
            }
        }
        // 3. Randomize edges
        Collections.shuffle(edges);
        // 4. Select edges for maze
        List<Edge> selectedEdges = new ArrayList();
        for (int i = 0; i < edges.size(); ++i) {
            Edge c = edges.get(i);
            if (maze[c.x1][c.y1] != maze[c.x2][c.y2]) {
                selectedEdges.add(c);
                int findVal = maze[c.x2][c.y2];
                int replaceVal = maze[c.x1][c.y1];
                for (int j = 0; j < width; ++j) {
                    for (int k = 0; k < height; ++k) {
                        if (maze[j][k] == findVal) {
                            maze[j][k] = replaceVal;
                        }
                    }
                }
            }
        }
        // 5. Generate Maze
        genMaze = new int[width*2+1][height*2+1];
        for (int i = 0; i < width*2+1; ++i) {
            for (int j = 0; j < height*2+1; ++j) {
                genMaze[i][j] = 0;
            }
        }
        List<Point> points = new ArrayList();
        for (int i = 0; i < selectedEdges.size(); ++i) {
            Edge c = selectedEdges.get(i);
            genMaze[c.x1*2+1][c.y1*2+1] = 1;
            genMaze[c.x2*2+1][c.y2*2+1] = 1;
            genMaze[c.x1+c.x2+1][c.y1+c.y2+1] = 1;
            points.add(new Point(c.x1*2+1,c.y1*2+1));
            points.add(new Point(c.x2*2+1,c.y2*2+1));
            points.add(new Point(c.x1+c.x2+1,c.y1+c.y2+1));
        }
    }
    
    /**
     *
     * @return
     */
    public float getStartX() {
        //return (wallWidth)/2*3;
        return startX;
    }
    
    /**
     *
     * @return
     */
    public float getStartY() {
        //return (wallHeight)/2*3;
        return startY;
    }
    
    /**
     * Checks if the position is within a wall or not.
     * @param x
     * @param y
     * @param radius
     * @return True if the position is in a path, false if in a wall.
     */
    public boolean positionGood(int x, int y, double radius) {
        for (int i = 0; i < genMaze.length; ++i) {
            for (int j = 0; j < genMaze[i].length; ++j) {
                if (genMaze[i][j] == 0) {
                    Rectangle rect = new Rectangle(i*wallWidth,j*wallHeight,wallWidth,wallHeight);
                    if (checkCircleRectangleCollision(x,y,radius,rect)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Check to see if objective has been met
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public boolean gotTarget(double x, double y, double radius) {
        double xDist = x - redDotX;
        double yDist = y - redDotY;
        double radii = radius + dotRadius;
        return xDist*xDist+yDist*yDist < radii*radii;
    }
    
    class Edge {
        int x1;
        int y1;
        int x2;
        int y2;
    }
    
    class Point {
        int x;
        int y;
        Point (int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    class Rectangle {
        double x;
        double y;
        double w;
        double h;
        
        public Rectangle(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
    
    private double clamp(double x, double low, double high) {
        if (x < low) {
            return low;
        }
        else if (x > high) {
            return high;
        }
        else {
            return x;
        }
    }
    
    private boolean checkCircleRectangleCollision
        (double x, double y, double r, Rectangle rect) {
        
        double closestX = clamp(x,rect.x,rect.x+rect.w);
        double closestY = clamp(y,rect.y,rect.y+rect.h);
        
        double xDist = x - closestX;
        double yDist = y - closestY;
        
        double dist = Math.sqrt(xDist*xDist + yDist*yDist);
        return dist < r;
    }
}
