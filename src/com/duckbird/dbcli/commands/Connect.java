package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Connect implements Command{
    @Parameter(names="-path", description = "Path/Name of the db to connect")
    private String path;

    public void execute(String[] args, JCommander jc) {
        try{
            jc.parse(args);
            //todo
            this.path = "";
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Invalid arguments!\nRun => help -name "+this.getName()+" to view usage.");
        }
    }

    public boolean compare(String command){
        return this.getName().contentEquals(command.toLowerCase());
    }

    public String getName() {
        return "connect";
    }
}
