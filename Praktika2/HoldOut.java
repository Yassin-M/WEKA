package Praktika2;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemovePercentage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class HoldOut {
    public static void main(String[] args) throws Exception{
        DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        if(data.classIndex()==-1){
            data.setClassIndex(data.numAttributes()-1);
        }

        //datu partiketa
        Randomize random = new Randomize();
        random.setSeed(42);
        random.setInputFormat(data);
        Instances randData = Filter.useFilter(data, random);
        //  train
        RemovePercentage remove = new RemovePercentage();
        remove.setPercentage(66);
        remove.setInvertSelection(true);
        remove.setInputFormat(randData);
        Instances train = Filter.useFilter(randData, remove);
        System.out.println(train.size());
        // test
        remove.setInvertSelection(false);
        remove.setInputFormat(randData);
        Instances test = Filter.useFilter(randData, remove);
        System.out.println(test.size());
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(train);

        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(nb, test);

        try(FileWriter writer = new FileWriter(args[1], true)){
            writer.write("====Hold-Out====\n");
            writer.write("Exekuzio data: " + LocalDateTime.now() + "\n");
            writer.write("Data-sortaren path-a: " + args[0] + "\n");
            writer.write("Ebaluazio fitxategia: " + args[1] + "\n");
            //Klase minoritarioa
            Attribute klasea = randData.classAttribute();
            AttributeStats stats = randData.attributeStats(randData.classIndex());
            int min = Integer.MAX_VALUE;
            int indizea = -1;
            for(int i = 0; i<klasea.numValues(); i++){
                if(stats.nominalCounts[i]==0) continue;
                if(stats.nominalCounts[i]<min){
                    min = stats.nominalCounts[i];
                    indizea = i;
                }
            }
            writer.write("Klase minoritarioari dagokion, \n");
            writer.write("\tPrecision: " + eval.precision(indizea) + "\n");
            writer.write("\tRecall: " + eval.recall(indizea) + "\n");
            writer.write("\tF-Score: " + eval.fMeasure(indizea) + "\n");
            writer.write("Weighted AVG: \n");
            writer.write("\tPrecision: " + eval.weightedPrecision() + "\n");
            writer.write("\tRecall: " + eval.weightedRecall() + "\n");
            writer.write("\tF-Score: " + eval.weightedFMeasure() + "\n");
            writer.write(eval.toMatrixString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
