'use strict';
let serverData = "";
console.info("reading...", serverData);
fetch("download/hello/").then(value => console.info(value));