# Dirichlet Matrix Factorization (DirMF)

This repository contains the source code of the experimentis performed for the paper "*Dirichlet Matrix Factorization: A Reliable Classification-based Recommender System*" submitted to *Applied Sciences* in November 2021.

The source code is structured as follows:

- `python` directory contains the Jupyter notebooks used to compute the predictions and recommendations of neural based collaborative filtering methods (i.e. GMF y NCF).
- `java` directort contains the maven project used to compute the predictions and recommendations of matrix factorization based collaborative filtering methods (i.e. DirMF, BeMF and PMF).

To ensure a fair comparison of all models, the [CF4J](http://cf4j.etsisi.upm.es/) training and test sets have been used in all datasets.

To run the experiments, you should follow the steps below:

1. Run `experiments.ExportTrainTestSplit.java` to export the CF4J datasets for use in python.
2. Calculate the MAE, Precision and Recall of GMF and NCF following the Jupyter Notebooks.
3. Write down the above results in the `experiments.Settings.java` file.
4. Run GridSearch to determine the optimal hyper-parameters of the matrix factorization models.
5. Record the best hyper-parameters in the `experiments.Settings.java` file.
5. Run the java files `experiments.QualityOfPredictions.java` and `experiments.QualityOfRecommendations.java` to compare all methods.

