export class JobList extends HTMLElement {
    constructor() {

        super();

        this.loadElements();
        this.initElements();
        this.innerHTML = this.tpl;
    }

    loadElements() {

        this.tpl = tplProvider.cloneMustacheTpl();
        this.nameLabel = this.tpl.querySelector('label[for="name"]');
        this.button = this.tpl.querySelector('#btn');
        this.displayElement = this.tpl.querySelector('#display');
        this.dataId = this.dataset.id;
    }
    initElements() {

        let values = {
            btnText: "mustacheBtn",
            displayText: "mustacheText",
        }
        console.log(this.tpl.innerText);
        this.tpl = Mustache.render(this.tpl.innerHTML, values);

    }
}