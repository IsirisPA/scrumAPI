#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .usage("Usage: -n <name>")
 .option("n", { alias: "name", describe: "Your name", type: "string", demandOption: true })
 .option("p", { alias: "password", describe: "password", type: "string" })
 .option("e", { alias: "email", describe: "email", type: "string" })
 .option("r", { alias: "roles", describe: "roles", type: "string" })
 .argv;
 

 
 const rolesArr = `${options.roles}`.split(',');
 const requestBody = {
	username: `${options.name}`,
	password: `${options.password}`,
	email: `${options.email}`,
	roles: rolesArr
 }
console.log(requestBody);


// The url depends on searching or not
const url = "http://localhost:8080/api/auth/signup"
axios({method:'post', url: url, data: requestBody})
 .then(res => {
   if (options.name) {
     // if searching for jokes, loop over the results
     console.log(res.data)
   } else {
     console.log(res.data.joke);
   }
 });