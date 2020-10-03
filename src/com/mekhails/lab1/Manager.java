package com.mekhails.lab1;

import java.util.logging.Level;

public class Manager {

    public Manager(String configFilename)
    {
        ConfigReader configReader = new ConfigReader(configFilename);

        String[] paramsConfig = configReader.readConfig();

        ParamsAnalyzer paramsAnalyzer = new ParamsAnalyzer(paramsConfig);

        if (!paramsAnalyzer.AreParamsValid())
            return;

        byteReader = new ByteReader(paramsAnalyzer.getInputStream(), -1);
        byteWriter = new ByteWriter(paramsAnalyzer.getOutputStream(), -1);

        reverser = new Reverser(paramsAnalyzer.getBuffSize());
    }

    public void Run()
    {
        if (!isEverythingAvailable())
        {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return;
        }

        reverser.cloneEachReversedByteBuff(byteReader, byteWriter);

        byteReader.Close();
        byteWriter.Close();
    }

    boolean isEverythingAvailable()
    {
        return  ((byteWriter != null) && (byteReader != null) && (reverser != null));
    }

    private ByteWriter byteWriter;
    private ByteReader byteReader;
    private Reverser reverser;
}
