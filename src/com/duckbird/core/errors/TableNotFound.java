package com.duckbird.core.errors;

public class TableNotFound extends Exception{
    public TableNotFound(String table_name){
        super("The provided table "+table_name+" was not found!");
    }
}
