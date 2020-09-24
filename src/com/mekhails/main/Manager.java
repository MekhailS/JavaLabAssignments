package com.mekhails.main;

public class Manager {

    public Manager(String configFilename)
    {
        configReader = new ConfigReader(configFilename);

        reverser = new Reverser(
                configReader.getBufferSize(),
                configReader.getInputFilename(), configReader.getOutputFilename());
    }

    public void Run()
    {
        reverser.cloneEachReversedByteBuff();
    }

    private ConfigReader configReader;
    private Reverser reverser;
}
