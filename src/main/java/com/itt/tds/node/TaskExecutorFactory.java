package com.itt.tds.node;

public class TaskExecutorFactory {

    public static TaskExecutor getTaskExecutor(String fileName) {

        if (fileName.endsWith(".java")) {
            return new JavaTaskExecutor();
        } else if (fileName.endsWith(".cpp")) {
            return new CPPTaskExecutor();
        } else if (fileName.endsWith(".py")) {
            return new PythonTaskExecutor();
        } else if (fileName.endsWith(".c")) {
            return new CTaskExecutor();
        }

        return null;
    }
}
