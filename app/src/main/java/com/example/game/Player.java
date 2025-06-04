package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.media.MediaPlayer;

public class Player {
    private Bitmap[] walkFrames;
    private Bitmap jumpFrame;
    private int currentFrame = 0;
    private long lastFrameTime = 0;
    private int frameInterval = 150;
    private float x = 100, y = 1400;
    private float velocityY = 0;
    private boolean jumping = false;
    private Context context;
    private int currentCharacter = 0; // 0: menino, 1: menina

    public Player(Context context, int selectedCharacter) {
        this.context = context;
        this.currentCharacter = selectedCharacter;
        loadSprites();
    }

    private void loadSprites() {
        Bitmap spriteSheet;
        if (currentCharacter == 0) {
            spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy_sheet);
        } else {
            spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl_sheet);
        }

        int frameWidth = spriteSheet.getWidth() / 6;
        int frameHeight = spriteSheet.getHeight() / 2;

        walkFrames = new Bitmap[6];

        for (int i = 0; i < 6; i++) {
            Bitmap original = Bitmap.createBitmap(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);

            if (currentCharacter == 1) {
                int scaledW = (int) (frameWidth * 1.5f);
                int scaledH = (int) (frameHeight * 1.5f);
                walkFrames[i] = Bitmap.createScaledBitmap(original, scaledW, scaledH, true);
            } else {
                walkFrames[i] = original;
            }
        }

        Bitmap jumpOriginal = Bitmap.createBitmap(spriteSheet, 2 * frameWidth, frameHeight, frameWidth, frameHeight);
        if (currentCharacter == 1) {
            int scaledW = (int) (frameWidth * 1.5f);
            int scaledH = (int) (frameHeight * 1.5f);
            jumpFrame = Bitmap.createScaledBitmap(jumpOriginal, scaledW, scaledH, true);
            y = 1400; // opcional: ajusta a altura da menina
        } else {
            jumpFrame = jumpOriginal;
            y = 1400;
        }
    }

    public void update(float accelX) {
        x += accelX * 5;

        if (jumping) {
            velocityY += 1;
            y += velocityY;
            if (y >= 1400) {
                y = 1400;
                jumping = false;
                velocityY = 0;
            }
        }

        if (!jumping) {
            long now = System.currentTimeMillis();
            if (now - lastFrameTime > frameInterval) {
                currentFrame = (currentFrame + 1) % walkFrames.length;
                lastFrameTime = now;
            }
        }
    }

    public void draw(Canvas canvas) {
        Bitmap frame = jumping ? jumpFrame : walkFrames[currentFrame];
        canvas.drawBitmap(frame, x, y, null);
    }

    public void jump() {
        if (!jumping) {
            jumping = true;
            velocityY = -30;
            MediaPlayer.create(context, R.raw.jump_sound).start();
        }
    }

    public void swapCharacter() {
        currentCharacter = (currentCharacter + 1) % 2;
        loadSprites(); // recarrega sprites do personagem novo
    }

    public RectF getBounds() {
        Bitmap frame = jumping ? jumpFrame : walkFrames[currentFrame];
        return new RectF(x, y, x + frame.getWidth(), y + frame.getHeight());
    }

    public float getX() {
        return x;
    }
}
