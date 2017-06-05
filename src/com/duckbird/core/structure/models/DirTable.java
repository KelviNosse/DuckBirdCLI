package com.duckbird.core.structure.models;

import com.duckbird.core.shared.Connection;
import com.duckbird.core.shared.Utils;

import java.io.*;

public class DirTable {
    private TableEntries[] tableEntries;
    public int offset;
    public DirTable(){
        this.tableEntries = new TableEntries[this.Length()];
        this.init();
    }

    public DirTable(TableEntries[] tableEntries){
        this.tableEntries = tableEntries;
    }

    private void init(){
        for(int i = 0; i < this.Length(); i++)
            this.tableEntries[i] = new TableEntries();
    }

    public void addTableEntry(String table_name, int offset, TableMetadata metadata){
        int len = table_name.length();
        for(int i = 0; i < 25 - len; i++) table_name += ' ';
        for(int i = 0; i < this.tableEntries.length; i++){
            if(Utils.isCharArrayEmpty(tableEntries[i].table_name)){
                this.tableEntries[i].table_name = table_name.toCharArray();
                this.tableEntries[i].block_offset = offset;
                this.tableEntries[i].TableMetadata = metadata;
                break;
            }
        }
    }

    public int Length(){
        int blocksize = (int)(float) Connection.getSuperblock().blocksize;
        float len = ((float)blocksize/(new TableMetadata()).Size());
        return (int)Math.ceil(len);
    }

    public int Size(){
        return (new TableEntries()).Size() * this.Length();
    }

    public void writeOnDisk() throws IOException {
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        System.out.println("Writing dir table on offset: "+this.offset);
        db_file.seek(this.offset);
        for(int i = 0; i < this.tableEntries.length; i++){
            db_file.writeChars(new String(this.tableEntries[i].table_name));
            db_file.writeInt(this.tableEntries[i].block_offset);
            db_file.writeInt(this.tableEntries[i].TableMetadata.table_size);
            db_file.writeInt(this.tableEntries[i].TableMetadata.registers);
            db_file.writeInt(this.tableEntries[i].TableMetadata.columns_count);
        }
        System.out.println("Finishing writing dir table at: "+db_file.getFilePointer());
        db_file.getFD().sync();
    }

    public boolean entryExists(String table_name){
        for(int i = 0; i < this.Length(); i++){
            String entryTableName = new String(this.tableEntries[i].table_name).trim().replaceAll(" ", "");
            if(table_name.equals(entryTableName)){
                return true;
            }
        }
        return false;
    }

    public TableEntries getEntry(String table_name){
        for(int i = 0; i < this.Length(); i++){
            String entryTableName = new String(this.tableEntries[i].table_name).trim().replaceAll(" ", "");
            if(table_name.equals(entryTableName)){
                return this.tableEntries[i];
            }
        }
        return null;
    }
}
