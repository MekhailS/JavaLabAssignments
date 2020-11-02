package com.mekhails.lab2alt.lab2;

import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.RC;

import java.io.*;
import java.util.logging.Level;

public class ByteWriter extends AConfigurable implements IWriter {

    /**
     * Vocabulary of Writer
     */
    private enum LexemeWriter implements ILexeme
    {
        BUFFER_SIZE("buffer size", SemanticAnalyzer.Semantic.SIZE);

        LexemeWriter(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        @Override
        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        private final String nameInConfig;
        private final SemanticAnalyzer.Semantic semantic;
    }

    @Override
    protected LexemeAndRule[] setOfRulesForVocabulary()
    {
        LexemeAndRule[] setRules =
                {
                        new LexemeAndRule(LexemeWriter.BUFFER_SIZE, paramVal -> {
                            bufferSize = (Integer)paramVal;
                            return RC.CODE_SUCCESS;
                        })
                };

        return setRules;
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
            FileInputStream cfgStream = new FileInputStream(s);

            return configure(cfgStream);
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        }
    }

    private int bufferSize;
    private IExecutable producer;

    private BufferedOutputStream bos;
}
