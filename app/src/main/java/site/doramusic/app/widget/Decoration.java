package site.doramusic.app.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public final class Decoration extends RecyclerView.ItemDecoration {

    private DecorationConfig config;
    private SparseArray<String> array;
    private Paint paint;
    private Paint.FontMetrics fontMetrics;

    private OnTitleIndexChangeListener onTitleIndexChangeListener;

    public Decoration(DecorationConfig config, SparseArray<String> array) {
        this.config = config;
        this.array = array;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(config.getTextSize());
        fontMetrics = paint.getFontMetrics();
    }

    public int keyAt(int position) {
        return array.keyAt(position);
    }

    public void setOnTitleIndexChangeListener(OnTitleIndexChangeListener listener) {
        onTitleIndexChangeListener = listener;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (array.indexOfKey(position) >= 0) {
                String tmp = array.get(position);
                float xOffset = config.getTextXOffset();
                float yOffset = child.getTop() - config.getHeight() / 2 - (fontMetrics.bottom + fontMetrics.top) / 2;

                View firstVisibleView = parent.getChildAt(0);
                View secondVisibleView = parent.getChildAt(1);
                int secondVisibleViewIndex = parent.getChildAdapterPosition(secondVisibleView);

                int bgColor = Color.rgb(config.getUnSelectBgColorR(), config.getUnSelectBgColorG(), config.getUnSelectBgColorB());
                int textColor = Color.rgb(config.getUnSelectTextColorR(), config.getUnSelectTextColorG(), config.getUnSelectTextColorB());
                if (array.indexOfKey(secondVisibleViewIndex) >= 0 && secondVisibleViewIndex == position
                        && firstVisibleView.getBottom() <= config.getHeight()) {
                    int currentTextR = config.getUnSelectTextColorR();
                    int currentTextG = config.getUnSelectTextColorG();
                    int currentTextB = config.getUnSelectTextColorB();
                    int currentBgR = config.getUnSelectBgColorR();
                    int currentBgG = config.getUnSelectBgColorG();
                    int currentBgB = config.getUnSelectBgColorB();

                    int endTextR = config.getSelectedTextColorR();
                    int endTextG = config.getSelectedTextColorG();
                    int endTextB = config.getSelectedTextColorB();
                    int endBgR = config.getSelectedBgColorR();
                    int endBgG = config.getSelectedBgColorG();
                    int endBgB = config.getSelectedBgColorB();
                    float percent = 1f * (config.getHeight() - firstVisibleView.getBottom()) / config.getHeight();
                    // text
                    currentTextR = (int) (currentTextR + (endTextR - currentTextR) * percent);
                    currentTextG = (int) (currentTextG + (endTextG - currentTextG) * percent);
                    currentTextB = (int) (currentTextB + (endTextB - currentTextB) * percent);
                    // bg
                    currentBgR = (int) (currentBgR + (endBgR - currentBgR) * percent);
                    currentBgG = (int) (currentBgG + (endBgG - currentBgG) * percent);
                    currentBgB = (int) (currentBgB + (endBgB - currentBgB) * percent);
                    bgColor = Color.rgb(currentBgR, currentBgG, currentBgB);
                    textColor = Color.rgb(currentTextR, currentTextG, currentTextB);
                }

                if (config.getLineHeight() > 0) {
                    paint.setColor(config.getLineColor());
                    c.drawRect(0, child.getTop() - config.getHeight(), child.getRight(), child.getTop() - config.getHeight() + config.getLineHeight(), paint);
                    paint.setColor(config.getLineColor());
                    c.drawRect(0, child.getTop() - config.getLineHeight(), child.getRight(), child.getTop(), paint);
                }
                paint.setColor(bgColor);
                c.drawRect(0, child.getTop() - config.getHeight() + config.getLineHeight(), child.getRight(), child.getTop() - config.getLineHeight(), paint);
                paint.setColor(textColor);
                c.drawText(tmp, xOffset, yOffset, paint);
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        View firstVisibleView = parent.getChildAt(0);
        int firstVisibleViewIndex = parent.getChildAdapterPosition(firstVisibleView);
        View secondVisibleView = parent.getChildAt(1);
        int secondVisibleViewIndex = parent.getChildAdapterPosition(secondVisibleView);

        String tmp = "";
        for (int i = 0; i < array.size(); i++) {
            int position = array.keyAt(i);
            if (firstVisibleViewIndex >= position) {
                tmp = array.get(array.keyAt(i));
                if (onTitleIndexChangeListener != null) {
                    onTitleIndexChangeListener.onTitleIndexChanged(i);
                }
            } else {
                break;
            }
        }

        if (!TextUtils.isEmpty(tmp)) {
            float top = 0;
            int currentTextR = config.getSelectedTextColorR();
            int currentTextG = config.getSelectedTextColorG();
            int currentTextB = config.getSelectedTextColorB();
            int currentBgR = config.getSelectedBgColorR();
            int currentBgG = config.getSelectedBgColorG();
            int currentBgB = config.getSelectedBgColorB();
            if (array.indexOfKey(secondVisibleViewIndex) >= 0
                    && firstVisibleView.getBottom() <= config.getHeight()) {
                // 第一个可见的控件是该组最后一个控件
                top = firstVisibleView.getBottom() - config.getHeight();
                // text
                int endTextR = config.getUnSelectTextColorR();
                int endTextG = config.getUnSelectTextColorG();
                int endTextB = config.getUnSelectTextColorB();
                // bg
                int endBgR = config.getUnSelectBgColorR();
                int endBgG = config.getUnSelectBgColorG();
                int endBgB = config.getUnSelectBgColorB();

                float percent = 1f * Math.abs(top) / config.getHeight();

                currentTextR = (int) (currentTextR + (endTextR - currentTextR) * percent);
                currentTextG = (int) (currentTextG + (endTextG - currentTextG) * percent);
                currentTextB = (int) (currentTextB + (endTextB - currentTextB) * percent);
                currentBgR = (int) (currentBgR + (endBgR - currentBgR) * percent);
                currentBgG = (int) (currentBgG + (endBgG - currentBgG) * percent);
                currentBgB = (int) (currentBgB + (endBgB - currentBgB) * percent);
            }
            if (config.getLineHeight() > 0) {
                paint.setColor(config.getLineColor());
                c.drawRect(0, top, parent.getWidth(), top + config.getLineHeight(), paint);
                paint.setColor(config.getLineColor());
                c.drawRect(0, top + config.getHeight() - config.getLineHeight(), parent.getWidth(), top + config.getHeight(), paint);
            }
            paint.setColor(Color.rgb(currentBgR, currentBgG, currentBgB));
            c.drawRect(0, top + config.getLineHeight(), parent.getWidth(), top + config.getHeight() - config.getLineHeight(), paint);
            float xOffset = config.getTextXOffset();
            float yOffset = top + config.getHeight() / 2 - (fontMetrics.bottom + fontMetrics.top) / 2;

            int color = Color.rgb(currentTextR, currentTextG, currentTextB);
            paint.setColor(color);
            c.drawText(tmp, xOffset, yOffset, paint);
        } else {
            if (onTitleIndexChangeListener != null) {
                onTitleIndexChangeListener.onTitleIndexChanged(-1);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (array.indexOfKey(childAdapterPosition) >= 0) {
            outRect.top = config.getHeight();
        }
    }

    public interface OnTitleIndexChangeListener {
        void onTitleIndexChanged(int index);
    }
}