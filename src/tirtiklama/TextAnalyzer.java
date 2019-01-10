package tirtiklama;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import zemberek.morphology.TurkishMorphology;
import zemberek.morphology.analysis.SentenceAnalysis;
import zemberek.morphology.analysis.SingleAnalysis;

public class TextAnalyzer {
    private String content = null;
    private TurkishMorphology tm = TurkishMorphology.createWithDefaults();
    
    public TextAnalyzer(String filePath) throws FileNotFoundException, IOException{
        content = new String(Files.readAllBytes(Paths.get(filePath)));
    }
    
    public HashMap<String, Integer> getLemmas(){
        SentenceAnalysis result = tm.analyzeAndDisambiguate(content);
        List<SingleAnalysis> wa = result.bestAnalysis();
        HashMap<String, Integer> lemmas = new HashMap<String, Integer>();
        for(int i = 0; i < wa.size(); i++){
            String word = wa.get(i).getLemmas().get(0);
            if(isUseless(word)){
                continue;
            }
            Integer frequency = lemmas.get(word);
            lemmas.put(word, frequency == null ? 1 : frequency + 1);
        }
        //cleanUselesses(lemmas);
        return lemmas;
    }
    
    // Is this word shouldn't list on word frequenc list?
    private boolean isUseless(String w){
        return isConnective(w) || isTooShort(w) || isNonWord(w) || isUNK(w);
    }
    
    // Is this word a connective?
    private boolean isConnective(String w){
        String[] list = {"ve", "veya", "de", "da", "mı", "mi", "mu", "mü"};
        for(String conn: list){
            if(conn.equals(w)){
                return true;
            }
        }
            
        return false;
    }
    
    // Is this word is too short to be a regular word?
    private boolean isTooShort(String w){return w.length() < 2;}
    
    // Is this word consist of alphabetic characters? 
    private boolean isNonWord(String w){return !Pattern.matches("[a-zA-ZİÖÜŞÇĞıöüşçğ]+",w);}
    
    // Sometimes returning UNK, is this one of them?
    private boolean isUNK(String w){return w == "UNK";}
    
    private void cleanUselesses(HashMap<String, Integer> data){
        String[] list = {"ve", "veya", "de", "da", "mı", "mi", "mu", "mü"};
        for(String conn: list){
            data.remove(conn);
        }
    }
}
