package com.incident.polyandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.incident.polyandroid.R;
import com.incident.polyandroid.firebase.MyDatabase;
import com.incident.polyandroid.models.EventModel;
import com.incident.polyandroid.viewholder.EventViewHolder;

public abstract class EventListFragment extends Fragment {

    private static final String TAG = "DEBUG_DB";

    private MyDatabase mDatabase;
    private FirebaseRecyclerAdapter<EventModel, EventViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private Context mContext;
    private LinearLayoutManager mManager;

    public EventListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "my fragment view created");
        View rootView = inflater.inflate(R.layout.fragment_all_event, container, false);
        mContext = this.getContext();
        //init MyDatabase and get the reference to it
        mDatabase = new MyDatabase();
        //init the recyclerView with the container et set it to a fixed size
        mRecycler = rootView.findViewById(R.id.events_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "my fragment activity created");

        //set the layout manager
        mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);

        // Set up FireBaseRecyclerAdapter with the Query
        Query eventsQuery = getQuery(mDatabase.getReference());
        Log.d(TAG, "query : " + eventsQuery.toString());

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<EventModel>()
                .setQuery(eventsQuery, EventModel.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<EventModel, EventViewHolder>(options) {

            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);


            //itemView.setOn

                /*itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onItemClick(View v) {
                        final int position = holder.getAdapterPosition();

                        Context context = getActivity().getApplicationContext(); //TODO gerer click detail
                        CharSequence text = "A IMPLEMENTER" + position;
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });*/

                return new EventViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, final int position, @NonNull EventModel model) {
                holder.bindToEvent(model, mContext);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(mContext, "event : " + position, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "start");
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "stop");
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    public abstract Query getQuery(DatabaseReference databaseReference);
}
