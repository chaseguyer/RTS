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
            Scanner scan = new Scanner(new File("RTS Data/therapists/rts-login.txt")).useDelimiter("\n");
            Scanner small;
            
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
            
            // make this therapists directory
            File f = new File("RTS Data/therapists/" + username);
            f.mkdir();
            
            // add therapist to rts-login file
            writer = new PrintWriter(new FileOutputStream("RTS Data/therapists/rts-login.txt", true));
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
                // create the patient's directory
                File f = new File("RTS Data/patients/" + fName + "_" + lName);
                f.mkdirs();
                
                // add patient's info to their own file
                if(!MainMenu.isLoadedPatient) {
                    writer = new PrintWriter(new FileOutputStream("RTS Data/patients/" + fName + "_" + lName + "/" + fName + "_" + lName + "_info" + ".txt", true));
                    writer.append(lName + "," + fName + "," + lArm + "," + rArm + "," + bArm + "," + fm + "," + gs + "," + ps + "," + hp + "\n");
                } else {    
                    writer = new PrintWriter(new FileOutputStream("RTS Data/patients/" + fName + "_" + lName + "/" + fName + "_" + lName + "_info" + ".txt", false));
                    writer.write(lName + "," + fName + "," + lArm + "," + rArm + "," + bArm + "," + fm + "," + gs + "," + ps + "," + hp + "\n");
                }
                writer.close();
                
                // add patient to patient list
                writer = new PrintWriter(new FileOutputStream("RTS Data/therapists/" + thName + "/" + thName + "-patient-list.txt", true));
                writer.append(fName + "," + lName + "\n");
                writer.close();
            } catch (FileNotFoundException e) {} 
        } else if(!isNewPatient) { // load patient
            try {
                Scanner scan = new Scanner(new File("RTS Data/patients/" + fName + "_" + lName + "/" + fName + "_" + lName + "_info" + ".txt")).useDelimiter("\n");
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
    
    // this function checks  whether or not a patient exists
    public boolean isPatient(String therapistName, String pFirst, String pLast) {
        ArrayList<String> pList = new ArrayList<String>();
        ArrayList<String> patientFirst = new ArrayList<String>();
        ArrayList<String> patientLast = new ArrayList<String>();   
        
        try{
            Scanner scan = new Scanner(new File("RTS Data/therapists/" + therapistName + "/" + therapistName + "-patient-list.txt")).useDelimiter("\n");
            Scanner small;
            
            while(scan.hasNextLine()) {
                small = new Scanner(scan.nextLine()).useDelimiter(","); 
                patientFirst.add(small.next());
                patientLast.add(small.next());
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
    
    // this function creates the routine folder 
    public void addRoutineFolder(String first, String last, String routineName) { 
        // create the routine folder
        File dir = new File("RTS Data/patients/" + first + "_" + last + "/" + routineName);
        dir.mkdirs();        
    }
    
    // this function creates the routine subfolder information
    public void addRoutine(String first, String last, String routineName, String routine) {
        try {
            // add routine
            writer = new PrintWriter(new FileOutputStream("RTS Data/patients/" + first + "_" + last + "/" + routineName + "/" + first + "_" + last + "_" + routineName + ".txt", true));
            
            if(routine.equals("ISPY")) {
                writer.append("ISPY\n");
                writer.close();

                String file = "RTS Data/patients/" + first + "_" + last + "/" + routineName + "/ISpyGameInfo.txt";
                File f = new File(file);
                writer = new PrintWriter(new FileOutputStream(f, false)); // false, therefore DO NOT APPEND, overwrite
                writer.append(MainMenu.area1.isChecked() + " " + 
                                MainMenu.area2.isChecked() + " " + 
                                MainMenu.area3.isChecked() + " " + 
                                MainMenu.area4.isChecked() + " " + 
                                MainMenu.area5.isChecked() + " " + 
                                MainMenu.area6.isChecked() + " " + 
                                MainMenu.area7.isChecked() + " " + 
                                MainMenu.area8.isChecked() + " " + 
                                MainMenu.area9.isChecked() + " " + 
                                MainMenu.iSpyRoundsTillStats.getText() + " " +
                                MainMenu.iSpyReshuffleBox.isChecked() + " " +
                                MainMenu.iSpyStripedBox.isChecked() + " " +
                                MainMenu.iSpyRepetitions.getText()
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
                
                boolean orientation = false;
                if(MainMenu.leftOrien.isChecked()) orientation = true;
                
                String file = "RTS Data/patients/" + first + "_" + last + "/" + routineName + "/MemoryGameInfo.txt";
                File f = new File(file);
                writer = new PrintWriter(new FileOutputStream(f, false));
                writer.append(orientation + " " +
                                MainMenu.dispPercent.getText() + " " +
                                MainMenu.cardPairs.getText() + " " +
                                MainMenu.memRoundsTillStats.getText() + " " +
                                MainMenu.cardReveal.getText() + " " +
                                difficulty + " " +
                                MainMenu.memStripedBox.isChecked() + " " + 
                                MainMenu.memRepetitions.getText()
                );      
                writer.close();
            }
            
            
            
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        
    }
    
    public void runRoutine(String first, String last, String routineName) {
        ArrayList<String> routines = new ArrayList<String>(); 
        
        File f = new File("RTS Data/patients/" + first + "_" + last + "/" + routineName + "/" + first + "_" + last + "_" + routineName + ".txt");
        
        try {
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()) {
                String s = scan.nextLine();
                routines.add(s);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        
        MainMenu.stage.clear();
        RTS.menu.hide();
        
        for(String name : routines) {
            if(name.equals("ISPY")) {
                
                
                ((Game) Gdx.app.getApplicationListener()).setScreen(new I_Spy(first, last, routineName));
                if(!MainMenu.continueRoutine) break;
            }
            else if(name.equals("MEMORY")) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MemoryGame(first, last, routineName));                
                if(!MainMenu.continueRoutine) break;
            }            
        }        
        
        MainMenu.stage.addActor(MainMenu.patientMenuTitleTable);
        RTS.menu.show();
    }    
}