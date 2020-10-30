package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Reverser implements IExecutor {

    private enum VocabularyReverser {

        NONE("default", SemanticAnalyzer.Semantic.EMPTY);

        VocabularyReverser(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
        {nameInConfig = nameInConfig_; semantic = semantic_;}

        public final String nameInConfig;
        public final SemanticAnalyzer.Semantic semantic;

    }

    @Override
    public int execute(Object o)
    {
        byte[] buffer = (byte[])o;

        reverseBitsInBuffer(buffer);

        return(consumer.execute(buffer));
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
    public int setConfig(String s)
    {
        try
        {
            FileInputStream fis = new FileInputStream(s);
            return configure(fis);
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
        return 1;
    }

    private int configure(FileInputStream cfgStream)
    {
        VocabularyReverser[] params = VocabularyReverser.values();

        String[] paramsNamesInConfig = new String[params.length];
        for (int i = 0; i < params.length; i++)
            paramsNamesInConfig[i] = params[i].nameInConfig;

        ConfigReader configReader = new ConfigReader(paramsNamesInConfig);
        configReader.readConfig(cfgStream);

        if (!configReader.isConfigValid())
            return 1;

        for (VocabularyReverser param: params)
        {
            ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
            Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

            if (paramValue == null)
                return 1;

            switch (param)
            {
                case NONE:
                    break;
            }
        }
        return 0;
    }

    private void reverseBitsInBuffer(byte[] buffer)
    {
        reverseBytesInBuffer(buffer);
        for (int i = 0; i < buffer.length; i++)
        {
            buffer[i] = reverseBitsInByte(buffer[i]);
        }
    }

    private void reverseBytesInBuffer(byte[] buffer)
    {
        for (int i = 0; i < buffer.length/2; i++)
        {
            byte tmp = buffer[i];
            buffer[i] = buffer[buffer.length - 1 - i];
            buffer[buffer.length - 1 - i] = tmp;
        }
    }

    private byte reverseBitsInByte(byte x)
    {
        byte res = 0;
        for (int i = 0; i < 8; i++)
        {
            res <<= 1;
            res |= ( (x >> i) & 1);
        }
        return res;
    }


    private IExecutor consumer;
    private IExecutor producer;
}
