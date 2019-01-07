package tirtiklama;

public class NaiveBayes {
    double[][] values;
    
    NaiveBayes(double[][] v){
        values = v;
        int i = 0;
        for(double[] val : values){
            System.out.printf("%f ", val[i]);
            System.out.println("numericP: " + numericP(val[i], 0.12));
        }
        System.out.println("");
        System.out.println("mean: " + mean(i));
        System.out.println("variance: " + variance(i));
        System.out.println("stddev: " + stddev(i));
    }
    
    private double numericP(double i, double v){
        double mean = i;
        double stddev = 0.000000001;
        
        return (1 / Math.sqrt(2 * Math.PI * Math.pow(stddev, 2))) * Math.pow(Math.E, ((0.0-0.5)*Math.pow(((v - mean)/stddev), 2)));
    }
    
    private double mean(int i){
        double sum = 0;
        
        for(double[] val : values){
            sum += val[i];
        }
        
        return sum / values.length;
    }
    
    private double variance(int i){
        double mean = mean(i);
        double tmp = 0;
        
        for(double[] val : values){
            tmp += (val[i] - mean) * (val[i] + mean);
        }
        return tmp/(values.length-1);
    }
    
    private double stddev(int i){
       return Math.sqrt(variance(i));
    }
}
