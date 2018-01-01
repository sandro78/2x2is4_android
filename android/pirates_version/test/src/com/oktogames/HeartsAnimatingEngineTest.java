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

package com.oktogames;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import com.oktogames.activity.GameActivity;
import com.oktogames.engine.GameLevel;
import com.oktogames.engine.HeartsAnimatingEngine;
import com.oktogames.engine.MultiplicationTableEngineImpl;
import com.oktogames.engine.OperationType;

/**
 * Tests for HeartsAnimatingEngine
 */
public class HeartsAnimatingEngineTest extends ActivityInstrumentationTestCase2<GameActivity> {

    public HeartsAnimatingEngineTest() {
        super("com.oktogames.activity", GameActivity.class);
    }

    public ImageView[] textViewArray = {new ImageView(null), new ImageView(null), new ImageView(null),
            new ImageView(null), new ImageView(null), new ImageView(null),
            new ImageView(null), new ImageView(null), new ImageView(null),
            new ImageView(null), new ImageView(null), new ImageView(null)};

    public void testHeartsAnimatingFor2() {
        MultiplicationTableEngineImpl engine =
                new MultiplicationTableEngineImpl(2, OperationType.MULTIPLICATION, GameLevel.EASY);

        HeartsAnimatingEngine heartsEngine = new HeartsAnimatingEngine(null, textViewArray);
        heartsEngine.chargeEngine(engine);



    }
}
