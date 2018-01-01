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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.oktogames.app_2x2is4.light.R;

import java.util.Date;

/**
 */
public class HistoryListAdapter extends BaseAdapter {
    private Context context = null;
    private static Drawable rogerLoose;
    private static Drawable rogerWon;
    private static Drawable help2;
    private static Drawable stopWatch;
    private static Drawable deleteImage;
    private static Drawable deleteImagePressed;

    public HistoryListAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return HistoryResult.getInstance().getHistory().size();
    }

    public Object getItem(int position) {
        int i = 0;
        for (HistoryEntry entry: HistoryResult.getInstance().getHistory()){
            if(i==position)return entry;
            i++;
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    private View newView(Context context, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.history_item, parent, false);
    }

    private void bindView(final int position, View view) {
        HistoryEntry historyEntry = null;
        int i=0;
        for(HistoryEntry entry: HistoryResult.getInstance().getHistory()){
            if(i==position)historyEntry = entry;
            i++;
        }

        if (historyEntry != null) {
            TextView labelView = (TextView) view.findViewById(R.id.history_timer);
            TextView dateView = (TextView) view.findViewById(R.id.history_game_time);
            dateView.setText(new Date(Long.parseLong(historyEntry.getDate())).toString());
            if(stopWatch==null){
                stopWatch = DrawableUtils.getDrawableFromAssets(context, "img/stopwatch.png");
            }

            ((ImageView)view.findViewById(R.id.history_stopwatch)).setImageDrawable(stopWatch);

            labelView.setText(DrawableUtils.getTimerString(Integer.parseInt(historyEntry.getResult())));
            Button deleteButton = (Button) view.findViewById(R.id.history_delete);
            if(deleteImage==null){
                deleteImage = DrawableUtils.getDrawableFromAssets(context, "img/delete.png");
            }
            if(deleteImagePressed==null){
                deleteImagePressed = DrawableUtils.getDrawableFromAssets(context, "img/delete_pressed.png");
            }
            deleteButton.setBackgroundDrawable(deleteImage);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.history);
                    builder.setMessage(R.string.are_you_sure);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HistoryResult.getInstance().removeHistory(position);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });
            deleteButton.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            v.setBackgroundDrawable(deleteImagePressed);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.setBackgroundDrawable(deleteImage);
                            break;

                    }
                    return false;
                }
            });

            ImageView operation = (ImageView) view.findViewById(R.id.history_multiplication);
            if (historyEntry.getOperationType().equals("0")) {
                operation.setImageResource(R.drawable.multiplication);
            } else {
                operation.setImageResource(R.drawable.division);
            }
            ImageView level = (ImageView) view.findViewById(R.id.history_level);
            if (historyEntry.getGameLvel().equals("0")) {
                level.setImageResource(R.drawable.easy_level);
            } else if (historyEntry.getGameLvel().equals("1")) {
                level.setImageResource(R.drawable.medium_level);
            } else {
                level.setImageResource(R.drawable.hard_level);
            }
            TextView historyParams = (TextView) view.findViewById(R.id.history_params);
            historyParams.setText(historyEntry.getNumber() + "x" +
                    (Integer.parseInt(historyEntry.getMaxNumberInTimesTable())));

            TextView helps = (TextView) view.findViewById(R.id.history_helps);
            helps.setText(historyEntry.getHelps());

            if(help2==null){
                help2 = DrawableUtils.getDrawableFromAssets(context, "img/help2.png");
            }
            ImageView help2Img = (ImageView)view.findViewById(R.id.history_help2);
            help2Img.setImageDrawable(help2);


            ImageView cup = (ImageView) view.findViewById(R.id.history_cup);
            if (historyEntry.getWonSign().equals("0")) {
                if(rogerLoose==null){
                    rogerLoose = DrawableUtils.getDrawableFromAssets(context, "img/cup.png");

                }
                cup.setImageDrawable(rogerLoose);
            } else {
                if(rogerWon==null){
                    rogerWon = DrawableUtils.getDrawableFromAssets(context, "img/roger_won.png");
                }
                cup.setImageDrawable(rogerWon);
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = newView(context, parent);
        }
        bindView(position, view);
        return view;
    }
}

