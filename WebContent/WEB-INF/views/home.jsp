<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon"
	href=".\WebContent\Resources\img\favicon.ico.png" type="image/x-icon">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/css/materialize.min.css">
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initials-scale=1.0" />
<title>Color Note</title>
</head>
<body>
	<%@ page import="java.util.*,mvc.model.*"%>
	<%	
				List<Note> notas = (List<Note>) session.getAttribute("listNotes");
				User user = (User) session.getAttribute("user");
			%>
	<!-- Dropdown Structure -->
	<ul id="dropdown1" class="dropdown-content">
		<li><a href="./UserOptions">Minha Conta</a></li>
		<li><a href="./">Sair</a></li>
	</ul>

	<nav class="nav-extended">
		<div class="nav-wrapper">
			<a href="#!" class="brand-logo center"><i class="material-icons">cloud</i>Color
				Note</a>
			<ul class="left hide-small-only">
				<li><a href="#!" class="dropdown-trigger"
					data-target="dropdown1"><i class="material-icons">more_vert</i></a></li>

			</ul>
		</div>
		<div class="nav-content">
			<a
				class="btn-floating btn-large halfway-fab waves-effect waves-light teal modal-trigger"
				href="#modal_note" onclick="modalAdd()"> <i class="material-icons">add</i>

			</a>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			
			<div class="col s12 m6">
				<div class="card blue-grey darken-1 hoverable">
					<div class="card-content white-text">
						<span class="card-title" id="tl">Todo dia uma citação diferente!</span>
						<p id="bd">With some everyday quotes and forecast</p>
					</div>
					<div class="card-action">
					</div>
				</div>
			</div>
		
			<%
				if (notas != null) {
					for (Note nota : notas) {
			%>
			<div class="col s12 m6">
				<div class="card blue-grey darken-1 hoverable"
					onclick="selectNote(<%=nota.getNote_id()%>)">
					<div class="card-content white-text">
						<span class="card-title" id="tl<%=nota.getNote_id()%>"><%=nota.getTitle()%></span>
						<p id="bd<%=nota.getNote_id()%>"><%=nota.getBody()%></p>
					</div>
					<div class="card-action">
							<button onclick="deleteNote(<%=nota.getNote_id()%>)" class="waves-effect waves-light btn-small">
								<i class="material-icons right">delete</i> Delete
							</button>
						<a class="btn-flat disabled">Last Edit: <%=nota.getLast_edit()%></a>
					</div>
				</div>
			</div>
			<%
				}
				} else {
			%>
			<div class="valign-wrapper center-align">
				<h5>Nenhuma nota foi encontrada :/</h5>
			</div>
			<%
				}
			%>
		</div>
	</div>


	<div id="modal_note" class="modal">
			<div class="modal-content">
				<div class="input-field">
					<span class="teal-text text-lighten-2">Titulo</span>
					<input type="text" id="mtitle" name="mtitle">
				</div>
				<br>
				<div class="input-field">
					<span class="teal-text text-lighten-2">Corpo</span>
					<textarea id="mbody" name="mbody" class="materialize-textarea"></textarea>
					<input id="Maction" type="hidden" name="action"
						value="edit">
					<input type="hidden" id="mnote_id"
						name="note_id" value="">
				</div>
			</div>
			<div class="modal-footer">
				<button onclick="noteHandler(<%=user.getUser_id()%>)"
					class="modal-close waves-effect waves-red btn red lighten-1">Salvar</button>
			</div>
	</div>


	<script type="text/javascript"
		src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-rc.2/js/materialize.min.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".dropdown-trigger").dropdown();
			$('.sidenav').sidenav();
			$('#modal_note').modal();
			$.ajax({
				type: 'GET',
				url : 'http://api.openweathermap.org/data/2.5/weather?id=3448439&appid=9f1f5601018bb5eb47dd1893e63d019f',
				success : function(data, status, xhr){
					clima = data
					getQuote(clima);
				},
				error: function(xhr, status, error){alert(error);}		
			});
		});
		function getQuote(weather){
			$.ajax({
				type: 'GET',
				url : 'https://quotes.rest/qod',
				success : function(data, status, xhr){
					citacao = data.contents.quotes[0];
					fillDailyCard(weather,citacao);
				},
				error: function(xhr, status, error){alert(error);}		
			});
		}
		function selectNote(noteId) {
			var Modalelem = document.querySelector('#modal_note');
            var instance = M.Modal.init(Modalelem);
            instance.open();
			//console.log("You clicked note: ", noteId);
			title = $('#tl' + noteId).text();
			body = $('#bd' + noteId).text();
			//console.log("Titulo:",title,"Corpo:",body);
			$('#mtitle').val(title);
			$('#mnote_id').val(noteId);
			$('#mbody').text(body);
			$('#Maction').val("edit");
			
		}
		function modalAdd(){
			$('#Maction').val("add");
			$('#mtitle').val("");
			$('#mbody').val("");
			
		}
		function noteHandler(userId){
			
			var action = $('#Maction').val();
			var title = $('#mtitle').val();
			var note_id = $('#mnote_id').val();
			var body = $('#mbody').val();	
			var method = "";
			if(action == "edit"){
				method = "PUT";
			}else if(action == "add"){
				method = "POST";
			}
			$.ajax({
				type: method,
				url : './note',
				contentType : 'application/x-www-form-urlencoded',
				data : "user_id=" + userId + '&note_id=' + note_id + '&title=' + title + '&body=' + body,
				success : function(data, status, xhr){
					if(method == "PUT"){
					$('#tl' + note_id).text(title);
					$('#bd' + note_id).text(body);
					}
				},
				error: function(xhr, status, error){alert(error);}		
			});
		}
		
		function deleteNote(noteId){
			var Modalelem = document.querySelector('#modal_note');
			var instance = M.Modal.init(Modalelem);
            
			$.ajax({
				type: 'DELETE',
				url : './note',
				contentType : 'application/x-www-form-urlencoded',
				data : "note_id=" + noteId,
				success : function(data, status, xhr){console.log("deleted")},
				error: function(xhr, status, error){}		
			});
			instance.close();
		}
		
		function fillDailyCard(clima, citacao){
			
			$('#tl').text("Todo dia uma citação diferente!");
			var textao = clima.name + ", " + clima.main.temp/10 + "ºC  "
			textao += ", " + clima.weather[0].main + "      \"" + citacao.quote + "\" - " + citacao.author;
			$('#bd').text(textao);
		}
	</script>
</body>
</html>