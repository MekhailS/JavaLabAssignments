package com.mekhails.lab2alt.lab2;

import java.util.logging.Logger;

public class Main
{
    public static void main(String[] Args)
    {

        Logger logger = Logger.getLogger("pipeline");

        /*if (Args == null || Args.length == 0) {
            logger.log(Level.SEVERE, Log.ERROR.COMMAND_PROMPT.name);
            return;
        }

        String configFilename = Args[0];*/

        String configFilename = "src\\com\\mekhails\\lab2alt\\lab2\\config\\managerConfig.txt";

        Manager mng = new Manager(configFilename, logger);

        mng.configureAndConstructPipeline();

        if (mng.isEverythingAvailable())
            mng.run();

    }
}
