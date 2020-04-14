var path = require('path');
var admin = require('firebase-admin');
var serviceAccount = require(path.resolve( __dirname, "./firebase-credentials.json") );
var bodyParser = require("body-parser");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://catan-f1c9a.firebaseio.com/'
});
var db = admin.database();
var ref = db.ref("extensions");
ref.once("value", function(snapshot) {
  console.log(snapshot.val());
});

const getExtensionByName=async(req,res)=>{
    const queryName = req.query.name;
      console.log("HTTP Get Request");
    	var userReference = db.ref("/extensions/");

      ref.orderByKey().equalTo(queryName).on("child_added", (snapshot)=> {
          res.json(snapshot.val());
    });

}
const insertExtension=async(req,res)=>{
    console.log("HTTP Put Request");

  var name = req.body.name;
  var available=req.body.available
	// var id = Math.random().toString(36).substring(7);
	var referencePath = '/extensions/';
	var userReference = db.ref(referencePath);
	userReference.set({name:{available:available,description:''}}, 
				 function(error) {
					if (error) {
						res.json({status:'error',message:error});
					} 
					else {
						res.json({status:'succes',message:''});
					}
			});
}

module.exports={
    getExtensionByName,
    insertExtension
}