
const express = require('express');
const app = express();
var path = require('path');
var admin = require('firebase-admin');
// var serviceAccount = require(path.resolve( __dirname, "./firebase-credentials.json") );
var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const router=require(path.resolve( __dirname, "./routes"));
app.use('/',router);
app.listen(3000);