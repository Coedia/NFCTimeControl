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

package com.naroh.nfctimecontrol.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.joda.time.DateTime;

import com.naroh.nfctimecontrol.R;
import com.naroh.nfctimecontrol.data.PlacesDAO;
import com.naroh.nfctimecontrol.dialogs.NewTagDialog;
import com.naroh.nfctimecontrol.fragments.MainActivityFragment;
import com.naroh.nfctimecontrol.fragments.PlacesFragment;
import com.naroh.nfctimecontrol.fragments.StatsFragment;
import com.naroh.nfctimecontrol.helpers.NfcHandler;
import com.naroh.nfctimecontrol.helpers.SPHelper;

public class MainActivity extends AppCompatActivity implements NewTagDialog.WriteToNFC{

    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;
    String placeNameToWrite = "";
    private NfcHandler nfcHandler;
    private boolean erase;
    private Drawer drawer;
    private CoordinatorLayout parentLayout;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = (CoordinatorLayout) findViewById(R.id.parent);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcHandler = new NfcHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize NavigationDrawer using MaterialDrawer library
        initializeNavigationDrawer(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, MainActivityFragment.newInstance()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NewTagDialog dialog = new NewTagDialog();
                dialog.show(getSupportFragmentManager(), "newtag");
            }
        });
    }

    private void initializeNavigationDrawer(Toolbar toolbar) {

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.main).withIcon(R.drawable.ic_home_black_24dp),
                        new PrimaryDrawerItem().withName(R.string.stats).withIcon(R.drawable.ic_assessment_black_24dp),
                        new PrimaryDrawerItem().withName(R.string.places).withIcon(R.drawable.ic_room_black_24dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 0:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, MainActivityFragment.newInstance()).commit();
                                break;
                            case 1:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, StatsFragment.newInstance()).commit();
                                break;
                            case 2:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, PlacesFragment.newInstance(), "placesfragment").commit();
                            default:
                                break;
                        }
                        getSupportFragmentManager().executePendingTransactions();
                        drawer.closeDrawer();
                        return true;
                    }
                })
        .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.erase_tag) {
            erase = true;
            snackbar = Snackbar.make(parentLayout, R.string.scan_to_erase, Snackbar.LENGTH_INDEFINITE);
            // Set red background, white text
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
            ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
            snackbar.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void enableForegroundMode(){
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    public void disableForegroundMode(){
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundMode();
        // Detect if the intent is a NFC one - when scanning a tag while the activity is in
        // the background
        detectIfNfcIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundMode();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Detect if the intent is a NFC one - when scanning a tap while the activity is on top
        detectIfNfcIntent(intent);
    }

    public void detectIfNfcIntent(Intent intent){
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                || intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)){

            // Get the NFC tag from the intent
            Tag discoveredTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            // The user has just wrote a new place name, so write it to the NFC tag
            if(!placeNameToWrite.equals("")) {
                nfcHandler.writeTag(discoveredTag, placeNameToWrite);
                placeNameToWrite = "";
                SPHelper.putBoolean(this, "first_time", false);
                snackbar.dismiss();
            }else if (erase){
                // The user asked to erase the tag via the menu button
                nfcHandler.eraseTag(discoveredTag);
                erase = false;
                snackbar.dismiss();
            }else {
                String name = nfcHandler.readTag(intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES));
                if (name != null) {
                    PlacesDAO db = new PlacesDAO(this);
                    db.check(DateTime.now(), name);

                } else
                    Toast.makeText(MainActivity.this, R.string.error_reading_nfc_tag, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void setNameToWrite(String nameToWrite) {
        placeNameToWrite = nameToWrite;
        snackbar = Snackbar.make(parentLayout, R.string.scan_to_write, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

}
