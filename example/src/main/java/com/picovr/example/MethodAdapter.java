package com.picovr.example;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.List;

public class MethodAdapter extends ArrayAdapter<String> {
    private static final String TAG = "MethodAdapter";

    List<String> mMethods;
    int mResourceId;
    Context mContext;

    public MethodAdapter(Context context, int resourceId, List<String> methods){
        super(context, resourceId, methods);
        mMethods = methods;
        mResourceId = resourceId;
        mContext = context;
    };

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(android.R.id.text1);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(mMethods.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Method m = mContext.getClass().getDeclaredMethod(mMethods.get(position));
                    m.setAccessible(true);
                    m.invoke(mContext);
                }  catch (Exception e){
                    Log.e(TAG, "onClick: " + e.toString() );
                    Log.e(TAG, "onClick: error: "  + e.getCause().getMessage());
                    ResultUtil.showResult(mContext, e.getCause().getMessage());
                }
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView textView;
    }
}
