/**
MIT License

Copyright (c) 2018 OkToGames

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.oktogames.app_2x2is4.light.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.oktogames.app_2x2is4.light.BuyButtonTouchListener;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.TwoXTwoIs4Activity;
import com.oktogames.app_2x2is4.light.action.GameControlButtonOnTouchListener;
import com.oktogames.app_2x2is4.light.android.graphics.drawable.AnimationDrawableExt;
import com.oktogames.app_2x2is4.light.engine.*;
import com.oktogames.app_2x2is4.light.utils.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The Main game activity.
 */
public class GameActivity extends SharedActivity {
    private AnimationDrawable seaFrameAnimation;
    private AnimationDrawable sunFrameAnimation;
    private AnimationDrawable pirateShipAnimation;
    private AnimationDrawable pirateShipCrashFrameAnimation;
    private AnimationDrawable pirateLoose;
    private AnimationDrawable pirateWon;
    private AnimationDrawable ourShipCrashFrameAnimation;
    private AnimationDrawable ourShotAnimation;
    private AnimationDrawable pirateShotAnimation;
    private Button pauseBtn;
    private Button helpBtn;
    private Button finishBtn;
    private Button restartBtn;
    private MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl();
    private String currentAnswer = "";
    private String expectedAnswer = "";
    private int timeCounter = 0;
    private int myCanonShotsDone = 0;
    private int pirateCanonShotsDone = 0;
    private boolean answerWasProvided = false;
    private ImageView ourCanon;
    private ImageView pirateCanon;
    private ImageView[] ourHearts = new ImageView[MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER];
    private ImageView[] pirateHearts = new ImageView[MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER];
    private HeartsAnimatingEngine ourHeartsEngine;
    private HeartsAnimatingEngine pirateHeartsEngine;
    private CombinationImpl currentCombination = CombinationImpl.nullCombination();
    private int[] ourCanonImagesIds = {R.drawable.our_canon_anim0, R.drawable.our_canon_anim1, R.drawable.our_canon_anim2,
            R.drawable.our_canon_anim3, R.drawable.our_canon_anim4, R.drawable.our_canon_anim5,
            R.drawable.our_canon_anim6, R.drawable.our_canon_anim7, R.drawable.our_canon_anim8,
            R.drawable.our_canon_anim9, R.drawable.our_canon_anim10, R.drawable.our_canon_anim11};

    private int[] pirateCanonImagesIds = {R.drawable.pirate_canon_anim0, R.drawable.pirate_canon_anim1, R.drawable.pirate_canon_anim2,
            R.drawable.pirate_canon_anim3, R.drawable.pirate_canon_anim4, R.drawable.pirate_canon_anim5,
            R.drawable.pirate_canon_anim6, R.drawable.pirate_canon_anim7, R.drawable.pirate_canon_anim8,
            R.drawable.pirate_canon_anim9, R.drawable.pirate_canon_anim10, R.drawable.pirate_canon_anim11};
    private static final int MAX_BLICKS_COUNT = 6;
    private int helpsCounter = 0;
    private boolean isSoundOn = true;
    private int blickingCounter = 0;
    private OperationType operationType = OperationType.MULTIPLICATION;
    private int number = 0;
    private int maxNumber = MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER;
    private GameLevel gameLevel = GameLevel.EASY;
    private Timer timer;
    private Handler timerHandler = new Handler();
    private Timer timerControlAnswer;
    private Handler handlerTimerControlAnswer = new Handler();
    private Button t1;
    private Button t2;
    private Button t3;
    private Button t4;
    private Button t5;
    private Button soundButton;
    private static TimerTask timerTask;
    private static AnimationDrawableExt pirateDemoCanonAnimationExt;
    private Timer pirateDemoTimer;
    private ImageView multipl;
    private ImageView division;
    private ImageView easy;
    private ImageView medium;
    private ImageView hard;
    private ImageView animationBall;
    private Timer blickTimer;
    private static Runnable ballAnimation;
    private AnimationDrawable ourShipAnimation;
    private static Drawable learnTableBGSmall;
    private boolean isHeapSmall = false;
    private Drawable sea, sun, ourShip, pirateShip;
    private static Drawable rogerWon;
    private static Drawable cup;
    private static Drawable rogerLoose;
    public static final int heapSize = 32*1024*1024; //32Mb
    private boolean isGameEnd = false;
    private static final int ALERT_WIDTH = 250;
    private Drawable[] drawablesPirateCanon;
    private int resultSowDialogCounter;
    private boolean isShowRateDialog;
    private static final String TAG = "GameActivity";
    private boolean isPremiumGameMode = false;

    private int getMaxFreeNumber() {
        return 2;
    }

    private void startPremiumGame() {
        int premiumGameNumber = TwoXTwoIs4Activity.bumpAndStorePremiumGameNumber(this);
        number = premiumGameNumber;
        isPremiumGameMode = true;
        restartBtn.setVisibility(View.INVISIBLE);
        ImageView resultRestartButton = (ImageView) findViewById(R.id.result_reload);
        resultRestartButton.setVisibility(View.INVISIBLE);
        RelativeLayout resultsLayout = (RelativeLayout) findViewById(R.id.result_id);
        resultsLayout.setVisibility(View.GONE);

        final GameActivity _self = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.premium_game_title);
        Resources res = getResources();
        builder.setMessage(String.format(res.getString(R.string.premium_game_explanation), premiumGameNumber+2));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int index) {
                _self.startGame();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == KeyEvent.KEYCODE_BACK;
            }
        });
        dialog.show();
    }

    public void onCreate(Bundle savedInstanceState) {
        if (Runtime.getRuntime().maxMemory()< heapSize){
            isHeapSmall = true;
        }
        resultSowDialogCounter = getSettings().getInt(RESULT_SHOW_COUNT, 0);
        isShowRateDialog = getSettings().getBoolean(RATE_DIALOG_SHOW, true);
        int level = getSettings().getInt(GAME_LEVEL, 1);
        if (level==2) gameLevel = GameLevel.MEDIUM;
        else if (level == 3)gameLevel=GameLevel.HARD;

        int operation = getSettings().getInt(GAME_OPERATION, 1);
        if (operation==2)operationType = OperationType.DIVISION;

        super.onCreate(savedInstanceState);

        try {
            if (DrawableUtils.isTablet(this)== DrawableUtils.ScreenSize.XLARGE &&
                    (getWindowManager().getDefaultDisplay().getWidth()==1024 &&
                            getWindowManager().getDefaultDisplay().getHeight() == 768)) {
                setContentView(R.layout.game1024);
            } else {
                setContentView(R.layout.game);
            }
        } catch (Exception e) {
            setContentView(R.layout.game);
        }
    }

    private void initOurShip(){
        ourShipAnimation = new AnimationDrawable();
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim1.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim2.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim3.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim4.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim5.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim6.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim7.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim8.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim9.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim10.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim11.png"), 250);
        ourShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim12.png"), 250);
        ourShipAnimation.setOneShot(false);
    }

    private void initSun(){
        sunFrameAnimation = new AnimationDrawable();
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim1.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim2.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim3.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim4.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim5.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim6.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim7.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim8.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim9.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim10.png"), 200);
        sunFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim11.png"), 200);
        sunFrameAnimation.setOneShot(false);
    }


    private void initSea(){
        seaFrameAnimation = new AnimationDrawable();
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim1.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim2.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim3.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim4.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim5.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim6.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim7.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim8.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim9.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim10.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim11.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim12.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim13.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim14.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim15.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim16.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim17.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim18.png"), 170);
        seaFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim19.png"), 170);
        seaFrameAnimation.setOneShot(false);
    }


    private void initOurShot() {
        ourShotAnimation = new AnimationDrawable();
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "img/transparent.png"), 1);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim1.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim2.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim3.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim4.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim5.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim6.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim7.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_shot_smoke_anim8.png"), 62);
        ourShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "img/transparent.png"), 1);
        ourShotAnimation.setOneShot(true);
    }

    private void initPirateShot() {
        pirateShotAnimation = new AnimationDrawable();
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "img/transparent.png"), 1);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim1.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim2.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim3.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim4.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim5.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim6.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim7.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_shot_smoke_anim8.png"), 62);
        pirateShotAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "img/transparent.png"), 1);
        pirateShotAnimation.setOneShot(true);
    }

    private void initPirateShip(){
        pirateShipAnimation = new AnimationDrawable();
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim1.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim2.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim3.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim4.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim5.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim6.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim7.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim8.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim9.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim10.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim11.png"), 250);
        pirateShipAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim12.png"), 250);
        pirateShipAnimation.setOneShot(false);
    }

    private void initPirateCrash(){
        pirateShipCrashFrameAnimation = new AnimationDrawable();
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim1.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim2.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim3.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim4.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim5.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim6.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim7.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim8.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim9.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim10.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim11.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim12.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim13.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim14.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim15.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim16.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim17.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim18.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim19.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim20.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim21.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim22.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim23.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim24.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim25.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim26.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim27.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim28.png"), 100);
        pirateShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim29.png"), 100);
        pirateShipCrashFrameAnimation.setOneShot(true);
    }

    private void initOurCrash(){
        ourShipCrashFrameAnimation = new AnimationDrawable();
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim1.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim2.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim3.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim4.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim5.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim6.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim7.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim8.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim9.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim10.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim11.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim12.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim13.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim14.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim15.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim16.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim17.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim18.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim19.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim20.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim21.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim22.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim23.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim24.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim25.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim26.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim27.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim28.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim29.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim30.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim31.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim32.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim33.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim34.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim35.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim36.png"), 100);
        ourShipCrashFrameAnimation.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim37.png"), 100);

        ourShipCrashFrameAnimation.setOneShot(true);
    }

    private void startGame() {
        isGameEnd = false;
        stopPirateCanonDemo();
        RelativeLayout settingsLayout = (RelativeLayout)findViewById(R.id.settings_id);
        settingsLayout.setVisibility(View.GONE);
        stopExamine();
        currentCombination = CombinationImpl.nullCombination();
        MediaPlayerFacade.playClick(GameActivity.this);
        startExamine();
    }

    private void prepareAndShowSettings(){
        disableChooseAndControlButtons();
        findViewById(R.id.answer_id).setVisibility(View.INVISIBLE);

        if(operationType == OperationType.MULTIPLICATION){
            multipl.setImageResource(R.drawable.multiplication_selected);
            division.setImageResource(R.drawable.division);
        } else {
            multipl.setImageResource(R.drawable.multiplication);
            division.setImageResource(R.drawable.division_selected);
        }

        if (gameLevel==GameLevel.EASY){
            easy.setImageResource(R.drawable.easy_level_selected);
            medium.setImageResource(R.drawable.medium_level);
            hard.setImageResource(R.drawable.hard_level);
        } else if (gameLevel==GameLevel.MEDIUM){
            easy.setImageResource(R.drawable.easy_level);
            medium.setImageResource(R.drawable.medium_level_selected);
            hard.setImageResource(R.drawable.hard_level);
        } else {
            easy.setImageResource(R.drawable.easy_level);
            medium.setImageResource(R.drawable.medium_level);
            hard.setImageResource(R.drawable.hard_level_selected);
        }

        multipl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipl.setImageResource(R.drawable.multiplication_selected);
                division.setImageResource(R.drawable.division);
                operationType = OperationType.MULTIPLICATION;
                changeVisibilityOfTables(number, false, operationType);
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipl.setImageResource(R.drawable.multiplication);
                division.setImageResource(R.drawable.division_selected);
                operationType = OperationType.DIVISION;
                changeVisibilityOfTables(number, false, operationType);

            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setImageResource(R.drawable.easy_level_selected);
                medium.setImageResource(R.drawable.medium_level);
                hard.setImageResource(R.drawable.hard_level);
                gameLevel = GameLevel.EASY;
                pirateShotAnimation = null;
                startPirateCanonDemo(gameLevel);
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setImageResource(R.drawable.easy_level);
                medium.setImageResource(R.drawable.medium_level_selected);
                hard.setImageResource(R.drawable.hard_level);
                gameLevel = GameLevel.MEDIUM;
                pirateShotAnimation = null;
                startPirateCanonDemo(gameLevel);
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setImageResource(R.drawable.easy_level);
                medium.setImageResource(R.drawable.medium_level);
                hard.setImageResource(R.drawable.hard_level_selected);
                gameLevel = GameLevel.HARD;
                pirateShotAnimation = null;
                startPirateCanonDemo(gameLevel);
            }
        });

        final SeekBar seekBar = (SeekBar) findViewById(R.id.settings_seek_number);
        final TextView numberTextView = (TextView)findViewById(R.id.settings_number);
        numberTextView.setText(String.valueOf(number + 2));
        seekBar.setIndeterminate(false);
        seekBar.setProgress(number);
        changeVisibilityOfTables(number, false, operationType);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                number = progress;
                numberTextView.setText(String.valueOf(progress + 2));
                changeVisibilityOfTables(progress, true, operationType);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress()>getMaxFreeNumber()) {
                    number = getMaxFreeNumber();
                    seekBar.setProgress(number);
                    numberTextView.setText(String.valueOf(number + 2));
                    changeVisibilityOfTables(number, true, operationType);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showBuyAlert(GameActivity.this, R.string.times_promo);
                        }
                    }, 300);
                }
            }
        });

        changeVisibilityOfTables(number, true, operationType);

        ImageView startImage = (ImageView)findViewById(R.id.settings_start_game);
        startImage.setImageDrawable(DrawableUtils.getStart(this));
        startImage.setOnTouchListener(new GameControlButtonOnTouchListener(this, startImage,
                GameControlButtonOnTouchListener.TypeControlButton.START));
        startImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        ImageView cancelImage = (ImageView)findViewById(R.id.settings_cancel);
        cancelImage.setImageDrawable(DrawableUtils.getCancel(this));
        cancelImage.setOnTouchListener(new GameControlButtonOnTouchListener(this, cancelImage,
                GameControlButtonOnTouchListener.TypeControlButton.CANCEL));
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerFacade.playClick(GameActivity.this);
                stopExamine();
                toMainView();
            }
        });
        if(isHeapSmall){
            initByHeapSmall();
        }
        
        animationBall.setVisibility(View.INVISIBLE);
        findViewById(R.id.settings_id).setVisibility(View.VISIBLE);

        if (!isHeapSmall) {
            if (ourShipAnimation==null){
                initOurShip();
            }
            findViewById(R.id.our_ship).setBackgroundDrawable(ourShipAnimation);
            ourShipAnimation.stop();
            ourShipAnimation.start();
        } else {
            //
            if(ourShip==null){
                ourShip=DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim1.png");
            }
            findViewById(R.id.our_ship).setBackgroundDrawable(ourShip);
        }
        isGameEnd = false;
    }

    private void toMainView() {
        Intent j = new Intent(GameActivity.this, TwoXTwoIs4Activity.class);
        j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(j);
        finish();
    }

    private void changeVisibilityOfTables(int progress, boolean isAnimate, OperationType type) {
        if(learnTableBGSmall==null){
            learnTableBGSmall = DrawableUtils.getDrawableFromAssets(this, "img/learn_bg_small.jpg");
        }
        int[] timesIds = {R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7, R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12};
        int[] devsIds = {R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8, R.id.v9, R.id.v10, R.id.v11, R.id.v12};
        int[] worcIds;

        if (findViewById(R.id.v2).getBackground()==null) {
            findViewById(R.id.v2).setBackgroundDrawable(learnTableBGSmall);
        }
        if (findViewById(R.id.t2).getBackground()==null) {
            findViewById(R.id.t2).setBackgroundDrawable(learnTableBGSmall);
        }

        if (type==OperationType.MULTIPLICATION){
            worcIds = timesIds;
            findViewById(R.id.v2).setVisibility(View.INVISIBLE);
            for(int id : devsIds){
                View view = findViewById(id);
                view.setVisibility(View.INVISIBLE);
            }
        } else {
            worcIds = devsIds;
            findViewById(R.id.v2).setVisibility(View.VISIBLE);
            for(int id : timesIds){
                View view = findViewById(id);
                view.setVisibility(View.INVISIBLE);
            }
        }

        int i = 0;
        for (int id : worcIds) {
            if (isAnimate) {
                if ((i + 1) == progress) {
                    if (findViewById(id).getVisibility() == ViewGroup.INVISIBLE) {
                        findViewById(id).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
                    }
                } else if (i == progress) {
                    if (findViewById(id).getBackground()==null) {
                        findViewById(id).setBackgroundDrawable(learnTableBGSmall);
                    }
                    if (findViewById(id).getVisibility() == ViewGroup.VISIBLE) {
                        findViewById(id).startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
                    }
                }
            }
            if (i >= progress) {
                findViewById(id).setVisibility(View.INVISIBLE);
            } else {
                if (findViewById(id).getBackground()==null) {
                    findViewById(id).setBackgroundDrawable(learnTableBGSmall);
                }
                findViewById(id).setVisibility(View.VISIBLE);
            }
            i++;
        }
    }

    private void prepareChooseAndControlButtons(){
        t1.setOnTouchListener(new GameControlButtonOnTouchListener(this, t1,
                GameControlButtonOnTouchListener.TypeControlButton.BOMB));
        t2.setOnTouchListener(new GameControlButtonOnTouchListener(this, t2,
                GameControlButtonOnTouchListener.TypeControlButton.BOMB));
        t3.setOnTouchListener(new GameControlButtonOnTouchListener(this, t3,
                GameControlButtonOnTouchListener.TypeControlButton.BOMB));
        t4.setOnTouchListener(new GameControlButtonOnTouchListener(this, t4,
                GameControlButtonOnTouchListener.TypeControlButton.BOMB));
        t5.setOnTouchListener(new GameControlButtonOnTouchListener(this, t5,
                GameControlButtonOnTouchListener.TypeControlButton.BOMB));

        t1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                digitButtonPressed(t1);
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                digitButtonPressed(t2);
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                digitButtonPressed(t3);
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                digitButtonPressed(t4);
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                digitButtonPressed(t5);
            }
        });

        pauseBtn.setOnTouchListener(new GameControlButtonOnTouchListener(this, pauseBtn,
                GameControlButtonOnTouchListener.TypeControlButton.PAUSE));
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pauseBtn.setEnabled(false);
                stopTimers();
                findViewById(R.id.answer_id).setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle(R.string.pause);
                builder.setPositiveButton(R.string.continued, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startTimer();
                        new Handler().post(new Runnable() {
                            public void run() {
                                try {
                                    findViewById(R.id.answer_id).setVisibility(View.VISIBLE);
                                    updateLabels();
                                    if (pauseBtn != null) {
                                        pauseBtn.setEnabled(true);
                                    }
                                } catch (Exception e) {
                                    updateLabels();
                                }
                            }
                        });
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return i == KeyEvent.KEYCODE_BACK;
                    }
                });
                dialog.show();
                dialog.getWindow().setLayout(DrawableUtils.getPixels(GameActivity.this, ALERT_WIDTH),
                        dialog.getWindow().getAttributes().height);
            }
        });
        helpBtn.setOnTouchListener(new GameControlButtonOnTouchListener(this, helpBtn,
                GameControlButtonOnTouchListener.TypeControlButton.HELP));
        finishBtn.setOnTouchListener(new GameControlButtonOnTouchListener(this, finishBtn,
                GameControlButtonOnTouchListener.TypeControlButton.FINISH));
        restartBtn.setOnTouchListener(new GameControlButtonOnTouchListener(this, restartBtn,
                GameControlButtonOnTouchListener.TypeControlButton.RESTART));

        helpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                helpBtn.setEnabled(false);
                helpButtonPressed();
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle(R.string.remember);
                builder.setPositiveButton(R.string.continued, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startTimer();
                        prepareNextCombination();
                        updateLabels();
                        helpBtn.setEnabled(true);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        return i == KeyEvent.KEYCODE_BACK;
                    }
                });
                WindowManager.LayoutParams wmLm = dialog.getWindow().getAttributes();
                Display display = getWindowManager().getDefaultDisplay();
                dialog.getWindow().setAttributes(wmLm);
                if (DrawableUtils.isTablet(GameActivity.this)!=DrawableUtils.ScreenSize.XLARGE) {
                    wmLm.y = display.getHeight() / 2;
                }
                dialog.show();
                dialog.getWindow().setLayout(DrawableUtils.getPixels(GameActivity.this, ALERT_WIDTH),
                        dialog.getWindow().getAttributes().height);
            }
        });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!currentCombination.isNull()) {
                    restartBtn.setEnabled(false);
                    findViewById(R.id.answer_id).setVisibility(View.INVISIBLE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    builder.setTitle(R.string.restart);
                    builder.setMessage(R.string.restart);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // todo there was currentCombination = null may be problem with result dialog
                            stopExamine();
                            currentCombination = CombinationImpl.nullCombination();
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    prepareAndShowSettings();
                                    restartBtn.setEnabled(true);
                                }
                            });


                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Handler().post(new Runnable() {
                                public void run() {
                                    findViewById(R.id.answer_id).setVisibility(View.VISIBLE);
                                    if (restartBtn!=null) {
                                        restartBtn.setEnabled(true);
                                    }
                                }
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            return i == KeyEvent.KEYCODE_BACK;
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setLayout(DrawableUtils.getPixels(GameActivity.this, ALERT_WIDTH),
                            dialog.getWindow().getAttributes().height);
                }
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currentCombination.isNull()) {
                    toMainView();
                } else {
                    finishBtn.setEnabled(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    builder.setTitle(R.string.finish);
                    builder.setMessage(R.string.finish);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopExamine();
                            toMainView();
                        }

                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Handler().post(new Runnable() {
                                public void run() {
                                    updateLabels();
                                    finishBtn.setEnabled(true);
                                }
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            return i == KeyEvent.KEYCODE_BACK;
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setLayout(DrawableUtils.getPixels(GameActivity.this, ALERT_WIDTH),
                            dialog.getWindow().getAttributes().height);
                }
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                isSoundOn = !isSoundOn;
                if(isSoundOn){
                    soundButton.setBackgroundResource(R.drawable.sound_on);
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                } else {
                    soundButton.setBackgroundResource(R.drawable.sound_off);
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                }
            }
        });

    }

    private void disableChooseAndControlButtons() {
        t1.setOnTouchListener(null);
        t2.setOnTouchListener(null);
        t3.setOnTouchListener(null);
        t4.setOnTouchListener(null);
        t5.setOnTouchListener(null);
        pauseBtn.setOnTouchListener(null);
        helpBtn.setOnTouchListener(null);
        restartBtn.setOnTouchListener(null);
        finishBtn.setOnTouchListener(null);

        t1.setOnClickListener(null);
        t2.setOnClickListener(null);

        t3.setOnClickListener(null);
        t4.setOnClickListener(null);
        t5.setOnClickListener(null);
        pauseBtn.setOnClickListener(null);
        helpBtn.setOnClickListener(null);
        restartBtn.setOnClickListener(null);
        finishBtn.setOnClickListener(null);
        soundButton.setOnClickListener(null);
    }

    private void startMainAnimation(){
        if (!isHeapSmall) {
            //start Sea animation
            if(seaFrameAnimation==null){
                initSea();
            }
            findViewById(R.id.sea).setBackgroundDrawable(seaFrameAnimation);
            seaFrameAnimation.stop();
            seaFrameAnimation.start();

            //start Sun animation
            if(sunFrameAnimation==null){
                initSun();
            }
            findViewById(R.id.sun).setBackgroundDrawable(sunFrameAnimation);
            sunFrameAnimation.stop();
            sunFrameAnimation.start();

            //start Our Ship Animation
            if(ourShipAnimation==null){
                initOurShip();
            }
            //our ship was crashed or it is first start
            if (engine.getShotsForWin()>pirateCanonShotsDone || pirateCanonShotsDone==0) {
                findViewById(R.id.our_ship).setBackgroundDrawable(ourShipAnimation);
                ourShipAnimation.stop();
                ourShipAnimation.start();
            }

            //start Pirate Ship Animation
            if(pirateShipAnimation ==null){
                initPirateShip();
            }
            findViewById(R.id.pirate_ship).setBackgroundDrawable(pirateShipAnimation);
            pirateShipAnimation.stop();
            pirateShipAnimation.start();
        } 
    }

    private void startExamine() {
        initOurHearts();
        initPirateHearts();
        prepareChooseAndControlButtons();
        maxNumber = getSettings().getInt(CURRENT_NUMBER, 8)+3;
        engine = new MultiplicationTableEngineImpl(number+2, operationType, gameLevel, maxNumber);
        MediaPlayerFacade.playBackground(this, true);
        engine.reinit();
        timeCounter = 0;
        myCanonShotsDone = 0;
        pirateCanonShotsDone = 0;
        answerWasProvided = false;
        zeroCanonImages();
        startTimer();
        ourHeartsEngine = new HeartsAnimatingEngine(this, ourHearts);
        pirateHeartsEngine = new HeartsAnimatingEngine(this, pirateHearts);
        prepareNextCombination();
        updateLabels();
        ourHeartsEngine.chargeEngine(engine);
        pirateHeartsEngine.chargeEngine(engine);
        //Combination Label show
        findViewById(R.id.answer_id).setVisibility(View.VISIBLE);
        animationBall.setVisibility(View.INVISIBLE);
        helpsCounter = 0;
    }

    private void prepareNextCombination() {
        if (!currentCombination.isNull()) {
            if (pirateCanonShotsDone < engine.getShotsForWin()) {
                boolean needMakeShot = false;
                MultiplicationTableEngineImpl.CanonReadiness myCanonReadiness =
                        engine.getMyCanonReadiness(needMakeShot);
                needMakeShot = myCanonReadiness.isNeedMakeShot();
                if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == myCanonReadiness.getReadiness()) {
                    ourCanon.setImageResource(ourCanonImagesIds[0]);
                } else {
                    ourCanon.setImageResource(ourCanonImagesIds[myCanonReadiness.getReadiness()]);
                }
                if (needMakeShot) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ourCanon.setImageResource(ourCanonImagesIds[ourCanonImagesIds.length-1]);
                        }
                    });
                    if(ourShotAnimation==null){
                        initOurShot();
                    }
                    ((ImageView)findViewById(R.id.our_shot)).setImageDrawable(ourShotAnimation);
                    new Handler().post(new Runnable() {
                        public void run() {
                            if (ourShotAnimation!=null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MediaPlayerFacade.playCanonShot(GameActivity.this);
                                    }
                                });
                                ourShotAnimation.stop();
                                ourShotAnimation.start();
                            }
                        }
                    });

                    new Handler().post(new Runnable() {
                        public void run() {
                            if (ourShotAnimation!=null) {
                                makeCanonShot(false);
                            }
                        }
                    });

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ourCanon.setImageResource(ourCanonImagesIds[0]);
                        }
                    }, 250);

                }
            }
        }
        currentCombination = engine.getCombination();
        if (!currentCombination.isNull()) {
            expectedAnswer = String.valueOf(currentCombination.getResult());
            int[] answers = currentCombination.generateAnswersVariants();
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);
            t5.setVisibility(View.VISIBLE);

            if (answers.length == 5) {
                t1.setText(String.valueOf(answers[0]));
                t2.setText(String.valueOf(answers[1]));
                t3.setText(String.valueOf(answers[2]));
                t4.setText(String.valueOf(answers[3]));
                t5.setText(String.valueOf(answers[4]));
            } else {
                t1.setVisibility(View.INVISIBLE);
                t2.setText(String.valueOf(answers[0]));
                t3.setText(String.valueOf(answers[1]));
                t4.setText(String.valueOf(answers[2]));
                t5.setVisibility(View.INVISIBLE);
            }
        } else {
            ((TextView) findViewById(R.id.answer_id)).setText("");
            stopTimers();
            isGameEnd = true;
        }
        currentAnswer = "";
    }

    private void updateLabels() {
        TextView combinationCountLabel = (TextView) findViewById(R.id.combination_count);
        if (engine==null || combinationCountLabel==null){
            return;
        }
        combinationCountLabel.setText(String.valueOf(engine.combinationsCount()));

        TextView timerLabel = (TextView) findViewById(R.id.timer);
        timerLabel.setText(DrawableUtils.getTimerString(timeCounter));
        final TextView combinationLabel = (TextView) findViewById(R.id.answer_id);
        if (currentCombination!=null && !currentCombination.isNull()) {
            String question = currentCombination.getFirstOperand() + "x" +
                    currentCombination.getSecondOperand() + "=" + currentAnswer;
            if (operationType == OperationType.DIVISION) {
                question = question.replaceAll("x", ":");
            }

            if (currentAnswer.equals(expectedAnswer)) {
                answerWasProvided = false;
                combinationLabel.setTextColor(Color.GREEN);
            } else if (!currentAnswer.equals("") && !currentAnswer.equals(expectedAnswer)) {
                combinationLabel.setTextColor(Color.RED);
            } else {
                combinationLabel.setTextColor(Color.BLACK);
            }
            combinationLabel.setText(question);
        } else {
            combinationLabel.setText("");
            combinationLabel.setTextColor(Color.BLACK);
            stopExamine();
            new Handler().post(new Runnable() {
                public void run() {
                    pirateShotAnimation=null;
                    ourShotAnimation=null;
                    pirateShipCrashFrameAnimation=null;
                    ourShipCrashFrameAnimation=null;

                    if (isGameEnd) {
                        isGameEnd = false;
                        //store history
                        HistoryEntry historyEntry = new HistoryEntry(
                                String.valueOf(System.currentTimeMillis()),
                                operationType == OperationType.MULTIPLICATION ? "0" : "1",
                                String.valueOf(timeCounter),
                                String.valueOf(number+2),
                                String.valueOf(helpsCounter),
                                String.valueOf(gameLevel.getValue()),
                                isVictory() ? "0" : "1",
                                String.valueOf(maxNumber));
                        HistoryResult.getInstance().addResult(historyEntry);
                        String savedHistory = getSettings().getString(HISTORY, "");
                        savedHistory = savedHistory + HistoryResult.getInstance().historyToString(historyEntry);
                        SharedPreferences.Editor editor = getEditor();
                        editor.putString(HISTORY, savedHistory);
                        editor.commit();

                        new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                        showResults();
                                                          }
                                                            }, 1000);
                    }
                }
            });
        }
        combinationLabel.invalidate();
    }

    private void zeroCanonImages() {
        ourCanon.setImageResource(R.drawable.our_canon_anim0);
        pirateCanon.setImageResource(R.drawable.pirate_canon_anim0);
    }

    private void initOurHearts() {
        ourHearts[11] = (ImageView) findViewById(R.id.our_heart1);
        ourHearts[10] = (ImageView) findViewById(R.id.our_heart2);
        ourHearts[9] = (ImageView) findViewById(R.id.our_heart3);
        ourHearts[8] = (ImageView) findViewById(R.id.our_heart4);
        ourHearts[7] = (ImageView) findViewById(R.id.our_heart5);
        ourHearts[6] = (ImageView) findViewById(R.id.our_heart6);
        ourHearts[5] = (ImageView) findViewById(R.id.our_heart7);
        ourHearts[4] = (ImageView) findViewById(R.id.our_heart8);
        ourHearts[3] = (ImageView) findViewById(R.id.our_heart9);
        ourHearts[2] = (ImageView) findViewById(R.id.our_heart10);
        ourHearts[1] = (ImageView) findViewById(R.id.our_heart11);
        ourHearts[0] = (ImageView) findViewById(R.id.our_heart12);
    }

    private void initPirateHearts() {
        pirateHearts[11] = (ImageView) findViewById(R.id.pirate_heart1);
        pirateHearts[10] = (ImageView) findViewById(R.id.pirate_heart2);
        pirateHearts[9] = (ImageView) findViewById(R.id.pirate_heart3);
        pirateHearts[8] = (ImageView) findViewById(R.id.pirate_heart4);
        pirateHearts[7] = (ImageView) findViewById(R.id.pirate_heart5);
        pirateHearts[6] = (ImageView) findViewById(R.id.pirate_heart6);
        pirateHearts[5] = (ImageView) findViewById(R.id.pirate_heart7);
        pirateHearts[4] = (ImageView) findViewById(R.id.pirate_heart8);
        pirateHearts[3] = (ImageView) findViewById(R.id.pirate_heart9);
        pirateHearts[2] = (ImageView) findViewById(R.id.pirate_heart10);
        pirateHearts[1] = (ImageView) findViewById(R.id.pirate_heart11);
        pirateHearts[0] = (ImageView) findViewById(R.id.pirate_heart12);
    }

    private boolean isVictory() {
        return (myCanonShotsDone > pirateCanonShotsDone);
    }

    private void startTimer() {
        timer = new Timer("TimeLabelTimer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerHandler.post(new Runnable() {
                    public void run() {
                        updateTimerLabel();
                    }
                });
            }
        }, 1000, 1000);
        startTimerControlAnswer();
    }

    private void startTimerControlAnswer() {
        int delayAndInterval = (4 - engine.getGameLevel().getValue()) * 2000;
        if (engine!=null) {
            timerControlAnswer = new Timer("ControlAnswerTimer");
            timerControlAnswer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handlerTimerControlAnswer.post(new Runnable() {
                        public void run() {
                            controlAnswer();
                        }
                    });
                }
            }, delayAndInterval, delayAndInterval);
        }
    }
    
    private void stopBlickTimers(){
        if(timerControlAnswer!=null){
            timerControlAnswer.cancel();
            timerControlAnswer = null;
        }
        
        if (blickTimer!=null){
            blickTimer.cancel();
            blickTimer = null;
        }

        if (helpBtn!=null) {
            helpBtn.setBackgroundDrawable(DrawableUtils.getHelp(this));
        }
        answerWasProvided = false;
    }

    private void controlAnswer() {
        if (!answerWasProvided) {
            blickTimer = new Timer();
            blickTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    blickHelpButton();
                }
            }, 0, 100);
        }
        answerWasProvided = false;
    }

    private void updateTimerLabel() {
        timeCounter++;
        if (engine==null) return;
        try {
            Handler lablesUpdateHandler = new Handler();
            lablesUpdateHandler.post(new Runnable() {
                public void run() {
                    updateLabels();
                }
            });

            if (pirateCanonShotsDone < engine.getShotsForWin()) {
                boolean needMakeShot = false;
                MultiplicationTableEngineImpl.CanonReadiness pirateCanonReadiness =
                        engine.getPirateCanonReadiness(timeCounter, needMakeShot);
                needMakeShot = pirateCanonReadiness.isNeedMakeShot();
                if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == pirateCanonReadiness.getReadiness()) {
                    pirateCanon.setImageResource(pirateCanonImagesIds[0]);
                } else {
                    pirateCanon.setImageResource(pirateCanonImagesIds[pirateCanonReadiness.getReadiness()]);
                }
                if (needMakeShot) {
                    pirateCanon.setImageResource(pirateCanonImagesIds[pirateCanonImagesIds.length-1]);
                    ImageView pirateShot = (ImageView) findViewById(R.id.pirate_shot);
                    pirateShot.setVisibility(View.VISIBLE);
                    if(pirateShotAnimation==null){
                        initPirateShot();
                    }
                    pirateShot.setImageDrawable(pirateShotAnimation);

                    new Handler().post(new Runnable() {
                        public void run() {
                            if (pirateShotAnimation!=null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MediaPlayerFacade.playCanonShot(GameActivity.this);
                                    }
                                });
                                pirateShotAnimation.stop();
                                pirateShotAnimation.start();
                            }
                        }
                    });
                    new Handler().post(new Runnable() {
                        public void run() {
                            if (pirateShotAnimation!=null) {
                                makeCanonShot(true);
                            }
                        }
                    });

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pirateCanon.setImageResource(pirateCanonImagesIds[0]);
                            animationBall.setVisibility(View.INVISIBLE);
                        }
                    }, 250);
                }
            } else {
                zeroCanonImages();
            }
        } catch (NullPointerException e) {
            Log.d("GameActivity", "NPE for updateTimerLabel: " + e.getMessage());
            finish();
        }
    }

    private void stopTimers() {
        if (timer!=null) {
            timer.cancel();
            timer = null;
        }
        if (timerControlAnswer!=null) {
            timerControlAnswer.cancel();
            timerControlAnswer = null;
        }

        if (pirateDemoTimer!=null) {
            pirateDemoTimer.cancel();
            pirateDemoTimer = null;
        }

        if (timerTask!=null) {
            timerTask.cancel();
            timerTask = null;
        }

        stopBlickTimers();
    }

    private void stopExamine() {
        stopTimers();
        MediaPlayerFacade.stopPlayBackground();
    }

    private void showResults() {
        disableChooseAndControlButtons();
        MediaPlayerFacade.stopPlayBackground();
        stopTimers();
        RelativeLayout resultsLayout = (RelativeLayout)findViewById(R.id.result_id);
        RelativeLayout resultPane = (RelativeLayout)findViewById(R.id.result_view_id);

        if (DrawableUtils.isTablet(this)== DrawableUtils.ScreenSize.LARGE) {

            RelativeLayout.LayoutParams  resultPaneLayoutParams = new RelativeLayout.LayoutParams(
                    (int)(getWindowManager().getDefaultDisplay().getWidth()*0.6),
                    (int)(getWindowManager().getDefaultDisplay().getHeight()*0.6));
            resultPaneLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            resultPaneLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            resultPane.setLayoutParams(resultPaneLayoutParams);
        }

        TextView resultTimer = (TextView)findViewById(R.id.timer_result);
        resultTimer.setText(DrawableUtils.getTimerString(timeCounter));
        TextView resultHelpCnt = (TextView)findViewById(R.id.helps_count);
        ImageView help2Img = (ImageView)findViewById(R.id.help2);
        help2Img.setImageDrawable(DrawableUtils.getHelp2(this));


        resultHelpCnt.setText(String.format("%02d", helpsCounter));
        ImageView rogerImg = (ImageView)findViewById(R.id.roger);

        ImageView pirateWonCup = (ImageView)findViewById(R.id.result_cup);
        resultsLayout.setVisibility(View.VISIBLE);
        if(isVictory()){
            MediaPlayerFacade.playPirateLoose(this);
            if(cup ==null){
                cup = DrawableUtils.getDrawableFromAssets(this, "img/cup.png");
            }
            pirateWonCup.setImageDrawable(cup);

            if (!isHeapSmall) {
                if (pirateLoose==null)pirateLoose = initPirateLooseAnimation();
                rogerImg.setImageDrawable(pirateLoose);
                pirateLoose.stop();
                pirateLoose.start();
            } else {
                if (rogerLoose==null){
                    rogerLoose = DrawableUtils.getDrawableFromAssets(this, "img/roger_loose.png");
                }
                rogerImg.setImageDrawable(rogerLoose);
            }
        } else {
            MediaPlayerFacade.playPirateWon(this);
            if(rogerWon==null){
                rogerWon = DrawableUtils.getDrawableFromAssets(this, "img/roger_won.png");
            }
            pirateWonCup.setImageDrawable(rogerWon);

            if (!isHeapSmall) {
                if (pirateWon==null)pirateWon = initPirateWonAnimation();
                rogerImg.setImageDrawable(pirateWon);
                pirateWon.stop();
                pirateWon.start();
            } else {
                rogerImg.setImageDrawable(rogerWon);
            }
        }
        resultSowDialogCounter++;

        if (resultSowDialogCounter%5==0 && isShowRateDialog) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    builder.setMessage(R.string.rate_message);

                    builder.setPositiveButton(R.string.rate_it, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isShowRateDialog = false;
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL)));
                        }
                    });
                    builder.setNegativeButton(R.string.never, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isShowRateDialog = false;
                        }
                    });
                    builder.setNeutralButton(R.string.later, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                            return i == KeyEvent.KEYCODE_BACK;
                        }
                    });
                    dialog.show();

                }
            });
        }
    }
    
    private AnimationDrawable initPirateWonAnimation(){
        AnimationDrawable piratWon = new AnimationDrawable();
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim1.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim2.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim3.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim4.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim5.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim6.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim7.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim8.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim9.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim10.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim1.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim2.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim3.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim4.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim5.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim6.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim7.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim8.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim9.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim10.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim1.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim2.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim3.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim4.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim5.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim6.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim7.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim8.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim9.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim10.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim1.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim2.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim3.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim4.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim5.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim6.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim7.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim8.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim9.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim10.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim1.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim2.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim3.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim4.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim5.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim6.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim7.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim8.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim9.png"), 100);
        piratWon.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_won_anim10.png"), 100);
        return piratWon;
    }

    private AnimationDrawable initPirateLooseAnimation(){
        AnimationDrawable pirateLoose = new AnimationDrawable();
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim1.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim2.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim3.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim4.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim5.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim6.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim7.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim8.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim9.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim10.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim11.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim12.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim13.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim14.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim15.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim16.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim17.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim1.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim2.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim3.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim4.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim5.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim6.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim7.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim8.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim9.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim10.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim11.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim12.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim13.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim14.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim15.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim16.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim17.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim1.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim2.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim3.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim4.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim5.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim6.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim7.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim8.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim9.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim10.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim11.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim12.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim13.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim14.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim15.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim16.png"), 200);
        pirateLoose.addFrame(DrawableUtils.getDrawableFromAssets(this, "anim/pirate_loose_anim17.png"), 200);
        return pirateLoose;
    }

    private void blickHelpButton() {
        if (++blickingCounter == MAX_BLICKS_COUNT) {
            blickingCounter = 0;
            if (blickTimer!=null) {
                blickTimer.cancel();
                blickTimer = null;
            }
        }
        if (blickingCounter%2==0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    helpBtn.setBackgroundDrawable(DrawableUtils.getHelp(GameActivity.this));
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    helpBtn.setBackgroundDrawable(DrawableUtils.getHelpPressed(GameActivity.this));
                }
            });
        }
    }

    private void digitButtonPressed(final Button button) {
        currentAnswer = button.getText().toString();
        if (ballAnimation!=null){
            ballAnimation = null;
            animationBall.setVisibility(View.INVISIBLE);
        }
        if (expectedAnswer.equals(currentAnswer)) {
            startCanonBallAnimationForButton(button, true);
            MediaPlayerFacade.playRightKey(this);
            answerWasProvided = true;
            prepareNextCombination();
            stopBlickTimers();
            startTimerControlAnswer();
        } else {
            startCanonBallAnimationForButton(button, false);
            MediaPlayerFacade.playBedKey(this);
            for (int i = 4 - engine.getGameLevel().getValue(); i > 0; i--) {
                updateTimerLabel();
            }
        }
        updateLabels();
    }

    private void startCanonBallAnimationForButton(Button button, boolean isOur) {
        int shotsForWin = engine.getShotsForWin();
        if (shotsForWin == pirateCanonShotsDone || (engine.combinationsCount() == 0)) {
            return;
        }

        int duration = 400;
        int ballSize = DrawableUtils.getPixels(this, 20);
        int cannonWidth = DrawableUtils.getPixels(this, 50);
        int cannonHeight = DrawableUtils.getPixels(this, 20);

        switch (DrawableUtils.isTablet(this)){
            case SMALL: cannonWidth = DrawableUtils.getPixels(this, 39);
                cannonHeight = DrawableUtils.getPixels(this, 15);
                ballSize = DrawableUtils.getPixels(this, 15);
                break;
            case XLARGE: cannonWidth = DrawableUtils.getPixels(this, 100);
                cannonHeight = DrawableUtils.getPixels(this, 40);
                ballSize = DrawableUtils.getPixels(this, 40);
                break;
            case LARGE: cannonWidth = DrawableUtils.getPixels(this, 85);
                cannonHeight = DrawableUtils.getPixels(this, 34);
                ballSize = DrawableUtils.getPixels(this, 34);
                break;
        }

        final int[] startPointArray = new int[2];
        button.getLocationOnScreen(startPointArray);
        startPointArray[0] = startPointArray[0]+ballSize/2;
        startPointArray[1] = startPointArray[1]+ballSize/2;

        int[] endPointArray = new int[2];

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (isOur) {
            findViewById(R.id.our_cannon).getLocationOnScreen(endPointArray);
            endPointArray[0] = endPointArray[0] + findViewById(R.id.our_cannon).getWidth() - cannonWidth/2;
            endPointArray[1] = endPointArray[1] + findViewById(R.id.our_cannon).getHeight()  - cannonHeight/2;
        } else {
            findViewById(R.id.pirate_cannon).getLocationOnScreen(endPointArray);
            endPointArray[1] = endPointArray[1] + findViewById(R.id.pirate_cannon).getHeight() - cannonHeight/2;
            endPointArray[0] = endPointArray[0] + DrawableUtils.getPixels(this, 18);
        }

        if (animationBall.getBackground() == null) {
            animationBall.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        }

        final TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE, startPointArray[0],
                Animation.ABSOLUTE, endPointArray[0],
                Animation.ABSOLUTE, startPointArray[1],
                Animation.ABSOLUTE, endPointArray[1]);
        translateAnimation.setDuration(duration);
        translateAnimation.setFillAfter(false);
        translateAnimation.setFillBefore(false);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(1.8f, 0.5f, 1.8f, 0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setFillBefore(false);

        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(false);
        animationSet.setFillBefore(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animationBall.startAnimation(animationSet);
            }
        });
    }

    private void makeCanonShot(boolean isPirate) {
        int shotsForWin = engine.getShotsForWin();
        if (myCanonShotsDone == shotsForWin || pirateCanonShotsDone == shotsForWin) {
            return;
        }
        if (!isPirate) {
            myCanonShotsDone++;
            if (myCanonShotsDone == shotsForWin) {
                ImageView imgPirateShip = (ImageView) findViewById(R.id.pirate_ship);
                if (!isHeapSmall) {
                    if (pirateShipCrashFrameAnimation==null){
                        initPirateCrash();
                    }
                    imgPirateShip.setBackgroundDrawable(pirateShipCrashFrameAnimation);
                    pirateShipCrashFrameAnimation.stop();
                    pirateShipAnimation.stop();
                    pirateShipAnimation = null;
                    pirateShipCrashFrameAnimation.start();
                } else {
                    imgPirateShip.setBackgroundDrawable(
                            DrawableUtils.getDrawableFromAssets(this, "anim/pirate_crash_anim29.png"));
                }
            }
            pirateHeartsEngine.eliminateOneHeart();
        } else {
            pirateCanonShotsDone++;
            if (pirateCanonShotsDone == shotsForWin) {
                ImageView imgOurShip = (ImageView) findViewById(R.id.our_ship);
                if (!isHeapSmall) {
                    if(ourShipCrashFrameAnimation==null){
                        initOurCrash();
                    }
                    imgOurShip.setBackgroundDrawable(ourShipCrashFrameAnimation);
                    ourShipCrashFrameAnimation.stop();
                    ourShipAnimation.stop();
                    ourShipAnimation = null;
                    ourShipCrashFrameAnimation.start();
                } else {
                    imgOurShip.setBackgroundDrawable(
                            DrawableUtils.getDrawableFromAssets(this, "anim/our_crash_anim37.png"));
                }
            }
            ourHeartsEngine.eliminateOneHeart();
        }
    }

    private void helpButtonPressed() {
        stopTimers();
        currentAnswer = expectedAnswer;
        helpsCounter++;
        answerWasProvided = true;
        updateLabels();
    }

    private void stopPirateCanonDemo(){
        Object animationCanon = pirateCanon.getDrawable();
        if (animationCanon instanceof AnimationDrawable) {
            ((AnimationDrawable)animationCanon).stop();
        }
        ImageView pirateShot = (ImageView) findViewById(R.id.pirate_shot);
        Drawable pirateCanonAnimation = pirateShot.getDrawable();
        if (pirateCanonAnimation instanceof AnimationDrawable) {
            ((AnimationDrawable)pirateCanonAnimation).stop();
        }
        stopDemoAnimation();
    }

    private void startPirateCanonDemo(GameLevel level) {
        stopDemoAnimation();

        int countFrames = MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER;
        int duration = (int) ((3.0 - level.getValue()) * 1.7) * 1000;
        pirateDemoCanonAnimationExt = new AnimationDrawableExt() {
            @Override
            public void onAnimationFinish() {
                if (pirateShotAnimation != null) {
                    pirateShotAnimation.stop();
                    findViewById(R.id.pirate_shot).setVisibility(View.VISIBLE);
                    pirateShotAnimation.start();
                }
            }
        };
        if (drawablesPirateCanon == null) {
            drawablesPirateCanon = new Drawable[]{
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim0.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim1.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim2.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim3.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim4.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim5.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim6.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim7.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim8.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim9.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim10.png"),
                    DrawableUtils.getDrawableFromAssets(this, "anim/pirate_canon_anim11.png")
            };
        }

        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[0], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[1], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[2], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[3], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[4], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[5], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[6], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[7], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[8], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[9], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[10], duration / countFrames);
        pirateDemoCanonAnimationExt.addFrame(drawablesPirateCanon[11], duration / countFrames);
        pirateDemoCanonAnimationExt.setOneShot(true);

        final ImageView pirateShot = (ImageView) findViewById(R.id.pirate_shot);
        if (pirateShotAnimation == null) {
            initPirateShot();
        } else {
            pirateShotAnimation.stop();
        }
        pirateShot.setImageDrawable(null);
        pirateShot.setImageDrawable(pirateShotAnimation);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (pirateDemoCanonAnimationExt != null) {
                    pirateDemoCanonAnimationExt.stop();
                }
                GameActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pirateDemoCanonAnimationExt != null) {
                            pirateDemoCanonAnimationExt.start();
                        }
                    }
                });
            }
        };
        pirateCanon.setImageDrawable(pirateDemoCanonAnimationExt);
        pirateDemoTimer = new Timer();
        pirateDemoTimer.schedule(timerTask, 50, duration + 150);

    }

    private void stopDemoAnimation() {
        if (pirateDemoCanonAnimationExt != null) {
            if (timerTask != null) {
                pirateDemoCanonAnimationExt.setVisible(false, false);
                pirateDemoCanonAnimationExt.getmAnimationHandler().removeCallbacks(pirateDemoCanonAnimationExt.getRunnablePostDel());
                findViewById(R.id.pirate_shot).setVisibility(View.INVISIBLE);
            }
            pirateDemoCanonAnimationExt.stop();
            pirateDemoCanonAnimationExt = null;
        }
        if (pirateShotAnimation != null) {
            pirateShotAnimation.setVisible(false, false);
            pirateShotAnimation.stop();
            pirateShotAnimation = null;
        }
        if (pirateDemoTimer != null) {
            pirateDemoTimer.cancel();
            pirateDemoTimer = null;
        }
    }

    @Override
    protected void onResume() {
        Bundle b = getIntent().getExtras();
        if(b != null)
            isPremiumGameMode = b.getBoolean("premium_mode");

        if (isPremiumGameMode) {
            number = getSettings().getInt(
                    TwoXTwoIs4Activity.premiumGameNumberSettingsKey, TwoXTwoIs4Activity.minPremiumGameFreeNumber
            );
        }
        else {
            number = getSettings().getInt(GAME_NUMBER, number);
        }
        MaxCurrentLearnNumber.setCurrentNumber(getSettings().getInt(CURRENT_NUMBER, getMaxFreeNumber()));

        super.onResume();
        //Init UI elements
        if((engine!=null && engine.combinationsCount()==0 &&
                findViewById(R.id.result_id).getVisibility()!=View.VISIBLE)|| engine==null){
        ourCanon = (ImageView) findViewById(R.id.our_cannon);
        pirateCanon = (ImageView) findViewById(R.id.pirate_cannon);
        t1 = (Button) findViewById(R.id.choose1);
        t2 = (Button) findViewById(R.id.choose2);
        t3 = (Button) findViewById(R.id.choose3);
        t4 = (Button) findViewById(R.id.choose4);
        t5 = (Button) findViewById(R.id.choose5);

        // init settings buttons
        multipl = (ImageView)findViewById(R.id.settings_multiplication);
        division = (ImageView)findViewById(R.id.settings_division);
        easy = (ImageView)findViewById(R.id.settings_easy);
        medium = (ImageView)findViewById(R.id.settings_medium);
        hard = (ImageView)findViewById(R.id.settings_hard);


        findViewById(R.id.game_main_layout).
                setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.GAME_BACKGROUND));
        findViewById(R.id.bomb_top).
                setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        t1.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        t2.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        t3.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        t4.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));
        t5.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BOMB));

        if(findViewById(R.id.bottom_field).getBackground()==null)
        {
            findViewById(R.id.bottom_field).
                    setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BACKGROUND_SMALL));
            findViewById(R.id.result_view_id).
                    setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.SHOW_RESULTS_BG));
            Drawable gameSettingsEmpty = DrawableUtils.getDrawableFromAssets(this, DrawableUtils.GAME_SETTINGS_EMPTY);
           findViewById(R.id.result_id).
                setBackgroundDrawable(gameSettingsEmpty);
            findViewById(R.id.settings_id).
                    setBackgroundDrawable(gameSettingsEmpty);
            findViewById(R.id.settings_view_id).
                    setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.GAME_SETTINGS_BOX));
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/MAFW.TTF");
        TextView answerText = (TextView) findViewById(R.id.answer_id);
        answerText.setTypeface(tf);
        pauseBtn = (Button) findViewById(R.id.pause);
        pauseBtn.setBackgroundDrawable(DrawableUtils.getPause(this));

        helpBtn = (Button) findViewById(R.id.help);
        helpBtn.setBackgroundDrawable(DrawableUtils.getHelp(this));
        finishBtn = (Button) findViewById(R.id.finish);
        finishBtn.setBackgroundDrawable(DrawableUtils.getFinish(this));
        restartBtn = (Button) findViewById(R.id.restart);
        restartBtn.setBackgroundDrawable(DrawableUtils.getRestart(this));
            restartBtn.setVisibility(isPremiumGameMode ? View.INVISIBLE : View.VISIBLE);
        soundButton = (Button) findViewById(R.id.sound);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopExamine();
            }
        });
        final ImageView resultFinishButton = (ImageView) findViewById(R.id.result_finish);
            resultFinishButton.setImageDrawable(DrawableUtils.getFinish(this));
        resultFinishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopExamine();
                toMainView();
            }
        });
        resultFinishButton.setOnTouchListener(new GameControlButtonOnTouchListener(this, resultFinishButton,
                GameControlButtonOnTouchListener.TypeControlButton.FINISH_IMG));

        final ImageView resultRestartButton = (ImageView) findViewById(R.id.result_reload);
            resultRestartButton.setImageDrawable(DrawableUtils.getRestart(this));
        resultRestartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pirateWon = null;
                pirateLoose = null;
                pirateCanonShotsDone = 0;
                RelativeLayout resultsLayout = (RelativeLayout) findViewById(R.id.result_id);
                resultsLayout.setVisibility(View.GONE);
                stopExamine();
                stopTimers();
                disableChooseAndControlButtons();
                startPirateCanonDemo(gameLevel);
                startMainAnimation();
                prepareAndShowSettings();
                System.gc();
            }
        });
        resultRestartButton.setOnTouchListener(new GameControlButtonOnTouchListener(this, resultRestartButton,
                GameControlButtonOnTouchListener.TypeControlButton.RESTART_IMG));
            resultRestartButton.setVisibility(isPremiumGameMode ? View.INVISIBLE : View.VISIBLE);

            Button buy_button = (Button) findViewById(R.id.buy_button);
            buy_button.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.PRO));
            buy_button.setOnTouchListener(new BuyButtonTouchListener(this, R.id.buy_button));

            Button premium_button = (Button) findViewById(R.id.premium_button);
            premium_button.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.PRO));
            premium_button.setOnTouchListener(new BuyButtonTouchListener(this, R.id.premium_button));

            animationBall = (ImageView)findViewById(R.id.animation_ball);
        animationBall.setVisibility(View.INVISIBLE);
            if (!isPremiumGameMode)
                prepareAndShowSettings();
            else
                startGame();
            if(isHeapSmall){
                initByHeapSmall();
            }

        } else if (engine!=null && engine.combinationsCount()>0){
            MaxCurrentLearnNumber.setCurrentNumber(getSettings().getInt(CURRENT_NUMBER, getMaxFreeNumber()));
            if(engine!=null && engine.combinationsCount()>0){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startTimer();
                        updateLabels();
                        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        MediaPlayerFacade.playBackground(GameActivity.this, true);
                        if(isSoundOn){
                            soundButton.setBackgroundResource(R.drawable.sound_on);
                            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                        } else {
                            soundButton.setBackgroundResource(R.drawable.sound_off);
                            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                        }
                    }
                });
            } else {
                Log.d("GameActivity", "onRestart() engine is null or engine.combinationsCount() = 0");
            }
        }
    }

    private void initByHeapSmall() {
        if(sea==null){
            sea=DrawableUtils.getDrawableFromAssets(this, "anim/sea_anim1.png");
        }
        findViewById(R.id.sea).setBackgroundDrawable(sea);
        if(sun==null){
            sun=DrawableUtils.getDrawableFromAssets(this, "anim/sun_anim1.png");
        }
        findViewById(R.id.sun).setBackgroundDrawable(sun);

        if(ourShip==null){
            ourShip=DrawableUtils.getDrawableFromAssets(this, "anim/our_ship_anim1.png");
        }
        findViewById(R.id.our_ship).setBackgroundDrawable(ourShip);

        if(pirateShip==null){
            pirateShip=DrawableUtils.getDrawableFromAssets(this, "anim/pirate_ship_anim1.png");
        }
        findViewById(R.id.pirate_ship).setBackgroundDrawable(pirateShip);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (!isHeapSmall) {
                startMainAnimation();
            }
            if(findViewById(R.id.settings_id).getVisibility()==View.VISIBLE)
            startPirateCanonDemo(gameLevel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerFacade.destroy();
        stopTimers();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopExamine();
        MediaPlayerFacade.destroy();
        DrawableUtils.unbindDrawables(findViewById(R.id.game_main_layout));
        DrawableUtils.unbindDrawables(findViewById(R.id.bottom_field));
        DrawableUtils.unbindDrawables(findViewById(R.id.bomb_layout));
        DrawableUtils.unbindDrawables(findViewById(R.id.our_ship));
        DrawableUtils.unbindDrawables(findViewById(R.id.pirate_ship));
        DrawableUtils.unbindDrawables(findViewById(R.id.sea));
        DrawableUtils.unbindDrawables(findViewById(R.id.our_cannon));
        DrawableUtils.unbindDrawables(findViewById(R.id.our_shot));
        DrawableUtils.unbindDrawables(findViewById(R.id.pirate_cannon));
        DrawableUtils.unbindDrawables(findViewById(R.id.pirate_shot));
        DrawableUtils.unbindDrawables(findViewById(R.id.result_id));
        DrawableUtils.unbindDrawables(findViewById(R.id.roger));
        DrawableUtils.unbindDrawables(findViewById(R.id.settings_id));
        seaFrameAnimation = null;
        sunFrameAnimation= null;
        pirateShipAnimation = null;
        pirateShipCrashFrameAnimation= null;
        ourShipAnimation = null;
        pirateLoose= null;
        pirateWon= null;
        pirateShotAnimation= null;
        ourShipCrashFrameAnimation= null;
        ourShotAnimation=null;
        pirateShotAnimation=null;
        rogerWon=null;
        cup=null;
        rogerLoose=null;
        drawablesPirateCanon = null;

        System.gc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isPremiumGameMode) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(GAME_NUMBER, number);
            editor.commit();
            int level = 1;
            if (gameLevel == GameLevel.MEDIUM) level = 2;
            else if (gameLevel == GameLevel.HARD) level = 3;
            editor.putInt(GAME_LEVEL, level);
            editor.commit();

            int operation = 1;
            if (operationType == OperationType.DIVISION) operation = 2;
            editor.putInt(GAME_OPERATION, operation);
            editor.commit();


            editor.putInt(RESULT_SHOW_COUNT, resultSowDialogCounter);
            editor.commit();

            editor.putBoolean(RATE_DIALOG_SHOW, isShowRateDialog);
            editor.commit();
        }

        AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

}
