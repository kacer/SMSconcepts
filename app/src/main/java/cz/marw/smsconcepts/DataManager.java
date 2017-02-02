package cz.marw.smsconcepts;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Martinek on 18. 1. 2017.
 */

public class DataManager {

    public final static String FILE_NAME = "Concepts.csv";

    private final static String DELIMITER = "|$ß|";

    private List<Concept> concepts;

    public DataManager() {
        concepts = new ArrayList<>();
    }

    public void save(Context context) {
        try {
            FileWriter writer = new FileWriter(context.getFilesDir() + System.getProperty("file.separator") + FILE_NAME);
            for(Concept c : concepts) {
                writer.write(c.getName() + DELIMITER + c.getContent());
                writer.write(System.lineSeparator());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(Context context) {
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if(inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String row;
                while((row = reader.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(row, DELIMITER);
                    String name = st.nextToken();
                    String content = st.nextToken();
                    concepts.add(new Concept(name, content));
                }
                reader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Concept> getConcepts() {
        return concepts;
    }

    private static DataManager instance;

    public static DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static void addConcept(Concept concept, Context context) {
        getInstance().getConcepts().add(concept);
        getInstance().save(context);
    }

    public static void removeConcept(Concept concept, Context context) {
        getInstance().getConcepts().remove(concept);
        getInstance().save(context);
    }

    public static void loadData(Context context) {
        if(getInstance().getConcepts().isEmpty()) {
            getInstance().load(context);
        }
    }

}
