package cornell.eickleapp.fragments;
import cornell.eickleapp.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GoalsFragment extends Fragment{
	public GoalsFragment(){}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);
          
        return rootView;
    }
	
	
}
