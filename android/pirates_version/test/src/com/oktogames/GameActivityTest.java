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
import com.oktogames.activity.GameActivity;
import com.oktogames.engine.CombinationImpl;
import com.oktogames.engine.GameLevel;
import com.oktogames.engine.MultiplicationTableEngineImpl;
import com.oktogames.engine.OperationType;

import java.util.*;

/**
 * The GameActivity class tests
 */
public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

    public GameActivityTest() {
        super("com.oktogames.activity", GameActivity.class);
    }


    public void testMultiplicationTableInitForMultiplication() throws Throwable {
        MultiplicationTableEngineImpl engine =
                new MultiplicationTableEngineImpl(2, OperationType.MULTIPLICATION, GameLevel.MEDIUM);
        Assert.assertEquals("Combinations count doesn't match expected", engine.combinationsCount(),
                MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1);
        Assert.assertEquals("Game level is not medium", engine.getGameLevel(), GameLevel.MEDIUM);
        for (int i = 0; i < MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1; ++i) {
            CombinationImpl combination = engine.getCombination();
            Assert.assertTrue("First operand doesn't equal 2", combination.getFirstOperand() == 2);
            Assert.assertTrue("Second operand doesn't belong to range [2..MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getSecondOperand() >= 2 &&
                            combination.getSecondOperand() <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);
            Assert.assertTrue("Result doesn't belong to range [4..2*MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getResult() >= 2 * 2 &&
                            combination.getResult() <= 2 * MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);

            Assert.assertTrue("Result doesn't multiple by 2", 0 == combination.getResult() % 2);

            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.MULTIPLICATION);
            Assert.assertEquals("Left combinations count do not match expectable value", engine.combinationsCount(),
                    (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1 - i - 1));

            System.out.println(String.format("%s*%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
        //Check no combinations anymore case
        Assert.assertEquals("", engine.combinationsCount(), 0);
        Assert.assertTrue("Some unexpectable object for no more combinations case", (CombinationImpl.nullCombination()).
                equals(engine.getCombination()));
    }


    public void testMultiplicationTableInitForDivision() throws Throwable {
        MultiplicationTableEngineImpl engine =
                new MultiplicationTableEngineImpl(2, OperationType.DIVISION, GameLevel.EASY);


        Assert.assertEquals("Combinations count doesn't match expected",
                engine.combinationsCount(), MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1);
        Assert.assertEquals("Game level is not easy", engine.getGameLevel(), GameLevel.EASY);

        for (int i = 0; i < MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1; ++i) {
            CombinationImpl combination = engine.getCombination();


            Assert.assertTrue("First operand doesn't belong range [4..2*MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getFirstOperand() >= 2 * 2 &&
                            combination.getFirstOperand() <= 2 * MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);


            Assert.assertTrue("First operand doesn't multiple by 2", 0 == combination.getFirstOperand() % 2);
            Assert.assertTrue("Second operand doesn't equal 2", combination.getSecondOperand() == 2);
            Assert.assertTrue("Result doesn't belong to range [2..MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getResult() >= 2 &&
                            combination.getResult() <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);
            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.DIVISION);
            Assert.assertEquals("Left combinations count do not match expectable value",
                    engine.combinationsCount(), MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1 - i - 1);


            System.out.println(String.format("%s:%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
        //Check no combinations anymore case
        Assert.assertEquals("", engine.combinationsCount(), 0);
        Assert.assertTrue("Some unexpectable object for no more combinations case", (CombinationImpl.nullCombination()).
                equals(engine.getCombination()));
    }


    public void testMultiplicationTableReinit() throws Throwable {
        MultiplicationTableEngineImpl engine =
                new MultiplicationTableEngineImpl(2, OperationType.DIVISION, GameLevel.HARD);


        Assert.assertEquals("Combination count is not MAX_NUMBER_IN_TIMES_TABLE-1",
                engine.combinationsCount(), MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1);
        Assert.assertEquals("Game level is not hard", engine.getGameLevel(), GameLevel.HARD);

        for (int i = 0; i < MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1; ++i)
            engine.getCombination();
        Assert.assertEquals("Combination count is not 0", engine.combinationsCount(), 0);
        engine.reinit();
        Assert.assertEquals("Combination count is not MAX_NUMBER_IN_TIMES_TABLE-1",
                engine.combinationsCount(), MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1);
        for (int i = 0; i < MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1; ++i) {
            CombinationImpl combination = engine.getCombination();
            Assert.assertTrue("First operand doesn't belong range [4..2*MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getFirstOperand() >= 2 * 2 &&
                            combination.getFirstOperand() <= 2 * MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);
            Assert.assertTrue("First operand doesn't multiple by 2", 0 == combination.getFirstOperand() % 2);
            Assert.assertTrue("Second operand doesn't equal 2", combination.getSecondOperand() == 2);
            Assert.assertTrue("Result doesn't belong to range [2..MAX_NUMBER_IN_TIMES_TABLE]",
                    combination.getResult() >= 2 &&
                            combination.getResult() <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE);
            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.DIVISION);
            Assert.assertEquals("Left combinations count do not match expectable value",
                    engine.combinationsCount(), (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1 - i - 1));
            System.out.println(String.format("%s:%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
    }


    public void testGenerateAnswersVariants() throws Throwable {
        CombinationImpl combination = new CombinationImpl(OperationType.MULTIPLICATION, 7, 8, 56);
        int[] answers = combination.generateAnswersVariants();
        Assert.assertEquals("Answers number doesn't match to expectable", 5, answers.length);
        int foundEqualResults = 0;
        int foundLessResults = 0;
        int foundLargerResults = 0;
        for (int answer : answers) {
            System.out.println(String.format("answer=%s", new Object[]{answer}));
            if (56 == answer) {
                foundEqualResults++;
                continue;
            }
            if (56 > answer) {
                foundLessResults++;
                continue;
            }
            if (56 < answer) {
                foundLargerResults++;
                continue;
            }
            Assert.fail("Shouldn't be here!");
        }
        Assert.assertEquals("Equal results do not match", 1, foundEqualResults);
        Assert.assertEquals("Less results do not match", 2, foundLessResults);
        Assert.assertEquals("Larger results do not match", 2, foundLargerResults);
    }


    public void testGenerateAnswersVariants2() throws Throwable {
        CombinationImpl combination = new CombinationImpl(OperationType.DIVISION, 4, 2, 2);
        int[] answers = combination.generateAnswersVariants();
        Assert.assertEquals("Answers number doesn't match to expectable", 3, answers.length);
        int foundEqualResults = 0;
        int foundLessResults = 0;
        int foundLargerResults = 0;
        for (int i = 0; i < answers.length; ++i) {
            int answer = answers[i];
            System.out.println(String.format("answer=%s", new Object[]{answer}));
            if (2 == answer) {
                foundEqualResults++;
                continue;
            }
            if (2 > answer) {
                foundLessResults++;
                Assert.assertEquals("Less answer do not match expectable", 1, answer);
                continue;
            }
            if (2 < answer) {
                foundLargerResults++;
                Assert.assertEquals("Larger answer do not match expectable", 3, answer);
                continue;
            }
            Assert.fail("Shouldn't be here!");
        }
        Assert.assertEquals("Equal results do not match", 1, foundEqualResults);
        Assert.assertEquals("Less results do not match", 1, foundLessResults);
        Assert.assertEquals("Larger results do not match", 1, foundLargerResults);
    }


    public void testCheckVariantsUnity() throws Throwable {
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(
                MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER, OperationType.MULTIPLICATION,
                GameLevel.HARD);
        for (int i = 0; i < (MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER - 1) *
                (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1); ++i) {
            CombinationImpl combination = engine.getCombination();
            int[] answers = combination.generateAnswersVariants();
            Integer[] newArray = new Integer[answers.length];
            int k = 0;
            for (int value : answers) {
                newArray[k++] = Integer.valueOf(value);
            }
            Assert.assertFalse("Duplicate answer in variants", findDup(Arrays.asList(newArray)));
        }
    }

    public static boolean findDup(List alKey) {

        boolean boolDup = false;
        Map repeated = new HashMap();
        Iterator iterator = alKey.iterator();
        while (iterator.hasNext()) {

            Integer value = (Integer) iterator.next();
            if (repeated.containsKey(value)) {
                boolDup = true;
            } else {
                repeated.put(value, "val");
            }
        }
        return (boolDup);
    }


    public void testGetShotsForWinFromEngine() throws Throwable {
        for (int i = 2; i <= MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER; ++i) {
            MultiplicationTableEngineImpl engine =
                    new MultiplicationTableEngineImpl(i, OperationType.DIVISION, GameLevel.EASY);
            Assert.assertEquals("Num of shots for win do not match expectable", i - 1, engine.getShotsForWin());
        }
    }


    public void testGetMyCanonReadiness() throws Throwable {
        MultiplicationTableEngineImpl engine =
                new MultiplicationTableEngineImpl(2, OperationType.DIVISION, GameLevel.MEDIUM);
        boolean needMakeShot = false;

        MultiplicationTableEngineImpl.CanonReadiness myCanonReadiness = engine.getMyCanonReadiness(needMakeShot);
        Assert.assertEquals("My canon readiness do not match expectable", 0, myCanonReadiness.getReadiness());
        needMakeShot = myCanonReadiness.isNeedMakeShot();
        Assert.assertFalse("Make shot sign does not match", needMakeShot);
        for (int i = 1; i <= MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1; ++i) {
            engine.getCombination();
            myCanonReadiness = engine.getMyCanonReadiness(needMakeShot);
            int readiness = myCanonReadiness.getReadiness();
            needMakeShot = myCanonReadiness.isNeedMakeShot();
            Assert.assertEquals("My canon readiness do not match expectable", i, readiness);
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == readiness)
                Assert.assertTrue("Make shot sign does not match", needMakeShot);
            else
                Assert.assertFalse("Make shot sign does not match", needMakeShot);
        }
    }


    public void testGetPirateCanonReadinessForEasyLavel() throws Throwable {
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.EASY);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness pirateCanonReadiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable", 0, pirateCanonReadiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) * 2 * 4; ++i) {
            pirateCanonReadiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 4 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) ?
                            (0 == i / 4 ? 0 : (0 == i % 4 ? (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) : 0)) :
                            i / 4 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1)), pirateCanonReadiness.getReadiness());
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == pirateCanonReadiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }


    public void testGetPirateCanonReadinessForMediumLevel() throws Throwable {
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2,
                OperationType.DIVISION, GameLevel.MEDIUM);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness pirateCanonReadiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, pirateCanonReadiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) * 2 * 3; ++i) {
            pirateCanonReadiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 3 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) ?
                            (0 == i / 3 ? 0 : (0 == i % 3 ? (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) : 0)) :
                            i / 3 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1)), pirateCanonReadiness.getReadiness());
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == pirateCanonReadiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }


    public void testGetPirateCanonReadinessForHardLevel() throws Throwable {
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.HARD);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness pirateCanonReadiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, pirateCanonReadiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) * 2 * 2; ++i) {
            pirateCanonReadiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = pirateCanonReadiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 2 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) ?
                            (0 == i / 2 ? 0 : (0 == i % 2 ? (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) : 0)) :
                            i / 2 % (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1)),
                    pirateCanonReadiness.getReadiness());
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE - 1) == pirateCanonReadiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }


    public void testGetHealthStatusFromEngineFor5Level() {
        if (5 > MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER)//Skip test due it doesn't make sense in this mode
            return;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(5, OperationType.DIVISION,
                GameLevel.EASY);
        int shots = engine.getShotsForWin();
        Assert.assertEquals("Shots for win do not match expectable", 4, shots);
        for (int j = 0; j <= shots; ++j) {
            if (0 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 0, engine.getHealthStatus(j));
            else if (j == shots)
                Assert.assertEquals("Health status doesn't match to expectable", -1, engine.getHealthStatus(j));
            else if (1 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 2, engine.getHealthStatus(j));
            else if (2 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 4, engine.getHealthStatus(j));
            else if (3 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 6, engine.getHealthStatus(j));
        }
    }


    public void testGetHealthStatusFromEngineForAllLevels() {
        for (int i = 2; i <= MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER; ++i) {
            MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(i, OperationType.DIVISION,
                    GameLevel.EASY);
            int shots = engine.getShotsForWin();
            for (int j = 0; j <= shots; ++j) {
                if (0 == j)
                    Assert.assertEquals("Health status doesn't match to expectable", 0, engine.getHealthStatus(j));
                else if (j == shots)
                    Assert.assertEquals("Health status doesn't match to expectable", -1, engine.getHealthStatus(j));
                else
                    Assert.assertEquals("Health status doesn't match to expectable",
                            (j * ((MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER - 1) / shots)),
                            engine.getHealthStatus(j));
            }
        }
    }

    public void testMultiplicationTableInitForMultiplicationMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.MULTIPLICATION,
                GameLevel.MEDIUM, maxTimesTableNumber);
        Assert.assertEquals("Combinations count doesn't match expected", engine.combinationsCount(), maxTimesTableNumber - 1);
        Assert.assertEquals("Game level is not medium", engine.getGameLevel(), GameLevel.MEDIUM);
        for (int i = 0; i < maxTimesTableNumber - 1; ++i) {
            CombinationImpl combination = engine.getCombination();
            Assert.assertTrue("First operand doesn't equal 2", combination.getFirstOperand() == 2);
            Assert.assertTrue("Second operand doesn't belong to range [2..5]", combination.getSecondOperand() >= 2 && combination.getSecondOperand() <= maxTimesTableNumber);
            Assert.assertTrue("Result doesn't belong to range [4..2*5]", combination.getResult() >= 2 * 2 && combination.getResult() <= 2 * maxTimesTableNumber);
            Assert.assertTrue("Result doesn't multiple by 2", 0 == combination.getResult() % 2);
            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.MULTIPLICATION);
            Assert.assertEquals("Left combinations count do not match expectable value", engine.combinationsCount(), maxTimesTableNumber - 1 - i - 1);
            System.out.println(String.format("%s*%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
        //Check no combinations anymore case
        Assert.assertEquals(engine.combinationsCount(), 0);
        Assert.assertTrue("Some unexpectable object for no more combinations case",
                engine.getCombination().equals(CombinationImpl.nullCombination()));
    }

    public void testMultiplicationTableInitForDivisionMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.EASY, maxTimesTableNumber);
        Assert.assertEquals("Combinations count doesn't match expected", engine.combinationsCount(), maxTimesTableNumber - 1);
        Assert.assertEquals("Game level is not easy", engine.getGameLevel(), GameLevel.EASY);
        for (int i = 0; i < maxTimesTableNumber - 1; ++i) {
            CombinationImpl combination = engine.getCombination();
            Assert.assertTrue("First operand doesn't belong range [4..2*5]",
                    combination.getFirstOperand() >= 2 * 2 && combination.getFirstOperand() <= 2 * maxTimesTableNumber);
            Assert.assertTrue("First operand doesn't multiple by 2", 0 == combination.getFirstOperand() % 2);
            Assert.assertTrue("Second operand doesn't equal 2", combination.getSecondOperand() == 2);
            Assert.assertTrue("Result doesn't belong to range [2..5]",
                    combination.getResult() >= 2 && combination.getResult() <= maxTimesTableNumber);
            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.DIVISION);
            Assert.assertEquals("Left combinations count do not match expectable value",
                    engine.combinationsCount(), maxTimesTableNumber - 1 - i - 1);
            System.out.println(String.format("%s:%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
        Assert.assertEquals("", engine.combinationsCount(), 0);
        Assert.assertTrue("Some unexpectable object for no more combinations case",
                engine.getCombination().equals(CombinationImpl.nullCombination()));
    }

    public void testMultiplicationTableReinitMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.HARD, maxTimesTableNumber);
        Assert.assertEquals("Combination count is not 4", engine.combinationsCount(), maxTimesTableNumber - 1);
        Assert.assertEquals("Game level is not hard", engine.getGameLevel(), GameLevel.HARD);
        for (int i = 0; i < maxTimesTableNumber - 1; ++i)
            engine.getCombination();
        Assert.assertEquals("Combination count is not 0", engine.combinationsCount(), 0);
        engine.reinit();
        Assert.assertEquals("Combination count is not MAX_NUMBER_IN_TIMES_TABLE-1",
                engine.combinationsCount(), maxTimesTableNumber - 1);
        for (int i = 0; i < maxTimesTableNumber - 1; ++i) {
            CombinationImpl combination = engine.getCombination();
            Assert.assertTrue("First operand doesn't belong range [4..2*5]",
                    combination.getFirstOperand() >= 2 * 2 && combination.getFirstOperand() <= 2 * maxTimesTableNumber);
            Assert.assertTrue("First operand doesn't multiple by 2", 0 == combination.getFirstOperand() % 2);
            Assert.assertTrue("Second operand doesn't equal 2", combination.getSecondOperand() == 2);
            Assert.assertTrue("Result doesn't belong to range [2..5]",
                    combination.getResult() >= 2 && combination.getResult() <= maxTimesTableNumber);
            Assert.assertEquals("Operation type is incorrect", combination.getOperationType(), OperationType.DIVISION);
            Assert.assertEquals("Left combinations count do not match expectable value", engine.combinationsCount(), maxTimesTableNumber - 1 - i - 1);
            System.out.println(String.format("%s:%s=%s",
                    new Object[]{combination.getFirstOperand(), combination.getSecondOperand(), combination.getResult()}));
        }
    }

    public void testGetMyCanonReadinessMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.MEDIUM, maxTimesTableNumber);
        boolean needMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getMyCanonReadiness(needMakeShot);
        Assert.assertEquals("My canon readiness do not match expectable", 0, readiness.getReadiness());
        Assert.assertFalse("Make shot sign does not match", needMakeShot);
        for (int i = 1; i <= maxTimesTableNumber - 1; ++i) {
            engine.getCombination();
            readiness = engine.getMyCanonReadiness(needMakeShot);
            needMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("My canon readiness do not match expectable",
                    i * (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber), readiness.getReadiness());
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber) * (maxTimesTableNumber - 1) ==
                    readiness.getReadiness())
                Assert.assertTrue("Make shot sign does not match", needMakeShot);
            else
                Assert.assertFalse("Make shot sign does not match", needMakeShot);
        }
    }

    public void testGetPirateCanonReadinessForEasyLevelMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.EASY, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readinessPirates = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = readinessPirates.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readinessPirates.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 4; ++i) {
            MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 4 % (maxTimesTableNumber - 1) ? (0 == i / 4 ? 0 : (0 == i % 4 ?
                            (maxTimesTableNumber - 1)*d : 0)) : i / 4 % (maxTimesTableNumber - 1) * d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetPirateCanonReadinessForMediumLevelMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.MEDIUM, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 3; ++i) {
            readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 3 % (maxTimesTableNumber - 1) ? (0 == i / 3 ? 0 : (0 == i % 3 ?
                            (maxTimesTableNumber - 1)*d : 0)) : i / 3 % (maxTimesTableNumber - 1)*d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetPirateCanonReadinessForHardLevelMaxTimesTableNumber5() {
        int maxTimesTableNumber = 5;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.HARD, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = readiness.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 2; ++i) {
            readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 2 % (maxTimesTableNumber - 1) ? (0 == i / 2 ? 0 : (0 == i % 2 ?
                            (maxTimesTableNumber - 1)*d : 0)) :
                            i / 2 % (maxTimesTableNumber - 1)*d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetMyCanonReadinessMaxTimesTableNumber6() {
        int maxTimesTableNumber = 6;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.MEDIUM, maxTimesTableNumber);
        boolean needMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getMyCanonReadiness(needMakeShot);
        Assert.assertEquals("My canon readiness do not match expectable", 0, readiness.getReadiness());
        Assert.assertFalse("Make shot sign does not match", needMakeShot);
        for (int i = 1; i <= maxTimesTableNumber - 1; ++i) {
            engine.getCombination();
            readiness = engine.getMyCanonReadiness(needMakeShot);
            needMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("My canon readiness do not match expectable",
                    i * (MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber), readiness.getReadiness());
            if ((MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber) * (maxTimesTableNumber - 1) ==
                    readiness.getReadiness())
                Assert.assertTrue("Make shot sign does not match", needMakeShot);
            else
                Assert.assertFalse("Make shot sign does not match", needMakeShot);
        }
    }

    public void testGetPirateCanonReadinessForEasyLevelMaxTimesTableNumber6() {
        int maxTimesTableNumber = 6;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.EASY, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readinessPirates = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = readinessPirates.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readinessPirates.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 4; ++i) {
            MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 4 % (maxTimesTableNumber - 1) ? (0 == i / 4 ? 0 : (0 == i % 4 ?
                            (maxTimesTableNumber - 1)*d : 0)) : i / 4 % (maxTimesTableNumber - 1) * d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetPirateCanonReadinessForMediumLevelMaxTimesTableNumber6() {
        int maxTimesTableNumber = 6;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.MEDIUM, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 3; ++i) {
            readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 3 % (maxTimesTableNumber - 1) ? (0 == i / 3 ? 0 : (0 == i % 3 ?
                            (maxTimesTableNumber - 1)*d : 0)) : i / 3 % (maxTimesTableNumber - 1)*d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetPirateCanonReadinessForHardLevelMaxTimesTableNumber6() {
        int maxTimesTableNumber = 6;
        int d = MultiplicationTableEngineImpl.MAX_NUMBER_IN_TIMES_TABLE / maxTimesTableNumber;
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(2, OperationType.DIVISION,
                GameLevel.HARD, maxTimesTableNumber);
        boolean needToMakeShot = false;
        MultiplicationTableEngineImpl.CanonReadiness readiness = engine.getPirateCanonReadiness(0, needToMakeShot);
        needToMakeShot = readiness.isNeedMakeShot();
        Assert.assertEquals("Pirate canon readiness do not match expectable",
                0, readiness.getReadiness());
        Assert.assertFalse("Unexpected make shot sign", needToMakeShot);
        for (int i = 1; i <= (maxTimesTableNumber - 1) * 2 * 2; ++i) {
            readiness = engine.getPirateCanonReadiness(i, needToMakeShot);
            needToMakeShot = readiness.isNeedMakeShot();
            Assert.assertEquals("Pirate canon readiness do not match expectable",
                    (0 == i / 2 % (maxTimesTableNumber - 1) ? (0 == i / 2 ? 0 : (0 == i % 2 ?
                            (maxTimesTableNumber - 1)*d : 0)) :
                            i / 2 % (maxTimesTableNumber - 1)*d), readiness.getReadiness());
            if ((maxTimesTableNumber - 1)*d == readiness.getReadiness()) {
                Assert.assertTrue("Unexpected make shot sign", needToMakeShot);
            }
        }
    }

    public void testGetHealthStatusFromEngineFor5LevelMaxTimesTableNumber6() {
        MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(5,
                OperationType.DIVISION, GameLevel.EASY, 6);
        int shots = engine.getShotsForWin();
        Assert.assertEquals("Shots for win do not match expectable", 4, shots);
        for (int j = 0; j <= shots; ++j) {
            if (0 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 0, engine.getHealthStatus(j));
            else if (j == shots)
                Assert.assertEquals("Health status doesn't match to expectable", -1, engine.getHealthStatus(j));
            else if (1 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 2, engine.getHealthStatus(j));
            else if (2 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 4, engine.getHealthStatus(j));
            else if (3 == j)
                Assert.assertEquals("Health status doesn't match to expectable", 6, engine.getHealthStatus(j));
        }

    }

    public void testGetHealthStatusFromEngineForAllLevelsMaxTimesTableNumber6() {
        for (int i = 2; i <= MultiplicationTableEngineImpl.MAX_OPENED_TIMES_TABLE_NUMBER; ++i) {
            MultiplicationTableEngineImpl engine = new MultiplicationTableEngineImpl(i,
                    OperationType.DIVISION, GameLevel.EASY, 6);
            int shots = engine.getShotsForWin();
            for (int j = 0; j <= shots; ++j) {
                if (0 == j)
                    Assert.assertEquals("Health status doesn't match to expectable",
                            0, engine.getHealthStatus(j));
                else if (j == shots)
                    Assert.assertEquals("Health status doesn't match to expectable", -1, engine.getHealthStatus(j));
                else
                    Assert.assertEquals( "Health status doesn't match to expectable",
                            (j * ((MultiplicationTableEngineImpl.MAX_TIMES_TABLE_NUMBER - 1) / shots)),
                            engine.getHealthStatus(j));
            }
        }
    }


}
