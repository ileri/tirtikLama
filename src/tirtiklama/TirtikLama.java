package tirtiklama;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class TirtikLama {
    public static void main(String[] args) throws IOException {
        TextAnalyzer textAnalyzer1 = new TextAnalyzer("README.md");
        TextAnalyzer textAnalyzer2 = new TextAnalyzer("notREADME.md");
        
        HashMap<String, Integer> results1 = textAnalyzer1.getLemmas();
        HashMap<String, Integer> results2 = textAnalyzer2.getLemmas();
        
        HashMap<String, Double> n1  = new L2(results1).normalize();
        HashMap<String, Double> n2  = new L2(results2).normalize();
        
        FeatureExtraction fe = new FeatureExtraction();
        fe.add(n1);
        fe.add(n2);
        
        ARFF arff = new ARFF(fe);
        HashMap[] trainSet = new HashMap[2];
        trainSet[0] = n1;
        trainSet[1] = n2;
        arff.addTrainSet(trainSet);
        arff.saveTrainARFF("deneme.arff");
        
        HashMap[] testSet = new HashMap[1];
        testSet[0] = n1;
        arff.addTestSet(testSet);
        arff.saveTestARFF("denemeT.arff");
        
        double[] fe1 = fe.extractFeatures(n1);
        double[] fe2 = fe.extractFeatures(n2);
        
        System.out.println("Size: " + fe1.length + " - " + Arrays.toString(fe1));
        System.out.println("Size: " + fe2.length + " - " + Arrays.toString(fe2));

        CosineSimilarity cs1 = new CosineSimilarity(n1, n2);
        Iterator iterator =  n1.entrySet().iterator();
        
        /*while(iterator.hasNext()){
            System.out.println(iterator.next());
        }*/
        
        System.out.println("similarity: %"  + cs1.similarity()*100);

    }
    
    
}
