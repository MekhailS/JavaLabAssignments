package com.mekhails.lab1;

import java.io.*;

public class Reverser {

    public Reverser(int bufferSize)
    {
        buffer = new byte[bufferSize];
    }

    public void cloneEachReversedByteBuff(ByteReader br, ByteWriter bw)
    {
        int nBytesRead = 0;

        while ((nBytesRead = br.Read(buffer, 0, buffer.length)) != -1)
        {
            reverseBitsInBuffer();

            bw.Write(buffer, buffer.length - nBytesRead, nBytesRead);
        }
    }

    private void reverseBitsInBuffer()
    {
        reverseBytesInBuffer();
        for (int i = 0; i < buffer.length; i++)
        {
            buffer[i] = reverseBitsInByte(buffer[i]);
        }
    }

    private void reverseBytesInBuffer()
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

    private byte[] buffer;
}
