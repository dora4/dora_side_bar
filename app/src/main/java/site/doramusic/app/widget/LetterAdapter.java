package site.doramusic.app.widget;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public class LetterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LetterAdapter() {
        super(R.layout.item_side_bar);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_side_bar, s);
    }
}
