const express = require('express');
const bodyParser = require('body-parser');
const colors = require('colors');

var jwt = require('jsonwebtoken');

require('dotenv').config({path: './.env'})
const app = express();
const port = 8081;

const tokenHeaderName = process.env.TOKEN_HEADER_NAME;
const tokenPrefix = process.env.TOKEN_PREFIX;
const jwt_key = process.env.JWT_KEY;


app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

app.use(function (req, res, next) {
	console.log('--------Message from user service--------');
	
	const JWT = req.headers[tokenHeaderName].replace(tokenPrefix, '').trim();
	console.log('Verify JWT:', JWT);
	try {
	  let decoded = jwt.verify(JWT, Buffer.from(jwt_key, 'base64'), {algorithms: ["HS256"]});
	  console.log('Valid JWT, access permitted!'.green)
	  console.log("decoded JWT: ", decoded);
	  next();
	} catch(err) {
	  // err
	  console.log(err)
	  console.log('Invalid JWT, access denied!'.red)
	  res.status('401').send('Invalid JWT');
	}
});

app.get('/user', (req, res) => {
  res.send('this message came from user service.');
});

app.listen(port, () => {
  console.log(`user service listening at http://localhost:${port}`);
});