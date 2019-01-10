package tirtiklama;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TirtikLama {
    
    static int K = 5;
    
    static FeatureExtraction fe = new FeatureExtraction();
    static ARFF arff = new ARFF(fe);
    static String trainDataSetPath = "dataset/train";
    static String testDataSetPath  = "dataset/test";
    static String modelPath        = "dataset/output/textTrainModel.arff";
    static String testPath         = "dataset/output/textTest.arff";
    
    public static void main(String[] args) throws Exception {
        File  trainModelFile = new File(modelPath);
        if(!trainModelFile.exists()){
            // Halihazırda eğitilmiş model yoksa eğitim yap
            System.out.println("Model Training...");
            trainARFF(trainDataSetPath);
        }

        // Zaten eğitilmiş bir model varsa onu kullan
        System.out.println("Reading Trained Model");
        TrainModel tm = ARFF.readTrainModel(modelPath);
        // Train Datasını Yazdır
        
        /* // EĞİTİM SETİNİN ÖNİZLENMESİ İSTENİRSE 
        for(SingleTrainData t : tm.data){
            System.out.println(Arrays.toString(t.values));
        }
        System.out.println("Model Size: " + Arrays.toString(tm.getModelSize()));
        */
        
        KNN knn = new KNN(K, tm);
        
        // Test
        System.out.println("Testing...");
        testARFF(tm);

        double[][] testArrays = ARFF.readTestModel(testPath);
        File folder = new File(testDataSetPath);
        File[] listOfFiles = folder.listFiles();
        for(int i = 0; i < testArrays.length; i++){
            knn.classify(listOfFiles[i].getName(), testArrays[i]).print();
            System.out.println("");
        }
        
        
    }

    private static HashMap[] trainARFF(String trainDataSetPath) throws IOException{
        File folder = new File(trainDataSetPath);
        File[] listOfFiles = folder.listFiles();
        HashMap[] trainSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            fe.add(n);
            trainSet[i] = n;
        }

        arff.addTrainSet(trainSet);
        arff.saveTrainARFF(listOfFiles, modelPath);
        
        return trainSet;
    }
        
    private static HashMap[] testARFF(TrainModel tm) throws IOException{
        File trainFolder = new File(trainDataSetPath);
        File[] trainListOfFiles = trainFolder.listFiles();
        
        File testFoler = new File(testDataSetPath);
        File[] testListOfFiles = testFoler.listFiles();
        
        HashMap[] testSet = new HashMap[testListOfFiles.length];
        
        for (int i = 0; i < testListOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(testListOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            testSet[i] = n;
        }

        arff.addTestSet(testSet);
        arff.saveTestARFF(trainListOfFiles, testListOfFiles, testPath, tm);
        
        return testSet;
    }
    
}
