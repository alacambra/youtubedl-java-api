import {HttpClient} from "./HttpClient.js"

const processResponse = Symbol('processResponse');

export class DataClient {

    constructor() {
        this.httpClient = new HttpClient();
    }

    sendJob(payload) {
        console.log("owner=" + JSON.stringify(payload));
        return this.httpClient.postData("../views/download/job/", payload);
    }

    cleanJobs() {
        return this.httpClient.delete("../views/download/job/finished");
    }

    [processResponse](response) {
        response.text().then(b => console.log(b));
    }
}

export class JobClient {

    constructor() {
        this.httpClient = new HttpClient();
    }

    async getCurrentJobs() {

        // let response = await fetch("/dev-data/response-multiple.json");
        // let body = await response.text();
        // const jobs = JSON.parse(body);

        const jobs = await this.httpClient.getData("../views/download/job/");

        jobs.forEach(job => {
            const fileName = this.getFileNameFromText(job);
            job["fileName"] = fileName;
        });

        console.log(jobs);

        return jobs;
    }

    getFileNameFromText(job) {
        const fileName = job.line.filter(l => l.match("Destination"))
            .map(l => {
                const index = l.lastIndexOf("/");
                return l.substr(index + 1);
            });

        return fileName[0];
    }

}