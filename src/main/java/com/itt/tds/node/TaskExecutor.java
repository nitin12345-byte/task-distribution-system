package com.itt.tds.node;

import com.itt.tds.core.model.TaskResult;
import java.io.File;

public interface TaskExecutor {

    public TaskResult execute(File file);
}
