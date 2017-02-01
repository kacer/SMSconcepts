package cz.marw.smsconcepts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout conceptsWrapper;
    private TextView tvNoConcept;

    private List<Concept> concepts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        File file = new File(getFilesDir(), DataManager.FILE_NAME);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DataManager.loadData(this);
        concepts = DataManager.getInstance().getConcepts();

        conceptsWrapper = (LinearLayout) findViewById(R.id.conceptsWrapper);
        tvNoConcept = (TextView) findViewById(R.id.tvNoConcept);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!concepts.isEmpty())
            createItems();
        else {
            conceptsWrapper.removeAllViews();
            conceptsWrapper.addView(tvNoConcept);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.deleteAll:
                if(!concepts.isEmpty())
                    deleteAllConcepts();
                return true;
            case R.id.about:
                aboutApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createConcept(View view) {
        Intent intent = new Intent(this, AddConcepts.class);
        startActivity(intent);
    }

    private void createItems() {
        conceptsWrapper.removeAllViews();
        for(int i = 0; i < concepts.size(); i++) {
            final String concept = concepts.get(i).getContent();
            Button item = new Button(this);
            item.setId(i);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createMessage(concept);
                }
            });
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    createLocalMenu(view.getId());
                    return true;
                }
            });
            item.setTextAppearance(this, R.style.Concepts);
            item.setBackgroundResource(R.drawable.item_concept_style);
            item.setHeight(160);
            item.setMaxLines(2);
            item.setText(concept);
            conceptsWrapper.addView(item);
        }
    }

    private void createMessage(String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        //sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("sms_body", message);

        startActivity(sendIntent);
    }

    private void createLocalMenu(final int index) {
        String[] items = {"Upravit", "Smazat"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Co chceš dělat?")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            editContept(index);
                        } else if(i == 1) {
                            removeConcept(index);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editContept(int index) {
        Intent intent = new Intent(this, AddConcepts.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    private void removeConcept(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Víš co děláš...");
        builder.setMessage("Fakt to chceš zahodit?");
        builder.setPositiveButton("Hai", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Concept concept = concepts.get(index);
                DataManager.removeConcept(concept, MainActivity.this);
                onResume();
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

    private void deleteAllConcepts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Víš co děláš...");
        builder.setMessage("Fakt to chceš zahodit?");
        builder.setPositiveButton("Hai", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                concepts.clear();
                DataManager.getInstance().save(MainActivity.this);
                onResume();
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

    private void aboutApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("O aplikaci");
        builder.setMessage("Aplikaci vytvořil Martin Donát \u00a9 2017 \npro Petra Hejduka.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
