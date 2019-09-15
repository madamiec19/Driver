package com.example.driver10;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "move_table")
public class Move {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String hour;

    private String date;

    private String car;

    private int kmStart;

    private int kmStop;

    @Ignore
    private boolean[] moveTypes;

    private String moveTypesString;

    private String driver;

    private String route;

    private String comment;

    private double value;

    public void setId(int id) {
        this.id = id;
    }

    public Move(){}

    public Move(String car, String hour, String date, int kmStart, int kmStop, boolean[] moveTypes, String driver, String route, String comment) {
        this.car = car;
        this.hour = hour;
        this.date = date;
        this.kmStart = kmStart;
        this.kmStop = kmStop;
        this.moveTypes = moveTypes;
        this.driver = driver;
        this.route = route;
        this.comment = comment;
        setValue();
        setMoveTypesString();
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private void setMoveTypesString(){
        moveTypesString = "";
        for(int i=0; i<10; i++){
            if(moveTypes[i]) {
                switch (i) {

                    case 0:
                        moveTypesString += "podstawienie do wynajmu (w tym do podmiany) [UW], ";
                        break;

                    case 1:
                        moveTypesString += "odbiór z wynajmu (w tym z podmiany) [UW], ";
                        break;
                    case 2:
                        moveTypesString += "relokacja między biurami/parkingami[RL], ";
                        break;
                    case 3:
                        moveTypesString += "wsparcie przy podstawieniu/odbiorze innego auta[RL], ";
                        break;
                    case 4:
                        moveTypesString += "podstawienie na serwis[RL], ";
                        break;
                    case 5:
                        moveTypesString += "odbiór po serwisie[RL], ";
                        break;
                    case 6:
                        moveTypesString += "mycie karoserii[M], ";
                        break;
                    case 7:
                        moveTypesString += "odkurzanie[M], ";
                        break;
                    case 8:
                        moveTypesString += "nieudane podstawienie[RL], ";
                        break;
                    case 9:
                        moveTypesString += "zakupy firmowe i inne[RL]";
                        break;
                }
            }
        }
    }

    private void setValue(){
        int kilometers = kmStop - kmStart;
        if((!moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[4] && !moveTypes[5]) || (moveTypes[4] && moveTypes[5]))   kilometers /=2;
        if(kilometers>=50) value = 15 + 0.1*(kilometers-50);
        else value = 5+kilometers*0.2;

        if(moveTypes[6])
            if(!(moveTypes[6] && !moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3] && !moveTypes[4] && !moveTypes[5] && !moveTypes[7]
                    && !moveTypes[8] && !moveTypes[9] ))
                value +=5.0;

        if(moveTypes[7])
            if(!(moveTypes[7] && !moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3] && !moveTypes[4] && !moveTypes[5] && !moveTypes[6]
                    && !moveTypes[8] && !moveTypes[9] ))
                value +=5.0;


    }

    public double getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public String getCar() {
        return car;
    }

    public int getKmStart() {
        return kmStart;
    }

    public int getKmStop() {
        return kmStop;
    }

    public String getMoveTypesString() {
        return moveTypesString;
    }

    public String getDriver() {
        return driver;
    }

    public String getRoute() {
        return route;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setKmStart(int kmStart) {
        this.kmStart = kmStart;
    }

    public void setKmStop(int kmStop) {
        this.kmStop = kmStop;
    }

    public void setMoveTypesString(String moveTypesString) {
        this.moveTypesString = moveTypesString;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean[] getMoveTypes(){return moveTypes;}
}
