package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Obstacle {
    private Bitmap image;
    private float x, y;
    private boolean active = true;

    public Obstacle(Context context, float x, float y) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.rock);
        this.image = Bitmap.createScaledBitmap(this.image, 128, 128, true); // ajuste se necessário
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        if (active) {
            canvas.drawBitmap(image, x, y, null);
        }
    }

    public void move(float dx) {
        x += dx;
    }

    public RectF getBounds() {
        return new RectF(x, y, x + image.getWidth(), y + image.getHeight());
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public float getX() {
        return x;
    }
}
