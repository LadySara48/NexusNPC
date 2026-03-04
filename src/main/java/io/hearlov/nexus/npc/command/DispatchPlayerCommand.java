package io.hearlov.nexus.npc.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.Arrays;

public class DispatchPlayerCommand extends Command{

    public DispatchPlayerCommand(){
        super("DPC", "Dispatch Player Command", "/dpc (player) (command)");
        this.setPermission("nexus.npc.dispatch");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        if(args.length < 2) return false;

        String pname = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        String command = String.join(" ", args);

        Player player = sender.getServer().getPlayer(pname);
        if(player == null) return false;

        sender.getServer().executeCommand(player, command);

        return true;
    }

}
