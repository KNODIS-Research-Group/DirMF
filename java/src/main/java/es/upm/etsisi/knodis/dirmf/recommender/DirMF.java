package es.upm.etsisi.knodis.dirmf.recommender;

import es.upm.etsisi.knodis.dirmf.experiment.Settings;
import es.upm.etsisi.cf4j.data.DataModel;
import es.upm.etsisi.cf4j.data.Item;
import es.upm.etsisi.cf4j.data.User;
import es.upm.etsisi.cf4j.recommender.Recommender;
import es.upm.etsisi.cf4j.util.Maths;
import es.upm.etsisi.cf4j.util.process.Parallelizer;
import es.upm.etsisi.cf4j.util.process.Partible;
import org.apache.commons.math3.special.Gamma;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class DirMF extends Recommender implements PredictionReliabilityRecommender, RecommendationReliabilityRecommender {
    private int numFactors;

    private int numIters;

    private double learningRate;

    private double regularization;

    private double[] ratings;

    private double[][][] P;

    private double[][][] Q;

    public DirMF(DataModel datamodel, Map<String, Object> params) {
        this(
                datamodel,
                (int) params.get("numFactors"),
                (int) params.get("numIters"),
                (double) params.get("learningRate"),
                (double) params.get("regularization"),
                (double[]) params.get("ratings"),
                params.containsKey("seed") ? (long) params.get("seed") : System.currentTimeMillis()
        );
    }

    public DirMF(DataModel datamodel, int numFactors, int numIters, double learningRate, double regularization, double[] ratings, long seed) {
        super(datamodel);

        this.numFactors = numFactors;
        this.numIters = numIters;
        this.learningRate = learningRate;
        this.regularization = regularization;
        this.ratings = ratings;

        Random rand = new Random(seed);

        this.P = new double[ratings.length][datamodel.getNumberOfUsers()][numFactors];
        for (int r = 0; r < ratings.length; r++) {
            for (int u = 0; u < datamodel.getNumberOfUsers(); u++) {
                for (int k = 0; k < numFactors; k++) {
                    this.P[r][u][k] = rand.nextDouble();
                }
            }
        }


        this.Q = new double[ratings.length][datamodel.getNumberOfItems()][numFactors];
        for (int r = 0; r < ratings.length; r++) {
            for (int i = 0; i < datamodel.getNumberOfItems(); i++) {
                for (int k = 0; k < numFactors; k++) {
                    this.Q[r][i][k] = rand.nextDouble();
                }
            }
        }
    }

    public int getNumFactors() {
        return numFactors;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getRegularization() {
        return regularization;
    }

    public double[] getRatings() {
        return ratings;
    }

    @Override
    public void fit() {
        System.out.println("\nFitting " + this.toString());

        for (int iter = 1; iter <= this.numIters; iter++) {
            Parallelizer.exec(datamodel.getUsers(), new UpdateUsersFactors());
            Parallelizer.exec(datamodel.getItems(), new UpdateItemsFactors());

            if ((iter % 10) == 0) System.out.print(".");
            if ((iter % 100) == 0) System.out.println(iter + " iterations");
        }
    }

    @Override
    public double predict(int userIndex, int itemIndex) {
        double max = this.getProbability(userIndex, itemIndex, 0);
        int index = 0;

        for (int r = 1; r < this.ratings.length; r++) {
            double prob = this.getProbability(userIndex, itemIndex, r);
            if (max < prob) {
                max = prob;
                index = r;
            }
        }

        return this.ratings[index];
    }

    private double getProbability(int userIndex, int itemIndex, int r) {
        double dot = Maths.logistic(Maths.dotProduct(this.P[r][userIndex], this.Q[r][itemIndex]));

        double sum = 0;
        for (int i = 0; i < this.ratings.length; i++) {
            sum += Maths.logistic(Maths.dotProduct(this.P[i][userIndex], this.Q[i][itemIndex]));
        }

        return dot / sum;
    }

    @Override
    public double getPredictionReliability(int userIndex, int itemIndex) {
        double prediction = this.predict(userIndex, itemIndex);

        int r = 0;
        while (this.ratings[r] != prediction) {
            r++;
        }

        return this.getProbability(userIndex, itemIndex, r);
    }

    public double[] getProbabilityDistribution(int userIndex, int itemIndex) {
        return this.getProbabilityDistribution(this.P[userIndex], this.Q[itemIndex]);
    }

    public double[] getProbabilityDistribution(double[][] pu, double[][] qi) {
        double[] probs = new double[this.ratings.length];
        for (int r = 0; r < probs.length; r++) {
            double dot = Maths.logistic(Maths.dotProduct(pu[r], qi[r]));
            probs[r] = dot;
        }
        return probs;
    }

    @Override
    public double getRecommendationReliability(int userIndex, int itemIndex) {
        double reliability = 0;
        for (int r = 0; r < this.ratings.length; r++) {
            double rating = this.ratings[r];
            if (rating >= Settings.LIKE_THRESHOLD) {
                reliability += this.getProbability(userIndex, itemIndex, r);
            }
        }
        return reliability;
    }

    public double[][] getUserParams(int userIndex) {
        return this.P[userIndex];
    }

    public double[][] getItemParams(int itemIndex) {
        return this.Q[itemIndex];
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("DirMF(")
                .append("numFactors=").append(this.numFactors)
                .append("; ")
                .append("numIters=").append(this.numIters)
                .append("; ")
                .append("learningRate=").append(this.learningRate)
                .append("; ")
                .append("regularization=").append(this.regularization)
                .append("; ")
                .append("ratings=").append(Arrays.toString(this.ratings))
                .append(")");
        return str.toString();
    }

    /**
     * Auxiliary inner class to parallelize user factors computation
     */
    private class UpdateUsersFactors implements Partible<User> {

        @Override
        public void beforeRun() { }

        @Override
        public void run(User user) {
            int userIndex = user.getUserIndex();

            for (int pos = 0; pos < user.getNumberOfRatings(); pos++) {
                int itemIndex = user.getItemAt(pos);

                double sum = 0;
                for (int s = 0; s < ratings.length; s++) {
                    double dot = Maths.dotProduct(P[s][userIndex], Q[s][itemIndex]);
                    sum += Maths.logistic(dot);
                }

                for (int s = 0; s < ratings.length; s++) {
                    double rating = user.getRatingAt(pos);
                    double r_ui = (rating == ratings[s] ? Math.exp(rating) : 1) / (ratings.length - 1 + Math.exp(rating));

                    double dot = Maths.dotProduct(P[s][userIndex], Q[s][itemIndex]);
                    double logit = Maths.logistic(dot);

                    for (int k = 0; k < numFactors; k++) {
                        double gradient = Q[s][itemIndex][k] * logit * (1 - logit) * (Gamma.digamma(logit) - Gamma.digamma(sum) - Math.log(r_ui));
                        P[s][userIndex][k] -= learningRate * (gradient + regularization * P[s][userIndex][k]);
                    }
                }
            }
        }

        @Override
        public void afterRun() { }
    }

    /**
     * Auxiliary inner class to parallelize item factors computation
     */
    private class UpdateItemsFactors implements Partible<Item> {

        @Override
        public void beforeRun() { }

        @Override
        public void run(Item item) {
            int itemIndex = item.getItemIndex();

            for (int pos = 0; pos < item.getNumberOfRatings(); pos++) {
                int userIndex = item.getUserAt(pos);

                double sum = 0;
                for (int s = 0; s < ratings.length; s++) {
                    double dot = Maths.dotProduct(P[s][userIndex], Q[s][itemIndex]);
                    sum += Maths.logistic(dot);
                }

                for (int s = 0; s < ratings.length; s++) {
                    double rating = item.getRatingAt(pos);
                    double r_ui = (rating == ratings[s] ? Math.exp(rating) : 1) / (ratings.length - 1 + Math.exp(rating));

                    double dot = Maths.dotProduct(P[s][userIndex], Q[s][itemIndex]);
                    double logit = Maths.logistic(dot);

                    for (int k = 0; k < numFactors; k++) {
                        double gradient = P[s][userIndex][k] * logit * (1 - logit) * (Gamma.digamma(logit) - Gamma.digamma(sum) - Math.log(r_ui));
                        Q[s][itemIndex][k] -= learningRate * (gradient + regularization * Q[s][itemIndex][k]);
                    }
                }
            }
        }

        @Override
        public void afterRun() { }
    }
}
