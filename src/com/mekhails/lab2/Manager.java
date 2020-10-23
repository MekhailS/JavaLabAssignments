package com.mekhails.lab2;

import javafx.util.Pair;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.IWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class Manager {

    private enum VocabularyManager {

        READER("reader", SemanticAnalyzer.Semantic.READER),
        INPUT_FILE("input file", SemanticAnalyzer.Semantic.FILE_IN),

        WORKER("worker", SemanticAnalyzer.Semantic.WORKER),

        WRITER("writer", SemanticAnalyzer.Semantic.WRITER),
        OUTPUT_FILE("output file", SemanticAnalyzer.Semantic.FILE_OUT);

        VocabularyManager(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        public final String nameInConfig;
        public final SemanticAnalyzer.Semantic semantic;

    }

    public Manager(String configFilename)
    {
        try
        {
            FileInputStream cfgStream = new FileInputStream(configFilename);

            VocabularyManager[] params = VocabularyManager.values();

            String[] paramsNamesInConfig = new String[params.length];
            for (int i = 0; i < params.length; i++)
                paramsNamesInConfig[i] = params[i].nameInConfig;

            ConfigReader configReader = new ConfigReader(paramsNamesInConfig);
            configReader.readConfig(cfgStream);

            if (!configReader.isConfigValid())
                return;

            for (VocabularyManager param: params)
            {
                ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
                Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

                if (paramValue == null)
                    return;

                switch (param)
                {
                    case INPUT_FILE:
                        reader.setInputStream((FileInputStream)paramValue);
                        break;

                    case OUTPUT_FILE:
                        writer.setOutputStream((FileOutputStream)paramValue);
                        break;

                    case READER:
                        reader = new ByteReader();
                        reader.setConfig((String)paramValue);
                        break;

                    case WRITER:
                        writer = new ByteWriter();
                        writer.setConfig((String)paramValue);
                        break;

                    case WORKER:
                    {
                        Pair<IExecutor, String>[] workersAndFilesArr = (Pair<IExecutor, String>[])paramValue;
                        executors = new IExecutor[workersAndFilesArr.length];
                        for (int i = 0; i<workersAndFilesArr.length; i++)
                        {
                            Pair<IExecutor, String> workerAndFile = workersAndFilesArr[i];
                            executors[i] = workerAndFile.getKey();
                            executors[i].setConfig(workerAndFile.getValue());
                        }
                        break;
                    }
                }
            }
            if (!isEverythingAvailable())
                return;
            LinkEverything();

        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
        }
    }

    public void Run()
    {
        while(true)
        {
            if (reader.execute(null) != 0)
                return;
        }
    }

    boolean isEverythingAvailable()
    {
        return  ((reader != null) && (writer != null) && (executors != null));
    }

    private int LinkEverything()
    {
        IExecutor[] executorsWReaderAndWriter = new IExecutor[1 + executors.length + 1];
        executorsWReaderAndWriter[0] = reader;
        System.arraycopy(executors, 0, executorsWReaderAndWriter, 1, executors.length);
        executorsWReaderAndWriter[executorsWReaderAndWriter.length - 1] = writer;

        for (int i = 0; i < executorsWReaderAndWriter.length; i++)
        {
            IExecutor executor = executorsWReaderAndWriter[i];
            executor.setConsumer(reader);
            if (i > 0)
                executor.setProducer(executorsWReaderAndWriter[i-1]);
            if (i < executorsWReaderAndWriter.length - 1)
                executor.setConsumer(executorsWReaderAndWriter[i+1]);

        }
        return 0;
    }

    private IReader reader;
    private IWriter writer;
    private IExecutor[] executors;
}
