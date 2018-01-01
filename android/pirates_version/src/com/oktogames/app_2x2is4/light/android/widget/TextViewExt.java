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

package com.oktogames.app_2x2is4.light.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.oktogames.app_2x2is4.light.utils.MaxCurrentLearnNumber;

/**
 * Class extendet standart android widget TaxtView.
 */
public class TextViewExt extends TextViewSettingsExt {

    public TextViewExt(Context context) {
        super(context);
    }

    public TextViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewExt(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        int startSpan = text.toString().indexOf("=");
        int textViewNumber;
        if (text.toString().contains(":")) {
            textViewNumber = Integer.parseInt(text.toString().substring(startSpan + 1, text.toString().length()));
        } else {
            textViewNumber = Integer.parseInt(text.toString().substring(text.toString().indexOf("x") + 1, startSpan));
        }
        if (MaxCurrentLearnNumber.getCurrentNumber() < textViewNumber) {
            setVisibility(View.INVISIBLE);
        }
    }

    public void setTextSize(float size) {
        super.setTextSize(size);
    }

    public void setTextColor(int color) {
        super.setTextColor(color);
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        super.setShadowLayer(radius, dx, dy, color);
    }

    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
    }

    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
