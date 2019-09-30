import {DataClient} from "../remote/DataClient.js"

const processResponse = Symbol('processResponse');

class DownloaderForm extends HTMLElement {

    constructor() {
        super();
        console.log("Init DownloadFrom Controller");
        this.client = new DataClient();
    }

    connectedCallback() {

        console.log("connectedCallback DownloadFrom Controller");
        const template = document.querySelector('#downloaderForm');
        console.log("Template DownloadFrom loaded");

        const owners = this.getAttribute('owners');
        const checked = this.getAttribute('checked');
        const node = document.importNode(template.content, true);
        node.querySelector("downloader-owner").setAttribute("owners", owners);

        node.querySelector("downloader-owner").setAttribute("checked", checked);

        const button = node.querySelector("button");
        button.addEventListener("click", () => this.beginJob().then(""));

        console.log("DownloaderForm before-attach");
        this.appendChild(node);
        console.log("DownloaderForm done")
    }

    beginJob() {
        const payload = {};
        payload.owner = document.querySelector('input[name=owner]:checked').value;
        payload.url = document.querySelector('input[name=url]').value;
        payload.extractAudio = document.querySelector('input[name=extractAudio]').checked || false;

        console.log("sending payload...", payload);
        return this.client.sendJob(payload).then(jobInfo => console.log(jobInfo));
    }
}


class DownloaderOwner extends HTMLElement {
    constructor() {
        super();
        console.log("Init DownloaderOwner Controller");
    }

    connectedCallback() {
        const template = document.querySelector('#owner-option');
        const node = document.importNode(template.content, true);
        const checked = this.getAttribute('checked');
        console.log("DownloaderOwner before-attach");
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
        console.log("DownloaderOwner done")
    }
}

customElements.define("downloader-owner", DownloaderOwner);
customElements.define("downloader-form", DownloaderForm);