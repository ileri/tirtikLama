package tirtiklama;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSimilarity {
    HashMap<String, Double> data1 = null;
    HashMap<String, Double> data2 = null;
    
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
            
            Double val2 = getDoubleVal(data2.get(e.getKey()));
            System.out.println("key: " + e.getKey() + "\tval1: " + val1 + "\tval2: " + val2);
            similarity += (val1 * val2);
        }
        //if(similarity > 1) return 1; // round 1.000000007 like values
        return similarity;
    }
    
    private double getDoubleVal(Map.Entry o){
        return (Double)o.getValue() == null ? 0 : (Double)o.getValue();
    }
    
    private double getDoubleVal(Double d){
        return d == null ? 0 : d;
    }
}
