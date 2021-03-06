package com.mekhails.lab2;

import java.util.logging.Level;

public class Main
{
    public static void main(String[] Args)
    {
        if (Args == null || Args.length == 0) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.COMMAND_PROMPT.name);
            return;
        }

        String configFilename = Args[0];

        Manager mng = new Manager(configFilename);

        mng.configureAndConstructPipeline();

        if (mng.isEverythingAvailable())
            mng.run();

    }
}
