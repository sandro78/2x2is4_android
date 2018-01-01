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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Old purpose JNI stub for MultiplicationTableEngineImpl class C++.
 * Load libraries code:
 * <code>static {
 * System.loadLibrary("stlport");
 * System.loadLibrary("multitableengine");
 * System.loadLibrary("multitableengine-jni");
 * }</code>
 * <p/>
 * New appointment java realisation game engine.
 */
public class MultiplicationTableEngineImpl {
    public final static int MAX_TIMES_TABLE_NUMBER = 12;
    public final static int MAX_HISTORY_SIZE = 10;
    public final static int MAX_NUMBER_IN_TIMES_TABLE = 12;
    public final static int MAX_OPENED_TIMES_TABLE_NUMBER = 12;

    private OperationType operationType;
    private int number;
    private GameLevel gameLevel;
    private static Random random = new Random();
    private ArrayList<CombinationImpl> combinationImplsList = new ArrayList<CombinationImpl>();
    private int lastTimesTableNumber;


    public MultiplicationTableEngineImpl(int number, OperationType type, GameLevel lavel, int lastTimesTableNumber) {
        this.operationType = type;
        this.gameLevel = lavel;
        this.number = number;
        this.lastTimesTableNumber = lastTimesTableNumber;

        assert (number >= 2 && number <= MAX_OPENED_TIMES_TABLE_NUMBER);
        fillCombinationsList();
    }

    public MultiplicationTableEngineImpl(int number, OperationType type, GameLevel lavel) {
        this(number, type, lavel, MAX_NUMBER_IN_TIMES_TABLE);
    }

    public MultiplicationTableEngineImpl(){
        number = 2;
        operationType = OperationType.UNKNOWN;
        gameLevel = GameLevel.EASY;
        lastTimesTableNumber = 3;
    }

    private void fillCombinationsList() {
        switch (operationType)
        {
            case MULTIPLICATION:
                for (int i = 2; i <= number; ++i) {
                for (int j = 2; j <= lastTimesTableNumber; ++j) {
                    combinationImplsList.add(new CombinationImpl(operationType, i, j, i * j));
                }
            }
            break;

            case DIVISION:
                for (int i = 2; i <= number; ++i) {
                for (int j = 2; j <= lastTimesTableNumber; ++j) {
                    combinationImplsList.add(new CombinationImpl(operationType, i * j, i, j));
                }
            }
            break;

            default:
                assert(false);
        }
        Collections.shuffle(combinationImplsList);
    }



    public static int generateNumber() {
        return Math.abs(random.nextInt());
    }

    public CombinationImpl getCombination() {
        if (0 == combinationImplsList.size())
            return CombinationImpl.nullCombination();
        CombinationImpl combinationImpl = combinationImplsList.get(combinationImplsList.size()-1);
        combinationImplsList.remove(combinationImplsList.size()-1);
        return combinationImpl;
    }

    public int combinationsCount() {
        return combinationImplsList.size();
    }

    public void reinit() {
        combinationImplsList.clear();
        fillCombinationsList();
    }

    public int getShotsForWin() {
        return number - 1;
    }

    public int getHealthStatus(int shotsWereDone) {
        if (0 == shotsWereDone)
            return 0;
        int shotsForWin = getShotsForWin();
        if (shotsForWin == shotsWereDone)
            return -1;
        return shotsWereDone * ((MAX_TIMES_TABLE_NUMBER - 1) / shotsForWin);
    }

    public CanonReadiness getMyCanonReadiness(boolean needMakeShot) {
        if (needMakeShot) needMakeShot = false;
        int delta = combinationImplsList.size() % (lastTimesTableNumber - 1);
        int correctDelta = MAX_NUMBER_IN_TIMES_TABLE/lastTimesTableNumber;
        if (0 == delta) {
            if ((number - 1) * (lastTimesTableNumber - 1) == combinationImplsList.size())
                return new CanonReadiness(needMakeShot, 0);
            else {
                return new CanonReadiness(true, ((lastTimesTableNumber - 1)*correctDelta));
            }
        } else {
            return new CanonReadiness(needMakeShot, (lastTimesTableNumber - 1 - delta)*correctDelta);
        }
    }
    
    public class CanonReadiness {
        private boolean needMakeShot = false;
        private int readiness;

        public CanonReadiness(boolean needMakeShot, int readiness) {
            this.needMakeShot = needMakeShot;
            this.readiness = readiness;
        }

        public boolean isNeedMakeShot() {
            return needMakeShot;
        }

        public int getReadiness() {
            return readiness;
        }
    }

    private int getPirateCanonRatio() {
        switch (gameLevel) {
            case EASY:
                return 4;
            case MEDIUM:
                return 3;
            default:
                return 2;
        }
    }

    public CanonReadiness getPirateCanonReadiness(int timeCounter, boolean makeShotSign){
        if (makeShotSign) makeShotSign = false;
        if (0 == timeCounter/getPirateCanonRatio())
            return new CanonReadiness(makeShotSign, 0);
        int delta = timeCounter/getPirateCanonRatio()%(lastTimesTableNumber-1);
        int correctDelta = MAX_NUMBER_IN_TIMES_TABLE/lastTimesTableNumber;
        if (0 == delta) {
            if (0 == timeCounter%getPirateCanonRatio())
                makeShotSign = true;
            return new CanonReadiness(makeShotSign, 0 == timeCounter%getPirateCanonRatio() ?
                    (lastTimesTableNumber-1)*correctDelta : 0);
        }
        else
            return new CanonReadiness(makeShotSign, delta*correctDelta);
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }
}
