package com.duckbird.dbcli.commands;

import com.beust.jcommander.*;

public class Help implements Command {
    @Parameter(names="-command", description = "Shows the specified command help")
    private String command;

    public void execute(String[] args, JCommander jc) {
        try {
            jc.parse(args);
            //do more things
            if(this.command != null){
                //show default help => list all commands
            }
        }catch(Exception e){
            System.out.println("Argument invalid!");
        }
    }

    public String getName(){
        return "help";
    }

    public boolean compare(String command){
        return this.getName().contentEquals(command.toLowerCase());
    }
}
