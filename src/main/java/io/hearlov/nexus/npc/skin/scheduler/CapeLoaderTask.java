package io.hearlov.nexus.npc.skin.scheduler;

import cn.nukkit.Server;
import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.scheduler.AsyncTask;
import io.hearlov.nexus.npc.NexusNPC;
import io.hearlov.nexus.npc.skin.SkinCache;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class CapeLoaderTask extends AsyncTask {

    protected final Map<String, Path> pngPaths;
    protected final NexusNPC plugin;

    private static final Set<Integer> ACCEPTED_SKIN_SIZES = Set.of(
            64 * 32 * 4
    );

    public CapeLoaderTask(Map<String, Path> pngPaths, NexusNPC plugin){
        this.pngPaths = pngPaths;
        this.plugin = plugin;
    }

    @Override
    public void onRun(){
        Map<String, byte[]> capes = new HashMap<>();

        for(String pngPath : pngPaths.keySet()){
            Path path = pngPaths.get(pngPath);
            byte[] bytes = convert(path);

            if(bytes.length > 0){
                capes.put(pngPath, bytes);
            }
        }

        this.setResult(capes);
    }

    protected byte[] convert(Path pngPath){
        try(ImageInputStream iis = ImageIO.createImageInputStream(pngPath.toFile())){

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                throw new IllegalArgumentException("Image is not readable");
            }

            ImageReader reader = readers.next();
            reader.setInput(iis, true, true);

            if (!"png".equalsIgnoreCase(reader.getFormatName())) {
                throw new IllegalArgumentException("Only PNG images are allowed");
            }

            BufferedImage image = reader.read(0);
            validateBitDepth(image);
            return convertToSkinData(image);

        }catch(Exception e){
            plugin.getLogger().info("Cape data: " + pngPath.toString() + " failed to load");
        }
        return new byte[0];
    }

    /* ---------------- INTERNAL ---------------- */

    private static void validateBitDepth(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        int pixelSize = cm.getPixelSize(); // toplam bit

        // RGBA 8-bit = 32 bit
        if (pixelSize != 32) {
            throw new IllegalArgumentException("Only 8-bit PNG images are supported");
        }
    }

    private static byte[] convertToSkinData(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        int size = width * height * 4;
        validateSize(size);

        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();

        for(int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        return outputStream.toByteArray();
    }

    private static void validateSize(int size) {
        if (!ACCEPTED_SKIN_SIZES.contains(size)) {
            throw new IllegalArgumentException("Invalid skin size");
        }
    }

    @Override
    public void onCompletion(Server server) {
        @SuppressWarnings("unchecked")
        Map<String, byte[]> map = (Map<String, byte[]>) getResult();
        SkinCache.putCapeDatas(map);
    }
}
