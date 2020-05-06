const router=require('express').Router();
const {lobbyController}=require('../controllers');

router.get('/all', lobbyController.getLobbies);
router.post('/add', lobbyController.addLobby);
router.post('/startgame', lobbyController.startGame);
router.post('/join', lobbyController.joinLobby);
router.post('/leave', lobbyController.leaveLobby);

module.exports=router;