package com.duckbird.dbcli;
import com.beust.jcommander.JCommander;
import com.duckbird.dbcli.commands.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli {
    private JCommander jc;
    public static List createCommands(){
        List<Command> queue = new ArrayList<>();
        queue.add(new Help());
        queue.add(new CreateDB());
        queue.add(new DeleteDB());
        return queue;
    }

    private void addCommands(List<Command> commands){
        this.jc = new JCommander();
        for(Command c: commands) this.jc.addCommand(c.getName(), c);
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
            this.addCommands(commands);
            System.out.println("\nType a command");
            System.out.print("> ");
            try {
                String[] args = scanner.nextLine().split("\\s+");
                Command c = this.getCommand(commands, args[0]);
                c.execute(args, this.jc);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
