#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .option("u", { alias: "taskId", describe: "Task Id", type: "string", demandOption: true })
 .option("p", { alias: "status", describe: "New Status", type: "string" })
 .option("j", { alias: "token", describe: "jwt token", type: "string" })
 .argv;
 
 const config = {
    headers: { Authorization: `Bearer ${options.token}` }
};
 

 
 const requestBody = {
	taskId: `${options.taskId}`,
	status: `${options.status}`
 }


// The url depends on searching or not
const url = "http://localhost:8080/api/task/status"
axios.defaults.headers.common = {'Authorization': `Bearer ${options.token}`}
axios({method:'post', url: url, data: requestBody})
 .then(res => {
        console.log(res.data)
 });