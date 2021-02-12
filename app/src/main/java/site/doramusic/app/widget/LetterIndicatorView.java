package site.doramusic.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public final class LetterIndicatorView extends View {

    private int indicatorBgColor;
    private float indicatorItemWidth;
    private float indicatorItemHeight;
    private float indicatorTextSize;
    private int indicatorSelectedTextColor;
    private int indicatorSelectedBgColor;
    private float indicatorSelectedBgRadius;
    private int indicatorUnselectedTextColor;

    private float zoomUpIndicatorTextSize;
    private int zoomUpIndicatorTextColor;
    private int zoomUpIndicatorBgColor;
    private float zoomUpIndicatorBgRadius;
    private float zoomUpIndicatorMargin;

    private Drawable headerDrawable;

    private ArrayList<String> indicators;

    private Paint paint;
    private Rect textBounds;
    private Path path;
    private RectF zoomUpCircleRect;

    private boolean isOnTouchMode;
    private float zoomUpIndicatorCenterY;

    private int onTouchIndex;
    private int outChangeIndex;
    private OnIndicatorIndexChangeListener onIndicatorIndexChangeListener;

    public LetterIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public LetterIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        configDefaultAttrs();
        loadXmlAttrs(attrs);

        indicators = new ArrayList<>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textBounds = new Rect();
        path = new Path();
        zoomUpCircleRect = new RectF();
    }

    private void configDefaultAttrs() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        indicatorBgColor = Color.TRANSPARENT;
        indicatorItemWidth = indicatorItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        indicatorTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        indicatorSelectedTextColor = Color.parseColor("#1b8fe6");
        indicatorSelectedBgColor = Color.TRANSPARENT;
        indicatorSelectedBgRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        indicatorUnselectedTextColor = Color.parseColor("#646464");

        zoomUpIndicatorTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        zoomUpIndicatorTextColor = Color.WHITE;
        zoomUpIndicatorBgColor = Color.argb(0x30, 0x00, 0x00, 0x00);
        zoomUpIndicatorBgRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        zoomUpIndicatorMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
    }

    private void loadXmlAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LetterIndicatorView);

            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_bg_color)) {
                indicatorBgColor = array.getColor(R.styleable.LetterIndicatorView_dora_indicator_bg_color, indicatorBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_item_width)) {
                indicatorItemWidth = array.getDimension(R.styleable.LetterIndicatorView_dora_indicator_item_width, indicatorItemWidth);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_item_height)) {
                indicatorItemHeight = array.getDimension(R.styleable.LetterIndicatorView_dora_indicator_item_height, indicatorItemHeight);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_text_size)) {
                indicatorTextSize = array.getDimension(R.styleable.LetterIndicatorView_dora_indicator_text_size, indicatorTextSize);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_selected_text_color)) {
                indicatorSelectedTextColor = array.getColor(R.styleable.LetterIndicatorView_dora_indicator_selected_text_color, indicatorSelectedTextColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_bg_color)) {
                indicatorSelectedBgColor = array.getColor(R.styleable.LetterIndicatorView_dora_indicator_bg_color, indicatorSelectedBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_selected_bg_radius)) {
                indicatorSelectedBgRadius = array.getDimension(R.styleable.LetterIndicatorView_dora_indicator_selected_bg_radius, indicatorSelectedBgRadius);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_indicator_unselected_text_color)) {
                indicatorUnselectedTextColor = array.getColor(R.styleable.LetterIndicatorView_dora_indicator_unselected_text_color, indicatorUnselectedTextColor);
            }

            if (array.hasValue(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_text_size)) {
                zoomUpIndicatorTextSize = array.getDimension(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_text_size, zoomUpIndicatorTextSize);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_bg_color)) {
                zoomUpIndicatorBgColor = array.getColor(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_bg_color, zoomUpIndicatorBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_text_color)) {
                zoomUpIndicatorTextColor = array.getColor(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_text_color, zoomUpIndicatorTextColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_bg_radius)) {
                zoomUpIndicatorBgRadius = array.getDimension(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_bg_radius, zoomUpIndicatorBgRadius);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_margin)) {
                zoomUpIndicatorMargin = array.getDimension(R.styleable.LetterIndicatorView_dora_zoom_up_indicator_margin, zoomUpIndicatorMargin);
            }

            if (array.hasValue(R.styleable.LetterIndicatorView_dora_header_drawable)) {
                headerDrawable = array.getDrawable(R.styleable.LetterIndicatorView_dora_header_drawable);
            }

            array.recycle();
        }
    }

    public void setIndicators(ArrayList<String> indicators) {
        this.indicators.clear();
        this.indicators.addAll(indicators);
    }

    public void setOutChangeIndex(int index) {
        outChangeIndex = index;
        if (!isOnTouchMode) {
            onTouchIndex = outChangeIndex;
            invalidate();
        }
    }

    public void setOnIndicatorIndexChangeListener(OnIndicatorIndexChangeListener listener) {
        onIndicatorIndexChangeListener = listener;
    }

    public void attachToRecyclerView(RecyclerView recyclerView, DecorationConfig config, SparseArray<String> array) {
        final Decoration decoration = new Decoration(config, array);
        recyclerView.addItemDecoration(decoration);
        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        decoration.setOnTitleIndexChangeListener(new Decoration.OnTitleIndexChangeListener() {
            @Override
            public void onTitleIndexChanged(int index) {
                setOutChangeIndex(index);
            }
        });
        setOnIndicatorIndexChangeListener(new LetterIndicatorView.OnIndicatorIndexChangeListener() {
            @Override
            public void onIndicatorIndexChanged(int index) {
                if (index >= 0) {
                    if (llm != null)
                        llm.scrollToPositionWithOffset(decoration.keyAt(index), 0);
                } else if (index == -1) {
                    if (llm != null)
                        llm.scrollToPositionWithOffset(0, 0);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                if (x >= getWidth() - indicatorItemWidth) {
                    isOnTouchMode = true;
                }
            case MotionEvent.ACTION_MOVE:
                if (isOnTouchMode) {
                    float y = event.getY();
                    calculateOnTouchIndex(y);
                    if (onIndicatorIndexChangeListener != null) {
                        onIndicatorIndexChangeListener.onIndicatorIndexChanged(onTouchIndex);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onTouchIndex = outChangeIndex;
                invalidate();
                isOnTouchMode = false;
                break;
            default:
        }
        if (isOnTouchMode) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void calculateOnTouchIndex(float y) {
        float firstItemTop = (getHeight() - getTotalItemHeight()) / 2;
        onTouchIndex = (int) Math.floor((y - firstItemTop) / indicatorItemHeight);
        if (onTouchIndex < 0) {
            onTouchIndex = -1;
        }
        if (onTouchIndex >= indicators.size()) {
            onTouchIndex = indicators.size() - 1;
        }
        outChangeIndex = onTouchIndex;
        zoomUpIndicatorCenterY = y;
        if (zoomUpIndicatorCenterY < firstItemTop) {
            zoomUpIndicatorCenterY = firstItemTop;
        }
        float lastItemBottom = firstItemTop + getTotalItemHeight();
        if (zoomUpIndicatorCenterY > lastItemBottom) {
            zoomUpIndicatorCenterY = lastItemBottom;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            double zoomUpWidth = Math.sqrt(3) * zoomUpIndicatorBgRadius + zoomUpIndicatorBgRadius;
            wSize = (int) Math.floor(indicatorItemWidth + zoomUpWidth + zoomUpIndicatorMargin + 0.5);
        }
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.AT_MOST) {
            hSize = (int) Math.floor(getTotalItemHeight() + 0.5);
        }
        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicatorBg(canvas);
        drawIndicators(canvas);
        drawZoomUpIndicator(canvas);
    }

    private void drawIndicatorBg(Canvas canvas) {
        float left = getWidth() - indicatorItemWidth;
        float top = 0;
        float right = getWidth();
        float bottom = getHeight();
        paint.setColor(indicatorBgColor);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawIndicators(Canvas canvas) {
        float firstItemTop = (getHeight() - getTotalItemHeight()) / 2;
        float left = getWidth() - indicatorItemWidth;
        float right = getWidth();

        // header
        if (headerDrawable != null) {
            int headerLeft = (int) (right - (indicatorItemWidth + headerDrawable.getIntrinsicWidth()) / 2);
            int headerTop = (int) (firstItemTop - (indicatorItemHeight + headerDrawable.getIntrinsicHeight()) / 2);
            int headerRight = (int) (right - (indicatorItemWidth - headerDrawable.getIntrinsicWidth()) / 2);
            int headerBottom = (int) (firstItemTop - (indicatorItemHeight - headerDrawable.getIntrinsicHeight()) / 2);
            headerDrawable.setBounds(headerLeft, headerTop, headerRight, headerBottom);
            headerDrawable.draw(canvas);
        }

        // title
        for (int i = 0; i < indicators.size(); i++) {
            float top = firstItemTop + i * indicatorItemHeight;
            float bottom = top + indicatorItemHeight;

            if (i == onTouchIndex) {
                paint.setColor(indicatorSelectedBgColor);
                float centerX = (left + right) / 2;
                float centerY = (top + bottom) / 2;
                path.reset();
                path.addCircle(centerX, centerY, indicatorSelectedBgRadius, Path.Direction.CCW);
                canvas.drawPath(path, paint);

                paint.setColor(indicatorSelectedTextColor);
            } else {
                paint.setColor(indicatorUnselectedTextColor);
            }
            paint.setTextSize(indicatorTextSize);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            String title = indicators.get(i);
            paint.getTextBounds(title, 0, title.length(), textBounds);
            float xOffset = left + (indicatorItemWidth - textBounds.width()) / 2;
            float yOffset = top + indicatorItemHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            canvas.drawText(title, xOffset, yOffset, paint);
        }
    }

    public void drawZoomUpIndicator(Canvas canvas) {
        if (isOnTouchMode && onTouchIndex >= 0) {
            // zoom up indicator bg
            float zoomUpRight = getWidth() - indicatorItemWidth - zoomUpIndicatorMargin;
            float circleX = (float) (zoomUpRight - Math.sqrt(3) * zoomUpIndicatorBgRadius);
            float circleY = zoomUpIndicatorCenterY;
            zoomUpCircleRect.left = circleX - zoomUpIndicatorBgRadius;
            zoomUpCircleRect.top = circleY - zoomUpIndicatorBgRadius;
            zoomUpCircleRect.right = circleX + zoomUpIndicatorBgRadius;
            zoomUpCircleRect.bottom = circleY + zoomUpIndicatorBgRadius;
            path.reset();
            double degree = Math.toRadians(60);
            path.moveTo(zoomUpRight, circleY);
            path.lineTo((float) (circleX + Math.cos(degree) * zoomUpIndicatorBgRadius), (float) (circleY - Math.sin(degree) * zoomUpIndicatorBgRadius));
            path.moveTo(zoomUpRight, circleY);
            path.lineTo((float) (circleX + Math.cos(degree) * zoomUpIndicatorBgRadius), (float) (circleY + Math.sin(degree) * zoomUpIndicatorBgRadius));
            path.arcTo(zoomUpCircleRect, 60, 240);
            paint.setColor(zoomUpIndicatorBgColor);
            canvas.drawPath(path, paint);
            // zoom up indicator text
            paint.setColor(zoomUpIndicatorTextColor);
            paint.setTextSize(zoomUpIndicatorTextSize);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            String tmp = indicators.get(onTouchIndex);
            paint.getTextBounds(tmp, 0, tmp.length(), textBounds);
            float xOffset = zoomUpIndicatorBgRadius - textBounds.width() / 2;
            float yOffset = zoomUpIndicatorCenterY - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            canvas.drawText(tmp, xOffset, yOffset, paint);
        }
    }

    private float getTotalItemHeight() {
        float total = 0;
        total += indicators.size() * indicatorItemHeight;
        return total;
    }

    public interface OnIndicatorIndexChangeListener {
        void onIndicatorIndexChanged(int index);
    }
}