package com.example.driver10.MVVM;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "move_table")
public class Move {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int hour;

    private String date;

    private String car;

    private int kmStart;

    private int kmStop;

    @Ignore
    private boolean[] moveTypes;

    private String moveTypesString;

    private String driver;

    private String placeStart;

    private String placeStop;

    private String middlePoint;

    private String comment;

    private double value;

    private double costs;

    @Ignore
    private double kilometersValue;

    @Ignore
    private double typesValue;


    //////////////////////////////////////////////////////////////////////////////////////
    public void setId(int id) {
        this.id = id;
    }

    public Move(){}

    public Move(String car, int hour, String date, int kmStart, int kmStop, boolean[] moveTypes,
                String placeStart, String middlepoint, String placeStop, String driver, String comment, double costs) {
        this.car = car;
        this.hour = hour;
        this.date = date;
        this.kmStart = kmStart;
        this.kmStop = kmStop;
        this.moveTypes = moveTypes;
        this.driver = driver;
        this.placeStart = placeStart;
        this.placeStop = placeStop;
        this.middlePoint = middlepoint;
        this.comment = comment;
        this.costs = costs;
        setValue();
        setMoveTypesString();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
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
        for(int i=0; i<7; i++){
            if(moveTypes[i]) {
                switch (i) {

                    case 0:
                        moveTypesString += "wsparcie ";
                        break;
                    case 1:
                        moveTypesString += "wydanie ";
                        break;
                    case 2:
                        moveTypesString += "odbiór ";
                        break;
                    case 3:
                        moveTypesString += "relokacja/tankowanie ";
                        break;
                    case 4:
                        moveTypesString += "mycie karoserii ";
                        break;
                    case 5:
                        moveTypesString += "odkurzanie ";
                        break;
                    case 6:
                        moveTypesString += "nieudane podstawienie ";
                        break;
                }

            }
        }
    }

    private void setValueOld(){
        boolean a, b, c, d;
        int mKilometers = kmStop - kmStart;

        // a = wybrane tylko wsparcie
        a = moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3]
                && !moveTypes[4] && !moveTypes[5] && !moveTypes[6];
        // b - wybranie tylko mycie
        b = !moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3]
                && moveTypes[4] && !moveTypes[5] && !moveTypes[6];
        // c - wybranie tylko odkurzanie
        c = !moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3]
                && !moveTypes[4] && moveTypes[5] && !moveTypes[6];
        // d - wybranie mycia i odkurzania
        d = !moveTypes[0] && !moveTypes[1] && !moveTypes[2] && !moveTypes[3]
                && moveTypes[4] && moveTypes[5] && !moveTypes[6];

        //podzielenie kilometrów na pół przy powyższych zdarzeniach
        if(a || b || c || d) mKilometers /= 2;
        double mValue;
        if(mKilometers<50) mValue = 5 + 0.2*mKilometers;
        else mValue = 15 + 0.1*(mKilometers-50);

        // jeśli jest mycie + coś to +5
        if(moveTypes[4] && (moveTypes[1] || moveTypes[2] || moveTypes[3] || moveTypes[0]
                || moveTypes[5] || moveTypes[6])) mValue += 5;
        // jeśli jest odkurzanie + coś to +5
        if(moveTypes[5] && (moveTypes[1] || moveTypes[2] || moveTypes[3] || moveTypes[0]
                || moveTypes[4] || moveTypes[6])) mValue += 5;
        value = mValue;


    }

    private void setValue(){
        double mValue = 0.0;
        for(int i=0; i<7; i++){
            if(moveTypes[i]) mValue += 5.0;
            setTypesValue(mValue);
        }
        double kilometers = kmStop - kmStart;
        if(moveTypes[6]) kilometers /= 2;
        if(kilometers<50 && moveTypes[0]) {
            mValue += 0.2*kilometers;
            setKilometersValue(0.2*kilometers);
        }
        if(kilometers<50 && !moveTypes[0]) {
            mValue += 0.6*kilometers;
            setKilometersValue(0.6*kilometers);
        }
        if(kilometers>=50 && moveTypes[0]) {
            mValue += 10 + 0.1*(kilometers-50);
            setKilometersValue(10 + 0.1*(kilometers-50));
        }
        if(kilometers>=50 && !moveTypes[0]) {
            mValue += 30 + 0.1*(kilometers-50);
            setKilometersValue(30 + 0.1*(kilometers-50));
        }

        value = mValue;

    }

    public String getPlaceStart() {
        return placeStart;
    }

    public void setPlaceStart(String placeStart) {
        this.placeStart = placeStart;
    }

    public String getPlaceStop() {
        return placeStop;
    }

    public void setPlaceStop(String placeStop) {
        this.placeStop = placeStop;
    }

    public String getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(String middlePoint) {
        this.middlePoint = middlePoint;
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

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }
    public double getKilometersValue() {
        return kilometersValue;
    }

    public void setKilometersValue(double kilometersValue) {
        this.kilometersValue = kilometersValue;
    }

    public double getTypesValue() {
        return typesValue;
    }

    public void setTypesValue(double typesValue) {
        this.typesValue = typesValue;
    }
}
