package com.mekhails.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader
{

    final static String delimiter = ":";
    final static int numParameters = 3;

    enum Vocabulary
    {
        INPUT_FILE("input file", 0),
        OUTPUT_FILE("output file", 1),
        BUFFER_SIZE("buffer size", 2);

        Vocabulary(String str, int i_) { name = str; i = i_;}

        public final String name;
        public final int i;
    }

    public ConfigReader(String configFilename_s)
    {
        configFilename = configFilename_s;
    }

    public String getConfigFilename() {
        return configFilename;
    }

    public String[] readConfig()
    {
        try
        {
            String[] params = new String[numParameters];

            BufferedReader bufR = new BufferedReader(new FileReader(configFilename));

            for (String line = bufR.readLine(); line != null; line = bufR.readLine())
            {
                String[] tokens = line.split(delimiter);

                if (tokens.length != 2) {
                    System.out.println("Error: Not correct config line format");
                    return null;
                }

                for (int i = 0; i < tokens.length; i++)
                {
                    String curToken = tokens[i].trim();

                    if (curToken.equalsIgnoreCase(Vocabulary.INPUT_FILE.name))
                        params[Vocabulary.INPUT_FILE.i] = tokens[++i].trim();

                    else if (curToken.equalsIgnoreCase(Vocabulary.OUTPUT_FILE.name))
                        params[Vocabulary.OUTPUT_FILE.i] = tokens[++i].trim();

                    else if (curToken.equalsIgnoreCase(Vocabulary.BUFFER_SIZE.name))
                        params[Vocabulary.BUFFER_SIZE.i] = tokens[++i].trim();
                }
            }
            return params;
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: can not open config file");
        } catch (IOException e) {
            System.out.println("Error: can not read config file");
        }
        return null;
    }

    String configFilename;
}
