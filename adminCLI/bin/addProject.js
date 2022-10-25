#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .usage("Usage: -n <name>")
 .option("t", { alias: "title", describe: "Project Title", type: "string", demandOption: true })
 .option("u", { alias: "usernames", describe: "usernames", type: "string" })
 .option("r", { alias: "roles", describe: "roles", type: "string" })
 .option("d", { alias: "description", describe: "description", type: "string" })
 .option("j", { alias: "token", describe: "jwt token", type: "string" })
 .argv;
 
 const config = {
    headers: { Authorization: `Bearer ${options.token}` }
};

 const requestBody = {
	title: `${options.title}`,
	description: `${options.description}`,
	users: {}
	}
 
 const usernamesArr = `${options.usernames}`.split(',');
 const rolesArr = `${options.roles}`.split(',');
for(i=0; i<usernamesArr.length; i++) {
requestBody.users[usernamesArr[i]] = rolesArr[i] ;
}
 

 
 


console.log(requestBody);


// The url depends on searching or not
const url = "http://localhost:8080/api/auth/project/add"
axios.defaults.headers.common = {'Authorization': `Bearer ${options.token}`}
axios({method:'post', url: url, data: requestBody})
 .then(res => {
        console.log(res.data)
 });