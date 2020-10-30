package com.mekhails.lab2;

import javafx.util.Pair;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.IWriter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;

public class SemanticAnalyzer {

    public final static String propertiesDelimiter = ";";

    enum Semantic
    {
        EMPTY,
        FILE_IN,
        FILE_OUT,
        BOOL,
        SIZE,
        READER,
        WRITER,
        EXECUTOR
    }

    public static Object parseParam(ArrayList<String> parameter, Semantic parameterSemantic)
    {
        try
        {
            if (parameter == null)
                return null;

            Object res = null;

            switch(parameterSemantic)
            {
                case EMPTY:
                    break;

                case FILE_OUT:
                {
                    String outFilename = parameter.get(0);
                    File inFile = new File(outFilename);
                    if (!inFile.canWrite())
                        break;

                    res = outFilename;
                    break;
                }

                case FILE_IN:
                {
                    String inFilename = parameter.get(0);
                    File inFile = new File(inFilename);
                    if (!inFile.canRead())
                        break;

                    res = inFilename;
                    break;
                }

                case SIZE:
                {
                    Integer size = Integer.parseInt(parameter.get(0));
                    if (size < 0)
                        size = null;

                    res = size;
                    break;
                }

                case BOOL:
                {
                    String word = parameter.get(0);
                    if (word.equals("True"))
                        res = true;
                    else if (word.equals("False"))
                        res = false;
                    break;
                }

                case WRITER :
                {
                    String[] tokens = parameter.get(0).split(propertiesDelimiter);

                    String cfgFilename = tokens[1].trim();
                    File cfgFile = new File(cfgFilename);
                    if (!cfgFile.canWrite())
                        break;

                    String className = tokens[0].trim();
                    Class<?> writerClass = Class.forName(className);
                    Constructor<?> writerConstructor = writerClass.getConstructor(null);
                    IWriter writer = (IWriter) writerConstructor.newInstance(null);

                    res = new Pair<IWriter, String>(writer, cfgFilename);
                    break;
                }

                case READER:
                {
                    String[] tokens = parameter.get(0).split(propertiesDelimiter);

                    String cfgFilename = tokens[1].trim();
                    File cfgFile = new File(cfgFilename);
                    if (!cfgFile.canRead())
                        break;

                    String className = tokens[0].trim();
                    Class<?> readerClass = Class.forName(className);
                    Constructor<?> readerConstructor = readerClass.getConstructor(null);
                    IReader reader = (IReader) readerConstructor.newInstance(null);

                    res = new Pair<IReader, String>(reader, cfgFilename);
                    break;
                }

                case EXECUTOR:
                {
                    Pair<IExecutor, String>[] resArr = new Pair[parameter.size()];
                    for (int i = 0; i<parameter.size(); i++)
                    {
                        String[] tokens = parameter.get(i).split(propertiesDelimiter);

                        String cfgFilename = tokens[1].trim();

                        String className = tokens[0].trim();
                        Class<?> executorClass = Class.forName(className);
                        Constructor<?> executorConstructor = executorClass.getConstructor(null);
                        IExecutor executor = (IExecutor) executorConstructor.newInstance(null);

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
        catch (ClassNotFoundException | NoSuchMethodException
                | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
        return null;
    }
}
