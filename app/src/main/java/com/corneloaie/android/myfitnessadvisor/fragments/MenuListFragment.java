package com.corneloaie.android.myfitnessadvisor.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.MenuListObject;

import java.util.List;

public class MenuListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;
    private OnMenuSelectedListener mOnMenuSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnMenuSelectedListener = (OnMenuSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnMenuSelectedListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        mRecyclerView = view.findViewById(R.id.menu_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MenuListObject menuListObject = MenuListObject.get(getActivity());
        List<String> menuList = menuListObject.getMenus();
        mMenuAdapter = new MenuAdapter(menuList);
        mRecyclerView.setAdapter(mMenuAdapter);
        return view;

    }

    public interface OnMenuSelectedListener {
        void onMenuSelcted(String menu);
    }

    private class MenuHolder extends RecyclerView.ViewHolder {

        private TextView mMenuTextView;
        private String menuTitle;

        public MenuHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.menu_item, parent, false));
            mMenuTextView = itemView.findViewById(R.id.menu_title_textView);
            itemView.setOnClickListener(view -> mOnMenuSelectedListener.onMenuSelcted(menuTitle));
        }

        public void bind(String menuTitle) {
            this.menuTitle = menuTitle;
            mMenuTextView.setText(menuTitle);
            switch (menuTitle) {
                case "Summary":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.green_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "Lifetime":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.blue_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "Heartrate":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.red_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "Sleep":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.deep_purple_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "Profile":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.cyan_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
                case "LifeCoach":
                    mMenuTextView.setBackgroundColor(getResources().getColor(R.color.orange_200));
                    mMenuTextView.setTextColor(getResources().getColor(R.color.white));
                    break;
            }
        }
    }


    private class MenuAdapter extends RecyclerView.Adapter<MenuHolder> {

        private List<String> menuList;

        public MenuAdapter(List<String> menuList) {
            this.menuList = menuList;
        }

        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new MenuHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            String menu = menuList.get(position);
            holder.bind(menu);
        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }
    }
}
