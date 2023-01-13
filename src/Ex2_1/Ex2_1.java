package Ex2_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Ex2_1 {

    String currentPath;

    public Ex2_1(String currentPath) {
        this.currentPath = currentPath;
    }

    public static String[] createTextFiles(int n, int seed, int bound) throws IOException {
        String[] fileNames = new String[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(bound);
            try {
                String fName = "file_" + (i + 1);
                fileNames[i] = fName;
                FileWriter file = new FileWriter(fName + ".txt");
                for (int j = 1; j <= x; j++) {
                    file.write("This is line number " + j + "\n");
                }
                file.close();
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
        return fileNames;
    }

    public static int getNumOfLines(String[] fileNames) throws IOException {
        int lines = 0;
        String currentPath = new java.io.File(".").getCanonicalPath();
        for (int i = 0; i < fileNames.length; i++) {
            String fName = currentPath + "\\" + fileNames[i] + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(fName));
            while (reader.readLine() != null)
                lines++;
            reader.close();
        }
        return lines;
    }

    public int getNumOfLinesThreads(String[] fileNames) throws IOException, InterruptedException {
        int totalLines = 0;
        String currentPath = new java.io.File(".").getCanonicalPath();
        for (int i = 0; i < fileNames.length; i++) {
            String fName = currentPath + "\\" + fileNames[i] + ".txt";
            MyThread thread = new MyThread(fName);
            thread.start();
            thread.join();
            totalLines += thread.getTotalRows();
        }
        return totalLines;
    }

    public int getNumOfLinesThreadPool(String[] fileNames) throws InterruptedException, IOException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(fileNames.length);
        int totalNumOfLines = 0;
        String currentPath = new java.io.File(".").getCanonicalPath();
        for (int i = 0; i < fileNames.length; i++) {
            String fName = currentPath + "\\" + fileNames[i] + ".txt";
            Future<Integer> future = executor.submit(new LineCounter(fName));
            totalNumOfLines += future.get();
        }
        executor.shutdown();
        return totalNumOfLines;
    }


    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        String currentPath = new java.io.File(".").getCanonicalPath();
        //System.out.println("Current dir:" + currentPath);

        Ex2_1 ex2_1 = new Ex2_1(currentPath);

        String[] ans = Ex2_1.createTextFiles(10, 2, 1000000);
        System.out.println(Arrays.toString(ans));

        long start = System.currentTimeMillis();
        System.out.println("1: " + Ex2_1.getNumOfLines(ans));
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;
        System.out.println("it took " + elapsedTime);

        long start2 = System.currentTimeMillis();
        int answer = ex2_1.getNumOfLinesThreads(ans);
        System.out.println("2: " + answer);
        long end2 = System.currentTimeMillis();
        long elapsedTime2 = end2 - start2;
        System.out.println("the second took " + elapsedTime2);

        long start3 = System.currentTimeMillis();
        int answer3 = ex2_1.getNumOfLinesThreadPool(ans);
        System.out.println("3: " + answer3);
        long end3 = System.currentTimeMillis();
        long elapsedTime3 = end3 - start3;
        System.out.println("the third took " + elapsedTime3);

    }
}
