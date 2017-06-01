package com.duckbird.core;
import static com.duckbird.core.shared.Utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FileDatabase {
    public String name;
    public String formattedSize;
    public String path;
    public long size;
    private RandomAccessFile file;

    public FileDatabase(String name, String path, long size){
        this.name = name;
        this.size = size;
        this.path = path;
        this.formattedSize = formatFileSize(size);
    }

    public void allocate() {
        try{
            this.connect(this.path+"/"+this.name+".duck");
            this.file.setLength(this.size);
            System.out.println("Db created successfully :)");
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
            System.out.println("No such file or directory to create db.");
        } catch (IOException e) {
            System.out.println("An error ocurred when creating db :(");
        }
    }

    public void connect(String dbName) throws FileNotFoundException {
        this.file = new RandomAccessFile(dbName, "rwd");
    }
}
