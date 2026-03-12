package io.hearlov.nexus.npc.utils;

import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import io.hearlov.nexus.npc.NexusNPC;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

public class PNGToSkin{

    private static final Set<Integer> ACCEPTED_SKIN_SIZES = Set.of(
            64 * 32 * 4,
            64 * 64 * 4,
            128 * 128 * 4
    );

    private static final Set<Integer> ACCEPTED_CAPE_SIZES = Set.of(
            64 * 32 * 4
    );

    public static byte[] convert(Path pngPath, NexusNPC plugin, int type){
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
            return convertToSkinData(image, type);

        }catch(Exception e){
            plugin.getLogger().info("Skin data: " + pngPath.toString() + " failed to load");
        }
        return new byte[0];
    }

    private static void validateBitDepth(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        int pixelSize = cm.getPixelSize();

        if (pixelSize != 32) {
            throw new IllegalArgumentException("Only 8-bit PNG images are supported");
        }
    }

    private static byte[] convertToSkinData(BufferedImage image, int type){
        int width = image.getWidth();
        int height = image.getHeight();

        int size = width * height * 4;
        validateSize(size, type);

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
        byte[] stc = outputStream.toByteArray();

        try{
            outputStream.close();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        return stc;
    }

    private static void validateSize(int size, int type){
        if(type == 0 && !ACCEPTED_SKIN_SIZES.contains(size)){
            throw new IllegalArgumentException("Invalid skin size");
        }else if(type == 1 && !ACCEPTED_CAPE_SIZES.contains(size)){
            throw new IllegalArgumentException("Invalid cape size");
        }
    }

}
