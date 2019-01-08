package tirtiklama;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ARFF {
    private FeatureExtraction featureExtraction = null;
    private HashMap<String, Double>[] trainSet = null;
    private HashMap<String, Double>[] testSet = null;
    
    ARFF(FeatureExtraction fe){
        featureExtraction = fe;
    }
    
    public void addTrainSet(HashMap<String, Double>[] ts){
        trainSet = ts;
    }
    
    public void addTestSet(HashMap<String, Double>[] ts){
        testSet = ts;
    }
    /*
    public void saveTrainARFF(String filePath) throws IOException{
        String[] words = featureExtraction.wordsArray();
        String[] contents = new String[3 + words.length + trainSet.length ];
        int lineCounter = 0;
        contents[lineCounter++] = "@RELATION words";
        
        for(int i = 0; i < words.length; i++){
            contents[lineCounter++] = "@ATTRIBUTE " + words[i] + " NUMERIC";
        }
        contents[lineCounter++] = "@ATTRIBUTE class " + classAttribute();
        
        contents[lineCounter++] = "@DATA";
        
        for(int i = 0; i < trainSet.length; i++){
            contents[lineCounter++] = getARFFValues(trainSet[i]) + ", " + i;
        }
        
       writeStringArray(contents, filePath);
    }
    
    public void saveTestARFF(String filePath) throws IOException{
        String[] words = featureExtraction.wordsArray();
        String[] contents = new String[3 + words.length + testSet.length ];
        int lineCounter = 0;
        
        contents[lineCounter++] = "@RELATION words";
        
        for(int i = 0; i < words.length; i++){
            contents[lineCounter++] = "@ATTRIBUTE " + words[i] + " NUMERIC";
        }
        contents[lineCounter++] = "@ATTRIBUTE class " + classAttribute();
        
        contents[lineCounter++] = "@DATA";
        
        for(int i = 0; i < testSet.length; i++){
            contents[lineCounter++] = getARFFValues(testSet[i]) + ", 0";
        }
        
        writeStringArray(contents, filePath);
    }
    
    private String getWordValues(String word){
        double[] values = new double[trainSet.length];
        for(int i = 0; i < trainSet.length; i++){
            values[i] = trainSet[i].getOrDefault(word, 0.0);
        }
        
        return Arrays.stream(values).mapToObj(String::valueOf)
        .collect(Collectors.joining(", "));
    }
    

    private String getARFFValues(HashMap<String, Double> h){
        return Arrays.stream(featureExtraction.extractFeatures(h))
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(", "));
    }
    
    
    private void writeStringArray(String[] contents, String filePath) throws IOException{
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter(filePath));
        for (int i = 0; i < contents.length; i++){
            outputWriter.write(contents[i]);
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }
    
    private String classAttribute(){
        int[] classes = new int[trainSet.length];
        
        for(int i = 0; i < trainSet.length; i++){
            classes[i] = i;
        }
        
        return "{" + Arrays.stream(classes).mapToObj(String::valueOf)
        .collect(Collectors.joining(", ")) + "}";
    }
    */
}
