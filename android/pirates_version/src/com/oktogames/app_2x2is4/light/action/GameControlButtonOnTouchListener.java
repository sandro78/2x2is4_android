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

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;

/**
 * Game control buttons touch listener.
 */
public class GameControlButtonOnTouchListener implements View.OnTouchListener{
    private View button;
    private Context context;

    public enum TypeControlButton{PAUSE, HELP, FINISH, RESTART, BOMB, SOUND, FINISH_IMG, RESTART_IMG, START, CANCEL}
    public TypeControlButton type;

    public GameControlButtonOnTouchListener(Context context, View button, TypeControlButton type) {
        this.button = button;
        this.type = type;
        this.context = context;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                switch (type){
                    case FINISH:
                        button.setBackgroundDrawable(DrawableUtils.getFinishPressed(context));
                        break;
                    case HELP:
                        button.setBackgroundDrawable(DrawableUtils.getHelpPressed(context));
                        break;
                    case PAUSE:
                        button.setBackgroundDrawable(DrawableUtils.getPausePressed(context));
                        break;
                    case RESTART:
                        button.setBackgroundDrawable(DrawableUtils.getRestartPressed(context));
                        break;
                    case BOMB:
                        button.setBackgroundDrawable(DrawableUtils.getBombPressed(context));
                        break;
                    case FINISH_IMG:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getFinishPressed(context));
                        break;
                    case RESTART_IMG:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getRestartPressed(context));
                        break;
                    case CANCEL:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getCancelPressed(context));
                        break;
                    case START:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getStartPressed(context));
                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
                switch (type){
                    case FINISH:
                        button.setBackgroundDrawable(DrawableUtils.getFinish(context));
                        break;
                    case HELP:
                        button.setBackgroundDrawable(DrawableUtils.getHelp(context));
                        break;
                    case PAUSE:
                        button.setBackgroundDrawable(DrawableUtils.getPause(context));
                        break;
                    case RESTART:
                        button.setBackgroundDrawable(DrawableUtils.getRestart(context));
                        break;
                    case BOMB:
                        button.setBackgroundDrawable(DrawableUtils.getBomb(context));
                        break;
                    case FINISH_IMG:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getFinish(context));
                        break;
                    case RESTART_IMG:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getRestart(context));
                        break;
                    case CANCEL:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getCancel(context));
                        break;
                    case START:
                        ((ImageView)button).setImageDrawable(DrawableUtils.getStart(context));
                        break;
                }
                break;

        }
        return false;
    }
}
