package com.mekhails.lab1;

public class Main
{
    public static void main(String[] Args)
    {
        Manager mng1 = new Manager("config.txt");
        Manager mng2 = new Manager("config2.txt");

        mng1.Run();
        mng2.Run();
    }
}
