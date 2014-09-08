package myexpenses.ng2.com.myexpenses.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import myexpenses.ng2.com.myexpenses.R;

/**
 * Created by Nikos on 7/26/2014.
 */
public class HistoryListViewAdapter extends CursorAdapter{

    LayoutInflater inflater;

    public HistoryListViewAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_history ,parent , false );

        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        TextView tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        ImageView ivCategory = (ImageView) view.findViewById(R.id.ivCategory);

        Typeface typeface = Typeface.createFromAsset(context.getAssets() , "fonts/font_exo2.otf");

        tvPrice.setTypeface(typeface);
        tvDate.setTypeface(typeface);
        tvCategory.setTypeface(typeface);
        tvNotes.setTypeface(typeface);


        tvPrice.setText(cursor.getString(3) + " €");
        tvDate.setText(cursor.getString(2));
        tvCategory.setText(cursor.getString(1));
        if(cursor.getString(1).equals("Food")){
            ivCategory.setImageResource(R.drawable.food);
        }else if(cursor.getString(1).equals("Personal")){
            ivCategory.setImageResource(R.drawable.personal);
        }else if(cursor.getString(1).equals("Clothing")){
            ivCategory.setImageResource(R.drawable.clothing);
        }else if(cursor.getString(1).equals("Drinks")){
            ivCategory.setImageResource(R.drawable.drinks);
        }
        tvNotes.setText(cursor.getString(4));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
        TextView tvNotes = (TextView) view.findViewById(R.id.tvNotes);
        ImageView ivCategory = (ImageView) view.findViewById(R.id.ivCategory);

        Typeface typeface = Typeface.createFromAsset(context.getAssets() , "fonts/font_exo2.otf");
        tvPrice.setTypeface(typeface);
        tvDate.setTypeface(typeface);
        tvCategory.setTypeface(typeface);
        tvNotes.setTypeface(typeface);

        tvPrice.setText(cursor.getString(3) + " €");
        tvDate.setText(cursor.getString(2));
        tvCategory.setText(cursor.getString(1));
        if(cursor.getString(1).equals("Food")){
            ivCategory.setImageResource(R.drawable.food);
        }else if(cursor.getString(1).equals("Personal")){
            ivCategory.setImageResource(R.drawable.personal);
        }else if(cursor.getString(1).equals("Clothing")){
            ivCategory.setImageResource(R.drawable.clothing);
        }else if(cursor.getString(1).equals("Drinks")){
            ivCategory.setImageResource(R.drawable.drinks);
        }
        tvNotes.setText(cursor.getString(4));
    }
}
