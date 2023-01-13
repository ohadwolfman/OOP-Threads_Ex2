package Ex2_2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import Ex2_2.Task.*;

public class CustomExecutor {

    MyThreadPoolExecutor executor;

    public CustomExecutor() {
        executor = new MyThreadPoolExecutor();
    }

    private <V>Future<V> submitToExecutor(Task<V> task){
        Future<V> future = null;
        try {
            future = executor.submit(task);
        } catch (Exception e) {
            executor.gracefullyTerminate();
        }
        return future;
    }

    public <V>Future<V> submit(Task<V> task) {
        return submitToExecutor(task);
    }

    public <V>Future<V> submit(Callable<V> callable) {
        Task<V> task = Task.createTask(callable);
        return submitToExecutor(task);
    }

    public <V>Future<V> submit(Callable<V> callable, TaskType type) {
        Task<V> task = Task.createTask(callable, type);
        return submitToExecutor(task);
    }

    public int getCurrentMax(){
        return executor.getMaxPriority();
    }

    public void gracefullyTerminate() {
        executor.gracefullyTerminate();
    }

    /*public static void main(String[] args) throws InterruptedException, ExecutionException {

        CustomExecutor executor = new CustomExecutor();

        for(int j = 1; j < 10; j++) {
            Task<Integer> task = Task.createTask(()->{
                System.out.println("Other triggered successfully");
                Thread.sleep(3000);
                int sum = 1;
                for (int i = 1; i <= 10; i++) {
                    sum *= i;
                }
                System.out.println("Other completed successfully");
                return sum;
            }, TaskType.OTHER);

            Task<String> task2 = Task.createTask(()->{
                int sum = 1;
                System.out.println("Computational triggered successfully");
                Thread.sleep(500);
                for (int i = 1; i <= 10; i++) {
                    sum *= i;
                }
                System.out.println("Computational completed�successfully");
                return "sum="+sum;
            }, TaskType.COMPUTATIONAL);

//			System.out.println(task.getTaskPriority());
//			System.out.println(task2.getTaskPriority());

            Future<Integer> sumTask = executor.submit(task);
            Future<String> sumTask2 = executor.submit(task2);

//			System.out.println(sumTask.get());
//			System.out.println(sumTask2.get());
        }

        executor.gracefullyTerminate();
    }*/
}