package com.tripplanner.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.tripplanner.R;

import java.util.ArrayList;

/**
 * Created by Łukasz on 23.05.2018.
 */

public class AtomPayListAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public AtomPayListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);


            holder.tvFruit = (TextView) convertView.findViewById(R.id.animal);
            holder.tvnumber = (TextView) convertView.findViewById(R.id.number);
            holder.btn_plus = (Button) convertView.findViewById(R.id.plus);
            holder.btn_minus = (Button) convertView.findViewById(R.id.minus);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFruit.setText(MainActivity.modelArrayList.get(position).getFruit());
        holder.tvnumber.setText(String.valueOf(MainActivity.modelArrayList.get(position).getNumber()));


        holder.btn_plus.setTag(R.integer.btn_plus_view, convertView);
        holder.btn_plus.setTag(R.integer.btn_plus_pos, position);
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_plus.getTag(R.integer.btn_plus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                Integer pos = (Integer) holder.btn_plus.getTag(R.integer.btn_plus_pos);

                int number = Integer.parseInt(tv.getText().toString()) + 1;
                tv.setText(String.valueOf(number));

                MainActivity.modelArrayList.get(pos).setNumber(number);

            }
        });

        holder.btn_minus.setTag(R.integer.btn_minus_view, convertView);
        holder.btn_minus.setTag(R.integer.btn_minus_pos, position);
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                Integer pos = (Integer) holder.btn_minus.getTag(R.integer.btn_minus_pos);

                int number = Integer.parseInt(tv.getText().toString()) - 1;
                tv.setText(String.valueOf(number));

                MainActivity.modelArrayList.get(pos).setNumber(number);

            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected Button btn_plus, btn_minus;
        private TextView tvFruit, tvnumber;

    }
}
