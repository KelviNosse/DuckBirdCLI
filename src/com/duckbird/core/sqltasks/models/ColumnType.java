package com.duckbird.core.sqltasks.models;

//supported types
public enum ColumnType {
    INT(4),
    CHAR(2),
    CHAR_ARRAY(4000),
    DOUBLE(8);

    private int size;
    ColumnType(int value) {
        this.size = value;
    }
    public int getSize(){
        return this.size;
    }
}
