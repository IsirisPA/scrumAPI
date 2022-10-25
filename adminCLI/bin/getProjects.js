#!/usr/bin/env node

const yargs = require("yargs");
const axios = require("axios");

const options = yargs
 .usage("Usage: -n <token>")
 .option("n", { alias: "token", describe: "Bearer token", type: "string", demandOption: true })
 .argv;
 
console.log(`${options.token}`);

const config = {
    headers: { Authorization: `Bearer ${options.token}` }
};
// The url depends on searching or not
axios.defaults.headers.common = {'Authorization': `Bearer ${options.token}`}
const url = "http://localhost:8080/api/auth/project/get/user"
axios({method:'get', url: url})
 .then(res => {
		console.log("Tasks assigned to user are: ")
        console.log(res.data)
 });