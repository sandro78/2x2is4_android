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

package com.oktogames.app_2x2is4.light.action;

import android.view.MotionEvent;
import android.view.View;
import com.oktogames.app_2x2is4.light.activity.SharedActivity;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;
import com.oktogames.app_2x2is4.light.utils.MediaPlayerFacade;

/**
 * Main buttons touch listener (change background after touch).
 */
public class ButtonOnTouchListener implements View.OnTouchListener{
    private View button;
    private SharedActivity activity;

    public ButtonOnTouchListener(SharedActivity activity, View button) {
        this.button = button;
        this.activity = activity;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                button.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(activity, DrawableUtils.BUTTON_SELECTED));
                MediaPlayerFacade.playClick(activity);
                break;
            case MotionEvent.ACTION_UP:
                button.setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(activity, DrawableUtils.BUTTON));
                break;

        }
        return false;
    }
}
