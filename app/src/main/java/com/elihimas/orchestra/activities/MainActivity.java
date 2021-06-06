package com.elihimas.orchestra.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elihimas.orchestra.R;
import com.elihimas.orchestra.activities.adapers.ExamplesAdapter;
import com.elihimas.orchestra.java.Orchestra;
import com.elihimas.orchestra.java.ViewReference;
import com.elihimas.orchestra.model.Examples;
import com.elihimas.orchestra.usecases.ExampleStarter;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private RecyclerView optionsRecycler;
    private ExampleStarter exampleStarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        animate();
    }

    private void init() {
        optionsRecycler = findViewById(R.id.optionsRecycler);
        exampleStarter = new ExampleStarter(this);


        RecyclerView optionsRecycler = findViewById(R.id.optionsRecycler);
        ExamplesAdapter adapter = new ExamplesAdapter(this::exampleSelected);
        optionsRecycler.hasFixedSize();
        optionsRecycler.setAdapter(adapter);
    }

    private void exampleSelected(Examples example) {
        Consumer<ViewReference> animation = createAnimation(example);

        runAndOpen(animation, example);
    }

    private Consumer<ViewReference> createAnimation(Examples example) {
        //TODO create proper animations to all cases
        Consumer<ViewReference> animation = ViewReference::fadeOut;
        switch (example) {
            case Scale:
                animation = createScaleAnimation();
                break;
            case Fade:
                animation = ViewReference::fadeOut;
                break;
            case Extensions:
                break;
            case Interpolator:
                break;
            case AnimateImage:
                break;
            case Bouncing:
                break;
            case BackgroundAndTextColor:
                break;
            case ConstrainsLayout:
                break;
            case CoordinatorLayout:
                break;
            case Slide:
                break;
        }

        return animation;
    }

    private Consumer<ViewReference> createScaleAnimation() {
        return viewReference -> {
            viewReference.scale(0, scaleAnimation -> {
                scaleAnimation.setDuration(400);
            });
        };
    }

    private void runAndOpen(Consumer<ViewReference> animation, Examples example) {
        //TODO run the animation or remove unused code
//        Orchestra
//                .launch(orchestra -> animation.accept(orchestra.on(optionsRecycler)))
//                .then(() -> exampleStarter.execute(example));
        exampleStarter.execute(example);
    }

    private void animate() {
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
