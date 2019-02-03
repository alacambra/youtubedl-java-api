class Downloader extends HTMLElement {

    connectedCallback() {
        
        const owners = this.getAttribute('owners');

        owners.split(",").map(owner => {
            const element = document.createElement('input');
            element.setAttribute("type", "radio");
            element.setAttribute("name", "owner");
            element.setAttribute("value", owner);
            element.innerHTML = owner;

            const template = document.querySelector('#radio');
            const e = document.importNode(template.content, true);
            const input = e.querySelector('input');
            input.setAttribute("value", owner);
            const span = e.querySelector('span');
            span.innerHTML = owner;

            return e;
        }).forEach(owner => {
            this.appendChild(owner);
        });
    }
}

customElements.define("i-downloader", Downloader);