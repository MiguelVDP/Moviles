package dadm.scaffold.space;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;

public class ParalaxBackground extends ScreenGameObject {

    private Bitmap bitmap;
    private double pixelFactor;
    private double speedY;
    private double imageHeight;
    private double imageWidth;
    private int screenHeight;
    private int screenWidth;
    private double targetWidth;

    private final Matrix matrix = new Matrix();
    private Rect srcRect = new Rect();
    private Rect dstRect = new Rect();


    //We load a bitmap, calculate the speed,
    //and store the height and width of both the screen and image at its display size.
    public ParalaxBackground(GameEngine gameEngine, int speed,
                             int drawableResId) {
        Drawable spriteDrawable = gameEngine.getContext().getResources()
                .getDrawable(drawableResId);
        bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
        this.pixelFactor = gameEngine.pixelFactor;
        speedY = speed * pixelFactor / 1000d;
        imageHeight = spriteDrawable.getIntrinsicHeight() * pixelFactor;
        imageWidth = spriteDrawable.getIntrinsicWidth() * pixelFactor;
        screenHeight = gameEngine.height;
        screenWidth = gameEngine.width;
        targetWidth = Math.min(imageWidth, screenWidth);
    }


    @Override
    public void startGame() {

    }

    //Updates the sprite position
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedY * elapsedMillis;
    }

    public void onDraw(Canvas canvas) {

        efficientDraw(canvas);
//        //This is what the first part of the code does; it draws the image one more time, but
//        //translates all the height of the image, so they tile
//        if (mPositionY > 0) {
//            mMatrix.reset();
//            mMatrix.postScale((float) (mPixelFactor),
//                    (float) (mPixelFactor));
//            mMatrix.postTranslate(0, (float) (mPositionY - mImageHeight));
//            canvas.drawBitmap(mBitmap, mMatrix, null);
//        }
//        mMatrix.reset();
//        mMatrix.postScale((float) (mPixelFactor),
//                (float) (mPixelFactor));
//        mMatrix.postTranslate(0, (float) mPositionY);
//        canvas.drawBitmap(mBitmap, mMatrix, null);
//
//        //Once the Y position gets out of the screen, the second part of the drawing is no longer
//        //needed, so we subtract the image's height. With this, the image keeps the same
//        //position and scrolls smoothly, because the second part of the draw is now equivalent
//        //to the first one, which is not entered.
//        if (mPositionY > mScreenHeight) {
//            mPositionY -= mImageHeight;
//        }
    }

    private void efficientDraw(Canvas canvas) {
        if (positionY < 0) {
            srcRect.set(0,
                    (int) (-positionY/ pixelFactor),
                    (int) (targetWidth / pixelFactor),
                    (int) ((screenHeight - positionY)/ pixelFactor));
            dstRect.set(0,
                    0,
                    (int) targetWidth,
                    (int) screenHeight);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        }
        else {
            srcRect.set(0,
                    0,
                    (int) (targetWidth / pixelFactor),
                    (int) ((screenHeight - positionY) / pixelFactor));
            dstRect.set(0,
                    (int) positionY,
                    (int) targetWidth,
                    (int) screenHeight);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            // We need to draw the previous block
            srcRect.set(0,
                    (int) ((imageHeight - positionY) / pixelFactor),
                    (int) (targetWidth / pixelFactor),
                    (int) (imageHeight / pixelFactor));
            dstRect.set(0,
                    0,
                    (int) targetWidth,
                    (int) positionY);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        }
        if (positionY > screenHeight) {
            positionY -= imageHeight;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
