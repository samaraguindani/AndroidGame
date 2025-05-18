package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;

public class Player {
    private Bitmap[][] spriteFrames; // [personagem][frame]
    private int currentCharacter = 0; // 0: guerreiro, 1: maga
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int frameInterval = 150;
    private float x = 100, y = 1200;
    private float velocityY = 0;
    private boolean jumping = false;
    private Context context;

    public Player(Context context) {
        this.context = context;
        loadSprites();
    }

    private void loadSprites() {
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_spritesheet);
        int frameWidth = spriteSheet.getWidth() / 3;
        int frameHeight = spriteSheet.getHeight() / 2;

        spriteFrames = new Bitmap[2][3];

        // Guerreiro (linha 0)
        for (int i = 0; i < 3; i++) {
            spriteFrames[0][i] = Bitmap.createBitmap(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
        }

        // Maga (linha 1)
        for (int i = 0; i < 3; i++) {
            spriteFrames[1][i] = Bitmap.createBitmap(spriteSheet, i * frameWidth, frameHeight, frameWidth, frameHeight);
        }
    }


    public void update(float accelX) {
        x += accelX * 5;

        if (jumping) {
            velocityY += 1;
            y += velocityY;
            if (y >= 1200) {
                y = 1200;
                jumping = false;
                velocityY = 0;
            }
        }

        // Atualiza animação
        long now = System.currentTimeMillis();
        if (now - lastFrameTime > frameInterval) {
            currentFrame = (currentFrame + 1) % spriteFrames[currentCharacter].length;
            lastFrameTime = now;
        }
    }

    public void draw(Canvas canvas) {
        Bitmap frame = spriteFrames[currentCharacter][currentFrame];
        canvas.drawBitmap(frame, x, y, null);
    }

    public void jump() {
        if (!jumping) {
            jumping = true;
            velocityY = -30;
            MediaPlayer.create(context, R.raw.jump_sound).start(); // jump_sound.wav em res/raw
        }
    }

    public void swapCharacter() {
        currentCharacter = (currentCharacter + 1) % 2;
    }

    public RectF getBounds() {
        Bitmap frame = spriteFrames[currentCharacter][currentFrame];
        return new RectF(x, y, x + frame.getWidth(), y + frame.getHeight());
    }

    public float getX() {
        return x;
    }
}
