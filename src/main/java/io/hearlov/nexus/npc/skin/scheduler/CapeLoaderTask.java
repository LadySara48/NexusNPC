package io.hearlov.nexus.npc.skin.scheduler;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import io.hearlov.nexus.npc.NexusNPC;
import io.hearlov.nexus.npc.skin.SkinCache;
import io.hearlov.nexus.npc.utils.PNGToSkin;

import java.awt.*;
import java.nio.file.Path;
import java.util.*;

public class CapeLoaderTask extends AsyncTask {

    protected final Map<String, Path> pngPaths;
    protected final NexusNPC plugin;

    public CapeLoaderTask(Map<String, Path> pngPaths, NexusNPC plugin){
        this.pngPaths = pngPaths;
        this.plugin = plugin;
    }

    @Override
    public void onRun(){
        Map<String, byte[]> capes = new HashMap<>();

        for(String pngPath : pngPaths.keySet()){
            Path path = pngPaths.get(pngPath);
            byte[] bytes = PNGToSkin.convert(path, this.plugin, 1);

            if(bytes.length > 0){
                capes.put(pngPath, bytes);
            }
        }

        this.setResult(capes);
    }

    @Override
    public void onCompletion(Server server){
        @SuppressWarnings("unchecked")
        Map<String, byte[]> map = (Map<String, byte[]>) getResult();
        SkinCache.putCapeDatas(map);
    }
}
