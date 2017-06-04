package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.sqltasks.models.DBTable;

public class TableCreator {
    private DBTable table;
    public TableCreator(DBTable table){
        this.table = table;
    }

    public void create(){
        //todo create table on disk with specified table data

    }
}
