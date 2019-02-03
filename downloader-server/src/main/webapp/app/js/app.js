import DataClient from "./DataClient.js"

class Downloader extends HTMLElement {

    constructor() {
        super();
        this.client = new DataClient();
    }

    connectedCallback() {
        const owners = this.getAttribute('owners');
        const checked = this.getAttribute('checked');
        const submit = document.querySelector('#submitJob');

        submit.addEventListener("click", () => {
            const payload = {};
            payload.owner = document.querySelector('input[name=owner]:checked').value;
            payload.url = document.querySelector('input[name=url]').value;
            payload.extractAudio = document.querySelector('input[name=extractAudio]').checked || false;

            this.client.sendJob(payload);
        });
        owners.split(",").map(owner => {
            const template = document.querySelector('#radio');

            const e = document.importNode(template.content, true);
            const input = e.querySelector('input');
            input.setAttribute("value", owner);

            if (checked == owner) {
                input.setAttribute("checked", true);
            }

            const span = e.querySelector('span');
            span.innerHTML = owner;

            return e;
        }).forEach(owner => {
            this.appendChild(owner);
        });
    }
}

customElements.define("i-downloader", Downloader);