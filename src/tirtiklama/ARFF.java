package tirtiklama;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

final class TrainModel{
    public List<String> words;
    public List<double[]> values;
    
    TrainModel(List<String> words, List<double[]> values){
        this.words = words;
        this.values = values;
    }
    
    public int[] getModelSize(){
        int[] sizes = {values.size(), values.get(0).length};
        return sizes;
    }
}

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
        return Arrays.stream(featureExtraction.extractTextFeatures(h))
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
    
    public static TrainModel readTrainModel(String fileName) throws Exception{
        FileInputStream fstream = new FileInputStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        List<String> wordList = new ArrayList<String>();
        List<double[]> valueList = new ArrayList<double[]>();
        
        
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            String[] tmp_values = strLine.split(" ");
            
            // Read attributes
            if(tmp_values[0].equals("@ATTRIBUTE") && !tmp_values[1].equals("class")){
                wordList.add(tmp_values[1]);
            }
            
            // Read data
            if(tmp_values[0].equals("@DATA")){
                while((strLine = br.readLine()) != null){
                    valueList.add(Arrays.stream(strLine.split(", "))
                        .mapToDouble(Double::parseDouble)
                        .toArray());
                }
            }
        }

        //Close the input stream
        fstream.close();
        return new TrainModel(wordList, valueList);
    }
}
