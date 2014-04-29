package cornell.trickleapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cornell.trickleapp.DrinkCalendar.MyGestureListener;
import cornell.trickleapp.adapter.NavDrawerListAdapter;
import cornell.trickleapp.fragments.GoalsFragment;
import cornell.trickleapp.fragments.HelpFragment;
import cornell.trickleapp.fragments.HomeFragment;
import cornell.trickleapp.fragments.KiipFragment;
import cornell.trickleapp.fragments.RemindersFragment;
import cornell.trickleapp.fragments.SettingsFragment;
import cornell.trickleapp.model.NavDrawerItem;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class FlyOutContainer extends LinearLayout implements
		OnItemClickListener, OnClickListener {

	// References to groups contained in this view.
	private DatabaseHandler db;
	private ListView menu;
	private View content;
	private TextView day;
	private TextView monthDate;
	private Button menuDrawer;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ListView mDrawerList;
	// private static GestureDetectorCompat mDetector;
	// Constants
	private static int menuSize = 80;
	private static int menuMargin = 0;
	private static int image_no = 0;
	private static ArrayList<Integer> image_id_list = new ArrayList<Integer>();
	private static TextView tvImagePlaceholder;
	private static String classTitle;
	public enum MenuState {
		CLOSED, OPEN
	};

	// Position information attributes
	protected int currentContentOffset = 0;
	protected MenuState menuCurrentState = MenuState.CLOSED;

	public FlyOutContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlyOutContainer(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		this.menu = (ListView) this.getChildAt(0);
		this.content = this.getChildAt(1);
		// String dummy3=this.menu.getContext().toString();
		// grabs the context and display the tutorial accordingly
		classTitle = this.content.getContext().getClass().toString()
				.substring(25);
		db = new DatabaseHandler(this.content.getContext());
		//1st time tutorial
		if (!db.variableExistAll("initial_tutorial")){
			db.addValue("initial_tutorial", 1);
			tutorialDetector(classTitle);
		}
			
		// set up the tutorials accordingly

		/*
		 * day=(TextView)findViewById(R.id.tvDateDisplayTop);
		 * 
		 * monthDate=(TextView)findViewById(R.id.tvDateDisplayBot);
		 * 
		 * Calendar today = Calendar.getInstance(); int dayOfWeek =
		 * today.get(Calendar.DAY_OF_WEEK); SimpleDateFormat sdf = new
		 * SimpleDateFormat("EEEE"); String dayOfTheWeek =
		 * sdf.format(dayOfWeek); int dayOfMonth =
		 * today.get(Calendar.DAY_OF_MONTH); int month =
		 * today.get(Calendar.MONTH); day.setText(dayOfTheWeek);
		 * monthDate.setText(month+"/"+dayOfMonth);
		 */

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		menuSize = display.getWidth() / 5;
		menuMargin = (display.getWidth()) - menuSize;

		this.menu.setVisibility(View.GONE);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		ListView layout = (ListView) this.menu;
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		/*navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
*/
		ListAdapter adapter = new NavDrawerListAdapter(this.getContext(),
				navDrawerItems);

		menu.setAdapter(adapter);
		menu.setOnItemClickListener(new SlideMenuClickListener());

		menuDrawer = (Button) findViewById(R.id.bToggleMenu);
		menuDrawer.setOnClickListener(this);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed)
			this.calculateChildDimensions();

		this.menu.layout(left, top, right - menuMargin, bottom);

		this.content.layout(left + this.currentContentOffset, top, right
				+ this.currentContentOffset, bottom);

	}

	public void toggleMenu() {
		switch (this.menuCurrentState) {
		case CLOSED:
			this.menu.setVisibility(View.VISIBLE);
			this.currentContentOffset = this.getMenuWidth();
			this.content.offsetLeftAndRight(currentContentOffset);
			this.menuCurrentState = MenuState.OPEN;
			break;
		case OPEN:
			this.content.offsetLeftAndRight(-currentContentOffset);
			this.currentContentOffset = 0;
			this.menuCurrentState = MenuState.CLOSED;
			this.menu.setVisibility(View.GONE);
			break;
		}

		this.invalidate();
	}

	private int getMenuWidth() {
		return this.menu.getLayoutParams().width;
	}

	private void calculateChildDimensions() {
		this.content.getLayoutParams().height = this.getHeight();
		this.content.getLayoutParams().width = this.getWidth();

		this.menu.getLayoutParams().width = this.getWidth() - menuMargin;
		this.menu.getLayoutParams().height = this.getHeight();
	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragment
		switch (position) {
		case 0:
			Intent goToThisPage = new Intent("cornell.trickleapp.MAINMENU3");
			getContext().startActivity(goToThisPage);
			break;
		case 1:
			tutorialDetector(classTitle);
			break;
		case 2:
			goToThisPage = new Intent("cornell.trickleapp.INITIALSURVEY");
			getContext().startActivity(goToThisPage);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub
		displayView(position);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		toggleMenu();
	}

	// dialogue that shows up as tutorial
	private void tutorialInitialization(final int type) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this.content.getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.tutorial);
		Button close = (Button) dialog.findViewById(R.id.bClose);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		tvImagePlaceholder = (TextView) dialog
				.findViewById(R.id.tvImagePlaceholder);
		image_id_list = new ArrayList<Integer>();
		switch (type) {
		case 0:
			image_id_list.add(R.drawable.tutorial_001);
			image_id_list.add(R.drawable.tutorial_002);
			image_id_list.add(R.drawable.tutorial_003);
			image_id_list.add(R.drawable.tutorial_004);
			image_id_list.add(R.drawable.tutorial_005);
			image_id_list.add(R.drawable.tutorial_006);
			image_id_list.add(R.drawable.tutorial_007);
			
			break;
		case 1:
			image_id_list.add(R.drawable.tutorial_011);
			image_id_list.add(R.drawable.tutorial_012);
			image_id_list.add(R.drawable.tutorial_013);
			image_id_list.add(R.drawable.tutorial_014);
			image_id_list.add(R.drawable.tutorial_015);
			break;
		case 2:
			break;
		case 3:
			image_id_list.add(R.drawable.tutorial_016);
			image_id_list.add(R.drawable.tutorial_017);
			image_id_list.add(R.drawable.tutorial_018);
			image_id_list.add(R.drawable.tutorial_019);
			image_id_list.add(R.drawable.tutorial_020);
			break;
		case 4:
			image_id_list.add(R.drawable.tutorial_021);
			image_id_list.add(R.drawable.tutorial_022);
			image_id_list.add(R.drawable.tutorial_023);
			break;
		case 5:
			image_id_list.add(R.drawable.tutorial_008);
			image_id_list.add(R.drawable.tutorial_009);
			image_id_list.add(R.drawable.tutorial_010);
			break;
		case 6:
			image_id_list.add(R.drawable.tutorial_008);
			image_id_list.add(R.drawable.tutorial_009);
			image_id_list.add(R.drawable.tutorial_010);
			break;
		case 7:
			break;
		}
		// initializes swiping left/right control
		image_no = 1;
		tvImagePlaceholder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// right swipe
				if (image_no < image_id_list.size()) {
					tvImagePlaceholder.setBackgroundResource(image_id_list
							.get(image_no));
					image_no++;
				}
				else{
					dialog.dismiss();
				}
			}
		});
		int content_width=this.content.getWidth();
		tvImagePlaceholder.setWidth(content_width);
		int content_height=(int)(content_width*1.5);
		tvImagePlaceholder.setHeight(content_height);
		tvImagePlaceholder.setBackgroundResource(image_id_list.get(0));

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
			}

		});
		dialog.show();

	}

	private void tutorialDetector(String classTitle) {
		if (classTitle.equals("MainMenu3")) {
			tutorialInitialization(0);
		}
		if (classTitle.equals("DrinkCounter")) {
			tutorialInitialization(1);
		}
		if (classTitle.equals("AfterDrinkSurvey")) {
			tutorialInitialization(2);
		}
		if (classTitle.equals("DrinkCalendar")) {
			tutorialInitialization(3);
		}
		if (classTitle.equals("SocialVisualization")) {
			tutorialInitialization(4);
		}
		if (classTitle.equals("GoalsLayout")) {
			tutorialInitialization(5);
		}
		if (classTitle.equals("GoalsTracking")) {
			tutorialInitialization(6);
		}
		if (classTitle.equals("Settings")) {
			tutorialInitialization(7);
		}

	}

}
