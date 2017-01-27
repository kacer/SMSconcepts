package cz.marw.smsconcepts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddConcepts extends AppCompatActivity {

    private EditText etConcept;
    private Button btnThrow;

    private Concept concept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concepts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        etConcept = (EditText) findViewById(R.id.dtConcept);
        btnThrow = (Button) findViewById(R.id.btnThrow);
        btnThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throwAway();
            }
        });

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        if(index >= 0) {
            concept = DataManager.getInstance().getConcepts().get(index);
            etConcept.setText(concept.getContent());
        }
    }

    @Override
    public void onBackPressed() {
        throwAway();
    }

    public void throwAway() {
        if(etConcept.getText().toString().trim().equals("")) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddConcepts.this);
            builder.setTitle("Víš co děláš...");
            builder.setMessage("Fakt to chceš zahodit?");
            builder.setPositiveButton("Hai", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("Iie", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void saveConcept(View view) {
        String text = this.etConcept.getText().toString();
        if(!text.trim().equals("")) {
            text = text.replace("\n", "");
            if(concept == null) {
                Concept concept = new Concept(text);
                DataManager.addConcept(concept, this);
            } else {
                concept.setContent(text);
                DataManager.getInstance().save(this);
            }
            finish();
        }

    }

}
