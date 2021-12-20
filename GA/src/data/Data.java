package data;

import model.Taxi;

import java.io.File;
import java.util.Scanner;

public class Data {
    public int N;
    public int M;
    public int K;
    public int[] volume = new int [M];
    public int[][] distance = new int[2*N+2*M+1][2*N+2*M+1];
    public Taxi[] taxis;

    public Data() {
    }

    public static Data readData(File fn) {
        Scanner in = null;
        try {
            Data data = new Data();

            in   = new Scanner(fn);

            data.N = in.nextInt();
            data.M  = in.nextInt();
            data.K = in.nextInt();
            System.out.println(data.K);

            data.volume = new int [data.M];
            for (int i = 0; i < data.M ; i++) {
                data.volume[i] = in.nextInt();
            }

            data.taxis = new Taxi[data.K];
            for (int i = 0; i < data.K ; i++) {
                data.taxis[i] = new Taxi();
                data.taxis[i].maxVolume = in.nextInt();
            }

            int size = 2*data.N+ 2*data.M+1;
            data.distance = new int [size][size];
            for(int i = 0;i < size;i++){
                data.distance[i] = new int[2*data.N+ 2*data.M+1];
                for(int j = 0;j<2*data.N+ 2*data.M+1;j++) {
                    data.distance[i][j] = in.nextInt();
                }
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch(Exception e) {
            }
        }
        return null;
    }
}
