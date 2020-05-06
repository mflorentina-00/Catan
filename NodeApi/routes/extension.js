const router=require('express').Router();
const {extensionController}=require('../controllers');
const validateToken=require('./validateToken')
router.get('/getExtensionByName',validateToken,extensionController.getExtensionByName);
router.put('/insertExtension',extensionController.insertExtension);
module.exports=router;

