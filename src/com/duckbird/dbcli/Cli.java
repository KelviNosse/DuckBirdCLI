package com.duckbird.dbcli;
import com.duckbird.dbcli.commands.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli {
    public static List createCommands(){
        List<Command> queue = new ArrayList<>();
        queue.add(new Help());
        return queue;
    }

    public Command getCommand(List<Command> commands, String command) throws Exception {
        for(Command c: commands){
            if(c.compare(command)) return c;
        }
        throw new Exception("Command not found!");
    }


    public void Run(){
        List<Command> commands = this.createCommands();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("\nType a command");
            System.out.print("> ");
            String command = scanner.next();
            try {
                Command c = this.getCommand(commands, command);
                c.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
