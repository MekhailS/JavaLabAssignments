package com.mekhails.lab1;

public class Main
{
    public static void main(String[] Args)
    {
        if (Args == null || Args.length == 0) {
            System.out.println("Error: empty arguments of command prompt");
            return;
        }

        String configFilename = Args[0];

        Manager mng = new Manager(configFilename);

        mng.Run();

        //Manager mng1 = new Manager("src/com/mekhails/lab1/config1.txt");
        //Manager mng2 = new Manager("src/com/mekhails/lab1/config2.txt");

        //mng1.Run();
        //mng2.Run();
    }
}
