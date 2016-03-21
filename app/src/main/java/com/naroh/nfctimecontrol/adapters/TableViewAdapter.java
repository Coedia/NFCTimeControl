package com.naroh.nfctimecontrol.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;
import com.naroh.nfctimecontrol.models.Check;

public class TableViewAdapter extends TableDataAdapter {
    private List<Check> data;
    private Context context;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;


    private int paddingLeft = 20;
    private int paddingTop = 15;
    private int paddingRight = 20;
    private int paddingBottom = 15;
    private int textSize = 14;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;

    public TableViewAdapter(Context context, List<Check> data) {
        super(context, data);
        this.context = context;
        this.data = data;
        this.dateFormatter = DateTimeFormat.shortDate().withLocale(Locale.getDefault());
        this.timeFormatter= DateTimeFormat.shortTime().withLocale(Locale.getDefault());
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final TextView textView = new TextView(getContext());
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTypeface(textView.getTypeface(), typeface);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        try {
            switch (columnIndex){
                case 0:
                    textView.setText(dateFormatter.print(data.get(rowIndex).getCheckIn()) + "\n" + timeFormatter.print(data.get(rowIndex).getCheckIn()));
                    break;
                case 1:
                    textView.setText(dateFormatter.print(data.get(rowIndex).getCheckOut()) + "\n" + timeFormatter.print(data.get(rowIndex).getCheckOut()));
                    break;
                case 2:
                    textView.setText(String.valueOf(data.get(rowIndex).getHours()) + " min");
                    break;
            }

        } catch(final IndexOutOfBoundsException e) {
            Log.w(getClass().getSimpleName(), "No Sting given for row " + rowIndex + ", column " + columnIndex + ". "
                    + "Caught exception: " + e.toString());
            // Show no text
        }

        return textView;
    }
}
