const express = require('express')
const app = express()
const port = 8081

app.get('/user', (req, res) => {
  res.send('this message came from user service.')
})

app.listen(port, () => {
  console.log(`user service listening at http://localhost:${port}`)
})