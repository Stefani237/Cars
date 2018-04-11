package UI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.CarLog;
import UI.CarEvents.eventType;
import javafx.event.Event;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

/** 
 * This Car class implements information from the carEvents interface and it's manages all the race events. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class Car implements CarEvents {
	private int id;
	private int model_id;
	private CarLog log;
	private double speed;
	private Color color;
	private int wheelRadius;
	private String type;
	private String shape;
	private Map<eventType, ArrayList<EventHandler<Event>>> carHashMap;
	private Color colors[] = { Color.RED, Color.AQUA, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
			Color.VIOLET, Color.GRAY, Color.CORAL };

	public Car(int id, int model_id, CarLog log) {
		this.id = id;
		this.model_id = model_id;
		this.log = log;
		this.speed = 1;
		this.color = Color.RED;
		this.wheelRadius = 5;
		carHashMap = new HashMap<eventType, ArrayList<EventHandler<Event>>>();
		for (eventType et : eventType.values())
			carHashMap.put(et, new ArrayList<EventHandler<Event>>());
	}

	public int getId() {
		return id;
	}

	public int getModelId() {
		return model_id;
	}

	public void setModel_id(int model_id) {
		this.model_id = model_id;
	}

	public Color getColor() {
		return color;
	}

	public int getRadius() {
		return wheelRadius;
	}

	public double getSpeed() {
		return speed;
	}

	public void setColor(Color color, int raceCounter) {

		this.color = color;
		processEvent(eventType.COLOR, new ActionEvent());

	}

	public void setSpeed(double speed, int raceCounter) {
		this.speed = speed;
		processEvent(eventType.SPEED, new ActionEvent());
	}

	public void setRadius(int wheelRadius, int raceCounter) {
		this.wheelRadius = wheelRadius;
		processEvent(eventType.RADIUS, new ActionEvent());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		processEvent(eventType.TYPE, new ActionEvent());
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
		processEvent(eventType.SHAPE, new ActionEvent());
	}

	public synchronized void addEventHandler(EventHandler<Event> l, eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al == null)
			al = new ArrayList<EventHandler<Event>>();
		al.add(l);
		carHashMap.put(et, al);
	}

	public synchronized void removeEventHandler(EventHandler<Event> l, eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al != null && al.contains(l))
			al.remove(l);
		carHashMap.put(et, al);
	}

	/* gets the events and sends the event message to the log */

	private void processEvent(eventType et, Event e) {
		String msg;
		ArrayList<EventHandler<Event>> al;
		synchronized (this) {
			al = carHashMap.get(et);
			if (al == null)
				return;
		}
		msg = "CarRaceView" + (getModelId()) + " | car number: " + (getId() + 1) + " | actionCommand: "
				+ et.toString() + " | array size is: " + al.size();
		Platform.runLater(() -> {
			log.printMsg(msg);
		});

		for (int i = 0; i < al.size(); i++) {
			EventHandler<Event> handler = (EventHandler<Event>) al.get(i);
			handler.handle(e);
		}
	}
}