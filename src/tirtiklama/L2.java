package tirtiklama;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class L2 {
    double norm = 0.0;
    Iterator iterator = null;
    HashMap<String, Integer> data = null;
    public L2(HashMap<String, Integer> dataParam){
        data = dataParam;
        iterator = data.entrySet().iterator();
        while(iterator.hasNext()){
            double value = new Double((int)((Map.Entry)iterator.next()).getValue());
            norm += value * value;
        }
        norm = Math.sqrt(norm);
    }
    
    public HashMap<String, Double> normalize(){
        HashMap<String, Double> newData = new HashMap<String, Double>();
        iterator = data.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            newData.put((String)entry.getKey(),new Double((int)entry.getValue()/norm));
        }

        return newData;
    }
    
}
