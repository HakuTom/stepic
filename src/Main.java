import com.sun.deploy.xml.XMLable;

/**
 * Created by dokgo on 16.10.16.
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("hi");
    }
}

interface TextAnalyzer {
    Label processText(String text);
}

enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, OK
}

abstract class KeywordAnalyzer implements TextAnalyzer{
    abstract protected String[] getKeywords();
    abstract protected Label getLabel();

    public Label processText(String text){
        Label label = Label.OK;
        for(String key : getKeywords()){
            if ( text.contains(key) ) label = getLabel();
        }
        return label;
    }
}

class SpamAnalyzer extends KeywordAnalyzer {
    private String[] keywords;
    public SpamAnalyzer(String [] keywords){
        int length = keywords.length;
        this.keywords = new String [length];

        for(int i = 0; i < keywords.length; i++)
            this.keywords[i] = keywords[i];
    }

    protected String[] getKeywords(){
        return keywords;
    }
    protected Label getLabel(){return Label.SPAM; }
}

class NegativeTextAnalyzer extends KeywordAnalyzer{
    public NegativeTextAnalyzer(){}
    protected String[] getKeywords(){return new String[]{":(", "=(", ":|"};}

    protected Label getLabel(){
        return Label.NEGATIVE_TEXT;
    }
}

class TooLongTextAnalyzer implements TextAnalyzer{
    private int maxLength;

    public TooLongTextAnalyzer(int maxLength){
        this.maxLength = maxLength;
    }

    public Label processText(String text){
        if (text.length() > maxLength) return Label.TOO_LONG;
        return Label.OK;
    }
}