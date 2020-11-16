const express = require('express');
const bodyParser = require('body-parser')
const multer = require('multer') // v1.0.5
const colors = require('colors');

const app = express();
const port = 9000;
const users = new Set();
const upload = multer() // for parsing multipart/form-data

users.add('0001');
users.add('0002');
users.add('0003');

app.use(bodyParser.json()) // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })) // for parsing application/x-www-form-urlencoded

app.post('/auth', upload.array(), function (req, res) {
	
	const apiKey = req.body.api_key;

  console.log('--------Message from authentication service--------');
  console.log("User's api key is: " + apiKey);

	if (users.has(apiKey)) {
    console.log('Valid api key, authentication success!'.green);
    res.send('success!');
	} else {
    console.log('Invalid api key, authentication failed!'.red)
    res.status('401').send('illegal user');
	}
  	
})

app.listen(port, () => {
  console.log(`Auth service listening at http://localhost:${port}`)
})