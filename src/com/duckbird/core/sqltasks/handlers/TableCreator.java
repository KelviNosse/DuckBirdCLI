package com.duckbird.core.sqltasks.handlers;

import com.duckbird.core.errors.InvalidColumnType;
import com.duckbird.core.sqltasks.models.ColumnType;
import com.duckbird.core.sqltasks.models.DBColumns;
import com.duckbird.core.sqltasks.models.DBTable;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;

import java.io.IOException;
import java.util.List;

public class TableCreator {
    public TableCreator(){

    }

    public void create(String name, List<ColumnDefinition> columns) throws InvalidColumnType {
        //todo create table on disk with specified table data
        DBColumns cols = new DBColumns();
        for(int i = 0; i < columns.size(); i++){
            String columnName = columns.get(i).getColumnName();
            ColumnType columnType = this.validateColType(columns.get(i).getColDataType().toString());
            cols.add(columnName, columnType);
        }
        DBTable table = new DBTable(name, cols);
        try{
            table.saveOnDisk();
            System.out.println("Table was created successfully!");
        }catch(IOException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private ColumnType validateColType(String type_name) throws InvalidColumnType {
            if(type_name.toUpperCase().indexOf("(") != -1){
                String name = type_name.toUpperCase().substring(0, type_name.toUpperCase().indexOf("(")-1);
                if(ColumnType.valueOf(name) != ColumnType.CHAR) throw new InvalidColumnType(type_name);
                return ColumnType.CHAR_ARRAY;
            }else{
                try{
                    switch (ColumnType.valueOf(type_name.toUpperCase())){
                        case INT:
                            return ColumnType.INT;
                        case CHAR:
                            return ColumnType.CHAR;
                        case DOUBLE:
                            return ColumnType.DOUBLE;
                        default:
                            throw new InvalidColumnType(type_name);
                    }
                }catch(IllegalArgumentException e){
                    throw new InvalidColumnType(type_name);
                }
            }
    }
}
