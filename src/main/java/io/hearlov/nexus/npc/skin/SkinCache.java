package io.hearlov.nexus.npc.skin;

import cn.nukkit.entity.data.Skin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import cn.nukkit.utils.Config;
import io.hearlov.nexus.npc.NexusNPC;
import io.hearlov.nexus.npc.skin.scheduler.CapeLoaderTask;
import io.hearlov.nexus.npc.skin.scheduler.GeometryLoaderTask;
import io.hearlov.nexus.npc.skin.scheduler.SkinLoaderTask;

public class SkinCache{

    private static Map<String, Skin> cachedSkins;

    private static Map<String, byte[]> cachedSkinDatas;
    private static Map<String, String> cachedGeometryDatas;
    private static Map<String, byte[]> cachedCapeDatas;

    private static boolean skinReturned = false;
    private static boolean geometryReturned = false;
    private static boolean capeReturned = false;
    private static Config config;

    public static void setup(Path pluginPath) {

        Map<String, Path> skinPaths = new HashMap<>();
        Map<String, Path> geometryPaths = new HashMap<>();
        Map<String, Path> capePaths = new HashMap<>();

        Path skinsDir = pluginPath.resolve("skins");
        Path capesDir = pluginPath.resolve("capes");
        Path geometriesDir = pluginPath.resolve("geometries");

        createDirIfNotExists(skinsDir);
        createDirIfNotExists(capesDir);
        createDirIfNotExists(geometriesDir);

        config = new Config(
                pluginPath.resolve("skins.yml").toString(),
                Config.YAML
        );

        for(String skinName : config.getKeys(false)){

            String skinFile = config.getString(skinName + ".skin", "");
            String skinId   = config.getString(skinName + ".skinId", "");

            if(skinFile.isEmpty() || skinId.isEmpty()) continue;

            Path skinPath = skinsDir.resolve(skinFile + ".png");
            if(!Files.exists(skinPath)) continue;

            String geometryFile = config.getString(skinName + ".geometry", "");
            String geometryId   = config.getString(skinName + ".geometryId", "");

            if(!geometryFile.isEmpty() && geometryId.isEmpty()) continue;

            Path geometryPath = null;
            if(!geometryFile.isEmpty()){
                Path gp = geometriesDir.resolve(geometryFile + ".json");
                if(!Files.exists(gp)) continue;
                geometryPath = gp;
            }

            String capeFile = config.getString(skinName + ".cape", "");
            Path capePath = null;

            if(!capeFile.isEmpty()){
                Path cp = capesDir.resolve(capeFile + ".png");
                if(Files.exists(cp)){
                    capePath = cp;
                }
            }

            skinPaths.put(skinName, skinPath);

            if(geometryPath != null){
                geometryPaths.put(skinName, geometryPath);
            }

            if(capePath != null){
                capePaths.put(skinName, capePath);
            }
        }

        NexusNPC.getInstance().getServer().getScheduler().scheduleAsyncTask(new SkinLoaderTask(skinPaths, NexusNPC.getInstance()));
        NexusNPC.getInstance().getServer().getScheduler().scheduleAsyncTask(new CapeLoaderTask(capePaths, NexusNPC.getInstance()));
        NexusNPC.getInstance().getServer().getScheduler().scheduleAsyncTask(new GeometryLoaderTask(geometryPaths, NexusNPC.getInstance()));
    }

    public static Map<String, Skin> getCachedSkins(){
        return cachedSkins;
    }

    public static void putSkinDatas(Map<String, byte[]> skins){
        cachedSkinDatas = skins;
        skinReturned = true;
        setupable();
    }

    public static void putGeometryDatas(Map<String, String> geometries){
        cachedGeometryDatas = geometries;
        geometryReturned = true;
        setupable();
    }

    public static void putCapeDatas(Map<String, byte[]> capes){
        cachedCapeDatas = capes;
        capeReturned = true;
        setupable();
    }

    public static void setupable(){
        if(skinReturned && geometryReturned && capeReturned){
            handleSkins();

            skinReturned = false;
            geometryReturned = false;
            capeReturned = false;

            hashMemory();
        }
    }

    public static void handleSkins(){
        Map<String, Skin> skins = new HashMap<>();
        for(String skinName : config.getKeys(false)){
            if(!cachedSkinDatas.containsKey(skinName)) continue;

            String skinId = config.getString(skinName + ".skinId", "");
            String geometryId = config.getString(skinName + ".geometryId", "");

            Skin skin = new Skin();

            skin.setSkinId(skinId.isEmpty() ? UUID.randomUUID().toString() : skinId);
            skin.setSkinData(cachedSkinDatas.get(skinName));

            if(cachedGeometryDatas.containsKey(skinName)){
                skin.setPlayFabId("00000000-0000-0000-0000-000000000000");
                skin.setGeometryName(geometryId);
                skin.setGeometryData(cachedGeometryDatas.get(skinName));
                skin.setPremium(false);
                skin.setTrusted(true);
            }

            if(cachedCapeDatas.containsKey(skinName)) {
                skin.setCapeData(cachedCapeDatas.get(skinName));
            }

            skins.put(skinName, skin);
        }

        cachedSkins = skins;
    }

    public static void hashMemory(){
        cachedSkinDatas = null;
        cachedGeometryDatas = null;
        cachedCapeDatas = null;
        config = null;
    }

    private static void createDirIfNotExists(Path path){
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception ignored) {}
    }

}