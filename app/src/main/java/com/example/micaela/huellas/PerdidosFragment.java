package com.example.micaela.huellas;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;


public class PerdidosFragment extends BaseFragment {

    private ActionButton mActionButton;
    private RecyclerView mRecyclerView;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perdidos, container, false);

        List<String> animales = new ArrayList<>();
        animales.add("fdgdg");
        animales.add("fdgdg");
        animales.add("fdgdg");
        animales.add("fdgdg");
        animales.add("fdgdg");
        animales.add("fdgdg");

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        inicializarFAB(view);
        inicializarRecycler(view, animales);

        return view;
    }

    private void inicializarRecycler(View view, List<String> animales) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerAdapter mAdapter = new RecyclerAdapter(animales, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    mActionButton.hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
    }


    private void inicializarFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.fab);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.top_out); */
            }
        });
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitulo;
        private TextView textViewEstado;
        private TextView textViewDescripcion;
        private ImageView imageViewFoto;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitulo = (TextView)itemView.findViewById(R.id.textView_titulo);
            textViewEstado = (TextView)itemView.findViewById(R.id.textView_estado);
            textViewDescripcion= (TextView)itemView.findViewById(R.id.textView_descripcion);
            imageViewFoto = (ImageView)itemView.findViewById(R.id.imageView_foto);
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<String> mAnimales;
        private Context mContext;

        public RecyclerAdapter(List<String> animales, Context context) {
            mContext = context;
            mAnimales = animales;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_perdidos_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textViewTitulo.setText("Golden mediano marron");
            holder.textViewDescripcion.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.");

            if (position == 1 || position == 3 || position == 5) {
                holder.textViewEstado.setText("BUSCADO");
                holder.textViewEstado.setBackgroundResource(R.color.orange_light);
            }
            else {
                holder.textViewEstado.setText("ENCONTRADO");
                holder.textViewEstado.setBackgroundResource(R.color.blue_light);
            }

            holder.imageViewFoto.setImageResource(R.mipmap.dog);
        }


        @Override
        public int getItemCount() {
            return mAnimales.size();
        }
    }
}
