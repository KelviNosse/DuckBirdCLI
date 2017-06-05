package com.duckbird.core.shared;

import com.duckbird.core.FileDatabase;
import com.duckbird.core.errors.InvalidDBFile;
import com.duckbird.core.structure.models.Bitmap;
import com.duckbird.core.structure.models.Blocks;
import com.duckbird.core.structure.models.DirTable;
import com.duckbird.core.structure.models.Superblock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Connection {
    private static Connection instance = new Connection();
    public FileDatabase database;
    public Bitmap bitmap;
    public Superblock sb;
    public DirTable dirTable;
    public Blocks blocks;
    public static FileDatabase getDatabase() {
        return instance.database;
    }
    public static Bitmap getBitmap(){ return instance.bitmap; }
    public static Superblock getSuperblock(){ return instance.sb; }
    public static DirTable getDirTable(){ return instance.dirTable; }
    public static Blocks getBlocks(){ return instance.blocks; }
    public static void setDatabase(FileDatabase db){
        instance.database = db;
    }
    public static void setBitmap(Bitmap bm){
        instance.bitmap = bm;
    }
    public static void setSuperblock(Superblock sb){
        instance.sb = sb;
    }
    public static void setDirTable(DirTable dir){
        instance.dirTable = dir;
    }
    public static void setBlocks(Blocks blocks){
        instance.blocks = blocks;
    }
    public static boolean isConnected(){
        return (getDatabase() != null) && (getBitmap() != null) && (getDirTable() != null);
    }
    public static void reload() throws IOException, InvalidDBFile {
        IOStreamDB db = new IOStreamDB(new File(Utils.getInstance().file_path));
        db.load();
    }
    private Connection() {}
}
