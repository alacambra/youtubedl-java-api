export class TplProvider {

    constructor() {
        console.log("building.... " + new Date());
    }

    load() {
        let c = document.querySelector("#formcomponent")
        this.tpl = c.content.cloneNode(true);
        this.mustacheTpl = document.querySelector("#formcomponentWithMustache").cloneNode(true);
    }

    downloadTemplates() {
        fetch("templates.js")
            .then(response => response.text())
            .then(t => {
                let wrapper = document.createElement('div');
                wrapper.innerHTML = t;
                document.querySelector("body").appendChild(wrapper);
            })
    }

    cloneTpl() {
        return this.tpl.cloneNode(true);
    }

    cloneMustacheTpl() {
        return this.mustacheTpl.cloneNode(true);
    }
}

export let tplProvider = new TplProvider();