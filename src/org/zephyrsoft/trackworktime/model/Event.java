package org.zephyrsoft.trackworktime.model;

public class Event extends Base implements Comparable<Event> {
	private Integer id = null;
	private Integer week = null;
	private Integer task = null;
	private Integer type = null;
	private String time = null;
	private String text = null;
	
	public Event() {
		// do nothing
	}
	
	public Event(Integer id, Integer week, Integer task, Integer type, String time, String text) {
		this.id = id;
		this.week = week;
		this.task = task;
		this.type = type;
		this.time = time;
		this.text = text;
	}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getWeek() {
		return week;
	}
	
	public Integer getTask() {
		return task;
	}
	
	public Integer getType() {
		return type;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setWeek(Integer week) {
		this.week = week;
	}
	
	public void setTask(Integer task) {
		this.task = task;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public int compareTo(Event another) {
		return compare(getTime(), another.getTime(), compare(getId(), another.getId(), 0));
	}
	
	/**
	 * This is used e.g. by an ArrayAdapter in a ListView and it is also useful for debugging.
	 * 
	 * @see org.zephyrsoft.trackworktime.model.Base#toString()
	 */
	@Override
	public String toString() {
		return getTime() + " / " + getType() + " / " + getTask() + " - " + getText();
	}
	
}