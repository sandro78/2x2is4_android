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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import com.oktogames.app_2x2is4.light.DispatchActivity;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.TwoXTwoIs4Activity;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;

/**
 *  Activity for save settings this application.
 */
public class SharedActivity extends Activity {
    private static SharedPreferences settings= null;
    public final static String TWO_X_TWO_APP = "TWO_X_TWO_APP";
    public final static String LEARN_CURRENT_POSITION = "LEARN_CURRENT_POSITION";
    public final static String CURRENT_NUMBER = "CURRENT_NUMBER";
    public final static String HISTORY = "HISTORY";
    public static final String GAME_NUMBER = "GAME_NUMBER";
    public static final String GAME_LEVEL = "GAME_LEVEL";
    public static final String GAME_OPERATION = "GAME_OPERATION";
    public static final String LAST_ACTIVITY = "LAST_ACTIVITY";
    public static final String SHOW_BUNNER = "SHOW_BUNNER";
    public static final String RESULT_SHOW_COUNT = "RESULT_SHOW_COUNT";
    public static final String RATE_DIALOG_SHOW = "RATE_DIALOG_SHOW";
    public static final String MARKET_URL = "market://details?id=com.oktogames.app_2x2is4.light";
    public final static String WAS_UPDATED = "WAS_UPDATED";

    private static final byte[] SALT = new byte[] {
            46, -65, -30, -128, 103, 57, -74, 64, -51, -88, 95, 45, -77, 17, 36, 113, -11, -102, 64,
            89
    };
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn53coHQGWOoy3DFRsMjffzsIn0J8KKAMwuCirPeswdlbIIXHeU8XOSqVAo+uTR6/e4togzfY1bhMxmQ0gb29mJACJHOrT2Ei6344EKnj7ri9EHrmQ37Jzsfk2rLjqV82iiCyiFNOI9DGfSzCBqxFLe9umLugMiIPhr4Mpdkpd+XzsWtK1a2yXVtjxjhJ4bG2LqrUZqwnAf638TSa8FpWidaV4Ev6u/2kQ8xp9Q+DaRE/hEBxYlkoRFKZhRwhmHn5BRG/PQdKP0Omd2ZqYegrvXZ1Tpgk0eFReH/QLD2UqlyU9uKSFCJqDMhz0bWPvXLrQt9+we2LIT/7oAhTwPmQYQIDAQAB";

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        if (settings==null) {
            settings = getSharedPreferences(TWO_X_TWO_APP, 4);
        }
//        9 = Build.VERSION_CODES.GINGERBREAD
        if (Build.VERSION.SDK_INT < 9){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
//        SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6
            setRequestedOrientation(6);
        }

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    public SharedPreferences.Editor getEditor(){
        return settings.edit();
    }

    public SharedPreferences getSettings(){
        if (settings==null) {
            settings = getSharedPreferences(TWO_X_TWO_APP, 4);
        }
        return settings;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!getClass().getName().equals(DispatchActivity.class.getName())) {
            SharedPreferences.Editor editor = getEditor();
            editor.putString(LAST_ACTIVITY, getClass().getName());
            editor.commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(getClass().getName().equals(TwoXTwoIs4Activity.class.getName())){
                finish();
                return true;
            }
            Intent i = new Intent(SharedActivity.this, TwoXTwoIs4Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
