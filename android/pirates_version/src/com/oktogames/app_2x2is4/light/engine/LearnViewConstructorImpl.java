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

package com.oktogames.app_2x2is4.light.engine;

/**
 * JNI stub for learn view library.
 */
public class LearnViewConstructorImpl {

    public LearnViewConstructorImpl() {
    }

    public native String answerLabelRectForExprRect(int x, int y, int width, int height);

    public native String expressionLabelRect(int index, int width, int height);

    public native int fontSizeForHeight(int height);

    public native String headerLabelRect(int width, int height);

    public Dimension getHeaderLabelRect(int width, int height){
        String dim = headerLabelRect(width, height);
        return new Dimension(Integer.parseInt(dim.split(",")[2]), Integer.parseInt(dim.split(",")[3]));
    }

    public Dimension getExpressionLabelRect(int width, int height){
        String dim = expressionLabelRect(1, width, height);
        return new Dimension(Integer.parseInt(dim.split(",")[2]), Integer.parseInt(dim.split(",")[3]));
    }

    public Dimension getAnswerLabelRectForExprRect(int width, int height){
        String dim = answerLabelRectForExprRect(0, 0, width, height);
        return new Dimension(Integer.parseInt(dim.split(",")[2]), Integer.parseInt(dim.split(",")[3]));
    }

    public class Dimension{
        int width;
        int height;

        private Dimension(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
