package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private int selectedCharacter = 0; // 0 = guerreiro, 1 = maga

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button warriorButton = findViewById(R.id.btnWarrior);
        Button mageButton = findViewById(R.id.btnMage);
        Button playButton = findViewById(R.id.btnPlay);

        warriorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCharacter = 0;
            }
        });

        mageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCharacter = 1;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(StartActivity.this, MainActivity.class);
                gameIntent.putExtra("character", selectedCharacter);
                startActivity(gameIntent);
                finish(); // fecha o menu
            }
        });
    }
}
