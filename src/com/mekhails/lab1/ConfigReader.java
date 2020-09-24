package com.mekhails.lab1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader
{

    enum Vocabulary
    {
        INPUT_FILE("input file"),
        OUTPUT_FILE("output file"),
        BUFFER_SIZE("buffer size"),
        DELIMITER(":");

        Vocabulary(String str) { name = str;}

        private String name;
    }

    public ConfigReader(String configFilename_s)
    {
        configFilename = configFilename_s;
        readConfig();
    }

    public String getConfigFilename()
    {
        return configFilename;
    }

    public String getInputFilename()
    {
        return inputFilename;
    }

    public String getOutputFilename()
    {
        return outputFilename;
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    private void readConfig()
    {
        try
        {
            BufferedReader bufR = new BufferedReader(new FileReader(configFilename));

            for (String line = bufR.readLine(); line != null; line = bufR.readLine())
            {
                String[] tokens = line.split(Vocabulary.DELIMITER.name);

                if (tokens.length != 2)
                    throw new Exception("Not correct config line format");

                for (int i = 0; i< tokens.length; i++)
                {
                    String curToken = tokens[i].trim();

                    if (curToken.equalsIgnoreCase(Vocabulary.INPUT_FILE.name))
                        inputFilename = tokens[++i].trim();

                    else if (curToken.equalsIgnoreCase(Vocabulary.OUTPUT_FILE.name))
                        outputFilename = tokens[++i].trim();

                    else if (curToken.equalsIgnoreCase(Vocabulary.BUFFER_SIZE.name))
                        bufferSize = Integer.parseInt(tokens[++i].trim());
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: can not open config file");
        } catch (IOException e) {
            System.out.println("Error: can not read config file");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private String configFilename;
    private String inputFilename, outputFilename;
    private int bufferSize = 0;
}
