const router=require('express').Router();
const {extensionController}=require('../controllers');
router.get('/getExtensionByName',extensionController.getExtensionByName);
router.put('/insertExtension',extensionController.insertExtension);
module.exports=router;

