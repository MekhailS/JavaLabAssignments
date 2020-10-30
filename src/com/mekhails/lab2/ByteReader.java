package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.RC;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class ByteReader implements IReader {

    private enum VocabularyReader implements IVocabularyConfig, IVocabularySemantic
    {
        BUFFER_SIZE("buffer size", SemanticAnalyzer.Semantic.SIZE);

        VocabularyReader(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        @Override
        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        private final String nameInConfig;
        private final SemanticAnalyzer.Semantic semantic;
    }

    @Override
    public RC setInputStream(FileInputStream fileInputStream)
    {
        bis = new BufferedInputStream(fileInputStream);
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] b)
    {
        try
        {
            int bytesRead = 0;
            while (true)
            {
                byte[] buffer = new byte[bufferSize];
                bytesRead = bis.read(buffer, 0, bufferSize);

                if (bytesRead == -1)
                    return RC.CODE_SUCCESS;

                if (bytesRead != buffer.length)
                    buffer = Arrays.copyOfRange(buffer, 0, bytesRead);

                RC code = consumer.execute(buffer);
                if (code != RC.CODE_SUCCESS)
                    return code;
            }
        }
        catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.READER.name);
            return RC.CODE_FAILED_TO_READ;
        }
    }

    @Override
    public RC setConsumer(IExecutable o)
    {
        consumer = o;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable o) { return RC.CODE_SUCCESS; }

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
        VocabularyReader[] params = VocabularyReader.values();
        ConfigReader configReader = ConfigReader.getConfigByVocabulary(params, cfgStream);

        if (configReader == null)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;

        for (VocabularyReader param: params)
        {
            ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
            Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

            if (paramValue == null && param.semantic != SemanticAnalyzer.Semantic.EMPTY)
                return RC.CODE_CONFIG_SEMANTIC_ERROR;

            switch (param)
            {
                case BUFFER_SIZE:
                    bufferSize = (Integer)paramValue;
                    break;
            }
        }
        return RC.CODE_SUCCESS;
    }

    private int bufferSize;
    private IExecutable consumer;

    private BufferedInputStream bis;
}
