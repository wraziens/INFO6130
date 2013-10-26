import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseStore {
	public String value;
	public String variable;
	public Date date;
	public String type;
	public Integer day;
	public Integer month;
	public Integer year;
	public String time;
	public String day_week;
	//Should not be explicitly used but rather should use the below static methods
	//when creating a DatabaseStore to preserve consistency among the Question Types
	public DatabaseStore(String variable, String value, 
			Date date, String ques_type){
		this.type = ques_type;
		this.value = value;
		this.date = date;
		this.variable=variable;
		
		//for easy access, convert the date attributes
		SimpleDateFormat day_ft = new SimpleDateFormat("dd", Locale.US);
		SimpleDateFormat month_ft = new SimpleDateFormat("MM", Locale.US);
		SimpleDateFormat year_ft = new SimpleDateFormat("yyyy", Locale.US);
		SimpleDateFormat day_week_ft = new SimpleDateFormat("E", Locale.US);
		SimpleDateFormat time_ft = new SimpleDateFormat("HH:mm:ss", Locale.US);
		
		this.day = Integer.parseInt(day_ft.format(date));
		this.month = Integer.parseInt(month_ft.format(date));
		this.year = Integer.parseInt(year_ft.format(date));
		this.day_week = day_week_ft.format(date);
		this.time = time_ft.format(date);
	}
	
	public static DatabaseStore DatabaseSliderStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Slider" );
	}
	
	public static DatabaseStore DatabaseTextStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Text" );
	}	
	
	public static DatabaseStore DatabaseIntegerStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Integer" );
	}
	
	public static DatabaseStore DatabaseDoubleStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Double" );
	}
	
	public static DatabaseStore DatabaseMultichoiceStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Multi" );
	}
	
	public static DatabaseStore DatabaseRadioStore(String variable, String value, Date date){
		return new DatabaseStore(variable, value, date, "Radio" );
	}
}
