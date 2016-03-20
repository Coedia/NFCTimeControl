/*
 * Copyright (c) 2016. OneOctopus www.oneoctopus.es
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.nfctimecontrol.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.adapters.PlacesAdapter;
import es.oneoctopus.nfctimecontrol.data.PlacesDAO;
import es.oneoctopus.nfctimecontrol.decorators.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesFragment extends Fragment {
    private PlacesDAO db;
    private List<String> placesList;
    private RecyclerView list;
    private PlacesAdapter adapter;

    public PlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment PlacesFragment.
     */
    public static PlacesFragment newInstance() {
        return new PlacesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (RecyclerView) view.findViewById(R.id.places_list);
        db = new PlacesDAO(getActivity());
        placesList = db.getPlaceNames();

        adapter = new PlacesAdapter(getActivity(), placesList);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setHasFixedSize(true);
        list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the list every time the fragment is shown again to reflect changes in case
        // a place is deleted from its activity
        // TODO change this, probably better ways to implement it

        if(adapter != null)
            adapter.replaceData(db.getPlaceNames()
            );
    }
}
