/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itt.tds.node;

import com.itt.tds.core.model.TaskResult;
import java.io.File;

/**
 *
 * @author nitin.jangid
 */
public class PythonTaskExecutor implements TaskExecutor {

    public PythonTaskExecutor() {
    }

    @Override
    public TaskResult execute(File file) {
        return new TaskResult();
    }

}
