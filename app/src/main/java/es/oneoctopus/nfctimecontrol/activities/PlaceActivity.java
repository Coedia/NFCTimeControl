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

package es.oneoctopus.nfctimecontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.data.PlacesDAO;

public class PlaceActivity extends AppCompatActivity {
    private String place;
    private TextView placeName;
    private TextView timesHere;
    private PlacesDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        if (getIntent().getExtras().getString("place") != null)
            place = getIntent().getExtras().getString("place");

        placeName = (TextView) findViewById(R.id.place_name);
        timesHere = (TextView) findViewById(R.id.times_here);

        db = new PlacesDAO(this);

        setPlaceInfo();
    }

    private void setPlaceInfo() {
        placeName.setText(place);
        timesHere.setText(getResources().getQuantityString(R.plurals.times_here, ((Long)db.getVisits(place)).intValue(), db.getVisits(place)));
    }
}
