package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Coin {
    private Bitmap image;
    private float x, y;
    private boolean collected = false;

    public Coin(Context context, float x, float y) {
        this.x = x;
        this.y = y;

        // Carrega e redimensiona a moeda
        Bitmap raw = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        this.image = Bitmap.createScaledBitmap(raw, 64, 64, true);
    }

    public void draw(Canvas canvas) {
        if (!collected) {
            canvas.drawBitmap(image, x, y, null);
        }
    }

    public RectF getBounds() {
        return new RectF(x, y, x + image.getWidth(), y + image.getHeight());
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public void move(float dx) {
        x += dx * 5;
    }

    public float getX() {
        return x;
    }

}
