package cz.marw.smsconcepts;


/**
 * Created by Martinek on 18. 1. 2017.
 */
public class Concept {

    private String name;
    private String content;

    public Concept(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
