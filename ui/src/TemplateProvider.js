const cache = {};

export let Templates = {
    loadTemplate: (templateName) => {

        const request = new XMLHttpRequest();
        return new Promise(function (resolve, reject) {

            if (cache.hasOwnProperty(templateName)) {
                resolve(cache[templateName]);
            } else {
                request.open('GET', templateName, true);
                request.addEventListener('load', (event) => {

                    if (event.target.status >= 400) {

                        const r = {"status": event.target.status, "text": event.target.statusText};
                        reject(r);

                    } else {
                        const tpl = event.target.response;
                        const templates = document.createElement('div');
                        templates.innerHTML = tpl;
                        cache[templateName] = templates;
                        resolve(templates);
                    }
                });
                request.send();
            }
        });
    }
};