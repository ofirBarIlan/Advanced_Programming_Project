package model;

import java.util.Observable;

public class Model_Stub extends Observable {
    int row, col, score;
    String word, direction;

    
    boolean isValid =true;    //added new - false for checking


    public Model_Stub(){

    }

    public void setRow(int r){
        this.row = r;
        System.out.println("New row is " + row);
    }
    public void setDirection(String newval) {
        this.direction = newval;
        if(direction!="")
        System.out.println("New direction is " + direction);
    }
    public void setCol(int newval) {
        this.col = newval;
        System.out.println("New col is " + col);
    }
    public void setScore(int newval) {
        this.score = newval;
    }
    public void setIsValid(boolean newval) {
        this.isValid = newval;
    }
    public void setWord(String newval) {
        this.word = newval;
        if(word!="")
        System.out.println("New word is " + word);
    }

    public void calcScore(){    //added new
        if(isValid) {
            this.setIsValid(true);
            // update score from server
            this.setScore(2);
            System.out.println("New score is " + this.score);
            setChanged();
            notifyObservers();
        }
        else{
            this.setIsValid(false);
            setChanged();
            notifyObservers();
        }

    }

    public int getScore(){       //added new
        return this.score;        
    }

    public Boolean getIsValid() {
        return this.isValid;
    }    
}