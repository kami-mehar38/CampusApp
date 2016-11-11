package abbottabad.comsats.campusapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * This project CampusApp is created by Kamran Ramzan on 11-Nov-16.
 */

class CircleBitmapDisplayer extends RoundedBitmapDisplayer {


    CircleBitmapDisplayer() {
        super(0);
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin));
    }

    private static class CircleDrawable extends RoundedBitmapDisplayer.RoundedDrawable {

        private Bitmap mBitmap;

        CircleDrawable(Bitmap bitmap, int margin) {
            super(bitmap, 0, margin);
            this.mBitmap = bitmap;
        }

        @Override
        public void draw(Canvas canvas) {
            int radius = 0;
            if(mBitmap.getWidth() > mBitmap.getHeight()) {
                radius = mBitmap.getHeight() / 2;
            }else {
                radius = mBitmap.getWidth() / 2;
            }
            canvas.drawRoundRect(mRect, radius, radius, paint);
        }
    }
}
