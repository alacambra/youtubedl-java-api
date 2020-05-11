import {Templates} from "../TemplateProvider.js";
import {DataClient} from "../remote/DataClient.js";

export class DownloaderForm extends HTMLElement {

    constructor() {
        super();
        console.log("Init DownloadFrom Controller");
        this.connected = false;
        this.currentTimer = undefined;
    }

    async connectedCallback() {

        if (!this.connected) {
            this.connected = true;
            const templates = await Templates.loadTemplate("download/downloader-form.tpl.html");
            const template = templates.querySelector('#downloaderForm');
            const owners = this.getAttribute('owners');
            const checked = this.getAttribute('checked');
            const node = document.importNode(template.content, true);

            node.querySelector("downloader-owner").setAttribute("owners", owners);
            node.querySelector("downloader-owner").setAttribute("checked", checked);

            const button = node.querySelector("button");
            button.addEventListener("click", () => this.beginJob().then(""));

            this.currentJob = node.getElementById("jobStatus");
            this.appendChild(node);
        }
    }

    beginJob() {
        const payload = {};
        payload.owner = document.querySelector('input[name=owner]:checked').value;
        payload.url = document.querySelector('input[name=url]').value;
        payload.extractAudio = document.querySelector('input[name=extractAudio]').checked || false;

        console.log("sending payload...", payload);
        return DataClient.sendJob(payload)
            .then(jobInfo => {
                if (this.currentTimer !== undefined) {
                    clearInterval(this.currentTimer);
                }

                this.currentTimer = setInterval(_ => DataClient.getJob(jobInfo.id)
                    .then(j => {
                        if (j.status === "DONE") {
                            clearInterval(this.currentTimer);
                            this.currentTimer = undefined;
                        }
                        this.setResult(j);
                    }), 1000);
            });
    }

    setResult(job) {
        this.currentJob.innerHTML = job.jobId + " : " + job.status;
    }
}


class DownloaderOwner extends HTMLElement {

    constructor() {
        super();
        this.connected = false;
    }

    async connectedCallback() {

        if (!this.connected) {

            this.connected = true;
            const templates = await Templates.loadTemplate("download/owner.tpl.html");
            const template = templates.querySelector('#owner-option');
            const checked = this.getAttribute('checked');
            const owners = this.getAttribute('owners');

            owners.split(",").map(owner => {

                const node = document.importNode(template.content, true);
                const input = node.querySelector('input');

                input.setAttribute("value", owner);

                if (checked === owner) {
                    input.setAttribute("checked", true);
                }

                const span = node.querySelector('span');
                span.innerHTML = owner;

                return node;

            }).forEach(owner => {
                this.appendChild(owner);
            });
        }
    }
}

customElements.define("downloader-owner", DownloaderOwner);
customElements.define("downloader-form", DownloaderForm);
