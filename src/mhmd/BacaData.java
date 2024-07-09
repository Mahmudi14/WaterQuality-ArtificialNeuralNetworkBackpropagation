package mhmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public final class BacaData {

    double[][] dataDouble;
    String file = "D:/SourceCode/Java/WaterQuality_Backpropagation/src/dataset/waterQuality1.csv";
    final int percentDataTraining = 80;
    final int percentDataTesting = 20;
    int numDataTraining = 0;
    int numDataTesting = 0;
    int numData = 0;
    double[][] dataTesting;
    double[][] dataTraining;
    double[][] data;

    public BacaData() {
        try {
            bacaFile();
        } catch (FileNotFoundException ex) {
        }
    }

    public void bacaFile() throws FileNotFoundException {
        File path = new File(this.file);
        Scanner sc = new Scanner(path);
        sc.nextLine();//Skip header
        ArrayList<double[]> isiFile = new ArrayList<>();
        while (sc.hasNextLine()) {
            String isi = sc.nextLine();
            String[] arrisi = isi.split(",");
            double[] arrIsiDouble = new double[arrisi.length];
            boolean add = true;
            for (int i = 0; i < arrisi.length; i++) {
                if (arrisi[i].equalsIgnoreCase("#NUM!")) {
                    add = false;
                    break;
                }
                arrIsiDouble[i] = Double.parseDouble(arrisi[i]);
            }
            if (add) {
                isiFile.add(arrIsiDouble);
            }
        }
        this.dataDouble = new double[isiFile.size()][];
        for (int i = 0; i < isiFile.size(); i++) {
            this.dataDouble[i] = isiFile.get(i);
        }

        double[] min = new double[dataDouble[0].length];
        double[] max = new double[dataDouble[0].length];
        for (int j = 0; j < dataDouble[0].length; j++) {
            min[j] = Double.MAX_VALUE;
            max[j] = Double.MIN_VALUE;
        }

        for (int i = 0; i < dataDouble.length; i++) {
            for (int j = 0; j < dataDouble[i].length; j++) {
                double value = dataDouble[i][j];
                if (value < min[j]) {
                    min[j] = value;
                }
                if (value > max[j]) {
                    max[j] = value;
                }
            }
        }

        data = new double[dataDouble.length][dataDouble[0].length];
        for (int i = 0; i < dataDouble.length; i++) {
            for (int j = 0; j < dataDouble[i].length; j++) {
                double value = dataDouble[i][j];
                double normalValue = (value - min[j]) / (max[j] - min[j]);
                data[i][j] = normalValue;
            }
        }

        if (data != null) {
            double totalPercent = percentDataTesting + percentDataTraining;
            numData = data.length;
            numDataTraining = (int) (numData * (percentDataTraining / totalPercent));
            numDataTesting = (int) (numData * (percentDataTesting / totalPercent));
        }

        dataTraining = new double[numDataTraining][];
        dataTesting = new double[numDataTesting][];

        for (int i = 0; i < dataTraining.length; i++) {
            dataTraining[i] = data[i];
        }

        for (int i = 0; i < dataTesting.length; i++) {
            dataTesting[i] = data[i + dataTraining.length];
        }

    }
}
