/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    
    // Creates a new therapist
    public void newTherapist(String username, String password) {
        try{
            writer = new PrintWriter(new FileOutputStream("rts-login.txt", true));
            writer.append(username + "," + password + "\n");
            writer.close();
        } catch (FileNotFoundException e) {}
    }
    
    // This function creates/edits the patient information section
    public void patientInfo(
            String thName, String fName, String lName, // therapist name, patient first name, patient last name
            boolean lArm, boolean rArm, boolean bArm,  // boolean values for most involved arm
            float fm, float gs, float ps, float hp,    // float values for the various tests
            boolean isNewPatient) {                    // indicates if this is a new patient or editing a an existing patient
        
        if(isNewPatient) {
            try{
                // add patient's info to their own file
                if(!MainMenu.isLoadedPatient) {
                    writer = new PrintWriter(new FileOutputStream(lName + "_" + fName + ".txt", true));
                    writer.append(lName + "," + fName + "," + lArm + "," + rArm + "," + bArm + "," + fm + "," + gs + "," + ps + "," + hp + "\n");
                } else {    
                    writer = new PrintWriter(new FileOutputStream(lName + "_" + fName + ".txt", false));
                    writer.write(lName + "," + fName + "," + lArm + "," + rArm + "," + bArm + "," + fm + "," + gs + "," + ps + "," + hp + "\n");
                }
                writer.close();
                
                // add patient to patient list
                writer = new PrintWriter(new FileOutputStream(thName + "-patient-list.txt", true));
                writer.append(lName + "," + fName + "\n");
                writer.close();
            } catch (FileNotFoundException e) {} 
        } else if(!isNewPatient) { // load patient
            try {
                Scanner scan = new Scanner(new File(lName + "_" + fName + ".txt")).useDelimiter("\n");
                Scanner small;
                
                while(scan.hasNextLine()) {
                    small = new Scanner(scan.nextLine()).useDelimiter(",");
                    MainMenu.pLast = small.next();
                    MainMenu.pFirst = small.next();
                    MainMenu.lArmBool = Boolean.valueOf(small.next());
                    MainMenu.rArmBool = Boolean.valueOf(small.next());
                    MainMenu.bArmBool = Boolean.valueOf(small.next());
                    MainMenu.fm = Float.valueOf(small.next());
                    MainMenu.gs = Float.valueOf(small.next());
                    MainMenu.ps = Float.valueOf(small.next());
                    MainMenu.hp = Float.valueOf(small.next());
                }
            } catch(FileNotFoundException e) {}
        }        
    }
    
    public boolean isPatient(String therapistName, String pFirst, String pLast) {
        ArrayList<String> pList = new ArrayList<String>();
        ArrayList<String> patientFirst = new ArrayList<String>();
        ArrayList<String> patientLast = new ArrayList<String>();   
        
        try{
            Scanner scan = new Scanner(new File(therapistName + "-patient-list.txt")).useDelimiter("\n");
            Scanner small;
            
            while(scan.hasNextLine()) {
                small = new Scanner(scan.nextLine()).useDelimiter(","); 
                patientLast.add(small.next());
                patientFirst.add(small.next());
            }
            scan.close();
        } catch(FileNotFoundException e) {}
        
        // check for first and last names in the fields
        for(String last : patientLast) {
            if(last.equals(pLast)) {
                for(String first : patientFirst) {
                    if(first.equals(pFirst)) {
                        return true;
                    }                
                }                
            }               
        }
        return false;
    }
    
    public void addRoutine(String first, String last, String routineName, String routine) {
        try {
            // add patient to patient list
            writer = new PrintWriter(new FileOutputStream(first + "_" + last + "_" + routineName + ".txt", true));
            
            File dir = new File(first + last + "/Data/" + routineName);
            dir.mkdirs();
            
            if(routine.equals("ISPY")) {
                writer.append("ISPY\n");
                writer.close();

                String file = first + last + "/Data/" + routineName + "/ISpyGameInfo.txt";
                File f = new File(file);
                writer = new PrintWriter(new FileOutputStream(f, true));
                writer.append(MainMenu.area1.getText() + " " + 
                                MainMenu.area2.getText() + " " + 
                                MainMenu.area3.getText() + " " + 
                                MainMenu.area4.getText() + " " + 
                                MainMenu.area5.getText() + " " + 
                                MainMenu.area6.getText() + " " + 
                                MainMenu.area7.getText() + " " + 
                                MainMenu.area8.getText() + " " + 
                                MainMenu.area9.getText() + " " + 
                                MainMenu.iSpyRoundsTillStats.getText() + " " +
                                MainMenu.iSpyStripedBox.getText() + " "
                );
                writer.close();
            }
            
            if (routine.equals("MEMORY")) {
                writer.append("MEMORY\n");
                writer.close();

                int difficulty = 0;
                if(MainMenu.easy.isChecked()) difficulty = 0;
                if(MainMenu.med.isChecked()) difficulty = 1;
                if(MainMenu.hard.isChecked()) difficulty = 2;
                
                String file = first + last + "/Data/" + routineName + "/MemoryGameInfo.txt";
                File f = new File(file);
                writer = new PrintWriter(new FileOutputStream(f, true));
                writer.append(MainMenu.leftOrien.getText() + " " +
                                MainMenu.dispPercent.getText() + " " +
                                MainMenu.cardPairs.getText() + " " +
                                MainMenu.memRoundsTillStats.getText() + " " +
                                MainMenu.cardReveal.getText() + " " +
                                difficulty + " " +
                                MainMenu.memStripedBox.getText() + " "
                );      
                writer.close();
            }
            
            
            
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        
    }
    
    public void runRoutine(String first, String last, String routineName) {
        ArrayList<String> routines = new ArrayList<String>(); 
        
        File f = new File(first + "_" + last + "_" + routineName + ".txt");
        
        try {
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()) {
                String s = scan.next();
                routines.add(s);
            }
            scan.close();
        } catch (FileNotFoundException e) {}
        
        for(String name : routines) {
            if(name.equals("ISPY")) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new I_Spy(first, last, routineName));
            }
            else if(name.equals("MEMORY")) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame(first, last, routineName));                
            }
        }                
    }    
}