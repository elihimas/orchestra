package com.elihimas.orchestra.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elihimas.orchestra.Orchestra;
import com.elihimas.orchestra.R;
import com.elihimas.orchestra.activities.adapers.ExamplesAdapter;
import com.elihimas.orchestra.usecases.ExampleStarter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        animate();
    }

    private void init() {
        ExampleStarter exampleStarter = new ExampleStarter(this);
        RecyclerView optionsRecycler = findViewById(R.id.optionsRecycler);
        ExamplesAdapter adapter = new ExamplesAdapter(optionsRecycler, example -> exampleStarter.execute(example));
        optionsRecycler.hasFixedSize();
        optionsRecycler.setAdapter(adapter);
    }

    private void animate() {
        RecyclerView optionsRecycler = findViewById(R.id.optionsRecycler);

        Orchestra.onRecycler(optionsRecycler).slideAppear();
        Orchestra.setup(setupContext -> {
            setupContext.on(optionsRecycler).alpha(0);
        });

        Orchestra.launch(orchestra -> {
            orchestra.delay(200);
            orchestra.on(optionsRecycler).fadeIn();
        });
    }
}
