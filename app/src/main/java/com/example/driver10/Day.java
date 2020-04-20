package com.example.driver10;

public class Day {
    private String date;
    private String startHour;
    private double hours;
    private int moves;
    private double movesValue;

    public Day(String date, String startHour, double hours, int moves, double movesValue){
        this.date = date;
        this.startHour = startHour;
        this.hours = hours;
        this.moves = moves;
        this.movesValue = movesValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public double getMovesValue() {
        return movesValue;
    }

    public void setMovesValue(double movesValue) {
        this.movesValue = movesValue;
    }
}
