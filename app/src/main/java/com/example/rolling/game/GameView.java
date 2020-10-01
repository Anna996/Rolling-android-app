package com.example.rolling.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.rolling.GameInfo;
import com.example.rolling.InfoChangedListener;
import com.example.rolling.R;
import com.example.rolling.activities.GameActivity;
import com.example.rolling.items.LevelSettings;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements Runnable {
    private static float s_ScreenRatioX, s_ScreenRatioY;
    private boolean m_IsPlaying, m_isGameOver = false, m_IsLevelFinished = false, isLost = false;
    private boolean m_GotScore = false, showCoin = false, isCoinShowed = false, isCoinIntersect = false, isMute;
    private int m_ScreenX, m_ScreenY;
    private int m_Score = 0, m_BestScore, m_Counter = 0, m_GameTime;
    private int m_Sound_enemy, m_Sound_gotScore, m_Sound_finish, m_Sound_coin;
    private int flag1 = -1, flag2 = -1, flag3 = -1, m_GameSpeed, m_MaxGameSpeed;
    private int[] m_ObstaclesID, widthToDivideArray, heightToDivideArray;
    private float[] m_ObstaclesXIndex;
    private Thread m_Thread;
    private Random m_Random;
    private Bitmap m_Volume;
    private Paint m_Paint;
    private Coin m_Coins;
    private MainPlayer m_Player;
    private Obstacle[] m_Obstacle1, m_Obstacle2, m_Obstacle3;
    private Background m_Background1, m_Background2;
    private SoundPool m_SoundPool;
    private SharedPreferences m_Prefs;
    private InfoChangedListener<Integer, Boolean> m_GameOverListener;

    public GameView(GameActivity i_GameActivity, LevelSettings i_Settings, int i_BestScore, int i_AmountOfCoins) {
        super(i_GameActivity);
        m_BestScore = i_BestScore;
        m_Random = new Random();
        m_Prefs = i_GameActivity.getSharedPreferences(GameInfo.gameInfo_TAG, Context.MODE_PRIVATE);
        m_GameSpeed = i_Settings.getStartSpeed();
        m_MaxGameSpeed = i_Settings.getMaxSpeed();
        m_ObstaclesID = i_Settings.getResIdObstacles();
        widthToDivideArray = i_Settings.getWidthToDivideArray();
        heightToDivideArray = i_Settings.getHeightToDivideArray();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                    setUsage(AudioAttributes.USAGE_GAME).build();
            m_SoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        } else {
            m_SoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        m_Sound_enemy = m_SoundPool.load(i_GameActivity, R.raw.punch_or_whack, 1);
        m_Sound_gotScore = m_SoundPool.load(i_GameActivity, R.raw.hitting_metal, 1);
        m_Sound_coin = m_SoundPool.load(i_GameActivity, R.raw.glass_ping, 1);
        m_Sound_finish = m_SoundPool.load(i_GameActivity, R.raw.ta_da, 1);

        m_ScreenX = i_GameActivity.getScreenX();
        m_ScreenY = i_GameActivity.getScreenY();

        s_ScreenRatioX = 2960f / m_ScreenX;
        s_ScreenRatioY = 1440f / m_ScreenY;
        m_Background1 = new Background(m_ScreenX, m_ScreenY, i_Settings.getResIdFirstBackground(), getResources());
        m_Background2 = new Background(m_ScreenX, m_ScreenY, i_Settings.getResIdSecondBackground(), getResources());

        int num = m_Random.nextInt(3);
        m_Player = new MainPlayer(m_ObstaclesID[num], widthToDivideArray[num], heightToDivideArray[num], m_ScreenX, m_ScreenY, getResources());
        m_ObstaclesXIndex = new float[]{0, (m_ScreenX / 2) - (m_Player.getM_Width() / 2), m_ScreenX - m_Player.getM_Width()};

        m_Background2.y = -m_ScreenY;

        m_Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_Paint.setTextSize(55);
        m_Paint.setColor(Color.WHITE);
        m_Paint.setStyle(Paint.Style.FILL);

        // initialize the obstacle:

        m_Obstacle1 = createObstacles();
        int y = -m_Player.getM_Height();

        y -= m_ScreenY / 3;
        m_Obstacle2 = createObstacles();
        for (Obstacle obstacle : m_Obstacle2) {
            obstacle.setM_Y(y);
        }
        y -= m_ScreenY / 3;
        m_Obstacle3 = createObstacles();
        for (Obstacle obstacle : m_Obstacle3) {
            obstacle.setM_Y(y);
        }

        m_Coins = new Coin(getResources(), i_AmountOfCoins);
        m_Coins.setM_X(m_ScreenX / 2 - m_Coins.getM_Width() / 2);

        isMute = m_Prefs.getBoolean(GameInfo.is_mute_TAG, false);
        m_Volume = BitmapFactory.decodeResource(getResources(), R.drawable.ic_volume_off_black_24dp);
        setVolume();
        m_GameTime = i_Settings.getGameTime();
        gameTimer();
    }

    public void setGameOverListener(InfoChangedListener<Integer, Boolean> i_GameOverListener) {
        this.m_GameOverListener = i_GameOverListener;
    }

    public static float getS_ScreenRatioX() {
        return s_ScreenRatioX;
    }

    public static float getS_ScreenRatioY() {
        return s_ScreenRatioY;
    }

    private void gameTimer() {
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                m_Counter++;
                if (m_Counter % 15 == 0) {
                    showCoin = true;
                    m_Coins.setM_Y(-m_Coins.getM_Height());
                    m_Coins.setM_X(m_ScreenX / 2 - m_Coins.getM_Width() / 2);
                }
                if (m_Counter % 5 == 0 && m_GameSpeed <= m_MaxGameSpeed) {
                    m_GameSpeed += 2;
                }
                if (m_Counter == m_GameTime) {
                    m_isGameOver = true;
                }
            }

        }, 1000, 1000);
    }

    //determinate randomly the index of the obstacle with same id as main player
    private Obstacle[] createObstacles() {

        Obstacle[] obstacles;
        int numOfObstaclesInRow = m_Random.nextInt(3) + 1;
        obstacles = new Obstacle[numOfObstaclesInRow];

        if (numOfObstaclesInRow == 3) {

            int index1, index2, index3 = 0;
            index1 = m_Random.nextInt(3);
            index2 = m_Random.nextInt(3);
            while (index1 == index2) {
                index2 = m_Random.nextInt(3);
            }
            for (int i = 0; i < 3; i++) {
                if (index1 != i && index2 != i) {
                    index3 = i;
                    break;
                }
            }

            int id1, id2, id3 = 0;
            id1 = m_Random.nextInt(3);
            id2 = m_Random.nextInt(3);
            while (id1 == id2) {
                id2 = m_Random.nextInt(3);
            }
            for (int i = 0; i < 3; i++) {
                if (id1 != i && id2 != i) {
                    id3 = i;
                    break;
                }
            }

            obstacles[0] = new Obstacle(getResources(), widthToDivideArray[id1], heightToDivideArray[id1], m_ObstaclesID[id1]);
            obstacles[0].setM_X((int) m_ObstaclesXIndex[index1]);
            obstacles[1] = new Obstacle(getResources(), widthToDivideArray[id2], heightToDivideArray[id2], m_ObstaclesID[id2]);
            obstacles[1].setM_X((int) m_ObstaclesXIndex[index2]);
            obstacles[2] = new Obstacle(getResources(), widthToDivideArray[id3], heightToDivideArray[id3], m_ObstaclesID[id3]);
            obstacles[2].setM_X((int) m_ObstaclesXIndex[index3]);
        } else if (numOfObstaclesInRow == 2) {
            int index1, index2;
            index1 = m_Random.nextInt(3);
            index2 = m_Random.nextInt(3);
            while (index1 == index2) {
                index2 = m_Random.nextInt(3);
            }

            int id1, id2;
            id1 = m_Random.nextInt(3);
            id2 = m_Random.nextInt(3);
            while (id1 == id2) {
                id2 = m_Random.nextInt(3);
            }

            obstacles[0] = new Obstacle(getResources(), widthToDivideArray[id1], heightToDivideArray[id1], m_ObstaclesID[id1]);
            obstacles[0].setM_X((int) m_ObstaclesXIndex[index1]);
            obstacles[1] = new Obstacle(getResources(), widthToDivideArray[id2], heightToDivideArray[id2], m_ObstaclesID[id2]);
            obstacles[1].setM_X((int) m_ObstaclesXIndex[index2]);
        } else {
            int index = m_Random.nextInt(3);
            int id = m_Random.nextInt(3);
            obstacles[0] = new Obstacle(getResources(), widthToDivideArray[id], heightToDivideArray[id], m_ObstaclesID[id]);
            obstacles[0].setM_X((int) m_ObstaclesXIndex[index]);
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.setM_Y(-obstacle.getM_Height());
        }

        return obstacles;
    }

    @Override
    public void run() {
        while (m_IsPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        m_Background1.y += 10;

        m_Background2.y += 10;

        if (m_Background1.y >= m_Background1.m_Background.getHeight())//>)// 0 m_MaxHeight)
        {
            m_Background1.y = -m_ScreenY;
        }
        if (m_Background2.y >= m_Background1.m_Background.getHeight())//m_ScreenY - 10 * m_ScreenRatioY)//0)//> m_MaxHeight)
        {
            m_Background2.y = -m_ScreenY;
        }

        ////// obstacle enemy //////////////

        for (Obstacle obstacle : m_Obstacle1) {
            obstacle.setM_Y(obstacle.getM_Y() + m_GameSpeed);
            if (obstacle.getM_Y() > m_ScreenY && !m_isGameOver) {
                m_GotScore = false;
                flag1 = -1;
                m_Obstacle1 = createObstacles();
                break;
            }

            flag1 = DoWhenACollisionOccurs(obstacle) != (-1) ? DoWhenACollisionOccurs(obstacle) : flag1;
        }

        for (Obstacle obstacle : m_Obstacle2) {
            obstacle.setM_Y(obstacle.getM_Y() + m_GameSpeed);
            if (obstacle.getM_Y() > m_ScreenY && !m_isGameOver) {
                m_GotScore = false;
                flag2 = -1;
                m_Obstacle2 = createObstacles();
                break;
            }

            flag2 = DoWhenACollisionOccurs(obstacle) != (-1) ? DoWhenACollisionOccurs(obstacle) : flag2;
        }


        for (Obstacle obstacle : m_Obstacle3) {
            obstacle.setM_Y(obstacle.getM_Y() + m_GameSpeed);
            if (obstacle.getM_Y() > m_ScreenY && !m_isGameOver) {
                m_GotScore = false;
                flag3 = -1;
                m_Obstacle3 = createObstacles();
                break;
            }

            flag3 = DoWhenACollisionOccurs(obstacle) != (-1) ? DoWhenACollisionOccurs(obstacle) : flag3;
        }

        if (showCoin) {
            if (m_Coins.getM_Y() > m_ScreenY) {
                isCoinShowed = false;
                showCoin = false;
                isCoinIntersect = false;
            } else popCoin();
        }

        if (m_Obstacle1[0].getM_Y() >= m_ScreenY && m_Obstacle2[0].getM_Y() >= m_ScreenY && m_Obstacle3[0].getM_Y() >= m_ScreenY) {
            if (!isMute) {
                m_SoundPool.play(m_Sound_finish, 1, 1, 0, 0, 1);
            }
            m_IsLevelFinished = true;
            m_IsPlaying = false;
        }
        doWhenCollectICoin();
        setVolume();
    }

    private void popCoin() {
        if (!isCoinShowed) {
            if (m_Obstacle1[0].getM_Y() < 0) {
                m_Coins.setM_Y(m_Obstacle1[0].getM_Y() - m_ScreenY / 6);
                isCoinShowed = true;
            } else if (m_Obstacle2[0].getM_Y() < 0) {
                m_Coins.setM_Y(m_Obstacle2[0].getM_Y() - m_ScreenY / 6);
                isCoinShowed = true;
            } else if (m_Obstacle3[0].getM_Y() < 0) {
                m_Coins.setM_Y(m_Obstacle3[0].getM_Y() - m_ScreenY / 6);
                isCoinShowed = true;
            }
        } else m_Coins.setM_Y(m_Coins.getM_Y() + m_Obstacle1[0].speed);
    }

    private void doWhenCollectICoin() {
        if (Rect.intersects(m_Player.getCollisionShape(), m_Coins.getCollisionShape())) {
            if (!isCoinIntersect) {
                isCoinIntersect = true;
                if (!isMute) {
                    m_SoundPool.play(m_Sound_coin, 1, 1, 0, 0, 1);
                }
                m_Coins.DoWhenCollected();
            }
        }
    }

    private int DoWhenACollisionOccurs(Obstacle i_Obstacle) {
        int flag = -1;

        if (Rect.intersects(i_Obstacle.getCollisionShape(), m_Player.getCollisionShape())) {
            if (i_Obstacle.getM_ID() == m_Player.getM_ID()) {
                if (!m_GotScore) {
                    if (!isMute) {
                        m_SoundPool.play(m_Sound_gotScore, 1, 1, 0, 0, 1);
                    }
                    m_Score++;
                    m_GotScore = true;
                }
                flag = i_Obstacle.getM_ID();
            } else {
                if (!isMute) {
                    m_SoundPool.play(m_Sound_enemy, 1, 1, 0, 0, 1);
                }
                m_isGameOver = true;
                isLost = true;
            }
        }
        return flag;
    }

    private void setVolume() {
        if (isMute) {
            m_Volume = BitmapFactory.decodeResource(getResources(), R.drawable.mute);
        } else m_Volume = BitmapFactory.decodeResource(getResources(), R.drawable.speaker);

        int width = m_Volume.getWidth();
        int height = m_Volume.getHeight();

        width /= 14;
        height /= 7;

        width *= (int) (width * s_ScreenRatioX);
        height *= (int) (height * s_ScreenRatioY);
        m_Volume.createScaledBitmap(m_Volume, width, height, false);
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(m_Background1.m_Background, m_Background1.x, m_Background1.y, m_Paint);
            canvas.drawBitmap(m_Background2.m_Background, m_Background2.x, m_Background2.y, m_Paint);

            for (Obstacle obstacle : m_Obstacle1) {
                if (obstacle.getM_ID() != flag1)
                    canvas.drawBitmap(obstacle.getM_player(), obstacle.getM_X(), obstacle.getM_Y(), m_Paint);
            }
            for (Obstacle obstacle : m_Obstacle2) {
                if (obstacle.getM_ID() != flag2)
                    canvas.drawBitmap(obstacle.getM_player(), obstacle.getM_X(), obstacle.getM_Y(), m_Paint);
            }
            for (Obstacle obstacle : m_Obstacle3) {
                if (obstacle.getM_ID() != flag3)
                    canvas.drawBitmap(obstacle.getM_player(), obstacle.getM_X(), obstacle.getM_Y(), m_Paint);
            }

            if (!isCoinIntersect) {
                canvas.drawBitmap(m_Coins.getM_player(), m_Coins.getM_X(), m_Coins.getM_Y(), m_Paint);
            }

            canvas.drawText("Score : " + m_Score, m_ScreenX / 2 -  m_Player.getM_Width() / 2, m_Player.getM_Height() + 30, m_Paint);
            canvas.drawText("High Score : " + m_BestScore, 30, m_Player.getM_Height() / 2, m_Paint);
            canvas.drawBitmap(m_Coins.GetSmallPicture(), m_ScreenX - m_ScreenX / 5, m_Coins.GetSmallPicture().getHeight() / 4, m_Paint);
            canvas.drawText("" + m_Coins.getM_AmountOfCoins(), m_ScreenX - m_ScreenX / 12, m_Player.getM_Height() / 2, m_Paint);
            canvas.drawBitmap(m_Volume, m_ScreenX - 2 * m_Volume.getWidth(), 2 * m_Volume.getHeight(), m_Paint);
            canvas.drawBitmap(m_Player.getM_player(), m_Player.getM_X(), m_Player.getM_Y(), m_Paint);

            if (m_isGameOver && (m_IsLevelFinished || isLost)) {
                m_IsPlaying = false;
                if (m_GameOverListener != null) {
                    m_GameOverListener.infoChanged(m_Score, m_Coins.getM_AmountOfCoins(), m_IsLevelFinished);
                }
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        m_IsPlaying = true;
        m_Thread = new Thread(this);
        m_Thread.start();
    }

    public void pause() {
        try {
            m_IsPlaying = false;
            m_Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (event.getX() >= m_ScreenX - 3 * m_Volume.getWidth() / 2 &&
                        event.getX() <= m_ScreenX - m_Volume.getWidth() / 2 &&
                        event.getY() <= 2 * m_Volume.getHeight() + m_Volume.getHeight() &&
                        event.getY() >= 2 * m_Volume.getHeight()) {
                    isMute = !isMute;
                    SharedPreferences.Editor editor = m_Prefs.edit();
                    editor.putBoolean(GameInfo.is_mute_TAG, isMute);
                    editor.apply();
                    setVolume();
                } else {
                    if (event.getX() >= m_ScreenX - m_Player.getM_Width() / 2) {
                        m_Player.setM_X(m_ScreenX - m_Player.getM_Width());
                    } else if (event.getX() <= m_Player.getM_Width() / 2) {
                        m_Player.setM_X(0);
                    } else m_Player.setM_X((int) (event.getX() - m_Player.getM_Width() / 2));
                }
                return true;
        }

        return true;
    }
}