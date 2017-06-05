package com.duckbird.dbcli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.duckbird.core.shared.IOStreamDB;
import com.duckbird.core.shared.Utils;

import java.io.File;

public class Connect implements Command{
    @Parameter(names="-path", description = "Path with name of the db to connect.")
    private String path;

    public void execute(String[] args, JCommander jc) {
        try{
            jc.parse(args);
            if(path != null){
                IOStreamDB io = new IOStreamDB(new File(path));
                Utils.getInstance().file_path = path;
                System.out.println("Connecting...");
                io.load();
                System.out.println("Connected to db successfully!");
                this.path = "";
            }else System.out.println("You need to specify the path!");
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
