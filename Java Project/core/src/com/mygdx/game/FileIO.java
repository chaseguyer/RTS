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
    public void patientInfo(String thName, String fName, String lName, float fm, float gs, float ps, float hp, boolean isNewPatient) {
        if(isNewPatient) {
            try{
                // add patient's info to their own file
                writer = new PrintWriter(new FileOutputStream(lName + "_" + fName + ".txt", true));
                writer.append(lName + "," + fName + "," + fm + "," + gs + "," + ps + "," + hp + "\n");
                writer.close();
                
                // add patient to patient list
                writer = new PrintWriter(new FileOutputStream(thName + "-patient-list.txt", true));
                writer.append(lName + "," + fName + "\n");
                writer.close();
            } catch (FileNotFoundException e) {} 
        } else { // load patient
            try {
                Scanner scan = new Scanner(new File(lName + "_" + fName + ".txt"));
                // open patient file
                // dump info into textFields?
                
                
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