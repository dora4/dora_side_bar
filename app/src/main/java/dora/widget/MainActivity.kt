package dora.widget

import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val adapter = LetterAdapter(this)
    lateinit var rv_main: RecyclerView
    lateinit var liv_main: LetterIndicatorView
    lateinit var decorationConfig: DecorationConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_main = findViewById(R.id.rv_main)
        liv_main = findViewById(R.id.liv_main)
        decorationConfig = DecorationConfig.Builder()
                .setLine(1, Color.parseColor("#ebebeb"))
                .setSelectedTextColor(0x04, 0xd5, 0xd5)
                .setUnSelectTextColor(0x64, 0x64, 0x64)
                .setSelectedBgColor(0xff, 0xff, 0xff)
                .setUnSelectBgColor(0xee, 0xee, 0xee)
                .setTextXOffset(DensityUtils.dp2px(this, 12f).toFloat())
                .setTextSize(DensityUtils.dp2px(this, 14f).toFloat())
                .setHeight(DensityUtils.dp2px(this, 30f))
                .build()
        val content: ArrayList<String> = ArrayList()
        content.add("aba")
        content.add("bcb")
        content.add("bcc")
        content.add("cdd")
        content.add("ccd")
        content.add("dab")
        content.add("dac")
        content.add("dbb")
        content.add("dcb")
        content.add("dcd")
        content.add("dda")
        content.add("ddb")
        content.add("ddd")
        // 字母头在条目中第一次出现的位置
        val array = buildIndicatorSideLetter(content)
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.setHasFixedSize(true)
        rv_main.itemAnimator = DefaultItemAnimator()
        rv_main.adapter = adapter
        //更新侧边栏字母
        liv_main.setIndicators(array)
        adapter.addData(content)
        rv_main.addItemDecoration(SpaceItemDecoration(1))
        rv_main.addItemDecoration(SideBarItemDecoration(array))
        liv_main.attachToRecyclerView(rv_main, decorationConfig, array)

        updateData()
    }

    private fun updateData() {
        val content: ArrayList<String> = arrayListOf()
        content.add("few")
        content.add("little")
        content.add("more")
        content.add("and")
        content.add("less")
        content.add("other")
        content.add("on")
        content.add("this")
        content.add("that")
        content.add("or")
        content.add("in")
        content.add("out")
        content.add("at")
        content.add("with")
        content.add("where")
        content.add("how")
        content.add("when")

        // 更新侧边栏字母
        liv_main.setIndicators(buildIndicatorSideLetter(content))
        adapter.setList(content)
        // 移除最后一个条目装饰
        rv_main.removeItemDecoration(rv_main.getItemDecorationAt(rv_main.itemDecorationCount - 1))
        // 添加条目装饰
        liv_main.attachToRecyclerView(rv_main, decorationConfig, buildIndicatorSideLetter(content))
    }

    private fun buildIndicatorSideLetter(indicators: List<String>) : SparseArray<String> {
        Collections.sort(indicators)
        val array = SparseArray<String>()
        var lastUpperCaseLetter = ""
        for ((index, indicator) in indicators.withIndex()) {
            if (lastUpperCaseLetter != indicator.toUpperCase().substring(0, 1)) {
                array.put(index, indicator.toUpperCase().substring(0, 1))
            }
            lastUpperCaseLetter = indicator.toUpperCase().substring(0, 1)
        }
        return array
    }
}