package com.qtec.assistant.ui;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TextView;
import com.qtec.assistant.ChatData;
import com.qtec.assistant.ExtraContent;
import com.qtec.assistant.R;
import com.qtec.common.base.BaseAdapter;
import com.qtec.common.base.BaseViewHolder;
import java.util.List;

/**
 * @author gongw
 * @date 2019/1/11
 */
public class ChatAdapter extends BaseAdapter<ChatData> {

    public ChatAdapter(Context context, int layoutRes, List<ChatData> datas) {
        super(context, layoutRes, datas);
    }

    @Override
    public int getItemLayout(ChatData data) {
        int layoutRes = mLayoutRes;
        if(data != null){
            switch (data.getStyle()){
                case ChatData.Style.CHAT_BUBBLE_LEFT:
                    layoutRes = R.layout.adapter_chat_bubble_left;
                    break;
                case ChatData.Style.CHAT_BUBBLE_RIGHT:
                    layoutRes = R.layout.adapter_chat_bubble_right;
                    break;
                default:
                    break;
            }
        }
        return layoutRes;
    }

    @Override
    protected void convertView(BaseViewHolder viewHolder, final ChatData chatData) {
        if(chatData != null){
            switch (chatData.getStyle()){
                case ChatData.Style.CHAT_BUBBLE_LEFT:
                    ((TextView)viewHolder.getView(R.id.tvContent)).setText(chatData.getContent());
                    if(chatData.getExtraContent()!=null && chatData.getExtraContent().size() > 0){
                        RecyclerView recyclerView = viewHolder.getView(R.id.rvSubContents);
                        switch (chatData.getExtraContent().get(0).getStyle()){
                            case ExtraContent.Style.SAY_SAMPLE:
                                int totalLength = 0;
                                final Paint paint = new Paint();
                                //注意这里的12要与R.layout.adapter_say_sample中TextView的textsize一样
                                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, mContext.getResources().getDisplayMetrics()));
                                for(ExtraContent extraContent : chatData.getExtraContent()){
                                    //注意这里要加上R.layout.adapter_say_sample中TextView的padding值与margin值
                                    totalLength += paint.measureText(extraContent.getTitle()) +
                                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2 * 3 + 2 * 8, mContext.getResources().getDisplayMetrics());
                                }
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, totalLength);
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int i) {
                                        //注意这里要加上R.layout.adapter_say_sample中TextView的padding值与margin值
                                        return (int) (paint.measureText(chatData.getExtraContent().get(i).getTitle()) +
                                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2 * 3 + 2 * 8, mContext.getResources().getDisplayMetrics()));
                                    }
                                });
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setAdapter(new ExtraContentAdapter(mContext, R.layout.adapter_say_sample, chatData.getExtraContent()));
                                break;
                            case ExtraContent.Style.SELECTOR_CONTACT:
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(new ExtraContentAdapter(mContext, R.layout.adapter_select_contact, chatData.getExtraContent()));
                                break;
                            case ExtraContent.Style.TODO_CREATE:
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(new ExtraContentAdapter(mContext, R.layout.adapter_todo_created, chatData.getExtraContent()));
                                break;
                            default:
                                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recyclerView.setAdapter(new ExtraContentAdapter(mContext, R.layout.adapter_select_contact, chatData.getExtraContent()));
                                break;
                        }
                    }
                    break;
                case ChatData.Style.CHAT_BUBBLE_RIGHT:
                    ((TextView)viewHolder.getView(R.id.tvContent)).setText(chatData.getContent());
                    break;
                default:
                    break;
            }
        }
    }
}
