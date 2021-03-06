export class HttpClient {

    constructor() {
        const credentials = "same-origin";
    }

    getData(url = ``) {
        return fetch(url).then(response => response.json());
    }


    postData(url = ``, data = {}) {
        // Default options are marked with *
        return fetch(url, {
            method: "POST", // *GET, POST, PUT, DELETE, etc.           
            headers: {
                "Content-Type": "application/json"
            },
            redirect: "follow", // manual, *follow, error
            referrer: "no-referrer", // no-referrer, *client
            body: JSON.stringify(data), // body data type must match "Content-Type" header
        })
            .then(response => response.json()); // parses response to JSON
    }

    delete(url = ``, data = {}) {
        // Default options are marked with *
        return fetch(url, {
            method: "DELETE", // *GET, POST, PUT, DELETE, etc.
            headers: {},
            redirect: "follow", // manual, *follow, error
            referrer: "no-referrer", // no-referrer, *client
        })
            .then(response => response.json()); // parses response to JSON
    }

    putData(url = ``, data = {}) {
        // Default options are marked with *
        return fetch(url, {
            method: "PUT", // *GET, POST, PUT, DELETE, etc.
            mode: "no-cors", // no-cors, cors, *same-origin
            cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
            credentials: "same-origin", // include, *same-origin, omit
            headers: {
                "Content-Type": "application/json",
                // "Content-Type": "application/x-www-form-urlencoded",
            },
            redirect: "follow", // manual, *follow, error
            referrer: "no-referrer", // no-referrer, *client
            body: JSON.stringify(data), // body data type must match "Content-Type" header
        })
            .then(response => response.json()); // parses response to JSON
    }

}