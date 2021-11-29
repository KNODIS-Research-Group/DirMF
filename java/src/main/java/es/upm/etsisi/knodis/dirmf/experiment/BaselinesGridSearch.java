package es.upm.etsisi.knodis.dirmf.experiment;

import es.upm.etsisi.knodis.dirmf.recommender.BeMF;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MAE;
import es.upm.etsisi.cf4j.recommender.matrixFactorization.PMF;
import es.upm.etsisi.cf4j.util.Range;
import es.upm.etsisi.cf4j.util.optimization.GridSearch;
import es.upm.etsisi.cf4j.util.optimization.ParamsGrid;

import java.io.IOException;

public class BaselinesGridSearch {

    private final static int TOP_N = 10;

    public static void main(String[] args) throws IOException {

        DataModel datamodel = Settings.DATAMODEL;

        ParamsGrid paramsGrid;
        GridSearch gridSearch;

        // PMF Recommender

        paramsGrid = new ParamsGrid();

        paramsGrid.addParam("numFactors", new int[]{2, 4, 6, 8, 10});
        paramsGrid.addParam("lambda", Range.ofDoubles(0.005, 0.005, 20));
        paramsGrid.addParam("gamma", Range.ofDoubles(0.005, 0.005, 20));

        paramsGrid.addFixedParam("numIters", 50);
        paramsGrid.addFixedParam("seed", Settings.RANDOM_SEED);

        gridSearch = new GridSearch(datamodel, paramsGrid, PMF.class, MAE.class);
        gridSearch.fit();
        gridSearch.printResults(TOP_N);


        // BeMF Recommender

        paramsGrid = new ParamsGrid();

        paramsGrid.addParam("numFactors", new int[]{2, 4, 6, 8});
        paramsGrid.addParam("learningRate", Range.ofDoubles(0.002, 0.002, 10));
        paramsGrid.addParam("regularization", Range.ofDoubles(0.01, 0.01, 20));
        paramsGrid.addParam("numIters", new int[]{50, 75, 100});

        paramsGrid.addFixedParam("ratings", Settings.RATINGS);
        paramsGrid.addFixedParam("seed", Settings.RANDOM_SEED);

        gridSearch = new GridSearch(datamodel, paramsGrid, BeMF.class, MAE.class);
        gridSearch.fit();
        gridSearch.printResults(TOP_N);
    }
}
