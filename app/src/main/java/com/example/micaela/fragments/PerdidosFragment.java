package com.example.micaela.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.micaela.activities.PrincipalActivity;
import com.example.micaela.adapters.AnimalesAdapter;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.clases.Perdidos;
import com.example.micaela.huellas.R;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class PerdidosFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private IPerdidos mIperdidosImpl;

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perdidos, container, false);

        List<Perdidos> perdidos = null;
        mIperdidosImpl = new IPerdidosImpl(getActivity().getApplicationContext());

        try {
            perdidos = mIperdidosImpl.getPerdidos();
            SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
            inicializarRecycler(view, perdidos);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void inicializarRecycler(View view, List<Perdidos> perdidos) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AnimalesAdapter mAdapter = new AnimalesAdapter(perdidos, 0);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    ((PrincipalActivity)getBaseActivity()).getActionButton().hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        ((PrincipalActivity)getBaseActivity()).getActionButton().show();
                    }
                }
            }
        });
    }

    /*    public class AsyncTaskPerdidos extends AsyncTask<Void, Void, Perdidos> {

        public asyncTaskPerdidos (AsyncTaskListener asyncTaskListener){
            this.asyncTaskListener = asyncTaskListener;
        }
        @Override
        protected JSONObject doInBackground(Perdidos... perdidos) {
            int count = urls.length;
            JSONObject json = new JSONObject();
            for(int i = 0; i < count; i++){
                if(isCancelled())
                    break;
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(urls[i].toString());
                HttpResponse response;
                try {
                    response = httpclient.execute(httpget);
                    if(response.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            InputStream instream = entity.getContent();
                            String result= convertStreamToString(instream);
                            json.put("response_" + String.valueOf(i+1), new JSONObject(result));
                            instream.close();
                        }
                    } else {
//cancel(true);
                    }
                } catch (Exception e) {
                    cancel(true);
                }
                publishProgress((int) ((i / (float) count) * 100 ));
            }
            return json;
        }

        protected void onPreExecute() {
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show(); //Mostramos el diálogo antes de comenzar
        }

        protected void onPostExecute(Integer bytes) {
            dialog.dismiss();
        }

        protected void onProgressUpdate (Float... valores) {
            int p = Math.round(100*valores[0]);
            dialog.setProgress(p);
        }


    }

*/

}
