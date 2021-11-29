package es.upm.etsisi.knodis.dirmf.recommender;

import es.upm.etsisi.cf4j.data.DataModel;

public interface RecommendationReliabilityRecommender {
    double getRecommendationReliability(int userIndex, int itemIndex);
    DataModel getDataModel();
}
