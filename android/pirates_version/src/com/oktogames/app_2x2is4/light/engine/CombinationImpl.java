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
 * Combinations class realisation.
 */
public class CombinationImpl {
    OperationType operationType;
    int firstOperand;
    int secondOperand;
    int result;

    public CombinationImpl(OperationType opType, int first, int second, int res) {
        operationType = opType;
        firstOperand = first;
        secondOperand = second;
        result = res;
    }

    private boolean isAlreadyInAnswers(int answer, int[] answers) {
        for (int currentAnswer : answers)
            if (currentAnswer == answer) return true;
        return false;
    }

    public int[] generateAnswersVariants() {
        int numOfAnswers = (result < 10 ? 3 : 5);
        int rightAnswerIndex = MultiplicationTableEngineImpl.generateNumber() % numOfAnswers;
        assert (rightAnswerIndex >= 0 && rightAnswerIndex < numOfAnswers);
        int[] answers = new int[numOfAnswers];
        int lessAnswers = 0, largerAnswers = 0;
        for (int i = 0; i < numOfAnswers; ++i) {
            if (i == rightAnswerIndex) {
                answers[i] = result;
                continue;
            }
            int maxAttempts = 100;
            do {
                int delta = MultiplicationTableEngineImpl.generateNumber() % (result / 2) + 1;
                assert (delta > 0 && delta <= result / 2);
                if (lessAnswers > largerAnswers) {
                    if (!isAlreadyInAnswers(result + delta, answers)) {
                        answers[i] = result + delta;
                        largerAnswers++;
                        break;
                    }
                } else {
                    if (!isAlreadyInAnswers(result - delta, answers)) {
                        answers[i] = result - delta;
                        lessAnswers++;
                        break;
                    }
                }
            } while (0 != --maxAttempts);
            assert (0 != maxAttempts);
        }
        return answers;
    }

    public boolean isNull() {
        return (OperationType.UNKNOWN == operationType && 0 == firstOperand && 0 == secondOperand && 0 == result);
    }

    public static CombinationImpl nullCombination()
    {
        return new CombinationImpl(OperationType.UNKNOWN, 0, 0, 0);
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getFirstOperand() {
        return firstOperand;
    }

    public int getSecondOperand() {
        return secondOperand;
    }

    public int getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CombinationImpl)) return false;

        CombinationImpl that = (CombinationImpl) o;

        if (firstOperand != that.firstOperand) return false;
        if (result != that.result) return false;
        if (secondOperand != that.secondOperand) return false;
        if (operationType != that.operationType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = operationType != null ? operationType.hashCode() : 0;
        result1 = 31 * result1 + firstOperand;
        result1 = 31 * result1 + secondOperand;
        result1 = 31 * result1 + result;
        return result1;
    }
}
