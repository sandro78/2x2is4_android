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
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.engine.OperationType;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;
import com.oktogames.app_2x2is4.light.view.TwoDirectionsViewSwitcher;

/**
 * Learn activity.
 */
public class LearnActivity extends SharedActivity{

    private static final int ROWS = 2;
    private TwoDirectionsViewSwitcher viewSwitcher = null;
    private ImageView up;
    private ImageView down;
    private ImageView left;
    private ImageView reight;

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.learn);
        up= (ImageView)findViewById(R.id.up_btn);
        down= (ImageView)findViewById(R.id.down_btn);
        left= (ImageView)findViewById(R.id.left_btn);
        reight = (ImageView)findViewById(R.id.right_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentNumber = getSettings().getInt(CURRENT_NUMBER, 8);
        int currentPositions = getSettings().getInt(LEARN_CURRENT_POSITION, 0);
        currentNumber +=3;

        if (viewSwitcher == null) {
            viewSwitcher = new TwoDirectionsViewSwitcher(this, ROWS,
                    currentPositions, this);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.table_row);
            viewSwitcher.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
            
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 2, OperationType.MULTIPLICATION, currentNumber), 0);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 3, OperationType.MULTIPLICATION, currentNumber), 1);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 4, OperationType.MULTIPLICATION, currentNumber), 2);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 5, OperationType.MULTIPLICATION, currentNumber), 3);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 6, OperationType.MULTIPLICATION, currentNumber), 4);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 7, OperationType.MULTIPLICATION, currentNumber), 5);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 8, OperationType.MULTIPLICATION, currentNumber), 6);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 9, OperationType.MULTIPLICATION, currentNumber), 7);

            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 2, OperationType.DIVISION, currentNumber), 8);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 3, OperationType.DIVISION, currentNumber), 9);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 4, OperationType.DIVISION, currentNumber), 10);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 5, OperationType.DIVISION, currentNumber), 11);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 6, OperationType.DIVISION, currentNumber), 12);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 7, OperationType.DIVISION, currentNumber), 13);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 8, OperationType.DIVISION, currentNumber), 14);
            viewSwitcher.addView(DrawableUtils.makeLearnPage(this, 9, OperationType.DIVISION, currentNumber), 15);
            linearLayout.addView(viewSwitcher, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                    FrameLayout.LayoutParams.FILL_PARENT, Gravity.CENTER_HORIZONTAL));
        }

        viewSwitcher.invalidate();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(LEARN_CURRENT_POSITION, viewSwitcher.getCurrentScreen());
        editor.commit();
    }

    public ImageView getUp() {
        return up;
    }

    public ImageView getDown() {
        return down;
    }

    public ImageView getLeft() {
        return left;
    }

    public ImageView getReight() {
        return reight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DrawableUtils.unbindDrawables(viewSwitcher);
        DrawableUtils.unbindDrawables(findViewById(R.id.table_row));
        viewSwitcher = null;
        System.gc();
    }
}


