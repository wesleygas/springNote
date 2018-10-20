package mvc.controller;


import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.model.DAO;
import mvc.model.User;

@Controller
public class UserController {
	@GetMapping("/")
	public String serve() {
		
		System.out.println("Serve Home Page");
		return "login.html";
	}
	
	@GetMapping("/UserOptions")
	public String optionsPage() {
		
		return "userOptions.jsp";
	}
	
	@PostMapping("/")
	public String loginSignup(HttpSession session,
			@RequestParam(value="username", required=true)  String username,
			@RequestParam(value="password", required=true)  String password,
			@RequestParam(value="signUp"  , required=false) String signUp) {
		
		DAO dao = new DAO();
		
		if(signUp != null) {
			User user = new User();
			user.setSenha(password);
			user.setUsername(username);
			user.setIs_active(true);
			int result = dao.addUser(user);
			if(result == 2) {
				System.out.println("USUARIO JA CADASTRADO");
				return "login.html";
			}
		}
		
		//Login Feito por post pra esconder as credenciais
		User user = dao.getUserByName(username);
		System.out.println("Serve Home Page");
		if(user.getUsername() != null && user.getSenha().equals(password)) {
			System.out.println("credenciado");
			session.setMaxInactiveInterval(1800);
			dao.editUserLastSession(user);
			session.setAttribute("user", user);
			session.setAttribute("listNotes", dao.getNotesFromUser(user));
			return "home.jsp";
		}else {
			//Usuario nao encontrado
			return "login.html";
		}
	}
	
	@PatchMapping("/")
	@ResponseBody
	public String patchUser(@RequestBody MultiValueMap<String, String> parameters) {
		System.out.println("Tapeando o usuario");
		Integer user_id = Integer.parseInt(parameters.getFirst("id"));
		String new_pass = parameters.getFirst("npass");
		DAO dao = new DAO();
		User user = dao.getUserById(user_id);
		System.out.println(new_pass);
		dao.changeUserPassword(user, new_pass);
		return "pass";
	}
	
	@DeleteMapping("/")
	@ResponseBody
	public String deactivateUser(@RequestBody MultiValueMap<String, String> parameters) {
		System.out.println("Desativando o usuario");
		Integer user_id = Integer.parseInt(parameters.getFirst("id"));
		DAO dao = new DAO();
		User user = dao.getUserById(user_id);
		dao.deactivateUser(user);
		return "pass";
	}
}
