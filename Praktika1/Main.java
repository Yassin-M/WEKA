package Praktika1;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.FileSourcedConverter;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception{
        WekaKudeatzailea kudeatzailea = WekaKudeatzailea.getKudeatzailea();
        kudeatzailea.datuOrokorrak();

    }


}
