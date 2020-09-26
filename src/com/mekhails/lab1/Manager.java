package com.mekhails.lab1;

public class Manager {

    public Manager(String configFilename)
    {
        ConfigReader configReader = new ConfigReader(configFilename);

        String[] params = configReader.readConfig();

        paramsAnalyzer = new ParamsAnalyzer(params);

        if (!paramsAnalyzer.AreParamsValid()){
            System.out.println("Error: parameters in config file are not valid");
            return;
        }

        reverser = new Reverser(paramsAnalyzer.getBuffSize());
    }

    public void Run()
    {
        if (!isEverythingAvailable())
        {
            System.out.println("Error has occurred while processing config file");
            return;
        }

        ByteReader br = new ByteReader(paramsAnalyzer.getInputStream(), -1);
        ByteWriter bw = new ByteWriter(paramsAnalyzer.getOutputStream(), -1);

        reverser.cloneEachReversedByteBuff(br, bw);

        br.Close();
        bw.Close();
    }

    private boolean isEverythingAvailable()
    {
        return  ((paramsAnalyzer != null) && (reverser != null));
    }

    private ParamsAnalyzer paramsAnalyzer;
    private Reverser reverser;
}
