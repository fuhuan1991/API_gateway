const express = require('express');
const bodyParser = require('body-parser')
const multer = require('multer') // v1.0.5

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
	console.log('Auth:')
  	console.log(req.body);
  	const apiKey = req.body.api_key;
  	if (users.has(apiKey)) {
		res.send('success!');
  	} else {
  		res.status('401').send('illegal user');
  	}
  	
})

app.listen(port, () => {
  console.log(`Auth service listening at http://localhost:${port}`)
})