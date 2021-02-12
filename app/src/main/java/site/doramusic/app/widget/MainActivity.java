package site.doramusic.app.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;

import java.util.ArrayList;

import dora.util.DensityUtils;
import dora.util.ViewUtils;
import dora.widget.decoration.SideBarItemDecoration;
import dora.widget.decoration.SpaceItemDecoration;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_main;
    private LetterIndicatorView liv_main;
    private LetterAdapter adapter = new LetterAdapter();
    SparseArray<String> array = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv_main = findViewById(R.id.rv_main);
        liv_main = findViewById(R.id.liv_main);
        DecorationConfig decorationConfig = new DecorationConfig.Builder()
                .setLine(1, Color.parseColor("#ebebeb"))
                .setSelectedTextColor(0x04, 0xd5, 0xd5)
                .setUnSelectTextColor(0x64, 0x64, 0x64)
                .setSelectedBgColor(0xff, 0xff, 0xff)
                .setUnSelectBgColor(0xee, 0xee, 0xee)
                .setTextXOffset(DensityUtils.dp2px(12))
                .setTextSize(DensityUtils.dp2px(14))
                .setHeight(DensityUtils.dp2px(30))
                .build();
        array.put(0, "A");
        array.put(1, "B");
        array.put(2, "C");
        array.put(3, "D");
        ViewUtils.configRecyclerView(rv_main, new LinearLayoutManager(this)).setAdapter(adapter);
        rv_main.addItemDecoration(new SpaceItemDecoration(1));
        rv_main.addItemDecoration(new SideBarItemDecoration(array));
        liv_main.attachToRecyclerView(rv_main, decorationConfig, array);

        ArrayList<String> content = new ArrayList<>();
        content.add("aaa");
        content.add("bbb");
        content.add("ccc");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        adapter.addData(content);

        ArrayList<String> letters = new ArrayList<>();
        letters.add("A");
        letters.add("B");
        letters.add("C");
        letters.add("D");
        liv_main.setIndicators(letters);

        update();
    }

    public void update() {
        ArrayList<String> content = new ArrayList<>();
        content.add("aaa");
        content.add("ccc");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        content.add("ddd");
        adapter.setList(content);

        //更新条目头字母
        array.clear();
        array.put(0, "A");
        array.put(1, "C");
        array.put(2, "D");

        //更新侧边栏字母
        ArrayList<String> letters = new ArrayList<>();
        letters.add("A");
        letters.add("C");
        letters.add("D");
        liv_main.setIndicators(letters);
    }
}