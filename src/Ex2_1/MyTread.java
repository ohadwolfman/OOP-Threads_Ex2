package Ex2_1;

import java.io.BufferedReader;
import java.io.FileReader;

class MyThread extends Thread {

    String fileName;
    int fileLines;

    public MyThread(String fileName) {
        this.fileName = fileName;
        this.fileLines = 0;
    }

    public int getTotalRows() {
        return fileLines;
    }

    public void run() {
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
        this.fileLines += lines;
    }
}
