package com.duckbird.core.errors;

public class InvalidColumnType extends Exception{
    public InvalidColumnType(String col_type){
        super("The column type "+col_type+ " is not supported!");
    }

    public InvalidColumnType(){

    }
}
