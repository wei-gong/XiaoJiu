package com.qtec.assistant.ui;

import android.content.Context;
import android.widget.TextView;
import com.qtec.assistant.ExtraContent;
import com.qtec.assistant.R;
import com.qtec.common.base.BaseAdapter;
import com.qtec.common.base.BaseViewHolder;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/17
 */
public class ExtraContentAdapter extends BaseAdapter<ExtraContent> {

    public ExtraContentAdapter(Context context, int layoutRes, List<ExtraContent> datas) {
        super(context, layoutRes, datas);
    }

    @Override
    public int getItemLayout(ExtraContent data) {
        int layoutRes = mLayoutRes;
        if(data != null){
            switch (data.getStyle()){
                case ExtraContent.Style.SAY_SAMPLE:
                    layoutRes = R.layout.adapter_say_sample;
                    break;
                case ExtraContent.Style.SELECTOR_CONTACT:
                    layoutRes = R.layout.adapter_select_contact;
                    break;
                case ExtraContent.Style.TODO_CREATE:
                    layoutRes = R.layout.adapter_todo_created;
                    break;
                default:
                    break;
            }
        }
        return layoutRes;
    }

    @Override
    protected void convertView(BaseViewHolder viewHolder, ExtraContent extraContent) {
        if(extraContent != null){
            switch (extraContent.getStyle()){
                case ExtraContent.Style.SAY_SAMPLE:
                    ((TextView)viewHolder.getView(R.id.tvSample)).setText(extraContent.getTitle());
                    break;
                case ExtraContent.Style.SELECTOR_CONTACT:
                    ((TextView)viewHolder.getView(R.id.tvTitle)).setText(extraContent.getTitle());
                    ((TextView)viewHolder.getView(R.id.tvSubTitle)).setText(extraContent.getSubTitle());
                    break;
                case ExtraContent.Style.TODO_CREATE:
//                    ((ImageView)viewHolder.getView(R.id.ivImage)).setImageURI(extraContent.getImage());
                    ((TextView)viewHolder.getView(R.id.tvTitle)).setText(extraContent.getTitle());
                    ((TextView)viewHolder.getView(R.id.tvDate)).setText(extraContent.getSubTitle());
                    break;
                default:
                    break;
            }
        }
    }
}
