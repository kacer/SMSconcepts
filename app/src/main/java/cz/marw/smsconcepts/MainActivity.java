package cz.marw.smsconcepts;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout conceptsWrapper;

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

        conceptsWrapper = (LinearLayout) findViewById(R.id.conceptsWrapper);

    }

    @Override
    protected void onResume() {
        super.onResume();
        createItems();
    }

    public void createConcept(View view) {
        Intent intent = new Intent(this, AddConcepts.class);
        //intent.putExtra("message", "Zpráva k editaci");
        startActivity(intent);
    }

    private void createItems() {
        conceptsWrapper.removeAllViews();
        List<Concept> concepts = DataManager.getInstance().getConcepts();
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
                    System.out.println("Long touch");
                    createLocalMenu(view.getId());
                    return true;
                }
            });
            //item.setTextAppearance(this, R.style.Concepts);
            item.setText(concept);
            conceptsWrapper.addView(item);
        }
    }

    public void createMessage(String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        //sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("sms_body", message);

        startActivity(sendIntent);
    }

    public void createLocalMenu(final int index) {
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

    public void editContept(int index) {
        System.out.println(index);
    }

    public void removeConcept(int index) {
        System.out.println(index);
    }

}
