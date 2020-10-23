package com.mekhails.lab1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ParamsAnalyzer {

    public ParamsAnalyzer(String[] params) {
        try
        {
            if (params.length != ConfigReader.numParameters)
            {
                System.out.println("Error: not enough parameters from config file");
                return;
            }

            fis = new FileInputStream(params[ConfigReader.Vocabulary.INPUT_FILE.i]);
            fos = new FileOutputStream(params[ConfigReader.Vocabulary.OUTPUT_FILE.i]);
            buffSize = Integer.parseInt(params[ConfigReader.Vocabulary.BUFFER_SIZE.i]);
        } catch (FileNotFoundException e) {
            System.out.println("Error: can not access input of output file");
        }
    }

    public boolean AreParamsValid()
    {
        return (buffSize <= 0 || fis == null || fos == null);
    }

    public int getBuffSize() {
        return buffSize;
    }

    public FileInputStream getInputStream() {
        return fis;
    }

    public FileOutputStream getOutputStream() {
        return fos;
    }

    private FileInputStream fis;
    private FileOutputStream fos;
    private int buffSize;
}
