'use strict';

const PORT = process.env.PORT || 8888;
const express = require('express');
const http = require('http');
const app = express();

const smsRoute = require('./routes/sms');

app.use((req, res, next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.put('/api/v1/sms', smsRoute.put);

http.createServer(app).listen(PORT, () => {
    console.info('mock listening on port ' + PORT);
});
