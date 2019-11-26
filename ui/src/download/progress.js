'use strict';

function retry(id) {

    let url = "download/job/" + id;    console.info("sending request", url);

    fetch(url).then(result => {
        console.info("received result:", result.body());
        if (result.done) {
            console.info("job done", id);
            window.clearInterval(timer);
        } else {
            setTimeout(retry(id), 2000);
        }
        document.getElementById("progress").innerText(result);
        console.info("received response:", result);
    }, reason => console.error("problems getting job", reason));
}

console.info("setting interval for id".serverData);
setTimeout(retry(serverData), 200);
