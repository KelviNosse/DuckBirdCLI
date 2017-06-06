package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.errors.InvalidColumnValue;
import com.duckbird.core.errors.TableNotFound;
import com.duckbird.core.shared.Connection;
import com.duckbird.core.structure.models.DirTable;
import com.duckbird.core.structure.models.TableEntries;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DeleteRecord {
    public DeleteRecord(){}

    public void deleteFrom(String table_name, String leftExpression, String rightExpression, String condition) throws TableNotFound, IOException, InvalidColumnValue {
        DirTable dirTable = Connection.getDirTable();
        if(!dirTable.entryExists(table_name)) throw new TableNotFound(table_name);
        TableEntries entry = dirTable.getEntry(table_name);
        int block = (int) ((int) (entry.registers_offset + Connection.getSuperblock().blocksize) / Connection.getSuperblock().blocksize);
        String column = leftExpression;
        String condition_value = "";
        RandomAccessFile db_file = Connection.getDatabase().getFile();
        db_file.seek(entry.registers_offset);
        for(int i = 0; i < entry.TableMetadata.columns_count; i++){
            int length = db_file.readInt();
            String val = "";
            for(int k = 0; k < length; k++) val += db_file.readChar();
            if(!val.equals(column)) throw new InvalidColumnValue(column);
            condition_value = val;
            switch (condition){
                case ">":
                    break;
                case "=":
//                    if(column.equals(condition_value)){
//                        entry.TableMetadata.registers = 0;
//                        Connection.getBitmap().toggle(block);
//                        System.out.println("Row deleted.");
//                    }
                    break;
                case "<":
                    break;
                case ">=":
                    break;
                case "<=":
                    break;
            }
            break;
        }

//        Connection.getBitmap().toggle(block);
//        entry.registers_offset = 0;
//        entry.TableMetadata.registers = 0;
//        entry.TableMetadata.table_size = 0;
//        entry.block_offset = 0;
//        Connection.setDirTable(dirTable);
//        Connection.getSuperblock().write();
//        Connection.getBitmap().writeOnDisk();
//        Connection.getDirTable().writeOnDisk();
    }
}
