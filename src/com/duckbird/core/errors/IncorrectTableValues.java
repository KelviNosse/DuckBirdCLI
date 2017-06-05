package com.duckbird.core.errors;

public class IncorrectTableValues extends Exception{
    public IncorrectTableValues(String table_name, int columns, int columns_provided){
        super("Table "+table_name+" has "+columns+" columns but "+columns_provided+" values were supplied");
    }
}
