package io.hearlov.nexus.npc.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import io.hearlov.nexus.npc.forms.skins.SkinsForm;

public class NexusSkinCommand extends Command {

    public NexusSkinCommand(){
        super("skins", "Shows about NexusNPC Skins", "/skins");
        this.setPermission("nexus.npc.skin");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        if(!sender.isPlayer()) return false;

        (new SkinsForm()).sendForm(sender.asPlayer());

        return true;
    }
}