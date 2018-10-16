package mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class NotaController {
	@RequestMapping("/")
	public String execute() {
		System.out.println("L�gica do MVC");
		return "info";
	}

	@RequestMapping("criaTarefa")
	public String form() {
		return "formTarefa";
	}

	@RequestMapping("adicionaTarefa")
	public String adiciona() {
		return "oi";
	}
}
