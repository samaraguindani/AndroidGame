package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private GameThread thread;
    private Player player;
    private SensorManager sensorManager;
    private Bitmap background;
    private MediaPlayer backgroundMusic;
    private Random rand = new Random();
    private float accelX = 0;
    private List<Coin> coins = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private int score = 0;

    public GameView(Context context, int character) {
        super(context);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        getHolder().addCallback(this);

        player = new Player(context, character);

        //grear moedas random
        int numCoins = rand.nextInt(6) + 5; // entre 5 e 10 moedas
        for (int i = 0; i < numCoins; i++) {
            float x = rand.nextInt(1200) + 800; // entre 800 e 3000 (fora da tela)
            float y = 990 + rand.nextInt(21);   // entre 990 e 1010
            coins.add(new Coin(context, x, y));
        }

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        thread = new GameThread(getHolder(), this);
        setFocusable(true);

        backgroundMusic = MediaPlayer.create(context, R.raw.background_music);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1.0f, 1.0f);
        backgroundMusic.start();

    }

    public void update() {
        player.update(0);

        // Atualiza moedas e verifica colisões
        for (Coin coin : coins) {
            coin.move(-2); // move moeda para esquerda

            if (!coin.isCollected() &&
                    android.graphics.RectF.intersects(player.getBounds(), coin.getBounds())) {
                coin.collect();
                MediaPlayer.create(getContext(), R.raw.coin_sound).start();
                score++;
            }
        }

        // Geração controlada por distância real entre moedas
        if (coins.size() == 0 || (coins.get(coins.size() - 1).getX() < getWidth() - 800)) {
            float x = getWidth() + rand.nextInt(300); // aparece depois da tela
            float y = 990 + rand.nextInt(21); // altura aleatória no chão

            coins.add(new Coin(getContext(), x, y));
        }

        // Geração dinâmica de obstáculos
        if (obstacles.size() == 0 || obstacles.get(obstacles.size() - 1).getX() < getWidth() - 800) {
            float x = getWidth() + rand.nextInt(400);
            float y = 1700;

            obstacles.add(new Obstacle(getContext(), x, y));
        }

        for (Obstacle obs : obstacles) {
            obs.move(-15);

            if (obs.isActive() && RectF.intersects(player.getBounds(), obs.getBounds())) {
                obs.deactivate();
                score = Math.max(0, score - 1);
            }
        }

        // Remove obstáculos fora da tela
        obstacles.removeIf(obs -> obs.getX() < -100);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawBitmap(background, 0, 0, null);

            // Desenha moedas
            for (Coin coin : coins) {
                coin.draw(canvas);
            }

            // Desenha o jogador
            player.draw(canvas);

            // Desenha a pontuação
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(60);
            canvas.drawText("Moedas: " + score, 50, 80, paint);
        }

        for (Obstacle obs : obstacles) {
            obs.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.jump();
            return true;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread == null || !thread.isAlive()) {
            thread = new GameThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelX = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void pause() {
        thread.setRunning(false);
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void resume() {
        if (thread == null || !thread.isAlive()) {
            thread = new GameThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }


    public void swapCharacter() {
        player.swapCharacter();
    }
}
