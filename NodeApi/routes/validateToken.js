var jwt=require('jsonwebtoken');
require('dotenv').config();

module.exports=async (req,res,next)=>{
    let token=req.header['x-access-token']||req.headers['authorization'];
    if(!token)
    {
        res.status(403).json({status:'error',message:'Missing access token from HTTP Headers'});
    }
    else
    {
        if(token.startsWith('Bearer'))
        {
            token=token.slice(7,token.length);
        }

        jwt.verify(token,process.env.ACCESS_TOKEN_SECRET,(err,decode)=>{
            if(err)
            {
                return res.status(401).json({
                    status:'error',
                    message:'Provided access token is invalid'
                });
            }
            else 
            {
                next();
            }
        })
    }
}
