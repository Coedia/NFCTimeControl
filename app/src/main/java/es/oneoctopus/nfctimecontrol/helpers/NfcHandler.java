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

package es.oneoctopus.nfctimecontrol.helpers;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import org.ndeftools.Message;
import android.os.Parcelable;
import android.widget.Toast;

import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.UnsupportedRecord;

import java.io.IOException;

import es.oneoctopus.nfctimecontrol.R;
import es.oneoctopus.nfctimecontrol.other.Constants;
import es.oneoctopus.nfctimecontrol.other.IterableMessage;

public class NfcHandler {
    private Context context;

    public NfcHandler(Context context) {
        this.context = context;
    }

    public Tag formatTag(Tag tag){
        NdefFormatable formatableTag = NdefFormatable.get(tag);
        if(formatableTag != null){
            try{
                NdefRecord emptyRecord = new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null);
                NdefMessage emptyMessage = new NdefMessage(emptyRecord);
                formatableTag.connect();
                formatableTag.format(emptyMessage);
                return formatableTag.getTag();
            } catch (FormatException | IOException e) {
                e.printStackTrace();
                return null;
            }
        } else return null;
    }

    /**
     * Write the desired data into the scanned NFC tag.
     * @param tag scanned tag
     * @param name the place name
     * @return true if the operation is successful, false otherwise
     */
    public boolean writeTag(Tag tag, String name){
        Ndef ndefTag = Ndef.get(tag);

        // Check if the tag is formatted. If it is not, format it
        if(ndefTag == null)
            ndefTag = Ndef.get(formatTag(tag));

        // The format wasn't successful, so we abort the mission
        if(ndefTag == null)
            return false;

        // Check if the NFC tag is writable
        if(!ndefTag.isWritable()){
            Toast.makeText(context, R.string.nfc_tag_not_writable, Toast.LENGTH_LONG).show();
            return false;
        }

        // Create a record so the device knows what application should handle the tag
        NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());
        // Create a mimetype with the package name and the place name
        byte[] placeNameBytes = name.getBytes();
        NdefRecord dataRecord = NdefRecord.createMime(Constants.NFC_MIME_TYPE, placeNameBytes);
        // Create the definitive NFC message
        NdefMessage message = new NdefMessage(new NdefRecord[] {appRecord, dataRecord});

        // Check if there is enough space
        int messageSize = message.toByteArray().length;
        if(ndefTag.getMaxSize() < messageSize){
            Toast.makeText(context, R.string.nfc_tag_no_space, Toast.LENGTH_LONG).show();
            return false;
        }
        // Try to format and write the data in the tag
        try{
            ndefTag.connect();
            ndefTag.writeNdefMessage(message);
            ndefTag.close();
            Toast.makeText(context, R.string.nfc_tag_successful_write, Toast.LENGTH_LONG).show();
            return true;
        } catch (FormatException | IOException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.nfc_tag_error_writing, Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public String readTag(Parcelable[] data){
        if(data == null)
            return null;
        NdefMessage[] messages = new NdefMessage[data.length];
        for(int i=0; i<data.length; i++)
            messages[i] = (NdefMessage) data[i];
        
        if (messages.length > 0) {
            IterableMessage message = new IterableMessage();
            for (NdefMessage m : messages) {
                for (NdefRecord record : m.getRecords()) {
                    try {
                        message.add(Record.parse(record));
                    } catch (FormatException e) {
                        e.printStackTrace();
                        // if record is unsuported or corrupted, keep it.
                        message.add(UnsupportedRecord.parse(record));
                    }

                }

            }
            return parse(message);
        }else
            return null;
    }

    private String parse(IterableMessage message) {
        if (message.size() == 0)
            return null;
        for (Record r : message.getAllRecords()) {
            if (r instanceof MimeRecord)
                return new String(((MimeRecord) r).getData());
        }
        return null;
    }


}
