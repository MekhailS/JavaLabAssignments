package com.mekhails.lab1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;

public class ParamsAnalyzer {

    public ParamsAnalyzer(String[] params) {
        try
        {
            if (params.length != ConfigReader.numParameters)
            {
                Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
                return;
            }

            fis = new FileInputStream(params[ConfigReader.Vocabulary.INPUT_FILE.i]);
            fos = new FileOutputStream(params[ConfigReader.Vocabulary.OUTPUT_FILE.i]);
            buffSize = Integer.parseInt(params[ConfigReader.Vocabulary.BUFFER_SIZE.i]);
        } catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
    }

    public boolean AreParamsValid()
    {
        return !(buffSize <= 0 || fis == null || fos == null);
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
