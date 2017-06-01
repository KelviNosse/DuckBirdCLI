package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.duckbird.core.FileDatabase;
import com.duckbird.core.errors.*;
import com.duckbird.core.shared.Connection;

import static com.duckbird.core.shared.Utils.fromFormatToSize;

public class CreateDB implements Command{
    @Parameter(names = "-name", description = "Name of the db to create.")
    String name;
    @Parameter(names = "-size", description = "Size of the db.")
    String size;
    @Parameter(names = "-blocksize", description = "Block size of the db.")
    String blocksize;
    @Parameter(names = "-path", description = "Path directory to create db")
    String path = "";
    private FileDatabase database = Connection.getDatabase();
    public void execute(String[] args, JCommander jc) {
        try{
            jc.parse(args);
            if(this.name != null && this.size != null && this.blocksize != null){
                this.path = this.path.isEmpty() ? "." : this.path;
                long dbSize = fromFormatToSize(this.size);
                int blockSize = (int)(long)fromFormatToSize(this.blocksize);
                database = new FileDatabase(this.name, this.path, dbSize, blockSize);
                database.allocate();
            }else
                System.out.println("Size, name or blocksize are not specified.");
            this.name = "";
            this.path = "";
            this.size = "";
            this.blocksize = "";
        }catch(InvalidBlockSize e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid arguments!\nRun => help -name "+this.getName()+" to view usage.");
        }
    }

    public boolean compare(String command){
        return this.getName().contentEquals(command.toLowerCase());
    }

    public String getName() {
        return "createdb";
    }

}
