const express = require('express');
const app = express();
const colors = require('colors');
const port = 8080;

app.get('/products', (req, res) => {
	console.log('--------Message from product service--------');
	console.log('Currently this end point does not require authorization, access permitted!'.green)
  res.send('this message came from product service.');
})

app.listen(port, () => {
  console.log(`product service listening at http://localhost:${port}`);
})