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

package com.oktogames.app_2x2is4.light;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.oktogames.app_2x2is4.light.action.ButtonOnTouchListener;
import com.oktogames.app_2x2is4.light.activity.*;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;
import com.oktogames.app_2x2is4.light.utils.MediaPlayerFacade;

public class TwoXTwoIs4Activity extends SharedActivity {
    private boolean isSecondTimeStart;
    private RelativeLayout bannerRelativeLayout;
    private ImageView closeImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TwoXTwoIs4Activity _self = this;
        isSecondTimeStart = getSettings().getBoolean(SHOW_BUNNER, true);
        int layouteId = R.layout.main;
        if (DrawableUtils.isTablet(this)== DrawableUtils.ScreenSize.LARGE &&
                (getWindowManager().getDefaultDisplay().getWidth()==800 &&
                        getWindowManager().getDefaultDisplay().getHeight() == 600)) {
            layouteId = R.layout.main1024;
        }

        if (!isSecondTimeStart) {
            RelativeLayout rootRelativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT);
            rootRelativeLayout.setLayoutParams(params);

            View mainView = ((LayoutInflater) getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE)).inflate(layouteId,
                    null);
            mainView.setLayoutParams(params);
            rootRelativeLayout.addView(mainView, 0);

            bannerRelativeLayout = new RelativeLayout(this);
            bannerRelativeLayout.setBackgroundDrawable(
                    DrawableUtils.getDrawableFromAssets(this, DrawableUtils.GAME_SETTINGS_EMPTY));
            bannerRelativeLayout.setLayoutParams(params);
            ImageView bannerImg = new ImageView(this);
            closeImageView = new ImageView(this);

            if (DrawableUtils.isTablet(this)!= DrawableUtils.ScreenSize.SMALL) {
                bannerImg.setImageDrawable(DrawableUtils.getDrawableFromAssets(this, "img/banner_android.png"));
            } else {
                bannerImg.setImageDrawable(DrawableUtils.getDrawableFromAssets(this, "img/banner_android_small.png"));
            }
            float scale = 0.6f;
            int width = (int)(getWindowManager().getDefaultDisplay().getWidth()*scale);

            RelativeLayout.LayoutParams paramsImg = new RelativeLayout.LayoutParams(width, (int)(width/1.33));
            paramsImg.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsImg.addRule(RelativeLayout.CENTER_HORIZONTAL);
            bannerImg.setLayoutParams(paramsImg);
            bannerImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openWebURL("http://2x2is4.net");
                    bannerRelativeLayout.setVisibility(View.INVISIBLE);
                    closeImageView.setVisibility(View.INVISIBLE);
                }
            });
            bannerRelativeLayout.addView(bannerImg, 0);

            RelativeLayout closeBannerRelativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams paramsCloseBanner = new RelativeLayout.LayoutParams(width, (int)(width/1.33));
            paramsCloseBanner.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsCloseBanner.addRule(RelativeLayout.CENTER_HORIZONTAL);
            closeBannerRelativeLayout.setLayoutParams(paramsCloseBanner);

            int widthClose = (int)(getWindowManager().getDefaultDisplay().getWidth()*0.05);

            RelativeLayout.LayoutParams closeImgLayoutParams =
                    new RelativeLayout.LayoutParams(widthClose, widthClose);
            closeImgLayoutParams.addRule(RelativeLayout.ALIGN_TOP);
            closeImgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            closeImageView.setLayoutParams(closeImgLayoutParams);
            closeImageView.setImageResource(R.drawable.close);
            closeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bannerRelativeLayout.setVisibility(View.INVISIBLE);
                    closeImageView.setVisibility(View.INVISIBLE);
                }
            });

            closeBannerRelativeLayout.addView(closeImageView, 0);

            rootRelativeLayout.addView(bannerRelativeLayout, 1);
            rootRelativeLayout.addView(closeBannerRelativeLayout, 2);

            setContentView(rootRelativeLayout);
        } else {
            setContentView(layouteId);
        }


        if (findViewById(R.id.main_layout).getBackground() == null) {
            findViewById(R.id.main_layout).
                    setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.COMMON_BACKGROUND));
        }

        final Button buttonGame = (Button) findViewById(R.id.buttonGame);
        buttonGame.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BUTTON));
        buttonGame.setOnTouchListener(new ButtonOnTouchListener(this, buttonGame));
        buttonGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TwoXTwoIs4Activity.this, GameActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        final Button buttonLearn = (Button) findViewById(R.id.buttonLearn);
        buttonLearn.setOnTouchListener(new ButtonOnTouchListener(this, buttonLearn));
        buttonLearn.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BUTTON));
        buttonLearn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TwoXTwoIs4Activity.this, LearnActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        final Button buttonHistory = (Button) findViewById(R.id.buttonHistory);
        buttonHistory.setOnTouchListener(new ButtonOnTouchListener(this, buttonHistory));
        buttonHistory.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BUTTON));
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TwoXTwoIs4Activity.this, HistoryActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        final Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.BUTTON));
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(TwoXTwoIs4Activity.this, SettingsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        buttonSettings.setOnTouchListener(new ButtonOnTouchListener(this, buttonSettings));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerFacade.destroy();
        DrawableUtils.unbindDrawables(findViewById(R.id.main_layout));
        findViewById(R.id.main_layout).setBackgroundDrawable(null);
        DrawableUtils.unbindDrawables(findViewById(R.id.linearLayout1));
        DrawableUtils.unbindDrawables(findViewById(R.id.buttonGame));
        DrawableUtils.unbindDrawables(findViewById(R.id.buttonLearn));
        DrawableUtils.unbindDrawables(findViewById(R.id.buttonHistory));
        DrawableUtils.unbindDrawables(findViewById(R.id.buttonSettings));
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse(inURL) );
        startActivity( browse );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isSecondTimeStart){
            bannerRelativeLayout.setVisibility(View.INVISIBLE);
            closeImageView.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = getEditor();
                    editor.putBoolean(SHOW_BUNNER, true);
                    isSecondTimeStart = true;
                    editor.commit();
                    bannerRelativeLayout.startAnimation(
                            AnimationUtils.loadAnimation(TwoXTwoIs4Activity.this, R.anim.banner));
                    bannerRelativeLayout.setVisibility(View.VISIBLE);
                    closeImageView.startAnimation(
                            AnimationUtils.loadAnimation(TwoXTwoIs4Activity.this, R.anim.banner));
                    closeImageView.setVisibility(View.VISIBLE);
                }
            }, 100);
        }
    }
}
