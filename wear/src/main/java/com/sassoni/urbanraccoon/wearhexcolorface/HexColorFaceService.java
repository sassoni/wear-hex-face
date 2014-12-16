package com.sassoni.urbanraccoon.wearhexcolorface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.TextPaint;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class HexColorFaceService extends CanvasWatchFaceService {

    private static final String TAG = HexColorFaceService.class.getSimpleName();
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    public class Engine extends CanvasWatchFaceService.Engine {
        static final int MSG_UPDATE_TIME = 0;
        static final int BACKGROUND_COLOR = Color.BLACK;
        static final int FOREGROUND_COLOR = Color.WHITE;
        static final int TEXT_SIZE = 50;

        final TextPaint textPaint = new TextPaint();
        Time time;

        final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Log.i(TAG, "updating time");
                invalidate();
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(HexColorFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            textPaint.setColor(FOREGROUND_COLOR);
            textPaint.setTextSize(TEXT_SIZE);

            time = new Time();
            updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            Log.i(TAG, "onDraw");
            canvas.drawColor(BACKGROUND_COLOR);
            time.setToNow();

            String format = "%1$02d";
            String hour = String.format(format, time.hour);
            String minutes = String.format(format, time.minute);
            String seconds = String.format(format, time.second);

            Log.i(TAG, "Color: " + "#"+hour+minutes+seconds);
            int c = Color.parseColor("#"+hour+minutes+seconds);
            textPaint.setColor(c);

            canvas.drawText( hour + ":" + minutes + ":" + seconds, 10, 100, textPaint);
        }

        @Override
        public void onTimeTick() {
            Log.i(TAG, "onTimeTick");
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            Log.i(TAG, "onAmbientModeChanged: " + inAmbientMode);
            if (isInAmbientMode()) {
//                boolean antiAlias = !inAmbientMode;
//                mHourPaint.setAntiAlias(antiAlias);
//                mMinutePaint.setAntiAlias(antiAlias);
//                mSecondPaint.setAntiAlias(antiAlias);
//                mTickPaint.setAntiAlias(antiAlias);
            }
            invalidate();
            updateTimer();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.i(TAG, "onVisibilityChanged");
            super.onVisibilityChanged(visible);
            if (visible) {
//                registerReceiver();
                time.clear(TimeZone.getDefault().getID());
                time.setToNow();
            } //else {
            // unregisterReceiver();
            //}
            updateTimer();
        }

        private void updateTimer() {
            Log.i(TAG, "updateTimer");
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

    }

}
