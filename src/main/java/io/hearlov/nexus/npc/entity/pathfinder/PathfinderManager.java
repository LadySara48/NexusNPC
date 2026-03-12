package io.hearlov.nexus.npc.entity.pathfinder;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.TaskHandler;
import io.hearlov.nexus.npc.entity.NexusEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PathfinderManager{

    private static PathfinderManager instance;

    private final Map<Long, TaskHandler> handlers = new ConcurrentHashMap<>();

    private PathfinderManager(){}

    public static PathfinderManager getInstance() {
        if (instance == null) instance = new PathfinderManager();
        return instance;
    }

    public void start(Plugin plugin, NexusEntity entity) {
        if (entity.movementType == 0) return; // Statik NPC → hareket yok

        long id = entity.getId();
        if (handlers.containsKey(id)) return; // Zaten çalışıyor

        PathfinderTask task    = new PathfinderTask(plugin, entity);
        TaskHandler   handler = plugin.getServer().getScheduler()
                .scheduleRepeatingTask(plugin, task, 2); // period = 1 tick = 50ms (100ms)

        handlers.put(id, handler);
    }

    public void stop(NexusEntity entity) {
        TaskHandler handler = handlers.remove(entity.getId());
        if (handler != null) handler.cancel();
    }

    public void stopAll() {
        handlers.values().forEach(TaskHandler::cancel);
        handlers.clear();
    }

    public void remove(long entityId) {
        handlers.remove(entityId);
    }

    public boolean isRunning(NexusEntity entity) {
        return handlers.containsKey(entity.getId());
    }
}
