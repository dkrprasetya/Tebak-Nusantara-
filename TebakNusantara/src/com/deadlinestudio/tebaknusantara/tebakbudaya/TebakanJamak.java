/*
 * TebakanJamak.java
 *
 * Created on August 1, 2013, 8:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.deadlinestudio.tebaknusantara.tebakbudaya;

public class TebakanJamak {
   private int id;
   private String desc;
   private String choice1;
   private String choice2;
   private String choice3;
   private String choice4;
   private int correct;
   private String about;
   private boolean answered;
   
    /** Creates a new instance of TebakanJamak */
    public TebakanJamak() {
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getDesc(){
        return desc;
    }
    
    public void setDesc(String desc){
        this.desc = desc;
    }
    
    public String getChoice1(){
        return choice1;
    }
    
    public void setChoice1(String choice1){
        this.choice1 = choice1; 
    }
    
    public String getChoice2(){
        return choice2;
    }
    
    public void setChoice2(String choice2){
        this.choice2 = choice2; 
    }
    public String getChoice3(){
        return choice3;
    }
    
    public void setChoice3(String choice3){
        this.choice3 = choice3; 
    }
    public String getChoice4(){
        return choice4;
    }
    
    public void setChoice4(String choice4){
        this.choice4 = choice4; 
    }
    
    public String getChoice(int index){
        switch (index){
            case 0 : return choice1;
            case 1 : return choice2;
            case 2 : return choice3;
            case 3 : return choice4;
        }
        return "";
    }
    
    public int getCorrect(){
        return correct;
    }
    
    public void setCorrect(int correct){
        this.correct = correct;
    }
    
    public boolean getAnswered(){
        return answered;
    }
    
    public void setAnswered(boolean answered){
        this.answered = answered;
    }
    
    public String getAbout(){
        return about;
    }
    
    public void setAbout(String about){
        this.about = about;
    }
}
