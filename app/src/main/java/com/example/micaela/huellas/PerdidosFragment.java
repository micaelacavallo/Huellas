package com.example.micaela.huellas;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class PerdidosFragment extends BaseFragment {

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perdidos, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.perdidos_recycler_view);

        /* recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerAdapter mAdapter = new RecyclerAdapter("PASAR LISTA");
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/
        return view;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }


    }

    public abstract class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context mContext;

        public RecyclerAdapter(List<String> animales, Context context) {
            mContext = context;

        }

        protected abstract ViewHolder getViewHolder(View view);

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_datos_item, viewGroup, false);
            return getViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
