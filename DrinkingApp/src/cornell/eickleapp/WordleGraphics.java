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

public class WordleGraphics extends View {

	Context mContext;
	ArrayList<String[]> nestArrayDrinkList = new ArrayList<String[]>();
	ArrayList<String[]> nestArrayNoDrinkList = new ArrayList<String[]>();
	String word, title;
	int count;
	ArrayList<Rect> wordRectList = new ArrayList<Rect>();

	public WordleGraphics(Context context, ArrayList<String[]> d,
			ArrayList<String[]> nd, String t) {
		super(context);
		// TODO Auto-generated constructor stu
		mContext = context;
		nestArrayDrinkList = d;
		nestArrayNoDrinkList = nd;
		title = t;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		float leftX = canvas.getWidth() / 4;
		float rightX = 3 * canvas.getWidth() / 4;

		// left/drink cloud
		setUpDrinkCloud(canvas,true);
		//right/no drink cloud
		setUpDrinkCloud(canvas,false);

	}

	private void setUpDrinkCloud(Canvas canvas, Boolean drink) {
		ArrayList<String[]> arrayList;
		if (drink)
			arrayList = nestArrayDrinkList;
		else
			arrayList = nestArrayNoDrinkList;

		Paint titlePaint = new Paint();
		titlePaint.setTextSize(30);
		titlePaint.setTextAlign(Align.CENTER);
		titlePaint.setColor(Color.rgb(0, 153, 204));
		if (drink)
			canvas.drawText("Drinking", canvas.getWidth() / 4, 50, titlePaint);
		else if (!drink)
			canvas.drawText("Non-Drinking", 3 * canvas.getWidth() / 4, 50,
					titlePaint);

		if (arrayList == null) {

			if (drink) {
				canvas.drawText("N/A", canvas.getWidth() / 4,
						canvas.getHeight() / 2, titlePaint);
			} else if (!drink) {
				canvas.drawText("N/A", 3 * canvas.getWidth() / 4,
						canvas.getHeight() / 2, titlePaint);
			}
		}

		if (arrayList != null) {

			for (int n = 0; n < arrayList.size(); n++) {
				word = arrayList.get(n)[0];
				count = Integer.valueOf(arrayList.get(n)[1]);

				Paint textPainter = new Paint();
				if (drink)
					textPainter.setColor(Color.rgb(247, 144, 30));
				else if (!drink)
					textPainter.setColor(Color.rgb(14, 109, 97));
				textPainter.setTextSize(20 * 1 + count * 2);
				textPainter.setTextAlign(Align.CENTER);
				Rect wordBound = new Rect();
				textPainter.getTextBounds(word, 0, word.length(), wordBound);
//<------------------------root of the branching
				if (n == 0) {
					if (drink) {
						canvas.drawText(word, canvas.getWidth() / 4,
								canvas.getHeight() - 50, textPainter);
						wordBound.set(
								(canvas.getWidth() / 4)
										- (wordBound.width() / 2),
								canvas.getHeight() - (wordBound.height()) - 50,
								(canvas.getWidth() / 4)
										+ (wordBound.width() / 2),
								canvas.getHeight() - 50);
					} else if (!drink) {
						canvas.drawText(word, 3 * canvas.getWidth() / 4,
								canvas.getHeight() - 50, textPainter);
						wordBound.set(
								3 * (canvas.getWidth() / 4)
										- (wordBound.width() / 2),
								canvas.getHeight() - (wordBound.height() - 50),
								3 * (canvas.getWidth() / 4)
										+ (wordBound.width() / 2),
								canvas.getHeight() - 50);

					}

				} else {
					boolean continueSearch = true;
					boolean intersection = false;
					while (continueSearch) {
						intersection = false;
						Random random = new Random();
						int randomNode = random
								.nextInt(wordRectList.size() - 0) + 0;
						int randomSide = random.nextInt(5 - 1) + 1;
						;
						// int randomSide = 1;
						Rect parentRect = wordRectList.get(randomNode);
						switch (randomSide) {
//<-------------------right side branching
						case 1:
							float parentRightSideX = parentRect.centerX()
									+ (parentRect.width() / 2)
									+ (wordBound.width() / 2);
							float parentRightSideY = parentRect.centerY();
							// checks if intersects anything
							wordBound.set(
									(int) parentRightSideX
											- (wordBound.width() / 2),
									(int) (parentRightSideY - wordBound
											.height()),
									(int) (parentRightSideX + (wordBound
											.width() / 2)),
									(int) (parentRightSideY));
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound,canvas,drink)) {
									canvas.drawText(word, parentRightSideX,
											parentRightSideY, textPainter);
									continueSearch = false;
								}

							}
							break;
//<-------------------Left side branching
						case 3:
							float parentLeftSideX = parentRect.exactCenterX()
									- (parentRect.width() / 2)
									- (wordBound.width() / 2);
							float parentLeftSideY = parentRect.exactCenterY();
							// checks if intersects anything
							wordBound
									.set((int) parentLeftSideX
											- (wordBound.width() / 2),
											(int) (parentLeftSideY - wordBound
													.height()),
											(int) (parentLeftSideX + (wordBound
													.width() / 2)),
											(int) (parentLeftSideY));
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound,canvas,drink)) {
									canvas.drawText(word, parentLeftSideX,
											parentLeftSideY, textPainter);
									continueSearch = false;
								}

							}
							break;
//<-------------------Top side branching
						case 4:
							float parentTopSideX = parentRect.exactCenterX();
							float parentTopSideY = parentRect.exactCenterY()
									- (parentRect.height() / 2);
							// checks if intersects anything
							wordBound
									.set((int) parentTopSideX
											- (wordBound.width() / 2),
											(int) (parentTopSideY - wordBound
													.height()),
											(int) (parentTopSideX + (wordBound
													.width() / 2)),
											(int) (parentTopSideY));
							intersection = false;
							for (int i = 0; i < wordRectList.size(); i++) {
								if (Rect.intersects(wordBound,
										wordRectList.get(i))) {
									intersection = true;
								}
							}
							if (!intersection) {
								if (checkBoundary(wordBound,canvas,drink)) {
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

	private Boolean checkBoundary(Rect bound, Canvas canvas,Boolean drink) {
		if (drink){
			if (bound.left > 0 && bound.right < canvas.getWidth() / 2
					&& bound.top > 0 && bound.bottom < canvas.getHeight()) {
				return true;
			} else {
				return false;
			}
		}
		else{
			if (bound.left > canvas.getWidth() / 2
					&& bound.right < canvas.getWidth()
					&& bound.top > 0
					&& bound.bottom < canvas
							.getHeight()) {
				return true;
			}else{
				return false;
			}
		}
	}
}
