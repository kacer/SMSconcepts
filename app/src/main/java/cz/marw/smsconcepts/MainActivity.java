package cz.marw.smsconcepts;

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
        startActivity(intent);
    }

    private void createItems() {
        conceptsWrapper.removeAllViews();
        //ScrollView sc = new ScrollView(this); ZBYTECNE
        for(final Concept c : DataManager.getInstance().getConcepts()) {
            Button item = new Button(this);
            //item.setMaxLines(1);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createMessage(c.getContent());
                }
            });
            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    System.out.println("Long touch");
                    return true;
                }
            });
            item.setTextAppearance(this, R.style.Concepts);
            item.setText(c.getContent());
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

}
