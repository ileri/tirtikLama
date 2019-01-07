package tirtiklama;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSimilarity {
    HashMap<String, Double> data1 = null;
    HashMap<String, Double> data2 = null;
    
    public static double similarity(double[] p1, double[] p2){
        double similarity = 0.0;
        for(int i = 0; i < p1.length; i++){
            similarity += (p1[i] * p2[i]);
        }
        return similarity;
    }
    
    public CosineSimilarity(HashMap<String, Double> d1, HashMap<String, Double> d2){
        data1 = d1;
        data2 = d2;
    }
    
    public double similarity(){
        double similarity = 0.0;
        
        Iterator i1 = data1.entrySet().iterator();
        while(i1.hasNext()){
            Map.Entry e = (Map.Entry)i1.next();
            Double val1 = getDoubleVal(e);
            
            Double val2 = data2.getOrDefault(e.getKey(), 0.0);
            //System.out.printf("key: %20s\tval1: %2.15f\tval2: %2.15f\n", e.getKey(), (double)val1, (double)val2);
            similarity += (val1 * val2);
        }
        //if(similarity > 1) return 1; // round 1.000000007 like values
        return similarity;
    }
    
    private double getDoubleVal(Map.Entry o){
        return (Double)o.getValue() == null ? 0 : (Double)o.getValue();
    }

}
