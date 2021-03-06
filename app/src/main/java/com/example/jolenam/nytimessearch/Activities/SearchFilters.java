package com.example.jolenam.nytimessearch.Activities;

import java.io.Serializable;

/**
 * Created by jolenam on 6/24/16.
 */
public class SearchFilters implements Serializable {
    String month;
    String day;
    String year;

    String sortType;
    boolean checkedArts;
    boolean checkedFashion;
    boolean checkedSports;

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCheckedArts(boolean checkedArts) {
        this.checkedArts = checkedArts;
    }

    public void setCheckedFashion(boolean checkedFashion) {
        this.checkedFashion = checkedFashion;
    }

    public void setCheckedSports(boolean checkedSports) {
        this.checkedSports = checkedSports;
    }


    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getYear() {
        return year;
    }


    public boolean isCheckedSports() {
        return checkedSports;
    }

    public String getSortType() {
        return sortType;
    }

    public boolean isCheckedArts() {
        return checkedArts;
    }

    public boolean isCheckedFashion() {
        return checkedFashion;
    }

    public SearchFilters() {
    }

    /*public SearchFilters(String sortType) {
        this.sortType = sortType;
    }
    public SearchFilters(String month, String day, String year, String sortType, boolean checkedArts, boolean checkedFashion, boolean checkedSports) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.sortType = sortType;
        this.checkedArts = checkedArts;
        this.checkedFashion = checkedFashion;
        this.checkedSports = checkedSports;
    }*/
}

