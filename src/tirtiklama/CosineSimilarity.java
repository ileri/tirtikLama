package tirtiklama;

public class CosineSimilarity {
    public static double similarity(double[] p1, double[] p2){
        double similarity = 0.0;
        if(p1.length != p2.length){
            System.out.println("Size of p1: " + p1.length + " , Size of p2: " + p2.length);
        }
        for(int i = 0; i < p1.length; i++){
            similarity += (p1[i] * p2[i]);
        }
        if(similarity > 1) return 1;
        if(similarity < 0) return 0;
        return similarity;
    }
}
