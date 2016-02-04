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
    
    public LinkedList usernameList = new LinkedList(); // maybe combine these (java version of c++ pair?)
    public LinkedList passwordList = new LinkedList();
    
    public void checkTherapistCreds(String username, String password) {
        // add therpist username and pw
        
        
        
        
        //Scanner scan;
        
        // what if there are no therapists?
        //scan = new Scanner("rts-db.txt");
        
        /*
        String line = "", name, pw;
        while(scan.hasNext() && line!= null) {
            name = scan.next();
            pw = scan.next();
           
            line = scan.nextLine();
            
            usernameList.add(name);
            passwordList.add(pw);
        }
        */
        //scan.close();

        //loadPatients();
        
        
        while(usernameList != null) {
            
            
        }
        
        
        
        
    }
    
    public void newTherapist(String username, String password) {
        try{
            writer = new PrintWriter("rts-db.txt", "UTF-8");
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e) {  
        }
        
        writer.println(username + " " + password);
        
        // this is only temporary
        usernameList.add(username);
        passwordList.add(password);
        
        
        writer.close();
    }
    
    public void loadPatients() {
        LinkedList patientList = new LinkedList();
        
        // find therapist, skip his password, scan all in based off of \n until whitespace found
            // (or something to that effect)
        
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