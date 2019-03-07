package tirtiklama;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


final class Distance{
    public String name;
    public double distance;
    
    Distance(String name, double distance){
        this.name = name;
        this.distance = distance;
    }
}

final class ClassifyResult{
    public String name;
    public List<Distance> classifyResults;
    
    ClassifyResult(String name){
        this.name = name;
        classifyResults = new ArrayList<Distance>();
    }
    
    ClassifyResult(String name, List<Distance> classifyResults){
        this.name = name;
        this.classifyResults = classifyResults;
    }
    
    public void add(Distance r){
        classifyResults.add(r);
    }
    
    public void add(String name, double distance){
        add(new Distance(name, distance));
    }
    
    public ClassifyResult selectFirst(int k){
        sortDistances();
        return new ClassifyResult(name, classifyResults.subList(0, k));
    }
    
    public void print(){
        for(Distance r : classifyResults){
            System.out.printf("Distance between %30s and %30s is: %f\t( Similarity: %% %f )\n", name, r.name, r.distance, (1-r.distance)*100 );
        }
    }
    
    private void sortDistances(){
        Collections.sort(classifyResults, new Comparator<Distance>() {
            @Override
            public int compare(Distance s1, Distance s2) {
                return Double.valueOf(s1.distance)
                        .compareTo(Double.valueOf(s2.distance));
            }
        });
    }
}


public class KNN {
    int k;
    FeatureExtraction fe;
    TrainModel tm;
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

    KNN(int k, TrainModel tm) {
        if(k % 2 == 0){
            System.err.println("k muts be odd");
            System.exit(1);
        }
        this.k = k;
        this.tm = tm;
    }
    
    public ClassifyResult classify(String name, double[] point){
        ClassifyResult cr = new ClassifyResult(name);
        for(SingleTrainData t : tm.data){
            cr.add(t.name, cosine_distance(point, t.values));
        }
        return cr.selectFirst(k);
    }
    
    private double cosine_distance(double[] p1, double[] p2){
        return 1 - CosineSimilarity.similarity(p1, p2);
    }
}
