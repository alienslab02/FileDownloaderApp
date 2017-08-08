package com.bilal.filedownloaderapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bilal.filedownloaderapp.adapters.PinBoardAdapter;
import com.bilal.filedownloaderapp.models.PinBoard;
import com.bilal.filedownloaderapp.tools.APIResponseParser;
import com.bilal.filedownloaderapp.tools.EndlessRecyclerViewScrollListener;
import com.bilal.filedownloaderapp.utils.Constants;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private RequestQueue queue;
    private ArrayList<PinBoard> pinBoards;
    // Recycler View
    private SwipeRefreshLayout swipeContainer;
    private PinBoardAdapter boardAdapter;
    private RecyclerView boardRecyclerView;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this.getApplicationContext());
        initComponents();
        requestAPI(0);
    }


    private void initComponents() {
        pinBoards = new ArrayList<>();
        boardRecyclerView = (RecyclerView) findViewById(R.id.boardList);
        boardAdapter = new PinBoardAdapter(this, pinBoards);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        boardRecyclerView.setLayoutManager(mLayoutManager);
        boardRecyclerView.setAdapter(boardAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager)mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                requestAPI(page);
            }
        };
        boardRecyclerView.addOnScrollListener(scrollListener);
        boardAdapter.setOnItemClickListener(itemClickListener);

        // initialize swipe-refresh
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setOnRefreshListener(refreshListener);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(boardRecyclerView != null) {
                    boardRecyclerView.scrollToPosition(0);
                }
            }
        });

    }

    PinBoardAdapter.OnItemClickListener itemClickListener = new PinBoardAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int pos) {
            PinBoard board = pinBoards.get(pos);
            boardAdapter.cancelLoading(board);
        }
    };

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestAPI(0);
        }
    };

    private void adjustMorePinBoards(ArrayList<PinBoard> moreBoards, int page) {
        if(page == 0) {
            pinBoards.clear();
            scrollListener.resetState();
        }
        pinBoards.addAll(moreBoards);
        boardAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    public void requestAPI(final int page) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    APIResponseParser parser = new APIResponseParser();
                    ArrayList<PinBoard> pinBoards = parser.parsePinBoard(response);
                    adjustMorePinBoards(pinBoards, page);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LOG("Error: -> " + error.toString());
            }
        });

        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        boardAdapter.onDestroy();
        super.onDestroy();
    }
}
