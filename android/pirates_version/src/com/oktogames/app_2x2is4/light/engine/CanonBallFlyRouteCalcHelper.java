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

import android.graphics.Point;

/**
 */
public class CanonBallFlyRouteCalcHelper {
    private Point _currentPoint;
    private Point _finalPoint;
    private int _availableStepsOfAnimation, _canonHeight, _canonBallSize;
    private int AMOUNT_OF_ANIMATIONS_STEPS = 5;
    private float ANIMATION_DURATION = 0.05f;

    public CanonBallFlyRouteCalcHelper() {
        _availableStepsOfAnimation = _canonHeight = _canonBallSize = 0;
    }

    public boolean calcRouteFrom(Point startPoint, Point endPoint, int size, int height) {
        if (0 != _availableStepsOfAnimation)
            return false;

        _currentPoint = startPoint;
        _finalPoint = endPoint;
        _availableStepsOfAnimation = AMOUNT_OF_ANIMATIONS_STEPS;
        _canonBallSize = size;
        _canonHeight = height;
        return true;

    }

    public boolean nextAnimation(){
        if (0 == _availableStepsOfAnimation)
            return false;

        if (1 == _availableStepsOfAnimation)
            _currentPoint = _finalPoint;
        else {
            int deltaX = (_finalPoint.x - _currentPoint.x)/_availableStepsOfAnimation;
            int deltaY = (_finalPoint.y - _currentPoint.y)/_availableStepsOfAnimation;
            _currentPoint.x += deltaX;
            _currentPoint.y += deltaY;
        }
        _availableStepsOfAnimation--;
        return true;

    }

    public Point getTargetPoint(){
        return _currentPoint;
    }

    public int frameShrinkSize(){
        return (_canonBallSize*2)/(AMOUNT_OF_ANIMATIONS_STEPS-_availableStepsOfAnimation+1);
    }

    public float animationDuration(){
        return ANIMATION_DURATION;
    }

}
