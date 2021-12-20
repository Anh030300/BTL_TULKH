package algorithm;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import data.Data;
import model.*;

/**
 * Define an algorithm interface
 *
 * @author ljnk975
 */
public abstract class Algorithm {

    // Ten cua thuat toan
    public String name;

    /// -> Input
    public Data data;

    /// -> Output
    /**
     * Time elapse
     */
    protected long timeElapse;


    protected int MaxDistance;

    /**
     * Taxi
     */
    protected Taxi[] taxis;

    protected Algorithm(String name) {
        this.name = name;
    }

    /**
     * @return ten thuat toan
     */
    public String getName() {
        return this.name;
    }

    /**
     * Main algorithm
     */
    protected abstract void doAlgorithm();

    /**
     * @return thoi gian tinh
     */
    public long getTimeElapse() {
        return timeElapse;
    }

    /**
     * Get number of mobile sensor
     *
     * @return the mobile sensor
     */
    public int getMaxDistance() {
        return this.MaxDistance;
    }

    /**
     * Get k barrier
     *
     * @return the list of barrier
     */
    public Taxi[] getTaxis() {
        return this.taxis;
    }


    /**
     * Read output directory
     *
     * @param filename
     */
    private final void readOutput(String filename) {
        Scanner in = null;
        try {
            in = new Scanner(new File(filename));
            data.K = in.nextInt();
            MaxDistance = in.nextInt();
            timeElapse = in.nextLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }

    protected PrintStream metaPs;

    /**
     * Solve proplem
     *
     * @param odir  output directory
     * @param fname file name
     * @param data  data
     */
    public final void solve(String odir, String fname, Data data) {
        this.data = data;

        String filename = odir + "/" + fname + ".txt"; // output file path

        File f = new File(filename.substring(0, filename.lastIndexOf("/"))); // output directory
        if (!f.exists()) // create if not exists
            f.mkdirs();

        f = new File(filename); // output file
//        if(f.exists()) { // if exists
//            this.readOutput(filename); // read the output
//            return;
//        }

        PrintStream ps = null; // printstream
        try {
            metaPs = new PrintStream(new FileOutputStream(filename + ".meta"));

            this.timeElapse = System.currentTimeMillis(); // start time
            this.doAlgorithm(); // do solve
            this.timeElapse = System.currentTimeMillis() - this.timeElapse; // end time

            ps = new PrintStream(new FileOutputStream(filename));

            // Output
            //ps.println(this.data.K);
            ps.println(MaxDistance);
            ps.println(timeElapse);


            for (int i = 0; i < data.K; i++) {
                Taxi taxi = taxis[i];
                for (int j = 0; j < taxi.visitedList.size(); j++)
                    ps.print(taxi.visitedList.get(j) + "  ");
                ps.println();
            }

            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                metaPs.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public String toString() {
        return "Algothirm " + name;
    }

    public static void run(Algorithm alg, int nRun) {
        FileOutputStream fos = null;
        PrintStream ps = null;
        for (int i = 11; i <= 11; i++) {
            try {
                fos = new FileOutputStream("./data/DataSet" + i + "/ketqua" + alg.getName() + " dataset" + i + ".txt");
                ps = new PrintStream(fos);

                if (nRun == 1)
                    ps.println("filename          result     time ");
                else
                    ps.println("filename          best      avg      dlc(%)     time ");
                File f = new File("./data/DataSet" + i + "/input.txt");
                String name = "Dataset " + i;
                Data data = Data.readData(f);

                System.out.println("Dataset " + i);

                double[] kqO = new double[nRun];
                double kqAv = 0;
                double timeAv = 0;
                double doLC = 0;
                double best = Integer.MAX_VALUE;

                for (int r = 0; r < nRun; r++) {
                    alg.solve("./data/Dataset" + i, "output"+ r, data);
                    kqO[r] = alg.getMaxDistance();
                    kqAv += alg.getMaxDistance();
                    timeAv += alg.getTimeElapse();
                    if (alg.getMaxDistance() < best)
                        best = alg.getMaxDistance();
                }

                timeAv /= nRun;
                kqAv /= nRun;
                for (int v = 0; v < nRun; v++)
                    doLC += (kqO[v] - kqAv) * (kqO[v] - kqAv);
                doLC = Math.sqrt(doLC / nRun) / kqAv;
                if (nRun == 1)
                    ps.println(name + "      " + best + "      " + timeAv);
                else
//                    	ps.print(String.format("%.4f", doLC)+", ");
                    ps.println(name + "      " + best + "      " + kqAv + "      " + doLC + "      " + timeAv);
                ps.println("-------------------------------------------------");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ps.close();
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
