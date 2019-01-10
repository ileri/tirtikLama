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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

final class SingleTrainData{
    public String name;
    public double[] values;
    
    SingleTrainData(String name, double[] values){
        this.name = name;
        this.values = values;
    }
}

final class TrainModel{
    public List<String> words;
    public List<SingleTrainData> data;
    
    TrainModel(List<String> words, List<SingleTrainData> data){
        this.words = words;
        this.data = data;
    }
    
    public int[] getModelSize(){
        int[] sizes = {data.size(), data.get(0).values.length};
        return sizes;
    }
    
    public double[][] getDataArray(){
        double[][] array = new double[data.size()][];
        Iterator<SingleTrainData> iterator = data.iterator();
        int i = 0;
        while(iterator.hasNext()){
            array[i++] = iterator.next().values;
        }
        
        double[][] t = new double[array[0].length][array.length];
        for(i = 0; i < t.length; i++){
            for(int j = 0; j < t[0].length; j++){
                t[i][j] = array[j][i];
            }
        }
        System.out.println("OK");
        return t;
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

    public void saveTrainARFF(File[] listOfFiles, String filePath) throws IOException{
        String[] words = featureExtraction.wordsArray();
        String[] contents = new String[3 + words.length + trainSet.length ];
        int lineCounter = 0;
        contents[lineCounter++] = "@RELATION words";
        
        for(int i = 0; i < words.length; i++){
            contents[lineCounter++] = "@ATTRIBUTE " + words[i] + " NUMERIC";
        }
        contents[lineCounter++] = "@ATTRIBUTE class " + classAttribute(listOfFiles);
        
        contents[lineCounter++] = "@DATA";
        
        for(int i = 0; i < trainSet.length; i++){
            contents[lineCounter++] = getARFFValues(trainSet[i]) + ", " + listOfFiles[i].getName();
        }
        
       writeStringArray(contents, filePath);
    }
    
    public void saveTestARFF(File[] listOfFiles, String filePath) throws IOException{
        String[] words = featureExtraction.wordsArray();
        String[] contents = new String[3 + words.length + testSet.length ];
        int lineCounter = 0;
        
        contents[lineCounter++] = "@RELATION words";
        
        for(int i = 0; i < words.length; i++){
            contents[lineCounter++] = "@ATTRIBUTE " + words[i] + " NUMERIC";
        }
        contents[lineCounter++] = "@ATTRIBUTE class " + classAttribute(listOfFiles);
        
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
    
    private String classAttribute(File[] listOfFiles){
        String[] classes = new String[trainSet.length];
        
        for(int i = 0; i < trainSet.length; i++){
            classes[i] = listOfFiles[i].getName();
        }
        
        return "{" + Arrays.stream(classes).collect(Collectors.joining(", ")) + "}";
    }
    
    public static TrainModel readTrainModel(String fileName) throws Exception{
        FileInputStream fstream = new FileInputStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        List<String> wordList = new ArrayList<String>();
        List<SingleTrainData> valueList = new ArrayList<SingleTrainData>();

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
                    String[] line = strLine.split(", ");
                    
                    valueList.add(
                        new SingleTrainData(
                                line[line.length-1], 
                                Arrays.stream(Arrays.copyOfRange(line, 0, (line.length - 1)))
                                .mapToDouble(Double::parseDouble).toArray()
                        )
                    );
                }
            }
        }

        //Close the input stream
        fstream.close();
        return new TrainModel(wordList, valueList);
    }
}
