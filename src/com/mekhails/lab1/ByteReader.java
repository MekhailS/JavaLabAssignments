package com.mekhails.lab1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

public class ByteReader {

    ByteReader(FileInputStream fis, int size)
    {
        if (size < 0)
            bis = new BufferedInputStream(fis);
        else
            bis = new BufferedInputStream(fis, size);
    }

    int Read(byte[] buffer, int offset, int lenToRead)
    {
        try
        {
            return bis.read(buffer, offset, lenToRead);

        } catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.READER.name);
        }
        return -1;
    }

    void Close()
    {
        try
        {
            bis.close();
        } catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.READER.name);
        }
    }

    private BufferedInputStream bis;
}
