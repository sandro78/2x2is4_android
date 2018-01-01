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

package com.oktogames.app_2x2is4.light.engine;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.oktogames.app_2x2is4.light.R;

/**
 *
 */
public class HeartsAnimatingEngine {
    private int shotsForWin = 0;
    private int chargedShots = 0;
    private ImageView[] heartsArray;
    private Activity activity;

    public HeartsAnimatingEngine(Activity activity, ImageView[] heartsArray) {
        this.heartsArray = heartsArray;
        this.shotsForWin = this.chargedShots = 0;
        this.activity = activity;
    }

    public int getStartIndex() {
        return (MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER - 1) / 2 - shotsForWin / 2;
    }

    public void chargeEngine(MultiplicationTableEngineImpl engine) {
        shotsForWin = chargedShots = engine.getShotsForWin();
        for (int i = getStartIndex(); i < getStartIndex() + shotsForWin; ++i) {
            heartsArray[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < getStartIndex(); ++i) {
            heartsArray[i].setVisibility(View.INVISIBLE);
        }
        for (int i = getStartIndex() + shotsForWin; i <
                MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER - 1; ++i) {
            heartsArray[i].setVisibility(View.INVISIBLE);

        }
    }

    public boolean eliminateOneHeart() {
        if (0 == chargedShots)
            return false;
        final int index = getStartIndex() + shotsForWin - chargedShots;
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.hearts);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                heartsArray[index].setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        heartsArray[index].startAnimation(animation);
        chargedShots--;
        return true;

    }
}
