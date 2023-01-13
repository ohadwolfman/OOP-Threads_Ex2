package Ex2_2;

import java.util.concurrent.Callable;

public class Task<V> implements Callable<V>, Comparable<Task<V>> {
    private Callable<V> task;
    private TaskType taskType;

    public enum TaskType {
        COMPUTATIONAL(1){
            @Override
            public String toString() {
                return "Computational Task";
            }
        },
        IO(2){
            @Override
            public String toString(){
                return "IO-Bound Task";
            }
        },
        OTHER(3){
            @Override
            public String toString(){
                return "Unknown Task";
            }
        };

        private int typePriority;

        private TaskType(int priority){
            if (validatePriority(priority)) {
                typePriority = priority;
            } else {
                throw new IllegalArgumentException("Priority is not an integer");
            }
        }

        public void setPriority(int priority){
            if(validatePriority(priority)) {
                this.typePriority = priority;
            } else {
                throw new IllegalArgumentException("Priority is not an integer");
            }
        }

        public int getPriorityValue(){
            return typePriority;
        }

        public TaskType getType(){
            return this;
        }


        /**
         * priority is represented by an integer value, ranging from 1 to 10
         * @param priority
         * @return whether the priority is valid or not
         */
        private static boolean validatePriority(int priority){
            if (priority < 1 || priority >10) return false;
            return true;
        }
    }

    private Task(Callable<V> task, TaskType taskType) {
        this.task = task;
        this.taskType = taskType;
    }

    public static <V> Task<V> createTask(Callable<V> callable) {
        TaskType taskType = TaskType.OTHER;
        taskType.setPriority(5);
        return Task.createTask(callable, taskType);
    }

    public static <V> Task<V> createTask(Callable<V> callable, TaskType taskType) {
        Task<V> newTask = new Task<V>(callable, taskType);
        return newTask;
    }

    public Callable<V> getTask() {
        return task;
    }

    public void setTask(Callable<V> task) {
        this.task = task;
    }

    public int getTaskPriority() {
        return this.taskType.getPriorityValue();
    }

    public void setTaskPriority(int priority) {
        this.taskType.setPriority(priority);
    }

    @Override
    public V call() throws Exception {
        return task.call();
    }

    public Runnable asRunnable() {
        return () -> {
            try {
                task.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public int compareTo(Task<V> task) {
        Integer thisPriority = this.getTaskPriority();
        Integer otherPriority = task.getTaskPriority();
        return thisPriority.compareTo(otherPriority);
    }
}