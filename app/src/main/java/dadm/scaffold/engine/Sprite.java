package dadm.scaffold.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import dadm.scaffold.space.BodyType;

public abstract class Sprite extends ScreenGameObject {

    protected double rotation;

    protected double pixelFactor;

    private final Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    private Paint mPaint;


    protected Sprite (GameEngine gameEngine, int drawableRes) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        mPaint=new Paint();
        this.pixelFactor = gameEngine.pixelFactor;

        this.height = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        this.width = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        radius = Math.max(height, width)/2;
    }

    //This method performs occlusion culling-like optimization
    //check if the positions on the x and y axes and see if the sprite is inside
    //the drawing area
    @Override
    public void onDraw(Canvas canvas) {
        if (positionX > canvas.getWidth()
                || positionY > canvas.getHeight()
                || positionX < - width
                || positionY < - height) {
            return;
        }

//        mPaint.setColor(Color.YELLOW);
//        if(bodyType == BodyType.Circular){
//            canvas.drawCircle((int)(positionX+width/2),(int)(positionY+height/2),(int)radius,mPaint);
//        }else {
//            canvas.drawRect(mBoundingRect, mPaint);
//        }

        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate((float) positionX, (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width/2), (float) (positionY + height/2));
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
