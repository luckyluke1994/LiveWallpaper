package com.example.maidaidien.matrixeffectcanvas.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.enrico.colorpicker.colorDialog;
import com.example.maidaidien.matrixeffectcanvas.R;

import java.util.Random;

/**
 * Created by mai.dai.dien on 21/02/2017.
 */

public class MatrixWall extends WallpaperService {
    private boolean mVisible; // Visible flag
    Canvas canvas;
    int drawSpeed = 10;
    Context context;

    // ======== MATRIX LIVE WALLPAPER VARS
    int background_color = Color.parseColor("#FF000000");
    int text_color = Color.parseColor("#FF8BFF4A");

    int width = 1000000; //default initial width
    int height = 100; //default initial height
    int fontSize = 15; //font size of the text which will fall
    int columnSize = width / fontSize; //column size ; no of digit required to fill the screen
    int parentWidth;
    String text = "MATRIXRAIN";  // Text which need to be drawn
    char[] textChar = text.toCharArray(); // split the character of the text
    int textLength = textChar.length;   //length of the length text
    Random rand = new Random(); //random generater

    int[] textPosition; // contain the position which will help to draw the text
    //======================

    @Override
    public Engine onCreateEngine() {
        context = this;

        //Initalise and read the preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        text = sharedPref.getString("matrix_scroll_text", "MATRIX");
        drawSpeed = Integer.parseInt(sharedPref.getString("matrix_falling_speed", "10"));
        fontSize = Integer.parseInt(sharedPref.getString("matrix_font_size", "15"));
        background_color = colorDialog.getPickerColor(getBaseContext(), 1);
        text_color = colorDialog.getPickerColor(getBaseContext(), 2);

        textChar = text.toCharArray(); // split the character of the text
        textLength = textChar.length;
        columnSize = width / fontSize;

        return new LiveWall(); // this calls contain the wallpaper code
    }

    /*
    * this class extends the engine for the live wallpaper
    * THis class implements all the draw calls required to draw the wallpaper
    * This call is to neseted inside the wallpaper service class to function properly
    * don't know why though :(
     */
    public class LiveWall extends Engine {
        // this is to handle the thread
        final Handler mHandler = new Handler();
        //the tread responsibe for drawing this thread get calls every time
        // drawspeed vars set the execution speed
        private final Runnable mDrawFrame = new Runnable() {
            @Override
            public void run() {
                //Matrix code to the color when changed
                // callback can also be used but I havent
                background_color = colorDialog.getPickerColor(getBaseContext(), 1);
                text_color =colorDialog.getPickerColor(getBaseContext(), 2);
                // This method get called each time to drwaw thw frame
                // Engine class does not provide any invlidate methods
                // as used in canvas
                // set your draw call here
                drawFrame();
            }
        };

        // Called when the surface is created
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            //update  the matrix variables
            width = getDesiredMinimumWidth();
            height = getDesiredMinimumHeight();
            columnSize = width/fontSize;
            //initalise the textposiotn to zero
            textPosition = new int[columnSize+1]; //add one more drop
            for(int x = 0; x < columnSize; x++) {
                textPosition[x] = 1;
            }
            // call the draw method
            // this is where you must call your draw code
            drawFrame();
        }

        // remove thread
        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawFrame);
        }

        //called when varaible changed
        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                text = sharedPref.getString("matrix_scroll_text", "MATRIX");
                drawSpeed = Integer.parseInt(sharedPref.getString("matrix_falling_speed","10"));
                fontSize = Integer.parseInt(sharedPref.getString("matrix_font_size","15"));
                background_color = colorDialog.getPickerColor(getBaseContext(), 1);
                text_color =colorDialog.getPickerColor(getBaseContext(), 2);



                textChar = text.toCharArray(); // split the character of the text
                textLength = textChar.length;
                columnSize = width/fontSize;
                // call the draw function
                drawFrame();
            } else {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                text = sharedPref.getString("matrix_scroll_text", "MATRIX");
                drawSpeed = Integer.parseInt(sharedPref.getString("matrix_falling_speed","10"));
                fontSize = Integer.parseInt(sharedPref.getString("matrix_font_size","15"));
                background_color = colorDialog.getPickerColor(getBaseContext(), 1);
                text_color =colorDialog.getPickerColor(getBaseContext(), 2);



                textChar = text.toCharArray(); // split the character of the text
                textLength = textChar.length;
                columnSize = width/fontSize;

                // this is necessay to remove the call back
                mHandler.removeCallbacks(mDrawFrame);
            }
        }

        //called when surface destroyed
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            //this is necessay to remove the call back
            mHandler.removeCallbacks(mDrawFrame);
        }

        // this function which contain the code to draw
        // this function contain the the main draw call
        // this function need to call every time the code is executed
        // the thread call this functioin with some delay "drawspeed"
        public void drawFrame() {
            //getting the surface holder
            final SurfaceHolder holder = getSurfaceHolder();

            canvas = null;
            try {
                canvas = holder.lockCanvas(); // get the canvas
                if (canvas != null) {
                    // draw something

                    canvasDraw();
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            // Reschedule the next redraw
            // this is the replacement for the invilidate funtion
            // every time call the drawFrame to draw the matrix
            mHandler.removeCallbacks(mDrawFrame);
            if (mVisible) {
                // set the execution delay
                mHandler.postDelayed(mDrawFrame, drawSpeed);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // update when surface changed
            // some matrix variable
            // though not needed
            Paint paint = new Paint();
            paint.setColor(background_color);
            paint.setAlpha(255); //set the alpha
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, width, height, paint);
        }
    }

    // draw the text on the bitmap
    void drawText() {
        // set up the paint
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setTextSize(fontSize);

        // loop and paint
        for (int index = 0; index < textPosition.length; index++) {
            // draw the text at the random position
            canvas.drawText(""+textChar[rand.nextInt(textLength)], index * fontSize, textPosition[index] * fontSize, paint);
            // check if text has reached bottom or not
            if (textPosition[index] * fontSize > height && Math.random() > 0.975) {
                textPosition[index] = 0;
            }
            textPosition[index]++;
        }
    }

    public void canvasDraw() {
        // set the paint for the canvas
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(5);
        paint.setStyle(Paint.Style.FILL);
        // draw rect to clear the canvas
        canvas.drawRect(0, 0, width, height, paint);

        drawText(); // draw the canvas
    }
}
