package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.errors.*;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.sqltasks.models.ColumnType;
import com.duckbird.core.structure.models.DirTable;
import com.duckbird.core.structure.models.TableEntries;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class Insertion {
    public Insertion(){}

    public void insert(String table_name, List<Column> columns, List<Expression> values) throws TableNotFound, IncorrectTableValues, IOException, NoSuchFreeSpace, InvalidDBFile, InvalidColumnValue {
        DirTable dirTable = Connection.getDirTable();
        if(!dirTable.entryExists(table_name)) throw new TableNotFound(table_name);
        TableEntries entry = dirTable.getEntry(table_name);
        int column_count = entry.TableMetadata.columns_count;
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        db_file.seek(entry.block_offset);
        if(columns.size() != column_count) throw new IncorrectTableValues(table_name, column_count, columns.size());
        for(int i = 0; i < column_count; i++){
            int length = db_file.readInt();
            String col_name = "";
            for (int k = 0; k < length; k++) col_name += db_file.readChar();
            int type_len = db_file.readInt();
            String type_name = "";
            for(int k = 0; k < type_len; k++) type_name += db_file.readChar();
            ColumnType col_type = ColumnType.valueOf(type_name);
            if(!col_name.equals(columns.get(i).toString())) throw new InvalidColumnValue(columns.get(i).toString());
        }
        int values_size = 0;
        for(int i = 0; i < values.size(); i++){
            values_size += values.get(i).toString().length() + 4;
        }
        if(values_size >= Connection.getSuperblock().blocksize) throw new NoSuchFreeSpace();
        int freeBlock = Connection.getBitmap().freeBlock();
        entry.registers_offset = (int) (freeBlock*Connection.getSuperblock().blocksize);
        Connection.setDirTable(dirTable);
        Connection.getBitmap().set(freeBlock);
        db_file.seek(entry.registers_offset);
        for(int i = 0; i < values.size(); i++){
            db_file.writeInt(values.get(i).toString().length());
            db_file.writeChars(values.get(i).toString());
        }
        Connection.getSuperblock().used_size += Connection.getSuperblock().blocksize;
        db_file.getFD().sync();
        Connection.getSuperblock().write();
        Connection.getBitmap().writeOnDisk();
        Connection.getDirTable().writeOnDisk();
        Connection.reload();
        System.out.println("Values were inserted successfully!");
    }
}
