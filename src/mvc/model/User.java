package mvc.model;

import java.sql.Timestamp;

public class User {
	private Integer user_id;
	private String username;
	private String senha;
	private byte foto[];
	private Timestamp last_session;
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public Timestamp getLast_session() {
		return last_session;
	}
	public void setLast_session(Timestamp last_session) {
		this.last_session = last_session;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	private boolean is_active;
	
	public boolean isSessionActive() {
		long now = System.currentTimeMillis();
		if(last_session.getTime() + (2 * 60 * 1000) > now) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
