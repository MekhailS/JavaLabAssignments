package com.mekhails.lab2;

import com.mekhails.lab2.Log;

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
        configFilename = "src\\com\\mekhails\\lab2\\managerConfig.txt";

        Manager mng = new Manager(configFilename);

        if (mng.isEverythingAvailable())
            mng.Run();
    }
}
