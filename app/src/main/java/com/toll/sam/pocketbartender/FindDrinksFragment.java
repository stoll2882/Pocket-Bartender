package com.toll.sam.pocketbartender;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindDrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindDrinksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "FindDrinksTAG";
    private List<Drink> drinkList = new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    protected ListView listView;
    protected ArrayList<String> ingredientList;
    protected ArrayList<String> selectedIngredients;
    private ArrayAdapter<String> ingredientsAdapter;

    Button searchButton;

    public FindDrinksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindDrinksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindDrinksFragment newInstance(String param1, String param2) {
        FindDrinksFragment fragment = new FindDrinksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_find_drinks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.ingredientsList);
        ingredientList = new ArrayList<>();
        ingredientsAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),  android.R.layout.simple_list_item_multiple_choice, ingredientList);
        listView.setAdapter(ingredientsAdapter);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getIngredientsList();

        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray indexs = listView.getCheckedItemPositions();
                for (int i = 0; i < indexs.size(); i++) {
                    if (indexs.get(i)) {
                        selectedIngredients.add(ingredientList.get(i));
                    }
                }

            }
        });
    }

    private void getIngredientsList() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray ingredients = result.getJSONArray("drinks");
                            int length = ingredients.length();
                            for(int x = 0; x < length; x++) {
                                JSONObject ingredient = (JSONObject) ingredients.get(x);
                                System.out.println(">"+ingredient.getString("strIngredient1")+"<");
                                ingredientList.add(ingredient.getString("strIngredient1"));
                            }
                            Collections.sort(ingredientList);
                            ingredientsAdapter.notifyDataSetChanged();
                            queue.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

            }
        }

        public CustomAdapter() {super();}

        /*
         * inflates the card view
         *
         * @param the viewgroup parent aka recyclerview
         * @return an inflated view
         */
        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FindDrinksFragment.this)
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