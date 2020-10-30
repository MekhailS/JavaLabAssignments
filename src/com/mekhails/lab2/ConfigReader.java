package com.mekhails.lab2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class ConfigReader
{
    final static String delimiter = ":";

    public static ConfigReader getConfigByVocabulary(IVocabularyConfig[] params, FileInputStream cfgStream)
    {
        String[] paramsNamesInConfig = new String[params.length];
        for (int i = 0; i<params.length; i++)
            paramsNamesInConfig[i] = params[i].getNameInConfig();

        ConfigReader configReader = new ConfigReader(paramsNamesInConfig);
        if (!configReader.readConfig(cfgStream))
            return null;

        if (!configReader.isConfigValid())
            return null;

        return configReader;
    }

    private ConfigReader(String[] allowedConfigParamsNames_) { allowedConfigParamsNames = allowedConfigParamsNames_; }

    private boolean readConfig(FileInputStream cfgStream)
    {
        try
        {
            if (allowedConfigParamsNames == null)
                return false;

            BufferedReader bufR = new BufferedReader(new InputStreamReader(cfgStream));

            for (String line = bufR.readLine(); line != null; line = bufR.readLine())
            {
                if (line.equals(""))
                    continue;
                String[] tokens = line.split(delimiter);

                if (tokens.length < 2) {
                    Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
                    return false;
                }

                for (int i = 0; i < tokens.length; i++)
                {
                    String curToken = tokens[i].trim();

                    if (Arrays.asList(allowedConfigParamsNames).contains(curToken))
                    {
                        if (params.containsKey(curToken))
                        {
                            ArrayList<String> parameterNames = params.get(curToken);
                            parameterNames.add(tokens[++i].trim());
                        }
                        else
                        {
                            ArrayList<String> parameterNames = new ArrayList<>();
                            parameterNames.add(tokens[++i].trim());

                            params.put(curToken, parameterNames);
                        }
                    }
                }
            }
            if (params.size() != allowedConfigParamsNames.length)
                return false;
            return true;
        } catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
        return false;
    }

    public boolean isConfigValid()
    {
        return (params != null && allowedConfigParamsNames != null && params.size() == allowedConfigParamsNames.length);
    }

    public ArrayList<String> getParameter(String parameterName)
    {
        return params.get(parameterName);
    }

    private HashMap<String, ArrayList<String>> params = new HashMap<>();
    private String[] allowedConfigParamsNames;
}
