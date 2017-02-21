package com.example.maidaidien.matrixeffectcanvas.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by mai.dai.dien on 21/02/2017.
 */

public class MatrixEffect extends View {
    int width = 1000000; // default initial width
    int height = 100;    // default initial height
    Canvas canvas = null;// default canvas
    Bitmap canvasBitmap; // bitmap used to create the canvas
    int fontSize = 15;   // font size of the text which will fall
    int columnSize = width / fontSize; // column size, no digit required to fill screen
    int parentWidth;
    String text = "MATRIXRAIN"; // text which need to be drawn
    char[] textChar = text.toCharArray(); // split the character of the text
    int textLength = textChar.length;
    Random rand = new Random();

    int[] textPosition; // contain the position which will help to draw the text


    public MatrixEffect(Context context) {
        super(context);
    }

    public MatrixEffect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrixEffect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    //function responsonsible for draw calls

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        // draw the bitmap to canvas
        canvas.drawBitmap(canvasBitmap, 0, 0, paint);

        // call the draw command
        canvasDraw();

        // Redraw the canvas
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
        // create a Bitmap
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(canvasBitmap);
        // init paint with black rectangle
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, width, height, paint);

        columnSize = width / fontSize;
        // initalise the textposiotn to zero
        textPosition = new int[columnSize+1]; // add one more drop
        for(int x = 0; x < columnSize; x++) {
            textPosition[x] = 0;
        }
    }
}
