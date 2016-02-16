/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author Kenny
 */
public class Node {
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private int setnum;
    
    public Node(int setnum) {
        this.setnum = setnum;
        this.left = this.right = this.up = this.down = false;
    }

    /**
     * @return the left
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public boolean isRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * @return the up
     */
    public boolean isUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public void setUp(boolean up) {
        this.up = up;
    }

    /**
     * @return the down
     */
    public boolean isDown() {
        return down;
    }

    /**
     * @param down the down to set
     */
    public void setDown(boolean down) {
        this.down = down;
    }

    /**
     * @return the setnum
     */
    public int getSetnum() {
        return setnum;
    }

    /**
     * @param setnum the setnum to set
     */
    public void setSetnum(int setnum) {
        this.setnum = setnum;
    }
}
