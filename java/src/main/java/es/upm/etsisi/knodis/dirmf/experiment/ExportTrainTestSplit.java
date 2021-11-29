package es.upm.etsisi.knodis.dirmf.experiment;

import es.upm.etsisi.cf4j.data.*;
import es.upm.etsisi.knodis.dirmf.experiment.Settings;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;

public class ExportTrainTestSplit {

    public static void main (String[] args) throws Exception {

        DataModel datamodel = null;

        if (Settings.EXPORT_PREFIX.equals("ml1m")) {
            datamodel = BenchmarkDataModels.MovieLens1M();
        }
        else if (Settings.EXPORT_PREFIX.equals("filmtrust")){
            datamodel = BenchmarkDataModels.FilmTrust();
        }
        else if (Settings.EXPORT_PREFIX.equals("myanimelist")){
            datamodel = BenchmarkDataModels.MyAnimeList();
        }
        else if (Settings.EXPORT_PREFIX.equals("netflix")){
            datamodel = BenchmarkDataModels.NetflixPrize();
        }

        String[] HEADERS = { "user", "item", "rating"};

        // Train file

        File trainFile = new File("../data/" + Settings.EXPORT_PREFIX + "/training-ratings.csv");

        File parent = trainFile.getAbsoluteFile().getParentFile();
        parent.mkdirs();

        CSVPrinter trainCsvPrinter = new CSVPrinter(new FileWriter(trainFile), CSVFormat.DEFAULT.withHeader(HEADERS));

        for (User user : datamodel.getUsers()) {
            for (int pos = 0; pos < user.getNumberOfRatings(); pos++) {
                int itemIndex = user.getItemAt(pos);
                Item item = datamodel.getItem(itemIndex);
                double rating = user.getRatingAt(pos);

                trainCsvPrinter.printRecord(user.getUserIndex(), item.getItemIndex(), rating);
            }
        }

        trainCsvPrinter.close();

        System.out.println("File " + trainFile.toString() + " generated successfully.");

        // Test file

        File testFile = new File("../data/" + Settings.EXPORT_PREFIX + "/test-ratings.csv");

        CSVPrinter testCsvPrinter = new CSVPrinter(new FileWriter(testFile), CSVFormat.DEFAULT.withHeader(HEADERS));

        for (TestUser testUser : datamodel.getTestUsers()) {
            for (int pos = 0; pos < testUser.getNumberOfTestRatings(); pos++) {
                int testitemIndex = testUser.getTestItemAt(pos);
                TestItem item = datamodel.getTestItem(testitemIndex);
                double rating = testUser.getTestRatingAt(pos);

                testCsvPrinter.printRecord(testUser.getUserIndex(), item.getItemIndex(), rating);
            }
        }

        testCsvPrinter.close();

        System.out.println("File " + testFile.toString() + " generated successfully.");
    }
}
