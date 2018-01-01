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

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.oktogames.app_2x2is4.light.R;
import com.oktogames.app_2x2is4.light.activity.SharedActivity;

/**
 * Game sound player.
 */
public class MediaPlayerFacade {
    private static MediaPlayer playBackground;
    private static MediaPlayer playCanonShot;
    private static MediaPlayer playClick;
    private static MediaPlayer playBedKey;
    private static MediaPlayer playPirateLoose;
    private static MediaPlayer playPirateWon;
    private static MediaPlayer playRightKey;

    private MediaPlayerFacade() {
    }

    public static void playBackground(SharedActivity context, boolean isLoop) {
        if (playBackground == null) {
            playBackground = MediaPlayer.create(context, R.raw.background);
            playBackground.setLooping(isLoop);
        }
        utilWork(playBackground, context);
    }

    public static void stopPlayBackground() {
        if (playBackground != null) {
            playBackground.release();
            playBackground = null;
        }
    }

    public static void playCanonShot(SharedActivity context) {
        if (playCanonShot == null) {
            playCanonShot = MediaPlayer.create(context, R.raw.canon_shot);
        }
        utilWork(playCanonShot, context);
    }

    public static void playClick(SharedActivity context) {
        if (playClick == null) {
            playClick = MediaPlayer.create(context, R.raw.click);
        }
        utilWork(playClick, context);
    }

    public static void playBedKey(SharedActivity context) {
        if (playBedKey == null) {
            playBedKey = MediaPlayer.create(context, R.raw.not_correct_key);
        }
        utilWork(playBedKey, context);
    }

    public static void playPirateLoose(SharedActivity context) {
        if (playPirateLoose == null) {
            playPirateLoose = MediaPlayer.create(context, R.raw.pirate_loose);
        }
        utilWork(playPirateLoose, context);
    }

    public static void playPirateWon(SharedActivity context) {
        if (playPirateWon == null) {
            playPirateWon = MediaPlayer.create(context, R.raw.pirate_won);
        }
        utilWork(playPirateWon, context);
    }

    public static void playRightKey(SharedActivity context) {
        if (playRightKey == null) {
            playRightKey = MediaPlayer.create(context, R.raw.right_key);
        }
        utilWork(playRightKey, context);
    }


    public static void destroy() {
        MediaPlayerFacade.stopPlayBackground();
        if (playCanonShot != null) {
            playCanonShot.release();
            playCanonShot = null;
        }
        if (playClick != null) {
            playClick.release();
            playClick = null;
        }
        if (playBedKey != null) {
            playBedKey.release();
            playBedKey = null;
        }
        if (playPirateLoose != null) {
            playPirateLoose.release();
            playPirateLoose = null;
        }
        if (playPirateWon != null) {
            playPirateWon.release();
            playPirateWon = null;
        }
        if (playRightKey != null) {
            playRightKey.release();
            playRightKey = null;
        }
    }

    private static void utilWork(final MediaPlayer player, Context context) {
        AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//        int vol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (player != null) {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.start();
        }
    }
}

