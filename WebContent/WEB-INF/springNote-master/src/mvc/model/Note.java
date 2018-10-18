package mvc.model;

import java.sql.Timestamp;

public class Note {
	private Integer note_id;
	private String title;
	private String body;
	private byte image[];
	private Timestamp last_edit;
	private Integer user_id;
	private Integer tema_id;
	
	public Integer getNote_id() {
		return note_id;
	}
	public void setNote_id(Integer note_id) {
		this.note_id = note_id;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	
	public Timestamp getLast_edit() {
		return last_edit;
	}
	public void setLast_edit(Timestamp last_edit) {
		this.last_edit = last_edit;
	}
	
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
	
	public Integer getTema_id() {
		return tema_id;
	}
	public void setTema_id(Integer tema_id) {
		this.tema_id = tema_id;
	}
	
	
	
}
