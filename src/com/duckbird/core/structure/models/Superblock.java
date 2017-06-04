package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;
import java.io.IOException;
import java.io.RandomAccessFile;

import static com.duckbird.core.shared.Utils.*;

public class Superblock {
    public String name;
    public long disksize, blocksize;
    public int table_count;
    private RandomAccessFile db;
    public int tableLimit;
    public int used_size = 0;
    public int blocks_amount;
    public Superblock(String name, long disksize, long blocksize, int table_count){
        this.name = name;
        this.disksize = disksize;
        this.blocksize = blocksize;
        this.table_count = table_count;
        this.tableLimit = 0;
        this.blocks_amount = (int)Math.ceil(((float)disksize/blocksize));
    }

    public void write() throws IOException {
        RandomAccessFile db = Connection.getDatabase().getFile();
        db.seek(0);
        db.writeInt(MAGIC_NUMBER);
        System.out.println("Name len: "+this.name.length());
        db.writeInt(this.name.length());
        db.writeChars(this.name); char[] name = this.name.toCharArray();
        System.out.println("File pointer: "+db.getFilePointer());
        db.writeLong(this.disksize);
        db.writeLong(this.blocksize);
        db.writeInt(this.used_size);
        db.writeInt(REGISTERS);
        db.writeInt(this.table_count);
        db.writeInt(this.tableLimit);
        db.getFD().sync();
    }


    public int getAvailableSize(){
        return (int) (float)(this.disksize - this.used_size);
    }
}
