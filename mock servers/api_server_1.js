const express = require('express')
const app = express()
const port = 8080

app.get('/products', (req, res) => {
  res.send('this message came from products service.')
})

app.listen(port, () => {
  console.log(`product service listening at http://localhost:${port}`)
})