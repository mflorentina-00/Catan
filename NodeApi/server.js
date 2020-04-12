const http = require('http')
const express=require('express');
const port = 3000;
const app = express();
app.get('/',(request, response)=>{
    response.send("Hello word");
})
app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`))

