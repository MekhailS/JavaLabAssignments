package com.mekhails.lab2;

import javafx.util.Pair;
import ru.spbstu.pipeline.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class Manager {

    private enum VocabularyManager implements IVocabularyConfig, IVocabularySemantic
    {
        READER("reader", SemanticAnalyzer.Semantic.READER),
        INPUT_FILE("input file", SemanticAnalyzer.Semantic.FILE_IN),

        WORKER("worker", SemanticAnalyzer.Semantic.EXECUTOR),

        WRITER("writer", SemanticAnalyzer.Semantic.WRITER),
        OUTPUT_FILE("output file", SemanticAnalyzer.Semantic.FILE_OUT);

        VocabularyManager(String nameInConfig_, SemanticAnalyzer.Semantic semantic_)
            {nameInConfig = nameInConfig_; semantic = semantic_;}

        @Override
        public SemanticAnalyzer.Semantic getSemantic() { return semantic; }
        public String getNameInConfig() { return nameInConfig; }

        private final String nameInConfig;
        private final SemanticAnalyzer.Semantic semantic;
    }

    public Manager(String configFilename_)
    {
        configFilename = configFilename_;
    }

    public RC configureAndConstructPipeline()
    {
        try
        {
            FileInputStream cfgStream = new FileInputStream(configFilename);

            VocabularyManager[] params = VocabularyManager.values();
            ConfigReader configReader = ConfigReader.getConfigByVocabulary(params);

            if (configReader == null)
                return RC.CODE_CONFIG_GRAMMAR_ERROR;

            for (VocabularyManager param: params)
            {
                ArrayList<String> paramName = configReader.getParameter(param.nameInConfig);
                Object paramValue = SemanticAnalyzer.parseParam(paramName, param.semantic);

                if (paramValue == null && param.semantic != SemanticAnalyzer.Semantic.EMPTY)
                    return RC.CODE_CONFIG_SEMANTIC_ERROR;

                switch (param)
                {
                    case INPUT_FILE:
                        inFilename = (String)paramValue;
                        break;

                    case OUTPUT_FILE:
                        outFilename = (String)paramValue;
                        break;

                    case READER:
                        Pair<IReader, String> readerAndConfig = (Pair<IReader, String>)paramValue;
                        reader = readerAndConfig.getKey();
                        reader.setConfig(readerAndConfig.getValue());
                        break;

                    case WRITER:
                        Pair<IWriter, String> writerAndConfig = (Pair<IWriter, String>)paramValue;
                        writer = writerAndConfig.getKey();
                        writer.setConfig(writerAndConfig.getValue());
                        break;

                    case WORKER:
                    {
                        Pair<IExecutor, String>[] workersAndFilesArr = (Pair<IExecutor, String>[])paramValue;
                        if (workersAndFilesArr == null)
                            return RC.CODE_CONFIG_SEMANTIC_ERROR;

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
                return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;

            return linkEverything();
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        }
        catch (NullPointerException e){
            return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
        }
    }

    public RC run()
    {
        try
        {
            FileInputStream fis = new FileInputStream(inFilename);
            FileOutputStream fos = new FileOutputStream(outFilename);

            reader.setInputStream(fis);
            writer.setOutputStream(fos);

            RC code = reader.execute(null);

            fis.close();
            fos.close();

            return code;
        }
        catch (FileNotFoundException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.CONFIG.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        } catch (IOException e) {
            Log.LOGGER.log(Level.SEVERE, Log.ERROR.WRITER.name);
            return RC.CODE_INVALID_INPUT_STREAM;
        }
    }

    boolean isEverythingAvailable()
    {
        return  ((reader != null) && (writer != null) && (executors != null));
    }

    private RC linkEverything()
    {
        IPipelineStep[] pipelineSteps = new IPipelineStep[1 + executors.length + 1];
        pipelineSteps[0] = reader;
        System.arraycopy(executors, 0, pipelineSteps, 1, executors.length);
        pipelineSteps[pipelineSteps.length - 1] = writer;

        RC code = RC.CODE_SUCCESS;

        for (int i = 0; i < pipelineSteps.length; i++)
        {
            IPipelineStep step = pipelineSteps[i];
            if (i > 0)
            {
                code = step.setProducer(pipelineSteps[i - 1]);
                if (code != RC.CODE_SUCCESS)
                    return code;
            }
            if (i < pipelineSteps.length - 1)
            {
                code = step.setConsumer(pipelineSteps[i + 1]);
                if (code != RC.CODE_SUCCESS)
                    return code;
            }

        }
        return RC.CODE_SUCCESS;
    }

    private IReader reader;
    private IWriter writer;
    private IExecutor[] executors;

    private String inFilename;
    private String outFilename;
    private String configFilename;
}
