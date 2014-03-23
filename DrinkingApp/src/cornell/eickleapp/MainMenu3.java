package cornell.eickleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainMenu3 extends Activity {

	FlyOutContainer root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
	    
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.menu3, null);

		this.setContentView(root);
		
		
	}


	public void toggleMenu(View v){
		this.root.toggleMenu();
	}

}
