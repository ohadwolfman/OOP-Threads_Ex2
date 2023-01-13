package Ex2_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.Callable;

class LineCounter implements Callable<Integer> {

    private String fileName;

    public LineCounter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Integer call() throws Exception {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while(reader.readLine() != null) {
                lines++;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

}
