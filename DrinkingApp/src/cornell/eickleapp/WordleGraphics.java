package cornell.eickleapp;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.View;
import android.widget.CheckBox;

public class WordleGraphics extends View {

	Context mContext;
	ArrayList<String[]> nestArrayDrinkList = new ArrayList<String[]>();
	ArrayList<String[]> nestArrayNoDrinkList = new ArrayList<String[]>();
	
	ArrayList<CheckValue>checkboxes= new ArrayList<CheckValue>();
	ArrayList<Integer>moodValue= new ArrayList<Integer>();
	ArrayList<String>posList= new ArrayList<String>();
	ArrayList<String>negList= new ArrayList<String>();
	
	String word, title;
	int count;
	ArrayList<Rect> wordRectList = new ArrayList<Rect>();
	int topEdge = 0;

	public WordleGraphics(Context context, ArrayList<String[]> d,
			ArrayList<String[]> nd, String t) {
		super(context);
		mContext = context;
		nestArrayDrinkList = d;
		nestArrayNoDrinkList = nd;
		title = t;
		organizeMoodList();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		float leftX = canvas.getWidth() / 4;
		float rightX = 3 * canvas.getWidth() / 4;

		// left/drink cloud

		setUpDrinkCloud(canvas, true);

		/*
		 * line to divide the cloud values Paint linePaint = new Paint();
		 * linePaint.setStrokeWidth(10); linePaint.setTextAlign(Align.CENTER);
		 * linePaint.setColor(Color.rgb(242, 147, 39));
		 * canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2,
		 * canvas.getHeight(), linePaint);
		 */
		// right/no drink cloud
		setUpDrinkCloud(canvas, false);

	}

	private int getTotalCount(ArrayList<String[]> values) {
		int sum = 0;
		for (int i = 0; i < values.size(); i++) {
			sum += Integer.parseInt(values.get(i)[1]);
		}
		return sum;
	}

	private void setUpDrinkCloud(Canvas canvas, Boolean drink) {

		wordRectList = new ArrayList<Rect>();
		ArrayList<String[]> arrayList;
		if (drink)
			arrayList = nestArrayDrinkList;
		else
			arrayList = nestArrayNoDrinkList;

		Paint titlePaint = new Paint();
		titlePaint.setTextSize(60);
		titlePaint.setTextAlign(Align.CENTER);
		titlePaint.setColor(Color.rgb(0, 153, 204));
		if (drink) {
			canvas.drawText("Drinking", canvas.getWidth() / 2, 50, titlePaint);
			topEdge = 50;
		} else if (!drink) {
			canvas.drawText("Non-Drinking", canvas.getWidth() / 2,
					canvas.getHeight() / 2 + 50, titlePaint);
			topEdge = 50;
		}
		if (arrayList == null) {

			if (drink) {
				canvas.drawText("N/A", canvas.getWidth() / 2,
						canvas.getHeight() / 4, titlePaint);

			} else if (!drink) {
				canvas.drawText("N/A", canvas.getWidth() / 2,
						3 * canvas.getHeight() / 4, titlePaint);
			}
		}

		if (arrayList != null) {
			titlePaint.setTextAlign(Align.LEFT);

			// <---size>
			for (int n = 0; n < arrayList.size(); n++) {
				word = arrayList.get(n)[0];
				count = Integer.valueOf(arrayList.get(n)[1]);

				Paint textPainter = new Paint();
				textPainter.setTextSize(16);
				//if word is in positivelist color green
				if(posList.indexOf(word)!=-1){
					textPainter.setColor(Color.rgb(14, 109, 97));
				}
				else
					textPainter.setColor(Color.rgb(247, 144, 30));
					
					

				// The sizes should be relative to the total amount
				int total_cnt = getTotalCount(arrayList);

				double size = 400 * (count / Double.valueOf(total_cnt));
				if ((int) size > 50) {
					size = 30.0;
				}

				// setup the text
				textPainter.setTextSize((int) size);
				textPainter.setTextAlign(Align.LEFT);
				Rect wordBound = new Rect();
				textPainter.getTextBounds(word, 0, word.length(), wordBound);
				// root of the branching

				if (n == 0) {
					if (drink) {
						canvas.drawText(word, canvas.getWidth() / 2, topEdge
								+ canvas.getHeight() / 4, textPainter);

						wordBound.set(canvas.getWidth() / 2, topEdge
								+ wordBound.height() / 4 - wordBound.height(),
								canvas.getWidth() / 2 + wordBound.width(),
								topEdge + canvas.getHeight() / 4);
					} else if (!drink) {
						canvas.drawText(word, canvas.getWidth() / 2, topEdge
								+ canvas.getHeight() / 2 + wordBound.height()
								+ canvas.getHeight() / 4, textPainter);

						wordBound.set(
								canvas.getWidth() / 2,
								topEdge + canvas.getHeight() / 2
										+ wordBound.height() / 4
										- wordBound.height(),
								canvas.getWidth() / 2 + wordBound.width(),
								topEdge + canvas.getHeight() / 2
										+ wordBound.height()
										+ canvas.getHeight() / 4);
					}
					wordRectList.add(wordBound);
				} else {
					boolean continueSearch = true;
					boolean intersection = false;
					while (continueSearch) {
						intersection = false;
						Random random = new Random();
						int randomNode = random.nextInt(wordRectList.size());

						int randomSide = random.nextInt(5 - 1) + 1;

						Rect parentRect = wordRectList.get(randomNode);
						// <---switch>
						switch (randomSide) {
						// <-------------------right side branching
						case 1:
							int parentRightSideX = parentRect.right;
							int parentRightSideY = parentRect.bottom;
							// checks if intersects anything
							wordBound.set(parentRightSideX, parentRightSideY
									- wordBound.height(), parentRightSideX
									+ wordBound.width(), parentRightSideY);
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound, canvas, drink)) {
									canvas.drawText(word, parentRightSideX,
											parentRightSideY, textPainter);
									continueSearch = false;
								}

							}
							break;
						// <-------------------bottom side branching
						case 2:
							int parentSideX = parentRect.left;
							int parentSideY = parentRect.bottom;
							// checks if intersects anything
							wordBound.set(parentSideX, parentSideY, parentSideX
									+ wordBound.width(), parentSideY
									+ wordBound.height());
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound, canvas, drink)) {
									canvas.drawText(word, parentSideX,
											parentSideY + wordBound.height(),
											textPainter);
									continueSearch = false;
								}

							}
							break;
						// <-------------------Left side branching
						case 3:
							int parentLeftSideX = parentRect.left;
							int parentLeftSideY = parentRect.bottom;
							// checks if intersects anything
							wordBound.set(parentLeftSideX - wordBound.width(),
									parentLeftSideY - wordBound.height(),
									parentLeftSideX, parentLeftSideY);
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound, canvas, drink)) {
									canvas.drawText(word, parentLeftSideX
											- wordBound.width(),
											parentLeftSideY, textPainter);
									continueSearch = false;
								}

							}
							break;
						// <-------------------Top side branching
						case 4:
							int parentTopSideX = parentRect.left;
							int parentTopSideY = parentRect.top;
							// checks if intersects anything
							wordBound.set(parentTopSideX, parentTopSideY
									- wordBound.height(), parentTopSideX
									+ wordBound.width(), parentTopSideY);
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound, canvas, drink)) {
									canvas.drawText(word, parentTopSideX,
											parentTopSideY, textPainter);
									continueSearch = false;
								}

							}
							break;
						}

					}
				}

				// saves so we can pull up the corner to slap in more words
				wordRectList.add(wordBound);

			}
		}
	}

	private Boolean checkBoundary(Rect bound, Canvas canvas, Boolean drink) {
		if (drink) {
			if (bound.left >= 0 && bound.right <= canvas.getWidth()
					&& bound.top >= 60
					&& bound.bottom <= canvas.getHeight() / 2) {
				return true;
			} else {
				return false;
			}
		} else {
			if (bound.left >= 0 && bound.right <= canvas.getWidth()
					&& bound.top >= canvas.getHeight() / 2 + 60
					&& bound.bottom <= canvas.getHeight()) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void organizeMoodList() {
		checkboxes.add(new CheckValue("happy",
				(CheckBox) findViewById(R.id.wordle_happy)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("productive",
				(CheckBox) findViewById(R.id.wordle_productive)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("lazy",
				(CheckBox) findViewById(R.id.wordle_lazy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("foggy",
				(CheckBox) findViewById(R.id.wordle_foggy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sick",
				(CheckBox) findViewById(R.id.wordle_sick)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("strong",
				(CheckBox) findViewById(R.id.wordle_strong)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("excited",
				(CheckBox) findViewById(R.id.wordle_excited)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("angry",
				(CheckBox) findViewById(R.id.wordle_angry)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("unhappy",
				(CheckBox) findViewById(R.id.wordle_unhappy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sad",
				(CheckBox) findViewById(R.id.wordle_sad)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("defeated",
				(CheckBox) findViewById(R.id.wordle_defeated)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("social",
				(CheckBox) findViewById(R.id.wordle_social)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("fun",
				(CheckBox) findViewById(R.id.wordle_fun)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("sad",
				(CheckBox) findViewById(R.id.wordle_sad)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("energetic",
				(CheckBox) findViewById(R.id.wordle_energetic)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("healthy",
				(CheckBox) findViewById(R.id.wordle_healthy)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("lonely",
				(CheckBox) findViewById(R.id.wordle_lonely)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("successful",
				(CheckBox) findViewById(R.id.wordle_successful)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("apathetic",
				(CheckBox) findViewById(R.id.wordle_apathetic)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("optimistic",
				(CheckBox) findViewById(R.id.wordle_optimistic)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("loved",
				(CheckBox) findViewById(R.id.wordle_loved)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("embarrassed",
				(CheckBox) findViewById(R.id.wordle_embarrassed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sloppy",
				(CheckBox) findViewById(R.id.wordle_sloppy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("out of control",
				(CheckBox) findViewById(R.id.wordle_out_control)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("relaxed",
				(CheckBox) findViewById(R.id.wordle_relaxed)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("uncomfortable",
				(CheckBox) findViewById(R.id.wordle_uncomfortable)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("advernturous",
				(CheckBox) findViewById(R.id.wordle_adventurous)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("stressed",
				(CheckBox) findViewById(R.id.wordle_stressed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("anxious",
				(CheckBox) findViewById(R.id.wordle_anxious)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("depressed",
				(CheckBox) findViewById(R.id.wordle_depressed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("humorous",
				(CheckBox) findViewById(R.id.wordle_humorous)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("regretful",
				(CheckBox) findViewById(R.id.wordle_regretful)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("hopeful",
				(CheckBox) findViewById(R.id.wordle_hopeful)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("outgoing",
				(CheckBox) findViewById(R.id.wordle_outgoing)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("busy",
				(CheckBox) findViewById(R.id.wordle_busy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("tired",
				(CheckBox) findViewById(R.id.wordle_tired)));
		moodValue.add(0);
		for (int i=0;i<moodValue.size();i++){
			if (moodValue.get(i)==1)
				posList.add(checkboxes.get(i).value);
			else
				negList.add(checkboxes.get(i).value);
		}
	}
}
