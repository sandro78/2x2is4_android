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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.oktogames.app_2x2is4.light.activity.SharedActivity;

/**
 */
public class DispatchActivity extends SharedActivity {
    private String TAG = "SharedActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Class activityClass;

            try {
                activityClass = Class.forName(
                        getSettings().getString(LAST_ACTIVITY, TwoXTwoIs4Activity.class.getName()));
            } catch(ClassNotFoundException ex) {
                activityClass = TwoXTwoIs4Activity.class;
            }

            Intent i = new Intent(DispatchActivity.this, activityClass);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.nill, R.anim.nill);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Some start problems, try again.", e);
            finish();
        }
    }
}
