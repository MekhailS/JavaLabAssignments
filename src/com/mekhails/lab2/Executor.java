package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Executor implements IExecutor {

    private enum VocabularyExecutor {

        BUFFER_SIZE("buffer size", SemanticAnalyzer.Semantic.SIZE);

        VocabularyExecutor(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
        {nameInConfig = nameInConfig_; semantic = semantic_;}

        public final String nameInConfig;
        public final SemanticAnalyzer.Semantic semantic;

    }

    @Override
    public int setConsumer(Object o)
    {
        consumer = (IExecutor)o;
        return 0;
    }

    @Override
    public int setProducer(Object o)
    {
        producer = (IExecutor)o;
        return 0;
    }

    @Override
    public int execute(Object o)
    {
        return 0;
    }

    @Override
    public int setConfig(String s)
    {
        try
        {
            FileInputStream fis = new FileInputStream(s);
            configure(fis);
            return 0;
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
        return 1;
    }

    protected int configure(FileInputStream cfgStream)
    {
        VocabularyExecutor[] params = VocabularyExecutor.values();

        String[] paramsNamesInConfig = new String[params.length];
        for (int i = 0; i<params.length; i++)
            paramsNamesInConfig[i] = params[i].nameInConfig;

        ConfigReader configReader = new ConfigReader(paramsNamesInConfig);
        configReader.readConfig(cfgStream);

        if (!configReader.isConfigValid())
            return 1;

        for (VocabularyExecutor param: params)
        {
            ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
            Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

            if (paramValue == null)
                return 1;

            switch (param)
            {
                case BUFFER_SIZE:
                    bufferSize = (Integer)paramValue;
                    break;
            }
        }
        return 0;
    }

    protected int bufferSize;
    protected IExecutor consumer;
    protected IExecutor producer;
}
