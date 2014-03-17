package cornell.eickleapp.fragments;
import cornell.eickleapp.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class KiipFragment extends Fragment{
	public KiipFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_kiip, container, false);
          
        return rootView;
    }
	
	
}
