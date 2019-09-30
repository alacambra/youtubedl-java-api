import HttpClient from "./HttpClient.js"

const processResponse = Symbol('processResponse');

export class DataClient {

    constructor() {
        this.httpClient = new HttpClient();
        console.info("hello client!");
    }

    sendJob(payload) {
        console.log("owner=" + JSON.stringify(payload));
        return this.httpClient.postData("../views/download/job/", payload);
    }

    [processResponse](response) {
        response.text().then(b => console.log(b));
    }
}

export class JobClient {

    constructor() {

    }

    getCurrentJobs() {
        fetch("/dev-data/response-multiple.json").then(response => response.text().then(body => {
                let jobs = JSON.parse(body);
                this.renderJobs(jobs);
            })
        );
    }

}