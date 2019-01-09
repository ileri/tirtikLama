package tirtiklama;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KNN {
    int k;
    FeatureExtraction fe;
    HashMap<String, HashMap> tr;
    
    KNN(FeatureExtraction fe, HashMap<String, HashMap> tr, int k){
        if(k % 2 == 0){
            System.err.println("k muts be odd");
            System.exit(1);
        }
        this.k = k;
        this.fe = fe;
        this.tr = tr;
    }
    
    public Object[][] classify(double[] point){
        HashMap<String, double[]> values = fe.extractFeatures(tr);
        Object[][] distances = new Object[values.size()][2];
        Iterator iterator = values.entrySet().iterator();
        
        int counter = 0;
        
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            HashMap<String, double[]> hm = new HashMap<String, double[]>();
            hm.put((String)entry.getKey(), (double[])entry.getValue());
            distances[counter][0] = (String)entry.getKey();
            distances[counter][1] = cosine_distance(point, (double[])entry.getValue());
            counter++;
        }

        Arrays.sort(distances, (a, b) -> Double.compare((double)a[1], (double)b[1]));
        
        Object[][] returning = new Object[k][2];
        for(int i = 0; i < returning.length; i++){
            returning[i] = distances[i];
        }
            
        return returning;
    }
    
    private double cosine_distance(double[] p1, double[] p2){
        return 1 - CosineSimilarity.similarity(p1, p2);
    }
    
    private double euclid_distance(double[] p1, double[] p2){
        double sum = 0;
        for(int i = 0; i < p1.length; i++){
            sum += Math.abs(p1[i] - p2[i]);
        }
        return Math.sqrt(sum);
    }
    
}
