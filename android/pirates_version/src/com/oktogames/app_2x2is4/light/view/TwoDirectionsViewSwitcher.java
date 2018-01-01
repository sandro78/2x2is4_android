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

package com.oktogames.app_2x2is4.light.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.activity.LearnActivity;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;

/**
 * This class allows to switch between multiple screens (layouts) in the same
 * way as the Android home screen (Launcher application) both vertically and horizontally.
 * <p/>
 */
public class TwoDirectionsViewSwitcher extends ViewGroup {
    private static final int ANIMATION_SCREEN_SET_DURATION_MILLIS = 500;
    private boolean isXMove = false;
    private boolean isYMove = false;
    private boolean isMoveBegin = false;
    private int mRows = 2;
    private static final int SNAP_VELOCITY = 400;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private final static int TOUCH_STATE_REST = 0;
    private final static int TOUCH_STATE_SCROLLING = 1;
    private int mTouchState = TOUCH_STATE_REST;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mTouchSlop;
    private int mMaximumVelocity;
    private int mCurrentScreen;
    private LearnActivity activity;
    private Animation animation;
    private static Drawable drawableBackground;
    private static Drawable downWhite;
    private static Drawable leftWhite;
    private static Drawable rightWhite;
    private static Drawable upWhite;
    private static Drawable down;
    private static Drawable left;
    private static Drawable right;
    private static Drawable up;

    private enum Direction {LEFT, RIGHT, UP, DOWN}

    private int mLastAppliedHeight = 0;

    public TwoDirectionsViewSwitcher(Context context, int rows, int currentScreen, LearnActivity activity) {
        super(context);
        mCurrentScreen = currentScreen;
        mRows = rows;
        this.activity = activity;
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        if (drawableBackground==null) {
            drawableBackground = DrawableUtils.getDrawableFromAssets(activity, DrawableUtils.LEARN_BG_LANDSCAPE);
        }

        if (downWhite==null) {
            downWhite = DrawableUtils.getDrawableFromAssets(activity, "img/down_white.png");
        }
        if (leftWhite==null) {
            leftWhite = DrawableUtils.getDrawableFromAssets(activity, "img/left_white.png");
        }
        if (rightWhite==null) {
            rightWhite = DrawableUtils.getDrawableFromAssets(activity, "img/right_white.png");
        }
        if (upWhite==null) {
            upWhite = DrawableUtils.getDrawableFromAssets(activity, "img/up_white.png");
        }
        if (down==null) {
            down = DrawableUtils.getDrawableFromAssets(activity, "img/down.png");
        }
        if (left==null) {
            left = DrawableUtils.getDrawableFromAssets(activity, "img/left.png");
        }
        if (right==null) {
            right = DrawableUtils.getDrawableFromAssets(activity, "img/right.png");
        }
        if (up==null) {
            up = DrawableUtils.getDrawableFromAssets(activity, "img/up.png");
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        if (mLastAppliedHeight != height) {

            int row = mCurrentScreen / (getChildCount() / mRows);
            int cell = mCurrentScreen % (getChildCount() / mRows);
            mScroller.setFinalX(cell * width);
            mScroller.setFinalY(row * height);
            getChildAt(mCurrentScreen).setBackgroundDrawable(drawableBackground);
            invalidate();

            mLastAppliedHeight = height;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childWidth = 0;
        final int count = getChildCount();

        int currentViewPosition = 0;
        for (int j = 0; j < mRows; j++) {
            for (int i = 0; i < count / mRows; i++) {
                final View child = getChildAt(currentViewPosition);
                if (child.getVisibility() != View.GONE) {
                    childWidth = child.getMeasuredWidth();
                    final int childHeight = child.getMeasuredHeight();
                    child.layout(childLeft, j * childHeight, childLeft + childWidth, child.getMeasuredHeight() + j * childHeight);
                    currentViewPosition++;
                }
                childLeft += childWidth;
            }
            childLeft = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                markButtonsOnTouch();
                isMoveBegin = true;
                if(mCurrentScreen-1>=0){
                    if (getChildAt(mCurrentScreen-1).getBackground()==null) {
                        getChildAt(mCurrentScreen-1).setBackgroundDrawable(drawableBackground);
                    }
                } 
                if(mCurrentScreen+1<getChildCount()){
                    if (getChildAt(mCurrentScreen+1).getBackground()==null) {
                        getChildAt(mCurrentScreen+1).setBackgroundDrawable(drawableBackground);
                    }
                }
                
                if(mCurrentScreen>7){
                    if (getChildAt(mCurrentScreen-8).getBackground()==null) {
                        getChildAt(mCurrentScreen-8).setBackgroundDrawable(drawableBackground);
                    }
                } else {
                    if (getChildAt(mCurrentScreen+8).getBackground()==null) {
                        getChildAt(mCurrentScreen+8).setBackgroundDrawable(drawableBackground);
                    }
                }

                // Remember where the motion event started
                mLastMotionX = x;
                mLastMotionY = y;

                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;

                break;

            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                final int yDiff = (int) Math.abs(y - mLastMotionY);

                boolean xMoved = xDiff > mTouchSlop;
                boolean yMoved = yDiff > mTouchSlop;

                if (xMoved || yMoved) {
                    // Scroll if the user moved far enough along the X axis
                    mTouchState = TOUCH_STATE_SCROLLING;
                }

                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    // Scroll to follow the motion event
                    final int deltaX = (int) (mLastMotionX - x);
                    final int deltaY = (int) (mLastMotionY - y);
                    mLastMotionX = x;
                    mLastMotionY = y;

                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    if (Math.abs(deltaX) > Math.abs(deltaY) && isMoveBegin) {
                        isMoveBegin = false;
                        isXMove = true;
                        isYMove = false;
                    } else if (Math.abs(deltaX) < Math.abs(deltaY) && isMoveBegin) {
                        isMoveBegin = false;
                        isXMove = false;
                        isYMove = true;
                    }

                    if (isXMove) {
                        if (deltaX < 0) {
                            markDirection(Direction.LEFT);
                            if (scrollX > 0) {
                                scrollBy(Math.max(-scrollX, deltaX), 0);
                            }
                        } else if (deltaX > 0) {
                            markDirection(Direction.RIGHT);
                            final int availableToScroll = getChildAt(getChildCount() / mRows - 1).getRight() - scrollX - getWidth();
                            if (availableToScroll > 0) {
                                scrollBy(Math.min(availableToScroll, deltaX), 0);
                            }
                        }
                    } else if (isYMove) {
                        if (deltaY < 0) {
                            markDirection(Direction.UP);
                            if (scrollY > 0) {
                                scrollBy(0, Math.max(-scrollY, deltaY));
                            }
                        } else if (deltaY > 0) {
                            markDirection(Direction.DOWN);
                            final int availableToScroll = getChildAt(getChildCount() - 1).getBottom() - scrollY - getHeight();
                            if (availableToScroll > 0) {
                                scrollBy(0, Math.min(availableToScroll, deltaY));
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                invisibleButtons();
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int velocityX = (int) velocityTracker.getXVelocity();
                    int velocityY = (int) velocityTracker.getYVelocity();

                    if (isXMove) {
                        if (velocityX > SNAP_VELOCITY && mCurrentScreen >= 0) {
                            // Fling hard enough to move left
                            if(mCurrentScreen==8 || mCurrentScreen==0)break;
                            snapToXScreen(mCurrentScreen - 1);
                        } else if (velocityX < -SNAP_VELOCITY && mCurrentScreen <= getChildCount()) {
                            // Fling hard enough to move right
                            if(mCurrentScreen==15 || mCurrentScreen==7)break;
                            snapToXScreen(mCurrentScreen + 1);
                        } else {
                            Log.d(this.getClass().getName(), "snapToXDestination");
                            snapToXDestination();
                        }
                    } else {
                        if (velocityY > SNAP_VELOCITY && mCurrentScreen >= 0) {
                            // Fling hard enough to move UP
                            snapToYScreen(mCurrentScreen - getChildCount() / mRows);
                        } else if (velocityY < -SNAP_VELOCITY && mCurrentScreen <= getChildCount()) {
                            // Fling hard enough to move down
                            snapToYScreen(mCurrentScreen + getChildCount() / mRows);
                        } else {
                            snapToYDestination();
                        }
                    }

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    isXMove = false;
                    isYMove = false;
                    isMoveBegin = false;
                }

                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL: {
                mTouchState = TOUCH_STATE_REST;
            }
        }
        return true;
    }

    private void snapToXDestination() {
        final int screenHeight = getHeight();
        final int screenWidth = getWidth();
        final int whichScreenX = (getScrollX() + (screenWidth / 2)) / screenWidth;
        final int whichScreenY = (getScrollY() + (screenHeight / 2)) / screenHeight;

        snapToXScreen(whichScreenY * (getChildCount() / mRows) + whichScreenX);
    }

    private void snapToYDestination() {
        final int screenHeight = getHeight();
        final int screenWidth = getWidth();
        final int whichScreenX = (getScrollX() + (screenWidth / 2)) / screenWidth;
        final int whichScreenY = (getScrollY() + (screenHeight / 2)) / screenHeight;
        snapToYScreen(whichScreenY * (getChildCount() / mRows) + whichScreenX);
    }

    private void snapToXScreen(int whichScreen) {
        int cell = whichScreen % (getChildCount() / mRows);
        if (cell >=0 && cell < getChildCount() / mRows) {
            mCurrentScreen = whichScreen;
            final int newX = cell * getWidth();
            final int delta = newX - getScrollX();
            smoothScrollBy(delta, 0);
        }
    }

    private void snapToYScreen(int whichScreen) {
        if (whichScreen >= 0 && whichScreen < getChildCount()) {
            int row = whichScreen / (getChildCount() / mRows);
            mCurrentScreen = whichScreen;
            final int newY = row * getHeight();
            final int delta = newY - getScrollY();
            smoothScrollBy(0, delta);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    //Implementation was taken from here: http://www.matt-reid.co.uk/blog_post.php?id=62
    //There a lot of ideas how to make scrolling even more smoothly
    public void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, ANIMATION_SCREEN_SET_DURATION_MILLIS);
        invalidate();
    }

    public int getCurrentScreen() {
        return mCurrentScreen;
    }

    private void markButtonsOnTouch() {
        activity.getDown().clearAnimation();
        activity.getReight().clearAnimation();
        activity.getLeft().clearAnimation();
        activity.getUp().clearAnimation();

        if (mCurrentScreen == 0) {
            //down/reight
            activity.getDown().setVisibility(VISIBLE);
            activity.getReight().setVisibility(VISIBLE);
        } else if (mCurrentScreen > 0 && mCurrentScreen < 7) {
            //down /reight/ left
            activity.getDown().setVisibility(VISIBLE);
            activity.getReight().setVisibility(VISIBLE);
            activity.getLeft().setVisibility(VISIBLE);
        } else if (mCurrentScreen == 7) {
            //left / down
            activity.getLeft().setVisibility(VISIBLE);
            activity.getDown().setVisibility(VISIBLE);

        } else if (mCurrentScreen == 8) {
            //up / reight
            activity.getUp().setVisibility(VISIBLE);
            activity.getReight().setVisibility(VISIBLE);
        } else if (mCurrentScreen > 8 && mCurrentScreen < 15) {
            // up /left / reight
            activity.getUp().setVisibility(VISIBLE);
            activity.getLeft().setVisibility(VISIBLE);
            activity.getReight().setVisibility(VISIBLE);

        } else if (mCurrentScreen == 15) {
            // up / left
            activity.getUp().setVisibility(VISIBLE);
            activity.getLeft().setVisibility(VISIBLE);
        }
    }

    private void invisibleButtons() {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(activity, R.anim.fadeout1sec);
        }
        animation.setFillAfter(false);
        if (activity.getDown().getVisibility() == VISIBLE) {
            activity.getDown().startAnimation(animation);
        }
        if (activity.getReight().getVisibility() == VISIBLE) {
            activity.getReight().startAnimation(animation);
        }
        if (activity.getLeft().getVisibility() == VISIBLE) {
            activity.getLeft().startAnimation(animation);
        }
        if (activity.getUp().getVisibility() == VISIBLE) {
            activity.getUp().startAnimation(animation);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activity.getDown().setVisibility(INVISIBLE);
                activity.getReight().setVisibility(INVISIBLE);
                activity.getLeft().setVisibility(INVISIBLE);
                activity.getUp().setVisibility(INVISIBLE);
                backButtonsState();
                postInvalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
    
    private void markDirection(Direction direction) {
        backButtonsState();

        switch (direction) {
            case DOWN:
                activity.getDown().setImageDrawable(downWhite);
                break;
            case LEFT:
                activity.getLeft().setImageDrawable(leftWhite);
                break;
            case RIGHT:
                activity.getReight().setImageDrawable(rightWhite);
                break;
            case UP:
                activity.getUp().setImageDrawable(upWhite);
                break;
        }

    }

    private void backButtonsState() {
        activity.getDown().setImageDrawable(down);
        activity.getLeft().setImageDrawable(left);
        activity.getReight().setImageDrawable(right);
        activity.getUp().setImageDrawable(up);
    }

}

