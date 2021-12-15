package com.toll.sam.pocketbartender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
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
    CustomAdapter adapter = new CustomAdapter();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Drink> drinkList = new ArrayList<>();

    ActivityResultLauncher<Intent> launcher;

    BrowseDrinksAPI drinkAPI;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.drinkList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        drinkAPI = new BrowseDrinksAPI(this);

        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    searchDrinks();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        // setup launcher for receiving intent / result back from VideoDetailActivity
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
//                        Log.d(TAG, "onActivityResult: ");
                        // this callback executes when MainActivity returns from
                        // starting an activity (e.g. SecondActivity) that was
                        // started for a result
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            System.out.println("success");
                        }
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_drinks, container, false);
    }

    public void searchDrinks() throws MalformedURLException {
        drinkAPI = new BrowseDrinksAPI(this);
        drinkAPI.fetchDrinks();
    }

    public void receivedDrinks(List<Drink> drinks) {
        this.drinkList = drinks;
        adapter.notifyDataSetChanged();
    }

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
        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CardView myCardView1;
            TextView myText1;
            ImageView myImage1;

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
                myImage1 = itemView.findViewById(R.id.imageView);

                // wire 'em up!!
                itemView.setOnClickListener(this);
            }
            /*
             * updates the view in the recycler view
             *
             * @param the respective video in the list
             * @return none
             */
            public void updateView(Drink d) {
                myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                myText1.setText(d.getName());
                myImage1.setImageBitmap(d.getImageReference());
            }

            /*
             * makes its so the user can select an item
             *
             * @param the respective video
             * @return a selected video
             */
            public void selectItem(Drink d) {
                if (multiSelect) {
                    if (selectedItems.contains(d)) {
                        selectedItems.remove(d);
                        myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        selectedItems.add(d);
                        myCardView1.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
                    }
                    actionMode.setTitle(selectedItems.size() + " item(s) selected");
                }
            }

            @Override
            public void onClick(View view) {
                selectItem(drinkList.get(getAdapterPosition()));

                Drink currDrink = drinkList.get(getAdapterPosition());

                if (currDrink != null) {
                    Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
                    String id = currDrink.getId();
                    intent.putExtra("id", id);
                    launcher.launch(intent);
                }
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
            View view = LayoutInflater.from(getActivity())
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
            Drink d = drinkList.get(position);
            holder.updateView(d);
        }

        @Override
        public int getItemCount() {
            return drinkList.size();
        }
    }
}