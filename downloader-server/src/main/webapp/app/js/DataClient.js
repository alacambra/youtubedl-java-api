import HttpClient from "./HttpClient.js"

export default class DataClient {

    constructor() {
        this.httpClient = new HttpClient();
        console.info("hello client!");
    }

    sendJob(payload) {
        console.log("owner=" + JSON.stringify(payload));
        return this.httpClient.postData("/downloader/views/download/job/", payload);
    }
}