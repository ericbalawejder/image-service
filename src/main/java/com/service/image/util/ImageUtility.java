package com.service.image.util;

import com.service.image.exception.ImageCompressionException;
import com.service.image.exception.ImageDecompressionException;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtility {

    public static byte[] compressImage(byte[] data) {
        final Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        final byte[] temp = new byte[4 * 1024];
        try {
            while (!deflater.finished()) {
                final int size = deflater.deflate(temp);
                outputStream.write(temp, 0, size);
                outputStream.close();
            }
        } catch (Exception e) {
            throw new ImageCompressionException();
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        final Inflater inflater = new Inflater();
        inflater.setInput(data);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        final byte[] temp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                final int count = inflater.inflate(temp);
                outputStream.write(temp, 0, count);
            }
            outputStream.close();
        } catch (Exception e) {
            throw new ImageDecompressionException();
        }
        return outputStream.toByteArray();
    }

}
