package com.duckbird.core.structure;


import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.models.*;

import java.io.IOException;

public class DBStructure {
    private long disksize;
    private int blocksize;
    private String name;
    public int num_blocks;
    public int tableLimit;
    private int table_count;
    public DBStructure(){
        this.name = Connection.getDatabase().name;
        this.disksize = Connection.getDatabase().size;
        this.blocksize = (int)(long)Connection.getDatabase().blocksize;
        this.num_blocks = (int) (this.disksize/this.blocksize);
        this.table_count = 0;
    }

    public void create(){
        Connection.setSuperblock(new Superblock(this.name, this.disksize, this.blocksize, this.table_count));
        Connection.setBitmap(new Bitmap(this.num_blocks));
        Connection.setDirTable(new DirTable());
        Connection.setBlocks(new Blocks());
        this.initProtocol();
        try {
            Connection.getSuperblock().write();
            Connection.getBitmap().writeOnDisk();
            Connection.getDirTable().writeOnDisk();
            Connection.getBlocks().allocate();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void initProtocol(){
        float bytes_size = ((float)this.num_blocks/8);
        int bmapBlocks = (int)Math.ceil((bytes_size /Connection.getDatabase().blocksize));
        int dirTableBlocks = (int)Math.ceil(((float)Connection.getDirTable().Size()/this.blocksize));
        Connection.getBitmap().toggle(0); //superblock toggled
        for(int i = 0; i < bmapBlocks; i++) Connection.getBitmap().toggle(i+1); //bitmap blocks toggled
        for(int i = 0; i < dirTableBlocks; i++) Connection.getBitmap().toggle(i+1+bmapBlocks); //dirtable blocks toggled
        Connection.getSuperblock().used_size = (bmapBlocks*this.blocksize) + (dirTableBlocks*this.blocksize) + this.blocksize;
        Connection.getBitmap().offset = this.blocksize;
        Connection.getDirTable().offset = this.blocksize + (bmapBlocks*this.blocksize);
        Connection.getBlocks().offset = this.blocksize + (bmapBlocks*this.blocksize) + (dirTableBlocks*this.blocksize);
        Connection.getBlocks().blocksUsed = 1 + bmapBlocks + dirTableBlocks;
        System.out.println("Expected bitmap size: "+(bmapBlocks*this.blocksize));
    }
}
