var path = require('path');
var bodyParser = require("body-parser");

const boards = [{
    lobbyid: 1,
    board: [ {
        "resource" : "Grain",
        "number" : 5
      }, {
        "resource" : "Grain",
        "number" : 8
      }, {
        "resource" : "Brick",
        "number" : 4
      }, {
        "resource" : "Ore",
        "number" : 10
      }, {
        "resource" : "Wool",
        "number" : 11
      }, {
        "resource" : "Ore",
        "number" : 10
      }, {
        "resource" : "Wool",
        "number" : 2
      }, {
        "resource" : "Lumber",
        "number" : 4
      }, {
        "resource" : "Grain",
        "number" : 3
      }, {
        "resource" : "Lumber",
        "number" : 12
      }, {
        "resource" : "Grain",
        "number" : 3
      }, {
        "resource" : "Brick",
        "number" : 9
      }, {
        "resource" : "Lumber",
        "number" : 8
      }, {
        "resource" : "Wool",
        "number" : 5
      }, {
        "resource" : "Wool",
        "number" : 6
      }, {
        "resource" : "Lumber",
        "number" : 6
      }, {
        "resource" : "Ore",
        "number" : 9
      }, {
        "resource" : "Desert",
        "number" : 0
      }, {
        "resource" : "Brick",
        "number" : 11
      } ]
},
{
   lobbyid: 2,
    board: { "field2" : "abc" , "field2" : "def" }
} ];
 

const getAllBoards = async(req, res) => {
    return res.status(200).json({boards: boards});
};

const getBoard = async(req, res) => {
    // Get Single Table
    console.log('Accepted Get Request');
    if(req.body.hasOwnProperty('lobbyid')) {
    
        console.log('Good lobby id');
        if(boards == null) {
            return res.status(400).json({status : 'error', message: 'No board available.'});
        }

        const found = boards.some(board => board.lobbyid === parseInt(req.body.lobbyid)); //daca exista sau nu

        if(found) {
            res.json( (boards.filter(board => board.lobbyid === parseInt(req.body.lobbyid)))[0].board );
        } else {
            res.status(400).json({status : 'error', message: `No board with the id of ${req.body.lobbyid}`});
        }
    } 
    else {
        console.log(req.body);
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
};

// Create Board
const createBoard = async(req, res) => {

    const newBoard = {
        lobbyid: parseInt(req.body.lobbyid),
        board: req.body.board
    }

    // lobbyid obligatoriu specificat
    if(!newBoard.lobbyid) {
        return res.status(400).json({status : 'error', message: 'Please include lobbyid'});
    }

    boards.push(newBoard);
    //console.log(tables);
    res.status(200).json({status : 'succes', message: 'Board successfully created'});
};

// Update Board
const updateBoard = async(req, res) => {

    if(req.body.hasOwnProperty('lobbyid')){
        const found = boards.some(board => board.lobbyid === parseInt(req.body.lobbyid));

        if(found) {
            const updatedBoard = req.body.board; // noul board
            boards.forEach(brd => {
                if(brd.lobbyid === parseInt(req.body.lobbyid)) {
                    brd.board = updatedBoard;
                    return res.status(200).json({status : 'succes', message: 'Board successfully updated', board: brd});
                }
            });
        }
        else {
            res.status(400).json({status : 'error', message: `No board with the id of ${req.body.lobbyid}`});
        }
    }
    else {
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
};

// Delete Board
const deleteBoard = async(req, res) => {

    
    if(req.body.hasOwnProperty('lobbyid')){
        const found = boards.some(board => board.lobbyid === parseInt(req.body.lobbyid));

        if(found) {
            boards.forEach(board => {
                if(board.lobbyid === parseInt(req.body.lobbyid)) {
                    let pos = boards.indexOf(board)
                    boards.splice(pos, 1);
                    return res.status(200).json({status : 'succes', message: 'Board successfully deleted'});
                }
            });
        } else {
            res.status(400).json({status : 'error', message: `No board with the id of ${req.body.lobbyid}`});
        }

    }
    else {
        res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing lobbyid.'});
    }
    
};

module.exports={
    getBoard, createBoard, updateBoard, deleteBoard, getAllBoards
}