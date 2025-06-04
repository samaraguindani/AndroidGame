package com.example.game;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int selectedCharacter = getIntent().getIntExtra("character", 0);

        FrameLayout layout = new FrameLayout(this);
        gameView = new GameView(this, selectedCharacter);
        layout.addView(gameView);

        // Criação do botão de pause/resume
        Button pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setAlpha(0.7f); // botão semi-transparente
        pauseButton.setTextSize(18);
        pauseButton.setPadding(10, 5, 10, 5);

        // Estilo e posição
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = 50;
        params.topMargin = 150;
        pauseButton.setLayoutParams(params);

        layout.addView(pauseButton);

        // Alternância Pause/Resume
        pauseButton.setOnClickListener(v -> {
            if (gameView.isPaused()) {
                gameView.resume();
                pauseButton.setText("Pause");
            } else {
                gameView.pause();
                pauseButton.setText("Resume");
            }
        });

        setContentView(layout);
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
