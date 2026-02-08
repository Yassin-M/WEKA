package Praktika1;

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import java.io.File;
import java.io.FileWriter;

public class Main {
    public static void main(String args[]) throws Exception{
        DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        if(data.classIndex()==-1){
            data.setClassIndex(data.numAttributes()-1);
        }
        //praktikatzeko, praktika honetan ez da eskatzen
        AttributeSelection filter = new AttributeSelection();
        filter.setEvaluator(new CfsSubsetEval());
        filter.setSearch(new BestFirst());
        filter.setInputFormat(data);
        Instances dataBerria = Filter.useFilter(data, filter);

        System.out.println("Fitxategiko path-a: " + args[0]);
        System.out.println("Instantzia kopurua: " + dataBerria.numInstances());
        System.out.println("Atributu kopurua (Filtroa baino lehen): " + data.numAttributes());
        System.out.println("Atributu kopurua (Filtroa eta gero): " + dataBerria.numAttributes());
        System.out.println("Lehen atributuak har ditzakeen balio ezberdinak: " + dataBerria.attribute(0).numValues());
        Attribute klasea = dataBerria.attribute(dataBerria.numAttributes()-1);
        int[] maiztasunak = dataBerria.attributeStats(dataBerria.numAttributes()-1).nominalCounts;
        System.out.println("Azken atributuak:");
        for(int i = 0; i<klasea.numValues();i++){
            String identifikatzailea = klasea.value(i);
            int maiztasuna = maiztasunak[i];
            System.out.println("Identifikatzailea: " + identifikatzailea + ", Maiztasuna: " + maiztasuna);
        }
        int min = Integer.MAX_VALUE;
        int indizea = -1;
        for(int i = 0; i<klasea.numValues(); i++){
            if(maiztasunak[i]==0) continue;
            if(maiztasunak[i]<min){
                min = maiztasunak[i];
                indizea = i;
            }
        }
        System.out.println("Klase minoritarioa: " + klasea.value(indizea) + ", eta bere maiztasuna: " + min);
        System.out.println("Azken aurreko atributuaren missing value kopurua: " + dataBerria.attributeStats(dataBerria.numAttributes()-2).missingCount);
    }
}
