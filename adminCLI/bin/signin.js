#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .usage("Usage: -n <name>")
 .option("n", { alias: "name", describe: "Your name", type: "string", demandOption: true })
 .option("s", { alias: "password", describe: "password", type: "string" })
 
 .argv;
 
 const requestBody = {
	username: `${options.name}`,
	password: `${options.password}`,
 }
console.log(requestBody);


// The url depends on searching or not
const url = "http://localhost:8080/api/auth/signin"
axios({method:'post', url: url, data: requestBody})
 .then(res => {
   if (options.name) {
     // if searching for jokes, loop over the results
     console.log(res.data)
     
   } else {
     console.log(res.data);
   }
 });