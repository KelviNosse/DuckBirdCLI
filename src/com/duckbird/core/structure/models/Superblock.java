package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;
import java.io.IOException;
import java.io.RandomAccessFile;

import static com.duckbird.core.shared.Utils.*;

public class Superblock {
    private String name;
    private long disksize, blocksize;
    private int table_count;
    private RandomAccessFile db;
    private int tableLimit;
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
        RandomAccessFile db = Connection.getDatabase().file;
        db.seek(0);
        db.writeInt(MAGIC_NUMBER);
        db.writeChars(this.name);
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
