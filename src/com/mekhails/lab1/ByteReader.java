package com.mekhails.lab1;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
            System.out.println("Error: cannot read from input file");;
        }
        return -1;
    }

    void Close()
    {
        try
        {
            bis.close();
        } catch (IOException e) {
            System.out.println("Error: error during closing input file");
        }
    }

    private BufferedInputStream bis;
}
