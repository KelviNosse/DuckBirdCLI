package com.duckbird.core;
import com.duckbird.core.errors.InvalidBlockSize;
import com.duckbird.core.errors.InvalidDiskSize;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.DBStructure;

import static com.duckbird.core.shared.Utils.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FileDatabase {
    public String name;
    public String formattedSize;
    public String path;
    public long size, blocksize;
    public RandomAccessFile file;
    private DBStructure dbStructure;
    public FileDatabase(String name, String path, long size, int blocksize) throws InvalidBlockSize, InvalidDiskSize {
        this.name = name;
        this.size = checkValidSize(size);
        this.path = path;
        this.blocksize = checkValidBlockSize(blocksize, size);
        this.formattedSize = formatFileSize(size);
    }

    public void allocate() {
        try{
            this.connect(this.path+"/"+this.name+".duck");
            this.file.setLength(this.size);
            Connection.setDatabase(this);
            this.dbStructure = new DBStructure();
            this.dbStructure.create();
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
