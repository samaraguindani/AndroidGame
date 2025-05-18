package com.example.game;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recebe o personagem selecionado da StartActivity (0 = guerreiro, 1 = maga)
        int selectedCharacter = getIntent().getIntExtra("character", 0);

        FrameLayout layout = new FrameLayout(this);
        gameView = new GameView(this, selectedCharacter); // passa personagem
        layout.addView(gameView);

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
