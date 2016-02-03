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
    
    FileInputStream in = null;
    FileOutputStream out = null;
    
    public void openDB() {
        try {
            // This will HAVE to change
            File db = new File("/Users/chaseguyer/Documents/RTS/Documents/dbFile");
            
            if(db.createNewFile()) {
                System.out.println("New db file was created.");
            } else {
                System.out.println("There already exists a databse.");
            }
            
            // Regardless of whether or not file exists, we want it in the input stream
            in = new FileInputStream("/Users/chaseguyer/Documents/RTS/Documents/dbFile.txt");
            
        } catch(IOException e) {
            //System.out.println("Could not find the file");
        }        
    
        // add therpist username and pw
        LinkedList usernameList = new LinkedList(); // maybe combine these (java version of c++ pair?)
        LinkedList passwordList = new LinkedList();
        
    
    }
    
    public void checkTherapistCreds(String username, String password) {
       
    
        loadPatients();
    }
    
    public void loadPatients() {
        LinkedList patientList = new LinkedList();
        
        // find therapist, skip his password, scan all in based off of \n until whitespace found
            // (or something to that effect)
        
    }
    
    public void addTherapist(String name, String pw) {
        
        
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



*/