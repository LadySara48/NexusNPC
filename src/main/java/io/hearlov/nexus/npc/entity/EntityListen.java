package io.hearlov.nexus.npc.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;

import io.hearlov.nexus.npc.forms.edit.EditFormMain;

import java.util.HashMap;
import java.util.Map;

public class EntityListen{

    private static final Map<String, Integer> interactive = new HashMap<>();

    public static void addInteractiveIntruct(Integer type, Player player){
        interactive.put(player.getXUID(), type);
    }

    public static void removeInteractive(Player player){
        interactive.remove(player.getXUID());
    }

    public static Integer getInteractive(Player player){
        return interactive.get(player.getXUID());
    }

    public static Boolean isInteractive(Player player){
        return interactive.containsKey(player.getXUID());
    }

    public static void onInteract(Player player, Entity entity){
        if(!(entity instanceof NexusEntity)) return;
        Integer type = getInteractive(player);
        removeInteractive(player);

        switch(type){
            case 0: //Edit
                (new EditFormMain((NexusEntity) entity)).sendForm(player);
                break;
            case 1: //Remove
                entity.close();
                player.sendMessage("This entity has been removed.");
                break;
        }
    }

}
