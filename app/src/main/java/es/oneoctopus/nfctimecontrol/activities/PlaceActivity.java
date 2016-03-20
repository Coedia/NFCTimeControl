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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.adapters.PlacesAdapter;
import es.oneoctopus.nfctimecontrol.adapters.TableViewAdapter;
import es.oneoctopus.nfctimecontrol.data.PlacesDAO;
import es.oneoctopus.nfctimecontrol.fragments.PlacesFragment;
import es.oneoctopus.nfctimecontrol.models.Check;

public class PlaceActivity extends AppCompatActivity {
    private String place;
    private TextView placeName;
    private TextView timesHere;
    private PlacesDAO db;
    private List<Check> checks;
    private TableView tableView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        if (getIntent().getExtras().getString("place") != null) {
            place = getIntent().getExtras().getString("place");
            position = getIntent().getExtras().getInt("position");
        }

        placeName = (TextView) findViewById(R.id.place_name);
        timesHere = (TextView) findViewById(R.id.times_here);
        tableView = (TableView) findViewById(R.id.tableView);

        db = new PlacesDAO(this);

        setPlaceInfo();
    }

    private void setPlaceInfo() {
        placeName.setText(place);
        timesHere.setText(getResources().getQuantityString(R.plurals.times_here, ((Long)db.getVisitsCount(place)).intValue(), db.getVisitsCount(place)));
        checks = db.getChecksIn(place);

        TableViewAdapter adapter = new TableViewAdapter(this, checks);
        tableView.setDataAdapter(adapter);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Checkin", "Checkout", "Time"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.delete_place){
            db.delete(place);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
