package io.hearlov.nexus.npc.entity.pathfinder;

import cn.nukkit.scheduler.PluginTask;
import io.hearlov.nexus.npc.entity.NexusEntity;

public class PathfinderTask extends PluginTask<cn.nukkit.plugin.Plugin>{

    private final NexusPathfinder pathfinder;
    private final NexusEntity     entity;

    public PathfinderTask(cn.nukkit.plugin.Plugin plugin, NexusEntity entity){
        super(plugin);
        this.entity     = entity;
        this.pathfinder = new NexusPathfinder(entity);
    }

    @Override
    public void onRun(int currentTick){
        if (entity.isClosed() || !entity.isAlive()){
            PathfinderManager.getInstance().remove(this.entity.getId());
            this.cancel();
            return;
        }
        pathfinder.tick();
    }

    @SuppressWarnings("unused")
    public NexusPathfinder getPathfinder() { return pathfinder; }

    @SuppressWarnings("unused")
    public void forceReturn() { pathfinder.forceReturn(); }
}
