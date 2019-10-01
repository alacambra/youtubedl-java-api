import {DataClient} from "../remote/DataClient.js"

const processResponse = Symbol('processResponse');

export class DownloaderForm extends HTMLElement {

    constructor() {
        super();
        console.log("Init DownloadFrom Controller");
        this.client = new DataClient();
        this.connected = false;
    }

    connectedCallback() {

        if (!this.connected) {
            const template = document.querySelector('#downloaderForm');
            const owners = this.getAttribute('owners');
            const checked = this.getAttribute('checked');
            const node = document.importNode(template.content, true);

            node.querySelector("downloader-owner").setAttribute("owners", owners);
            node.querySelector("downloader-owner").setAttribute("checked", checked);

            const button = node.querySelector("button");
            button.addEventListener("click", () => this.beginJob().then(""));

            this.appendChild(node);
            this.connected = true;
        }
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
        this.connected = false;
    }

    connectedCallback() {
        if (!this.connected) {
            const template = document.querySelector('#owner-option');
            const node = document.importNode(template.content, true);
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
            this.connected = true;
        }
    }
}

customElements.define("downloader-owner", DownloaderOwner);
customElements.define("downloader-form", DownloaderForm);
