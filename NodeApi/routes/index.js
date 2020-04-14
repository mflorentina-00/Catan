const router = require('express').Router();
const extension = require('./extension');
router.use('/extension',extension);

module.exports=router;