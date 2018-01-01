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

import android.view.MotionEvent;
import android.view.View;
import com.oktogames.app_2x2is4.light.activity.SharedActivity;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;
import com.oktogames.app_2x2is4.light.utils.MediaPlayerFacade;

/**
 */
public class BuyButtonTouchListener implements View.OnTouchListener {
    private SharedActivity activity;
    private int element_id;

    public BuyButtonTouchListener(SharedActivity activity, int element_id) {
        this.activity = activity;
        this.element_id = element_id;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                activity.findViewById(this.element_id).setBackgroundDrawable(DrawableUtils.
                        getDrawableFromAssets(activity, DrawableUtils.PRO_PRESSED));
                MediaPlayerFacade.playClick(activity);
                break;
            case MotionEvent.ACTION_UP:
                activity.findViewById(this.element_id).setBackgroundDrawable(DrawableUtils.
                        getDrawableFromAssets(activity, DrawableUtils.PRO));
                break;
        }
        return false;
    }
}
