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

    private EditText concept;
    private Button btnThrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concepts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        concept = (EditText) findViewById(R.id.dtConcept);
        btnThrow = (Button) findViewById(R.id.btnThrow);
        btnThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throwAway();
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        if(message != null)
            concept.setText(message);

    }

    @Override
    public void onBackPressed() {
        throwAway();
    }

    public void throwAway() {
        if(concept.getText().toString().trim().equals("")) {
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
        String text = this.concept.getText().toString();
        if(!text.trim().equals("")) {
            text = text.replace("\n", "");
            Concept concept = new Concept(text);
            DataManager.addConcept(concept, this);
            finish();
        }

    }

}
