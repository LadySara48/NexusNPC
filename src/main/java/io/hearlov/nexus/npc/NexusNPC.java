package io.hearlov.nexus.npc;

import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import io.hearlov.nexus.npc.command.DispatchPlayerCommand;
import io.hearlov.nexus.npc.command.NPCCommand;
import io.hearlov.nexus.npc.command.NexusNPCCommand;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.registry.RegisterException;
import cn.nukkit.registry.Registries;
import io.hearlov.nexus.npc.command.NexusSkinCommand;
import io.hearlov.nexus.npc.entity.NexusEntity;
import io.hearlov.nexus.npc.entity.pathfinder.NexusPathfinder;
import io.hearlov.nexus.npc.entity.pathfinder.PathfinderManager;
import io.hearlov.nexus.npc.skin.SkinCache;

public class NexusNPC extends PluginBase{

    private static NexusNPC instance;
    public static NexusNPC getInstance(){ return instance; }

    public static int pathfindertick = 2;

    @Override
    public void onLoad(){
        instance = this;
        __initEntity();
    }

    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        pathfindertick = this.getConfig().getInt("Pathfinder-Per-Tick", 2);
        NexusPathfinder.preSetup(this.getConfig());

        __initCommand();

        getServer().getPluginManager().registerEvents(new io.hearlov.nexus.npc.listener.EntityListener(), this);

        if(!getDataFolder().exists()){
            if(!getDataFolder().mkdir()) getServer().getPluginManager().disablePlugin(this);
        }

        SkinCache.setup(getDataFolder().getAbsoluteFile().toPath());
    }

    @Override
    public void onDisable(){
        PathfinderManager.getInstance().stopAll();
        SkinCache.hashMemory();
    }

    public void __initEntity(){
        try {
            Registries.ENTITY.registerCustomEntity(this, NexusEntity.class);

            Registries.ENTITY.rebuildTag();
        }catch(RegisterException e){
            throw new RuntimeException(e);
        }
    }

    public void __initCommand(){
        List<Command> commands = new ArrayList<>();

        commands.add(new NexusNPCCommand());
        commands.add(new NPCCommand());
        commands.add(new NexusSkinCommand());
        commands.add(new DispatchPlayerCommand());

        this.getServer().getCommandMap().registerAll("hearlovnexusnpc", commands);
    }

}