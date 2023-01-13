package Ex2_2;

import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolExecutor extends ThreadPoolExecutor{

    static int processors = Runtime.getRuntime().availableProcessors();
    private int [] priorityArray = new int [11];
    private int maxPriority = 100;

    public MyThreadPoolExecutor() {
        super(processors/2, processors-1, 300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
    }

    protected <V> RunnableFuture<V> newTaskFor(Task<V> task) {
        CustomFutureTask<V> ft = new CustomFutureTask<>(task);
        return ft;
    }

    public <V> Future<V> submit(Task<V> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<V> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }

    @Override
    protected void beforeExecute(Thread thread, Runnable run) {
        if (run instanceof CustomFutureTask) {
            CustomFutureTask cft = (CustomFutureTask)run;
            int priority = cft.getTask().getTaskPriority();
            priorityArray[priority]++;
            if(priority < maxPriority) {
                maxPriority = priority;
            }
        }
        super.beforeExecute(thread, run);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof CustomFutureTask) {
            CustomFutureTask cft = (CustomFutureTask)r;
            int priority = cft.getTask().getTaskPriority();
            priorityArray[priority]--;
            if(priority == maxPriority && priorityArray[priority] == 0) {
                int i = priority+1;
                while(i < 11 && priorityArray[i] == 0) {
                    i++;
                }
                maxPriority = i;
            }
        }
    }

    public int getMaxPriority() {
        return maxPriority;
    }

    public void gracefullyTerminate() {
        this.shutdown();
        try {
            if (!this.awaitTermination(60, TimeUnit.SECONDS)) {
                this.shutdownNow();
            }
        } catch (InterruptedException ex) {
            this.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}