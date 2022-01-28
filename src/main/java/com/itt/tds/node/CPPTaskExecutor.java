package com.itt.tds.node;

import com.itt.tds.core.model.TaskResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CPPTaskExecutor implements TaskExecutor {

    @Override
    public TaskResult execute(File file) {
        TaskResult taskResult = new TaskResult();

        try {

            Process process = Runtime.getRuntime().exec("javac " + file.getName(), null, new File(file.getParent()));
            process.waitFor();

            String error = getDataFromStream(process.getErrorStream());

            if (error.isEmpty()) {
                process = Runtime.getRuntime().exec("java " + file.getName(), null, new File(file.getParent()));
                process.waitFor();

                error = getDataFromStream(process.getErrorStream());
                String output = getDataFromStream(process.getInputStream());
                System.out.println("Result : " + output);

                if (error.isEmpty()) {
                    taskResult.setResult(output);

                } else {
                    taskResult.setErrorCode(1);
                    taskResult.setErrorMessage(error);
                }

            } else {
                taskResult.setErrorCode(2);
                taskResult.setErrorMessage(error);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return taskResult;
    }

    private String getDataFromStream(InputStream errorStream) throws IOException {

        byte[] byteArray = errorStream.readAllBytes();

        if (byteArray.length > 0) {
            return new String(byteArray);
        }

        return "";
    }
}
