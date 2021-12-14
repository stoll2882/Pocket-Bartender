package com.toll.sam.pocketbartender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseDrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseDrinksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowseDrinksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseDrinksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseDrinksFragment newInstance(String param1, String param2) {
        BrowseDrinksFragment fragment = new BrowseDrinksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate layout here?
        //setContentView(R.layout.fragment_browse_drinks);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //not sure where to put this
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make a request for more drinks to fill recycler view
            }
        });

        class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
            boolean multiSelect = false;
            ActionMode actionMode;
            ActionMode.Callback callbacks;
            List<Drink> selectedItems = new ArrayList<>(); // the list of currently selected items in CAM

            /*
             * handles click events for each view in the recycler view
             *
             * @param n/a
             * @return n/a
             */
            class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
                CardView myCardView1;
                TextView myText1;

                /*
                 * constructor for each view in recycler view
                 *
                 * @param the view
                 * @return an item in the recycler view
                 */
                public CustomViewHolder(@NonNull View itemView) {
                    super(itemView);

                    myCardView1 = itemView.findViewById(R.id.myCardView1);
                    myText1 = itemView.findViewById(R.id.myText1);
                    myImage1 = itemView.findViewById(R.id.myImage1);

                    // wire 'em up!!
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                }
                /*
                 * updates the view in the recycler view
                 *
                 * @param the respective video in the list
                 * @return none
                 */
                public void updateView(Video v) {
                    myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                    myText1.setText(v.getTitle());
                    myImage1.setImageResource(v.getPosterId());
                }

                /*
                 * makes its so the user can select an item
                 *
                 * @param the respective video
                 * @return a selected video
                 */
                public void selectItem(Video v) {
                    if (multiSelect) {
                        if (selectedItems.contains(v)) {
                            selectedItems.remove(v);
                            myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                        } else {
                            selectedItems.add(v);
                            myCardView1.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
                        }
                        actionMode.setTitle(selectedItems.size() + " item(s) selected");
                    }
                }

                /*
                 * click listener for each view in the recycler view
                 *
                 * @param the selected view
                 * @return launches the VideoDetailActivity
                 */
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    selectItem(videosList.get(getAdapterPosition()));
                    Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);

                    intent.putExtra("watched", videosList.get(getAdapterPosition()).getWatched());
                    intent.putExtra("title", videosList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("type", videosList.get(getAdapterPosition()).getType());
                    intent.putExtra("posterId", videosList.get(getAdapterPosition()).getPosterId());

                    intent.putExtra("editOrNew", getAdapterPosition());
                    launcher.launch(intent);
                }
            }

            /*
             * inflates the card view
             *
             * @param the viewgroup parent aka recyclerview
             * @return an inflated view
             */
            @NonNull
            @Override
            public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.card_view_list_item, parent, false);
                return new CustomViewHolder(view);
            }

            /*
             * both under here are required in the class
             *
             * @param n/a
             * @return n/a
             */
            @Override
            public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
                Video v = videosList.get(position);
                holder.updateView(v);
            }

            @Override
            public int getItemCount() {
                return videosList.size();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_drinks, container, false);
    }
}