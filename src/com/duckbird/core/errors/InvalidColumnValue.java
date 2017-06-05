package com.duckbird.core.errors;

public class InvalidColumnValue extends Exception{
    public InvalidColumnValue(String column_name){
        super("The provided column "+column_name+" is invalid!");
    }
}
