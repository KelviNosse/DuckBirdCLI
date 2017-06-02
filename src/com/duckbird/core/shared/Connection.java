package com.duckbird.core.shared;

import com.duckbird.core.FileDatabase;

public class Connection {
    private static Connection instance = new Connection();
    public FileDatabase database;
    public static FileDatabase getDatabase() {
        return instance.database;
    }
    public static void setDatabase(FileDatabase db){
        instance.database = db;
    }
    private Connection() {}
}
