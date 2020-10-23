package com.mekhails.lab2;

import javafx.util.Pair;
import ru.spbstu.pipeline.IExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;

public class SemanticAnalyzer {

    public final static String propertiesDelimiter = ";";

    enum Semantic
    {
        FILE_IN,
        FILE_OUT,
        SIZE,
        READER,
        WRITER,
        WORKER;
    }

    public static Object parseParam(ArrayList<String> parameter, Semantic parameterSemantic)
    {
        try
        {
            Object res = null;

            switch(parameterSemantic)
            {
                case FILE_IN:
                    res = new FileInputStream(parameter.get(0));
                    break;

                case FILE_OUT:
                    res = new FileOutputStream(parameter.get(0));
                    break;

                case SIZE:
                {
                    Integer size = Integer.parseInt(parameter.get(0));
                    if (size < 0)
                        size = null;

                    res = size;
                    break;
                }

                case WRITER :
                {
                    String cfgFilename = (String)parameter.get(0);
                    File cfgFile = new File(cfgFilename);

                    if (!cfgFile.canWrite())
                        break;
                    res = cfgFilename;
                    break;
                }

                case READER:
                {
                    String cfgFilename = (String)parameter.get(0);
                    File cfgFile = new File(cfgFilename);

                    if (!cfgFile.canRead())
                        break;
                    res = cfgFilename;
                    break;
                }

                case WORKER:
                {
                    Pair<IExecutor, String>[] resArr = new Pair[parameter.size()];
                    for (int i = 0; i<parameter.size(); i++)
                    {
                        String[] tokens = parameter.get(i).split(propertiesDelimiter);

                        String cfgFilename = tokens[1].trim();

                        IExecutor executor = new Reverser();
                        File cfgFile = new File(cfgFilename);

                        if (!cfgFile.canRead())
                            break;

                        resArr[i] = new Pair<IExecutor, String>(executor, cfgFilename);
                    }
                    res = resArr;
                    break;
                }
            }
            return res;
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
        return null;
    }
}
