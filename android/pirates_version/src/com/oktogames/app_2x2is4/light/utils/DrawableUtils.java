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

package com.oktogames.app_2x2is4.light.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.oktogames.app_2x2is4.light.engine.MultiplicationTableEngineImpl;
import com.oktogames.app_2x2is4.light.engine.OperationType;

import java.io.IOException;

/**
 * Class contain utility methods for change Drawable resources.
 */
public class DrawableUtils {
    public final static String LEARN_BG_LANDSCAPE = "img/learn_bg_landscape.jpg";
    public final static String COMMON_BACKGROUND = "img/common_background.jpg";
    public final static String BACKGROUND_SMALL = "img/background_small.png";
    public final static String BUTTON = "img/button.png";
    public final static String BUTTON_SELECTED = "img/button_selected.png";
    public final static String GAME_BACKGROUND = "img/background.png";
    public final static String BOMB = "img/bomb.png";
    public final static String PRO = "img/buy_button.png";
    public final static String PRO_PRESSED = "img/buy_button_pressed.png";
    public final static String BOMB_PRESSED = "img/bomb_pressed.png";
    public static final String SHOW_RESULTS_BG = "img/results_bg_landscape.png";
    public static final String GAME_SETTINGS_EMPTY = "img/game_settings_empty.png";
    public static final String GAME_SETTINGS_BOX = "img/game_settings_box.png";
    public static final String SETTINGS_BG_LANDSCAPE_FONE = "img/settings_fone_landscape.png";
    private static Drawable finishPressed;
    private static Drawable finish;
    private static Drawable restartPressed;
    private static Drawable restart;
    private static Drawable helpPressed;
    private static Drawable help;
    private static Drawable pausePressed;
    private static Drawable pause;
    private static Drawable cancelPressed;
    private static Drawable cancel;
    private static Drawable startPressed;
    private static Drawable start;
    private static Drawable bombPressed;
    private static Drawable bomb;
    private static Drawable help2;
    private static final int tableScale= 2;
    public enum ScreenSize {NORMAL, LARGE, XLARGE, SMALL};

    public static int getPixels(Activity activity, int dipPixel) {
        final float scale = activity.getApplicationContext().getResources().getDisplayMetrics().densityDpi;
        return (int) (dipPixel * scale / 160);
    }

    public static int getDpPixels(Context context, int pixel) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (pixel * 160 / scale);
    }

    public static float getScale(Activity activity) {
        return 0.6f;
    }

    private static AssetManager assets;
    private static BitmapFactory.Options options;
    private static Bitmap bitmapImage;
    private static int screenWidth;
    private static int screenHeight;

    public static Drawable getDrawableFromAssets(Context context, String fileName) {
        return getDrawableFromAssets(context, fileName, -1, -1);

    }

    public static Drawable getDrawableFromAssets(Context context, String fileName, int widthD, int heightD) {
        if (options==null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            options = new BitmapFactory.Options();
            options.inTempStorage = new byte[4096];
            options.inScaled = true;
            options.inDensity = metrics.densityDpi;

            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
        }
        if (assets==null) {
            assets = context.getAssets();
        }

        try {
            try {
                bitmapImage = BitmapFactory.decodeStream(assets.open(fileName), null, options);
            } catch (OutOfMemoryError e) {
                System.gc();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e1) {}
                bitmapImage = BitmapFactory.decodeStream(assets.open(fileName), null, options);
            }
            int width = bitmapImage.getWidth();
            int height = bitmapImage.getHeight();
            double scaleW = 1;
            double scaleH = 1;
            int scale = 1;
            if(width/screenWidth>1){
                scaleW = Math.floor(width/screenWidth);
            } 
            
            if (height/screenHeight>1){
                scaleH = Math.floor(height/screenHeight);
            }
            
            if(scaleH>1 || scaleW >1){
              scale = (int)Math.floor(Math.max(scaleH, scaleW));
            }


            if(scale>1 || (widthD>0 && heightD>0)){
                if (widthD>0 && heightD>0){
                    width = widthD;
                    height = heightD;
                }

                Log.d("DrawableUtils", "Scaled img:"+ fileName+ ", width="+width+", height="+height+
                        ", scaleW="+scaleW+", scaleH="+scaleH+", scale="+scale);
                bitmapImage = Bitmap.createScaledBitmap(bitmapImage, width/scale,
                        height/scale, true);
            }


            
        } catch (IOException e) {
            Log.d("getDrawableFromAssets()", fileName + " is not fount in the assets");
        }

        return new BitmapDrawable(bitmapImage);

    }

    public static Drawable changeDrawableDimention(Drawable drawable, int newWidth, int newHeight) {
        if (drawable != null) {
            Bitmap bitmapOrg = ((BitmapDrawable) drawable).getBitmap();
            int width = bitmapOrg.getWidth();
            int height = bitmapOrg.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height, matrix, true);
            drawable = new BitmapDrawable(resizedBitmap);
            return drawable;
        }
        return null;
    }

    public static TextView getTextView(Activity context, int number, int secondParam, int height, int width, OperationType type) {
        TextView rowTextView = new TextView(context);
        String strRow;
        if (type == OperationType.MULTIPLICATION) {
            strRow = number + "x" + secondParam + "=" + number * secondParam;
        } else {
            strRow = number * secondParam + ":" + number + "=" + secondParam;
        }
        int startSpan = strRow.indexOf("=");
        Spannable textRowSpan = new SpannableString(strRow);
        textRowSpan.setSpan(new ForegroundColorSpan(Color.WHITE), startSpan + 1, strRow.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        rowTextView.setHeight((int) (height * getScale(context)));
        rowTextView.setWidth((int) (width * getScale(context)));
        rowTextView.setText(textRowSpan);
        int textSize = 45;
        switch (isTablet(context)){
            case XLARGE:
                textSize = 110;
                break;
            case LARGE:
                textSize = 55;
                break;
            case SMALL:
                textSize = 42;
                break;
        }

        rowTextView.setTextSize(textSize * getScale(context));
        rowTextView.setTextColor(Color.BLACK);
        rowTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        rowTextView.setTypeface(null, 1);
        rowTextView.setPadding(0,0,0,0);
        return rowTextView;
    }

    public static TableLayout makeLearnPage(Activity activity, int number, OperationType type, int currentNumber) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth(), height = display.getHeight();

        int rows = width > height ? 3 : 2;
        int cells = width > height ? 4 : 6;

        TableLayout mainTableLayout = new TableLayout(activity);
        TextView headerTextView = new TextView(activity);
        int labelWidth = width / 2;
        int labelHeight = (int) (height * 0.2);
        headerTextView.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        headerTextView.setTextColor(Color.WHITE);
        int textSize = 110;
        switch (isTablet(activity)){
            case XLARGE:
                textSize = 110*tableScale;
                break;
            case LARGE:
                textSize = 115;
                break;
            case SMALL:
                textSize = 90;
                break;
        }
        headerTextView.setTextSize(textSize * getScale(activity));
        headerTextView.setPadding(0, 0, 0, 0);
        headerTextView.setLines(1);
        headerTextView.setSingleLine();
        headerTextView.setText(String.valueOf(number));
        headerTextView.setTypeface(null, 1);
        headerTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow tableRowHeader = new TableRow(activity);
        tableRowHeader.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.FILL_PARENT, 1));
        tableRowHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRowHeader.addView(new TextView(activity), new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.FILL_PARENT, 1));
        tableRowHeader.addView(headerTextView);
        tableRowHeader.addView(new TextView(activity), new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.FILL_PARENT, 1));


        int padding = 5;
        switch (isTablet(activity)){
            case XLARGE:
                padding = 5*tableScale;
                break;
            case LARGE:
                padding = 5;
                break;
            case SMALL:
                padding = 3;
                break;
        }


        int paddingKoef = 5;

        switch (isTablet(activity)){
            case XLARGE:
                paddingKoef = 25*tableScale;
                break;
            case LARGE:
                paddingKoef = 10;
                break;
            case SMALL:
                paddingKoef = 3;
        }

        TableRow emptyRowHeader = new TableRow(activity);
        emptyRowHeader.addView(new TextView(activity));
        emptyRowHeader.setPadding((int) (padding * getScale(activity)), (int) (paddingKoef*getScale(activity)),
                padding, (int) (paddingKoef*getScale(activity)));

        mainTableLayout.addView(emptyRowHeader);
        mainTableLayout.addView(tableRowHeader);


        if (isTablet(activity)==ScreenSize.LARGE || isTablet(activity)==ScreenSize.XLARGE) {
            TableRow emptyRowHeader2 = new TableRow(activity);
            emptyRowHeader2.addView(new TextView(activity));
            emptyRowHeader2.setPadding((int) (padding * getScale(activity)), (int) (paddingKoef*getScale(activity)),
                    padding, (int) (paddingKoef*getScale(activity)));
            mainTableLayout.addView(emptyRowHeader2);
        }

        for (int i = 1; i <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / rows; i++) {
            TableRow tableRow = new TableRow(activity);
            tableRow.setGravity(Gravity.CENTER);
            tableRow.setPadding(0,0,0,0);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.FILL_PARENT));

            for (int j = i; j <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE; j = j + cells) {
                TextView textViewExt = DrawableUtils.getTextView(activity, number, j,
                        labelHeight + padding,
                        labelWidth + paddingKoef*2, type);

                if (j > currentNumber) {
                    textViewExt.setVisibility(View.INVISIBLE);
                }
                tableRow.addView(textViewExt);
            }
            mainTableLayout.addView(tableRow);
        }
        return mainTableLayout;
    }

    public static void unbindDrawables(View view) {
        if (view != null && view.getBackground() != null) {
            view.getBackground().setCallback(null);
            view.setBackgroundDrawable(null);
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            try
            {
                ((ViewGroup) view).removeAllViews();
            }
            catch(UnsupportedOperationException ignore)
            {
                //if can't remove all view (e.g. adapter view) - no problem
            }
        }
    }

    public static String getTimerString(int timeCounter) {
        int hours = (timeCounter / 60 / 60 % 60);
        int minutes = (timeCounter / 60 % 60);
        int seconds = (timeCounter % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Drawable getFinishPressed(Context context){
        if(finishPressed==null){
            finishPressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/finish_pressed.png");
        }
        return finishPressed;
    }

    public static Drawable getFinish(Context context){
        if(finish==null){
            finish = DrawableUtils.getDrawableFromAssets(context,
                    "img/finish.png");
        }
        return finish;
    }

    public static Drawable getRestartPressed(Context context){
        if(restartPressed==null){
            restartPressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/restart_pressed.png");
        }
        return restartPressed;
    }
    public static Drawable getRestart(Context context){
        if(restart==null){
            restart = DrawableUtils.getDrawableFromAssets(context,
                    "img/restart.png");
        }
        return restart;
    }

    public static Drawable getHelpPressed(Context context){
        if(helpPressed==null){
            helpPressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/help_pressed.png");
        }
        return helpPressed;
    }
    public static Drawable getHelp(Context context){
        if(help==null){
            help = DrawableUtils.getDrawableFromAssets(context,
                    "img/help.png");
        }
        return help;
    }

    public static Drawable getPausePressed(Context context){
        if(pausePressed==null){
            pausePressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/pause_pressed.png");
        }
        return pausePressed;
    }
    public static Drawable getPause(Context context){
        if(pause==null){
            pause = DrawableUtils.getDrawableFromAssets(context,
                    "img/pause.png");
        }
        return pause;
    }

    public static Drawable getCancelPressed(Context context){
        if(cancelPressed==null){
            cancelPressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/cancel_pressed.png");
        }
        return cancelPressed;
    }
    public static Drawable getCancel(Context context){
        if(cancel==null){
            cancel = DrawableUtils.getDrawableFromAssets(context,
                    "img/cancel.png");
        }
        return cancel;
    }

    public static Drawable getStartPressed(Context context){
        if(startPressed==null){
            startPressed = DrawableUtils.getDrawableFromAssets(context,
                    "img/start_pressed.png");
        }
        return startPressed;
    }
    public static Drawable getStart(Context context){
        if(start==null){
            start = DrawableUtils.getDrawableFromAssets(context,
                    "img/start.png");
        }
        return start;
    }

    public static Drawable getBombPressed(Context context){
        if(bombPressed==null){
            bombPressed = DrawableUtils.getDrawableFromAssets(context,
                    BOMB_PRESSED);
        }
        return bombPressed;
    }
    public static Drawable getBomb(Context context){
        if(bomb==null){
            bomb = DrawableUtils.getDrawableFromAssets(context,
                    BOMB);
        }
        return bomb;
    }

    public static Drawable getHelp2(Context context){
        if(help2==null){
            help2 = DrawableUtils.getDrawableFromAssets(context,
                    "img/help2.png");
        }
        return help2;
    }

    public static ScreenSize isTablet(Context context) {
        /*  SCREENLAYOUT_SIZE_LARGE = 3;
            SCREENLAYOUT_SIZE_XLARGE = 4;*/
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >3) return ScreenSize.XLARGE;
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==3) return ScreenSize.LARGE;
        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==2) return ScreenSize.NORMAL;
        return ScreenSize.SMALL;
    }
}

