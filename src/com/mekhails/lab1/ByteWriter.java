package com.mekhails.lab1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteWriter {

    ByteWriter(FileOutputStream fos, int size)
    {

        if (size < 0)
            bos = new BufferedOutputStream(fos);
        else
            bos = new BufferedOutputStream(fos, size);
    }

    void Write(byte[] buffer, int offset, int lenToWrite)
    {
        try
        {
            bos.write(buffer, offset, lenToWrite);
        } catch (IOException e) {
            System.out.println("Error: cannot write to output file");
        }
    }

    void Close()
    {
        try
        {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            System.out.println("Error: error during closing output file");
        }
    }

    private BufferedOutputStream bos;
}
