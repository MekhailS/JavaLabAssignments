package com.mekhails.lab2;

import ru.spbstu.pipeline.IReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public class ByteReader extends  Executor implements IReader {

    @Override
    public int setInputStream(FileInputStream fileInputStream)
    {
        bis = new BufferedInputStream(fileInputStream);
        return 0;
    }

    @Override
    public int setProducer(Object o) { return 0; }

    @Override
    public int execute(Object o)
    {
        try
        {
            int bytesRead = 0;
            byte[] buffer = new byte[bufferSize];
            bytesRead = bis.read(buffer, 0, bufferSize);

            if (bytesRead == -1)
                return 1;

            byte[] bufferSliced = Arrays.copyOfRange(buffer, 0, bytesRead);
            return consumer.execute(bufferSliced);
        }
        catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.READER.name);
        }
        return 1;
    }

    private BufferedInputStream bis;
}
