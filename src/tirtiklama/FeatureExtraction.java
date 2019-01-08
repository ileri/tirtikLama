package tirtiklama;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FeatureExtraction {
    private Set<String> words = new HashSet<String>();
    
    FeatureExtraction(){}
    
    FeatureExtraction(HashMap<String, Double>[] hm){
        add(hm);
    }
    
    public String[] wordsArray(){
        return Arrays.copyOf(words.toArray(), words.size(), String[].class);
    }
    
    public void add(HashMap<String, Double>[] hm){
        for(HashMap<String, Double> h : hm){
            add(h);
        }
    }
    
    public void add(HashMap<String, Double> h){
        Iterator i = h.entrySet().iterator();
        while(i.hasNext()){
            words.add((String)(((Map.Entry)i.next())).getKey());
        }
    }
    
    public double[] extractTextFeatures(HashMap<String, Double> h){
        add(h);
        double[] features = new double[words.size()];
        
        Iterator i = words.iterator();
        int count = 0;
        while(i.hasNext()){
            features[count++] = h.getOrDefault(((String)i.next()), 0.0);
        }
        
        return features;
    }
    
    public double[][] extractFeatures(HashMap<String, Double>[] hm){
        double[][] features = new double[hm.length][];
        
        int count = 0;
        for(HashMap<String, Double> h :hm){
            features[count++] = extractTextFeatures(h);
        }
        
        return features;
    }
    
    public HashMap extractFeatures(HashMap<String, HashMap> hm){
        HashMap<String, double[]> features = new HashMap<String, double[]>();
        Iterator iterator = hm.entrySet().iterator();
        
        while(iterator.hasNext()){
            Map.Entry next = (Map.Entry)iterator.next();
            features.put((String)next.getKey(), extractTextFeatures((HashMap<String, Double>)next.getValue()));
        }
        
        return features;
    }
}
