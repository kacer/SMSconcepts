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
import android.widget.Toast;

public class AddConcepts extends AppCompatActivity {

    private EditText etName, etConcept;
    private Button btnThrow;

    private Concept concept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concepts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        etName = (EditText) findViewById(R.id.etName);
        etConcept = (EditText) findViewById(R.id.etConcept);
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
            etName.setText(concept.getName());
            etConcept.setText(concept.getContent());
        }
    }

    @Override
    public void onBackPressed() {
        throwAway();
    }

    public void throwAway() {
        String name = etName.getText().toString();
        String text = etConcept.getText().toString();
        if(text.trim().equals("") && name.trim().equals("")) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddConcepts.this);
            builder.setTitle(getString(R.string.alert_dialog_title));
            builder.setMessage(getString(R.string.alert_dialog_message));
            builder.setPositiveButton(getString(R.string.alert_dialog_positive_button), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.alert_dialog_negative_button), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void saveConcept(View view) {
        String name = etName.getText().toString();
        String text = etConcept.getText().toString();
        if(!text.trim().equals("") && !name.trim().equals("")) {
            text = text.replace("\n", "");
            if(concept == null) {
                Concept concept = new Concept(name, text);
                DataManager.addConcept(concept, this);
            } else {
                concept.setName(name);
                concept.setContent(text);
                DataManager.getInstance().save(this);
            }
            finish();
        } else {
            Toast.makeText(this, getString(R.string.toast_must_be_filled), Toast.LENGTH_LONG).show();
        }

    }

}
