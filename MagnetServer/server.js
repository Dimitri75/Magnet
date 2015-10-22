var express    = require('express');
var app        = express();
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;

// ROUTES FOR OUR API
var router = express.Router();

router.get('/test', function(req, res) {
    res.json({ message: 'It seems to be working' });   
});

router.get('/', function(req, res) {
    res.json({ message: 'Welcome to the MagnetServer api !' });   
});

// REGISTER OUR ROUTES
app.use('/api', router);

// START THE SERVER
app.listen(port);
console.log('MagnetServer listening on port : ' + port);