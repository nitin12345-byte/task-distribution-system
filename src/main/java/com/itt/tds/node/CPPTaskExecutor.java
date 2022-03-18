package com.itt.tds.node;

import com.itt.tds.core.Constants;
import com.itt.tds.core.enums.TaskResultErrorCode;
import com.itt.tds.core.logging.LogManager;
import com.itt.tds.core.logging.Logger;
import com.itt.tds.core.model.TaskResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class CPPTaskExecutor implements TaskExecutor {

    private Logger logger;

    public CPPTaskExecutor() {
        logger = LogManager.getLogger(this.getClass().getName());
    }

    @Override
    public TaskResult execute(File file) {
        TaskResult taskResult = new TaskResult();

        try {

            logger.logInfo("execute", "CPPTaskexecutor is executing the java file");

            Process process = Runtime.getRuntime().exec("g++ " + file.getName(), null, new File(file.getParent()));
            process.waitFor();

            String error = getDataFromStream(process.getErrorStream());

            if (error.isEmpty()) {
                process = Runtime.getRuntime().exec(file.getParent() + "\\a.exe");
                boolean isProccessExecutedInTimeLimit = process.waitFor(10, TimeUnit.SECONDS);

                if (isProccessExecutedInTimeLimit) {
                    error = getDataFromStream(process.getErrorStream());
                    String output = getDataFromStream(process.getInputStream());

                    logger.logInfo("result", output);

                    if (error.isEmpty()) {
                        taskResult.setResult(output);

                    } else {
                        taskResult.setErrorCode(TaskResultErrorCode.RUN_TIME_ERROR.getValue());
                        taskResult.setErrorMessage(error);
                        logger.logInfo("execute", "Runtime error occured + \n" + error);
                    }
                } else {
                    process.destroyForcibly();
                    taskResult.setErrorCode(TaskResultErrorCode.EXECUTION_TIME_EXCEED_ERROR.getValue());
                    taskResult.setErrorMessage("Execution time exceed");
                    logger.logInfo("execute", "execution time exceed");
                }

            } else {
                taskResult.setErrorCode(TaskResultErrorCode.COMPILE_TIME_ERROR.getValue());
                taskResult.setErrorMessage(error);
                logger.logInfo("execute", "Compile time error occured + \n" + error);

            }

        } catch (Exception exception) {
            logger.logError("execute", "Unknown error occured", exception);
        }

        return taskResult;
    }

    private String getDataFromStream(InputStream errorStream) throws IOException {

        byte[] byteArray = errorStream.readAllBytes();

        if (byteArray.length > 0) {
            return new String(byteArray);
        }

        return Constants.EMPTY_STRING;
    }

}
