package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Reverser extends Executor {

    @Override
    public int execute(Object o)
    {
        byte[] buffer = (byte[])o;

        if (buffer.length > bufferSize)
            return 1;

        reverseBitsInBuffer(buffer);

        return(consumer.execute(buffer));
    }

    private void reverseBitsInBuffer(byte[] buffer)
    {
        reverseBytesInBuffer(buffer);
        for (int i = 0; i < buffer.length; i++)
        {
            buffer[i] = reverseBitsInByte(buffer[i]);
        }
    }

    private void reverseBytesInBuffer(byte[] buffer)
    {
        for (int i = 0; i < buffer.length/2; i++)
        {
            byte tmp = buffer[i];
            buffer[i] = buffer[buffer.length - 1 - i];
            buffer[buffer.length - 1 - i] = tmp;
        }
    }

    private byte reverseBitsInByte(byte x)
    {
        byte res = 0;
        for (int i = 0; i < 8; i++)
        {
            res <<= 1;
            res |= ( (x >> i) & 1);
        }
        return res;
    }
}
