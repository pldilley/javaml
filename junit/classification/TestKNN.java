/**
 * %SVN.HEADER%
 */
package junit.classification;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import org.junit.Assert;
import org.junit.Test;

import be.abeel.util.TimeInterval;

public class TestKNN {

    @Test
    public void testKNNIris() {
        try {
            Dataset data = FileHandler.loadDataset(new File("devtools/data/iris.data"), 4, ",");
            System.out.println("Loader: " + data.classes());
            Classifier knn = new KNearestNeighbors(5);
            CrossValidation cv = new CrossValidation(knn);
            Map<Object, PerformanceMeasure> p = cv.crossValidation(data, 5, new Random(10));
            System.out.println(p);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test KNN usage on the sparse sample.
     * 
     */
    @Test
    public void testSparseKNN() {

        try {
            /* Load a data set */
            Dataset data = FileHandler.loadSparseDataset(new File("devtools/data/smallsparse.tsv"), 0, "\t", ":");
            /*
             * Contruct a KNN classifier that uses 5 neighbors to make a
             * decision.
             */
            Classifier knn = new KNearestNeighbors(5);
            knn.buildClassifier(data);
            System.out.println("Building complete!");
            /*
             * Load a data set, this can be a different one, but we will use the
             * same one.
             */
            Dataset dataForClassification = FileHandler.loadSparseDataset(new File("devtools/data/smallsparse.tsv"), 0,
                    "\t", ":");
            /* Counters for correct and wrong predictions. */
            int correct = 0, wrong = 0;
            int count = 0;
            /* Classify all instances and check with the correct class values */
            for (int i = 0; i < 15; i++) {
                Instance inst = dataForClassification.instance(i);

                long time = System.currentTimeMillis();
                System.out.print("Processing instance: " + ++count + "\t");
                Object predictedClassValue = knn.classify(inst);
                Object realClassValue = inst.classValue();
                if (predictedClassValue.equals(realClassValue))
                    correct++;
                else
                    wrong++;
                System.out.println(new TimeInterval(System.currentTimeMillis() - time));

            }
            System.out.println("Correct predictions  " + correct);
            System.out.println("Wrong predictions " + wrong);

        } catch (IOException e) {
            Assert.assertTrue(false);
        }

    }
}
