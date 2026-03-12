package io.hearlov.nexus.npc.entity;

import cn.nukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class NPCCommandSender extends ConsoleCommandSender{

    public final NexusEntity owner;

    public NPCCommandSender(NexusEntity entity){
        this.owner = entity;
    }

    @Override
    public @NotNull String getName() {
        return "NexusEntity";
    }

    @SuppressWarnings("unused")
    public NexusEntity getOwner(){
        return owner;
    }

}
