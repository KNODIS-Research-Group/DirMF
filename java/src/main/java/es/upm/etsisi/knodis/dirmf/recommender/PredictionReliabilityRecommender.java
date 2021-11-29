package es.upm.etsisi.knodis.dirmf.recommender;

import es.upm.etsisi.cf4j.data.DataModel;

public interface PredictionReliabilityRecommender {
    double predict(int userIndex, int itemIndex);
    double getPredictionReliability(int userIndex, int itemIndex);
    DataModel getDataModel();
}
