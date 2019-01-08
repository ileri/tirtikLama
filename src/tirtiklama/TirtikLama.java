package tirtiklama;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class TirtikLama {
    static FeatureExtraction fe = new FeatureExtraction();
    static ARFF arff = new ARFF(fe);
    
    public static void main(String[] args) throws IOException {
        String trainDataSetPath = "dataset/train";
        String testDataSetPath  = "dataset/test";
        
        HashMap<String, HashMap> tr = train(trainDataSetPath);
        HashMap<String, double[]> ef = fe.extractFeatures(tr);
        
        KNN knn = new KNN(ef, 5);
        HashMap<String, HashMap> ts = test(testDataSetPath);
        Iterator testIterator = ts.entrySet().iterator();
        
        while(testIterator.hasNext()){
            Map.Entry t = (Map.Entry)testIterator.next();
            Object[][] res = knn.classify(fe.extractTextFeatures((HashMap)t.getValue()));
            for(Object[] r : res){
                System.out.printf("Distance between %s and %s is: %f\n", (String)t.getKey(), r[0], r[1]);
            }
        }
        System.out.println("");

        Iterator similarityTrainIterator = tr.entrySet().iterator();
        Iterator similarityTestIterator  = ts.entrySet().iterator();

        while(similarityTestIterator.hasNext()){
            Map.Entry tmp_test = (Map.Entry)similarityTestIterator.next();
            while(similarityTrainIterator.hasNext()){
                Map.Entry tmp_train = (Map.Entry)similarityTrainIterator.next();
                HashMap tts = (HashMap)tmp_test.getValue();
                HashMap ttr = (HashMap)tmp_train.getValue();
                CosineSimilarity cs1 = new CosineSimilarity(ttr, tts);
                System.out.printf("Similarity between %s and %s is: %f\n", (String)tmp_test.getKey(), (String)tmp_train.getKey(), cs1.similarity()*100);
            }
        }
    }
    
    private static HashMap train(String trainDataSetPath) throws IOException{
        File folder = new File(trainDataSetPath);
        File[] listOfFiles = folder.listFiles();
        HashMap<String, HashMap> trainSet = new HashMap<String, HashMap>(); 
        //HashMap[] trainSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            fe.add(n);
            trainSet.put(listOfFiles[i].getName(), n);
        }
        
        return trainSet;
    }
    
    private static HashMap test(String testDataSetPath) throws IOException{
        File folder = new File(testDataSetPath);
        File[] listOfFiles = folder.listFiles();
        HashMap<String, HashMap> testSet = new HashMap<String, HashMap>(); 
        //HashMap[] trainSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            testSet.put(listOfFiles[i].getName(), n);
        }
        
        return testSet;
    }
    
    /*
    public static HashMap[] trainARFF(String trainDataSetPath) throws IOException{
        File folder = new File(trainDataSetPath);
        File[] listOfFiles = folder.listFiles();
        HashMap[] trainSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            fe.add(n);
            trainSet[i] = n;
        }

        arff.addTrainSet(trainSet);
        arff.saveTrainARFF("dataset/outputs/train.arff");
        
        return trainSet;
    }
        
    public static HashMap[] testARFF(String testDataSetPath) throws IOException{
        File folder = new File(testDataSetPath);
        File[] listOfFiles = folder.listFiles();
        
        HashMap[] testSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            testSet[i] = n;
        }

        arff.addTestSet(testSet);
        arff.saveTestARFF("dataset/outputs/test.arff");
        
        return testSet;
    }
    */
    
}
