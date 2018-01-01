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
import junit.framework.Assert;
import com.oktogames.activity.LearnActivity;
import com.oktogames.engine.LearnViewConstructorImpl;

/**
 * LearnActivity test class.
 */
public class LearnActivityTest extends ActivityInstrumentationTestCase2<LearnActivity> {

    public LearnActivityTest() {
        super("com.oktogames.activity", LearnActivity.class);
    }

    public void testHeaderLabelRectSizeForiPhone() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String header = learn.headerLabelRect(480, 320);
        Assert.assertEquals("Rect doesn't match to expected", "187,26,106,53", header);
        Assert.assertEquals("Font size doesn't match to expected", 53, learn.fontSizeForHeight(53));
    }

    public void testHeaderLabelRectSizeForiPadLandscape() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String header = learn.headerLabelRect(1024, 768);
        Assert.assertEquals("Rect doesn't match to expected", "399,71,226,113", header);
        Assert.assertEquals("Font size doesn't match to expected", 113, learn.fontSizeForHeight(113));
    }

    public void testHeaderLabelRectSizeForiPadPortrait() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String header = learn.headerLabelRect(768, 1024);
        Assert.assertEquals("Rect doesn't match to expected", "271,114,226,113", header);
        Assert.assertEquals("Font size doesn't match to expected", 113, learn.fontSizeForHeight(113));
    }

    public void testExpressionsLabelsRectsForiPhone() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String rect = learn.expressionLabelRect(0, 480, 320);
        Assert.assertEquals("Rect doesn't match to expected", rect, "15,106,100,30");
        String rect2 = learn.answerLabelRectForExprRect(15, 106, 100, 30);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "115,106,50,30");

        rect = learn.expressionLabelRect(5, 480, 320);
        Assert.assertEquals("Rect doesn't match to expected", rect, "165,146,100,30");
        rect2 = learn.answerLabelRectForExprRect(165, 146, 100, 30);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "265,146,50,30");

        rect = learn.expressionLabelRect(11, 480, 320);
        Assert.assertEquals("Rect doesn't match to expected", rect, "315,226,100,30");
        rect2 = learn.answerLabelRectForExprRect(315, 226, 100, 30);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "415,226,50,30");

    }

    public void testExpressionsLabelsRectsForiPadLandscape() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String rect = learn.expressionLabelRect(0, 1024, 768);
        Assert.assertEquals("Rect doesn't match to expected", rect, "39,256,210,63");
        String rect2 = learn.answerLabelRectForExprRect(39, 256, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "249,256,105,63");

        rect = learn.expressionLabelRect(5, 1024, 768);
        Assert.assertEquals("Rect doesn't match to expected", rect, "354,340,210,63");
        rect2 = learn.answerLabelRectForExprRect(354, 340, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "564,340,105,63");

        rect = learn.expressionLabelRect(11, 1024, 768);
        Assert.assertEquals("Rect doesn't match to expected", rect, "669,508,210,63");
        rect2 = learn.answerLabelRectForExprRect(669, 508, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "879,508,105,63");

    }

    public void testExpressionsLabelsRectsForiPadPortrait() throws Throwable {
        LearnViewConstructorImpl learn = new LearnViewConstructorImpl();
        String rect = learn.expressionLabelRect(0, 768, 1024);
        Assert.assertEquals("Rect doesn't match to expected", rect, "69,341,210,63");
        String rect2 = learn.answerLabelRectForExprRect(69, 341, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "279,341,105,63");

        rect = learn.expressionLabelRect(5, 768, 1024);
        Assert.assertEquals("Rect doesn't match to expected", rect, "69,761,210,63");
        rect2 = learn.answerLabelRectForExprRect(69, 761, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "279,761,105,63");

        rect = learn.expressionLabelRect(11, 768, 1024);
        Assert.assertEquals("Rect doesn't match to expected", rect, "384,761,210,63");
        rect2 = learn.answerLabelRectForExprRect(384, 761, 210, 63);
        Assert.assertEquals("Rect doesn't match to expected", rect2, "594,761,105,63");

    }



}
