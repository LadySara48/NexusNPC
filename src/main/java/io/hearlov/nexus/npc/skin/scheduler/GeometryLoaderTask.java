package io.hearlov.nexus.npc.skin.scheduler;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import io.hearlov.nexus.npc.NexusNPC;
import io.hearlov.nexus.npc.skin.SkinCache;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GeometryLoaderTask extends AsyncTask {

    private final Map<String, Path> geometryPaths;
    private final NexusNPC plugin;

    public GeometryLoaderTask(Map<String, Path> geometryPaths, NexusNPC plugin) {
        this.geometryPaths = geometryPaths;
        this.plugin = plugin;
    }

    @Override
    public void onRun() {
        Map<String, String> geometryDataMap = new HashMap<>();

        for (String str : geometryPaths.keySet()) {
            Path path = geometryPaths.get(str);

            try {

                if (!Files.exists(path)) {
                    plugin.getLogger().info("[GeometryLoader] Geometry file not found: " + path);
                    continue;
                }

                if (!path.toString().endsWith(".json")) {
                    plugin.getLogger().info("[GeometryLoader] Invalid geometry file (not json): " + path);
                    continue;
                }

                String data = Files.readString(path);

                if (data.isEmpty()) {
                    plugin.getLogger().info("[GeometryLoader] Geometry file is empty: " + path);
                    continue;
                }

                geometryDataMap.put(str, data);

            } catch (Exception e) {
                plugin.getLogger().info(
                        "[GeometryLoader] Failed to load geometry: " + path + " | " + e.getMessage()
                );
            }
        }

        this.setResult(geometryDataMap);
    }

    @Override
    public void onCompletion(Server server) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) getResult();
        SkinCache.putGeometryDatas(map);
    }
}
