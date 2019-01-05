package tirtiklama;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class TirtikLama {
    static FeatureExtraction fe = new FeatureExtraction();
    static ARFF arff = new ARFF(fe);
    
    public static void main(String[] args) throws IOException {
        HashMap[] tr = trainARFF();
        HashMap[] ts = testARFF();
        
        for(int i = 0; i < ts.length; i++){
            for(int j = 0; j < tr.length; j++){
                CosineSimilarity cs1 = new CosineSimilarity(tr[j], ts[i]);
                System.out.println(i + " and " + j + " similarity: %"  + cs1.similarity()*100);
            }
        }
        /*
        double[] fe1 = fe.extractFeatures(n1);
        double[] fe2 = fe.extractFeatures(n2);
        
        System.out.println("Size: " + fe1.length + " - " + Arrays.toString(fe1));
        System.out.println("Size: " + fe2.length + " - " + Arrays.toString(fe2));

        CosineSimilarity cs1 = new CosineSimilarity(n1, n2);
        Iterator iterator =  n1.entrySet().iterator();
        
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        
        System.out.println("similarity: %"  + cs1.similarity()*100);
        
        */

    }
    
    public static HashMap[] trainARFF() throws IOException{
        File folder = new File("dataset/train");
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
        
    public static HashMap[] testARFF() throws IOException{
        File folder = new File("dataset/test");
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
    
    
}
