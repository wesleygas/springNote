package mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import mvc.model.DAO;
import mvc.model.Note;




@Controller
public class NotaController {

	@PostMapping("/note")
	@ResponseBody
	public String addNote(@RequestBody MultiValueMap<String, String> parameters) {
		DAO dao = new DAO();
		Note note = new Note();
		note.setUser_id(Integer.parseInt(parameters.getFirst("user_id")));
		note.setTitle(parameters.getFirst("title"));
		note.setBody(parameters.getFirst("body"));
		note.setTema_id(1);
		if (note.getTitle().length() > 0 && note.getBody().length() > 0) {
			dao.addNoteToUser(note);
			return "adicionada";
		} else {
			System.out.print("Notas Vazias");
			return "nota vazia";
		}
		
	}

	@DeleteMapping("/note")
	@ResponseBody
	public String deleteNote(@RequestBody MultiValueMap<String, String> parameters) {
		Note note = new Note();
		DAO dao = new DAO();
		note.setNote_id(Integer.parseInt(parameters.getFirst("note_id")));
		dao.deleteNote(note);
		return "deletada";
	}
	
	@PutMapping("/note")
	@ResponseBody
	public String updateNote(@RequestBody MultiValueMap<String, String> parameters) {
		DAO dao = new DAO();
		Note note = new Note();
		String note_id = parameters.getFirst("note_id");
		String titulo = parameters.getFirst("title");
		note.setNote_id(Integer.parseInt(note_id));
		note.setTitle(titulo);
		note.setBody(parameters.getFirst("body"));
		if (note.getTitle().length() > 0 && note.getBody().length() > 0) {
			dao.editNote(note);
			return "daqui a pouco timestamp";
		} else {
			System.out.print("Notas Vazias");
			return "att vazia";
		}
	}
}
