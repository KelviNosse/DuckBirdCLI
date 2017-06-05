package com.duckbird.core.errors;

public class ConnectionNotEstablished extends Exception{
    public ConnectionNotEstablished(){
        super("You are currently not connected to a db.\nPlease connect first!");
    }
}
