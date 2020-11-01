package com.mekhails.lab2alt.lab2;

public class Main
{
    public static void main(String[] Args)
    {
        /*if (Args == null || Args.length == 0) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.COMMAND_PROMPT.name);
            return;
        }

        String configFilename = Args[0];*/

        String configFilename = "src\\com\\mekhails\\lab2alt\\lab2\\managerConfig.txt";

        Manager mng = new Manager(configFilename);

        mng.configureAndConstructPipeline();

        if (mng.isEverythingAvailable())
            mng.run();

    }
}
