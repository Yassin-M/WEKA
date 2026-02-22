package Praktika3;

import java.io.FileWriter;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Programa2 {

    public static void main(String[] args) throws Exception{
        Classifier cls = (Classifier) SerializationHelper.read(args[0]);
        DataSource source = new DataSource(args[1]);
        Instances test = source.getDataSet();
        if(test.classIndex()==-1) {
            test.setClassIndex(test.numAttributes()-1);
        }
        Instances train = source.getStructure();
        if(train.classIndex()==-1) {
            train.setClassIndex(train.numAttributes()-1);
        }

        Evaluation eval = new Evaluation(train);

        try(FileWriter writer = new FileWriter(args[2], true)){
            writer.write("====3.Praktika====\n");
            for(int i = 0; i<test.numInstances(); i++) {
                Instance predizituBeharrekoa = test.get(i);
                double predizitutakoa = eval.evaluateModelOnce(cls, predizituBeharrekoa);
                double erreala = predizituBeharrekoa.classValue();
                String ondo = "";
                if(predizitutakoa==erreala) ondo = "ondo predizituta";
                else ondo = "txarto predizituta";
                writer.write("Instantziaren balioa, " + erreala + " da, eta sailkatzaileak " + predizitutakoa + "iragarri du, beraz " + ondo);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}
