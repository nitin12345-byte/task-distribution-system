package com.itt.tds.distributor;

import com.itt.tds.core.model.Task;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Queue {

    private BlockingQueue blockingQueue;
    private static Queue queue = null;

    private Queue() {
        blockingQueue = new ArrayBlockingQueue(1024);
    }

    public static Queue getInstance() {
        if (queue == null) {
            queue = new Queue();
        }

        return queue;
    }

    public void put(Task task) throws InterruptedException {
        blockingQueue.put(task);
    }

    public Task poll() {
        return (Task) blockingQueue.poll();
    }

    public Task peek() {
        return (Task) blockingQueue.peek();
    }
}
