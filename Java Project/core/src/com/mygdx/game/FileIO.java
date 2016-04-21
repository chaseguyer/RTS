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
    
}