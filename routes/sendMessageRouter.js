global.atob = require("atob");

const express = require("express");
const bodyParser = require("body-parser");

const sendMessageRouter = express.Router();

sendMessageRouter.route('/')
.all((req, res, next) => {
    res.statusCode = 200;
    res.setHeader('Content-Type', 'text/plain');
    console.log('in sendMessageRouter.all');
    next();
})
    .post(function (req, res, next) {
    // var item = {
    //     'comment': {'S': res.body.comment},
    //     'name': {'S': res.body.name},
    //     'email': {'S': res.body.email},
    //     'website': {'S': res.body.website}
    // }
    // console.log('comment:' + item.comment + ' name:' + item.name + ' email:' + item.email + ' website:' + item.website);
        console.log(req.body);

        var config = require("../config.json");
        var pwd = atob(config.gmail_pwd);
        // console.log("read from config json file:" + pwd);

        var nodemailer = require('nodemailer');
        var transporter = nodemailer.createTransport("SMTP", {
            service: 'gmail',
            auth: {
                user: 'funsbayarea@gmail.com',
                pass: pwd,
                tls: true
            }
        });

        var mailOptions = {
            from: 'funsbayarea@gmail.com',
            to: 'funsbayarea@gmail.com',
            subject: 'Sending user feedback using Node.js',
            text: JSON.stringify(req.body)
        };

        transporter.sendMail(mailOptions, function(error, info){
            if (error) {
                console.log(error);
            } else {
                console.log('Email sent: ' + info.response);
            }
        });

        res.end('Done!');
    });

module.exports = sendMessageRouter;