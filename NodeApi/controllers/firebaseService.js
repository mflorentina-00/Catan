var path = require('path');
var admin = require('firebase-admin');
var serviceAccount = require(path.resolve( __dirname, "./firebase-credentials.json") );
var bodyParser = require("body-parser");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: 'https://catan-f1c9a.firebaseio.com/'
  });
var db = admin.database();

getDb=function()
{
    return db;
}
  module.exports={
    getDb
  };