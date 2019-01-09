package tirtiklama;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class TirtikLama {
    
    static int K = 3;
    
    static FeatureExtraction fe = new FeatureExtraction();
    static ARFF arff = new ARFF(fe);
    static String trainDataSetPath = "dataset/train";
    static String testDataSetPath  = "dataset/test";
    static String modelPath        = "dataset/model/textTrainModel.arff";
    
    public static void main(String[] args) throws Exception {
        File  trainModelFile = new File(modelPath);
        if(!trainModelFile.exists()){
            System.out.println("Model Training...");
            trainARFF(trainDataSetPath);
            // Halihazırda eğitilmiş model yoksa eğitim yap
            
        }
        // Zaten eğitilmiş bir model varsa onu kullan
        System.out.println("Reading Trained Model");
        TrainModel tm = ARFF.readTrainModel(modelPath);
        for(SingleTrainData t : tm.data){
            System.out.println(Arrays.toString(t.values));
        }
        System.out.println("Model Size: " + Arrays.toString(tm.getModelSize()));
        
        KNN knn = new KNN(K, tm);
        
        // Test
        System.out.println("Testing...");
        HashMap<String, HashMap> ts = test(tm, testDataSetPath);
        Iterator testIterator = ts.entrySet().iterator();
        System.out.println("Testing...");
        while(testIterator.hasNext()){
            Map.Entry t = (Map.Entry)testIterator.next();
            Object[] object_array = ((HashMap)t.getValue()).values().toArray();
            double[] double_array = new double[object_array.length];
            for(int i = 0; i < object_array.length; i++){
                double_array[i] = (double)object_array[i];
            }
            knn.classify((String)t.getKey(), double_array).print();

            System.out.println("");
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
            System.out.println("");
            trainSet.put(listOfFiles[i].getName(), n);
        }
        return trainSet;
    }
    
    private static HashMap test(TrainModel tm, String testDataSetPath) throws IOException{
        File folder = new File(testDataSetPath);
        File[] listOfFiles = folder.listFiles();
        HashMap<String, HashMap> testSet = new HashMap<String, HashMap>(); 
        //HashMap[] trainSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            TextAnalyzer textAnalyzer = new TextAnalyzer(listOfFiles[i].getAbsolutePath());
            HashMap<String, Integer> lemmas = textAnalyzer.getLemmas();
            HashMap<String, Double> normalized = new L2(lemmas).normalize();
            HashMap<String, Double> singleTestData = getTestFeatures(tm, normalized);
            testSet.put(listOfFiles[i].getName(), singleTestData);
        }
        
        return testSet;
    }
    
    private static HashMap<String, Double> getTestFeatures(TrainModel tm, HashMap<String, Double> normalized){
        HashMap<String, Double> features = new HashMap<String, Double>();
        for(String word : tm.words){
            features.put(word, normalized.getOrDefault(word, 0.0));
        }
        return features;
    }
    
    private static void showAllSimilarities(HashMap<String, HashMap> tr, HashMap<String, HashMap> ts) throws IOException{
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

        trainARFF(trainDataSetPath);
        testARFF(testDataSetPath);
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
        
    private static HashMap[] testARFF(String testDataSetPath) throws IOException{
        File folder = new File(testDataSetPath);
        File[] listOfFiles = folder.listFiles();
        
        HashMap[] testSet = new HashMap[listOfFiles.length];
        
        for (int i = 0; i < listOfFiles.length; i++) {
            HashMap<String, Double> n = new L2(new TextAnalyzer(listOfFiles[i].getAbsolutePath()).getLemmas()).normalize();
            testSet[i] = n;
        }

        arff.addTestSet(testSet);
        arff.saveTestARFF(listOfFiles, "/dataset/outputs/test.arff");
        
        return testSet;
    }
    
}
