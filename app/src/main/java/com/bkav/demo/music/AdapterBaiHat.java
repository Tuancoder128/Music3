package com.bkav.demo.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by vst on 10/04/2018.
 */

public class AdapterBaiHat extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<ThongTinBaiHat> arrayList;

    public AdapterBaiHat(Context context, int resource, ArrayList<ThongTinBaiHat> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    private class ViewHolder{
        private TextView mSoThuTu;
        private TextView mTenBaHat;
        //private TextView mTheLoai;
        private TextView mTime;
        private ImageView mOther;

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charSequence = charSequence.toString().toLowerCase();
                FilterResults results = new FilterResults();
                if(charSequence != null && charSequence.toString().length() > 0){
                    ArrayList<ThongTinBaiHat> arrayList = new ArrayList<>();
                    for (ThongTinBaiHat baihat : arrayList) {
                        if(baihat.toString().toLowerCase().contains(charSequence)){
                            arrayList.add(baihat);
                        }
                    }
                    results.values = arrayList;
                    results.count = arrayList.size();
                }
                else {
                    results.values = arrayList;
                    results.count = arrayList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList.clear();
                for (ThongTinBaiHat song : (ArrayList<ThongTinBaiHat>) filterResults.values){
                    arrayList.add(song);
                }
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.mSoThuTu = view.findViewById(R.id.number);
            viewHolder.mTime = view.findViewById(R.id.time);
            viewHolder.mTenBaHat = view.findViewById(R.id.name_song);
            //viewHolder.mTheLoai  = view.findViewById(R.id.detail);
            viewHolder.mOther = view.findViewById(R.id.other);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final ThongTinBaiHat thongTinBaiHat = arrayList.get(i);

        viewHolder.mTenBaHat.setText(thongTinBaiHat.getTenBaiHat());
        viewHolder.mTime.setText(thongTinBaiHat.getThoigian());
        //viewHolder.mTheLoai.setText(thongTinBaiHat.getTheloai());
        viewHolder.mSoThuTu.setText("" + thongTinBaiHat.getSothutu());
        viewHolder.mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu =  new PopupMenu(context,viewHolder.mOther);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        return view;
    }
}





















