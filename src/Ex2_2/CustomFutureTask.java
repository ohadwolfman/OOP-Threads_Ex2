package Ex2_2;

import java.util.concurrent.FutureTask;

public class CustomFutureTask<V> extends FutureTask<V> implements Comparable<CustomFutureTask<V>>{

    private Task<V> task;

    public CustomFutureTask(Task<V> task) {
        super(task);
        this.task = task;
    }

    public Task<V> getTask() {
        return task;
    }

    @Override
    public int compareTo(CustomFutureTask<V> other) {
        return this.task.compareTo(other.task);
    }

}
