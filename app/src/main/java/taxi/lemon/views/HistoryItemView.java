package taxi.lemon.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import taxi.lemon.R;

/**
 * TODO: document your custom view class.
 */
public class HistoryItemView extends View {
    public static final String TAG = HistoryItemView.class.getSimpleName();

    private StatusItem mStatus;
    private AddressItem mStartAddress;
    private AddressItem mEndAddress;
    private CostItem mCost;
    private DateItem mDate;

    private void logger(String text) {
        Log.d(TAG, text);
    }

    public HistoryItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public HistoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HistoryItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        logger("init()");
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HistoryItemView, defStyle, 0);

        mStatus = new StatusItem(
                a.getDimension(R.styleable.HistoryItemView_statusSize, TextItem.defSize),
                a.getColor(R.styleable.HistoryItemView_statusColor, TextItem.defColor)
        );

        mStartAddress = new AddressItem(
                a.getDimension(R.styleable.HistoryItemView_addressSize, TextItem.defSize),
                a.getColor(R.styleable.HistoryItemView_addressColor, TextItem.defColor)
        );

        mEndAddress = new AddressItem(
                a.getDimension(R.styleable.HistoryItemView_addressSize, TextItem.defSize),
                a.getColor(R.styleable.HistoryItemView_addressColor, TextItem.defColor)
        );

        mCost = new CostItem(
                a.getDimension(R.styleable.HistoryItemView_costSize, TextItem.defSize),
                a.getColor(R.styleable.HistoryItemView_costColor, TextItem.defColor)
        );

        mDate = new DateItem(
                a.getDimension(R.styleable.HistoryItemView_dateSize, TextItem.defSize),
                a.getColor(R.styleable.HistoryItemView_dateColor, TextItem.defColor)
        );

//        mExampleString = a.getString(
//                R.styleable.HistoryItem_exampleString);
//        mExampleColor = a.getColor(
//                R.styleable.HistoryItem_exampleColor,
//                mExampleColor);
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.HistoryItem_exampleDimension,
//                mExampleDimension);
//
//        if (a.hasValue(R.styleable.HistoryItem_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.HistoryItem_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }

        a.recycle();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        logger("onDraw()");

        mStatus.draw(canvas);
        mCost.draw(canvas);
        mStartAddress.draw(canvas);
        mEndAddress.draw(canvas);
        mDate.draw(canvas);
    }

    /**
     * Set text for status item
     * @param status - status of order
     */
    public void setStatusText(String status) {
        logger("setStatusText()");
        mStatus.setText(status);
        mStatus.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set status text size
     * @param textSize - new text size
     */
    public void setStatusTextSize(float textSize) {
        mStatus.setTextSize(textSize);
        mStatus.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set status text color
     * @param color - new text color
     */
    public void setStatusTextColor(int color) {
        mStatus.setTextColor(color);
        mStatus.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set text for cost item
     * @param cost - order's cost
     */
    public void setCostText(String cost) {
        logger("setStatusText()");
        mCost.setText(cost);
        mCost.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set cost text size
     * @param textSize - new text size
     */
    public void setCostTextSize(float textSize) {
        mCost.setTextSize(textSize);
        mCost.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set cost text color
     * @param color - new text color
     */
    public void setCostTextColor(int color) {
        mCost.setTextColor(color);
        mCost.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set text for connect address item
     * @param startAddress - order's connect address
     */
    public void setStartAddressText(String startAddress) {
        logger("setStatusText()");
        mStartAddress.setText(startAddress);
        mStartAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set connect address text size
     * @param textSize - new text size
     */
    public void setStartAddressSize(float textSize) {
        mStartAddress.setTextSize(textSize);
        mStartAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set connect address text color
     * @param color - new text color
     */
    public void setStartAddressColor(int color) {
        mStartAddress.setTextColor(color);
        mStartAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set text for end address item
     * @param endAddress - order's end address
     */
    public void setEndAddressText(String endAddress) {
        logger("setStatusText()");
        mEndAddress.setText(endAddress);
        mEndAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set end address text size
     * @param textSize - new text size
     */
    public void setEndAddressSize(float textSize) {
        mEndAddress.setTextSize(textSize);
        mEndAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set end address text color
     * @param color - new text color
     */
    public void setEndAddressColor(int color) {
        mEndAddress.setTextColor(color);
        mEndAddress.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set text for date item
     * @param date - order's date
     */
    public void setDateText(String date) {
        logger("setStatusText()");
        mDate.setText(date);
        mDate.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set date text size
     * @param textSize - new text size
     */
    public void setDateTextSize(float textSize) {
        mDate.setTextSize(textSize);
        mDate.invalidateTextPaintAndMeasurements();
        invalidate();
    }

    /**
     * Set date text color
     * @param color - new text color
     */
    public void setDateTextColor(int color) {
        mDate.setTextColor(color);
        mDate.invalidateTextPaintAndMeasurements();
        invalidate();
    }

// TODO: 03.12.2015 add doc comments for below classes and methods
    /**
     * Order's status
     */
    private class StatusItem extends TextItem {

        public StatusItem() {
        }

        public StatusItem(float mTextSize, int mTextColor) {
            super(mTextSize, mTextColor);
        }

        public StatusItem(String mText, float mTextSize, int mTextColor) {
            super(mText, mTextSize, mTextColor);
        }

        @Override
        public void draw(Canvas canvas) {
//            logger("draw()");
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

//            logger("paddingLeft - " + paddingLeft + "\npaddingTop - " + paddingTop + "\npaddingRight - " + paddingRight + "\npaddingBottom - " + paddingBottom);

//            logger("viewWidth - " + getWidth() + "\nviewHeight - " + getHeight());

            int contentWidth = getWidth() - paddingLeft - paddingRight;
            int contentHeight = getHeight() - paddingTop - paddingBottom;

//            logger("contentWidth - " + contentWidth + "\ncontentHeight - " + contentHeight);

            canvas.drawText(
                    getText(),
                    paddingLeft,
                    paddingTop - getTextHeight(),
                    getTextPaint()
            );
        }
    }

    /**
     * Order's address
     */
    private class AddressItem extends TextItem {

        public boolean wasDrawn;

        public AddressItem() {
        }

        public AddressItem(float mTextSize, int mTextColor) {
            super(mTextSize, mTextColor);
            this.wasDrawn = false;
        }

        public AddressItem(String mText, float mTextSize, int mTextColor) {
            super(mText, mTextSize, mTextColor);
            this.wasDrawn = false;
        }

        @Override
        public void draw(Canvas canvas) {

//            logger("draw()");
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

//            logger("paddingLeft - " + paddingLeft + "\npaddingTop - " + paddingTop + "\npaddingRight - " + paddingRight + "\npaddingBottom - " + paddingBottom);

//            logger("viewWidth - " + getWidth() + "\nviewHeight - " + getHeight());

            int contentWidth = getWidth() - paddingLeft - paddingRight;
            int contentHeight = getHeight() - paddingTop - paddingBottom;

//            logger("contentWidth - " + contentWidth + "\ncontentHeight - " + contentHeight);

            canvas.drawText(
                    getText(),
                    paddingLeft,
                    paddingTop - ((mStartAddress.wasDrawn) ? mStartAddress.getTextHeight() : 0) - mStatus.getTextHeight() - getTextHeight(),
                    getTextPaint()
            );

            this.wasDrawn = true;
        }
    }

    /**
     * Order's cost
     */
    private class CostItem extends TextItem {

        public CostItem() {
        }

        public CostItem(float mTextSize, int mTextColor) {
            super(mTextSize, mTextColor);
        }

        public CostItem(String mText, float mTextSize, int mTextColor) {
            super(mText, mTextSize, mTextColor);
        }

        @Override
        public void draw(Canvas canvas) {
//            logger("draw()");
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

//            logger("paddingLeft - " + paddingLeft + "\npaddingTop - " + paddingTop + "\npaddingRight - " + paddingRight + "\npaddingBottom - " + paddingBottom);

//            logger("viewWidth - " + getWidth() + "\nviewHeight - " + getHeight());

            int contentWidth = getWidth() - paddingLeft - paddingRight;
            int contentHeight = getHeight() - paddingTop - paddingBottom;

//            logger("contentWidth - " + contentWidth + "\ncontentHeight - " + contentHeight);

            canvas.drawText(
                    getText(),
                    paddingLeft + contentWidth - getTextWidth(),
                    paddingTop - getTextHeight(),
                    getTextPaint()
            );
        }
    }

    /**
     * Order's date
     */
    private class DateItem extends TextItem {

        public DateItem() {
        }

        public DateItem(float mTextSize, int mTextColor) {
            super(mTextSize, mTextColor);
        }

        public DateItem(String mText, float mTextSize, int mTextColor) {
            super(mText, mTextSize, mTextColor);
        }

        @Override
        public void draw(Canvas canvas) {
//            logger("draw()");
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

//            logger("paddingLeft - " + paddingLeft + "\npaddingTop - " + paddingTop + "\npaddingRight - " + paddingRight + "\npaddingBottom - " + paddingBottom);

//            logger("viewWidth - " + getWidth() + "\nviewHeight - " + getHeight());

            int contentWidth = getWidth() - paddingLeft - paddingRight;
            int contentHeight = getHeight() - paddingTop - paddingBottom;

//            logger("contentWidth - " + contentWidth + "\ncontentHeight - " + contentHeight);

            canvas.drawText(
                    getText(),
                    paddingLeft + contentWidth - getTextWidth(),
                    paddingTop + contentHeight,
                    getTextPaint()
            );
        }
    }

    /**
     * Hold all required parameters needed to draw a TextPaint element
     */
    private abstract class TextItem {
        public static final String defText = "";
        public static final int defColor = Color.BLACK;
        public static final float defSize = 24;

        private TextPaint mTextPaint;
        private String mText;
        private float mTextSize;
        private int mTextColor;
        private float mTextWidth;
        private float mTextHeight;

        private TextItem() {
            // Set up a default TextPaint object
            this.mTextPaint = new TextPaint();
            this.mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            this.mTextPaint.setTextAlign(Paint.Align.LEFT);
            this.mText = defText;
            this.mTextSize = 0;
            this.mTextColor = Color.BLACK;
        }

        protected TextItem(float mTextSize, int mTextColor) {
            this();
            this.mTextSize = mTextSize;
            this.mTextColor = mTextColor;
        }

        protected TextItem(String mText, float mTextSize, int mTextColor) {
            this(mTextSize, mTextColor);
            this.mText = mText;

            invalidateTextPaintAndMeasurements();
        }

        /**
         * Invalidate TextPaint and measurements
         */
        public void invalidateTextPaintAndMeasurements() {
            logger("invalidateTextPaintAndMeasurements()");
            this.mTextPaint.setTextSize(mTextSize);
            this.mTextPaint.setColor(mTextColor);
            this.mTextWidth = mTextPaint.measureText(mText);

            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            this.mTextHeight = fontMetrics.top;

            logger("textWidth - " + this.mTextWidth + "\ntextHeight - " + this.mTextHeight);
        }

        /**
         * Draws the TextItem on canvas
         * @param canvas - canvas which will draw the element
         */
        public abstract void draw(Canvas canvas);

        public TextPaint getTextPaint() {
            return mTextPaint;
        }

        public String getText() {
            return mText;
        }

        public void setText(String mText) {
            this.mText = mText;
        }

        public float getTextSize() {
            return mTextSize;
        }

        public void setTextSize(float mTextSize) {
            this.mTextSize = mTextSize;
        }

        public int getTextColor() {
            return mTextColor;
        }

        public void setTextColor(int mTextColor) {
            this.mTextColor = mTextColor;
        }

        public float getTextWidth() {
            return mTextWidth;
        }

        public void setTextWidth(float mTextWidth) {
            this.mTextWidth = mTextWidth;
        }

        public float getTextHeight() {
            return mTextHeight;
        }

        public void setTextHeight(float mTextHeight) {
            this.mTextHeight = mTextHeight;
        }
    }
}
