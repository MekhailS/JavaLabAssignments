package com.mekhails.lab2;

import com.mekhails.lab2.Log;
import ru.spbstu.pipeline.IWriter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class ByteWriter extends Executor implements IWriter {

    @Override
    public int setOutputStream(FileOutputStream fileOutputStream)
    {
        bos = new BufferedOutputStream(fileOutputStream);
        return 0;
    }

    @Override
    public int setConsumer(Object o) { return 0; }

    @Override
    public int execute(Object o)
    {
        try
        {
            byte[] buffer = (byte[])o;

            if (buffer.length > bufferSize)
                return 1;

            bos.write(buffer, 0, buffer.length);
            bos.flush();
            return 0;
        }
        catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.WRITER.name);
        }
        return 1;
    }

    private BufferedOutputStream bos;
}
