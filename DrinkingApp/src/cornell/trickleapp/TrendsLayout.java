package cornell.trickleapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class TrendsLayout extends LinearLayout{
View addonV;
LinearLayout layout;
private LinearLayout linearLay;
    private void inflateLayout(Context context) {

        LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main = layoutInflater.inflate(R.layout.trends, this);
        layout=(LinearLayout)main.findViewById(R.id.llTesting);
        layout.addView(findViewById(R.layout.trends2), 400, 200);
        layout.addView(findViewById(R.layout.trends_bottom), 400, 200);
    }
    
    public TrendsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateLayout(context);
    }
 
    public TrendsLayout(Context context) {
        super(context);
        inflateLayout(context);
    }
	
}
