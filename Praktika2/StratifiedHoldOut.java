package Praktika2;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileWriter;
import java.time.LocalDateTime;

public class StratifiedHoldOut {
    public static void main(String[] args) throws Exception{
        DataSource sourceTrain = new DataSource(args[0]);
        Instances train = sourceTrain.getDataSet();
        if(train.classIndex()==-1){
            train.setClassIndex(train.numAttributes()-1);
        }
        DataSource sourceTest = new DataSource(args[1]);
        Instances test = sourceTest.getDataSet();
        if(test.classIndex()==-1){
            test.setClassIndex(test.numAttributes()-1);
        }
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(train);
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(nb, test);
        try(FileWriter writer = new FileWriter(args[2])){
            writer.write("====Stratified Hold-Out====");
            writer.write("Exekuzio-data: " + LocalDateTime.now() + "\n");
            writer.write("Train path" + args[0] + "\n");
            writer.write("Test path" + args[1] + "\n");
            writer.write(eval.toMatrixString());
            writer.write("Accuracy: " + eval.pctCorrect()/100);
        }
    }
}
