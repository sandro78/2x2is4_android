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

import android.util.Log;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * History Result Bean
 */
public class HistoryResult {
    private static HistoryResult ourInstance = new HistoryResult();
    private static Set<HistoryEntry> historySet = Collections.synchronizedSet(
            new TreeSet<HistoryEntry>(new HistoryEntryComparator()));
    public final static String SPR = "/";

    public static HistoryResult getInstance() {
        return ourInstance;
    }

    private HistoryResult() {
    }

    public Set<HistoryEntry> getHistory() {
        return historySet;
    }

    public void setHistory(Set<HistoryEntry> set) {
        historySet = set;
    }

    public void stringToMap(String stringHistory) {
        if (stringHistory != null && stringHistory.length() > 0) {
            String[] records = stringHistory.split(SPR);
            if (records.length >= 1) {
                for (String record : records) {
                    String[] fields = record.split(";");
                    if (fields.length >= 1) {
                        historySet.add(new HistoryEntry(fields[0], fields[1], fields[2],
                                fields[3], fields[4], fields[5], fields[6], fields[7]));
                    }
                }
            }
        }
        Log.d("HISTORYRESULT", " stringToMap = " + historySet);
    }

    public String historyToString() {
        String result = "";
        for (HistoryEntry entryId : historySet) {
            result = result + historyToString(entryId);
        }
        Log.d("HISTORYRESULT", " historyToString = " + result);
        return result;
    }

    public String historyToString(HistoryEntry history) {
        return history.toString() + SPR;
    }

    public void addResult(HistoryEntry history) {
        historySet.add(history);
        if (historySet.size()>10) {
            HistoryEntry entryForDelete = null;
            for (HistoryEntry entry : historySet) {
                entryForDelete = entry;
            }
            historySet.remove(entryForDelete);
        }

    }

    public Set<HistoryEntry> removeHistory(int recordId) {
        int i = 0;
        HistoryEntry entryForDelete = null;
        for (HistoryEntry entry : historySet) {
            if (i == recordId) {
                entryForDelete = entry;
                break;
            }
            i++;
        }
        historySet.remove(entryForDelete);
        return historySet;
    }

    public static void main(String[] args) {
        String str = "1329140616754;0;16;2;0;0;0;13/1329140837917;0;35;2;0;0;0;12/" +
                "1329140837917;0;35;2;0;0;0;11/1329140837917;0;35;2;0;0;0;10/";
        HistoryResult.getInstance().stringToMap(str);
        HistoryResult.getInstance().removeHistory(2);
        System.out.println(HistoryResult.getInstance().historyToString());
        System.out.println(historySet.toString());
    }
}
