import {JobClient} from "../remote/DataClient.js";

export class JobList extends HTMLElement {

    constructor() {
        super();
        this.jobClient = new JobClient();
        this.loadElements();
        console.info(this.innerText)
    }

    loadElements() {
        this.listTpl = document.querySelector('#downloadListTpl');
        this.button = document.createElement("button");
        this.button.innerText = "click";
        this.jobs = document.createElement("div");
        this.appendChild(this.button);
        this.appendChild(this.jobs);
        this.button.addEventListener("click", ev => this.fetchJobs());

    }

    fetchJobs() {
        fetch("/dev-data/response-multiple.json").then(response => response.text().then(body => {
            let jobs = JSON.parse(body);
            this.renderJobs(jobs);
        }));

        this.jobClient.fetchJobs();
    }

    renderJobs(jobs) {
        this.jobs.innerHTML = "";
        jobs.forEach(job => this.renderJob(job));
    }

    renderJob(job) {
        job = this.flattenResponse(job);
        let rendered = Mustache.render(this.listTpl.innerHTML, job);
        let div = document.createElement("div");
        div.innerHTML = rendered;
        this.jobs.appendChild(div);
    }

    flattenResponse(json) {
        let info = json["downloadJobInfo"];
        Object.keys(info).forEach(k => {
            json[k] = info[k];
        });
        return json;
    }
}

customElements.define("job-list", JobList);