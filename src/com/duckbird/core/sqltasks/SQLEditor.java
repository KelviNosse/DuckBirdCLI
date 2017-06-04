package com.duckbird.core.sqltasks;

import jdk.nashorn.internal.runtime.ParserException;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

public class SQLEditor extends CCJSqlParserManager{
    public SQLEditor(){
        super();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\b\b\b");
        System.out.println("DuckBird SQL Runner v1.0");
        System.out.println("Type a sql statement or type exit to return to cli.");
        while(true){
            String sql;
            sql = scanner.nextLine();
            if(sql.equalsIgnoreCase("exit")) break;
            try {
                this.validateSQL(sql);
            } catch (JSQLParserException e) {
                System.out.println("Sql invalid, or not supported :(");
            }
        }   
    }

    private void SelectHandler(Select statement){
        SelectBody body = statement.getSelectBody();
        if(body instanceof PlainSelect){
            //todo call select handler class to go execute tasks through this data
            PlainSelect select = (PlainSelect)body;
            List<SelectItem> selectedItems = select.getSelectItems();
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(statement);
            Expression expression = select.getWhere();
            if(expression instanceof AndExpression){
                //currently only support 'AND' expression once
                AndExpression andExpression = (AndExpression)expression;
                Expression leftExpression = andExpression.getLeftExpression();
                Expression rightExpression = andExpression.getRightExpression();
            }else{
                //single expression
            }
        }
    }

    private void CreateTableHandler(CreateTable ct){
        //todo call create table handler class to go execute tasks through this data
        System.out.println("Table name: "+ct.getTable().getName());
        List<ColumnDefinition> cols = ct.getColumnDefinitions();
        for(int i = 0; i < cols.size(); i++){
            System.out.println("Col name: "+cols.get(i).getColumnName());
            System.out.println("Col type: "+cols.get(i).getColDataType());
        }
    }

    private void InsertHandler(Insert insert){
        //todo call insert into handler class to go execute tasks through this data
        System.out.println("Table name:"+insert.getTable().getName());
        List<Column> columns = insert.getColumns();
        for(int i = 0; i < columns.size(); i++){
            System.out.println("Column name: "+columns.get(i).getColumnName());
        }
        List<Expression> values = ((ExpressionList)insert.getItemsList()).getExpressions();
        for(int i = 0; i < values.size(); i++){
            System.out.println("Value: "+values.get(i).toString());
        }
    }

    private void DropTableHandler(Drop drop){
        //todo call drop table handler class to go execute tasks through this data
        System.out.println("Table name to drop: "+drop.getName());
    }

    private void DeleteFromHandler(Delete delete){
        System.out.println("Table name: "+delete.getTable().getName());
        Expression where = delete.getWhere();
        System.out.println("Where: "+where.toString());
    }

    private void UpdateHandler(Update update){
        System.out.println("Table name: "+update.getTable().getName());
        List<Column> columns = update.getColumns();
        for(int i = 0; i < columns.size(); i++){
            System.out.println("Column: "+columns.get(i).getColumnName());
        }
        List<Expression> expressions = update.getExpressions();
        for(int i = 0; i < expressions.size(); i++){
            System.out.println("Expression: "+expressions.get(i).toString());
        }
        Expression where = update.getWhere();
        System.out.println("Where: "+where.toString());
    }

    private void validateSQL(String sql) throws JSQLParserException, ParserException {
        Statement statement = this.parse(new StringReader(sql));
        if(statement != null){
            System.out.println(statement.toString());
            if(statement instanceof Select){
                this.SelectHandler((Select)statement);
            }else if(statement instanceof CreateTable){
                this.CreateTableHandler((CreateTable)statement);
            }else if(statement instanceof Insert){
                this.InsertHandler((Insert)statement);
            }else if(statement instanceof Drop){
                this.DropTableHandler((Drop)statement);
            }else if(statement instanceof Delete){
                this.DeleteFromHandler((Delete)statement);
            }else if(statement instanceof Update){
                this.UpdateHandler((Update)statement);
            }
        }
    }
}