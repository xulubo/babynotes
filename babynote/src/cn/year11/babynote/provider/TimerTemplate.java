package cn.year11.babynote.provider;

public class TimerTemplate {

	private long id;
	private String title;
	private String description;
	private String type;
	private int icon_resource_id;
	private long duration;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getIcon_resource_id() {
		return icon_resource_id;
	}
	public void setIcon_resource_id(int icon_resource_id) {
		this.icon_resource_id = icon_resource_id;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
}
