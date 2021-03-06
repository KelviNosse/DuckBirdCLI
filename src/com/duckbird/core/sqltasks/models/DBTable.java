package com.duckbird.core.sqltasks.models;

import com.duckbird.core.errors.InvalidDBFile;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.models.TableMetadata;
import dnl.utils.text.table.TextTable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

public class DBTable {
    public String name;
    public DBColumns columns;
    private RandomAccessFile file;
    private Object[][] rows;
    public DBTable(String name, DBColumns columns){
        this.name = name;
        this.columns = columns;
        this.file = Connection.getDatabase().getFile();
    }

    public void saveOnDisk() throws IOException, InvalidDBFile {
        this.writeColumns();
    }

    public void writeColumns() throws IOException, InvalidDBFile {
        List<DBColumn> col_list = columns.getList();
        int columnsBlocks = (int)Math.ceil(((float)columns.sizeOnDisk()/Connection.getSuperblock().blocksize));
        int offset = 0;
        for(int c = 0; c < columnsBlocks; c++){
            int freeBlock = Connection.getBitmap().freeBlock();
            offset = (int) (Connection.getSuperblock().blocksize*freeBlock) + 4;
            this.file.seek(offset);
            for(int i = 0; i < col_list.size(); i++){
                int column_length = col_list.get(i).name.length();
                this.file.writeInt(column_length);
                String column_name = col_list.get(i).name;
                this.file.writeChars(column_name);
                String type = col_list.get(i).type.name();
                this.file.writeInt(type.length());
                this.file.writeChars(type);
            }
            Connection.getBitmap().set(freeBlock);
        }
        this.file.getFD().sync();
        Connection.getSuperblock().used_size += columnsBlocks*Connection.getSuperblock().blocksize;
        TableMetadata metadata = new TableMetadata();
        metadata.columns_count = col_list.size();
        metadata.registers = 0;
        metadata.table_size = 0;
        Connection.getSuperblock().table_count += 1;
        Connection.getDirTable().addTableEntry(this.name, offset, metadata);
        Connection.getSuperblock().write();
        System.out.println("Bitmap before write");
        System.out.println(Connection.getBitmap().toString());
        Connection.getBitmap().writeOnDisk();
        Connection.getDirTable().writeOnDisk();
        Connection.reload();
    }

    public void print(){
        List<DBColumn> columns = this.columns.getList();
        String[] columnNames = new String[columns.size()];
        Object[][] data = this.rows;
        for(int i = 0; i < columns.size(); i++){
            columnNames[i] = columns.get(i).name;
        }
        TextTable tt = new TextTable(columnNames, data);
        tt.printTable();
    }

    public void setRows(int rows_len, int cols_len, LinkedList<String> rows){
        this.rows = new Object[rows_len][cols_len];
        for(int i = 0; i < rows_len; i++){
            for(int k = 0; k < cols_len; k++){
                this.rows[i][k] = rows.pop();
            }
        }
    }
}
