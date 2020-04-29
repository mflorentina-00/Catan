var db=require('./firebaseService').getDb();
var ref = db.ref("users");
const jwt=require('jsonwebtoken');
require('dotenv').config();
const crypto = require("crypto");

const login=async(req,res)=>
{
    const username=req.body.username;
    const password=req.body.password;
    const queryUsername=username;
    var user=null;

    const result=await ref.orderByKey().equalTo(queryUsername).on("value", (snapshot)=> {
        user=snapshot.val();
    });

    if(!user)
    {
       return res.status(400).json({status : 'error', message: 'User not found!'});
    }
    var userCred=Object.values(user)[0];
    if(userCred.userpassword!=password)
    {
        return res.status(400).json({status:'error',message:'Wrong password!'});
    }
    const accessToken=jwt.sign(userCred.userId,process.env.ACCESS_TOKEN_SECRET);
    return res.status(200).json({status:'success',message:'Logged in succesfully!',access_token:accessToken});
 
}

const register=async(req,res)=>
{
    const email = req.body.email;
    const username = req.body.username;
    const password = req.body.password;
    const confirmpassword = req.body.confirmpassword;
    var user=null;

    if(!req.body.hasOwnProperty('email')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing email.'});
    }
    else if(!req.body.hasOwnProperty('username')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing username.'});
    }
    else if(!req.body.hasOwnProperty('password')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing password.'});
    }
    else if(!req.body.hasOwnProperty('confirmpassword')){
        return res.status(400).json({status : 'error', message: 'Malformed JSON body. Missing confirmpassword.'});
    }
    else if(password != confirmpassword){
        return res.status(400).json({status : 'error', message: 'Confirmpassword doesn\'t match the password'});
    }

    const queryUsername=username;
    const result=await ref.orderByKey().equalTo(queryUsername).on("value", (snapshot)=> {
        user = snapshot.val();
    });


    if(!user)
    {   
        // putem adauga user-ul
        //...
        console.log("HTTP Register POST Request");


	    var referencePath = '/users/';
        var userReference = db.ref(referencePath);
        const id = crypto.randomBytes(16).toString("hex");
	    userReference.child(username).set(
            {
                id: id,
                email: email,
                extension: '',
                username: username,
                password: password
            }, 
                function(error) {
                    if (error) {
                        console.log(error);
                        res.json({status:'error', message: 'Server error, plase contact your server administrator.'});
                    } 
                    else {
                        res.status(200).json({status : 'success', message: 'User succesfully created!'});
                    }
                }
        );

    } else {
        return res.status(400).json({status : 'error', message: 'The username is not available!'});
    }
}
module.exports={
    login, register
}