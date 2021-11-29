package es.upm.etsisi.knodis.dirmf.experiment;

import es.upm.etsisi.knodis.dirmf.recommender.DirMF;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.qualityMeasure.prediction.MAE;
import es.upm.etsisi.cf4j.util.Range;
import es.upm.etsisi.cf4j.util.optimization.GridSearch;
import es.upm.etsisi.cf4j.util.optimization.ParamsGrid;

import java.io.IOException;

public class DirMFGridSearch {

    private final static int TOP_N = 10;

    public static void main(String[] args) throws IOException {

        DataModel datamodel = Settings.DATAMODEL;

        ParamsGrid paramsGrid = new ParamsGrid();

        // Modify these values to define the search coverage
        paramsGrid.addParam("numFactors", new int[]{4, 7 ,10});
        paramsGrid.addParam("learningRate", Range.ofDoubles(0.01, 0.01, 4));
        paramsGrid.addParam("regularization", Range.ofDoubles(0.01, 0.01, 4));
        paramsGrid.addParam("numIters", new int[]{50, 100});

        paramsGrid.addFixedParam("ratings", Settings.RATINGS);
        paramsGrid.addFixedParam("seed", Settings.RANDOM_SEED);

        GridSearch gridSearch = new GridSearch(datamodel, paramsGrid, DirMF.class, MAE.class);
        gridSearch.fit();
        gridSearch.printResults(TOP_N);
    }
}
