package Praktika2;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

import java.io.File;

public class StratifyData {
    public static void main(String[] args) throws Exception{
        DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        if(data.classIndex()==-1){
            data.setClassIndex(data.numAttributes()-1);
        }
        //estratifikatutako partiketa
        Resample resample = new Resample();
        resample.setRandomSeed(1);
        resample.setSampleSizePercent(80);
        resample.setInvertSelection(false);
        resample.setNoReplacement(true);
        resample.setInputFormat(data);
        Instances train = Filter.useFilter(data, resample);
        System.out.println(train.size());
        resample.setInvertSelection(true);
        resample.setInputFormat(data);
        Instances dev = Filter.useFilter(data, resample);
        System.out.println(dev.size());

        ArffSaver saver = new ArffSaver();
        saver.setFile(new File(args[1]));
        saver.setInstances(train);
        saver.writeBatch();

        saver = new ArffSaver();
        saver.setFile(new File(args[2]));
        saver.setInstances(dev);
        saver.writeBatch();
    }
}
