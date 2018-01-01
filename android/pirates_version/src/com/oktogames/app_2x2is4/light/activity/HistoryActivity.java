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
import android.util.Log;
import android.widget.ListView;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.utils.DrawableUtils;
import com.oktogames.app_2x2is4.light.utils.HistoryListAdapter;
import com.oktogames.app_2x2is4.light.utils.HistoryResult;

/**
 * HistoryActivity class.
 */
public class HistoryActivity extends SharedActivity {
    private static final String TAG = "HistoryActivity";

    public void onCreate(Bundle savedInstanceState) {
        //load history from Preference Settings
        try {
            HistoryResult.getInstance().stringToMap(getSettings().getString(HISTORY, ""));
            super.onCreate(savedInstanceState);
            setContentView(R.layout.history);
            findViewById(R.id.history_layout).
                    setBackgroundDrawable(DrawableUtils.getDrawableFromAssets(this, DrawableUtils.LEARN_BG_LANDSCAPE));
            ListView listView = (ListView) findViewById(R.id.history_list);
            listView.setAdapter(new HistoryListAdapter(this));
        } catch (Exception e) {
            finish();
            Log.e(TAG, "Some history activity exception, try again", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getEditor();
        editor.putString(HISTORY, HistoryResult.getInstance().historyToString());
        editor.commit();
    }

}
