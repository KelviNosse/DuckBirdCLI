package com.duckbird.core.errors;

public class InvalidDBFile extends Exception
{
    public InvalidDBFile(String db_file){
        super("The database file "+db_file+" is invalid!");
    }
}
