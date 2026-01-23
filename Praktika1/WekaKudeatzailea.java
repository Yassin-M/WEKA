package Praktika1;

import weka.core.Attribute;
import weka.core.AttributeStats;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.FileSourcedConverter;
import weka.experiment.Stats;

import java.io.File;
import java.util.Scanner;

public class WekaKudeatzailea {
    private static WekaKudeatzailea wekaKudeatzailea = null;

    private WekaKudeatzailea(){}

    public static WekaKudeatzailea getKudeatzailea(){
        if(wekaKudeatzailea==null){
            wekaKudeatzailea = new WekaKudeatzailea();
        }
        return wekaKudeatzailea;
    }

    public void datuOrokorrak () throws Exception{
        DataSource source = new DataSource("/home/yassin/Klase/3/WEKA/Praktikak/1.Praktika/Datuak/heart-c.arff");
        Instances data = source.getDataSet();
        if(data.classIndex() == -1){
            data.setClassIndex(data.numAttributes() - 1);
        }
        fitxategiPath(source);
        System.out.println("Instantzia kopurua: " + data.size());
        System.out.println("Atributu kopurua: " + data.numAttributes());
        System.out.println("Lehenengo atributuak har ditzakeen balio ezberdinak: " + data.numDistinctValues(0));
        // azken atributua


        System.out.println("Azken aurreko atributuak dituen missing value atributuak: " + data.attributeStats(data.numAttributes()-2).missingCount);
        System.out.println("Menua erabili nahi duzu?(Bai 1/Ez 0)");
        Scanner teklatua = new Scanner(System.in);
        int erabAukera = teklatua.nextInt();
        if(erabAukera==1){
            if(true){
                System.out.println("Datu-sorta honen atributuak: ");
                for(int i=0; i<data.numAttributes(); i++){
                    System.out.println((i+1) + ". " + data.attribute(i).name());
                }
                System.out.println("Zein atributuren buruzko informazio aztertu nahi duzu? (Jarri zenbakia)");
                int aukera;
                do{
                    aukera = teklatua.nextInt();
                }while(aukera<1 && aukera>14);
                Attribute aukeratutakoa = data.attribute(aukera-1);
                AttributeStats estatistikak = data.attributeStats(aukera-1);
                System.out.println("Atributu honen missing values: " + estatistikak.missingCount + ", distinct values: " + estatistikak.distinctCount + ", unique values: " + estatistikak.uniqueCount);
                System.out.println("Beste estatistika batzuk");
                if(aukeratutakoa.isNominal()){
                    this.atributuNominala(aukeratutakoa, estatistikak);
                }else{
                    this.zenbakizkoAtributua(estatistikak.numericStats);
                }
            }
        }else{
            System.out.println("Agur");
        }
    }

    public void atributuNominala(Attribute pAtributua, AttributeStats pStats){
        for(int i = 0;i<pAtributua.numValues();i++){
            System.out.println("Identifikatzailea: " + pAtributua.name() + ", Kopurua: " + pStats.nominalCounts[i]);
        }
    }

    public void zenbakizkoAtributua(Stats pStat){
        System.out.println("Minimoa: " + pStat.min);
        System.out.println("Maximoa: " + pStat.max);
        System.out.println("Batezbestekoa: " + pStat.mean);
        System.out.println("Desbideratze tipikoa: " + pStat.stdDev);
    }



    private void fitxategiPath(DataSource source){
        try {
            Object loader = source.getLoader();
            if (loader instanceof FileSourcedConverter) {
                File archivo = ((FileSourcedConverter) loader).retrieveFile();
                String path = archivo.getAbsolutePath();
                System.out.println("Fitxategiko path-a hurrengoa da: " + path);
            } else {
                System.out.println("Ez dago fitxategirik");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
