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

package com.oktogames.app_2x2is4.light.android.graphics.drawable;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

/**
 */
public abstract class AnimationDrawableExt extends AnimationDrawable {

    private Handler mAnimationHandler  = new Handler();
    private Runnable runnablePostDel;

    public AnimationDrawableExt(AnimationDrawable aniDrawable) {
        for (int i = 0; i < aniDrawable.getNumberOfFrames(); i++) {
            this.addFrame(aniDrawable.getFrame(i), aniDrawable.getDuration(i));
        }
    }

    public AnimationDrawableExt() {
    }

    @Override
    public void start() {
        super.start();
        runnablePostDel = new Runnable() {
            public void run() {
                onAnimationFinish();
            }
        };
        mAnimationHandler.postDelayed(runnablePostDel, getTotalDuration() + 50);
    }


    public int getTotalDuration() {

        int iDuration = 0;

        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }

        return iDuration;
    }

    public abstract void onAnimationFinish();

    public Runnable getRunnablePostDel() {
        return runnablePostDel;
    }

    public Handler getmAnimationHandler() {
        return mAnimationHandler;
    }

    @Override
    public boolean isOneShot() {
        return super.isOneShot();
    }

    @Override
    public boolean isRunning() {
        return super.isRunning();
    }
}
