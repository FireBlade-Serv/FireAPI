package fr.glowstoner.fireapi.utils.calendar;

import java.io.Serializable;

import fr.glowstoner.fireapi.utils.calendar.enums.FireCalendarFormat;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FireCalendar implements Serializable{
	
	private static final long serialVersionUID = -3263231749761556128L;
	
	private int dayOfMonth, month, hourOfDay, second, minute, milisecond, year;
	
	public int get(FireCalendarFormat format) {
		switch (format) {
			case DAY_OF_MOUTH:
				return this.dayOfMonth;
			case HOUR_OF_DAY:
				return this.hourOfDay;
			case MILISECOND:
				return this.milisecond;
			case MINUTE:
				return this.minute;
			case MOUTH:
				return this.month;
			case SECOND:
				return this.second;
			case YEAR:
				return this.year;
		}
		
		return -1;
	}
}