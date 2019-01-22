package com.qtec.xiaojiu

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.qtec.common.base.BaseAdapter
import com.qtec.common.base.BaseViewHolder
import com.qtec.router.provider.IAssistantProvider
import com.qtec.router.provider.ITodoProvider
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * @author gongw
 * @date 2019/1/21
 */
class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMime.setOnClickListener{

        }

        fabCreate.setOnClickListener {
            val assistantService : IAssistantProvider? = ARouter.getInstance().build(IAssistantProvider.PATH_ASSISTANT_SERVICE).navigation() as IAssistantProvider?
            assistantService?.goToAssistantActivity(this@MainActivity)
        }

        rvSupportList.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        rvSupportList.adapter = SupportServiceAdapter(this@MainActivity, R.layout.adapter_support_service, SupportServicesModel.getInstance().supportServiceList)
    }

    private class SupportServiceAdapter(context: Context?, layoutRes: Int, datas: MutableList<SupportService>?) : BaseAdapter<SupportService>(context, layoutRes, datas) {

        override fun convertView(viewHolder: BaseViewHolder?, t: SupportService?) {
            viewHolder?.getView<TextView>(R.id.tvTitle)?.text = t?.title
            viewHolder?.getView<TextView>(R.id.tvSubTtitle)?.text = t?.subTitle
            viewHolder?.itemView?.setOnClickListener{
                val todoService : ITodoProvider? = ARouter.getInstance().build(ITodoProvider.PATH_TODO_SERVICE).navigation() as ITodoProvider?
                todoService?.goTodoListActivity(this.mContext as MainActivity?)
            }
        }

    }

}