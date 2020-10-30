package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.RC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Reverser implements IExecutor {

    private enum VocabularyReverser implements IVocabularyConfig, IVocabularySemantic
    {
        REVERSING("enable reversing", SemanticAnalyzer.Semantic.BOOL);

        VocabularyReverser(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
        {nameInConfig = nameInConfig_; semantic = semantic_;}

        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        public final String nameInConfig;
        public final SemanticAnalyzer.Semantic semantic;
    }

    @Override
    public RC execute(byte[] buffer)
    {
        if (enableReversing)
            reverseBitsInBuffer(buffer);

        return(consumer.execute(buffer));
    }

    @Override
    public RC setConsumer(IExecutable o)
    {
        consumer = o;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable o)
    {
        producer = o;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String s)
    {
        try
        {
            FileInputStream fis = new FileInputStream(s);
            return configure(fis);
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        }
    }

    private RC configure(FileInputStream cfgStream)
    {
        VocabularyReverser[] params = VocabularyReverser.values();

        ConfigReader configReader = ConfigReader.getConfigByVocabulary(params, cfgStream);

        if (configReader == null)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;

        for (VocabularyReverser param: params)
        {
            ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
            Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

            if (paramValue == null && param.semantic != SemanticAnalyzer.Semantic.EMPTY)
                return RC.CODE_CONFIG_SEMANTIC_ERROR;

            switch (param)
            {
                case REVERSING:
                    enableReversing = (Boolean)paramValue;
                    break;
            }
        }
        return RC.CODE_CONFIG_SEMANTIC_ERROR;
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

    private IExecutable consumer;
    private IExecutable producer;
    boolean enableReversing;
}
