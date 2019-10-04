// Include the cluster module
var cluster = require('cluster');

// Code to run if we're in the master process
if (cluster.isMaster) {

    // Count the machine's CPUs
    var cpuCount = require('os').cpus().length;

    // Create a worker for each CPU
    for (var i = 0; i < cpuCount; i += 1) {
        cluster.fork();
    }

    // Listen for terminating workers
    cluster.on('exit', function (worker) {

        // Replace the terminated workers
        console.log('Worker ' + worker.id + ' died :(');
        cluster.fork();

    });

// Code to run if we're in a worker process
} else {
    var AWS = require('aws-sdk');
    var express = require('express');
    var bodyParser = require('body-parser');
    var commentRouter = require('./routes/commentRouter');
    var sendMessageRouter = require('./routes/sendMessageRouter');

    AWS.config.region = process.env.REGION

    var sns = new AWS.SNS();
    var ddb = new AWS.DynamoDB();

    var ddbTable =  process.env.STARTUP_SIGNUP_TABLE;
    var snsTopic =  process.env.NEW_SIGNUP_TOPIC;
    var app = express();

    app.set('view engine', 'ejs');
    app.set('views', __dirname + '/views');
    app.use(bodyParser.urlencoded({extended:false}));
    // app.use(express.static('static'));
    var path = require('path');
    app.use(express.static(path.join(__dirname, 'static')));
    app.use(bodyParser.json());

    // [ mongo db
    // const MongoClient = require('mongodb').MongoClient;
    // const assert = require('assert');
    // var config = require("./config.json");
    // var mongo_pwd = config.mongo_pwd;
    // const url = 'mongodb://admin:'+mongo_pwd+'@daidaius-shard-00-00-5xslt.gcp.mongodb.net:27017,daidaius-shard-00-01-5xslt.gcp.mongodb.net:27017,daidaius-shard-00-02-5xslt.gcp.mongodb.net:27017/test?ssl=true&replicaSet=daidaius-shard-0&authSource=admin&retryWrites=true'
    // const dbname = "bayareafuns";
    // MongoClient.connect(url, (err, client) => {
    //     assert.equal(err, null);
    //     console.log('Connected correctly to the server');
    //     const db = client.db(dbname);
    //     const collection = db.collection('article');
    //     collection.find({}).toArray((err, docs) => {
    //         assert.equal(err, null);
    //         console.log('Found:\n');
    //         console.log(docs);
    //
    //         client.close();
    //     })
    // });
    // ]

    app.use('/comment', commentRouter);
    app.use('/sendmessage', sendMessageRouter);
    app.get('/', function(req, res) {
        res.render('index', {
            static_path: 'static',
            theme: process.env.THEME || 'flatly',
            flask_debug: process.env.FLASK_DEBUG || 'false'
        });
    });

    app.post('/signup', function(req, res) {
        var item = {
            'email': {'S': req.body.email},
            'name': {'S': req.body.name},
            'preview': {'S': req.body.previewAccess},
            'theme': {'S': req.body.theme}
        };

        ddb.putItem({
            'TableName': ddbTable,
            'Item': item,
            'Expected': { email: { Exists: false } }        
        }, function(err, data) {
            if (err) {
                var returnStatus = 500;

                if (err.code === 'ConditionalCheckFailedException') {
                    returnStatus = 409;
                }

                res.status(returnStatus).end();
                console.log('DDB Error: ' + err);
            } else {
                sns.publish({
                    'Message': 'Name: ' + req.body.name + "\r\nEmail: " + req.body.email 
                                        + "\r\nPreviewAccess: " + req.body.previewAccess 
                                        + "\r\nTheme: " + req.body.theme,
                    'Subject': 'New user sign up!!!',
                    'TopicArn': snsTopic
                }, function(err, data) {
                    if (err) {
                        res.status(500).end();
                        console.log('SNS Error: ' + err);
                    } else {
                        res.status(201).end();
                    }
                });            
            }
        });
    });

    var port = process.env.PORT || 3000;

    var server = app.listen(port, function () {
        console.log('Server running at http://127.0.0.1:' + port + '/');
    });
}