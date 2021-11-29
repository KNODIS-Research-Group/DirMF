package es.upm.etsisi.knodis.dirmf.experiment;

import es.upm.etsisi.cf4j.data.BenchmarkDataModels;
import es.upm.etsisi.cf4j.data.DataModel;

import java.util.Map;

public class Settings {

    // Global settings
    public static final long RANDOM_SEED = 43;

    public static final int PLOT_WIDTH = 950;
    public static final int PLOT_HEIGHT = 534;

    public static final int NUM_RECOMMENDATIONS = 10;

    // Dataset settings
    public static DataModel DATAMODEL = null;
    public static double[] RATINGS;
    public static double LIKE_THRESHOLD;

    public static String EXPORT_PREFIX;

    // DirMF parameters
    public static Map<String, Object> DIRMF_PARAMS;

    // Baselines parameters
    public static Map<String, Object> BEMF_PARAMS;
    public static Map<String, Object> PMF_PARAMS;

    //Neural Baselines
    public static double GMF_MAE;
    public static double GMF_COVERAGE;
    public static double GMF_PRECISION;
    public static double GMF_RECALL;

    public static double NCF_MAE;
    public static double NCF_COVERAGE;
    public static double NCF_PRECISION;
    public static double NCF_RECALL;


    // Uncomment this for MovieLens
    /*static {
        try {
            DATAMODEL = BenchmarkDataModels.MovieLens1M();
            RATINGS = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
            LIKE_THRESHOLD = 4.0;

            EXPORT_PREFIX = "ml1m";

            DIRMF_PARAMS = Map.of(
                    "numFactors", 6,
                    "numIters", 50,
                    "learningRate", 0.022,
                    "regularization", 0.01,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
            );

            BEMF_PARAMS = Map.of(
                    "numFactors", 2,
                    "numIters", 100,
                    "learningRate", 0.006,
                    "regularization", 0.16,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
            );

            PMF_PARAMS = Map.of(
                    "numFactors", 8,
                    "numIters", 50,
                    "gamma", 0.01,
                    "lambda", 0.045,
                    "seed", RANDOM_SEED
            );

            GMF_MAE = 0.7036164465208747;
            GMF_COVERAGE = 1.0;
            GMF_PRECISION = 0.7542625340685808;
            GMF_RECALL = 0.3150217704279688;

            NCF_MAE = 0.7126104259069086;
            NCF_COVERAGE = 1.0;
            NCF_PRECISION = 0.7695750595351295;
            NCF_RECALL = 0.2840877406383902;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    // Uncomment this for FilmTrust
    /*static {
        try {
            DATAMODEL = BenchmarkDataModels.FilmTrust();
            RATINGS = new double[]{0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0};
            LIKE_THRESHOLD = 3.5;

            EXPORT_PREFIX = "filmtrust";

            DIRMF_PARAMS = Map.of(
                    "numFactors", 8,
                    "numIters", 100,
                    "learningRate", 0.09,
                    "regularization", 0.015,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
                    );

            BEMF_PARAMS = Map.of(
                    "numFactors", 2,
                    "numIters", 75,
                    "learningRate", 0.02,
                    "regularization", 0.06,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
            );

            PMF_PARAMS = Map.of(
                    "numFactors", 4,
                    "numIters", 50,
                    "gamma", 0.015,
                    "lambda", 0.1,
                    "seed", RANDOM_SEED
            );

            GMF_MAE = 0.7925031240310939;
            GMF_COVERAGE = 1.0;
            GMF_PRECISION = 0.5600725680700493;
            GMF_RECALL = 0.5118581334702241;

            NCF_MAE = 0.6305451755110952;
            NCF_COVERAGE = 1.0;
            NCF_PRECISION = 0.6817910119547401;
            NCF_RECALL = 0.6566600164459108;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


      // Uncomment this for MyAnimeList
      static {
        try {
            DATAMODEL = BenchmarkDataModels.MyAnimeList();
            RATINGS = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
            LIKE_THRESHOLD = 7.0;

            EXPORT_PREFIX = "myanimelist";


            DIRMF_PARAMS = Map.of(
                    "numFactors", 8,
                    "numIters", 100,
                    "learningRate", 0.01,
                    "regularization", 0.02,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
                    );

            BEMF_PARAMS = Map.of(
                    "numFactors", 4,
                    "numIters", 100,
                    "learningRate", 0.004,
                    "regularization", 0.1,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
            );

            PMF_PARAMS = Map.of(
                    "numFactors", 10,
                    "numIters", 50,
                    "gamma", 0.005,
                    "lambda", 0.085,
                    "seed", RANDOM_SEED
            );

            GMF_MAE = 0.9150075794929836;
            GMF_COVERAGE = 1.0;
            GMF_PRECISION = 0.7226153996249882;
            GMF_RECALL = 0.43146685673147717;

            NCF_MAE = 0.901127891111204;
            NCF_COVERAGE = 1.0;
            NCF_PRECISION = 0.74430261807196;
            NCF_RECALL = 0.4114008594284523;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      // Uncomment this for Netflix
/*    static {
        try {
            DATAMODEL = BenchmarkDataModels.MyAnimeList();
            RATINGS = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
            LIKE_THRESHOLD = 4.0;

            EXPORT_PREFIX = "netflix";


            DIRMF_PARAMS = Map.of(
                    "numFactors", 10,
                    "numIters", 50,
                    "learningRate", 0.02,
                    "regularization", 0.02,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
                    );

            BEMF_PARAMS = Map.of(
                    "numFactors", 4,
                    "numIters", 100,
                    "learningRate", 0.004,
                    "regularization", 0.1,
                    "ratings", RATINGS,
                    "seed", RANDOM_SEED
            );

            PMF_PARAMS = Map.of(
                    "numFactors", 10,
                    "numIters", 50,
                    "gamma", 0.005,
                    "lambda", 0.085,
                    "seed", RANDOM_SEED
            );

            GMF_MAE = 0.6861803149776209;
            GMF_COVERAGE = 1.0;
            GMF_PRECISION = 0.600562101316028;
            GMF_RECALL = 0.29922371652744606;

            NCF_MAE = 0.6882784305068139;
            NCF_COVERAGE = 1.0;
            NCF_PRECISION = 0.6401984761614494;
            NCF_RECALL = 0.3079389919867263;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
