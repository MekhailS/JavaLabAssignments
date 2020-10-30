package com.mekhails.lab2;

import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class ByteWriter implements IWriter {

    private enum VocabularyWriter implements IVocabularyConfig, IVocabularySemantic
    {
        BUFFER_SIZE("buffer size", SemanticAnalyzer.Semantic.SIZE);

        VocabularyWriter(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        @Override
        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        private final String nameInConfig;
        private final SemanticAnalyzer.Semantic semantic;
    }

    @Override
    public RC setOutputStream(FileOutputStream fileOutputStream)
    {
        bos = new BufferedOutputStream(fileOutputStream);
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] buffer)
    {
        try
        {
            int i = 0;
            while (i*bufferSize + bufferSize <= buffer.length - 1)
            {
                bos.write(buffer, i*bufferSize, bufferSize);
                i += 1;
            }
            bos.write(buffer,i*bufferSize, buffer.length - i*bufferSize);
            bos.flush();
            return RC.CODE_SUCCESS;
        }
        catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.WRITER.name);
            return RC.CODE_FAILED_TO_WRITE;
        }
    }

    @Override
    public RC setConsumer(IExecutable o) { return RC.CODE_SUCCESS; }

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
        VocabularyWriter[] params = VocabularyWriter.values();
        ConfigReader configReader = ConfigReader.getConfigByVocabulary(params, cfgStream);

        if (configReader == null)
            return RC.CODE_CONFIG_GRAMMAR_ERROR;

        for (VocabularyWriter param: params)
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
    private IExecutable producer;

    private BufferedOutputStream bos;
}
