package Praktika2;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class kfCV {
    public static void main(String[] args) throws Exception{
        DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        if(data.classIndex()==-1){
            data.setClassIndex(data.numAttributes()-1);
        }

        NaiveBayes nb = new NaiveBayes();
        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(nb, data, 5, new Random(1));

        try(FileWriter writer = new FileWriter(args[1], true)){
            writer.write("====kfCV====\n");
            writer.write(eval.toMatrixString()+"\n");
            writer.write("Precision metrika klase bakoitzeko: \n");
            Attribute klasea = data.classAttribute();
            for(int i=0; i< klasea.numValues();i++){
                String identifikatzailea = klasea.value(i);
                double precision = eval.precision(i);
                writer.write("\tIdentifikatzailea: " + identifikatzailea + ", Precision: " + precision + "\n");
            }
            writer.write("Exekuzio-data: " + LocalDateTime.now() + "\n");
            writer.write("Datu-Sorta path-a" + args[0] + "\n");
            writer.write("Ebaluazioaren path-a" + args[1] + "\n");
            writer.write(eval.toSummaryString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
