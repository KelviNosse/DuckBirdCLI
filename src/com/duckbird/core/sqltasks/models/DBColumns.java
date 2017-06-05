package com.duckbird.core.sqltasks.models;

import java.util.ArrayList;
import java.util.List;

public class DBColumns {
    public List<DBColumn> columns;
    public DBColumns(){
        this.columns = new ArrayList<>();
    }
    public void add(String name, ColumnType type){
        DBColumn column = new DBColumn();
        column.name = name;
        column.type = type;
        this.columns.add(column);
    }

    public int size(){
        int size = 0;
        for(int i = 0; i < columns.size(); i++) size += columns.get(i).type.getSize();
        return size;
    }

    public int labelSize(){
        int size = 0;
        for(int i = 0; i < columns.size(); i++) size += columns.get(i).name.length() + 4;
        return size;
    }

    public List<DBColumn> getList(){
        return this.columns;
    }

}
