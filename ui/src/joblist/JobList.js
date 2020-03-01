import {JobClient} from "../remote/DataClient.js";
import {Templates} from "../TemplateProvider.js";

export class JobList extends HTMLElement {

    constructor() {
        super();
        this.loadElements();
        console.info(this.innerText)
    }

    async loadElements() {

        const template = await Templates.loadTemplate("joblist/job-list.tpl.html");
        this.listTpl = template.querySelector('#downloadListTpl');
        this.button = document.createElement("button");
        this.button.innerText = "click";
        this.jobs = document.createElement("div");
        this.appendChild(this.button);
        this.appendChild(this.jobs);
        this.button.addEventListener("click", ev => this.fetchJobs());

    }

    fetchJobs() {
        JobClient.getCurrentJobs().then(j => this.renderJobs(j));
    }

    renderJobs(jobs) {
        this.jobs.innerHTML = "";
        console.log(jobs);
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