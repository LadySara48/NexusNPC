package io.hearlov.nexus.npc.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.entity.Entity;
import cn.nukkit.nbt.tag.CompoundTag;
import io.hearlov.nexus.npc.entity.EntityListen;

import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.forms.create.CreateForm;

import java.util.Map;

public class NPCCommand extends Command {

    public NPCCommand(){
        super("npc", "NPC General Command", "/npc help");
        setPermission("nexus.npc.npc");

        commandParameters.clear();
        commandParameters.put("default", new CommandParameter[]{CommandParameter.newEnum("type", new String[]{"create", "edit", "remove"})});
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log){
        if(!sender.isPlayer()){
            sender.sendMessage("You can only use this command in the game.");
            return 0;
        }
        Player player = sender.asPlayer();
        ParamList params = (ParamList) result.getValue();

        String selected = (String) params.getResult(0);

        switch(selected){
            case "create":
                (new CreateForm()).sendForm(player);
                break;
            case "edit":
                EntityListen.addInteractiveIntruct(0, player);
                player.sendMessage("Please click on the NPC you want to §l§eedit§r.");
                break;
            case "remove":
                EntityListen.addInteractiveIntruct(1, player);
                player.sendMessage("Please click on the NPC you want to §l§eremove§r.");
                break;
        }

        return 1;
    }
}