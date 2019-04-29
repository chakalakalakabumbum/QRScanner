package com.example.newpc.qrcode.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newpc.qrcode.R;

public class MainActivity extends AppCompatActivity {
    Button gen, scan;

    //Custom URL
    EditText customURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gen = (Button) this.findViewById(R.id.gen);
        scan = (Button) this.findViewById(R.id.scan);
        bindView();
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(MainActivity.this, GeneratorActivity.class);
                startActivity(gIntent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(MainActivity.this, ReaderActivity.class)
                        .putExtra("CUSTOM_URL", customURL.getText().toString());
                startActivity(rIntent);
            }
        });
    }

    private void bindView(){
        customURL = MainActivity.this.findViewById(R.id.url_text);
    }
}
