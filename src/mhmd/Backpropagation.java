package mhmd;

import java.util.Random;

public class Backpropagation {

    public static void main(String[] args) {
        BacaData bc = new BacaData();
        int MAX_EPOCH = 1000;
        int numInput = 1;
        int numHidden = 9;
        int numOutput = 1;
        double alpha = 0.1;
        double treshold = 0;
        double epsilonThreshold = 0.0000001;

        double[][] bobotV = null;
        double[][] bobotW = null;

        double[][] dataTraining = bc.dataTraining;

        if (dataTraining != null && dataTraining.length > 0) {
            numInput = dataTraining[0].length;
            double[][] V = new double[numInput][numHidden];
            double[][] W = new double[numHidden + 1][numOutput];

            Random r = new Random();

            for (int i = 0; i < V.length; i++) {
                for (int j = 0; j < V[i].length; j++) {
                    V[i][j] = r.nextDouble();
                }
            }
            for (int i = 0; i < W.length; i++) {
                for (int j = 0; j < W[i].length; j++) {
                    W[i][j] = r.nextDouble();
                }
            }

            boolean isConvergen = false;
            int epoch = 1;

            while (epoch <= MAX_EPOCH && !isConvergen) {
                int numYsamadenganT = 0;
                for (int t = 0; t < dataTraining.length; t++) {
                    numInput = dataTraining[t].length;
                    double[] T = new double[numOutput];
                    T[0] = dataTraining[t][dataTraining[0].length - 1];

                    double[] X = new double[numInput];
                    for (int i = 0; i < numInput - 1; i++) {
                        X[i] = dataTraining[t][i];
                    }
                    X[numInput - 1] = 1;

                    //---------------------------------------------------------------
                    //feedfordward
                    //--------------------------------------------------------------
                    //Hitung Znet
                    double[] Znet = new double[numHidden];
                    for (int j = 0; j < Znet.length; j++) {
                        double sum = 0;
                        for (int i = 0; i < Znet.length; i++) {
                            double XiVij = X[i] * V[i][j];
                            sum += XiVij;
                        }
                        Znet[j] = sum;

                    }

                    //Hitung Z
                    double[] Z = new double[numHidden + 1];
                    for (int j = 0; j < numHidden; j++) {
                        Z[j] = 1.0 / (1.0 + Math.exp(-Znet[j]));
                    }
                    Z[numHidden] = 1;

                    //hitung Ynet
                    double[] Ynet = new double[numOutput];
                    for (int k = 0; k < numOutput; k++) {
                        double sum = 0;
                        for (int j = 0; j < Z.length; j++) {
                            double ZiWjk = Z[j] * W[j][k];
                            sum += ZiWjk;
                        }
                        Ynet[k] = sum;
                    }

                    //Hitung Y
                    double[] Y = new double[numOutput];
                    for (int k = 0; k < numOutput; k++) {
                        Y[k] = 1.0 / (1.0 + Math.exp(-Ynet[k]));
                    }

                    //update threshold
                    for (int k = 0; k < numOutput; k++) {
                        if (T[k] >= 1 && treshold > Y[k]) {
                            treshold = Y[k] - epsilonThreshold;
                        } else if (T[k] <= 0 && treshold < Y[k]) {
                            treshold = Y[k] + epsilonThreshold;
                        }
                    }

                    //Bakpropagation
                    int YT = 0;
                    //hitung faktor eror dilayer output
                    double[] doY = new double[numOutput];
                    for (int k = 0; k < numOutput; k++) {
                        doY[k] = (T[k] - Y[k]) * Y[k] * (1 - Y[k]);

                        if (Y[k] == T[k]) {
                            YT++;
                        }
                    }

                    if (YT == T.length) {
                        numYsamadenganT++;
                    }

                    //Hitung deltaW
                    double[][] deltaW = new double[numHidden + 1][numOutput];
                    for (int j = 0; j < deltaW.length; j++) {
                        for (int k = 0; k < deltaW[j].length; k++) {
                            deltaW[j][k] = alpha * doY[k] * Z[j];
                        }
                    }

                    //hitung doNetY
                    double[] doYnet = new double[numHidden];
                    for (int j = 0; j < numHidden; j++) {
                        double sum = 0;
                        for (int k = 0; k < numOutput; k++) {
                            double doYkWjk = doY[k] * W[j][k];
                            sum += doYkWjk;
                        }
                        doYnet[j] = sum;
                    }

                    //Hitung doZ
                    double[] doZ = new double[numHidden];
                    for (int j = 0; j < numHidden; j++) {
                        doZ[j] = doYnet[j] * Z[j] * (1 - Z[j]);
                    }

                    //Delta V
                    double[][] deltaV = new double[numInput][numHidden];
                    for (int i = 0; i < numInput; i++) {
                        for (int j = 0; j < numHidden; j++) {
                            deltaV[i][j] = alpha * doZ[j] * X[i];
                        }
                    }

                    //--------------------------------------
                    //update Bobot
                    for (int i = 0; i < W.length; i++) {
                        for (int j = 0; j < W[i].length; j++) {
                            W[i][j] = W[i][j] + deltaW[i][j];
                        }
                    }

                    for (int i = 0; i < V.length; i++) {
                        for (int j = 0; j < V[i].length; j++) {
                            V[i][j] = V[i][j] + deltaV[i][j];
                        }
                    }
                }
                if (numYsamadenganT == dataTraining.length) {
                    isConvergen = true;
                }

                epoch++;
            }
            bobotW = W;
            bobotV = V;
        }

        //Proses testing
        int nTrue = 0;
        double[][] datatesting = bc.dataTesting;
        double result[][] = new double[datatesting.length][2];

        if (datatesting.length > 0) {
            for (int t = 0; t < datatesting.length; t++) {
                numInput = datatesting[t].length;
                double[] T = new double[numOutput];
                T[0] = datatesting[t][datatesting[0].length - 1];

                double[] X = new double[numInput];
                for (int i = 0; i < numInput - 1; i++) {
                    X[i] = datatesting[t][i];
                }
                X[numInput - 1] = 1.0;

                //--------------------------------------------------
                //feedforward
                double[] Znet = new double[numHidden];
                for (int j = 0; j < numHidden; j++) {
                    double sum = 0;
                    for (int i = 0; i < numInput; i++) {
                        double XiVij = X[i] * bobotV[i][j];
                        sum += XiVij;
                    }
                    Znet[j] = sum;
                }

                double[] Z = new double[numHidden + 1];
                for (int j = 0; j < numHidden; j++) {
                    Z[j] = 1.0 / (1.0 + Math.exp(-Znet[j]));
                }
                Z[numHidden] = 1.0;

                double[] Ynet = new double[numOutput];
                for (int k = 0; k < numOutput; k++) {
                    double sum = 0;
                    for (int j = 0; j < Z.length; j++) {
                        double ZjWjk = Z[j] * bobotW[j][k];
                        sum += ZjWjk;
                    }
                    Ynet[k] = sum;
                }

                double[] Y = new double[numOutput];
                for (int k = 0; k < numOutput; k++) {
                    Y[k] = 1.0 / (1.0 + Math.exp(-Ynet[k]));
                }

                double[] Ytreshold = new double[numOutput];
                //double threshold = 0.91;
                for (int k = 0; k < numOutput; k++) {
                    if (Y[k] >= treshold) {
                        Ytreshold[k] = 1;
                    } else {
                        Ytreshold[k] = 0;
                    }
                }

                //Trace Result
//                System.out.print("data_testing-" + t + " Target - Prediksi ");
                for (int k = 0; k < numOutput; k++) {
                    result[t][0] = T[k];
                    result[t][1] = Ytreshold[k];
                    if (k > 0) {
//                        System.out.print(", ");
                    }
//                    System.out.print("[" + T[k] + "-" + Ytreshold[k] + ":");
                    if (T[k] == Ytreshold[k]) {
//                        System.out.print(" True ");
                        nTrue++;
                    } else {
//                        System.out.print(" False ");
                    }
//                    System.out.print("]");
                }
//                System.out.println();

            }
            double TPPostif = 0;
            double FPPostif = 0;
            double FNPostif = 0;
            double TNPostif = 0;
            
            double TPNegatif = 0;
            double FPNegatif = 0;
            double FNNegatif = 0;
            double TNNegatif = 0;
            
            for (int i = 0; i < result.length; i++) {
                //Positif
                double actual = result[i][0];
                double predict = result[i][1];
                if(actual==1.0 && predict==1.0){
                    TPPostif++;
                }else if(actual==1.0 && predict==0.0){
                    FNPostif++;
                }else if(actual==0.0 && predict==1.0){
                    FPPostif++;
                }else if(actual==0.0 && predict==0.0){
                    TNPostif++;
                }
                
                //Negatif
                if(actual==0.0 && predict==0.0){
                    TPNegatif++;
                }else if(actual==0.0 && predict==1.0){
                    FNNegatif++;
                }else if(actual==1.0 && predict==0.0){
                    FPNegatif++;
                }else if(actual==1.0 && predict==1.0){
                    TNNegatif++;
                }
                
            }
            System.out.println(TPPostif);
            System.out.println(FPPostif);
            System.out.println(FNPostif);
            System.out.println(TNPostif);
            
            System.out.println("");
            
            System.out.println(TPNegatif);
            System.out.println(FPNegatif);
            System.out.println(FNNegatif);
            System.out.println(TNNegatif);
            System.out.println("");
            //Positif
            double precisionPositif = TPPostif / (TPPostif+FPPostif);
            double recalPositif =  TPPostif / (TPPostif+FNPostif);
            double F1_ScorePositif = (2*recalPositif*precisionPositif)/(recalPositif+precisionPositif);
            
            //Megatif
            double precisionNegatif = TPNegatif / (TPNegatif+FPNegatif);
            double recalNegatif =  TPNegatif / (TPNegatif+FNNegatif);
            double F1_ScoreNegatif = (2*recalNegatif*precisionNegatif)/(recalNegatif+precisionNegatif);
            
            
            System.out.println("\t\tPrecision\tRecal\tF1-Score");
            System.out.println("Aman\t\t"+String.format("%.2f", precisionPositif)+"\t\t"+String.format("%.2f", recalPositif)+"\t"+String.format("%.2f", F1_ScorePositif));
            System.out.println("Tidak Aman\t"+String.format("%.2f", precisionNegatif)+"\t\t"+String.format("%.2f", recalNegatif)+"\t"+String.format("%.2f", F1_ScoreNegatif));
            double akurasi = ((double) nTrue / (double) datatesting.length) * 100;
            System.out.println("---------------------------------------------------");
//            System.out.println("Threshold: "+treshold);
            System.out.println("AKURASI: " + String.format("%.2f", akurasi) + "%");
        }
    }
}