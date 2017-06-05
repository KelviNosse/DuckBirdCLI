package com.duckbird.core.errors;

public class NoSuchFreeSpace extends Exception{
    public NoSuchFreeSpace(){
        super("There's no such space to allocate this");
    }
}
