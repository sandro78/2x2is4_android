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

package com.oktogames.app_2x2is4.light.utils;

/**
 * History Entry Bean
 */
public class HistoryEntry {
    private String date;
    private String operationType;
    private String result;
    private String number;
    private String helps;
    private String gameLvel;
    private String wonSign;
    private String maxNumberInTimesTable;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHelps() {
        return String.format("%02d", Integer.parseInt(helps));
    }

    public void setHelps(String helps) {
        this.helps = helps;
    }

    public String getGameLvel() {
        return gameLvel;
    }

    public void setGameLvel(String gameLvel) {
        this.gameLvel = gameLvel;
    }

    public String getWonSign() {
        return wonSign;
    }

    public void setWonSign(String wonSign) {
        this.wonSign = wonSign;
    }

    public String getMaxNumberInTimesTable() {
        return maxNumberInTimesTable;
    }

    public void setMaxNumberInTimesTable(String maxNumberInTimesTable) {
        this.maxNumberInTimesTable = maxNumberInTimesTable;
    }

    @Override
    public String toString() {
        return date + ';' + operationType + ';' +
                result + ';' +
                number + ';' +
                helps + ';' +
                gameLvel + ';' +
                wonSign + ';' +
                maxNumberInTimesTable;
    }

    public HistoryEntry(String date, String operationType, String result, String number, String helps,
                        String gameLvel, String wonSign, String maxNumberInTimesTable) {
        this.date = date;
        this.operationType = operationType;
        this.result = result;
        this.number = number;
        this.helps = helps;
        this.gameLvel = gameLvel;
        this.wonSign = wonSign;
        this.maxNumberInTimesTable = maxNumberInTimesTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryEntry)) return false;

        HistoryEntry that = (HistoryEntry) o;

        if (!date.equals(that.date)) return false;
        if (!gameLvel.equals(that.gameLvel)) return false;
        if (!helps.equals(that.helps)) return false;
        if (!maxNumberInTimesTable.equals(that.maxNumberInTimesTable)) return false;
        if (!number.equals(that.number)) return false;
        if (!operationType.equals(that.operationType)) return false;
        if (!result.equals(that.result)) return false;
        if (!wonSign.equals(that.wonSign)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = date.hashCode();
        result1 = 31 * result1 + operationType.hashCode();
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + number.hashCode();
        result1 = 31 * result1 + helps.hashCode();
        result1 = 31 * result1 + gameLvel.hashCode();
        result1 = 31 * result1 + wonSign.hashCode();
        result1 = 31 * result1 + maxNumberInTimesTable.hashCode();
        return result1;
    }
}
