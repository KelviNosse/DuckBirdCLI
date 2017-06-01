package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.duckbird.core.shared.Utils;

public class DeleteDB implements Command{
    @Parameter(names = "-name", description = "Name of the db to delete.")
    private String name;
    @Parameter(names = "-path", description = "Path directory to delete db")
    private String path = "";
    public void execute(String[] args, JCommander jc) {
        try{
            jc.parse(args);
            if(this.name != null) this.path = this.path.isEmpty() ? "./" : this.path;
            if(!this.path.endsWith("/")) this.path += "/";
            Utils.getInstance().dropFileDB(this.path+this.name);
        }catch(Exception e){
            System.out.println("Invalid arguments!\nRun => help -name "+this.getName()+" to view usage.");
        }
        this.name = "";
        this.path = "";
    }

    public boolean compare(String command){
        return this.getName().contentEquals(command.toLowerCase());
    }

    public String getName() {
        return "removedb";
    }
}
