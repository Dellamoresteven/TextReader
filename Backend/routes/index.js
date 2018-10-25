var express = require('express');
var router = express.Router();

/* GET home page. */
router.post('/', function(req, res, next) {
   	const userQuery = {
   		test: "test"
   	};
   	res.json(userQuery);
});

module.exports = router;
