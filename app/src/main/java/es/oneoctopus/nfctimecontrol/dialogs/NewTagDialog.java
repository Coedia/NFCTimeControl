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

package es.oneoctopus.nfctimecontrol.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import es.oneoctopus.nfctimecontrol.R;

public class NewTagDialog extends DialogFragment {
    private WriteToNFC activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = (WriteToNFC) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.set_venues_name));
        final View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_venue_name, null);
        builder.setView(layout);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText name = (EditText) layout.findViewById(R.id.name);
                String venueName = name.getText().toString();
                if (!venueName.trim().equals("")){
                    // Send the name to the main activity so it gets written in the NFC tag
                    activity.setNameToWrite(venueName);
                }else{
                    Toast.makeText(getActivity(), R.string.insert_valid_name, Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    public interface WriteToNFC {
        void setNameToWrite(String nameToWrite);
    }
}
