package io.hearlov.nexus.npc.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import io.hearlov.nexus.npc.NexusNPC;

public class NexusNPCCommand extends Command {

    public NexusNPCCommand(){
        super("nexusnpc", "Shows about NexusNPC", "/nexusnpc");
        this.setPermission("nexus.npc.about");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        NexusNPC base = NexusNPC.getInstance();
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("  §6§lNEXUS NON PLAYABLE PLAYER & SKIN LIB §8| §7v" + base.getDescription().getVersion());
        sender.sendMessage("  §7▪ §fAuthor: §e" + String.join(", ", base.getDescription().getAuthors()));
        sender.sendMessage("  §7▪ §fAPI Version: §b" + base.getDescription().getCompatibleAPIs().getFirst() + "+");
        sender.sendMessage("  §7" + base.getDescription().getDescription());
        sender.sendMessage("§8§m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        return true;
    }
}