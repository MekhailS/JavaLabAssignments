package com.mekhails.main;

import java.io.*;

public class Reverser {

    public Reverser(int bufferSize, String fileSource_s, String fileDest_s)
    {
        buffer = new byte[bufferSize];
        fileSource = fileSource_s;
        fileDest = fileDest_s;
    }

    public void cloneEachReversedByteBuff()
    {
        try
        {
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(fileSource));
            BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(fileDest));

            int nBytesRead = 0;

            while ((nBytesRead = fis.read(buffer, 0, buffer.length)) != -1)
            {
                reverseBitsInBuffer();

                fos.write(buffer, buffer.length - nBytesRead, nBytesRead);
            }

            fos.flush();
            fis.close();
            fos.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: no files for reversing bits");
        } catch (IOException e) {
            System.out.println("Error: can not read/write from/to file");
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
    private String fileSource;
    private String fileDest;
}
