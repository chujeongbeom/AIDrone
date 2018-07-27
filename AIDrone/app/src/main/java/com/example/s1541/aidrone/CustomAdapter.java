package com.example.s1541.aidrone;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

class CustomAdapter extends BaseAdapter {

    public class ListContents {

        String msg;
        int type;

        ListContents(String msg, int type) {
            this.msg = msg;
            this.type = type;
        }

    }

    private ArrayList<ListContents> m_List;

    public CustomAdapter() {
        m_List = new ArrayList<ListContents>();
    }

    //외부에서 아이템 추가 요청 시 사용
    public void add(String msg, int type) {
        m_List.add(new ListContents(msg, type));
    }

    //외부에서 아이템 삭제 요청 시 사용
    public void remove(int position) {
        m_List.remove(position);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        TextView text = null;
        CustomHolder holder = null;
        LinearLayout layout = null;
        View viewRight = null;
        View viewLeft = null;

        //리스트가 길어지면 현재 화면에 보이지 않는 아이템은 converView가 null 상태로 들어 온다.
        if(convertView == null) {
            //view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_chatitem, parent, false);

            layout = (LinearLayout) convertView.findViewById(R.id.layout);
            text = (TextView) convertView.findViewById(R.id.text);
            viewRight = (View) convertView.findViewById(R.id.imageViewRight);
            viewLeft = (View) convertView.findViewById(R.id.imageViewleft);

            //홀더 생성 및 tag로 등록
            holder = new CustomHolder();
            holder.mTextView = text;
            holder.layout = layout;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);

        } else {
            holder = (CustomHolder) convertView.getTag();
            text = holder.mTextView;
            layout = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }

        //Text 등록
        text.setText(m_List.get(position).msg);

        if( m_List.get(position).type == 0 ) {
            text.setBackgroundResource(R.drawable.cloud1);
            layout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }else if(m_List.get(position).type == 1){
            text.setBackgroundResource(R.drawable.cloud2);
            layout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
        }

        return convertView;

    }

    private class CustomHolder {
        TextView mTextView;
        LinearLayout layout;
        View viewRight;
        View viewLeft;
    }

}
