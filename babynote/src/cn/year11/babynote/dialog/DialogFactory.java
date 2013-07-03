package cn.year11.babynote.dialog;

import java.util.HashMap;

import android.content.Context;

import cn.year11.babynote.provider.event.DiaperEvent;
import cn.year11.babynote.provider.event.DosingEvent;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.FeedingEvent;
import cn.year11.babynote.provider.event.GrowthEvent;
import cn.year11.babynote.provider.event.HealthEvent;
import cn.year11.babynote.provider.event.VaccineEvent;

public class DialogFactory {

	private static HashMap<String, Class<?>> mDialogs = new HashMap<String, Class<?>>();
	
	static {
		mDialogs.put(DiaperEvent.TYPE, DiaperDialog.class);
		mDialogs.put(DosingEvent.TYPE, DoseDialog.class);
		mDialogs.put(FeedingEvent.TYPE, FeedingDialog.class);
		mDialogs.put(GrowthEvent.TYPE, GrowthDialog.class);
		mDialogs.put(HealthEvent.TYPE, HealthDialog.class);
		mDialogs.put(VaccineEvent.TYPE, VaccineDialog.class);
		
	}
	
	public static void showEventDialog(Context c, Event event)
	{
		EventDialog d;
		if (event.getEventType().equals(DosingEvent.TYPE)) {
			d = new DoseDialog(c, event);
		}
		else if (event.getEventType().equals(HealthEvent.TYPE)) {
			d =new HealthDialog(c, event);
		}
		else if (event.getEventType().equals(DiaperEvent.TYPE)) {
			d =new DiaperDialog(c, event);
		}
		else if (event.getEventType().equals(FeedingEvent.TYPE)) {
			d =new FeedingDialog(c, event);
		}
		else if (event.getEventType().equals(VaccineEvent.TYPE)) {
			d =new VaccineDialog(c, event);
		}
		else if (event.getEventType().equals(GrowthEvent.TYPE)) {
			d =new GrowthDialog(c, event);
		}
		else {
			return;
		}
		
		d.show();
	}
}
