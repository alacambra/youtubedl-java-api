export class AppSlot extends HTMLElement {

    constructor() {
        super();
        this.root = this;
    }

    connectedCallback() {

        document.addEventListener("app-nav", e => this.onNavigation(e));

        this.form = this.querySelector("downloader-form");
        this.jobList = this.querySelector("job-list");

        this.current = this.form;
        this.removeChild(this.jobList);
    }

    onNavigation(evt) {

        const {detail} = evt;
        const {hash} = detail;
        const {text} = detail;
        const {href} = detail;

        console.log("onNavigation", hash, text, href);

        this.removeChild(this.current);

        let view;

        if (hash === "newjob") {
            view = this.form;
            this.root.appendChild(view);
        } else if (hash === "joblist") {
            view = this.jobList;
            this.root.appendChild(view);
        } else {
            view = this.form;
            this.root.appendChild(view);
        }

        this.current = view;
    }
}


customElements.define("app-slot", AppSlot);