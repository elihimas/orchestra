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
        Consumer<ViewReference> animation = createFadeAnimation(200);
        switch (example) {
//            case Splash:
//                break;
            case Scale:
                animation = createScaleAnimation();
                break;
            case Fade:
                animation = createFadeAnimation(600);
                break;
            case Extensions:
                break;
            case Interpolator:
                break;
//            case AnimateImage:
//                break;
            case Bouncing:
                break;
            case BackgroundAndTextColor:
                break;
            case ConstrainsLayout:
                break;
//            case CoordinatorLayout:
//                break;
            case Slide:
                break;
        }

        return animation;
    }

    private Consumer<ViewReference> createFadeAnimation(int duration) {
        return viewReference -> viewReference.fadeOut(fadeOutAnimation -> {
            fadeOutAnimation.setDuration(duration);
        });
    }

    private Consumer<ViewReference> createScaleAnimation() {
        return viewReference -> viewReference
                .parallel(parallel -> {
                            parallel.scale(0.3f, scaleAnimation -> {
                                scaleAnimation.setDuration(200);
                            });
                            parallel.fadeOut(fadeOutAnimation -> {
                                fadeOutAnimation.setDuration(200);
                            });
                        }
                );
    }

    private void runAndOpen(Consumer<ViewReference> animation, Examples example) {
        Orchestra
                .launch(orchestra -> {
                    ViewReference optionsRecycleReference = orchestra.on(optionsRecycler);
                    animation.accept(optionsRecycleReference);
                })
                .then(() -> exampleStarter.execute(example));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Orchestra.setup(setupContext ->
                setupContext.on(optionsRecycler)
                        .alpha(1)
                        .scale(1));
    }

    private void animate() {
        Orchestra.onRecycler(optionsRecycler).slideAppear();

        Orchestra.launch(orchestra -> {
            orchestra.delay(200);
            orchestra.on(optionsRecycler).fadeIn();
        });
    }
}
