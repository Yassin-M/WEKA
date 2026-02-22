package Praktika3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Programa1 {

    public static void main(String[] args) throws Exception{
        DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        if(data.classIndex()==-1) {
            data.setClassIndex(data.numAttributes()-1);
        }
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(data);
        SerializationHelper.write(args[1], nb);
        //kfcv
        nb = new NaiveBayes();
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(nb, data, 5, new Random(1));
        try(FileWriter writer = new FileWriter(args[2], true)){
            writer.write("====kfCV====\n");
            writer.write(eval.toMatrixString() +"\n");
        }catch(IOException e) {
            e.printStackTrace();
        }

        Randomize randomizer = new Randomize();
        randomizer.setSeed(42);
        randomizer.setInputFormat(data);
        Instances randData = Filter.useFilter(data, randomizer);

        RemovePercentage remove = new RemovePercentage();
        remove.setPercentage(70);
        remove.setInvertSelection(true);
        remove.setInputFormat(randData);
        Instances train = Filter.useFilter(randData, remove);
        System.out.println(train.size());

        remove.setInvertSelection(false);
        remove.setInputFormat(randData);
        Instances test = Filter.useFilter(randData, remove);
        System.out.println(test.size());

        nb = new NaiveBayes();
        nb.buildClassifier(train);
        eval = new Evaluation(train);
        eval.evaluateModel(nb, test);

        try(FileWriter writer = new FileWriter(args[2], true)){
            writer.write("====Hold-Out====\n");
            writer.write(eval.toMatrixString() +"\n");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
