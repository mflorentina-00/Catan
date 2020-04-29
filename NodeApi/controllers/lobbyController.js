var db = require('./firebaseService').getDb();
var ref = db.ref("lobbies");
var path = require('path');
const axios = require('axios');
var all_lobbies = [];

ref.on("value", function(snapshot) {
	all_lobbies = [];
	snapshot.forEach(function(child) {
		all_lobbies.push({  
			extension: child.val().extension,
			first: child.val().first,
			gameid: child.val().gameid,
			master: child.val().master,
			second: child.val().second,
			third: child.val().third,
			lobbyid : child.key
		});
  	});
});
	

const getLobbies = async(req, res) => {
	res.send(all_lobbies);
}



function get_user_extension(username) {
    return db.ref('/users/'+username+'/extension')
    .once('value')
    .then(function(bref) {
        var extension= bref.val();
        return {
            extension: extension
        };
    });
}

function get_lobby_extension(lobbyid) {
    return db.ref('/lobbies/'+lobbyid+'/extension')
    .once('value')
    .then(function(bref) {
        var extension= bref.val();
        return {
            extension: extension
        };
    });
}

function get_game_id(lobbyid) {
	return db.ref('/lobbies/'+lobbyid+'/gameid')
	.once('value')
	.then(function(data) {
		var game_id = data.val();
		return {
			gameid: game_id
		}
	})
}

var lobbies = db.ref("lobbies");

const addLobby = async(req,resp) => {
	var create_game_data = {
	    "username": "catan",
		"password": "catan",
		"command": "newGame",
		"arguments": "{\"scenario\": \"SettlersOfCatan\"}" // sper ca nimeni sa nu faca asa ceva. OMG STRING INSTEAD OF JSON WOOOOOT NICELY DONE
	};
	axios
	  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', create_game_data)
	  .then(res => {	
	   	if(res.data.code == 100)	{
	   		var game_id = res.data.data.slice(11, res.data.data.length - 2);
		   	var new_lobby = lobbies.push();
		   	var username = req.body.username;
		   	get_user_extension(req.body.username).then(function(data) {
		   	new_lobby.set({
		   			first : "-",
		   			second: "-",
		   			third: "-",
		   			master : username,
		   			extension : data.extension,
		   			gameid : game_id
		   		});
		   	});
		   
		   	resp.send(`{"lobbyid" : "${new_lobby.key}", "gameid" : "${game_id}"}`);	

		   	var set_maxplayers_data = {
			    "username": "catan",
				"password": "catan",
				"command": "setMaxPlayers",
   				"arguments": "{\"gameId\":\""+game_id+"\",\"maxPlayers\":\"3\"}" 
			};

			axios
				  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', set_maxplayers_data)
				  .then(res => {	
				   	if(res.data.code == 100)	{
				   		console.log(res.data);
				   	}
				   	else
				   		console.log(res.data);
				  })
				  .catch(error => {
				   	console.error(error);
				  }); 
			request_to_ge(game_id);
	   	}
	   	else
	   		console.log(res);
	  })
	  .catch(error => {
	    console.error(error)
	  }) 
}

function sleep(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
} 




async function request_to_ge(game_id) {
	await sleep(500);
	var add_player_data = {
			    "username": "catan",
			    "password": "catan",
			    "command": "addPlayer",
			    "arguments": "{\"gameId\":\"" + game_id + "\"}"
			};
			
			axios
				  .post('https://catan-engine.herokuapp.com/Catan/managerRequest/', add_player_data)
				  .then(res => {	
				   	if(res.data.code == 100)	{
				   		console.log(res.data);
				   	}
				   	else
				   		console.log(res.data);
				  })
				  .catch(error => {
				   	console.error(error);
				  });
}

const joinLobby = async(req, res) => {
	var lobby_id = req.body.lobbyid;
	var username = req.body.username;
	var lobby = db.ref("lobbies").child(lobby_id);
	get_user_extension(username).then(function(data) {
		if(data.extension == all_lobbies[lobby_id].extension) {
			if(all_lobbies[lobby_id].first === '-' || all_lobbies[lobby_id].second === '-' || all_lobbies[lobby_id].third === '-') {
				if(all_lobbies[lobby_id].first === '-') {
					lobby.update({
						"first":username
					});
					res.send(`{"place" : "1", "error" : "-"}`);
					return;
				}
				else if(all_lobbies[lobby_id].second === '-'){
					lobby.update({
						"second":username
					});
					res.send(`{"place" : "2", "error" : "-"}`);
					return;
				}
				else {
					lobby.update({
						"third":username
					});
					res.send(`{"place" : "3", "error" : "-"}`);
					return;
				}
			}
			else {
				res.send(`{"place" : "-", "error" : "noplace"}`);
				return;
			}
		}
		res.send(`{"place" : "-", "error" : "diff ext"}`);
		return;
	});
}

const leaveLobby = async(req,res) => {
	var lobby_id = req.body.lobbyid;
	var username = req.body.username;
	var lobby = db.ref("lobbies").child(lobby_id);
	
	if(all_lobbies[lobby_id].master === username) {
		lobby.set(JSON.parse(`{"${lobby_id}": null}`));
	}
	else {
		if(all_lobbies[lobby_id].first === username) {
			lobby.update({
				"first":"-"
			});
		}
		else if(all_lobbies[lobby_id].second === username) {
			lobby.update({
				"second":"-"
			});
		}
		else {
			lobby.update({
				"third":"-"
			});
		}
	}
	res.send("done");
}


const startGame = async(req, response) => {

	await sleep(1000);

	var game_id = req.body.gameid;
	var add_player_data = {
			    "username": "catan",
			    "password": "catan",
			    "command": "startGame",
    			"arguments": "{\"gameId\":\""+ game_id +"\"}"
	};
	axios
		.post('https://catan-engine.herokuapp.com/Catan/managerRequest/', add_player_data)
		.then(res => {	
			if(res.data.code == 100)	{
				console.log(res.data);
				boards = db.ref('boards');
				boards.child(game_id).set({
					data : res.data.data
				});
				response.send({"ports" : ["None", 
"None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","Grain","Grain","None","ThreeForOne","ThreeForOne","None","None","Lumber","Lumber","None","Brick","Brick","None","None","ThreeForOne","ThreeForOne","None","ThreeForOne","ThreeForOne","None","Ore","Ore","None","ThreeForOne","ThreeForOne","None","Wool","Wool","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None","None"],


"board":[{"resource":"Ore","number":5},{"resource":"Lumber","number":3},{"resource":"Wool","number":12},{"resource":"Grain","number":2},{"resource":"Grain","number":8},{"resource":"Wool","number":4},{"resource":"Grain","number":6},{"resource":"Wool","number":3},{"resource":"Desert","number":0},{"resource":"Brick","number":10},{"resource":"Lumber","number":8},{"resource":"Brick","number":5},{"resource":"Lumber","number":11},{"resource":"Lumber","number":11},{"resource":"Grain","number":9},{"resource":"Ore","number":9},{"resource":"Brick","number":4},{"resource":"Ore","number":6},{"resource":"Wool","number":10}]});
		}
			else{
				console.log(res.data);
				response.send(res);
			}
		})
		.catch(error => {
		 	console.error(error);
		 	response.send(error);
		});
}

module.exports={
    getLobbies, addLobby, joinLobby, leaveLobby, startGame
}