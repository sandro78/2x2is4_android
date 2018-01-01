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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;

/**
 * Settings View
 */
public class SettingsActivity extends SharedActivity {
    private TextView numberTextView;
    private int currentNumber = 9;
    private int maxFreeNumber = 7;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsv);
        findViewById(R.id.settings_table_layout).
                setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.LEARN_BG_LANDSCAPE));
        findViewById(R.id.setting_t2_table).
                setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.SETTINGS_BG_LANDSCAPE_FONE));

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentNumber = getSettings().getInt(CURRENT_NUMBER, 7);
        numberTextView = (TextView) findViewById(R.id.settings_number_text);
        numberTextView.setText(String.valueOf(currentNumber + 3));

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seek_settings);
        seekBar.setIndeterminate(false);
        seekBar.setProgress(currentNumber);
        changeVisibilityOfRows(currentNumber, false, false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                currentNumber = progress;
                numberTextView.setText(String.valueOf(progress + 3));
                changeVisibilityOfRows(progress, true, false);

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress()>maxFreeNumber){
                    currentNumber = maxFreeNumber;
                    numberTextView.setText(String.valueOf(maxFreeNumber + 3));
                    changeVisibilityOfRows(maxFreeNumber, true, true);
                    seekBar.setProgress(maxFreeNumber);
                }
            }
        });
        int width = (int)(getWindowManager().getDefaultDisplay().getWidth()*0.4);
        Log.i("SettingsActivity", "h1 = "+ seekBar.getLayoutParams().height);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(width, seekBar.getLayoutParams().height));

        width = (int)(getWindowManager().getDefaultDisplay().getWidth()*0.8);
        int height = (int)(getWindowManager().getDefaultDisplay().getHeight()*0.7);
        LinearLayout mainLinearLayout = (LinearLayout)findViewById(R.id.t2_settings);
        Log.i("SettingsActivity", "h2 = "+ mainLinearLayout.getLayoutParams().height);
        mainLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();

    }

    private void saveSettings(){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(CURRENT_NUMBER, currentNumber);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        saveSettings();
        super.onDestroy();
        DrawableUtils.unbindDrawables(findViewById(R.id.settings_table_layout));
        DrawableUtils.unbindDrawables(findViewById(R.id.t2_settings));
        System.gc();
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        saveSettings();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        saveSettings();
        super.onRestart();
    }

    private void changeVisibilityOfRows(int progress, boolean isAnimate, boolean isProDialogShow) {
        TextView text1 = (TextView) findViewById(R.id.s1);
        TextView text2 = (TextView) findViewById(R.id.s2);
        TextView text3 = (TextView) findViewById(R.id.s3);
        TextView text4 = (TextView) findViewById(R.id.s4);
        TextView text5 = (TextView) findViewById(R.id.s5);
        TextView text6 = (TextView) findViewById(R.id.s6);
        TextView text7 = (TextView) findViewById(R.id.s7);
        TextView text8 = (TextView) findViewById(R.id.s8);
        TextView text9 = (TextView) findViewById(R.id.s9);


        TextView[] textArray = {text1, text2, text3, text4, text5, text6, text7, text8, text9};
        int i = 0;
        for (TextView tv : textArray) {
            if (isAnimate) {
                if ((i + 1) == progress) {
                    if (tv.getVisibility() == ViewGroup.INVISIBLE) {
                        tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
                    }
                } else if (i == progress) {
                    if (tv.getVisibility() == ViewGroup.VISIBLE) {
                        tv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
                    }
                }
            }
            if (i >= progress) {
                tv.setVisibility(View.INVISIBLE);
            } else {
                tv.setVisibility(View.VISIBLE);
            }
            i++;
        }
        if (isProDialogShow){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showBuyAlert(SettingsActivity.this, R.string.times_promo);
                }
            }, 300);
        }
    }

}
