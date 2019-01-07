package tirtiklama;

import java.util.Arrays;

public class KNN {
    double[][] values;
    int k;
    
    KNN(double[][] values, int k){
        if(k % 2 == 0){
            System.err.println("k muts be odd");
            System.exit(1);
        }
        this.k = k;
        this.values = values;
    }
    
    public double[][] classify(double[] point){
        double[][] distances = new double[values.length][2];
        
        for(int i = 0; i < values.length; i++){
            distances[i][0] = i;
            distances[i][1] = cosine_distance(point, values[i]);
        }
        Arrays.sort(distances, (a, b) -> Double.compare(a[1], b[1]));
        
        double[][] returning = new double[k][2];
        for(int i = 0; i < returning.length; i++){
            returning[i] = distances[distances.length - i - 1];
        }
            
        return returning;
    }
    
    private double cosine_distance(double[] p1, double[] p2){
        return CosineSimilarity.similarity(p1, p2);
    }
}
