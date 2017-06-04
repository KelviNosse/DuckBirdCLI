package com.duckbird.core.sqltasks.models;

public class DBTable {
    public String name;
    public DBColumns columns;
    public DBTable(String name, DBColumns columns){
        this.name = name;
        this.columns = columns;
    }
}
