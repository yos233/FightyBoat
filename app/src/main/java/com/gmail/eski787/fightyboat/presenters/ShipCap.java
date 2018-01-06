package com.gmail.eski787.fightyboat.presenters;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.errorprone.annotations.DoNotCall;

import static com.gmail.eski787.fightyboat.views.SeaView.SHIP_RADIUS;

/**
 * Concrete implementations of {@link ShipCap} will implement draw calls for the end of a
 * {@link com.gmail.eski787.fightyboat.game.Ship} in the style specified.
 * Created by Erik on 12/29/2016.
 */

public abstract class ShipCap {
    private static final String TAG = ShipCap.class.getSimpleName();

    CapDirection mDirection;

    ShipCap(CapDirection direction) {
        super();
        mDirection = direction;
    }

    /**
     * @param type      The cap type to return.
     * @param direction The direction for the cap to open.
     * @return An instance of a {@link ShipCap} with specified parameters.
     */
    @NonNull
    public static ShipCap getCap(CapType type, CapDirection direction) {
        ShipCap cap = null;

        switch (type) {
            case POINT:
                return new PointShipCap(direction);
            case SQUARE:
                return new SquareShipCap(direction);
            case ROUND:
                return new RoundShipCap(direction);
        }
        throw new RuntimeException("Invalid CapType: " + type.toString());
    }

    private void setDirection(CapDirection direction) {
        mDirection = direction;
    }

    public abstract void drawCap(Canvas canvas, Paint paint, int startX, int startY, int endX, int endY);

    /**
     * Determines which tile edge the {@link com.gmail.eski787.fightyboat.game.Ship} continues on to.
     */
    public enum CapDirection {
        LEFT, RIGHT, UP, DOWN
    }

    /**
     * Available styles for {@link com.gmail.eski787.fightyboat.game.Ship} end caps.
     */
    public enum CapType {
        POINT, SQUARE, ROUND
    }

    private static class RoundShipCap extends ShipCap {
        RoundShipCap(CapDirection direction) {
            super(direction);
        }

        @Override
        public void drawCap(Canvas canvas, Paint paint, int startX, int startY, int endX, int endY) {
            final int width = endX - startX;
            final int height = endY - startY;

            final int midX = startX + (width / 2);
            final int midY = startY + (height / 2);
            final int minDimension = Math.min(width, height);
            final float radius = minDimension * SHIP_RADIUS;

            float minX = 0, maxX = 0, minY = 0, maxY = 0;

            switch (mDirection) {
                case UP:
                    maxX = midX + radius;
                    minX = midX - radius;
                    maxY = midY;
                    minY = startY;
                    break;
                case DOWN:
                    maxX = midX + radius;
                    minX = midX - radius;
                    maxY = endY;
                    minY = midY;
                    break;
                case LEFT:
                    maxX = midX;
                    minX = startX;
                    maxY = midY + radius;
                    minY = midY - radius;
                    break;
                case RIGHT:
                    maxX = endX;
                    minX = midX;
                    maxY = midY + radius;
                    minY = midY - radius;
                    break;
            }

            canvas.drawCircle(midX, midY, radius, paint);
            canvas.drawRect(minX, minY, maxX, maxY, paint);
        }
    }

    private static class PointShipCap extends ShipCap {
        PointShipCap(CapDirection direction) {
            super(direction);
        }

        @Override
        @DoNotCall
        public void drawCap(Canvas canvas, Paint paint, int startX, int startY, int endX, int endY) {
            Log.d(TAG, "PointShipCap not implemented.");
//            throw new RuntimeException("PointShipCap not implemented. Do Not Use.");
        }
    }

    private static class SquareShipCap extends ShipCap {
        SquareShipCap(CapDirection direction) {
            super(direction);
        }

        @Override
        public void drawCap(Canvas canvas, Paint paint, int startX, int startY, int endX, int endY) {
            final int width = endX - startX;
            final int height = endY - startY;

            final int midX = startX + (width / 2);
            final int midY = startY + (height / 2);
            final int minDimension = Math.min(width, height);
            final int radius = (int) (minDimension * SHIP_RADIUS);

            int minX, maxX, minY, maxY;

            minX = midX - radius;
            maxX = midX + radius;
            minY = midY - radius;
            maxY = midY + radius;

            switch (mDirection) {
                case UP:
                    minY = startY;
                    break;
                case DOWN:
                    maxY = endY;
                    break;
                case LEFT:
                    minX = startX;
                    break;
                case RIGHT:
                    maxX = endX;
                    break;
            }

            canvas.drawRect(minX, minY, maxX, maxY, paint);
        }
    }
}
