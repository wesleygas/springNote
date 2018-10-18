package mvc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO {
	private Connection connection = null;

	public DAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/color_note?", "root", "root");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println(" ====== DAO: ERRO AO CONECTAR COM O MYSQL ==============");
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(" ====== DAO: ERRO AO FECHAR A CONEXAO COM O MYSQL ==============");
		}
	}

	public List<Note> getNotesFromUser(User user) {
		List<Note> notas = new ArrayList<Note>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM tb_note WHERE user_id =?");
			stmt.setInt(1, user.getUser_id());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Note note = new Note();
				note.setNote_id(rs.getInt("note_id"));
				note.setTitle(rs.getString("title"));
				note.setBody(rs.getString("body"));
				note.setImage(rs.getBytes("image"));
				note.setLast_edit(rs.getTimestamp("last_edit"));
				note.setUser_id(rs.getInt("user_id"));
				note.setTema_id(rs.getInt("tema_id"));
				notas.add(note);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(
					"DAO: ERRO AO PEGAR NOTAS DO USUARIO" + user.getUser_id().toString() + "DO BANCO DE DADOS");
		}
		return notas;
	}

	public List<Note> getAllNotes() {
		List<Note> notas = new ArrayList<Note>();
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM tb_note");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Note note = new Note();
				note.setNote_id(rs.getInt("note_id"));
				note.setTitle(rs.getString("title"));
				note.setBody(rs.getString("body"));
				note.setImage(rs.getBytes("image"));
				note.setLast_edit(rs.getTimestamp("last_edit"));
				note.setUser_id(rs.getInt("user_id"));
				note.setTema_id(rs.getInt("tema_id"));
				notas.add(note);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO PEGAR NOTAS DO BANCO DE DADOS");
		}
		return notas;
	}

	public Integer addNoteToUser(Note note) { 
		Integer error_code = 0;
		String sql = "INSERT INTO tb_note (title,body,image,last_edit,user_id,tema_id) values(?,?,?,?,?,?)";

		try {

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, note.getTitle());
			stmt.setString(2, note.getBody());
			stmt.setBytes(3, note.getImage());
			stmt.setTimestamp(4, note.getLast_edit());
			stmt.setInt(5, note.getUser_id());
			stmt.setInt(6, note.getTema_id());
			stmt.execute();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(" ====== DAO: ERRO AO ADICIONAR NOTA NO USUARIO DE ID: " + note.getUser_id() + "==============");
			error_code = 1;
		}
		return error_code;
	}

	public User getUserById(Integer userId) {
		User user = new User();
		int n_users = 0;
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM tb_user WHERE user_id=? and is_active=1");
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				n_users += 1;
				user.setUser_id(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setSenha(rs.getString("senha"));
				user.setFoto(rs.getBytes("foto"));
				user.setLast_session(rs.getTimestamp("last_session"));
				user.setIs_active(rs.getBoolean("is_active"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO PEGAR USER DO BANCO DE DADOS");
		}
		if (n_users > 1) {
			System.out.println("MAIS QUE UM USUARIO COM O MESMO ID!");
			return user;
		}
		return user;
	}

	public int addUser(User user) {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM tb_user WHERE username=?");
			stmt.setString(1, user.getUsername());
			ResultSet rs = stmt.executeQuery();
			if (!rs.isBeforeFirst()) {
				String sql = "INSERT INTO tb_user (username,senha,foto,last_session,is_active) values(?,?,?,?,?)";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, user.getUsername());
				stmt.setString(2, user.getSenha());
				stmt.setBytes(3, user.getFoto());
				stmt.setTimestamp(4, user.getLast_session());
				stmt.setBoolean(5, user.isIs_active());
				stmt.execute();
				stmt.close();

				return 1;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO ADICIONAR USER NO BANCO DE DADOS VIA USERNAME");
		}

		return 0;
	}

	public User getUserByName(String username) {
		User user = new User();
		int n_users = 0;
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("SELECT * FROM tb_user WHERE username=? and is_active=1");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				n_users += 1;
				user.setUser_id(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setSenha(rs.getString("senha"));
				user.setFoto(rs.getBytes("foto"));
				user.setLast_session(rs.getTimestamp("last_session"));
				user.setIs_active(rs.getBoolean("is_active"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO PEGAR USER DO BANCO DE DADOS");
		}
		if (n_users > 1) {
			System.out.println("MAIS QUE UM USUARIO COM O MESMO username!");
			return user;
		}
		return user;

	}

	public void editUserPassword(String password, User user) {
		String sql = "UPDATE tb_user SET password=? WHERE user_id=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setInt(2, user.getUser_id());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO EDITAR USER PASSWORD");
		}
	}

	public void editUserLastSession(User user) {
		String sql = "UPDATE tb_user SET last_session=? WHERE user_id=?";
		long time = System.currentTimeMillis();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setTimestamp(1, timestamp);
			stmt.setInt(2, user.getUser_id());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO MODIFICAR LAST_SESSION TIMESTAMP");
		}
	}

	public void deactivateUser(User user) {
		String sql = "UPDATE tb_user SET is_active=? WHERE user_id=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setBoolean(1, false);
			stmt.setInt(2, user.getUser_id());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO DESATIVAR USUARIO");
		}
	}

	public void editNote(Note note) {
		String sql = "UPDATE tb_note SET body=?, title=?, last_edit=? WHERE note_id=?";
		long time = System.currentTimeMillis();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, note.getBody());
			stmt.setString(2, note.getTitle());
			stmt.setTimestamp(3, timestamp);
			stmt.setInt(4, note.getNote_id());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO ATUALIZAR NOTA");
		}
	}
	
	public void deleteNote(Note note) {
		String sql = "DELETE FROM tb_note WHERE note_id=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, note.getNote_id());
			stmt.execute();
			stmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO DELETAR NOTAS DO USUARIO");
		}
		
	}

	public void changeUserPassword(User user, String new_password) {
		String sql = "UPDATE tb_user SET senha=? WHERE user_id=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, new_password);
			stmt.setInt(2, user.getUser_id());
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DAO: ERRO AO MUDAR SENHA DE USUARIO");
		}
		
	}

}
