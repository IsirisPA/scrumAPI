#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .option("u", { alias: "userId", describe: "User Id", type: "string", demandOption: true })
 .option("p", { alias: "projectId", describe: "Project Id", type: "string" })
 .option("j", { alias: "token", describe: "jwt token", type: "string" })
 .argv;
 
 const config = {
    headers: { Authorization: `Bearer ${options.token}` }
};
 

 
 const requestBody = {
	userId: `${options.userId}`,
	projectId: `${options.projectId}`
 }


// The url depends on searching or not
const url = "http://localhost:8080/api/auth/project/remove/user"
axios.defaults.headers.common = {'Authorization': `Bearer ${options.token}`}
axios({method:'post', url: url, data: requestBody})
 .then(res => {
        console.log(res.data)
 });