const express = require('express');
const bodyParser = require('body-parser')


const app = express();
app.use(bodyParser.json());

//app.use(app.router);

app.post('/', (req, res) => {

    console.dir(req);

    console.dir(req.body);

    res.send('Hello World!')
});

app.listen(4000, () => console.log('Example app listening on port 4000!'))
