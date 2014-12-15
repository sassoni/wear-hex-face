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

import java.util.concurrent.TimeUnit;

public class HexColorFaceService extends CanvasWatchFaceService {

    private static final String TAG = HexColorFaceService.class.getSimpleName();
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    public class Engine extends CanvasWatchFaceService.Engine {
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
                updateTimeHandler.sendEmptyMessageDelayed(123, delayMs);
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
            updateTimeHandler.sendEmptyMessage(123);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            Log.i(TAG, "onDraw");
            canvas.drawColor(BACKGROUND_COLOR);
            time.setToNow();
            canvas.drawText(Integer.toString(time.hour)+":"+Integer.toString(time.minute)+":"+Integer.toString(time.second), 10, 100, textPaint);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }
    }

}
