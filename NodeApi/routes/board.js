const router = require('express').Router();
const {boardController} = require('../controllers');

router.get('/all', boardController.getAllBoards);
router.get('/', boardController.getBoard);
router.post('/',boardController.getBoard);
router.post('/', boardController.createBoard);
router.put('/', boardController.updateBoard);
router.delete('/', boardController.deleteBoard);

module.exports = router;

