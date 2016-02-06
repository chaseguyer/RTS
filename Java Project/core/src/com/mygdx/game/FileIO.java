/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.io.*;
import java.util.*;

/**
 *
 * @author chaseguyer
 */
public class FileIO {

    public PrintWriter writer = null;
    
    
    
    
    ArrayList<String> unList = new ArrayList<String>();
    ArrayList<String> pwList = new ArrayList<String>();
    
    ArrayList<String> patientList = new ArrayList<String>();
    
    // just to be clear, i have no idea what i'm doing...
    public boolean isValidLogin(String username, String password) {
        try{
            Scanner scan = new Scanner(new File("rts-login.txt")).useDelimiter("\n");
            Scanner small = new Scanner("");
            
            // scan in the entries from the file and add them to the list of users
            while(scan.hasNextLine()) {
                small = new Scanner(scan.nextLine()).useDelimiter(","); // ...AND IT SHOWS :D 
                unList.add(small.next());
                pwList.add(small.next());
            }
            scan.close();
        } catch(FileNotFoundException e) {}
        
        // check if the un and pw we've been handed matches any one of these
        for(int i = 0; i < unList.size(); i++) {
            if(unList.get(i).equals(username) && pwList.get(i).equals(password)) {
                return true;
            }
        }        
        return false;
    }
    
    public void newTherapist(String username, String password) {
        try{
            writer = new PrintWriter(new FileOutputStream("rts-login.txt", true));
            writer.append(username + "," + password + "\n");
            writer.close();
        } catch (FileNotFoundException e) {}
    }
    
    public void loadPatients() {
        
    }
            
}

/*

SAVE
PrintWriter writer=null;
String mapName=map.getMapName();
map.changeMap(spawnedMonsters, items, null);

try {
    writer = new PrintWriter("player.txt", "UTF-8");
} catch (FileNotFoundException ex) {
    Logger.getLogger(apoc.class.getName()).log(Level.SEVERE, null, ex);
} catch (UnsupportedEncodingException ex) {
   Logger.getLogger(apoc.class.getName()).log(Level.SEVERE, null, ex);
}
    if(player.activeItem.isRanged())
    writer.println(player.activeItem.getID() + " "+ player.activeItem.getRemainingRounds()+" "+player.activeItem.getAmmoType()+" "+player.totalRounds(player.activeItem.getAmmoType()));
writer.close()l

LOAD
Scanner scan;
        if(nameT==null)
            scan= new Scanner(new File("last.txt"));
        else
            scan= new Scanner(new File(name));
        ArrayList<Monster> monsters=new ArrayList<Monster>();
         
        String line="";
        while(scan.hasNextLine() && line!= null && !line.equalsIgnoreCase("!M"))
            line=scan.nextLine();
        if(line==null || !scan.hasNextLine()) return monsters;
        line=scan.next();
        while(!line.equalsIgnoreCase("!I"))
        {
            Monster m=getMonster(line);      
            float a=scan.nextFloat();
scan.close()l


Scanner scan;
        if(nameT==null)
            scan= new Scanner(new File("last.txt"));
        else
            scan= new Scanner(new File(name));
        ArrayList<Monster> monsters=new ArrayList<Monster>();
         
        String line="";
        while(scan.hasNextLine() && line!= null && !line.equalsIgnoreCase("!M"))
            line=scan.nextLine();
        if(line==null || !scan.hasNextLine()) return monsters;
        line=scan.next();
        while(!line.equalsIgnoreCase("!I"))
        {
            Monster m=getMonster(line);      
            float a=scan.nextFloat();
scan.close()l



*/