import {DataClient} from "../remote/DataClient.js"

export class AppNav extends HTMLElement {

    constructor() {
        super();
        this.client = new DataClient();
    }

    connectedCallback() {
        const links = this.querySelectorAll("a");
        console.log(links);
        links.forEach(link => this.registerLinkListener(link));

        const clear = this.querySelector("#clearFinishedJobs");
        clear.addEventListener("click", e => {
            return this.client.cleanJobs();
        });
    }

    registerLinkListener(el) {
        el.onclick = evt => this.onLinkClicked(evt);
    }

    onLinkClicked(evt) {
        evt.preventDefault();
        const event = new CustomEvent("app-nav", {
            detail: {
                uri: evt.target.uri,
                hash: evt.target.hash.substr(1),
                text: evt.target.text,
                href: evt.target.href,
            },
            bubbles: true
        });

        console.log("event", event.detail);
        this.dispatchEvent(event);
    }
}

customElements.define("app-nav", AppNav);