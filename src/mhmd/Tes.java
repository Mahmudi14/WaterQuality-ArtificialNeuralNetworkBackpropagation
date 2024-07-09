package mhmd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tes {

    public static void main(String[] args) throws IOException {
        BacaData file = new BacaData();
        double[][] data = file.data;
        File path = new File("D:/SourceCode/Java/WaterQuality_Backpropagation/src/dataset/waterQuality2.csv");
        FileWriter fw = new FileWriter(path);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
               double v = data[i][j];
               fw.append(String.valueOf(v)+"; ");
            }
            fw.append("\n");
        }
        fw.close();

    }

}
